package com.pro.electronic.store.entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    private Date orderdDate;

    private Date deliveredDate;

    //one user can order multiple product
    //error - multiple product order can done by multiple user

    private String orderStatus;
    private  String paymentStatus;

    private int orderAmount;

    @Column(length = 1000 )
    private  String billingAddress;
    private String billingName;
    private  String billingPhoneNo;

    @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_Id")
    private User user;

    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
   private List<OrderItem> orderItemList = new ArrayList<>();
}
