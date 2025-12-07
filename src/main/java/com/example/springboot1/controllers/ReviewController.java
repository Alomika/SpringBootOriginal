package com.example.springboot1.controllers;

import com.example.springboot1.model.BasicUser;
import com.example.springboot1.model.FoodOrder;
import com.example.springboot1.model.Review;
import com.example.springboot1.repositories.BasicUserRepo;
import com.example.springboot1.repositories.FoodOrderRepo;
import com.example.springboot1.repositories.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class ReviewController {

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private BasicUserRepo basicUserRepo;

    @Autowired
    private FoodOrderRepo foodOrderRepo;

    @GetMapping("allReviews")
    public CollectionModel<EntityModel<Review>> getAll() {
        List<EntityModel<Review>> reviews = reviewRepo.findAll().stream()
                .map(review -> EntityModel.of(review,
                        linkTo(methodOn(ReviewController.class).getReviewById(review.getId())).withSelfRel(),
                        linkTo(methodOn(ReviewController.class).getAll()).withRel("allReviews")))
                .collect(Collectors.toList());

        return CollectionModel.of(reviews,
                linkTo(methodOn(ReviewController.class).getAll()).withSelfRel());
    }

    @GetMapping("/review/{id}")
    public EntityModel<Review> getReviewById(@PathVariable int id) {
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        return EntityModel.of(review,
                linkTo(methodOn(ReviewController.class).getReviewById(id)).withSelfRel(),
                linkTo(methodOn(ReviewController.class).getAll()).withRel("allReviews"));
    }

    @GetMapping("/chat/{chatId}")
    public CollectionModel<EntityModel<Review>> getMessagesByChatId(@PathVariable int chatId) {
        List<EntityModel<Review>> reviews = reviewRepo.findByChatId(chatId).stream()
                .map(review -> EntityModel.of(review,
                        linkTo(methodOn(ReviewController.class).getReviewById(review.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(reviews,
                linkTo(methodOn(ReviewController.class).getMessagesByChatId(chatId)).withSelfRel());
    }

    @GetMapping("/user/{userId}/comments")
    public CollectionModel<EntityModel<Review>> getReviewsByCommentOwner(@PathVariable int userId) {
        List<EntityModel<Review>> reviews = reviewRepo.findByCommentOwnerId(userId).stream()
                .map(review -> EntityModel.of(review,
                        linkTo(methodOn(ReviewController.class).getReviewById(review.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(reviews,
                linkTo(methodOn(ReviewController.class).getReviewsByCommentOwner(userId)).withSelfRel());
    }

    @GetMapping("/user/{userId}/feedbacks")
    public CollectionModel<EntityModel<Review>> getReviewsByFeedbackUser(@PathVariable int userId) {
        List<EntityModel<Review>> reviews = reviewRepo.findByFeedbackUser_Id(userId).stream()
                .map(review -> EntityModel.of(review,
                        linkTo(methodOn(ReviewController.class).getReviewById(review.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(reviews,
                linkTo(methodOn(ReviewController.class).getReviewsByFeedbackUser(userId)).withSelfRel());
    }

    @PostMapping("/order/{orderId}/review")
    public EntityModel<Review> createReviewForOrder(@PathVariable Integer orderId, @RequestBody ReviewRequest request) {
        FoodOrder order = foodOrderRepo.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        BasicUser commentOwner = basicUserRepo.findById(request.getCommentOwnerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment owner not found"));
        BasicUser feedbackUser = basicUserRepo.findById(request.getFeedbackUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback user not found"));
        Review review = new Review();
        review.setRating(request.getRating());
        review.setReviewText(request.getReviewText());
        review.setCommentOwner(commentOwner);
        review.setFeedbackUser(feedbackUser);
        review.setDateCreated(LocalDate.now());

        Review saved = reviewRepo.save(review);

        return EntityModel.of(saved,
                linkTo(methodOn(ReviewController.class).getReviewById(saved.getId())).withSelfRel(),
                linkTo(methodOn(ReviewController.class).getAll()).withRel("allReviews"));
    }

    @PostMapping("insertReview")
    public EntityModel<Review> insertReview(@RequestBody Review review) {
        Review saved = reviewRepo.save(review);
        return EntityModel.of(saved,
                linkTo(methodOn(ReviewController.class).getReviewById(saved.getId())).withSelfRel(),
                linkTo(methodOn(ReviewController.class).getAll()).withRel("allReviews"));
    }

    @PutMapping("/review/update/{id}")
    public EntityModel<Review> updateMessage(@PathVariable int id, @RequestBody Review reviewDetails) {
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        review.setReviewText(reviewDetails.getReviewText());
        Review updated = reviewRepo.save(review);
        return EntityModel.of(updated,
                linkTo(methodOn(ReviewController.class).getReviewById(updated.getId())).withSelfRel(),
                linkTo(methodOn(ReviewController.class).getAll()).withRel("allReviews"));
    }

    @DeleteMapping("/deleteReview/{id}")
    public EntityModel<String> deleteReview(@PathVariable int id) {
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        reviewRepo.delete(review);
        String message = "Deleted message with id " + id;
        return EntityModel.of(message,
                linkTo(methodOn(ReviewController.class).getAll()).withRel("allReviews"));
    }
}