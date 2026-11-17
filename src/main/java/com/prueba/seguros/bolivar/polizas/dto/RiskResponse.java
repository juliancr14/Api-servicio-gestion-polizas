package com.prueba.seguros.bolivar.polizas.dto;

import com.prueba.seguros.bolivar.polizas.model.Risk;
import com.prueba.seguros.bolivar.polizas.model.RiskStatus;

public record RiskResponse(Long id, String description, RiskStatus status, Long policyId) {

	public static RiskResponse from(Risk risk) {
		return new RiskResponse(
				risk.getId(),
				risk.getDescription(),
				risk.getStatus(),
				risk.getPolicy().getId());
	}
}
