package org.seda.payer.manager.monitoraggio.actions;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.dati.AssegnaTranAFittizioRequest;
import com.seda.payer.pgec.webservice.commons.dati.AssegnaTranAFittizioResponse;
//import com.seda.payer.riversamento.webservice.srv.FaultType;

public class AssegnaTranAFittizioAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		replyAttributes(false, request,"validator.message", Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		super.service(request);
		setProfile(request);
		 
		String codiceTransazione = (String)request.getAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		
		// stampa singolo gateway o multigateway	0 no	1 si
		String multiGTW = request.getAttribute("multiGTW")!=null?(String)request.getAttribute("multiGTW"):"0";

		AssegnaTranAFittizioRequest in = new AssegnaTranAFittizioRequest(codiceTransazione, multiGTW, userBean.getUserName());
		AssegnaTranAFittizioResponse out = null;
		try 
		{	
			out = WSCache.commonsServer.assegnaTranAFittizio(in, request);
			int code = Integer.parseInt(out.getResponse().getRetCode().getValue());
			switch (code)
			{
			case 0:
			case 1:	
				setFormMessage("monitoraggioTransazioniForm", "Transazione importata nel flusso con successo" , request);
				break;
			default:
				setFormMessage("monitoraggioTransazioniForm", "Errore nell'aggancio della transazione al flusso fittizio" , request);				
			}
		}
		/*
		catch (FaultType e) {
			e.printStackTrace();
			setFormMessage("monitoraggioTransazioniForm", "Errore generico nell'aggancio della transazione" , request);
		} */catch (RemoteException e) {
			e.printStackTrace();
			setFormMessage("monitoraggioTransazioniForm", "Errore generico nell'aggancio della transazione" , request);
		}
		return null;
	}
}
