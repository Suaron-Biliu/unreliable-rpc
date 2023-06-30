package org.suaron.core;

import lombok.Data;

/**
 * 传输对象
 * 你可以理解一个调用 ，一个这样的对象
 * 然后这个对象在远程调用的时候会被字节化 传输过去 然后 又被序列化回对象
 */
@Data
public class UnreliableInvocation {
    /**
     * 代表请求 ID
     */
    private String uuid;
    /**
     * 调用目标方法
     */
    private String method;
    /**
     * 调用目标服务名称
     */
    private String serviceName;
    /**
     * 请求参数
     */
    private Object[] args;
    /**
     * 响应数据
     */
    private Object response;

}
