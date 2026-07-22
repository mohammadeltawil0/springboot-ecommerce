package com.ecommerce.mel_ecom.respository;

import com.ecommerce.mel_ecom.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
