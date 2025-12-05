package com.example.springboot1.repositories;

import com.example.springboot1.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepo extends JpaRepository<Driver, Integer> {
    Driver getDriverById(Integer id);
    Driver getDriverByName(String name);
    Driver getDriverByLoginAndPassword(String login, String password);
}
