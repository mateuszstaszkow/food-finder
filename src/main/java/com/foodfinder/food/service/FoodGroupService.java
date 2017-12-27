package com.foodfinder.food.service;

import com.foodfinder.food.repository.FoodGroupRepository;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.food.domain.mapper.FoodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class FoodGroupService {

    private final FoodGroupRepository foodGroupRepository;
    private final FoodMapper foodMapper;
    private final FoodLiveSearchService liveSearchService;

    private static final int LIVE_SEARCH_PAGE_SIZE = 10;

    public List<FoodGroupDTO> getFoodGroupList(Pageable pageable, String name) {
        if(name == null) {
            return getFoodGroupList(pageable);
        }
        return liveSearchService.getFoodGroups(name, LIVE_SEARCH_PAGE_SIZE);
    }

    public List<FoodGroupDTO> getFoodGroupList(Pageable pageable) {
        return Optional.ofNullable(foodGroupRepository.findAll(pageable))
                .map(foodMapper::foodGroupListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public FoodGroupDTO getFoodGroup(Long id) {
        return Optional.ofNullable(foodGroupRepository.findById(id))
                .map(foodMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    public void postFoodGroup(FoodGroupDTO foodGroup) {
        FoodGroup foodGroupEntity = Optional.ofNullable(foodGroup)
                .map(foodMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        foodGroupRepository.save(foodGroupEntity);
    }

    public void updateFoodGroup(Long id, FoodGroupDTO foodGroup) {
        FoodGroup foodGroupEntity = Optional.ofNullable(foodGroup)
                .map(foodMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        foodGroupEntity.setId(id);
        foodGroupRepository.save(foodGroupEntity);
    }
}