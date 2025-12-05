package com.example.springboot1.controllers;

import com.example.springboot1.model.Chat;
import com.example.springboot1.repositories.ChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class ChatController {

    @Autowired
    private ChatRepo chatRepo;

    @GetMapping("/allChats")
    public CollectionModel<EntityModel<Chat>> getAll() {
        List<EntityModel<Chat>> chats = chatRepo.findAll().stream()
                .map(chat -> EntityModel.of(chat,
                        linkTo(methodOn(ChatController.class).getChatById(chat.getId())).withSelfRel(),
                        linkTo(methodOn(ChatController.class).getAll()).withRel("allChats")))
                .collect(Collectors.toList());

        return CollectionModel.of(chats,
                linkTo(methodOn(ChatController.class).getAll()).withSelfRel());
    }

    @GetMapping("/chat/{id}")
    public EntityModel<Chat> getChatById(@PathVariable int id) {
        Chat chat = chatRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
        return EntityModel.of(chat,
                linkTo(methodOn(ChatController.class).getChatById(id)).withSelfRel(),
                linkTo(methodOn(ChatController.class).getAll()).withRel("allChats"));
    }

    @GetMapping("/order/{orderId}")
    public CollectionModel<EntityModel<Chat>> getChatByOrderId(@PathVariable int orderId) {
        List<EntityModel<Chat>> chats = chatRepo.findByFoodOrderIdOrderByDateCreatedAsc(orderId).stream()
                .map(chat -> EntityModel.of(chat,
                        linkTo(methodOn(ChatController.class).getChatById(chat.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(chats,
                linkTo(methodOn(ChatController.class).getChatByOrderId(orderId)).withSelfRel());
    }

    @PostMapping("insertChat")
    public EntityModel<Chat> insertChat(@RequestBody Chat chat) {
        Chat saved = chatRepo.save(chat);
        return EntityModel.of(saved,
                linkTo(methodOn(ChatController.class).getChatById(saved.getId())).withSelfRel(),
                linkTo(methodOn(ChatController.class).getAll()).withRel("allChats"));
    }

    @DeleteMapping("/delete/{id}")
    public EntityModel<String> deleteChat(@PathVariable int id) {
        Chat chat = chatRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));
        chatRepo.delete(chat);
        String message = "Deleted chat with id " + id;
        return EntityModel.of(message,
                linkTo(methodOn(ChatController.class).getAll()).withRel("allChats"));
    }
}
