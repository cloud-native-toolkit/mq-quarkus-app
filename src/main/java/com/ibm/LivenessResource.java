package com.ibm;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class LivenessResource implements HealthCheck {

	@Override
	public HealthCheckResponse call() {

		/**
		 * TODO Replace with context-specific application liveness check
		 */
		return HealthCheckResponse.builder()
				.name("qm-liveness-check")
				.up()
				.build();

	}
}
