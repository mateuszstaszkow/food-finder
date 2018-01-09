package com.foodfinder.integration.migrator;

import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.migrator.domain.dto.*;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class MigratorIntegrationTest extends IntegrationTestSetup {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenResponse_whenMigrateGroups_thenAddFoodGroupToDatabase() throws Exception {
        UsdaGroupResponseDTO mainGroupResponse = getUsdaGroupResponseDTO();
        String jsonGroup = mapper.writeValueAsString(mainGroupResponse);

        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlPathMatching("/groups/*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonGroup)));

        mockMvc.perform(post("/api/migrate/groups"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/groups?name=food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("food group")));
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenResponse_whenMigrateProducts_thenAddProductToDatabase() throws Exception {
        UsdaGroupResponseDTO mainGroupResponse = getUsdaGroupResponseDTO();
        String jsonGroup = mapper.writeValueAsString(mainGroupResponse);
        UsdaFoodResponseDTO mainFoodResponse = getUsdaFoodResponseDTO();
        String jsonProduct = mapper.writeValueAsString(mainFoodResponse);

        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlPathMatching("/groups/*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonGroup)));
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlPathMatching("/products/*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonProduct)));

        mockMvc.perform(post("/api/migrate/products"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/products?name=Banana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Bananas, raw")));
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenResponse_whenMigrateAll_thenAddProductToDatabase() throws Exception {
        UsdaGroupResponseDTO mainGroupResponse = getUsdaGroupResponseDTO();
        String jsonGroup = mapper.writeValueAsString(mainGroupResponse);
        UsdaFoodResponseDTO mainFoodResponse = getUsdaFoodResponseDTO();
        String jsonProduct = mapper.writeValueAsString(mainFoodResponse);

        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlPathMatching("/groups/*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonGroup)));
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlPathMatching("/products/*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonProduct)));

        mockMvc.perform(post("/api/migrate/all"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/products?name=Banana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Bananas, raw")));
    }

    private UsdaGroupResponseDTO getUsdaGroupResponseDTO() throws Exception {
        FoodGroupResponseDTO foodGroupResponseDTO = new FoodGroupResponseDTO(0,"1","food group");
        List<FoodGroupResponseDTO> item = Collections.singletonList(foodGroupResponseDTO);
        ListDTO groupListDTO = ListDTO.builder()
                .item(item)
                .build();
        return new UsdaGroupResponseDTO(groupListDTO);
    }

    private UsdaFoodResponseDTO getUsdaFoodResponseDTO() throws Exception {
        List<CompositionResponseDTO> nutrients = getNutrients();
        ProductResponseDTO food = ProductResponseDTO.builder()
                .ndbno(null)
                .name("Bananas, raw")
                .weight(100f)
                .measure("g")
                .nutrients(nutrients)
                .description("Very good bananas")
                .shortDescription("Good bananas")
                .foodGroup(new FoodGroupDTO(1L, "food group"))
                .build();
        List<ProductResponseDTO> foods = Collections.singletonList(food);
        FoodGroupReportDTO group = new FoodGroupReportDTO(100L,"food group");
        List<FoodGroupReportDTO> groups = Collections.singletonList(group);
        ReportDTO reportDTO = ReportDTO.builder()
                .sr(1)
                .groups(groups)
                .subset("subset")
                .end(1)
                .start(1)
                .total(1)
                .foods(foods)
                .build();
        return new UsdaFoodResponseDTO(reportDTO);
    }

    private List<CompositionResponseDTO> getNutrients() throws Exception {
        CompositionResponseDTO protein = CompositionResponseDTO.builder()
                .nutrient_id(null)
                .nutrient("Protein")
                .unit("g")
                .value(101f)
                .gm(11f)
                .build();
        CompositionResponseDTO carbohydrates = CompositionResponseDTO.builder()
                .nutrient_id(null)
                .nutrient("Carbohydrate, by difference")
                .unit("g")
                .value(102f)
                .gm(12f)
                .build();
        CompositionResponseDTO fat = CompositionResponseDTO.builder()
                .nutrient_id(null)
                .nutrient("Total lipid (fat)")
                .unit("g")
                .value(103f)
                .gm(13f)
                .build();
        CompositionResponseDTO energy = CompositionResponseDTO.builder()
                .nutrient_id(null)
                .nutrient("Energy")
                .unit("g")
                .value(104f)
                .gm(14f)
                .build();
        return Arrays.asList(protein, carbohydrates, fat, energy);
    }
}
