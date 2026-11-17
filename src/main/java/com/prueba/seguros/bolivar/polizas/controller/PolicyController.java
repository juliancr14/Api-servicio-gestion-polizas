package com.prueba.seguros.bolivar.polizas.controller;

import com.prueba.seguros.bolivar.polizas.dto.PolicyResponse;
import com.prueba.seguros.bolivar.polizas.dto.RiskRequest;
import com.prueba.seguros.bolivar.polizas.dto.RiskResponse;
import com.prueba.seguros.bolivar.polizas.model.PolicyStatus;
import com.prueba.seguros.bolivar.polizas.model.PolicyType;
import com.prueba.seguros.bolivar.polizas.service.PolicyService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/polizas")
public class PolicyController {

	private final PolicyService policyService;

	public PolicyController(PolicyService policyService) {
		this.policyService = policyService;
	}

	@GetMapping
	public List<PolicyResponse> findPolicies(
			@RequestParam(name = "tipo", required = false) PolicyType type,
			@RequestParam(name = "estado", required = false) PolicyStatus status) {
		return policyService.findPolicies(type, status);
	}

	@GetMapping("/{id}/riesgos")
	public List<RiskResponse> findRisks(@PathVariable Long id) {
		return policyService.findRisks(id);
	}

	@PostMapping("/{id}/renovar")
	public PolicyResponse renew(@PathVariable Long id) {
		return policyService.renew(id);
	}

	@PostMapping("/{id}/cancelar")
	public PolicyResponse cancel(@PathVariable Long id) {
		return policyService.cancel(id);
	}

	@PostMapping("/{id}/riesgos")
	@ResponseStatus(HttpStatus.CREATED)
	public RiskResponse addRisk(@PathVariable Long id, @Valid @RequestBody RiskRequest request) {
		return policyService.addRisk(id, request);
	}
}
