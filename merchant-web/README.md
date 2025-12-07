# HZCU Merchant Web - 商家管理后台

智能食堂点餐平台商家端前端应用，基于 Next.js 构建。

## 📋 项目概述

本项目是 HZCU-Order 智能食堂点餐平台的商家管理端，提供订单管理、菜品管理、数据统计等功能。

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Next.js | 16.x | React 框架，使用 App Router |
| TypeScript | 5.x | 类型安全 |
| CSS Modules | - | 样式隔离 |
| Lucide React | - | 图标库 |

## 📁 项目结构

```
merchant-web/
├── app/                          # Next.js App Router
│   ├── globals.css              # 全局样式和设计变量
│   ├── layout.tsx               # 根布局
│   ├── page.tsx                 # 首页（重定向到 /dashboard）
│   ├── login/                   # 登录页面
│   │   ├── page.tsx
│   │   └── page.module.css
│   └── dashboard/               # 仪表盘（需登录）
│       ├── layout.tsx           # 仪表盘布局（含侧边栏）
│       ├── layout.module.css
│       ├── page.tsx             # 工作台/订单管理
│       ├── page.module.css
│       ├── menu/                # 菜品管理
│       │   ├── page.tsx
│       │   └── page.module.css
│       └── stats/               # 数据统计
│           ├── page.tsx
│           └── page.module.css
├── components/                   # 可复用组件
│   ├── Sidebar.tsx              # 侧边栏导航
│   ├── Sidebar.module.css
│   ├── OrderCard.tsx            # 订单卡片
│   └── OrderCard.module.css
└── package.json
```

## � 快速开始

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 启动生产服务器
npm start
```

默认访问地址：http://localhost:3000

## 📖 功能模块

### 1. 登录模块 (`/login`)
- 商家账号密码登录
- 登录成功后跳转到工作台

### 2. 工作台 (`/dashboard`)
- 实时订单列表展示
- 订单状态筛选（待接单/制作中/待取餐/已完成）
- 订单操作：接单、拒单、开始制作、叫号取餐、完成订单

### 3. 菜品管理 (`/dashboard/menu`)
- 菜品列表展示（含图片、名称、分类、价格、销量）
- 菜品搜索和分类筛选
- 新增/编辑菜品（支持图片上传）
- 菜品上下架状态切换
- 菜品删除

### 4. 数据统计 (`/dashboard/stats`)
- 今日订单数
- 今日营业额
- 累计用户数
- 平均评分

## � 关键 API 接口

本项目对接后端 API，接口文档详见 `/docs/API/API.md`。

### 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/merchant/auth/login` | 商家登录 |
| POST | `/merchant/auth/logout` | 退出登录 |
| GET | `/merchant/profile` | 获取商家资料 |
| PUT | `/merchant/profile` | 更新商家资料 |

### 订单接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/merchant/dashboard/orders` | 获取实时订单列表 |
| GET | `/merchant/orders/{orderId}` | 获取订单详情 |
| POST | `/merchant/orders/{orderId}/accept` | 接单 |
| POST | `/merchant/orders/{orderId}/reject` | 拒单 |
| POST | `/merchant/orders/{orderId}/start` | 开始制作 |
| POST | `/merchant/orders/{orderId}/ready` | 完成制作/待取餐 |
| POST | `/merchant/orders/{orderId}/complete` | 确认取餐 |
| POST | `/merchant/orders/{orderId}/print` | 打印小票 |

### 菜品接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/merchant/dishes` | 获取菜品列表 |
| POST | `/merchant/dishes` | 新建菜品 |
| GET | `/merchant/dishes/{dishId}` | 获取菜品详情 |
| PUT | `/merchant/dishes/{dishId}` | 更新菜品 |
| DELETE | `/merchant/dishes/{dishId}` | 删除菜品 |
| POST | `/merchant/dishes/{dishId}/status` | 上/下架菜品 |

### 统计接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/merchant/stats/overview` | 核心数据看板 |
| GET | `/merchant/stats/dishes` | 菜品销售排行 |
| GET | `/merchant/stats/operations` | 营业分析 |

## 📝 开发历史

| 日期 | 版本 | 更新内容 |
|------|------|----------|
| 2025-12-08 | v0.1.0 | 初始版本发布 |
| - | - | ✅ 项目初始化 (Next.js + TypeScript) |
| - | - | ✅ 登录页面实现 |
| - | - | ✅ 仪表盘布局（侧边栏 + 头部） |
| - | - | ✅ 订单管理功能（工作台） |
| - | - | ✅ 菜品管理功能（列表、搜索、筛选、增删改） |
| - | - | ✅ 菜品图片上传功能 |
| - | - | ✅ 数据统计页面 |
| - | - | ✅ 退出登录功能 |
| - | - | ✅ UI 美化（现代化设计、动效） |

## 🎨 设计规范

### 颜色变量
```css
--primary: #6366f1;        /* 主色 - Indigo */
--primary-hover: #4f46e5;  /* 主色悬停态 */
--background: #f8fafc;     /* 页面背景 */
--foreground: #0f172a;     /* 主文字色 */
--card: #ffffff;           /* 卡片背景 */
--border: #e2e8f0;         /* 边框色 */
--muted-foreground: #64748b; /* 次要文字 */
--success: #10b981;        /* 成功色 */
--warning: #f59e0b;        /* 警告色 */
--error: #ef4444;          /* 错误色 */
```

### 字体
- 主字体：Inter (Google Fonts)
- 备用：-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto

## 📄 许可证

本项目仅供学习交流使用。
