package com.foodfinder.user.service;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.mapper.DayMapper;
import com.foodfinder.user.domain.dto.BasicUserDTO;
import com.foodfinder.user.domain.dto.UserDTO;
import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.domain.mapper.UserMapper;
import com.foodfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DayMapper dayMapper;

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

    public List<DayDTO> getUserDays(Long id, Date date, Date from, Date to) {
        if(date != null) {
            return getDayList(id, date);
        }
        if((from == null) && (to == null)) {
            return getDayList(id);
        }
        from = (from == null) ? new Date(0) : from;
        to = (to == null) ? new Date() : to;

        return getDayList(id, from, to);
    }

    private List<DayDTO> getDayList(Long id, Date date) {
        Date from = getDateByHms(date, 0, 0, 0);
        Date to = getDateByHms(date, 23, 59, 59);

        return getDayList(id, from, to);
    }

    private List<DayDTO> getDayList(Long id) {
        return Optional.ofNullable(userRepository.findUserDays(id))
                .map(dayMapper::dayListToDto)
                .orElseThrow(NotFoundException::new);
    }

    private List<DayDTO> getDayList(Long id, Date from, Date to) {
        return Optional.ofNullable(userRepository.findUserDaysByDateBetween(id, from, to))
                .map(dayMapper::dayListToDto)
                .orElseThrow(NotFoundException::new);
    }

    private Date getDateByHms(Date date, int hours, int minutes, int seconds) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);

        return calendar.getTime();
    }
}