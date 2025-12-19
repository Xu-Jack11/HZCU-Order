package com.hzcu.order.repository;

import com.hzcu.order.entity.Order;
import com.hzcu.order.entity.User;
import com.hzcu.order.entity.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
        Optional<Order> findByOrderNo(String orderNo);

        List<Order> findByUserOrderByCreatedAtDesc(User user);

        List<Order> findByCanteenOrderByCreatedAtDesc(Canteen canteen);

        List<Order> findByCanteenAndStatus(Canteen canteen, String status);

        Optional<Order> findFirstByCanteenAndCreatedAtAfterOrderByCreatedAtDesc(Canteen canteen, LocalDateTime since);

        @Query("SELECT COUNT(o) FROM Order o WHERE o.canteen = :canteen AND o.status = :status AND o.createdAt >= :since")
        long countByCanteenAndStatusSince(@Param("canteen") Canteen canteen, @Param("status") String status,
                        @Param("since") LocalDateTime since);

        @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.canteen = :canteen AND o.status = 'COMPLETED' AND o.createdAt >= :since")
        BigDecimal sumAmountByCanteenSince(@Param("canteen") Canteen canteen, @Param("since") LocalDateTime since);

        @Query("SELECT oi.dishName as dishName, SUM(oi.quantity) as quantity " +
                        "FROM OrderItem oi JOIN oi.order o " +
                        "WHERE o.canteen = :canteen AND o.status = 'COMPLETED' " +
                        "GROUP BY oi.dishName ORDER BY quantity DESC")
        List<Object[]> findTopSellingDishes(@Param("canteen") Canteen canteen);

        @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status AND o.createdAt >= :since")
        long countByStatusSince(@Param("status") String status, @Param("since") LocalDateTime since);

        @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'COMPLETED' AND o.createdAt >= :since")
        BigDecimal sumAmountSince(@Param("since") LocalDateTime since);

        @Query("SELECT oi.dishName as dishName, SUM(oi.quantity) as quantity " +
                        "FROM OrderItem oi JOIN oi.order o " +
                        "WHERE o.status = 'COMPLETED' " +
                        "GROUP BY oi.dishName ORDER BY quantity DESC")
        List<Object[]> findTopSellingDishesGlobal();
}
