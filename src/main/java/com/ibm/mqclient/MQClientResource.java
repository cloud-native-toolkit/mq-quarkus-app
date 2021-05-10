package com.ibm.mqclient;

import java.nio.file.Paths;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.HealthResource.Status;
import com.ibm.mqclient.model.ResponseData;
import com.ibm.mqclient.service.MQService;
import io.quarkus.runtime.configuration.ProfileManager;

@Path("/api")
public class MQClientResource {
	
	final Logger LOG = LoggerFactory.getLogger(MQClientResource.class);

	@Inject
	private MQService mqService;

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
    
    @Path("/gk")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Status health() {
        return new Status();
    }
	
}
