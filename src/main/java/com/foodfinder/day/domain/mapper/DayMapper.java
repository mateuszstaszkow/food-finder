package com.foodfinder.day.domain.mapper;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.day.domain.entity.TimedDish;
import com.foodfinder.dish.domain.mapper.DishMapper;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = DishMapper.class)
public interface DayMapper {

    Day toEntity(DayDTO dayDTO);

    DayDTO toDto(Day day);

    TimedDish toEntity(TimedDishDTO timedDishDTO);

    TimedDishDTO toDto(TimedDish timedDish);

    List<DayDTO> dayListToDto(Page<Day> productPage);
}
