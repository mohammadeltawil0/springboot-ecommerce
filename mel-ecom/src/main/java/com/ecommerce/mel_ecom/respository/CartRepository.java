package com.ecommerce.mel_ecom.respository;

import com.ecommerce.mel_ecom.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
