package com.techtrove.productservice.service.impl;

import com.techtrove.productservice.dto.ProductRequest;
import com.techtrove.productservice.dto.ProductResponse;
import com.techtrove.productservice.exception.ProductNotFoundException;
import com.techtrove.productservice.model.Product;
import com.techtrove.productservice.repository.ProductRepository;
import com.techtrove.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        return mapProductToDTO(productRepository.save(product));
    }


    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(this::mapProductToDTO).collect(Collectors.toList());
    }

    private ProductResponse mapProductToDTO(Product product) {
        return ProductResponse.builder()
                .description(product.getDescription())
                .price(product.getPrice())
                .id(product.getId())
                .name(product.getName())
                .build();
    }

    public ProductResponse getProductById(String id) {
       Optional<Product> product = productRepository.findById(id);

       if(product.isPresent()){
           return mapProductToDTO(product.get());
       }else{
           throw new ProductNotFoundException("Product not found");
       }

    }

    public ProductResponse updateProduct(String id, ProductRequest productRequest) {

        Product product = mapToProduct(productRequest);

        if (productRepository.existsById(id)) {
            product.setId(id);
            return mapProductToDTO(productRepository.save(product));
        }
        return null; // Product with the given ID not found
    }

    public boolean deleteProduct(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false; // Product with the given ID not found
    }

    private Product mapToProduct(ProductRequest productRequest){
        return Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
    }
}