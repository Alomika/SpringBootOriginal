package com.example.springboot1.repositories;

import com.example.springboot1.model.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuisineRepo extends JpaRepository<Cuisine, Integer> {
    List<Cuisine> findCuisineByRestaurantId(int restaurantId);
    List<Cuisine> findByFoodOrders_Id(int foodOrderId);

}
