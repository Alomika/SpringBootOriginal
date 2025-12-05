package com.example.springboot1.repositories;

import com.example.springboot1.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Integer> {
    Chat getChatById(Integer id);
    List<Chat> findByFoodOrderIdOrderByDateCreatedAsc(Integer orderId);
}
