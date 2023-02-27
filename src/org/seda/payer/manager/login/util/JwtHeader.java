package org.seda.payer.manager.login.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtHeader {
	
	@JsonProperty("typ")
	protected String type;
	@JsonProperty("alg")
	protected String algorithm;
	
	public JwtHeader(){}
	
	public JwtHeader(String type, String algorithm){
		this.type = type;
		this.algorithm = algorithm;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
