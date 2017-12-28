package com.foodfinder.user.repository;

import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(Long id);

    User findByEmail(String email);

    @Query("SELECT u.days FROM user u WHERE u.id = ?1")
    Set<Day> findUserDays(Long id);
}