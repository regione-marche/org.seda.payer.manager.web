package org.seda.payer.manager.login.util;

public class CasRequest {
	
	protected String jwt;
	protected String payload;
	
	public CasRequest(){}
	
	public CasRequest(String jwt){
		this.jwt=jwt;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
	
}
