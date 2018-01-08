package com.foodfinder.food;

import com.foodfinder.food.domain.dto.CompositionDTO;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.Composition;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.food.domain.mapper.FoodMapper;
import com.foodfinder.food.repository.ProductRepository;
import com.foodfinder.food.service.FoodLiveSearchService;
import com.foodfinder.food.service.ProductService;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private FoodMapper foodMapper;

    @MockBean
    private FoodLiveSearchService foodLiveSearchService;

    private ProductService productService;
    private PageRequest defaultPageRequest;
    private Product product;
    private ProductDTO productDTO;
    private List<ProductDTO> productsDTO;
    private Page<Product> productsPage;

    @Before
    public void setup() {
        productService = new ProductService(productRepository, foodLiveSearchService, foodMapper);
        defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        product = Product.builder()
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
        productsPage = new PageImpl<>(Collections.singletonList(product));

        productDTO = ProductDTO.builder()
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
    }

    @Test
    public void givenProductId_whenGetProduct_thenReturnDto() throws Exception {
        given(productRepository.findOne(1L)).willReturn(product);
        given(foodMapper.toDto(product)).willReturn(productDTO);

        assertEquals(productService.getProduct(1L), productDTO);
    }

    @Test
    public void whenGetProducts_thenReturnDtoList() throws Exception {
        given(productRepository.findAll(defaultPageRequest)).willReturn(productsPage);
        given(foodMapper.productListToDto(productsPage)).willReturn(productsDTO);

        assertEquals(productService.getProductList(defaultPageRequest, null), productsDTO);
    }

    @Test
    public void givenName_whenGetProducts_thenReturnDtoList() throws Exception {
        given(foodLiveSearchService.getProducts("Lemon", 10)).willReturn(productsDTO);

        assertEquals(productService.getProductList(defaultPageRequest, "Lemon"), productsDTO);
    }
}