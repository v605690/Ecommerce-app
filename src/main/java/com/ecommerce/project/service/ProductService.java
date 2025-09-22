package com.ecommerce.project.service;

import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.dto.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductResponse getAllProducts();

    ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchProductByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO product);

    ProductDTO deleteProduct(Long productId);
}
