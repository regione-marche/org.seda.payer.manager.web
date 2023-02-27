package org.seda.payer.manager.ioitalia.actions.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class MessagesRequest {
	
	protected int time_to_live = 3600;
	protected Content content;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	protected DefaultAddresses default_addresses;
	protected String fiscal_code;
	
	public int getTime_to_live() {
		return time_to_live;
	}
	public void setTime_to_live(int time_to_live) {
		this.time_to_live = time_to_live;
	}
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	public DefaultAddresses getDefault_addresses() {
		return default_addresses;
	}
	public void setDefault_addresses(DefaultAddresses default_addresses) {
		this.default_addresses = default_addresses;
	}
	public String getFiscal_code() {
		return fiscal_code;
	}
	public void setFiscal_code(String fiscal_code) {
		this.fiscal_code = fiscal_code;
	}
	
}
