package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.TipologiaStrutturaRicettiva;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoTipologiaStruttureAddAction extends BaseManagerAction
{
	private static String codop = "add";
	private static String ritornaViewstate = "impostasoggiornoTipologiaStrutture_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
//	private String codiceProvincia = "";

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Eseguo le azioni
		 */
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
			resetParametri(request);
			break;
			
		case TX_BUTTON_NULL:
			break;
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("vista",ritornaViewstate);
			break;

		case TX_BUTTON_AGGIUNGI:
			/*
			 * Recupero i parametri dalla form
			 */
			parametri = getTipologiaStruttureFormParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			String esito =  checkTipologiaStruttura(parametri);
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
				
					TipologiaStrutturaRicettiva tipologiaStrutturaRicettiva = new TipologiaStrutturaRicettiva(
							                                                 parametri.get("codiceTipologiaStruttura").toString(),
																                     parametri.get("descrizioneTipologiaStruttura").toString(), 
																	                              null, usernameAutenticazione);
				
				InserisciTipologiaStrutturaRicettivaRequest in = new InserisciTipologiaStrutturaRicettivaRequest();
//				in.setTipologiaSoggetto(tipoSoggetto);
				in.setTipologiaStrutturaRicettiva(tipologiaStrutturaRicettiva);
				try {
					InserisciTipologiaStrutturaRicettivaResponse out = WSCache.impostaSoggiornoConfigServer.inserisciTipologiaStrutturaRicettiva(in, request);
					
					if (out != null)
					{
						if(out.getRetCode().equals("00")){
							request.setAttribute("vista",ritornaViewstate);
							//resetto i parametri se inserito
							resetParametri(request);
						} 
							setFormMessage("var_form", out.getRetMessage(), request);

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
		
		/*case TX_BUTTON_SOCIETA_CHANGED:
			parametri = getAbilitaCanalePagamentoTipoServizioEnteFormParameters(request,codop);
			
			if (!parametri.get("codiceSocieta").toString().equals(""))
				loadTipologiaServizioXml_DDL(request, session, parametri.get("codiceSocieta").toString(), true);
			
			request.setAttribute(Field.TX_BUTTON_SOCIETA_CHANGED.format(), null);
			request.setAttribute("fired_button_hidden", null);
			request.setAttribute(Field.TX_BUTTON_AGGIUNGI.format(),Field.TX_BUTTON_AGGIUNGI.format());
			
			break;*/
		}
		
		/*
		 * Se non esco dalla pagina carico le drop-down-lists
		 */
		/*if (!firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
		{
			loadSocietaUtenteEnteXml_DDL(request, session);
			//loadSocietaUtenteServizioEnteXml_DDL(request, session);
			if (firedButton.equals(FiredButton.TX_BUTTON_AGGIUNGI))
				loadTipologiaServizioXml_DDL(request, session, "", false);
			
			loadCanaliPagamentoAbilitazioneXml_DDL(request, session);
		}*/	
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
//		request.setAttribute("do_command_name","ImpostaSoggiornoTipologiaSoggettoAdd.do");
		request.setAttribute("do_command_name","tipologiastruttureAdd.do");	
		request.setAttribute("codop",codop);
		request.setAttribute("bmodify", true);

		//loadDDLStatic(request, session);

//		codiceProvincia = (request.getAttribute("tx_provincia") == null ? "" : (String)request.getAttribute("tx_provincia"));
//		loadListaProvince(request);
//		loadBelfiore(codiceProvincia, request);
		
//		aggiornamentoCombo(request, session);

		return null;
		
	}
	
/*	private void loadListaProvince(HttpServletRequest request)
	{
		RecuperaProvinceResponse getProvinceRes = new RecuperaProvinceResponse();
		try {
			getProvinceRes = WSCache.commonsServer.recuperaProvince();
		} catch (com.seda.payer.pgec.webservice.commons.srv.FaultType e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		request.setAttribute("listProvince", getProvinceRes.getListXml());
	}

	private void loadBelfiore(String siglaProvincia, HttpServletRequest request) {
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
		String codiceTipologiaStruttura = (request.getAttribute("tx_codice") == null ? "" : (String)request.getAttribute("tx_codice"));
		String descrizioneTipologiaStruttura = (request.getAttribute("tx_descrizione") == null ? "" : (String)request.getAttribute("tx_descrizione"));
//		String chiaveTipoSoggetto = (request.getAttribute("tx_chiave_soggetti") == null ? "" : (String)request.getAttribute("tx_chiave_soggetti"));
//		String codiceTipoSoggetto = (request.getAttribute("tx_cod_soggetto") == null ? "" : (String)request.getAttribute("tx_cod_soggetto"));
//		String descrizioneTipoSoggetto = (request.getAttribute("tx_desc_soggetto") == null ? "" : (String)request.getAttribute("tx_desc_soggetto"));
//		String flagEsenzione = (request.getAttribute("ddl_flag_esenzione") == null ? "N" : (String)request.getAttribute("ddl_flag_esenzione"));

		param.put("codiceTipologiaStruttura", codiceTipologiaStruttura);
		param.put("descrizioneTipologiaStruttura", descrizioneTipologiaStruttura);
//		param.put("codiceTipoSoggetto", codiceTipoSoggetto);
//		param.put("descrizioneTipoSoggetto", descrizioneTipoSoggetto);
//		param.put("flagEsenzione", flagEsenzione);
//		param.put("codiceProvincia", codiceProvincia);

		return param;
	}

	private String checkTipologiaStruttura(HashMap<String,Object> parametri )
	{

		if (((String)parametri.get("codiceTipologiaStruttura")).equals(""))
			return Messages.SPECIFICA_IL_CAMPO.format("Codice Tipologia");
		
		if (((String)parametri.get("descrizioneTipologiaStruttura")).equals(""))
			return Messages.SPECIFICA_IL_CAMPO.format("Descrizione Tipologia");
		
		return "OK";
	}
	
/*	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
		switch(getFiredButton(request)) 
		{
			   case TX_BUTTON_PROVINCIA_CHANGED:
				loadBelfiore(codiceProvincia, request);
					break;
				default:
				loadBelfiore(codiceProvincia, request);
		}		
	}*/
	


}
