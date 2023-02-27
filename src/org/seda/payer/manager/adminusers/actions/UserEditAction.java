package org.seda.payer.manager.adminusers.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.GruppoAgenziaPageList;
import com.seda.payer.pgec.webservice.adminusers.dati.ResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.SelezionaUtenteRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.SelezionaUtenteResponseType;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class UserEditAction extends BaseAdminusersAction{

	private static String codop = "edit";

	SelezionaUtenteRequestType selezionaReq = null;
	SelezionaUtenteResponseType selezionaRes = null;
	ResponseType aggiornaResponse = null;
	ResponseType selezionaResponse = null;
	
	public Object service(HttpServletRequest request) throws ActionException {
		try {
			String selezionaRetCode = null;
			String selezionaRetMessage = null;
			ProfiloUtente profiloUtente = ProfiloUtente.AMNULL;
			
			HttpSession session = request.getSession();
			/*
			 * Individuo la richiesta dell'utente
			 */
			FiredButton firedButton = getFiredButton(request);
			/*
			 * Azzero il campo hidden "fired_button_hidden" per evitare
			 * che possa essere riproposto nel caso in cui interviene 
			 * il "validator"
			 */
			request.setAttribute("fired_button_hidden", "");
			/*
			 * FLOW - Se la richiesta è di tornare al form di ricerca esco
			 * dopo aver cancellato i dati, eventualmente, messi in sessione
			 */
			if (firedButton.equals(FiredButton.TX_BUTTON_CERCA))
			{
				request.setAttribute("vista","adminusers_search");
				session.removeAttribute("selezionaRes");
				return null;
			}
			if (!firedButton.equals(FiredButton.TX_BUTTON_NULL) 
					&& !firedButton.equals(FiredButton.TX_BUTTON_RESET)
					&& !firedButton.equals(FiredButton.TX_BUTTON_STEP1))
			{
				/*
				 *  Ricavo i parametri della request
				 */
				getForm1Parameters(request,codop);
				/*
				 *  Setto i flag
				 */
				setForm1Flags(request);
				/*
				 * Ricavo il profilo dell'utente
				 */
				profiloUtente = getProfiloUtente(userProfile);
				
				/*
				 * Abilito/disabilito le DDL "Società", "Utente" ed "Ente" 
				 * in funzione del profilo selezionato
				 */
				setDDL(request,profiloUtente);
			}
			
			//inizio LP PG200060
			String template = getTemplateCurrentApplication(request, session); 
			if(!template.equalsIgnoreCase("regmarche")) {
			//fine LP PG200060
			//inizio - RE180181 SB
			
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
			 * Compio le azioni corrispondenti alla richiesta dell'utente
			 */
			switch(firedButton)
			{
				case TX_BUTTON_PROFILO_UTENTE_CHANGED:
					request.setAttribute("tx_button_profilo_utente_changed", "XX");
					//request.setAttribute("tx_societa", "");
					codiceSocieta = "";
					break;

				case TX_BUTTON_SOCIETA_CHANGED:
					request.setAttribute("tx_button_societa_changed", "XX");
					if (!codiceSocieta.equals(""))
					{
						//Carico la lista degli Utenti
						loadDDLUtente(request, session, codiceSocieta, "", true);
					}
					break;

				case TX_BUTTON_UTENTE_CHANGED:
					request.setAttribute("tx_button_utente_changed", "XX");
					loadDDLUtente(request, session, codiceSocieta, "", false);
					if (!codiceUtente.equals(""))
						loadDDLEnte(request, session, codiceSocieta, "", codiceUtente, true);
					break;
					
				case TX_BUTTON_ENTE_CHANGED:
					request.setAttribute("tx_button_ente_changed", "XX");
					loadDDLUtente(request, session, codiceSocieta, "", false);
					loadDDLEnte(request, session, codiceSocieta, "", codiceUtente, false);
					if (!codiceEnte.equals(""))
						// Carico la DDL delle "Tipologie Servizio"
						loadTipologiaServizioXml_DDL_3(request, session, codiceSocieta, codiceUtente, codiceEnte, true);
					break;
					/*
					 * Clicco il pulsante Add Tipologia Servizio
					 */
				case TX_BUTTON_ADD:
					System.out.println("init aggiunte tipologie");
					loadDDLUtente(request, session, codiceSocieta, "", false);
					loadDDLEnte(request, session, codiceSocieta, "", codiceUtente, false);
					addTipologiaServizio(request, session);
					System.out.println("fine aggiunte tipologie");
					break;
					
					/*
					 * Clicco il pulsante Remove Tipologia Servizio
					 */
				case TX_BUTTON_REMOVE:
					loadDDLUtente(request, session, codiceSocieta, "", false);
					loadDDLEnte(request, session, codiceSocieta, "", codiceUtente, false);
					removeTipologiaServizio(request, session);
					break;

				/*
				 * Vengo dallo STEP 2
				 */
				case TX_BUTTON_STEP1:
					getAdd1ParametersFromSession(request,session);
					profiloUtente = getProfiloUtente(userProfile);
					setDDL(request,profiloUtente);
					loadDDLUtente(request, session, codiceSocieta, "", true);
					loadDDLEnte(request, session, codiceSocieta, "", codiceUtente, true);
					if (profiloUtente.name().equals("AMEN"))
						loadTipologiaServizioXml_DDL_3(request, session, codiceSocieta, codiceUtente, codiceEnte, false);
					
					setForm1Attributes(request);
					break;
				/*
				 * Vado allo STEP 2	
				 */
				case TX_BUTTON_AVANTI:
					String esito = checkInputFields(request,profiloUtente, codop, session);
					request.setAttribute("inputFieldChecked", esito);
					if (esito.equals("OK")) putAdd1ParametersInSession(request,session);
					else  {
						loadDDLUtente(request, session, codiceSocieta, "", false);
						loadDDLEnte(request, session, codiceSocieta, "", codiceUtente, false);
						if (profiloUtente.name().equals("AMEN"))
							loadTipologiaServizioXml_DDL_3(request, session, codiceSocieta, codiceUtente, codiceEnte, false);
						
						setFormMessage("var_form", esito, request);
					}
					break;
					/*
					 * Clicco il pulsante Salva (visibile solo se scelgo il profilo PYCO)
					 */
				case TX_BUTTON_EDIT_END:
					esito = checkInputFields(request,profiloUtente, codop, session);
					request.setAttribute("inputFieldChecked", esito);
					if (esito.equals("OK")) 
					{
						putAdd1ParametersInSession(request,session);
						getAdd1ParametersFromSession(request,session);
						UserEditAction2 userEdit2 = new UserEditAction2();
						userEdit2.modificaUtente(request, session, "var_form", true, false);
					}
					break;
					/*
					 * Clicco il pulsante Salva (visibile solo se scelgo il profilo OPER)
					 */
				case TX_BUTTON_EDIT_OPERATORE:
					esito = checkInputFields(request,profiloUtente, codop, session);
					request.setAttribute("inputFieldChecked", esito);
					if (esito.equals("OK")) 
					{
						putAdd1ParametersInSession(request,session);
						getAdd1ParametersFromSession(request,session);
						
						if (getTemplateCurrentApplication(request, session).equals("lepida"))
						{
							org.seda.payer.manager.adminusers.lepida.actions.UserEditAction2 userEdit2Lepida = new org.seda.payer.manager.adminusers.lepida.actions.UserEditAction2();
							userEdit2Lepida.modificaUtente(request, session, "var_form", true);
						}
						else
						{
							UserEditAction2 userEdit2 = new UserEditAction2();
							userEdit2.modificaUtente(request, session, "var_form", false, true);
						}
					}
					break;
				case TX_BUTTON_RESET:
				case TX_BUTTON_NULL: 
					/*
					 * Recupero le informazioni dell'utente da aggiornare
					 * e le salvo in sessione nell'attributo "selezionaRes" 
					 * e le cancello se torno al form di ricerca oppure, 
					 * nello STEP2, se la modifica è stata effettuata correttamente.
					 *  
					 * Queste informazioni servono anche nello STEP2:
					 * - per settare i flag relativi alle applicazioni la
					 *   prima volta che si esegue il passaggio STEP1-->STEP2
					 *   
					 * - per effettuare la funzione di RESET che riporta
					 *   la situazione dei flag a quella iniziale  
					 *   
					 * Per distinguere il primo passaggio  STEP1-->STEP2 dai
					 * successivi uso una variabile di sessione, "firstStep2",
					 * che viene resettata nello STEP2 
					 */
				try {
					String chiaveutenteApp = (String)request.getAttribute("tx_chiaveutente");
					chiaveutenteApp = (chiaveutenteApp == null ? (String)request.getAttribute("tx_chiaveutente_hidden") : chiaveutenteApp);
					chiaveUtente = Long.parseLong(chiaveutenteApp);
					if (chiaveUtente.longValue() > 0)
					{
						selezionaReq = new SelezionaUtenteRequestType(chiaveUtente);
						selezionaRes = WSCache.adminUsersServer.selezionaUtente(selezionaReq, request);
						selezionaResponse = selezionaRes.getResponse();
						if (selezionaResponse != null)
						{
							selezionaRetCode = selezionaResponse.getRetCode();
							selezionaRetMessage = selezionaResponse.getRetMessage();
							if (selezionaRetCode.equals("00"))
							{
								setForm1AttributesFromWs(session, request, selezionaRes);
								profiloUtente = getProfiloUtente(userProfile);
								setDDL(request,profiloUtente);
								loadDDLUtente(request, session, codiceSocieta, "", true);
								loadDDLEnte(request, session, codiceSocieta, "", codiceUtente, true);
								if (profiloUtente.name().equals("AMEN"))
									loadTipologiaServizioXml_DDL_3(request, session, codiceSocieta, codiceUtente, codiceEnte, true);
								
								session.setAttribute("selezionaRes", selezionaRes);
								session.setAttribute("firstStep2", "Y");
							}
							else
								setFormMessage("var_form", selezionaRetCode + " - " + selezionaRetMessage, request);
						}
						else
							setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
					else
						setFormMessage("var_form",Messages.PROFILO_NON_VALIDO.format(), request);
					
				} catch (FaultType e) {
					setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("var_form", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				}
				break;
			}
			request.setAttribute("Step", "1");
			/*
			 * Le DDL "Profilo Utente", "Società", "Utente" ed "Ente"
			 * erano sempre disabilitate come da specifica.
			 * Sono state abilitate su richiesta di Lepida
			 */
			
	/*		request.setAttribute("ddlProfiloUtenteDisabled", true);
			request.setAttribute("ddlSocietaDisabled", true);
			request.setAttribute("ddlUtenteDisabled", true);
			request.setAttribute("ddlEnteDisabled", true);
	*/
			request.setAttribute("usernameModificabile", false);
			request.setAttribute("codiceFiscaleModificabile", false);
			/*
			 * Setto gli attributi relativi ai campi disabilitati
			 * ricavati dai campi hidden
			 */
			request.setAttribute("tx_chiaveutente_hidden", chiaveUtente);
			request.setAttribute("tx_username", username);
			request.setAttribute("tx_userprofile", userProfile);
			request.setAttribute("tx_societa", codiceSocieta);
			request.setAttribute("tx_utente", codiceUtente);
			request.setAttribute("tx_ente", codiceEnte);
			request.setAttribute("tx_codiceFiscale", codiceFiscale);
			//inizio LP PG200060
			if(!template.equalsIgnoreCase("regmarche")) {
			//fine LP PG200060
			request.setAttribute("tx_pertinenza", entePertinenza);	//EP160510_001 GG 03112016	//TODO da verificare
			request.setAttribute("tx_gruppo_agenzia", codiceGruppoAgenzia);	//RE180181_001 SB
			//inizio LP PG200060
			}
			//fine LP PG200060
			/*
			 * Carico le società
			 */
			loadSocietaXml_DDL(request);
			
			/*
			 * Setto l'action della form di "adminusers_var.jsp"
			 * ed il tipo di operazione
			 */
			request.setAttribute("do_command_name","userEdit.do");
			request.setAttribute("codop",codop);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("errore = " + e);
			// TODO: handle exception
		}
		
		return null;
	}
	
}