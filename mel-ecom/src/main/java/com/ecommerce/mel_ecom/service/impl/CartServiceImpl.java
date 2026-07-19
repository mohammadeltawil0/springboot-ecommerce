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
import com.ecommerce.mel_ecom.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

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
        if (product.getQuantity() < quantity) {
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
        cart.getCartItems().add(newCartItem);
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

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            throw new APIException("No carts exist!");
        }
        Stream<CartDTO> cartsStream = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    List<ProductDTO> products = cart.getCartItems().stream()
                            .map(product -> modelMapper.map(product.getProduct(), ProductDTO.class))
                            .toList();
                    cartDTO.setProducts(products);
                    return cartDTO;
                });
        return cartsStream.toList();
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity())); // Correct quantity fix
        List<ProductDTO> products = cart.getCartItems().stream()
                .map(product -> modelMapper.map(product.getProduct(), ProductDTO.class))
                .toList();
        cartDTO.setProducts(products); // Get products in cart output JSON
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        // Check if the cart exists
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        // Check the quantity of the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is out of stock!");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Please make an order of " + product.getProductName() + " less than or equal to the current available quantity " + product.getQuantity() + "!");
        }
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " is not available in the cart!");
        }
        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
        cartRepository.save(cart);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        if (updatedCartItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedCartItem.getCartItemId());
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });
        cartDTO.setProducts(productDTOStream.toList());
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
