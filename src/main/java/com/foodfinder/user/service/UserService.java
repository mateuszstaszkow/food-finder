package com.foodfinder.user.service;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.mapper.DayMapper;
import com.foodfinder.user.domain.dto.UserDTO;
import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.domain.mapper.UserMapper;
import com.foodfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return Optional.ofNullable(userRepository.findById(id))
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
        userEntity.setId(id);
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

    public List<DayDTO> getDayList(Long id, Date date) {
        return getDayList(id).stream()
                .filter(day -> DateUtils.isSameDay(date, day.getDate()))
                .collect(Collectors.toList());
    }

    public List<DayDTO> getDayList(Long id) {
        return Optional.ofNullable(userRepository.findUserDays(id))
                .map(dayMapper::dayListToDto)
                .orElseThrow(NotFoundException::new);
    }

    public List<DayDTO> getDayList(Long id, Date from, Date to) {
        return getDayList(id).stream()
                .filter(day -> from.before(day.getDate()) && to.after(day.getDate()))
                .collect(Collectors.toList());
    }
}