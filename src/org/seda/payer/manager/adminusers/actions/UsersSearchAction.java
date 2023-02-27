package org.seda.payer.manager.adminusers.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.GruppoAgenziaPageList;
import com.seda.payer.pgec.webservice.adminusers.dati.ListaUtentiRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.ListaUtentiResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.ListaUtentiResponseTypePageInfo;
import com.seda.payer.pgec.webservice.adminusers.dati.ResponseType;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class UsersSearchAction extends BaseAdminusersAction{

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Resetto l'eventuale messaggio rimasto nel viewstate
		 */
		resetFormMessage(request);
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		//System.out.println("UsersSearchAction ".concat(firedButton.toString()));

		/*
		 * Azzero il campo hidden "fired_button_hidden" per evitare
		 * che possa essere riproposto nel caso in cui interviene 
		 * il "validator"
		 */
		request.setAttribute("fired_button_hidden", "");
		/*
		 * Se è presente in sessione uno degli attributi "addUserRetMessage" 
		 * o "editUserRetMessage" vuol dire che è stato inserito/modificato
		 * correttamente un utente e devo visualizzare il messaggio
		 */
		String addUserRetMessage = (String) session.getAttribute("addUserRetMessage");
		if (addUserRetMessage != null) 
		{
			session.removeAttribute("addUserRetMessage");
			setFormMessage("search_form", addUserRetMessage, request);
		}
		String editUserRetMessage = (String) session.getAttribute("editUserRetMessage");
		if (editUserRetMessage != null) 
		{
			session.removeAttribute("editUserRetMessage");
			setFormMessage("search_form", editUserRetMessage, request);
		}
		/*
		 * FLOW - Se la richiesta è relativa all'inserimento di un nuovo utente esco
		 */
		if (firedButton.equals(FiredButton.TX_BUTTON_NUOVO)) return null;
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		/*
		 * Chiamo il metodo per i settaggi relativi al profilo utente
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) setProfile(request);
		/*
		 * Carico la lista delle società
		 */
		loadSocietaXml_DDL(request);
		
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, session); 
		if(!template.equalsIgnoreCase("regmarche")) {
		//fine LP PG200060
		//inizio - RE180181 SB
		/*
		 * Carico la lista dei Gruppi Agenzia
		 */
		String gruppiAgenziaLstXml="";
		//Se la lista non è presente in sessione non la ricarico
		if(session.getAttribute("listaGruppiAgenzia")==null || session.getAttribute("listaGruppiAgenzia").equals("")){
			GruppoAgenziaPageList gruppiAgenziaLst = getGruppoAgenziaListDDL(request);
			if(gruppiAgenziaLst!= null && gruppiAgenziaLst.getRetCode().equals("00")){
				gruppiAgenziaLstXml=gruppiAgenziaLst.getGruppoAgenziaListXml();
				request.setAttribute("listaGruppiAgenzia", gruppiAgenziaLstXml);
				session.setAttribute("listaGruppiAgenzia", gruppiAgenziaLstXml);
			}
		}else{
			request.setAttribute("listaGruppiAgenzia", session.getAttribute("listaGruppiAgenzia"));
		}
		
		//fine - RE180181 SB
		//inizio LP PG200060
		}
		//fine LP PG200060

		
		/*
		 * Carico la lista delle applicazioni nell'attributo "listaApplicazioniPayer"
		 * Vencono caricate le applicazioni "Attive" e "Protette" e, come descrizione, viene
		 * utilizzata quella di "MAF-APPLICATION.XML"
		 */
		//loadPayerApplicationsXml_DDL(request, true, true, true);
		loadPayerApplicationsXml_DDL_properties(request, session, true, false);
		/*
		 *  Ricavo i parametri della request
		 */
		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");

		String userName = ((String)request.getAttribute("tx_username") == null ? "" : (String)request.getAttribute("tx_username"));
		String userProfile = ((String)request.getAttribute("tx_userprofile") == null ? "" : (String)request.getAttribute("tx_userprofile"));
		String applicazione = ((String)request.getAttribute("tx_applicazione") == null ? "" : (String)request.getAttribute("tx_applicazione"));
		String siglaProvincia = ((String)request.getAttribute("tx_provincia") == null) ? "" : (String)request.getAttribute("tx_provincia");
		String utenzaAttiva = ((String)request.getAttribute("tx_utenzaattiva") == null ? "" : (String)request.getAttribute("tx_utenzaattiva"));
		//inizio LP PG200060
		//String pertinenza = ((String)request.getAttribute("tx_pertinenza") == null ? "" : (String)request.getAttribute("tx_pertinenza"));
		//String codiceGruppoAgenzia = ((String)request.getAttribute("tx_gruppo_agenzia") == null ? "" : (String)request.getAttribute("tx_gruppo_agenzia"));
		String pertinenza = "";
		String codiceGruppoAgenzia = "";
		if(!template.equalsIgnoreCase("regmarche")) {
		//fine LP PG200060
			pertinenza = ((String)request.getAttribute("tx_pertinenza") == null ? "" : (String)request.getAttribute("tx_pertinenza"));
			codiceGruppoAgenzia = ((String)request.getAttribute("tx_gruppo_agenzia") == null ? "" : (String)request.getAttribute("tx_gruppo_agenzia"));
		//inizio LP PG200060
		}
		//fine LP PG200060
		
		ResponseType response = null;
		ListaUtentiResponseTypePageInfo wsPageInfo = null;
		/*
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		switch(firedButton)
		{
			case TX_BUTTON_CERCA: 
				try {
					/*
					 * Ricarico le DDL per Provincia e UtentiEnti
					 */
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
					
					//ripulisco eventuali risultati precedenti
					request.setAttribute("listaUsers", "");
					/*
					 * Recupero i dati
					 */
					ListaUtentiRequestType in = new ListaUtentiRequestType(
																userName,
																"Y",
																userProfile,
																getParamCodiceSocieta(), 
																getParamCodiceUtente(), 
																siglaProvincia, 
																getParamCodiceEnte(), 
																applicazione,
																utenzaAttiva,
																rowsPerPage, 
																pageNumber,
																order,
																pertinenza,
																codiceGruppoAgenzia);
			           
					ListaUtentiResponseType out = WSCache.adminUsersServer.listaUtenti(in, request);
					response = out.getResponse();
					if (response.getRetCode().equals("00"))
					{
						wsPageInfo = out.getPageInfo();
						if(wsPageInfo != null)
						{
							if(wsPageInfo.getNumRows()>0)
							{
								/*
								 * Recupero le informazioni di paginazione da passare alla jsp
								 */
								com.seda.data.spi.PageInfo pageInfo = new PageInfo();
								pageInfo.setFirstRow(wsPageInfo.getFirstRow());
								pageInfo.setLastRow(wsPageInfo.getLastRow());
								pageInfo.setNumPages(wsPageInfo.getNumPages());
								pageInfo.setNumRows(wsPageInfo.getNumRows());
								pageInfo.setPageNumber(wsPageInfo.getPageNumber());
								pageInfo.setRowsPerPage(wsPageInfo.getRowsPerPage());
								
								request.setAttribute("listaUsers", out.getListaUtentiXml()); 
								request.setAttribute("listaUsers.pageInfo", pageInfo);
							}
							else
								setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
						}
						else
							setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
					}
					else
					{
						String messaggio = response.getRetMessage(); 
						setFormMessage("search_form", (messaggio == null || messaggio.equals("")) ? "Errore generico" : messaggio, request);
					}
			} catch (FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}

				break;
				
			case TX_BUTTON_RESET:
				resetParametri(request);
				setProfile(request);
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				break;
				
			case TX_BUTTON_SOCIETA_CHANGED:
				String societa = getParamCodiceSocieta();
				loadProvinciaXml_DDL(request, session, societa,true);
				LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvincia), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : getParamCodiceUtente()), true);
				request.setAttribute("tx_provincia","");
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : getParamCodiceUtente()), true);
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_NULL: 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				break;
				
			case TX_BUTTON_INDIETRO:
				/*
				 * Resetto i campi di input nel caso in cui la richiesta (POST) giunga dalla form 
				 * di inserimento o di modifica
				 */
				request.setAttribute("tx_societa", "");
				request.setAttribute("tx_provincia", "");
				request.setAttribute("tx_UtenteEnte", "");
				request.setAttribute("tx_username", null);
				request.setAttribute("tx_username_hidden", null);
				request.setAttribute("tx_userprofile","");
				request.setAttribute("tx_applicazione", "");
				request.setAttribute("tx_utenzaattiva", "");
				break;
		}
		
		//resetto la lista degli user name per la form di inserimento (salvati in sessione per non doverli ricaricare ogni volta)
		session.setAttribute("listaUserNameJson", null);
		return null;
	}
	
}