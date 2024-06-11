package com.pro.electronic.store.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateOrderRequest {
    @NotBlank(message = "Cart Id is required..!")
    private  String  cartId;
    @NotBlank(message = "User Id is required..!")
    private  String userId;

    private String orderStatus ="PENDING";
    private  String paymentStatus="NotPaid";

    @NotBlank(message = "Billing address is required..!")
    private  String billingAddress;
    @NotBlank(message = "Billing Name is required..!")
    private String billingName;
    @NotBlank(message = "Billing Phone no  is required..!")
    @Length(min = 10,max = 10,message = "Contact Number Should be Correct..!!")
    private  String billingPhoneNo;


}
