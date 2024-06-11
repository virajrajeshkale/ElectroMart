package com.pro.electronic.store.controllers;

import com.pro.electronic.store.dtos.AddItemToCartRequest;
import com.pro.electronic.store.dtos.ApiResponseMessage;
import com.pro.electronic.store.dtos.CartDto;
import com.pro.electronic.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;
    //add item  to cart

    @PostMapping("/{userId}")
    
    public ResponseEntity<CartDto> addItemToCart (@PathVariable String userId, @RequestBody AddItemToCartRequest request)
    {
        CartDto cartDto = cartService.addItemToCart(userId ,request);
        return  new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    //remove item from cart
    @DeleteMapping("/{userId}/items/{cartItemId}")
    public  ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId, @PathVariable  int cartItemId)
    {
        cartService.removeItemToCart(userId,cartItemId);

        ApiResponseMessage responseMessage = ApiResponseMessage
                                                                                    .builder()
                                                                                    .message("Item Removed Success from cart")
                                                                                    .status(HttpStatus.OK)
                                                                                    .success(true).build();

        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    //clear cart
    @DeleteMapping("/{userId}")
    public  ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId)
    {
        cartService.clearCart(userId);

        ApiResponseMessage responseMessage = ApiResponseMessage
                .builder()
                .message("Now Cart is Blanked..!!")
                .status(HttpStatus.OK)
                .success(true).build();

        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto>getCart(@PathVariable String userId)
    {
        CartDto cart = cartService.getCartByUser(userId);
         return  new ResponseEntity<>(cart,HttpStatus.OK);
    }
}
