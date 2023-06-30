package org.suaron.customer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suaron.comon.NettyRpcDecoder;
import org.suaron.comon.NettyRpcEncoder;
import org.suaron.comon.SendJob;
import org.suaron.comon.config.ClientConfig;


public class Customer {
    private Logger log = LoggerFactory.getLogger(Customer.class);
    public static EventLoopGroup clientGroup = new NioEventLoopGroup();

    private ClientConfig clientConfig;

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    /**
     * 启动 客户端应用
     */

    public Object initUnreliableClient() throws InterruptedException {
        EventLoopGroup clientGroup =   new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            // 初始化管道
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new NettyRpcEncoder());
                ch.pipeline().addLast(new NettyRpcDecoder());
                ch.pipeline().addLast(new CustomerHandler());
            }
        });
        ChannelFuture future = bootstrap.connect(clientConfig.getServerAddress(), clientConfig.getPort()).sync();
        log.info("启动 Unreliable 消费端");

        Thread asyncSendJob = new Thread(new SendJob(future));
        asyncSendJob.start();


        return null;
    }

}
