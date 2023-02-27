package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.ConfigRtEnte;
import com.seda.payer.core.dao.ConfigRtEnteDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteResponse;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteResponsePageInfo;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteSearchRequest;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteSearchResponse;

public class EntiInvioRTAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
			setProfile(request);
		}
		switch(firedButton) {

			case TX_BUTTON_RESET:
				resetParams(request, session);
				break;
			case TX_BUTTON_DELETE:
				cancel(request);
				resetParams(request, session);
				search(request);
				break;
			case TX_BUTTON_NULL:
				resetParams(request, session);
				break;
			case TX_BUTTON_INDIETRO:
				resetParams(request, session);
			case TX_BUTTON_ADD:
			case TX_BUTTON_CERCA:
				search(request);
				break;
				
			case TX_BUTTON_AGGIUNGI:
				break;
				
			default:
				break;
		}
		
		return null;
	}

	protected void resetParams(HttpServletRequest request, HttpSession session) {
		request.setAttribute("ddlSocietaUtenteEnte", "");
		request.setAttribute("tx_societa", "");
		request.setAttribute("tx_utente", "");
		request.setAttribute("tx_ente", "");
		request.setAttribute("tx_codiceIPA", "");
		request.setAttribute("tx_utentelogin", "");
		request.setAttribute("tx_password", "");
		request.setAttribute("tx_URL", "");
		request.setAttribute("tx_mail", "");
		request.setAttribute("tx_ente_abilitato", "");
		
		session.setAttribute("ddlSocietaUtenteEnte", "");
		session.setAttribute("tx_societa", "");
		session.setAttribute("tx_utente", "");
		session.setAttribute("tx_ente", "");
		session.setAttribute("tx_codiceIPA", "");
		session.setAttribute("tx_utentelogin", "");
		session.setAttribute("tx_password", "");
		session.setAttribute("tx_URL", "");
		session.setAttribute("tx_mail", "");
		session.setAttribute("tx_ente_abilitato", "");
	}
	
	
	
}
