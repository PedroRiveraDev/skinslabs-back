package com.skinslabs.back.repository;

import com.skinslabs.back.model.FlujoAutomatizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlujoAutomatizadoRepository extends JpaRepository<FlujoAutomatizado, Long> {
} 