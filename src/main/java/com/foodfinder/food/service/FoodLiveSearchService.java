package com.foodfinder.food.service;

import com.foodfinder.food.repository.FoodGroupRepository;
import com.foodfinder.food.repository.ProductRepository;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.food.domain.mapper.FoodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class FoodLiveSearchService {

    private final ProductRepository productRepository;
    private final FoodMapper foodMapper;
    private final FoodTranslationService translationService;
    private final FoodGroupRepository foodGroupRepository;

    private static final String PRODUCT_TYPE = " raw";
    private static final String PRODUCT_TYPE_PL = " surow";

    public List<ProductDTO> getProducts(String name, int limit) {
        if(translationService.isPolishLanguage()) {
            return getTranslatedProducts(name, limit);
        }
        return getOriginalProducts(name, limit);
    }

    public List<FoodGroupDTO> getFoodGroups(String name, int limit) {
        if(translationService.isPolishLanguage()) {
            return getTranslatedFoodGroupsLiveSearch(name, limit);
        }
        return getOriginalFoodGroupsLiveSearch(name, limit);
    }

    public List<ProductDTO> getOriginalProducts(String name, int limit) {
        return Optional.ofNullable(productRepository.findByNameStartsWithAndIsCondition(name, PRODUCT_TYPE))
                .map(this::getSortedDTOs)
                .map(p -> getAdditionalProducts(limit, p, productRepository.findByNameStartsWith(name)))
                .map(p -> getAdditionalProducts(limit, p, productRepository.findTop10ByNameContaining(name)))
                .map(p -> sortAndTruncateProductList(p, limit))
                .orElseThrow(NotFoundException::new);
    }

    private List<FoodGroupDTO> getOriginalFoodGroupsLiveSearch(String name, int limit) {
        return Optional.ofNullable(foodGroupRepository.findTop10ByNameContaining(name))
                .map(groups -> truncateFoodGroupList(groups, limit))
                .map(foodMapper::foodGroupListToDto)
                .orElseThrow(NotFoundException::new);
    }

    private List<FoodGroupDTO> getTranslatedFoodGroupsLiveSearch(String name, int limit) {
        return Optional.ofNullable(foodGroupRepository.findTop10ByTranslatedNameContaining(name))
                .map(groups -> truncateFoodGroupList(groups, limit))
                .map(foodMapper::foodGroupListToDto)
                .orElseThrow(NotFoundException::new);
    }

    private List<ProductDTO> getTranslatedProducts(String name, int limit) {
        return Optional.ofNullable(productRepository.findByTranslatedNameStartsWithAndIsCondition(name, PRODUCT_TYPE_PL))
                .map(this::getSortedDTOs)
                .map(p -> getAdditionalProducts(limit, p, productRepository.findByTranslatedNameStartsWith(name)))
                .map(p -> getAdditionalProducts(limit, p, productRepository.findTop10ByTranslatedNameContaining(name)))
                .map(p -> sortAndTruncateProductList(p, limit))
                .orElseThrow(NotFoundException::new);
    }

    private List<ProductDTO> getAdditionalProducts(int limit, List<ProductDTO> rawProducts, List<Product> productDbList) {
        if(rawProducts.size() >= limit) {
            return rawProducts;
        }

        List<ProductDTO> additionalProducts = getSortedDTOs(productDbList);
        int toMaximum = limit - rawProducts.size();
        int productsToFind = toMaximum < additionalProducts.size() ? toMaximum : additionalProducts.size();

        IntStream.range(0, productsToFind)
                .forEach(productNo -> {
                    ProductDTO additionalProduct = additionalProducts.get(productNo);
                    if(!rawProducts.contains(additionalProduct)) {
                        rawProducts.add(additionalProduct);
                    }
                });
        
        return rawProducts;
    }

    private List<ProductDTO> getSortedDTOs(List<Product> productDbList) {
        return Optional.ofNullable(productDbList)
                .map(products -> {
                    products.sort(Comparator.comparingInt(product -> product.getName().length()));
                    return products;
                })
                .map(foodMapper::productListToDto)
                .orElseThrow(NotFoundException::new);
    }

    private List<ProductDTO> sortAndTruncateProductList(List<ProductDTO> products, int limit) {
        return products.stream()
                .sorted(Comparator.comparingLong(p -> foodMapper.toEntity((ProductDTO) p).getHits()).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<FoodGroup> truncateFoodGroupList(List<FoodGroup> foodGroups, int limit) {
        return foodGroups.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
