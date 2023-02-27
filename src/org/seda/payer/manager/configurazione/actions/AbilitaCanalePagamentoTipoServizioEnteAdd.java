package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnte;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteSaveRequest;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.StatusResponse;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.Response;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class AbilitaCanalePagamentoTipoServizioEnteAdd extends BaseManagerAction
{
	private static String codop = "add";
	private static String ritornaViewstate = "AbilitaCanalePagamentoTipoServizioEnte_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;

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
				in.setCodOp("1");
				in.setAbilitacanalepagamentotiposervizioente(abilitaCanalePagamentoTipoServizioEnte);
				try {
					StatusResponse out = WSCache.abilitazTributiServer.save(in, request);
					if (out != null)
					{
						Response response = out.getResponse();
						if (response != null)
						{
							String insertRetCode = response.getReturncode().toString();
							if (insertRetCode.equals(ResponseReturncode.value1.getValue()))
								insertRetCode = "00";
							request.setAttribute("insertRetCode", insertRetCode);
							if(response.getReturncode().toString().equals(ResponseReturncode.value1.getValue()))
							{
								session.setAttribute("recordInserted", "OK");
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
		
		case TX_BUTTON_SOCIETA_CHANGED:
			/*
			 * Recupero i parametri dalla form
			 */
			parametri = getAbilitaCanalePagamentoTipoServizioEnteFormParameters(request,codop);
			
			if (!parametri.get("codiceSocieta").toString().equals(""))
				loadTipologiaServizioXml_DDL(request, session, parametri.get("codiceSocieta").toString(), true);
			
			//forzo il bottone add per andare allo screen corretto
			request.setAttribute(Field.TX_BUTTON_SOCIETA_CHANGED.format(), null);
			request.setAttribute("fired_button_hidden", null);
			request.setAttribute(Field.TX_BUTTON_AGGIUNGI.format(),Field.TX_BUTTON_AGGIUNGI.format());
			
			break;
		}
		
		/*
		 * Se non esco dalla pagina carico le drop-down-lists
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
		{
			loadSocietaUtenteEnteXml_DDL(request, session);
			//loadSocietaUtenteServizioEnteXml_DDL(request, session);
			if (firedButton.equals(FiredButton.TX_BUTTON_AGGIUNGI))
				loadTipologiaServizioXml_DDL(request, session, "", false);
			
			loadCanaliPagamentoAbilitazioneXml_DDL(request, session);
		}	
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","AbilitaCanalePagamentoTipoServizioEnteAdd.do");
		request.setAttribute("codop",codop);
		return null;
		
	}
	


}
