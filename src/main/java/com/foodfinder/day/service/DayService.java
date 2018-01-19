package com.foodfinder.day.service;

import com.foodfinder.container.exceptions.service.ExceptionStasher;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.day.domain.entity.TimedDish;
import com.foodfinder.day.domain.mapper.DayMapper;
import com.foodfinder.day.repository.DayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class DayService {

    private final DayRepository dayRepository;
    private final DayMapper dayMapper;
    private final HitsService hitsService;
    private final ExceptionStasher exceptionStasher;

    @Value("${spring.jackson.date-format}")
    private String dateFormat;

    public List<DayDTO> getDayList(Pageable pageable, Date date, Date from, Date to) {
        if(date != null) {
            return getDayList(date);
        }
        if((from == null) && (to == null)) {
            return getDayList(pageable);
        }
        return getDayList(from, to);
    }

    public DayDTO getDay(Long id) {
        return Optional.ofNullable(dayRepository.findOne(id))
                .map(dayMapper::toDto)
                .map(this::sortTimedDishesByOrder)
                .orElseThrow(NotFoundException::new);
    }

    public Day postDay(DayDTO day) {
        Day dayEntity = Optional.ofNullable(day)
                .map(dayMapper::toEntity)
                .orElseThrow(BadRequestException::new);

        dayEntity = hitsService.incrementHitsForANewDay(dayEntity);

        try {
            return dayRepository.saveAndFlush(dayEntity);
        } catch (Exception exception) {
            exceptionStasher.stash(exception, "Error during day save");
            throw new BadRequestException(exception);
        }
    }

    public void updateDay(Long id, DayDTO day) {
        Day dayEntity = Optional.ofNullable(day)
                .map(dayMapper::toEntity)
                .map(d -> verifyTimedDishes(d, id))
                .orElseThrow(BadRequestException::new);
        dayEntity = hitsService.incrementHitsForAnUpdatedDay(dayEntity);

        try {
            dayRepository.save(dayEntity);
        } catch (Exception exception) {
            exceptionStasher.stash(exception, "Error during day update");
            throw new BadRequestException(exception);
        }
    }

    private List<DayDTO> getDayList(Date date) {
        String formattedDate = new SimpleDateFormat(dateFormat).format(date);

        return Optional.ofNullable(dayRepository.findByDateString(formattedDate))
                .map(dayMapper::dayListToDto)
                .map(this::sortTimedDishesByOrder)
                .orElseThrow(NotFoundException::new);
    }

    private List<DayDTO> getDayList(Pageable pageable) {
        return Optional.ofNullable(dayRepository.findAll(pageable))
                .map(dayMapper::dayListToDto)
                .map(this::sortTimedDishesByOrder)
                .orElseThrow(NotFoundException::new);
    }

    private List<DayDTO> getDayList(Date from, Date to) {
        from = (from == null) ? new Date(0) : from;
        to = (to == null) ? new Date() : to;

        return Optional.ofNullable(dayRepository.findByDateBetween(from, to))
                .map(dayMapper::dayListToDto)
                .map(this::sortTimedDishesByOrder)
                .orElseThrow(NotFoundException::new);
    }

    private Day verifyTimedDishes(Day day, Long id) {
        Map<Integer, TimedDish> timedDishes = Optional.ofNullable(dayRepository.findOne(id))
                .map(Day::getTimedDishes)
                .map(Collection::stream)
                .map(timedDishStream -> timedDishStream.collect(Collectors.toMap(TimedDish::getDishOrder, t -> t)))
                .orElse(new HashMap<>());

        day.getTimedDishes()
                .stream()
                .filter(timedDish -> timedDishes.get(timedDish.getDishOrder()) != null)
                .forEach(timedDish -> timedDish.setId(timedDishes.get(timedDish.getDishOrder()).getId()));
        day.setId(id);

        return day;
    }

    private List<DayDTO> sortTimedDishesByOrder(List<DayDTO> days) {
        return days.stream()
                .map(this::sortTimedDishesByOrder)
                .collect(Collectors.toList());
    }

    private DayDTO sortTimedDishesByOrder(DayDTO day) {
        List<TimedDishDTO> timedDishes = day.getTimedDishes()
                .stream()
                .sorted(Comparator.comparingInt(TimedDishDTO::getDishOrder))
                .collect(Collectors.toList());
        day.setTimedDishes(timedDishes);

        return day;
    }
}