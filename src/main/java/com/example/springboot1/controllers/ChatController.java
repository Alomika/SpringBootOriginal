package com.example.springboot1.controllers;

import com.example.springboot1.model.Chat;
import com.example.springboot1.model.FoodOrder;
import com.example.springboot1.repositories.ChatRepo;
import com.example.springboot1.repositories.FoodOrderRepo;
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
public class ChatController {

    @Autowired
    private ChatRepo chatRepo;
    @Autowired
    private FoodOrderRepo foodOrderRepo;

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

    @GetMapping("/order/{orderId}/chat")
    public EntityModel<Chat> getChatForOrder(@PathVariable int orderId) {
        FoodOrder order = foodOrderRepo.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        Chat chat = order.getChat();
        if (chat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No chat exists for this order");
        }

        return EntityModel.of(chat,
                linkTo(methodOn(ChatController.class).getChatById(chat.getId())).withSelfRel());
    }
    @PostMapping("/order/{orderId}/chat1")
    public EntityModel<Chat> createChatForOrder(@PathVariable Integer orderId, @RequestBody ChatRequest request) {
        FoodOrder order = foodOrderRepo.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getChat() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chat already exists for this order");
        }

        Chat chat = new Chat();
        chat.setName(request.getName());
        chat.setDateCreated(LocalDate.now());

        Chat savedChat = chatRepo.save(chat);
        order.setChat(savedChat);
        order.setDateUpdated(LocalDate.now());
        foodOrderRepo.save(order);

        return EntityModel.of(savedChat,
                linkTo(methodOn(ChatController.class).getChatById(savedChat.getId())).withSelfRel());
    }


    @PostMapping("/insertChat")
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
