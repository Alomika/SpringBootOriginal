package com.example.springboot1.controllers;

import com.example.springboot1.model.*;
import com.example.springboot1.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FoodOrderController {
@Autowired
    private FoodOrderRepo foodOrderRepo;
@Autowired
private DriverRepo driverRepo;
@Autowired
private RestaurantRepo restaurantRepo;
@Autowired
private BasicUserRepo basicUserRepo;
@Autowired
private CuisineRepo cuisineRepo;

    // Get all orders
    @GetMapping("/allOrders")
    public List<FoodOrder> getAllOrders() {
        return foodOrderRepo.findAll();
    }

    // Get order by ID
    @GetMapping("/order/{id}")
    public FoodOrder getOrderById(@PathVariable Integer id) {
        return foodOrderRepo.getFoodOrderById(id);
    }

    @GetMapping("/order/{orderId}/cuisines")
    public List<Cuisine> getCuisinesForOrder(@PathVariable Integer orderId) {
        FoodOrder order = foodOrderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        List<Cuisine> cuisines = order.getCuisineList();
        cuisines.size();

        return cuisines;
    }

    @GetMapping("/buyer/{buyerId}")
    public List<FoodOrder> getOrdersByBuyer(@PathVariable Integer buyerId) {
        return foodOrderRepo.findByBuyerIdWithCuisines(buyerId);
    }



    // Get orders by driver ID
    @GetMapping("/driver/{driverId}")
    public List<FoodOrder> getOrdersByDriver(@PathVariable Integer driverId) {
        return foodOrderRepo.findAll().stream()
                .filter(order -> order.getDriver() != null && order.getDriver().getId() == driverId)
                .toList();
    }
    @PostMapping("/insertOrder")
    public FoodOrder createOrder(@RequestBody OrderRequest request) {

        BasicUser buyer = basicUserRepo.findById(request.getBuyerId())
                .orElseThrow(() -> new RuntimeException("Buyer not found: " + request.getBuyerId()));
        Restaurant restaurant = restaurantRepo.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found: " + request.getRestaurantId()));

        Driver driver = null;
        if (request.getDriverId() != null) {
            driver = driverRepo.findById(request.getDriverId()).orElse(null);
        }

        List<Cuisine> cuisines = new ArrayList<>();
        if (request.getCuisineIds() != null) {
            for (Integer id : request.getCuisineIds()) {
                cuisines.add(cuisineRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Cuisine not found: " + id)));
            }
        }

        FoodOrder order = new FoodOrder();
        order.setName(request.getName());
        order.setPrice(request.getPrice());
        order.setBuyer(buyer);
        order.setRestaurant(restaurant);
        order.setDriver(driver);
        order.setCuisineList(cuisines);
        order.setOrderStatus(OrderStatus.PENDING); // set default status
        order.setDateCreated(LocalDate.now());
        order.setDateUpdated(LocalDate.now());

        FoodOrder saved = foodOrderRepo.save(order);
        System.out.println("Saved order ID: " + saved.getId());
        return saved;
    }








    // Optional: Update order
    @PutMapping("updateOrder/{id}")
    public FoodOrder updateOrder(@PathVariable Integer id, @RequestBody FoodOrder orderDetails) {
        FoodOrder order = foodOrderRepo.getFoodOrderById(id);
        if (order != null) {
            order.setName(orderDetails.getName());
            order.setPrice(orderDetails.getPrice());
            order.setOrderStatus(orderDetails.getOrderStatus());
            order.setDriver(orderDetails.getDriver());
            return foodOrderRepo.save(order);
        }
        return null;
    }

    // Optional: Delete order
    @DeleteMapping("deleteOrder/{id}")
    public void deleteOrder(@PathVariable Integer id) {
        foodOrderRepo.deleteById(id);
    }
}