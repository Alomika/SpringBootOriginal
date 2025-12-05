package com.example.springboot1.repositories;

import com.example.springboot1.model.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicUserRepo extends JpaRepository<BasicUser, Integer> {
    BasicUser getBasicUserById(Integer id);
    BasicUser getBasicUserByName(String name);
    BasicUser getBasicUserByLoginAndPassword(String login, String password);

    Boolean getBasicUserByLogin(String login);
}
