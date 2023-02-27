package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.TipologiaStrutturaRicettiva;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoTipologiaStruttureEditAction extends BaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "impostasoggiornoTipologiaStrutture_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
//	private String codiceProvincia = "";
	private String codiceTipologiaStrutture = "";
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		//String codiceComune = "";
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			codiceTipologiaStrutture = (request.getAttribute("tx_codice") == null ? "" : (String)request.getAttribute("tx_codice"));
//			String chiaveTipoSogg = (request.getAttribute("tx_chiave_soggetti") == null ? "" : (String)request.getAttribute("tx_chiave_soggetti"));
			if (!codiceTipologiaStrutture.equals("")) 
//					&& !chiaveTipoSogg.equals(""))
			{				
				RecuperaTipologiaStrutturaRicettivaResponse detailResponse = null;
				TipologiaStrutturaRicettiva tipologiaStrutturaRicettiva = new TipologiaStrutturaRicettiva(codiceTipologiaStrutture, "",  null, "");
				RecuperaTipologiaStrutturaRicettivaRequest in = new RecuperaTipologiaStrutturaRicettivaRequest();
				try {
					in.setChiaveTipologiaStruttura(tipologiaStrutturaRicettiva.getChiaveTipologiaStruttura());
					detailResponse = WSCache.impostaSoggiornoConfigServer.recuperaTipologiaStrutturaRicettiva(in, request);
					if (detailResponse != null)
					{
						if (detailResponse.getTipologiaStruttura() != null)
						{
							request.setAttribute("tx_chiave_tipologia", detailResponse.getTipologiaStruttura().getChiaveTipologiaStruttura());
							request.setAttribute("tx_codice", detailResponse.getTipologiaStruttura().getChiaveTipologiaStruttura());
							request.setAttribute("tx_descrizione", detailResponse.getTipologiaStruttura().getDescrizioneTipologiaStruttura());
						}
						else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
					else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				} catch (FaultType e) {
					setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("var_form", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				}
			}
			break;
			
		case TX_BUTTON_EDIT:
			/*
			 * Recupero i parametri dalla form
			 */
			parametri = getTipologiaStruttureFormParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			String esito = checkTipologiaStrutture(parametri);
			/*
			 * Se OK effettuo il salvataggio
			 */
			if (esito.equals("OK"))
			{
				//Recupero del codice operatore
				UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
				if( user!=null) 
				   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
					usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
					Calendar cal = Calendar.getInstance();
					TipologiaStrutturaRicettiva tipoStrutturaRicettiva = new TipologiaStrutturaRicettiva(parametri.get("codiceTipologiaStrutture").toString(),
																                               parametri.get("descrizioneTipologiaStrutture").toString(),
																                               cal, 
																                               usernameAutenticazione);
																	
				
				AggiornaTipologiaStrutturaRicettivaRequest in = new AggiornaTipologiaStrutturaRicettivaRequest();
				in.setTipologiaStrutturaRicettiva(tipoStrutturaRicettiva);
				
				try {
					AggiornaTipologiaStrutturaRicettivaResponse out = WSCache.impostaSoggiornoConfigServer.aggiornaTipologiaStrutturaRicettiva(in, request);
					if (out != null)
					{
						if(out.equals("00")){
							session.setAttribute("recordUpdated", out.getRetMessage());
							request.setAttribute("vista",ritornaViewstate);
						}else setFormMessage("var_form", out.getRetMessage(), request);

					}else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				} catch (FaultType e) {
					setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("var_form", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				}
			}
			else
				setFormMessage("var_form", esito, request);
			break;
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("vista",ritornaViewstate);
			break;
			
		}
		/*
		 * Se non esco dalla pagina carico le drop-down-lists
		 * NOTA: le drop-down-lists, con "codop == edit" vengono
		 * disabilite nella jsp
		 */
		/*if (!firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
		{
			loadSocietaUtenteEnteXml_DDL(request, session);
			//loadSocietaUtenteServizioEnteXml_DDL(request, session);
			loadTipologiaServizioXml_DDL(request, session, codiceSocieta, true);
			
		}*/
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","tipologiastruttureEdit.do");
		request.setAttribute("codop",codop);
		request.setAttribute("bmodify", false);
//		loadDDLStatic(request, session);
//		loadBelfiore(codiceProvincia, request);
		
		//aggiornamentoCombo(request, session);
		return null;
	}
	

/*	private void loadBelfiore(String siglaProvincia, HttpServletRequest request) {
		if (siglaProvincia != null && siglaProvincia.length() > 0)
		{
			RecuperaBelfioreResponse getBelfioreRes = new RecuperaBelfioreResponse();
			try {
				getBelfioreRes = WSCache.commonsServer.recuperaBelfiore(new RecuperaBelfioreRequest(siglaProvincia));
			} catch (com.seda.payer.pgec.webservice.commons.srv.FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			request.setAttribute("listbelfiore", getBelfioreRes.getListXml());
		}
	}*/
	
	private HashMap<String,Object> getTipologiaStruttureFormParameters(HttpServletRequest request, String codop)
	{
		HashMap<String,Object> param = new HashMap<String, Object>();
		String codiceTipologiaStrutture = (request.getAttribute("tx_codice") == null ? "" : (String)request.getAttribute("tx_codice"));
		String descrizioneTipologiaStrutture = (request.getAttribute("tx_descrizione") == null ? "" : (String)request.getAttribute("tx_descrizione"));
//		String chiaveTipoSoggetto = (request.getAttribute("tx_chiave_soggetti") == null ? "" : (String)request.getAttribute("tx_chiave_soggetti"));
//		String codiceTipoSoggetto = (request.getAttribute("tx_cod_soggetto") == null ? "" : (String)request.getAttribute("tx_cod_soggetto"));
//		String descrizioneTipoSoggetto = (request.getAttribute("tx_desc_soggetto") == null ? "" : (String)request.getAttribute("tx_desc_soggetto"));
//		String flagEsenzione = (request.getAttribute("ddl_flag_esenzione") == null ? "N" : (String)request.getAttribute("ddl_flag_esenzione"));
		
		param.put("codiceTipologiaStrutture", codiceTipologiaStrutture);
		param.put("descrizioneTipologiaStrutture", descrizioneTipologiaStrutture);
		
/*		param.put("codiceComune", codiceComune);
		param.put("chiaveTipoSoggetto", chiaveTipoSoggetto);
		param.put("codiceTipoSoggetto", codiceTipoSoggetto);
		param.put("descrizioneTipoSoggetto", descrizioneTipoSoggetto);
		param.put("flagEsenzione", flagEsenzione);
		param.put("codiceProvincia", codiceProvincia);*/

		return param;
	}
	private String checkTipologiaStrutture(HashMap<String,Object> parametri )
	{
		/*
		 * Controllo che sia stato selezionato un item dalle due drop-down-list
		 */
		//DDL Provincia
		if (((String)parametri.get("codiceTipologiaStrutture")).equals(""))
			return Messages.SPECIFICA_IL_CAMPO.format("Codice");
		//DDL Comune
		if (((String)parametri.get("descrizioneTipologiaStrutture")).equals(""))
			return Messages.SPECIFICA_IL_CAMPO.format("Descrizione");
		
		return "OK";
	}
	
}
