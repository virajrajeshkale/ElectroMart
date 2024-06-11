package com.pro.electronic.store.services.Implementation;

import com.pro.electronic.store.dtos.CreateOrderRequest;
import com.pro.electronic.store.dtos.OrderDto;
import com.pro.electronic.store.dtos.PageableResponse;
import com.pro.electronic.store.entites.*;
import com.pro.electronic.store.exceptions.BadApiRequest;
import com.pro.electronic.store.exceptions.ResourceNotFoundException;
import com.pro.electronic.store.helper.PageableHelper;
import com.pro.electronic.store.repositories.CartRepository;
import com.pro.electronic.store.repositories.OrderItemRepository;
import com.pro.electronic.store.repositories.OrderRepository;
import com.pro.electronic.store.repositories.UserRepository;
import com.pro.electronic.store.services.OrderService;
import org.apache.coyote.Request;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("For given id user is Not Found..!"));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("For Given Id the Cart is not found..!!"));

         List<CartItem> cartItems = cart.getItems();

        if (cartItems.size()<=0) {
            throw new BadApiRequest("Invalid number of item");
        }

        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhoneNo(orderDto.getBillingPhoneNo())
                .billingAddress(orderDto.getBillingAddress())
                .orderdDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();


            //converting cartitems to OrderItems

        AtomicReference<Integer>orderAmount = new AtomicReference<>(0);

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
            return  orderItem;
        }).collect(Collectors.toList());

         order.setOrderItemList(orderItems);
         order.setOrderAmount(orderAmount.get());

         //clearning cart
        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder,OrderDto.class);
    }

        @Override
    public void removeOrder(String orderId) {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("For Given id Order is not found..!!"));
            orderRepository.delete(order);
        }

    @Override
    public List<OrderDto> getOrderOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("For given User Id User is not found..!!"));
        List<Order> orderList = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orderList.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrder(int pageNumber, int pageSize, String sortBy, String sortDir) {
        List<Order> allOrders = orderRepository.findAll();
        Sort sort = (sortDir.equalsIgnoreCase("desc")?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending()));
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order>page = orderRepository.findAll(pageable);
        return PageableHelper.getPageableResponse(page,OrderDto.class);
    }


    @Override
    public OrderDto updateOrder( String orderId,  CreateOrderRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("For given id Order is not found"));
        order.setOrderStatus(request.getOrderStatus());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setDeliveredDate(new Date());
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder,OrderDto.class);
    }
}
