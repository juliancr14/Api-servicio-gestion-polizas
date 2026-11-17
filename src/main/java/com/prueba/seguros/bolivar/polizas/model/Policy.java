package com.prueba.seguros.bolivar.polizas.model;

import com.prueba.seguros.bolivar.polizas.exception.BusinessRuleException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "policies")
public class Policy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private PolicyType type;

	@Enumerated(EnumType.STRING)
	private PolicyStatus status;

	private BigDecimal canon;

	private BigDecimal premium;

	@OneToMany(mappedBy = "policy")
	private List<Risk> risks = new ArrayList<>();

	protected Policy() {
	}

	public Policy(PolicyType type, BigDecimal canon, BigDecimal premium) {
		this.type = type;
		this.status = PolicyStatus.ACTIVA;
		this.canon = canon;
		this.premium = premium;
	}

	public Long getId() {
		return id;
	}

	public PolicyType getType() {
		return type;
	}

	public PolicyStatus getStatus() {
		return status;
	}

	public BigDecimal getCanon() {
		return canon;
	}

	public BigDecimal getPremium() {
		return premium;
	}

	public List<Risk> getRisks() {
		return risks;
	}

	public void renew(BigDecimal ipcPercentage) {
		if (status == PolicyStatus.CANCELADA) {
			throw new BusinessRuleException("A cancelled policy cannot be renewed");
		}
		BigDecimal increaseFactor = BigDecimal.ONE.add(ipcPercentage.movePointLeft(2));
		canon = increase(canon, increaseFactor);
		premium = increase(premium, increaseFactor);
		status = PolicyStatus.RENOVADA;
	}

	public void cancel() {
		status = PolicyStatus.CANCELADA;
		risks.forEach(Risk::cancel);
	}

	public Risk addRisk(String description) {
		if (type != PolicyType.COLECTIVA) {
			throw new BusinessRuleException("Risks can only be added to collective policies");
		}
		Risk risk = new Risk(description, this);
		risks.add(risk);
		return risk;
	}

	private BigDecimal increase(BigDecimal amount, BigDecimal increaseFactor) {
		return amount.multiply(increaseFactor).setScale(2, RoundingMode.HALF_UP);
	}
}
