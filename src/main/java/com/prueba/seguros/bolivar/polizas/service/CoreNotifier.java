package com.prueba.seguros.bolivar.polizas.service;

import com.prueba.seguros.bolivar.polizas.model.CoreEventType;

public interface CoreNotifier {

	void notify(CoreEventType eventType, Long policyId);
}
