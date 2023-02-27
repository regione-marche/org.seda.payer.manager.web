package org.seda.payer.manager.ioitalia.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
public interface InviaMessaggiAppIOInterface {

	@POST
	@Path("/profiles")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	Response profiles(org.seda.payer.manager.ioitalia.rest.ProfilesRequest messaggi);
	
	@POST
	@Path("/messages")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	Response messages(MessagesRequest messagesRequest);
	

}
