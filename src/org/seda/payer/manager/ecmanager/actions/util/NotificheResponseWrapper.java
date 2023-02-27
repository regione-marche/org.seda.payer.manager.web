package org.seda.payer.manager.ecmanager.actions.util;


import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificheResponseWrapper {
	
	@JsonProperty("notifiche")
	public WebServiceOutput<NotificheLis> webServiceOutput;

	public NotificheResponseWrapper() {
		super();
		
	}
	
	public WebServiceOutput<NotificheLis> getWebServiceOutput() {
		return webServiceOutput;
	}

	public void setWebServiceOutput(WebServiceOutput<NotificheLis> webServiceOutput) {
		this.webServiceOutput = webServiceOutput;
	}

}
