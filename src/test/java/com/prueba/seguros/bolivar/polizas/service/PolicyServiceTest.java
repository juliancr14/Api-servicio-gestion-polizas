package com.prueba.seguros.bolivar.polizas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.prueba.seguros.bolivar.polizas.dto.PolicyResponse;
import com.prueba.seguros.bolivar.polizas.dto.RiskRequest;
import com.prueba.seguros.bolivar.polizas.dto.RiskResponse;
import com.prueba.seguros.bolivar.polizas.exception.BusinessRuleException;
import com.prueba.seguros.bolivar.polizas.exception.ResourceNotFoundException;
import com.prueba.seguros.bolivar.polizas.model.CoreEventType;
import com.prueba.seguros.bolivar.polizas.model.Policy;
import com.prueba.seguros.bolivar.polizas.model.PolicyStatus;
import com.prueba.seguros.bolivar.polizas.model.PolicyType;
import com.prueba.seguros.bolivar.polizas.model.Risk;
import com.prueba.seguros.bolivar.polizas.model.RiskStatus;
import com.prueba.seguros.bolivar.polizas.repository.PolicyRepository;
import com.prueba.seguros.bolivar.polizas.repository.RiskRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

	private static final Long POLICY_ID = 1L;
	private static final BigDecimal IPC_PERCENTAGE = new BigDecimal("10");

	@Mock
	private PolicyRepository policyRepository;

	@Mock
	private RiskRepository riskRepository;

	@Mock
	private CoreNotifier coreNotifier;

	private PolicyService policyService;

	@BeforeEach
	void setUp() {
		policyService = new PolicyService(policyRepository, riskRepository, coreNotifier, IPC_PERCENTAGE);
	}

	@Test
	void renewIncreasesCanonAndPremiumByIpcAndMarksPolicyAsRenewed() {
		givenPolicy(newPolicy(PolicyType.INDIVIDUAL));

		PolicyResponse response = policyService.renew(POLICY_ID);

		assertThat(response.canon()).isEqualByComparingTo("1100.00");
		assertThat(response.premium()).isEqualByComparingTo("220.00");
		assertThat(response.status()).isEqualTo(PolicyStatus.RENOVADA);
		verify(coreNotifier).notify(CoreEventType.RENOVACION, POLICY_ID);
	}

	@Test
	void renewRejectsCancelledPolicy() {
		Policy policy = newPolicy(PolicyType.INDIVIDUAL);
		policy.cancel();
		givenPolicy(policy);

		assertThatThrownBy(() -> policyService.renew(POLICY_ID)).isInstanceOf(BusinessRuleException.class);
		verifyNoInteractions(coreNotifier);
	}

	@Test
	void cancelCancelsPolicyAndAllItsRisks() {
		Policy policy = newPolicy(PolicyType.COLECTIVA);
		policy.addRisk("Warehouse");
		policy.addRisk("Office");
		givenPolicy(policy);

		PolicyResponse response = policyService.cancel(POLICY_ID);

		assertThat(response.status()).isEqualTo(PolicyStatus.CANCELADA);
		assertThat(policy.getRisks()).extracting(Risk::getStatus).containsOnly(RiskStatus.CANCELADO);
		verify(coreNotifier).notify(CoreEventType.CANCELACION, POLICY_ID);
	}

	@Test
	void addRiskAddsRiskToCollectivePolicy() {
		givenPolicy(newPolicy(PolicyType.COLECTIVA));
		when(riskRepository.save(any(Risk.class))).then(returnsFirstArg());

		RiskResponse response = policyService.addRisk(POLICY_ID, new RiskRequest("Warehouse"));

		assertThat(response.description()).isEqualTo("Warehouse");
		assertThat(response.status()).isEqualTo(RiskStatus.ACTIVO);
		verify(coreNotifier).notify(CoreEventType.ACTUALIZACION, POLICY_ID);
	}

	@Test
	void addRiskRejectsIndividualPolicy() {
		givenPolicy(newPolicy(PolicyType.INDIVIDUAL));

		assertThatThrownBy(() -> policyService.addRisk(POLICY_ID, new RiskRequest("Warehouse")))
				.isInstanceOf(BusinessRuleException.class);
		verifyNoInteractions(riskRepository, coreNotifier);
	}

	@Test
	void renewFailsWhenPolicyDoesNotExist() {
		when(policyRepository.findById(POLICY_ID)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> policyService.renew(POLICY_ID)).isInstanceOf(ResourceNotFoundException.class);
	}

	private Policy newPolicy(PolicyType type) {
		return new Policy(type, new BigDecimal("1000.00"), new BigDecimal("200.00"));
	}

	private void givenPolicy(Policy policy) {
		when(policyRepository.findById(POLICY_ID)).thenReturn(Optional.of(policy));
	}
}
