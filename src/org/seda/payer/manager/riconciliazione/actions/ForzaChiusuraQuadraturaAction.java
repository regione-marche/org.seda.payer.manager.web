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
import com.seda.payer.pgec.webservice.commons.dati.ForzaChiusuraQuadraturaRequest;
import com.seda.payer.pgec.webservice.commons.dati.ForzaChiusuraQuadraturaResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiCBIResponse;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.dati.RiepilogoMovimentiCBI;

public class ForzaChiusuraQuadraturaAction extends BaseRiconciliazioneAction {

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
		String chiaveMovimento = request.getParameter("chiaveMovimento2");
		if (chiaveMovimento != null) {
			boolean closed = false;
			UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
			ForzaChiusuraQuadraturaResponse res = null;
			try {
				WSCache.logWriter.logDebug("chiusura quadratura in corso: chiave movimento='"+chiaveMovimento+"' utente='"+user.getUserName()+"'");
				ForzaChiusuraQuadraturaRequest forzaChiusuraQuadraturaRequest = new  ForzaChiusuraQuadraturaRequest();
				forzaChiusuraQuadraturaRequest.setChiaveMovimento(Integer.parseInt(chiaveMovimento));
				forzaChiusuraQuadraturaRequest.setOperatore(user.getUserName());
				res =  WSCache.commonsServer.forzaChiusuraQuadratura(forzaChiusuraQuadraturaRequest, request);
				RecuperaMovimentiCBIResponse movimentiCBI = getListaMovimenti(request);
				PageInfo pageInfo = getMovimentiPageInfo(movimentiCBI.getPageInfo());
				if(pageInfo.getNumRows() > 0) {
					String lista = movimentiCBI.getMovimentiCBIXml();					
					session.setAttribute(Field.LISTA_MOVIMENTI.format(), lista);
					session.setAttribute(Field.LISTA_MOVIMENTI_PAGEINFO.format(), pageInfo);
					RiepilogoMovimentiCBI[] listaGrouped= movimentiCBI.getRiepilogoMovimentiCBI();
					session.setAttribute(Field.LISTA_MOVIMENTI_GROUPED.format(), listaGrouped);
				}
//				else 
					//setMessage(Messages.TX_NO_TRANSACTIONS.format());
 
				closed = res.getResponse().getRetCode().equals(ResponseTypeRetCode.value1);
			} 
			catch (Exception e) {
// non serve a nulla
				//				setErrorMessage(e.getMessage());
				WSCache.logWriter.logError("chiusura quadratura dal movimento fallita: chiave movimento='"+chiaveMovimento+"' utente='"+user.getUserName()+"'", e);
			}
			if(closed) {
//				setMessage(Messages.CHIUSURA_QUADRATURA_OK.format(chiaveMovimento));
				setFormMessage("riconciliazioneTransazioniForm", Messages.CHIUSURA_QUADRATURA_OK.format(chiaveMovimento), request);
				WSCache.logWriter.logDebug("chiusura quadratura dal movimento effettuata: chiave movimento='"+chiaveMovimento+"' utente='"+user.getUserName()+"' esito='"+res.getResponse().getRetMessage()+"'");
			}
			else {
//				setMessage(Messages.CHIUSURA_QUADRATURA_KO.format(chiaveMovimento));
				setFormMessage("riconciliazioneTransazioniForm", Messages.CHIUSURA_QUADRATURA_OK.format(chiaveMovimento), request);
				WSCache.logWriter.logDebug("chiusura quadratura dal movimento fallita: chiave movimento='"+chiaveMovimento+"' utente='"+user.getUserName()+"' esito='"+res.getResponse().getRetMessage()+"'");
			}
			
//			request.setAttribute(Field.TX_MESSAGE.format(), getMessage());
//			request.setAttribute(Field.TX_ERROR_MESSAGE.format(), getErrorMessage());

			request.setAttribute(Field.LISTA_MOVIMENTI.format(), session.getAttribute(Field.LISTA_MOVIMENTI.format()));
			request.setAttribute(Field.LISTA_MOVIMENTI_PAGEINFO.format(), session.getAttribute(Field.LISTA_MOVIMENTI_PAGEINFO.format()));
			request.setAttribute(Field.LISTA_MOVIMENTI_GROUPED.format(), session.getAttribute(Field.LISTA_MOVIMENTI_GROUPED.format()));
		}
		return null;
	}

}
