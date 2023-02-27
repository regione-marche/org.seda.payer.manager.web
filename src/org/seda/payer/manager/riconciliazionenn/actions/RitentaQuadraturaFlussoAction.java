package org.seda.payer.manager.riconciliazionenn.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.pgec.webservice.commons.dati.RitentaQuadraturaFlussoNodoRequest;
import com.seda.payer.pgec.webservice.commons.dati.RitentaQuadraturaFlussoNodoResponse;

public class RitentaQuadraturaFlussoAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		String idquad = (String)request.getAttribute("idquad");
		Long keyQuadratura = new Long(idquad);
		
		try {
			WSCache.logWriter.logDebug("ritenta quadratura flusso in corso: utente='" + user.getUserName() + "'");
			RitentaQuadraturaFlussoNodoResponse res = WSCache.commonsServer.ritentaQuadraturaFlussoNodo(new RitentaQuadraturaFlussoNodoRequest(keyQuadratura), request); 
			
			if (res != null)
			{
				String resCode = res.getRetCode();
				
				if (resCode.equals("00")) {
					//se l'operazione ha esito positivo allora imposto attributi
					//e parameter della request per rieseguire la ricerca per la
					//chiave quadratura del flusso processato
					request.setAttribute("tx_button_cerca", "1");
					//inizio LP PG21XX05 - Lascio inalterati i parametri di ricerca
					//request.getParameterMap().putIfAbsent("keyQuadratura", "" + keyQuadratura);	
					//fine LP PG21XX05 - Lascio inalterati i parametri di ricerca
					setFormMessage("riconciliazioneTransazioniNodoForm", "Procedura ritenta quadratura del flusso ESEGUITA.", request);
				} else {
					String mess = "Procedura ritenta quadratura del flusso NON riuscita";
					String mess0 = res.getRetMessage(); 
					if(mess0 != null && mess0.trim().length() > 0) {
						mess += " " +  mess0.trim();
					}
					mess += ".";
					setFormMessage("riconciliazioneTransazioniNodoForm", mess, request);
				}
			}
			else
				setFormMessage("riconciliazioneTransazioniNodoForm", "Errore nella procedura ritenta quadratura del flusso." , request);
		} 
		catch (Exception e) {
			setErrorMessage(e.getMessage());
			WSCache.logWriter.logError("Procedura ritenta quadratura del fluss fallita: utente='" + user.getUserName() + "'", e);
		}
		return null;
	}

}
