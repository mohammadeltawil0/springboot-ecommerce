package com.ecommerce.mel_ecom.service;

import com.ecommerce.mel_ecom.payload.CartDTO;

public interface CartService {

    CartDTO addProductToCart(Long productId, Integer quantity);
}
