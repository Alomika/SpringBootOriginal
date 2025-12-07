package com.example.springboot1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int rating;
    private String reviewText;
    @Column(columnDefinition = "DATE")
    private LocalDate dateCreated;
    @ManyToOne
    private BasicUser commentOwner;

    @ManyToOne
    private BasicUser feedbackUser;
    @ManyToOne
    private Chat chat;

    public Review(String reviewText, BasicUser commentOwner, Chat chat) {
        this.reviewText = reviewText;
        this.commentOwner = commentOwner;
        this.chat = chat;
    }
    public Review(int rating, String reviewText, BasicUser commentOwner, BasicUser feedbackUser) {
        this.rating = rating;
        this.reviewText = reviewText;
        this.commentOwner = commentOwner;
        this.feedbackUser = feedbackUser;
    }
    @Override
    public String toString() {
        return commentOwner.getName() + " " + commentOwner.getSurname() + ": " + reviewText;
    }

}
