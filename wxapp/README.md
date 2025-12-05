# HZCU-Order 校园点餐微信小程序

## 项目简介

HZCU-Order 是一款面向浙大城市学院的校园堂食点餐微信小程序，为师生提供便捷的食堂/餐厅堂食点餐服务。用户可以通过小程序浏览菜品、下单点餐，到店取餐就餐。

## 技术栈

- **框架**: 微信小程序原生框架
- **语言**: TypeScript
- **样式**: WXSS
- **组件框架**: glass-easel

## 项目结构

```
wxapp/
├── miniprogram/                 # 小程序主目录
│   ├── app.json                 # 小程序全局配置
│   ├── app.ts                   # 小程序入口文件
│   ├── app.wxss                 # 全局样式
│   ├── images/                  # 图片资源目录
│   │   ├── tabbar/              # 底部导航图标
│   │   ├── category/            # 分类图标
│   │   ├── shops/               # 商家Logo
│   │   ├── goods/               # 商品图片
│   │   ├── menu/                # 菜单图标
│   │   ├── order/               # 订单状态图标
│   │   └── avatar/              # 用户头像
│   ├── pages/                   # 页面目录
│   │   ├── index/               # 首页（商家列表）
│   │   ├── shop/                # 商家详情页（点餐）
│   │   ├── order/               # 订单列表页
│   │   ├── mine/                # 个人中心页
│   │   ├── checkout/            # 结算页
│   │   └── logs/                # 日志页
│   └── utils/                   # 工具函数
├── typings/                     # TypeScript类型定义
├── package.json                 # 项目配置
├── project.config.json          # 项目配置
└── tsconfig.json                # TypeScript配置
```

## 页面说明

### 1. 首页 (pages/index)

**功能描述**: 展示商家列表，支持搜索和分类筛选

**主要功能**:
- 地址定位显示与切换
- 关键词搜索商家
- 分类导航（汉堡披萨、炸鸡薯条、水果、甜品饮料等）
- 筛选标签（附近推荐/发现好菜）
- 商家列表展示（评分、月销量、等待时间、距离、优惠标签）
- 下拉刷新、上拉加载更多
- 数据从后端API获取

**数据结构**:
```typescript
interface ShopItem {
  id: number;
  name: string;
  logo: string;
  rating: number;
  monthlySales: number;
  waitTime: number;       // 预计等待时间（分钟）
  distance: string;
  minPrice: number;       // 起点价格
  tags: string[];
}
```

### 2. 商家详情页 (pages/shop)

**功能描述**: 商家详情与点餐功能

**主要功能**:
- 商家信息展示（名称、预计等待时间、月销量、公告）
- 收藏/举报功能
- 标签页切换（点餐/评价/商家信息）
- 左侧分类导航
- 右侧商品列表
- 商品加减购物车
- 底部购物车栏
- 购物车详情弹窗
- 去结算跳转

**数据结构**:
```typescript
interface GoodsItem {
  id: number;
  name: string;
  description: string;
  image: string;
  price: number;
  originalPrice?: number;
  monthlySales: number;
  goodRate: number;
  tags: string[];
  count: number;
}

interface Category {
  id: number;
  name: string;
  goods: GoodsItem[];
}
```

### 3. 订单页 (pages/order)

**功能描述**: 订单列表管理

**主要功能**:
- 订单状态筛选（全部/待付款/制作中/待取餐/已完成）
- 订单列表展示
- 取消订单
- 去付款
- 再来一单
- 去评价
- 确认取餐
- 下拉刷新
- 数据从后端API获取

**数据结构**:
```typescript
interface OrderItem {
  id: number;
  shopId: number;
  shopName: string;
  shopLogo: string;
  goods: GoodsItem[];
  totalCount: number;
  totalPrice: number;
  status: 'pending' | 'paid' | 'preparing' | 'ready' | 'completed';
  // pending: 待付款, paid: 已付款, preparing: 制作中, ready: 待取餐, completed: 已完成
  statusText: string;
  createTime: string;
  pickupCode?: string;    // 取餐码
  tableNo?: string;       // 桌号（可选）
}
```

### 4. 个人中心页 (pages/mine)

**功能描述**: 用户个人信息与设置

**主要功能**:
- 用户信息展示/登录
- 订单快捷入口（待付款/制作中/待取餐/已完成）
- 常用餐厅管理
- 我的收藏
- 优惠券
- 帮助中心
- 意见反馈
- 关于我们
- 联系客服

### 5. 结算页 (pages/checkout)

**功能描述**: 订单确认与支付

**主要功能**:
- 就餐方式选择（堂食/打包）
- 桌号选择（堂食时）
- 商品列表确认
- 取餐时间选择
- 备注输入（口味偏好等）
- 价格明细（商品金额、打包费、优惠券）
- 提交订单
- 生成取餐码

## 全局配置

### TabBar 配置

```json
{
  "tabBar": {
    "color": "#999999",
    "selectedColor": "#3498db",
    "list": [
      { "pagePath": "pages/index/index", "text": "首页" },
      { "pagePath": "pages/order/order", "text": "订单" },
      { "pagePath": "pages/mine/mine", "text": "我的" }
    ]
  }
}
```

### 主题色

| 颜色名称 | 色值 | 用途 |
|---------|------|------|
| 主色调 | #007AFF | 导航栏、按钮、选中状态 |
| 辅助色 | #5AC8FA | 辅助按钮、高亮背景 |
| 警告色 | #FF9500 | 评分、优惠信息 |
| 危险色 | #FF3B30 | 价格、删除、待付款 |
| 文字色 | #333333 | 主要文字 |
| 次要文字 | #666666 | 次要文字 |
| 占位符 | #999999 | 占位符文字 |
| 背景色 | #F7F8FA | 页面背景 |
| 卡片背景 | #FFFFFF | 卡片、弹窗背景 |

## API 接口预留

### 商家相关

| 接口 | 方法 | 描述 |
|------|------|------|
| /api/shops | GET | 获取商家列表 |
| /api/shops/:id | GET | 获取商家详情 |
| /api/shops/:id/goods | GET | 获取商家商品列表 |
| /api/shops/:id/comments | GET | 获取商家评价 |

### 订单相关

| 接口 | 方法 | 描述 |
|------|------|------|
| /api/orders | GET | 获取订单列表 |
| /api/orders | POST | 创建订单 |
| /api/orders/:id | GET | 获取订单详情 |
| /api/orders/:id/cancel | POST | 取消订单 |
| /api/orders/:id/confirm | POST | 确认取餐 |

### 用户相关

| 接口 | 方法 | 描述 |
|------|------|------|
| /api/user/login | POST | 用户登录 |
| /api/user/info | GET | 获取用户信息 |
| /api/user/bindPhone | POST | 绑定手机号 |
| /api/user/bindStudent | POST | 绑定学号 |
| /api/user/favorites | GET | 获取收藏列表 |

## 本地存储

| Key | 类型 | 描述 |
|-----|------|------|
| userInfo | Object | 用户信息 |
| cartData | Object | 购物车数据 |
| lastShop | Object | 上次点餐商家 |
| logs | Array | 操作日志 |

## 开发指南

### 环境要求

- 微信开发者工具 >= 1.06
- Node.js >= 14.0

### 安装依赖

```bash
npm install
```

### 构建 TypeScript

```bash
npm run build
```

### 开发调试

1. 使用微信开发者工具打开 `wxapp` 目录
2. 在项目设置中开启 ES6 转 ES5、增强编译
3. 点击编译即可预览

### 图片资源替换

当前项目使用 SVG 占位图片，正式上线前请替换为实际图片：

1. `images/tabbar/` - 底部导航图标（建议尺寸：81x81px）
2. `images/category/` - 分类图标（建议尺寸：80x80px）
3. `images/shops/` - 商家 Logo（建议尺寸：140x140px）
4. `images/goods/` - 商品图片（建议尺寸：160x160px）

## 待开发功能
- [ ] **后端对接** (优先级高)：对接真实 API，替换 Mock 数据
- [ ] **用户认证**：微信登录 (OpenID) 与用户信息同步
- [ ] **支付集成**：微信支付功能
- [ ] **状态管理**：购物车跨页面持久化
- [ ] **性能优化**：长列表虚拟滚动 (Virtual List)
- [ ] 订单实时状态推送 (WebSocket)
- [ ] 优惠券系统
- [ ] 评价功能
- [ ] 收藏商家功能

## 后端接口对接说明

### 接口请求封装

建议在 `utils/request.ts` 中封装统一的请求方法：

```typescript
const BASE_URL = 'https://api.example.com';  // 后端接口地址

export const request = (options: WechatMiniprogram.RequestOption) => {
  return new Promise((resolve, reject) => {
    wx.request({
      ...options,
      url: BASE_URL + options.url,
      header: {
        'Content-Type': 'application/json',
        'Authorization': wx.getStorageSync('token') || '',
        ...options.header
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(res);
        }
      },
      fail: reject
    });
  });
};
```

### 接口调用示例

```typescript
// 获取商家列表
import { request } from '../../utils/request';

const loadShopList = async () => {
  try {
    const res = await request({
      url: '/api/shops',
      method: 'GET',
      data: { page: 1, pageSize: 10 }
    });
    this.setData({ shopList: res.data });
  } catch (error) {
    wx.showToast({ title: '加载失败', icon: 'none' });
  }
};
```

## 版本历史

### v1.1.0 (2025-12-04)
- **UI/UX 全面重构**：现代化卡片式设计，统一蓝白配色 (#007AFF)
- **适配优化**：iPhone X+ 安全区域适配，自适应布局防止溢出
- **交互优化**：首页吸顶冲突修复，搜索框与按钮对齐优化
- **页面升级**：首页、点餐页、订单页、个人中心页、结算页样式全量更新
- **功能增强**：个人中心订单状态跳转支持，订单列表页入场动画优化

### v1.0.0 (2024-01-15)
- 初始版本
- 完成基础页面框架
- 首页商家列表
- 商家详情与点餐
- 订单列表
- 个人中心
- 结算流程
- 预留后端API接口

## 团队成员

- 前端开发：dev/front-end 分支

## 许可证

MIT License
