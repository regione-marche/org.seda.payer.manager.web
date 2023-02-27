package org.seda.payer.manager.ioitalia.actions.models;

public class PaymentData {
	
	protected long amount; //required
	protected String notice_number; //required
	protected boolean invalid_after_due_date = false;
	
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getNotice_number() {
		return notice_number;
	}
	public void setNotice_number(String notice_number) {
		this.notice_number = notice_number;
	}
	public boolean isInvalid_after_due_date() {
		return invalid_after_due_date;
	}
	public void setInvalid_after_due_date(boolean invalid_after_due_date) {
		this.invalid_after_due_date = invalid_after_due_date;
	}
	
}
