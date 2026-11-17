package com.prueba.seguros.bolivar.polizas.service;

import com.prueba.seguros.bolivar.polizas.dto.PolicyResponse;
import com.prueba.seguros.bolivar.polizas.dto.RiskRequest;
import com.prueba.seguros.bolivar.polizas.dto.RiskResponse;
import com.prueba.seguros.bolivar.polizas.exception.ResourceNotFoundException;
import com.prueba.seguros.bolivar.polizas.model.CoreEventType;
import com.prueba.seguros.bolivar.polizas.model.Policy;
import com.prueba.seguros.bolivar.polizas.model.PolicyStatus;
import com.prueba.seguros.bolivar.polizas.model.PolicyType;
import com.prueba.seguros.bolivar.polizas.model.Risk;
import com.prueba.seguros.bolivar.polizas.repository.PolicyRepository;
import com.prueba.seguros.bolivar.polizas.repository.RiskRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PolicyService {

	private final PolicyRepository policyRepository;
	private final RiskRepository riskRepository;
	private final CoreNotifier coreNotifier;
	private final BigDecimal ipcPercentage;

	public PolicyService(
			PolicyRepository policyRepository,
			RiskRepository riskRepository,
			CoreNotifier coreNotifier,
			@Value("${app.ipc-percentage}") BigDecimal ipcPercentage) {
		this.policyRepository = policyRepository;
		this.riskRepository = riskRepository;
		this.coreNotifier = coreNotifier;
		this.ipcPercentage = ipcPercentage;
	}

	@Transactional(readOnly = true)
	public List<PolicyResponse> findPolicies(PolicyType type, PolicyStatus status) {
		return policyRepository.findByTypeAndStatus(type, status).stream()
				.map(PolicyResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<RiskResponse> findRisks(Long policyId) {
		return findPolicy(policyId).getRisks().stream()
				.map(RiskResponse::from)
				.toList();
	}

	@Transactional
	public PolicyResponse renew(Long policyId) {
		Policy policy = findPolicy(policyId);
		policy.renew(ipcPercentage);
		coreNotifier.notify(CoreEventType.RENOVACION, policyId);
		return PolicyResponse.from(policy);
	}

	@Transactional
	public PolicyResponse cancel(Long policyId) {
		Policy policy = findPolicy(policyId);
		policy.cancel();
		coreNotifier.notify(CoreEventType.CANCELACION, policyId);
		return PolicyResponse.from(policy);
	}

	@Transactional
	public RiskResponse addRisk(Long policyId, RiskRequest request) {
		Policy policy = findPolicy(policyId);
		Risk risk = riskRepository.save(policy.addRisk(request.description()));
		coreNotifier.notify(CoreEventType.ACTUALIZACION, policyId);
		return RiskResponse.from(risk);
	}

	private Policy findPolicy(Long policyId) {
		return policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Policy " + policyId + " not found"));
	}
}
