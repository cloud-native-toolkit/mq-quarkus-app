package com.ibm.mqclient.config;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.configuration.ProfileManager;

@Startup 
@ApplicationScoped
public class JvmTlsSecurity {
	
	public JvmTlsSecurity() {
		super();
	}

	final Logger LOG = LoggerFactory.getLogger(JvmTlsSecurity.class);
	
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
    public void init() {
    	
    	// Note: not using profiles yet.  But this code can tell you what profile is active.
        String activeProfile = ProfileManager.getActiveProfile();
        LOG.debug("Active profile is: {}", activeProfile );
        
    	LOG.debug("Initializing the application");
        
        LOG.debug("Setting the javax.net.ssl.keyStore from keyStore file: {}", keyStore );

        // For mTLS, this MQ client app needs to send a certificate to the MQM server
		System.setProperty("javax.net.ssl.keyStore", keyStore);
		System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
		
        

        
        LOG.debug("Setting the javax.net.ssl.trustStore from keyStore file: {}", trustStore );
        
        // The trust store has the CA certificate that signed the MQM server certificate.
        // This is required to setup a TLS connection with the MQM server
		System.setProperty("javax.net.ssl.trustStore", trustStore);
		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);


		System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "false");
    	
	}

}
