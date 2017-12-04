package com.foodfinder.food;

import com.foodfinder.food.domain.FoodMapper;
import com.foodfinder.food.domain.Product;
import com.foodfinder.food.domain.ProductRepository;
import com.foodfinder.food.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    private final ProductRepository productRepository;
    private final FoodMapper foodMapper;

    public List<ProductDTO> getProductList() {
        return Optional.ofNullable(productRepository.findAll())
                .map(foodMapper::productListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public ProductDTO getProduct(Long id) {
        return Optional.ofNullable(productRepository.findByNdbno(id))
                .map(foodMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    public void postProduct(ProductDTO product) {
        Product productEntity = Optional.ofNullable(product)
                .map(foodMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        productRepository.save(productEntity);
    }

    public void updateProduct(Long id, ProductDTO product) {
        Product productEntity = Optional.ofNullable(product)
                .map(foodMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        productEntity.setNdbno(id);
        productRepository.save(productEntity);
    }
}