package com.example.springboot1.controllers;

import com.example.springboot1.model.Chat;
import com.example.springboot1.model.OrderStatus;

public class OrderUpdateRequest {
    private OrderStatus status;
    private Integer driverId;
    private String name;
    private Double price;
    private Chat chat;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public OrderUpdateRequest() {
    }

    public OrderUpdateRequest(OrderStatus status, Integer driverId, String name, Double price) {
        this.status = status;
        this.driverId = driverId;
        this.name = name;
        this.price = price;
    }

    // Getters and Setters
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}