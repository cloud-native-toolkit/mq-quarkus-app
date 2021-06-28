package com.ibm.mqclient.config;

public class QueueStatus {

	private String queueName;
	
	private boolean queueAvailable;
	
	private boolean hasMessages;
	
	public QueueStatus() {
		this.queueName = "UNSET";
		this.queueAvailable = false;
		this.hasMessages = false;
	}

	public boolean isQueueAvailable() {
		return queueAvailable;
	}

	public void setQueueAvailable(boolean queueAvailable) {
		this.queueAvailable = queueAvailable;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public boolean hasMessages() {
		return hasMessages;
	}

	public void setHasMessages(boolean hasMessages) {
		this.hasMessages = hasMessages;
	}	
	
}
