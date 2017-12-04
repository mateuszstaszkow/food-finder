package com.foodfinder.food;

import com.foodfinder.food.domain.DishRepository;
import com.foodfinder.food.domain.FoodMapper;
import com.foodfinder.food.dto.DishDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DishService {

    private final DishRepository dishRepository;
    private final FoodMapper foodMapper;

    public List<DishDTO> getDishList() {
        return Optional.ofNullable(dishRepository.findAll())
                .map(foodMapper::dishListToDto)
                .orElseThrow(NotFoundException::new);
    }
}
