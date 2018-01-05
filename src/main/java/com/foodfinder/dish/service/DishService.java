package com.foodfinder.dish.service;

import com.foodfinder.dish.repository.DishRepository;
import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.dish.domain.entity.Dish;
import com.foodfinder.dish.domain.mapper.DishMapper;
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
public class DishService {

    private final DishRepository dishRepository;
    private final DishLiveSearchService liveSearchService;
    private final DishMapper dishMapper;

    private static final int LIVE_SEARCH_PAGE_SIZE = 10;

    public List<DishDTO> getDishList(Pageable pageable, String name) {
        if(name == null) {
            return getDishList(pageable);
        }
        return liveSearchService.getDishes(name, LIVE_SEARCH_PAGE_SIZE);
    }

    public DishDTO getDish(Long id) {
        return Optional.ofNullable(dishRepository.findOne(id))
                .map(dishMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    public void postDish(DishDTO dish) {
        Dish dishEntity = Optional.ofNullable(dish)
                .map(dishMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        dishEntity.setHits(0L);
        dishRepository.save(dishEntity);
    }

    public void updateDish(Long id, DishDTO dish) {
        Dish dishEntity = Optional.ofNullable(dish)
                .map(dishMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        dishEntity.setId(id);
        dishRepository.save(dishEntity);
    }

    private List<DishDTO> getDishList(Pageable pageable) {
        return Optional.ofNullable(dishRepository.findAll(pageable))
                .map(dishMapper::dishListToDto)
                .orElseThrow(NotFoundException::new);
    }
}
