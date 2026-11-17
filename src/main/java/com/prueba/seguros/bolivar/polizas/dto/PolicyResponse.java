package com.prueba.seguros.bolivar.polizas.dto;

import com.prueba.seguros.bolivar.polizas.model.Policy;
import com.prueba.seguros.bolivar.polizas.model.PolicyStatus;
import com.prueba.seguros.bolivar.polizas.model.PolicyType;
import java.math.BigDecimal;

public record PolicyResponse(Long id, PolicyType type, PolicyStatus status, BigDecimal canon, BigDecimal premium) {

	public static PolicyResponse from(Policy policy) {
		return new PolicyResponse(
				policy.getId(),
				policy.getType(),
				policy.getStatus(),
				policy.getCanon(),
				policy.getPremium());
	}
}
