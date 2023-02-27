package org.seda.payer.manager.configurazione.actions;

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

import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.AbilitaSistemiEsterniSecureSite;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.AbilitaSistemiEsterniSecureSiteDetailRequest;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.AbilitaSistemiEsterniSecureSiteDetailResponse;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.AbilitaSistemiEsterniSecureSiteSaveRequest;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.Response;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.StatusResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class AbilitaSistemiEsterniSecureSiteEdit extends BaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "AbilitaSistemiEsterniSecureSite_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			String urlAccesso = (request.getAttribute("tx_url") == null ? "" : (String)request.getAttribute("tx_url"));
			String idServizio= (request.getAttribute("tx_idServizio") == null ? "" : (String)request.getAttribute("tx_idServizio"));
			if (!urlAccesso.equals("")) 
			{	
				AbilitaSistemiEsterniSecureSiteDetailResponse detailResponse = null;
				AbilitaSistemiEsterniSecureSiteDetailRequest in = new AbilitaSistemiEsterniSecureSiteDetailRequest(urlAccesso, idServizio);
											
				try {
					detailResponse = WSCache.abilitaSistemiEsterniSecureSiteServer.getAbilitaSistemiEsterniSecureSite(in, request);
					if (detailResponse != null)
					{
						if (detailResponse.getAbilitasistemiesternisecuresite() != null)
						{
							request.setAttribute("urlAccesso", detailResponse.getAbilitasistemiesternisecuresite().getUrlAccesso());
							request.setAttribute("descrizione", detailResponse.getAbilitasistemiesternisecuresite().getDescrizione());
							request.setAttribute("idServizio", detailResponse.getAbilitasistemiesternisecuresite().getIdServizio());
							request.setAttribute("pathFileImmagine", detailResponse.getAbilitasistemiesternisecuresite().getPathFileImmagine());
							request.setAttribute("codiceTipologiaServizio", detailResponse.getAbilitasistemiesternisecuresite().getCodiceTipologiaServizio());
							request.setAttribute("chk_flagAttivazione", detailResponse.getAbilitasistemiesternisecuresite().getFlagAttivazione().equals("Y"));
							request.setAttribute("chk_flagRedirect", detailResponse.getAbilitasistemiesternisecuresite().getFlagRedirect().equals("Y"));
							
							request.setAttribute("chk_flagCalcoloCosti", detailResponse.getAbilitasistemiesternisecuresite().getFlagCalcoloCosti().equals("Y"));
							request.setAttribute("chk_flagInvioNotifica", detailResponse.getAbilitasistemiesternisecuresite().getFlagInvioNotificaPayer().equals("Y"));
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
			parametri = getAbilitaSistemiEsterniSecureSiteFormParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			String esito = checkInputAbilitaSistemiEsterniSecureSite(parametri);
			/*
			 * Se OK effttuo il salvataggio
			 */
			if (esito.equals("OK"))
			{
				//Recupero del codice operatore
				UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
				if( user!=null) 
				   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
				usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
				
				AbilitaSistemiEsterniSecureSite abilitaSistemiEsterniSecureSite = new AbilitaSistemiEsterniSecureSite (parametri.get("urlAccesso").toString(), 
						parametri.get("descrizione").toString(), parametri.get("pathFileImmagine").toString(), parametri.get("flagAttivazione").toString(),  
						parametri.get("flagRedirect").toString(), parametri.get("idServizio").toString(), parametri.get("codiceTipologiaServizio").toString(),
						parametri.get("flagCalcoloCosti").toString(),parametri.get("flagInvioNotifica").toString(),0, 
						usernameAutenticazione);
				
				AbilitaSistemiEsterniSecureSiteSaveRequest saveRequest = new AbilitaSistemiEsterniSecureSiteSaveRequest(abilitaSistemiEsterniSecureSite,"2");
									
				try {
					StatusResponse out = WSCache.abilitaSistemiEsterniSecureSiteServer.save(saveRequest, request);
					if (out != null)
					{
						Response response = out.getResponse();
						if (response != null)
						{
							String updateRetCode = response.getReturncode().toString();
							if (updateRetCode.equals(ResponseReturncode.value1.getValue()))
								updateRetCode = "00";
							request.setAttribute("updateRetCode", updateRetCode);
							if(response.getReturncode().toString().equals(ResponseReturncode.value1.getValue()))
							{
								session.setAttribute("recordUpdated", "OK");
								request.setAttribute("vista",ritornaViewstate);
							}
							else setFormMessage("var_form", response.getReturnmessage(), request);
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
			else
				setFormMessage("var_form", esito, request);
			break;
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("vista",ritornaViewstate);
			break;
			

		}
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","AbilitaSistemiEsterniSecureSiteEdit.do");
		request.setAttribute("codop",codop);
		return null;
	}

	/**
	 * Questa routine recupera i parametri dal form di inserimento o modifica
	 * per la tabella PYSECTB - Effettua inoltre il set delle 2 checkbox 
	 * 
	 * Nel caso della modifica i valori di URL e ID Servizio Abilitato
	 * non vengono recuperati dal form ma dai campi hidden che sono stati settati
	 * all'atto del caricamento del record da modificare
	 * 
	 * @param request
	 * @param codop - "add" o "edit"
	 * @return
	 */
	protected HashMap<String,Object> getAbilitaSistemiEsterniSecureSiteFormParameters(HttpServletRequest request, String codop)
	{
		HashMap<String,Object> param = new HashMap<String, Object>();
		String urlAccesso = "";
		String idServizio = "";
		
		/*
		 * INSERIMENTO
		 */
		if (codop.equals("add"))
		{
			/*
			 * Prendo i parametri dal form
			 */
			urlAccesso = (String)request.getAttribute("urlAccesso");
			idServizio = (String)request.getAttribute("idServizio");
		}
		/*
		 * MODIFICA
		 */
		else
		{
			/*
			 * Prendo i valori dai campi hidden che sono stati valorizzati dal WS
			 */
			urlAccesso = (request.getAttribute("tx_url") == null ? "" : (String)request.getAttribute("tx_url"));
			idServizio = (request.getAttribute("tx_idServizio") == null ? "" : (String)request.getAttribute("tx_idServizio"));
			request.setAttribute("urlAccesso", urlAccesso);
			request.setAttribute("idServizio", idServizio);
		}
		String flagAttivazione = (request.getAttribute("flagAttivazione") == null ? "N" : "Y");
		String flagRedirect = (request.getAttribute("flagRedirect") == null ? "N" : "Y");
		
		String flagCalcoloCosti = (request.getAttribute("flagCalcoloCosti") == null ? "N" : "Y");
		String flagInvioNotifica = (request.getAttribute("flagInvioNotifica") == null ? "N" : "Y");
		
		String descrizione = (request.getAttribute("descrizione") == null ? "" : (String)request.getAttribute("descrizione"));
		String pathFileImmagine = (request.getAttribute("pathFileImmagine") == null ? "" : (String)request.getAttribute("pathFileImmagine"));
		String codiceTipologiaServizio = (request.getAttribute("codiceTipologiaServizio") == null ? "" : (String)request.getAttribute("codiceTipologiaServizio"));
		param.put("urlAccesso", urlAccesso);
		param.put("descrizione", descrizione);
		param.put("pathFileImmagine", pathFileImmagine);
		param.put("flagAttivazione", flagAttivazione);
		param.put("flagRedirect", flagRedirect);
		param.put("flagCalcoloCosti", flagCalcoloCosti);
		param.put("flagInvioNotifica", flagInvioNotifica);
		
		param.put("idServizio", idServizio);
		param.put("codiceTipologiaServizio", codiceTipologiaServizio);
		/*
		 * Setto le due check-box di attivo e redirect
		 */
		request.setAttribute("chk_flagAttivazione", flagAttivazione.equals("Y"));
		request.setAttribute("chk_flagRedirect",  flagRedirect.equals("Y"));
		request.setAttribute("chk_flagCalcoloCosti", flagCalcoloCosti.equals("Y"));
		request.setAttribute("chk_flagInvioNotifica",  flagInvioNotifica.equals("Y"));
		
		return param;
	}

}
