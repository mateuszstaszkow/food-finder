package com.foodfinder.integration.food;

import com.foodfinder.food.domain.dto.*;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class FoodRecognitionIntegrationTest extends IntegrationTestSetup {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProduct_whenPostProduct_thenReturnsStatusCreated() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test".getBytes());

        GoogleVisionLabelAnnotationDTO annotation = new GoogleVisionLabelAnnotationDTO("", "Banana", 100f);
        List<GoogleVisionLabelAnnotationDTO> annotationList = Collections.singletonList(annotation);
        GoogleVisionResponseDTO response = new GoogleVisionResponseDTO(annotationList);
        List<GoogleVisionResponseDTO> responseList = Collections.singletonList(response);
        GoogleVisionMainResponseDTO mainResponse = new GoogleVisionMainResponseDTO(responseList);
        String jsonProduct = mapper.writeValueAsString(mainResponse);

        stubFor(post(urlPathMatching("/recognize/*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonProduct)));

        ProductDTO product = ProductDTO.builder()
                .id(1L)
                .name("Bananas, raw")
                .weight(10f)
                .measure("1.0 tbsp")
                .protein(new CompositionDTO())
                .carbohydrates(new CompositionDTO())
                .fat(new CompositionDTO())
                .energy(new CompositionDTO())
                .composition(new ArrayList<>())
                .foodGroup(new FoodGroupDTO())
                .build();

        mockMvc.perform(fileUpload("/api/recognize")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(product.getName())));
    }
}
