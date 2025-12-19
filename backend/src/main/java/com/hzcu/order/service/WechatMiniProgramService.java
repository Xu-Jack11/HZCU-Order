package com.hzcu.order.service;

import com.hzcu.order.config.WechatPlatformConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WechatMiniProgramService {

    @Autowired
    private WechatPlatformConfig wechatConfig;

    @Autowired
    private RestTemplate restTemplate;

    public Map<String, String> getOpenid(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={appSecret}&js_code={code}&grant_type=authorization_code";

        Map<String, String> params = new HashMap<>();
        params.put("appId", wechatConfig.getAppId());
        params.put("appSecret", wechatConfig.getAppSecret());
        params.put("code", code);

        WechatSessionResponse response = restTemplate.getForObject(url, WechatSessionResponse.class, params);

        if (response != null && response.getOpenid() != null) {
            Map<String, String> result = new HashMap<>();
            result.put("openid", response.getOpenid());
            result.put("session_key", response.getSession_key());
            return result;
        }
        return null;
    }

    @Data
    public static class WechatSessionResponse {
        private String openid;
        private String session_key;
        private String unionid;
        private Integer errcode;
        private String errmsg;
    }
}
