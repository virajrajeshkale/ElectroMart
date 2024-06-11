package com.pro.electronic.store.services;

import com.pro.electronic.store.dtos.PageableResponse;
import com.pro.electronic.store.dtos.UserDto;

import java.util.List;

//this is make interface because we can easily rewrite its method or override easily
//Interface is best use for loose coupling
public interface UserService {

    //create
    UserDto  CreateUser(UserDto userDto);

    //delete
    void  DeleteUser(String UserId);

    //update
    UserDto UpdateUser(UserDto  userDto , String UserId);

    //get all users
    PageableResponse<UserDto> getAllUser(int PageNumber , int PageSize, String SortBy, String SortDir);

    //get single user by id
    UserDto GetUserById(String UserId);

    //get user by email id
    UserDto GetUserByEmail(String email);

    //search
    List<UserDto> SearchUser(String Keyword);

    //other methods



}
