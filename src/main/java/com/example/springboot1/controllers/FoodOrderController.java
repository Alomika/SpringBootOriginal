package com.example.springboot1.controllers;

import com.example.springboot1.OrderStatusUpdateRequest;
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
    @Autowired
    private ChatRepo chatRepo;
    @Autowired
    private FoodOrderRepo foodOrderRepository;

    @GetMapping("/{driverId}/orders")
    public List<FoodOrder> getOrdersByDriver(@PathVariable Integer driverId) {
        return foodOrderRepository.findByDriverId(driverId);
    }

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
    @PutMapping("/updateOrder/{id}")
    public FoodOrder updateOrder(@PathVariable Integer id, @RequestBody OrderUpdateRequest request) {
        FoodOrder order = foodOrderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Update status if provided
        if (request.getStatus() != null) {
            order.setOrderStatus(request.getStatus());
        }

        // Update driver if provided
        if (request.getDriverId() != null) {
            Driver driver = driverRepo.findById(request.getDriverId())
                    .orElseThrow(() -> new RuntimeException("Driver not found with id: " + request.getDriverId()));
            order.setDriver(driver);
        }

        // Update name if provided
        if (request.getName() != null && !request.getName().isEmpty()) {
            order.setName(request.getName());
        }

        // Update price if provided
        if (request.getPrice() != null) {
            order.setPrice(request.getPrice());
        }

        // Always update the dateUpdated
        order.setDateUpdated(LocalDate.now());

        return foodOrderRepo.save(order);
    }
    @PostMapping("/order/{orderId}/chat")
    public FoodOrder createChatForOrder(@PathVariable Integer orderId, @RequestBody ChatRequest request) {
        FoodOrder order = foodOrderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // If chat already exists, return order as-is
        if (order.getChat() != null) {
            return order;
        }

        // Create new chat
        Chat chat = new Chat();
        chat.setName(request.getName());
        chat.setDateCreated(LocalDate.now());

        Chat savedChat = chatRepo.save(chat);

        // Update order to reference chat
        order.setChat(savedChat);
        order.setDateUpdated(LocalDate.now());
        return foodOrderRepo.save(order);
    }







    @PutMapping("/order/{id}/status")
    public FoodOrder updateOrderStatus(@PathVariable Integer id, @RequestBody OrderStatusUpdateRequest request) {

        FoodOrder order = foodOrderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(request.getStatus());
        order.setDateUpdated(LocalDate.now());

        return foodOrderRepo.save(order);
    }


    // Optional: Delete order
    @DeleteMapping("deleteOrder/{id}")
    public void deleteOrder(@PathVariable Integer id) {
        foodOrderRepo.deleteById(id);
    }
}