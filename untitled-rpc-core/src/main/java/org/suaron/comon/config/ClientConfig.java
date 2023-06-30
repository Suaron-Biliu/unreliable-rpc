package org.suaron.comon.config;

import lombok.Data;

@Data
public class ClientConfig {
    /**
     * 服务地址
     */
    private String serverAddress;
    /**
     * 端口
     */
    private Integer port;
}
