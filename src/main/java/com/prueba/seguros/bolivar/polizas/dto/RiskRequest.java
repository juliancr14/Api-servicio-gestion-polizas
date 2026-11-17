package com.prueba.seguros.bolivar.polizas.dto;

import jakarta.validation.constraints.NotBlank;

public record RiskRequest(@NotBlank String description) {
}
