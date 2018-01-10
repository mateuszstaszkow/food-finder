package com.foodfinder.food;

import com.foodfinder.food.domain.dto.CompositionDTO;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.Composition;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.food.domain.mapper.FoodMapper;
import com.foodfinder.food.repository.FoodGroupRepository;
import com.foodfinder.food.repository.ProductRepository;
import com.foodfinder.food.service.FoodLiveSearchService;
import com.foodfinder.food.service.FoodTranslationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class FoodLiveSearchServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private FoodMapper foodMapper;

    @MockBean
    private FoodTranslationService translationService;

    @MockBean
    private FoodGroupRepository foodGroupRepository;

    private FoodLiveSearchService foodLiveSearchService;
    private List<ProductDTO> productsDTO;
    private List<Product> products;
    private List<FoodGroupDTO> foodGroupsDTO;
    private List<FoodGroup> foodGroups;

    @Before
    public void setup() {
        foodLiveSearchService = new FoodLiveSearchService(productRepository, foodMapper, translationService, foodGroupRepository);

        Product product = Product.builder()
                .id(1L)
                .name("Lemon")
                .weight(10f)
                .measure("1.0 tbsp")
                .protein(new Composition())
                .carbohydrates(new Composition())
                .fat(new Composition())
                .energy(new Composition())
                .composition(new HashSet<>())
                .description("Very good lemon")
                .shortDescription("Good lemon")
                .foodGroup(new FoodGroup())
                .hits(10L)
                .translatedName("Cytryna")
                .build();
        products = Collections.singletonList(product);

        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .name("Lemon")
                .weight(10f)
                .measure("1.0 tbsp")
                .protein(new CompositionDTO())
                .carbohydrates(new CompositionDTO())
                .fat(new CompositionDTO())
                .energy(new CompositionDTO())
                .composition(new ArrayList<>())
                .foodGroup(new FoodGroupDTO())
                .build();
        productsDTO = Collections.singletonList(productDTO);

        FoodGroup foodGroup = new FoodGroup(1L, "Test products", "Testowe produkty");
        foodGroups = Collections.singletonList(foodGroup);

        FoodGroupDTO foodGroupDTO = new FoodGroupDTO(1L, "Test products");
        foodGroupsDTO = Collections.singletonList(foodGroupDTO);
    }

    @Test
    public void givenEnglishName_whenGetProducts_thenReturnEnglishDtoList() throws Exception {
        String productName = "Lemon";

        given(translationService.isPolishLanguage()).willReturn(false);
        given(productRepository.findByNameStartsWithAndIsCondition(productName, " raw")).willReturn(products);
        given(foodMapper.productListToDto(products)).willReturn(productsDTO);
        given(productRepository.findByNameStartsWith(productName)).willReturn(new ArrayList<>());
        given(productRepository.findTop10ByNameContaining(productName)).willReturn(new ArrayList<>());

        assertEquals(foodLiveSearchService.getProducts(productName, 10), productsDTO);
    }

    @Test
    public void givenEnglishName_whenGetFoodGroups_thenReturnEnglishDtoList() throws Exception {
        String foodGroupName = "Test";

        given(translationService.isPolishLanguage()).willReturn(false);
        given(foodGroupRepository.findTop10ByNameContaining(foodGroupName)).willReturn(foodGroups);
        given(foodMapper.foodGroupListToDto(foodGroups)).willReturn(foodGroupsDTO);

        assertEquals(foodLiveSearchService.getFoodGroups(foodGroupName, 10), foodGroupsDTO);
    }

}