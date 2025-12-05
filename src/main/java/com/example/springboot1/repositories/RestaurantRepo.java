package com.example.springboot1.repositories;

import com.example.springboot1.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepo extends JpaRepository<Restaurant, Integer> {
    Restaurant getRestaurantById(Integer id);
    Restaurant getRestaurantByName(String name);
}
