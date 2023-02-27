package org.seda.payer.manager.ioitalia.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.ManagerKeys;

import com.seda.j2ee5.maf.core.action.ActionException;

public class IoItaliaCaricaCsvAction extends IoItaliaBaseManagerAction {
	
	private String societa = "";
	private String codiceUtente = "";
	private String provincia = "";
	private String ente = "";

	private static final long serialVersionUID = -2797624591241208567L;

	@Override
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);	
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
			setProfile(request);
		}
		if(session.getAttribute("tx_societa") != null || session.getAttribute("tx_provincia") != null) {
			session.setAttribute("tx_societa", null);
			session.setAttribute("tx_provincia", null);
		}
		societa = (String) request.getAttribute("tx_societa");
		provincia = (String) request.getAttribute("tx_provincia");
		
		loadSocietaXml_DDL(request);
		//codiceUtente = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		codiceUtente = getParamCodiceUtente();
		loadProvinciaXml_DDL(request, session, societa, true);
		LoadListaUtentiEntiXml_DDL(request, session, societa, provincia, ente, codiceUtente, true);
		
		session.setAttribute("tx_societa_s", societa);
		session.setAttribute("tx_ente_s", getParamCodiceEnte());
//		YLM PG22XX06 INI
		session.setAttribute("tx_message", null);
//		YLM PG22XX06 FINE	
		
		return null;
	}
}
