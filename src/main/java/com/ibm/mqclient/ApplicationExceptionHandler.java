package com.ibm.mqclient;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ibm.mqclient.exceptions.AppException;
import com.ibm.mqclient.model.ErrorResponse;

@Provider
public class ApplicationExceptionHandler implements ExceptionMapper<AppException>  {


	@Override
	public Response toResponse(AppException e) {
    	ErrorResponse error = new ErrorResponse(e.getErrorCode(), e.getLocalizedMessage());
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();  
	}

}
