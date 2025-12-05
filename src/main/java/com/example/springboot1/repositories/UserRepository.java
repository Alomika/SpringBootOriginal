package com.example.springboot1.repositories;

import com.example.springboot1.model.BasicUser;
import com.example.springboot1.model.Driver;
import com.example.springboot1.model.Restaurant;
import com.example.springboot1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByLoginAndPassword(String login, String password);
}
