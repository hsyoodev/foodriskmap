package com.hangaramit.foodriskmap.service;

import com.hangaramit.foodriskmap.entity.Restaurant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class RestaurantServiceTests {

    @Autowired
    RestaurantService restaurantService;

    @DisplayName("업소 추가")
    @Test
//    @Transactional
    void add() {
        restaurantService.add();
    }

    @DisplayName("업소 조회")
    @Test
    @Transactional
    void index() {
        List<Restaurant> restaurants = restaurantService.index();
    }
}
