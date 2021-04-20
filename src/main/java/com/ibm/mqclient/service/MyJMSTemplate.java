package com.ibm.mqclient.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

@ApplicationScoped
final public class MyJMSTemplate {

	// System exit status value (assume unset value to be 1)
	private static int status = 1;

	@Inject
	@ConfigProperty(name = "mq.host", defaultValue = "localhost")
	private static String mqHostname;

	@Inject
	@ConfigProperty(name = "mq.port", defaultValue = "1414")
	private static int mqHostport;

	@Inject
	@ConfigProperty(name = "mq.qmgr", defaultValue = "QM1")
	private static String mqQmgr;

	@Inject
	@ConfigProperty(name = "mq.channel", defaultValue = "DEV.APP.SVRCONN")
	private static String mqChannel;

	@Inject
	@ConfigProperty(name = "mq.app_user", defaultValue = "app")
	private static String mqAppUser;

	@Inject
	@ConfigProperty(name = "mq.app_password", defaultValue = "passw0rd")
	private static String mqPassword;

	@Inject
	@ConfigProperty(name = "mq.queue_name", defaultValue = "DEV.QUEUE.1")
	private static String mqQueueName;

	@Inject
	@ConfigProperty(name = "app.name", defaultValue = "TestApp")
	private static String appName;

	final Logger LOG = LoggerFactory.getLogger(MyJMSTemplate.class);
	
	public MyJMSTemplate() {
		super();
		// TODO Auto-generated constructor stub
		// Create a connection factory
	}

	public void send(String strToSend) throws JMSException {
		JMSContext context = null;
		Destination destination = null;
		JMSProducer producer = null;

		try {
			JmsConnectionFactory cf = getJMSConectionFactory();

			// Create JMS objects
			context = cf.createContext();
			producer = context.createProducer();
			destination = context.createQueue("queue:///" + mqQueueName);

			TextMessage message = context.createTextMessage(strToSend);
			producer.send(destination, message);
			LOG.debug("Sent message:\n {}", message);

		} catch (Throwable e) {
			e.printStackTrace();
			throw (e);
		}
	}

	public String receiveMessage() throws JMSException {		
		JMSContext context = null;
		Destination destination = null;
		JMSConsumer consumer = null;
		
		try {
			JmsConnectionFactory cf = getJMSConectionFactory();

			// Create JMS objects
			context = cf.createContext();
			destination = context.createQueue("queue:///" + mqQueueName);
			consumer = context.createConsumer(destination); // autoclosable
			
			
			String receivedMessage = consumer.receiveBody(String.class, 15000); // in ms or 15 seconds
			LOG.debug("\nReceived message:\n {}", receivedMessage);
			return receivedMessage;
		} catch (Throwable e) {
			e.printStackTrace();
			throw (e);
		}
	}

	private JmsConnectionFactory getJMSConectionFactory() throws JMSException {
		try {
			JmsFactoryFactory ff;
			JmsConnectionFactory cf = null;

			ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
			cf = ff.createConnectionFactory();
			// Set the properties
			cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, mqHostname);
			cf.setIntProperty(WMQConstants.WMQ_PORT, mqHostport);
			cf.setStringProperty(WMQConstants.WMQ_CHANNEL, mqChannel);
			cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
			cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, mqQmgr);
			cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
			cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
			cf.setStringProperty(WMQConstants.USERID, mqAppUser);
			cf.setStringProperty(WMQConstants.PASSWORD, mqPassword);
			return cf;
		} catch (Throwable e) {
			e.printStackTrace();
			throw (e);
		}
	}

	/**
	 * Record this run as successful.
	 */
	private static void recordSuccess() {
		System.out.println("SUCCESS");
		status = 0;
		return;
	}

	/**
	 * Record this run as failure.
	 *
	 * @param ex
	 */
	private static void recordFailure(Exception ex) {
		if (ex != null) {
			if (ex instanceof JMSException) {
				processJMSException((JMSException) ex);
			} else {
				System.out.println(ex);
			}
		}
		System.out.println("FAILURE");
		status = -1;
		return;
	}

	/**
	 * Process a JMSException and any associated inner exceptions.
	 *
	 * @param jmsex
	 */
	private static void processJMSException(JMSException jmsex) {
		System.out.println(jmsex);
		Throwable innerException = jmsex.getLinkedException();
		if (innerException != null) {
			System.out.println("Inner exception(s):");
		}
		while (innerException != null) {
			System.out.println(innerException);
			innerException = innerException.getCause();
		}
		return;
	}
}
