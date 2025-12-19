package com.hzcu.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WechatPlatformConfig {
    private String appId;
    private String appSecret;
    private MchConfig mch;

    @Data
    public static class MchConfig {
        private String mchId;
        private String apiV3Key;
        private String notifyUrl;
        private String certPath;
        private String privateKeyPath;
    }
}
