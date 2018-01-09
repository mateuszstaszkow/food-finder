package com.foodfinder.user.repository;

import com.foodfinder.user.domain.entity.Pal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PalRepository extends JpaRepository<Pal, String> {

}