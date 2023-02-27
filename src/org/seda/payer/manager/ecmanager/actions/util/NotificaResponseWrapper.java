package org.seda.payer.manager.ecmanager.actions.util;


import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificaResponseWrapper {
	
	@JsonProperty("notifiche")
	public WebServiceOutput<Notifica> webServiceOutput;

	public NotificaResponseWrapper() {
		super();
		
	}
	
	public WebServiceOutput<Notifica> getWebServiceOutput() {
		return webServiceOutput;
	}

	public void setWebServiceOutput(WebServiceOutput<Notifica> webServiceOutput) {
		this.webServiceOutput = webServiceOutput;
	}

}
