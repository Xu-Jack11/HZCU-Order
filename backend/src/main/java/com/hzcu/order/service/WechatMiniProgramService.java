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

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public Map<String, String> getOpenid(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={appSecret}&js_code={code}&grant_type=authorization_code";

        Map<String, String> params = new HashMap<>();
        params.put("appId", wechatConfig.getAppid());
        params.put("appSecret", wechatConfig.getSecret());
        params.put("code", code);

        try {
            String responseStr = restTemplate.getForObject(url, String.class, params);
            System.out.println("Wechat getOpenid response: " + responseStr);
            if (responseStr != null) {
                WechatSessionResponse response = objectMapper.readValue(responseStr, WechatSessionResponse.class);
                if (response != null && response.getOpenid() != null) {
                    Map<String, String> result = new HashMap<>();
                    result.put("openid", response.getOpenid());
                    result.put("session_key", response.getSession_key());
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appId}&secret={appSecret}";
        Map<String, String> params = new HashMap<>();
        params.put("appId", wechatConfig.getAppid());
        params.put("appSecret", wechatConfig.getSecret());

        try {
            String responseStr = restTemplate.getForObject(url, String.class, params);
            logDebug("Wechat getAccessToken response: " + responseStr);
            if (responseStr != null) {
                WechatTokenResponse response = objectMapper.readValue(responseStr, WechatTokenResponse.class);
                if (response != null && response.getAccess_token() != null) {
                    return response.getAccess_token();
                } else {
                    logDebug("Wechat getAccessToken error: "
                            + (response != null ? response.getErrcode() + " " + response.getErrmsg() : "null"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logDebug("Exception in getAccessToken: " + e.getMessage());
        }
        return null;
    }

    public String getPhoneNumber(String code) {
        String accessToken = getAccessToken();
        if (accessToken == null)
            return null;

        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken;

        Map<String, String> body = new HashMap<>();
        body.put("code", code);

        try {
            logDebug("Requesting phone number with code: " + code + ", accessToken: " + accessToken);

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            org.springframework.http.HttpEntity<Map<String, String>> requestEntity = new org.springframework.http.HttpEntity<>(
                    body, headers);

            String responseStr = restTemplate.postForObject(url, requestEntity, String.class);
            logDebug("Wechat getPhoneNumber response: " + responseStr);
            if (responseStr != null) {
                WechatPhoneResponse response = objectMapper.readValue(responseStr, WechatPhoneResponse.class);
                if (response != null && (response.getErrcode() == null || response.getErrcode() == 0)
                        && response.getPhone_info() != null) {
                    return response.getPhone_info().getPhoneNumber();
                } else {
                    logDebug("Wechat getPhoneNumber error: "
                            + (response != null ? response.getErrcode() + " " + response.getErrmsg() : "null"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logDebug("Exception in getPhoneNumber: " + e.getMessage());
        }
        return null;
    }

    private void logDebug(String msg) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("debug.log", true))) {
            writer.write(new java.util.Date() + ": " + msg + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class WechatSessionResponse {
        @com.fasterxml.jackson.annotation.JsonProperty("openid")
        private String openid;
        @com.fasterxml.jackson.annotation.JsonProperty("session_key")
        private String session_key;
        @com.fasterxml.jackson.annotation.JsonProperty("unionid")
        private String unionid;
        @com.fasterxml.jackson.annotation.JsonProperty("errcode")
        private Integer errcode;
        @com.fasterxml.jackson.annotation.JsonProperty("errmsg")
        private String errmsg;
    }

    @Data
    public static class WechatTokenResponse {
        @com.fasterxml.jackson.annotation.JsonProperty("access_token")
        private String access_token;
        @com.fasterxml.jackson.annotation.JsonProperty("expires_in")
        private Integer expires_in;
        @com.fasterxml.jackson.annotation.JsonProperty("errcode")
        private Integer errcode;
        @com.fasterxml.jackson.annotation.JsonProperty("errmsg")
        private String errmsg;
    }

    @Data
    public static class WechatPhoneResponse {
        @com.fasterxml.jackson.annotation.JsonProperty("errcode")
        private Integer errcode;
        @com.fasterxml.jackson.annotation.JsonProperty("errmsg")
        private String errmsg;
        @com.fasterxml.jackson.annotation.JsonProperty("phone_info")
        private PhoneInfo phone_info;

        @Data
        public static class PhoneInfo {
            @com.fasterxml.jackson.annotation.JsonProperty("phoneNumber")
            private String phoneNumber;
            @com.fasterxml.jackson.annotation.JsonProperty("purePhoneNumber")
            private String purePhoneNumber;
            @com.fasterxml.jackson.annotation.JsonProperty("countryCode")
            private Integer countryCode;
            @com.fasterxml.jackson.annotation.JsonProperty("watermark")
            private Watermark watermark;
        }

        @Data
        public static class Watermark {
            @com.fasterxml.jackson.annotation.JsonProperty("timestamp")
            private Integer timestamp;
            @com.fasterxml.jackson.annotation.JsonProperty("appid")
            private String appid;
        }
    }
}
