package org.seda.payer.manager.adminusers.lepida.actions;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Crypto;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
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
import com.seda.security.webservice.dati.DatiAnagPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiContattiPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiGeneraliPersonaFisicaDelegato;
import com.seda.security.webservice.dati.UtentePIVA;
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
	protected String azioniPerTransazioniOK = null;
	protected String azioniPerTransazioniKO = null;
	protected String azioniPerRiconciliazioneManuale = null;
	protected String attivazioneEstrattoContoManager = null;
	protected String abilitazioneProfiloRiversamento = null;

	//dom R
	protected String mailContogestione = null;
	
	protected String abilitazioneMultiUtente = null;
	protected String configurazione = null;
	protected String adminusers = null;
	protected String ecmanager = null;
	//inizio LP PG200360
	protected String serviziattivi = null;
	//fine LP PG200360
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
	//inizio LP PG200060
	protected String monitoraggiocup = null;	//PG14001X_001 GG 17/12/2014
	//fine LP PG200060
	protected String rendicontazione = null;
	protected String riconciliazione = null;
	protected String riversamento = null;
	protected String ottico = null;
	protected String entrate = null;
	// dom R
	protected String contogestione = null;

	protected String ruoli = null;
	
	protected String monitoraggiocruss = null; //PG200170

	
	protected Calendar dataFineValiditaUtenza = null;
	protected List<String> listaApplicazioni = null;

	private HashMap<String, String> DDLTipologieServizio = null;
	private HashMap<String, String> DDLTipologieServizioSel = null;
	private String codicetipologiaservizio; 
	private String descrizionetipologiaservizio; 
	protected String listatipologiaservizio=null;

	
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
	/**
	 * Rimuove una tipologia servizio dalla prima DDL alla seconda DDL.  
	 * 					
	 * @param request
	 * @param session
	 */
	@SuppressWarnings("unchecked")
	protected void removeTipologiaServizio(HttpServletRequest request,
			HttpSession session) {
		
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

	/**
	 * Questo metodo recupera, per il primo step, i parametri di input dalla form utilizzate per
	 * l'inserimento di un nuovo utente (codop="add") o per la modifica
	 * di un utente gi� esistente (codop="edit")
	 * 
	 * NOTA: Nel caso di inserimento di un nuovo utente la password viene crittografata.
	 * @param request
	 * @param codop
	 * @throws ActionException 
	 */
	protected void getForm1Parameters(HttpServletRequest request, String codop) throws ActionException
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
		
		String param_username = (codop.equals("edit") ? "tx_username_hidden" : "tx_username");
		
		if (codop.equals("edit"))
			chiaveUtente = Long.parseLong((String)request.getParameter("tx_chiaveutente_hidden"));
		
		userProfile = (request.getParameter(param_userProfile) == null ? "" : request.getParameter(param_userProfile));
		codiceSocieta = (request.getParameter(param_codiceSocieta) == null ? "" : request.getParameter(param_codiceSocieta));
		codiceUtente = (request.getParameter(param_codiceUtente) == null ? "" : request.getParameter(param_codiceUtente));
		codiceEnte = (request.getParameter(param_codiceEnte) == null ? "" : request.getParameter(param_codiceEnte));
		username = (request.getParameter(param_username) == null ? "" : request.getParameter(param_username));
		utenzaAttiva =  ( request.getParameter("utenzaAttiva") == null ? "N" : "Y" );
		abilitazioneMultiUtente =  ( request.getParameter("abilitazioneMultiUtente") == null ? "N" : "Y" );
		codiceFiscale = (request.getParameter("tx_codiceFiscale") == null ? "" : request.getParameter("tx_codiceFiscale"));
		tipologiaservizio = (request.getParameter("tx_tipologia_servizio") == null ? "" : request.getParameter("tx_tipologia_servizio"));
		tipologiaserviziosel = (request.getParameter("tx_tipologia_servizio_sel") == null ? "" : request.getParameter("tx_tipologia_servizio_sel"));
	}

	protected void setAllForm2FlagForAMMI(HttpServletRequest request)
	{
		downloadFlussiRendicontazione =  "Y";
		invioFlussiRendicontazioneViaFtp = "Y";
		invioFlussiRendicontazioneViaEmail = "Y";
		azioniPerTransazioniOK = "Y";
		azioniPerTransazioniKO = "Y";
		azioniPerRiconciliazioneManuale = "Y";
		attivazioneEstrattoContoManager = "Y";
		abilitazioneProfiloRiversamento = "Y";

		//dom R
		mailContogestione = "Y";
		
		
		listaApplicazioni = new Vector<String>();
		configurazione = "Y";
		listaApplicazioni.add("configurazione");
		adminusers =  "Y";
		listaApplicazioni.add("adminusers");
		ecmanager =  "Y";
		listaApplicazioni.add("ecmanager");
		//inizio LP PG200360
		if(serviziAttiviOn(request)) {
			serviziattivi =  "Y";
			listaApplicazioni.add("serviziattivi");
		}
		//fine LP PG200360
		monitoraggio =  "Y";
		listaApplicazioni.add("monitoraggio");
		//inizio LP PG200060
		//PG14001X_001 GG 17/12/2014
		monitoraggiocup =  "Y";
		listaApplicazioni.add("monitoraggiocup");
		//fine LP PG200060
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
		
		monitoraggiocruss = "Y"; //PG200170
		listaApplicazioni.add("monitoraggiocruss"); //PG200170
		
		
		request.setAttribute("userIsAMMI", true);
	}
	
	protected void initForm2FlagForProfile(HttpServletRequest request, HttpSession session, String profile)
	{
		String sValue = "N";
		if (profile.toUpperCase().equals("AMMI"))
		{
			sValue = "Y";
			request.setAttribute("userIsAMMI", true);
		}
		
		downloadFlussiRendicontazione =  sValue;
		invioFlussiRendicontazioneViaFtp = sValue;
		invioFlussiRendicontazioneViaEmail = sValue;
		azioniPerTransazioniOK = sValue;
		azioniPerTransazioniKO = sValue;
		azioniPerRiconciliazioneManuale = sValue;
		attivazioneEstrattoContoManager = sValue;
		abilitazioneProfiloRiversamento = sValue;

		//dom R
		mailContogestione = sValue;
		
		configurazione = sValue;
		adminusers =  sValue;
		ecmanager =  sValue;
		//inizio LP PG200360
		if(serviziAttiviOn(request)) {
			serviziattivi =  sValue;
		}
		//fine LP PG200360
		monitoraggio =  sValue;
		//inizio LP PG200060
		monitoraggiocup =  sValue;	//PG14001X_001 GG 17/12/2014
		//fine LP PG200060
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
		
		monitoraggiocruss = sValue; //PG200170
		
		
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
			listaApplicazioni.add("monitoraggio");
			//inizio LP PG200060
			listaApplicazioni.add("monitoraggiocup");	//PG14001X_001 GG 17/12/2014
			//fine LP PG200060
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
			
			listaApplicazioni.add("monitoraggiocruss"); //PG200170
		}
			
		if (session != null)
			putAdd2ParametersInSession(request, session);
	}
	
	/**
	 * Questo metodo recupera, per il secondo step, i parametri di input dalle form utilizzate per
	 * l'inserimento di un nuovo utente o per la modifica
	 * di un utente gi� esistente.
	 * 
	 * NOTA: Se il profilo dell'utente che si sta inserendo/modificando � "AMMI" vengono settate tutte
	 * le applicazioni e tutti i flag e viene settato l'attributo "userIsAMMI" a "true" per disabilitare
	 * tutte le check-box e gli script
	 * 
	 * @param request
	 */
	protected void getForm2Parameters(HttpServletRequest request,HttpSession session) 
	{
		String profilo = (String)session.getAttribute("userAdd_userProfile");
		if (profilo != null && profilo.equals("AMMI"))
			setAllForm2FlagForAMMI(request);
		else
		{
			downloadFlussiRendicontazione =  ( request.getParameter("downloadFlussiRendicontazione") == null ? "N" : "Y" );
			invioFlussiRendicontazioneViaFtp =  ( request.getParameter("invioFlussiRendicontazioneViaFtp") == null ? "N" : "Y" );
			invioFlussiRendicontazioneViaEmail =  ( request.getParameter("invioFlussiRendicontazioneViaEmail") == null ? "N" : "Y" );
			azioniPerTransazioniOK =  ( request.getParameter("azioniPerTransazioniOK") == null ? "N" : "Y" );
			azioniPerTransazioniKO =  ( request.getParameter("azioniPerTransazioniKO") == null ? "N" : "Y" );
			azioniPerRiconciliazioneManuale =  ( request.getParameter("azioniPerRiconciliazioneManuale") == null ? "N" : "Y" );
			attivazioneEstrattoContoManager =  ( request.getParameter("attivazioneEstrattoContoManager") == null ? "N" : "Y" );
			abilitazioneProfiloRiversamento =  ( request.getParameter("abilitazioneProfiloRiversamento") == null ? "N" : "Y" );

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
				serviziattivi =  (request.getParameter("serviziattivi") == null ? "N" : "Y" );
				if (serviziattivi.equals("Y")) listaApplicazioni.add("serviziattivi");
			}
			//fine LP PG200360
			monitoraggio =  ( request.getParameter("monitoraggio") == null ? "N" : "Y" );
			if (monitoraggio.equals("Y")) listaApplicazioni.add("monitoraggio");
			//inizio LP PG200060
			//PG14001X_001 GG 17/12/2014
			monitoraggiocup =  ( request.getParameter("monitoraggiocup") == null ? "N" : "Y" );
			if (monitoraggiocup.equals("Y")) listaApplicazioni.add("monitoraggiocup");
			//fine LP PG200060			
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
			
			monitoraggiocruss =  ( request.getParameter("monitoraggiocruss") == null ? "N" : "Y" );
			if (monitoraggiocruss.equals("Y")) listaApplicazioni.add("monitoraggiocruss");

			
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
	 * OBSOLETO - Fa il check di campi relativi alle UTENZE e campi relativi ai PROFILI
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
		
		//questo if dovrebbe essere sempre verificato poich� il campo
		//� obbligatorio a livello di validator nella jsp
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
				
				com.seda.security.webservice.dati.SelezionaUtentePIVARequestType req = new com.seda.security.webservice.dati.SelezionaUtentePIVARequestType();
				req.setUserName(username);
				com.seda.security.webservice.dati.SelezionaUtentePIVAResponseType res = WSCache.securityServer.selezionaUtentePIVA(req, request);
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
		pyUser.setAzioniPerTransazioniOK(azioniPerTransazioniOK);
		pyUser.setAzioniPerTransazioniKO(azioniPerTransazioniKO);
		pyUser.setAzioniPerRiconciliazioneManuale(azioniPerRiconciliazioneManuale);
		pyUser.setAttivazioneEstrattoContoManager(attivazioneEstrattoContoManager);
		pyUser.setAbilitazioneProfiloRiversamento(abilitazioneProfiloRiversamento);

		//dom R
		pyUser.setMailContoGestione(mailContogestione);
		
		pyUser.setAbilitazioneMultiUtente(abilitazioneMultiUtente);
		pyUser.setDataUltimoAggiornamento(now);
		pyUser.setOperatoreUltimoAggiornamento(userBean.getUserName());
		pyUser.setListaTipologiaServizio(formattaListaTipologieServizio(DDLTipologieServizioSel));
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
	 * Questo metodo � utilizzato per LEPIDA
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
	 * Obsoleta per le UTENZE; � stato sostituito da "setSeUsersFormFlags" - Il campo "abilitazioneMultiUtente" � relativo ai profili
	 * @param request
	 */
	protected void setForm1Flags(HttpServletRequest request)
	{
		request.setAttribute("chk_utenzaAttiva",  utenzaAttiva.equals("Y"));
		request.setAttribute("chk_abilitazioneMultiUtente",  abilitazioneMultiUtente.equals("Y"));
	}
	
	/**
	 * Questo metodo setta i flag della form nello STEP 2 solo
	 * se il profilo � rimasto inalterato
	 */
	protected void checkProfileAndSetForm2Flags(HttpServletRequest request,SelezionaUtenteResponseType selezionaRes)
	{
		PyUserType pyUser = selezionaRes.getPyUser();
		String dbUserProfile = pyUser.getUserProfile();
		if (userProfile.equals(dbUserProfile))
		{
			request.setAttribute("chk_downloadFlussiRendicontazione",  downloadFlussiRendicontazione.equals("Y"));
			request.setAttribute("chk_invioFlussiRendicontazioneViaFtp",  invioFlussiRendicontazioneViaFtp.equals("Y"));
			request.setAttribute("chk_invioFlussiRendicontazioneViaEmail",  invioFlussiRendicontazioneViaEmail.equals("Y"));
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
			request.setAttribute("chk_log",  log.equals("Y"));
			//inizio LP PG21X007
			if(logRequestOn(request)) {
				request.setAttribute("chk_logrequest",  logrequest.equals("Y"));
			}
			//fine LP PG21X007
			request.setAttribute("chk_monitoraggio",  monitoraggio.equals("Y"));
			//inizio LP PG200060
			request.setAttribute("chk_monitoraggiocup",  monitoraggiocup.equals("Y"));	//PG14001X_001 GG 17/12/2014
			//fine LP PG200060			
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
			
			request.setAttribute("chk_monitoraggiocruss",  monitoraggiocruss.equals("Y")); //PG200170
			
		}
	}

	protected void setForm2Flags(HttpServletRequest request)
	{
		request.setAttribute("chk_downloadFlussiRendicontazione",  downloadFlussiRendicontazione.equals("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaFtp",  invioFlussiRendicontazioneViaFtp.equals("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaEmail",  invioFlussiRendicontazioneViaEmail.equals("Y"));
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
		//inizio LP PG200060
		request.setAttribute("chk_monitoraggiocup",  monitoraggiocup.equals("Y"));	//PG14001X_001 GG 17/12/2014
		//fine LP PG200060		
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
		
		request.setAttribute("chk_monitoraggiocruss",  monitoraggiocruss.equals("Y")); //PG200170
		
	}

	protected void setDisableForm2Flags(HttpServletRequest request)
	{
		request.setAttribute("chk_downloadFlussiRendicontazione",  downloadFlussiRendicontazione.equals("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaFtp",  invioFlussiRendicontazioneViaFtp.equals("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaEmail",  invioFlussiRendicontazioneViaEmail.equals("Y"));
		request.setAttribute("chk_azioniPerTransazioniOK",  azioniPerTransazioniOK.equals("Y"));
		request.setAttribute("chk_azioniPerTransazioniKO",  azioniPerTransazioniKO.equals("Y"));
		request.setAttribute("chk_azioniPerRiconciliazioneManuale",  azioniPerRiconciliazioneManuale.equals("Y"));
		request.setAttribute("chk_attivazioneEstrattoContoManager",  attivazioneEstrattoContoManager.equals("Y"));
		request.setAttribute("chk_abilitazioneProfiloRiversamento",  abilitazioneProfiloRiversamento.equals("Y"));

		//dom R
		request.setAttribute("chk_mailContogestione",  mailContogestione.equals("Y"));
		
	}

	/**
	 * Salva in sessione le informazioni del primo step per l'inserimento/modifica di un utente
	 * 
	 * @param request
	 */
	protected void putAdd1ParametersInSession(HttpServletRequest request,HttpSession session) 
	{
		if (session.getAttribute("userAdd_chiaveUtente") != null) session.removeAttribute("userAdd_chiaveUtente");
		if (session.getAttribute("userAdd_userProfile") != null) session.removeAttribute("userAdd_userProfile");
		if (session.getAttribute("userAdd_codiceSocieta") != null) session.removeAttribute("userAdd_codiceSocieta");
		if (session.getAttribute("userAdd_codiceUtente") != null) session.removeAttribute("userAdd_codiceUtente");
		if (session.getAttribute("userAdd_codiceEnte") != null) session.removeAttribute("userAdd_codiceEnte");
		if (session.getAttribute("userAdd_username") != null) session.removeAttribute("userAdd_username");
		if (session.getAttribute("userAdd_utenzaAttiva") != null) session.removeAttribute("userAdd_utenzaAttiva");
		if (session.getAttribute("userAdd_abilitazioneMultiUtente") != null) session.removeAttribute("userAdd_abilitazioneMultiUtente");
		if (session.getAttribute("userAdd_codiceFiscale") != null) session.removeAttribute("userAdd_codiceFiscale");
		
		session.setAttribute("userAdd_chiaveUtente", chiaveUtente);
		session.setAttribute("userAdd_userProfile", userProfile);
		session.setAttribute("userAdd_codiceSocieta", codiceSocieta);
		session.setAttribute("userAdd_codiceUtente", codiceUtente);
		session.setAttribute("userAdd_codiceEnte", codiceEnte);
		session.setAttribute("userAdd_username", username);
		session.setAttribute("userAdd_utenzaAttiva", utenzaAttiva);
		session.setAttribute("userAdd_abilitazioneMultiUtente", abilitazioneMultiUtente);
		session.setAttribute("userAdd_codiceFiscale", codiceFiscale);
	}

	/**
	 * Salva in sessione le informazioni del secondo step per l'inserimento di un nuovo utente
	 * 
	 * @param request
	 */
	protected void putAdd2ParametersInSession(HttpServletRequest request,HttpSession session) 
	{
		if (session.getAttribute("userAdd_downloadFlussiRendicontazione") != null) session.removeAttribute("userAdd_downloadFlussiRendicontazione");
		if (session.getAttribute("userAdd_invioFlussiRendicontazioneViaFtp") != null) session.removeAttribute("userAdd_invioFlussiRendicontazioneViaFtp");
		if (session.getAttribute("userAdd_invioFlussiRendicontazioneViaEmail") != null) session.removeAttribute("userAdd_invioFlussiRendicontazioneViaEmail");
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
		//inizio LP PG200360
		if(serviziAttiviOn(request)) {
			if (session.getAttribute("userAdd_serviziattivi") != null) session.removeAttribute("userAdd_serviziattivi");
		}
		//fine LP PG200360
		if (session.getAttribute("userAdd_monitoraggio") != null) session.removeAttribute("userAdd_monitoraggio");
		//inizio LP PG200060
		if (session.getAttribute("userAdd_monitoraggiocup") != null) session.removeAttribute("userAdd_monitoraggiocup");	//PG14001X_001 GG 17/12/2014
		//fine LP PG200060		
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
		
		
		session.setAttribute("userAdd_downloadFlussiRendicontazione", downloadFlussiRendicontazione);
		session.setAttribute("userAdd_invioFlussiRendicontazioneViaFtp", invioFlussiRendicontazioneViaFtp);
		session.setAttribute("userAdd_invioFlussiRendicontazioneViaEmail", invioFlussiRendicontazioneViaEmail);
		session.setAttribute("userAdd_azioniPerTransazioniOK", azioniPerTransazioniOK);
		session.setAttribute("userAdd_azioniPerTransazioniKO", azioniPerTransazioniKO);
		session.setAttribute("userAdd_azioniPerRiconciliazioneManuale", azioniPerRiconciliazioneManuale);
		session.setAttribute("userAdd_attivazioneEstrattoContoManager", attivazioneEstrattoContoManager);
		session.setAttribute("userAdd_abilitazioneProfiloRiversamento", abilitazioneProfiloRiversamento);

		//dom R		
		session.setAttribute("userAdd_mailContogestione", mailContogestione);
		
		session.setAttribute("userAdd_configurazione", configurazione);
		session.setAttribute("userAdd_adminusers", adminusers);
		session.setAttribute("userAdd_ecmanager", ecmanager);
		//inizio LP PG200360
		if(serviziAttiviOn(request)) {
			session.setAttribute("userAdd_serviziattivi", serviziattivi);
		}
		//fine LP PG200360
		session.setAttribute("userAdd_monitoraggio", monitoraggio);
		//inizio LP PG200060
		session.setAttribute("userAdd_monitoraggiocup", monitoraggiocup);	//PG14001X_001 GG 17/12/2014
		//fine LP PG200060		
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
		session.setAttribute("userAdd_monitoraggiocruss", monitoraggiocruss); //PG200170
		
	}

	/**
	 * Recupera le informazioni del primo step per l'inserimento di un nuovo utente dalla sessione
	 * 
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	protected void getAdd1ParametersFromSession(HttpServletRequest request,HttpSession session) 
	{
		if (session.getAttribute("userAdd_chiaveUtente") != null)
			chiaveUtente = (Long)session.getAttribute("userAdd_chiaveUtente");
		
		userProfile = (String)session.getAttribute("userAdd_userProfile");
		codiceSocieta = (String)session.getAttribute("userAdd_codiceSocieta" );
		codiceUtente = (String)session.getAttribute("userAdd_codiceUtente" );
		codiceEnte = (String)session.getAttribute("userAdd_codiceEnte" );
		username = (String)session.getAttribute("userAdd_username" );
		utenzaAttiva = (String)session.getAttribute("userAdd_utenzaAttiva" );
		abilitazioneMultiUtente = (String)session.getAttribute("userAdd_abilitazioneMultiUtente" );
		//inizio LP PG200060
		//TODO: da verificare in regmarche non si riesce a fare insert di un nuovo profilo
		//      perche' userAdd_codiceFiscale == "".
		//      Provo a fare la modifica di assegnare codiceFiscale = username.
		//      Eventualmente si torna indietro
		String template = getTemplateCurrentApplication(request, session); 
		if(template.equalsIgnoreCase("regmarche")) {
			codiceFiscale = (String)session.getAttribute("userAdd_codiceFiscale" );
			if(codiceFiscale == null || codiceFiscale.length() == 0) {
				codiceFiscale = username;
			}
		} else {
		//fine LP PG200060
		codiceFiscale = (String)session.getAttribute("userAdd_codiceFiscale" );
		//inizio LP PG200060
		}
		//fine LP PG200060
		DDLTipologieServizioSel= (HashMap<String, String>) session.getAttribute("listaTipologieServizioSel");

	}

	/**
	 * Setta gli attributi corrispondenti ai campi di input del primo step copmpresi i flag.
	 * Viene utilizzata quando si torna dallo STEP2 allo STEP1 e si recuperano i valori dalla sessione
	 * 
	 * @param request
	 */
	protected void setForm1Attributes(HttpServletRequest request) 
	{
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
		downloadFlussiRendicontazione = (String)session.getAttribute("userAdd_downloadFlussiRendicontazione" );
		invioFlussiRendicontazioneViaFtp = (String)session.getAttribute("userAdd_invioFlussiRendicontazioneViaFtp" );
		invioFlussiRendicontazioneViaEmail = (String)session.getAttribute("userAdd_invioFlussiRendicontazioneViaEmail" );
		azioniPerTransazioniOK = (String)session.getAttribute("userAdd_azioniPerTransazioniOK" );
		azioniPerTransazioniKO = (String)session.getAttribute("userAdd_azioniPerTransazioniKO" );
		azioniPerRiconciliazioneManuale = (String)session.getAttribute("userAdd_azioniPerRiconciliazioneManuale" );
		attivazioneEstrattoContoManager = (String)session.getAttribute("userAdd_attivazioneEstrattoContoManager" );
		abilitazioneProfiloRiversamento = (String)session.getAttribute("userAdd_abilitazioneProfiloRiversamento" );
		//dom R
		mailContogestione = (String)session.getAttribute("userAdd_mailContogestione" );

		configurazione = (String)session.getAttribute("userAdd_configurazione" );
		adminusers = (String)session.getAttribute("userAdd_adminusers" );
		ecmanager = (String)session.getAttribute("userAdd_ecmanager" );
		//inizio LP PG200360
		if(serviziAttiviOn(request)) {
			serviziattivi = (String) session.getAttribute("userAdd_serviziattivi");
		}
		//fine LP PG200360
		monitoraggio = (String)session.getAttribute("userAdd_monitoraggio" );
		//inizio LP PG200060
		monitoraggiocup = (String)session.getAttribute("userAdd_monitoraggiocup" );	//PG14001X_001 GG 17/12/2014
		//fine LP PG200060		
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
		
		monitoraggiocruss = (String)session.getAttribute("userAdd_monitoraggiocruss" ); //PG200170
		
	}

	protected void setForm1AttributesFromWs(HttpSession session, HttpServletRequest request,SelezionaUtenteResponseType selezionaRes)
	{
		PyUserType pyUser = selezionaRes.getPyUser();
		
		chiaveUtente = pyUser.getChiaveUtente();
		userProfile = pyUser.getUserProfile();
		codiceSocieta = pyUser.getCodiceSocieta();
		codiceUtente = pyUser.getCodiceUtente();
		codiceEnte = pyUser.getChiaveEnteConsorzio();
		username = pyUser.getUserName();
		codiceFiscale = pyUser.getUserName(); //utilizzato solo per Lepida
		listatipologiaservizio = pyUser.getListaTipologiaServizio();

		request.setAttribute("tx_chiaveutente_hidden", chiaveUtente);
		request.setAttribute("tx_username_hidden", username);
		request.setAttribute("tx_userprofile_hidden", userProfile);
		request.setAttribute("tx_societa_hidden", codiceSocieta);
		request.setAttribute("tx_utente_hidden", codiceUtente);
		request.setAttribute("tx_ente_hidden", codiceEnte);
		request.setAttribute("tx_codiceFiscale_hidden", codiceFiscale);
		
		request.setAttribute("chk_abilitazioneMultiUtente", pyUser.getAbilitazioneMultiUtente().equalsIgnoreCase("Y"));
		request.setAttribute("chk_utenzaAttiva", pyUser.getFlagAttivazione().equalsIgnoreCase("Y"));
		request.setAttribute("tx_tipologia_servizio_hidden", listatipologiaservizio);
	}

	protected void setForm2AttributesFromWs(HttpServletRequest request,SelezionaUtenteResponseType selezionaRes)
	{
		PyUserType pyUser = selezionaRes.getPyUser();
		listaApplicazioni = Arrays.asList(selezionaRes.getApplicazioni());

		request.setAttribute("chk_downloadFlussiRendicontazione", pyUser.getDownloadFlussiRendicontazione().equalsIgnoreCase("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaFtp", pyUser.getInvioFlussiRendicontazioneViaFtp().equalsIgnoreCase("Y"));
		request.setAttribute("chk_invioFlussiRendicontazioneViaEmail", pyUser.getInvioFlussiRendicontazioneViaEmail().equalsIgnoreCase("Y"));
		request.setAttribute("chk_azioniPerTransazioniOK", pyUser.getAzioniPerTransazioniOK().equalsIgnoreCase("Y"));
		request.setAttribute("chk_azioniPerTransazioniKO", pyUser.getAzioniPerTransazioniKO().equalsIgnoreCase("Y"));
		request.setAttribute("chk_azioniPerRiconciliazioneManuale", pyUser.getAzioniPerRiconciliazioneManuale().equalsIgnoreCase("Y"));
		request.setAttribute("chk_attivazioneEstrattoContoManager", pyUser.getAttivazioneEstrattoContoManager().equalsIgnoreCase("Y"));
		request.setAttribute("chk_abilitazioneProfiloRiversamento", pyUser.getAbilitazioneProfiloRiversamento().equalsIgnoreCase("Y"));

		//dom R
		request.setAttribute("chk_mailContogestione", pyUser.getMailContoGestione().equalsIgnoreCase("Y"));
		
		if (listaApplicazioni != null && !listaApplicazioni.isEmpty() )
		{
			request.setAttribute("chk_configurazione",  listaApplicazioni.contains("configurazione"));
			request.setAttribute("chk_adminusers",  listaApplicazioni.contains("adminusers"));
			request.setAttribute("chk_ecmanager",  listaApplicazioni.contains("ecmanager"));
			//inizio LP PG200360
			if(serviziAttiviOn(request)) {
				request.setAttribute("chk_serviziattivi",  listaApplicazioni.contains("serviziattivi"));
			}
			//fine LP PG200360
			request.setAttribute("chk_log",  listaApplicazioni.contains("log"));
			//inizio LP PG21X007
			if(logRequestOn(request)) {
				request.setAttribute("chk_logrequest", listaApplicazioni.contains("logrequest"));
			}
			//fine LP PG21X007
			request.setAttribute("chk_monitoraggio",  listaApplicazioni.contains("monitoraggio"));
			//inizio LP PG200060
			request.setAttribute("chk_monitoraggiocup",  listaApplicazioni.contains("monitoraggiocup"));	//PG14001X_001 GG 17/12/2014
			//fine LP PG200060			
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
			
			request.setAttribute("chk_monitoraggiocruss",  listaApplicazioni.contains("monitoraggiocruss")); //PG200170
		}
	}

	protected void checkProfileAndSetForm2AttributesFromWs(HttpServletRequest request,SelezionaUtenteResponseType selezionaRes)
	{
		PyUserType pyUser = selezionaRes.getPyUser();
		String dbUserProfile = pyUser.getUserProfile();
		/*
		 * 1) Se il profilo scelto � uguale a quello precedente e diverso da "AMMI"
		 *    carico il dati dal WS
		 * 2) Se il profilo scelto � uguale ad "AMMI"
		 *    setto tutti i flag altrimenti li inizializzo a seconda del profilo
		 */
		if (userProfile.equals(dbUserProfile))
		{
			if (!userProfile.equals("AMMI"))
			{
				//inizio LP PG200060
				try {
				//fine LP PG200060
				listaApplicazioni = Arrays.asList(selezionaRes.getApplicazioni());
				//inizio LP PG200060
				} catch (Exception e) {
					// TODO: handle exception
					listaApplicazioni = null;
				}
				//fine LP PG200060
				
				request.setAttribute("chk_downloadFlussiRendicontazione", pyUser.getDownloadFlussiRendicontazione().equalsIgnoreCase("Y"));
				request.setAttribute("chk_invioFlussiRendicontazioneViaFtp", pyUser.getInvioFlussiRendicontazioneViaFtp().equalsIgnoreCase("Y"));
				request.setAttribute("chk_invioFlussiRendicontazioneViaEmail", pyUser.getInvioFlussiRendicontazioneViaEmail().equalsIgnoreCase("Y"));
				request.setAttribute("chk_azioniPerTransazioniOK", pyUser.getAzioniPerTransazioniOK().equalsIgnoreCase("Y"));
				request.setAttribute("chk_azioniPerTransazioniKO", pyUser.getAzioniPerTransazioniKO().equalsIgnoreCase("Y"));
				request.setAttribute("chk_azioniPerRiconciliazioneManuale", pyUser.getAzioniPerRiconciliazioneManuale().equalsIgnoreCase("Y"));
				request.setAttribute("chk_attivazioneEstrattoContoManager", pyUser.getAttivazioneEstrattoContoManager().equalsIgnoreCase("Y"));
				request.setAttribute("chk_abilitazioneProfiloRiversamento", pyUser.getAbilitazioneProfiloRiversamento().equalsIgnoreCase("Y"));

				//dom R
				request.setAttribute("chk_mailContogestione", pyUser.getMailContoGestione().equalsIgnoreCase("Y"));
				
				if (listaApplicazioni != null && !listaApplicazioni.isEmpty() )
				{
					request.setAttribute("chk_configurazione",  listaApplicazioni.contains("configurazione"));
					request.setAttribute("chk_adminusers",  listaApplicazioni.contains("adminusers"));
					request.setAttribute("chk_ecmanager",  listaApplicazioni.contains("ecmanager"));
					//inizio LP PG200360
					if(serviziAttiviOn(request)) {
						request.setAttribute("chk_serviziattivi",  listaApplicazioni.contains("serviziattivi"));
					}
					//fine LP PG200360
					request.setAttribute("chk_log",  listaApplicazioni.contains("log"));
					//inizio LP PG21X007
					if(logRequestOn(request)) {
						request.setAttribute("chk_logrequest", listaApplicazioni.contains("logrequest"));
					}
					//fine LP PG21X007
					request.setAttribute("chk_monitoraggio",  listaApplicazioni.contains("monitoraggio"));
					//inizio LP PG200060
					request.setAttribute("chk_monitoraggiocup",  listaApplicazioni.contains("monitoraggiocup"));	//PG14001X_001 GG 17/12/2014
					//fine LP PG200060					
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

					//dom
					request.setAttribute("chk_contogestione",  listaApplicazioni.contains("contogestione"));

					request.setAttribute("chk_ruoli",  listaApplicazioni.contains("ruoli"));
					
					request.setAttribute("chk_monitoraggiocruss",  listaApplicazioni.contains("monitoraggiocruss"));
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
	 * In tal modo non � possibile associarle all'utente.
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
					 * Se l'applicazione � protetta 
					 * viene presa in considerazione
					 */
					if (protectedMap.containsKey(applicationName)) 
					{
						/*
						 * Il check-box relativo all'applicazione
						 * viene disabilitato se il profilo � "AMMI"
						 * o se non passa il controllo 
						 */
				    	 request.setAttribute(applicationName.concat("Disabled"), profilo.equals("AMMI") ? true : !profileManager.checkPassThrough(applicationName, profilo));
					}
				}
				
			}
		}
	}
	
	/**
	 * Abilita/disabilita le drop-down-list Societ�, Utente ed Ente
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
		
	protected UtentePIVA newUtentePIVA(HttpServletRequest request, HashMap<String, Object> parametri, String codop)
	{	
		Calendar now = Calendar.getInstance();
		HttpSession session = request.getSession();
		Calendar dataFineValiditaUtenzaSync = null;
		Calendar dataScadenzaFromWs = null;
		Calendar dataFineValiditaUtenza = (Calendar)parametri.get("dataFineValiditaUtenza");
		UserBean userBean = (UserBean)session.getAttribute("j_user_bean");
		if (codop.equals("edit")) dataScadenzaFromWs = (Calendar)session.getAttribute("dataScadenzaFromWs");
		/*
		 * Sincronizzo "dataFineValiditaUtenza" con "now" se si tratta di un inserimento
		 * oppure di una modifica in cui sia stata modificata la data di fine validit� dell'utenza
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
		com.seda.security.webservice.dati.UtentePIVA seUserPIVA = new UtentePIVA();
		
		/*
		*/
		seUserPIVA.setUsername((String)parametri.get("codiceFiscale"));
		seUserPIVA.setTipologiaUtente(UtentePIVATipologiaUtente.fromString((String) parametri.get("tipologiaUtente")));
		seUserPIVA.setUtenzaAttiva(UtentePIVAUtenzaAttiva.fromString((String)parametri.get("utenzaAttiva")));
		seUserPIVA.setDataInizioValiditaUtenza(null);
		seUserPIVA.setDataFineValiditaUtenza(dataFineValiditaUtenzaSync);
		seUserPIVA.setPrimoAccesso(UtentePIVAPrimoAccesso.fromString("Y"));
		seUserPIVA.setDataScadenzaPassword(null);
		seUserPIVA.setDataUltimoAccesso(null);
		seUserPIVA.setDataInserimentoUtenza(null);
		seUserPIVA.setDatiUtentePIVA(null);
		seUserPIVA.setNote((String)parametri.get("note"));
		seUserPIVA.setDataUltimoAggiornamento(now);
		seUserPIVA.setOperatoreUltimoAggiornamento(userBean.getUserName());
		
		DatiAnagPersonaFisicaDelegato datiAnagPersonaFisicaDelegato = new DatiAnagPersonaFisicaDelegato();
		
		DatiGeneraliPersonaFisicaDelegato datiGeneraliPersonaFisicaDelegato = new DatiGeneraliPersonaFisicaDelegato();
		DatiContattiPersonaFisicaDelegato datiContattiPersonaFisicaDelegato = new DatiContattiPersonaFisicaDelegato();
		
		
		datiGeneraliPersonaFisicaDelegato.setNome((String)parametri.get("nome"));
		datiGeneraliPersonaFisicaDelegato.setCognome((String)parametri.get("cognome"));
		String codiceFiscale = (String)parametri.get("codiceFiscale"); 
		datiGeneraliPersonaFisicaDelegato.setCodiceFiscale(codiceFiscale==null?"":codiceFiscale.toUpperCase());
		datiAnagPersonaFisicaDelegato.setDatiGeneraliPersonaFisicaDelegato(datiGeneraliPersonaFisicaDelegato);
		
		datiContattiPersonaFisicaDelegato.setMail((String)parametri.get("emailNotifiche"));
		datiContattiPersonaFisicaDelegato.setCellulare((String)parametri.get("smsNotifiche"));
		datiAnagPersonaFisicaDelegato.setDatiContattiPersonaFisicaDelegato(datiContattiPersonaFisicaDelegato);
		
		seUserPIVA.setDatiAnagPersonaFisicaDelegato(datiAnagPersonaFisicaDelegato);
		
		//seUserPIVA.setPassword((String)parametri.get("password"));
		//seUserPIVA.setPassword2("");
		//seUserPIVA.setPassword3("");
		//seUserPIVA.setPuk("");
		//seUserPIVA.setEmailNotifiche((String)parametri.get("emailNotifiche"));
		//seUserPIVA.setSmsNotifiche((String)parametri.get("smsNotifiche"));
		return seUserPIVA;
		
	}
	
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
