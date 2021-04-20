package com.ibm.mqclient;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ibm.mqclient.model.ResponseData;
import com.ibm.mqclient.service.MQService;

@Path("/api")
public class MQClientResource {
	
	@Inject
	private MQService mqService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }
    
    @Path("/send-hello-world")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseData sendHelloWorld() {
    	String dataSentToQueue = mqService.sendHelloWorld();
    	ResponseData responseData = new ResponseData("OK", "Successfully sent record to MQ", dataSentToQueue);
        return responseData;
    }
    
    @Path("/recv")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseData recv() {
    	String dataReceivedFromQueue = mqService.receiveMessage();
    	ResponseData responseData = new ResponseData("OK", "Successfully received record from MQ", dataReceivedFromQueue);
    	return responseData;
    }
    
    @Path("/send-json")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public ResponseData sendPost(Map<String,Object> requestMap) {
	       String dataSentToQueue = mqService.sendJson(requestMap);
	       ResponseData responseData = new ResponseData("OK", "Successfully sent record to MQ", dataSentToQueue);
	       return responseData;
	}
}