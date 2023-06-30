package org.suaron.comon;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.suaron.core.UnreliableProtocol;

/**
 * 基于Netty 编码器
 */
public class NettyRpcEncoder extends MessageToByteEncoder<UnreliableProtocol> {
    /**
     * 编码器
     * @param channelHandlerContext
     * @param unreliableProtocol
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          UnreliableProtocol unreliableProtocol,
                          ByteBuf byteBuf) throws Exception {
        byteBuf.writeShort(unreliableProtocol.getCheckCode());
        byteBuf.writeInt(unreliableProtocol.getContentLength());
        byteBuf.writeBytes(unreliableProtocol.getContent());
    }
}
