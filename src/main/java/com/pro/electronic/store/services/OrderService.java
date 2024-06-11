package com.pro.electronic.store.services;
import com.pro.electronic.store.dtos.CreateOrderRequest;
import com.pro.electronic.store.dtos.OrderDto;
import com.pro.electronic.store.dtos.PageableResponse;
import java.util.List;

public interface OrderService {

//create order
    OrderDto createOrder(CreateOrderRequest orderDto );

    //remove order
    void removeOrder(String orderId);

    //get order of user
    List<OrderDto> getOrderOfUser(String userId);

    PageableResponse<OrderDto>getOrder(int pageNumber,int pageSize,String sortBy,String sortDir);

    OrderDto updateOrder(String orderId,CreateOrderRequest request);

}
