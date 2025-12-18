package com.hzcu.order.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeChatService {

    @Value("${wechat.appid:}")
    private String appId;

    @Value("${wechat.secret:}")
    private String appSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 通过code获取access_token和openid
     */
    public Map<String, Object> getAccessToken(String code) {
        String url = String.format(
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
            appId, appSecret, code
        );

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);

            Map<String, Object> result = new HashMap<>();

            if (jsonNode.has("errcode") && jsonNode.get("errcode").asInt() != 0) {
                result.put("success", false);
                result.put("error", jsonNode.get("errmsg").asText());
                return result;
            }

            result.put("success", true);
            result.put("openid", jsonNode.get("openid").asText());
            if (jsonNode.has("session_key")) {
                result.put("session_key", jsonNode.get("session_key").asText());
            }
            if (jsonNode.has("unionid")) {
                result.put("unionid", jsonNode.get("unionid").asText());
            }

            return result;
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", "微信API调用失败: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 解密微信手机号
     */
    public String getPhoneNumber(String encryptedData, String iv, String sessionKey) {
        try {
            // Base64解码
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);

            // AES解密
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(sessionKeyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // 解析解密后的JSON
            String decryptedData = new String(decryptedBytes, StandardCharsets.UTF_8);
            JsonNode jsonNode = objectMapper.readTree(decryptedData);

            // 提取手机号
            if (jsonNode.has("phoneNumber")) {
                return jsonNode.get("phoneNumber").asText();
            }

            return null;
        } catch (Exception e) {
            System.err.println("解密手机号失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 通过手机号code获取手机号
     */
    public Map<String, Object> getPhoneNumberByCode(String code) {
        // 微信小程序手机号获取API URL
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + getAccessToken();

        try {
            // 构建请求体
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("code", code);

            String response = restTemplate.postForObject(url, requestBody, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);

            Map<String, Object> result = new HashMap<>();

            if (jsonNode.has("errcode") && jsonNode.get("errcode").asInt() != 0) {
                result.put("success", false);
                result.put("error", "微信API错误(" + jsonNode.get("errcode").asText() + "): " + jsonNode.get("errmsg").asText());
                return result;
            }

            if (jsonNode.has("phone_info")) {
                JsonNode phoneInfo = jsonNode.get("phone_info");
                if (phoneInfo.has("phoneNumber")) {
                    result.put("success", true);
                    result.put("phoneNumber", phoneInfo.get("phoneNumber").asText());
                    result.put("purePhoneNumber", phoneInfo.get("purePhoneNumber").asText());
                    if (phoneInfo.has("countryCode")) {
                        result.put("countryCode", phoneInfo.get("countryCode").asText());
                    }
                } else {
                    result.put("success", false);
                    result.put("error", "响应中缺少手机号字段");
                }
            } else {
                result.put("success", false);
                result.put("error", "无法获取手机号信息，响应格式异常");
            }

            return result;
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", "获取手机号失败: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 获取接口调用凭据
     */
    private String getAccessToken() {
        String url = String.format(
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
            appId, appSecret
        );

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode jsonNode = objectMapper.readTree(response);

            if (jsonNode.has("access_token")) {
                return jsonNode.get("access_token").asText();
            } else {
                throw new RuntimeException("获取access_token失败: " + jsonNode.get("errmsg").asText());
            }
        } catch (Exception e) {
            throw new RuntimeException("获取access_token异常: " + e.getMessage());
        }
    }

    /**
     * 验证数据完整性（可选，用于安全验证）
     */
    public boolean validateSignature(String rawData, String signature, String sessionKey) {
        // 这里可以实现数据签名的验证逻辑
        // 由于在小程序端已经通过wx.getUserProfile获取了用户信息，这里可以简化处理
        return true;
    }

    /**
     * 检查配置是否完整
     */
    public boolean isConfigured() {
        return appId != null && !appId.isEmpty() &&
               appSecret != null && !appSecret.isEmpty();
    }
}