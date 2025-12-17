package com.hzcu.order.service;

import com.hzcu.order.common.PageResult;
import com.hzcu.order.data.DataStore;
import com.hzcu.order.model.Shop;
import com.hzcu.order.repository.DishJdbcRepository;
import com.hzcu.order.repository.ShopJdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;

import static org.junit.jupiter.api.Assertions.*;

class CanteenServiceTest {
    private CanteenService service;

    @BeforeEach
    void setUp() {
        DataStore dataStore = new DataStore();
        // 提供一个简单的 ObjectProvider 桩实现，避免 Mockito 依赖
        ObjectProvider<ShopJdbcRepository> shopProvider = new ObjectProvider<ShopJdbcRepository>() {
            @Override public ShopJdbcRepository getObject(Object... args) { return null; }
            @Override public ShopJdbcRepository getIfAvailable() { return null; }
            @Override public ShopJdbcRepository getIfUnique() { return null; }
            @Override public ShopJdbcRepository getObject() { return null; }
        };
        // 该测试不调用菜品查询逻辑，传入 null 安全占位的 DishJdbcRepository
        DishJdbcRepository dishRepo = new DishJdbcRepository(null);
        service = new CanteenService(dataStore, shopProvider, dishRepo);
    }

    @Test
    void 查询食堂分页返回结构正常() {
        PageResult<Shop> result = service.queryCanteens(1, 20, null, null, null);
        assertNotNull(result);
        assertNotNull(result.getList());
        assertTrue(result.getTotal() >= 0);
    }

    @Test
    void 查不到食堂时_getShop抛出404() {
        // provider 返回 null，按实现将直接抛出 NOT_FOUND
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.getShop(123L));
    }

    @Test
    void 过滤与排序_按关键词和hot排序并分页() {
        // 构造一个返回固定数据的仓储桩
        java.util.List<Shop> all = new java.util.ArrayList<>();
        all.add(buildShop(1L, "A", 4.0, 10, "1.5km", java.util.List.of("noodles")));
        all.add(buildShop(2L, "B", 4.8, 50, "0.8km", java.util.List.of("spicy")));
        all.add(buildShop(3L, "C", 4.6, 30, "2.0km", java.util.List.of("spicy-special")));

        ShopJdbcRepository stubRepo = new ShopJdbcRepository(null) {
            @Override public java.util.List<Shop> findAll() { return all; }
            @Override public Shop findById(long id) { return all.stream().filter(s -> s.getId()==id).findFirst().orElse(null); }
        };

        ObjectProvider<ShopJdbcRepository> provider = new ObjectProvider<ShopJdbcRepository>() {
            @Override public ShopJdbcRepository getObject(Object... args) { return stubRepo; }
            @Override public ShopJdbcRepository getIfAvailable() { return stubRepo; }
            @Override public ShopJdbcRepository getIfUnique() { return stubRepo; }
            @Override public ShopJdbcRepository getObject() { return stubRepo; }
        };

        CanteenService svc = new CanteenService(new DataStore(), provider, new DishJdbcRepository(null));
        // 关键词 spicy 命中 id=2,3；hot 按月售降序应为 [2,3]
        PageResult<Shop> page = svc.queryCanteens(1, 2, "spicy", null, "hot");
        assertEquals(2, page.getTotal());
        assertEquals(2, page.getList().size());
        assertEquals(2L, page.getList().get(0).getId());
        assertEquals(3L, page.getList().get(1).getId());
    }

    private static Shop buildShop(long id, String name, double rating, int monthly, String distance, java.util.List<String> tags) {
        Shop s = new Shop();
        s.setId(id);
        s.setName(name);
        s.setRating(rating);
        s.setMonthlySales(monthly);
        s.setDistance(distance);
        s.setTags(tags);
        s.setCategoryIds(java.util.List.of());
        return s;
    }
}
