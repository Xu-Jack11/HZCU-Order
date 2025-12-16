package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.common.PageResult;
import com.hzcu.order.data.DataStore;
import com.hzcu.order.model.Shop;
import com.hzcu.order.repository.DishJdbcRepository;
import com.hzcu.order.repository.ShopJdbcRepository;
import com.hzcu.order.service.CanteenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CanteenControllerTest {

    @Test
    void 获取食堂列表返回200() throws Exception {
        // 使用独立构造的 MockMvc，避免 Mockito/ByteBuddy 与 JDK 版本不兼容
        DataStore dataStore = new DataStore();
        // 手动触发初始化数据
        try {
            java.lang.reflect.Method m = DataStore.class.getDeclaredMethod("init");
            m.setAccessible(true);
            m.invoke(dataStore);
        } catch (Exception ignore) {}

        // 提供一个返回 null 的简单 ObjectProvider，促使 Service 从数据库读取失败时返回空列表
        ObjectProvider<ShopJdbcRepository> provider = new ObjectProvider<ShopJdbcRepository>() {
            @Override public ShopJdbcRepository getObject(Object... args) { return null; }
            @Override public ShopJdbcRepository getIfAvailable() { return null; }
            @Override public ShopJdbcRepository getIfUnique() { return null; }
            @Override public ShopJdbcRepository getObject() { return null; }
        };

        // DishJdbcRepository 不在该接口调用路径中使用，传入 null 安全占位
        DishJdbcRepository dishRepo = new DishJdbcRepository(null);

        CanteenService canteenService = new CanteenService(dataStore, provider, dishRepo);
        CanteenController controller = new CanteenController(canteenService, dataStore);

        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller)
                // 统一返回体包装，保证序列化正常
                .setControllerAdvice()
                .build();

        mvc.perform(get("/canteens"))
                .andExpect(status().isOk());
    }
}
