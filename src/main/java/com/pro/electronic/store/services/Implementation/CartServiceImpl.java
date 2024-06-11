package com.pro.electronic.store.services.Implementation;

import com.pro.electronic.store.dtos.AddItemToCartRequest;
import com.pro.electronic.store.dtos.CartDto;
import com.pro.electronic.store.entites.Cart;
import com.pro.electronic.store.entites.CartItem;
import com.pro.electronic.store.entites.Product;
import com.pro.electronic.store.entites.User;
import com.pro.electronic.store.exceptions.BadApiRequest;
import com.pro.electronic.store.exceptions.ResourceNotFoundException;
import com.pro.electronic.store.repositories.CartItemRepository;
import com.pro.electronic.store.repositories.CartRepository;
import com.pro.electronic.store.repositories.ProductRepository;
import com.pro.electronic.store.repositories.UserRepository;
import com.pro.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        //fetching quantity and product id from user enter request
        String productId = request.getProductId();
        int quantity = request.getQuantity();

        if(quantity<=0)
            throw   new BadApiRequest("Requested Quantity is not valid..!!");
        //fetch product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product is not found for given id..!!"));


        //fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("For give Id User is Not found..!"));

        Cart cart = null;

//  if cart is not present for user them it throw exception so ued try catch block

try
{
    cart = cartRepository.findByUser(user).get();
}catch (NoSuchElementException ignored)
{
  //if cart is not available so we create an new cart
    cart = new Cart();
    cart.setCartId(UUID.randomUUID().toString());
    cart.setCreatedAt(new Date());
}

//       if cart is available then get all items of it
        AtomicReference<Boolean>updated = new AtomicReference<>(false);

        List<CartItem> items =  cart.getItems();
//now check if in available cart the product is already present in that cart the just increase the quantity an that cart price

      items = items.stream().map(item -> {
            //check id of item with present product id for matching

            if (item.getProduct().getProductId().equals(productId)) {
                //that means product is already present in that cart

                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

//        cart.setItems(updateditem);


//if cart not updated it means it is not available the go in if condition and add
if(!updated.get()) {
//create item toi add in cart
    CartItem createdCartItem = CartItem.builder()
            .quantity(quantity)
            .totalPrice(quantity * product.getDiscountedPrice())
            .product(product)
            .cart(cart)
            .build();

    //add items that newly created
    cart.getItems().add(createdCartItem);


}
        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);

        return mapper.map(updatedCart, CartDto.class);
    }


    @Override
    public void removeItemToCart(String userId, int cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("For given id cart is not found.."));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("For given id User is not found..!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("For given User Cart is Not found..!"));
            cart.getItems().clear();
            cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("For given userId user not found..!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("For given User Cart is Not Found..!!"));

        return  mapper.map(cart,CartDto.class);
    }
}
