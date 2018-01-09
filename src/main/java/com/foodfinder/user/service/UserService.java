package com.foodfinder.user.service;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.day.domain.entity.TimedDish;
import com.foodfinder.day.domain.mapper.DayMapper;
import com.foodfinder.day.service.DayService;
import com.foodfinder.user.domain.dto.BasicUserDTO;
import com.foodfinder.user.domain.dto.UserDTO;
import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.domain.mapper.UserMapper;
import com.foodfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DayMapper dayMapper;
    private final DayService dayService;

    public List<UserDTO> getUserList(Pageable pageable) {
        return Optional.ofNullable(userRepository.findAll(pageable))
                .map(userMapper::userListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public UserDTO getUser(Long id) {
        return Optional.ofNullable(userRepository.findOne(id))
                .map(userMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    public void postUser(UserDTO user) {
        User userEntity = Optional.ofNullable(user)
                .map(userMapper::toEntity)
                .orElseThrow(BadRequestException::new);
        userRepository.save(userEntity);
    }

    public void updateUser(Long id, UserDTO user) {
        User userEntity = Optional.ofNullable(user)
                .map(userMapper::toEntity)
                .orElseThrow(BadRequestException::new);

        User dbUser = userRepository.findOne(id);
        userEntity.setId(id);
        userEntity.setPassword(dbUser.getPassword());

        userRepository.save(userEntity);
    }

    public void updateBasicUser(Long id, BasicUserDTO user) {
        User userEntity = Optional.ofNullable(user)
                .map(userMapper::toEntity)
                .orElseThrow(BadRequestException::new);

        User dbUser = userRepository.findOne(id);
        userEntity.setId(id);
        userEntity.setPassword(dbUser.getPassword());
        userEntity.setEnabled(dbUser.getEnabled());
        userEntity.setRole(dbUser.getRole());

        userRepository.save(userEntity);
    }

    public List<DayDTO> getUserDays(Long id, Date from, Date to) {
        if((from == null) && (to == null)) {
            return getDayList(id);
        }
        from = (from == null) ? new Date(0) : from;
        to = (to == null) ? new Date() : to;

        return getDayList(id, from, to);
    }

    public DayDTO getUserDay(Long id, Date date) {
        Date from = getDateFrom(date);
        Date to = getDateTo(date);

        List<DayDTO> days = getDayList(id, from, to);

        if(days.isEmpty()) {
            return null;
        }

        return days.get(0);
    }

    public ResponseEntity<?> addOrUpdateUserDay(Long id, DayDTO dayDTO) {
        Date date = dayDTO.getDate();
        Date from = getDateFrom(date);
        Date to = getDateTo(date);
        List<Day> userDay = userRepository.findUserDayByDateBetween(id, from, to);

        if(userDay.isEmpty()) {
            dayService.postDay(dayDTO);
            addDayToUser(id, dayDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        dayService.updateDay(userDay.get(0).getId(), dayDTO);
        addDayToUser(id, dayDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addDayToUser(Long id, DayDTO dayDTO) {
        if(dayDTO == null || id == null) {
            return;
        }

        User user = userRepository.getOne(id);
        Set<Day> days = user.getDays() == null ? new HashSet<>() : user.getDays();
        Boolean dayNotExists = days.stream()
                .filter(day -> DateUtils.isSameDay(dayDTO.getDate(), day.getDate()))
                .collect(Collectors.toList())
                .isEmpty();

        if(dayNotExists) {
            days.add(dayMapper.toEntity(dayDTO));
            user.setDays(days);
            userRepository.save(user);
        }
    }

    private List<DayDTO> getDayList(Long id) {
        return Optional.ofNullable(userRepository.findUserDays(id))
                .map(dayMapper::dayListToDto)
                .map(this::sortTimedDishesByOrder)
                .orElseThrow(NotFoundException::new);
    }

    private List<DayDTO> getDayList(Long id, Date from, Date to) {
        return Optional.ofNullable(userRepository.findUserDaysByDateBetween(id, from, to))
                .map(dayMapper::dayListToDto)
                .map(this::sortTimedDishesByOrder)
                .orElseThrow(NotFoundException::new);
    }

    private Date getDateFrom(Date date) {
        return getDateByHms(date, 0, 0, 0);
    }

    private Date getDateTo(Date date) {
        return getDateByHms(date, 23, 59, 59);
    }

    private Date getDateByHms(Date date, int hours, int minutes, int seconds) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);

        return calendar.getTime();
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