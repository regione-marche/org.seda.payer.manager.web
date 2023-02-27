package org.seda.payer.manager.adminusers.actions;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.adminusers.util.Error;
import org.seda.payer.manager.adminusers.util.Profilo;
import org.seda.payer.manager.adminusers.util.ProfiloUtil;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.string.Pad;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.GetProgressivoComunicazioneRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.GetProgressivoComunicazioneResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciAnagraficaStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciAnagraficaStrutturaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaCatMerceologicaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaCatMerceologicaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.AnagraficaStrutturaRicettiva;
import com.seda.security.webservice.dati.AggiornaUtentePIVARequestType;
import com.seda.security.webservice.dati.AggiornaUtentePIVAResponseType;
import com.seda.security.webservice.dati.ContattiPIVA;
import com.seda.security.webservice.dati.DatiAnagPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiContattiPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiDocumentoPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento;
import com.seda.security.webservice.dati.DatiDomicilioPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiDomicilioPersonaFisicaDelegatoEsteroDomicilio;
import com.seda.security.webservice.dati.DatiGeneraliPIVA;
import com.seda.security.webservice.dati.DatiGeneraliPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiGeneraliPersonaFisicaDelegatoSesso;
import com.seda.security.webservice.dati.DatiResidenzaPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiResidenzaPersonaFisicaDelegatoEsteroResidenza;
import com.seda.security.webservice.dati.DatiUtentePIVA;
import com.seda.security.webservice.dati.DatidiNascitaPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatidiNascitaPersonaFisicaDelegatoFlagEsteroNascita;
import com.seda.security.webservice.dati.InserisciUtentePIVARequestType;
import com.seda.security.webservice.dati.InserisciUtentePIVAResponseType;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.dati.SedeLegale;
import com.seda.security.webservice.dati.SedeLegaleEsteroSedeLegale;
import com.seda.security.webservice.dati.SedeOperativa;
import com.seda.security.webservice.dati.SendMailCodiciRequestType;
import com.seda.security.webservice.dati.SendMailCodiciResponseType;
import com.seda.security.webservice.dati.TipoEmail;
import com.seda.security.webservice.dati.UtentePIVA;
import com.seda.security.webservice.dati.UtentePIVAFlagOperatoreBackOffice;
import com.seda.security.webservice.dati.UtentePIVATipologiaUtente;
import com.seda.security.webservice.dati.UtentePIVAUtenzaAttiva;
import com.seda.security.webservice.srv.FaultType;

public class SeUsersEditPersonaFisicaAction extends BaseAdminusersAction{
	
		private static final long serialVersionUID = 1L;
		private static String codop = "edit";
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
			/*
			String impostaSoggiorno_hidden = (String) request.getAttribute("impostaSoggiorno_hidden");
			request.setAttribute("impostaSoggiorno", impostaSoggiorno_hidden);
			*/
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

					
				case TX_BUTTON_AVANTI:
				
					break;
					
				case TX_BUTTON_ComuneNascitaEstero:
					profilo.setComuneNascitaEstero(ProfiloUtil.invertiFlag(profilo.getComuneNascitaEstero()));
					//ripulisco gli eventuali provincia/comune selezionati
					profilo.clearProvinciaNascita();
					profilo.clearComuneNascita();
					break;
				
				case TX_BUTTON_ComuneResidenzaEstero:
					
					profilo.setComuneResidenzaEstero(ProfiloUtil.invertiFlag(profilo.getComuneResidenzaEstero()));
					//ripulisco gli eventuali provincia/comune selezionati
					profilo.clearProvinciaResidenza();
					profilo.clearComuneResidenza();
					break;
					
				case TX_BUTTON_ComuneDomicilioEstero:
					profilo.setComuneDomicilioEstero(ProfiloUtil.invertiFlag(profilo.getComuneDomicilioEstero()));
					//ripulisco gli eventuali provincia/comune selezionati
					profilo.clearProvinciaDomicilio();
					profilo.clearComuneDomicilio();
					break;
					
				case TX_BUTTON_ProvinciaNascita:
					profilo.clearComuneNascita();
					break;
					
				case TX_BUTTON_ProvinciaResidenza:
					profilo.clearComuneResidenza();
					
					break;
					
				case TX_BUTTON_ProvinciaDomicilio:
					profilo.clearComuneDomicilio();
					break;
					
				case TX_BUTTON_ProvinciaSedeLegale:
					profilo.clearComuneSedeLegale();
					break;
					
				case TX_BUTTON_ComuneSedeLegaleEstero:
					profilo.setComuneSedeLegaleEstero(ProfiloUtil.invertiFlag(profilo.getComuneSedeLegaleEstero()));
					profilo.clearProvinciaSedeLegale();
					profilo.clearComuneSedeLegale();
					break;
				
				case TX_BUTTON_FamigliaMerceologica:
					profilo.clearClassificazioneMerceologica();
					break;
				
				case TX_BUTTON_TipologiaAlloggio:
					profilo.clearNumeroAutorizzazione();
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
					//fine LP PG200060
// INIZIO 20/12/2017 - Marini: "trentrisc" effettuava il controllo del CF lato maffo tramite la regola regex...che NON prevede l'omocodia 
//			           Con queste modifiche il CF anche per Trentivo viene calcolato e controllata l'omocodia
//					if (template.equals("trentrisc")) { 
//						errore = checkSeUsersInputFieldsPersonaFisicaTR(profilo,codop);
//					} else {
					//PG180170 GG 17102018 - inizio
					//errore = checkSeUsersInputFieldsPersonaFisica(profilo,codop);
//					}
					//FINE   20/12/2017 - Marini:					
					if (template.equalsIgnoreCase("trentrisc") && profilo.isFlagNoControlli()) {
						Boolean bCheck = true;
						if (session.getAttribute("flagNoControlli")!=null) 
							bCheck = !(Boolean)session.getAttribute("flagNoControlli");
						errore = checkSeUsersInputFieldsPersonaFisica2(profilo,codop,bCheck);
					} else {
						//inizio LP PG200060
						//errore = checkSeUsersInputFieldsPersonaFisica(profilo,codop);
						errore = checkSeUsersInputFieldsPersonaFisica(profilo, codop, template);
						//fine LP PG200060
					}
					//PG180170 GG 17102018 - fine
					request.setAttribute("utentePIVAChecked", errore);
					
					if (errore.getErrorvalue().equals("")) 
					{
						esito = "OK";
						try {
							// AGGIORNO L'UTENTE
							AggiornaUtentePIVARequestType req = new AggiornaUtentePIVARequestType();
							UtentePIVA utente = new UtentePIVA();
							//inizio LP PG200060
							String numAutorizzazione = "";
							boolean bInsAnagraficaStruttura = false;
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
									bInsAnagraficaStruttura = true;	//L'anagrafica struttura va inserita solo in caso di generazione di un nuovo codice autorizzazione in quanto non esistente una struttura per stesso codice fiscale/ piva e categoria merceologica
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
							//inizio LP PG200060
							//getDatiRegistrazione(utente, profilo, null, session);
							getDatiRegistrazione(utente, profilo, null, session, template);
							//fine LP PG200060
							req.setUtentePIVA(utente);
							String utenzaAttivaWEB =  req.getUtentePIVA().getUtenzaAttiva().getValue();
							String utenzaAttivaDB = (String) session.getAttribute("utenzaAttivaDB");
							
							AggiornaUtentePIVAResponseType out = WSCache.securityServer.aggiornaUtentePIVA(req, request);
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
								request.setAttribute("editUserRetCode", retCode);
								
								//String userName = (String)request.getAttribute("tx_username");
								String userName = profilo.getUsername();
								
								SendMailCodiciRequestType in=new SendMailCodiciRequestType();
								SendMailCodiciResponseType outsend = null;
								in.setUserName(userName);
								in.setUrlSito(request.getServerName());
								
								if (retCode.equals("00")) {
									//PG180050_001 GG - inizio
									boolean salvataggioOK = true;
									String retMessage2 = "";
									//inizio LP PG200060
									if(!template.equalsIgnoreCase("regmarche")) {
									//fine LP PG200060
									//Nel caso delle tipologie di alloggio Locazioni brevi e Locazioni brevi agenzie genero l'anagrafica struttura del WIS
									if (isSoris(request) && (profilo.getClassificazioneMerceologicaDettaglio()!=null && (profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.61") || profilo.getClassificazioneMerceologicaDettaglio().equals("55.20.62")))) {
										if (bInsAnagraficaStruttura) {	//TODO da verificare se serve tale condizione, cioè se inserisco struttura solo in caso di nuova autorizzazione
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
									if (salvataggioOK) {
										session.setAttribute("addUserRetMessage", retMessage);
										
										//Da verificare se spostare qui quanto segue rif. PG180050_001
										if (utenzaAttivaDB != utenzaAttivaWEB) 
											if (utenzaAttivaDB == "Y") {
												in.setTipoEmail(TipoEmail.REV);
												outsend =  WSCache.securityServer.sendMailCodici(in, request);
											}
											else {
												in.setTipoEmail(TipoEmail.ATT);
												outsend =  WSCache.securityServer.sendMailCodici(in, request);
											}
										if (outsend!=null) {
											ResponseType responseType = outsend.getResponse();
										
											if (responseType.getRetCode().equals("00"))
												setFormMessage("form_personafisica", "Errore nell''invio email" , request);
										}
									}
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
						esito = "KO";
						setFormMessage("form_personafisica", errore.getErrorvalue(), request);
					}
					break;
				
				case TX_BUTTON_RESET:
					resetParametri(request);
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
			
			//inizio LP PG200060
			if(!template.equalsIgnoreCase("regmarche")) {
			//fine LP PG200060
			//PG180170 GG - inizio
			boolean flagNoControlli = false;
			//inizio LP PG200060
			//String template = getTemplateCurrentApplication(request, request.getSession());
			//fine LP PG200060
			if (template.equalsIgnoreCase("trentrisc")) {
				String strStepOld = session.getAttribute("Stepold")==null?"":(String)session.getAttribute("Stepold");
				if (strStepOld.equalsIgnoreCase("personagiuridica")) {
					if (profilo.isFlagNoControlli())
						flagNoControlli = true;
				}
				
			}
			session.setAttribute("flagNoControlli", flagNoControlli);
			//PG180170 GG - fine
			//inizio LP PG200060
			}
			//fine LP PG200060
			
			/*
			 * Setto l'action della form di "seUsers_personafisica.jsp",
			 * il tipo di operazione e lo step
			 */
			
			gestisciProvComu(request,session,profilo,step);
			
			AddProfiloInSession(session, profilo);
			MoveProfiloInRequest(request, profilo);
			request.setAttribute("do_command_name","seUsersEditPersonaFisica.do");
			request.setAttribute("codop",codop);
			session.setAttribute("Step", step);
			session.setAttribute("Stepold", stepOld == null ? session.getAttribute("Stepold") : stepOld);
			request.setAttribute("Step", session.getAttribute("Step"));
			request.setAttribute("Stepold", session.getAttribute("Stepold"));
			request.setAttribute("inputFieldChecked", esito);
			return null;
		}

		
		
		//inizio LP PG200060
		//private void getDatiRegistrazione(UtentePIVA utente, Profilo profilo, String flagAbilitazioneAutomatica, HttpSession session)
		private void getDatiRegistrazione(UtentePIVA utente, Profilo profilo, String flagAbilitazioneAutomatica, HttpSession session, String template)
		//fine LP PG200060
		{
			userBean = (UserBean) session.getAttribute(SignOnKeys.USER_BEAN);
			
			Calendar now = Calendar.getInstance();
			
			utente.setUsername(profilo.getUsername()); //in modifica è la chiave
			utente.setTipologiaUtente(UtentePIVATipologiaUtente.fromString(profilo.getTipologiaUtente()));
			
			
			//dati da non modificare
			utente.setUtenzaAttiva(UtentePIVAUtenzaAttiva.fromString(profilo.isUtenzaAttiva()? "Y": "N")); 
			utente.setDataInizioValiditaUtenza(null);
			utente.setDataFineValiditaUtenza(profilo.getDataFineValiditaUtenza());
			utente.setPrimoAccesso(null);
			utente.setNote(profilo.getNote());
		
			
			utente.setDataScadenzaPassword(null); 
			utente.setDataUltimoAccesso(null); 
			utente.setDataInserimentoUtenza(null); 
			
			utente.setDataUltimoAggiornamento(now);
		    utente.setOperatoreUltimoAggiornamento(userBean.getUserName());
			//inizio LP PG200060
			if(!template.equalsIgnoreCase("regmarche")) {
			//fine LP PG200060
		    utente.setFlagOperatoreBackOffice(UtentePIVAFlagOperatoreBackOffice.fromString(profilo.isOperatoreBackOffice()? "Y": "N"));
			//inizio LP PG200060
			} else {
				utente.setFlagOperatoreBackOffice(UtentePIVAFlagOperatoreBackOffice.fromString("N"));
			}
			//fine LP PG200060
			
		    //dati impresa
			//inizio LP PG200060
			if(!template.equalsIgnoreCase("regmarche")) {
			//fine LP PG200060
		    String numeroAutorizzazione = profilo.getNumeroAutorizzazione()==null?"":(String)profilo.getNumeroAutorizzazione();
		    if (utente.getTipologiaUtente().equals(UtentePIVATipologiaUtente.G) || utente.getTipologiaUtente().equals(UtentePIVATipologiaUtente.D))
		    	utente.setDatiUtentePIVA(getDatiPIVA(profilo));
		    else if (utente.getTipologiaUtente().equals(UtentePIVATipologiaUtente.F) && numeroAutorizzazione.trim().length()>0)
		    	utente.setDatiUtentePIVA(getDatiPIVA(profilo));
		    else 
		    {
		    	//potrei aver compilato i dati della persona giuridica; se alla fine ho scelto persona fisica
		    	//al salvataggio devo ripulire le informazioni della persona giuridica
		    	utente.setDatiUtentePIVA(getDatiPIVAEmpty());
		    }
			//inizio LP PG200060
			} else {
			    if (utente.getTipologiaUtente().equals(UtentePIVATipologiaUtente.G))
			    	utente.setDatiUtentePIVA(getDatiPIVA(profilo));
			    else 
			    {
			    	//potrei aver compilato i dati della persona giuridica; se alla fine ho scelto persona fisica
			    	//al salvataggio devo ripulire le informazioni della persona giuridica
			    	utente.setDatiUtentePIVA(getDatiPIVAEmpty());
			    }
			}
			//fine LP PG200060

		    //dati persona fisica / delegato
		    utente.setDatiAnagPersonaFisicaDelegato(getDatiAnag(profilo));
		
		}
		
		private DatiAnagPersonaFisicaDelegato getDatiAnag(Profilo profilo)
		{
			DatiAnagPersonaFisicaDelegato anag = new DatiAnagPersonaFisicaDelegato();
		    //Dati generali
		    DatiGeneraliPersonaFisicaDelegato datiGenerali = new DatiGeneraliPersonaFisicaDelegato();
		    
		    datiGenerali.setCognome(profilo.getCognome());
		    datiGenerali.setNome(profilo.getNome());
		    datiGenerali.setSesso(profilo.getSesso().equals("F") ? DatiGeneraliPersonaFisicaDelegatoSesso.F : DatiGeneraliPersonaFisicaDelegatoSesso.M);
		    datiGenerali.setCodiceFiscale(profilo.getCodiceFiscale());
		    
		    anag.setDatiGeneraliPersonaFisicaDelegato(datiGenerali);

		    //Dati di Nascita
		    DatidiNascitaPersonaFisicaDelegato datiNascita = new DatidiNascitaPersonaFisicaDelegato();
		    
		    datiNascita.setDataDiNascita(profilo.getDataNascita().getTime());
		    datiNascita.setProvinciaDiNascita(profilo.getProvinciaNascitaSigla());
		    datiNascita.setCodbelfDiNascita(profilo.getComuneNascitaBelfiore());
		    datiNascita.setFlagEsteroNascita(profilo.getComuneNascitaEstero().equals("Y") ? DatidiNascitaPersonaFisicaDelegatoFlagEsteroNascita.Y : DatidiNascitaPersonaFisicaDelegatoFlagEsteroNascita.N);
		    
		    
		    anag.setDatidiNascitaPersonaFisicaDelegato(datiNascita);

		    //Dati Documento
		    DatiDocumentoPersonaFisicaDelegato datiDocumento = new DatiDocumentoPersonaFisicaDelegato();
		    
		    if (profilo.getTipoDocumento().equals("CI"))
		    	datiDocumento.setTipoDocumentoRiconoscimento(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.CI);
		    else if (profilo.getTipoDocumento().equals("PA"))
		    	datiDocumento.setTipoDocumentoRiconoscimento(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.PA);
		    else if (profilo.getTipoDocumento().equals("PP"))
		    	datiDocumento.setTipoDocumentoRiconoscimento(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.PP);
		    else 
		    	datiDocumento.setTipoDocumentoRiconoscimento(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.NP);

		    datiDocumento.setNumeroDocumentoRiconoscimento(profilo.getNumeroDocumento());
		    datiDocumento.setEnteRilascioDocumento(profilo.getEnteRilascioDocumento());
		    datiDocumento.setDataRilascioDocumento(profilo.getDataRilascioDocumento().getTime());
		    
		    anag.setDatiDocumentoPersonaFisicaDelegato(datiDocumento);

		    //Dati Residenza
		    DatiResidenzaPersonaFisicaDelegato datiResidenza = new DatiResidenzaPersonaFisicaDelegato();
		    
		    datiResidenza.setIndirizzoResidenza(profilo.getIndirizzoResidenza());
		    datiResidenza.setProvinciaResidenza(profilo.getProvinciaResidenzaSigla());
		    datiResidenza.setCodbelfResidenza(profilo.getComuneResidenzaBelfiore());
		    datiResidenza.setCapComuneResidenza(profilo.getCapResidenza());
		    datiResidenza.setEsteroResidenza(profilo.getComuneResidenzaEstero().equals("Y") ? DatiResidenzaPersonaFisicaDelegatoEsteroResidenza.Y : DatiResidenzaPersonaFisicaDelegatoEsteroResidenza.N);
		    
		    anag.setDatiResidenzaPersonaFisicaDelegato(datiResidenza);

		    //Dati Domicilio
		    DatiDomicilioPersonaFisicaDelegato datiDomicilio = new DatiDomicilioPersonaFisicaDelegato();
		    
		    datiDomicilio.setIndirizzoDomicilio(profilo.getIndirizzoDomicilio());
		    datiDomicilio.setProvinciaDomicilio(profilo.getProvinciaDomicilioSigla());
		    datiDomicilio.setCodbelfDomicilio(profilo.getComuneDomicilioBelfiore());
		    datiDomicilio.setCapComuneDomicilio(profilo.getCapDomicilio());
		    datiDomicilio.setEsteroDomicilio(profilo.getComuneDomicilioEstero().equals("Y") ? DatiDomicilioPersonaFisicaDelegatoEsteroDomicilio.Y : DatiDomicilioPersonaFisicaDelegatoEsteroDomicilio.N);
		    
		    anag.setDatiDomicilioPersonaFisicaDelegato(datiDomicilio);

		    //Dati Contatti
		    DatiContattiPersonaFisicaDelegato datiContatti = new DatiContattiPersonaFisicaDelegato();
		    
		    datiContatti.setMail(profilo.getEmail());
		    datiContatti.setPec(profilo.getEmailPEC());
		    datiContatti.setTelefono(profilo.getTelefono());
		    datiContatti.setCellulare(profilo.getCellulare());
		    datiContatti.setFax(profilo.getFax());
		    
		    anag.setDatiContattiPersonaFisicaDelegato(datiContatti);
		    
		    return anag;
		}
		
		private DatiUtentePIVA getDatiPIVA(Profilo profilo)
		{
			DatiUtentePIVA datiPiva = new DatiUtentePIVA();
			
			//Dati Generali
			DatiGeneraliPIVA datiGenerali = new DatiGeneraliPIVA();
			
			datiGenerali.setPartitaIVA(profilo.getPartitaIVA());
			datiGenerali.setRagioneSociale(profilo.getRagioneSociale());
			datiGenerali.setClassificazioneMerceologica(!profilo.getClassificazioneMerceologicaDettaglio().equals("") ? profilo.getClassificazioneMerceologicaDettaglio() : "xx.xx.xx");
			datiGenerali.setNumeroAutorizzazione(profilo.getNumeroAutorizzazione());
		    
			datiPiva.setDatiGeneraliPIVA(datiGenerali);

			//Sede Legale
			SedeLegale sedeLegale = new SedeLegale();
		    
			sedeLegale.setIndirizzoSedeLegale(profilo.getIndirizzoSedeLegale());
			sedeLegale.setCodbelfSedeLegale(profilo.getComuneSedeLegaleBelfiore());
			sedeLegale.setProvinciaSedeLegale(profilo.getProvinciaSedeLegaleSigla());
			sedeLegale.setCapComuneSedeLegale(profilo.getCapSedeLegale());
			sedeLegale.setEsteroSedeLegale(profilo.getComuneSedeLegaleEstero().equals("Y") ? SedeLegaleEsteroSedeLegale.Y : SedeLegaleEsteroSedeLegale.N);
		    
		    datiPiva.setSedeLegale(sedeLegale);

		    //Sede Operativa
		    SedeOperativa sedeOperativa = new SedeOperativa();
		    
		    sedeOperativa.setIndirizzoSedeOperativa(profilo.getIndirizzoSedeOperativa());
			sedeOperativa.setCodbelfSedeOperativa(profilo.getComuneSedeOperativaBelfiore());
			sedeOperativa.setProvinciaSedeOperativa(profilo.getProvinciaSedeOperativaSigla());
			sedeOperativa.setCapComuneSedeOperativa(profilo.getCapSedeOperativa());
			
		    datiPiva.setSedeOperativa(sedeOperativa);

		    //Contatti Impresa
		    ContattiPIVA contatti = new ContattiPIVA();
		    
		    contatti.setMailUtentePIVA(profilo.getEmailImpresa());
		    contatti.setPecUtentePIVA(profilo.getEmailPECImpresa());
		    contatti.setTelefonoUtentePIVA(profilo.getTelefonoImpresa());
		    contatti.setCellulareUtentePIVA(profilo.getCellulareImpresa());
		    contatti.setFaxUtentePIVA(profilo.getFaxImpresa());
		    
		    datiPiva.setContattiPIVA(contatti);
		    
			return datiPiva;
		}
		
		private DatiUtentePIVA getDatiPIVAEmpty()
		{
			DatiUtentePIVA datiPiva = new DatiUtentePIVA();
			
			//Dati Generali
			DatiGeneraliPIVA datiGenerali = new DatiGeneraliPIVA();
			
			datiGenerali.setPartitaIVA("");
			datiGenerali.setRagioneSociale("");
			datiGenerali.setClassificazioneMerceologica("xx.xx.xx");
			datiGenerali.setNumeroAutorizzazione("");
		    
			datiPiva.setDatiGeneraliPIVA(datiGenerali);

			//Sede Legale
			SedeLegale sedeLegale = new SedeLegale();
		    
			sedeLegale.setIndirizzoSedeLegale("");
			sedeLegale.setCodbelfSedeLegale("");
			sedeLegale.setProvinciaSedeLegale("");
			sedeLegale.setCapComuneSedeLegale("");
			sedeLegale.setEsteroSedeLegale(SedeLegaleEsteroSedeLegale.N);
		    
		    datiPiva.setSedeLegale(sedeLegale);

		    //Sede Operativa
		    SedeOperativa sedeOperativa = new SedeOperativa();
		    
		    sedeOperativa.setIndirizzoSedeOperativa("");
			sedeOperativa.setCodbelfSedeOperativa("");
			sedeOperativa.setProvinciaSedeOperativa("");
			sedeOperativa.setCapComuneSedeOperativa("");
			
		    datiPiva.setSedeOperativa(sedeOperativa);

		    //Contatti Impresa
		    ContattiPIVA contatti = new ContattiPIVA();
		    
		    contatti.setMailUtentePIVA("");
		    contatti.setPecUtentePIVA("");
		    contatti.setTelefonoUtentePIVA("");
		    contatti.setCellulareUtentePIVA("");
		    contatti.setFaxUtentePIVA("");
		    
		    datiPiva.setContattiPIVA(contatti);
		    
			return datiPiva;
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
