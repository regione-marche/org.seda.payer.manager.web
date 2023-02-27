package org.seda.payer.manager.ioitalia.actions.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class PrescriptionData {
	
	protected String nre; //required
	@JsonInclude(JsonInclude.Include.NON_NULL)
	protected String iup;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	protected String prescriber_fiscal_code;
	
	public String getNre() {
		return nre;
	}
	public void setNre(String nre) {
		this.nre = nre;
	}
	public String getIup() {
		return iup;
	}
	public void setIup(String iup) {
		this.iup = iup;
	}
	public String getPrescriber_fiscal_code() {
		return prescriber_fiscal_code;
	}
	public void setPrescriber_fiscal_code(String prescriber_fiscal_code) {
		this.prescriber_fiscal_code = prescriber_fiscal_code;
	}
	
}
