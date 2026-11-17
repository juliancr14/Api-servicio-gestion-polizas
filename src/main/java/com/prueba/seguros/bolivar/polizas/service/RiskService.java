package com.prueba.seguros.bolivar.polizas.service;

import com.prueba.seguros.bolivar.polizas.dto.RiskResponse;
import com.prueba.seguros.bolivar.polizas.exception.ResourceNotFoundException;
import com.prueba.seguros.bolivar.polizas.model.CoreEventType;
import com.prueba.seguros.bolivar.polizas.model.Risk;
import com.prueba.seguros.bolivar.polizas.repository.RiskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RiskService {

	private final RiskRepository riskRepository;
	private final CoreNotifier coreNotifier;

	public RiskService(RiskRepository riskRepository, CoreNotifier coreNotifier) {
		this.riskRepository = riskRepository;
		this.coreNotifier = coreNotifier;
	}

	@Transactional
	public RiskResponse cancel(Long riskId) {
		Risk risk = riskRepository.findById(riskId)
				.orElseThrow(() -> new ResourceNotFoundException("Risk " + riskId + " not found"));
		risk.cancel();
		coreNotifier.notify(CoreEventType.ACTUALIZACION, risk.getPolicy().getId());
		return RiskResponse.from(risk);
	}
}
