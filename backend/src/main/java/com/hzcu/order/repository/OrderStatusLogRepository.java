package com.hzcu.order.repository;

import com.hzcu.order.entity.OrderStatusLog;
import com.hzcu.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusLogRepository extends JpaRepository<OrderStatusLog, Long> {
    List<OrderStatusLog> findByOrderOrderByCreatedAtDesc(Order order);
}
