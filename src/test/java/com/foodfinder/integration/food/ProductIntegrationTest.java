package com.foodfinder.integration.food;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.food.domain.dto.CompositionDTO;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.repository.ProductRepository;
import com.foodfinder.integration.config.IntegrationTestSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProductIntegrationTest extends IntegrationTestSetup {

    @Autowired
    private ProductRepository productRepository;
    
    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetProducts_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProduct_whenPostProduct_thenReturnsStatusCreated() throws Exception {
        ProductDTO product = getProduct(1L);
        String jsonProduct = new ObjectMapper().writeValueAsString(product);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProduct))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProductWithNullId_whenPostProduct_thenReturnsStatusCreated() throws Exception {
        ProductDTO product = getProduct(null);
        String jsonProduct = new ObjectMapper().writeValueAsString(product);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProduct))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProduct_whenGetProduct_thenReturnsProduct() throws Exception {
        ProductDTO product = getProduct(1L);
        String jsonProduct = new ObjectMapper().writeValueAsString(product);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProduct))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(product.getName())));
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProduct_whenGetProductByName_thenReturnsProductArray() throws Exception {
        ProductDTO product = getProduct(null);
        String jsonProduct = new ObjectMapper().writeValueAsString(product);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProduct))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/products?name=Lem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(product.getName())));
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProductsAndProduct_whenUpdateProduct_thenNotAddANewOne() throws Exception {
        addProductToDatabase();

        long before = productRepository.count();

        String result = mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        ProductDTO product = (ProductDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<ProductDTO>>(){}))).get(0);
        String jsonProduct = new ObjectMapper().writeValueAsString(product);

        mockMvc.perform(post("/api/products/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProduct))
                .andExpect(status().isOk());

        assertEquals(before, productRepository.count());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProductsAndProduct_whenUpdateProduct_thenUpdateNameField() throws Exception {
        addProductToDatabase();

        String result = mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        ProductDTO product = (ProductDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<ProductDTO>>(){}))).get(0);
        product.setName("Updated product");
        String jsonProduct = new ObjectMapper().writeValueAsString(product);

        mockMvc.perform(post("/api/products/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProduct))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/products/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(product.getName())));
    }

    private void addProductToDatabase() throws Exception {
        ProductDTO product = getProduct(null);
        String jsonProduct = new ObjectMapper().writeValueAsString(product);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProduct))
                .andExpect(status().isCreated());
    }

    private ProductDTO getProduct(Long id) throws Exception {
        return ProductDTO.builder()
                .id(id)
                .name("Lemon")
                .weight(10f)
                .measure("1.0 tbsp")
                .protein(new CompositionDTO(null, "", "g", 10f))
                .carbohydrates(new CompositionDTO(null, "", "g", 20f))
                .fat(new CompositionDTO(null, "", "g", 30f))
                .energy(new CompositionDTO(null, "", "kcal", 400f))
                .composition(new ArrayList<>())
                .foodGroup(new FoodGroupDTO(1L))
                .build();
    }
}