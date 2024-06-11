package com.pro.electronic.store.entites;

import com.pro.electronic.store.Validate.ImageNameValid;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table
public class Category {
    @Id
    private  String CategoryId;
    @Column(name = "Category_title",nullable = false,length = 30,unique = true)
    private String title;
    @Column(name = "Description",nullable = false,length=300)
    private String description;
    @Column(nullable = false)
    @ImageNameValid
    private String CoverImage;

//    cascade - whenever we update delete or make any change on product reflect both side
    //fetch (Lazy) - when ever we call category the product cant be called which are mapped
    @OneToMany(mappedBy ="category" ,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

}
