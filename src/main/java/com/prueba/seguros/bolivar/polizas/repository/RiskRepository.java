package com.prueba.seguros.bolivar.polizas.repository;

import com.prueba.seguros.bolivar.polizas.model.Risk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskRepository extends JpaRepository<Risk, Long> {
}
