package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaConfigurazioneImpSoggToHostRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaConfigurazioneImpSoggToHostResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaConfigurazioneImpostaSoggiornoRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaConfigurazioneImpostaSoggiornoResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaEntiImpostaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaEntiImpostaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.VerificaAnagraficaContribuentiRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.VerificaAnagraficaContribuentiResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.ConfigurazioneImpostaSoggiorno;
import com.seda.payer.impostasoggiorno.webservice.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaProvinceResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoConfigurazioneImpostaEdit extends BaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "impostaSoggiornoConfigurazioneImposta_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
	private String codiceProvincia = "";
	private String codBelf = "";

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		String codiceComune = "";
		//String codiceEnte = "";
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			codiceComune = (request.getAttribute("tx_comune") == null ? "" : (String)request.getAttribute("tx_comune"));
			//codiceEnte = (request.getAttribute("tx_ente") == null ? "" : (String)request.getAttribute("tx_ente"));
			//String[] chiave = codiceEnte.split("|");
			if (!codiceComune.equals(""))
			{				
				RecuperaConfigurazioneImpostaSoggiornoResponse detailResponse = null;
				
				RecuperaConfigurazioneImpostaSoggiornoRequest in = new RecuperaConfigurazioneImpostaSoggiornoRequest();
				try {
					in.setCodiceBelfiore(codiceComune);
					detailResponse = WSCache.impostaSoggiornoConfigServer.recuperaConfigurazioneImpostaSoggiorno(in, request);
					if (detailResponse != null)
					{
						if (detailResponse.getConfigurazioneImpostaSoggiorno() != null)
						{
							request.setAttribute("tx_comune", detailResponse.getConfigurazioneImpostaSoggiorno().getCodiceBelfiore());
							request.setAttribute("tx_ente", detailResponse.getConfigurazioneImpostaSoggiorno().getCodiceSocieta()
													+"|"+detailResponse.getConfigurazioneImpostaSoggiorno().getCodiceUtente()
													+"|"+detailResponse.getConfigurazioneImpostaSoggiorno().getChiaveEnte());
							request.setAttribute("tx_cod_ges", detailResponse.getConfigurazioneImpostaSoggiorno().getCodiceEnteGestionaleEntrate());
							request.setAttribute("tx_is_ges", detailResponse.getConfigurazioneImpostaSoggiorno().getImpostaServizioGestionaleEntrate());
							request.setAttribute("tx_trib_ges", detailResponse.getConfigurazioneImpostaSoggiorno().getCodiceTributoGestionaleEntrate());
							request.setAttribute("tx_cod_ges_sel", detailResponse.getConfigurazioneImpostaSoggiorno().getCodiceEnteGestionaleEntrate());
							request.setAttribute("tx_is_ges_sel", detailResponse.getConfigurazioneImpostaSoggiorno().getImpostaServizioGestionaleEntrate());
							request.setAttribute("tx_anno_doc", detailResponse.getConfigurazioneImpostaSoggiorno().getAnnoDocumentoGestionaleEntrate());

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
				
				VerificaAnagraficaContribuentiRequest vreq = new VerificaAnagraficaContribuentiRequest();
				vreq.setCodCiseGes(parametri.get("codISSel").toString());
				vreq.setCodEnteGes(parametri.get("codEntrataSel").toString());
				VerificaAnagraficaContribuentiResponse res = new VerificaAnagraficaContribuentiResponse();
				try {
				res = WSCache.impostaSoggiornoConfigServer.verificaAnagraficaContribuenti(vreq, request);
				if( res.getRisultato().getRetCode().equals(ResponseTypeRetCode.value1)){
					
					//String[] chiaveE = parametri.get("codEnte").toString().split("|");
					
					String[] chiaveE= new String[3]; //= (parametri.get("codEnte").toString()).split("|");
					
					StringTokenizer st = new StringTokenizer(parametri.get("codEnte").toString(),"|");
				    int i = 0; 
					while (st.hasMoreTokens()) {
						chiaveE[i] = st.nextToken();
						//System.out.println(chiaveE[i]);
						i++;
				     }
					
					ConfigurazioneImpostaSoggiorno configurazione = new ConfigurazioneImpostaSoggiorno(
																		parametri.get("codiceComune").toString(),
																			chiaveE[0],chiaveE[1],chiaveE[2],
																				parametri.get("codEntrata").toString().trim(),
																					parametri.get("codIS").toString().trim(),
																						parametri.get("codTributo").toString().trim(),
																							null,
																							usernameAutenticazione,"",
																							parametri.get("annoDoc").toString().trim());
				
				AggiornaConfigurazioneImpSoggToHostRequest in = new AggiornaConfigurazioneImpSoggToHostRequest();
				
				in.setConfigurazioneImpostaSoggiorno(configurazione);
				in.setCodUtente(in.getConfigurazioneImpostaSoggiorno().getCodiceUtente());
				

					AggiornaConfigurazioneImpSoggToHostResponse out = WSCache.impostaSoggiornoConfigServer.aggiornaConfigurazioneImposta(in, request);
					
					if (out != null)
					{
						if(out.equals("00")){
							session.setAttribute("recordUpdated", out.getRetMessage());
							request.setAttribute("vista",ritornaViewstate);
						}else setFormMessage("var_form", out.getRetMessage(), request);

					}else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				}else{
					setFormMessage("var_form", "Errore: " + res.getRisultato().getRetMessage(), request);
				}

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
		request.setAttribute("do_command_name","ImpostaSoggiornoConfigurazioneImpostaEdit.do");
		request.setAttribute("codop",codop);
		codiceProvincia = (String)request.getAttribute("tx_provincia");
		codBelf = (request.getAttribute("tx_comune") == null ? "" : (String)request.getAttribute("tx_comune"));
		loadListaProvince(request);
		loadBelfiore(codiceProvincia, request);
		loadEnti(codBelf, request);
		
		//aggiornamentoCombo(request, session);
		return null;
	}
	
	private void loadEnti(String codBelf, HttpServletRequest request) {
		if (codBelf != null && codBelf.length() > 0)
		{
			RecuperaListaEntiImpostaResponse getEntiRes = new RecuperaListaEntiImpostaResponse();
			RecuperaListaEntiImpostaRequest in = new RecuperaListaEntiImpostaRequest();
			in.setCodBelf(codBelf);
			try {
				getEntiRes = WSCache.impostaSoggiornoConfigServer.recuperaListaEntiImposta(in, request);
			} catch (com.seda.payer.impostasoggiorno.webservice.srv.FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			request.setAttribute("listente", getEntiRes.getListXml());
		}
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
		String codEnte = (request.getAttribute("tx_ente") == null ? "" : (String)request.getAttribute("tx_ente"));
		String codIS = (request.getAttribute("tx_is_ges") == null ? "" : (String)request.getAttribute("tx_is_ges"));
		String codEntrata = (request.getAttribute("tx_cod_ges") == null ? "" : (String)request.getAttribute("tx_cod_ges"));
		String codISSel = (request.getAttribute("tx_is_ges_sel") == null ? "" : (String)request.getAttribute("tx_is_ges_sel"));
		String codEntrataSel = (request.getAttribute("tx_cod_ges_sel") == null ? "" : (String)request.getAttribute("tx_cod_ges_sel"));
		
		String codTributo = (request.getAttribute("tx_trib_ges") == null ? "" : (String)request.getAttribute("tx_trib_ges"));
		
		//PG140470 - Trentrisc
		String annoDoc = (request.getAttribute("tx_anno_doc") == null ? "" : (String)request.getAttribute("tx_anno_doc"));

		param.put("codiceComune", codiceComune);
		param.put("codEnte", codEnte);
		param.put("codIS", codIS);
		param.put("codEntrata", codEntrata);
		param.put("codTributo", codTributo);
		param.put("codISSel", codISSel);
		param.put("codEntrataSel", codEntrataSel);
		param.put("annoDoc", annoDoc);

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
