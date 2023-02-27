package org.seda.payer.manager.riconciliazione.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.pgec.webservice.commons.dati.AggiornaRiconciliazioneRequest;
import com.seda.payer.pgec.webservice.commons.dati.AggiornaRiconciliazioneResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiApertiResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniNonQuadrateResponse;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.dati.RiepilogoTransazioniNonQuadrate;

public class EliminaAssociazioneAction extends BaseRiconciliazioneAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
//		setProfile(request);
		
		HttpSession session = request.getSession();
		loadDDLStatic(request, session);
		loadDDLProvincia(request, session, request.getParameter("tx_societa"),false);
		loadDDLUtente(request, session, request.getParameter("tx_societa"), request.getParameter("tx_provincia"),false);
		String codiceTransazione = request.getParameter("chiaveTransazioneMov");
		String chiaveMovimento = request.getParameter("chiaveMovimento");
		boolean closed = false;
		tx_SalvaStato(request);
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		AggiornaRiconciliazioneResponse res = null;
		try {
			WSCache.logWriter.logDebug("rimozione transazione dal movimento in corso: id transazione='"+codiceTransazione+"' chiave movimento='"+chiaveMovimento+"' utente='"+user.getUserName()+"'");
			AggiornaRiconciliazioneRequest aggiornaRiconciliazioneRequest = new  AggiornaRiconciliazioneRequest(codiceTransazione, "0", user.getUserName());
			res =  WSCache.commonsServer.aggiornaRiconciliazione(aggiornaRiconciliazioneRequest, request);
			RecuperaTransazioniNonQuadrateResponse listaTransazioni = getTransazioniNonQuadrate(request, chiaveMovimento);
			PageInfo pageInfo = getPageInfo(listaTransazioni.getPageInfo());
			if(pageInfo.getNumRows() > 0) {
				String lista = listaTransazioni.getTransazioniNonQuadrateXml();					
				request.setAttribute(Field.LISTA_TRANSAZIONI_NQ.format(), lista);
				request.setAttribute(Field.LISTA_TRANSAZIONI_NQ_PAGEINFO.format(), pageInfo);
				RiepilogoTransazioniNonQuadrate riepilogo = listaTransazioni.getRiepilogoTransazioniNonQuadrate();
				request.setAttribute(Field.RIEPILOGO_TRANSAZIONI_NQ.format(), riepilogo);
			}
			else setMessage(Messages.TX_NO_TRANSACTIONS.format());
			closed = res.getResponse().getRetCode().equals(ResponseTypeRetCode.value1);
		} 
		catch (Exception e) {
			setErrorMessage(e.getMessage());
			WSCache.logWriter.logError("rimozione transazione dal movimento fallita: id transazione='"+codiceTransazione+"' chiave movimento='"+chiaveMovimento+"' utente='"+user.getUserName()+"'",e);
		}
		if(closed) {
			setMessage(Messages.ELIMINA_ASSOCIAZIONE_OK.format());
			WSCache.logWriter.logDebug("rimozione transazione dal movimento effettuata: id transazione='"+codiceTransazione+"' chiave movimento='"+chiaveMovimento+"' utente='"+user.getUserName()+"' esito='"+res.getResponse().getRetMessage()+"'");
		}
		else {
			setMessage(Messages.ELIMINA_ASSOCIAZIONE_KO.format());
			WSCache.logWriter.logDebug("rimozione transazione dal movimento fallita: id transazione='"+codiceTransazione+"' chiave movimento='"+chiaveMovimento+"' utente='"+user.getUserName()+"' esito='"+res.getResponse().getRetMessage()+"'");
		}
		try {
			RecuperaMovimentiApertiResponse movimentiAperti = getListaMovimentiAperti(request);
			request.setAttribute(Field.LISTA_MOVIMENTI_APERTI.format(), movimentiAperti.getMovimentiCBIXml());
		} 
		catch (Exception e) {
			setErrorMessage(e.getMessage());
			return null;
		}
		request.setAttribute(Field.TX_MESSAGE.format(), getMessage());
		request.setAttribute(Field.TX_ERROR_MESSAGE.format(), getErrorMessage());
		return null;
	}

}
