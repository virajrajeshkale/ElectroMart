package com.pro.electronic.store.dtos;

import com.pro.electronic.store.entites.Cart;
import com.pro.electronic.store.entites.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private int CartItemId;

    private ProductDto product;

    private  int quantity;
    private  int totalPrice;

}
