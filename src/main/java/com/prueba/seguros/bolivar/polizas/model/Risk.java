package com.prueba.seguros.bolivar.polizas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "risks")
public class Risk {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	@Enumerated(EnumType.STRING)
	private RiskStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policy_id")
	private Policy policy;

	protected Risk() {
	}

	Risk(String description, Policy policy) {
		this.description = description;
		this.status = RiskStatus.ACTIVO;
		this.policy = policy;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public RiskStatus getStatus() {
		return status;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void cancel() {
		status = RiskStatus.CANCELADO;
	}
}
