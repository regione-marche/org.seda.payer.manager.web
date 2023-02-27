package org.seda.payer.manager.ecmanager.actions.util;

import java.util.List;
import java.util.Map;

public class ReinvioNotifica {
	
	private String attachmentEndValidityDate;
	private String chiaveNotifica;
	private String messageType;
	private String receiver;
	private Map<String,List<String>> properties;
	
	
	public String getAttachmentEndValidityDate() {
		return attachmentEndValidityDate;
	}
	public String getChiaveNotifica() {
		return chiaveNotifica;
	}
	public String getMessageType() {
		return messageType;
	}
	public String getReceiver() {
		return receiver;
	}
	public Map<String, List<String>> getProperties() {
		return properties;
	}
	public void setAttachmentEndValidityDate(String attachmentEndValidityDate) {
		this.attachmentEndValidityDate = attachmentEndValidityDate;
	}
	public void setChiaveNotifica(String chiaveNotifica) {
		this.chiaveNotifica = chiaveNotifica;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public void setProperties(Map<String, List<String>> properties) {
		this.properties = properties;
	}
	
	
	

}
