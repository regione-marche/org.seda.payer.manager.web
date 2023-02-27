package org.seda.payer.manager.adminusers.actions;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.adminusers.dati.ListaUtenzeCsvRequest;
import com.seda.payer.pgec.webservice.adminusers.dati.ListaUtenzeCsvResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.security.webservice.dati.ListaUtentiRequestType;
import com.seda.security.webservice.dati.ListaUtentiRequestTypeTipologiaUtente;

@SuppressWarnings("serial")
public class SeUsersSearchAction extends BaseManagerAction{

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

		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, session); 
		//fine LP PG200060

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
		if (firedButton.equals(FiredButton.TX_BUTTON_NUOVO)) 
			return null;
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		/*
		 * Chiamo il metodo per i settaggi relativi al profilo utente
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) setProfile(request);
		/*
		 *  Ricavo i parametri della request
		 */
		String userName = ((String)request.getAttribute("tx_username") == null ? "" : (String)request.getAttribute("tx_username"));
		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		Calendar dataFineValiditaUtenzaDa = (Calendar)request.getAttribute("dataScadenza_da");
		Calendar dataFineValiditaUtenzaA = (Calendar)request.getAttribute("dataScadenza_a");
		String utenzaAttiva = ((String)request.getAttribute("tx_utenzaattiva") == null ? "" : (String)request.getAttribute("tx_utenzaattiva"));
		String numeroAutorizzazione = ((String)request.getAttribute("tx_numautorizzazione") == null ? "" : (String)request.getAttribute("tx_numautorizzazione"));
		String emailPersonaFisicaDelegato = ((String)request.getAttribute("tx_email") == null ? "" : (String)request.getAttribute("tx_email"));
		String cognomeNomePersonaFisicaDelegato = ((String)request.getAttribute("tx_cognomenome") == null ? "" : (String)request.getAttribute("tx_cognomenome"));
		String ragioneSociale = ((String)request.getAttribute("tx_ragionesociale") == null ? "" : (String)request.getAttribute("tx_ragionesociale"));
		
		//inizio LP PG200060
		/*
		String tipoUtenza = ((String)request.getAttribute("tx_tipoutenza") == null ? "" : (String)request.getAttribute("tx_tipoutenza")); //PG170280
		
		String cfPiva = ((String) request.getAttribute("tx_cfPIva")==null ? "" : (String) request.getAttribute("tx_cfPIva")); //PG190330_001 SB
		*/
		String tipoUtenza = "";
		String cfPiva = "";
		if(!template.equalsIgnoreCase("regmarche")) {
			tipoUtenza = ((String)request.getAttribute("tx_tipoutenza") == null ? "" : (String)request.getAttribute("tx_tipoutenza")); //PG170280
			cfPiva = ((String) request.getAttribute("tx_cfPIva")==null ? "" : (String) request.getAttribute("tx_cfPIva")); //PG190330_001 SB
		}
		//fine LP PG200060
		
		/*
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		switch(firedButton)
		{
			case TX_BUTTON_CERCA: 
				try {
					/*
					 * Recupero i dati
					 */
					com.seda.security.webservice.dati.ListaUtentiRequestType in = new ListaUtentiRequestType();
					
					in.setDebugOn("");
					in.setRowsPerPage(rowsPerPage);
					in.setPageNumber(pageNumber);
					in.setOrder(order);
					in.setUserName(userName);
					in.setDataFineValiditaUtenza_da(dataFineValiditaUtenzaDa);
					in.setDataFineValiditaUtenza_a(dataFineValiditaUtenzaA);
					in.setUtenzaAttiva(utenzaAttiva);
					in.setNumeroAutorizzazione(numeroAutorizzazione);
					in.setEmailPersonaFisicaDelegato(emailPersonaFisicaDelegato);
					in.setCognomeNomePersonaFisicaDelegato(cognomeNomePersonaFisicaDelegato);
					in.setRagioneSociale(ragioneSociale);
					

					ListaUtentiRequestTypeTipologiaUtente tipologiaUtente = null;
					
					//inizio LP PG200060
					if(!template.equalsIgnoreCase("regmarche")) {
					//fine LP PG200060
					if(tipoUtenza.equals("G"))
						tipologiaUtente = ListaUtentiRequestTypeTipologiaUtente.G;
					if(tipoUtenza.equals("F"))
						tipologiaUtente = ListaUtentiRequestTypeTipologiaUtente.F;
					if(tipoUtenza.equals("D"))
						tipologiaUtente = ListaUtentiRequestTypeTipologiaUtente.D;
					//inizio LP PG200060
					}
					//fine LP PG200060
					in.setTipologiaUtente(tipologiaUtente );
					
					in.setCfPiva(cfPiva);   //PG190330_001 SB
					
					com.seda.security.webservice.dati.ListaUtentiResponseType out =
						WSCache.securityServer.listaUtenti(in, request);
					com.seda.security.webservice.dati.ResponseType response = 
						out.getResponse();
					if (response.getRetCode().equals("00"))
					{
						com.seda.security.webservice.dati.ListaUtentiResponseTypePageInfo wsPageInfo = out.getPageInfo();
						if(wsPageInfo != null)
						{
							if(wsPageInfo.getNumRows()>0)
							{
								/*
								 * Recupero la lista delle utenze bloccate
								 */
								List<String> lockedUsers = getLockedUsers();
								/*
								 * Recupero le informazioni di paginazione
								 * da passare alla jsp
								 */
								com.seda.data.spi.PageInfo pageInfo = new PageInfo();
								pageInfo.setFirstRow(wsPageInfo.getFirstRow());
								pageInfo.setLastRow(wsPageInfo.getLastRow());
								pageInfo.setNumPages(wsPageInfo.getNumPages());
								pageInfo.setNumRows(wsPageInfo.getNumRows());
								pageInfo.setPageNumber(wsPageInfo.getPageNumber());
								pageInfo.setRowsPerPage(wsPageInfo.getRowsPerPage());
								
								request.setAttribute("listaUsers", addLockedUsersColumn(out.getListXml(),lockedUsers,"SE"));
								request.setAttribute("listaUsers.pageInfo", pageInfo);
								request.setAttribute("riepilogoUtenze", out.getGroupXml());
							}
							else setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
						}
						else setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
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
			case TX_BUTTON_DOWNLOAD:
				String file="";
				try {
					ListaUtenzeCsvRequest reqCsv = new ListaUtenzeCsvRequest();
					reqCsv.setUserName(userName);
					reqCsv.setDataFineValiditaUtenza_da(dataFineValiditaUtenzaDa);
					reqCsv.setDataFineValiditaUtenza_a(dataFineValiditaUtenzaA);
					reqCsv.setUtenzaAttiva(utenzaAttiva);
					reqCsv.setNumeroAutorizzazione(numeroAutorizzazione);
					reqCsv.setEmailPersonaFisicaDelegato(emailPersonaFisicaDelegato);
					reqCsv.setCognomeNomePersonaFisicaDelegato(cognomeNomePersonaFisicaDelegato);
					reqCsv.setRagioneSociale(ragioneSociale);
					
					ListaUtenzeCsvResponse resCsv = WSCache.adminUsersServer.listaUtenzeCsv(reqCsv, request);
					if (resCsv != null && resCsv.getResponse() != null && resCsv.getResponse().getRetCode().equals("00"))
					{
						//WSCache.logWriter.logInfo("Lunghezza stringa csv download: " + resCsv.getStringaCsv().length() + ". Lunghezza in Bytes: " + resCsv.getStringaCsv().getBytes().length);
						file = resCsv.getStringaCsv();
						//aggiustamento carattere \r perso nel webservice 
						file = file.replaceAll("\n", "\r\n");
						Calendar cal = Calendar.getInstance();
						String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
						request.setAttribute("filename",timestamp + "_ListaUtenze.csv");
						request.setAttribute("fileCsv", file);
					}
					else
					{
						setFormMessage("search_form", "Si è verificato un errore durante l'esportazione dei dati" , request);
					}
				} catch (FaultType e1) {
					setFormMessage("search_form", e1.getMessage() , request);
					e1.printStackTrace();
				} catch (RemoteException e1) {
					setFormMessage("search_form", e1.getMessage() , request);
					e1.printStackTrace();
				}
				break;
			case TX_BUTTON_RESET:
				resetParametri(request);
				request.setAttribute("dataScadenza_da", null);
				request.setAttribute("dataScadenza_a", null);
				setProfile(request);
				break;
				
			case TX_BUTTON_NULL: 
				break;
				
			case TX_BUTTON_INDIETRO:
				/*
				 * Resetto i campi di input nel caso in cui
				 * la richiesta (POST) giunga dalla form 
				 * di inserimento o di modifica
				 */
				request.setAttribute("tx_username", null);
				request.setAttribute("tx_username_hidden", null);
				request.setAttribute("tx_utenzaattiva", "");
				//inizio LP PG200060
				if(!template.equalsIgnoreCase("regmarche")) {
				//fine LP PG200060
				request.setAttribute("tx_tipoutenza", "");
				//inizio LP PG200060
				}
				//fine LP PG200060
				break;
		}
		return null;
	}


}
