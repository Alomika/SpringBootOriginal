package com.example.springboot1.controllers;

import java.time.LocalDate;

class MessageRequest {
    private String reviewText;
    private long ownerId;
    private long chatId;
    private LocalDate sentDate;

    public LocalDate getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDate sentDate) {
        this.sentDate = sentDate;
    }

    public MessageRequest() {

    }

    public LocalDate getDateSent() {
        return dateSent;
    }

    public void setDateSent(LocalDate dateSent) {
        this.dateSent = dateSent;
    }

    private LocalDate dateSent;

    public MessageRequest(String text, long ownerId, long chatId) {
        this.reviewText = text;
        this.ownerId = ownerId;
        this.chatId = chatId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}
