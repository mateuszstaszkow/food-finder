package com.foodfinder.day.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DayDTO {

    private Long id;
    private Date date;
    private List<TimedDishDTO> timedDishes;
}
