package com.foodfinder.food.service;

import com.foodfinder.food.dao.ProductRepository;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.food.domain.mapper.FoodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final FoodMapper foodMapper;
    private final ProductsLiveSearchService liveSearchService;

    private static final int LIVE_SEARCH_PAGE_SIZE = 10;

    @Value("${food-finder.english-flag}")
    private String englishFlag;

    public List<ProductDTO> getProductList(Pageable pageable, String name, String language) {
        if(name == null) {
            return getProductList(pageable);
        } else if(language == null || language.equals(englishFlag)) {
            return liveSearchService.getProducts(name, LIVE_SEARCH_PAGE_SIZE);
        }
        return liveSearchService.getTranslatedProducts(name, LIVE_SEARCH_PAGE_SIZE);
    }

    public List<ProductDTO> getProductList(Pageable pageable) {
        return Optional.ofNullable(productRepository.findAll(pageable))
                .map(foodMapper::productListToDto)
                .orElseThrow(NotFoundException::new);
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
        productRepository.save(productEntity);
    }

    public void updateProduct(Long id, ProductDTO product) {
        Product productEntity = Optional.ofNullable(product)
                .map(foodMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        productEntity.setId(id);
        productRepository.save(productEntity);
    }
}