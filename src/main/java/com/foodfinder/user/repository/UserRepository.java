package com.foodfinder.user.repository;

import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query("SELECT u.days FROM user u WHERE u.id = ?1")
    Set<Day> findUserDays(Long id);

    @Query("SELECT d FROM user u JOIN u.days d WHERE u.id = ?1 AND d.date BETWEEN ?2 AND ?3")
    Set<Day> findUserDaysByDateBetween(Long id, Date from, Date to);
}