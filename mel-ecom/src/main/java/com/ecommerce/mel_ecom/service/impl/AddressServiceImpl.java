package com.ecommerce.mel_ecom.service.impl;

import com.ecommerce.mel_ecom.model.Address;
import com.ecommerce.mel_ecom.model.User;
import com.ecommerce.mel_ecom.payload.AddressDTO;
import com.ecommerce.mel_ecom.respository.AddressRepository;
import com.ecommerce.mel_ecom.service.AddressService;
import com.ecommerce.mel_ecom.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {

        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }
}
