package org.seda.payer.manager.ws;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;

public class WsLogRequestThread implements Runnable {
	
	private String uri = null;
	
	private Entity<?> entity = null;
	
	public WsLogRequestThread(String uri, Entity<?> entity) {
		super();
		this.uri = uri;
		this.entity = entity;
	}	

	public void run() {
		Response responseWS = null;
		try {
			Client client = ClientBuilder.newClient();
			//System.out.println("inizio chiamata prova log request WS");
			WebTarget target = client.target(uri);
			//System.out.println("-target uri= " + target.getUri());
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			responseWS = builder.post(entity);
			if(responseWS != null) {
				//System.out.println("-response = " + responseWS.getStatus());
				//System.out.println("-response info = " + responseWS.getStatusInfo());
				responseWS.close();
				responseWS = null;
			}
		} catch (Exception e) {
			System.out.println("WsLogRequestThread::run errore: " + e.getMessage());
		} finally {
			if(responseWS != null)
				responseWS.close();
		}
	}		
	
}
