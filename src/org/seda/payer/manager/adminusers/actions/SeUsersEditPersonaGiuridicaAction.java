package org.seda.payer.manager.adminusers.actions;

import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.adminusers.util.Error;
import org.seda.payer.manager.adminusers.util.Profilo;
import org.seda.payer.manager.adminusers.util.ProfiloUtil;
import org.seda.payer.manager.util.ManagerKeys;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.adminusers.dati.ResponseType;

public class SeUsersEditPersonaGiuridicaAction extends BaseAdminusersAction  {
		
		private static final long serialVersionUID = 1L;
			private static String codop = "edit";
			private String step="personagiuridica";
			private String stepOld;

			ResponseType response = null;
			Profilo profilo;

			public Object service(HttpServletRequest request) throws ActionException {

				HttpSession session = request.getSession();
				String esito = "OK";
				Calendar cal = Calendar.getInstance();
				request.setAttribute("yearNow", cal.get(Calendar.YEAR));
				
				Error errore;
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
				//setApplsDisabled(request, session);
				
				if (session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA) != null)
					profilo = (Profilo) session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA);
				
				if (!firedButton.equals(FiredButton.TX_BUTTON_RESET) && 
					!firedButton.equals(FiredButton.TX_BUTTON_NULL) &&
					!firedButton.equals(FiredButton.TX_BUTTON_STEP1) &&
					(!firedButton.equals(FiredButton.TX_BUTTON_AVANTI) || !session.getAttribute("Step").equals("seuseredit")))
					 
				{
					/*
					 *  Ricavo i parametri della request
					 */
					profilo = getPersonaGiuridicaFromRequest(request,profilo);
				}
				/*
				 * Compio le azioni corrispondenti alla richiesta dell'utente
				 */
				switch(firedButton)
				{
					/*
					 * Torno indietro allo STEP 1
					 */
					case TX_BUTTON_STEP1:
						if (session.getAttribute("Step").equals("personafisica"))
							step="personagiuridica";
						else 
							if (session.getAttribute("Step").equals("personagiuridica"))
								stepOld="personagiuridica";
						gestisciProvComu(request,session,profilo,step);
						break;
						/*
						 * La richiesta arriva dallo STEP1
						 * 
						 * Se trovo in sessione uno degli oggetti relativi allo STEP2
						 * non è la prima volta che lo STEP2 viene eseguito:
						 * recupero dalla sessione gli attributi
						 */
					case TX_BUTTON_AVANTI:
						
						if (session.getAttribute("Stepold").equals("seuseredit") && session.getAttribute("Step").equals("seuseredit")) {	
							stepOld="seuseredit";
							gestisciProvComu(request,session,profilo,step);
						}
						else
						{
							stepOld="personagiuridica";
							//PG180170 GG 17102018 - inizio
							//errore = checkSeUsersInputFieldsPersonaGiuridica(request, profilo, session, codop);
							String template = getTemplateCurrentApplication(request, request.getSession());
							//inizio LP PG200060
							if(template.equalsIgnoreCase("regmarche")) {
								errore = checkSeUsersInputFieldsPersonaGiuridica(request, profilo, session, codop);
							} else {
							//fine LP PG200060
							if (template.equalsIgnoreCase("trentrisc") && profilo.isFlagNoControlli()) {
								errore = checkSeUsersInputFieldsPersonaGiuridica2(request, profilo, session, codop);
							} else {
								errore = checkSeUsersInputFieldsPersonaGiuridica(request, profilo, session, codop);
							}
							//inizio LP PG200060
							}
							//fine LP PG200060
							//PG180170 GG 17102018 - fine
							
							if (!errore.getErrorvalue().equals("")) {
								esito = "KO";
								gestisciProvComu(request,session,profilo,step);
								setFormMessage("form_personagiuridica", errore.getErrorvalue(), request);
							}
							else 
								esito = "OK";
						}
						break;
						
					case TX_BUTTON_ProvinciaSedeLegale:
						profilo.clearComuneSedeLegale();
						gestisciProvComu(request,session,profilo,step);
						break;
						
					case TX_BUTTON_ComuneSedeLegaleEstero:
						profilo.setComuneSedeLegaleEstero(ProfiloUtil.invertiFlag(profilo.getComuneSedeLegaleEstero()));
						profilo.clearProvinciaSedeLegale();
						profilo.clearComuneSedeLegale();
						gestisciProvComu(request,session,profilo,step);
						break;
					case TX_BUTTON_ProvinciaSedeOperativa:
						profilo.clearComuneSedeOperativa();
						gestisciProvComu(request,session,profilo,step);
						break;
					case TX_BUTTON_FamigliaMerceologica:
						profilo.clearClassificazioneMerceologica();
						gestisciProvComu(request,session,profilo,step);
						break;
					//PG180050_001 GG - inizio
					case TX_BUTTON_TipologiaAlloggio:
						//profilo.clearClassificazioneMerceologica();
						profilo.clearNumeroAutorizzazione();	//TODO da verificare
						gestisciProvComu(request,session,profilo,step);
						break;
					//PG180050_001 GG - fine	
					case TX_BUTTON_RESET:
						gestisciProvComu(request,session,profilo,step);
						resetParametri(request);
						break;
						
					case TX_BUTTON_NULL:
						gestisciProvComu(request,session,profilo,step);
						break;	
					/*
					 * Torno al form di ricerca	
					 */
					case TX_BUTTON_CERCA:
						request.setAttribute("vista","seUsers_search");
						break;
				}
				/*
				 * Setto l'action della form di "seUsers_personagiuridica.jsp",
				 * il tipo di operazione e lo step
				 */
				AddProfiloInSession(session, profilo);
				MoveProfiloInRequest(request, profilo);
				request.setAttribute("do_command_name","seUsersEditPersonaGiuridica.do");
				request.setAttribute("codop",codop);
				session.setAttribute("Step", step);
				session.setAttribute("Stepold", stepOld == null ? session.getAttribute("Stepold") : stepOld);
				request.setAttribute("Step", session.getAttribute("Step"));
				request.setAttribute("Stepold", session.getAttribute("Stepold"));
				request.setAttribute("tipologiaUtente", profilo.getTipologiaUtente());
				request.setAttribute("inputFieldChecked", esito);
				return null;
			}

}
