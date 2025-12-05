package com.example.springboot1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Double price;
    @ManyToOne
    private BasicUser buyer;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "foodorder_cuisine",
            joinColumns = @JoinColumn(name = "foodorder_id"),
            inverseJoinColumns = @JoinColumn(name = "cuisine_id")
    )
    private List<Cuisine> cuisineList;
    @OneToOne
    private Chat chat;
    @ManyToOne
    private Driver driver;
    @ManyToOne
    private Restaurant restaurant;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;

    public FoodOrder(String name, Double price, BasicUser buyer, Restaurant restaurant, Driver driver) {
        this.name = name;
        this.price = price;
        this.buyer = buyer;
        this.restaurant = restaurant;
        this.driver = driver;
    }

    public FoodOrder(String name, Double price, BasicUser buyer, List<Cuisine> cuisineList, Restaurant restaurant, OrderStatus orderStatus, Driver driver) {
        this.name = name;
        this.price = price;
        this.buyer = buyer;
        this.cuisineList = cuisineList;
        this.restaurant = restaurant;
        this.orderStatus = orderStatus;
        this.driver = driver;
    }

    @Override
    public String toString() {
        return name + " Price: " + price + " Status: " + orderStatus;
    }
}
