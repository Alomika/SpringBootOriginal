package com.example.springboot1;

import com.example.springboot1.model.Chat;
import com.example.springboot1.model.OrderStatus;

public class OrderStatusUpdateRequest {
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
