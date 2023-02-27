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

import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTipologiaSoggettoRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTipologiaSoggettoResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaSoggettoRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaSoggettoResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.TipologiaSoggetto;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaProvinceResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoTipologiaSoggettoEdit extends BaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "impostaSoggiornoTipologiaSoggetti_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
	private String codiceProvincia = "";

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		String codiceComune = "";
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			codiceComune = (request.getAttribute("tx_comune") == null ? "" : (String)request.getAttribute("tx_comune"));
			String chiaveTipoSogg = (request.getAttribute("tx_chiave_soggetti") == null ? "" : (String)request.getAttribute("tx_chiave_soggetti"));
			if (!codiceComune.equals("") 
					&& !chiaveTipoSogg.equals(""))
			{				
				RecuperaTipologiaSoggettoResponse detailResponse = null;
				//PG180050_001 GG Introdotti descrizioneAggiuntiva e progressivoOrdinamento
				TipologiaSoggetto tipoSoggetto = new TipologiaSoggetto(chiaveTipoSogg, codiceComune, "", "", "", null, "", "", 0);
				RecuperaTipologiaSoggettoRequest in = new RecuperaTipologiaSoggettoRequest();
				try {
					in.setTipologiaSoggetto(tipoSoggetto);
					detailResponse = WSCache.impostaSoggiornoConfigServer.recuperaTipologiaSoggetto(in, request);
					if (detailResponse != null)
					{
						if (detailResponse.getTipologiaSoggetto() != null)
						{
							request.setAttribute("tx_comune", detailResponse.getTipologiaSoggetto().getCodiceBelfiore());
							request.setAttribute("tx_chiave_soggetti", detailResponse.getTipologiaSoggetto().getChiaveTipologiaSoggetto());
							request.setAttribute("tx_cod_soggetto", detailResponse.getTipologiaSoggetto().getCodiceTipologiaSoggetti());
							request.setAttribute("tx_desc_soggetto", detailResponse.getTipologiaSoggetto().getDescrizioneSoggetto());
							request.setAttribute("ddl_flag_esenzione", detailResponse.getTipologiaSoggetto().getFlagEsenzione());
							//PG180050_001 GG - inizio
							request.setAttribute("tx_desc_aggiuntiva_soggetto", detailResponse.getTipologiaSoggetto().getDescrizioneAggiuntiva());
							request.setAttribute("tx_prog_ordinamento", detailResponse.getTipologiaSoggetto().getProgressivoOrdinamento());
							//PG180050_001 GG - fine
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
			parametri = getTipologiaSoggettoFormParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			//String esito = checkTipologiaSoggetto(parametri);
			/*
			 * Se OK effettuo il salvataggio
			 */
			//if (esito.equals("OK"))
			//{
				//Recupero del codice operatore
				UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
				if( user!=null) 
				   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
					usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
					
					TipologiaSoggetto tipoSoggetto = new TipologiaSoggetto(parametri.get("chiaveSoggetti").toString(), 
															parametri.get("codiceComune").toString(),
																parametri.get("codiceTipoSoggetto").toString(), 
																	parametri.get("descrizioneTipoSoggetto").toString(), 
																		parametri.get("flagEsenzione").toString(), null, usernameAutenticazione,
																		parametri.get("descrizioneAggiuntiva").toString(), Integer.valueOf(parametri.get("progressivoOrdinamento").toString())	//PG180050_001 GG
																		);
				
				AggiornaTipologiaSoggettoRequest in = new AggiornaTipologiaSoggettoRequest();
				in.setTipologiaSoggetto(tipoSoggetto);
				
				try {
					AggiornaTipologiaSoggettoResponse out = WSCache.impostaSoggiornoConfigServer.aggiornaTipologiaSoggetti(in, request);
					
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
			/*}
			else
				setFormMessage("var_form", esito, request);*/
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
		request.setAttribute("do_command_name","ImpostaSoggiornoTipologiaSoggettoEdit.do");
		request.setAttribute("codop",codop);
		codiceProvincia = (String)request.getAttribute("tx_provincia");
		loadListaProvince(request);
		loadBelfiore(codiceProvincia, request);
		
		//aggiornamentoCombo(request, session);
		return null;
	}
	

	private void loadListaProvince(HttpServletRequest request)
	{
		RecuperaProvinceResponse getProvinceRes = new RecuperaProvinceResponse();
		try {
			getProvinceRes = WSCache.commonsServer.recuperaProvince(request);
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
				getBelfioreRes = WSCache.commonsServer.recuperaBelfiore(new RecuperaBelfioreRequest(siglaProvincia), request);
			} catch (com.seda.payer.pgec.webservice.commons.srv.FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			request.setAttribute("listbelfiore", getBelfioreRes.getListXml());
		}
	}
	
	private HashMap<String,Object> getTipologiaSoggettoFormParameters(HttpServletRequest request, String codop)
	{
		HashMap<String,Object> param = new HashMap<String, Object>();
		//codiceProvincia = (request.getAttribute("tx_provincia") == null ? "" : (String)request.getAttribute("tx_provincia"));
		String codiceComune = (request.getAttribute("tx_comune") == null ? "" : (String)request.getAttribute("tx_comune"));
		String chiaveSoggetti = (request.getAttribute("tx_chiave_soggetti") == null ? "" : (String)request.getAttribute("tx_chiave_soggetti"));
		String codiceTipoSoggetto = (request.getAttribute("tx_cod_soggetto") == null ? "" : (String)request.getAttribute("tx_cod_soggetto"));
		String descrizioneTipoSoggetto = (request.getAttribute("tx_desc_soggetto") == null ? "" : (String)request.getAttribute("tx_desc_soggetto"));
		String flagEsenzione = (request.getAttribute("ddl_flag_esenzione") == null ? "N" : (String)request.getAttribute("ddl_flag_esenzione"));
		//PG180050_001 GG - inizio
		String descrizioneAggiuntiva = (request.getAttribute("tx_desc_aggiuntiva_soggetto") == null ? "" : (String)request.getAttribute("tx_desc_aggiuntiva_soggetto"));
		Integer progressivoOrdinamento = (request.getAttribute("tx_prog_ordinamento") == null ? 0 : Integer.valueOf((String)request.getAttribute("tx_prog_ordinamento")));
		//PG180050_001 GG - fine
		
		param.put("codiceComune", codiceComune);
		param.put("chiaveSoggetti", chiaveSoggetti);
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
	@SuppressWarnings("unused")
	private String checkTipologiaSoggetto(HashMap<String,Object> parametri )
	{
		/*
		 * Controllo che sia stato selezionato un item dalle due drop-down-list
		 */
		//DDL Provincia
		if (((String)parametri.get("tx_provincia")).equals(""))
			return Messages.SELEZIONARE_UN_ELEMENTO_LISTA.format("Provincia");
		//DDL Comune
		if (((String)parametri.get("tx_comune")).equals(""))
			return Messages.SELEZIONARE_UN_ELEMENTO_LISTA.format("Comune");
		
		return "OK";
	}
	
}
