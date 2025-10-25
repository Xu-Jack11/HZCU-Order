# 智能食堂点餐平台数据库设计（MySQL）

## 1. 设计原则
- **字符集**：`utf8mb4`。
- **命名规则**：表使用下划线风格（如 `user_profile`），主键命名 `id`，外键列命名 `<entity>_id`。
- **时间字段**：统一包含 `created_at`、`updated_at`，需 `TIMESTAMP`/`DATETIME` 并设置默认值。
- **逻辑删除**：需要保留历史的业务数据增加 `is_deleted TINYINT(1)`。
- **审计追踪**：关键业务表记录操作用户/管理员。

## 2. 模块划分
1. 基础用户与账户模块
2. 食堂与商家模块
3. 菜品与菜单管理
4. 订单与支付模块
5. 营销促销模块
6. 内容与运营管理
7. 财务与结算
8. 系统配置与监控

---

## 3. 核心 ER 概览
- `user`（C 端用户）一对多 `order`、`user_coupon`、`favorite`、`review`、`address`。
- `canteen`（食堂）一对多 `dish`、`merchant_account`、`promotion`、`settlement`。
- `dish` 与 `dish_spec`、`dish_tag`、`menu` 多对多。
- `order` 与 `order_item` 一对多；与 `payment_record`、`refund_record` 一对多。
- `admin_user` 与 `admin_role` 多对多；`admin_role` 与 `admin_permission` 多对多。

---

## 4. 表结构详情

### 4.1 基础用户与账户
| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `user` | `id` | BIGINT PK | 主键，雪花或自增 |
| | `openid` | VARCHAR(64) | 微信 openid，唯一 |
| | `unionid` | VARCHAR(64) | 微信 unionid，可空 |
| | `nickname` | VARCHAR(64) | 昵称 |
| | `avatar_url` | VARCHAR(255) | 头像 |
| | `mobile` | VARCHAR(20) | 手机号，唯一，可空 |
| | `status` | TINYINT | 0 正常，1 禁用 |
| | `last_login_at` | DATETIME | 最后登录时间 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `user_profile` | `user_id` | BIGINT PK/FK | 对应 user |
| | `gender` | TINYINT | 0 未知 1 男 2 女 |
| | `campus_id` | BIGINT | 校区/院校标识 |
| | `campus_card_no` | VARCHAR(32) | 校园卡号 |
| | `campus_card_status` | TINYINT | 0 未绑定,1 已绑定,2 绑定中 |
| | `birthday` | DATE | 可空 |
| | `remark` | VARCHAR(255) | 备注 |
| | `updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `user_asset` | `user_id` | BIGINT PK/FK | 对应 user |
| | `balance` | DECIMAL(10,2) | 余额 |
| | `points` | INT | 积分 |
| | `total_recharge` | DECIMAL(10,2) | 累计充值 |
| | `total_spend` | DECIMAL(10,2) | 累计消费 |
| | `updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `user_address` | `id` | BIGINT PK | — |
| | `user_id` | BIGINT FK | 对应 user |
| | `receiver` | VARCHAR(32) | 收货人 |
| | `phone` | VARCHAR(20) | 联系方式 |
| | `campus` | VARCHAR(64) | 校区 |
| | `building` | VARCHAR(64) | 楼栋/宿舍号 |
| | `is_default` | TINYINT | 默认地址标识 |
| | `is_deleted` | TINYINT | 逻辑删除 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `favorite` | `id` | BIGINT PK | — |
| | `user_id` | BIGINT FK | — |
| | `target_type` | TINYINT | 1 菜品 2 食堂 |
| | `target_id` | BIGINT | 对应菜品/食堂 |
| | `created_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `footprint` | `id` | BIGINT PK | — |
| | `user_id` | BIGINT FK | — |
| | `target_type` | TINYINT | 1 菜品 2 食堂 |
| | `target_id` | BIGINT | — |
| | `viewed_at` | DATETIME | 浏览时间 |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `user_notification` | `id` | BIGINT PK | — |
| | `user_id` | BIGINT FK | — |
| | `type` | VARCHAR(32) | system/order/promotion |
| | `title` | VARCHAR(128) | — |
| | `content` | TEXT | — |
| | `is_read` | TINYINT | — |
| | `extra` | JSON | 关联数据 |
| | `created_at/updated_at` | DATETIME | — |

### 4.2 食堂与商家
| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `canteen` | `id` | BIGINT PK | — |
| | `name` | VARCHAR(64) | 食堂名称 |
| | `campus` | VARCHAR(64) | 所属校区 |
| | `location` | VARCHAR(128) | 地址/经纬度 JSON |
| | `contact_phone` | VARCHAR(20) | 联系方式 |
| | `status` | TINYINT | 0 正常 1 待审核 2 禁用 3 打烊 |
| | `business_hours` | JSON | 不同时段营业时间 |
| | `service_fee_rate` | DECIMAL(5,2) | 平台抽成比例 |
| | `remark` | VARCHAR(255) | — |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `canteen_status` | `id` | BIGINT PK | — |
| | `canteen_id` | BIGINT FK | — |
| | `status` | TINYINT | 0 营业中 1 忙碌 2 已打烊 3 即将打烊 |
| | `estimated_wait_time` | INT | 单位分钟 |
| | `updated_at` | DATETIME | 最近刷新 |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `canteen_announcement` | `id` | BIGINT PK | — |
| | `canteen_id` | BIGINT FK | — |
| | `title` | VARCHAR(128) | — |
| | `content` | TEXT | — |
| | `type` | TINYINT | 1 食堂公告 2 促销 |
| | `effective_from/to` | DATETIME | 生效时间范围 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `merchant_account` | `id` | BIGINT PK | — |
| | `canteen_id` | BIGINT FK | 所属食堂 |
| | `username` | VARCHAR(32) | 登录账号，唯一 |
| | `password_hash` | VARCHAR(128) | 加密存储 |
| | `real_name` | VARCHAR(32) | 负责人 |
| | `mobile` | VARCHAR(20) | 联系方式 |
| | `role` | VARCHAR(32) | owner/manager/staff |
| | `status` | TINYINT | 0 正常 1 禁用 |
| | `last_login_at` | DATETIME | — |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `merchant_operation_log` | `id` | BIGINT PK | — |
| | `merchant_account_id` | BIGINT FK | 操作人 |
| | `operation` | VARCHAR(64) | 操作类型 |
| | `detail` | JSON | 具体内容 |
| | `created_at` | DATETIME | — |

### 4.3 菜品与菜单管理
| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `dish_category` | `id` | BIGINT PK | — |
| | `canteen_id` | BIGINT FK | — |
| | `name` | VARCHAR(32) | 分类名 |
| | `sort_order` | INT | 排序值 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `dish_tag` | `id` | BIGINT PK | — |
| | `canteen_id` | BIGINT FK | — |
| | `name` | VARCHAR(32) | 如“加急可选” |
| | `type` | TINYINT | 1 口味 2 特殊标签 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `dish` | `id` | BIGINT PK | — |
| | `canteen_id` | BIGINT FK | — |
| | `category_id` | BIGINT FK | — |
| | `name` | VARCHAR(64) | 菜品名 |
| | `description` | TEXT | 主要食材等 |
| | `cover_image` | VARCHAR(255) | 图片 |
| | `month_sales` | INT | 月销量（统计缓存） |
| | `base_price` | DECIMAL(8,2) | 基础价格（默认规格） |
| | `status` | TINYINT | 0 上架 1 下架 2 售罄 |
| | `is_deleted` | TINYINT | 逻辑删除 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `dish_spec` | `id` | BIGINT PK | — |
| | `dish_id` | BIGINT FK | — |
| | `name` | VARCHAR(32) | 规格名（大份/小份） |
| | `price` | DECIMAL(8,2) | 规格价格 |
| | `stock` | INT | 当前库存，-1 表示不限 |
| | `is_default` | TINYINT | 默认规格 |
| | `spicy_level` | TINYINT | 0 不辣 1 微辣 2 中辣 3 重辣 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `dish_spec_option` | `id` | BIGINT PK | — |
| | `spec_id` | BIGINT FK | 对应规格 |
| | `option_type` | VARCHAR(32) | extra辣度/加料等 |
| | `option_name` | VARCHAR(32) | 选项名 |
| | `extra_price` | DECIMAL(6,2) | 附加价格 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `dish_tag_relation` | `dish_id` | BIGINT PK/FK | — |
| | `tag_id` | BIGINT PK/FK | 多对多关联 |
| | `created_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `menu` | `id` | BIGINT PK | — |
| | `canteen_id` | BIGINT FK | — |
| | `name` | VARCHAR(32) | 如早餐/午餐 |
| | `start_time` | TIME | 生效开始 |
| | `end_time` | TIME | 生效结束 |
| | `is_active` | TINYINT | 是否启用 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `menu_dish` | `menu_id` | BIGINT PK/FK | — |
| | `dish_id` | BIGINT PK/FK | — |
| | `sort_order` | INT | 排序 |
| | `created_at` | DATETIME | — |

### 4.4 订单与支付
| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `cart_snapshot` | `id` | BIGINT PK | — |
| | `user_id` | BIGINT FK | — |
| | `items` | JSON | 临时购物车内容 |
| | `total_amount` | DECIMAL(10,2) | 总价 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `order` | `id` | BIGINT PK | 平台订单号 |
| | `user_id` | BIGINT FK | 下单用户 |
| | `canteen_id` | BIGINT FK | 食堂 |
| | `order_no` | VARCHAR(32) | 对用户展示号，唯一 |
| | `status` | TINYINT | 0 待支付 1 待接单 2 制作中 3 待取餐 4 已完成 5 已取消 |
| | `dining_mode` | TINYINT | 0 立即取餐 1 预约 |
| | `reserve_start` | DATETIME | 预约开始时间 |
| | `reserve_end` | DATETIME | 预约结束时间 |
| | `total_amount` | DECIMAL(10,2) | 商品总额 |
| | `package_fee` | DECIMAL(8,2) | 打包费 |
| | `discount_amount` | DECIMAL(10,2) | 优惠金额 |
| | `payable_amount` | DECIMAL(10,2) | 应付金额 |
| | `paid_amount` | DECIMAL(10,2) | 实付 |
| | `payment_method` | TINYINT | 1 微信 2 校园卡 |
| | `pickup_code` | VARCHAR(16) | 取餐号 |
| | `pickup_window` | VARCHAR(16) | 窗口号 |
| | `remark` | VARCHAR(255) | 用户备注 |
| | `cancel_reason` | VARCHAR(255) | 取消原因 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `order_item` | `id` | BIGINT PK | — |
| | `order_id` | BIGINT FK | — |
| | `dish_id` | BIGINT FK | — |
| | `spec_id` | BIGINT FK | 所选规格 |
| | `dish_name` | VARCHAR(64) | 冗余存储 |
| | `spec_name` | VARCHAR(32) | 冗余 |
| | `unit_price` | DECIMAL(8,2) | 原价 |
| | `quantity` | INT | 数量 |
| | `extra_options` | JSON | 加辣等 |
| | `total_price` | DECIMAL(10,2) | 小计 |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `order_status_log` | `id` | BIGINT PK | — |
| | `order_id` | BIGINT FK | — |
| | `from_status` | TINYINT | — |
| | `to_status` | TINYINT | — |
| | `operator_type` | TINYINT | 1 用户 2 商家 3 系统 |
| | `operator_id` | BIGINT | 操作人 |
| | `remark` | VARCHAR(255) | — |
| | `created_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `payment_record` | `id` | BIGINT PK | — |
| | `order_id` | BIGINT FK | — |
| | `pay_no` | VARCHAR(32) | 支付流水 |
| | `channel` | VARCHAR(16) | wxpay/campus_card |
| | `amount` | DECIMAL(10,2) | 支付金额 |
| | `status` | TINYINT | 0 待支付 1 成功 2 失败 |
| | `paid_at` | DATETIME | 支付时间 |
| | `raw_response` | JSON | 第三方返回 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `refund_record` | `id` | BIGINT PK | — |
| | `order_id` | BIGINT FK | 对应订单 |
| | `after_sale_id` | BIGINT FK | 对应售后 |
| | `refund_no` | VARCHAR(32) | 退款流水号 |
| | `amount` | DECIMAL(10,2) | 退款金额 |
| | `reason` | VARCHAR(255) | 退款原因 |
| | `status` | TINYINT | 0 审核中 1 成功 2 失败 |
| | `processed_at` | DATETIME | 完成时间 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `after_sale` | `id` | BIGINT PK | — |
| | `order_id` | BIGINT FK | — |
| | `user_id` | BIGINT FK | — |
| | `type` | TINYINT | 1 退款 2 投诉 |
| | `status` | TINYINT | 0 待处理 1 处理中 2 完成 3 驳回 |
| | `content` | TEXT | 用户描述 |
| | `evidence` | JSON | 图片等 |
| | `handled_by` | BIGINT | 管理端处理人 |
| | `handled_at` | DATETIME | — |
| | `result` | TEXT | 处理结果 |
| | `created_at/updated_at` | DATETIME | — |

### 4.5 营销促销
| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `coupon_template` | `id` | BIGINT PK | — |
| | `scope_type` | TINYINT | 0 平台 1 食堂 |
| | `scope_id` | BIGINT | 对应平台为 0，食堂 ID |
| | `name` | VARCHAR(64) | 优惠券名称 |
| | `type` | TINYINT | 1 满减 2 折扣 |
| | `threshold_amount` | DECIMAL(10,2) | 满减门槛 |
| | `discount_amount` | DECIMAL(10,2) | 减免金额 |
| | `discount_rate` | DECIMAL(4,2) | 折扣率（0-1） |
| | `total_count` | INT | 发放总量 |
| | `claimed_count` | INT | 已领取数量 |
| | `valid_from/to` | DATETIME | 有效期 |
| | `status` | TINYINT | 0 未上架 1 上架 2 下架 |
| | `created_by` | BIGINT | 创建人（管理员/商家） |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `user_coupon` | `id` | BIGINT PK | — |
| | `coupon_template_id` | BIGINT FK | — |
| | `user_id` | BIGINT FK | — |
| | `canteen_id` | BIGINT | 限定食堂，可空 |
| | `status` | TINYINT | 0 未使用 1 已使用 2 过期 |
| | `used_order_id` | BIGINT | 使用的订单 |
| | `claimed_at` | DATETIME | 领取时间 |
| | `used_at` | DATETIME | 使用时间 |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `promotion` | `id` | BIGINT PK | — |
| | `canteen_id` | BIGINT FK | — |
| | `name` | VARCHAR(64) | 活动名称 |
| | `type` | TINYINT | 1 满减活动 2 折扣菜品 |
| | `rule` | JSON | 阈值、折扣 |
| | `start_time/end_time` | DATETIME | — |
| | `status` | TINYINT | 0 未开始 1 进行中 2 已结束 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `promotion_dish` | `promotion_id` | BIGINT PK/FK | — |
| | `dish_id` | BIGINT PK/FK | — |
| | `discount_price` | DECIMAL(8,2) | 折扣价 |
| | `created_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `bundle` | `id` | BIGINT PK | 优惠套餐 |
| | `canteen_id` | BIGINT FK | — |
| | `name` | VARCHAR(64) | 套餐名 |
| | `total_price` | DECIMAL(10,2) | 套餐价 |
| | `status` | TINYINT | 0 上架 1 下架 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `bundle_item` | `bundle_id` | BIGINT PK/FK | — |
| | `dish_id` | BIGINT PK/FK | — |
| | `quantity` | INT | 数量 |
| | `created_at` | DATETIME | — |

### 4.6 内容与运营管理
| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `banner` | `id` | BIGINT PK | — |
| | `title` | VARCHAR(64) | 标题 |
| | `image_url` | VARCHAR(255) | 图片地址 |
| | `jump_link` | VARCHAR(255) | 跳转链接 |
| | `position` | VARCHAR(32) | home/merchant |
| | `sort_order` | INT | 展示顺序 |
| | `status` | TINYINT | 0 未上架 1 上架 2 下架 |
| | `created_by` | BIGINT | 管理员 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `announcement` | `id` | BIGINT PK | — |
| | `scope` | TINYINT | 0 全平台 1 食堂 2 校园通知 |
| | `target_id` | BIGINT | scope=1 时为食堂 ID |
| | `title` | VARCHAR(128) | — |
| | `content` | TEXT | — |
| | `effective_from/to` | DATETIME | — |
| | `status` | TINYINT | 0 草稿 1 发布 2 下线 |
| | `created_by` | BIGINT | 管理员 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `review` | `id` | BIGINT PK | — |
| | `order_id` | BIGINT FK | — |
| | `user_id` | BIGINT FK | — |
| | `canteen_id` | BIGINT FK | — |
| | `dish_id` | BIGINT | 主菜品，可空 |
| | `rating` | TINYINT | 1-5 |
| | `tags` | JSON | 好评标签 |
| | `content` | TEXT | 用户评价 |
| | `images` | JSON | 图片 |
| | `status` | TINYINT | 0 待审核 1 通过 2 删除 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `review_audit_log` | `id` | BIGINT PK | — |
| | `review_id` | BIGINT FK | — |
| | `admin_id` | BIGINT FK | 审核人 |
| | `action` | TINYINT | 1 通过 2 删除 3 还原 |
| | `remark` | VARCHAR(255) | 说明 |
| | `created_at` | DATETIME | — |

### 4.7 财务与结算
| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `settlement` | `id` | BIGINT PK | — |
| | `canteen_id` | BIGINT FK | — |
| | `period_start/end` | DATE | 结算周期 |
| | `order_count` | INT | 订单数量 |
| | `gross_amount` | DECIMAL(12,2) | 订单总额 |
| | `platform_fee` | DECIMAL(12,2) | 平台抽成 |
| | `service_fee` | DECIMAL(12,2) | 固定服务费 |
| | `net_amount` | DECIMAL(12,2) | 应结金额 |
| | `status` | TINYINT | 0 待确认 1 待打款 2 已打款 |
| | `confirmed_at` | DATETIME | 食堂确认 |
| | `paid_at` | DATETIME | 结算完成 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `settlement_order` | `id` | BIGINT PK | — |
| | `settlement_id` | BIGINT FK | — |
| | `order_id` | BIGINT FK | — |
| | `order_amount` | DECIMAL(10,2) | 订单金额 |
| | `platform_fee` | DECIMAL(10,2) | 抽成 |
| | `service_fee` | DECIMAL(10,2) | 服务费 |
| | `created_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `transaction_record` | `id` | BIGINT PK | — |
| | `order_id` | BIGINT FK | — |
| | `user_id` | BIGINT FK | — |
| | `canteen_id` | BIGINT FK | — |
| | `type` | TINYINT | 1 支付 2 退款 3 余额充值 |
| | `amount` | DECIMAL(12,2) | 金额（退款为负） |
| | `payment_channel` | VARCHAR(16) | wxpay/campus_card |
| | `trade_no` | VARCHAR(32) | 外部流水 |
| | `trade_at` | DATETIME | 交易时间 |
| | `created_at/updated_at` | DATETIME | — |

### 4.8 系统配置与监控
| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `admin_user` | `id` | BIGINT PK | — |
| | `username` | VARCHAR(32) | 登录账号 |
| | `password_hash` | VARCHAR(128) | — |
| | `real_name` | VARCHAR(32) | 姓名 |
| | `mobile` | VARCHAR(20) | — |
| | `email` | VARCHAR(64) | — |
| | `status` | TINYINT | 0 正常 1 禁用 |
| | `last_login_at` | DATETIME | — |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `admin_role` | `id` | BIGINT PK | — |
| | `name` | VARCHAR(32) | 如超级管理员 |
| | `description` | VARCHAR(128) | — |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `admin_permission` | `id` | BIGINT PK | — |
| | `code` | VARCHAR(64) | 权限标识 |
| | `name` | VARCHAR(64) | 说明 |
| | `module` | VARCHAR(32) | 所属模块 |
| | `created_at/updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `admin_role_permission` | `role_id` | BIGINT PK/FK | — |
| | `permission_id` | BIGINT PK/FK | — |
| | `created_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `admin_user_role` | `user_id` | BIGINT PK/FK | — |
| | `role_id` | BIGINT PK/FK | — |
| | `created_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `system_param` | `id` | BIGINT PK | — |
| | `param_key` | VARCHAR(64) | 如 `order.auto_cancel_minutes` |
| | `param_value` | VARCHAR(128) | — |
| | `description` | VARCHAR(255) | — |
| | `updated_by` | BIGINT | 管理员 |
| | `updated_at` | DATETIME | — |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `system_monitor` | `id` | BIGINT PK | — |
| | `service_name` | VARCHAR(64) | 被监控服务 |
| | `status` | TINYINT | 0 正常 1 异常 |
| | `metrics` | JSON | 指标数据 |
| | `checked_at` | DATETIME | 检测时间 |

| 表名 | 字段 | 类型 | 说明 |
|------|------|------|------|
| `audit_log` | `id` | BIGINT PK | — |
| | `operator_type` | TINYINT | 1 管理员 2 商家 |
| | `operator_id` | BIGINT | 操作人 ID |
| | `action` | VARCHAR(64) | 操作类型 |
| | `request_path` | VARCHAR(128) | 接口路径 |
| | `changes` | JSON | 变更详情 |
| | `ip_address` | VARCHAR(45) | 来源 IP |
| | `created_at` | DATETIME | — |

---

## 5. 关键索引建议
- `user`: 索引 `idx_user_openid`，`uk_user_mobile`。
- `dish`: 组合索引 `(canteen_id, status)`、全文/LIKE 索引 `name`。
- `dish_spec`: 索引 `(dish_id, is_default)`。
- `order`: 索引 `uk_order_no`，组合索引 `(user_id, status, created_at)` 和 `(canteen_id, status, created_at)`。
- `order_status_log`: 索引 `(order_id, created_at)`。
- `payment_record`: 索引 `uk_pay_no`。
- `coupon_template`: 索引 `(scope_type, scope_id, status)`。
- `user_coupon`: 索引 `(user_id, status)`。
- `promotion_dish`: 索引 `(promotion_id, dish_id)`。
- `review`: 索引 `(canteen_id, status)`、`(dish_id, status)`。
- `settlement`: 索引 `(canteen_id, period_start, period_end)`。
- `transaction_record`: 索引 `(canteen_id, trade_at)`、`(user_id, trade_at)`。

---

## 6. 数据一致性与扩展考虑
- **库存扣减**：使用 `dish_spec` 的库存字段，结合乐观锁或库存表 `dish_stock_log`（后续扩展）防止超卖。
- **高并发订单**：关键订单写操作放在事务中，使用消息队列异步更新统计数据（如 `month_sales`）。
- **预约取餐**：建立业务校验防止同一时段超出食堂处理能力，可扩展 `reservation_slot` 表。
- **跨校区支持**：未来可新增 `campus` 表，`canteen` 关联，方便扩张。
- **数据分区**：大体量表（`order`, `transaction_record`, `audit_log`）考虑按时间范围分区或分库分表。
- **日志留存**：`audit_log` 保留至少 180 天，归档到冷存储。
