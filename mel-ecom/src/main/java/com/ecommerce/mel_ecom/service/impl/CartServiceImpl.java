package com.ecommerce.mel_ecom.service.impl;

import com.ecommerce.mel_ecom.exception.APIException;
import com.ecommerce.mel_ecom.exception.ResourceNotFoundException;
import com.ecommerce.mel_ecom.model.Cart;
import com.ecommerce.mel_ecom.model.CartItem;
import com.ecommerce.mel_ecom.model.Product;
import com.ecommerce.mel_ecom.payload.CartDTO;
import com.ecommerce.mel_ecom.payload.CartItemDTO;
import com.ecommerce.mel_ecom.payload.ProductDTO;
import com.ecommerce.mel_ecom.respository.CartItemRepository;
import com.ecommerce.mel_ecom.respository.CartRepository;
import com.ecommerce.mel_ecom.respository.ProductRepository;
import com.ecommerce.mel_ecom.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        // Find existing cart or create one
        Cart cart = createCart();
        // Retrieve product details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        // Perform validations (e.g. stock exists)
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                cart.getCartId(),
                productId
        );
        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart!");
        }
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is out of stock!");
        }
        if (product.getQuantity() == quantity) {
            throw new APIException("Please make an order of " + product.getProductName() + " less than or equal to the current available quantity " + product.getQuantity() + "!");
        }
        // Create CartItem
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        // Save CartItem
        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity()); // Will reduce quantity after payments
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);
        // Return updated Cart
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productsStream = cartItems.stream()
                .map(item -> {
                    ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
                    map.setQuantity(item.getQuantity());
                    return map;
                });
        cartDTO.setProducts(productsStream.toList());
        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }

}
