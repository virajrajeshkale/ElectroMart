package com.pro.electronic.store.services;

import com.pro.electronic.store.dtos.AddItemToCartRequest;
import com.pro.electronic.store.dtos.CartDto;
import lombok.*;


public interface CartService {

    //add Item to cart
    //case 1 : cart for user is not available then can create cart the add item in it
    //case 2 : If cart is available then just add item in it

    //add item in available cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //remove item form cart
    void removeItemToCart(String userId,int cartItem);

    //remove all item form cart
    void  clearCart(String userId);

    CartDto getCartByUser(String userId);

}
