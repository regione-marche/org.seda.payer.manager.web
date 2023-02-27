/**
 * 
 */
package org.seda.payer.manager.riversamento.actions;
/*
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
//import com.seda.j2ee5.maf.core.action.HtmlAction;
import com.seda.payer.riversamento.webservice.dati.AggiornamentoFlagCBIRequest;
import com.seda.payer.riversamento.webservice.dati.AggiornamentoFlagCBIResponse;
import com.seda.payer.riversamento.webservice.srv.FaultType;

public class AggiornaFlagCBIAction extends BaseRiversamentoAction {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 *//*
	public AggiornaFlagCBIAction() {
	}

	/* (non-Javadoc)
	 * @see com.seda.j2ee5.maf.core.action.ActionService#service(javax.servlet.http.HttpServletRequest)
	 *//*
	public Object service(HttpServletRequest request) throws ActionException {

   	    super.service(request);
		salvaStato(request);

		switch(getFiredButton(request)) 
		{
			case TX_BUTTON_SET_FLAG_CBI: 
		   	    aggiornaFlag(request);
				break;
			case TX_BUTTON_CERCA:
				break;
				
		}		
   	    
   	    return null;
	}

	private void aggiornaFlag(HttpServletRequest request) throws ActionException
	{

		String codSocieta = "", codUtente = "", dataRiv = "", codEnte = "", tipoRend = "", tipoRiv = "", statoFlagCBI = "", operatore = "";
//		String nota="";
        String coordinate = "";
		AggiornamentoFlagCBIResponse out;
		AggiornamentoFlagCBIRequest in = new AggiornamentoFlagCBIRequest();

		try {			
			codSocieta = (String) request.getParameter("codSoc");
			codUtente = (String) request.getParameter("codUte");
			dataRiv = (String) request.getParameter("dataRiv");
			codEnte = (String) request.getParameter("codEnte");
			tipoRend = (String) request.getParameter("tipoRend");
			tipoRiv = (String) request.getParameter("tipoRiv");
			statoFlagCBI = (String) request.getParameter("fcbi");
			coordinate = (String)request.getParameter("coord");
			coordinate = coordinate.trim();
			operatore = (String)request.getAttribute("operatore");

			// confronto data riversamento
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Date dataRiversamento = sdf.parse(dataRiv);
			Date attuale = new Date();

			if (dataRiversamento.after(attuale)&&request.getAttribute("avanzaFlag")==null)
			{
				//visualizzare l'alert
				return;
			}	
			
			
			if (statoFlagCBI.equalsIgnoreCase("S")&&coordinate.equals(""))
			{
				//coordinate non esistenti
				setFormMessage("ricercaRiversamentiForm", "Il Flusso CBI non puo' essere richiesto: coordinate bancarie ente beneficiario mancanti" , request);
			}
			else
			{
				in.setCodiceSocieta(codSocieta);
				in.setCodiceUtente(codUtente);
				in.setDataRiversamento(dataRiv.substring(0,10));
				in.setEnteBeneficiario(codEnte);
				in.setTipoRendicontazione(tipoRend);
				in.setTipoRiversamento(tipoRiv);
				in.setStatoFlagCBI(statoFlagCBI);
				in.setOperatore(operatore);
				out =  WSCache.riversamentoServer.aggiornamentoFlagCBI(in, request);
				request.setAttribute("message_retCode", out.getRisultato().getRetMessage());
				request.setAttribute("code_retCode", out.getRisultato().getRetCode());
						
				setFormMessage("ricercaRiversamentiForm", "Flusso CBI Richiesto" , request);
			}
			
			//andare in ricerca
			request.setAttribute("tx_button_set_flag_cbi",null);
			request.setAttribute("tx_button_cerca", "");

/*			
			RicercaRiversamentiAction ricerca = new RicercaRiversamentiAction();

			ricerca.codiceSocieta = this.codiceSocieta;
			ricerca.codiceProvincia = this.codiceProvincia;
			ricerca.codiceUtente = this.codiceUtente;
			ricerca.codiceEnteBeneficiario = this.codiceEnteBeneficiario;

			ricerca.ricercaRiversamenti(request);
*//*
		} 
		catch (FaultType fte) 
		{
			WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
			fte.printStackTrace();
			setFormMessage("ricercaRiversamentiForm", "errore: " + decodeMessaggio(fte), request);
		}
		catch (RemoteException re)
		{
			WSCache.logWriter.logError("errore: " + re.getMessage(),re);
			re.printStackTrace();
			setFormMessage("ricercaRiversamentiForm", testoErroreColl, request);
			
		}
		catch (Exception e) {
			WSCache.logWriter.logError("errore: " + e.getMessage(),e);
			setFormMessage("ricercaRiversamentiForm", "errore: " + testoErrore, request);
		}
	}
}
*/