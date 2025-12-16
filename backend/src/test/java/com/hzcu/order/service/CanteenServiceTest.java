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
}
