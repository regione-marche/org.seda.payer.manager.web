package org.seda.payer.manager.adminusers.util;

public class Error implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	
	private boolean errorvisible = false;
	private String errorvalue = "";
	
	
	public void setErrorvisible(boolean errorVisible) {
		this.errorvisible = errorVisible;
	}
	public boolean isErrorvisible() {
		return errorvisible;
	}
	public String getErrorvalue() {
		return errorvalue;
	}
	
	public void addErrorvalue(String newErrorValue) {
		this.setErrorvisible(true);
		if (!this.errorvalue.contains(newErrorValue))
		{
			// se l'errore è già contenuto non viene replicato
			this.errorvalue +=(this.errorvalue.length()==0) ? "" : "<br />";
			this.errorvalue += newErrorValue;
		}
	}
	public void resetError()
	{
		this.setErrorvisible(false);
		this.errorvalue = "";
	}
	
}
