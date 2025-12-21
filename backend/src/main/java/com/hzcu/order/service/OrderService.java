package com.hzcu.order.service;

import com.hzcu.order.entity.Order;
import com.hzcu.order.entity.OrderItem;
import com.hzcu.order.entity.User;
import com.hzcu.order.entity.Canteen;
import com.hzcu.order.entity.OrderStatusLog;
import com.hzcu.order.repository.OrderRepository;
import com.hzcu.order.repository.OrderItemRepository;
import com.hzcu.order.repository.OrderStatusLogRepository;
import com.hzcu.order.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderStatusLogRepository orderStatusLogRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Order createOrder(Order order, List<OrderItem> items) {
        order.setOrderNo(generateOrderNo());
        order.setStatus("PENDING_PAYMENT");
        order.setCreatedAt(LocalDateTime.now());

        // Prevent cascade saving of items with null order_id
        order.setItems(null);

        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : items) {
            item.setOrder(savedOrder);
            
            // 确保菜品名称不为null，如果为null则通过dish_id查询
            String dishName = null;
            if (item.getDish() != null && item.getDish().getName() != null) {
                dishName = item.getDish().getName();
            } else if (item.getDish() != null && item.getDish().getDishId() != null) {
                // 如果name为null但dishId存在，从数据库查询完整的dish信息
                Optional<com.hzcu.order.entity.Dish> dishOpt = dishRepository.findById(item.getDish().getDishId());
                if (dishOpt.isPresent()) {
                    dishName = dishOpt.get().getName();
                    item.setDish(dishOpt.get()); // 更新为完整的dish对象
                }
            }
            item.setDishName(dishName); // 设置菜品名称快照
            
            if (item.getTotalPrice() == null) {
                // simple calc fallback
                item.setTotalPrice(item.getUnitPrice().multiply(new java.math.BigDecimal(item.getQuantity())));
            }
            orderItemRepository.save(item);
        }
        savedOrder.setItems(items); // Set back for return

        logStatusChange(savedOrder, null, "PENDING_PAYMENT", "SYSTEM", 0L, "Order created");
        return savedOrder;
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Order> getOrdersByCanteen(Canteen canteen) {
        return orderRepository.findByCanteenOrderByCreatedAtDesc(canteen);
    }

    public List<Order> getOrdersByCanteenAndStatus(Canteen canteen, String status) {
        return orderRepository.findByCanteenAndStatus(canteen, status);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public void updateOrderStatus(Order order, String newStatus, String operatorType, Long operatorId, String remark) {
        String fromStatus = order.getStatus();
        order.setStatus(newStatus);

        // Assign pickup code when paid
        if ("PAID".equals(newStatus) && (order.getPickupCode() == null || order.getPickupCode().isEmpty())) {
            order.setPickupCode(generatePickupCode(order.getCanteen()));
        }

        orderRepository.save(order);

        logStatusChange(order, fromStatus, newStatus, operatorType, operatorId, remark);

        // Notify user
        notificationService.sendOrderStatusNotification(order.getUser().getUserId(), newStatus, order.getOrderNo());
    }

    private String generatePickupCode(Canteen canteen) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<String> codes = orderRepository.findPickupCodesByCanteenSince(canteen, startOfDay);

        int maxCode = 100;
        for (String code : codes) {
            try {
                if (code.matches("\\d+")) {
                    int val = Integer.parseInt(code);
                    if (val > maxCode) {
                        maxCode = val;
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }

        return String.valueOf(maxCode + 1);
    }

    private void logStatusChange(Order order, String fromStatus, String toStatus, String operatorType, Long operatorId,
            String remark) {
        OrderStatusLog log = OrderStatusLog.builder()
                .order(order)
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .operatorType(operatorType)
                .operatorId(operatorId)
                .remark(remark)
                .build();
        orderStatusLogRepository.save(log);
    }

    private String generateOrderNo() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
