package org.seda.payer.manager.util;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ValidatoreRequest {

	public String email;
	
	public ValidatoreRequest(String email){
		this.email=email;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}