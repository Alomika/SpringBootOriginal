package com.example.springboot1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
public class Cuisine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String ingredients;
    private Double price;
    private boolean spicy;
    private boolean vegan;
@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
@JsonIgnore
    @ManyToMany(mappedBy = "cuisineList")
    private List<FoodOrder> foodOrders;

    public Cuisine() { }

    public Cuisine(String name, String ingredients, Double price, boolean spicy, boolean vegan, Restaurant restaurant) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.spicy = spicy;
        this.vegan = vegan;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return name + " - " + ingredients + " $" + price;
    }
}
