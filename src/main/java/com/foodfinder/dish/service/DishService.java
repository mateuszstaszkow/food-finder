package com.foodfinder.dish.service;

import com.foodfinder.dish.dao.DishRepository;
import com.foodfinder.dish.domain.mapper.DishMapper;
import com.foodfinder.dish.domain.dto.DishDTO;
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
    private final DishMapper dishMapper;

    public List<DishDTO> getDishList() {
        return Optional.ofNullable(dishRepository.findAll())
                .map(dishMapper::dishListToDto)
                .orElseThrow(NotFoundException::new);
    }
}
