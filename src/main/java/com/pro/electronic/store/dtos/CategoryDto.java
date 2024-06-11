package com.pro.electronic.store.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {


    private  String CategoryId;
@Size(min = 3 ,message = "Title have Minimum  size 3..")
@NotNull(message = "Title Required..!")
    private String title;

@NotNull(message = "Description Required..!!")
@Size(max = 300,message = "Description limit 300")
    private String description;

    @NotNull(message = "Image Required..!!")
    @Size(min = 3 ,message = "Minimum  size 3..")
    private String CoverImage;
}
