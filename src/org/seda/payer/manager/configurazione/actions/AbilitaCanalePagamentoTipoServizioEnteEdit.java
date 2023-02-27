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

import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnte;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteDetailRequest;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteDetailResponse;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteSaveRequest;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.Response;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.StatusResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class AbilitaCanalePagamentoTipoServizioEnteEdit extends BaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "AbilitaCanalePagamentoTipoServizioEnte_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		String codiceSocieta = "";
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			codiceSocieta = (request.getAttribute("tx_societa") == null ? "" : (String)request.getAttribute("tx_societa"));
			String codiceUtente = (request.getAttribute("tx_utente") == null ? "" : (String)request.getAttribute("tx_utente"));
			String chiaveEnte = (request.getAttribute("tx_ente") == null ? "" : (String)request.getAttribute("tx_ente"));
			String chiaveCanalePagamento = (request.getAttribute("tx_canalePagamento") == null ? "" : (String)request.getAttribute("tx_canalePagamento"));
			String codiceTipologiaServizio = (request.getAttribute("tx_codiceTipologiaServizio") == null ? "" : (String)request.getAttribute("tx_codiceTipologiaServizio"));
			if (!codiceSocieta.equals("") 
					&& !codiceUtente.equals("")
					&& !chiaveEnte.equals("")
					&& !chiaveCanalePagamento.equals("")
					&& !codiceTipologiaServizio.equals(""))
			{				
				AbilitaCanalePagamentoTipoServizioEnteDetailResponse detailResponse = null;
				AbilitaCanalePagamentoTipoServizioEnteDetailRequest in = new AbilitaCanalePagamentoTipoServizioEnteDetailRequest(codiceSocieta, codiceUtente, chiaveEnte, chiaveCanalePagamento, codiceTipologiaServizio);
				try {
					detailResponse = WSCache.abilitazTributiServer.getAbilitaCanalePagamentoTipoServizioEnte(in, request);
					if (detailResponse != null)
					{
						if (detailResponse.getAbilitacanalepagamentotiposervizioente() != null)
						{
							String ddlSocietaUtenteEnte = detailResponse.getAbilitacanalepagamentotiposervizioente().getCompanyCode();
							ddlSocietaUtenteEnte = ddlSocietaUtenteEnte.concat("|").concat(detailResponse.getAbilitacanalepagamentotiposervizioente().getUserCode());
							ddlSocietaUtenteEnte = ddlSocietaUtenteEnte.concat("|").concat(detailResponse.getAbilitacanalepagamentotiposervizioente().getChiaveEnte());
							request.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
							
							request.setAttribute("ddlTipologiaServizio", detailResponse.getAbilitacanalepagamentotiposervizioente().getCodiceTipologiaServizio());
							request.setAttribute("ddlCanalePagamento", detailResponse.getAbilitacanalepagamentotiposervizioente().getChiaveCanalePagamento());
							request.setAttribute("chk_flagAttivazione", detailResponse.getAbilitacanalepagamentotiposervizioente().getFlagAttivazione().equals("Y"));
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
			parametri = getAbilitaCanalePagamentoTipoServizioEnteFormParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			String esito = checkInputAbilitaCanalePagamentoTipoServizioEnte(parametri);
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
				
				AbilitaCanalePagamentoTipoServizioEnte abilitaCanalePagamentoTipoServizioEnte = new AbilitaCanalePagamentoTipoServizioEnte(parametri.get("codiceSocieta").toString(),
						parametri.get("codiceUtente").toString(),parametri.get("chiaveEnte").toString(),parametri.get("codiceCanalePagamento").toString(),
						parametri.get("codiceTipologiaServizio").toString(),parametri.get("flagAttivazione").toString(),usernameAutenticazione);
				
				AbilitaCanalePagamentoTipoServizioEnteSaveRequest in = new AbilitaCanalePagamentoTipoServizioEnteSaveRequest();
				in.setCodOp("2");
				in.setAbilitacanalepagamentotiposervizioente(abilitaCanalePagamentoTipoServizioEnte);
				
				try {
					StatusResponse out = WSCache.abilitazTributiServer.save(in, request);
					
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
		 * Se non esco dalla pagina carico le drop-down-lists
		 * NOTA: le drop-down-lists, con "codop == edit" vengono
		 * disabilite nella jsp
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
		{
			loadSocietaUtenteEnteXml_DDL(request, session);
			//loadSocietaUtenteServizioEnteXml_DDL(request, session);
			loadTipologiaServizioXml_DDL(request, session, codiceSocieta, true);
			
			loadCanaliPagamentoAbilitazioneXml_DDL(request, session);
		}
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","AbilitaCanalePagamentoTipoServizioEnteEdit.do");
		request.setAttribute("codop",codop);
		return null;
	}

}
