package com.pro.electronic.store.entites;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int orderItemId;

    private  int quantity;
    private int totalPrice;

    @OneToOne
    @JoinColumn(name = "product_Id")
    private Product product;

    @ManyToOne
  @JoinColumn(name = "order_id")
    private Order order;
}
