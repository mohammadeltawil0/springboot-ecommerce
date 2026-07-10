package com.ecommerce.mel_ecom.service;

import com.ecommerce.mel_ecom.model.Product;
import com.ecommerce.mel_ecom.payload.ProductDTO;
import com.ecommerce.mel_ecom.payload.ProductResponse;
import jakarta.validation.Valid;

public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchProductByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, @Valid Product product);
}
