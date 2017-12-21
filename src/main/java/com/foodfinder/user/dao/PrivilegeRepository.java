package com.foodfinder.user.dao;

import com.foodfinder.user.domain.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

}
