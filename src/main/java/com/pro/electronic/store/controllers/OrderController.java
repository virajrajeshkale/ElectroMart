package com.pro.electronic.store.controllers;

import com.pro.electronic.store.dtos.ApiResponseMessage;
import com.pro.electronic.store.dtos.CreateOrderRequest;
import com.pro.electronic.store.dtos.OrderDto;
import com.pro.electronic.store.dtos.PageableResponse;
import com.pro.electronic.store.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //create
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid  @RequestBody CreateOrderRequest request)
    {
        OrderDto order = orderService.createOrder(request);
        return  new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    //remove order
    @DeleteMapping("/{orderId}")
    public  ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId)
    {
        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage =ApiResponseMessage.builder().message("Order Removed Success..!!").success(true).status(HttpStatus.OK).build();
        return  new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    //Get orders Of user
    @GetMapping("/users/{userId}")
    public  ResponseEntity<List<OrderDto>>getOrdersOfUser(@PathVariable String userId)
    {
        List<OrderDto> orderOfUser = orderService.getOrderOfUser(userId);
        return new ResponseEntity<>(orderOfUser,HttpStatus.OK);
    }
@GetMapping
    public ResponseEntity<PageableResponse<OrderDto>>getAllOrders(
        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue = "orderdDate", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
)
    {
        PageableResponse<OrderDto> orderslist = orderService.getOrder(pageNumber, pageSize, sortBy, sortDir);
    return  new ResponseEntity<>(orderslist,HttpStatus.OK);
    }

    @PutMapping("/update/{orderId}")
    public  ResponseEntity<OrderDto>updateOrder(@PathVariable String orderId,@RequestBody  CreateOrderRequest request)
    {
        OrderDto orderDto = orderService.updateOrder(orderId, request);
        return new ResponseEntity<>(orderDto,HttpStatus.OK);
    }
}
