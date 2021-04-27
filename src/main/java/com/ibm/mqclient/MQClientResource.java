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
	
	@Inject
	@ConfigProperty(name = "app.keystore", defaultValue = "passw0rd")
    private String keyStore;
	
	@Inject
	@ConfigProperty(name = "app.keystore.pw", defaultValue = "passw0rd")
    private String keyStorePassword;
	
	@Inject
	@ConfigProperty(name = "app.truststore", defaultValue = "passw0rd")
    private String trustStore;
	
	@Inject
	@ConfigProperty(name = "app.truststore.pw", defaultValue = "passw0rd")
    private String trustStorePassword;
    
    @PostConstruct
    void init() {
        // do something
    	LOG.debug("GK Initializing the application");

        String filename="result.csv";
        java.nio.file.Path pathToFile = Paths.get(filename);
        System.out.println(pathToFile.toAbsolutePath());
        
        // For mTLS, this MQ client app needs to send a certificate to the MQM server
		System.setProperty("javax.net.ssl.keyStore", keyStore);
		System.setProperty("javax.net.ssl.keyStorePassword", "passw0rd");
		
        
        String activeProfile = ProfileManager.getActiveProfile();
        LOG.debug("Active profile is: {}", activeProfile );
        // The trust store has the CA certificate that signed the MQM server certificate.
        // This is required to setup a TLS connection with the MQM server
        
		System.setProperty("javax.net.ssl.trustStore", trustStore);
		System.setProperty("javax.net.ssl.trustStorePassword", "passw0rd");


		System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "false");
    	
    }
}
