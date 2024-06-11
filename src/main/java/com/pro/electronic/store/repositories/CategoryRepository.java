package com.pro.electronic.store.repositories;

import com.pro.electronic.store.dtos.CategoryDto;
import com.pro.electronic.store.entites.Cart;
import com.pro.electronic.store.entites.Category;
import com.pro.electronic.store.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Locale;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,String> {


}
