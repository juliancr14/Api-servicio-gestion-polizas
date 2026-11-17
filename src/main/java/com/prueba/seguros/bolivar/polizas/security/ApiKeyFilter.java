package com.prueba.seguros.bolivar.polizas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

	public static final String API_KEY_HEADER = "x-api-key";

	private static final String H2_CONSOLE_PATH = "/h2-console";

	private final String apiKey;

	public ApiKeyFilter(@Value("${app.api-key}") String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().startsWith(H2_CONSOLE_PATH);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (apiKey.equals(request.getHeader(API_KEY_HEADER))) {
			filterChain.doFilter(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing API key");
		}
	}
}
