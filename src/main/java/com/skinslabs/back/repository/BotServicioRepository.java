package com.skinslabs.back.repository;

import com.skinslabs.back.model.BotServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotServicioRepository extends JpaRepository<BotServicio, Long> {
} 