package com.example.springboot1.repositories;

import com.example.springboot1.model.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodOrderRepo extends JpaRepository<FoodOrder, Integer> {
    FoodOrder getFoodOrderById(Integer id);
    FoodOrder getFoodOrderByBuyer_Id(Integer userid);
    FoodOrder getFoodOrderByDriver_Id(Integer userid);
    @Query("SELECT DISTINCT o FROM FoodOrder o LEFT JOIN FETCH o.cuisineList WHERE o.buyer.id = :buyerId")
    List<FoodOrder> findByBuyerIdWithCuisines(@Param("buyerId") Integer buyerId);

    List<FoodOrder> findByDriverId(Integer driverId);
}
