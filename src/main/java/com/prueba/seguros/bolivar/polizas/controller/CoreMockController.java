package com.prueba.seguros.bolivar.polizas.controller;

import com.prueba.seguros.bolivar.polizas.dto.CoreEventRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core-mock")
public class CoreMockController {

	private static final Logger log = LoggerFactory.getLogger(CoreMockController.class);

	@PostMapping("/evento")
	public void registerEvent(@Valid @RequestBody CoreEventRequest request) {
		log.info("Attempted to send event {} for policy {} to CORE", request.event(), request.policyId());
	}
}
