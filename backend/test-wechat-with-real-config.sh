#!/bin/bash

# 微信登录测试脚本（使用真实配置）
# 使用方法: ./test-wechat-with-real-config.sh <appid> <secret>

if [ $# -ne 2 ]; then
    echo "使用方法: $0 <微信小程序AppID> <微信小程序AppSecret>"
    echo ""
    echo "示例:"
    echo "$0 wx1234567890abcdef 1234567890abcdef1234567890abcdef"
    echo ""
    echo "获取AppID和AppSecret的步骤:"
    echo "1. 登录 https://mp.weixin.qq.com/"
    echo "2. 选择您的小程序"
    echo "3. 进入'开发' -> '开发管理' -> '开发设置'"
    echo "4. 复制'AppID(小程序ID)'和'AppSecret(小程序密钥)'"
    exit 1
fi

APPID=$1
SECRET=$2

echo "正在测试微信登录..."
echo "AppID: $APPID"
echo "AppSecret: [隐藏]"
echo ""

# 发送测试请求
curl -X POST http://localhost:8080/api/v1/users/wechat-login \
  -H "Content-Type: application/json" \
  -d "{
    \"code\": \"test_real_config_$(date +%s)\",
    \"userInfo\": {
      \"nickName\": \"真实API测试用户\",
      \"avatarUrl\": \"https://example.com/real-test-avatar.png\",
      \"gender\": 1,
      \"city\": \"杭州\",
      \"province\": \"浙江\",
      \"country\": \"中国\"
    }
  }"

echo ""
echo "测试完成！"
echo ""
echo "如果返回成功，说明配置正确。"
echo "如果返回错误，请检查："
echo "1. AppID和AppSecret是否正确"
echo "2. 小程序是否已发布或在开发工具中运行"
echo "3. 网络连接是否正常"