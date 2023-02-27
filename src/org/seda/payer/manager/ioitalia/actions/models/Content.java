package org.seda.payer.manager.ioitalia.actions.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Content {
	
	protected String subject;
	protected String markdown;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	protected PaymentData payment_data;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	protected PrescriptionData prescription_data;
	protected String due_date;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMarkdown() {
		return markdown;
	}
	public void setMarkdown(String markdown) {
		this.markdown = markdown;
	}
	public PaymentData getPayment_data() {
		return payment_data;
	}
	public void setPayment_data(PaymentData payment_data) {
		this.payment_data = payment_data;
	}
	public PrescriptionData getPrescription_data() {
		return prescription_data;
	}
	public void setPrescription_data(PrescriptionData prescription_data) {
		this.prescription_data = prescription_data;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

}
