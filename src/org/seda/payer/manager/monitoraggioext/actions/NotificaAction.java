package org.seda.payer.manager.monitoraggioext.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.pgec.webservice.mip.dati.MIPSavePaymentStatusRequest;
import com.seda.payer.pgec.webservice.mip.dati.MIPSavePaymentStatusResponse;
import com.seda.payer.pgec.webservice.mip.dati.ModuloIntegrazionePagamentiPaymentStatus;

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
				String numeroOperazione = (String)request.getAttribute("numop");
				String idPortale =  (String)request.getAttribute("idportale");
				if (!idTransazione.equals(""))
				{
					initMPS(idTransazione, numeroOperazione, idPortale, nomeForm, request, session);
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
		request.setAttribute("action", (String)request.getAttribute("action"));
		request.setAttribute("vista", (String)request.getAttribute("vista"));

		return null;
		
		
	}

	
	private boolean initMPS(String idTransazione, String numeroOperazione, String idPortale, String formName, HttpServletRequest request, HttpSession session)
	{
		UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		
		ModuloIntegrazionePagamentiPaymentStatus mipPS = new ModuloIntegrazionePagamentiPaymentStatus();
		mipPS.setChiaveTransazione(idTransazione);
		mipPS.setModalitaNotifica("M");
		mipPS.setOperatoreUltimoAggiornamento(userBean.getUserName());
		mipPS.setIdPortale(idPortale);
		mipPS.setNumeroOperazione(numeroOperazione);
		
		MIPSavePaymentStatusRequest reqPS = new MIPSavePaymentStatusRequest(mipPS);
		try {
			MIPSavePaymentStatusResponse resPS = WSCache.mipServer.savePaymentStatus(reqPS, request);

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
