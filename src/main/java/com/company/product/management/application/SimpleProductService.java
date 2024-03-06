package com.company.product.management.application;

import com.company.product.management.domain.Product;
import com.company.product.management.infrastructure.DatabaseProductRepository;
import com.company.product.management.infrastructure.ListProductRepository;
import com.company.product.management.presentation.ProductDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleProductService {

    private DatabaseProductRepository databaseProductRepository;
    private ModelMapper modelMapper;
    private ValidationService validationService;


    @Autowired
    SimpleProductService(DatabaseProductRepository databaseProductRepository, ModelMapper modelMapper, ValidationService validationService) {
        this.databaseProductRepository = databaseProductRepository;
        this.modelMapper = modelMapper;
        this.validationService = validationService;
    }

    public ProductDto add(ProductDto productDto) {
        //1. ProductDto 를 Product 로 변환하는 코드
        Product product = modelMapper.map(productDto, Product.class);
        validationService.checkValid(product);

        //2. 레포지토리를 호출하는 코드
        Product savedProduct = databaseProductRepository.add(product);

        //3. Product 를 ProductDto 로 변환하는 코드
        ProductDto savedProductDto = modelMapper.map(savedProduct, ProductDto.class);

        //4. DTO 를 반환하는 코드
        return savedProductDto;
    }

    public ProductDto findById(Long id) {
        Product product = databaseProductRepository.findById(id);
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return productDto;
    }

    public List<ProductDto> findAll() {
        List<Product> products = databaseProductRepository.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        return productDtos;
    }

    public List<ProductDto> findByNameContaining(String name) {
        List<Product> products = databaseProductRepository.findByNameContaining(name);
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        return productDtos;
    }

    public ProductDto update(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        Product updatedProduct = databaseProductRepository.update(product);
        ProductDto updatedProductDto = modelMapper.map(updatedProduct, ProductDto.class);
        return updatedProductDto;
    }

    public void delete(Long id) {
        databaseProductRepository.delete(id);
    }
}
