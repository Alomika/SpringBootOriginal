package com.example.springboot1.controllers;

import com.example.springboot1.model.Chat;

public class OrderChatUpdateRequest {
    private Chat chat;
    public Chat getChat() {
        return chat;
    }
    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
