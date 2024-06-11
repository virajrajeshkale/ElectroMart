package com.pro.electronic.store.entites;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Cart_Item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int CartItemId;

    @OneToOne
    @JoinColumn(name = "Product_Id")
    private Product product;

    private  int quantity;
    private  int totalPrice;

//    mapped with cart

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "Cart_Id")
    private  Cart cart;

}
