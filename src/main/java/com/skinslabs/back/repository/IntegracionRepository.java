package com.skinslabs.back.repository;

import com.skinslabs.back.model.Integracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegracionRepository extends JpaRepository<Integracion, Long> {
} 