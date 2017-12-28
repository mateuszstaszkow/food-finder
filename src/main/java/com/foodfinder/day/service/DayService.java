package com.foodfinder.day.service;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.entity.Day;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class DayService {

    private final DayRepository dayRepository;
    private final DayMapper dayMapper;

    @Value("${food-finder.date-format}")
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

    public List<DayDTO> getDayList(Date date) {
        String formattedDate = new SimpleDateFormat(dateFormat).format(date);

        return Optional.ofNullable(dayRepository.findByDateString(formattedDate))
                .map(dayMapper::dayListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public List<DayDTO> getDayList(Pageable pageable) {
        return Optional.ofNullable(dayRepository.findAll(pageable))
                .map(dayMapper::dayListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public List<DayDTO> getDayList(Date from, Date to) {
        from = (from == null) ? new Date(0) : from;
        to = (to == null) ? new Date() : to;

        return Optional.ofNullable(dayRepository.findByDateBetween(from, to))
                .map(dayMapper::dayListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public DayDTO getDay(Long id) {
        return Optional.ofNullable(dayRepository.findOne(id))
                .map(dayMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    public void postDay(DayDTO day) {
        Day dayEntity = Optional.ofNullable(day)
                .map(dayMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        dayRepository.save(dayEntity);
    }

    public void updateDay(Long id, DayDTO day) {
        Day dayEntity = Optional.ofNullable(day)
                .map(dayMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        dayEntity.setId(id);
        dayRepository.save(dayEntity);
    }
}