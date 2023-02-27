package org.seda.payer.manager.util;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

//import com.esed.log.req.dati.LogPap;
import com.esed.log.req.dati.LogWin;
import com.seda.commons.properties.tree.PropertiesTree;

public class LoggerUtil {
	public void saveWinLog(LogWin logWin , PropertiesTree configuration) {

			String uri = "";
			
			uri =  configuration.getProperty(PropertiesPath.wsLogRequest.format(PropertiesPath.defaultnode.format()));
		
			if ( uri != null 
					&& !uri.equals("")){
				 uri = uri + "/saveWinLogger";
				 System.out.println("uri ws LOG = " + uri);
			}
			
			
			if(logWin != null && uri != "") {
				
				Entity<LogWin> entity =  Entity.entity(logWin, MediaType.APPLICATION_JSON);

				WsLogRequestThread wsLogRequestThread = new WsLogRequestThread(uri, entity);
				Thread thread = new Thread(wsLogRequestThread);
				thread.start();		
				
			}
		
		
	}


}
