# 智能食堂点餐平台 API 设计

## 1. 通用约定
- **基础 URL**：`https://api.example.com/v1`
- **认证方式**：HTTP Header `Authorization: Bearer <TOKEN>`。C 端用户、商家端、管理端分别使用不同的 OAuth2 客户端凭证获取 token。
- **请求格式**：`application/json`，除文件上传外。
- **响应格式**：
  ```json
  {
    "code": "0",
    "message": "OK",
    "data": {}
  }
  ```
- **分页参数**：`page`（从 1 开始），`pageSize`（默认 20，最大 100）。
- **通用错误码**：
  | code | message          | 说明                     |
  |------|------------------|--------------------------|
  | 0    | OK               | 成功                     |
  | 4000 | INVALID_PARAM    | 参数错误                 |
  | 4001 | UNAUTHORIZED     | 未认证或 token 失效      |
  | 4003 | FORBIDDEN        | 无访问权限               |
  | 4004 | NOT_FOUND        | 资源不存在               |
  | 4090 | CONFLICT         | 状态冲突（库存不足等）   |
  | 5000 | SERVER_ERROR     | 服务端异常               |

---

## 2. C 端（微信小程序）API

### 2.1 用户认证与账户
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| POST | `/auth/wechat/login` | 小程序登录（code -> token） | 否 | body: `{ "code": "" }`；返回用户信息与 token |
| POST | `/auth/logout` | 退出登录 | 是 | 清除 token |
| GET | `/users/profile` | 获取个人资料 | 是 | 返回昵称、手机号、校园卡绑定状态等 |
| PUT | `/users/profile` | 更新资料 | 是 | 可修改头像、昵称、联系方式 |
| POST | `/users/campus-card/bind` | 绑定校园卡 | 是 | body: 卡号、手机号验证 |
| GET | `/users/assets` | 我的资产总览 | 是 | 余额、优惠券数量、积分等 |

### 2.2 首页 & 发现
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/home/feed` | 首页聚合数据 | 否 | 返回轮播图、公告、推荐食堂、推荐菜品 |
| GET | `/canteens` | 食堂列表 | 否 | 支持状态过滤、排序（距离、热度） |
| GET | `/canteens/{canteenId}` | 食堂详情 | 否 | 地址、营业时间、公告、活动等 |
| GET | `/canteens/{canteenId}/statuses` | 食堂实时状态 | 否 | 返回营业状态、预计排队时间 |
| GET | `/announcements` | 公告列表 | 否 | type 可选：平台公告/食堂促销/校园通知 |

### 2.3 搜索 & 筛选
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/search` | 全局搜索 | 否 | query 参数：`keyword`、`type`(dish/canteen) |
| GET | `/dishes/filters` | 获取可用筛选项 | 否 | 返回分类、标签、价格区间 |
| GET | `/canteens/{canteenId}/dishes` | 食堂菜品列表 | 否 | 支持分类、标签、价格区间、多规格 |
| GET | `/dishes/{dishId}` | 菜品详情 | 否 | 规格、月销量、主食材、标签、评价概览 |

### 2.4 购物车与下单
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/cart` | 查询购物车 | 是 | 返回菜品项、规格、数量、结算金额 |
| PUT | `/cart` | 更新购物车 | 是 | body: 菜品项数组（增/减/删除） |
| DELETE | `/cart` | 清空购物车 | 是 | — |
| POST | `/orders/preview` | 订单预览 | 是 | 计算优惠、打包费、出餐时间 |
| POST | `/orders` | 创建订单 | 是 | 支持立即取餐或预约取餐；需支付方式 |
| POST | `/orders/{orderId}/pay` | 支付订单 | 是 | 支持微信支付、校园卡余额支付 |
| POST | `/orders/{orderId}/cancel` | 取消订单 | 是 | 待支付/待接单状态下可用 |
| GET | `/orders/estimate-time` | 取餐时间估算 | 是 | query: `canteenId`,`dishIds`,`mode` |

### 2.5 订单中心
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/orders` | 订单列表 | 是 | query: `status`, `page`, `pageSize` |
| GET | `/orders/{orderId}` | 订单详情 | 是 | 包括进度日志、取餐号、窗口号 |
| GET | `/orders/{orderId}/track` | 订单状态追踪 | 是 | 用于实时轮询或 WebSocket 订阅 |
| POST | `/orders/{orderId}/after-sale` | 申请售后 | 是 | type: refund/complaint |
| POST | `/orders/{orderId}/reorder` | 再来一单 | 是 | 复制原订单到购物车 |
| POST | `/orders/{orderId}/review` | 提交评价 | 是 | 评分、标签、文字、图片 |
| GET | `/orders/{orderId}/contact` | 获取联系信息 | 是 | 返回商家电话、客服渠道 |

### 2.6 收藏、足迹与通知
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| POST | `/favorites` | 收藏菜品/食堂 | 是 | body: `targetType`, `targetId` |
| DELETE | `/favorites/{favoriteId}` | 取消收藏 | 是 | — |
| GET | `/favorites` | 收藏列表 | 是 | 支持分类筛选 |
| GET | `/footprints` | 浏览历史 | 是 | 按时间倒序 |
| GET | `/notifications` | 通知中心 | 是 | 系统消息、订单提醒 |
| PUT | `/notifications/{notificationId}/read` | 标记已读 | 是 | 支持批量 |

---

## 3. 商家端 API

### 3.1 认证与账户
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| POST | `/merchant/auth/login` | 商家账号密码登录 | 否 | 返回 token、店铺信息 |
| POST | `/merchant/auth/logout` | 退出登录 | 是 | — |
| GET | `/merchant/profile` | 商家资料 | 是 | 基本信息、营业时间、联系人 |
| PUT | `/merchant/profile` | 更新资料 | 是 | 包括营业状态、自动打烊设置 |

### 3.2 营业台（订单处理）
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/merchant/dashboard/orders` | 实时订单列表 | 是 | 支持状态筛选、按创建时间排序 |
| GET | `/merchant/orders/{orderId}` | 订单详情 | 是 | 包含备注、取餐号 |
| POST | `/merchant/orders/{orderId}/accept` | 接单 | 是 | 可触发自动打印 |
| POST | `/merchant/orders/{orderId}/reject` | 拒单 | 是 | body: `reason` |
| POST | `/merchant/orders/{orderId}/start` | 开始制作 | 是 | 更新状态为制作中 |
| POST | `/merchant/orders/{orderId}/ready` | 完成制作/待取餐 | 是 | 生成或更新取餐号、窗口号 |
| POST | `/merchant/orders/{orderId}/complete` | 确认取餐 | 是 | 标记订单完成 |
| POST | `/merchant/orders/{orderId}/print` | 一键打印 | 是 | 失败返回错误信息 |
| GET | `/merchant/orders/stream` | 订单实时推送 | 是 | WebSocket 或 SSE |

### 3.3 菜品与菜单管理
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/merchant/dishes` | 菜品列表 | 是 | 支持状态、分类、关键词筛选 |
| POST | `/merchant/dishes` | 新建菜品 | 是 | 包括规格、图片（多 part） |
| GET | `/merchant/dishes/{dishId}` | 菜品详情 | 是 | — |
| PUT | `/merchant/dishes/{dishId}` | 更新菜品 | 是 | 支持局部更新 |
| DELETE | `/merchant/dishes/{dishId}` | 删除菜品 | 是 | 逻辑删除 |
| POST | `/merchant/dishes/{dishId}/status` | 上/下架、售罄设置 | 是 | body: `status`, `stock` |
| POST | `/merchant/dishes/batch/status` | 批量上架/下架 | 是 | 传递菜品 ID 列表 |
| POST | `/merchant/dishes/batch/price` | 批量调价 | 是 | 支持增量或绝对值 |
| GET | `/merchant/menus` | 菜单配置列表 | 是 | 早餐/午餐/晚餐等 |
| PUT | `/merchant/menus/{menuId}` | 更新菜单 | 是 | 绑定菜品、时间段 |
| POST | `/merchant/menus/switch` | 快速上架/打烊 | 是 | 一键操作所有菜品 |

### 3.4 营销与促销
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/merchant/coupons` | 商家券列表 | 是 | 状态筛选 |
| POST | `/merchant/coupons` | 创建商家券 | 是 | 满减、折扣等配置 |
| PUT | `/merchant/coupons/{couponId}` | 更新商家券 | 是 | 调整有效期或库存 |
| POST | `/merchant/coupons/{couponId}/status` | 上/下架商家券 | 是 | — |
| GET | `/merchant/promotions` | 店内活动列表 | 是 | 满减活动、折扣菜品 |
| POST | `/merchant/promotions` | 创建店内活动 | 是 | 包括活动时间、规则 |
| PUT | `/merchant/promotions/{promotionId}` | 更新活动 | 是 | — |
| POST | `/merchant/promotions/{promotionId}/status` | 启用/停用活动 | 是 | — |
| GET | `/merchant/bundles` | 优惠套餐列表 | 是 | — |
| POST | `/merchant/bundles` | 创建套餐 | 是 | 菜品组合 + 套餐价 |
| PUT | `/merchant/bundles/{bundleId}` | 更新套餐 | 是 | — |
| DELETE | `/merchant/bundles/{bundleId}` | 删除套餐 | 是 | — |

### 3.5 数据统计
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/merchant/stats/overview` | 核心数据看板 | 是 | 当日/周/月订单与营收 |
| GET | `/merchant/stats/dishes` | 菜品表现 | 是 | 销量排行榜、退款数据 |
| GET | `/merchant/stats/operations` | 营业分析 | 是 | 出餐时长、峰值时段 |

---

## 4. 管理端 API

### 4.1 认证与权限
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| POST | `/admin/auth/login` | 管理员登录 | 否 | 支持 MFA |
| POST | `/admin/auth/logout` | 退出登录 | 是 | — |
| GET | `/admin/profile` | 个人资料与权限 | 是 | 返回角色、权限列表 |
| GET | `/admin/roles` | 角色列表 | 是 | 权限粒度配置 |
| POST | `/admin/roles` | 创建角色 | 是 | 权限点数组 |
| PUT | `/admin/roles/{roleId}` | 更新角色 | 是 | — |
| DELETE | `/admin/roles/{roleId}` | 删除角色 | 是 | 需校验绑定关系 |

### 4.2 平台概览与监控
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/admin/dashboard` | 平台核心指标 | 是 | 注册用户、活跃数、GMV 等 |
| GET | `/admin/dashboard/trends` | 指标趋势数据 | 是 | 支持时间区间 |
| GET | `/admin/system/health` | 系统健康监测 | 是 | API、服务状态 |
| GET | `/admin/system/logs` | 操作日志列表 | 是 | 支持按管理员、时间过滤 |

### 4.3 用户与食堂管理
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/admin/canteens` | 食堂列表 | 是 | 审核状态、营业状态 |
| POST | `/admin/canteens` | 新增食堂 | 是 | 基本信息、联系人 |
| PUT | `/admin/canteens/{canteenId}` | 编辑食堂 | 是 | 更新资质、费率等 |
| POST | `/admin/canteens/{canteenId}/status` | 启用/禁用食堂 | 是 | 或强制打烊 |
| POST | `/admin/canteens/{canteenId}/accounts` | 分配商家账号 | 是 | 建立登录凭证 |
| GET | `/admin/users` | C 端用户列表 | 是 | 支持举报状态筛选 |
| POST | `/admin/users/{userId}/status` | 禁用/启用用户 | 是 | 处理违规 |
| POST | `/admin/users/{userId}/tags` | 添加用户标签 | 是 | VIP、黑名单等 |

### 4.4 平台运营与内容
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/admin/banners` | 首页轮播图列表 | 是 | 支持排序 |
| POST | `/admin/banners` | 新增轮播图 | 是 | 图片上传、跳转链接 |
| PUT | `/admin/banners/{bannerId}` | 更新轮播图 | 是 | — |
| DELETE | `/admin/banners/{bannerId}` | 删除轮播图 | 是 | — |
| GET | `/admin/announcements` | 公告列表 | 是 | 类型筛选 |
| POST | `/admin/announcements` | 发布公告 | 是 | 平台/校内通知等 |
| PUT | `/admin/announcements/{announcementId}` | 编辑公告 | 是 | — |
| DELETE | `/admin/announcements/{announcementId}` | 删除公告 | 是 | — |
| GET | `/admin/coupons` | 平台优惠券列表 | 是 | 全局券、跨食堂活动 |
| POST | `/admin/coupons` | 创建平台券 | 是 | 使用范围、门槛、限量 |
| PUT | `/admin/coupons/{couponId}` | 更新平台券 | 是 | — |
| POST | `/admin/coupons/{couponId}/status` | 上/下架平台券 | 是 | — |
| GET | `/admin/reviews` | 用户评价管理 | 是 | 审核状态、举报处理 |
| POST | `/admin/reviews/{reviewId}/status` | 审核评价 | 是 | 通过/删除 |

### 4.5 财务与结算
| Method | Path | 描述 | 鉴权 | 备注 |
|--------|------|------|------|------|
| GET | `/admin/settlements` | 对账周期列表 | 是 | 查询结算状态 |
| POST | `/admin/settlements` | 生成结算报表 | 是 | 指定周期、食堂 |
| GET | `/admin/settlements/{settlementId}` | 结算详情 | 是 | 含抽成、服务费 |
| GET | `/admin/transactions` | 交易流水查询 | 是 | 支持支付方式、时间、金额过滤 |
| GET | `/admin/reports/finance` | 财务报表 | 是 | 营收、利润等 |

---

## 5. 实时推送与消息
- **WebSocket**：
  - `/ws/orders/{userId}`：C 端订单状态推送。
  - `/ws/merchant/{canteenId}`：商家端新订单、消息推送。
  - `/ws/admin/alerts`：管理端系统告警。
- **消息类型**：
  - `ORDER_STATUS_UPDATE`、`NEW_ORDER`, `SYSTEM_ALERT`, `PROMOTION`.

---

## 6. 文件与图片上传
- 统一使用 `POST /upload`，使用表单上传。
- 返回结构：`{ "url": "https://...","resourceId":"..." }`。
- 根据 `resourceType`（banner、dish_image、qualification）做业务归类。

---

## 7. 审计与安全
- 所有敏感操作（拒单、财务导出、管理员权限调整）记录操作日志。
- 请求需携带 `X-Request-ID` 以便链路追踪。
- 支持幂等性：支付、订单创建等接口使用 `Idempotency-Key`。
- 接口限流：登录、支付类接口设置严控阈值；搜索接口有速率限制。

---

## 8. 版本规划
- `v1`：核心下单流程、商家订单处理、基础管理后台。
- `v1.1`（预留）：实时排队预测、菜品推荐、AI 智能调度。
- `v2`（预留）：多校区支持、第三方配送对接。
