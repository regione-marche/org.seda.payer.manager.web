
package org.seda.payer.manager.wisconfig.actions;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.CodiceFiscalePIVA;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PayerCommand;
import org.seda.payer.manager.util.PayerCommandException;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciAnagraficaStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciAnagraficaStrutturaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.AnagraficaStrutturaRicettiva;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaProvinceResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.security.webservice.dati.AggiornaUtentePIVARequestType;
import com.seda.security.webservice.dati.AggiornaUtentePIVAResponseType;
import com.seda.security.webservice.dati.DatiAnagPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiContattiPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiGeneraliPIVA;
import com.seda.security.webservice.dati.DatiGeneraliPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiUtentePIVA;
import com.seda.security.webservice.dati.ListaUtentiRequestType;
import com.seda.security.webservice.dati.ListaUtentiResponseType;
import com.seda.security.webservice.dati.SedeLegale;
import com.seda.security.webservice.dati.UtentePIVA;
import com.seda.security.webservice.dati.UtentePIVATipologiaUtente;
import com.seda.security.webservice.dati.UtentePIVAUtenzaAttiva;

@SuppressWarnings("serial")
public class ImpostaSoggiornoAnagStrutturaAddTrentrisc extends BaseManagerAction implements PayerCommand
{
	private static String codop = "add";
	private static String ritornaViewstate = "impostaSoggiornoAnagStruttura_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
	private String codiceProvincia = "";
	//inizio LP PG1800XX_016
	String templateName = "";
	//fine LP PG1800XX_016

	public Object execute(HttpServletRequest request) throws PayerCommandException {
		HttpSession session = request.getSession();
		//inizio LP PG1800XX_016
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		templateName = userBean.getTemplate(applicationName);
		
	
		//fine LP PG1800XX_016
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
		
		//PG180050_001 GG	
		case TX_BUTTON_TIPOLSTRUT_CHANGED:
			String strutturaRicettiva = request.getAttribute("strutturaRicettiva")==null?"":(String)request.getAttribute("strutturaRicettiva");
			if (strutturaRicettiva.equals("018") || strutturaRicettiva.equals("019")) {
				request.setAttribute("tx_autorizzazione","");
			}
			request.setAttribute(Field.TX_BUTTON_TIPOLSTRUT_CHANGED.format(), null);
			request.setAttribute("fired_button_hidden", null);
			break;

		case TX_BUTTON_AGGIUNGI:
			/*
			 * Recupero i parametri dalla form
			 */
			parametri = getAnagStrutturaFormParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			String esito =  checkAnagStruttura(parametri,request);
			/*
			 * Se OK effettuo il salvataggio
			 */
			if (esito.equals("OK"))
			{
				//PG180050_001 GG - inizio
				String numAutorizzazione = parametri.get("codiceAutorizzazione").toString();
				try {
					//Recupero del codice operatore
					UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
					if( user!=null) 
					   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
						usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
						AnagraficaStrutturaRicettiva anagraficaStrutturaRicettiva = new AnagraficaStrutturaRicettiva(
							parametri.get("chiaveAnagrafeStrutt").toString(), 
								parametri.get("codiceComune").toString(), 
									numAutorizzazione, //parametri.get("codiceAutorizzazione").toString(),	//PG180050_001 GG
										parametri.get("codiceFiscaleStruttura").toString().toUpperCase(),
											parametri.get("ragioneSociale").toString(),
												parametri.get("insegna").toString(),
													parametri.get("titolare").toString(),
														parametri.get("codiceFiscaleTitolare").toString().toUpperCase(),
															parametri.get("indirizzo").toString(),
																parametri.get("cap").toString(),
																	parametri.get("strutturaRicettiva").toString(),
																		parametri.get("mail").toString(),
																			"N", //flag primo accesso
																				parametri.get("codiceUtente").toString(),
																					parametri.get("codiceEnte").toString(),
																						parametri.get("impostaServizio").toString(),
																							parametri.get("codiceContrib").toString(),null,usernameAutenticazione,
																							parametri.get("mailPec").toString().trim(),
																							parametri.get("flagSubentro") != null ? parametri.get("flagSubentro").toString() : "N", //PG170240
																							parametri.get("dataValiditaInizio") != null ? (Calendar)parametri.get("dataValiditaInizio") : null,
																							parametri.get("dataValiditaFine") != null ? (Calendar)parametri.get("dataValiditaFine") : null,
																							parametri.get("dataObbligoComunicazioneInizio") != null ? (Calendar)parametri.get("dataObbligoComunicazioneInizio") : null,
																							parametri.get("dataObbligoComunicazioneFine") != null ? (Calendar)parametri.get("dataObbligoComunicazioneFine") : null,
																							parametri.get("note") != null ? parametri.get("note").toString() : "", 	//PG180170		//TODO da verificare
																							parametri.get("numeroAutorizzazioneCorrelata") != null ? parametri.get("numeroAutorizzazioneCorrelata").toString():"",
																							parametri.get("telefono") != null ? parametri.get("telefono").toString():"",
																						    parametri.get("dataNascitaTit") != null ? (Calendar)parametri.get("dataNascitaTit"):null,
																						    parametri.get("provinciaTit") != null ? parametri.get("provinciaTit").toString():"",
																						    parametri.get("comuneTit") != null ? parametri.get("comuneTit").toString():"",
																						    parametri.get("dataNascitaGest") != null ? (Calendar)parametri.get("dataNascitaGest"):null,
																						    parametri.get("provinciaGest") != null ? parametri.get("provinciaGest").toString():"",
																						    parametri.get("comuneGest") != null ? parametri.get("comuneGest").toString():"",
																						    parametri.get("cfGest") != null ? parametri.get("cfGest").toString():"",
																						    parametri.get("anagraficaGest") != null ? parametri.get("anagraficaGest").toString():"",
																						    null,null,null,null,null,
																						    parametri.get("comuneCatastale") != null ? parametri.get("comuneCatastale").toString():"",
																						    parametri.get("particellaEdificiale") != null ? parametri.get("particellaEdificiale").toString():"",
																						    parametri.get("subalterno") != null ? parametri.get("subalterno").toString():"",
																						    parametri.get("superficie") != null ? (BigDecimal)parametri.get("superficie"):new BigDecimal(0.00),
																						    parametri.get("postiLetto") != null ? (Integer)parametri.get("postiLetto"):new Integer(0),
																						    parametri.get("piano") != null ? parametri.get("piano").toString():""
																									
						);	//PG160040 GG 19052016
					
					InserisciAnagraficaStrutturaRequest in = new InserisciAnagraficaStrutturaRequest();
					in.setAnagraficaStruttura(anagraficaStrutturaRicettiva);
				
					InserisciAnagraficaStrutturaResponse out = WSCache.impostaSoggiornoConfigServer.inserisciAnagraficaStruttura(in, request);
					
					if (out != null)
					{
						if(out.getRetCode().equals("00")){
							request.setAttribute("vista",ritornaViewstate);
							//resetto i parametri se inserito
							resetParametri(request);
							setFormMessage("var_form_anag_struttura", out.getRetMessage(), request);
							
							//PG150420 GG 20052016 - inizio
							String template = getTemplateCurrentApplication(request, request.getSession());
								//Eseguo select su SEUSRTB per recupero codice società e codice utente nel caso di un solo record individuato.
								ListaUtentiRequestType listaUtentiRequestType = new ListaUtentiRequestType(null, 1000, 1, null, null, null, null, null, 
										ManagerStarter.configuration.getProperty(PropertiesPath.societa.format(template)), 
										anagraficaStrutturaRicettiva.getCodiceAutorizzazione(), null, null, null, null, null,null);
								ListaUtentiResponseType outListaUtentiPerAutorizzazione = WSCache.securityServer.listaUtenti(listaUtentiRequestType, request);
								if (outListaUtentiPerAutorizzazione.getResponse().getRetCode().equals("00")) {
									String xmlListaUtenti = outListaUtentiPerAutorizzazione.getListXml();
									String[] utente = getUtentiPerAutorizzazione(xmlListaUtenti, anagraficaStrutturaRicettiva);
									//Se non ho trovato utenti per codice belfiore e numero autorizzazione non sincronizzo la SEUSRTB
									if (Integer.valueOf(utente[0])>0) {
										//Se ho più di utenti per codice belfiore e numero autorizzazione non sincronizzo la SEUSRTB
										//Nel caso del manager riporto opportuna segnalazione
										if (Integer.valueOf(utente[0])>1) {
											setFormMessage("var_form_anag_struttura", out.getRetMessage().concat(". Sincronizzazione utenze correlate non eseguita"), request);
										} else {
											//Se ho un solo utente per codice belfiore e numero autorizzazione provvedo alla relativa sincronizzazione
											String username = utente[1];
											String codSoc = utente[2];
											AggiornaUtentePIVARequestType aggiornaUtentePIVARequestType = new AggiornaUtentePIVARequestType();
											aggiornaUtentePIVARequestType.setCodSoc(codSoc);
											aggiornaUtentePIVARequestType.setUtentePIVA(getUtentePIVA(anagraficaStrutturaRicettiva,request,username,utente[3],utente[4]));
											try {
												AggiornaUtentePIVAResponseType outUsr = WSCache.securityServer.aggiornaUtentePIVA(aggiornaUtentePIVARequestType, request);
				//								ResponseType response = outUsr.getResponse();
				//								if (response != null)
				//								{
				//									session.setAttribute("recordUpdated", "Anagrafica Struttura aggiornata con successo con sincronizzazione utenza");
				//								}
												setFormMessage("var_form_anag_struttura", out.getRetMessage().concat(". Sincronizzazione utenza correlata eseguita"), request);
											} catch (FaultType e) {
												e.printStackTrace();
											} catch (RemoteException e) {
												e.printStackTrace();
											}
										}
									}
								}
							
							//PG150420 GG 20052016 - fine
						} else setFormMessage("var_form_anag_struttura", out.getRetMessage(), request);

					}else setFormMessage("var_form_anag_struttura", Messages.GENERIC_ERROR.format(), request);
				} catch (FaultType e) {
					setFormMessage("var_form_anag_struttura", "Errore: " + e.getLocalizedMessage(), request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("var_form_anag_struttura", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				}
			}
			else
				setFormMessage("var_form_anag_struttura", esito, request);
			break;
		
		/*case TX_BUTTON_SOCIETA_CHANGED:
			parametri = getAbilitaCanalePagamentoTipoServizioEnteFormParameters(request,codop);
			
			if (!parametri.get("codiceSocieta").toString().equals(""))
				loadTipologiaServizioXml_DDL(request, session, parametri.get("codiceSocieta").toString(), true);
			
			request.setAttribute(Field.TX_BUTTON_SOCIETA_CHANGED.format(), null);
			request.setAttribute("fired_button_hidden", null);
			request.setAttribute(Field.TX_BUTTON_AGGIUNGI.format(),Field.TX_BUTTON_AGGIUNGI.format());
			
			break;*/
		}
		
		/*
		 * Se non esco dalla pagina carico le drop-down-lists
		 */
		/*if (!firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
		{
			loadSocietaUtenteEnteXml_DDL(request, session);
			//loadSocietaUtenteServizioEnteXml_DDL(request, session);
			if (firedButton.equals(FiredButton.TX_BUTTON_AGGIUNGI))
				loadTipologiaServizioXml_DDL(request, session, "", false);
			
			loadCanaliPagamentoAbilitazioneXml_DDL(request, session);
		}*/	
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","ImpostaSoggiornoAnagStrutturaAdd.do");
		request.setAttribute("codop",codop);

		//loadDDLStatic(request, session);

		loadListaTipoStrutture(request);
		//codiceProvincia = (request.getAttribute("tx_provincia") == null ? "" : (String)request.getAttribute("tx_provincia"));
		loadListaProvince(request);
		//loadBelfiore(codiceProvincia, request);
		
		aggiornamentoCombo(request, session);
		
		Calendar cal = Calendar.getInstance();
		request.setAttribute("yearNow", cal.get(Calendar.YEAR));

		return null;
		
	}
	
	private void loadListaProvince(HttpServletRequest request)
	{
		RecuperaProvinceResponse getProvinceRes = new RecuperaProvinceResponse();
		try {
			getProvinceRes = WSCache.commonsServer.recuperaProvince(request);
		} catch (com.seda.payer.pgec.webservice.commons.srv.FaultType e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		request.setAttribute("listProvince", getProvinceRes.getListXml());
	}

	private void loadBelfiore(String siglaProvincia, HttpServletRequest request, HttpSession session, String lista) {
		//if(session.getAttribute(lista)==null || session.getAttribute(lista).equals("")){
			if (siglaProvincia != null && siglaProvincia.length() > 0)
			{
				RecuperaBelfioreResponse getBelfioreRes = new RecuperaBelfioreResponse();
				try {
					getBelfioreRes = WSCache.commonsServer.recuperaBelfiore(new RecuperaBelfioreRequest(siglaProvincia), request);
				} catch (com.seda.payer.pgec.webservice.commons.srv.FaultType e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				request.setAttribute(lista, getBelfioreRes.getListXml());
				session.setAttribute(lista, getBelfioreRes.getListXml());
			}
		//}
	}
	
	private HashMap<String,Object> getAnagStrutturaFormParameters(HttpServletRequest request, String codop)
	{
		HashMap<String,Object> param = new HashMap<String, Object>();
		codiceProvincia = (request.getAttribute("tx_provincia") == null ? "" : (String)request.getAttribute("tx_provincia"));
		String codiceComune = (request.getAttribute("tx_comune") == null ? "" : (String)request.getAttribute("tx_comune"));
		
		String codiceAutorizzazione = (request.getAttribute("tx_autorizzazione") == null ? "" : (String)request.getAttribute("tx_autorizzazione"));
		
		String codiceFiscaleStruttura = (request.getAttribute("tx_fiscale") == null ? "" : (String)request.getAttribute("tx_fiscale"));
		codiceFiscaleStruttura = codiceFiscaleStruttura.toUpperCase();
		
		String ragioneSociale = (request.getAttribute("tx_rag_soc") == null ? "" : (String)request.getAttribute("tx_rag_soc"));
		String insegna = (request.getAttribute("tx_insegna") == null ? "" : (String)request.getAttribute("tx_insegna"));
		
		
		String codiceFiscaleTitolare = (request.getAttribute("tx_fiscale_tit") == null ? "" : (String)request.getAttribute("tx_fiscale_tit"));
		codiceFiscaleTitolare = codiceFiscaleTitolare.toUpperCase();
		
		String titolare = (request.getAttribute("tx_titolare") == null ? "" : (String)request.getAttribute("tx_titolare"));
		String cap = (request.getAttribute("tx_cap") == null ? "" : (String)request.getAttribute("tx_cap"));
		String indirizzo = (request.getAttribute("tx_indirizzo") == null ? "" : (String)request.getAttribute("tx_indirizzo"));
		String mail = (request.getAttribute("tx_mail") == null ? "" : (String)request.getAttribute("tx_mail"));
		String strutturaRicettiva = (request.getAttribute("strutturaRicettiva") == null ? "" : (String)request.getAttribute("strutturaRicettiva"));
		String chiaveAnagrafeStrutt = (request.getAttribute("tx_anag_struttura") == null ? "" : (String)request.getAttribute("tx_anag_struttura"));
		String codiceUtente = (request.getAttribute("tx_cutecute") == null ? "" : (String)request.getAttribute("tx_cutecute"));
		String primoAccesso = (request.getAttribute("tx_sannfacc") == null ? "Y" : (String)request.getAttribute("tx_sannfacc"));
		String codiceEnte = (request.getAttribute("tx_csancges") == null ? "" : (String)request.getAttribute("tx_csancges"));
		String impostaServizio = (request.getAttribute("tx_csancise") == null ? "" : (String)request.getAttribute("tx_csancise"));
		String codiceContrib = (request.getAttribute("tx_csanccon") == null ? "" : (String)request.getAttribute("tx_csanccon"));
		String mailPec = (request.getAttribute("tx_mailPec") == null ? "" : (String)request.getAttribute("tx_mailPec"));	//PG160040 GG 19052016

		//PG170240
		String flagSubentro = ((String)request.getAttribute("flagSubentro") == null ? "" : (String)request.getAttribute("flagSubentro"));
		Calendar dataValiditaInizio = (request.getAttribute("tx_data_da") == null ? null : (Calendar)request.getAttribute("tx_data_da"));
		Calendar dataValiditaFine = (request.getAttribute("tx_data_a") == null ? null : (Calendar)request.getAttribute("tx_data_a"));
		Calendar dataObbligoComunicazioneInizio = (request.getAttribute("tx_data_obbl_da") == null ? null : (Calendar)request.getAttribute("tx_data_obbl_da"));
		Calendar dataObbligoComunicazioneFine = (request.getAttribute("tx_data_obbl_a") == null ? null : (Calendar)request.getAttribute("tx_data_obbl_a"));
		
		String note = (request.getAttribute("tx_note") == null ? "" : (String)request.getAttribute("tx_note"));		//PG180170
		
		//PG190300_001 SB - inizio
		String numAutCorr = (request.getAttribute("tx_autorizzazione_corr") == null ? "" : (String)request.getAttribute("tx_autorizzazione_corr"));
		String telefono = (request.getAttribute("tx_telefono") == null ? "" : (String)request.getAttribute("tx_telefono"));
		Calendar dataNascitaTit = (request.getAttribute("tx_data_nascita_tit") == null ? null : (Calendar)request.getAttribute("tx_data_nascita_tit"));
		String provinciaTit = (request.getAttribute("tx_provincia_tit") == null ? "" : (String)request.getAttribute("tx_provincia_tit"));
		String comuneTit = (request.getAttribute("tx_comune_tit") == null ? "" : (String)request.getAttribute("tx_comune_tit"));
		Calendar dataNascitaGest = (request.getAttribute("tx_data_nascita_gest") == null ? null : (Calendar)request.getAttribute("tx_data_nascita_gest"));
		String provinciaGest = (request.getAttribute("tx_provincia_gest") == null ? "" : (String)request.getAttribute("tx_provincia_gest"));
		String comuneGest = (request.getAttribute("tx_comune_gest") == null ? "" : (String)request.getAttribute("tx_comune_gest"));
		String anagraficaGest = (request.getAttribute("tx_gestore") == null ? "" : (String)request.getAttribute("tx_gestore"));
		String codFiscaleGest = (request.getAttribute("tx_fiscale_gest") == null ? "" : (String)request.getAttribute("tx_fiscale_gest"));
		//PG190300_001 SB - fine
		
		//PG190330_001 SB - inizio
		String strSuperficie = request.getAttribute("tx_superficie") == null ? "" : (String) request.getAttribute("tx_superficie");
		String strPostiLetto = request.getAttribute("tx_postiLetto") == null ? "" : (String)request.getAttribute("tx_postiLetto");
		String comuneCatastale = (request.getAttribute("tx_comuneCatastale") == null ? "" : (String)request.getAttribute("tx_comuneCatastale"));
		String particellaEdificiale = (request.getAttribute("tx_particellaEdificiale") == null ? "" : (String)request.getAttribute("tx_particellaEdificiale"));
		String subalterno = (request.getAttribute("tx_subalterno") == null ? "" : (String)request.getAttribute("tx_subalterno"));
		BigDecimal superficie = (strSuperficie.trim().length() == 0 ? new BigDecimal(0.00) :  new BigDecimal(strSuperficie));
		Integer postiLetto = (strPostiLetto.trim().length() == 0 ? new Integer(0) : new Integer(strPostiLetto));
		String piano = (request.getAttribute("tx_piano") == null ? "" : (String)request.getAttribute("tx_piano"));
		//PG190330_001 SB - fine
		
		param.put("codiceProvincia", codiceProvincia);
		param.put("codiceComune", codiceComune);
		param.put("codiceAutorizzazione", codiceAutorizzazione);
		param.put("codiceFiscaleStruttura", codiceFiscaleStruttura);
		param.put("ragioneSociale", ragioneSociale);
		param.put("insegna", insegna);
		param.put("codiceFiscaleTitolare", codiceFiscaleTitolare);
		param.put("titolare", titolare);		
		param.put("cap", cap);		
		param.put("indirizzo", indirizzo);
		param.put("mail", mail);
		param.put("strutturaRicettiva", strutturaRicettiva);
		param.put("chiaveAnagrafeStrutt", chiaveAnagrafeStrutt);		
		param.put("codiceUtente", codiceUtente);		
		param.put("primoAccesso", primoAccesso.equals("")?"N":"Y");
		param.put("codiceEnte", codiceEnte);		
		param.put("impostaServizio", impostaServizio);
		param.put("codiceContrib", codiceContrib);
		param.put("mailPec", mailPec);	//PG160040 GG 19052016
		
		//PG170240
		param.put("flagSubentro", flagSubentro);
		param.put("dataValiditaInizio", dataValiditaInizio);
		param.put("dataValiditaFine", dataValiditaFine);
		param.put("dataObbligoComunicazioneInizio", dataObbligoComunicazioneInizio);
		param.put("dataObbligoComunicazioneFine", dataObbligoComunicazioneFine);
		
		param.put("note", note);	//PG180170
		
		//PG190300_001 SB - inizio
		param.put("numeroAutorizzazioneCorrelata", numAutCorr);
		param.put("telefono", telefono);
		param.put("dataNascitaTit", dataNascitaTit);
		param.put("provinciaTit", provinciaTit);
		param.put("comuneTit", comuneTit);
		param.put("dataNascitaGest", dataNascitaGest);
		param.put("provinciaGest", provinciaGest);
		param.put("comuneGest", comuneGest);
		param.put("cfGest", codFiscaleGest);
		param.put("anagraficaGest", anagraficaGest);
		//PG190300_001 SB - fine
		
		
		//PG190330 SB - inizio
		param.put("comuneCatastale", comuneCatastale);
		param.put("particellaEdificiale", particellaEdificiale);
		param.put("subalterno", subalterno);
		param.put("superficie", superficie);
		param.put("postiLetto", postiLetto);
		param.put("piano", piano);
		//PG190330 SB - fine
		
		return param;
	}

	private String checkAnagStruttura(HashMap<String,Object> parametri, HttpServletRequest request)
	{
		/*
		 * Controllo che sia stato selezionato un item dalle due drop-down-list
		 */
		//DDL Provincia
		if (((String)parametri.get("codiceProvincia")).equals(""))
			return Messages.SELEZIONARE_UN_ELEMENTO_LISTA.format("Provincia");
		//DDL Comune
		if (((String)parametri.get("codiceComune")).equals(""))
			return Messages.SELEZIONARE_UN_ELEMENTO_LISTA.format("Comune");

		/*
		 * Controllo la presenza del codice fiscale / PIVA
		 */
		if (((String)parametri.get("codiceFiscaleStruttura")).trim().length()>0){
			
			String sRes = CodiceFiscalePIVA.controlloCodiceFiscale_PartitaIva_NomeEmpty((String)parametri.get("codiceFiscaleStruttura"), "Cod.Fisc.");
			if (sRes != null && sRes.length() > 0)
				return sRes;
		}
			
		if (((String)parametri.get("codiceFiscaleTitolare")).trim().length()>0){
			String sRes = CodiceFiscalePIVA.controlloCodiceFiscale_PartitaIva_NomeEmpty((String)parametri.get("codiceFiscaleTitolare"), "Cod.Fisc.Tit.");
			if (sRes != null && sRes.length() > 0)
				return sRes;
		}
		
		//PG160040 GG 19052016 - inizio
		if ((((String)parametri.get("mail")).trim().equals("") && ((String)parametri.get("mailPec")).trim().equals(""))){
			return "Valorizzare almeno un indirizzo mail";
		}
		//PG160040 GG 19052016 - fine
				
		return "OK";
	}
	
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
		String provincia="";
		FiredButton firedButton=getFiredButton(request);
		request.setAttribute("listbelfiore", session.getAttribute("listbelfiore"));
		request.setAttribute("listbelfioreTit", session.getAttribute("listbelfioreTit"));
		request.setAttribute("listbelfioreGest", session.getAttribute("listbelfioreGest"));
		switch(firedButton) 
		{
			   case TX_BUTTON_PROVINCIA_TITOLARE_CHANGED:{
				   provincia = (request.getAttribute("tx_provincia_tit") == null ? "" : (String)request.getAttribute("tx_provincia_tit"));
				   loadBelfiore(provincia, request, session, "listbelfioreTit");
			   }break;
			   case TX_BUTTON_PROVINCIA_GESTORE_CHANGED:{
				   provincia = (request.getAttribute("tx_provincia_gest") == null ? "" : (String)request.getAttribute("tx_provincia_gest"));
				   loadBelfiore(provincia, request, session, "listbelfioreGest");
			   }break;
				default:{
					provincia = (request.getAttribute("tx_provincia") == null ? "" : (String)request.getAttribute("tx_provincia"));
					loadBelfiore(provincia, request, session, "listbelfiore");
				}
		}		
	}
	
	private void loadListaTipoStrutture(HttpServletRequest request)
	{
		RecuperaListaTipologiaStrutturaRicettivaResponse res = new RecuperaListaTipologiaStrutturaRicettivaResponse();
		RecuperaListaTipologiaStrutturaRicettivaRequest in = new RecuperaListaTipologiaStrutturaRicettivaRequest();
		in.setDescrizioneTipologiaStruttura("");
		try {
			res = WSCache.impostaSoggiornoConfigServer.ricercaTipologiaStrutture(in, request);
		} catch (com.seda.payer.impostasoggiorno.webservice.srv.FaultType e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		request.setAttribute("listStrutture", res.getListXml());
	}
	
	private String[] getUtentiPerAutorizzazione(String listXml, AnagraficaStrutturaRicettiva anagraficaStrutturaRicettiva)
	{
		String[] utente = new String[5];	
		int contatore = 0;
		String codUtente = "";
		String codSoc = "";
		String tipologiaUtente = "";
		String attivazione = "";
		//inizio LP PG21XX04 Leak
		CachedRowSet webRowSetUtenti = null;
		//fine LP PG21XX04 Leak
		try
		{
			//inizio LP PG21XX04 Leak
			//CachedRowSet webRowSetUtenti = Convert.stringToWebRowSet(listXml);
			webRowSetUtenti = Convert.stringToWebRowSet(listXml);
			//fine LP PG21XX04 Leak
			if (webRowSetUtenti != null) 
			{
				while (webRowSetUtenti.next()) {
					String codBelfSedeLegale = webRowSetUtenti.getString(9)==null?"":webRowSetUtenti.getString(9).trim();
					if (anagraficaStrutturaRicettiva.getCodiceBelfiore().trim().equals(codBelfSedeLegale))
					{
						contatore++;
						codUtente = webRowSetUtenti.getString(5);
						codSoc = webRowSetUtenti.getString(12);
						tipologiaUtente = webRowSetUtenti.getString(11);
						attivazione = webRowSetUtenti.getString(4);
					}
				}
				utente[0] = String.valueOf(contatore);
				utente[1] = codUtente;
				utente[2] = codSoc;
				utente[3] = tipologiaUtente;
				utente[4] = attivazione;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			try {
				if(webRowSetUtenti != null) {
					webRowSetUtenti.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return utente;
	}
	
	UtentePIVA getUtentePIVA (AnagraficaStrutturaRicettiva anagraficaStrutturaRicettiva, HttpServletRequest request, String username,String tipologiaUtente, String attivazione) {
		UtentePIVA utentePIVA = null;
		DatiGeneraliPIVA datiGeneraliPIVA = new DatiGeneraliPIVA(anagraficaStrutturaRicettiva.getPartitaIVAStruttura(), anagraficaStrutturaRicettiva.getRagioneSocialeStruttura(), null, null);
		SedeLegale sedeLegale = new SedeLegale(anagraficaStrutturaRicettiva.getIndirizzoStruttura(), null, null, anagraficaStrutturaRicettiva.getCapStruttura(), null);
		DatiUtentePIVA datiUtentePIVA = new DatiUtentePIVA(datiGeneraliPIVA, sedeLegale, null, null);
		DatiGeneraliPersonaFisicaDelegato datiGeneraliPersonaFisicaDelegato = new DatiGeneraliPersonaFisicaDelegato();
		datiGeneraliPersonaFisicaDelegato.setCodiceFiscale(anagraficaStrutturaRicettiva.getCodFiscaleTitolare());
		DatiContattiPersonaFisicaDelegato datiContattiPersonaFisicaDelegato = new DatiContattiPersonaFisicaDelegato();
		String mail = anagraficaStrutturaRicettiva.getEmail()==null?"":anagraficaStrutturaRicettiva.getEmail().trim();
		if (mail.length() > 50) 
			mail = mail.substring(0, 50);
		datiContattiPersonaFisicaDelegato.setMail(mail);
		String mailPec = anagraficaStrutturaRicettiva.getEmailPec()==null?"":anagraficaStrutturaRicettiva.getEmailPec().trim();
		if (mailPec.length() > 50) 
			mailPec = mailPec.substring(0, 50);
		datiContattiPersonaFisicaDelegato.setPec(mailPec);
		DatiAnagPersonaFisicaDelegato datiAnagPersonaFisicaDelegato = new DatiAnagPersonaFisicaDelegato();
		datiAnagPersonaFisicaDelegato.setDatiGeneraliPersonaFisicaDelegato(datiGeneraliPersonaFisicaDelegato);
		datiAnagPersonaFisicaDelegato.setDatiContattiPersonaFisicaDelegato(datiContattiPersonaFisicaDelegato);
		//UtentePIVA utentePIVA = new UtentePIVA(username, tipologiaUtente, utenzaAttiva, dataInizioValiditaUtenza, dataFineValiditaUtenza, primoAccesso, dataScadenzaPassword, dataUltimoAccesso, dataInserimentoUtenza, note, dataUltimoAggiornamento, operatoreUltimoAggiornamento, datiUtentePIVA, datiAnagPersonaFisicaDelegato)
		utentePIVA = new UtentePIVA(username, UtentePIVATipologiaUtente.fromString(tipologiaUtente), UtentePIVAUtenzaAttiva.fromString(attivazione), null, null, null, null, null, null, null, null, 
				anagraficaStrutturaRicettiva.getOperatoreUltimoAggiornamento(),
				datiUtentePIVA, datiAnagPersonaFisicaDelegato);
		return utentePIVA;
	}
	
}
