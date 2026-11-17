package com.prueba.seguros.bolivar.polizas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prueba.seguros.bolivar.polizas.model.CoreEventType;
import jakarta.validation.constraints.NotNull;

public record CoreEventRequest(
		@JsonProperty("evento") @NotNull CoreEventType event,
		@JsonProperty("polizaId") @NotNull Long policyId) {
}
