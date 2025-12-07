package com.example.springboot1.controllers;

import com.example.springboot1.model.BasicUser;
import com.example.springboot1.model.Chat;
import com.example.springboot1.model.Review;
import com.example.springboot1.model.User;
import com.example.springboot1.repositories.BasicUserRepo;
import com.example.springboot1.repositories.ChatRepo;
import com.example.springboot1.repositories.ReviewRepo;
import com.example.springboot1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatMessageController {

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private BasicUserRepo basicUserRepo;
    @GetMapping("/reviews/simple/chat/{chatId}")
    public ResponseEntity<List<SimpleReviewDto>> getSimpleReviewsByChatId(@PathVariable int chatId) {
        List<Review> reviews = reviewRepo.findByChatId(chatId);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<SimpleReviewDto> reviewsDto = reviews.stream()
                .map(r -> new SimpleReviewDto(
                        r.getDateCreated().toString(),
                        r.getReviewText(),
                        r.getCommentOwner() != null ? r.getCommentOwner().getId() : null,
                        r.getChat().getId()))
                .sorted(Comparator.comparing(SimpleReviewDto::getDateCreated))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviewsDto);
    }
    @PostMapping("/reviews/simple/chat/{chatId}")
    public ResponseEntity<SimpleReviewDto> sendSimpleReview(
            @PathVariable int chatId,
            @RequestBody SimpleReviewDto reviewDto
    ) {
        if (reviewDto.getCommentOwnerId() == 0 || reviewDto.getCommentOwnerId() == 0) {
            return ResponseEntity.badRequest().body(null);
        }
        if (reviewDto.getReviewText() == null || reviewDto.getReviewText().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Review review = new Review();
        review.setDateCreated(LocalDate.now());
        review.setReviewText(reviewDto.getReviewText());
        review.setCommentOwner(basicUserRepo.findById(reviewDto.getCommentOwnerId()).orElse(null));
        review.setChat(chatRepo.findById(chatId).orElse(null));

        Review saved = reviewRepo.save(review);

        SimpleReviewDto dto = new SimpleReviewDto(
                saved.getDateCreated().toString(),
                saved.getReviewText(),
                saved.getCommentOwner() != null ? saved.getCommentOwner().getId() : null,
                saved.getChat().getId()
        );

        return ResponseEntity.ok(dto);
    }
    @GetMapping("/reviews/simple/user/{commentOwnerId}")
    public ResponseEntity<List<SimpleReviewDto>> getSimpleReviewsByUserId(@PathVariable int commentOwnerId) {
        List<Review> reviews = reviewRepo.findByCommentOwnerId(commentOwnerId);

        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<SimpleReviewDto> reviewsDto = reviews.stream()
                .map(r -> new SimpleReviewDto(
                        r.getDateCreated() != null ? r.getDateCreated().toString() : null,
                        r.getReviewText(),
                        r.getCommentOwner() != null ? r.getCommentOwner().getId() : 0,
                        r.getChat() != null ? r.getChat().getId() : 0  // <-- handle null chat
                ))
                .sorted(Comparator.comparing(SimpleReviewDto::getDateCreated))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviewsDto);
    }

}
