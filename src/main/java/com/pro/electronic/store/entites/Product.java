package com.pro.electronic.store.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table
public class Product {

    @Id
    private  String productId;
    @Column(nullable = false)
    private String title;
    @Column(length = 10000)
    private String description;
    private int price;
    private int discountedPrice;
    private  int quantity;
    private Date addedDate;
    private Boolean stock;
    private  boolean live;
    private String productImage;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Category_id")
    private  Category category;

}


