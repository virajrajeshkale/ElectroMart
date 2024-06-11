package com.pro.electronic.store.dtos;

import com.pro.electronic.store.entites.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDto {

    private  String productId;

   @NotNull(message = "Title Required..!!")
    private String title;

    @NotNull(message = "Description Required..!!")
    private String description;

    @NotNull(message = "Price Required..!!")
    private int price;

    private int discountedPrice;
    @NotNull(message = "quantity Required..!!")
    private  int quantity;

    private Date addedDate;
    @NotNull(message = "InStock Required..!!")
    private Boolean stock;
    @NotNull(message = "Product Live Filed Required..!!")
    private  boolean live;

 private String productImage;
 private CategoryDto category;

}
