package com.hzcu.order.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CanteenSystemTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void 全链路_首页与列表接口可用() {
        String base = "http://localhost:" + port + "/api/v1";
        ResponseEntity<String> feed = restTemplate.getForEntity(base + "/home/feed", String.class);
        assertEquals(200, feed.getStatusCode().value());
        assertTrue(feed.getBody() != null && feed.getBody().contains("\"code\":\"0\""));

        ResponseEntity<String> list = restTemplate.getForEntity(base + "/canteens", String.class);
        assertEquals(200, list.getStatusCode().value());
        assertTrue(list.getBody() != null && list.getBody().contains("\"code\":\"0\""));
    }
}
