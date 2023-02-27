package org.seda.payer.manager.monitoraggiocup.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.pgec.webservice.mip.dati.MICSaveCupStatusRequest;
import com.seda.payer.pgec.webservice.mip.dati.MICSaveCupStatusResponse;
import com.seda.payer.pgec.webservice.mip.dati.ModuloIntegrazioneCupStatus;

public class NotificaAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException 
	{
		HttpSession session = request.getSession();
		
		String nomeForm = "";
		if (request.getAttribute("form") != null)
			nomeForm = (String)request.getAttribute("form");
		
		try
		{
			if (request.getAttribute("idtrans") != null && !request.getAttribute("idtrans").equals(""))
			{
				String idTransazione = (String)request.getAttribute("idtrans");
				String codiceFiscale = (String)request.getAttribute("codiceFiscale");
				String codicePagamento = (String)request.getAttribute("codicePagamento");
				String numeroOperazione = (String)request.getAttribute("numop");
				String idPortale =  (String)request.getAttribute("idportale");
				if (!idTransazione.equals(""))
				{
					initMCS(idTransazione, codiceFiscale, codicePagamento, numeroOperazione, idPortale, nomeForm, request, session);
				}
				else
					setFormMessage(nomeForm, "Id transazione non valido", request);
			}
			else
				setFormMessage(nomeForm, "Parametri mancanti", request);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			setFormMessage(nomeForm, e.getMessage(), request);
		}
		
		//se non ho settato nessun messaggio
		if ((getMessage() == null || getMessage().equals("")) &&
				(getErrorMessage() == null || getErrorMessage().equals("")))
			setFormMessage(nomeForm, "La rinotifica è stata inizializzata correttamente per la transazione selezionata.", request);
		
		request.setAttribute("idtrans", request.getAttribute("idtrans"));
		request.setAttribute("codiceFiscale", request.getAttribute("codiceFiscale"));
		request.setAttribute("codicePagamento", request.getAttribute("codicePagamento"));
		request.setAttribute("action", (String)request.getAttribute("action"));
		request.setAttribute("vista", (String)request.getAttribute("vista"));

		return null;
		
		
	}

	
	private boolean initMCS(String idTransazione, String codiceFiscale, String codicePagamento, String numeroOperazione, String idPortale, String formName, HttpServletRequest request, HttpSession session)
	{
		UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		
		ModuloIntegrazioneCupStatus micStatus = new ModuloIntegrazioneCupStatus();
		micStatus.setChiaveTransazione(idTransazione);
		micStatus.setCodiceFiscale(codiceFiscale);
		micStatus.setCodicePagamento(codicePagamento);
		micStatus.setModalitaNotifica("M");
		micStatus.setOperatoreUltimoAggiornamento(userBean.getUserName());
		micStatus.setIdPortale(idPortale);
		micStatus.setNumeroOperazione(numeroOperazione);
		
		MICSaveCupStatusRequest reqPS = new MICSaveCupStatusRequest(micStatus);
		try {
			MICSaveCupStatusResponse resPS = WSCache.mipServer.saveCupStatus(reqPS, request);

			if (!resPS.getRetCode().equals("00"))
			{
				setFormMessage(formName, "Si è verificato un errore. Impossibile inizializzare la rinotifica.", request);
				return false;
			}
			else
				return true;
			
		} catch (Exception e) {
			setFormMessage(formName, "Si è verificato un errore. Impossibile inizializzare la rinotifica.", request);
			return false;
		}
	}
}
