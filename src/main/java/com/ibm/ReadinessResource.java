package com.ibm;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSException;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import com.ibm.mqclient.config.QueueStatus;
import com.ibm.mqclient.service.MyJMSTemplate;

@Readiness
@ApplicationScoped
public class ReadinessResource implements HealthCheck {

	@Inject
	private MyJMSTemplate jmsTemplate;

	@Override
	public HealthCheckResponse call() {

		QueueStatus status = new QueueStatus();
		try {
			status = jmsTemplate.getQueueStatus();
		} catch (JMSException e) {
			status.setQueueAvailable(false);
		}

		return HealthCheckResponse.builder()
				.name("qm-liveness-check")
				.withData("queueName", status.getQueueName())
				.withData("hasMessages", status.hasMessages())
				.state(status.isQueueAvailable())
				.build();
	}

}
