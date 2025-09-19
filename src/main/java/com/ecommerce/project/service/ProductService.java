package com.ecommerce.project.service;

import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.model.Product;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);
}
