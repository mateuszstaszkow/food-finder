package com.foodfinder.diet.domain;

import com.foodfinder.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    Diet findByUsers(List<User> users);
}