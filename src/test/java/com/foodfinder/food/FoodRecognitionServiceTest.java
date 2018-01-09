package com.foodfinder.food;

import com.foodfinder.day.service.HitsService;
import com.foodfinder.food.domain.dto.*;
import com.foodfinder.food.service.FoodLiveSearchService;
import com.foodfinder.food.service.FoodRecognitionService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class FoodRecognitionServiceTest {

    @MockBean
    private FoodLiveSearchService liveSearchService;

    @MockBean
    private HitsService hitsService;

    @MockBean
    private RestTemplate restTemplate;

    private FoodRecognitionService foodRecognitionService;
    private ProductDTO productDTO;
    private List<ProductDTO> productsDTO;

    @Before
    public void setup() {
        foodRecognitionService = new FoodRecognitionService(liveSearchService, hitsService);

        productDTO = ProductDTO.builder()
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
        productsDTO = Collections.singletonList(productDTO);

        ReflectionTestUtils.setField(foodRecognitionService, "GOOGLE_VISION_URL", "http://test.com");
        ReflectionTestUtils.setField(foodRecognitionService, "GOOGLE_AUTH", "auth");
    }

    //TODO
    @Test
    @Ignore
    public void givenProductId_whenGetProduct_thenReturnDto() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test".getBytes());

        GoogleVisionLabelAnnotationDTO annotation = new GoogleVisionLabelAnnotationDTO("", "banana", 100f);
        List<GoogleVisionLabelAnnotationDTO> annotationList = Collections.singletonList(annotation);
        GoogleVisionResponseDTO response = new GoogleVisionResponseDTO(annotationList);
        List<GoogleVisionResponseDTO> responseList = Collections.singletonList(response);
        GoogleVisionMainResponseDTO mainResponse = new GoogleVisionMainResponseDTO(responseList);

        given(restTemplate.postForObject(Mockito.any(), Mockito.any(), Mockito.any()))
                .willReturn(mainResponse);
        given(liveSearchService.getOriginalProducts("banana", 10)).willReturn(productsDTO);

        assertEquals(foodRecognitionService.recognizeFood(file), productDTO);
    }

}