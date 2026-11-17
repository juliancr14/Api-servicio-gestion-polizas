package com.prueba.seguros.bolivar.polizas.client;

import com.prueba.seguros.bolivar.polizas.dto.CoreEventRequest;
import com.prueba.seguros.bolivar.polizas.model.CoreEventType;
import com.prueba.seguros.bolivar.polizas.security.ApiKeyFilter;
import com.prueba.seguros.bolivar.polizas.service.CoreNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class HttpCoreNotifier implements CoreNotifier {

	private static final Logger log = LoggerFactory.getLogger(HttpCoreNotifier.class);
	private static final String EVENT_PATH = "/core-mock/evento";

	private final RestClient restClient;

	public HttpCoreNotifier(
			@Value("${app.core.base-url}") String baseUrl,
			@Value("${app.api-key}") String apiKey) {
		this.restClient = RestClient.builder()
				.baseUrl(baseUrl)
				.defaultHeader(ApiKeyFilter.API_KEY_HEADER, apiKey)
				.build();
	}

	@Override
	public void notify(CoreEventType eventType, Long policyId) {
		try {
			restClient.post()
					.uri(EVENT_PATH)
					.body(new CoreEventRequest(eventType, policyId))
					.retrieve()
					.toBodilessEntity();
		} catch (RestClientException exception) {
			log.warn("Event {} for policy {} could not be sent to CORE", eventType, policyId, exception);
		}
	}
}
