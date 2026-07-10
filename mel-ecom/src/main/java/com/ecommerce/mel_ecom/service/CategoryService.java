package com.ecommerce.mel_ecom.service;

import com.ecommerce.mel_ecom.model.Category;
import com.ecommerce.mel_ecom.payload.CategoryDTO;
import com.ecommerce.mel_ecom.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO category, Long categoryId);
}
