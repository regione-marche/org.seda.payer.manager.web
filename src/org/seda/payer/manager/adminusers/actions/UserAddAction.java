package org.seda.payer.manager.adminusers.actions;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.adminusers.actions.UserAddAction2;
import org.seda.payer.manager.util.PayerCommand;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.GruppoAgenziaPageList;

@SuppressWarnings("serial")
public class UserAddAction extends BaseAdminusersAction implements PayerCommand {

	private static String codop = "add";
	public Object service(HttpServletRequest request) {
		HttpSession session = request.getSession();
		ProfiloUtente profiloUtente = ProfiloUtente.AMNULL;
		String esito = "";
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		//System.out.println("UserAddAction ".concat(firedButton.toString()));
		/*
		 * Azzero il campo hidden "fired_button_hidden" per evitare
		 * che possa essere riproposto nel caso in cui interviene 
		 * il "validator"
		 */
		request.setAttribute("fired_button_hidden", "");
		//
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET) && 
				!firedButton.equals(FiredButton.TX_BUTTON_STEP1) &&
				!firedButton.equals(FiredButton.TX_BUTTON_NULL) &&
				!firedButton.equals(FiredButton.TX_BUTTON_NUOVO)) 
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
			
			
			/*
			 * Controllo che siano stati riempiti
			 * tutti i campi obbligatori
			 */
			esito = checkInputFields(request,profiloUtente,codop, session);
			request.setAttribute("inputFieldChecked", esito);
		}
		/*
		 * Carico la lista delle società
		 */
		loadSocietaXml_DDL(request);
		request.setAttribute("ddlProfiloUtenteDisabled", false);
		request.setAttribute("usernameModificabile", true);
		
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, session); 
		if(!template.equalsIgnoreCase("regmarche")) {
		//fine LP PG200060
		/*
		 * Carico la lista dei gruppi agenzia
		 */
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
		//inizio LP PG200060
		}
		//fine LP PG200060
		
		//fine - RE180181 SB
		/*
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		switch(firedButton)
		{
			/*
			 * Vengo dallo STEP2
			 */
			case TX_BUTTON_STEP1:
				getAdd1ParametersFromSession(request,session);
				setForm1Attributes(request);
				profiloUtente = getProfiloUtente(userProfile);
				setDDL(request,profiloUtente);
				loadDDLUtente(request, session, codiceSocieta, "", true);
				loadDDLEnte(request, session, codiceSocieta, "", codiceUtente, true);
				if (profiloUtente.name().equals("AMEN"))
					loadTipologiaServizioXml_DDL_3(request, session, codiceSocieta, codiceUtente, codiceEnte, false);
				
				break;
				
			/*
			 * Vado allo STEP2	
			 */
			case TX_BUTTON_AVANTI:
				//inizio LP PG200060
				//if (esito.equals("OK")) 
				//	putAdd1ParametersInSession(request,session);
				//else
				if (esito.equals("OK")) {
					putAdd1ParametersInSession(request,session);
					if(template.equalsIgnoreCase("regmarche")) {
						request.setAttribute("tx_username_hidden", username);
					}
				} else
				//fine LP PG200060
				{
					loadDDLUtente(request, session, codiceSocieta, "", false);
					loadDDLEnte(request, session, codiceSocieta, "", codiceUtente, false);
					if (profiloUtente.name().equals("AMEN"))
						loadTipologiaServizioXml_DDL_3(request, session, codiceSocieta, codiceUtente, codiceEnte, false);
					
					setFormMessage("var_form", esito, request);
				}
				break;
			
				
			case TX_BUTTON_PROFILO_UTENTE_CHANGED:
				request.setAttribute("tx_button_profilo_utente_changed", "XX");
				request.setAttribute("tx_societa", "");
				initForm2FlagForProfile(request, session, userProfile);
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
				loadDDLUtente(request, session, codiceSocieta, "", false);
				loadDDLEnte(request, session, codiceSocieta, "", codiceUtente, false);
				addTipologiaServizio(request, session);
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
				 * Clicco il pulsante Salva (visibile solo se scelgo il profilo PYCO)
				 */
			case TX_BUTTON_AGGIUNGI_END:
				if (esito.equals("OK")) 
				{
					putAdd1ParametersInSession(request,session);
					UserAddAction2 userAdd2 = new UserAddAction2();
					userAdd2.inserisciUtente(request, session, "var_form", false);
				} else
					//inizio LP PG200060
					if(!template.equalsIgnoreCase("regmarche")) {
					//fine LP PG200060
					setFormMessage("var_form", esito, request);
					//inizio LP PG200060
					}
					//fine LP PG200060
				break;

				/*
				 * Clicco il pulsante Salva (visibile solo se scelgo il profilo OPERATORE)
				 */
			case TX_BUTTON_AGGIUNGI_OPERATORE:
				if (esito.equals("OK")) 
				{
					putAdd1ParametersInSession(request,session);
					if (getTemplateCurrentApplication(request, session).equals("lepida"))
					{
						org.seda.payer.manager.adminusers.lepida.actions.UserAddAction2 userAdd2Lepida = new org.seda.payer.manager.adminusers.lepida.actions.UserAddAction2();
						userAdd2Lepida.inserisciUtente(request, session, "var_form", true);
					}
					else
					{
						UserAddAction2 userAdd2 = new UserAddAction2();
						userAdd2.inserisciUtente(request, session, "var_form", true);
					}
				}
				break;
				
			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL: 
			case TX_BUTTON_NUOVO: 
				resetParametri(request);
				
				/*
				 * IMPOSTAZIONI DI PARTENZA
				 */
				// Il profilo è attivo
				request.setAttribute("chk_utenzaAttiva",true);
				//Le DDL "Società", "Utente" ed "Ente" sono disabilitate
				//fino a che non viene selezionato un profilo utente
				setDDL (request, ProfiloUtente.AMMI);
				
				break;
				
			/*
			 * Torno al form di ricerca	
			 */
			case TX_BUTTON_CERCA:
				request.setAttribute("vista","adminusers_search");
				break;
		}
		

		/*
		 * Setto l'action della form di "adminusers_var.jsp",
		 * il tipo di operazione e lo step
		 */
		request.setAttribute("do_command_name","userAdd.do");
		request.setAttribute("codop",codop);
		request.setAttribute("Step", "1");
		return null;
	}
	
	
	public Object execute(HttpServletRequest request) {
		
		return this.service(request);
	}
	
	
	
}