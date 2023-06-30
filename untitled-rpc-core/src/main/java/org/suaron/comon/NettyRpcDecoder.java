package org.suaron.comon;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.suaron.core.UnreliableProtocol;

import static org.suaron.constance.UnreliableConstants.CHECK_CODE;
import java.util.List;

/**
 * 基于netty 自定义协议解码
 */
public class NettyRpcDecoder extends ByteToMessageDecoder {

    public final int BASE_LENGTH = 6;

    /**
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            //限制大于 > 1000 的数据包 ，可配置太麻烦写的有需要的自己改吧
            if (1000 < byteBuf.readableBytes()) {
                byteBuf.skipBytes(byteBuf.readableBytes());
            }
            int beginReader;
            while (true) {
                beginReader = byteBuf.readerIndex();
                byteBuf.markReaderIndex();
                if (byteBuf.readShort() == CHECK_CODE) {
                    break;
                } else {
                    // 客户端校验失败 判定为非法客户端 关闭通道
                    channelHandlerContext.close();
                    return;
                }
            }
            int length = byteBuf.readInt();
            //说明剩余的数据包不是完整的，这里需要重置下读索引
            if (byteBuf.readableBytes() < length) {
                byteBuf.readerIndex(beginReader);
                return;
            }
            byte[] data = new byte[length];
            byteBuf.readBytes(data);
            UnreliableProtocol rpcProtocol = new UnreliableProtocol(data);

            list.add(rpcProtocol);
        }
    }

}
