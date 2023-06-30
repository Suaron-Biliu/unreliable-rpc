package org.suaron.provider;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.suaron.core.UnreliableInvocation;
import org.suaron.core.UnreliableProtocol;

import java.lang.reflect.Method;

import static org.suaron.cahce.ProviderCache.PROVIDER_CLASS_MAP;

/**
 * 服务处理器
 * 1.用于解析协议
 * 2.协议转换出 上下文
 * 3.上下文获取 目标类 -》目标方法 -》 参数
 * 4.动态代理调用
 */
public class ProviderHandle extends  ChannelHandlerAdapter  {

    /**
     * 读取数据
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UnreliableProtocol protocol = (UnreliableProtocol) msg;
        String invocationJson = new String(protocol.getContent(), 0, protocol.getContentLength());
        // 解析出代理
        UnreliableInvocation unreliableInvocation = JSON.parseObject(invocationJson, UnreliableInvocation.class);
        // 对外提供的服务放在这边 作为缓冲 ，然后也会放在注册中心 或者nosql 相关内容
        Object  exposeObject = PROVIDER_CLASS_MAP.get(unreliableInvocation.getServiceName());

        Method [] methods = exposeObject.getClass().getDeclaredMethods();
        Object result = null;
        for (Method method : methods) {
            if (method.getName().equals(unreliableInvocation.getMethod())) {
                // 通过反射找到目标对象，然后执行目标方法并返回对应值
                if (method.getReturnType().equals(Void.TYPE)) {
                    method.invoke(exposeObject, unreliableInvocation.getArgs());
                } else {
                    result = method.invoke(exposeObject, unreliableInvocation.getArgs());
                }
                break;
            }
        }
        // 调用成功 获取结果
        unreliableInvocation.setResponse(result);
        // 结果重新包装成协议内容 返回去
        UnreliableProtocol respRpcProtocol = new UnreliableProtocol(JSON.toJSONString(unreliableInvocation).getBytes());
        ctx.writeAndFlush(respRpcProtocol);
    }

    /**
     * 异常事件处理
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}
