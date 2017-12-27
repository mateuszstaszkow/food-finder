package com.foodfinder.dish.domain.mapper;

import com.foodfinder.dish.domain.dto.DishDTO;
import com.foodfinder.dish.domain.entity.Dish;
import com.foodfinder.food.domain.mapper.FoodMapper;
import com.foodfinder.food.service.FoodTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DishMapper {

    private final FoodTranslationService foodTranslationService;
    private final FoodMapper foodMapper;

    public Dish toEntity(DishDTO dishDTO) {
        if ( dishDTO == null ) {
            return null;
        }

        Dish dish = new Dish();

        dish.setId( dishDTO.getId() );
        dish.setProducts( foodMapper.productListToEntitySet( dishDTO.getProducts() ) );
        dish.setHits( dishDTO.getHits() );
        if(foodTranslationService.isPolishLanguage()) {
            dish.setTranslatedName( dishDTO.getName() );
            dish.setTranslatedDescription( dishDTO.getDescription() );
        } else {
            dish.setName( dishDTO.getName() );
            dish.setDescription( dishDTO.getDescription() );
        }

        return dish;
    }

    public DishDTO toDto(Dish dish) {
        if ( dish == null ) {
            return null;
        }

        DishDTO dishDTO = new DishDTO();

        dishDTO.setId( dish.getId() );
        dishDTO.setProducts( foodMapper.productListToDto( dish.getProducts() ) );
        dishDTO.setHits( dish.getHits() );
        if(foodTranslationService.isPolishLanguage()) {
            dishDTO.setName( dish.getTranslatedName() );
            dishDTO.setDescription( dish.getDescription() );
        } else {
            dishDTO.setName( dish.getName() );
            dishDTO.setDescription( dish.getDescription() );
        }

        return dishDTO;
    }

    public List<DishDTO> dishListToDto(List<Dish> dishList) {
        if ( dishList == null ) {
            return null;
        }

        List<DishDTO> list = new ArrayList<>( dishList.size() );
        for ( Dish dish : dishList ) {
            list.add( toDto( dish ) );
        }

        return list;
    }

    public List<DishDTO> dishListToDto(Page<Dish> dishList) {
        if ( dishList == null ) {
            return null;
        }

        List<DishDTO> list = new ArrayList<>();
        for ( Dish dish : dishList ) {
            list.add( toDto( dish ) );
        }

        return list;
    }
}
