package com.hangaramit.foodriskmap.repository;

import com.hangaramit.foodriskmap.entity.Restaurant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    List<Restaurant> findByLatIsNotNullAndLngIsNotNull();
}
