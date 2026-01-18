# 白盒测试用例设计说明书 (用户认证功能)

## 1. 概述
本文档针对 HZCU-Order 系统的用户认证核心逻辑 (`AuthService`) 进行白盒测试设计。重点通过控制流分析，覆盖微信登录、商户登录及其内部的安全校验逻辑。

## 2. 测试对象
- **核心组件**: `AuthService.java`
- **关键方法**: 
    - `loginWithWechatCode(code, nickname, avatarUrl, phoneCode)`
    - `loginMerchant(username, password)` (内部调用 `authenticateAndGenerateToken`)

---

## 3. 逻辑路径分析

### 3.1 微信登录逻辑 (`loginWithWechatCode`)
1. **微信接口交互**:
   - 分支 A: `wechatService.getOpenid` 返回 null 或无效 data -> 拋出 `BadCredentialsException`。
   - 分支 B: 接口返回有效 `openid` -> 继续流程。
2. **手机号自动绑定**:
   - 分支 C: `phoneCode` 为空 -> 跳过绑定。
   - 分支 D: `phoneCode` 有效 -> 调用微信接口获取手机号并更新用户。
   - 分支 E: 获取手机号失败 -> 流程继续，但不更新手机号（非阻塞）。

### 3.2 角色授权逻辑 (`authenticateAndGenerateToken`)
1. **身份验证**:
   - 路径 F: `authenticationManager.authenticate` 失败（账号密码错）-> 向上抛出异常。
2. **角色匹配校验 (Strict Role Check)**:
   - 分支 G: 用户权限集合中不包含预期角色（如商户尝试登录管理端）-> 抛出 `BadCredentialsException`。
   - 分支 H: 角色匹配成功 -> 更新最后登录时间，生成 JWT。

---

## 4. 白盒测试用例表

| 编号 | 测试场景/逻辑点 | 输入数据 (Input) | 覆盖级别 | 预期结果 (Expected) |
| :--- | :--- | :--- | :--- | :--- |
| **AUTH-TC-01** | **微信登录失败** | code="error_code" | 异常路径覆盖 | 抛出 BadCredentialsException |
| **AUTH-TC-02** | **微信正常登录 (无手机号)** | code="valid", phoneCode=null | 语句覆盖 | 成功生成 Token，不触发手机绑定 |
| **AUTH-TC-03** | **微信登录并绑定手机** | phoneCode="p123", 获取成功 | 分支覆盖 (Path D) | 用户记录中的 mobile 字段被更新并保存 |
| **AUTH-TC-04** | **商户登录-角色不符** | username="admin", role="MERCHANT" | 判定覆盖 (Path G) | 认证通过但角色检查不匹配，抛出权限异常 |
| **AUTH-TC-05** | **商户登录-成功路径** | 正确账号密码 + ROLE_MERCHANT | 完整路径覆盖 | 生成 Token，且调用 `updateLastLogin` |

---

## 5. 验证执行建议
使用 `Mockito` 深度模拟 `WechatMiniProgramService` 的返回结果，确保能精确触发“绑定手机成功”与“绑定手机失败”这两个细分分支。
