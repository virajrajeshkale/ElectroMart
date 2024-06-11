package com.pro.electronic.store.services;

import com.pro.electronic.store.dtos.PageableResponse;
import com.pro.electronic.store.dtos.ProductDto;
import com.pro.electronic.store.entites.Product;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;


public interface ProductSerivce {

    //create
    ProductDto create (ProductDto productDto);

    //delete
    void delete (String productId);

    // update
        ProductDto update(ProductDto productDto,String productId);

    //getall
//    PageableResponse<ProductDto> getAllProduct ();
    PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    //getSingle
    ProductDto getSingleProduct(String  productId);

    //search product
   // PageableResponse<ProductDto>  searchByTitle(String subTitle);
    PageableResponse<ProductDto>  searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);


    //list of all live product
  //  PageableResponse<ProductDto>  liveProductList();
    PageableResponse<ProductDto>  liveProductList(int pageNumber, int pageSize, String sortBy, String sortDir);

    //creating product with category
    ProductDto createWithCategory(ProductDto productDto ,String categoryId);

    //  update Category of product
    ProductDto updateCategory(String productId,String categoryId);

    //method to get all product of same category

    PageableResponse<ProductDto>getAllProductOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);

}
