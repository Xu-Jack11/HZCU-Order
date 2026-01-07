# 白盒测试用例设计说明书

## 1. 概述
本文档基于白盒测试原则（逻辑覆盖），针对 HZCU-Order 系统中不包含余额逻辑的**标准下单流程**（`OrderService.createOrder`）进行单元测试设计。重点关注业务代码的内部逻辑分支、循环及异常路径。

## 2. 测试对象
- **核心组件**: `OrderService.java`
- **关键方法**: `createOrder(Order order, List<OrderItem> items)`

---

## 3. 核心逻辑路径分析

根据 `OrderService.createOrder` 的代码实现，我们将其拆解为以下控制流路径：

1. **订单基础信息初始化**:
   - `orderNo` 生成逻辑。
   - 初始状态 forced 设置为 `PENDING_PAYMENT`。

2. **子订单项 (OrderItem) 处理循环与分支**:
   - **分支 A (菜品名称快照)**:
     - 路径 1: 传入数据已包含菜品名称 (`dish.getName() != null`)。
     - 路径 2: 传入数据不含名称但含 ID，需从 `DishRepository` 数据库查询。
     - 路径 3: 数据库查询未命中（边缘场景测试）。
   - **分支 B (金额计算补全)**:
     - 路径 4: 子项总价已传入。
     - 路径 5: 子项总价缺失，由系统执行 `unitPrice * quantity` 补全。

3. **数据持久化与日志**:
   - 验证 `order` 先存，`item` 后存（保持外键关联）。
   - 验证 `OrderStatusLog` 是否正确记录了创建轨迹。

---

## 4. 白盒测试用例表

| 编号 | 测试场景/路径 | 输入数据特征 (Input) | 覆盖级别 | 预期结果 (Expected) |
| :--- | :--- | :--- | :--- | :--- |
| **ORD-TC-01** | **完整数据路径** | 包含菜品名、包含子项总价 | 语句覆盖 | 订单成功创建，无需额外 DB 查询或计算 |
| **ORD-TC-02** | **名称补全路径** | `dishName = null`, 提供 `dishId` | 分支覆盖 (Path 2) | 调用 `dishRepository.findById` 并将查出的名存入快照 |
| **ORD-TC-03** | **价格计算路径** | `item.totalPrice = null` | 分支覆盖 (Path 5) | 代码执行 `multiply` 运算，存入正确的子项总金额 |
| **ORD-TC-04** | **日志完整性** | 常规下单 | 逻辑覆盖 | 在 `OrderStatusLog` 表中生成一条 `fromStatus=null`, `toStatus=PENDING_PAYMENT` 的记录 |
| **ORD-TC-05** | **空子项校验** | `items` 为空列表 | 循环覆盖 (0次) | 成功保存订单主体，不进入循环，不抛出异常 |

---

## 5. 覆盖率验证策略 (White-box Criteria)

1. **语句覆盖率 (100%)**: 确保 `createOrder` 方法中的每一行代码都至少被执行一次。
2. **分支覆盖率 (100%)**: 以及菜品名称查询的 `if-else if` 检查。
3. **隔离性**: 使用 `Mockito` 模拟 Repository 层，专注于业务逻辑代码的验证。

