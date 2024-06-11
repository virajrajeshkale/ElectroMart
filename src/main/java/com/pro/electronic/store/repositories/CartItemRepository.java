package com.pro.electronic.store.repositories;

import com.pro.electronic.store.entites.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository  extends JpaRepository<CartItem,Integer> {

}
