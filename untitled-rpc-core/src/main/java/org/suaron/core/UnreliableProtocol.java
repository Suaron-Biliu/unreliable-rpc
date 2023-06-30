package org.suaron.core;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import static org.suaron.constance.UnreliableConstants.CHECK_CODE;

/**
 * 为了解决在socket 通信过程中传输数据 出现粘包情况
 * 自定义一个传输协议
 */
@Data
public class UnreliableProtocol implements Serializable {
    /**
     * 这是在 jdk 14 发布对一个新的批注
     *
      */
    @Serial
    private static final long serialVersionUID = 3197827165078026758L;
    /**
     * 客户端校验码
     */
    private short checkCode = CHECK_CODE;

    /**
     * 设置好固定的包长度解决粘包问题
     */
    private int contentLength;
    /**
     * 传输数据
     * 这个对于在 unreliable rpc 中就是代表 我们 Invocation 对象字节化后的形态
     */
    private byte[] content;

    public UnreliableProtocol(byte[] content) {
        this.content = content;
    }
}