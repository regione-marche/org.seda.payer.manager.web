package org.seda.payer.manager.actions;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.adminusers.util.Error;
import org.seda.payer.manager.adminusers.util.Profilo;
import org.seda.payer.manager.adminusers.util.ProfiloUtil;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.CodiceFiscalePIVA;
import org.seda.payer.manager.util.Crypto;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.UCheckDigit;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.security.ChryptoServiceException;
import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.application.ApplicationsData;
import com.seda.j2ee5.maf.core.security.RealmProfilesManager;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.payer.pgec.webservice.adminusers.dati.PyUserType;
import com.seda.payer.pgec.webservice.adminusers.dati.SelezionaUtenteResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipologiaServizioXml_DDL_2RequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipologiaServizioXml_DDL_2ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
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
import com.seda.security.webservice.dati.SedeLegale;
import com.seda.security.webservice.dati.SedeLegaleEsteroSedeLegale;
import com.seda.security.webservice.dati.SedeOperativa;
import com.seda.security.webservice.dati.SelezionaUtentePIVARequestType;
import com.seda.security.webservice.dati.SelezionaUtentePIVAResponseType;
import com.seda.security.webservice.dati.UtentePIVA;
import com.seda.security.webservice.dati.UtentePIVAFlagOperatoreBackOffice;
import com.seda.security.webservice.dati.UtentePIVAPrimoAccesso;
import com.seda.security.webservice.dati.UtentePIVATipologiaUtente;
import com.seda.security.webservice.dati.UtentePIVAUtenzaAttiva;
import com.seda.tag.core.DdlOption;

@SuppressWarnings("serial")
public class BaseAdminusersAction extends BaseManagerAction{

	protected String userProfile = null;
	protected String codiceSocieta = null;
	protected String codiceUtente = null;
	protected String codiceEnte = null;
	protected String tipologiaservizio = null;
	protected String tipologiaserviziosel = null;
	protected Long chiaveUtente = null;
	protected String username = null;
	protected String password = null;
	protected String tipologiaUtente = null;
	protected String cognome = null;
	protected String nome = null;
	protected String codiceFiscale = null;
	protected String emailNotifiche = null;
	protected String smsNotifiche = null;
	protected String note = null;
	protected String passwordAutogenerata = null;
	protected String nessunaScadenza = null;
	protected String utenzaAttiva = null;
	protected String downloadFlussiRendicontazione = null;
	protected String invioFlussiRendicontazioneViaFtp = null;
	protected String invioFlussiRendicontazioneViaEmail = null;
	protected String invioFlussiRendicontazioneViaWs = null;
	protected String azioniPerTransazioniOK = null;
	protected String azioniPerTransazioniKO = null;
	protected String azioniPerRiconciliazioneManuale = null;
	protected String attivazioneEstrattoContoManager = null;
	protected String abilitazioneProfiloRiversamento = null;
	protected String entePertinenza = null;	//EP160150_001 GG 03112016
	
//dom R
	protected String mailContogestione = null;
	
	protected String abilitazioneMultiUtente = null;
	protected String configurazione = null;
	protected String adminusers = null;
	protected String ecmanager = null;
	//inizio LP PG200360
	protected String serviziattivi = null;
	//fine LP PG200360
	protected String ecanagrafica = null;
	protected String ecuffmanager = null;
	protected String ecnotifiche = null;
	protected String ecagemanager = null; //PG180300 20190325 CT
	protected String eccedenze = null;
	protected String monitoraggioext = null;
	//PG150180_001 GG - inizio
	protected String monitoraggionn = null;
	protected String riconciliazionenn = null;
	//PG150180_001 GG - fine
	protected String analysis = null;
	protected String log = null;
	//inizio LP PG21X007
	protected String logrequest = null;
	//fine LP PG21X007
	protected String monitoraggio = null;
	protected String rendicontazione = null;
	protected String riconciliazione = null;
	protected String riversamento = null;
	protected String ottico = null;
	protected String entrate = null;
// dom R
	protected String contogestione = null;

	protected String ruoli = null;
	protected String wismanager = null;
	protected String wisconfig = null;
	protected String monitoraggiowis = null;
	
	
	protected String walletmanager = null;
	protected String walletmonitoraggio = null;
	protected String walletanagraficacontribuenti = null;
	protected String walletricaricheborsellino = null;
	protected String walletsollecitodiscarico = null;
	protected String walletservizio = null;
	protected String walletconfig = null;
	protected String blackboxconfig = null;
	protected String modello3config = null;
	protected String mercatoconfig = null;
	protected String mercatimanager = null;
	protected String monitoraggiomercati = null;
	protected String monitoraggiocup = null;
	protected String monitoraggiosoap = null;
	protected String monitoraggiocruss = null;
	
	
	protected Calendar dataFineValiditaUtenza = null;
	protected List<String> listaApplicazioni = null;

	private HashMap<String, String> DDLTipologieServizio = null;
	private HashMap<String, String> DDLTipologieServizioSel = null;
	private String codicetipologiaservizio; 
	private String descrizionetipologiaservizio; 
	protected String listatipologiaservizio=null;

	//PG180010 - inizio
	protected String riconciliazionemt = null;
	protected String associazioniProvvisorieRiconciliazionemt = null;
	protected String associazioniDefinitiveRiconciliazionemt = null;
	//PG180010 - fine
	
	protected String codiceGruppoAgenzia = "";  //RE180181 SB
	
	protected String gestioneuffici = ""; //SVILUPPO_001_LUCAP_30062020
	protected String ioitalia = ""; // PG210160
	
	//PG200120
	protected String blackboxpos = ""; 
	protected String blackboxposlog = "";
	//FINE PG200120

	// inizio SR PGNTMGR-56
	protected String prenotazioneFatturazione = null;
	protected String richiesteelaborazioni = null;
	// fine SR PGNTMGR-56

	protected String inviaufficio=null;

	/**
	 * Recupera la lista delle Tipologie di Servizio (codice, descrizione) ---
	 * Le cerca in sessione e, se non ci sono le carica tramite il WS e poi le
	 * mette in sessione sotto forma di HashMap--- "
	 * 
	 * @param request
	 * @param session
	 * @param codiceSocieta
	 * @param forceReload
	 */
	@SuppressWarnings("unchecked")
	protected void loadTipologiaServizioXml_DDL_3(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String codiceUtente, String chiaveEnte, boolean forceReload) {
		GetListaTipologiaServizioXml_DDL_2RequestType in = null;
		GetListaTipologiaServizioXml_DDL_2ResponseType res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "listaTipologieServizio";
		String lista2 = "listaTipologieServizioSel";
		String tipservizioselect = "";
				
		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload && session.getAttribute(lista2) != null) {
			
			DDLTipologieServizio= (HashMap<String, String>) session.getAttribute(lista);
			DDLTipologieServizioSel= (HashMap<String, String>) session.getAttribute(lista2);
			//request.setAttribute(lista, (HashMap<String, String>) session.getAttribute(lista));
			//request.setAttribute(lista2, (HashMap<String, String>) session.getAttribute(lista2));
			
			listTipologieServizio(request, DDLTipologieServizio, DDLTipologieServizioSel);
		} else {
			try {
				in = new GetListaTipologiaServizioXml_DDL_2RequestType();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceUtente(codiceUtente);
				in.setChiaveEnte(chiaveEnte);
				
				in.setListaTipologiaServizio("");
				
				res = WSCache.commonsServer.getListaTipologiaServizioXml_DDL_2(in, request);
				
				response = res.getResponse();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getXml();
					//request.setAttribute(lista, xml);
					DDLTipologieServizio = getCreateHashMap(xml);
					session.setAttribute(lista, DDLTipologieServizio);	
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento delle Tipologie Servizio: "
							+ response.getRetMessage());
				}
				
				// valorizzo il secondo HashMap.
				
				if (request.getAttribute("tx_tipologia_servizio_hidden") != null) {
					in = new GetListaTipologiaServizioXml_DDL_2RequestType();
					in.setCodiceSocieta(codiceSocieta);
					in.setCodiceUtente(codiceUtente);
					in.setChiaveEnte(chiaveEnte);
					tipservizioselect=formattaStringaTipologieServizio((String)request.getAttribute("tx_tipologia_servizio_hidden"));
					in.setListaTipologiaServizio(tipservizioselect);
				
					res = WSCache.commonsServer.getListaTipologiaServizioXml_DDL_2(in, request);
				
					response = res.getResponse();
					if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
						xml = res.getXml();
					
						DDLTipologieServizioSel = getCreateHashMap_2(xml);
						if (!DDLTipologieServizioSel.isEmpty()) {
							DDLTipologieServizio = matchDDL1DDL2(DDLTipologieServizio, DDLTipologieServizioSel);
							session.setAttribute(lista, DDLTipologieServizio);
						}			
					} else {
						session.setAttribute(lista, null);
						setErrorMessage("Errore nel caricamento delle Tipologie Servizio Selezionate: "
								+ response.getRetMessage());
					}
				} else { 
					DDLTipologieServizioSel = getCreateHashMap_2(null);
				}
				session.setAttribute(lista2, DDLTipologieServizioSel);
				
				listTipologieServizio(request, DDLTipologieServizio, DDLTipologieServizioSel);
				
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Servizio: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Servizio: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<String, String> matchDDL1DDL2(
			HashMap<String, String> DDLTipologieServizio,
			HashMap<String, String> DDLTipologieServizioSel) {
		// 
		Iterator iteratorsel = DDLTipologieServizioSel.keySet().iterator();
		String key="";
			
		while(iteratorsel.hasNext()){
		   	key=(String) iteratorsel.next();
			if (DDLTipologieServizio.containsKey(key))
				DDLTipologieServizio.remove(key);
		}
		
		return DDLTipologieServizio;
	}

	private String formattaStringaTipologieServizio(String servizio) {
		// questo metodo serve per formattare la stringa di tipologie servizio che mi arriva dal DB (AAA|YYY|ZZZ) nel formato
		// 'AAA','YYY','ZZZ'
		
		String listaTipologieServizio="";
		String[] list = (servizio.split("\\|"));
		if(list.length > 0)
		{
			ArrayList<String> listaTipologiaServizio = new ArrayList<String>();
			for(int i=0; i<list.length; i++)
			{
				listaTipologiaServizio.add(list[i]);
			}
		
			if (listaTipologiaServizio != null) {
				for (String tipserv : listaTipologiaServizio) {
					listaTipologieServizio+="'" + tipserv + "',";
				}	
			}
		}
		if (listaTipologieServizio.equals(""))
			return listaTipologieServizio;
		else
			return listaTipologieServizio.substring(0, listaTipologieServizio.length()-1);
	}

	@SuppressWarnings("unchecked")
	protected void listTipologieServizio(HttpServletRequest request,
			HashMap<String, String> DDLTipologieServizio, HashMap<String, String> DDLTipologieServizioSel) {
			
			List<DdlOption> optionList  = new ArrayList<DdlOption>();
			List<DdlOption> optionListSel  = new ArrayList<DdlOption>();

			try {
				//ArrayList<TabellaParametriCMU> listMutCombo = (ArrayList<TabellaParametriCMU>)catastoBD.listCausaliMutConce(tabellaParametriCMATO);
				
				String key="";
				String value="";
				
				if (DDLTipologieServizio != null) {
						
					Iterator iterator = DDLTipologieServizio.keySet().iterator();
	
				    while(iterator.hasNext()){
				    	key=(String) iterator.next();
				    	value=DDLTipologieServizio.get(key);
			        
				    	DdlOption option = new DdlOption();
				    	option.setSValue(key);
				    	option.setSText(value);
				    	optionList.add(option);      
			        }
				}
		        
				if (DDLTipologieServizioSel != null) {
				   Iterator iteratorsel = DDLTipologieServizioSel.keySet().iterator();
		      
		    	   while(iteratorsel.hasNext()){
			        	key=(String) iteratorsel.next();
			        	value=DDLTipologieServizioSel.get(key);
			        
			        	DdlOption option = new DdlOption();
						option.setSValue(key);
						option.setSText(value);
						optionListSel.add(option);      
			       }
		       	}
		       	
			}catch (Exception e) {
				setErrorMessage("1Errore nel caricamento delle Tipologie Servizio: FaultType - "
						+ e.getLocalizedMessage());
			}
			//request.setAttribute(Attributes.LIST_CAUSALI_MUT, optionList);	
			request.setAttribute("listTipologieServizio", optionList);
			request.setAttribute("listTipologieServizioSel", optionListSel);
		}

	
	/**
	 * Aggiunge una tipologia servizio dalla prima DDL alla seconda DDL.  
	 * 					
	 * @param request
	 * @param session
	 */
	@SuppressWarnings("unchecked")
	protected void addTipologiaServizio(HttpServletRequest request,
			HttpSession session) {
		try
		{
			String lista = "listaTipologieServizio";
			String lista2 = "listaTipologieServizioSel";
			
			Object tipologieServizio;
			String[] tipologieselezionate=null;
			
			tipologieServizio=request.getAttribute("tx_tipologia_servizio");
			
			if (tipologieServizio!=null){
				if (tipologieServizio instanceof String) {
					tipologieselezionate=new String[1];
					tipologieselezionate[0] = (String) request.getAttribute("tx_tipologia_servizio");
				} else 
					tipologieselezionate = (String[]) request.getAttribute("tx_tipologia_servizio");
			}
			
			
			HashMap<String, String> DDLTipologieServizio= (HashMap<String, String>) session.getAttribute(lista);
			HashMap<String, String> DDLTipologieServizioSel= (HashMap<String, String>) session.getAttribute(lista2);
			/*
			 * Cerco la stringa XML in sessions
			 */
			if (session.getAttribute(lista) != null &&  tipologieselezionate!= null) {
				
				for (String tipologiaselezionata : tipologieselezionate) {
					String tiposerv = DDLTipologieServizio.remove(tipologiaselezionata);
					DDLTipologieServizioSel.put(tipologiaselezionata, tiposerv);
				}
				
				// salvo il valore delle due HashMap nella Session
				session.setAttribute(lista, DDLTipologieServizio);
				session.setAttribute(lista2, DDLTipologieServizioSel);
				// richiamo il metodo che crea le due dropdownlist.
				listTipologieServizio(request, DDLTipologieServizio, DDLTipologieServizioSel);
			} else {
				listTipologieServizio(request, DDLTipologieServizio, DDLTipologieServizioSel);
				setErrorMessage("Errore: Selezionare tipologia servizio da aggiungere");
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("errore = " + e.getMessage());
		}
		
	}
	/**
	 * Rimuove una tipologia servizio dalla prima DDL alla seconda DDL.  
	 * 					
	 * @param request
	 * @param session
	 */
	@SuppressWarnings("unchecked")
	protected void removeTipologiaServizio(HttpServletRequest request,
			HttpSession session) {
		try
		{
			String lista = "listaTipologieServizio";
			String lista2 = "listaTipologieServizioSel";
			
			Object tipologieServizio;
			String[] tipologieselezionatesel=null;
			
			tipologieServizio=request.getAttribute("tx_tipologia_servizio_sel");
			
			if (tipologieServizio!=null){
				if (tipologieServizio instanceof String) {
					tipologieselezionatesel=new String[1];
					tipologieselezionatesel[0] = (String) request.getAttribute("tx_tipologia_servizio_sel");
				}	else
					tipologieselezionatesel = (String[]) request.getAttribute("tx_tipologia_servizio_sel");
			}
			
			HashMap<String, String> DDLTipologieServizio= (HashMap<String, String>) session.getAttribute(lista);
			HashMap<String, String> DDLTipologieServizioSel= (HashMap<String, String>) session.getAttribute(lista2);
			/*
			 * Cerco la stringa XML in sessions
			 */
			if (session.getAttribute(lista2) != null &&  tipologieselezionatesel != null) {
				
				for (String tipologiaselezionata : tipologieselezionatesel) {
					String tiposerv = DDLTipologieServizioSel.remove(tipologiaselezionata);
					DDLTipologieServizio.put(tipologiaselezionata, tiposerv);
				}
				// salvo il valore delle due HashMap nella Session
				session.setAttribute(lista, DDLTipologieServizio);
				session.setAttribute(lista2, DDLTipologieServizioSel);
				// richiamo il metodo che crea le due dropdownlist.
				listTipologieServizio(request, DDLTipologieServizio, DDLTipologieServizioSel);
			} else {
				listTipologieServizio(request, DDLTipologieServizio, DDLTipologieServizioSel);
				setErrorMessage("Errore: Selezionare tipologia servizio da rimuovere");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("errore = " + e.getMessage());
			// TODO: handle exception
		}
		
	}

	/**
	 * Questo metodo recupera, per il primo step, i parametri di input dalla form utilizzate per
	 * l'inserimento di un nuovo utente (codop="add") o per la modifica
	 * di un utente già esistente (codop="edit")
	 * 
	 * NOTA: Nel caso di inserimento di un nuovo utente la password viene crittografata.
	 * @param request
	 * @param codop
	 * @throws ActionException 
	 */
	protected void getForm1Parameters(HttpServletRequest request, String codop)
	{
		
/*		String param_userProfile = (codop.equals("edit") ? "tx_userprofile_hidden" : "tx_userprofile");
		String param_codiceSocieta = (codop.equals("edit") ? "tx_societa_hidden" : "tx_societa");
		String param_codiceUtente = (codop.equals("edit") ? "tx_utente_hidden" : "tx_utente");
		String param_codiceEnte = (codop.equals("edit") ? "tx_ente_hidden" : "tx_ente");
*/
		/*
		 * VB - 4 Agosto 2011
		 * Modificato rispetto a sopra a fronte della richiesta di Lepida di rendere
		 * modificabile il profilo utente
		 */
		String param_userProfile =  "tx_userprofile";
		String param_codiceSocieta =  "tx_societa";
		String param_codiceUtente = "tx_utente";
		String param_codiceEnte =  "tx_ente";
		String param_entePertinenza = "tx_pertinenza";	//EP160510_001 GG 03112016
		String param_gruppoAgenzia = "tx_gruppo_agenzia"; //RE180181 SB
		
		String param_username = (codop.equals("edit") ? "tx_username_hidden" : "tx_username");
		
		if (codop.equals("edit"))
			chiaveUtente = Long.parseLong(request.getParameter("tx_chiaveutente_hidden"));

		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		String usernameAtr = (String)request.getAttribute("tx_username"); 
		//fine LP PG200060
		
		
		userProfile = (request.getParameter(param_userProfile) == null ? "" : request.getParameter(param_userProfile));
		codiceSocieta = (request.getParameter(param_codiceSocieta) == null ? "" : request.getParameter(param_codiceSocieta));
		codiceUtente = (request.getParameter(param_codiceUtente) == null ? "" : request.getParameter(param_codiceUtente));
		codiceEnte = (request.getParameter(param_codiceEnte) == null ? "" : request.getParameter(param_codiceEnte));
		username = (request.getParameter(param_username) == null ? "" : request.getParameter(param_username));
		if(username.equals(""))
				username = (request.getAttribute(param_username) == null ? "" : (String)request.getAttribute(param_username));
		//inizio LP PG200060
		if(template.equals("regmarche")) {
			if(username == null || username.trim().equals("")) {
				username = usernameAtr;
			}
		}
		//fine LP PG200060
		utenzaAttiva =  ( request.getParameter("utenzaAttiva") == null ? "N" : "Y" );
		abilitazioneMultiUtente =  ( request.getParameter("abilitazioneMultiUtente") == null ? "N" : "Y" );
		codiceFiscale = (request.getParameter("tx_codiceFiscale") == null ? "" : request.getParameter("tx_codiceFiscale"));
		tipologiaservizio = (request.getParameter("tx_tipologia_servizio") == null ? "" : request.getParameter("tx_tipologia_servizio"));
		tipologiaserviziosel = (request.getParameter("tx_tipologia_servizio_sel") == null ? "" : request.getParameter("tx_tipologia_servizio_sel"));
		codiceGruppoAgenzia = (request.getParameter(param_gruppoAgenzia)==null ? "" : request.getParameter(param_gruppoAgenzia)); //RE180181 SB
		
		//entePertinenza = (request.getParameter(param_entePertinenza) == null ? "" : request.getParameter(param_entePertinenza));	//EP160510_001 GG 03112016
		entePertinenza = request.getParameter(param_entePertinenza); //EP160510_001 GG 10112016 //In sostituzione della precedente qualora template differenti possano condividere le utenze per evitare sovrascrittura del codice ente di pertinenza in aggiornamento utenza da template differente come in sviluppo
	}

	protected void setAllForm2FlagForAMMI(HttpServletRequest request)
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060

		downloadFlussiRendicontazione =  "Y";
		invioFlussiRendicontazioneViaFtp = "Y";
		invioFlussiRendicontazioneViaEmail = "Y";
		invioFlussiRendicontazioneViaWs = "Y";
		azioniPerTransazioniOK = "Y";
		azioniPerTransazioniKO = "Y";
		azioniPerRiconciliazioneManuale = "Y";
		attivazioneEstrattoContoManager = "Y";
		abilitazioneProfiloRiversamento = "Y";
		prenotazioneFatturazione = "Y";
		//dom R
		mailContogestione = "Y";
		
		listaApplicazioni = new Vector<String>();
		configurazione = "Y";
		listaApplicazioni.add("configurazione");
		adminusers =  "Y";
		listaApplicazioni.add("adminusers");
		ecmanager =  "Y";
		listaApplicazioni.add("ecmanager");

		richiesteelaborazioni =  "Y";
		listaApplicazioni.add("richiesteelaborazioni");

		inviaufficio="Y";
		listaApplicazioni.add("inviaufficio");

		//inizio LP PG200360
		if(serviziAttiviOn(request)) {
			serviziattivi = "Y";
			listaApplicazioni.add("serviziattivi");
		}
		//fine LP PG200360
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
			//fine LP PG200060
			ecanagrafica =  "Y";
			listaApplicazioni.add("ecanagrafica");
			ecuffmanager =  "Y";
			listaApplicazioni.add("ecuffmanager");
			ecnotifiche =  "Y";
			listaApplicazioni.add("ecnotifiche");
			ecagemanager =  "Y";					//PG180300 20190325 CT
			listaApplicazioni.add("ecagemanager");	//PG180300 20190325 CT
			//inizio LP PG200060
		}
		//fine LP PG200060
		monitoraggio =  "Y";
		listaApplicazioni.add("monitoraggio");
		log =  "Y";
		listaApplicazioni.add("log");
		//inizio LP PG21X007
		if(logRequestOn(request)) {
			logrequest = "Y";
			listaApplicazioni.add("logrequest");
		}
		//fine LP PG21X007
		rendicontazione =  "Y";
		listaApplicazioni.add("rendicontazione");
		riconciliazione =  "Y";
		listaApplicazioni.add("riconciliazione");
		riversamento =  "Y";
		listaApplicazioni.add("riversamento");
		eccedenze = "Y";
		listaApplicazioni.add("eccedenze");
		monitoraggioext = "Y";
		listaApplicazioni.add("monitoraggioext");
		//PG150180_001 GG - inizio
		monitoraggionn = "Y";
		listaApplicazioni.add("monitoraggionn");
		riconciliazionenn = "Y";
		listaApplicazioni.add("riconciliazionenn");
		//PG150180_001 GG - fine
		analysis = "Y";
		listaApplicazioni.add("analysis");
		ottico = "Y";
		listaApplicazioni.add("ottico");
		entrate = "Y";
		listaApplicazioni.add("entrate");
// dom R
		contogestione = "Y";
		listaApplicazioni.add("contogestione");

		ruoli = "Y";
		listaApplicazioni.add("ruoli");
	
		modello3config= "Y";
		listaApplicazioni.add("modello3config");
		
		if(!template.equals("aosta")) {

			wismanager = "Y";
			listaApplicazioni.add("wismanager");
			wisconfig = "Y";
			listaApplicazioni.add("wisconfig");
			monitoraggiowis = "Y";
			listaApplicazioni.add("monitoraggiowis");

			blackboxconfig= "Y";
			listaApplicazioni.add("blackboxconfig");
			
			//PG200120
			blackboxpos = "Y";
			listaApplicazioni.add("blackboxpos");
			
			blackboxposlog = "Y";
			listaApplicazioni.add("blackboxposlog");
			//FINE PG200120
		}
		//inizio LP PG200420 - 20210423
		else {
			blackboxconfig= "Y";
			listaApplicazioni.add("blackboxconfig");
		}
		//fine LP PG200420 - 20210423
		
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		if(!template.equals("aosta")) {
			walletmanager = "Y";
			listaApplicazioni.add("walletmanager");
			walletmonitoraggio = "Y";
			listaApplicazioni.add("walletmonitoraggio");
			walletanagraficacontribuenti = "Y";
			listaApplicazioni.add("walletanagraficacontribuenti");
			
			walletricaricheborsellino = "Y";
			listaApplicazioni.add("walletricaricheborsellino");
			
			walletsollecitodiscarico = "Y";
			listaApplicazioni.add("walletsollecitodiscarico");
			
			
			walletservizio = "Y";
			listaApplicazioni.add("walletservizio");
			walletconfig= "Y";
			listaApplicazioni.add("walletconfig");
		
		}
		//PG180040 - Mercati
		mercatoconfig= "Y";
		listaApplicazioni.add("mercatoconfig");
		mercatimanager= "Y";
		listaApplicazioni.add("mercatimanager");
		monitoraggiomercati= "Y";
		listaApplicazioni.add("monitoraggiomercati");
		
		//PG180010 - inizio
		riconciliazionemt ="Y";
		associazioniProvvisorieRiconciliazionemt = "Y";
		associazioniDefinitiveRiconciliazionemt ="Y";
		listaApplicazioni.add("riconciliazionemt");
		//PG180010 - fine

		}else {
			monitoraggiocup = "Y";
			listaApplicazioni.add("monitoraggiocup");
			monitoraggiocruss = "Y";
			listaApplicazioni.add("monitoraggiocruss");
			
		}
		//fine LP PG200060
		
//		YLM PG22XX07 INI
		monitoraggiosoap = "Y";
		listaApplicazioni.add("monitoraggiosoap");
//		YLM PG22XX07 FINE
		
		//inizio SVILUPPO_001_LUCAP_30062020
		gestioneuffici = "Y";
		listaApplicazioni.add("gestioneuffici");
		//fine SVILUPPO_001_LUCAP_30062020
		
		//inizio PG210160
		ioitalia = "Y";
		listaApplicazioni.add("ioitalia");
		//fine PG210160
		
		request.setAttribute("userIsAMMI", true);
	}
	
	protected void initForm2FlagForProfile(HttpServletRequest request, HttpSession session, String profile)
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		String sValue = "N";
		if (profile.toUpperCase().equals("AMMI"))
		{
			sValue = "Y";
			request.setAttribute("userIsAMMI", true);
		}
		
		downloadFlussiRendicontazione =  sValue;
		invioFlussiRendicontazioneViaFtp = sValue;
		invioFlussiRendicontazioneViaEmail = sValue;
		invioFlussiRendicontazioneViaWs = sValue;
		azioniPerTransazioniOK = sValue;
		azioniPerTransazioniKO = sValue;
		azioniPerRiconciliazioneManuale = sValue;
		attivazioneEstrattoContoManager = sValue;
		abilitazioneProfiloRiversamento = sValue;
		prenotazioneFatturazione = sValue;
		richiesteelaborazioni = sValue;
		//dom R
		mailContogestione = sValue;
		inviaufficio=sValue;
		
		configurazione = sValue;
		adminusers =  sValue;
		ecmanager =  sValue;
		//inizio LP PG200360
		if(serviziAttiviOn(request)) {
			serviziattivi = sValue;
		}
		//fine LP PG200360
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		ecanagrafica =  sValue;
		ecuffmanager =  sValue;
		ecnotifiche =  sValue;
		ecagemanager =  sValue;	//PG180300 20190325 CT
		//inizio LP PG200060
		}
		//fine LP PG200060
		monitoraggio =  sValue;
		log =  sValue;
		//inizio LP PG21X007
		if(logRequestOn(request)) {
			logrequest = sValue;
		}
		//fine LP PG21X007
		rendicontazione =  sValue;
		riconciliazione =  sValue;
		riversamento =  sValue;
		eccedenze = sValue;
		monitoraggioext = sValue;
		//PG150180_001 GG - inizio
		monitoraggionn = sValue;
		riconciliazionenn = sValue;
		//PG150180_001 GG - fine
		analysis = sValue;
		ottico = sValue;
		entrate = sValue;
//dom R
		contogestione = sValue;

		ruoli = sValue;
		wismanager = sValue;
		wisconfig = sValue;
		monitoraggiowis = sValue;
		
		blackboxconfig = sValue; //PG200140
		modello3config = sValue; //PG200140
		
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		walletmanager = sValue;
		walletmonitoraggio = sValue;
		walletanagraficacontribuenti = sValue;
		walletricaricheborsellino = sValue;
		walletsollecitodiscarico = sValue;
		walletservizio = sValue;
		walletconfig = sValue;
		//PG180040 - Mercati
		mercatoconfig = sValue;
		mercatimanager = sValue;
		monitoraggiomercati = sValue;
		
		//PG180010 -inizio
		riconciliazionemt = sValue;
		associazioniProvvisorieRiconciliazionemt = sValue;
		associazioniDefinitiveRiconciliazionemt = sValue;
		//PG180010 - fine
		//inizio LP PG200060
		}else {
			monitoraggiocruss = sValue;
			monitoraggiocup = sValue;
		}
		//fine LP PG200060
		
//		YLM PG22XX07 INI
		monitoraggiosoap = sValue; 
//		YLM PG22XX07 FINE
		
		gestioneuffici = sValue; //SVILUPPO_001_LUCAP_30062020
		
		ioitalia = sValue; // PG210160
		
		//PG200120
		blackboxpos = sValue; 
		blackboxposlog = sValue;
		//FINE PG200120
		
		if (sValue.equals("Y"))
		{
			//se le applicazioni sono tutte selezionate, le aggiungo alla listaApplicazioni
			listaApplicazioni = new Vector<String>();
			listaApplicazioni.add("configurazione");
			listaApplicazioni.add("adminusers");
			listaApplicazioni.add("ecmanager");
			//inizio LP PG200360
			if(serviziAttiviOn(request)) {
				listaApplicazioni.add("serviziattivi");
			}
			//fine LP PG200360
			//inizio LP PG200060
			if(!template.equals("regmarche")) {
			//fine LP PG200060
			listaApplicazioni.add("ecanagrafica");
			listaApplicazioni.add("ecuffmanager");
			//inizio LP PG200060
			}
			//fine LP PG200060
			listaApplicazioni.add("monitoraggio");
			listaApplicazioni.add("log");
			//inizio LP PG21X007
			if(logRequestOn(request)) {
				listaApplicazioni.add("logrequest");
			}
			//fine LP PG21X007
			listaApplicazioni.add("rendicontazione");
			listaApplicazioni.add("riconciliazione");
			listaApplicazioni.add("riversamento");
			listaApplicazioni.add("eccedenze");
			listaApplicazioni.add("monitoraggioext");
			//PG150180_001 GG - inizio
			listaApplicazioni.add("monitoraggionn");
			listaApplicazioni.add("riconciliazionenn");
			//PG150180_001 GG - fine
			listaApplicazioni.add("analysis");
			listaApplicazioni.add("ottico");
			listaApplicazioni.add("entrate");
//dom R
			listaApplicazioni.add("contogestione");

			listaApplicazioni.add("ruoli");
			listaApplicazioni.add("wismanager");
			listaApplicazioni.add("wisconfig");
			listaApplicazioni.add("monitoraggiowis");
			listaApplicazioni.add("modello3config"); //PG200140
			listaApplicazioni.add("blackbox"); //PG200140
			//inizio LP PG200060
			if(!template.equals("regmarche")) {
			//fine LP PG200060
			listaApplicazioni.add("walletmanager");
			listaApplicazioni.add("walletmonitoraggio");
			listaApplicazioni.add("walletanagraficacontribuenti");
			listaApplicazioni.add("walletricaricheborsellino");
			listaApplicazioni.add("walletsollecitodiscarico");
			listaApplicazioni.add("walletservizio");
			listaApplicazioni.add("walletconfig");
			listaApplicazioni.add("mercatoconfig");		//PG180040
			listaApplicazioni.add("mercatimanager");	//PG180040
			listaApplicazioni.add("monitoraggiomercati"); //PG180040
			listaApplicazioni.add("riconciliazionemt");//PG180010 
			
			//inizio LP PG200060
			}
			//fine LP PG200060
			listaApplicazioni.add("gestioneuffici"); //SVILUPPO_001_LUCAP_30062020
			//PG200120
			listaApplicazioni.add("blackboxpos");
			listaApplicazioni.add("blackboxposlog");

			//FINE PG200120
			listaApplicazioni.add("richiesteelaborazioni");
			listaApplicazioni.add("inviaufficio");
		}
			
		if (session != null)
			putAdd2ParametersInSession(request, session);
	}
	
	/**
	 * Questo metodo recupera, per il secondo step, i parametri di input dalle form utilizzate per
	 * l'inserimento di un nuovo utente o per la modifica
	 * di un utente già esistente.
	 * 
	 * NOTA: Se il profilo dell'utente che si sta inserendo/modificando è "AMMI" vengono settate tutte
	 * le applicazioni e tutti i flag e viene settato l'attributo "userIsAMMI" a "true" per disabilitare
	 * tutte le check-box e gli script
	 * 
	 * @param request
	 */
	protected void getForm2Parameters(HttpServletRequest request,HttpSession session) 
	{
		//inizio LP PG200060
		
		Enumeration paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
		  String paramName = (String)paramNames.nextElement();
		  System.out.print("<b>" + paramName + ":</b>\n");
		  String paramValue = request.getHeader(paramName);
		  System.out.println(paramValue);
		}
		
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		String profilo = (String)session.getAttribute("userAdd_userProfile");
		if (profilo != null && profilo.equals("AMMI"))
			setAllForm2FlagForAMMI(request);
		else
		{
			downloadFlussiRendicontazione =  ( request.getParameter("downloadFlussiRendicontazione") == null ? "N" : "Y" );
			invioFlussiRendicontazioneViaFtp =  ( request.getParameter("invioFlussiRendicontazioneViaFtp") == null ? "N" : "Y" );
			invioFlussiRendicontazioneViaEmail =  ( request.getParameter("invioFlussiRendicontazioneViaEmail") == null ? "N" : "Y" );
			invioFlussiRendicontazioneViaWs =  ( request.getParameter("invioFlussiRendicontazioneViaWs") == null ? "N" : "Y" );
			azioniPerTransazioniOK =  ( request.getParameter("azioniPerTransazioniOK") == null ? "N" : "Y" );
			azioniPerTransazioniKO =  ( request.getParameter("azioniPerTransazioniKO") == null ? "N" : "Y" );
			azioniPerRiconciliazioneManuale =  ( request.getParameter("azioniPerRiconciliazioneManuale") == null ? "N" : "Y" );
			attivazioneEstrattoContoManager =  ( request.getParameter("attivazioneEstrattoContoManager") == null ? "N" : "Y" );
			abilitazioneProfiloRiversamento =  ( request.getParameter("abilitazioneProfiloRiversamento") == null ? "N" : "Y" );
			prenotazioneFatturazione =  ( request.getParameter("prenotazioneFatturazione") == null ? "N" : "Y" );
			//dom R
			mailContogestione = ( request.getParameter("mailContogestione") == null ? "N" : "Y" );
				
			listaApplicazioni = new Vector<String>();
			configurazione =  ( request.getParameter("configurazione") == null ? "N" : "Y" );
			if (configurazione.equals("Y")) listaApplicazioni.add("configurazione");
			adminusers =  ( request.getParameter("adminusers") == null ? "N" : "Y" );
			if (adminusers.equals("Y")) listaApplicazioni.add("adminusers");
			ecmanager =  ( request.getParameter("ecmanager") == null ? "N" : "Y" );
			if (ecmanager.equals("Y")) listaApplicazioni.add("ecmanager");
			//inizio LP PG200360
			if(serviziAttiviOn(request)) {
				serviziattivi = (request.getParameter("serviziattivi") == null ? "N" : "Y");
				if (serviziattivi.equals("Y")) listaApplicazioni.add("serviziattivi");
			}
			//fine LP PG200360
			//inizio LP PG200060
			if(!template.equals("regmarche")) {
			//fine LP PG200060
			ecanagrafica =  ( request.getParameter("ecanagrafica") == null ? "N" : "Y" );
			if (ecanagrafica.equals("Y")) listaApplicazioni.add("ecanagrafica");
			ecuffmanager =  ( request.getParameter("ecuffmanager") == null ? "N" : "Y" );
			if (ecuffmanager.equals("Y")) listaApplicazioni.add("ecuffmanager");
			ecnotifiche =  ( request.getParameter("ecnotifiche") == null ? "N" : "Y" );
			if (ecnotifiche.equals("Y")) listaApplicazioni.add("ecnotifiche");
			ecagemanager =  ( request.getParameter("ecagemanager") == null ? "N" : "Y" );	//PG180300 20190325 CT
			if (ecagemanager.equals("Y")) listaApplicazioni.add("ecagemanager");			//PG180300 20190325 CT
			//inizio LP PG200060
			}
			//fine LP PG200060
			monitoraggio =  ( request.getParameter("monitoraggio") == null ? "N" : "Y" );
			if (monitoraggio.equals("Y")) listaApplicazioni.add("monitoraggio");
			log =  ( request.getParameter("log") == null ? "N" : "Y" );
			if (log.equals("Y")) listaApplicazioni.add("log");
			//inizio LP PG21X007
			if(logRequestOn(request)) {
				logrequest =  ( request.getParameter("logrequest") == null ? "N" : "Y" );
				if (logrequest.equals("Y")) listaApplicazioni.add("logrequest");
			}
			//fine LP PG21X007
			rendicontazione =  ( request.getParameter("rendicontazione") == null ? "N" : "Y" );
			if (rendicontazione.equals("Y")) listaApplicazioni.add("rendicontazione");
			riconciliazione =  ( request.getParameter("riconciliazione") == null ? "N" : "Y" );
			if (riconciliazione.equals("Y")) listaApplicazioni.add("riconciliazione");
			riversamento =  ( request.getParameter("riversamento") == null ? "N" : "Y" );
			if (riversamento.equals("Y")) listaApplicazioni.add("riversamento");
			eccedenze =  ( request.getParameter("eccedenze") == null ? "N" : "Y" );
			if (eccedenze.equals("Y")) listaApplicazioni.add("eccedenze");
			monitoraggioext =  ( request.getParameter("monitoraggioext") == null ? "N" : "Y" );
			if (monitoraggioext.equals("Y")) listaApplicazioni.add("monitoraggioext");
			//PG150180_001 GG - inizio
			monitoraggionn =  ( request.getParameter("monitoraggionn") == null ? "N" : "Y" );
			if (monitoraggionn.equals("Y")) listaApplicazioni.add("monitoraggionn");
			riconciliazionenn =  ( request.getParameter("riconciliazionenn") == null ? "N" : "Y" );
			if (riconciliazionenn.equals("Y")) listaApplicazioni.add("riconciliazionenn");
			//PG150180_001 GG - fine
			analysis =  ( request.getParameter("analysis") == null ? "N" : "Y" );
			if (analysis.equals("Y")) listaApplicazioni.add("analysis");
			ottico =   ( request.getParameter("ottico") == null ? "N" : "Y" );
			if (ottico.equals("Y")) listaApplicazioni.add("ottico");
			entrate =   ( request.getParameter("entrate") == null ? "N" : "Y" );
			if (entrate.equals("Y")) listaApplicazioni.add("entrate");
// dom R
			contogestione =   ( request.getParameter("contogestione") == null ? "N" : "Y" );
			if (contogestione.equals("Y")) listaApplicazioni.add("contogestione");

			ruoli =   ( request.getParameter("ruoli") == null ? "N" : "Y" );
			if (ruoli.equals("Y")) listaApplicazioni.add("ruoli");
			
			wismanager =   ( request.getParameter("wismanager") == null ? "N" : "Y" );
			if (wismanager.equals("Y")) listaApplicazioni.add("wismanager");
			wisconfig =   ( request.getParameter("wisconfig") == null ? "N" : "Y" );
			if (wisconfig.equals("Y")) listaApplicazioni.add("wisconfig");
			monitoraggiowis = (request.getParameter("monitoraggiowis") == null ? "N" : "Y" );
			if (monitoraggiowis.equals("Y")) listaApplicazioni.add("monitoraggiowis");
			
			
			blackboxconfig =   ( request.getParameter("blackboxconfig") == null ? "N" : "Y" );
			if (blackboxconfig.equals("Y")) listaApplicazioni.add("blackboxconfig");
			
			
			modello3config =   ( request.getParameter("modello3config") == null ? "N" : "Y" );
			if (modello3config.equals("Y")) listaApplicazioni.add("modello3config");
			
			
			//inizio LP PG200060
			if(!template.equals("regmarche")) {
			//fine LP PG200060
			walletmanager =   ( request.getParameter("walletmanager") == null ? "N" : "Y" );
			if (walletmanager.equals("Y")) listaApplicazioni.add("walletmanager");
			
			walletmonitoraggio =   ( request.getParameter("walletmonitoraggio") == null ? "N" : "Y" );
			if (walletmonitoraggio.equals("Y")) listaApplicazioni.add("walletmonitoraggio");

			walletanagraficacontribuenti =   ( request.getParameter("walletanagraficacontribuenti") == null ? "N" : "Y" );
			if (walletanagraficacontribuenti.equals("Y")) listaApplicazioni.add("walletanagraficacontribuenti");
			
			walletricaricheborsellino=   ( request.getParameter("walletricaricheborsellino") == null ? "N" : "Y" );
			if (walletricaricheborsellino.equals("Y")) listaApplicazioni.add("walletricaricheborsellino");
			
			walletsollecitodiscarico=   ( request.getParameter("walletsollecitodiscarico") == null ? "N" : "Y" );
			if (walletsollecitodiscarico.equals("Y")) listaApplicazioni.add("walletsollecitodiscarico");
			
			
			walletservizio =   ( request.getParameter("walletservizio") == null ? "N" : "Y" );
			if (walletservizio.equals("Y")) listaApplicazioni.add("walletservizio");
			
			walletconfig =   ( request.getParameter("walletconfig") == null ? "N" : "Y" );
			if (walletconfig.equals("Y")) listaApplicazioni.add("walletconfig");
			
			
			
			
			//PG180040 - Mercati
			mercatoconfig = ( request.getParameter("mercatoconfig") == null ? "N" : "Y" );
			if (mercatoconfig.equals("Y")) listaApplicazioni.add("mercatoconfig");
			mercatimanager = ( request.getParameter("mercatimanager") == null ? "N" : "Y" );
			if (mercatimanager.equals("Y")) listaApplicazioni.add("mercatimanager");
			monitoraggiomercati = ( request.getParameter("monitoraggiomercati") == null ? "N" : "Y" );
			if (monitoraggiomercati.equals("Y")) listaApplicazioni.add("monitoraggiomercati");
			
			//PG180010 - inizio
			riconciliazionemt = ( request.getParameter("riconciliazionemt") == null ? "N" : "Y" );
			if (riconciliazionemt.equals("Y")) listaApplicazioni.add("riconciliazionemt");
			associazioniProvvisorieRiconciliazionemt = ( request.getParameter("associazioniProvvisorieRiconciliazionemt") == null ? "N" : "Y" );
			associazioniDefinitiveRiconciliazionemt = ( request.getParameter("associazioniDefinitiveRiconciliazionemt") == null ? "N" : "Y" );
			//inizio LP PG200060
			}else {
				monitoraggiocup =   ( request.getParameter("monitoraggiocup") == null ? "N" : "Y" );
				if (monitoraggiocup.equals("Y")) listaApplicazioni.add("monitoraggiocup");
				monitoraggiocruss =   ( request.getParameter("monitoraggiocruss") == null ? "N" : "Y" );
				if (monitoraggiocruss.equals("Y")) listaApplicazioni.add("modello3config");
			}
			//fine LP PG200060
			
//			YLM PG22XX07 INI
			monitoraggiosoap =   ( request.getParameter("monitoraggiosoap") == null ? "N" : "Y" );
			if (monitoraggiosoap.equals("Y")) listaApplicazioni.add("monitoraggiosoap");
//			YLM PG22XX07 FINE
			
			
			//inizio SVILUPPO_001_LUCAP_30062020
			gestioneuffici = ( request.getParameter("gestioneuffici") == null ? "N" : "Y" );
			if (gestioneuffici.equals("Y")) listaApplicazioni.add("gestioneuffici");
			//fine SVILUPPO_001_LUCAP_30062020
			
			//inizio PG210160
			ioitalia = ( request.getParameter("ioitalia") == null ? "N" : "Y" );
			if (ioitalia.equals("Y")) listaApplicazioni.add("ioitalia");
			//fine PG210160
			
			//PG200120
			blackboxpos = ( request.getParameter("blackboxpos") == null ? "N" : "Y" );
			if (blackboxpos.equals("Y")) listaApplicazioni.add("blackboxpos");
			
			blackboxposlog = ( request.getParameter("blackboxposlog") == null ? "N" : "Y" );
			if (blackboxposlog.equals("Y")) listaApplicazioni.add("blackboxposlog");
			//FINE PG200120

			richiesteelaborazioni =  ( request.getParameter("richiesteelaborazioni") == null ? "N" : "Y" );
			if (richiesteelaborazioni.equals("Y")) listaApplicazioni.add("richiesteelaborazioni");

			inviaufficio = ( request.getParameter("inviaufficio")== null ? "N" : "Y");
			if(inviaufficio.equals("Y")) listaApplicazioni.add("inviaufficio");

			request.setAttribute("userIsAMMI", false);
		}
	}
	
	/**
	 * Questo metodo effettua una validazione applicativa dei
	 * campi di input per la modifica e l'inserimento di un utente
	 * @param parametri
	 * @return
	 */
	protected String checkSeUsersInputFields(HashMap<String,Object> parametri,String codop)
	{
		Calendar now = Calendar.getInstance();

		try 
		{
			String password = (String)parametri.get("password");
		if (codop.equals("add") && 
				!((String)parametri.get("passwordAutogenerata")).equals("Y") &&
				password.equals("") )
			return Messages.SPECIFICARE_PASSWORD.format();
		
		if (codop.equals("add") && 
				!((String)parametri.get("passwordAutogenerata")).equals("Y") &&
				!verificaComplessitaPWD(password) )
			return Messages.SPECIFICARE_PASSWORD_VALIDA.format(".,!#%");

		/*
		 * CRITTOGRAFO LA PASSWORD
		 */
		if (codop.equals("add") && 
				!((String)parametri.get("passwordAutogenerata")).equals("Y") &&
				verificaComplessitaPWD(password) )
		{
			password = Crypto.encrypt(password);
			parametri.put("password", password);
		}
		
		if (!((String)parametri.get("nessunaScadenza")).equals("Y") && 
				(Calendar)parametri.get("dataFineValiditaUtenza") == null)
			return Messages.SPECIFICARE_DATA_SCADENZA.format();
		
		if (!((String)parametri.get("nessunaScadenza")).equals("Y") && 
				((String)parametri.get("utenzaAttiva")).equals("Y") && 
				(Calendar)parametri.get("dataFineValiditaUtenza") != null &&
				!((Calendar)parametri.get("dataFineValiditaUtenza")).after(now))
			return Messages.SPECIFICARE_DATA_SCADENZA_VALIDA.format();
		
		if ( ((String)parametri.get("nome")).equals("") || 
				((String)parametri.get("cognome")).equals("") || 	
				((String)parametri.get("codiceFiscale")).equals("") || 
				((String)parametri.get("tipologiaUtente")).equals("") || 
				((String)parametri.get("username")).equals("") || 
				((String)parametri.get("emailNotifiche")).equals(""))
				return Messages.CAMPI_OBBLIGATORI.format("*");

		} catch (UnsupportedEncodingException e) {
			return "Errore in fase di decrittografia della password - Contattare l'Amministratore";
		} catch (ChryptoServiceException e) {
			return "Errore in fase di decrittografia della password - Contattare l'Amministratore";
		}		
		return "OK";
	}
	
	/**
	 * Questa routine recupera i parametri dal form di inserimento o modifica
	 * per la tabella SEUSRTB
	 * 
	 * @param request
	 * @param codop - "add" o "edit"
	 * @return
	 */
	protected HashMap<String,Object> getSeUsersFormParameters(HttpServletRequest request, String codop)
	{
		HashMap<String,Object> param = new HashMap<String, Object>();
		/*
		 * INSERIMENTO
		 */
		if (codop.equals("add"))
		{
			String username = (request.getAttribute("tx_username") == null ? "" : (String)request.getAttribute("tx_username"));
			param.put("username", username);
			
			String nome = (request.getAttribute("tx_nome") == null ? "" : (String)request.getAttribute("tx_nome"));
			param.put("nome", nome);
			
			String cognome = (request.getAttribute("tx_cognome") == null ? "" : (String)request.getAttribute("tx_cognome"));
			param.put("cognome", cognome);
			
			String codiceFiscale = (request.getAttribute("tx_codiceFiscale") == null ? "" : (String)request.getAttribute("tx_codiceFiscale"));
			param.put("codiceFiscale", codiceFiscale);
		}
		/*
		 * MODIFICA
		 */
		else
		{
			String username = (request.getAttribute("tx_username_hidden") == null ? "" : (String)request.getAttribute("tx_username_hidden"));
			param.put("username", username);
			
			String nome = (request.getAttribute("tx_nome_hidden") == null ? "" : (String)request.getAttribute("tx_nome_hidden"));
			param.put("nome", nome);
			
			String cognome = (request.getAttribute("tx_cognome_hidden") == null ? "" : (String)request.getAttribute("tx_cognome_hidden"));
			param.put("cognome", cognome);
			
			String codiceFiscale = (request.getAttribute("tx_codiceFiscale_hidden") == null ? "" : (String)request.getAttribute("tx_codiceFiscale_hidden"));
			param.put("codiceFiscale", codiceFiscale);
			/*
			 * Setto gli attributi relativi ai campi disabilitati
			 * ricavati dai campi hidden
			 */
			request.setAttribute("tx_username_hidden", username);
			request.setAttribute("tx_nome_hidden", nome);
			request.setAttribute("tx_cognome_hidden", cognome);
			request.setAttribute("tx_codiceFiscale_hidden", codiceFiscale);

		}
		String utenzaAttiva = (request.getAttribute("utenzaAttiva") == null ? "N" : "Y");
		param.put("utenzaAttiva", utenzaAttiva);
		
		String password = (request.getAttribute("tx_password") == null ? "" : (String)request.getAttribute("tx_password"));
		param.put("password", password);
		
		String note = (request.getAttribute("note") == null ? "" : (String)request.getAttribute("note"));
		param.put("note", note);

		String tipologiaUtente = (request.getAttribute("tipologiaUtente") == null ? "" : (String)request.getAttribute("tipologiaUtente"));
		param.put("tipologiaUtente", tipologiaUtente);

		String emailNotifiche = (request.getAttribute("emailNotifiche") == null ? "" : (String)request.getAttribute("emailNotifiche"));
		param.put("emailNotifiche", emailNotifiche);

		String smsNotifiche = (request.getAttribute("smsNotifiche") == null ? "" : (String)request.getAttribute("smsNotifiche"));
		param.put("smsNotifiche", smsNotifiche);

		String passwordAutogenerata = (request.getAttribute("passwordAutogenerata") == null ? "N" : "Y");
		param.put("passwordAutogenerata", passwordAutogenerata);

		String nessunaScadenza = (request.getAttribute("nessunaScadenza") == null ? "N" : "Y");
		param.put("nessunaScadenza", nessunaScadenza);

		Calendar dataFineValiditaUtenza = (nessunaScadenza.equals("Y") ? getFine2099(request) : (Calendar)request.getAttribute("dataScadenza"));
		param.put("dataFineValiditaUtenza", dataFineValiditaUtenza);

		return param;
	}

	/**
	 * Fa il check di campi relativi alle UTENZE e campi relativi ai PROFILI
	 * @param request
	 * @param profiloUtente
	 * @param codop
	 * @return
	 */
	protected String checkInputFields(HttpServletRequest request,ProfiloUtente profiloUtente, String codop, HttpSession session)
	{
		String esito = "OK";
		//String lista = "listaTipologieServizio";
		//String lista2 = "listaTipologieServizioSel";
		
		if (codiceSocieta.equals("") &&
				(profiloUtente.equals(ProfiloUtente.AMEN) || 
				profiloUtente.equals(ProfiloUtente.AMUT) ||
				profiloUtente.equals(ProfiloUtente.AMSO)))
					esito = Messages.SPECIFICARE_CODICE_SOCIETA.format();
		else
		{
			if (codiceUtente.equals("") && 
					(profiloUtente.equals(ProfiloUtente.AMEN) || 
					profiloUtente.equals(ProfiloUtente.AMUT)) )
				esito = Messages.SPECIFICARE_CODICE_UTENTE.format();
			else
			{
				if (codiceEnte.equals("") && 
						profiloUtente.equals(ProfiloUtente.AMEN) )
					esito = Messages.SPECIFICARE_CODICE_ENTE.format();
				/*else
				{
					HashMap<String, String> tipologieServizioSel = (HashMap<String, String>) session.getAttribute(lista2);
					
					if (profiloUtente.equals(ProfiloUtente.AMEN) && (tipologieServizioSel==null || tipologieServizioSel.isEmpty()))
						esito = Messages.SPECIFICARE_TIPOLOGIA_SERVIZIO.format();
				
				}*/
			}
		}
		
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		//questo if dovrebbe essere sempre verificato poichè il campo
		//è obbligatorio a livello di validator nella jsp
			
			if((template.equals("soris")||template.equals("newsoris")) && !profiloUtente.equals(ProfiloUtente.PYCO)){
				System.out.println("SORIS: profilo " + profiloUtente + " salta il controllo");
			}else{
				if (username != null && !username.equals(""))
				{
					try
					{
						//verifico che lo username inserito appartenga ad utente esistente nella tabella SEUSRTB
						
						/*
						com.seda.security.webservice.dati.SelezionaUtenteRequestType req = new com.seda.security.webservice.dati.SelezionaUtenteRequestType(username);		
						com.seda.security.webservice.dati.SelezionaUtenteResponseType res = WSCache.securityServer.selezionaUtente(req);
						com.seda.security.webservice.dati.ResponseType response = null;
						*/
						
						SelezionaUtentePIVARequestType req = new SelezionaUtentePIVARequestType();	
						req.setUserName(username);
						SelezionaUtentePIVAResponseType res = WSCache.securityServer.selezionaUtentePIVA(req, request);
						com.seda.security.webservice.dati.ResponseType response = null;
						
						if (res != null && res.getResponse() != null)
						{
							response = res.getResponse();
							String retCode = response.getRetCode();
							String retMessage = response.getRetMessage();
							if (retCode.equals("00"))
							{
								if (res.getSelezionaUtentePIVAResponse() == null)
									esito = Messages.USERNAME_NON_VALIDO.format();
							}
							else
							{
								if (res.getSelezionaUtentePIVAResponse() == null)
									esito = Messages.USERNAME_NON_VALIDO.format();
								else
									esito = retCode + " - " + retMessage;
							}
						}
						else
							esito = Messages.GENERIC_ERROR.format();
					}
					catch (Exception e) {}
				}
				//inizio LP PG200060
			}
		}
		//fine LP PG200060
					
		return esito;
	}
	
	/**
	 * Fa il check di campi relativi ai PROFILI per TRENTINO RISCOSSIONI
	 * @param request
	 * @param profiloUtente
	 * @param codop
	 * @return
	 */
	protected String checkInputFieldsTrentRisc(HttpServletRequest request,ProfiloUtente profiloUtente, String codop, HttpSession session)
	{
		String esito = "OK";
		//String lista = "listaTipologieServizio";
		//String lista2 = "listaTipologieServizioSel";
		
		if (codiceSocieta.equals("") &&
				(profiloUtente.equals(ProfiloUtente.AMEN) || 
				profiloUtente.equals(ProfiloUtente.AMUT) ||
				profiloUtente.equals(ProfiloUtente.AMSO)))
					esito = Messages.SPECIFICARE_CODICE_SOCIETA.format();
		else
		{
			if (codiceUtente.equals("") && 
					(profiloUtente.equals(ProfiloUtente.AMEN) || 
					profiloUtente.equals(ProfiloUtente.AMUT)) )
				esito = Messages.SPECIFICARE_CODICE_UTENTE.format();
			else
			{
				if (codiceEnte.equals("") && 
						profiloUtente.equals(ProfiloUtente.AMEN) )
					esito = Messages.SPECIFICARE_CODICE_ENTE.format();
				/*else
				{
					HashMap<String, String> tipologieServizioSel = (HashMap<String, String>) session.getAttribute(lista2);
					
					if (profiloUtente.equals(ProfiloUtente.AMEN) && (tipologieServizioSel==null || tipologieServizioSel.isEmpty()))
						esito = Messages.SPECIFICARE_TIPOLOGIA_SERVIZIO.format();
				
				}*/
			}
		}
		
	
		return esito;
	}
	
	/**
	 * Fa il check di campi relativi ai PROFILI per BOLZANO
	 * @param request
	 * @param profiloUtente
	 * @param codop
	 * @return
	 */
	protected String checkInputFieldsBolzano(HttpServletRequest request,ProfiloUtente profiloUtente, String codop, HttpSession session)
	{
		String esito = "OK";
		//String lista = "listaTipologieServizio";
		//String lista2 = "listaTipologieServizioSel";
		
		if (codiceSocieta.equals("") &&
				(profiloUtente.equals(ProfiloUtente.AMEN) || 
				profiloUtente.equals(ProfiloUtente.AMUT) ||
				profiloUtente.equals(ProfiloUtente.AMSO)))
					esito = Messages.SPECIFICARE_CODICE_SOCIETA.format();
		else
		{
			if (codiceUtente.equals("") && 
					(profiloUtente.equals(ProfiloUtente.AMEN) || 
					profiloUtente.equals(ProfiloUtente.AMUT)) )
				esito = Messages.SPECIFICARE_CODICE_UTENTE.format();
			else
			{
				if (codiceEnte.equals("") && 
						profiloUtente.equals(ProfiloUtente.AMEN) )
					esito = Messages.SPECIFICARE_CODICE_ENTE.format();
				/*else
				{
					HashMap<String, String> tipologieServizioSel = (HashMap<String, String>) session.getAttribute(lista2);
					
					if (profiloUtente.equals(ProfiloUtente.AMEN) && (tipologieServizioSel==null || tipologieServizioSel.isEmpty()))
						esito = Messages.SPECIFICARE_TIPOLOGIA_SERVIZIO.format();
				
				}*/
			}
		}
		
	
		return esito;
	}

	
	protected PyUserType getPyUser(HttpServletRequest request,HttpSession session,Calendar now)
	{
		UserBean userBean = (UserBean)session.getAttribute("j_user_bean");
		PyUserType pyUser = new PyUserType();

		pyUser.setChiaveUtente(chiaveUtente);
		pyUser.setUserName(username);
		pyUser.setUserProfile(userProfile);
		pyUser.setFlagAttivazione(utenzaAttiva);
		pyUser.setCodiceSocieta(codiceSocieta);
		pyUser.setCodiceUtente(codiceUtente);
		pyUser.setChiaveEnteConsorzio(codiceEnte);
		pyUser.setChiaveEnteConsorziato("");
		pyUser.setDownloadFlussiRendicontazione(downloadFlussiRendicontazione);
		pyUser.setInvioFlussiRendicontazioneViaFtp(invioFlussiRendicontazioneViaFtp);
		pyUser.setInvioFlussiRendicontazioneViaEmail(invioFlussiRendicontazioneViaEmail);
		pyUser.setInvioFlussiRendicontazioneViaWs(invioFlussiRendicontazioneViaWs);
		pyUser.setAzioniPerTransazioniOK(azioniPerTransazioniOK);
		pyUser.setAzioniPerTransazioniKO(azioniPerTransazioniKO);
		pyUser.setAzioniPerRiconciliazioneManuale(azioniPerRiconciliazioneManuale);
		pyUser.setAttivazioneEstrattoContoManager(attivazioneEstrattoContoManager);
		pyUser.setAbilitazioneProfiloRiversamento(abilitazioneProfiloRiversamento);
		pyUser.setGruppoAgenzia(codiceGruppoAgenzia);  //RE180181 SB
		
		pyUser.setAssociazioniProvvisorieRiconciliazionemt(associazioniProvvisorieRiconciliazionemt);
		pyUser.setAssociazioniDefinitiveRiconciliazionemt(associazioniDefinitiveRiconciliazionemt);
		//dom R
		pyUser.setMailContoGestione(mailContogestione);
		
		pyUser.setEntePertinenza(entePertinenza);	//EP160510_001 GG 03112016
		
		pyUser.setAbilitazioneMultiUtente(abilitazioneMultiUtente);
		pyUser.setDataUltimoAggiornamento(now);
		pyUser.setOperatoreUltimoAggiornamento(userBean.getUserName());
		pyUser.setListaTipologiaServizio(formattaListaTipologieServizio(DDLTipologieServizioSel));
		
		pyUser.setFlagPrenotazioneFatturazione(prenotazioneFatturazione);
		return pyUser;
	}
	
	@SuppressWarnings("unchecked")
	protected String formattaListaTipologieServizio(
			HashMap<String, String> DDLTipologieServizioSel) {
		
		//questo metodo viene richiamato per formattare il campo lista tipologie servizio
		//nel seguente modo (valore1|valore2|...)
		
		String listaTipologiaServizioForInsert="";
		
		if (DDLTipologieServizioSel != null) {
			
			   Iterator iteratorsel = DDLTipologieServizioSel.keySet().iterator();
			   
			   String key="";
			   
	    	   while(iteratorsel.hasNext()){
		        	key=(String) iteratorsel.next();
		        	
		        	key+="|";
		        	listaTipologiaServizioForInsert+=key;
		       }
	       	}
		if (listaTipologiaServizioForInsert.equals(""))
			return listaTipologiaServizioForInsert;
		else
		return listaTipologiaServizioForInsert.substring(0, listaTipologiaServizioForInsert.length()-1);
	}

	/**
	 * Questo metodo è utilizzato per LEPIDA
	 * e valorizza lo "username" con il "codice fiscale"
	 * 
	 * @param request
	 * @param session
	 * @param now
	 * @return
	 */
	protected PyUserType getPyUserForLepida(HttpServletRequest request,HttpSession session,Calendar now)
	{
		PyUserType pyUser = getPyUser(request, session, now);
		pyUser.setUserName(codiceFiscale.toUpperCase());
		return pyUser;
	}

	/**
	 * Setta i flag nel form di inserimento/modifica "seUsers_var.jsp"
	 * @param request
	 * @param parametri
	 */
	protected void setSeUsersFormFlags(HttpServletRequest request, HashMap<String, Object> parametri)
	{
		request.setAttribute("chk_passwordAutogenerata", ((String)parametri.get("passwordAutogenerata")).equals("Y"));
		request.setAttribute("chk_nessunaScadenza",  ((String)parametri.get("nessunaScadenza")).equals("Y"));
		request.setAttribute("chk_utenzaAttiva",  ((String)parametri.get("utenzaAttiva")).equals("Y"));
	}
	
	/**
	 * Obsoleta per le UTENZE; è stato sostituito da "setSeUsersFormFlags" - Il campo "abilitazioneMultiUtente" è relativo ai profili
	 * @param request
	 */
	protected void setForm1Flags(HttpServletRequest request)
	{
		request.setAttribute("chk_utenzaAttiva",  utenzaAttiva.equals("Y"));
		request.setAttribute("chk_abilitazioneMultiUtente",  abilitazioneMultiUtente.equals("Y"));
	}
	
	/**
	 * Questo metodo setta i flag della form nello STEP 2 solo
	 * se il profilo è rimasto inalterato
	 */
	protected void checkProfileAndSetForm2Flags(HttpServletRequest request,SelezionaUtenteResponseType selezionaRes)
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		PyUserType pyUser = selezionaRes.getPyUser();
		String dbUserProfile = pyUser.getUserProfile();
		if (userProfile.equals(dbUserProfile))
		{
			request.setAttribute("chk_downloadFlussiRendicontazione",  downloadFlussiRendicontazione.equals("Y"));
			request.setAttribute("chk_invioFlussiRendicontazioneViaFtp",  invioFlussiRendicontazioneViaFtp.equals("Y"));
			request.setAttribute("chk_invioFlussiRendicontazioneViaEmail",  invioFlussiRendicontazioneViaEmail.equals("Y"));
			request.setAttribute("chk_invioFlussiRendicontazioneViaWs",  invioFlussiRendicontazioneViaWs.equals("Y"));
			request.setAttribute("chk_azioniPerTransazioniOK",  azioniPerTransazioniOK.equals("Y"));
			request.setAttribute("chk_azioniPerTransazioniKO",  azioniPerTransazioniKO.equals("Y"));
			request.setAttribute("chk_azioniPerRiconciliazioneManuale",  azioniPerRiconciliazioneManuale.equals("Y"));
			request.setAttribute("chk_attivazioneEstrattoContoManager",  attivazioneEstrattoContoManager.equals("Y"));
			request.setAttribute("chk_abilitazioneProfiloRiversamento",  abilitazioneProfiloRiversamento.equals("Y"));

			// dom R
			request.setAttribute("chk_mailContogestione",  mailContogestione.equals("Y"));

			request.setAttribute("chk_configurazione",  configurazione.equals("Y"));
			request.setAttribute("chk_adminusers",  adminusers.equals("Y"));
			request.setAttribute("chk_ecmanager",  ecmanager.equals("Y"));
			//inizio LP PG200360
			if(serviziAttiviOn(request)) {
				request.setAttribute("chk_serviziattivi",  serviziattivi.equals("Y"));
			}
			//fine LP PG200360
			//inizio LP PG200060
			if(!template.equals("regmarche")) {
			//fine LP PG200060
			request.setAttribute("chk_ecanagrafica",  ecanagrafica.equals("Y"));
			request.setAttribute("chk_ecuffmanager",  ecuffmanager.equals("Y"));
			request.setAttribute("chk_ecnotifiche",  ecnotifiche.equals("Y"));
			request.setAttribute("chk_ecagemanager",  ecagemanager.equals("Y"));	//PG180300 20190325 CT
			//inizio LP PG200060
			} else {
				request.setAttribute("chk_ecanagrafica", false);
				request.setAttribute("chk_ecuffmanager",  false);
				request.setAttribute("chk_ecnotifiche",  false);
				request.setAttribute("chk_ecagemanager",  false);
			}
			//fine LP PG200060
			request.setAttribute("chk_log",  log.equals("Y"));
			//inizio LP PG21X007
			if(logRequestOn(request)) {
				request.setAttribute("chk_logrequest",  logrequest.equals("Y"));
			}
			//fine LP PG21X007
			request.setAttribute("chk_monitoraggio",  monitoraggio.equals("Y"));
			request.setAttribute("chk_rendicontazione",  rendicontazione.equals("Y"));
			request.setAttribute("chk_riconciliazione",  riconciliazione.equals("Y"));
			request.setAttribute("chk_riversamento",  riversamento.equals("Y"));
			request.setAttribute("chk_eccedenze",  eccedenze.equals("Y"));
			request.setAttribute("chk_monitoraggioext",  monitoraggioext.equals("Y"));
			//PG150180_001 GG - inizio
			request.setAttribute("chk_monitoraggionn",  monitoraggionn.equals("Y"));
			request.setAttribute("chk_riconciliazionenn",  riconciliazionenn.equals("Y"));
			//PG150180_001 GG - fine
			request.setAttribute("chk_analysis",  analysis.equals("Y"));
			request.setAttribute("chk_ottico",  ottico.equals("Y"));
			request.setAttribute("chk_entrate",  entrate.equals("Y"));

			//DOM R
			request.setAttribute("chk_contogestione",  contogestione.equals("Y"));

			request.setAttribute("chk_ruoli",  ruoli.equals("Y"));
			request.setAttribute("chk_wismanager",  wismanager.equals("Y"));
			request.setAttribute("chk_wisconfig",  wisconfig.equals("Y"));
			request.setAttribute("chk_monitoraggiowis",  monitoraggiowis.equals("Y"));
			request.setAttribute("chk_modello3config",  modello3config.equals("Y")); //PG200140
			request.setAttribute("chk_blackboxconfig",  blackboxconfig.equals("Y")); //PG200140
			//inizio LP PG200060
			if(!template.equals("regmarche")) {
			//fine LP PG200060
			request.setAttribute("chk_walletmanager",  walletmanager.equals("Y"));
			request.setAttribute("chk_walletmonitoraggio",  walletmonitoraggio.equals("Y"));
			request.setAttribute("chk_walletanagraficacontribuenti",  walletanagraficacontribuenti.equals("Y"));
			request.setAttribute("chk_walletricaricheborsellino",  walletricaricheborsellino.equals("Y"));
			request.setAttribute("chk_walletsollecitodiscarico",  walletsollecitodiscarico.equals("Y"));
			request.setAttribute("chk_walletservizio",  walletservizio.equals("Y"));
			request.setAttribute("chk_walletconfig",  walletconfig.equals("Y"));
			
			
			//PG180040
			request.setAttribute("chk_mercatoconfig", mercatoconfig.equals("Y"));
			request.setAttribute("chk_mercatimanager", mercatimanager.equals("Y"));
			request.setAttribute("ckk_monitoraggiomercati", monitoraggiomercati.equals("Y"));
			
			//PG180010 - inizio
			request.setAttribute("chk_riconciliazionemt", riconciliazionemt.equals("Y"));
			request.setAttribute("chk_associazioniDefinitiveRiconciliazionemt", associazioniDefinitiveRiconciliazionemt.equals("Y"));
			request.setAttribute("chk_associazioniProvvisorieRiconciliazionemt", associazioniProvvisorieRiconciliazionemt.equals("Y"));
			//PG180010 - fine
			//inizio LP PG200060
			} else {
				request.setAttribute("chk_walletmanager", false);
				request.setAttribute("chk_walletmonitoraggio", false);
				request.setAttribute("chk_walletanagraficacontribuenti", false);
				request.setAttribute("chk_walletricaricheborsellino", false);
				request.setAttribute("chk_walletsollecitodiscarico", false);
				request.setAttribute("chk_walletservizio", false);
				request.setAttribute("chk_walletconfig", false);
				request.setAttribute("chk_mercatoconfig", false);
				request.setAttribute("chk_mercatimanager", false);
				request.setAttribute("ckk_monitoraggiomercati", false);
				request.setAttribute("chk_riconciliazionemt",  false);
				request.setAttribute("chk_associazioniDefinitiveRiconciliazionemt", false);
				request.setAttribute("chk_associazioniProvvisorieRiconciliazionemt", false);
				request.setAttribute("chk_monitoraggiocup", monitoraggiocup.equals("Y"));
				request.setAttribute("chk_monitoraggiocruss", monitoraggiocruss.equals("Y"));
			}
			//fine LP PG200060

//			YLM PG22XX07 INI
			request.setAttribute("chk_monitoraggiosoap", monitoraggiosoap.equals("Y"));
//			YLM PG22XX07 FINE
			request.setAttribute("chk_gestioneuffici",  gestioneuffici.equals("Y")); //SVILUPPO_001_LUCAP_30062020
			request.setAttribute("chk_ioitalia", ioitalia.equals("Y"));
			
			//PG200120
			request.setAttribute("chk_blackboxpos",  blackboxpos.equals("Y"));
			request.setAttribute("chk_blackboxposlog",  blackboxposlog.equals("Y")); 
			//FINE PG200120

			// inizio SR PGNTMGR-56
			request.setAttribute("chk_prenotazionefatturazione",  prenotazioneFatturazione.equals("Y"));
			request.setAttribute("chk_richiesteelaborazioni",  richiesteelaborazioni.equals("Y"));
			// fine SR PGNTMGR-56
			request.setAttribute("chk_inviaufficio",inviaufficio.equals("Y"));
		}
	}

	protected void setForm2Flags(HttpServletRequest request)
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		request.setAttribute("chk_downloadFlussiRendicontazione",  downloadFlussiRendicontazione.equals("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaFtp",  invioFlussiRendicontazioneViaFtp.equals("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaEmail",  invioFlussiRendicontazioneViaEmail.equals("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaWs",  invioFlussiRendicontazioneViaWs.equals("Y"));
		request.setAttribute("chk_azioniPerTransazioniOK",  azioniPerTransazioniOK.equals("Y"));
		request.setAttribute("chk_azioniPerTransazioniKO",  azioniPerTransazioniKO.equals("Y"));
		request.setAttribute("chk_azioniPerRiconciliazioneManuale",  azioniPerRiconciliazioneManuale.equals("Y"));
		request.setAttribute("chk_attivazioneEstrattoContoManager",  attivazioneEstrattoContoManager.equals("Y"));
		request.setAttribute("chk_abilitazioneProfiloRiversamento",  abilitazioneProfiloRiversamento.equals("Y"));
		//DOM R
		request.setAttribute("chk_mailContogestione",  mailContogestione.equals("Y"));

		request.setAttribute("chk_configurazione",  configurazione.equals("Y"));
		request.setAttribute("chk_adminusers",  adminusers.equals("Y"));
		request.setAttribute("chk_ecmanager",  ecmanager.equals("Y"));
		//inizio LP PG200360
		if(serviziAttiviOn(request)) {
			request.setAttribute("chk_serviziattivi",  serviziattivi.equals("Y"));
		}
		//fine LP PG200360
		

		request.setAttribute("chk_log",  log.equals("Y"));
		//inizio LP PG21X007
		if(logRequestOn(request)) {
			request.setAttribute("chk_logrequest",  logrequest.equals("Y"));
		}
		//fine LP PG21X007
		request.setAttribute("chk_monitoraggio",  monitoraggio.equals("Y"));
		request.setAttribute("chk_rendicontazione",  rendicontazione.equals("Y"));
		request.setAttribute("chk_riconciliazione",  riconciliazione.equals("Y"));
		request.setAttribute("chk_riversamento",  riversamento.equals("Y"));
		request.setAttribute("chk_eccedenze",  eccedenze.equals("Y"));
		request.setAttribute("chk_monitoraggioext",  monitoraggioext.equals("Y"));
		//PG150180_001 GG - inizio
		request.setAttribute("chk_monitoraggionn",  monitoraggionn.equals("Y"));
		request.setAttribute("chk_riconciliazionenn",  riconciliazionenn.equals("Y"));
		//PG150180_001 GG - fine
		request.setAttribute("chk_analysis",  analysis.equals("Y"));
		request.setAttribute("chk_ottico",  ottico.equals("Y"));
		request.setAttribute("chk_entrate",  entrate.equals("Y"));
		//DOM R
		request.setAttribute("chk_contogestione",  contogestione.equals("Y"));

		request.setAttribute("chk_ruoli",  ruoli.equals("Y"));
		request.setAttribute("chk_modello3config",  modello3config.equals("Y")); //PG200140
		
		if(!template.equals("aosta")) {
			request.setAttribute("chk_wismanager",  wismanager.equals("Y"));
			request.setAttribute("chk_wisconfig",  wisconfig.equals("Y"));
			request.setAttribute("chk_monitoraggiowis",  monitoraggiowis.equals("Y"));
			request.setAttribute("chk_blackboxconfig",  blackboxconfig.equals("Y")); //PG200140
			//PG200120
			request.setAttribute("chk_blackboxpos",  blackboxpos.equals("Y"));
			request.setAttribute("chk_blackboxposlog",  blackboxposlog.equals("Y"));
			//FINE PG200120
		}
		//inizio LP PG200420 - 20210423
		else {
			request.setAttribute("chk_blackboxconfig",  blackboxconfig.equals("Y"));
		}
		//fine LP PG200420 - 20210423
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
			if(!template.equals("aosta")) {
					request.setAttribute("chk_walletmanager",  walletmanager.equals("Y"));
					request.setAttribute("chk_walletmonitoraggio",  walletmonitoraggio.equals("Y"));
					request.setAttribute("chk_walletanagraficacontribuenti",  walletanagraficacontribuenti.equals("Y"));
					request.setAttribute("chk_walletricaricheborsellino",  walletricaricheborsellino.equals("Y"));
					request.setAttribute("chk_walletsollecitodiscarico",  walletsollecitodiscarico.equals("Y"));
					request.setAttribute("chk_walletservizio",  walletservizio.equals("Y"));
					request.setAttribute("chk_walletconfig",  walletconfig.equals("Y"));
			}
		
		//PG180040 - Mercati
		request.setAttribute("chk_mercatoconfig", mercatoconfig.equals("Y"));
		request.setAttribute("chk_mercatimanager", mercatimanager.equals("Y"));
		request.setAttribute("chk_monitoraggiomercati", monitoraggiomercati.equals("Y"));
		
		//PG180010 - inizio
		request.setAttribute("chk_riconciliazionemt", riconciliazionemt.equals("Y"));
		request.setAttribute("chk_associazioniDefinitiveRiconciliazionemt", associazioniDefinitiveRiconciliazionemt.equals("Y"));
		request.setAttribute("chk_associazioniProvvisorieRiconciliazionemt", associazioniProvvisorieRiconciliazionemt.equals("Y"));
		//PG180010 - fine
		
		//inizio LP PG200060
		request.setAttribute("chk_ecanagrafica",  ecanagrafica.equals("Y"));
		request.setAttribute("chk_ecuffmanager",  ecuffmanager.equals("Y"));
		request.setAttribute("chk_ecnotifiche",  ecnotifiche.equals("Y"));
		request.setAttribute("chk_ecagemanager",  ecagemanager.equals("Y"));	//PG180300 20190325 CT
		//fine LP PG200060
		
		//inizio LP PG200060
		} else {
			request.setAttribute("chk_walletmanager", false);
			request.setAttribute("chk_walletmonitoraggio", false);
			request.setAttribute("chk_walletanagraficacontribuenti", false);
			request.setAttribute("chk_walletricaricheborsellino", false);
			request.setAttribute("chk_walletsollecitodiscarico", false);
			request.setAttribute("chk_walletservizio", false);
			request.setAttribute("chk_walletconfig", false);
			request.setAttribute("chk_mercatoconfig", false);
			request.setAttribute("chk_mercatimanager", false);
			request.setAttribute("ckk_monitoraggiomercati", false);
			request.setAttribute("chk_riconciliazionemt",  false);
			request.setAttribute("chk_associazioniDefinitiveRiconciliazionemt", false);
			request.setAttribute("chk_associazioniProvvisorieRiconciliazionemt", false);
			request.setAttribute("chk_monitoraggiocup", monitoraggiocup.equals("Y"));
			request.setAttribute("chk_monitoraggiocruss", monitoraggiocruss.equals("Y"));
			//inizio LP PG200060
			request.setAttribute("chk_ecanagrafica", false);
			request.setAttribute("chk_ecuffmanager", false);
			request.setAttribute("chk_ecnotifiche", false);
			request.setAttribute("chk_ecagemanager", false);
			//fine LP PG200060
		}
		//fine LP PG200060
//		YLM PG22XX07 INI
		request.setAttribute("chk_monitoraggiosoap", monitoraggiosoap.equals("Y"));
//		YLM PG22XX07 FINE
		request.setAttribute("chk_gestioneuffici",  gestioneuffici.equals("Y")); //SVILUPPO_001_LUCAP_30062020
		request.setAttribute("chk_ioitalia", ioitalia.equals("Y")); // PG210160

		// inizio SR PGNTMGR-56
		request.setAttribute("chk_prenotazioneFatturazione",  prenotazioneFatturazione.equals("Y"));
		request.setAttribute("chk_richiesteelaborazioni",  richiesteelaborazioni.equals("Y"));
		// fine SR PGNTMGR-56
		request.setAttribute("chk_inviaufficio",inviaufficio.equals("Y"));
	}

	protected void setDisableForm2Flags(HttpServletRequest request)
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		request.setAttribute("chk_downloadFlussiRendicontazione",  downloadFlussiRendicontazione.equals("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaFtp",  invioFlussiRendicontazioneViaFtp.equals("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaEmail",  invioFlussiRendicontazioneViaEmail.equals("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaWs",  invioFlussiRendicontazioneViaWs.equals("Y"));
		request.setAttribute("chk_azioniPerTransazioniOK",  azioniPerTransazioniOK.equals("Y"));
		request.setAttribute("chk_azioniPerTransazioniKO",  azioniPerTransazioniKO.equals("Y"));
		request.setAttribute("chk_azioniPerRiconciliazioneManuale",  azioniPerRiconciliazioneManuale.equals("Y"));
		request.setAttribute("chk_attivazioneEstrattoContoManager",  attivazioneEstrattoContoManager.equals("Y"));
		request.setAttribute("chk_abilitazioneProfiloRiversamento",  abilitazioneProfiloRiversamento.equals("Y"));

		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		//PG180010 - inizio
		request.setAttribute("chk_associazioniProvvisorieRiconciliazionemt",  associazioniProvvisorieRiconciliazionemt.equals("Y"));
		request.setAttribute("chk_associazioniDefinitiveRiconciliazionemt",  associazioniDefinitiveRiconciliazionemt.equals("Y"));
		//inizio LP PG200060
		} else {
			request.setAttribute("chk_associazioniDefinitiveRiconciliazionemt", false);
			request.setAttribute("chk_associazioniProvvisorieRiconciliazionemt", false);
		}
		//fine LP PG200060

		//dom R
		request.setAttribute("chk_mailContogestione",  mailContogestione.equals("Y"));

		// inizio SR PGNTMGR-56
		request.setAttribute("chk_prenotazionefatturazione",  prenotazioneFatturazione.equals("Y"));
		// fine SR PGNTMGR-56
	}

	/**
	 * Salva in sessione le informazioni del primo step per l'inserimento/modifica di un utente
	 * 
	 * @param request
	 */
	protected void putAdd1ParametersInSession(HttpServletRequest request,HttpSession session) 
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		if (session.getAttribute("userAdd_chiaveUtente") != null) session.removeAttribute("userAdd_chiaveUtente");
		if (session.getAttribute("userAdd_userProfile") != null) session.removeAttribute("userAdd_userProfile");
		if (session.getAttribute("userAdd_codiceSocieta") != null) session.removeAttribute("userAdd_codiceSocieta");
		if (session.getAttribute("userAdd_codiceUtente") != null) session.removeAttribute("userAdd_codiceUtente");
		if (session.getAttribute("userAdd_codiceEnte") != null) session.removeAttribute("userAdd_codiceEnte");
		if (session.getAttribute("userAdd_username") != null) session.removeAttribute("userAdd_username");
		if (session.getAttribute("userAdd_utenzaAttiva") != null) session.removeAttribute("userAdd_utenzaAttiva");
		if (session.getAttribute("userAdd_abilitazioneMultiUtente") != null) session.removeAttribute("userAdd_abilitazioneMultiUtente");
		if (session.getAttribute("userAdd_codiceFiscale") != null) session.removeAttribute("userAdd_codiceFiscale");
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		if (session.getAttribute("userAdd_entePertinenza") != null) session.removeAttribute("userAdd_entePertinenza");	//EP160510_001 GG 03112016
		if (session.getAttribute("userAdd_gruppoAgenzia") != null) session.removeAttribute("userAdd_gruppoAgenzia");  //RE180181 SB
		//inizio LP PG200060
		}
		//fine LP PG200060
		
		session.setAttribute("userAdd_chiaveUtente", chiaveUtente);
		session.setAttribute("userAdd_userProfile", userProfile);
		session.setAttribute("userAdd_codiceSocieta", codiceSocieta);
		session.setAttribute("userAdd_codiceUtente", codiceUtente);
		session.setAttribute("userAdd_codiceEnte", codiceEnte);
		session.setAttribute("userAdd_username", username);
		session.setAttribute("userAdd_utenzaAttiva", utenzaAttiva);
		session.setAttribute("userAdd_abilitazioneMultiUtente", abilitazioneMultiUtente);
		session.setAttribute("userAdd_codiceFiscale", codiceFiscale);
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		session.setAttribute("userAdd_entePertinenza", entePertinenza);	//EP160510_001 GG 03112016
		session.setAttribute("userAdd_gruppoAgenzia", codiceGruppoAgenzia);	//RE180181 SB
		//inizio LP PG200060
		} else {
			session.setAttribute("userAdd_entePertinenza", null);	//EP160510_001 GG 03112016
			session.setAttribute("userAdd_gruppoAgenzia", null);	//RE180181 SB
		}
		//fine LP PG200060
	}

	/**
	 * Salva in sessione le informazioni del secondo step per l'inserimento di un nuovo utente
	 * 
	 * @param request
	 */
	protected void putAdd2ParametersInSession(HttpServletRequest request,HttpSession session) 
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		if (session.getAttribute("userAdd_downloadFlussiRendicontazione") != null) session.removeAttribute("userAdd_downloadFlussiRendicontazione");
		if (session.getAttribute("userAdd_invioFlussiRendicontazioneViaFtp") != null) session.removeAttribute("userAdd_invioFlussiRendicontazioneViaFtp");
		if (session.getAttribute("userAdd_invioFlussiRendicontazioneViaEmail") != null) session.removeAttribute("userAdd_invioFlussiRendicontazioneViaEmail");
		if (session.getAttribute("userAdd_invioFlussiRendicontazioneViaWs") != null) session.removeAttribute("userAdd_invioFlussiRendicontazioneViaWs");
		if (session.getAttribute("userAdd_azioniPerTransazioniOK") != null) session.removeAttribute("userAdd_azioniPerTransazioniOK");
		if (session.getAttribute("userAdd_azioniPerTransazioniKO") != null) session.removeAttribute("userAdd_azioniPerTransazioniKO");
		if (session.getAttribute("userAdd_azioniPerRiconciliazioneManuale") != null) session.removeAttribute("userAdd_azioniPerRiconciliazioneManuale");
		if (session.getAttribute("userAdd_attivazioneEstrattoContoManager") != null) session.removeAttribute("userAdd_attivazioneEstrattoContoManager");
		if (session.getAttribute("userAdd_abilitazioneProfiloRiversamento") != null) session.removeAttribute("userAdd_abilitazioneProfiloRiversamento");
		//dom R
		if (session.getAttribute("userAdd_mailContogestione") != null) session.removeAttribute("userAdd_mailContogestione");
		
		if (session.getAttribute("userAdd_configurazione") != null) session.removeAttribute("userAdd_configurazione");
		if (session.getAttribute("userAdd_adminusers") != null) session.removeAttribute("userAdd_adminusers");
		if (session.getAttribute("userAdd_ecmanager") != null) session.removeAttribute("userAdd_ecmanager");

		if (session.getAttribute("userAdd_richiesteelaborazioni") != null) session.removeAttribute("userAdd_richiesteelaborazioni");

		if (session.getAttribute("userAdd_inviaufficio") != null) session.removeAttribute("userAdd_inviaufficio");

		//inizio LP PG200360
		if(serviziAttiviOn(request)) {
			if (session.getAttribute("userAdd_serviziattivi") != null) session.removeAttribute("userAdd_serviziattivi");
		}
		//fine LP PG200360
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
			//fine LP PG200060
			if (session.getAttribute("userAdd_ecanagrafica") != null) session.removeAttribute("userAdd_ecanagrafica");
			if (session.getAttribute("userAdd_ecuffmanager") != null) session.removeAttribute("userAdd_ecuffmanager");
			//inizio LP PG200060
		}
		//fine LP PG200060
		if (session.getAttribute("userAdd_monitoraggio") != null) session.removeAttribute("userAdd_monitoraggio");
		if (session.getAttribute("userAdd_log") != null) session.removeAttribute("userAdd_log");
		if (session.getAttribute("userAdd_rendicontazione") != null) session.removeAttribute("userAdd_rendicontazione");
		if (session.getAttribute("userAdd_riconciliazione") != null) session.removeAttribute("userAdd_riconciliazione");
		if (session.getAttribute("userAdd_riversamento") != null) session.removeAttribute("userAdd_riversamento");

		if (session.getAttribute("userAdd_eccedenze") != null) session.removeAttribute("userAdd_eccedenze");
		if (session.getAttribute("userAdd_monitoraggioext") != null) session.removeAttribute("userAdd_monitoraggioext");
		//PG150180_001 GG - inizio
		if (session.getAttribute("userAdd_monitoraggionn") != null) session.removeAttribute("userAdd_monitoraggionn");
		if (session.getAttribute("userAdd_riconciliazionenn") != null) session.removeAttribute("userAdd_riconciliazionenn");
		//PG150180_001 GG - fine
		if (session.getAttribute("userAdd_analysis") != null) session.removeAttribute("userAdd_analysis");
		if (session.getAttribute("userAdd_ottico") != null) session.removeAttribute("userAdd_ottico");
		if (session.getAttribute("userAdd_entrate") != null) session.removeAttribute("userAdd_entrate");
		// dom R
		if (session.getAttribute("userAdd_contogestione") != null) session.removeAttribute("userAdd_contogestione");
		
		if (session.getAttribute("userAdd_ruoli") != null) session.removeAttribute("userAdd_ruoli");
		if (session.getAttribute("userAdd_wismanager") != null) session.removeAttribute("userAdd_wismanager");
		if (session.getAttribute("userAdd_wisconfig") != null) session.removeAttribute("userAdd_wisconfig");
		if (session.getAttribute("userAdd_monitoraggiowis") != null) session.removeAttribute("userAdd_monitoraggiowis");
		if (session.getAttribute("userAdd_blackboxconfig") != null) session.removeAttribute("userAdd_blackboxconfig"); //PG200140
		if (session.getAttribute("userAdd_modello3config") != null) session.removeAttribute("userAdd_modello3config"); //PG200140
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
			//fine LP PG200060
			if (session.getAttribute("userAdd_walletmanager") != null) session.removeAttribute("userAdd_walletmanager");
			if (session.getAttribute("userAdd_walletmonitoraggio") != null) session.removeAttribute("userAdd_walletmonitoraggio");
			if (session.getAttribute("userAdd_walletanagraficacontribuenti") != null) session.removeAttribute("userAdd_walletanagraficacontribuenti");
			if (session.getAttribute("userAdd_walletricaricheborsellino") != null) session.removeAttribute("userAdd_walletricaricheborsellino");
			if (session.getAttribute("userAdd_walletsollecitodiscarico") != null) session.removeAttribute("userAdd_walletsollecitodiscarico");
			if (session.getAttribute("userAdd_walletservizio") != null) session.removeAttribute("userAdd_walletservizio");
			if (session.getAttribute("userAdd_walletconfig") != null) session.removeAttribute("userAdd_walletconfig");
			//PG180040 - Mercati
			if (session.getAttribute("userAdd_mercatoconfig") != null) session.removeAttribute("userAdd_mercatoconfig");
			if (session.getAttribute("userAdd_mercatimanager") != null) session.removeAttribute("userAdd_mercatimanager");
			if (session.getAttribute("userAdd_monitoraggiomercati") != null) session.removeAttribute("userAdd_monitoraggiomercati");

			if (session.getAttribute("userAdd_gestioneuffici") != null) session.removeAttribute("userAdd_gestioneuffici"); //SVILUPPO_001_LUCAP_30062020

			//PG200120
			if (session.getAttribute("userAdd_blackboxpos") != null) session.removeAttribute("userAdd_blackboxpos");
			if (session.getAttribute("userAdd_blackboxposlog") != null) session.removeAttribute("userAdd_blackboxposlog");
			//FINE PG200120

			if (session.getAttribute("userAdd_downloadFlussiRendicontazione") != null) session.removeAttribute("userAdd_downloadFlussiRendicontazione");
			if (session.getAttribute("userAdd_prenotazioneFatturazione") != null) session.removeAttribute("userAdd_prenotazioneFatturazione");

			//PG180010 - inizio
			if (session.getAttribute("userAdd_riconciliazionemt") != null) session.removeAttribute("userAdd_riconciliazionemt");
			session.setAttribute("userAdd_associazioniDefinitiveRiconciliazionemt", associazioniDefinitiveRiconciliazionemt.equals("Y"));
			session.setAttribute("userAdd_associazioniProvvisorieRiconciliazionemt", associazioniProvvisorieRiconciliazionemt.equals("Y"));
			//PG180010 - fine
			//inizio LP PG200060
		} else  {
			session.setAttribute("userAdd_associazioniDefinitiveRiconciliazionemt", false);
			session.setAttribute("userAdd_associazioniProvvisorieRiconciliazionemt", false);
			if (session.getAttribute("userAdd_monitoraggiocup") != null) session.removeAttribute("userAdd_monitoraggiocup");
			if (session.getAttribute("userAdd_monitoraggiocruss") != null) session.removeAttribute("userAdd_monitoraggiocruss");
		}
		//fine LP PG200060

//		YLM PG22XX07 INI
		if (session.getAttribute("userAdd_monitoraggiosoap") != null) session.removeAttribute("userAdd_monitoraggiosoap");
//		YLM PG22XX07 FINE
		
		session.setAttribute("userAdd_downloadFlussiRendicontazione", downloadFlussiRendicontazione);
		session.setAttribute("userAdd_invioFlussiRendicontazioneViaFtp", invioFlussiRendicontazioneViaFtp);
		session.setAttribute("userAdd_invioFlussiRendicontazioneViaEmail", invioFlussiRendicontazioneViaEmail);
		session.setAttribute("userAdd_invioFlussiRendicontazioneViaWs", invioFlussiRendicontazioneViaWs);
		session.setAttribute("userAdd_azioniPerTransazioniOK", azioniPerTransazioniOK);
		session.setAttribute("userAdd_azioniPerTransazioniKO", azioniPerTransazioniKO);
		session.setAttribute("userAdd_azioniPerRiconciliazioneManuale", azioniPerRiconciliazioneManuale);
		session.setAttribute("userAdd_attivazioneEstrattoContoManager", attivazioneEstrattoContoManager);
		session.setAttribute("userAdd_abilitazioneProfiloRiversamento", abilitazioneProfiloRiversamento);
		session.setAttribute("userAdd_prenotazioneFatturazione", prenotazioneFatturazione);
		//dom R
		session.setAttribute("userAdd_mailContogestione", mailContogestione);

		session.setAttribute("userAdd_configurazione", configurazione);
		session.setAttribute("userAdd_adminusers", adminusers);
		session.setAttribute("userAdd_ecmanager", ecmanager);
		session.setAttribute("userAdd_richiesteelaborazioni", richiesteelaborazioni);
		session.setAttribute("userAdd_inviaufficio",inviaufficio);
		//inizio LP PG200360
		if(serviziAttiviOn(request)) {
			session.setAttribute("userAdd_serviziattivi", serviziattivi);
		}
		//fine LP PG200360
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
			//fine LP PG200060
			session.setAttribute("userAdd_ecanagrafica", ecanagrafica);
			session.setAttribute("userAdd_ecuffmanager", ecuffmanager);
			session.setAttribute("userAdd_ecnotifiche", ecnotifiche);
			session.setAttribute("userAdd_ecagemanager", ecagemanager);	//PG180300 20190325 CT
			//inizio LP PG200060
		} else  {
			session.setAttribute("userAdd_ecanagrafica", null);
			session.setAttribute("userAdd_ecuffmanager", null);
			session.setAttribute("userAdd_ecnotifiche", null);
			session.setAttribute("userAdd_ecagemanager", null);
		}
		//fine LP PG200060
		
		session.setAttribute("userAdd_monitoraggio", monitoraggio);
		session.setAttribute("userAdd_log", log);
		//inizio LP PG21X007
		if(logRequestOn(request)) {
			session.setAttribute("userAdd_logrequest", logrequest);
		}
		//fine LP PG21X007
		session.setAttribute("userAdd_rendicontazione", rendicontazione);
		session.setAttribute("userAdd_riconciliazione", riconciliazione);
		session.setAttribute("userAdd_riversamento", riversamento);

		session.setAttribute("userAdd_eccedenze", eccedenze);
		session.setAttribute("userAdd_monitoraggioext", monitoraggioext);
		//PG150180_001 GG - inizio
		session.setAttribute("userAdd_monitoraggionn", monitoraggionn);
		session.setAttribute("userAdd_riconciliazionenn", riconciliazionenn);
		//PG150180_001 GG - fine
		session.setAttribute("userAdd_analysis", analysis);
		session.setAttribute("userAdd_ottico", ottico);
		session.setAttribute("userAdd_ruoli", ruoli);
		session.setAttribute("userAdd_entrate", entrate);
		// dom R
		session.setAttribute("userAdd_contogestione", contogestione);
		
		session.setAttribute("userAdd_wismanager", wismanager);
		session.setAttribute("userAdd_wisconfig", wisconfig);
		session.setAttribute("userAdd_monitoraggiowis", monitoraggiowis);
		session.setAttribute("userAdd_blackboxconfig", blackboxconfig); //PG200140
		session.setAttribute("userAdd_modello3config", modello3config);	//PG200140
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
			//fine LP PG200060
			session.setAttribute("userAdd_walletmanager", walletmanager);
			session.setAttribute("userAdd_walletmonitoraggio", walletmonitoraggio);
			session.setAttribute("userAdd_walletanagraficacontribuenti", walletanagraficacontribuenti);
			session.setAttribute("userAdd_walletricaricheborsellino", walletricaricheborsellino);
			session.setAttribute("userAdd_walletsollecitodiscarico", walletsollecitodiscarico);
			session.setAttribute("userAdd_walletservizio", walletservizio);
			session.setAttribute("userAdd_walletconfig", walletconfig);
			//PG180040 - Mercati
			session.setAttribute("userAdd_mercatoconfig", mercatoconfig);
			session.setAttribute("userAdd_mercatimanager", mercatimanager);
			session.setAttribute("userAdd_monitoraggiomercati", monitoraggiomercati);

			//PG180010 - inizio
			session.setAttribute("userAdd_riconciliazionemt", riconciliazionemt);
			session.setAttribute("userAdd_associazioniDefinitiveRiconciliazionemt", associazioniDefinitiveRiconciliazionemt);
			session.setAttribute("userAdd_associazioniProvvisorieRiconciliazionemt", associazioniProvvisorieRiconciliazionemt);
			//PG180010 - fine
			//inizio LP PG200060
		} else {
			session.setAttribute("userAdd_walletmanager", null);
			session.setAttribute("userAdd_walletmonitoraggio", null);
			session.setAttribute("userAdd_walletanagraficacontribuenti", null);
			session.setAttribute("userAdd_walletricaricheborsellino", null);
			session.setAttribute("userAdd_walletsollecitodiscarico", null);
			session.setAttribute("userAdd_walletservizio", null);
			session.setAttribute("userAdd_walletconfig", null);	
			session.setAttribute("userAdd_mercatoconfig", null);
			session.setAttribute("userAdd_mercatimanager", null);
			session.setAttribute("userAdd_monitoraggiomercati", null);
			session.setAttribute("userAdd_riconciliazionemt", null);
			session.setAttribute("userAdd_associazioniDefinitiveRiconciliazionemt", null);
			session.setAttribute("userAdd_associazioniProvvisorieRiconciliazionemt", null);
			session.setAttribute("userAdd_monitoraggiocup", monitoraggiocup);
			session.setAttribute("userAdd_monitoraggiocruss", monitoraggiocruss);
		}
		//fine LP PG200060

//		YLM PG22XX07 INI
		session.setAttribute("userAdd_monitoraggiosoap", monitoraggiosoap);
//		YLM PG22XX07 FINE
		
		session.setAttribute("userAdd_gestioneuffici", gestioneuffici);	//SVILUPPO_001_LUCAP_30062020
		
		session.setAttribute("userAdd_ioitalia", ioitalia);	// PG210160
		
		//PG200120
		session.setAttribute("userAdd_blackboxpos", blackboxpos);
		session.setAttribute("userAdd_blackboxposlog", blackboxposlog); 
		//FINE PG200120
		
	}

	/**
	 * Recupera le informazioni del primo step per l'inserimento di un nuovo utente dalla sessione
	 * 
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	protected void getAdd1ParametersFromSession(HttpServletRequest request,HttpSession session) 
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		if (session.getAttribute("userAdd_chiaveUtente") != null)
			chiaveUtente = (Long)session.getAttribute("userAdd_chiaveUtente");
		
		userProfile = (String)session.getAttribute("userAdd_userProfile");
		codiceSocieta = (String)session.getAttribute("userAdd_codiceSocieta" );
		codiceUtente = (String)session.getAttribute("userAdd_codiceUtente" );
		codiceEnte = (String)session.getAttribute("userAdd_codiceEnte" );
		username = (String)session.getAttribute("userAdd_username" );
		utenzaAttiva = (String)session.getAttribute("userAdd_utenzaAttiva" );
		abilitazioneMultiUtente = (String)session.getAttribute("userAdd_abilitazioneMultiUtente" );
		codiceFiscale = (String)session.getAttribute("userAdd_codiceFiscale" );
		DDLTipologieServizioSel= (HashMap<String, String>) session.getAttribute("listaTipologieServizioSel");
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		entePertinenza = (String)session.getAttribute("userAdd_entePertinenza" );	//EP160510_001 GG 03112016
		codiceGruppoAgenzia = (String)session.getAttribute("userAdd_gruppoAgenzia" );	//RE180181 SB
		//inizio LP PG200060
		}
		//fine LP PG200060

	}

	/**
	 * Setta gli attributi corrispondenti ai campi di input del primo step copmpresi i flag.
	 * Viene utilizzata quando si torna dallo STEP2 allo STEP1 e si recuperano i valori dalla sessione
	 * 
	 * @param request
	 */
	protected void setForm1Attributes(HttpServletRequest request) 
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		/*
		 * Textbox e DropDownList
		 */
		if (chiaveUtente != null)
			request.setAttribute("tx_chiaveutente_hidden", chiaveUtente);
		
		request.setAttribute("tx_userprofile",userProfile);
		request.setAttribute("tx_societa" ,codiceSocieta);
		request.setAttribute("tx_utente" ,codiceUtente);
		request.setAttribute("tx_ente" ,codiceEnte);
		request.setAttribute("tx_username" ,username);		
		request.setAttribute("tx_codiceFiscale" ,codiceFiscale);
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		request.setAttribute("tx_pertinenza" ,entePertinenza);	//EP160510_001 GG 03112016
		request.setAttribute("tx_gruppo_agenzia",codiceGruppoAgenzia); //RE180181 SB
		//inizio LP PG200060
		}
		//fine LP PG200060
		/*
		 * CheckBox
		 */
		request.setAttribute("chk_utenzaAttiva",  utenzaAttiva.equals("Y"));
		request.setAttribute("chk_abilitazioneMultiUtente",  abilitazioneMultiUtente.equals("Y"));

	}
	
	/**
	 * Recupera le informazioni del secondo step per l'inserimento di un nuovo utente dalla sessione
	 * 
	 * @param request
	 */
	protected void getAdd2ParametersFromSession(HttpServletRequest request,HttpSession session) 
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		downloadFlussiRendicontazione = (String)session.getAttribute("userAdd_downloadFlussiRendicontazione" );
		invioFlussiRendicontazioneViaFtp = (String)session.getAttribute("userAdd_invioFlussiRendicontazioneViaFtp" );
		invioFlussiRendicontazioneViaEmail = (String)session.getAttribute("userAdd_invioFlussiRendicontazioneViaEmail" );
		invioFlussiRendicontazioneViaWs = (String)session.getAttribute("userAdd_invioFlussiRendicontazioneViaWs" );
		azioniPerTransazioniOK = (String)session.getAttribute("userAdd_azioniPerTransazioniOK" );
		azioniPerTransazioniKO = (String)session.getAttribute("userAdd_azioniPerTransazioniKO" );
		azioniPerRiconciliazioneManuale = (String)session.getAttribute("userAdd_azioniPerRiconciliazioneManuale" );
		attivazioneEstrattoContoManager = (String)session.getAttribute("userAdd_attivazioneEstrattoContoManager" );
		abilitazioneProfiloRiversamento = (String)session.getAttribute("userAdd_abilitazioneProfiloRiversamento" );
		prenotazioneFatturazione = (String)session.getAttribute("userAdd_prenotazioneFatturazione" );
		//dom R
		mailContogestione = (String)session.getAttribute("userAdd_mailContogestione" );
		configurazione = (String)session.getAttribute("userAdd_configurazione" );
		adminusers = (String)session.getAttribute("userAdd_adminusers" );
		ecmanager = (String)session.getAttribute("userAdd_ecmanager" );

		richiesteelaborazioni = (String)session.getAttribute("userAdd_richiesteelaborazioni");
		inviaufficio = (String)session.getAttribute("userAdd_inviaufficio");
		//inizio LP PG200360
		if(serviziAttiviOn(request)) {
			serviziattivi = (String) session.getAttribute("userAdd_serviziattivi");
		}
		//fine LP PG200360
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		ecanagrafica= (String)session.getAttribute("userAdd_ecanagrafica" );
		ecuffmanager = (String)session.getAttribute("userAdd_ecuffmanager" );
		ecnotifiche = (String)session.getAttribute("userAdd_ecnotifiche" );
		ecagemanager = (String)session.getAttribute("userAdd_ecagemanager" );	//PG180300 20190325 CT
		//inizio LP PG200060
		}
		//fine LP PG200060
		monitoraggio = (String)session.getAttribute("userAdd_monitoraggio" );
		log = (String)session.getAttribute("userAdd_log" );
		//inizio LP PG21X007
		if(logRequestOn(request)) {
			logrequest = (String) session.getAttribute("userAdd_logrequest");
		}
		//fine LP PG21X007
		rendicontazione = (String)session.getAttribute("userAdd_rendicontazione" );
		riconciliazione = (String)session.getAttribute("userAdd_riconciliazione" );
		riversamento = (String)session.getAttribute("userAdd_riversamento" );

		eccedenze = (String)session.getAttribute("userAdd_eccedenze" );
		monitoraggioext = (String)session.getAttribute("userAdd_monitoraggioext" );
		//PG150180_001 GG - inizio
		monitoraggionn = (String)session.getAttribute("userAdd_monitoraggionn" );
		riconciliazionenn = (String)session.getAttribute("userAdd_riconciliazionenn" );
		//PG150180_001 GG - fine
		analysis = (String)session.getAttribute("userAdd_analysis" );
		ottico = (String)session.getAttribute("userAdd_ottico" );
		entrate = (String)session.getAttribute("userAdd_entrate" );
// dom	R	
		contogestione = (String)session.getAttribute("userAdd_contogestione" );

		ruoli = (String)session.getAttribute("userAdd_ruoli" );
		wismanager = (String)session.getAttribute("userAdd_wismanager" );
		wisconfig = (String)session.getAttribute("userAdd_wisconfig" );
		monitoraggiowis = (String)session.getAttribute("userAdd_monitoraggiowis" );
		blackboxconfig = (String)session.getAttribute("userAdd_blackboxconfig" ); //PG200140
		modello3config = (String)session.getAttribute("userAdd_modello3config" ); //PG200140
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		walletmanager = (String)session.getAttribute("userAdd_walletmanager" );
		walletmonitoraggio = (String)session.getAttribute("userAdd_walletmonitoraggio" );
		walletanagraficacontribuenti = (String)session.getAttribute("userAdd_walletanagraficacontribuenti" );
		walletricaricheborsellino = (String)session.getAttribute("userAdd_walletricaricheborsellino" );
		walletsollecitodiscarico = (String)session.getAttribute("userAdd_walletsollecitodiscarico" );
		walletservizio = (String)session.getAttribute("userAdd_walletservizio" );
		walletconfig = (String)session.getAttribute("userAdd_walletconfig" );
		
		
		//PG180040 - Mercati
		mercatoconfig = (String)session.getAttribute("userAdd_mercatoconfig" );
		mercatimanager = (String)session.getAttribute("userAdd_mercatimanager" );
		monitoraggiomercati = (String)session.getAttribute("userAdd_monitoraggiomercati");
	
		//PG180010 - inizio
		riconciliazionemt = (String)session.getAttribute("userAdd_riconciliazionemt");
		associazioniProvvisorieRiconciliazionemt = (String)session.getAttribute("userAdd_associazioniProvvisorieRiconciliazionemt");
		associazioniDefinitiveRiconciliazionemt = (String)session.getAttribute("userAdd_associazioniDefinitiveRiconciliazionemt");
		//inizio LP PG200060
		}else {
			monitoraggiocup = (String)session.getAttribute("userAdd_monitoraggiocup");
			monitoraggiocruss = (String)session.getAttribute("userAdd_monitoraggiocruss");
		}
		//fine LP PG200060

//		YLM PG22XX07 INI
		monitoraggiosoap = (String)session.getAttribute("userAdd_monitoraggiosoap");
//		YLM PG22XX07 FINE
		gestioneuffici = (String)session.getAttribute("userAdd_gestioneuffici" ); //SVILUPPO_001_LUCAP_30062020
		ioitalia = (String)session.getAttribute("userAdd_ioitalia" ); // PG210160
		
		//PG200120
		blackboxpos = (String)session.getAttribute("userAdd_blackboxpos" ); 
		blackboxposlog = (String)session.getAttribute("userAdd_blackboxposlog" ); 
		//FINE PG200120
		
	}

	protected void setForm1AttributesFromWs(HttpSession session, HttpServletRequest request,SelezionaUtenteResponseType selezionaRes)
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		PyUserType pyUser = selezionaRes.getPyUser();
		
		chiaveUtente = pyUser.getChiaveUtente();
		userProfile = pyUser.getUserProfile();
		codiceSocieta = pyUser.getCodiceSocieta();
		codiceUtente = pyUser.getCodiceUtente();
		codiceEnte = pyUser.getChiaveEnteConsorzio();
		username = pyUser.getUserName();
		codiceFiscale = pyUser.getUserName(); //utilizzato solo per Lepida
		listatipologiaservizio = pyUser.getListaTipologiaServizio();
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		entePertinenza = pyUser.getEntePertinenza();	//EP160510_001 GG 03112016
		codiceGruppoAgenzia = pyUser.getGruppoAgenzia(); // RE180181 SB
		//inizio LP PG200060
		}
		//fine LP PG200060

		request.setAttribute("tx_chiaveutente_hidden", chiaveUtente);
		request.setAttribute("tx_username_hidden", username);
		request.setAttribute("tx_userprofile_hidden", userProfile);
		request.setAttribute("tx_societa_hidden", codiceSocieta);
		request.setAttribute("tx_utente_hidden", codiceUtente);
		request.setAttribute("tx_ente_hidden", codiceEnte);
		request.setAttribute("tx_codiceFiscale_hidden", codiceFiscale);
		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		request.setAttribute("tx_pertinenza_hidden", entePertinenza);	//EP160510_001 GG 03112016
		request.setAttribute("tx_gruppo_agenzia_hidden", codiceGruppoAgenzia);	//RE180181 SB
		//inizio LP PG200060
		}
		//fine LP PG200060
		
		request.setAttribute("chk_abilitazioneMultiUtente", pyUser.getAbilitazioneMultiUtente().equalsIgnoreCase("Y"));
		request.setAttribute("chk_utenzaAttiva", pyUser.getFlagAttivazione().equalsIgnoreCase("Y"));
		request.setAttribute("tx_tipologia_servizio_hidden", listatipologiaservizio);
	}

	protected void setForm2AttributesFromWs(HttpServletRequest request,SelezionaUtenteResponseType selezionaRes)
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		PyUserType pyUser = selezionaRes.getPyUser();
		listaApplicazioni = Arrays.asList(selezionaRes.getApplicazioni());

		request.setAttribute("chk_downloadFlussiRendicontazione", pyUser.getDownloadFlussiRendicontazione().equalsIgnoreCase("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaFtp", pyUser.getInvioFlussiRendicontazioneViaFtp().equalsIgnoreCase("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaEmail", pyUser.getInvioFlussiRendicontazioneViaEmail().equalsIgnoreCase("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaWs", pyUser.getInvioFlussiRendicontazioneViaWs().equalsIgnoreCase("Y"));
		request.setAttribute("chk_azioniPerTransazioniOK", pyUser.getAzioniPerTransazioniOK().equalsIgnoreCase("Y"));
		request.setAttribute("chk_azioniPerTransazioniKO", pyUser.getAzioniPerTransazioniKO().equalsIgnoreCase("Y"));
		request.setAttribute("chk_azioniPerRiconciliazioneManuale", pyUser.getAzioniPerRiconciliazioneManuale().equalsIgnoreCase("Y"));
		request.setAttribute("chk_attivazioneEstrattoContoManager", pyUser.getAttivazioneEstrattoContoManager().equalsIgnoreCase("Y"));
		request.setAttribute("chk_abilitazioneProfiloRiversamento", pyUser.getAbilitazioneProfiloRiversamento().equalsIgnoreCase("Y"));
		request.setAttribute("chk_prenotazioneFatturazione",  pyUser.getFlagPrenotazioneFatturazione().equalsIgnoreCase("Y"));

		//inizio LP PG200060
		if(!template.equals("regmarche")) {
		//fine LP PG200060
		//PG180010
		request.setAttribute("chk_associazioniDefinitiveRiconciliazionemt", pyUser.getAssociazioniDefinitiveRiconciliazionemt().equalsIgnoreCase("Y"));
		request.setAttribute("chk_associazioniProvvisorieRiconciliazionemt", pyUser.getAssociazioniProvvisorieRiconciliazionemt().equalsIgnoreCase("Y"));
		//inizio LP PG200060
		}
		//fine LP PG200060
		
		
		//dom R
		request.setAttribute("chk_mailContogestione", pyUser.getMailContoGestione().equalsIgnoreCase("Y"));
		
		if (listaApplicazioni != null && !listaApplicazioni.isEmpty() )
		{
			request.setAttribute("chk_configurazione",  listaApplicazioni.contains("configurazione"));
			request.setAttribute("chk_adminusers",  listaApplicazioni.contains("adminusers"));
			request.setAttribute("chk_ecmanager",  listaApplicazioni.contains("ecmanager"));
			//inizio LP PG200360
			if(serviziAttiviOn(request)) {
				request.setAttribute("chk_serviziattivi", listaApplicazioni.contains("serviziattivi"));
			}
			//fine LP PG200360
			//inizio LP PG200060
			if(!template.equals("regmarche")) {
			//fine LP PG200060
			request.setAttribute("chk_ecanagrafica",  listaApplicazioni.contains("ecanagrafica"));
			request.setAttribute("chk_ecuffmanager",  listaApplicazioni.contains("ecuffmanager"));
			request.setAttribute("chk_enotifiche",  listaApplicazioni.contains("ecnotifiche"));
			request.setAttribute("chk_ecagemanager",  listaApplicazioni.contains("ecagemanager"));	//PG180300 20190325 CT
			//inizio LP PG200060
			}
			//fine LP PG200060
			request.setAttribute("chk_log",  listaApplicazioni.contains("log"));
			//inizio LP PG21X007
			if(logRequestOn(request)) {
				request.setAttribute("chk_logrequest", listaApplicazioni.contains("logrequest"));
			}
			//fine LP PG21X007
			request.setAttribute("chk_monitoraggio",  listaApplicazioni.contains("monitoraggio"));
			request.setAttribute("chk_rendicontazione",  listaApplicazioni.contains("rendicontazione"));
			request.setAttribute("chk_riconciliazione",  listaApplicazioni.contains("riconciliazione"));
			request.setAttribute("chk_riversamento",  listaApplicazioni.contains("riversamento"));

			request.setAttribute("chk_eccedenze",  listaApplicazioni.contains("eccedenze"));
			request.setAttribute("chk_monitoraggioext",  listaApplicazioni.contains("monitoraggioext"));
			//PG150180_001 GG - inizio
			request.setAttribute("chk_monitoraggionn",  listaApplicazioni.contains("monitoraggionn"));
			request.setAttribute("chk_riconciliazionenn",  listaApplicazioni.contains("riconciliazionenn"));
			//PG150180_001 GG - fine
			request.setAttribute("chk_analysis",  listaApplicazioni.contains("analysis"));
			request.setAttribute("chk_ottico",  listaApplicazioni.contains("ottico"));
			request.setAttribute("chk_entrate",  listaApplicazioni.contains("entrate"));
//dom R
			request.setAttribute("chk_contogestione",  listaApplicazioni.contains("contogestione"));

			request.setAttribute("chk_ruoli",  listaApplicazioni.contains("ruoli"));
			//inizio LP PG200060
			if(!template.equals("regmarche")) {
			//fine LP PG200060
			request.setAttribute("chk_riconciliazionemt",  listaApplicazioni.contains("riconciliazionemt")); //PG180010
			//inizio LP PG200060
			}
			//fine LP PG200060

			request.setAttribute("chk_richiesteelaborazioni",  listaApplicazioni.contains("richiesteelaborazioni"));

			request.setAttribute("chk_inviaufficio",  listaApplicazioni.contains("inviaufficio"));
		}
	}

	protected void checkProfileAndSetForm2AttributesFromWs(HttpServletRequest request,SelezionaUtenteResponseType selezionaRes)
	{
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
		PyUserType pyUser = selezionaRes.getPyUser();
		String dbUserProfile = pyUser.getUserProfile();
		/*
		 * 1) Se il profilo scelto è uguale a quello precedente e diverso da "AMMI"
		 *    carico il dati dal WS
		 * 2) Se il profilo scelto è uguale ad "AMMI"
		 *    setto tutti i flag altrimenti li inizializzo a seconda del profilo
		 */
		if (userProfile.equals(dbUserProfile))
		{
			if (!userProfile.equals("AMMI"))
			{
				if(selezionaRes.getApplicazioni()!=null)
					listaApplicazioni = Arrays.asList(selezionaRes.getApplicazioni());
				
				request.setAttribute("chk_downloadFlussiRendicontazione", pyUser.getDownloadFlussiRendicontazione().equalsIgnoreCase("Y"));
				request.setAttribute("chk_invioFlussiRendicontazioneViaFtp", pyUser.getInvioFlussiRendicontazioneViaFtp().equalsIgnoreCase("Y"));
				request.setAttribute("chk_invioFlussiRendicontazioneViaEmail", pyUser.getInvioFlussiRendicontazioneViaEmail().equalsIgnoreCase("Y"));
				request.setAttribute("chk_invioFlussiRendicontazioneViaWs", pyUser.getInvioFlussiRendicontazioneViaWs().equalsIgnoreCase("Y"));
				request.setAttribute("chk_azioniPerTransazioniOK", pyUser.getAzioniPerTransazioniOK().equalsIgnoreCase("Y"));
				request.setAttribute("chk_azioniPerTransazioniKO", pyUser.getAzioniPerTransazioniKO().equalsIgnoreCase("Y"));
				request.setAttribute("chk_azioniPerRiconciliazioneManuale", pyUser.getAzioniPerRiconciliazioneManuale().equalsIgnoreCase("Y"));
				request.setAttribute("chk_attivazioneEstrattoContoManager", pyUser.getAttivazioneEstrattoContoManager().equalsIgnoreCase("Y"));
				request.setAttribute("chk_abilitazioneProfiloRiversamento", pyUser.getAbilitazioneProfiloRiversamento().equalsIgnoreCase("Y"));
				request.setAttribute("chk_prenotazioneFatturazione", pyUser.getFlagPrenotazioneFatturazione().equalsIgnoreCase("Y"));

				//inizio LP PG200060
				if(!template.equals("regmarche")) {
				//fine LP PG200060
				//PG180010
				request.setAttribute("chk_associazioniDefinitiveRiconciliazionemt", pyUser.getAssociazioniDefinitiveRiconciliazionemt().equalsIgnoreCase("Y"));
				request.setAttribute("chk_associazioniProvvisorieRiconciliazionemt", pyUser.getAssociazioniProvvisorieRiconciliazionemt().equalsIgnoreCase("Y"));
				//inizio LP PG200060
				}
				//fine LP PG200060
				//dom R
				request.setAttribute("chk_mailContogestione", pyUser.getMailContoGestione().equalsIgnoreCase("Y"));
				
				
				if (listaApplicazioni != null && !listaApplicazioni.isEmpty() )
				{
					request.setAttribute("chk_configurazione",  listaApplicazioni.contains("configurazione"));
					request.setAttribute("chk_adminusers",  listaApplicazioni.contains("adminusers"));
					request.setAttribute("chk_ecmanager",  listaApplicazioni.contains("ecmanager"));
					//inizio LP PG200360
					if(serviziAttiviOn(request)) {
						request.setAttribute("chk_serviziattivi", listaApplicazioni.contains("serviziattivi"));
					}
					//fine LP PG200360
					//inizio LP PG200060
					if(!template.equals("regmarche")) {
					//fine LP PG200060
					request.setAttribute("chk_ecanagrafica",  listaApplicazioni.contains("ecanagrafica"));
					request.setAttribute("chk_ecuffmanager",  listaApplicazioni.contains("ecuffmanager"));
					request.setAttribute("chk_ecnotifiche",  listaApplicazioni.contains("ecnotifiche"));
					request.setAttribute("chk_ecagemanager",  listaApplicazioni.contains("ecagemanager"));	//PG180300 20190325 CT
					//inizio LP PG200060
					}
					//fine LP PG200060
					request.setAttribute("chk_log",  listaApplicazioni.contains("log"));
					//inizio LP PG21X007
					if(logRequestOn(request)) {
						request.setAttribute("chk_logrequest", listaApplicazioni.contains("logrequest"));
					}
					//fine LP PG21X007
					request.setAttribute("chk_monitoraggio",  listaApplicazioni.contains("monitoraggio"));
					request.setAttribute("chk_rendicontazione",  listaApplicazioni.contains("rendicontazione"));
					request.setAttribute("chk_riconciliazione",  listaApplicazioni.contains("riconciliazione"));
					request.setAttribute("chk_riversamento",  listaApplicazioni.contains("riversamento"));
	
					request.setAttribute("chk_eccedenze",  listaApplicazioni.contains("eccedenze"));
					request.setAttribute("chk_monitoraggioext",  listaApplicazioni.contains("monitoraggioext"));
					//PG150180_001 GG - inizio
					request.setAttribute("chk_monitoraggionn",  listaApplicazioni.contains("monitoraggionn"));
					request.setAttribute("chk_riconciliazionenn",  listaApplicazioni.contains("riconciliazionenn"));
					//PG150180_001 GG - fine
					request.setAttribute("chk_analysis",  listaApplicazioni.contains("analysis"));
					request.setAttribute("chk_ottico",  listaApplicazioni.contains("ottico"));
					request.setAttribute("chk_entrate",  listaApplicazioni.contains("entrate"));
					//dom R
					request.setAttribute("chk_contogestione",  listaApplicazioni.contains("contogestione"));

					request.setAttribute("chk_ruoli",  listaApplicazioni.contains("ruoli"));
					request.setAttribute("chk_wismanager",  listaApplicazioni.contains("wismanager"));
					request.setAttribute("chk_wisconfig",  listaApplicazioni.contains("wisconfig"));
					request.setAttribute("chk_monitoraggiowis",  listaApplicazioni.contains("monitoraggiowis"));
					request.setAttribute("chk_blackboxconfig",  listaApplicazioni.contains("blackboxconfig")); //PG200140
					request.setAttribute("chk_modello3config",  listaApplicazioni.contains("modello3config")); //PG200140
					//inizio LP PG200060
					if(!template.equals("regmarche")) {
						//fine LP PG200060
						request.setAttribute("chk_walletmanager",  listaApplicazioni.contains("walletmanager"));
						request.setAttribute("chk_walletmonitoraggio",  listaApplicazioni.contains("walletmonitoraggio"));
						request.setAttribute("chk_walletanagraficacontribuenti",  listaApplicazioni.contains("walletanagraficacontribuenti"));
						request.setAttribute("chk_walletricaricheborsellino",  listaApplicazioni.contains("walletricaricheborsellino"));
						request.setAttribute("chk_walletsollecitodiscarico",  listaApplicazioni.contains("walletsollecitodiscarico"));
						request.setAttribute("chk_walletservizio",  listaApplicazioni.contains("walletservizio"));
						request.setAttribute("chk_walletconfig",  listaApplicazioni.contains("walletconfig"));
						//PG180040 - Mercati
						request.setAttribute("chk_mercatoconfig",  listaApplicazioni.contains("mercatoconfig"));
						request.setAttribute("chk_mercatimanager",  listaApplicazioni.contains("mercatimanager"));
						request.setAttribute("chk_monitoraggiomercati", listaApplicazioni.contains("monitoraggiomercati"));
						request.setAttribute("chk_riconciliazionemt", listaApplicazioni.contains("riconciliazionemt"));
						//inizio LP PG200060
					}else {
						request.setAttribute("chk_monitoraggiocup",  listaApplicazioni.contains("monitoraggiocup"));
						request.setAttribute("chk_monitoraggiocruss",  listaApplicazioni.contains("monitoraggiocruss"));
					}
					//fine LP PG200060

//					YLM PG22XX07 INI
					request.setAttribute("chk_monitoraggiosoap",  listaApplicazioni.contains("monitoraggiosoap"));
//					YLM PG22XX07 FINE
					request.setAttribute("chk_gestioneuffici",  listaApplicazioni.contains("gestioneuffici")); //SVILUPPO_001_LUCAP_30062020
					
					request.setAttribute("chk_ioitalia",  listaApplicazioni.contains("ioitalia")); // PG210160
					
					//PG200120
					request.setAttribute("chk_blackboxpos",  listaApplicazioni.contains("blackboxpos")); 
					request.setAttribute("chk_blackboxposlog",  listaApplicazioni.contains("blackboxposlog"));
					//FINE PG200120

					request.setAttribute("chk_richiesteelaborazioni",  listaApplicazioni.contains("richiesteelaborazioni"));
					request.setAttribute("chk_inviaufficio",  listaApplicazioni.contains("inviaufficio"));
				}
			}
			else
				setAllForm2FlagForAMMI(request);
		}
		else
			initForm2FlagForProfile(request, null, userProfile);
		
		if (userProfile.equals("AMMI"))
			setAllForm2FlagForAMMI(request);
	}
	
	/**
	 * Questo metodo setta come "disabled" le check-box delle
	 * applicazioni che non sono compatibili con il profilo dell'utente - 
	 * In tal modo non è possibile associarle all'utente.
	 * @param request
	 * @param session
	 */
	protected void setApplsDisabled(HttpServletRequest request,HttpSession session)
	{
		String applicationName = null;
		String profilo = (String)session.getAttribute("userAdd_userProfile");
		if (profilo != null && !profilo.equals(""))
		{
			ServletContext context = session.getServletContext();
			ApplicationsData applicationsData = (ApplicationsData) context.getAttribute(MAFAttributes.APPLICATIONS);
			if (applicationsData != null)
			{
				Iterator<String> itr = applicationsData.getApplicationsIterator();
				RealmProfilesManager profileManager = applicationsData.getRealmProfilesManager();
				HashMap<String, Boolean> protectedMap = applicationsData.getProtectedApplicationsMap();
				while (itr.hasNext())
				{
					applicationName = itr.next();
					/*
					 * Se l'applicazione è protetta 
					 * viene presa in considerazione
					 */
					if (protectedMap.containsKey(applicationName)) 
					{
						/*
						 * Il check-box relativo all'applicazione
						 * viene disabilitato se il profilo è "AMMI"
						 * o se non passa il controllo 
						 */
				    	 request.setAttribute(applicationName.concat("Disabled"), profilo.equals("AMMI") ? true : !profileManager.checkPassThrough(applicationName, profilo));
					}
				}
				
			}
		}
	}
	
	/**
	 * Abilita/disabilita le drop-down-list Società, Utente ed Ente
	 * in funzione del profilo dell utente da inserire
	 * 
	 * @param profiloUtente
	 */
	public void setDDL (HttpServletRequest request, ProfiloUtente profiloUtente)
	{
		switch(profiloUtente)
		{
			case AMMI:
				request.setAttribute("ddlSocietaDisabled", true);
				request.setAttribute("ddlUtenteDisabled", true);
				request.setAttribute("ddlEnteDisabled", true);
				request.setAttribute("ddlTipologieServizioDisabled", true);
				request.setAttribute("ddlTipologieServizioSelDisabled", true);
				request.setAttribute("addTipologiaServizioDisabled", true);
				request.setAttribute("removeTipologiaServizioDisabled", true);
				break;
			case AMSO:
				request.setAttribute("ddlSocietaDisabled", false);
				request.setAttribute("ddlUtenteDisabled", true);
				request.setAttribute("ddlEnteDisabled", true);
				request.setAttribute("ddlTipologieServizioDisabled", true);
				request.setAttribute("ddlTipologieServizioSelDisabled", true);
				request.setAttribute("addTipologiaServizioDisabled", true);
				request.setAttribute("removeTipologiaServizioDisabled", true);
				break;
			case AMUT:
				request.setAttribute("ddlSocietaDisabled", false);
				request.setAttribute("ddlUtenteDisabled", false);
				request.setAttribute("ddlEnteDisabled", true);
				request.setAttribute("ddlTipologieServizioDisabled", true);
				request.setAttribute("ddlTipologieServizioSelDisabled", true);
				request.setAttribute("addTipologiaServizioDisabled", true);
				request.setAttribute("removeTipologiaServizioDisabled", true);
				break;
			case AMEN:
				request.setAttribute("ddlSocietaDisabled", false);
				request.setAttribute("ddlUtenteDisabled", false);
				request.setAttribute("ddlEnteDisabled", false);
				request.setAttribute("ddlTipologieServizioDisabled", false);
				request.setAttribute("ddlTipologieServizioSelDisabled", false);
				request.setAttribute("addTipologiaServizioDisabled", false);
				request.setAttribute("removeTipologiaServizioDisabled", false);
				break;
			case AMNULL:
				request.setAttribute("ddlSocietaDisabled", true);
				request.setAttribute("ddlUtenteDisabled", true);
				request.setAttribute("ddlEnteDisabled", true);
				request.setAttribute("ddlTipologieServizioDisabled", true);
				request.setAttribute("ddlTipologieServizioSelDisabled", true);
				request.setAttribute("addTipologiaServizioDisabled", true);
				request.setAttribute("removeTipologiaServizioDisabled", true);
				break;
		}
	}
	/**
	 * Creo l'HashMap per gestire le prima DropDownList
	 * @return 
	 * @return 
	 */
	protected  HashMap<String, String> getCreateHashMap(String lista)
	{
		HashMap<String,String> DDLTipologieServizio = new HashMap<String, String>();
		//inizio LP PG21XX04 Leak
		WebRowSet webRowSet = null;
		//fine LP PG21XX04 Leak
		try {
			
			//inizio LP PG21XX04 Leak
			//WebRowSet webRowSet= Convert.stringToWebRowSet(lista);
			webRowSet = Convert.stringToWebRowSet(lista);
			//fine LP PG21XX04 Leak
			//Ciclo For - popolo la prima Hashmap con i dati dell' xml.
			
			for (int i = 0; i<webRowSet.size(); i++) {
				webRowSet.next();
				codicetipologiaservizio = webRowSet.getString("TSE_CTSECTSE");
				descrizionetipologiaservizio = webRowSet.getString("TSE_DTSEDTSE");
				DDLTipologieServizio.put(codicetipologiaservizio, descrizionetipologiaservizio);
			}
		} 
		catch (Exception e)  {
			//return DDLTipologieServizio ;
			System.out.println(e.getMessage());
		} 
		//inizio LP PG21XX04 Leak
		finally
		{
	    	try {
	    		if(webRowSet != null) {
	    			webRowSet.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return DDLTipologieServizio;
	} 
		
	/**
	 * Creo l'HashMap per gestire le seconda DropDownList
	 * @return 
	 * @return 
	 */
	protected  HashMap<String, String> getCreateHashMap_2(String lista2)
	{
		HashMap<String,String> DDLTipologieServizioSel = new HashMap<String, String>();
		//inizio LP PG21XX04 Leak
		WebRowSet webRowSet = null;
		//fine LP PG21XX04 Leak
		try {
			if (lista2!=null && lista2.length()>0) {
				
				//inizio LP PG21XX04 Leak
				//WebRowSet webRowSet= Convert.stringToWebRowSet(lista2);
				webRowSet = Convert.stringToWebRowSet(lista2);
				//fine LP PG21XX04 Leak
			//Ciclo For - popolo la seconda Hashmap con i dati dell' xml.
			
				for (int i = 0; i<webRowSet.size(); i++) {
					webRowSet.next();
					codicetipologiaservizio = webRowSet.getString("TSE_CTSECTSE");
					descrizionetipologiaservizio = webRowSet.getString("TSE_DTSEDTSE");
					DDLTipologieServizioSel.put(codicetipologiaservizio, descrizionetipologiaservizio);
				}
			}
		} 
		catch (Exception e)  {
			//return DDLTipologieServizio ;
			System.out.println(e.getMessage());
		} 
		//inizio LP PG21XX04 Leak
		finally
		{
	    	try {
	    		if(webRowSet != null) {
	    			webRowSet.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return DDLTipologieServizioSel;
	} 
		
	protected UtentePIVA newUtentePIVA(HttpServletRequest request, Profilo profilo, String codop)
	{	
		Calendar now = Calendar.getInstance();
		HttpSession session = request.getSession();
		Calendar dataFineValiditaUtenzaSync = null;
		Calendar dataScadenzaFromWs = null;
		Calendar dataFineValiditaUtenza = profilo.getDataFineValiditaUtenza();
		UserBean userBean = (UserBean)session.getAttribute("j_user_bean");
		
		if (codop.equals("edit")) dataScadenzaFromWs = (Calendar)session.getAttribute("dataScadenzaFromWs");
		/*
		 * Sincronizzo "dataFineValiditaUtenza" con "now" se si tratta di un inserimento
		 * oppure di una modifica in cui sia stata modificata la data di fine validità dell'utenza
		 */
		if ((codop.equals("add")) ||
			(codop.equals("edit") && dataScadenzaFromWs != null &&
					(dataFineValiditaUtenza.get(Calendar.YEAR) != dataScadenzaFromWs.get(Calendar.YEAR) ||
					 dataFineValiditaUtenza.get(Calendar.MONTH) != dataScadenzaFromWs.get(Calendar.MONTH) ||
					 dataFineValiditaUtenza.get(Calendar.DAY_OF_MONTH) != dataScadenzaFromWs.get(Calendar.DAY_OF_MONTH) )
			))
		{
			dataFineValiditaUtenzaSync = (Calendar)now.clone();
			dataFineValiditaUtenzaSync.set(dataFineValiditaUtenza.get(Calendar.YEAR),dataFineValiditaUtenza.get(Calendar.MONTH),dataFineValiditaUtenza.get(Calendar.DAY_OF_MONTH));
		}
		com.seda.security.webservice.dati.UtentePIVA utentePIVA = new UtentePIVA();
		
				
		
		DatiUtentePIVA datiUtentePIVA = new DatiUtentePIVA();
		
		DatiGeneraliPIVA datiGeneraliPIVA = new DatiGeneraliPIVA();
		SedeLegale sedeLegale = new SedeLegale();
		SedeOperativa sedeOperativa = new SedeOperativa();
		ContattiPIVA contattiPIVA = new ContattiPIVA();
		
		DatiAnagPersonaFisicaDelegato datiAnagPersonaFisicaDelegato = new DatiAnagPersonaFisicaDelegato();
		
		DatiGeneraliPersonaFisicaDelegato datiGeneraliPersonaFisicaDelegato = new DatiGeneraliPersonaFisicaDelegato();
		DatidiNascitaPersonaFisicaDelegato datidiNascitaPersonaFisicaDelegato = new DatidiNascitaPersonaFisicaDelegato();
		DatiDocumentoPersonaFisicaDelegato datiDocumentoPersonaFisicaDelegato = new DatiDocumentoPersonaFisicaDelegato();
		DatiResidenzaPersonaFisicaDelegato datiResidenzaPersonaFisicaDelegato = new DatiResidenzaPersonaFisicaDelegato();
		DatiDomicilioPersonaFisicaDelegato datiDomicilioPersonaFisicaDelegato = new DatiDomicilioPersonaFisicaDelegato();
		DatiContattiPersonaFisicaDelegato datiContattiPersonaFisicaDelegato = new DatiContattiPersonaFisicaDelegato();
		
		utentePIVA.setUsername(profilo.getUsername());
		utentePIVA.setTipologiaUtente(UtentePIVATipologiaUtente.fromString(profilo.getTipologiaUtente()));
		utentePIVA.setUtenzaAttiva(UtentePIVAUtenzaAttiva.fromString(profilo.isUtenzaAttiva()? "Y": "N"));
		utentePIVA.setDataInizioValiditaUtenza(null);
		utentePIVA.setDataFineValiditaUtenza(dataFineValiditaUtenzaSync);
		utentePIVA.setPrimoAccesso(UtentePIVAPrimoAccesso.fromString("Y"));
		utentePIVA.setDataScadenzaPassword(null);
		utentePIVA.setDataUltimoAccesso(null);
		utentePIVA.setDataInserimentoUtenza(null);
		utentePIVA.setNote(profilo.getNote());
		utentePIVA.setDataUltimoAggiornamento(now);
		utentePIVA.setOperatoreUltimoAggiornamento(userBean.getUserName());
		utentePIVA.setFlagOperatoreBackOffice(UtentePIVAFlagOperatoreBackOffice.fromString(profilo.isOperatoreBackOffice()? "Y": "N"));
				
		datiGeneraliPIVA.setPartitaIVA(profilo.getPartitaIVA().toUpperCase());
		datiGeneraliPIVA.setRagioneSociale(profilo.getRagioneSociale());
		datiGeneraliPIVA.setClassificazioneMerceologica(!profilo.getClassificazioneMerceologicaDettaglio().equals("") ? profilo.getClassificazioneMerceologicaDettaglio() : "xx.xx.xx");
		//datiGeneraliPIVA.setClassificazioneMerceologica(profilo.getClassificazioneMerceologica() != null ? profilo.getClassificazioneMerceologica() : "xx.xx.xx");
		datiGeneraliPIVA.setNumeroAutorizzazione(profilo.getNumeroAutorizzazione());
		datiUtentePIVA.setDatiGeneraliPIVA(datiGeneraliPIVA);
		
		sedeLegale.setIndirizzoSedeLegale(profilo.getIndirizzoSedeLegale());
		sedeLegale.setCodbelfSedeLegale(profilo.getComuneSedeLegaleBelfiore());
		sedeLegale.setProvinciaSedeLegale(profilo.getProvinciaSedeLegaleSigla());
		sedeLegale.setCapComuneSedeLegale(profilo.getCapSedeLegale());
		sedeLegale.setEsteroSedeLegale(SedeLegaleEsteroSedeLegale.fromString(profilo.getComuneSedeLegaleEstero()));
		datiUtentePIVA.setSedeLegale(sedeLegale);
		
		sedeOperativa.setIndirizzoSedeOperativa(profilo.getIndirizzoSedeOperativa());
		sedeOperativa.setProvinciaSedeOperativa(profilo.getProvinciaSedeOperativaSigla());
		sedeOperativa.setCodbelfSedeOperativa(profilo.getComuneSedeOperativaBelfiore());
		sedeOperativa.setCapComuneSedeOperativa(profilo.getCapSedeOperativa());
		datiUtentePIVA.setSedeOperativa(sedeOperativa);
		
		contattiPIVA.setMailUtentePIVA(profilo.getEmailImpresa());
		contattiPIVA.setPecUtentePIVA(profilo.getEmailPECImpresa());
		contattiPIVA.setCellulareUtentePIVA(profilo.getCellulareImpresa());
		contattiPIVA.setTelefonoUtentePIVA(profilo.getTelefonoImpresa());
		contattiPIVA.setFaxUtentePIVA(profilo.getFaxImpresa());
		datiUtentePIVA.setContattiPIVA(contattiPIVA);
		
		utentePIVA.setDatiUtentePIVA(datiUtentePIVA);
		
		datiGeneraliPersonaFisicaDelegato.setCodiceFiscale(profilo.getCodiceFiscale().toUpperCase());
		datiGeneraliPersonaFisicaDelegato.setNome(profilo.getNome());
		datiGeneraliPersonaFisicaDelegato.setCognome(profilo.getCognome());
		datiGeneraliPersonaFisicaDelegato.setSesso(DatiGeneraliPersonaFisicaDelegatoSesso.fromString(profilo.getSesso()));
		datiAnagPersonaFisicaDelegato.setDatiGeneraliPersonaFisicaDelegato(datiGeneraliPersonaFisicaDelegato);
		
		datidiNascitaPersonaFisicaDelegato.setDataDiNascita(new java.util.Date(profilo.getDataNascita().getTimeInMillis()));
		datidiNascitaPersonaFisicaDelegato.setProvinciaDiNascita(profilo.getProvinciaNascitaSigla());
		datidiNascitaPersonaFisicaDelegato.setCodbelfDiNascita(profilo.getComuneNascitaBelfiore());
		datidiNascitaPersonaFisicaDelegato.setFlagEsteroNascita(DatidiNascitaPersonaFisicaDelegatoFlagEsteroNascita.fromString(profilo.getComuneNascitaEstero()));
		datiAnagPersonaFisicaDelegato.setDatidiNascitaPersonaFisicaDelegato(datidiNascitaPersonaFisicaDelegato);
		
		if(profilo.getTipoDocumento()!=null && !profilo.getTipoDocumento().equals(""))
			datiDocumentoPersonaFisicaDelegato.setTipoDocumentoRiconoscimento(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.fromString(profilo.getTipoDocumento()));
		if(profilo.getNumeroDocumento()!=null)
			datiDocumentoPersonaFisicaDelegato.setNumeroDocumentoRiconoscimento(profilo.getNumeroDocumento());
		if(profilo.getDataRilascioDocumento()!=null)
			datiDocumentoPersonaFisicaDelegato.setDataRilascioDocumento(new java.util.Date(profilo.getDataRilascioDocumento().getTimeInMillis()));
		if(profilo.getEnteRilascioDocumento()!=null)
			datiDocumentoPersonaFisicaDelegato.setEnteRilascioDocumento(profilo.getEnteRilascioDocumento());
		datiAnagPersonaFisicaDelegato.setDatiDocumentoPersonaFisicaDelegato(datiDocumentoPersonaFisicaDelegato);
		
		datiResidenzaPersonaFisicaDelegato.setIndirizzoResidenza(profilo.getIndirizzoResidenza());
		datiResidenzaPersonaFisicaDelegato.setProvinciaResidenza(profilo.getProvinciaResidenzaSigla());
		datiResidenzaPersonaFisicaDelegato.setCodbelfResidenza(profilo.getComuneResidenzaBelfiore());
		datiResidenzaPersonaFisicaDelegato.setCapComuneResidenza(profilo.getCapResidenza());
		datiResidenzaPersonaFisicaDelegato.setEsteroResidenza(DatiResidenzaPersonaFisicaDelegatoEsteroResidenza.fromString(profilo.getComuneResidenzaEstero()));
		datiAnagPersonaFisicaDelegato.setDatiResidenzaPersonaFisicaDelegato(datiResidenzaPersonaFisicaDelegato);
		
		datiDomicilioPersonaFisicaDelegato.setIndirizzoDomicilio(profilo.getIndirizzoDomicilio());
		datiDomicilioPersonaFisicaDelegato.setProvinciaDomicilio(profilo.getProvinciaDomicilioSigla());
		datiDomicilioPersonaFisicaDelegato.setCodbelfDomicilio(profilo.getComuneDomicilioBelfiore());
		datiDomicilioPersonaFisicaDelegato.setCapComuneDomicilio(profilo.getCapDomicilio());
		datiDomicilioPersonaFisicaDelegato.setEsteroDomicilio(DatiDomicilioPersonaFisicaDelegatoEsteroDomicilio.fromString(profilo.getComuneDomicilioEstero()));
		datiAnagPersonaFisicaDelegato.setDatiDomicilioPersonaFisicaDelegato(datiDomicilioPersonaFisicaDelegato);
		
		datiContattiPersonaFisicaDelegato.setMail(profilo.getEmail());
		datiContattiPersonaFisicaDelegato.setPec(profilo.getEmailPEC());
		datiContattiPersonaFisicaDelegato.setCellulare(profilo.getCellulare());
		datiContattiPersonaFisicaDelegato.setTelefono(profilo.getTelefono());
		datiContattiPersonaFisicaDelegato.setFax(profilo.getFax());
		datiAnagPersonaFisicaDelegato.setDatiContattiPersonaFisicaDelegato(datiContattiPersonaFisicaDelegato);
		
		utentePIVA.setDatiAnagPersonaFisicaDelegato(datiAnagPersonaFisicaDelegato);
		
		
		return utentePIVA;
		
	}

	/**
	 * UTENTE / PARTITA IVA
	 * Questa routine recupera i parametri dal form di inserimento o modifica
	 * per la tabella SEUSRTB 
	 * 
	 * @param request
	 * @param codop - "add" o "edit"
	 * @return
	 */
	protected Profilo getSeUsersFormParametersUtentePIVA(HttpServletRequest request, String codop, Profilo profilo)
	{
		/*
		 * INSERIMENTO
		 */
		if (codop.equals("add"))
		{
			String username = (request.getAttribute("tx_username") == null ? "" : (String)request.getAttribute("tx_username"));
			profilo.setUsername(username);
			
			String password = (request.getAttribute("tx_password") == null ? "" : (String)request.getAttribute("tx_password"));
			profilo.setPassword(password);
			
			boolean passwordAutogenerata = (request.getAttribute("passwordAutogenerata") == null ? false : true);
			profilo.setPasswordAutogenerata(passwordAutogenerata);
			
			String tipologiaUtente = (request.getAttribute("tipologiaUtente") == null ? "" : (String)request.getAttribute("tipologiaUtente"));
			profilo.setTipologiaUtente(tipologiaUtente);
			
		}
		/*
		 * MODIFICA
		 */
		else
		{
			String username = (request.getAttribute("tx_username_hidden") == null ? "" : (String)request.getAttribute("tx_username_hidden"));
			profilo.setUsername(username);
			
			String tipologiaUtente = (request.getAttribute("tx_tipologiautente_hidden") == null ? "" : (String)request.getAttribute("tx_tipologiautente_hidden"));
			profilo.setTipologiaUtente(tipologiaUtente);
			
			/*
			 * Setto gli attributi relativi ai campi disabilitati
			 * ricavati dai campi hidden
			 */
			request.setAttribute("tx_username_hidden", username);
			request.setAttribute("tx_tipologiautente_hidden", tipologiaUtente);

		}

		boolean utenzaAttiva = (request.getAttribute("utenzaAttiva") == null ? false : true);
		profilo.setUtenzaAttiva(utenzaAttiva);
		
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		if(!template.equalsIgnoreCase("regmarche")) {
		//fine LP PG200060

		boolean operatoreBackOffice = (request.getAttribute("operatoreBackOffice") == null ? false : true);
		profilo.setOperatoreBackOffice(operatoreBackOffice);
		
		//PG180170 GG 17102018 
		boolean flagNoControlli = (request.getAttribute("flagNoControlli") == null ? false : true);
		profilo.setFlagNoControlli(flagNoControlli);
		//inizio LP PG200060
		} else {
			profilo.setOperatoreBackOffice(false);
			profilo.setFlagNoControlli(false);
		}
		//fine LP PG200060
		

		String note = (request.getAttribute("note") == null ? "" : (String)request.getAttribute("note"));
		profilo.setNote(note);

		

		boolean nessunaScadenza = (request.getAttribute("nessunaScadenza") == null ? false : true);
		profilo.setNessunaScadenza(nessunaScadenza);

		Calendar dataFineValiditaUtenza = (nessunaScadenza ? getFine2099(request) : (Calendar)request.getAttribute("dataScadenza"));
		profilo.setDataFineValiditaUtenza(dataFineValiditaUtenza);

		return profilo;
	}
	
	
	/**
	 * UTENTE / PARTITA IVA
	 * Questo metodo effettua una validazione applicativa dei
	 * campi di input per la modifica e l'inserimento di un utente
	 * @param parametri
	 * @return
	 */
	protected String checkSeUsersInputFieldsUtentePIVA(Profilo prof, String codop, HttpServletRequest request)
	{
		Calendar now = Calendar.getInstance();

		//try 
		//{
			String password = prof.getPassword();
			if (codop.equals("add") && !prof.isPasswordAutogenerata() && password.equals(""))
				return Messages.SPECIFICARE_PASSWORD.format();
			
			if (codop.equals("add") && !prof.isPasswordAutogenerata() && !verificaComplessitaPWD(password))
				return Messages.SPECIFICARE_PASSWORD_VALIDA.format(".,!#%");

			/*
			 * CRITTOGRAFO LA PASSWORD
			 */
			/*
			if (codop.equals("add") && !(prof.isPasswordAutogenerata() && verificaComplessitaPWD(password)))
			{
				password = Crypto.encrypt(password);
				prof.setPassword(password);
			}*/
			
			if (!(prof.isNessunaScadenza()) && prof.getDataFineValiditaUtenza() == null)
				return Messages.SPECIFICARE_DATA_SCADENZA.format();
			
			if (!(prof.isNessunaScadenza()) && (prof.isUtenzaAttiva()) && (prof.getDataFineValiditaUtenza() != null) &&	!(prof.getDataFineValiditaUtenza().after(now)))
				return Messages.SPECIFICARE_DATA_SCADENZA_VALIDA.format();
			
			if ((prof.getTipologiaUtente().equals("")) || (prof.getUsername().equals("")))
				return Messages.CAMPI_OBBLIGATORI.format("*");
	
			/*
			} catch (UnsupportedEncodingException e) {
				return "Errore in fase di decrittografia della password - Contattare l'Amministratore";
			} catch (ChryptoServiceException e) {
				return "Errore2 in fase di decrittografia della password - Contattare l'Amministratore";
			}*/
			//inizio LP PG200130
			String template = getTemplateCurrentApplication(request, request.getSession());
			if(template.equalsIgnoreCase("soris")||template.equalsIgnoreCase("newsoris")) {
				if(request.getSession().getAttribute("profiloStatico")!=null) {
					Profilo profAppoggio = (Profilo)request.getSession().getAttribute("profiloStatico");
					if(formattedFieldString(profAppoggio).equals(formattedFieldString(prof))) {
						if ((!prof.isNessunaScadenza() || !prof.isUtenzaAttiva()) && prof.getNote().trim().length() == 0) {
							return Messages.NOTE_OBBLIGATORIE.format();
						}
					}
				}
			}
			//fine LP PG200130
			
			if (codop.equals("add"))
			{
				//verifica disponibilità username
				if (ProfiloUtil.verificaDisponibilitàUsername(prof.getUsername(), request))
					prof.setUsernameValid("Y");
				else
				{
					prof.setUsernameValid("N");
					return "Username: Non disponibile";
				}
			}
			
			return "OK";
	}
	
	/**
	 * UTENTE / PARTITA IVA
	 * Salva in sessione le informazioni del primo step per l'inserimento/modifica di un UTENTE / PARTITA IVA
	 * 
	 * @param request
	 */
	protected void AddProfiloInSession(HttpSession session, Profilo profilo) 
	{
		if (session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA) != null)
			session.removeAttribute(ManagerKeys.PROFILO_UTENTEPIVA);
		
		session.setAttribute(ManagerKeys.PROFILO_UTENTEPIVA, profilo);
		
	}
	
	/**
	 * UTENTE / PARTITA IVA
	 * Metto in request le informazioni del PROFILO
	 * 
	 * @param request
	 */
	protected void MoveProfiloInRequest(HttpServletRequest request, Profilo profilo) 
	{	
		request.setAttribute("reg_profilo", profilo);
	}
	/**
	 * UTENTE / PARTITA IVA
	 * Questo metodo recupera, per lo step della persona fisica i parametri dalla request
	 * 
	 * @param request
	 */
	protected Profilo getPersonaFisicaFromRequest(HttpServletRequest request, Profilo profilo) 
	{		
	    //dati generali
		profilo.setCognome((String)request.getAttribute("txtCognome"));
		profilo.setNome((String)request.getAttribute("txtNome"));
		profilo.setCodiceFiscale((String)request.getAttribute("txtCodFiscale"));
		profilo.setSesso((String)request.getAttribute("grSesso"));
		//dati nascita
		profilo.setDataNascita((Calendar)request.getAttribute("data_nascita"));
		String provinciaNascita = (String)request.getAttribute("ddlProvinciaNascita");
		if (provinciaNascita != null)
		{
			if (provinciaNascita.contains("|"))
			{
				String[] aProvincia = GenericsDateNumbers.getSplit_NString(2, provinciaNascita);
				profilo.setProvinciaNascitaSigla(aProvincia[0]);
				profilo.setProvinciaNascita(aProvincia[1]);
			}
			else
				profilo.clearProvinciaNascita();
		}
		
		String comuneNascita = (String)request.getAttribute("ddlComuneNascita");
		profilo.setComuneNascitaSelected(comuneNascita);
		if (comuneNascita != null)
		{
			if (comuneNascita.contains("|"))
			{
				String[] aComune = GenericsDateNumbers.getSplit_NString(2, comuneNascita);
				profilo.setComuneNascitaBelfiore(aComune[0]);
				profilo.setComuneNascitaDescrizione(aComune[1]);
			}
			else
				profilo.clearComuneNascita();
		}
		
		//documento
		profilo.setTipoDocumento((String)request.getAttribute("ddlDocumento"));
		profilo.setNumeroDocumento((String)request.getAttribute("txtNumDocumento"));
		profilo.setEnteRilascioDocumento((String)request.getAttribute("txtEnteRilascio"));
		profilo.setDataRilascioDocumento((Calendar)request.getAttribute("data_rilascio"));
		
		//residenza
		profilo.setIndirizzoResidenza((String)request.getAttribute("txtIndirizzoResidenza"));
		
		String provinciaResidenza = (String)request.getAttribute("ddlProvinciaResidenza");
		if (provinciaResidenza != null)
		{
			if (provinciaResidenza.contains("|"))
			{
				String[] aProvinciaResidenza = GenericsDateNumbers.getSplit_NString(2, provinciaResidenza);
				profilo.setProvinciaResidenzaSigla(aProvinciaResidenza[0]);
				profilo.setProvinciaResidenza(aProvinciaResidenza[1]);
			}
			else
				profilo.clearProvinciaResidenza();
		}
		String comuneResidenza = (String)request.getAttribute("ddlComuneResidenza");
		profilo.setComuneResidenzaSelected(comuneResidenza);
		if (comuneResidenza != null)
		{
			if (comuneResidenza.contains("|"))
			{
				String[] aComuneResidenza = GenericsDateNumbers.getSplit_NString(2, comuneResidenza);
				profilo.setComuneResidenzaBelfiore(aComuneResidenza[0]);
				profilo.setComuneResidenzaDescrizione(aComuneResidenza[1]);
			}
			else
				profilo.clearComuneResidenza();
		}
		profilo.setCapResidenza((String)request.getAttribute("txtCapResidenza"));
		
		//domicilio
		profilo.setIndirizzoDomicilio((String)request.getAttribute("txtIndirizzoDomicilio"));
		
		String provinciaDomicilio = (String)request.getAttribute("ddlProvinciaDomicilio");
		if (provinciaDomicilio != null)
		{
			if (provinciaDomicilio.contains("|"))
			{
				String[] aProvinciaDomicilio = GenericsDateNumbers.getSplit_NString(2, provinciaDomicilio);
				profilo.setProvinciaDomicilioSigla(aProvinciaDomicilio[0]);
				profilo.setProvinciaDomicilio(aProvinciaDomicilio[1]);
			}
			else
				profilo.clearProvinciaDomicilio();
		}
		String comuneDomicilio = (String)request.getAttribute("ddlComuneDomicilio");
		profilo.setComuneDomicilioSelected(comuneDomicilio);
		if (comuneDomicilio != null)
		{
			if (comuneDomicilio.contains("|"))
			{
				String[] aComuneDomicilio = GenericsDateNumbers.getSplit_NString(2, comuneDomicilio);
				profilo.setComuneDomicilioBelfiore(aComuneDomicilio[0]);
				profilo.setComuneDomicilioDescrizione(aComuneDomicilio[1]);
			}
			else
				profilo.clearComuneDomicilio();
		}
		profilo.setCapDomicilio((String)request.getAttribute("txtCapDomicilio"));
		
		//contatti
		profilo.setEmail((String)request.getAttribute("txtEmail"));
		profilo.setEmailPEC((String)request.getAttribute("txtEmailPEC"));
		profilo.setTelefono((String)request.getAttribute("txtTelefono"));
		profilo.setCellulare((String)request.getAttribute("txtCellulare"));
		profilo.setFax((String)request.getAttribute("txtFax"));
		
		String template = getTemplateCurrentApplication(request, request.getSession());
		if (template.equalsIgnoreCase("soris")||template.equalsIgnoreCase("newsoris")) {
			if(request.getAttribute("ddlFamigliaMerceologica")!=null)
				profilo.setClassificazioneMerceologica((String)request.getAttribute("ddlFamigliaMerceologica"));
			if(request.getAttribute("ddlCategoriaMerceologica")!=null)
				profilo.setClassificazioneMerceologicaDettaglio((String)request.getAttribute("ddlCategoriaMerceologica"));
			if(request.getAttribute("txtNumeroAutorizzazione")!=null)
				profilo.setNumeroAutorizzazione((String)request.getAttribute("txtNumeroAutorizzazione"));	
			//sede legale
			if(request.getAttribute("txtIndirizzoSedeLegale")!=null)
				profilo.setIndirizzoSedeLegale((String)request.getAttribute("txtIndirizzoSedeLegale"));
			
			String provinciaSedeLegale = (String)request.getAttribute("ddlProvinciaSedeLegale");
			if (provinciaSedeLegale != null)
			{
				if (provinciaSedeLegale.contains("|"))
				{
					String[] aProvinciaSedeLegale = GenericsDateNumbers.getSplit_NString(2, provinciaSedeLegale);
					profilo.setProvinciaSedeLegaleSigla(aProvinciaSedeLegale[0]);
					profilo.setProvinciaSedeLegale(aProvinciaSedeLegale[1]);
				}
				else
					profilo.clearProvinciaSedeLegale();
			}
			String comuneSedeLegale = (String)request.getAttribute("ddlComuneSedeLegale");
			
			if(request.getAttribute("ddlComuneSedeLegale")!=null)
				profilo.setComuneSedeLegaleSelected(comuneSedeLegale);
			
			if (comuneSedeLegale != null)
			{
				if (comuneSedeLegale.contains("|"))
				{
					String[] aComuneSedeLegale = GenericsDateNumbers.getSplit_NString(2, comuneSedeLegale);
					profilo.setComuneSedeLegaleBelfiore(aComuneSedeLegale[0]);
					profilo.setComuneSedeLegaleDescrizione(aComuneSedeLegale[1]);
				}
				else
					profilo.clearComuneSedeLegale();
			}
			if(request.getAttribute("txtCapSedeLegale")!=null)
				profilo.setCapSedeLegale((String)request.getAttribute("txtCapSedeLegale"));

			//Se Imposta Soggiorno duplico dati Personali su Azienda
			//Controllo la valorizzazione della clssificazione merceologica per persona fisica inb quanto numero autorizzazione per LB e LBA non è valorizzato a priori
			if (profilo.getPersonaFisica().equalsIgnoreCase("Y") && (profilo.getClassificazioneMerceologica()!=null && profilo.getClassificazioneMerceologica().trim().length()>0)) {
				profilo.setRagioneSociale(profilo.getNome() + " " + profilo.getCognome());
				profilo.setPartitaIVA(profilo.getCodiceFiscale());
			}	
		}
		return profilo;
	}
	
	//inizio LP PG200060
	//protected org.seda.payer.manager.adminusers.util.Error checkSeUsersInputFieldsPersonaFisica(Profilo profilo, 
	//		String codop) {
	protected org.seda.payer.manager.adminusers.util.Error checkSeUsersInputFieldsPersonaFisica(Profilo profilo, String codop, String template) {
	//fine LP PG200060
		
		org.seda.payer.manager.adminusers.util.Error error = new org.seda.payer.manager.adminusers.util.Error();
		
		//verifica codice fiscale
		String codFiscaleCalcolato = ProfiloUtil.calcoloCodiceFiscale(profilo);
		
		if (!codFiscaleCalcolato.equalsIgnoreCase(profilo.getCodiceFiscale()))
		{

//marini	if (!ProfiloUtil.verificaCodiciFiscaliOmocodia(codFiscaleCalcolato, profilo.getCodiceFiscale()))
			if (codFiscaleCalcolato.trim().equals(""))
					error.addErrorvalue("Codice Fiscale: Valore non valido");
			else {
				if (!ProfiloUtil.verificaCodiciFiscaliOmocodia(codFiscaleCalcolato, profilo.getCodiceFiscale())) {
					if (!ProfiloUtil.verifica2CodiciFiscaliOmocodia(codFiscaleCalcolato, profilo.getCodiceFiscale())) {
						error.addErrorvalue("Codice Fiscale: Valore non valido");
					}
				}
			}
		}
		
		if (profilo.getEmail().equals("") && profilo.getEmailPEC().equals(""))
			error.addErrorvalue("Specificare almeno un indirizzo e-mail o e-mail PEC");
		
		//verifica telefono
		//inizio LP PG200060
		if(template.equalsIgnoreCase("regmarche")) {}
		//fine LP PG200060
		if (profilo.getCellulare().equals("") && profilo.getTelefono().equals("") && profilo.getFax().equals("")) {
			error.addErrorvalue("Specificare almeno un numero di Telefono o Cellulare o Fax");
		//inizio LP PG200060
		} else {
		//fine LP PG200060
		
		verificheResidenzaDomicilio(error, profilo);
		//inizio LP PG200060
		}
		//fine LP PG200060
		
		return error;
	}
	
	//PG180170 GG 17102018 - inizio
	
	protected Error checkSeUsersInputFieldsPersonaGiuridica2(
			HttpServletRequest request, Profilo profilo, HttpSession session, String codop) {
		
		org.seda.payer.manager.adminusers.util.Error error = new org.seda.payer.manager.adminusers.util.Error();
		
		if (profilo.getPartitaIVA() == null || profilo.getPartitaIVA().trim().length()==0)
			error.addErrorvalue("Specificare P. IVA / Cod. Fiscale");
		else {
			String sRes = CodiceFiscalePIVA.controlloCodiceFiscale_PartitaIva_NomeEmpty(profilo.getPartitaIVA(), "P. IVA / Cod. Fiscale");
			if (sRes != null && sRes.length() > 0)
				error.addErrorvalue(sRes);
		}
		
		if (profilo.getRagioneSociale() == null || profilo.getRagioneSociale().trim().length()==0) 
			error.addErrorvalue("Specificare la Ragione Sociale");
		
		if (profilo.getClassificazioneMerceologica() == null || profilo.getClassificazioneMerceologica().trim().length()==0) 
			error.addErrorvalue("Specificare la Classificazione Merceologica");
		else {
			if (profilo.getClassificazioneMerceologica().equals("55")) {
				if (profilo.getNumeroAutorizzazione() == null || profilo.getNumeroAutorizzazione().trim().length()==0) 
					error.addErrorvalue("Specificare il Num. Autoriz.");
			}
		}
				
		return error;
	}
	
	protected org.seda.payer.manager.adminusers.util.Error checkSeUsersInputFieldsPersonaFisica2(Profilo profilo, 
			String codop, Boolean bCheck) {
		
		org.seda.payer.manager.adminusers.util.Error error = new org.seda.payer.manager.adminusers.util.Error();
		
		if (bCheck) {
			if (profilo.getCognome() == null || profilo.getCognome().trim().length()==0) 
				error.addErrorvalue("Specificare il Cognome");
			if (profilo.getNome() == null || profilo.getNome().trim().length()==0) 
				error.addErrorvalue("Specificare il Nome");
			if (profilo.getCodiceFiscale() == null || profilo.getCodiceFiscale().trim().length()==0)
				error.addErrorvalue("Specificare il Codice Fiscale");
			else {
				String sRes = CodiceFiscalePIVA.controlloCodiceFiscale_PartitaIva_NomeEmpty(profilo.getCodiceFiscale(), "Codice Fiscale");
				if (sRes != null && sRes.length() > 0)
					error.addErrorvalue(sRes);
	//			String resCheck = controlloCodiceFiscale_PartitaIva(profilo.getCodiceFiscale());
	//			if (!resCheck.equals("OK"))
	//				error.addErrorvalue(resCheck);
			}
			if (profilo.getSesso() == null || profilo.getSesso().trim().length()==0) 
				error.addErrorvalue("Specificare il Sesso");		
		}
		
		return error;
	}
	
	protected String controlloCodiceFiscale_PartitaIva(String codiceFiscalePartitaIVA)
	{
		String esitoPartitaIva = "";
		if (codiceFiscalePartitaIVA.length() > 0)
		{
			//controllo se è una partita iva
			if (codiceFiscalePartitaIVA.length() == 11 && IsInt64(codiceFiscalePartitaIVA))
			{
				//è una partita iva
				
				//controllo completo partita iva
				try
				{
					esitoPartitaIva = controllaPIVA(codiceFiscalePartitaIVA, "");
					if(!esitoPartitaIva.equals(""))	
					{
						return esitoPartitaIva;
					}	
				}
				catch (Exception ex)
				{			
					ex.printStackTrace();
					return esitoPartitaIva;
				}
			}
			else if (codiceFiscalePartitaIVA.length() == 16)
			{
				//è un codice fiscale
				//controllo codice fiscale
				try
				{			
					UCheckDigit uCodFiscal = new UCheckDigit(codiceFiscalePartitaIVA);
					boolean bIsCFCorretto = uCodFiscal.controllaCorrettezza();
					if (!bIsCFCorretto)
					{
						esitoPartitaIva = "Codice Fiscale: Valore non valido.";
						return esitoPartitaIva;
					}
				}
				catch (Exception ex)
				{	
					ex.printStackTrace();
					esitoPartitaIva = "Codice Fiscale: Valore errato.";
					return esitoPartitaIva;
				}
			}
			else
			{
				//non è nè un codice fiscale nè una partita iva
				esitoPartitaIva = "Codice Fiscale/Partita IVA: Valore non valido.";
				return esitoPartitaIva;
			}
		}
		esitoPartitaIva ="OK";
		return esitoPartitaIva;
	}
	
	public static boolean IsInt64(String sValue)
	{
		try
		{
			Long.valueOf(sValue);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//metodo per il controllo della partita IVA
	static String controllaPIVA(String pi, String labelCampo)
	{
	    int i, c, s;
	    s = 0;
	    for( i=0; i<=9; i+=2 )
	    	s += pi.charAt(i) - '0';
	    for( i=1; i<=9; i+=2 ){
	       c = 2*( pi.charAt(i) - '0' );
	       if( c > 9 )  c = c - 9;
	       s += c;
	    }
	    if( (( 10 - s%10 )%10 != pi.charAt(10) - '0') || pi.equals("00000000000") )
	    {
	    	if (labelCampo.length() > 0)
	    		return labelCampo + ": partita IVA non valida. Il codice di controllo non corrisponde.";
	    	else
	    		return "Partita IVA: valore non valido. Il codice di controllo non corrisponde.";
	    }
	    return "";
	}
	//PG180170 GG 17102018 - fine
	
	protected org.seda.payer.manager.adminusers.util.Error checkSeUsersInputFieldsPersonaFisicaTR(Profilo profilo, 
			String codop) {
		
		org.seda.payer.manager.adminusers.util.Error error = new org.seda.payer.manager.adminusers.util.Error();
		
		
		if (profilo.getEmail().equals("") && profilo.getEmailPEC().equals(""))
			error.addErrorvalue("Specificare almeno un indirizzo e-mail o e-mail PEC");
		
		//verifica telefono
//		if (profilo.getCellulare().equals("") && profilo.getTelefono().equals("") && profilo.getFax().equals(""))
//			error.addErrorvalue("Specificare almeno un numero di Telefono o Cellulare o Fax");
		
		verificheResidenzaDomicilio(error, profilo);
		
		return error;
	}
	
	private void verificheResidenzaDomicilio(Error error, Profilo profilo)
	{
		boolean bResidenzaCompiled = false;
		//verifica residenza: se ha inserito almeno un campo allora deve inserirli tutti
		if (profilo.getIndirizzoResidenza().length() > 0 || 
				profilo.getProvinciaResidenza().length() > 0 ||
				profilo.getComuneResidenzaSelected().length() > 0 || 
				profilo.getCapResidenza().length() > 0)
		{
			bResidenzaCompiled = true;
			//ho compilato almeno un campo della residenza
			//se non li ho compilati tutti lo comunico
			if (!(profilo.getIndirizzoResidenza().length() > 0 && 
					(profilo.getProvinciaResidenza().length() > 0 || profilo.getComuneResidenzaEstero().equals("Y")) &&
					profilo.getComuneResidenzaSelected().length() > 0 && 
					(profilo.getCapResidenza().length() > 0 || profilo.getComuneResidenzaEstero().equals("Y"))))
			{
				error.addErrorvalue("Compilare tutti i campi della Residenza");
			}
		}
		
		//verifica domicilio: può essere specificato solo se è stata compilata la residenza
		
		if (profilo.getIndirizzoDomicilio().length() > 0 || 
				profilo.getProvinciaDomicilio().length() > 0 ||
				profilo.getComuneDomicilioSelected().length() > 0 || 
				profilo.getCapDomicilio().length() > 0)
		{
			//se ho compilato almeno un campo del domicilio 
			if (!bResidenzaCompiled)
			{
				//se non ha compilato la residenza lo comunico
				error.addErrorvalue("Il Domicilio può essere indicato solo se è stata inserita la Residenza");
			}
			else
			{
				//se non li ho compilati tutti lo comunico
				if (!(profilo.getIndirizzoDomicilio().length() > 0 && 
						(profilo.getProvinciaDomicilio().length() > 0 || profilo.getComuneDomicilioEstero().equals("Y")) &&
						profilo.getComuneDomicilioSelected().length() > 0 && 
						(profilo.getCapDomicilio().length() > 0 || profilo.getComuneDomicilioEstero().equals("Y"))))
				{
					error.addErrorvalue("Compilare tutti i campi del Domicilio");
				}
			}
		}
	}
	
/*	private void getProfiloFromSession(HttpServletRequest request,
			HttpSession session) {
		if (request.getAttribute("tipologiaUtente").equals("F")) {
			request.setAttribute("chk_passwordAutogenerata", (session.getAttribute(arg0)getPasswordAutogenerata().equals("Y")));
			request.setAttribute("chk_nessunaScadenza",  (profilo.getNessunaScadenza().equals("Y")));
			request.setAttribute("chk_utenzaAttiva",  (profilo.getUtenzaAttiva().equals("Y")));
			request.setAttribute("usrname", profilo.getUsername());
			request.setAttribute("note", profilo.getNote());
			request.setAttribute
		
	}
	*/
	protected void gestisciProvComu(HttpServletRequest request, HttpSession session,
			Profilo profilo, String step) {

		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		//fine LP PG200060
						
		if (step=="personafisica") {
			ProfiloUtil.loadProvince(request);
			
			if (profilo.getComuneNascitaEstero().equals("Y"))
			{
				profilo.setProvinciaNascitaSigla("EE");
				request.removeAttribute("ddlProvinciaNascita");
			}
			if (!profilo.getProvinciaNascitaSigla().equals(""))
				ProfiloUtil.loadComuni(request, profilo.getProvinciaNascitaSigla(), "listcomuninascita");
			if (profilo.getComuneResidenzaEstero().equals("Y"))
			{
				profilo.setProvinciaResidenzaSigla("EE");
				request.removeAttribute("ddlProvinciaResidenza");
			}
			if (!profilo.getProvinciaResidenzaSigla().equals(""))
				ProfiloUtil.loadComuni(request, profilo.getProvinciaResidenzaSigla(), "listcomuniresidenza");
			if (profilo.getComuneDomicilioEstero().equals("Y"))
			{
				profilo.setProvinciaDomicilioSigla("EE");
				request.removeAttribute("ddlProvinciaDomicilio");
			}
			if (!profilo.getProvinciaDomicilioSigla().equals(""))
				ProfiloUtil.loadComuni(request, profilo.getProvinciaDomicilioSigla(), "listcomunidomicilio");	
			
			//inizio LP PG200060
			if(!template.equalsIgnoreCase("regmarche")) {
			//fine LP PG200060
			if (profilo.getComuneSedeLegaleEstero().equals("Y"))
			{
				profilo.setProvinciaSedeLegaleSigla("EE");
				request.removeAttribute("ddlProvinciaSedeLegale");
			}
			if (!profilo.getProvinciaSedeLegaleSigla().equals(""))
				ProfiloUtil.loadComuni(request, profilo.getProvinciaSedeLegaleSigla(), "listcomunisedelegale");
			ProfiloUtil.loadFamiglieMerceologiche(request);
			//inizio LP PG190010_002_LP
			/*
			if (!profilo.getClassificazioneMerceologica().equals(""))
				ProfiloUtil.loadCategorieMerceologiche(request, profilo.getClassificazioneMerceologica());
		    */
			//String template = getTemplateCurrentApplication(request, request.getSession()); PG200060 commentato
			if (!profilo.getClassificazioneMerceologica().equals("")) {
				ProfiloUtil.loadCategorieMerceologiche(request, profilo.getClassificazioneMerceologica(), (template.equalsIgnoreCase("soris")||template.equalsIgnoreCase("newsoris")));
			}
			//fine LP PG190010_002_LP
			//inizio LP PG200060
			}
			//fine LP PG200060
		}
		else 
			if (step=="personagiuridica")	{
				
				ProfiloUtil.loadProvince(request);
				if (profilo.getComuneSedeLegaleEstero().equals("Y"))
				{
					profilo.setProvinciaSedeLegaleSigla("EE");
					request.removeAttribute("ddlProvinciaSedeLegale");
				}
				if (!profilo.getProvinciaSedeLegaleSigla().equals(""))
					ProfiloUtil.loadComuni(request, profilo.getProvinciaSedeLegaleSigla(), "listcomunisedelegale");
				if (!profilo.getProvinciaSedeOperativaSigla().equals(""))
					ProfiloUtil.loadComuni(request, profilo.getProvinciaSedeOperativaSigla(), "listcomunisedeoperativa");
				
				ProfiloUtil.loadFamiglieMerceologiche(request);
				//inizio LP PG190010_002_LP
				/*
				if (!profilo.getClassificazioneMerceologica().equals(""))
					ProfiloUtil.loadCategorieMerceologiche(request, profilo.getClassificazioneMerceologica());
			    */
				//String template = getTemplateCurrentApplication(request, request.getSession()); PG200060 commentato
				if (!profilo.getClassificazioneMerceologica().equals("")) {
					ProfiloUtil.loadCategorieMerceologiche(request, profilo.getClassificazioneMerceologica(), (template.equalsIgnoreCase("soris")||template.equalsIgnoreCase("newsoris")));
				}
				//fine LP PG190010_002_LP
				
		}	
	}
	protected Error checkSeUsersInputFieldsPersonaGiuridica(
			HttpServletRequest request, Profilo profilo, HttpSession session, String codop) {
		
		org.seda.payer.manager.adminusers.util.Error error = new org.seda.payer.manager.adminusers.util.Error();
		
		//verifica partita iva
		String sRes = CodiceFiscalePIVA.controlloCodiceFiscale_PartitaIva_NomeEmpty(profilo.getPartitaIVA(), "P. IVA / Cod. Fiscale");
		if (sRes != null && sRes.length() > 0)
			error.addErrorvalue(sRes);
		
		//controllo sede operativa: se ne ha completata una parte deve completarla tutta
		if (profilo.getIndirizzoSedeOperativa().length() > 0 || 
				profilo.getProvinciaSedeOperativa().length() > 0 ||
				profilo.getComuneSedeOperativaSelected().length() > 0 || 
				profilo.getCapSedeOperativa().length() > 0)
		{
			//ho compilato almeno un campo della sede operativa
			//se non li ho compilati tutti lo comunico
			
			if (!(profilo.getIndirizzoSedeOperativa().length() > 0 && 
					profilo.getProvinciaSedeOperativa().length() > 0 &&
					profilo.getComuneSedeOperativaSelected().length() > 0 && 
					profilo.getCapSedeOperativa().length() > 0))
			{
				error.addErrorvalue("Compilare tutti i campi della Sede Operativa");
			}
		}
		else
		{
			//non ho compilato la sede operativa, controllo che la sede legale non sia all'estero,
			//altrimenti richiedo di compilare la sede operativa
			
			if (profilo.getComuneSedeLegaleEstero().equals("Y"))
				error.addErrorvalue("Con Sede Legale estera è necessario indicare una Sede Operativa");
				
		}
		
		//verifica telefono
		String template = getTemplateCurrentApplication(request, request.getSession());
		if (!template.equals("trentrisc")){
			if (profilo.getCellulareImpresa().equals("") && profilo.getTelefonoImpresa().equals(""))
				error.addErrorvalue("Specificare almeno un numero di Telefono o Cellulare");
		}
			
			
			
		
		return error;
	}
	
	protected void resetPersonaGiuridica(Profilo profilo) {
		// 
		profilo.setPartitaIVA("");
		profilo.setRagioneSociale("");
		profilo.setIndirizzoSedeLegale("");
		profilo.setProvinciaSedeLegale("");
		profilo.setClassificazioneMerceologica("");
		profilo.setClassificazioneMerceologicaDettaglio("");
		profilo.setNumeroAutorizzazione("");
		profilo.setIndirizzoSedeLegale("");
		profilo.setProvinciaSedeLegaleSigla("");
		profilo.setProvinciaSedeLegale("");
		profilo.setComuneSedeLegaleSelected("");
		profilo.setComuneSedeLegaleBelfiore("");
		profilo.setCapSedeLegale("");
		profilo.setComuneSedeLegaleEstero(null);
		profilo.setIndirizzoSedeOperativa("");
		profilo.setProvinciaSedeOperativaSigla("");
		profilo.setProvinciaSedeOperativa("");
		profilo.setComuneSedeOperativaSelected("");
		profilo.setComuneSedeOperativaBelfiore("");
		profilo.setCapSedeOperativa("");
		profilo.setEmailImpresa("");
		profilo.setEmailPECImpresa("");
		profilo.setTelefonoImpresa("");
		profilo.setCellulareImpresa("");
		profilo.setFaxImpresa("");
	}
	
	protected void resetPersonaFisica(Profilo profilo) {
		// 
		 profilo.setCognome("");
		 profilo.setNome("");
		 profilo.setCodiceFiscale("");
		 profilo.setSesso("");
		 profilo.setDataNascita(null);
		 profilo.setProvinciaNascitaSigla("");
		 profilo.setProvinciaNascita("");
		 profilo.setComuneNascitaSelected("");
		 profilo.setComuneNascitaBelfiore("");
		 profilo.setComuneNascitaEstero(null);
		 profilo.setTipoDocumento("");
		 profilo.setNumeroDocumento("");
		 profilo.setEnteRilascioDocumento("");
		 profilo.setDataRilascioDocumento(null);
		 profilo.setIndirizzoResidenza("");
		 profilo.setProvinciaResidenzaSigla("");
		 profilo.setProvinciaResidenza("");
		 profilo.setComuneResidenzaSelected("");
		 profilo.setComuneResidenzaBelfiore("");
		 profilo.setCapResidenza("");
		 profilo.setComuneResidenzaEstero(null);
		 profilo.setIndirizzoDomicilio("");
		 profilo.setProvinciaDomicilioSigla("");
		 profilo.setProvinciaDomicilio("");
		 profilo.setComuneDomicilioSelected("");
		 profilo.setComuneDomicilioBelfiore("");
		 profilo.setCapDomicilio("");
		 profilo.setComuneDomicilioEstero(null);
		 profilo.setEmail("");
		 profilo.setEmailPEC("");
		 profilo.setTelefono("");
		 profilo.setCellulare("");
		 profilo.setFax("");

	}
	
	
	/**
	 * UTENTE / PARTITA IVA
	 * Questo metodo recupera, per lo step della persona giuridica i parametri dalla request
	 * 
	 * @param request
	 */
	protected Profilo getPersonaGiuridicaFromRequest(HttpServletRequest request, Profilo profilo) 
	{
		//dati generali
		profilo.setPartitaIVA((String)request.getAttribute("txtPartitaIVA"));
		profilo.setRagioneSociale((String)request.getAttribute("txtRagioneSociale"));
		profilo.setClassificazioneMerceologica((String)request.getAttribute("ddlFamigliaMerceologica"));
		profilo.setClassificazioneMerceologicaDettaglio((String)request.getAttribute("ddlCategoriaMerceologica"));
		profilo.setNumeroAutorizzazione((String)request.getAttribute("txtNumeroAutorizzazione"));
		
		//sede legale
		profilo.setIndirizzoSedeLegale((String)request.getAttribute("txtIndirizzoSedeLegale"));
		
		String provinciaSedeLegale = (String)request.getAttribute("ddlProvinciaSedeLegale");
		if (provinciaSedeLegale != null)
		{
			if (provinciaSedeLegale.contains("|"))
			{
				String[] aProvinciaSedeLegale = GenericsDateNumbers.getSplit_NString(2, provinciaSedeLegale);
				profilo.setProvinciaSedeLegaleSigla(aProvinciaSedeLegale[0]);
				profilo.setProvinciaSedeLegale(aProvinciaSedeLegale[1]);
			}
			else
				profilo.clearProvinciaSedeLegale();
		}
		String comuneSedeLegale = (String)request.getAttribute("ddlComuneSedeLegale");
		profilo.setComuneSedeLegaleSelected(comuneSedeLegale);
		if (comuneSedeLegale != null)
		{
			if (comuneSedeLegale.contains("|"))
			{
				String[] aComuneSedeLegale = GenericsDateNumbers.getSplit_NString(2, comuneSedeLegale);
				profilo.setComuneSedeLegaleBelfiore(aComuneSedeLegale[0]);
				profilo.setComuneSedeLegaleDescrizione(aComuneSedeLegale[1]);
			}
			else
				profilo.clearComuneSedeLegale();
		}
		profilo.setCapSedeLegale((String)request.getAttribute("txtCapSedeLegale"));
		
		
		//sede operativa
		profilo.setIndirizzoSedeOperativa((String)request.getAttribute("txtIndirizzoSedeOperativa"));
		
		String provinciaSedeOperativa = (String)request.getAttribute("ddlProvinciaSedeOperativa");
		if (provinciaSedeOperativa != null)
		{
			if (provinciaSedeOperativa.contains("|"))
			{
				String[] aProvinciaSedeOperativa = GenericsDateNumbers.getSplit_NString(2, provinciaSedeOperativa);
				profilo.setProvinciaSedeOperativaSigla(aProvinciaSedeOperativa[0]);
				profilo.setProvinciaSedeOperativa(aProvinciaSedeOperativa[1]);
			}
			else
				profilo.clearProvinciaSedeOperativa();
		}
		String comuneSedeOperativa = (String)request.getAttribute("ddlComuneSedeOperativa");
		profilo.setComuneSedeOperativaSelected(comuneSedeOperativa);
		if (comuneSedeOperativa != null)
		{
			if (comuneSedeOperativa.contains("|"))
			{
				String[] aComuneSedeOperativa = GenericsDateNumbers.getSplit_NString(2, comuneSedeOperativa);
				profilo.setComuneSedeOperativaBelfiore(aComuneSedeOperativa[0]);
				profilo.setComuneSedeOperativaDescrizione(aComuneSedeOperativa[1]);
			}
			else
				profilo.clearComuneSedeOperativa();
		}
		profilo.setCapSedeOperativa((String)request.getAttribute("txtCapSedeOperativa"));
		
		//contatti
		profilo.setEmailImpresa((String)request.getAttribute("txtEmail"));
		profilo.setEmailPECImpresa((String)request.getAttribute("txtEmailPEC"));
		profilo.setTelefonoImpresa((String)request.getAttribute("txtTelefono"));
		profilo.setCellulareImpresa((String)request.getAttribute("txtCellulare"));
		profilo.setFaxImpresa((String)request.getAttribute("txtFax"));
      
		return profilo;
	}
	
	//EP200510_PRE_JAVA1_8 SB - inizio
	protected String checkSeUtenteEsistente(HttpServletRequest request)
	{
		String esito = "OK";
		
		String template = getTemplateCurrentApplication(request, request.getSession());
		
		if((template.equals("soris")||template.equals("newsoris")) && !userProfile.equals(ProfiloUtente.PYCO)){
		
			if (username != null && !username.equals(""))
			{
				try
				{
					//verifico che lo username inserito NON appartenga ad utente esistente nella tabella SEUSRTB
					//Se esiste un' utenza con lo stesso username nella tabella SEUSRTB, la disattivo

					SelezionaUtentePIVARequestType req = new SelezionaUtentePIVARequestType();	
					req.setUserName(username);
					SelezionaUtentePIVAResponseType res = WSCache.securityServer.selezionaUtentePIVA(req, request);
					com.seda.security.webservice.dati.ResponseType response = null;
					
					if (res != null && res.getResponse() != null)
					{
						response = res.getResponse();
						String retCode = response.getRetCode();
						String retMessage = response.getRetMessage();
						System.out.println("retCode: "+ retCode +" - retMessage: " + retMessage);
						if (retCode.equals("00") && res.getSelezionaUtentePIVAResponse().getUtenzaAttiva().getValue().equals("Y"))
						{
							//Disabilito l'utenza
							AggiornaUtentePIVARequestType rq = new AggiornaUtentePIVARequestType(res.getSelezionaUtentePIVAResponse(), codiceSocieta);
							rq.getUtentePIVA().setUtenzaAttiva(UtentePIVAUtenzaAttiva.N);
							AggiornaUtentePIVAResponseType rs = WSCache.securityServer.aggiornaUtentePIVA(rq, request);
							System.out.println("retCode: "+ rs.getResponse().getRetCode() +" - retMessage: " + rs.getResponse().getRetMessage());
							if(!rs.getResponse().getRetCode().equals("00")){
								esito = "Non è stato possibile aggiungere il profilo: errore durante la disattivazione dell'utenza esistente";
							}else{
								System.out.println("L'utenza " + username + " è stata disabilitata, durante la creazione del nuovo profilo");
							}
						}
					}
					else
						esito = Messages.GENERIC_ERROR.format();
				}
				catch (Exception e) {}
			}
		}
		return esito;
	}
	//EP200510_PRE_JAVA1_8 SB - fine

	//PREJAVA18_LUCAP_16092020
	private String formattedFieldString(Profilo profilo) {
		String formattedFields = "";
		formattedFields+="utenzaAttiva:"+(profilo.isUtenzaAttiva()?"Y":"N")+"|";
		formattedFields+="tipologiaUtente:"+(profilo.getTipologiaUtente())+"|";
		formattedFields+="operatoreBackOffice:"+(profilo.isOperatoreBackOffice()?"Y":"N")+"|";
		formattedFields+="note:"+(profilo.getNote())+"|";
		formattedFields+="utenzaNonScadeMai:"+(profilo.isNessunaScadenza()?"Y":"N")+"|";
		formattedFields+="dataScadenza:"+(profilo.getDataFineValiditaUtenza());
		return formattedFields;
	}
	//FINE PREJAVA18_LUCAP_16092020

	//inizio LP PG200360
	private boolean serviziAttiviOn(HttpServletRequest request) {
		boolean bOk = false;
		PropertiesTree configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		if (configuration != null){
			String appo = configuration.getProperty("serviziattivi.visibile." + dbSchemaCodSocieta);
			bOk = (appo != null ? !appo.trim().equalsIgnoreCase("N") : false);
		}
		return bOk;
		
	}
	//fine LP PG200360
	//inizio LP PG21X007
	private boolean logRequestOn(HttpServletRequest request) {
		boolean bOk = false;
		PropertiesTree configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		if (configuration != null){
			String appo = configuration.getProperty("logrequest.visibile." + dbSchemaCodSocieta);
			bOk = (appo != null ? !appo.trim().equalsIgnoreCase("N") : false);
		}
		return bOk;
		
	}
	//fine LP PG21X007
}