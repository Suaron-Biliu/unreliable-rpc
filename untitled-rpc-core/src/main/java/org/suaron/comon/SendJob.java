package org.suaron.comon;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelFuture;
import org.suaron.core.UnreliableInvocation;
import org.suaron.core.UnreliableProtocol;

import static org.suaron.cahce.CustomerCache.SEND_QUEUE;
public class SendJob implements Runnable {


    private ChannelFuture channelFuture;

    public SendJob(ChannelFuture channelFuture) {

        this.channelFuture = channelFuture;
    }

    @Override
    public void run() {
        while (true){
            try {
                UnreliableInvocation invocation = SEND_QUEUE.take();
                String json = JSON.toJSONString(invocation);
                UnreliableProtocol protocol = new UnreliableProtocol(json.getBytes());
                channelFuture.channel().writeAndFlush(protocol);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

