package org.seda.payer.manager.adminusers.actions;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.adminusers.util.Error;
import org.seda.payer.manager.adminusers.util.Profilo;
import org.seda.payer.manager.adminusers.util.ProfiloUtil;
import org.seda.payer.manager.util.CodiceFiscalePIVA;
import org.seda.payer.manager.util.Crypto;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.security.ChryptoServiceException;
import com.seda.commons.string.Pad;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.GetProgressivoComunicazioneRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.GetProgressivoComunicazioneResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciAnagraficaStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciAnagraficaStrutturaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaCatMerceologicaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaCatMerceologicaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.AnagraficaStrutturaRicettiva;
import com.seda.security.webservice.dati.InserisciUtentePIVARequestType;
import com.seda.security.webservice.dati.InserisciUtentePIVARequestTypeFlagAbilitazioneAutomatica;
import com.seda.security.webservice.dati.InserisciUtentePIVAResponseType;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.srv.FaultType;

public class SeUsersAddPersonaFisicaAction extends BaseAdminusersAction{
	
		private static final long serialVersionUID = 1L;
		private static String codop = "add";
		private static String step = "personafisica";
		private String stepOld;
		InserisciUtentePIVARequestType in = null;
		InserisciUtentePIVAResponseType out = null;
		ResponseType response = null;
		Profilo profilo ;


		public Object service(HttpServletRequest request) throws ActionException {
			String retCode = null;
			String retMessage = null;
			String esito = "OK";
			Calendar cal = Calendar.getInstance();
			request.setAttribute("yearNow", cal.get(Calendar.YEAR));

			HttpSession session = request.getSession();
			//inizio LP PG200060
			String template = getTemplateCurrentApplication(request, session); 
			//fine LP PG200060
			
			/*
			 * Individuo la richiesta dell'utente
			 */
			
			//String impostaSoggiorno_hidden = request.getAttribute("impostaSoggiorno_hidden")==null?"":(String) request.getAttribute("impostaSoggiorno_hidden");
			//inizio LP PG200060
			//String impostaSoggiorno_hidden = (String) request.getAttribute("impostaSoggiorno_hidden");
			//request.setAttribute("impostaSoggiorno", impostaSoggiorno_hidden);
			String impostaSoggiorno_hidden = "";
			if(!template.equalsIgnoreCase("regmarche")) {
				impostaSoggiorno_hidden = (String) request.getAttribute("impostaSoggiorno_hidden");
				request.setAttribute("impostaSoggiorno", impostaSoggiorno_hidden);
			}
			//fine LP PG200060
			
			FiredButton firedButton = getFiredButton(request);
			/*
			 * Azzero il campo hidden "fired_button_hidden" per evitare
			 * che possa essere riproposto nel caso in cui interviene 
			 * il "validator"
			 */
			request.setAttribute("fired_button_hidden", "");
			//setApplsDisabled(request, session);
			
			//inizio LP PG200060
			if(!template.equalsIgnoreCase("regmarche")) {
			//fine LP PG200060
			request.setAttribute("flgGiuridica", "N");
			if (((String)request.getAttribute("formName")).equals("form_personagiuridica")) {
				request.setAttribute("flgGiuridica", "Y");
			}
			//inizio LP PG200060
			}
			//fine LP PG200060
			
			if (session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA) != null)
				profilo = (Profilo) session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA);
			
			if (!firedButton.equals(FiredButton.TX_BUTTON_RESET) && 
				!firedButton.equals(FiredButton.TX_BUTTON_NULL) &&
				!firedButton.equals(FiredButton.TX_BUTTON_AVANTI)) 
			{
				/*
				 *  Ricavo i parametri della request
				 */
				profilo = getPersonaFisicaFromRequest(request,profilo);
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
					
					stepOld="personafisica";
				
					request.setAttribute("tipologiaUtente", profilo.getTipologiaUtente());
						
					break;

					
					/*
					 * La richiesta arriva dallo STEP1
					 * 
					 * Se trovo in sessione uno degli oggetti relativi allo STEP2
					 * non è la prima volta che lo STEP2 viene eseguito:
					 * recupero dalla sessione gli attributi
					 */
				case TX_BUTTON_AVANTI:
					
					
					//gestisciProvComu(request,session,profilo,step);
					
					break;
					
				case TX_BUTTON_ComuneNascitaEstero:
					profilo.setComuneNascitaEstero(ProfiloUtil.invertiFlag(profilo.getComuneNascitaEstero()));
					//ripulisco gli eventuali provincia/comune selezionati
					profilo.clearProvinciaNascita();
					profilo.clearComuneNascita();
					//gestisciProvComu(request,session,profilo,step);
					break;
					
				case TX_BUTTON_ProvinciaNascita:
					profilo.clearComuneNascita();
					//gestisciProvComu(request,session,profilo,step);
					break;
					
				case TX_BUTTON_ProvinciaResidenza:
					profilo.clearComuneResidenza();
					//gestisciProvComu(request,session,profilo,step);
					break;
					
				case TX_BUTTON_ComuneResidenzaEstero:
					
					profilo.setComuneResidenzaEstero(ProfiloUtil.invertiFlag(profilo.getComuneResidenzaEstero()));
					//ripulisco gli eventuali provincia/comune selezionati
					profilo.clearProvinciaResidenza();
					profilo.clearComuneResidenza();
					//gestisciProvComu(request,session,profilo,step);
					break;
					
				case TX_BUTTON_ProvinciaDomicilio:
					profilo.clearComuneDomicilio();
					//gestisciProvComu(request,session,profilo,step);
					break;
					
				case TX_BUTTON_ComuneDomicilioEstero:
					profilo.setComuneDomicilioEstero(ProfiloUtil.invertiFlag(profilo.getComuneDomicilioEstero()));
					//ripulisco gli eventuali provincia/comune selezionati
					profilo.clearProvinciaDomicilio();
					profilo.clearComuneDomicilio();
					//gestisciProvComu(request,session,profilo,step);
					break;
					
				case TX_BUTTON_ProvinciaSedeLegale:
					profilo.clearComuneSedeLegale();
					//gestisciProvComu(request,session,profilo,step);
					break;
					
				case TX_BUTTON_ComuneSedeLegaleEstero:
					profilo.setComuneSedeLegaleEstero(ProfiloUtil.invertiFlag(profilo.getComuneSedeLegaleEstero()));
					profilo.clearProvinciaSedeLegale();
					profilo.clearComuneSedeLegale();
					//gestisciProvComu(request,session,profilo,step);
					break;
				
				case TX_BUTTON_FamigliaMerceologica:
					profilo.clearClassificazioneMerceologica();
					//gestisciProvComu(request,session,profilo,step);
					break;
				
				case TX_BUTTON_TipologiaAlloggio:
					profilo.clearNumeroAutorizzazione();
					//gestisciProvComu(request,session,profilo,step);
					break;
					
				case TX_BUTTON_ImpostaSoggiorno:
					request.setAttribute("impostaSoggiorno", ProfiloUtil.invertiFlag(impostaSoggiorno_hidden));
					//Imposto fisso alloggio come classificazione merceologica
					if (((String)request.getAttribute("impostaSoggiorno")).equals("Y")) {
						profilo.setClassificazioneMerceologica("55");
					} else {
						profilo.clearClassificazioneMerceologica();
					}
					//profilo.clearNumeroAutorizzazione();	//TODO da verificare se serve
					break;	
					
				case TX_BUTTON_AGGIUNGI:
					/*
					 * Controllo dei campi inseriti per la persona fisica.
					 */
					
					//inizio LP PG200060
					//String template = getTemplateCurrentApplication(request, request.getSession());
					//fine LP PG200060
					Error errore = null;
// INIZIO 20/12/2017 - Marini: "trentrisc" effettuava il controllo del CF lato maffo tramite la regola regex...che NON prevede l'omocodia 
//					           Con queste modifiche il CF anche per Trentivo viene calcolato e controllata l'omocodia
//					if (template.equals("trentrisc")) { 
//						errore = checkSeUsersInputFieldsPersonaFisicaTR(profilo,codop);
//					} else {
					//inizio LP PG200060
						//errore = checkSeUsersInputFieldsPersonaFisica(profilo,codop);
					errore = checkSeUsersInputFieldsPersonaFisica(profilo, codop, template);
					//fine LP PG200060
//					}
// FINE   20/12/2017 - Marini:
						
					request.setAttribute("utentePIVAChecked", errore);
					if (errore.getErrorvalue().equals("")) 
					{
						// INSERISCO IL NUOVO UTENTE
						esito = "OK";
						try {
							//inizio LP PG200060
							boolean bInsAnagraficaStruttura = false;
							String numAutorizzazione = "";
							if(!template.equalsIgnoreCase("regmarche")) {
							//fine LP PG200060
							//PG180050_001 GG - inizio
							//inizio LP PG200060
							//String numAutorizzazione = setNumeroAutorizzazione(profilo, request, session);
							numAutorizzazione = setNumeroAutorizzazione(profilo, request, session);
							//fine LP PG200060
							if (numAutorizzazione==null) {
								throw new Exception("Errore in inserisciUtente in setNumeroAutorizzazione");
							}
							//inizio LP PG200060
							//boolean bInsAnagraficaStruttura = false;
							//fine LP PG200060
							//Se numero autorizzazione è non valorizzato dunque non è stato trovato in archivio va generato
							if (isSoris(request) && (profilo.getClassificazioneMerceologicaDettaglio()!=null && (profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.61") || profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.62")))) {
								if (numAutorizzazione.trim().equals("")) {
									bInsAnagraficaStruttura = true;	//l'anagrafica struttura va inserita solo in caso di generazione di un nuovo codice autorizzazione in quanto non esistente una struttura per stesso codice fiscale/ piva e categoria merceologica
									//Se il numero autorizzazione non è stato precedentemente generato provvedo a generarlo
									GetProgressivoComunicazioneResponse res = null;
									Calendar now = Calendar.getInstance();
									int currentYear = now.get(Calendar.YEAR);
									if (profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.61")) {
										GetProgressivoComunicazioneRequest rq = new GetProgressivoComunicazioneRequest("LB", currentYear);
										res = WSCache.impostaSoggiornoConfigServer.getProgressivoComunicazione(rq, request);
										numAutorizzazione = "LB/".concat(String.valueOf(currentYear)).concat("/").concat(Pad.left(res.getChiave(), 8, '0')).concat("/0");
									} else if (profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.62")) {
										GetProgressivoComunicazioneRequest rq = new GetProgressivoComunicazioneRequest("LBA", currentYear);
										res = WSCache.impostaSoggiornoConfigServer.getProgressivoComunicazione(rq, request);
										numAutorizzazione = "LBA/".concat(String.valueOf(currentYear)).concat("/").concat(Pad.left(res.getChiave(), 8, '0')).concat("/0");
									}
								}
							}
							profilo.setNumeroAutorizzazione(numAutorizzazione);
							//PG180050_001 GG - fine
							//inizio LP PG200060
							}
							//fine LP PG200060
							InserisciUtentePIVARequestType in = newInserisciUtentePIVARequestType(request,profilo,codop);
						
							InserisciUtentePIVAResponseType out = WSCache.securityServer.inserisciUtentePIVA(in, request);
							ResponseType response = out.getResponse();
							if (response != null)
							{
								/*
								 * Setto l'attributo "addUserRetCode" perchè il flow sappia
								 * dove redirigere la richiesta: sulla pagina di ricerca
								 * se l'inserimento è terminato correttamente
								 * oppure ancora sulla pagina di inserimento
								 * 
								 * Se l'esito è positivo metto anche in sessione
								 * il messaggio da visualizzare all'utente sulla
								 * maschera di ricerca.
								 * 
								 */
								retCode = response.getRetCode();
								retMessage = response.getRetMessage();
								request.setAttribute("addUserRetCode", retCode);
								if (retCode.equals("00")) {
									//PG180050_001 GG - inizio
									boolean salvataggioOK = true;
									String retMessage2 = "";
									//inizio LP PG200060
									if(!template.equalsIgnoreCase("regmarche")) {
									//fine LP PG200060
									//Nel caso delle tipologie di alloggio Locazioni brevi e Locazioni brevi agenzie genero l'anagrafica struttura del WIS
									if (isSoris(request) && (profilo.getClassificazioneMerceologicaDettaglio()!=null && (profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.61") || profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.62")))) {
										if (bInsAnagraficaStruttura) {	//Inserisco struttura solo in caso di nuova autorizzazione
											salvataggioOK = false;
											//Inserisco nella PYSANTB
											InserisciAnagraficaStrutturaRequest in2 = new InserisciAnagraficaStrutturaRequest();
											String strutturaRicettiva = "";
											if (profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.61")) 
												strutturaRicettiva = "018";
											if (profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.62")) 
												strutturaRicettiva = "019";
											AnagraficaStrutturaRicettiva anagraficaStrutturaRicettiva = new AnagraficaStrutturaRicettiva(
													"", profilo.getComuneSedeLegaleBelfiore(), //codiceComune
													numAutorizzazione, profilo.getPartitaIVA(), profilo.getRagioneSociale(), "", "", "", profilo.getIndirizzoSedeLegale(), profilo.getCapSedeLegale(), strutturaRicettiva,
													"", "N", //flag primo accesso
													"", //codiceUtente
													"",	//codiceEnte
													"", //impostaServizio
													"", null, "",	//usernameAutenticazione
													"", "N", null, null, null, null, null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
											in2.setAnagraficaStruttura(anagraficaStrutturaRicettiva);
											InserisciAnagraficaStrutturaResponse out2 = WSCache.impostaSoggiornoConfigServer.inserisciAnagraficaStruttura(in2, request);
											if (out2 != null)
											{	if(out2.getRetCode().equals("00")){
													salvataggioOK = true;
													System.out.println("Completato inserimento anagrafica struttura " + out2.getRetMessage());
												} else {
													retMessage2 = "Errore in inserimento anagrafica struttura " + out2.getRetMessage();
												}
											} else {
												retMessage2 = "Errore generico in inserimento anagrafica struttura";
											}
										}
									}
									//PG180050_001 GG - fine
									//inizio LP PG200060
									}
									//fine LP PG200060
									if (salvataggioOK) 
										session.setAttribute("addUserRetMessage", retMessage);
									else
										//inizio LP PG200060
										if(!template.equalsIgnoreCase("regmarche")) {
										//fine LP PG200060
										setFormMessage("form_personafisica", retMessage2, request);
										//inizio LP PG200060
										}
										//fine LP PG200060
								}
								else 
									setFormMessage("form_personafisica", retCode + " - " + retMessage, request);

							}
							else
								setFormMessage("form_personafisica", Messages.GENERIC_ERROR.format(), request);
							
						} catch (FaultType e) {
							setFormMessage("form_personafisica", "Errore: " + e.getLocalizedMessage(), request);
							e.printStackTrace();
						} catch (RemoteException e) {
							setFormMessage("form_personafisica", "Errore: " + e.getMessage(), request);
							e.printStackTrace();
						} catch (Exception e) {
							setFormMessage("form_personafisica", "Errore: " + e.getMessage(), request);
							e.printStackTrace();
						}
					}
					else
					{	
						//gestisciProvComu(request,session,profilo,step);
						esito = "KO";
						setFormMessage("form_personafisica", errore.getErrorvalue(), request);
					}
					break;
				
				case TX_BUTTON_RESET:
					resetPersonaFisica(profilo);
					//resetParametri(request);
					//gestisciProvComu(request,session,profilo,step);
					break;
					
				case TX_BUTTON_NULL:
					break;
					
				/*
				 * Torno al form di ricerca	
				 */
				case TX_BUTTON_CERCA:
					request.setAttribute("vista","seUsers_search");
					break;
			}
			/*
			 * Setto l'action della form di "seUsers_personafisica.jsp",
			 * il tipo di operazione e lo step
			 */
			gestisciProvComu(request,session,profilo,step);
			
			AddProfiloInSession(session, profilo);
			MoveProfiloInRequest(request, profilo);
			request.setAttribute("do_command_name","seUsersAddPersonaFisica.do");
			request.setAttribute("codop",codop);
			session.setAttribute("Step", step);
			session.setAttribute("Stepold", stepOld == null ? session.getAttribute("Stepold") : stepOld);
			request.setAttribute("Step", session.getAttribute("Step"));
			request.setAttribute("Stepold", session.getAttribute("Stepold"));
			request.setAttribute("inputFieldChecked", esito);
			return null;
		}

		private InserisciUtentePIVARequestType newInserisciUtentePIVARequestType(HttpServletRequest request, Profilo profilo, String codop) {
			InserisciUtentePIVARequestType req = new InserisciUtentePIVARequestType();

			req.setDescrComuneDomicilio(profilo.getComuneDomicilioDescrizione());
			req.setDescrComuneNascita(profilo.getComuneNascitaDescrizione());
			req.setDescrComuneResidenza(profilo.getComuneResidenzaDescrizione());
			req.setDescrComuneSedeLegale(profilo.getComuneSedeLegaleDescrizione());
			req.setDescrComuneSedeOperativa(profilo.getComuneSedeOperativaDescrizione());
			
			req.setPasswordAutogenerata(profilo.isPasswordAutogenerata()?"Y":"N");
			try {
				req.setPassword(Crypto.encrypt(profilo.getPassword()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ChryptoServiceException e) {
				e.printStackTrace();
			}
			req.setUrlSito(request.getServerName());
			req.setFlagAbilitazioneAutomatica(InserisciUtentePIVARequestTypeFlagAbilitazioneAutomatica.fromString( profilo.isUtenzaAttiva()? "Y":"N"));
			req.setUtentePIVA(newUtentePIVA(request,profilo, codop));

			return req;
}
		
		//PG180050_001 GG - inizio
		public String setNumeroAutorizzazione(Profilo profilo, HttpServletRequest request, HttpSession session) throws Exception {
			String numAutorizzazione = null;
			if (isSoris(request) && (profilo.getClassificazioneMerceologicaDettaglio()!=null && (profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.61") || profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.62")))) {
				//Verifico se sulla PYSANTB esiste già una autorizzazione per stesso codice fiscale e classificazione merceologica
				//Se non esiste lo genero
				String chiaveTipologiaStruttura = "";
				if (profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.61"))
					chiaveTipologiaStruttura = "018";
				if (profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.62"))
					chiaveTipologiaStruttura = "019";
				RecuperaAnagraficaStrutturaCatMerceologicaResponse res = null;
				RecuperaAnagraficaStrutturaCatMerceologicaRequest rq = new RecuperaAnagraficaStrutturaCatMerceologicaRequest("", profilo.getPartitaIVA(), chiaveTipologiaStruttura);
				try {
					res = WSCache.impostaSoggiornoConfigServer.recuperaAnagraficaStrutturaCatMerceologica(rq, request);
					if (res!=null) {
						if (res.getResponse().getRetCode().getValue().equals("00")) {
							if (res.getAnagraficaStruttura() != null)
								numAutorizzazione = res.getAnagraficaStruttura().getCodiceAutorizzazione();
							else 
								numAutorizzazione = "";		//record non trovato
						} else 
							System.out.println("recuperaAnagraficaStrutturaCatMerceologica per codice fiscale " + profilo.getCodiceFiscale() + " e chiave tipologia struttura " + chiaveTipologiaStruttura  + " - ERRORE " + res.getResponse().getRetMessage());
					} else 
						System.out.println("recuperaAnagraficaStrutturaCatMerceologica per codice fiscale " + profilo.getCodiceFiscale() + " e chiave tipologia struttura " + chiaveTipologiaStruttura  + " - ERRORE");
				} catch (Exception e) {
					System.out.println("Errore in recuperaAnagraficaStrutturaCatMerceologica per codice fiscale " + profilo.getCodiceFiscale() + " e chiave tipologia struttura " + chiaveTipologiaStruttura);
					throw new Exception("Errore in recuperaAnagraficaStrutturaCatMerceologica per codice fiscale " + profilo.getCodiceFiscale() + " e chiave tipologia struttura " + chiaveTipologiaStruttura);
				}
			} else
				numAutorizzazione = profilo.getNumeroAutorizzazione();
			return numAutorizzazione;
		}
		
		public boolean isSoris(HttpServletRequest request) {
			String template = getTemplateCurrentApplication(request, request.getSession());
			if (template.equals("soris")||template.equals("newsoris"))
				return true;
			else
				return false;
		}
		//PG180050_001 GG - fine
}
