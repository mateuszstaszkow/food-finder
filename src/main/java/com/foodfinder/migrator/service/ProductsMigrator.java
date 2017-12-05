package com.foodfinder.migrator.service;

import com.foodfinder.food.dao.FoodGroupRepository;
import com.foodfinder.food.dao.ProductRepository;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.migrator.domain.dto.FoodGroupResponseDTO;
import com.foodfinder.migrator.domain.mapper.MigrationMapper;
import com.foodfinder.migrator.domain.dto.UsdaFoodResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
class ProductsMigrator {

    private final ProductRepository productRepository;
    private final MigrationMapper migrationMapper;
    private final FoodGroupRepository foodGroupRepository;

    @Value("${usda.products.url}")
    private String FOOD_URL;

    @Value("${usda.auth}")
    private String USDA_AUTH;

    private static final int PAGE_SIZE = 1500;

    void migrate(List<FoodGroupResponseDTO> foodGroups) {
        RestTemplate restTemplate = new RestTemplate();
        AtomicInteger counter = new AtomicInteger(0);
        Float foodGroupsCount = (float) foodGroups.size();
        Long start = new Date().getTime();

        foodGroups.forEach(foodGroup -> migrateProductsForGroup(foodGroup, restTemplate, counter, foodGroupsCount));

        logStatus(start);
    }

    private void logStatus(Long start) {
        Float duration = (new Date().getTime() - start)/ (float) 1000;
        log.info("Products count:" + productRepository.count() + ", time: " + duration + " s");
    }

    private void migrateProductsForGroup(FoodGroupResponseDTO foodGroup, RestTemplate restTemplate,
                                         AtomicInteger counter, Float foodGroupsCount) {

        FoodGroup dbFoodGroup = foodGroupRepository.findByName(foodGroup.getName());

        try {
            List<Product> productList = restTemplate.getForObject(buildUri(foodGroup.getId()), UsdaFoodResponseDTO.class)
                    .getReport()
                    .getFoods()
                    .stream()
                    .filter(productDto -> productRepository.findByName(productDto.getName()) == null)
                    .map(migrationMapper::toEntity)
                    .map(product -> product.setFoodGroup(dbFoodGroup))
                    .collect(Collectors.toList());

            productRepository.save(productList);
            Float progress = 100 * counter.addAndGet(1) / foodGroupsCount;
            log.info("Product migration progress: " + progress + " %");
        }
        catch (RestClientException exception) {
            log.error("USDA REST API error", exception);
        }
    }

    private URI buildUri(String id) {
        return UriComponentsBuilder.fromUriString(FOOD_URL)
                .queryParam("format", "json")
                .queryParam("api_key", USDA_AUTH)
                .queryParam("nutrients", "203")
                .queryParam("nutrients", "204")
                .queryParam("nutrients", "205")
                .queryParam("nutrients", "208")
                .queryParam("sort", "f")
                .queryParam("max", Integer.toString(PAGE_SIZE))
                .queryParam("start", "0")
                .queryParam("fg", id)
                .build()
                .toUri();
    }
}
