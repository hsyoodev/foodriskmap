package com.hangaramit.foodriskmap.repository;

import com.hangaramit.foodriskmap.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

}
