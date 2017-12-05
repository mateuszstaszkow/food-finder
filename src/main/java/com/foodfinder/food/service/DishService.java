package com.foodfinder.food.service;

import com.foodfinder.food.dao.DishRepository;
import com.foodfinder.food.domain.mapper.FoodMapper;
import com.foodfinder.food.domain.dto.DishDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class DishService {

    private final DishRepository dishRepository;
    private final FoodMapper foodMapper;

    public List<DishDTO> getDishList() {
        return Optional.ofNullable(dishRepository.findAll())
                .map(foodMapper::dishListToDto)
                .orElseThrow(NotFoundException::new);
    }
}
