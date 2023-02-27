/**
 * 
 */
package org.seda.payer.manager.riversamento.actions;
/*
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
//import com.seda.j2ee5.maf.core.action.HtmlAction;
import com.seda.payer.riversamento.webservice.dati.AggiornamentoStatoRiversamentoRequest;
import com.seda.payer.riversamento.webservice.dati.AggiornamentoStatoRiversamentoResponse;
//import com.seda.payer.riversamento.webservice.dati.RecuperaRiversamentiRequest;
//import com.seda.payer.riversamento.webservice.dati.RecuperaRiversamentiResponse;
import com.seda.payer.riversamento.webservice.srv.FaultType;

/**
 * @author rcarnicelli
 *
 *//*
public class AggiornaStatoAction extends BaseRiversamentoAction {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 *//*
	public AggiornaStatoAction() {
	}

	/* (non-Javadoc)
	 * @see com.seda.j2ee5.maf.core.action.ActionService#service(javax.servlet.http.HttpServletRequest)
	 *//*
	public Object service(HttpServletRequest request) throws ActionException 
	{
   	    super.service(request);
//		setProfile(request);	
		salvaStato(request);

   	    
		switch(getFiredButton(request)) 
		{
			case TX_BUTTON_AVANZA_STATO: 
				avanzaStato(request);
				break;
			case TX_BUTTON_CERCA:
				break;
				
		}		
		
		
		return null;
	}

	private void avanzaStato(HttpServletRequest request)
	{
		String codSocieta = "", codUtente = "", dataRiv = "", codEnte = "", tipoRend = "", tipoRiv = "", statoRiversamento = "", operatore = "", nota="";

		AggiornamentoStatoRiversamentoResponse out;
		AggiornamentoStatoRiversamentoRequest in = new AggiornamentoStatoRiversamentoRequest();
				
		WSCache.logWriter.logDebug("Pagina interrogazione di test");
		try {
			
			codSocieta = (String) request.getParameter("codSoc");
			codUtente = (String) request.getParameter("codUte");
			dataRiv = (String) request.getParameter("dataRiv");
			codEnte = (String) request.getParameter("codEnte");
			tipoRend = (String) request.getParameter("tipoRend");
			tipoRiv = (String) request.getParameter("tipoRiv");
			statoRiversamento = (String) request.getParameter("stato");
			

			operatore = (String)request.getAttribute("operatore");
			nota = "";
			if (statoRiversamento.equals("S"))
				nota = request.getParameter("nota");

			boolean continua = true;
			
			if(statoRiversamento.equals("C")||statoRiversamento.equals("E"))
			{
				// confronto data riversamento
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				Date dataRiversamento = sdf.parse(dataRiv);
				Date attuale = new Date();

				if (!dataRiversamento.after(attuale)||
						(dataRiversamento.after(attuale)&&request.getAttribute("avanzaStato")!=null))
				{
					continua = true;
				}
				else
				{
					continua = false;
					//eseguo l'alert
				}	
			}

			if (continua)
			{
				in.setCodiceSocieta(codSocieta);
				in.setCodiceUtente(codUtente);
				in.setDataRiversamento(dataRiv.substring(0,10));
				in.setEnteBeneficiario(codEnte);
				in.setTipoRendicontazione(tipoRend);
				in.setTipoRiversamento(tipoRiv);
				in.setStatoRiversamento(statoRiversamento);
				in.setOperatore(operatore);
				in.setNota(nota);
			
				out =  WSCache.riversamentoServer.aggiornamentoStatoRiversamento(in, request);
				request.setAttribute("message_retCode", out.getRisultato().getRetMessage());
				request.setAttribute("code_retCode", out.getRisultato().getRetCode());
				
	//			request.setAttribute("riv_message", out.getRisultato().getRetMessage());
				setFormMessage("ricercaRiversamentiForm", out.getRisultato().getRetMessage(), request);
	//			request.setAttribute("messaggioRis", out.getRisultato().getRetMessage());
				
				//rieseguo la ricerca a mano
/*
				RicercaRiversamentiAction act = new RicercaRiversamentiAction();
				act.ricercaRiversamenti(request);
*//*
				request.setAttribute("tx_button_avanza_stato",null);
				request.setAttribute("tx_button_cerca", "");
				
			}
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