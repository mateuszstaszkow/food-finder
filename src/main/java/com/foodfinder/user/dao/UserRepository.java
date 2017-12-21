package com.foodfinder.user.dao;

import com.foodfinder.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(Long id);

    User findByName(String name);

}