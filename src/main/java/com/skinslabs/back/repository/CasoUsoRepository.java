package com.skinslabs.back.repository;

import com.skinslabs.back.model.CasoUso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasoUsoRepository extends JpaRepository<CasoUso, Long> {
} 