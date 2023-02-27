/**
 * 
 */
package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.DispatchHtmlAction;

import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoSearchRequest;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoSearchResponse;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizio;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioCancelRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioDetailRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioDetailResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioResponsePageInfo;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSaveRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchRequest3;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class ConfigUtenteTipoServizioAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	@SuppressWarnings("unused")
	private String strCodiceSocieta,strCodiceUtente,usernameAutenticazione;
	private String userCodiceSocieta = null;
	//inizio LP PG200360	
	private String tagCausale = "___CAUS___";
    private ArrayList<String> listaCausali = null;
    private int indexRemove = -1;
    private int indexAdd = -1;
	//fine LP PG200360	

	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
		   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		   pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		   order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
			/*
			 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
			 */
			replyAttributes(false, request,"pageNumber","rowsPerPage","order","action");

		   HttpSession session = request.getSession();
		   UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		   /*
		    * VBruno
		    * "userCodiceSocieta" serve per gestire la profilazione utente; permette 
		    * di limitare la visibilità e l'operatività agli ambiti consentiti.
		    * E' sufficiente la società perchè "configurazione" è accessibile 
		    * solo ad AMMI ed AMSO
		    */
		   if (user != null)
			   userCodiceSocieta = (user.getProfile().equals("AMMI") ? "" : user.getCodiceSocieta());
		   else
			   userCodiceSocieta = "XXXXX";
		   /*
		    * VBruno
		    */
		if(user!=null) {
			   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
			   request.setAttribute("userAppl_codiceSocieta",user.getCodiceSocieta().trim());
			   request.setAttribute("userAppl_codiceUtente",user.getCodiceUtente().trim());
			   //request.setAttribute("userAppl_tipoBollettino",user.getT.getChiaveEnteCon().trim());
			   //request.setAttribute("userAppl_chiaveEnteEnt",userAppl.getChiaveEnteEnt().trim());
			}
		    usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
			strCodiceSocieta = (String)request.getAttribute("userAppl_codiceSocieta");
			strCodiceUtente = (String)request.getAttribute("userAppl_codiceUtente");
			//inizio LP PG200060	
			String template = getTemplateCurrentApplication(request, session);
			//fine LP PG200060

			//inizio LP PG200360	
			listaCausali = (ArrayList<String>) session.getAttribute("listaCausali");
			if(listaCausali == null) {
			    listaCausali = new ArrayList<String>();
			}
			
			if(request.getParameter("button_add_causale") != null) {
				indexRemove = -1;
				indexAdd = 1;
				try {
					edit(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
				session.setAttribute("gestioneCausali", causaliOn(request));
				return;
			} else if(request.getParameter("button_delete_causale") != null) {
				indexRemove = Integer.parseInt(request.getParameter("button_delete_causale"));
				indexAdd = -1;
				session.setAttribute("gestioneCausali", causaliOn(request));
				try {
					edit(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
				return;
			} else {
				String inEdit = (String) request.getSession().getAttribute("inEdit");
				String firedButtonLoc = (String)request.getAttribute("tx_button_reset");
				if(firedButtonLoc != null && inEdit != null && inEdit.trim().length() > 0) {
					session.setAttribute("inEdit", "");
					try {
						edit(request);
					} catch (ActionException e) {
						e.printStackTrace();
					}
					return;
				}
			}
			//fine LP PG200360	
			
			String firedButton = (String)request.getAttribute("tx_button_nuovo");
			if (firedButton!=null){
				if(firedButton.equals("Nuovo")){
					//inizio LP PG200360
					listaCausali = new ArrayList<String>();
					session.setAttribute("listaCausali", listaCausali);
					session.setAttribute("inEdit", "");
					//fine LP PG200360	
					try {
						//inizio LP PG200060	
						if(template.equalsIgnoreCase("regmarche")) {
							salvaFiltriDiRicerca(request, session); //PG130180_001_LP_24012020
						}
						//fine LP PG200060
						add(request);
					} catch (ActionException e) {
						e.printStackTrace();
					}
				}
			}
			//inizio LP PG200060	
			if(template.equalsIgnoreCase("regmarche")) {
				firedButton = (String)request.getAttribute("tx_button_cerca");
				if (firedButton!=null){
					if(firedButton.equals("Cerca")){
						salvaFiltriDiRicerca(request, session); //PG130180_001_LP_24012020
					}
				}
			}
			//fine LP PG200060
			firedButton = (String)request.getAttribute("tx_button_reset");
			//System.out.println(firedButton+"index");
			if (firedButton!=null){
				if(firedButton.equals("Reset")){
					//inizio LP PG200360	
					session.setAttribute("inEdit", "");
					//fine LP PG200360	
					try {
						//inizio LP PG200060	
						if(template.equalsIgnoreCase("regmarche")) {
							svuotaFiltriDiRicercaDaSession(session); //PG130180_001_LP_24012020
						}
						//fine LP PG200060
						add(request);
					} catch (ActionException e) {
						e.printStackTrace();
					}
				}
			}
			firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					//inizio LP PG200360	
					session.setAttribute("inEdit", "");
					//fine LP PG200360	
					try {
						//inizio LP PG200060	
						if(template.equalsIgnoreCase("regmarche")) {
							ripristinaFiltriDiRicerca(request, session); //PG130180_001_LP_24012020
						}
						//fine LP PG200060
						index(request);
					} catch (ActionException e) {
						e.printStackTrace();
					}
				}
			}
			//String firedButtonChanged = request.getParameter("tx_button_changed");
			/*FiredButton firedButtonChanged2 = getFiredButton(request);
			System.out.println(firedButtonChanged2);
			if(firedButtonChanged2.equals(FiredButton.TX_BUTTON_CHANGED)){
				try {
					editDDL(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
			}*/
			//inizio LP PG200360	
			session.setAttribute("gestioneCausali", causaliOn(request));
			//fine LP PG200360	
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		search(request);
		return null;
	}

	public Object search(HttpServletRequest request) throws ActionException {
		String strSocieta = null;
		String strUtente = null;
		String strTipologiaServizio = null;
		String strCodiceTipologiaServizio = null;
		//inizio LP PG200060	
		String template = getTemplateCurrentApplication(request, request.getSession());

		if(template.equalsIgnoreCase("regmarche")) {
			//PG130180_001_LP_24012020
			strSocieta = (String) request.getAttribute("configutentetiposervizio_desSoc");
			strUtente = (String) request.getAttribute("configutentetiposervizio_desUte");
			strTipologiaServizio = (String) request.getAttribute("configutentetiposervizio_desTp");
//			PG22XX09_YL5 INI
			strCodiceTipologiaServizio = (String) request.getAttribute("configutentetiposervizio_desCod");
//			PG22XX09_YL5 FINE
			//FINE PG130180_001_LP_24012020
		}
		//fine LP PG200060	
		
		try {
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButtonReset!=null)
			{				
				resetParametri(request);		
				//inizio LP PG200060	
				if(template.equalsIgnoreCase("regmarche")) {
					strSocieta = null;
					strUtente = null;
					strTipologiaServizio = null;
//					PG22XX09_YL5 INI
					strCodiceTipologiaServizio = null;
//					PG22XX09_YL5 FINE
					
				}
				//fine LP PG200060	
			}
			//inizio LP PG200060	
			if(!template.equalsIgnoreCase("regmarche")) {
			//fine LP PG200060	
			if (firedButton == null && firedButtonReset == null)
			{
				//codiceSocieta = (strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:request.getParameter("configutentetiposervizio_companyCode");
				//codiceUtente = (strCodiceUtente!=null && strCodiceUtente.length()>0)?strCodiceUtente:request.getParameter("configutentetiposervizio_codiceUtente");
				//codiceTipologiaServizio = request.getParameter("configutentetiposervizio_codiceTipologiaServizio");
				strSocieta = (String) request.getAttribute("configutentetiposervizio_desSoc");
				strUtente = (String) request.getAttribute("configutentetiposervizio_desUte");
				strTipologiaServizio = (String) request.getAttribute("configutentetiposervizio_desTp");
				strCodiceTipologiaServizio = (String) request.getAttribute("configutentetiposervizio_desCod");
			}
			//inizio LP PG200060	
			}
			//fine LP PG200060	
			//Commentato da V Bruno
/*			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					codiceTipologiaServizio = request.getParameter("");
					codiceSocieta = request.getParameter("");
					codiceUtente = request.getParameter("");
					strTipologiaServizio = request.getParameter("");
					strSocieta = request.getParameter("");
					strUtente = request.getParameter("");
					
				}
			}
*/			ConfigUtenteTipoServizioSearchResponse searchResponse = null;
			//inizio LP PG200060	
			if(template.equalsIgnoreCase("regmarche")) {
				if (firedButton!=null)
					 if(firedButton.equals("Indietro"))
						 searchResponse =getConfigUtenteTipoServizioSearchResponse(userCodiceSocieta,null,null,strSocieta,strUtente,strTipologiaServizio, rowsPerPage, pageNumber, order, request);
				 if (firedButton==null)  searchResponse =getConfigUtenteTipoServizioSearchResponse(userCodiceSocieta,"",strCodiceTipologiaServizio,strSocieta,strUtente,strTipologiaServizio, rowsPerPage, pageNumber, order, request);
			} else {
			//fine LP PG200060	
			if (firedButton!=null)
				 if(firedButton.equals("Indietro"))
					 searchResponse = getConfigUtenteTipoServizioSearchResponse(userCodiceSocieta,null,null,null,null,null, rowsPerPage, pageNumber, order, request);
				 if (firedButton==null)  searchResponse =getConfigUtenteTipoServizioSearchResponse(userCodiceSocieta,"",strCodiceTipologiaServizio,strSocieta,strUtente,strTipologiaServizio, rowsPerPage, pageNumber, order, request);
			//inizio LP PG200060	
			}
			//fine LP PG200060	
			ConfigUtenteTipoServizioResponse configutentetiposervizioServizioResponse = searchResponse.getResponse();
			ConfigUtenteTipoServizioResponsePageInfo responsePageInfo = configutentetiposervizioServizioResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//Commentato da V Bruno
/*			request.setAttribute("configutentetiposervizio_companyCode", codiceSocieta);
			request.setAttribute("configutentetiposervizio_codiceUtente", codiceUtente);
			request.setAttribute("configutentetiposervizio_codiceTipologiaServizio", codiceTipologiaServizio);
*/			request.setAttribute("configutentetiposervizio_desSoc", strSocieta);
			request.setAttribute("configutentetiposervizio_desUte", strUtente);
			request.setAttribute("configutentetiposervizio_desTp", strTipologiaServizio);
			// request.setAttribute("configutentetiposervizio_desCod", strCodiceTipologiaServizio);
			request.setAttribute("configutentetiposervizios", configutentetiposervizioServizioResponse.getListXml());
			request.setAttribute("configutentetiposervizios.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			/*
			 * La DDL Società/Utente viene caricata tenendo conto della profilazione utente:
			 * "userCodiceSocieta" infatti, se l'utente è un "AMSO" ha il valore del codice Società
			 * associato all'utente
			 */
			ConfigUtenteTipoServizioSearchResponse configUtenteTipoServizioSearchResponse = 
				getConfigUtenteTipoServizioSearchResponse2((new ConfigUtenteTipoServizioSearchRequest2(userCodiceSocieta,"","")), request);
			request.setAttribute("utentetiposervizios", configUtenteTipoServizioSearchResponse.getResponse().getListXml());
			/*
			 * NON viene caricata la DDL della tipologia servizio
			 */
			/*
			 * VIENE caricata comunque la DDL del tipo bollettino
			 */
			String tipoBollettino = "";
			BollettinoSearchResponse bolSearchResponse = getBollettinoSearchResponse(tipoBollettino, "", 0, 0, "", request);
			request.setAttribute("bollettini", bolSearchResponse.getResponse().getListXml());

			//inizio LP PG200360
			if(request.getAttribute("tassonomie") == null) {
				ConfigTassonomiaListaResponse configTassonomiaListaResponse = getTassonomiaListaResponse(request);
				request.setAttribute("tassonomie", configTassonomiaListaResponse.getListaTassonomia().getListXml());
			}
			//fine LP PG200360

		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		request.setAttribute("companyCode",request.getParameter("companyCode"));
        request.setAttribute("codiceUtente",request.getParameter("codiceUtente"));
        request.setAttribute("codiceTipologiaServizio",request.getParameter("codiceTipologiaServizio"));
		return null; 
	}	
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			loadDdlChiave(request);
			//inizio PG200360
			String tipoBoll = "";
			boolean bSPOM = false; 
		    boolean bEditInCorso = false;
			String inEdit = (String) request.getSession().getAttribute("inEdit");
			if(inEdit != null && inEdit.length() > 0) {
				bEditInCorso = true;
			}
			request.getSession().setAttribute("inEdit", "S");
			
			if(!bEditInCorso) {
			//fine PG200360
		
			ConfigUtenteTipoServizioDetailResponse response = 
				getConfigUtenteTipoServizioDetailSearchResponse(request.getParameter("configutentetiposervizio_companyCode"),request.getParameter("configutentetiposervizio_codiceUtente"),request.getParameter("configutentetiposervizio_codiceTipologiaServizio"), request);
			request.setAttribute("configutentetiposervizio_companyCode", response.getConfigutentetiposervizio().getCompanyCode());
			request.setAttribute("configutentetiposervizio_codiceUtente", response.getConfigutentetiposervizio().getUserCode());
			request.setAttribute("configutentetiposervizio_tipoBoll", response.getConfigutentetiposervizio().getTipoBoll());
			request.setAttribute("configutentetiposervizio_codiceTipologiaServizio", response.getConfigutentetiposervizio().getCodiceTipologiaServizio());
			request.setAttribute("configutentetiposervizio_numCC", response.getConfigutentetiposervizio().getNumCC());
			request.setAttribute("configutentetiposervizio_intCC", response.getConfigutentetiposervizio().getIntCC());
			request.setAttribute("configutentetiposervizio_tipoDoc", response.getConfigutentetiposervizio().getTipoDoc());
			request.setAttribute("configutentetiposervizio_flagRangeAbi", response.getConfigutentetiposervizio().getFlagRangeAbi());
			request.setAttribute("configutentetiposervizio_mailDest", response.getConfigutentetiposervizio().getMailDest());
			request.setAttribute("configutentetiposervizio_mailCCNascosta", response.getConfigutentetiposervizio().getEmailCCNascosta());
			request.setAttribute("configutentetiposervizio_mailMitt", response.getConfigutentetiposervizio().getEmailMitt());
			request.setAttribute("configutentetiposervizio_desMitt", response.getConfigutentetiposervizio().getDesMitt());
			request.setAttribute("configutentetiposervizio_flagAll", response.getConfigutentetiposervizio().getFlagAll());
			request.setAttribute("configutentetiposervizio_codiceSia", response.getConfigutentetiposervizio().getCodiceSia());
			request.setAttribute("configutentetiposervizio_codiceIban", response.getConfigutentetiposervizio().getCodiceIban());
			request.setAttribute("configutentetiposervizio_secondoCodiceIban", response.getConfigutentetiposervizio().getSecondoCodiceIban());	//PG150180_001 GG
			request.setAttribute("configutentetiposervizio_funzPag", response.getConfigutentetiposervizio().getFunzPag());
			request.setAttribute("configutentetiposervizio_flagFunzPagProt", response.getConfigutentetiposervizio().getFlagFunzPagProt());
			request.setAttribute("configutentetiposervizio_urlWeb", response.getConfigutentetiposervizio().getUrlWeb());
			request.setAttribute("configutentetiposervizio_flagTpPag", response.getConfigutentetiposervizio().getFlagTpPag().trim());
			request.setAttribute("configutentetiposervizio_flagIntegrazioneSeda", response.getConfigutentetiposervizio().getFlagIntegrazioneSeda());
			request.setAttribute("configutentetiposervizio_codiceUtenteSeda", response.getConfigutentetiposervizio().getCodiceUtenteSeda().trim());
			request.setAttribute("configutentetiposervizio_flagNotificaPagamento", response.getConfigutentetiposervizio().getFlagNotificaPagamento());  //PG170150 CT
			request.setAttribute("configutentetiposervizio_urlServizioWebNotificaPagamento", response.getConfigutentetiposervizio().getUrlServizioWebNotificaPagamento());  //PG170150 CT
			request.setAttribute("chk_flagPagoPA", response.getConfigutentetiposervizio().getFlagPagoPA().equals("Y"));	//TODO da verificare
			//inizio PG180290
			request.setAttribute("configutentetiposervizio_tipoDovuto", response.getConfigutentetiposervizio().getTipoDovuto());
			//fine PG180290
			//inizio SB PG210140
			request.setAttribute("configutentetiposervizio_capitolo", response.getConfigutentetiposervizio().getCapitolo());
			request.setAttribute("configutentetiposervizio_articolo", response.getConfigutentetiposervizio().getArticolo());
			request.setAttribute("configutentetiposervizio_codiceContabilita", response.getConfigutentetiposervizio().getCodiceContabilita());
			request.setAttribute("configutentetiposervizio_annoCompetenza", response.getConfigutentetiposervizio().getAnnoCompetenza());
			//fine SB PG210140
			
			//inizio PG200360
				String keyTassonomia = "";
				if(response.getConfigutentetiposervizio().getChiaveTassonomia() != null) {
					keyTassonomia = response.getConfigutentetiposervizio().getChiaveTassonomia();
					if(keyTassonomia.equals("0")) {
						keyTassonomia = "";
					}
				}
				request.setAttribute("configutentetiposervizio_chiaveTassonomia", keyTassonomia);
				tipoBoll = response.getConfigutentetiposervizio().getTipoBoll();
				bSPOM = (tipoBoll != null && tipoBoll.equalsIgnoreCase("SPOM"));
				String causali = response.getConfigutentetiposervizio().getCausali();
				
				if(causaliOn(request)) {
					listaCausali = new ArrayList<String>();
					if(bSPOM) {
						if(causali != null && causali.trim().length() > 0) {
							String [] splitCausali = causali.split(tagCausale);
							for (String appo : splitCausali) {
								if(appo.length() > 0) {
									listaCausali.add(appo);
								}
							}
						}
					}
				    request.getSession().setAttribute("listaCausali", listaCausali);
					request.setAttribute("listaCausali", listaCausali);
				}
			} else {
				tipoBoll = (String) request.getAttribute("configutentetiposervizio_tipoBoll");
				bSPOM = (tipoBoll != null && tipoBoll.equalsIgnoreCase("SPOM"));
				if(causaliOn(request)) {
					if(bSPOM) {
						if(listaCausali != null) {
							int k = listaCausali.size();
							listaCausali = new ArrayList<String>();
						    for (int i = 0; i < k; i++) {
						    	if(indexRemove == i) {
						    		indexRemove = -1;
						    		continue;
						    	}
						    	String curr = request.getParameter(String.format("causale_%d_testo", i));
						    	if(curr != null) {
						    		listaCausali.add(curr.trim());
						    	}
						    }
						    if(indexAdd > 0) {
						    	indexAdd = -1;
					    		listaCausali.add("");
						    }
						} else {
							listaCausali = new ArrayList<String>();
						}
					} else {
						listaCausali = new ArrayList<String>();
					}
				    request.getSession().setAttribute("listaCausali", listaCausali);
					request.setAttribute("listaCausali", listaCausali);
				}
			}
			//fine PG200360
			
			/*
			 * VIENE caricata la DDL del tipo bollettino
			 */
			//inizio PG200360
			//String tipoBollettino = "";
			//BollettinoSearchResponse bolSearchResponse = getBollettinoSearchResponse(tipoBollettino, "", 0, 0, "", request);
			//request.setAttribute("bollettini", bolSearchResponse.getResponse().getListXml());
			//fine PG200360
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object addDDL(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("fired_button_hidden", null);
			String arrStr = request.getParameter("configutentetiposervizio_strUtentetiposervizios");
			if (arrStr != null)
			{
				String[] strUtentetiposervizios = arrStr.split("\\|");
				/*
				 * Se non è stata selezionata alcuna coppia Società/Utente NON VIENE
				 * caricata la DDL Tipo Servizio.
				 * 
				 * Se è stata selezionata una coppia Società/Utente VIENE caricata la DDL
				 * Tipo Servizio in base alla Società selezionata
				 */
				if (!arrStr.equals(""))
				{
					ConfigUtenteTipoServizioSearchResponse configUtenteTipoServizioSearchResponse2 = null;
					configUtenteTipoServizioSearchResponse2 = 
						getConfigUtenteTipoServizioSearchResponse3((new ConfigUtenteTipoServizioSearchRequest3(strUtentetiposervizios[0])), request);
					request.setAttribute("utentetiposervizios2", configUtenteTipoServizioSearchResponse2.getResponse().getListXml());
				}
				/*
				 * La DDL Società/Utente viene caricata tenendo conto della profilazione utente:
				 * "userCodiceSocieta" infatti, se l'utente è un "AMSO" ha il valore del codice Società
				 * associato all'utente
				 */
				ConfigUtenteTipoServizioSearchResponse configUtenteTipoServizioSearchResponse = getConfigUtenteTipoServizioSearchResponse2((new ConfigUtenteTipoServizioSearchRequest2(userCodiceSocieta,"","")), request);
				request.setAttribute("utentetiposervizios", configUtenteTipoServizioSearchResponse.getResponse().getListXml());
			}
			/*
			 * VIENE caricata comunque la DDL del tipo bollettino
			 */
			String tipoBollettino = "";
			BollettinoSearchResponse bolSearchResponse = getBollettinoSearchResponse(tipoBollettino, "", 0, 0, "", request);
			request.setAttribute("bollettini", bolSearchResponse.getResponse().getListXml());
			//inizio LP PG200360
			if(request.getAttribute("tassonomie") == null) {
				ConfigTassonomiaListaResponse configTassonomiaListaResponse = getTassonomiaListaResponse(request);
				request.setAttribute("tassonomie", configTassonomiaListaResponse.getListaTassonomia().getListXml());
			}
			//fine LP PG200360
		} catch (FaultType e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object saveadd(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.INS_OK.format());
			//inizio LP PG200360	
			if(request.getParameter("button_add_causale") != null) {
				if(indexAdd > 0) {
			    	indexAdd = -1;
					if(listaCausali == null) {
					    listaCausali = new ArrayList<String>();
					}
		    		listaCausali.add("");
				    request.getSession().setAttribute("listaCausali", listaCausali);
					request.setAttribute("listaCausali", listaCausali);
				}
				return null;
			} else if(request.getParameter("button_delete_causale") != null) {
				return null;
			} else {
				String inEdit = (String) request.getSession().getAttribute("inEdit");
				String firedButtonLoc = (String)request.getAttribute("tx_button_reset");
				if(firedButtonLoc != null && inEdit != null && inEdit.trim().length() > 0) {
					return null;
				}
			}
			//fine LP PG200360	
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			FiredButton firedButtonChanged2 = getFiredButton(request);
			if(firedButtonChanged2.equals(FiredButton.TX_BUTTON_CHANGED))
				addDDL(request);
			else if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("configutentetiposervizio_companyCode") !=null){
						request.setAttribute("configutentetiposervizio_companyCode", null);
						request.setAttribute("configutentetiposervizio_codiceUtente", null);
						request.setAttribute("configutentetiposervizio_strTp", null);
						request.setAttribute("configutentetiposervizio_strSoc", null);
						request.setAttribute("configutentetiposervizio_strUte", null);
						request.setAttribute("configutentetiposervizio_codiceTipologiaServizio", null);
						index(request);
					}
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){					
					resetParametri(request);
				}
			}
			else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}

	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.UPDT_OK.format());
			//inizio LP PG200360	
			if(request.getParameter("button_add_causale") != null) {
				return null;
			} else if(request.getParameter("button_delete_causale") != null) {
				return null;
			} else {
				String inEdit = (String) request.getSession().getAttribute("inEdit");
				String firedButtonLoc = (String)request.getAttribute("tx_button_reset");
				if(firedButtonLoc != null && inEdit != null && inEdit.trim().length() > 0) {
					return null;
				}
			}
			//fine LP PG200360	
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			FiredButton firedButtonChanged2 = getFiredButton(request);
			if(firedButtonChanged2.equals(FiredButton.TX_BUTTON_CHANGED))
				loadDdlChiave(request);
			else if (firedButton!=null){
					if(firedButton.equals("Indietro")){
						if(request.getAttribute("configutentetiposervizio_companyCode") !=null){
							request.setAttribute("configutentetiposervizio_companyCode", null);
							request.setAttribute("configutentetiposervizio_codiceUtente", null);
							request.setAttribute("configutentetiposervizio_codiceTipologiaServizio", null);
							index(request);
						}
					}
				 }
				 else if(firedButtonReset!=null){
					 	if(firedButtonReset.equals("Reset")){					
					 		edit(request);
					    }
				      }else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	private void save(HttpServletRequest request) throws ActionException {
		String codiceUtente = "";
		String codiceTipologiaServizio = "";
		String companyCode = "";
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		//fine  LP PG200360
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		String arrStr = request.getParameter("configutentetiposervizio_strUtentetiposervizios");
		String arrStr2 = request.getParameter("configutentetiposervizio_codiceTipologiaServizio");
		String cod = request.getParameter("cod");
		//inizio LP PG200360
		//NOTA: il valore del parametro "codop" è gestito male sulle jsp uso 
		//      come discriminante tra operazione di "add" o "edit" il parametro "cod"
		//fine LP PG200360
		if (cod.equals("0") && arrStr!=null && arrStr.length()>0)
		{
			  String[] strUtentetiposervizios = arrStr.split("\\|");
			  companyCode = strUtentetiposervizios[0];
 		      codiceUtente= strUtentetiposervizios[1];
 		      strUtentetiposervizios = arrStr2.split("\\|");
 		      codiceTipologiaServizio= strUtentetiposervizios[0];
			  //inizio LP PG200360
 		      codOp = TypeRequest.ADD_SCOPE.scope();  
			  //fine LP PG200360
 		}
		if(cod.equals("1"))
		{
			  companyCode = request.getParameter("configutentetiposervizio_companyCode");
              codiceUtente= request.getParameter("configutentetiposervizio_codiceUtente");
              codiceTipologiaServizio= request.getParameter("configutentetiposervizio_codiceTipologiaServizio");
  			  //inizio LP PG200360
   		      codOp = TypeRequest.EDIT_SCOPE.scope();  
  			  //fine LP PG200360
		}
		
		String tipoBoll = (String) request.getAttribute("configutentetiposervizio_tipoBoll");
		String numCC= (String)request.getAttribute("configutentetiposervizio_numCC");
		String intCC= (String)request.getAttribute("configutentetiposervizio_intCC");
		String tipoDoc= (String)request.getAttribute("configutentetiposervizio_tipoDoc");
		String flagRangeAbi= (String)request.getAttribute("configutentetiposervizio_flagRangeAbi");
		String mailDest= (String)request.getAttribute("configutentetiposervizio_mailDest");
		String mailCCNascosta= (String)request.getAttribute("configutentetiposervizio_mailCCNascosta");
		String mailMitt= (String)request.getAttribute("configutentetiposervizio_mailMitt");
		String desMitt= (String)request.getAttribute("configutentetiposervizio_desMitt");
		String flagAll= (String)request.getAttribute("configutentetiposervizio_flagAll");
		String codiceSia= (String)request.getAttribute("configutentetiposervizio_codiceSia");
		String codiceIban= (String)request.getAttribute("configutentetiposervizio_codiceIban");
		String secondoCodiceIban= (String)request.getAttribute("configutentetiposervizio_secondoCodiceIban");	//PG150180_001 GG
		String funzPag= (String)request.getAttribute("configutentetiposervizio_funzPag");
		String flagFunzPagProt= (String)request.getAttribute("configutentetiposervizio_flagFunzPagProt");
		String urlWeb= (String)request.getAttribute("configutentetiposervizio_urlWeb");
		String flagTpPag= (String)request.getAttribute("configutentetiposervizio_flagTpPag");
		String flagIntegrazioneSeda = (String)request.getAttribute("configutentetiposervizio_flagIntegrazioneSeda");
		String codiceUtenteSeda = (String)request.getAttribute("configutentetiposervizio_codiceUtenteSeda"); 
		String flagNotificaPagamento = (String)request.getAttribute("configutentetiposervizio_flagNotificaPagamento");
		String urlServizioWebNotificaPagamento = (String)request.getAttribute("configutentetiposervizio_urlServizioWebNotificaPagamento");
		
		//inizio SB PG210140
		String articolo = (String)request.getAttribute("configutentetiposervizio_articolo")==null ? "" : (String)request.getAttribute("configutentetiposervizio_articolo");
		String capitolo = (String)request.getAttribute("configutentetiposervizio_capitolo")==null ? "" : (String)request.getAttribute("configutentetiposervizio_capitolo");
		String codiceContabilita = (String)request.getAttribute("configutentetiposervizio_codiceContabilita")==null ? "" : (String)request.getAttribute("configutentetiposervizio_codiceContabilita");
		String annoCompetenza = (String)request.getAttribute("configutentetiposervizio_annoCompetenza")==null ? "" : (String)request.getAttribute("configutentetiposervizio_annoCompetenza");
		//fine SB PG210140
		
		//inizio LP PG200060
		//String flagPagoPA = (request.getAttribute("flagPagoPA") == null ? "" : "Y");
		String flagPagoPA = "";
		String template = getTemplateCurrentApplication(request, request.getSession());
		if(!template.equalsIgnoreCase("regmarche")) {
			flagPagoPA = (request.getAttribute("flagPagoPA") == null ? "" : "Y");
		}
		//fine LP PG200060
		//inizio LP PG180290
		//inizio LP PG180290 2020-05-21
		//String tipoDovuto = null;
		String tipoDovuto = "";
		//fine LP PG180290 2020-05-21
		if(template.equalsIgnoreCase("trentrisc")) {
			tipoDovuto = (String) request.getAttribute("configutentetiposervizio_tipoDovuto");
			//inizio LP PG180290 2020-05-21
			if(tipoDovuto == null) {
				tipoDovuto = "";
			} else {
				tipoDovuto = tipoDovuto.trim(); 
			}
			//fine LP PG180290 2020-05-21
		}
		//fine LP PG180290

		//inizio LP PG200360
	    String chiaveTassonomia = (String) request.getAttribute("configutentetiposervizio_chiaveTassonomia");
	    if(chiaveTassonomia != null)
	    	chiaveTassonomia = chiaveTassonomia.trim();
	    if(chiaveTassonomia.length() == 0 || chiaveTassonomia.equals("0")) {
	    	//non dovrebbe succedere da interfaccia deve arrivare un valore =/= "0"
	    	chiaveTassonomia = null;
	    }
	    String causali = null;
		if(causaliOn(request)) {
			boolean bSPOM = (tipoBoll != null && tipoBoll.equalsIgnoreCase("SPOM"));
			causali = "";
			if(bSPOM) {
			    String causalONE = "";
				if(listaCausali == null) {
					listaCausali = (ArrayList<String>) request.getSession().getAttribute("listaCausali");
				}
				if(listaCausali != null) {
				    // righe causali
					int k = listaCausali.size();
					listaCausali.clear();
				    for (int i = 0; i < k; i++) {
				    	String curr = request.getParameter(String.format("causale_%d_testo", i));
				    	if(curr != null && curr.trim().length() > 0) {
				    		if(causalONE.length() > 0) {
				    			causalONE += tagCausale;
				    		}
				    		curr = curr.trim();
				    		causalONE += curr;
				    		listaCausali.add(curr);
				    	}
				    }
				    request.getSession().setAttribute("listaCausali", listaCausali);
					request.setAttribute("listaCausali", listaCausali);
				}
				causali = causalONE;
			}
		}
		//fine LP PG200360

		try {
			addDDL(request);
			if (checkForm(flagIntegrazioneSeda, codiceUtenteSeda, request))
			{
				//inizio LP PG200420 - Numero CC-Iban Postale
				numCC = (numCC != null ? numCC.trim() : "");
//				boolean bSPOM = (tipoBoll != null && tipoBoll.equalsIgnoreCase("SPOM"));
//				if(!template.equalsIgnoreCase("regmarche")
//				   && bSPOM
//				   && !GenericsDateNumbers.verificaNumeroCCIbanPostale(numCC, codiceIban, secondoCodiceIban))
//				{
//					setFormMessage("frmAction", "Numero CC: deve essere di un 'Iban Postale' associato alla Tipologia Servizio", request);
//					request.setAttribute("done", null);
//					return;
//				}
				//fine LP PG200420 - Numero CC-Iban Postale
			 	//PG150180_001 GG - Inserito secondoCodiceIban
				//inizio LP PG180290
				/*
				ConfigUtenteTipoServizio configUtente = new ConfigUtenteTipoServizio(companyCode,codiceUtente,codiceTipologiaServizio,tipoBoll,
						numCC,intCC,tipoDoc,flagRangeAbi,mailDest,mailCCNascosta,mailMitt,desMitt,flagAll,codiceSia,codiceIban,secondoCodiceIban,funzPag,flagFunzPagProt,
						urlWeb,flagTpPag,flagIntegrazioneSeda,codiceUtenteSeda,usernameAutenticazione, flagNotificaPagamento, urlServizioWebNotificaPagamento,flagPagoPA);
				*/		
				//inizio LP PG200360
				//ConfigUtenteTipoServizio configUtente = new ConfigUtenteTipoServizio(companyCode,codiceUtente,codiceTipologiaServizio,tipoBoll,
				//		numCC,intCC,tipoDoc,flagRangeAbi,mailDest,mailCCNascosta,mailMitt,desMitt,flagAll,codiceSia,codiceIban,secondoCodiceIban,funzPag,flagFunzPagProt,
				//		urlWeb,flagTpPag,flagIntegrazioneSeda,codiceUtenteSeda,usernameAutenticazione, flagNotificaPagamento, urlServizioWebNotificaPagamento,flagPagoPA, tipoDovuto);
				ConfigUtenteTipoServizio configUtente = new ConfigUtenteTipoServizio(companyCode,codiceUtente,codiceTipologiaServizio,tipoBoll,
						numCC,intCC,tipoDoc,flagRangeAbi,mailDest,mailCCNascosta,mailMitt,desMitt,flagAll,codiceSia,codiceIban,secondoCodiceIban,funzPag,flagFunzPagProt,
						urlWeb,flagTpPag,flagIntegrazioneSeda,codiceUtenteSeda,usernameAutenticazione, flagNotificaPagamento, urlServizioWebNotificaPagamento,flagPagoPA, tipoDovuto,
						chiaveTassonomia, causali, articolo, codiceContabilita , capitolo, annoCompetenza);
				//fin LP PG200360
				//fine LP PG180290
				/* we prepare object for save */
				ConfigUtenteTipoServizioSaveRequest saveRequest = new ConfigUtenteTipoServizioSaveRequest(configUtente,codOp);
				/* we save object */
				com.seda.payer.pgec.webservice.configutentetiposervizio.dati.StatusResponse res = WSCache.configUtenteTipoServizioServer.save(saveRequest, request);
	
				if(res != null)
				{
					com.seda.payer.pgec.webservice.configutentetiposervizio.dati.Response response = res.getResponse();
					if (response != null)
					{
						if (!response.getReturncode().equals(ResponseReturncode.value1))
						{
							request.setAttribute("error", "error"); 
							request.setAttribute("done", null);
							if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) setFormMessage("frmAction", Messages.INS_ERR.format("L'informazione è già presente in archivio"), request); 
							if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0)
							{
								setFormMessage("frmAction", Messages.UPDT_ERR.format(), request);
								loadDdlChiave(request);
							}
						}
						//inizio LP PG200360
						else {
						//reset parametri gestione causali
						    listaCausali = new ArrayList<String>();
						    request.getSession().setAttribute("listaCausali", listaCausali);
							request.setAttribute("listaCausali", listaCausali);
						    request.getSession().setAttribute("inEdit", "");
						}
					    //fine LP PG200360
					}
					else
					{
						request.setAttribute("error", "error"); 
						request.setAttribute("done", null);
						request.setAttribute("message", Messages.INS_ERR.format());				
					}
				}
				else
				{
					request.setAttribute("error", "error"); 
					request.setAttribute("done", null);
					request.setAttribute("message", Messages.INS_ERR.format());			
				}
			}
			else
				request.setAttribute("done", null);
			
		} catch (Exception e) {
			try {
				ConfigUtenteTipoServizioDetailResponse detailResponse =getConfigUtenteTipoServizio(companyCode,codiceUtente,codiceTipologiaServizio, request);
				//request.setAttribute("configutentetiposervizio", detailResponse.getConfigutentetiposervizio());
				request.setAttribute("configutentetiposervizio_companyCode", detailResponse.getConfigutentetiposervizio().getCompanyCode());
				request.setAttribute("configutentetiposervizio_codiceUtente", detailResponse.getConfigutentetiposervizio().getUserCode());
				request.setAttribute("configutentetiposervizio_codiceTipologiaServizio", detailResponse.getConfigutentetiposervizio().getCodiceTipologiaServizio());
				request.setAttribute("configutentetiposervizio_tipoBoll", detailResponse.getConfigutentetiposervizio().getTipoBoll());
				request.setAttribute("configutentetiposervizio_numCC", detailResponse.getConfigutentetiposervizio().getNumCC());
				request.setAttribute("configutentetiposervizio_intCC", detailResponse.getConfigutentetiposervizio().getIntCC());
				request.setAttribute("configutentetiposervizio_tipoDoc", detailResponse.getConfigutentetiposervizio().getTipoDoc());
				request.setAttribute("configutentetiposervizio_flagRangeAbi", detailResponse.getConfigutentetiposervizio().getFlagRangeAbi());
				request.setAttribute("configutentetiposervizio_mailDest", detailResponse.getConfigutentetiposervizio().getMailDest());
				request.setAttribute("configutentetiposervizio_mailCCNascosta", detailResponse.getConfigutentetiposervizio().getEmailCCNascosta());
				request.setAttribute("configutentetiposervizio_mailMitt", detailResponse.getConfigutentetiposervizio().getEmailMitt());
				request.setAttribute("configutentetiposervizio_desMitt", detailResponse.getConfigutentetiposervizio().getMailDest());
				request.setAttribute("configutentetiposervizio_flagAll", detailResponse.getConfigutentetiposervizio().getFlagAll());
				request.setAttribute("configutentetiposervizio_codiceSia", detailResponse.getConfigutentetiposervizio().getCodiceSia());
				request.setAttribute("configutentetiposervizio_codiceIban", detailResponse.getConfigutentetiposervizio().getCodiceIban());
				request.setAttribute("configutentetiposervizio_funzPag", detailResponse.getConfigutentetiposervizio().getFunzPag());
				request.setAttribute("configutentetiposervizio_flagFunzPagProt", detailResponse.getConfigutentetiposervizio().getFlagFunzPagProt());
				request.setAttribute("configutentetiposervizio_urlWeb", detailResponse.getConfigutentetiposervizio().getUrlWeb());
				request.setAttribute("configutentetiposervizio_flagTpPag", detailResponse.getConfigutentetiposervizio().getFlagTpPag());
				request.setAttribute("configutentetiposervizio_flagIntegrazioneSeda", detailResponse.getConfigutentetiposervizio().getFlagIntegrazioneSeda());
				request.setAttribute("configutentetiposervizio_codiceUtenteSeda", detailResponse.getConfigutentetiposervizio().getCodiceUtenteSeda().trim());
				request.setAttribute("configutentetiposervizio_flagNotificaPagamento", detailResponse.getConfigutentetiposervizio().getFlagNotificaPagamento());
				request.setAttribute("configutentetiposervizio_urlServizioWebNotificaPagamento", detailResponse.getConfigutentetiposervizio().getUrlServizioWebNotificaPagamento());
				request.setAttribute("chk_flagPagoPA", detailResponse.getConfigutentetiposervizio().getFlagPagoPA().equals("Y"));	//TODO da verificare
				
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	private boolean checkForm(String flagIntegrazioneSeda, String codiceUtenteSeda, HttpServletRequest request)
	{
		if (flagIntegrazioneSeda.equals("Y"))
		{
			if (codiceUtenteSeda.trim().equals(""))
			{
				setFormMessage("frmAction", "In caso di Integrazione Entrate = 'Si' è necessario impostare il Codice Utente Entrate", request);
				return false;
			}
		}
//		else if (flagIntegrazioneSeda.equals("N"))
//		{
//			if (!codiceUtenteSeda.trim().equals(""))
//			{
//				setFormMessage("frmAction", "In caso di Integrazione Entrate = 'No' il Codice Utente Seda non deve essere impostato", request);
//				return false;
//			}
//		}
		return true;
	}
	
	
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String companyCode = request.getParameter("configutentetiposervizio_companyCode");
        String codiceUtente= request.getParameter("configutentetiposervizio_codiceUtente");
        String codiceTipologiaServizio= request.getParameter("configutentetiposervizio_codiceTipologiaServizio");
		try {
			request.setAttribute("varname", "configutentetiposervizio");
			//stub = new ConfigUtenteTipoServizioImplementationStub(new URL(getConfigutentetiposervizioManagerEndpointUrl()), null);
			/* we prepare object for cancel */
			ConfigUtenteTipoServizioCancelRequest cancelRequest = new ConfigUtenteTipoServizioCancelRequest(companyCode,codiceUtente,codiceTipologiaServizio);
			/* we cancel object */
			cancelConfigUtenteTipoServizio(cancelRequest, request);
			request.setAttribute("message", Messages.CANC_OK.format());
		} catch (FaultType f) {
			request.setAttribute("message", Messages.CANCEL_ERRDIP.format());
			//request.setAttribute("message", Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) {
			request.setAttribute("message", Messages.CANCEL_ERRDIP.format());
		}
		return null;
	}
	
	protected int getDefaultListRows()
	{
		int defaultListRows = 4;
		PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		if(configuration != null)
		{
			String s_defaultListRows = configuration.getProperty(PropertiesPath.defaultListRows.format());
			if (s_defaultListRows != null) defaultListRows = Integer.parseInt(s_defaultListRows);
		}
		return defaultListRows;
	}
	
	private ConfigUtenteTipoServizioSearchResponse getConfigUtenteTipoServizioSearchResponse(
			String companyCode, String codiceUtente, String codiceTipologiaServizio,String strSocieta, String strUtente, String strTipologiaServizio, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigUtenteTipoServizioSearchResponse res = null;
		ConfigUtenteTipoServizioSearchRequest in = new ConfigUtenteTipoServizioSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setCodiceUtente(codiceUtente== null ? "" : codiceUtente);
		in.setStrDescrSocieta(strSocieta == null ? "" : strSocieta);
		in.setStrDescrUtente(strUtente == null ? "" : strUtente);
		in.setStrDescrTipologiaServizio(strTipologiaServizio == null ? "" : strTipologiaServizio);
		
		res = WSCache.configUtenteTipoServizioServer.getConfigUtenteTipoServizios(in, request);
		return res;
	}
	
	private ConfigUtenteTipoServizioSearchResponse getConfigUtenteTipoServizioSearchResponse2(
			ConfigUtenteTipoServizioSearchRequest2 configUtenteTipoServizio, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigUtenteTipoServizioSearchResponse res = null;
		ConfigUtenteTipoServizioSearchRequest2 in = new ConfigUtenteTipoServizioSearchRequest2();
		/*in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);*/
		in.setCompanyCode(configUtenteTipoServizio.getCompanyCode() == null ? "" : configUtenteTipoServizio.getCompanyCode());
		//in.setCodiceTipologiaServizio(configUtenteTipoServizio.g.getcodiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setCodiceUtente(configUtenteTipoServizio.getCodiceUtente() == null ? "" : configUtenteTipoServizio.getCodiceUtente());
		in.setProcName("");
		res = WSCache.configUtenteTipoServizioServer.getConfigUtenteTipoServizios2(in, request);
		return res;
	}
	
	private ConfigUtenteTipoServizioSearchResponse getConfigUtenteTipoServizioSearchResponse3(
			ConfigUtenteTipoServizioSearchRequest3 configUtenteTipoServizio, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigUtenteTipoServizioSearchResponse res = null;
		ConfigUtenteTipoServizioSearchRequest3 in = new ConfigUtenteTipoServizioSearchRequest3();
		/*in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);*/
		in.setCompanyCode(configUtenteTipoServizio.getCompanyCode() == null ? "" : configUtenteTipoServizio.getCompanyCode());
		//in.setCodiceTipologiaServizio(configUtenteTipoServizio.g.getcodiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		
		
		res = WSCache.configUtenteTipoServizioServer.getConfigUtenteTipoServizios3(in, request);
		return res;
	}
	
	private ConfigUtenteTipoServizioDetailResponse getConfigUtenteTipoServizioDetailSearchResponse(String companyCode, String userCode, String tipologiaServizioCode, HttpServletRequest request ) throws FaultType, RemoteException
	{
		 
		ConfigUtenteTipoServizioDetailResponse res = null;
		ConfigUtenteTipoServizioDetailRequest in = new ConfigUtenteTipoServizioDetailRequest(companyCode,userCode, tipologiaServizioCode);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setCodiceTipologiaServizio(tipologiaServizioCode == null ? "" : tipologiaServizioCode);
		in.setUserCode(userCode== null ? "" :userCode);
		
		res = WSCache.configUtenteTipoServizioServer.getConfigUtenteTipoServizio(in, request);
		return res;
	}
	
	
	private void  cancelConfigUtenteTipoServizio(ConfigUtenteTipoServizioCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		WSCache.configUtenteTipoServizioServer.cancel(cancelRequest, request);
	}
	
	private ConfigUtenteTipoServizioDetailResponse getConfigUtenteTipoServizio(String companyCode, String codiceUtente, String codiceTipologiaServizio, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
	{
		ConfigUtenteTipoServizioDetailRequest detailRequest = new ConfigUtenteTipoServizioDetailRequest(companyCode,codiceUtente,codiceTipologiaServizio);
		detailRequest.setCompanyCode(companyCode == null ? "" : companyCode);
		detailRequest.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		detailRequest.setUserCode(codiceUtente == null ? "" : codiceUtente);
		return WSCache.configUtenteTipoServizioServer.getConfigUtenteTipoServizio(detailRequest, request);
	}
	
	private BollettinoSearchResponse getBollettinoSearchResponse(
			String typeBollettino, String bollettinoDescription, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		BollettinoSearchResponse res = null;
		BollettinoSearchRequest in = new BollettinoSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setTipoBollettino(typeBollettino == null ? "" : typeBollettino);
		in.setDesTipoBollettino(bollettinoDescription == null ? "" : bollettinoDescription);
		
		res = WSCache.bollettinoServer.getBollettini(in, request);
		return res;
	}
	
	//inizio LP PG200360
	private ConfigTassonomiaListaResponse getTassonomiaListaResponse(HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigTassonomiaListaResponse res = null;
		ConfigTassonomiaListaRequest in = new ConfigTassonomiaListaRequest();
		in.setRowsPerPage(0);
		in.setPageNumber(0);
		in.setOrder("SPIA"); // ordina per "dati specifici incasso" 
		Date oggi = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dataValidita = df.format(oggi);
		in.setDataInizioValidita_da(dataValidita);
		in.setDataFineValidita_a(dataValidita);
		
		res = WSCache.configTassonomiaServer.lista(in, request);
		return res;
	}
	//fine LP PG200360

	@SuppressWarnings("unchecked")
	protected void resetParametri(HttpServletRequest request) {
		Enumeration e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			
			p = (String) e.nextElement();
			request.setAttribute(p, "");
		}
		return;
	}
	
	private void loadDdlChiave(HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigUtenteTipoServizioSearchResponse configUtenteTipoServizioSearchResponse = getConfigUtenteTipoServizioSearchResponse2((new ConfigUtenteTipoServizioSearchRequest2(userCodiceSocieta,"","")), request);
		request.setAttribute("utentetiposervizios", configUtenteTipoServizioSearchResponse.getResponse().getListXml());
	
		ConfigUtenteTipoServizioSearchResponse configUtenteTipoServizioSearchResponse2 = null;
		configUtenteTipoServizioSearchResponse2 = 
			getConfigUtenteTipoServizioSearchResponse3((new ConfigUtenteTipoServizioSearchRequest3(request.getParameter("configutentetiposervizio_companyCode"))), request);
		request.setAttribute("utentetiposervizios2", configUtenteTipoServizioSearchResponse2.getResponse().getListXml());
		
		String tipoBollettino = "";
		BollettinoSearchResponse bolSearchResponse = getBollettinoSearchResponse(tipoBollettino, "", 0, 0, "", request);
		request.setAttribute("bollettini", bolSearchResponse.getResponse().getListXml());
		
		//inizio LP PG200360
		if(request.getAttribute("tassonomie") == null) {
			ConfigTassonomiaListaResponse configTassonomiaListaResponse = getTassonomiaListaResponse(request);
			request.setAttribute("tassonomie", configTassonomiaListaResponse.getListaTassonomia().getListXml());
		}
		//fine LP PG200360
	}

	//inizio LP PG200060
	//PG130180_001_LP_24012020
	private void salvaFiltriDiRicerca(HttpServletRequest request, HttpSession session) {
		svuotaFiltriDiRicercaDaSession(session);
		session.setAttribute("configutentetiposervizio_desSoc", request.getAttribute("configutentetiposervizio_desSoc")!=null?request.getAttribute("configutentetiposervizio_desSoc"):"");
		session.setAttribute("configutentetiposervizio_desUte", request.getAttribute("configutentetiposervizio_desUte")!=null?request.getAttribute("configutentetiposervizio_desUte"):"");
		session.setAttribute("configutentetiposervizio_desTp", request.getAttribute("configutentetiposervizio_desTp")!=null?request.getAttribute("configutentetiposervizio_desTp"):"");
	}
	private HttpServletRequest ripristinaFiltriDiRicerca(HttpServletRequest request, HttpSession session) {
		request.setAttribute("configutentetiposervizio_desSoc",session.getAttribute("configutentetiposervizio_desSoc"));
		request.setAttribute("configutentetiposervizio_desUte",session.getAttribute("configutentetiposervizio_desUte"));
		request.setAttribute("configutentetiposervizio_desTp",session.getAttribute("configutentetiposervizio_desTp"));
		svuotaFiltriDiRicercaDaSession(session);
		return request;
	}
	private void svuotaFiltriDiRicercaDaSession(HttpSession session) {
		session.setAttribute("configutentetiposervizio_desSoc",null);
		session.setAttribute("configutentetiposervizio_desUte",null);
		session.setAttribute("configutentetiposervizio_desTp",null);
	}
	//FINE PG130180_001_LP_24012020

	private String getTemplateCurrentApplication(HttpServletRequest request, HttpSession session)
	{
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		if (applicationName != null)
		{
			UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
			if (userBean != null)
			{
				String templateName = userBean.getTemplate(applicationName);
				if (templateName != null && !templateName.equals(""))
					return templateName;
				else
					return "default";
			}
		}
		else
		{
			//recupero il name del template dal file di properties
			if (ManagerStarter.configuration != null)
			{
				String templateName = ManagerStarter.configuration.getProperty(PropertiesPath.templateBase.format() + request.getServerName());

				if (templateName != null && !templateName.equals(""))
					return templateName;
			}
		}
		return "default";
	}	
	//fine LP PG200060

	//inizio LP PG200360
	private boolean causaliOn(HttpServletRequest request) {
		boolean bOk = false;
		PropertiesTree configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		if (configuration != null){
			String appo = configuration.getProperty("serviziattivi.gestionecasuali." + dbSchemaCodSocieta);
			bOk = (appo != null ? !appo.trim().equalsIgnoreCase("N") : false);
		}
		return bOk;
	}
	//fine LP PG200360
}