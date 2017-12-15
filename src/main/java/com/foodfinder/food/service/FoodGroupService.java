package com.foodfinder.food.service;

import com.foodfinder.food.dao.FoodGroupRepository;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.food.domain.mapper.FoodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${food-finder.english-flag}")
    private String englishFlag;

    public List<FoodGroupDTO> getFoodGroupList(Pageable pageable, String name, String language) {
        if(name == null) {
            return getFoodGroupList(pageable);
        } else if(language == null || language.equals(englishFlag)) {
            return getFoodGroupsLiveSearch(name);
        }
        return getTranslatedFoodGroupsLiveSearch(name);
    }

    public List<FoodGroupDTO> getFoodGroupList(Pageable pageable) {
        return Optional.ofNullable(foodGroupRepository.findAll(pageable))
                .map(foodMapper::foodGroupListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public List<FoodGroupDTO> getFoodGroupsLiveSearch(String name) {
        return Optional.ofNullable(foodGroupRepository.findTop10ByNameContaining(name))
                .map(foodMapper::foodGroupListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public List<FoodGroupDTO> getTranslatedFoodGroupsLiveSearch(String name) {
        return Optional.ofNullable(foodGroupRepository.findTop10ByTranslatedNameContaining(name))
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