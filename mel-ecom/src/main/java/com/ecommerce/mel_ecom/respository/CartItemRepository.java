package com.ecommerce.mel_ecom.respository;

import com.ecommerce.mel_ecom.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
