package org.seda.payer.manager.util;


import org.seda.payer.manager.ecmanager.actions.util.WebServiceOutput;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidatoreResponseWrapper {
	
	@JsonProperty("email_sender")
	public WebServiceOutput<ValidatoreResponse[]> webServiceOutput;

	public ValidatoreResponseWrapper() {
		super();
		
	}
	
	public WebServiceOutput<ValidatoreResponse[]> getWebServiceOutput() {
		return webServiceOutput;
	}

	public void setWebServiceOutput(WebServiceOutput<ValidatoreResponse[]> webServiceOutput) {
		this.webServiceOutput = webServiceOutput;
	}

}
