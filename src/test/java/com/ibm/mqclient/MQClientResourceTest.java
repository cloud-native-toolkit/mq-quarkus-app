package com.ibm.mqclient;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;

import com.ibm.mqclient.service.MQService;

@QuarkusTest
public class MQClientResourceTest {
	
	@InjectMock
	MQService mqServiceMock;
	
    @Test
    public void sendHelloWorldhShouldReturn200() {
    	String helloWorld = "Hello world";
    	when(mqServiceMock.sendHelloWorld()).thenReturn(helloWorld);
    	
        given()
          .when().get("/api/send-hello-world")
          .then()
             .statusCode(200)
             .contentType(ContentType.JSON)
             .body("status", is("OK"))
             .body("data", is(helloWorld));
    }
    
    @Test
    public void receivingMessageShouldReturn200() {
       	String mockReceiveMessage = "Hello world from mock";
    	when(mqServiceMock.receiveMessage()).thenReturn(mockReceiveMessage);
    	
        given()
          .when().get("/api/recv")
          .then()
             .statusCode(200)
             .contentType(ContentType.JSON)
             .body("status", is("OK"))
             .body("data", is(mockReceiveMessage));
    }
    
    @Test
    public void sendJsonStringShouldReturn200() throws Exception {

    	String jsonRequestBody = "{\"firstName\":\"gerry\",\"lastName\":\"kovan\"}";
    	when(mqServiceMock.sendJson(any())).thenReturn(jsonRequestBody);
    	    	
        given()
        .header("Content-Type", "application/json")
        .body(jsonRequestBody)
        .when()
        .post("/api/send-json")
        .then()
           .statusCode(200)
           .contentType(ContentType.JSON)
           .body("status", is("OK"))
           .body("data", is(jsonRequestBody));        
    }
}
