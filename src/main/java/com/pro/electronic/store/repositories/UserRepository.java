package com.pro.electronic.store.repositories;

import com.pro.electronic.store.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//this is an data-layered handler Interface
//this class methods generate by runtime

public interface UserRepository extends JpaRepository<User,String> {

    //this method body written by run time from jpa
   Optional<User>  findByEmail(String email);

   Optional<User> findByEmailAndPassword(String email,String password);

  List<User>  findByNameContaining(String keyword);
}
