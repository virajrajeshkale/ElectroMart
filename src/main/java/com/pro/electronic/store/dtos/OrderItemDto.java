package com.pro.electronic.store.dtos;

import com.pro.electronic.store.entites.Order;
import com.pro.electronic.store.entites.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderItemDto {
    private  int orderItemId;
    private  int quantity;
    private int totalPrice;
    private Product product;

}
