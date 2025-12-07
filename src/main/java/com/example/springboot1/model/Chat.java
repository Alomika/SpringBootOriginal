package com.example.springboot1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String chatText;
    private LocalDate dateCreated;
    @JsonIgnore
    @OneToOne(mappedBy = "chat", cascade = CascadeType.ALL)
    private FoodOrder foodOrder;
    @JsonIgnore
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> messages;

    public Chat(String name, FoodOrder foodOrder) {
        this.name = name;
        this.foodOrder = foodOrder;
        this.dateCreated = LocalDate.now();
        this.messages = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatText() {
        return chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public FoodOrder getFoodOrder() {
        return foodOrder;
    }

    public void setFoodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
    }

    public List<Review> getMessages() {
        return messages;
    }

    public void setMessages(List<Review> messages) {
        this.messages = messages;
    }

    public Chat(String name, LocalDate dateCreated) {
        this.name = name;
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        String orderName = (foodOrder != null) ? foodOrder.getName() : "No order";
        return "Chat: " + orderName + " with " + name;
    }

}
