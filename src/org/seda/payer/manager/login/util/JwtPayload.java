package org.seda.payer.manager.login.util;

import com.fasterxml.jackson.annotation.JsonProperty;


public class JwtPayload {
	
	@JsonProperty("iss")
	protected String jwtIssuer;
	@JsonProperty("jti")
	protected String jwtId;
	@JsonProperty("exp")
	protected String expirationTime;
	
	public JwtPayload(){}
	
	public JwtPayload(String jwtIssuer, String jwtID, String expirationTime){
		this.jwtIssuer = jwtIssuer;
		this.jwtId = jwtID;
		this.expirationTime = expirationTime;
	}

	public String getJwtIssuer() {
		return jwtIssuer;
	}

	public String getJwtId() {
		return jwtId;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setJwtIssuer(String jwtIssuer) {
		this.jwtIssuer = jwtIssuer;
	}

	public void setJwtId(String jwtId) {
		this.jwtId = jwtId;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	public Long getLongExpirationTime()
	{
	    return Long.valueOf(this.expirationTime);
	}
	
	public String getCsrfToken()
	{
	    return this.jwtIssuer.split(" ")[1];
	}

	
	

}
