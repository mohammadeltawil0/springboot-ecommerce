package com.ecommerce.mel_ecom.service;

import com.ecommerce.mel_ecom.model.User;
import com.ecommerce.mel_ecom.payload.AddressDTO;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);
}
