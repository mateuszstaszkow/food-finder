package com.foodfinder.day.domain.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DayDTO {

    private Long id;
    private String name;
    private Date date;
    private List<TimedDishDTO> timedDishes;
}
