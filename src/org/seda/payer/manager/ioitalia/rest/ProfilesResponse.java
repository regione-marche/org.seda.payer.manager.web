package org.seda.payer.manager.ioitalia.rest;

public class ProfilesResponse extends AppIOResponse{
	
	protected Boolean sender_allowed;

	public Boolean getSender_allowed() {
		return sender_allowed;
	}

	public void setSender_allowed(Boolean sender_allowed) {
		this.sender_allowed = sender_allowed;
	}
}