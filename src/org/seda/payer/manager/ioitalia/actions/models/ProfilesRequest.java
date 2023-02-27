package org.seda.payer.manager.ioitalia.actions.models;

public class ProfilesRequest {
	
	protected String fiscal_code;
	
	public ProfilesRequest(String fiscal_code) {
		super();
		this.fiscal_code = fiscal_code;
	}

	public String getFiscal_code() {
		return fiscal_code;
	}

	public void setFiscal_code(String fiscal_code) {
		this.fiscal_code = fiscal_code;
	}
}