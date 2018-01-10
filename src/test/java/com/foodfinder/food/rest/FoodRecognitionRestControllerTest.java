package com.foodfinder.food.rest;

import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.food.domain.dto.CompositionDTO;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.service.FoodRecognitionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FoodRecognitionRestController.class)
@EnableSpringDataWebSupport
public class FoodRecognitionRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FoodRecognitionService foodRecognitionService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser
    public void givenFile_whenRecognize_thenReturnJson() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test".getBytes());

        ProductDTO product = ProductDTO.builder()
                .id(1L)
                .name("banana")
                .weight(10f)
                .measure("1.0 tbsp")
                .protein(new CompositionDTO())
                .carbohydrates(new CompositionDTO())
                .fat(new CompositionDTO())
                .energy(new CompositionDTO())
                .composition(new ArrayList<>())
                .foodGroup(new FoodGroupDTO())
                .build();

        given(foodRecognitionService.recognizeFood(file)).willReturn(product);

        mvc.perform(fileUpload("/api/recognize")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(product.getName())));
    }

    @Test
    public void givenUnauthorizedUser_thenReturnStatusUnauthorized() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test".getBytes());

        mvc.perform(fileUpload("/api/recognize")
                .file(file))
                .andExpect(status().isUnauthorized());
    }
}
