package com.techtrove.productservice.service;

import com.techtrove.productservice.dto.ProductRequest;
import com.techtrove.productservice.dto.ProductResponse;
import com.techtrove.productservice.model.Product;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(String id);

    ProductResponse updateProduct(String id, ProductRequest product);

    boolean deleteProduct(String id);
}
