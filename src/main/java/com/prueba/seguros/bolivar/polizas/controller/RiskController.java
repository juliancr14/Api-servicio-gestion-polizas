package com.prueba.seguros.bolivar.polizas.controller;

import com.prueba.seguros.bolivar.polizas.dto.RiskResponse;
import com.prueba.seguros.bolivar.polizas.service.RiskService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/riesgos")
public class RiskController {

	private final RiskService riskService;

	public RiskController(RiskService riskService) {
		this.riskService = riskService;
	}

	@PostMapping("/{id}/cancelar")
	public RiskResponse cancel(@PathVariable Long id) {
		return riskService.cancel(id);
	}
}
