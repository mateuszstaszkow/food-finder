package com.foodfinder.day.service;

import com.foodfinder.day.dao.DayRepository;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.day.domain.mapper.DayMapper;
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
public class DayService {

    private final DayRepository dayRepository;
    private final DayMapper dayMapper;

    public List<DayDTO> getDayList(Pageable pageable) {
        return Optional.ofNullable(dayRepository.findAll(pageable))
                .map(dayMapper::dayListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public DayDTO getDay(Long id) {
        return Optional.ofNullable(dayRepository.findById(id))
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