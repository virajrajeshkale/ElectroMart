package com.pro.electronic.store.repositories;

import com.pro.electronic.store.entites.Cart;
import com.pro.electronic.store.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {
    Optional<Cart> findByUser(User user);
}
