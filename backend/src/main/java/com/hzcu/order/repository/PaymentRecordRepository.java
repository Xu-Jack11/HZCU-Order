package com.hzcu.order.repository;

import com.hzcu.order.entity.PaymentRecord;
import com.hzcu.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Optional<PaymentRecord> findByOrder(Order order);

    Optional<PaymentRecord> findByPayNo(String payNo);
}
