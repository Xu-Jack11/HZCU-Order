package com.hzcu.order.service;

import com.hzcu.order.entity.Canteen;
import com.hzcu.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private com.hzcu.order.repository.UserRepository userRepository;

    @Autowired
    private com.hzcu.order.repository.CanteenRepository canteenRepository;

    public Map<String, Object> getCanteenStats(Canteen canteen, Integer days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

        long orderCount = orderRepository.countByCanteenAndStatusSince(canteen, "COMPLETED", since);
        BigDecimal totalRevenue = orderRepository.sumAmountByCanteenSince(canteen, since);
        if (totalRevenue == null)
            totalRevenue = BigDecimal.ZERO;

        long todayOrderCount = orderRepository.countByCanteenAndStatusSince(canteen, "COMPLETED", todayStart);
        BigDecimal todayRevenue = orderRepository.sumAmountByCanteenSince(canteen, todayStart);
        if (todayRevenue == null)
            todayRevenue = BigDecimal.ZERO;

        List<Object[]> topDishes = orderRepository.findTopSellingDishes(canteen);
        List<Map<String, Object>> topDishesList = topDishes.stream()
                .limit(5)
                .map(obj -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("dishName", obj[0]);
                    map.put("quantity", obj[1]);
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> stats = new HashMap<>();
        stats.put("orderCount", orderCount);
        stats.put("totalRevenue", totalRevenue);
        stats.put("todayOrderCount", todayOrderCount);
        stats.put("todayRevenue", todayRevenue);
        stats.put("averageRating", 4.8); // Placeholder since rating is not yet implemented
        stats.put("topDishes", topDishesList);

        return stats;
    }

    public Map<String, Object> getPlatformSummary() {
        LocalDateTime sinceToday = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime sinceYesterday = sinceToday.minusDays(1);

        long totalUsers = userRepository.count();
        long activeCanteens = canteenRepository.countByStatus(1);
        BigDecimal todayRevenue = orderRepository.sumAmountSince(sinceToday);
        if (todayRevenue == null)
            todayRevenue = BigDecimal.ZERO;

        BigDecimal yesterdayRevenue = orderRepository.sumAmountSince(sinceYesterday).subtract(todayRevenue);
        if (yesterdayRevenue == null)
            yesterdayRevenue = BigDecimal.ZERO;

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalUsers", totalUsers);
        summary.put("activeCanteens", activeCanteens);
        summary.put("todayRevenue", todayRevenue);
        summary.put("yesterdayRevenue", yesterdayRevenue);
        summary.put("revenueGrowth", calculateGrowth(todayRevenue, yesterdayRevenue));

        return summary;
    }

    private double calculateGrowth(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0)
            return 0.0;
        return current.subtract(previous)
                .divide(previous, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .doubleValue();
    }
}
