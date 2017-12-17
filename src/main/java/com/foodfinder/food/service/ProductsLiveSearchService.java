package com.foodfinder.food.service;

import com.foodfinder.food.dao.ProductRepository;
import com.foodfinder.food.domain.dto.ProductDTO;
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
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class ProductsLiveSearchService {

    private final ProductRepository productRepository;
    private final FoodMapper foodMapper;

    private static final String PRODUCT_TYPE = " raw";
    private static final String PRODUCT_TYPE_PL = " surow";

    public List<ProductDTO> getProducts(String name, int numberOfResults) {
        List<ProductDTO> rawProducts = getSortedDTOs(productRepository.findByNameStartsWithAndIsCondition(name, PRODUCT_TYPE));
        getAdditionalProducts(numberOfResults, rawProducts, productRepository.findByNameStartsWith(name));
        getAdditionalProducts(numberOfResults, rawProducts, productRepository.findTop10ByNameContaining(name));

        return rawProducts;
    }

    public List<ProductDTO> getTranslatedProducts(String name, int numberOfResults) {
        List<ProductDTO> rawProducts = getSortedDTOs(productRepository.findByTranslatedNameStartsWithAndIsCondition(name, PRODUCT_TYPE_PL));
        getAdditionalProducts(numberOfResults, rawProducts, productRepository.findByTranslatedNameStartsWith(name));
        getAdditionalProducts(numberOfResults, rawProducts, productRepository.findTop10ByTranslatedNameContaining(name));

        return rawProducts;
    }

    private void getAdditionalProducts(int numberOfResults, List<ProductDTO> rawProducts, List<Product> productDbList) {
        if(rawProducts.size() >= numberOfResults) {
            return;
        }

        List<ProductDTO> additionalProducts = getSortedDTOs(productDbList);
        int toMaximum = numberOfResults - rawProducts.size();
        int productsToFind = toMaximum < additionalProducts.size() ? toMaximum : additionalProducts.size();

        IntStream.range(0, productsToFind)
                .forEach(productNo -> {
                    ProductDTO additionalProduct = additionalProducts.get(productNo);
                    if(!rawProducts.contains(additionalProduct)) {
                        rawProducts.add(additionalProduct);
                    }
                });
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
}
