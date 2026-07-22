package com.ecommerce.mel_ecom.service;

import com.ecommerce.mel_ecom.model.User;
import com.ecommerce.mel_ecom.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressById(Long addressId);
}
