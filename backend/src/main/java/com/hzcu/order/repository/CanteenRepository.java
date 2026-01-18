package com.hzcu.order.repository;

import com.hzcu.order.entity.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanteenRepository extends JpaRepository<Canteen, Long> {
    List<Canteen> findByStatus(Integer status);

    List<Canteen> findByStatusOrderBySortOrderAsc(Integer status);

    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM canteen WHERE is_deleted = 0 OR is_deleted IS NULL ORDER BY sort_order ASC", nativeQuery = true)
    List<Canteen> findAllNotDeleted();

    List<Canteen> findByCampus(String campus);

    long countByStatus(Integer status);
}
