# HZCU-Order 后端（Spring Boot）

该目录下为基于 Spring Boot 的简易后端，提供微信小程序演示所需的核心接口，响应格式遵循 `docs/API` 里的 `{ code, message, data }` 约定，并内置与前端 mock 相同的示例数据（内存存储，重启即重置）。

## 快速开始
- 依赖：JDK 17、Maven
- 启动：
  ```bash
  cd backend
  mvn spring-boot:run
  ```
- 默认服务：`http://localhost:8080/api/v1`

## 已实现的主要接口
- 登录：`POST /auth/wechat/login`（返回 token 与示例用户），`POST /auth/logout`
- 首页：`GET /home/feed`（轮播/公告/推荐食堂）
- 食堂：`GET /canteens`（分页筛选），`GET /canteens/{id}`，`GET /canteens/{id}/dishes`，`GET /shops/{id}/comments`
- 订单：`GET /orders`，`POST /orders`，`POST /orders/{orderId}/pay`，`POST /orders/{orderId}/cancel`
- 商家确认取餐：`POST /merchant/orders/{orderId}/complete`

## 数据与逻辑说明
- 数据来自内存种子，覆盖商家、菜品分类、评论以及若干示例订单。
- 订单状态流转：创建为 `pending/待付款`，支付后变为 `preparing/制作中`，商家确认接口会标记 `completed/已完成`，取消则为 `canceled/已取消`。

## 前端接入
- 小程序的请求基地址已改为 `http://localhost:8080/api/v1`（`wxapp/miniprogram/utils/request.ts`），并关闭了本地 `USE_MOCK` 开关（`utils/api.ts`）。如需部署到其他域名，请同步修改基地址。
