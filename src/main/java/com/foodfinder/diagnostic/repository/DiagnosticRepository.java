package com.foodfinder.diagnostic.repository;

import com.foodfinder.diagnostic.domain.entity.Diagnostic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticRepository extends JpaRepository<Diagnostic, Long> {

}