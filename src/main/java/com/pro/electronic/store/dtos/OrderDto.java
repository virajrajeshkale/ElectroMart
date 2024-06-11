package com.pro.electronic.store.dtos;

import com.pro.electronic.store.entites.OrderItem;
import com.pro.electronic.store.entites.User;
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

public class OrderDto {

    private String orderId;
    private Date orderedDate = new Date();
    private Date deliveredDate;
    private String orderStatus ="PENDING";
    private  String paymentStatus="NotPaid";
    private int orderAmount;
    private  String billingAddress;
    private String billingName;
    private  String billingPhoneNo;

//    private UserDto user;
    private List<OrderItemDto> orderItemList = new ArrayList<>();
}
