package com.techtrove.productservice

import com.techtrove.productservice.dto.ProductRequest
import com.techtrove.productservice.model.Product
import com.techtrove.productservice.repository.ProductRepository
import com.techtrove.productservice.service.impl.ProductServiceImpl
import spock.lang.Specification
import spock.lang.Subject

class ProductServiceImplSpec extends Specification {

    def productRepository = Mock(ProductRepository)

    def productName = "NAME"
    def productDescription = "DESC"
    def price = BigDecimal.ONE

    @Subject
    def productService = new ProductServiceImpl(productRepository)

    def "Should create a new product"() {
        given:
        def productRequest = ProductRequest.builder()
                .price(price)
                .name(productName)
                .description(productDescription)
                .build()
        when:
        def result = productService.createProduct(productRequest)
        then:
        result != null
        result.id != null
        1 * productRepository.save(_) >> Product.builder()
                .id("ID")
                .price(BigDecimal.ONE)
                .name(productName)
                .description(productDescription)
                .build()
        0 * _
    }

    def "Should return a list of all products available"() {
        when:
        def result = productService.getAllProducts()
        then:
        result != null
        result.size() > 0
        1 * productRepository.findAll() >> Arrays.asList(Product.builder()
                .price(price).id("ID")
                .name(productName)
                .description(productDescription)
                .build()
        )
        0 * _
    }

    def "Should get a product by ID"() {
        when:
        def result = productService.getProductById("ID")
        then:
        result != null
        result.id == "ID"
        1 * productRepository.findById("ID") >> Optional.of(Product.builder()
                .id("ID")
                .description(productDescription)
                .name(productName)
                .price(price)
                .build())
        0 * _
    }

    def "Should update product data"() {
        given:
        def productRequest = ProductRequest.builder().price(price).name(productName).description(productDescription).build()
        when:
        def result = productService.updateProduct("ID", productRequest)
        then:
        1 * productRepository.existsById('ID') >> true
        1 * productRepository.save(_) >> Product.builder()
                .price(price)
                .id("ID")
                .name(productName)
                .description(productDescription)
                .build()
        0 * _
    }

    def "Should delete a product by provided id"(){
        when:
        def result = productService.deleteProduct("ID")
        then:
        result
        1 * productRepository.existsById('ID') >> true
        1 * productRepository.deleteById('ID')
        0 * _
    }

}