package com.pro.electronic.store.services;

import com.pro.electronic.store.dtos.CategoryDto;
import com.pro.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface CategoryService {

    //create
    CategoryDto Create(CategoryDto  categoryDto);

    //update
    CategoryDto Update(CategoryDto categoryDto,String userId);


    //delete
    void Delete(String userId);


    //getAll
    PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    //getSingle
    CategoryDto getSingle (String userId);

    //search
}
