package com.foodfinder.food;

import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.food.domain.mapper.FoodMapper;
import com.foodfinder.food.repository.FoodGroupRepository;
import com.foodfinder.food.service.FoodGroupService;
import com.foodfinder.food.service.FoodLiveSearchService;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class FoodGroupServiceTest {

    @MockBean
    private FoodGroupRepository foodGroupRepository;

    @MockBean
    private FoodMapper foodMapper;

    @MockBean
    private FoodLiveSearchService foodLiveSearchService;

    private FoodGroupService foodGroupService;
    private PageRequest defaultPageRequest;
    private FoodGroup foodGroup;
    private FoodGroupDTO foodGroupDTO;
    private List<FoodGroupDTO> foodGroupsDTO;
    private Page<FoodGroup> foodGroupsPage;

    @Before
    public void setup() {
        foodGroupService = new FoodGroupService(foodGroupRepository, foodLiveSearchService, foodMapper);
        defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        foodGroup = new FoodGroup(1L, "Test products", "Testowe produkty");
        foodGroupsPage = new PageImpl<>(Collections.singletonList(foodGroup));

        foodGroupDTO = new FoodGroupDTO(1L, "Test products");
        foodGroupsDTO = Collections.singletonList(foodGroupDTO);
    }

    @Test
    public void givenFoodGroupId_whenGetFoodGroup_thenReturnDto() throws Exception {
        given(foodGroupRepository.findOne(1L)).willReturn(foodGroup);
        given(foodMapper.toDto(foodGroup)).willReturn(foodGroupDTO);

        assertEquals(foodGroupService.getFoodGroup(1L), foodGroupDTO);
    }

    @Test
    public void whenGetFoodGroups_thenReturnDtoList() throws Exception {
        given(foodGroupRepository.findAll(defaultPageRequest)).willReturn(foodGroupsPage);
        given(foodMapper.foodGroupListToDto(foodGroupsPage)).willReturn(foodGroupsDTO);

        assertEquals(foodGroupService.getFoodGroupList(defaultPageRequest, null), foodGroupsDTO);
    }

    @Test
    public void givenName_whenGetFoodGroups_thenReturnDtoList() throws Exception {
        given(foodLiveSearchService.getFoodGroups("Test", 10)).willReturn(foodGroupsDTO);

        assertEquals(foodGroupService.getFoodGroupList(defaultPageRequest, "Test"), foodGroupsDTO);
    }
}