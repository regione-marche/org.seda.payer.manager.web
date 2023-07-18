/**
 * 
 */
package org.seda.payer.manager.configurazione.actions;


import java.rmi.RemoteException;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.WebRowSet;

import org.apache.commons.lang.StringEscapeUtils;
import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.core.bean.ConfigurazioneBlackBox;
import com.seda.payer.core.bean.ConfigurazioneEasyBridge;
import com.seda.payer.core.dao.BlackBoxDAOFactory;
import com.seda.payer.core.dao.ConfigurazioneBlackBoxDao;
import com.seda.payer.core.dao.ConfigurazioneEasyBridgeDao;
import com.seda.payer.core.dao.EasyBridgeDAOFactory;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteSearchRequest;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteSearchResponse;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoSearchResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaProvinceResponse;
import com.seda.payer.pgec.webservice.configgruppo.dati.ConfigGruppoListaRequest;
import com.seda.payer.pgec.webservice.configgruppo.dati.ConfigGruppoListaResponse;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest3;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.ente.dati.Ente;
import com.seda.payer.pgec.webservice.ente.dati.EnteCancelRequest;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailRequest;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailResponse;
import com.seda.payer.pgec.webservice.ente.dati.EnteResponse;
import com.seda.payer.pgec.webservice.ente.dati.EnteResponsePageInfo;
import com.seda.payer.pgec.webservice.ente.dati.EnteSaveRequest;
import com.seda.payer.pgec.webservice.ente.dati.EnteSearchForAddRequest;
import com.seda.payer.pgec.webservice.ente.dati.EnteSearchRequest;
import com.seda.payer.pgec.webservice.ente.dati.EnteSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

import groovyjarjarantlr4.v4.parse.ANTLRParser.throwsSpec_return;

import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;


public class EnteAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String strCodiceSocieta,strCodiceUtente,usernameAutenticazione;

	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
		   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		   pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		   order = (request.getParameter("order") == null) ? "" : request.getParameter("order");

		   HttpSession session = request.getSession();
		   UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		   replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		   if( user!=null) 
			   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
		   usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		   /*request.setAttribute("userAppl_codiceSocieta",userAppl.getCodiceSocieta().trim());
		   request.setAttribute("userAppl_codiceUtente",userAppl.getCodiceUtente().trim());
		   //request.setAttribute("userAppl_chiaveEnteCon",userAppl.getChiaveEnteCon().trim());
		   //request.setAttribute("userAppl_chiaveEnteEnt",userAppl.getChiaveEnteEnt().trim());
		}
		usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		strCodiceSocieta = (String)request.getAttribute("userAppl_codiceSocieta");
		strCodiceUtente = (String)request.getAttribute("userAppl_codiceUtente");*/
		   String firedButton = (String)request.getAttribute("tx_button_reset");
			//System.out.println(firedButton+"index");
			if (firedButton!=null){
				if(firedButton.equals("Reset")){
					try {
						add(request);
					} catch (ActionException e) {
						e.printStackTrace();
					}
				}
			}
		   firedButton = (String)request.getAttribute("tx_button_nuovo");
			if (firedButton!=null){
				if(firedButton.equals("Nuovo")){
					try {
						add(request);
					} catch (ActionException e) {
						e.printStackTrace();
					}
				}
			}
			firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					try {
						index(request);
					} catch (ActionException e) {
						e.printStackTrace();
					}
				}
			}
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		search(request);
		return null;
	}

	public Object search(HttpServletRequest request) throws ActionException {
		try {
			String companyCode = (strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:(String)request.getAttribute("ente_companyCode");
			String userCode = (strCodiceUtente!=null && strCodiceUtente.length()>0)?strCodiceUtente:(String)request.getAttribute("ente_userCode");
			String tipoEnte = (String)request.getAttribute("ente_tipoEnte");
			String strEnte = (String)request.getAttribute("ente_strEnte");
			String strDescrSocieta= (String)request.getAttribute("ente_companyCode");
			String strDescrUtente= (String)request.getAttribute("ente_strDescrUtente");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			
			if (firedButtonReset!=null){				
				resetParametri(request);
				companyCode="";
				userCode="";
				tipoEnte="";
				strEnte="";
				strDescrSocieta="";
				strDescrUtente="";
				
			}
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			if (firedButton!=null||firedButtonN!=null){
					userCode = request.getParameter("");
					tipoEnte = request.getParameter("");
					companyCode = request.getParameter("");
					strEnte = request.getParameter("");
					strDescrSocieta = request.getParameter("");
					strDescrUtente = request.getParameter("");
					
			}
//inizio LP PG200060
			//Fabrizio 04/05/2015
			HttpSession session = request.getSession();
			if ("Indietro".equals(firedButton)){
					//se ha premuto indietro rileggo i parametri di ricerca dalla sessione
					tipoEnte = (String)session.getAttribute("ente_tipoEnte");
					strEnte = (String)session.getAttribute("ente_strEnte");
					strDescrSocieta= (String)session.getAttribute("ente_companyCode");
					strDescrUtente= (String)session.getAttribute("ente_strDescrUtente");
					firedButton = null;
			} else {
				//metto sempre i parametri di ricerca in sessione
				session.setAttribute("ente_tipoEnte", (String)request.getAttribute("ente_tipoEnte"));
				session.setAttribute("ente_strEnte", (String)request.getAttribute("ente_strEnte"));
				session.setAttribute("ente_companyCode", (String)request.getAttribute("ente_companyCode"));
				session.setAttribute("ente_strDescrUtente", (String)request.getAttribute("ente_strDescrUtente"));
			
			}
			//fine Fabrizio 04/05/2015
//fine LP PG200060
			EnteSearchResponse searchResponse = null;
			if (firedButton!=null)
				 if(firedButton.equals("Indietro"))
					 searchResponse = getEnteSearchResponse(null,null, "", null,null,null,null, rowsPerPage, pageNumber, order, request);
				 if (firedButton==null)  searchResponse = getEnteSearchResponse("",userCode, "", tipoEnte,strEnte,strDescrSocieta,strDescrUtente, rowsPerPage, pageNumber, order, request);
			//EnteSearchResponse searchResponse = getEnteSearchResponse(companyCode,userCode, "", tipoEnte,strEnte,strDescrSocieta,strDescrUtente, rowsPerPage, pageNumber, order);
			EnteResponse enteServizioResponse = searchResponse.getResponse();
			EnteResponsePageInfo responsePageInfo = enteServizioResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("ente", searchRequest);
			request.setAttribute("ente_companyCode",companyCode);
			request.setAttribute("ente_userCode", userCode);
			request.setAttribute("ente_tipoEnte", tipoEnte);
			//request.setAttribute("ente_chiaveEnte", .getChiaveEnte());
			request.setAttribute("ente_strEnte", strEnte);
			request.setAttribute("ente_strDescrSocieta",strDescrSocieta);
			request.setAttribute("ente_strDescrUtente",strDescrUtente);  
			request.setAttribute("entes", enteServizioResponse.getListXml());
			request.setAttribute("entes.pageInfo", pageInfo);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			
			loadSocietaUtenti(request);			
			loadListaProvince(request);
			//inizio LP PG210040 
			loadDdlChiave(request);
			//fine LP PG210040 
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}

	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		request.setAttribute("companyCode",request.getParameter("companyCode"));
        request.setAttribute("userCode",request.getParameter("userCode"));
        request.setAttribute("chiaveEnte",request.getParameter("chiaveEnte"));
		return null; 
	}	

	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			//inizio LP PG210040 
			loadDdlChiave(request);
			//fine LP PG210040 

			String companyCode = (strCodiceSocieta != null && strCodiceSocieta.length() > 0) ? strCodiceSocieta : "";
			String userCode = (strCodiceUtente != null && strCodiceUtente.length() > 0) ? strCodiceUtente : "";
			
			EnteSearchResponse searchResponse = getEntesForAddSearchResponse(companyCode, userCode, "", request);
			request.setAttribute("entes_add", searchResponse.getResponse().getListXml());
			
			EnteDetailResponse response = getEnteDetailSearchResponse(request.getParameter("ente_companyCode"),request.getParameter("ente_userCode"), request.getParameter("ente_chiaveEnte"), request);
			request.setAttribute("ente_companyCode", response.getEnte().getCompanyCode());
			request.setAttribute("ente_userCode", response.getEnte().getUserCode());
			request.setAttribute("ente_tipoEnte", response.getEnte().getTipoEnte());
			request.setAttribute("ente_chiaveEnte", response.getEnte().getChiaveEnte());
			request.setAttribute("ente_emailAdmin", response.getEnte().getEmailAdmin());
			request.setAttribute("ente_cFiscalePIva", response.getEnte().getCFiscalePIva());
			request.setAttribute("ente_codiceSia", response.getEnte().getCodSia());
			request.setAttribute("ente_flagAutInvSMS", response.getEnte().getFlagAutInvSMS());
			request.setAttribute("ente_flagBeneficiario", response.getEnte().getFlagBeneficiario());
			request.setAttribute("ente_dirFlussiEccInput", response.getEnte().getDirFlussiEccInput());
			request.setAttribute("ente_dirSaveFlussiEccInput", response.getEnte().getDirSaveFlussiEccInput());
			request.setAttribute("ente_dirFlussiRimbOutput", response.getEnte().getDirFlussiRimbOutput());
			request.setAttribute("ente_dirSaveFlussiRimbOutput", response.getEnte().getDirSaveFlussiRimbOutput());
			request.setAttribute("ente_siteFtpFlussiRimbEcc", response.getEnte().getSiteFtpFlussiRimbEcc());
			request.setAttribute("ente_userSiteFtpFlussiRimbEcc", response.getEnte().getUserSiteFtpFlussiRimbEcc());
			request.setAttribute("ente_pswSiteFtpFlussiRimbEcc", response.getEnte().getPswSiteFtpFlussiRimbEcc());
			request.setAttribute("ente_dirFlussiCbiInput", response.getEnte().getDirFlussiCbiInput());
			request.setAttribute("ente_dirSaveFlussiCbiInput", response.getEnte().getDirSaveFlussiCbiInput());
			request.setAttribute("ente_dirFlussiRimbEccInput", response.getEnte().getDirFlussiRimbEccInput());
			request.setAttribute("ente_dirSaveFlussiRimbEccInput", response.getEnte().getDirSaveFlussiRimbEccInput());
			request.setAttribute("ente_userSiteFtpFlussiCbi", response.getEnte().getUserSiteFtpFlussiCbi());
			request.setAttribute("ente_pswSiteFtpFlussiCbi", response.getEnte().getPswSiteFtpFlussiCbi());
			request.setAttribute("ente_siteFtpFlussiCbi", response.getEnte().getSiteFtpFlussiCbi());
			request.setAttribute("ente_flagModello", response.getEnte().getFlagCreateMod21());
			request.setAttribute("ente_emailModello", response.getEnte().getEmailInvioMod21());
			request.setAttribute("ente_flagRuoli", response.getEnte().getFlagRuoli());
			request.setAttribute("ente_directoryFtpFlussiRimbEcc", response.getEnte().getDirFtpFlussiRimbEcc());
			request.setAttribute("ente_directoryFtpFlussiCbi", response.getEnte().getDirFtpFlussiCbi());
			request.setAttribute("ente_codIpaEnte", response.getEnte().getCodIpaEnte());
			//inizio LP PG200060
			String template = getTemplateCurrentApplication(request, request.getSession());
			if(!template.equalsIgnoreCase("regmarche")) {
			//fine LP PG200060
			//inizio PG180010
			request.setAttribute("ente_flagGDC", response.getEnte().getFlagGDC());
			request.setAttribute("ente_dirGDCInput", response.getEnte().getDirGDCInput());
			request.setAttribute("ente_dirGDCSave", response.getEnte().getDirGDCSave());
			request.setAttribute("ente_endpointUniit", response.getEnte().getEndpointUniit());
			//fine PG180010
			//inizio LP PG200060
			}
			//fine LP PG200060
			//inizio LP PG180290
			if(template.equalsIgnoreCase("trentrisc")) {
				request.setAttribute("ente_codIpaEnte", response.getEnte().getCodIpaEnte());
				request.setAttribute("ente_passwordEnte", response.getEnte().getPasswordEnte());
				request.setAttribute("ente_passwordSha256Ente", response.getEnte().getPasswordSha256Ente());
			}
			/*
			request.setAttribute("ente_numeroContoCorrente", response.getEnte().getNumeroContoCorrente());
			request.setAttribute("ente_intestatarioContoCorrente", response.getEnte().getIntestatarioContoCorrente());
			request.setAttribute("ente_flagControlloRangeAbi", response.getEnte().getFlagControlloRangeAbi());
			request.setAttribute("ente_emailDestinatario", response.getEnte().getEmailDestinatario());
			request.setAttribute("ente_emailCcn", response.getEnte().getEmailCcn());
			request.setAttribute("ente_emailMittente", response.getEnte().getEmailMittente());
			request.setAttribute("ente_descrizioneMittente", response.getEnte().getDescrizioneMittente());
			request.setAttribute("ente_flagAllegato", response.getEnte().getFlagAllegato());
			request.setAttribute("ente_urlSistemaEsterno", response.getEnte().getUrlSistemaEsterno());
			
			request.setAttribute("ente_descrizioneEnte",request.getParameter("ente_descrizioneEnte"));*/
			
			
			AnagEnteSearchResponse anagenteSearchResponse = getAnagEnteSearchResponse(response.getEnte().getChiaveEnte(),"", request);
			String descrizioneEnte = getDescrizioneEnte(anagenteSearchResponse.getResponse().getListXml());
			request.setAttribute("ente_descrizioneEnte", descrizioneEnte);

			//inizio LP 20180628
			request.setAttribute("ente_flagFlussoRendicontazioneNodo", response.getEnte().getFlagFlussoRendicontazioneNodo());
			request.setAttribute("ente_listaEmailFlussoRendicontazioneNodo", response.getEnte().getListaEmailFlussoRendicontazioneNodo());
			request.setAttribute("ente_urlWebServiceFlussoRendicontazioneNodo", response.getEnte().getUrlWebServiceFlussoRendicontazioneNodo());
			//fine LP 20180628
			//inizio LP PG210040
			if(template.equalsIgnoreCase("aosta")) {
				request.setAttribute("ente_codiceGruppo", response.getEnte().getCodiceGruppo());
			}
			//fine LP PG210040
			//add(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
 
	public Object saveadd(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.INS_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			FiredButton firedButtonChanged = getFiredButton(request);
			
			if(firedButtonChanged.equals(FiredButton.TX_BUTTON_CHANGED))
			{
				String siglaProvincia = request.getParameter("ente_siglaprovincia");
				//carico la lista degli enti filtrata per provincia
				AnagEnteSearchResponse anagenteSearchResponse = getAnagEnteSearchResponse("", siglaProvincia, request);
				request.setAttribute("entes_add2", anagenteSearchResponse.getResponse().getListXml());
				
				//ricarico la lista società/utenti e la lista province
				loadSocietaUtenti(request);			
				loadListaProvince(request);
				//inizio LP PG210040 
				loadDdlChiave(request);
				//fine LP PG210040 
			}
			else if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("ente_userCode") !=null){
						request.setAttribute("ente_userCode", null);
						request.setAttribute("ente_tipoEnte", null);
						request.setAttribute("ente_companyCode", null);
						request.setAttribute("ente_strEnte", null);
						request.setAttribute("ente_strDescrSocieta", null);
						request.setAttribute("ente_strDescrUtente", null);
					//	request.setAttribute("ente_codIpaEnte", null);  //cd pago 580 
					
						index(request);
					}
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset"))					
						resetParametri(request);
			}else{
				save(request);
				//inizio LP PG200060
				//TODO: su RM non viene eseguito inserisciEasyBridge ?
				String template = getTemplateCurrentApplication(request, request.getSession());
//				if(!template.equalsIgnoreCase("regmarche")) {
				//fine LP PG200060
				EsitoRisposte esito = new EsitoRisposte();
				esito = inserisciEasyBridge(request);
				//inizio LP PG200060
//				}
				//fine LP PG200060
			}
				
				
				
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}

	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.UPDT_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("ente_userCode") !=null){
						request.setAttribute("ente_userCode", null);
						request.setAttribute("ente_tipoEnte", null);
						request.setAttribute("ente_companyCode", null);
						request.setAttribute("ente_strEnte", null);
						request.setAttribute("ente_strDescrSocieta", null);
						request.setAttribute("ente_strDescrUtente", null);
					//	request.setAttribute("ente_codIpaEnte", null);  //cd pago 580 
						index(request);
					}
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){
					request.setAttribute("action", "edit");
					edit(request);					
				}
			}
			else 
			{				
				save(request);
				
				//inizio LP PG200060
				//TODO: su RM non viene eseguito inserisciEasyBridge ?
				String template = getTemplateCurrentApplication(request, request.getSession());
//				if(!template.equalsIgnoreCase("regmarche")) {
				//fine LP PG200060
				// salvo
				EsitoRisposte esito = new EsitoRisposte();
				esito = inserisciEasyBridge(request);
				//inizio LP PG200060
//				}
				//fine LP PG200060
				
				
			}
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}

	private void save(HttpServletRequest request) throws ActionException {
		org.seda.payer.manager.util.EnteViewState viewState = new org.seda.payer.manager.util.EnteViewState(request, "ente");
		String scope = request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		System.out.println();
		String sco = request.getParameter("scope");
		request.setAttribute("done", scope); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		/* we prepare data by scope */
		viewState.prepareData(sco);
		System.out.println(viewState.getFlagGDC());
		//EnteImplementationStub stub = null;
		try {
			//stub = new EnteImplementationStub(new URL(getEnteManagerEndpointUrl()), null);
			Ente ente = new Ente(viewState.getCompanyCode(), viewState.getUserCode(), viewState.getChiaveEnte(), 
								 viewState.getTipoEnte(), 
								 viewState.getEmailAdmin(),
								 viewState.getCFiscalePIva(),
								 viewState.getCodSia(),
								 viewState.getFlagAutInvSMS(),
								 //dom
								 viewState.getFlagBeneficiario(),
								 // fine dom
								 viewState.getDirFlussiEccInput(),
								 viewState.getDirSaveFlussiEccInput(),
								 viewState.getDirFlussiRimbOutput(),
								 viewState.getDirSaveFlussiRimbOutput(),
								 viewState.getSiteFtpFlussiRimbEcc(),
								 viewState.getUserSiteFtpFlussiRimbEcc(),
								 viewState.getPswSiteFtpFlussiRimbEcc(),
								 viewState.getDirFlussiCbiInput(),
								 viewState.getDirSaveFlussiCbiInput(),
								 viewState.getDirFlussiRimbEccInput(),
								 viewState.getDirSaveFlussiRimbEccInput(),
								 viewState.getUserSiteFtpFlussiCbi(),
								 viewState.getPswSiteFtpFlussiCbi(),
								 viewState.getSiteFtpFlussiCbi(),
								 viewState.getFlagModello(),
								 viewState.getEmailModello(),
								 viewState.getFlagRuoli(),
								 viewState.getDirectoryFtpFlussiRimbEcc(),
								 viewState.getDirectoryFtpFlussiCbi(),
								 usernameAutenticazione,
								 //inizio LP 20180628
								 (viewState.getFlagFlussoRendicontazioneNodo() != null ? viewState.getFlagFlussoRendicontazioneNodo() : ""),
								 (viewState.getListaEmailFlussoRendicontazioneNodo() != null ? viewState.getListaEmailFlussoRendicontazioneNodo() : ""),
								 (viewState.getUrlWebServiceFlussoRendicontazioneNodo() != null ?viewState.getUrlWebServiceFlussoRendicontazioneNodo() : ""),
								 //fine LP 20180628
								 //inizio PG180010
								 (viewState.getFlagGDC() != null ? viewState.getFlagGDC() : ""),
								 (viewState.getDirGDCInput() != null ?viewState.getDirGDCInput() : ""),
								 (viewState.getDirGDCSave() != null ?viewState.getDirGDCSave() : ""),
								 (viewState.getEndpointUniit() != null ?viewState.getEndpointUniit() : "")
								 //fine PG180010
						        //inizio LP PG180290
								 , viewState.getCodIpaEnte()
								 , viewState.getPasswordEnte()
								 , viewState.getPasswordSha256Ente()
						        //fine LP PG180290
								//inizio LP PG210040
								 , viewState.getCodiceGruppo()
								//fine LP PG210040
						          );
			
			/*viewState.getNumeroContoCorrente(), 
								 viewState.getIntestatarioContoCorrente(), viewState.getFlagControlloRangeAbi(), 
								 viewState.getEmailDestinatario(), viewState.getEmailCcn(), viewState.getEmailMittente(), 
								 viewState.getDescrizioneMittente(), viewState.getFlagAllegato(), viewState.getUrlSistemaEsterno(), 
								 usernameAutenticazione);*/
			/* we prepare object for save */
			EnteSaveRequest saveRequest = new EnteSaveRequest(ente, scope);
			/* we save object */
			saveEnte(saveRequest, request);

		} catch (FaultType e) {
			try {
				EnteDetailRequest detailRequest = new EnteDetailRequest(viewState.getCompanyCode(), viewState.getUserCode(), viewState.getChiaveEnte());
				EnteDetailResponse detailResponse = getEnte(detailRequest, request);
				//request.setAttribute("ente", detailResponse.getEnte());
				request.setAttribute("ente_companyCode", detailResponse.getEnte().getCompanyCode());
				request.setAttribute("ente_userCode", detailResponse.getEnte().getUserCode());
				request.setAttribute("ente_tipoEnte", detailResponse.getEnte().getTipoEnte());
				request.setAttribute("ente_chiaveEnte", detailResponse.getEnte().getChiaveEnte());			
				request.setAttribute("ente_emailAdmin", detailResponse.getEnte().getEmailAdmin());
				request.setAttribute("ente_cFiscalePIva", detailResponse.getEnte().getCFiscalePIva());
				request.setAttribute("ente_codSia", detailResponse.getEnte().getCodSia());
				request.setAttribute("ente_flagAutInvSMS", detailResponse.getEnte().getFlagAutInvSMS());
//dom
				request.setAttribute("ente_flagBeneficiario", detailResponse.getEnte().getFlagBeneficiario());
//fine dom
				request.setAttribute("ente_dirFlussiEccInput", detailResponse.getEnte().getDirFlussiEccInput());
				request.setAttribute("ente_dirSaveFlussiEccInput", detailResponse.getEnte().getDirSaveFlussiEccInput());
				request.setAttribute("ente_dirFlussiRimbOutput", detailResponse.getEnte().getDirFlussiRimbOutput()); 
				request.setAttribute("ente_dirSaveFlussiRimbOutput", detailResponse.getEnte().getDirSaveFlussiRimbOutput());
				request.setAttribute("ente_siteFtpFlussiRimbEcc", detailResponse.getEnte().getSiteFtpFlussiRimbEcc());
				request.setAttribute("ente_userSiteFtpFlussiRimbEcc", detailResponse.getEnte().getUserSiteFtpFlussiRimbEcc());
				request.setAttribute("ente_pswSiteFtpFlussiRimbEcc", detailResponse.getEnte().getPswSiteFtpFlussiRimbEcc());
				request.setAttribute("ente_dirFlussiCbiInput", detailResponse.getEnte().getDirFlussiCbiInput());
				request.setAttribute("ente_dirSaveFlussiCbiInput", detailResponse.getEnte().getDirFlussiCbiInput());
				request.setAttribute("ente_dirFlussiRimbEccInput", detailResponse.getEnte().getDirFlussiRimbEccInput());
				request.setAttribute("ente_dirSaveFlussiRimbEccInput", detailResponse.getEnte().getDirFlussiRimbEccInput());
				request.setAttribute("ente_userSiteFtpFlussiCbi", detailResponse.getEnte().getUserSiteFtpFlussiCbi());
				request.setAttribute("ente_pswSiteFtpFlussiCbi", detailResponse.getEnte().getPswSiteFtpFlussiCbi());
				request.setAttribute("ente_siteFtpFlussiCbi", detailResponse.getEnte().getSiteFtpFlussiCbi());
				request.setAttribute("ente_flagModello", detailResponse.getEnte().getFlagCreateMod21());
				request.setAttribute("ente_emailModello", detailResponse.getEnte().getEmailInvioMod21());
				request.setAttribute("ente_flagRuoli", detailResponse.getEnte().getFlagRuoli());
				request.setAttribute("ente_directoryFtpFlussiRimbEcc", detailResponse.getEnte().getDirFtpFlussiRimbEcc());
				request.setAttribute("ente_directoryFtpFlussiCbi", detailResponse.getEnte().getDirFtpFlussiCbi());
				//inizio LP 20180628
				request.setAttribute("ente_flagFlussoRendicontazioneNodo", detailResponse.getEnte().getFlagFlussoRendicontazioneNodo());
				request.setAttribute("ente_listaEmailFlussoRendicontazioneNodo", detailResponse.getEnte().getListaEmailFlussoRendicontazioneNodo());
				request.setAttribute("ente_urlWebServiceFlussoRendicontazioneNodo", detailResponse.getEnte().getUrlWebServiceFlussoRendicontazioneNodo());
				//fine LP 20180628
				//inizio PG180010
				request.setAttribute("ente_flagGDC", detailResponse.getEnte().getFlagGDC());
				request.setAttribute("ente_dirGDCInput", detailResponse.getEnte().getDirGDCInput());
				request.setAttribute("ente_dirGDCSave", detailResponse.getEnte().getDirGDCSave());
				request.setAttribute("ente_endpointUniit", detailResponse.getEnte().getEndpointUniit());
				//fine PG180010
				//inizio LP PG180290
				request.setAttribute("ente_codIpaEnte", detailResponse.getEnte().getCodIpaEnte());
				request.setAttribute("ente_passwordEnte", detailResponse.getEnte().getPasswordEnte());
				request.setAttribute("ente_passwordSha256Ente", detailResponse.getEnte().getPasswordSha256Ente());
				//fine LP PG180290
				//inizio LP PG210040
				request.setAttribute("ente_codiceGruppo", detailResponse.getEnte().getCodiceGruppo());
				//fine LP PG210040
			} catch (Exception ignore) { }
			if (scope.compareTo(TypeRequest.ADD_SCOPE.scope()) == 0) request.setAttribute("message", Messages.INS_ERRD.format(e.getMessage1()));
			if (scope.compareTo(TypeRequest.EDIT_SCOPE.scope()) == 0) request.setAttribute("message", Messages.CANCEL_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage1());
		} catch (Exception e) {
			request.setAttribute("message", Messages.INS_ERRD.format());
			System.out.println(e.getMessage());
		}
	}
	
	
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String companyCode = request.getParameter("ente_companyCode");
		String userCode = request.getParameter("ente_userCode");
		String chiaveEnte = request.getParameter("ente_chiaveEnte");
	    //EnteImplementationStub stub = null;
		try {
			request.setAttribute("varname", "ente");
			//stub = new EnteImplementationStub(new URL(getEnteManagerEndpointUrl()), null);
			/* we prepare object for cancel */
			EnteCancelRequest cancelRequest = new EnteCancelRequest(companyCode,userCode,chiaveEnte);
			/* we cancel object */
			cancelEnte(cancelRequest, request);
			request.setAttribute("message", Messages.CANC_OK.format());
		} catch (FaultType f) {
			request.setAttribute("message", Messages.CANCEL_ERRDIP.format());
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
	
	private EnteSearchResponse getEnteSearchResponse(
			String companyCode, String userCode, String chiaveEnte,String tipoEnte, String strEnte, String strDescrSocieta,String strDescrUtente,int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		EnteSearchResponse res = null;
		EnteSearchRequest in = new EnteSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setUserCode(userCode == null ? "" : userCode);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setTipoEnte(tipoEnte == null ? "" : tipoEnte);
		in.setStrEnte(strEnte == null ? "" : strEnte);
		in.setStrDescrSocieta(strDescrSocieta == null ? "" : strDescrSocieta);
		in.setStrDescrUtente(strDescrUtente == null ? "" : strDescrUtente);
		res = WSCache.enteServer.getEntes(in, request);
		return res;
	}
	
	private EnteSearchResponse getEntesForAddSearchResponse(
			String companyCode, String userCode, String chiaveEnte, HttpServletRequest request) throws FaultType, RemoteException
	{
		EnteSearchResponse res = null;
		EnteSearchForAddRequest in = new EnteSearchForAddRequest();

		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setUserCode(userCode == null ? "" : userCode);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		
		
		res = WSCache.enteServer.getEntesForAdd(in, request);
		return res;
	}
	
	private EnteDetailResponse getEnteDetailSearchResponse(String companyCode, String userCode, String chiaveEnte, HttpServletRequest request) throws FaultType, RemoteException
	{
		 
		EnteDetailResponse res = null;
		EnteDetailRequest in = new EnteDetailRequest(companyCode, userCode, chiaveEnte);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setUserCode(userCode == null ? "" : userCode);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		
		res = WSCache.enteServer.getEnte(in, request);
		return res;
	}
	
	private void  saveEnte(EnteSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.enteServer.save(saveRequest, request);
		
	}

	private void  cancelEnte(EnteCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.enteServer.cancel(cancelRequest, request);
		
	}
	

	private EnteDetailResponse getEnte (EnteDetailRequest detailRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		return WSCache.enteServer.getEnte(detailRequest, request);
	}
	
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
	
	private AnagEnteSearchResponse getAnagEnteSearchResponse(
			String chiaveEnte, String siglaProvincia, HttpServletRequest request) throws FaultType, RemoteException
	{
		AnagEnteSearchResponse res = null;
		AnagEnteSearchRequest in = new AnagEnteSearchRequest();
		
		in.setChiaveEnte(chiaveEnte);
		in.setSiglaProvincia(siglaProvincia);
		
		in.setRowsPerPage(0);
		in.setPageNumber(0);
		in.setOrder("");
		in.setCodiceBelfiore("");
		in.setCodiceEnte("");
		in.setDescrizioneEnte("");

		res = WSCache.anagEnteServer.getAnagEnti(in, request);
		return res;
	}
	
	private void loadSocietaUtenti(HttpServletRequest request) throws FaultType, RemoteException
	{
		String companyCode = (strCodiceSocieta != null && strCodiceSocieta.length() > 0) ? strCodiceSocieta : "";
		String userCode = (strCodiceUtente != null && strCodiceUtente.length() > 0) ? strCodiceUtente : "";

		EnteSearchResponse searchResponse = getEntesForAddSearchResponse(companyCode, userCode, "", request);
		request.setAttribute("entes_add", searchResponse.getResponse().getListXml());
	}
	
	private void loadListaProvince(HttpServletRequest request) throws FaultType, RemoteException
	{
		RecuperaProvinceResponse getProvinceRes = WSCache.commonsServer.recuperaProvince(request);
		request.setAttribute("listprovince", getProvinceRes.getListXml());
	}
	
	private String getDescrizioneEnte(String listXml)
	{
		//inizio LP PG21XX04 Leak
		WebRowSet webRowSetEnte = null;
		//fine LP PG21XX04 Leak
		try
		{
			//inizio LP PG21XX04 Leak
			//CachedRowSet webRowSetEnte = Convert.stringToWebRowSet(listXml);
			webRowSetEnte = Convert.stringToWebRowSet(listXml);
			//fine LP PG21XX04 Leak
			if (webRowSetEnte != null) 
			{
				if (webRowSetEnte.next())
					return webRowSetEnte.getString(6);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
	    	try {
	    		if(webRowSetEnte != null) {
	    			webRowSetEnte.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return "";
	}
	
	private EsitoRisposte inserisciEasyBridge(HttpServletRequest request) throws DaoException 
	{
		
		HttpSession session = request.getSession(); 
		
		String codiceIdentificativoDominio = (request.getAttribute("ente_cFiscalePIva") == null ? "" : (String)request.getAttribute("ente_cFiscalePIva"));
		String cutecute = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		String operatore = "manager_" + cutecute;
		
		
		
		ConfigurazioneEasyBridgeDao configurazioneEasyBridgeDAO;
		ConfigurazioneEasyBridge easybridge = new ConfigurazioneEasyBridge();
		easybridge.setCodiceIdentificativoDominio(codiceIdentificativoDominio);
		easybridge.setCutecute(cutecute);
		easybridge.setOperatore(operatore);
		
		
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String easyBridgeDataSourceString =  configuration.getProperty(PropertiesPath.dataSourceEasyBridge.format(cutecute));
		String easyBridgeDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaEasyBridge.format(cutecute));
		
		DataSource easyBridgeDataSource = null;
		
		try {	
			easyBridgeDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(easyBridgeDataSourceString));
			
		} catch (ServiceLocatorException e) {
			try {
				throw new ActionException("ServiceLocator error " + e.getMessage(),e);
			} catch (ActionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		
		//inizio LP PG21XX04 Leak
		//configurazioneEasyBridgeDAO = EasyBridgeDAOFactory.getEasyBridge(easyBridgeDataSource, easyBridgeDbSchema);
		//EsitoRisposte esitorisposte = new EsitoRisposte();
		//return configurazioneEasyBridgeDAO.insert(easybridge);
		Connection conn = null;
		try {
			conn = easyBridgeDataSource.getConnection();
			configurazioneEasyBridgeDAO = EasyBridgeDAOFactory.getEasyBridge(conn, easyBridgeDbSchema);
			return configurazioneEasyBridgeDAO.insert(easybridge);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new DaoException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
	}
	//inizio LP PG200060
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
	//inizio LP PG100040
	private void loadDdlChiave(HttpServletRequest request) throws FaultType, RemoteException
	{
		String template = getTemplateCurrentApplication(request, request.getSession());
		if(template.equalsIgnoreCase("aosta")) {
			if(request.getAttribute("gruppi") == null) {
				ConfigGruppoListaResponse configGruppoListaResponse = getGruppoListaResponse(request);
				request.setAttribute("gruppi", configGruppoListaResponse.getListaGruppo().getListXml());
			}
		}
	}
	
	private ConfigGruppoListaResponse getGruppoListaResponse(HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigGruppoListaResponse res = null;
		ConfigGruppoListaRequest in = new ConfigGruppoListaRequest();
		in.setRowsPerPage(0);
		in.setPageNumber(0);
		in.setOrder("DITA"); 
		
		res = WSCache.configGruppoServer.lista(in, request);
		return res;
	}
	//fine LP PG100040
}