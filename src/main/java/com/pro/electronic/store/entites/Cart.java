package com.pro.electronic.store.entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "Cart")
public class Cart {

    @Id
    private  String cartId;

    private Date createdAt;
    @OneToOne
    private User user;
        //orphanRemoval = true  - when we rome some relation or delete the it will be remove from database
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CartItem>items = new ArrayList<>();
}
