package com.ibm.mqclient.service;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mqclient.exceptions.AppException;

@ApplicationScoped
public class MQService {
	
	public MQService() {
		super();
		// TODO Auto-generated constructor stub
	}


    @Inject
	private MyJMSTemplate jmsTemplate;
	
    final Logger LOG = LoggerFactory.getLogger(MQService.class);
	
	public String sendHelloWorld() {
		

		try {
			String helloWorld = "Hello World!";
			jmsTemplate.send(helloWorld);
			LOG.debug("Successfully Sent message: {} to the queue", helloWorld);
			return helloWorld;
		} catch (JMSException ex) {
			throw new AppException("MQAPP001", "Error sending message to the queue.", ex);
		}
	}
	
	public String receiveMessage() {
	    try{
	    	String receivedMsg = jmsTemplate.receiveMessage();
	    	LOG.debug("Successfully received message: {} to the queue", receivedMsg);
	        return receivedMsg;
	    }catch(JMSException ex) {
	    	throw new AppException("MQAPP002", "Error receiving message from the queue.", ex);
	    }
	}
	
	public String sendJson(Map<String,Object> requestMap) {
		
		String jsonResult = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonResult = mapper.writerWithDefaultPrettyPrinter()
			  .writeValueAsString(requestMap);
		    jmsTemplate.send(jsonResult);
		    LOG.debug("Successfully Sent message:\n {} \n to the queue", jsonResult);
			return jsonResult;
		} catch (JsonProcessingException e) {
			throw new AppException("MQAPP003", "Error processing json request.", e);
		} catch(JMSException ex) {
			throw new AppException("MQAPP001", "Error sending message to the queue.", ex);
	    }		
	}	

}
