package com.foodfinder.food.service;

import com.foodfinder.food.repository.ProductRepository;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.food.domain.mapper.FoodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final FoodLiveSearchService liveSearchService;
    private final FoodMapper foodMapper;

    private static final int LIVE_SEARCH_PAGE_SIZE = 10;

    public List<ProductDTO> getProductList(Pageable pageable, String name) {
        if(name == null) {
            return getProductList(pageable);
        }
        return liveSearchService.getProducts(name, LIVE_SEARCH_PAGE_SIZE);
    }

    public ProductDTO getProduct(Long id) {
        return Optional.ofNullable(productRepository.findById(id))
                .map(foodMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    public void postProduct(ProductDTO product) {
        Product productEntity = Optional.ofNullable(product)
                .map(foodMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        productEntity.setHits(0L);
        productRepository.save(productEntity);
    }

    public void updateProduct(Long id, ProductDTO product) {
        Product productEntity = Optional.ofNullable(product)
                .map(foodMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        productEntity.setId(id);
        productRepository.save(productEntity);
    }

    private List<ProductDTO> getProductList(Pageable pageable) {
        return Optional.ofNullable(productRepository.findAll(pageable))
                .map(foodMapper::productListToDto)
                .orElseThrow(NotFoundException::new);
    }
}