package com.hangaramit.foodriskmap.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class RestaurantServiceTests {

    @Autowired
    RestaurantService restaurantService;

    @Test
    @Transactional
    void add() {
        restaurantService.add();
    }
}
