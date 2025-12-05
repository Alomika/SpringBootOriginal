package com.example.springboot1.repositories;

import com.example.springboot1.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Integer> {
    Review getReviewById(Integer id); // ok
    List<Review> findByChat_Id(Integer chatId);

    List<Review> findByCommentOwner_Id(int userId);

    List<Review> findByFeedbackUser_Id(int userId);
}
