package org.seda.payer.manager.monitoraggio.actions;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.notifiche.webservice.dati.DownloadQuietanzaBollettiniResponseType;
import com.seda.payer.notifiche.webservice.dati.ResponseRetcode;

public class StampaPDFBollettiniAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		
		DownloadQuietanzaBollettiniResponseType downloadQuietanzaBollettiniResponseType;
		String codiceTransazione = (String)request.getAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		
		downloadQuietanzaBollettiniResponseType=WSCache.notificheServer.downloadQuietanzaBollettini(codiceTransazione, request);
		
		if (downloadQuietanzaBollettiniResponseType!=null)
		{
			
			ResponseRetcode responseRetcode=downloadQuietanzaBollettiniResponseType.getResponse().getRetcode();
			
			if (responseRetcode.getValue().equals(ResponseRetcode._value1))
			{
				File f=new File(downloadQuietanzaBollettiniResponseType.getFileName());
				
				request.setAttribute("downloadPdfBollettini", downloadQuietanzaBollettiniResponseType.getFileName());
				request.setAttribute("filename",f.getName());
				
				f=null;
			}
			else
			{
				setFormMessage("monitoraggioTransazioniForm", "Errore nel download della stampa" , request);
				request.setAttribute("downloadPdfBollettini", "");
			}	
		}
		
		return null;
		
	}

}
