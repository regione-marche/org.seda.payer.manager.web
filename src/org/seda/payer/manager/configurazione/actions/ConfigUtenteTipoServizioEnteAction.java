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
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnte;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteCancelRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteDetailRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteDetailResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteResponsePageInfo;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSaveRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest3;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class ConfigUtenteTipoServizioEnteAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	@SuppressWarnings("unused")
	private String strCodiceSocieta,strCodiceUtente,strChiaveEnte,usernameAutenticazione;
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
		if( user!=null) {
			   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
			   request.setAttribute("userAppl_codiceSocieta",user.getCodiceSocieta().trim());
			   request.setAttribute("userAppl_codiceUtente",user.getCodiceUtente().trim());
			  // request.setAttribute("userAppl_chiaveEnteCon",user.getChiaveEnteCon().trim());
			   //request.setAttribute("userAppl_chiaveEnteEnt",user.getChiaveEnteEnt().trim());
			}
		    usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
			strCodiceSocieta = (String)request.getAttribute("userAppl_codiceSocieta");
			strCodiceUtente = (String)request.getAttribute("userAppl_codiceUtente");
			strChiaveEnte = (String)request.getAttribute("userAppl_chiaveEnte");
			String firedButton = (String)request.getAttribute("tx_button_nuovo");
			//System.out.println(firedButton+"index");
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
					//inizio LP PG200060	
					if(template.equalsIgnoreCase("regmarche")) {
						svuotaFiltriDiRicercaDaSession(session); //PG130180_001_LP_24012020
					}
					//fine LP PG200060	
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
		String strEnte = null;
		String strSocieta = null;
		String strUtente = null;
		String strTipologiaServizio = null;
		String codTipologiaServizio = null;
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession());
		
		if(template.equalsIgnoreCase("regmarche")) {
			strEnte = (String)request.getAttribute("configutentetiposervizioente_desEnte");
			strSocieta = (String)request.getAttribute("configutentetiposervizioente_desSoc");
			strUtente = (String)request.getAttribute("configutentetiposervizioente_desUte");
			strTipologiaServizio = (String)request.getAttribute("configutentetiposervizioente_desTp");
//			PG22XX09_YL5 INI
			codTipologiaServizio = (String)request.getAttribute("configutentetiposervizioente_desCod");
//			PG22XX09_YL5 FINE
		}
		//fine LP PG200060
		
		
		try {
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButtonReset!=null){				
				resetParametri(request);		
				//inizio LP PG200060
				if(template.equalsIgnoreCase("regmarche")) {
					strEnte = null;
					strSocieta = null;
					strUtente = null;
					strTipologiaServizio = null;
//					PG22XX09_YL5 INI
					codTipologiaServizio = null; 
//					PG22XX09_YL5 FINE
				}
				//fine LP PG200060
			}
			//inizio LP PG200060
			if(!template.equalsIgnoreCase("regmarche")) {
			//fine LP PG200060
			if (firedButton == null && firedButtonReset == null)
			{
				strEnte = (String) request.getAttribute("configutentetiposervizioente_desEnte");
				strSocieta = (String) request.getAttribute("configutentetiposervizioente_desSoc");
				strUtente = (String) request.getAttribute("configutentetiposervizioente_desUte");
				strTipologiaServizio = (String) request.getAttribute("configutentetiposervizioente_desTp");
				codTipologiaServizio = (String) request.getAttribute("configutentetiposervizioente_desCod");
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
					chiaveEnte = request.getParameter("");
					strEnte = request.getParameter("");
					strSocieta = request.getParameter("");
					strUtente = request.getParameter("");
					strTipologiaServizio = request.getParameter("");
					
				}
			}
*/			ConfigUtenteTipoServizioEnteSearchResponse searchResponse = null;
			//inizio LP PG200060
			if(template.equalsIgnoreCase("regmarche")) {
				if (firedButton!=null)
					 if(firedButton.equals("Indietro"))
						 searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta,null,null,null,strEnte,strSocieta,strUtente,strTipologiaServizio,rowsPerPage, pageNumber, order, request);
				if (firedButton==null)  searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta,"","",codTipologiaServizio,strEnte,strSocieta,strUtente,strTipologiaServizio,rowsPerPage, pageNumber, order, request);
				
			} else {
			//fine LP PG200060
			if (firedButton!=null)
				 if(firedButton.equals("Indietro"))
					 searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta,null,null,codTipologiaServizio,null,null,null,null,rowsPerPage, pageNumber, order, request);
				 if (firedButton==null)  searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta,"","",codTipologiaServizio,strEnte,strSocieta,strUtente,strTipologiaServizio,rowsPerPage, pageNumber, order, request);
			//inizio LP PG200060
			}
			//fine LP PG200060
			ConfigUtenteTipoServizioEnteResponse configutentetiposervizioenteServizioResponse = searchResponse.getResponse();
			ConfigUtenteTipoServizioEnteResponsePageInfo responsePageInfo = configutentetiposervizioenteServizioResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			
			//request.setAttribute("configutentetiposervizioente", searchRequest);
/*			request.setAttribute("configutentetiposervizioente_companyCode",codiceSocieta);
			request.setAttribute("configutentetiposervizioente_codiceUtente", codiceUtente);
			request.setAttribute("configutentetiposervizioente_chiaveEnte", chiaveEnte);
			request.setAttribute("configutentetiposervizioente_codiceTipologiaServizio", codiceTipologiaServizio);
*/			
			//inizio LP PG200060
			if(!template.equalsIgnoreCase("regmarche")) {
			//fine LP PG200060
			request.setAttribute("configutentetiposervizioente_desSoc", strSocieta);
			request.setAttribute("configutentetiposervizioente_desUte", strUtente);
			request.setAttribute("configutentetiposervizioente_desTp", strTipologiaServizio);
			request.setAttribute("configutentetiposervizioente_desEnte", strEnte);
			//inizio LP PG200060
//			PG22XX09_YL5 INI
			request.setAttribute("configutentetiposervizioente_desCod", codTipologiaServizio);
//			PG22XX09_YL5 FINE
			}
			//fine LP PG200060
			request.setAttribute("configutentetiposervizioentes", configutentetiposervizioenteServizioResponse.getListXml());
			request.setAttribute("configutentetiposervizioentes.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			/* we retry all entetiposervizios */
/*			String companyCode = (strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:"";
			String codiceUtente = (strCodiceUtente!=null && strCodiceUtente.length()>0)?strCodiceUtente:"";
			String chiaveEnte = (strChiaveEnte!=null && strChiaveEnte.length()>0)?strChiaveEnte:"";
*/			
			/*
			 * La DDL Società/Utente viene caricata tenendo conto della profilazione utente:
			 * "userCodiceSocieta" infatti, se l'utente è un "AMSO" ha il valore del codice Società
			 * associato all'utente
			 */
			ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse = getConfigUtenteTipoServizioEntes2(userCodiceSocieta,"","","", request);
			request.setAttribute("entetiposervizios", configUtenteTipoServizioEnteSearchResponse.getResponse().getListXml());
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
	public Object addDDL(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("fired_button_hidden", null);
			String arrStr = request.getParameter("configutentetiposervizioente_strEntetiposervizios");
			if (arrStr != null)
			{
				String[] strUtentetiposervizios = arrStr.split("\\|");
				/*
				 * Se non è stata selezionata alcuna terna Società/Utente/Ente NON VIENE
				 * caricata la DDL Tipo Servizio.
				 * 
				 * Se è stata selezionata una coppia Società/Utente VIENE caricata la DDL
				 * Tipo Servizio in base alla Società selezionata
				 */
				if (!arrStr.equals(""))
				{
					ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse2 = null;
					configUtenteTipoServizioEnteSearchResponse2 = getConfigUtenteTipoServizioEnteSearchResponse3((new ConfigUtenteTipoServizioEnteSearchRequest3(strUtentetiposervizios[0])), request);
					request.setAttribute("utentetiposervizios2", configUtenteTipoServizioEnteSearchResponse2.getResponse().getListXml());
				}
				/*
				 * La DDL Società/Utente/Ente viene caricata tenendo conto della profilazione utente:
				 * "userCodiceSocieta" infatti, se l'utente è un "AMSO" ha il valore del codice Società
				 * associato all'utente
				 */
				ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse = getConfigUtenteTipoServizioEntes2(userCodiceSocieta,"","","", request);
				request.setAttribute("entetiposervizios", configUtenteTipoServizioEnteSearchResponse.getResponse().getListXml());
			}
			/*
			 * VIENE caricata comunque la DDL del tipo bollettino
			 */
			String tipoBollettino = "";
			BollettinoSearchResponse bolSearchResponse = getBollettinoSearchResponse(tipoBollettino, "", 0, 0, "", request);
			request.setAttribute("bollettini", bolSearchResponse.getResponse().getListXml());
			//request.setAttribute("configutentetiposervizio_strUtentetiposervizios", arrStr);
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
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		request.setAttribute("companyCode",request.getParameter("companyCode"));
        request.setAttribute("codiceUtente",request.getParameter("codiceUtente"));
        request.setAttribute("chiaveEnte",request.getParameter("chiaveEnte"));
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
			
			//ConfigUtenteTipoServizioEnteImplementationStub stub = new ConfigUtenteTipoServizioEnteImplementationStub(new URL(getConfigutentetiposervizioenteManagerEndpointUrl()), null);
			ConfigUtenteTipoServizioEnteDetailResponse response = 
				getConfigUtenteTipoServizioEnteDetailResponse(request.getParameter("configutentetiposervizioente_companyCode"),request.getParameter("configutentetiposervizioente_codiceUtente"),request.getParameter("configutentetiposervizioente_chiaveEnte"),request.getParameter("configutentetiposervizioente_codiceTipologiaServizio"), request);
			//request.setAttribute("configutentetiposervizioente", response.getConfigutentetiposervizioente());
			request.setAttribute("configutentetiposervizioente_companyCode", response.getConfigutentetiposervizioente().getCompanyCode());
			request.setAttribute("configutentetiposervizioente_codiceUtente", response.getConfigutentetiposervizioente().getUserCode());
			request.setAttribute("configutentetiposervizioente_chiaveEnte", response.getConfigutentetiposervizioente().getChiaveEnte());
			request.setAttribute("configutentetiposervizioente_codiceTipologiaServizio", response.getConfigutentetiposervizioente().getCodiceTipologiaServizio());
			
			request.setAttribute("configutentetiposervizioente_numCc", response.getConfigutentetiposervizioente().getNumCc());
			request.setAttribute("configutentetiposervizioente_intCc", response.getConfigutentetiposervizioente().getIntestatarioCc());
			request.setAttribute("configutentetiposervizioente_tipoDoc", response.getConfigutentetiposervizioente().getTipoDoc());
			request.setAttribute("configutentetiposervizioente_flagConRange", response.getConfigutentetiposervizioente().getFlagConRange());
			request.setAttribute("configutentetiposervizioente_emailDest", response.getConfigutentetiposervizioente().getEmailDest());
			request.setAttribute("configutentetiposervizioente_emailCcn", response.getConfigutentetiposervizioente().getEmailCcNascosta());
			request.setAttribute("configutentetiposervizioente_emailTo", response.getConfigutentetiposervizioente().getEmailMitt());
			request.setAttribute("configutentetiposervizioente_emailDescTo", response.getConfigutentetiposervizioente().getDesMittente());
			request.setAttribute("configutentetiposervizioente_flagAll", response.getConfigutentetiposervizioente().getFlagAllegato());
			request.setAttribute("configutentetiposervizioente_codiceSia", response.getConfigutentetiposervizioente().getCodiceSia());
			request.setAttribute("configutentetiposervizioente_codiceIban", response.getConfigutentetiposervizioente().getCodiceIban());
			request.setAttribute("configutentetiposervizioente_secondoCodiceIban", response.getConfigutentetiposervizioente().getSecondoCodiceIban());//RP	//PG150180_001 GG
			request.setAttribute("configutentetiposervizioente_funzionePag", response.getConfigutentetiposervizioente().getFunzionePag());
			request.setAttribute("configutentetiposervizioente_tipoBol", response.getConfigutentetiposervizioente().getTipoBollettino());
			request.setAttribute("configutentetiposervizioente_flagPagProtetta", response.getConfigutentetiposervizioente().getFlagPagProtetta());
			request.setAttribute("configutentetiposervizioente_urlServWeb", response.getConfigutentetiposervizioente().getUrlServWeb());
			request.setAttribute("configutentetiposervizioente_flagTipoPag", response.getConfigutentetiposervizioente().getFlagTipoPag().trim());
			request.setAttribute("configutentetiposervizioente_flagIntegrazioneSeda", response.getConfigutentetiposervizioente().getFlagIntegrazioneSeda());
			request.setAttribute("configutentetiposervizioente_codiceUtenteSeda", response.getConfigutentetiposervizioente().getCodiceUtenteSeda().trim());
			request.setAttribute("configutentetiposervizioente_flagNotificaPagamento", response.getConfigutentetiposervizioente().getFlagNotificaPagamento());
			request.setAttribute("configutentetiposervizioente_urlServizioWebNotificaPagamento", response.getConfigutentetiposervizioente().getUrlServizioWebNotificaPagamento());
			request.setAttribute("chk_flagPagoPA", response.getConfigutentetiposervizioente().getFlagPagoPA().equals("Y"));	//TODO da verificare
			//inizio PG180290
			request.setAttribute("configutentetiposervizioente_tipoDovuto", response.getConfigutentetiposervizioente().getTipoDovuto());
			//fine PG180290
			
			request.setAttribute("configutentetiposervizioente_flagStampaAvvisoPagoPa", response.getConfigutentetiposervizioente().getFlagStampaAvvisoPagoPa());
			request.setAttribute("configutentetiposervizioente_giorniStampaAvvisoPagoPa", response.getConfigutentetiposervizioente().getGiorniStampaAvvisoPagoPa());
			
			request.setAttribute("configutentetiposervizioente_autorizzazioneStampaAvvisoPagoPa", response.getConfigutentetiposervizioente().getAutorizzazioneStampaAvvisoPagoPa());
		    request.setAttribute("configutentetiposervizioente_cbillStampaAvvisoPagoPa", response.getConfigutentetiposervizioente().getCbillStampaAvvisoPagoPa());
		    request.setAttribute("configutentetiposervizioente_infoEnteStampaAvvisoPagoPa", response.getConfigutentetiposervizioente().getInfoEnteStampaAvvisoPagoPa());
		    
		    //inizio SB PG210140
			request.setAttribute("configutentetiposervizioente_codiceContabilita", response.getConfigutentetiposervizioente().getCodiceContabilita());
			request.setAttribute("configutentetiposervizioente_capitolo", response.getConfigutentetiposervizioente().getCapitolo());
			request.setAttribute("configutentetiposervizioente_articolo", response.getConfigutentetiposervizioente().getArticolo());
			request.setAttribute("configutentetiposervizioente_annoCompetenza", response.getConfigutentetiposervizioente().getAnnoCompetenza());
			//fine SB PG210140
			request.setAttribute("configutentetiposervizioente_datapagamento", response.getConfigutentetiposervizioente().getDataDicituraPagamento());  //SB PG210170
			
			//inizio PG200360
				String keyTassonomia = "";
				if(response.getConfigutentetiposervizioente().getChiaveTassonomia() != null) {
					keyTassonomia = response.getConfigutentetiposervizioente().getChiaveTassonomia();
					if(keyTassonomia.equals("0")) {
						keyTassonomia = "";
					}
				}
				request.setAttribute("configutentetiposervizioente_chiaveTassonomia", keyTassonomia);
				tipoBoll = response.getConfigutentetiposervizioente().getTipoBollettino();
				bSPOM = (tipoBoll != null && tipoBoll.equalsIgnoreCase("SPOM"));
				String causali = response.getConfigutentetiposervizioente().getCausali();
				
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
				tipoBoll = (String) request.getAttribute("configutentetiposervizioente_tipoBol");
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
			 * VIENE caricata comunque la DDL del tipo bollettino
			 */
			//inizio PG200360
			//String tipoBollettino = "";
			//BollettinoSearchResponse bolSearchResponse = getBollettinoSearchResponse(tipoBollettino, "", 0, 0, "", request);
			//request.setAttribute("bollettini", bolSearchResponse.getResponse().getListXml());
			//fine PG200360
			
			//inizio PG200360
			//String keyTassonomia = "";
			//if(response.getConfigutentetiposervizioente().getChiaveTassonomia() != null) {
			//	keyTassonomia = response.getConfigutentetiposervizioente().getChiaveTassonomia();
			//	if(keyTassonomia.equals("0")) {
			//		keyTassonomia = "";
			//	}
			//}
			//request.setAttribute("configutentetiposervizioente_chiaveTassonomia", keyTassonomia);
			
			/*
			request.setAttribute("configutentetiposervizioente_emailFrom", response.getConfigutentetiposervizioente().getEmailFrom());
			request.setAttribute("configutentetiposervizioente_emailCcn", response.getConfigutentetiposervizioente().getEmailCcn());
			request.setAttribute("configutentetiposervizioente_emailDest", response.getConfigutentetiposervizioente().getEmailTo());
			request.setAttribute("configutentetiposervizioente_descrMittente", response.getConfigutentetiposervizioente().getDescrMittente());
			request.setAttribute("configutentetiposervizioente_attachFlag", response.getConfigutentetiposervizioente().getAttachFlag());
			
			request.setAttribute("descrizioneente", request.getParameter("configutentetiposervizioente_descrizioneEnte"));*/
			//add(request);
		} catch (Exception e) {
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
					if(request.getAttribute("configutentetiposervizioente_companyCode") !=null){
						request.setAttribute("configutentetiposervizioente_companyCode", null);
						request.setAttribute("configutentetiposervizioente_userCode", null);
						request.setAttribute("configutentetiposervizioente_codiceTipologiaServizio", null);
						request.setAttribute("configutentetiposervizioente_chiaveEnte", null);
						request.setAttribute("configutentetiposervizio_desSoc", null);
						request.setAttribute("configutentetiposervizio_desUte", null);
						request.setAttribute("configutentetiposervizio_desTp", null);
						index(request);
					}
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){					
					resetParametri(request);
				}
			}else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
			ignore.printStackTrace();
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
						if(request.getAttribute("configutentetiposervizioente_companyCode") !=null){
							request.setAttribute("configutentetiposervizioente_companyCode", null);
							request.setAttribute("configutentetiposervizioente_userCode", null);
							request.setAttribute("configutentetiposervizioente_codiceTipologiaServizio", null);
							request.setAttribute("configutentetiposervizioente_chiaveEnte", null);
							request.setAttribute("configutentetiposervizio_desSoc", null);
							request.setAttribute("configutentetiposervizio_desUte", null);
							request.setAttribute("configutentetiposervizio_desTp", null);
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
		String chiaveEnte = "";
		String codiceTipologiaServizio = "";
		String companyCode = "";
		String cod = request.getParameter("cod");
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		String arrStr = request.getParameter("configutentetiposervizioente_strEntetiposervizios");
		String arrStr2 = request.getParameter("configutentetiposervizio_codiceTipologiaServizio");
		//inizio LP PG200360
		//NOTA: il valore del parametro "codop" è gestito male sulle jsp uso 
		//      come discriminante tra operazione di "add" o "edit" il parametro "cod"
		//fine LP PG200360
		if (cod.equals("0")  && arrStr!=null && arrStr.length()>0)
		{
			  String[] strEntetiposervizios = arrStr.split("\\|");
			  companyCode = strEntetiposervizios[0];
 		      codiceUtente= strEntetiposervizios[1];
 		      chiaveEnte= strEntetiposervizios[2];
 		      strEntetiposervizios = arrStr2.split("\\|");
		      codiceTipologiaServizio= strEntetiposervizios[0];
			  //inizio LP PG200360
 		      codOp = TypeRequest.ADD_SCOPE.scope();  
			  //fine LP PG200360
 		}
		if (cod.equals("1"))
		{
			  companyCode = request.getParameter("configutentetiposervizioente_companyCode");
              codiceUtente= request.getParameter("configutentetiposervizioente_codiceUtente");
              chiaveEnte= request.getParameter("configutentetiposervizioente_chiaveEnte");
              codiceTipologiaServizio= request.getParameter("configutentetiposervizioente_codiceTipologiaServizio");
  			  //inizio LP PG200360
   		      codOp = TypeRequest.EDIT_SCOPE.scope();  
  			  //fine LP PG200360
		}
		String numCc= (String) request.getAttribute("configutentetiposervizioente_numCc");
		String intestatarioCc= (String)request.getAttribute("configutentetiposervizioente_intCc"); 
		String tipoDoc =(String)request.getAttribute("configutentetiposervizioente_tipoDoc");
		String flagConRange = (String)request.getAttribute("configutentetiposervizioente_flagConRange");
		String emailDest = (String)request.getAttribute("configutentetiposervizioente_emailDest");
		String emailCcn = (String)request.getAttribute("configutentetiposervizioente_emailCcn");
		String emailMitt= (String)request.getAttribute("configutentetiposervizioente_emailTo");
		String desMitt = (String)request.getAttribute("configutentetiposervizioente_emailDescTo");
		String flagAllegato= (String)request.getAttribute("configutentetiposervizioente_flagAll");
		String codiceSia = (String)request.getAttribute("configutentetiposervizioente_codiceSia");
		String codiceIban = (String)request.getAttribute("configutentetiposervizioente_codiceIban");
		String codiceSecondoIban = (String)request.getAttribute("configutentetiposervizioente_secondoCodiceIban");//RP	//PG150180_001 GG
		String funzionePag = (String)request.getAttribute("configutentetiposervizioente_funzionePag");
		String tipoBol = (String)request.getAttribute("configutentetiposervizioente_tipoBol");
		String flagPagProtetta = (String)request.getAttribute("configutentetiposervizioente_flagPagProtetta");
		String urlServWeb = (String)request.getAttribute("configutentetiposervizioente_urlServWeb");
		String flagTipoPag = (String)request.getAttribute("configutentetiposervizioente_flagTipoPag");
		String flagIntegrazioneSeda = (String)request.getAttribute("configutentetiposervizioente_flagIntegrazioneSeda");
		String codiceUtenteSeda = (String)request.getAttribute("configutentetiposervizioente_codiceUtenteSeda");
		String flagNotificaPagamento = (String)request.getAttribute("configutentetiposervizioente_flagNotificaPagamento")==null?"":(String)request.getAttribute("configutentetiposervizioente_flagNotificaPagamento");
		String urlServizioWebNotificaPagamento = (String)request.getAttribute("configutentetiposervizioente_urlServizioWebNotificaPagamento")==null?"":(String)request.getAttribute("configutentetiposervizioente_urlServizioWebNotificaPagamento");

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
			tipoDovuto = (String) request.getAttribute("configutentetiposervizioente_tipoDovuto");
			//inizio LP PG180290 2020-05-21
			if(tipoDovuto == null) {
				tipoDovuto = "";
			} else {
				tipoDovuto = tipoDovuto.trim(); 
			}
			//fine LP PG180290 2020-05-21
		}
		//fine LP PG180290
		
		String flagStampaPagoPaPdf = (String) request.getAttribute("configutentetiposervizioente_flagStampaAvvisoPagoPa");
		String giorniStampaAvvisoPagoPa = (String) request.getAttribute("configutentetiposervizioente_giorniStampaAvvisoPagoPa");
		
		String autorizzazioneStampaAvvisoPagoPa = (String) request.getAttribute("configutentetiposervizioente_autorizzazioneStampaAvvisoPagoPa");
	    String cbillStampaAvvisoPagoPa = (String) request.getAttribute("configutentetiposervizioente_cbillStampaAvvisoPagoPa");
	    String infoEnteStampaAvvisoPagoPa = (String) request.getAttribute("configutentetiposervizioente_infoEnteStampaAvvisoPagoPa");
	    
		//inizio LP PG200360
	    String chiaveTassonomia = (String) request.getAttribute("configutentetiposervizioente_chiaveTassonomia");
	    if(chiaveTassonomia != null)
	    	chiaveTassonomia = chiaveTassonomia.trim();
	    if(chiaveTassonomia.length() == 0 || chiaveTassonomia.equals("0")) {
	    	//non dovrebbe succedere da interfaccia deve arrivare un valore =/= "0"
	    	chiaveTassonomia = null;
	    }
	   
	    String causali = null;
		if(causaliOn(request)) {
			causali = "";
			boolean bSPOM = (tipoBol != null && tipoBol.equalsIgnoreCase("SPOM"));
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
	
		//inizio SB PG2100140
		String codiceContabilita = (String)request.getAttribute("configutentetiposervizioente_codiceContabilita") == null ? "" : (String)request.getAttribute("configutentetiposervizioente_codiceContabilita");
		String capitolo = (String)request.getAttribute("configutentetiposervizioente_capitolo") == null ? "" : (String)request.getAttribute("configutentetiposervizioente_capitolo");
		String articolo = (String)request.getAttribute("configutentetiposervizioente_articolo") == null ? "" : (String)request.getAttribute("configutentetiposervizioente_articolo");
		String annoCompetenza = (String)request.getAttribute("configutentetiposervizioente_annoCompetenza") == null ? "" : (String)request.getAttribute("configutentetiposervizioente_annoCompetenza");
		//fine SB PG2100140

	    String dataDicituraPagamento = (String)request.getAttribute("configutentetiposervizioente_datapagamento")==null?"":(String)request.getAttribute("configutentetiposervizioente_datapagamento"); //SB PG210170
	    
	    try {
			addDDL(request);
			if (checkForm(flagIntegrazioneSeda, codiceUtenteSeda, request)) 
			{
				//inizio LP PG200420 - Numero CC-Iban Postale
				numCc = (numCc != null ? numCc.trim() : "");
//				boolean bSPOM = (tipoBol != null && tipoBol.equalsIgnoreCase("SPOM"));
//				if(!template.equalsIgnoreCase("regmarche")
//				   && bSPOM
//				   && !GenericsDateNumbers.verificaNumeroCCIbanPostale(numCc, codiceIban, codiceSecondoIban))
//				{
//					setFormMessage("frmAction", "Numero CC: deve essere di un 'Iban Postale' associato all'Ente", request);
//					request.setAttribute("done", null);
//					return;
//				}
				//fine LP PG200420 - Numero CC-Iban Postale
				//PG150180_001 GG - Inserito codiceSecondoIban
				//inizio LP PG180290
				/*
				ConfigUtenteTipoServizioEnte configUtente = new ConfigUtenteTipoServizioEnte(companyCode, codiceUtente, chiaveEnte,  codiceTipologiaServizio, tipoBol,
			    numCc, intestatarioCc, tipoDoc, flagConRange,  emailDest, emailCcn,emailMitt, desMitt, flagAllegato, codiceSia,  codiceIban,codiceSecondoIban,
			     funzionePag, flagPagProtetta, urlServWeb, flagTipoPag, flagIntegrazioneSeda, codiceUtenteSeda, usernameAutenticazione, flagNotificaPagamento, urlServizioWebNotificaPagamento, flagPagoPA);
			    */
				//inizio LP PG200360
				//ConfigUtenteTipoServizioEnte configUtente = new ConfigUtenteTipoServizioEnte(companyCode, codiceUtente, chiaveEnte,  codiceTipologiaServizio, tipoBol,
				//	    numCc, intestatarioCc, tipoDoc, flagConRange,  emailDest, emailCcn,emailMitt, desMitt, flagAllegato, codiceSia,  codiceIban,codiceSecondoIban,
				//	     funzionePag, flagPagProtetta, urlServWeb, flagTipoPag, flagIntegrazioneSeda, codiceUtenteSeda, usernameAutenticazione, flagNotificaPagamento,
				//	     urlServizioWebNotificaPagamento, flagPagoPA, tipoDovuto, flagStampaPagoPaPdf, giorniStampaAvvisoPagoPa, autorizzazioneStampaAvvisoPagoPa, cbillStampaAvvisoPagoPa, infoEnteStampaAvvisoPagoPa);
				ConfigUtenteTipoServizioEnte configUtente = new ConfigUtenteTipoServizioEnte(companyCode, codiceUtente, chiaveEnte,  codiceTipologiaServizio, tipoBol,
					    numCc, intestatarioCc, tipoDoc, flagConRange,  emailDest, emailCcn,emailMitt, desMitt, flagAllegato, codiceSia,  codiceIban,codiceSecondoIban,
					     funzionePag, flagPagProtetta, urlServWeb, flagTipoPag, flagIntegrazioneSeda, codiceUtenteSeda, usernameAutenticazione, flagNotificaPagamento,
					     urlServizioWebNotificaPagamento, flagPagoPA, tipoDovuto, flagStampaPagoPaPdf, giorniStampaAvvisoPagoPa, autorizzazioneStampaAvvisoPagoPa, cbillStampaAvvisoPagoPa, infoEnteStampaAvvisoPagoPa,
					     chiaveTassonomia, causali, articolo ,codiceContabilita, capitolo, annoCompetenza, dataDicituraPagamento); 
				//fine LP PG200360
				//fine LP PG180290
				/* we prepare object for save */
				ConfigUtenteTipoServizioEnteSaveRequest saveRequest = new ConfigUtenteTipoServizioEnteSaveRequest(configUtente,codOp);
				/* we save object */
				com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.StatusResponse res = WSCache.configUtenteTipoServizioEnteServer.save(saveRequest, request);
				if (res != null)
				{
					com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.Response response = res.getResponse();
					if (response != null)
					{
						if (!response.getReturncode().equals(ResponseReturncode.value1))
						{
							request.setAttribute("error", "error"); 
							request.setAttribute("done", null);
							if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) setFormMessage("frmAction", Messages.INS_ERR.format("L'informazione è già presente in archivio"), request); 
							if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) 
							{
								loadDdlChiave(request);
								setFormMessage("frmAction", Messages.UPDT_ERR.format(), request);
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
				//ConfigUtenteTipoServizioEnteDetailRequest detailRequest = new ConfigUtenteTipoServizioEnteDetailRequest(companyCode,codiceUtente,chiaveEnte,codiceTipologiaServizio);
				ConfigUtenteTipoServizioEnteDetailResponse detailResponse = getConfigUtenteTipoServizioEnte(companyCode,codiceUtente,chiaveEnte,codiceTipologiaServizio, request);
				
				request.setAttribute("configutentetiposervizioente_companyCode", detailResponse.getConfigutentetiposervizioente().getCompanyCode());
				request.setAttribute("configutentetiposervizioente_userCode", detailResponse.getConfigutentetiposervizioente().getUserCode());
				request.setAttribute("configutentetiposervizioente_chiaveEnte", detailResponse.getConfigutentetiposervizioente().getChiaveEnte());
				request.setAttribute("configutentetiposervizioente_codiceTipologiaServizio", detailResponse.getConfigutentetiposervizioente().getCodiceTipologiaServizio());
				request.setAttribute("configutentetiposervizioente_numCc", detailResponse.getConfigutentetiposervizioente().getNumCc());
				request.setAttribute("configutentetiposervizioente_intCc", detailResponse.getConfigutentetiposervizioente().getIntestatarioCc());
				request.setAttribute("configutentetiposervizioente_tipoDoc", detailResponse.getConfigutentetiposervizioente().getTipoDoc());
				request.setAttribute("configutentetiposervizioente_flagConRange", detailResponse.getConfigutentetiposervizioente().getFlagConRange());
				request.setAttribute("configutentetiposervizioente_emailDest", detailResponse.getConfigutentetiposervizioente().getEmailDest());
				request.setAttribute("configutentetiposervizioente_emailCcn", detailResponse.getConfigutentetiposervizioente().getEmailCcNascosta());
				request.setAttribute("configutentetiposervizioente_emailTo", detailResponse.getConfigutentetiposervizioente().getEmailMitt());
				request.setAttribute("configutentetiposervizioente_emailDescTo", detailResponse.getConfigutentetiposervizioente().getDesMittente());
				request.setAttribute("configutentetiposervizioente_flagAll", detailResponse.getConfigutentetiposervizioente().getFlagAllegato());
				request.setAttribute("configutentetiposervizioente_codiceSia", detailResponse.getConfigutentetiposervizioente().getCodiceSia());
				request.setAttribute("configutentetiposervizioente_codiceIban", detailResponse.getConfigutentetiposervizioente().getCodiceIban());
				request.setAttribute("configutentetiposervizioente_funzionePag", detailResponse.getConfigutentetiposervizioente().getFunzionePag());
				request.setAttribute("configutentetiposervizioente_tipoBol", detailResponse.getConfigutentetiposervizioente().getTipoBollettino());
				request.setAttribute("configutentetiposervizioente_flagPagProtetta", detailResponse.getConfigutentetiposervizioente().getFlagPagProtetta());
				request.setAttribute("configutentetiposervizioente_urlServWeb", detailResponse.getConfigutentetiposervizioente().getUrlServWeb());
				request.setAttribute("configutentetiposervizioente_flagTipoPag", detailResponse.getConfigutentetiposervizioente().getFlagTipoPag());
				request.setAttribute("configutentetiposervizioente_flagIntegrazioneSeda", detailResponse.getConfigutentetiposervizioente().getFlagIntegrazioneSeda());
				request.setAttribute("configutentetiposervizioente_codiceUtenteSeda", detailResponse.getConfigutentetiposervizioente().getCodiceUtenteSeda().trim());
				request.setAttribute("configutentetiposervizioente_flagNotificaPagamento", detailResponse.getConfigutentetiposervizioente().getFlagNotificaPagamento());
				request.setAttribute("configutentetiposervizioente_urlServizioWebNotificaPagamento", detailResponse.getConfigutentetiposervizioente().getUrlServizioWebNotificaPagamento());
				request.setAttribute("chk_flagPagoPA", detailResponse.getConfigutentetiposervizioente().getFlagPagoPA().equals("Y"));	//TODO da verificare
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERR.format());
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
		String companyCode = request.getParameter("configutentetiposervizioente_companyCode");
        String codiceUtente= request.getParameter("configutentetiposervizioente_codiceUtente");
        String chiaveEnte= request.getParameter("configutentetiposervizioente_chiaveEnte");
        String codiceTipologiaServizio= request.getParameter("configutentetiposervizioente_codiceTipologiaServizio");
		try {
			request.setAttribute("varname", "configutentetiposervizioente");
			//stub = new ConfigUtenteTipoServizioEnteImplementationStub(new URL(getConfigutentetiposervizioenteManagerEndpointUrl()), null);
			/* we prepare object for cancel */
			ConfigUtenteTipoServizioEnteCancelRequest cancelRequest = new ConfigUtenteTipoServizioEnteCancelRequest(companyCode,codiceUtente,chiaveEnte,codiceTipologiaServizio);
			/* we cancel object */
			cancelConfigUtenteTipoServizioEnte(cancelRequest, request);
			request.setAttribute("message", Messages.CANC_OK.format());
		} catch (FaultType f) {
			request.setAttribute("message", Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) {
			request.setAttribute("message", Messages.GENERIC_ERR.format());
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
	
	private ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEntesSearchResponse(String codiceSocieta,String codiceUtente,String chiaveEnte, String codiceTipologiaServizio,String strEnte,String strSocieta,String strUtente,String strTipologiaServizio,int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException{
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteSearchRequest in = new ConfigUtenteTipoServizioEnteSearchRequest();
		
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		
		in.setCompanyCode(codiceSocieta == null ? "" : codiceSocieta);
		in.setCodiceUtente(codiceUtente == null ? "" : codiceUtente);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setStrDescrSocieta(strSocieta== null ? "" :strSocieta);
		in.setStrDescrTipologiaServizio(strTipologiaServizio== null ? "" :strTipologiaServizio);
		in.setStrEnte(strEnte== null ? "" :strEnte);
		in.setStrDescrUtente(strUtente== null ? "" :strUtente);
		
		//VERIFICARE SE AGGIUINGERE ALTRI in.set
		
		res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes(in, request);
		return res;
	}
	
	private ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEntes2(String codiceSocieta,String codiceUtente,String chiaveEnte, String procName, HttpServletRequest request) throws FaultType, RemoteException{
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteSearchRequest2 in = new ConfigUtenteTipoServizioEnteSearchRequest2();
		
		in.setCompanyCode(codiceSocieta == null ? "" : codiceSocieta);
		in.setCodiceUtente(codiceUtente == null ? "" : codiceUtente);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setProcName(procName == null ? "" : procName);
		
		res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes2(in, request);
		
		return res;
	}
	
	private ConfigUtenteTipoServizioEnteDetailResponse getConfigUtenteTipoServizioEnteDetailResponse(String companyCode, String codiceUtente, String chiaveEnte, String codiceTipologiaServizio, HttpServletRequest request) throws FaultType, RemoteException{
		
		ConfigUtenteTipoServizioEnteDetailResponse res = null;
		ConfigUtenteTipoServizioEnteDetailRequest in = new ConfigUtenteTipoServizioEnteDetailRequest(companyCode, codiceUtente, chiaveEnte, codiceTipologiaServizio);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setUserCode(codiceUtente == null ? "" : codiceUtente);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		
		res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEnte(in, request);
		return res;
	}
	
	private void  cancelConfigUtenteTipoServizioEnte(ConfigUtenteTipoServizioEnteCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.configUtenteTipoServizioEnteServer.cancel(cancelRequest, request);
		
	}
	
	private ConfigUtenteTipoServizioEnteDetailResponse getConfigUtenteTipoServizioEnte(String companyCode, String codiceUtente, String chiaveEnte, String codiceTipologiaServizio, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
	{
		ConfigUtenteTipoServizioEnteDetailRequest in = new ConfigUtenteTipoServizioEnteDetailRequest(companyCode, codiceUtente, chiaveEnte, codiceTipologiaServizio);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setUserCode(codiceUtente == null ? "" : codiceUtente);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		
		return WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEnte(in, request);
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
	
	private ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEnteSearchResponse3(
			ConfigUtenteTipoServizioEnteSearchRequest3 configUtenteTipoServizioEnteSearchRequest3, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteSearchRequest3 in = new ConfigUtenteTipoServizioEnteSearchRequest3();
		/*in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);*/
		in.setCompanyCode(configUtenteTipoServizioEnteSearchRequest3.getCompanyCode() == null ? "" : configUtenteTipoServizioEnteSearchRequest3.getCompanyCode());
		//in.setCodiceTipologiaServizio(configUtenteTipoServizio.g.getcodiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		
		
		res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes3(in, request);
		return res;
	}
	
	private void loadDdlChiave(HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse = getConfigUtenteTipoServizioEntes2(userCodiceSocieta,"","","", request);
		request.setAttribute("entetiposervizios", configUtenteTipoServizioEnteSearchResponse.getResponse().getListXml());
	
		ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse2 = null;
		configUtenteTipoServizioEnteSearchResponse2 = getConfigUtenteTipoServizioEnteSearchResponse3((new ConfigUtenteTipoServizioEnteSearchRequest3(request.getParameter("configutentetiposervizioente_companyCode"))), request);
		request.setAttribute("utentetiposervizios2", configUtenteTipoServizioEnteSearchResponse2.getResponse().getListXml());
		
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
		session.setAttribute("configutentetiposervizioente_desSoc", request.getAttribute("configutentetiposervizioente_desSoc")!=null?request.getAttribute("configutentetiposervizioente_desSoc"):"");
		session.setAttribute("configutentetiposervizioente_desUte", request.getAttribute("configutentetiposervizioente_desUte")!=null?request.getAttribute("configutentetiposervizioente_desUte"):"");
		session.setAttribute("configutentetiposervizioente_desEnte", request.getAttribute("configutentetiposervizioente_desEnte")!=null?request.getAttribute("configutentetiposervizioente_desEnte"):"");
		session.setAttribute("configutentetiposervizioente_desTp", request.getAttribute("configutentetiposervizioente_desTp")!=null?request.getAttribute("configutentetiposervizioente_desTp"):"");
	}
	private HttpServletRequest ripristinaFiltriDiRicerca(HttpServletRequest request, HttpSession session) {
		request.setAttribute("configutentetiposervizioente_desSoc",session.getAttribute("configutentetiposervizioente_desSoc"));
		request.setAttribute("configutentetiposervizioente_desUte",session.getAttribute("configutentetiposervizioente_desUte"));
		request.setAttribute("configutentetiposervizioente_desEnte",session.getAttribute("configutentetiposervizioente_desEnte"));
		request.setAttribute("configutentetiposervizioente_desTp",session.getAttribute("configutentetiposervizioente_desTp"));
		svuotaFiltriDiRicercaDaSession(session);
		return request;
	}
	private void svuotaFiltriDiRicercaDaSession(HttpSession session) {
		session.setAttribute("configutentetiposervizioente_desSoc",null);
		session.setAttribute("configutentetiposervizioente_desUte",null);
		session.setAttribute("configutentetiposervizioente_desEnte",null);
		session.setAttribute("configutentetiposervizioente_desTp",null);
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