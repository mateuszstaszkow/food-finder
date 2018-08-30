package com.foodfinder.user.service;

import com.foodfinder.container.exceptions.service.ExceptionStasher;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.day.domain.mapper.DayMapper;
import com.foodfinder.day.service.DayService;
import com.foodfinder.user.domain.dto.BasicUserDTO;
import com.foodfinder.user.domain.dto.UserDTO;
import com.foodfinder.user.domain.entity.Pal;
import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.domain.mapper.UserMapper;
import com.foodfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    private final ExceptionStasher exceptionStasher;

    private static final String GENDER_MAN = "male";
    private static final String GENDER_WOMAN = "female";

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

        try {
            userRepository.save(userEntity);
        } catch (Exception exception) {
            exceptionStasher.stash(exception, "Error during user save");
            throw new BadRequestException(exception);
        }
    }

    public void updateUser(Long id, UserDTO user) {
        User userEntity = Optional.ofNullable(user)
                .map(userMapper::toEntity)
                .orElseThrow(BadRequestException::new);

        User dbUser = userRepository.findOne(id);
        userEntity.setId(id);
        userEntity.setPassword(dbUser.getPassword());

        try {
            userRepository.save(userEntity);
        } catch (Exception exception) {
            exceptionStasher.stash(exception, "Error during user save");
            throw new BadRequestException(exception);
        }
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
        userEntity.setDays(dbUser.getDays());

        if(isEnergyUpdated(userEntity, dbUser)) {
            userEntity.setDailyEnergy(calculateEnergy(userEntity).intValue());
        }

        try {
            userRepository.save(userEntity);
        } catch (Exception exception) {
            exceptionStasher.stash(exception, "Error during basic user update");
            throw new BadRequestException(exception);
        }
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
            throw new NotFoundException();
        }

        return days.get(0);
    }

    public ResponseEntity<?> addOrUpdateUserDay(Long id, DayDTO dayDTO) {
        Date date = dayDTO.getDate();
        Date from = getDateFrom(date);
        Date to = getDateTo(date);
        List<Day> userDay = userRepository.findUserDayByDateBetween(id, from, to);

        if(userDay.isEmpty()) {
            Day dayEntity = dayService.postDay(dayDTO);
            addDayToUser(id, dayEntity);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        dayService.updateDay(userDay.get(0).getId(), dayDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Float calculateEnergy(User user) {
        Float pal = Optional.ofNullable(user.getPal())
                .map(Pal::getValue)
                .orElse(1f);

        if(user.getGender().equals(GENDER_WOMAN)) {
            return pal * harrisBenedictEquation(user, 655.1f, 9.567f, 1.85f, 4.68f);
        } else if(user.getGender().equals(GENDER_MAN)) {
            return pal * harrisBenedictEquation(user, 66.47f, 13.7f, 5f, 6.76f);
        }

        throw new BadRequestException("Invalid gender");
    }

    private Float harrisBenedictEquation(User user, Float a, Float b, Float c, Float d) {
        return a + b * user.getWeight() + c * user.getHeight() - d * user.getAge();
    }

    private Boolean isEnergyUpdated(User userEntity, User dbUser) {
        Boolean weightChanged = !userEntity.getWeight().equals(dbUser.getWeight());
        Boolean heightChanged = !userEntity.getHeight().equals(dbUser.getHeight());
        Boolean ageChanged = !userEntity.getAge().equals(dbUser.getAge());
        Boolean genderChanged = !userEntity.getGender().equals(dbUser.getGender());

        return weightChanged || heightChanged || ageChanged || genderChanged;
    }

    private void addDayToUser(Long userId, Day newDay) {
        if(newDay == null || userId == null) {
            return;
        }
        Optional.ofNullable(userRepository.getOne(userId))
                .map(dbUser -> appendDayToUser(dbUser, newDay))
                .map(userRepository::save);
    }

    private User appendDayToUser(User user, Day newDay) {
        Set<Day> userDays = user.getDays();
        userDays.add(newDay);
        user.setDays(userDays);
        return user;
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
        Date dayFirstDate = getDateByHms(date, 0, 0, 0);
        return new Date(dayFirstDate.getTime() - 1000);
    }

    private Date getDateTo(Date date) {
        Date dayLastDate = getDateByHms(date, 23, 59, 59);
        return new Date(dayLastDate.getTime() + 1000);
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