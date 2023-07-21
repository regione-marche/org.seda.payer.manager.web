package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;

import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioGetRecordResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioGetRecordRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioSaveRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioSaveResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioType;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ConfRendUtenteServizioEdit extends BaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "ConfRendUtenteServizio_search";
	private HashMap<String,Object> parametri = null;

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
			String codiceSocieta = (request.getAttribute("tx_societa") == null ? "" : (String)request.getAttribute("tx_societa"));
			String codiceUtente = (request.getAttribute("tx_utente") == null ? "" : (String)request.getAttribute("tx_utente"));
			String codiceTipologiaServizio = (request.getAttribute("tx_codiceTipologiaServizio") == null ? "" : (String)request.getAttribute("tx_codiceTipologiaServizio"));
			if (!codiceSocieta.equals("") 
					&& !codiceUtente.equals("")
					&& !codiceTipologiaServizio.equals(""))
			{
				ConfRendUtenteServizioGetRecordRequestType in = new ConfRendUtenteServizioGetRecordRequestType();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceUtente(codiceUtente);
				in.setTipologiaServizio(codiceTipologiaServizio);
				try {
					ConfRendUtenteServizioGetRecordResponseType out = WSCache.confRendUtenteServizioServer.getRecord(in, request);
					if (out != null)
					{
						ResponseType response = out.getResponse();
						if (response != null)
						{
							if(response.getRetCode().equals("00"))
							{
								setConfRendUtenteServizioFormFromWS(out.getBean(),request);
							}
							else setFormMessage("var_form", response.getRetMessage(), request);
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
			
		//aggiunta PG110260 
		case TX_BUTTON_AGGIORNA:
			parametri = getConfRendUtenteServizioFormParameters(request,codop);		
			String esitoRicarica = checkInputConfRend(parametri, request);
			if (!esitoRicarica.equals("OK"))
			{
				setFormMessage("var_form", esitoRicarica, request);
			} 
			break;
		//fine aggiunta PG110260	
			
		case TX_BUTTON_EDIT:
			/*
			 * Recupero i parametri dalla form
			 */
			parametri = getConfRendUtenteServizioFormParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			String esito = checkInputConfRend(parametri, request);
			/*
			 * Se OK effttuo il salvataggio
			 */
			if (esito.equals("OK"))
			{
				
				ConfRendUtenteServizioSaveRequestType in = new ConfRendUtenteServizioSaveRequestType();
				in.setCodOp("2");
				//inizio LP PG200060	
				//in.setBean(getConfRendUtenteServizioBean(parametri,session));
				String template = getTemplateCurrentApplication(request, session);
				in.setBean(getConfRendUtenteServizioBean(parametri, session, template));
				//fine LP PG200060	
				try {
					ConfRendUtenteServizioSaveResponseType out = WSCache.confRendUtenteServizioServer.save(in, request);
					if (out != null)
					{
						ResponseType response = out.getResponse();
						if (response != null)
						{
							request.setAttribute("updateRetCode", response.getRetCode());
							if(response.getRetCode().equals("00"))
							{
								session.setAttribute("recordUpdated", "OK");
								request.setAttribute("vista",ritornaViewstate);
							}
							else setFormMessage("var_form", response.getRetMessage(), request);
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
		 * Se non esco dalla pagina carico la drop-down-list
		 * NOTA: la drop-down-list, con "codop == edit" viene
		 * disabilita nella jsp
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
			loadSocietaUtenteServizioXml_DDL(request, session);
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","ConfRendUtenteServizioEdit.do");
		request.setAttribute("codop",codop);
		return null;
	}

	protected void setConfRendUtenteServizioFormFromWS(ConfRendUtenteServizioType bean,HttpServletRequest request)
	{
		if (bean == null) return;
		String ddlSocietaUtenteServizio = bean.getCodiceSocieta().concat("|").concat(bean.getCodiceUtente()).concat("|").concat(bean.getTipologiaServizio());
		request.setAttribute("ddlSocietaUtenteServizio", ddlSocietaUtenteServizio);
		request.setAttribute("chk_invioEmail",  bean.getFlagAbilitazioneInvioEmail().equals("Y"));
		request.setAttribute("chk_invioFtp",  bean.getFlagAbilitazioneInvioFtp().equals("Y"));
		request.setAttribute("chk_invioWebService",  bean.getFlagAbilitazioneInvioWebService().equals("Y"));
		
		request.setAttribute("chk_flagTrcComandiPolizia",  bean.getFlagTracciatoComandiPolizia().equals("Y"));	//PG200280
		request.setAttribute("rendquattrocento", bean.gettracciatoQuattrocento().equals("Y"));
		
		//aggiunta PG110260
		request.setAttribute("chk_rendicontazioneSeda", bean.getFlagTipoRendicontazione().equals("Y")); 
		if ( bean.getFlagTipoRendicontazione().equals("Y")) {
			request.setAttribute("chk_invioEmail",  false ); 
			request.setAttribute("disabled_invioEmail",  true ); 
			
			request.setAttribute("chk_invioFtp",  true ); 	//anomalia PG110260
			request.setAttribute("disabled_invioFtp",  true ); //anomalia PG110260
			
			request.setAttribute("chk_invioWebService",  false ); 
			request.setAttribute("disabled_invioWebService",  true );
			
			request.setAttribute("formatoFileRend", "TXT" );
			request.setAttribute("disabled_formatoFile",  true );

			request.setAttribute("disabled_rendquattrocento",  true );

		} else{
			request.setAttribute("disabled_invioEmail",  false );  
			
			request.setAttribute("disabled_invioFtp",  false ); //anomalia PG110260
			
			request.setAttribute("disabled_invioWebService",  false );
			
			request.setAttribute("disabled_formatoFile",  false );
			
			request.setAttribute("disabled_rendquattrocento",  false );
			
			request.setAttribute("formatoFileRend", "TXT" );
		}
		//Fine aggiunta PG110260
		request.setAttribute("emailDestinatario", bean.getEmailDestinatario());
		request.setAttribute("emailCCN", bean.getEmailConoscenzaNascosta());
		request.setAttribute("emailMittente", bean.getEmailMittente());
		request.setAttribute("descrizioneMittente", bean.getDescrizioneMittente());
		request.setAttribute("emailAttachMaxSizeKb", String.valueOf(bean.getMaxAttachSizeKb()));
		request.setAttribute("serverFtp", bean.getServerFtp());
		request.setAttribute("utenteFtp", bean.getUtenteFtp());
		request.setAttribute("passwordFtp", bean.getPasswordFtp());
		request.setAttribute("directoryFtp", bean.getDirRemotaServerFtp());
		request.setAttribute("chk_senzaCarico", bean.getFlagSenzaCarico().equals("Y"));
		request.setAttribute("ente", bean.getEnte());
		request.setAttribute("servizio", bean.getImpServ());
		request.setAttribute("chk_codContrib", bean.getFlagCodContrib().equals("Y"));
		request.setAttribute("codTributo", bean.getCodTributo());	//PG180260 GG 04122018
		if (bean.getFlagSenzaCarico().equals("Y")) {
			request.setAttribute("disableCarico",  false );
		} else {
			request.setAttribute("disableCarico",  true );
		}
		
		request.setAttribute("formatoFileRend", bean.getFormatoFileRend());
		request.setAttribute("urlWebService", bean.getUrlWebServiceEnte());
		request.setAttribute("utenteWebService", bean.getUtenteWebServiceEnte());
		request.setAttribute("passwordWebService", bean.getPasswordWebServiceEnte());		
	}

}
