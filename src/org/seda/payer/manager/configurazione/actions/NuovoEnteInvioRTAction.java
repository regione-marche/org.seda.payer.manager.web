package org.seda.payer.manager.configurazione.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.ManagerKeys;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.core.bean.ConfigRtEnte;
import com.seda.payer.core.dao.ConfigRtEnteDao;
import com.seda.payer.core.exception.DaoException;

public class NuovoEnteInvioRTAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String societa;
	private String utente;
	private String ente;
	private String codiceIPA;
	
	@Override
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
			setProfile(request);
		}
		loadSocietaUtenteEnteXml_DDL(request, session);
		switch(firedButton) {
			case TX_BUTTON_RESET:
				if(request.getAttribute("codop") != null && request.getAttribute("codop").equals("edit")) {
					
					request.setAttribute("tx_utentelogin", "");
					request.setAttribute("tx_password", "");
					request.setAttribute("tx_URL", "");
					request.setAttribute("tx_mail", "");
					request.setAttribute("tx_ente_abilitato", "");
					
					session.setAttribute("tx_utentelogin", "");
					session.setAttribute("tx_password", "");
					session.setAttribute("tx_URL", "");
					session.setAttribute("tx_mail", "");
					session.setAttribute("tx_ente_abilitato", "");
					
				} else {					
					resetParams(request, session);
				}
				
				break;
				
			case TX_BUTTON_NULL:
				resetParams(request, session);
				
				break;
				
			case TX_BUTTON_ADD:
				if(request.getAttribute("codop") == null || !request.getAttribute("codop").equals("edit")){
					dividiSocUtenteEnte(request);
				} 
				
				societa = (String) request.getAttribute("tx_societa");
				utente = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
				ente = (String) request.getAttribute("tx_ente");
				codiceIPA = (String) request.getAttribute("tx_codiceIPA");
				
				if(request.getAttribute("codop") == null || request.getAttribute("codop").equals("") || request.getAttribute("codop").equals("add")){
					if(checkConfigurazioneEsistente(societa, utente, ente, codiceIPA, request)) {
						request.setAttribute("tx_error_message", "Errore: Configurazione esistente.");
					}else {
						save(request);				
					}
				} else {
					save(request);
				}
				
				resetParams(request, session);
//				String descSoc = (String) request.getAttribute("desc_soc");
//				String descUte = (String) request.getAttribute("desc_ute");
//				String descEnte = (String) request.getAttribute("desc_ente");
//				request.setAttribute("tx_societa", descSoc);
//				request.setAttribute("tx_utente", descUte);
//				request.setAttribute("tx_ente", descEnte);
				break;
				
			case TX_BUTTON_EDIT:
				request.setAttribute("ddlSocietaUtenteEnte", 
						request.getAttribute("desc_soc") + " / " + 
						request.getAttribute("desc_ute") + " / " +
						request.getAttribute("desc_ente"));
				edit(request);
				
				break;
			case TX_BUTTON_INDIETRO:
				resetParams(request, session);
//				request.setAttribute("tx_societa", request.getAttribute("desc_soc"));
//				request.setAttribute("tx_utente", request.getAttribute("desc_ute"));
//				request.setAttribute("tx_ente", request.getAttribute("desc_ente"));
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
		session.setAttribute("tx_ente", "");
		session.setAttribute("tx_codiceIPA", "");
		session.setAttribute("tx_utentelogin", "");
		session.setAttribute("tx_password", "");
		session.setAttribute("tx_URL", "");
		session.setAttribute("tx_mail", "");
		session.setAttribute("tx_ente_abilitato", "");
		session.setAttribute("tx_societa_s", "");
		session.setAttribute("tx_utente_s", "");
		session.setAttribute("tx_ente_s", "");
		session.setAttribute("tx_codiceIPA_s", "");
		
	}
	
	private void dividiSocUtenteEnte(HttpServletRequest request) {
		if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
		{
			String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			if (!ddlSocietaUtenteEnte.equals("")&&codici.length>0) 
			{
				societa = codici[0];
				utente = codici[1];
				ente = codici[2];
				request.setAttribute("tx_societa", societa);
				request.setAttribute("tx_utente", utente);
				request.setAttribute("tx_ente", ente);

			}
		}
	}
}
