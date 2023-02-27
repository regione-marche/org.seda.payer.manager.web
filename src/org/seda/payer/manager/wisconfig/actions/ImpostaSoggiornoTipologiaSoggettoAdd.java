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
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTipologiaSoggettoRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTipologiaSoggettoResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.TipologiaSoggetto;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaProvinceResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoTipologiaSoggettoAdd extends BaseManagerAction
{
	private static String codop = "add";
	private static String ritornaViewstate = "impostaSoggiornoTipologiaSoggetti_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
	private String codiceProvincia = "";
	//inizio LP PG1800XX_016
	String templateName = "";
	//fine LP PG1800XX_016

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		//inizio LP PG1800XX_016
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		templateName = userBean.getTemplate(applicationName);
		
		if (templateName.equals("sassari"))
		{
			request.setAttribute("tx_provincia","SS");
			request.setAttribute("tx_comune","I452");
		}
		//fine LP PG1800XX_016
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
			//inizio LP PG1800XX_016
			if (templateName.equals("sassari"))
			{
				request.setAttribute("tx_provincia","SS");
				request.setAttribute("tx_comune","I452");
			}
			//fine LP PG1800XX_016
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
			parametri = getTipologiaSoggettoFormParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			String esito =  checkTipologiaSoggetto(parametri);
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
				
					TipologiaSoggetto tipoSoggetto = new TipologiaSoggetto("", 
															parametri.get("codiceComune").toString(),
																parametri.get("codiceTipoSoggetto").toString(), 
																	parametri.get("descrizioneTipoSoggetto").toString(), 
																		parametri.get("flagEsenzione").toString(), null, usernameAutenticazione,
																		parametri.get("descrizioneAggiuntiva").toString(), Integer.valueOf(parametri.get("progressivoOrdinamento").toString())	//PG180050_001 GG
																		);
				
				InserisciTipologiaSoggettoRequest in = new InserisciTipologiaSoggettoRequest();
				in.setTipologiaSoggetto(tipoSoggetto);
				
				try {
					InserisciTipologiaSoggettoResponse out = WSCache.impostaSoggiornoConfigServer.inserisciTipologiaSoggetti(in, request);
					
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
		request.setAttribute("do_command_name","ImpostaSoggiornoTipologiaSoggettoAdd.do");
		request.setAttribute("codop",codop);

		//loadDDLStatic(request, session);

		codiceProvincia = (request.getAttribute("tx_provincia") == null ? "" : (String)request.getAttribute("tx_provincia"));
		loadListaProvince(request);
		loadBelfiore(codiceProvincia, request);
		
		aggiornamentoCombo(request, session);

		return null;
		
	}
	
	private void loadListaProvince(HttpServletRequest request)
	{
		RecuperaProvinceResponse getProvinceRes = new RecuperaProvinceResponse();
		try {
			getProvinceRes = WSCache.commonsServer.recuperaProvince(request);
		} catch (com.seda.payer.pgec.webservice.commons.srv.FaultType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("listProvince", getProvinceRes.getListXml());
	}

	private void loadBelfiore(String siglaProvincia, HttpServletRequest request) {
		if (siglaProvincia != null && siglaProvincia.length() > 0)
		{
			RecuperaBelfioreResponse getBelfioreRes = new RecuperaBelfioreResponse();
			try {
				getBelfioreRes = WSCache.commonsServer.recuperaBelfiore(new RecuperaBelfioreRequest(siglaProvincia), request);
			} catch (com.seda.payer.pgec.webservice.commons.srv.FaultType e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("listbelfiore", getBelfioreRes.getListXml());
		}
	}
	
	private HashMap<String,Object> getTipologiaSoggettoFormParameters(HttpServletRequest request, String codop)
	{
		HashMap<String,Object> param = new HashMap<String, Object>();
		codiceProvincia = (request.getAttribute("tx_provincia") == null ? "" : (String)request.getAttribute("tx_provincia"));
		String codiceComune = (request.getAttribute("tx_comune") == null ? "" : (String)request.getAttribute("tx_comune"));
		String chiaveTipoSoggetto = (request.getAttribute("tx_chiave_soggetti") == null ? "" : (String)request.getAttribute("tx_chiave_soggetti"));
		String codiceTipoSoggetto = (request.getAttribute("tx_cod_soggetto") == null ? "" : (String)request.getAttribute("tx_cod_soggetto"));
		String descrizioneTipoSoggetto = (request.getAttribute("tx_desc_soggetto") == null ? "" : (String)request.getAttribute("tx_desc_soggetto"));
		String flagEsenzione = (request.getAttribute("ddl_flag_esenzione") == null ? "N" : (String)request.getAttribute("ddl_flag_esenzione"));
		//PG180050_001 GG - inizio
		String descrizioneAggiuntiva = (request.getAttribute("tx_desc_aggiuntiva_soggetto") == null ? "" : (String)request.getAttribute("tx_desc_aggiuntiva_soggetto"));
		Integer progressivoOrdinamento = (request.getAttribute("tx_prog_ordinamento") == null ? 0 : Integer.valueOf((String)request.getAttribute("tx_prog_ordinamento")));
		//PG180050_001 GG - fine

		param.put("codiceComune", codiceComune);
		param.put("chiaveTipoSoggetto", chiaveTipoSoggetto);
		param.put("codiceTipoSoggetto", codiceTipoSoggetto);
		param.put("descrizioneTipoSoggetto", descrizioneTipoSoggetto);
		param.put("flagEsenzione", flagEsenzione);
		param.put("codiceProvincia", codiceProvincia);
		//PG180050_001 GG - inizio
		param.put("descrizioneAggiuntiva", descrizioneAggiuntiva);
		param.put("progressivoOrdinamento", progressivoOrdinamento);
		//PG180050_001 GG - fine

		return param;
	}

	private String checkTipologiaSoggetto(HashMap<String,Object> parametri )
	{
		/*
		 * Controllo che sia stato selezionato un item dalle due drop-down-list
		 */
		//DDL Provincia
		if (((String)parametri.get("codiceProvincia")).equals(""))
			return Messages.SELEZIONARE_UN_ELEMENTO_LISTA.format("Provincia");
		//DDL Comune
		if (((String)parametri.get("codiceComune")).equals(""))
			return Messages.SELEZIONARE_UN_ELEMENTO_LISTA.format("Comune");
		
		return "OK";
	}
	
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
		switch(getFiredButton(request)) 
		{
			   case TX_BUTTON_PROVINCIA_CHANGED:
				loadBelfiore(codiceProvincia, request);
					break;
				default:
				loadBelfiore(codiceProvincia, request);
		}		
	}
	


}
