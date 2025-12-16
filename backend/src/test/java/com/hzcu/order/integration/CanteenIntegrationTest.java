package com.hzcu.order.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CanteenIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homeFeed_返回成功包裹结构() throws Exception {
        mockMvc.perform(get("/home/feed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("0"))
            .andExpect(jsonPath("$.data.banners").exists())
            .andExpect(jsonPath("$.data.recommendCanteens").exists());
    }

    @Test
    void canteens_列表接口返回成功() throws Exception {
        mockMvc.perform(get("/canteens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("0"))
                .andExpect(jsonPath("$.data.total").exists())
                .andExpect(jsonPath("$.data.list").exists());
    }
}
