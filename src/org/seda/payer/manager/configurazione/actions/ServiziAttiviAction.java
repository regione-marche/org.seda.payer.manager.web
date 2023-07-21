/**
 * 
 */
package org.seda.payer.manager.configurazione.actions;

import java.io.BufferedInputStream;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.WebRowSet;

import org.apache.commons.lang.StringEscapeUtils;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteResponse;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteResponsePageInfo;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteSearchRequest;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteSearchResponse;


import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.core.bean.ConfigurazioneModello3;
import com.seda.payer.core.dao.ConfigurazioneModello3DAOFactory;
import com.seda.payer.core.dao.ConfigurazioneModello3Dao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteGetRecordRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteGetRecordResponseType;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoSearchRequest;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoSearchResponse;

import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnte;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteDetailRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteDetailResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteResponsePageInfo;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSaveRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest3;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.DownloadServiziAttiviCsvRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.DownloadServiziAttiviCsvResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailRequest;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class ServiziAttiviAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;
	private String siglaProvincia;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String strCodiceSocieta, strCodiceUtente, strChiaveEnte, usernameAutenticazione;
	private String strTipologiaServizio;
	private String userCodiceSocieta = null;
	private String tagCausale = "___CAUS___";
    private ArrayList<String> listaCausali = null;
    private int indexRemove = -1;
    private int indexAdd = -1;
	private DataSource configurazioneModello3DataSource = null;
	private String dbSchemaCodSocieta;
	private String configurazioneModello3DbSchema = null;
	protected DataSource getConfigurazioneModello3DataSource(){return this.configurazioneModello3DataSource;}
	protected String getConfigurazioneModello3DbSchema(){return this.configurazioneModello3DbSchema;}

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession(false);
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order","action");
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Chiamo il metodo per i settaggi relativi al profilo utente
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
			setProfile(request);
		/*
		 * Carico le DDl statiche
		 */
		loadStaticXml_DDL(request, session);	
		
		siglaProvincia = isNull(request.getAttribute(Field.TX_PROVINCIA.format()));

		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
		   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		   pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		   order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order","action");
			
		//UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		UserBean user = getUserBean();
		/*
		* "userCodiceSocieta" serve per gestire la profilazione utente; permette 
		* di limitare la visibilità e l'operatività agli ambiti consentiti.
		* E' sufficiente la società perché "configurazione" è accessibile 
		* solo ad AMMI ed AMSO
		*/
	    /*
		if (user != null)
			userCodiceSocieta = (user.getProfile().equals("AMMI") ? "" : user.getCodiceSocieta());
		else
			userCodiceSocieta = "XXXXX";
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
		*/
			
		if (user != null) {
			userCodiceSocieta = (user.getProfile().equals("AMMI") ? "" : user.getCodiceSocieta());
			usernameAutenticazione = user.getUserName().trim();
		}
		strCodiceSocieta = getParamCodiceSocieta();
		strCodiceUtente = getParamCodiceUtente();
		strChiaveEnte = getParamCodiceEnte();
		String appoTipologiaServizio = getTipologiaServizio(request, session);
		if(appoTipologiaServizio != null && !appoTipologiaServizio.equals("") && user != null && user.getProfile().equals("AMMI")) {
			strTipologiaServizio = appoTipologiaServizio.substring(0, 3);
			//inizio LP PG21XX06 - Bug ButtonIndietro
			//if(appoTipologiaServizio.length() > 4){
			//	strCodiceSocieta = getTipologiaServizio(request,session).substring(4);
			//}
			//fine LP PG21XX06 - Bug ButtonIndietro
		} else {
			if (appoTipologiaServizio !=null && !appoTipologiaServizio.equals("") && appoTipologiaServizio.indexOf("_", 0) != -1) {
				String serv = appoTipologiaServizio;
				serv= serv.replace("'", "");
				strTipologiaServizio = serv.substring(0, 3);
			} else
				strTipologiaServizio  = appoTipologiaServizio;
		}
		
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
			
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String configurazioneModello3DataSource =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
		
		System.out.println("configurazioneModello3DataSource =" + configurazioneModello3DataSource);
		this.configurazioneModello3DbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		System.out.println("configurazioneModello3DbSchema =" + configurazioneModello3DbSchema);
		
		try {	
			this.configurazioneModello3DataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(configurazioneModello3DataSource));
			System.out.println("connessione eseguita");
			
		} catch (ServiceLocatorException e) {
			System.out.println("connessione per configurazioneModello3 non eseguita (1)!");
			try {	
				this.configurazioneModello3DataSource = ServiceLocator.getInstance().getDataSource("java:/".concat(configurazioneModello3DataSource));
				System.out.println("connessione eseguita");
			} catch (ServiceLocatorException e1) {
				System.out.println("connessione per configurazioneModello3 non eseguita (2)!");
			}
		}
		
		listaCausali = (ArrayList<String>) session.getAttribute("listaCausali");
		if(listaCausali == null) {
		    listaCausali = new ArrayList<String>();
		}
		
		if(request.getParameter("button_add_causale") != null) {
			firedButton = FiredButton.TX_BUTTON_ADD;
			request.setAttribute("action", "edit");
		} else if(request.getParameter("button_delete_causale") != null) {
			firedButton = FiredButton.TX_BUTTON_REMOVE;
			request.setAttribute("action", "edit");
		} else {
			String inEdit = (String) request.getSession().getAttribute("inEdit");
			if(firedButton.equals(FiredButton.TX_BUTTON_RESET) &&  inEdit != null && inEdit.trim().length() > 0) {
				firedButton = FiredButton.TX_BUTTON_RESET_INS;
			}
		}
		
		//String template = getTemplateCurrentApplication(request, session);
		switch(firedButton) {
		
			case TX_BUTTON_ADD:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(), false);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), getParamCodiceEnte(), false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
				indexRemove = -1;
				indexAdd = 1;
				edit(request);
				break;
				
			case TX_BUTTON_REMOVE:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(), false);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), getParamCodiceEnte(), false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
				indexRemove = Integer.parseInt(request.getParameter("button_delete_causale"));
				indexAdd = -1;
				edit(request);
				break;
		
			case TX_BUTTON_CERCA: 
				session.setAttribute("inEdit", "");
				try {
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(), false);
					//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), false);
					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), getParamCodiceEnte(), false);
					//loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), false);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
					
					salvaFiltriDiRicerca(request, session);
					search(request);

				} catch(Exception e) {
					setFormMessage("serviziAttiviForm", e.getMessage() , request);
					e.printStackTrace();
				}
				break;
			
			case TX_BUTTON_DOWNLOAD:
				String file="";
				String pathFile="";
				request.setAttribute("download", "Y");
				
				try {
						salvaFiltriDiRicerca(request, session);
						
						pathFile = download(request);
						System.out.println("Recuperato file : " +pathFile);
						
						File fileIs = new File(pathFile);
						FileInputStream fis = null;
						BufferedInputStream bis = null;
						DataInputStream dis = null;
				 
						try {
							fis = new FileInputStream(fileIs);
				 
							bis = new BufferedInputStream(fis);
							dis = new DataInputStream(bis);
							StringBuffer buffered = new StringBuffer("");
							while (dis.available() != 0) {
								buffered.append(dis.readLine()+"\n");
							}
							file = buffered.toString();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								fis.close();
								bis.close();
								dis.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
				} catch (Exception e) {
					request.setAttribute("download", "N");
					e.printStackTrace();
					break;
				}  
				System.out.println("File pronto per la JSP");
				//aggiustamento carattere \r perso ne webservice 
				file = file.replaceAll("\n", "\r\n");
				
				String template = getTemplateCurrentApplication(request, request.getSession());
				if(template.equals("aosta")||template.equals("aostaFR")) {
				    String[] specialChars = { "à", "â", "ä", "ç", "Ã©","è", "é", "ë", "ï", "î", "ö", "ô", "û", "ü", "ù", "æ", "Â", "Ä", "Ê", "Ë", "Î", "Ï", "Ô", "Ö", "Û", "Ü", "À", "Ç", "É", "È", "Ê", "Ô", "Æ"};
				    String[] standardChars = { "a", "a", "a", "c", "e","e", "e", "e", "i", "i", "o", "o", "u", "u", "u", "a", "A", "A", "E", "E", "I", "I", "O", "O", "U", "U", "A", "C", "E", "E", "E", "O", "A"};
					for (int i = 0; i < specialChars.length; i++) {
				        file = file.replaceAll(specialChars[i], standardChars[i]);
				    }	
				}
				
				request.setAttribute("sinteticoServiziAttivi", file);
				request.setAttribute("filename","sinteticoServiziAttivi.csv");

				File delfile = new File(pathFile);
				delfile.delete();

			break;	
			case TX_BUTTON_RESET:
				session.setAttribute("inEdit", "");
				resetParametri(request);
				setProfile(request);
				siglaProvincia = "";
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), getParamCodiceEnte(), true);
				//loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				//if(template.equalsIgnoreCase("regmarche")) {
					svuotaFiltriDiRicercaDaSession(session);
				//}
				try {
					add(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
				break;
	
			case TX_BUTTON_SOCIETA_CHANGED:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), "", "", "", true);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), getParamCodiceEnte(), true);
				//loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), "", true);
				break;
	
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(), false);
				loadTipologiaServizioXml_DDL_2(request, session, "", "", "", false);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, "", "", true);
				//loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				//loadListaGatewayXml_DDL(request, session, "", "", false);
				break;
	
			case TX_BUTTON_ENTE_CHANGED:
				loadProvinciaXml_DDL(request, session, "", false);
				LoadListaUtentiEntiXml_DDL(request, session, "" , "", "", "", false);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				//loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				break;
	
			case TX_BUTTON_NULL: 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				//loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				break;
				
			case TX_BUTTON_EDIT:
				session.setAttribute("inEdit", "");
				//inizio LP PG21XX06 - Bug ButtonIndietro
				//loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				////loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				//loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				////loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(), false);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
				//fine LP PG21XX06 - Bug ButtonIndietro
				edit(request);
				break;

			case TX_BUTTON_AGGIUNGI:
				session.setAttribute("inEdit", "");
				//inizio LP PG21XX06 - Bug ButtonIndietro
				//loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				////loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				//loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				////loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(), false);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
				//fine LP PG21XX06 - Bug ButtonIndietro
				saveedit(request);
				break;
				
			case TX_BUTTON_RESET_INS:				
				session.setAttribute("inEdit", "");
				//inizio LP PG21XX06 - Bug ButtonIndietro
				//loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				////loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				//loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				////loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(), false);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
				//fine LP PG21XX06 - Bug ButtonIndietro
				edit(request);
				break;

			case TX_BUTTON_INDIETRO: 
				session.setAttribute("inEdit", "");
				try {
					//if(template.equalsIgnoreCase("regmarche")) {
						ripristinaFiltriDiRicerca(request, session);
					//}
					//inizio LP PG21XX06 - Bug ButtonIndietro
					appoTipologiaServizio = getTipologiaServizio(request, session);
					if(appoTipologiaServizio != null && !appoTipologiaServizio.equals("") && user != null && user.getProfile().equals("AMMI")) {
						strTipologiaServizio = appoTipologiaServizio.substring(0, 3);
					} else {
						if (appoTipologiaServizio !=null && !appoTipologiaServizio.equals("") && appoTipologiaServizio.indexOf("_", 0) != -1) {
							String serv = appoTipologiaServizio;
							serv= serv.replace("'", "");
							strTipologiaServizio = serv.substring(0, 3);
						} else
							strTipologiaServizio  = appoTipologiaServizio;
					}
					loadProvinciaXml_DDL(request, session, null, false);
					loadTipologiaServizioXml_DDL_2(request, session, null, null, null, false);
					LoadListaUtentiEntiXml_DDL(request, session, null, null, null, null, false);
					salvaFiltriDiRicerca(request, session);
					//fine LP PG21XX06 - Bug ButtonIndietro
					search(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
			default:
				break;
		}
		//request.setAttribute(Field.TX_MESSAGE.format(), getMessage());
		//request.setAttribute(Field.TX_ERROR_MESSAGE.format(), getErrorMessage());
		session.setAttribute("gestioneCausali", causaliOn(request));
		return null;
	}

	/*
	public Object search(HttpServletRequest request) throws ActionException {
		String strEnte = null;
		String strSocieta = null;
		String strUtente = null;
		String strTipologiaServizio = null;
		String codTipologiaServizio = null;
		String template = getTemplateCurrentApplication(request, request.getSession());
		
		if(template.equalsIgnoreCase("regmarche")) {
			//strEnte = (String)request.getAttribute("configutentetiposervizioente_desEnte");
			//strSocieta = (String)request.getAttribute("configutentetiposervizioente_desSoc");
			//strUtente = (String)request.getAttribute("configutentetiposervizioente_desUte");
			//strTipologiaServizio = (String)request.getAttribute("configutentetiposervizioente_desTp");
			strEnte = (String)request.getAttribute("tx_UtenteEnte");
			strSocieta = (String)request.getAttribute("tx_Societa");
			strUtente = null;
			strTipologiaServizio = this.strTipologiaServizio != null ? this.strTipologiaServizio : ""; 
		}
		
		try {
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButtonReset!=null){				
				resetParametri(request);		
				if(template.equalsIgnoreCase("regmarche")) {
					strEnte = null;
					strSocieta = null;
					strUtente = null;
					strTipologiaServizio = null;
				}
			}
			if(!template.equalsIgnoreCase("regmarche")) {
				if (firedButton == null && firedButtonReset == null)
				{
					//strEnte = (String) request.getAttribute("configutentetiposervizioente_desEnte");
					//strSocieta = (String) request.getAttribute("configutentetiposervizioente_desSoc");
					//strUtente = (String) request.getAttribute("configutentetiposervizioente_desUte");
					//strTipologiaServizio = (String) request.getAttribute("configutentetiposervizioente_desTp");
					//codTipologiaServizio = (String) request.getAttribute("configutentetiposervizioente_desCod");
					strEnte = (String)request.getAttribute("txt_UtenteEnte");
					strSocieta = (String)request.getAttribute("txt_Societa");
					strUtente = null;
					strTipologiaServizio = this.strTipologiaServizio != null ? this.strTipologiaServizio : ""; 
				}
			}
			ConfigUtenteTipoServizioEnteSearchResponse searchResponse = null;
			if(template.equalsIgnoreCase("regmarche")) {
				if (firedButton!=null)
					 if(firedButton.equals("Indietro"))
						 searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta,null,null,null,strEnte,strSocieta,strUtente,strTipologiaServizio,rowsPerPage, pageNumber, order, request);
				if (firedButton==null)  searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta,"","","",strEnte,strSocieta,strUtente,strTipologiaServizio,rowsPerPage, pageNumber, order, request);
				
			} else {
				if (firedButton!=null)
					 if(firedButton.equals("Indietro"))
						 searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta,null,null,codTipologiaServizio,null,null,null,null,rowsPerPage, pageNumber, order, request);
				 if (firedButton==null)  searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta,"","",codTipologiaServizio,strEnte,strSocieta,strUtente,strTipologiaServizio,rowsPerPage, pageNumber, order, request);
			}
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
			
			if(!template.equalsIgnoreCase("regmarche")) {
				request.setAttribute("configutentetiposervizioente_desSoc", strSocieta);
				request.setAttribute("configutentetiposervizioente_desUte", strUtente);
				request.setAttribute("configutentetiposervizioente_desTp", strTipologiaServizio);
				request.setAttribute("configutentetiposervizioente_desEnte", strEnte);
			}
			request.setAttribute("configutentetiposervizioentes", configutentetiposervizioenteServizioResponse.getListXml());
			request.setAttribute("configutentetiposervizioentes.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	public Object search(HttpServletRequest request) throws ActionException {
		String strSocieta = null;
		//String strProv = null;
		String strEnte = null;
		String strUtente = null;
		String strTipologiaServizio = null;
		//String codTipologiaServizio = null;
		//String template = getTemplateCurrentApplication(request, request.getSession());
		String strCodSocieta = null;  
		//if(template.equalsIgnoreCase("regmarche")) {
			//strEnte = (String)request.getAttribute("configutentetiposervizioente_desEnte");
			//strSocieta = (String)request.getAttribute("configutentetiposervizioente_desSoc");
			//strUtente = (String)request.getAttribute("configutentetiposervizioente_desUte");
			//strTipologiaServizio = (String)request.getAttribute("configutentetiposervizioente_desTp");
		//}
		strCodSocieta = (userCodiceSocieta != "" ? userCodiceSocieta : strSocieta);  
		try {
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButtonReset!=null){				
				resetParametri(request);		
			}
			/*
			if(!template.equalsIgnoreCase("regmarche")) {
				if (firedButton == null && firedButtonReset == null)
				{
					//strEnte = (String) request.getAttribute("configutentetiposervizioente_desEnte");
					//strSocieta = (String) request.getAttribute("configutentetiposervizioente_desSoc");
					//strUtente = (String) request.getAttribute("configutentetiposervizioente_desUte");
					//strTipologiaServizio = (String) request.getAttribute("configutentetiposervizioente_desTp");
					//codTipologiaServizio = (String) request.getAttribute("configutentetiposervizioente_desCod");
					strSocieta = (String)request.getAttribute("tx_Societa");
					strEnte = (String)request.getAttribute("tx_UtenteEnte");
					strSocieta = (String) request.getAttribute("tx_societa");
					//strProv = (String) request.getAttribute("tx_provincia");
					strEnte = (String) request.getAttribute("tx_UtenteEnte");
					strTipologiaServizio = (String) request.getAttribute("tx_tipologia_servizio");
				}
			}
			*/
			ConfigUtenteTipoServizioEnteSearchResponse searchResponse = null;
			if (firedButton != null) {
				 if(firedButton.equals("Indietro")) {
					 //searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta,null,null,null,strEnte,strSocieta,strUtente,strTipologiaServizio,rowsPerPage, pageNumber, order, request);
					 //inizio LP PG21XX06 - Bug ButtonIndietro
					 //strUtente =  (String) request.getAttribute("configutentetiposervizioente_codiceUtente");
					 //fine LP PG21XX06 - Bug ButtonIndietro
					 if(strUtente == null) {
						strUtente = (String) request.getAttribute("tx_UtenteEnte");
						if(strUtente != null && strUtente.length() > 10) {
							//inizio LP PG21XX06 - Bug ButtonIndietro
							//strUtente = strUtente.substring(5, 5); 
							strUtente = strUtente.substring(5, 10); 
							//fine LP PG21XX06 - Bug ButtonIndietro
						}
					 }
					 //inizio LP PG21XX06 - Bug ButtonIndietro
					 //strSocieta = (String) request.getAttribute("configutentetiposervizioente_companyCode");
					 //fine LP PG21XX06 - Bug ButtonIndietro
					 if(strSocieta == null) {
						 strSocieta = (String) request.getAttribute("tx_societa");
						 
					 }
					 //inizio LP PG21XX06 - Bug ButtonIndietro
					//strEnte = (String) request.getAttribute("configutentetiposervizioente_chiaveEnte");
					 //fine LP PG21XX06 - Bug ButtonIndietro
					if(strEnte == null) {
						strEnte = (String) request.getAttribute("tx_UtenteEnte");
						if(strEnte != null && strEnte.length() > 10) {
							strEnte = strEnte.substring(10); 
						}
					}
					//inizio LP PG21XX06 - Bug ButtonIndietro
					//strTipologiaServizio = (String) request.getAttribute("configutentetiposervizioente_codiceTipologiaServizio");
					//if(strTipologiaServizio == null) {
					//	strTipologiaServizio = (String) request.getAttribute("tx_tipologia_servizio");
					//}
					strTipologiaServizio = this.strTipologiaServizio != null ? this.strTipologiaServizio : ""; 
					//fine LP PG21XX06 - Bug ButtonIndietro
					strCodSocieta = (userCodiceSocieta != "" ? userCodiceSocieta : strSocieta);
					//inizio LP PG21XX06 - Bug ButtonIndietro
					//searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta, strUtente, strEnte, null, null, null, null, strTipologiaServizio, rowsPerPage, pageNumber, order, request);
					searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(strCodSocieta, strUtente, strEnte, strTipologiaServizio, null, null, null, null, rowsPerPage, pageNumber, order, request);
					//fine LP PG21XX06 - Bug ButtonIndietro
				 }
			}
			if (firedButton == null) {
				strCodSocieta = (userCodiceSocieta != "" ? userCodiceSocieta : strCodiceSocieta);  
				strTipologiaServizio = this.strTipologiaServizio != null ? this.strTipologiaServizio : ""; 
				//inizio LP PG21XX06 - Bug ButtonIndietro
				//searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(strCodSocieta, strCodiceUtente, strChiaveEnte, null, null, null, null, strTipologiaServizio, rowsPerPage, pageNumber, order, request);
				searchResponse =getConfigUtenteTipoServizioEntesSearchResponse(strCodSocieta, strCodiceUtente, strChiaveEnte, strTipologiaServizio, null, null, null, null, rowsPerPage, pageNumber, order, request);
				//fine LP PG21XX06 - Bug ButtonIndietro
			}
				
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
			/*
			if(!template.equalsIgnoreCase("regmarche")) {
				strSocieta = (String) request.getAttribute("tx_Societa");
				strEnte = (String) request.getAttribute("tx_UtenteEnte");
				strSocieta = (String) request.getAttribute("tx_societa");
				//strProv = (String) request.getAttribute("tx_provincia");
				strEnte = (String) request.getAttribute("tx_UtenteEnte");
				strTipologiaServizio = (String) request.getAttribute("tx_tipologia_servizio");
				request.setAttribute("configutentetiposervizioente_desSoc", strSocieta);
				request.setAttribute("configutentetiposervizioente_desUte", strUtente);
				request.setAttribute("configutentetiposervizioente_desTp", strTipologiaServizio);
				request.setAttribute("configutentetiposervizioente_desEnte", strEnte);
			}
			*/
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
				/*
				ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse = getConfigUtenteTipoServizioEntes2(userCodiceSocieta,"","","", request);
				request.setAttribute("entetiposervizios", configUtenteTipoServizioEnteSearchResponse.getResponse().getListXml());
				*/
			}
			/*
			 * VIENE caricata comunque la DDL del tipo bollettino
			 */
			String tipoBollettino = "";
			BollettinoSearchResponse bolSearchResponse = getBollettinoSearchResponse(tipoBollettino, "", 0, 0, "", request);
			request.setAttribute("bollettini", bolSearchResponse.getResponse().getListXml());
			if(request.getAttribute("tassonomie") == null) {
				ConfigTassonomiaListaResponse configTassonomiaListaResponse = getTassonomiaListaResponse(request);
				request.setAttribute("tassonomie", configTassonomiaListaResponse.getListaTassonomia().getListXml());
			}
		} catch (FaultType e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			loadDdlChiave(request);
			
			ConfigUtenteTipoServizioEnteDetailResponse response = 
					getConfigUtenteTipoServizioEnteDetailResponse(request.getParameter("configutentetiposervizioente_companyCode"),request.getParameter("configutentetiposervizioente_codiceUtente"),request.getParameter("configutentetiposervizioente_chiaveEnte"),request.getParameter("configutentetiposervizioente_codiceTipologiaServizio"), request);

			String companyCode = response.getConfigutentetiposervizioente().getCompanyCode();
			String userCode = response.getConfigutentetiposervizioente().getUserCode();
			String chiaveEnte = response.getConfigutentetiposervizioente().getChiaveEnte();
			String tipoBoll = response.getConfigutentetiposervizioente().getTipoBollettino();
			String codiceTipologiaServizio = response.getConfigutentetiposervizioente().getCodiceTipologiaServizio();
			boolean bSPOM = (tipoBoll != null && tipoBoll.equalsIgnoreCase("SPOM")); 
			
			request.setAttribute("configutentetiposervizioente_companyCode", companyCode);
			request.setAttribute("configutentetiposervizioente_codiceUtente", userCode);
			request.setAttribute("configutentetiposervizioente_chiaveEnte", chiaveEnte);
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
			request.setAttribute("configutentetiposervizioente_tipoBol", tipoBoll);
			request.setAttribute("configutentetiposervizioente_flagPagProtetta", response.getConfigutentetiposervizioente().getFlagPagProtetta());
			request.setAttribute("configutentetiposervizioente_urlServWeb", response.getConfigutentetiposervizioente().getUrlServWeb());
			request.setAttribute("configutentetiposervizioente_flagTipoPag", response.getConfigutentetiposervizioente().getFlagTipoPag().trim());
			request.setAttribute("configutentetiposervizioente_flagIntegrazioneSeda", response.getConfigutentetiposervizioente().getFlagIntegrazioneSeda());
			request.setAttribute("configutentetiposervizioente_codiceUtenteSeda", response.getConfigutentetiposervizioente().getCodiceUtenteSeda().trim());
			request.setAttribute("configutentetiposervizioente_flagNotificaPagamento", response.getConfigutentetiposervizioente().getFlagNotificaPagamento());
			request.setAttribute("configutentetiposervizioente_urlServizioWebNotificaPagamento", response.getConfigutentetiposervizioente().getUrlServizioWebNotificaPagamento());
			request.setAttribute("chk_flagPagoPA", response.getConfigutentetiposervizioente().getFlagPagoPA().equals("Y"));	//TODO da verificare
			request.setAttribute("configutentetiposervizioente_tipoDovuto", response.getConfigutentetiposervizioente().getTipoDovuto());
			
			request.setAttribute("configutentetiposervizioente_giorniStampaAvvisoPagoPa", response.getConfigutentetiposervizioente().getGiorniStampaAvvisoPagoPa());
			
			request.setAttribute("configutentetiposervizioente_autorizzazioneStampaAvvisoPagoPa", response.getConfigutentetiposervizioente().getAutorizzazioneStampaAvvisoPagoPa());
		    request.setAttribute("configutentetiposervizioente_cbillStampaAvvisoPagoPa", response.getConfigutentetiposervizioente().getCbillStampaAvvisoPagoPa());
		    request.setAttribute("configutentetiposervizioente_infoEnteStampaAvvisoPagoPa", response.getConfigutentetiposervizioente().getInfoEnteStampaAvvisoPagoPa());
		    
		    //inizio SB PG210140
		    request.setAttribute("configutentetiposervizioente_articolo", response.getConfigutentetiposervizioente().getArticolo());
		    request.setAttribute("configutentetiposervizioente_capitolo", response.getConfigutentetiposervizioente().getCapitolo());
		    request.setAttribute("configutentetiposervizioente_annoCompetenza", response.getConfigutentetiposervizioente().getAnnoCompetenza());
		    request.setAttribute("configutentetiposervizioente_codiceContabilita", response.getConfigutentetiposervizioente().getCodiceContabilita());
		    
		    request.setAttribute("configutentetiposervizioente_datapagamento", response.getConfigutentetiposervizioente().getDataDicituraPagamento());  //SB PG210170
		    //inizio CD PAGO438
		    request.setAttribute("nome_struttura_ente", response.getConfigutentetiposervizioente().getStrutturaEnte());
			request.setAttribute("nome_struttura_ente_rag_soc", response.getConfigutentetiposervizioente().getStrutturaEnteFornitore());
			request.setAttribute("nome_ente", response.getConfigutentetiposervizioente().getNomeEnte());
			request.setAttribute("Nome_ref_tec", response.getConfigutentetiposervizioente().getNomeFornitore());
			request.setAttribute("cognome_ref_tec", response.getConfigutentetiposervizioente().getCognomeFornitore());
			request.setAttribute("Telefono_Referente_Tecnico_Ente", response.getConfigutentetiposervizioente().getTelefonoFornitore());
			request.setAttribute("mail_ref_tecnico", response.getConfigutentetiposervizioente().getMailFornitore());
			request.setAttribute("cognome_ente", response.getConfigutentetiposervizioente().getCognomeEnte());
			request.setAttribute("telefono_ente", response.getConfigutentetiposervizioente().getTelefonoEnte());
			request.setAttribute("mail_ente", response.getConfigutentetiposervizioente().getMailEnte());
		//	request.setAttribute("configutentetiposervizioente_statoAttivo", response.getConfigutentetiposervizioente().getStatoAttivo());

			//fine CD PAGO438
		    String flagPagoPa = "";
		    switch(response.getConfigutentetiposervizioente().getFlagStampaAvvisoPagoPa()) { 
		    case "N" : flagPagoPa = "No"; break;
		    case "Y" : flagPagoPa = "Sì"; break;
		    case "C" : flagPagoPa = "Configurazione"; break;
		    default: flagPagoPa = ""; break;}
		    request.setAttribute("configutentetiposervizioente_flagStampaAvvisoPagoPa",flagPagoPa);
		    
		    //Recuper la mail destinatario sulla PYREETB
		    ConfRendUtenteServizioEnteGetRecordRequestType in = new ConfRendUtenteServizioEnteGetRecordRequestType();
			in.setCodiceSocieta(companyCode);
			in.setCodiceUtente(userCode);
			in.setChiaveEnte(chiaveEnte);
			in.setTipologiaServizio(response.getConfigutentetiposervizioente().getCodiceTipologiaServizio());
		    ConfRendUtenteServizioEnteGetRecordResponseType out = WSCache.confRendUtenteServizioEnteServer.getRecord(in, request);
		    if(out !=null && out.getBean() != null) {
		    	request.setAttribute("configrentutentetiposervizioente_emaildestinatario", out.getBean().getEmailDestinatario());
		    }
		    
		    //fine SB PG210140
			/*
			 * VIENE caricata comunque la DDL del tipo bollettino
			String tipoBollettino = "";
			BollettinoSearchResponse bolSearchResponse = getBollettinoSearchResponse(tipoBollettino, "", 0, 0, "", request);
			request.setAttribute("bollettini", bolSearchResponse.getResponse().getListXml());
			 */
		    boolean bEditInCorso = false;
			String inEdit = (String) request.getSession().getAttribute("inEdit");
			if(inEdit != null && inEdit.length() > 0) {
				bEditInCorso = true;
			}
			request.getSession().setAttribute("inEdit", "S");
			
			if(bEditInCorso) {
				request.setAttribute("configutentetiposervizioente_chiaveTassonomia", request.getAttribute("configutentetiposervizioente_chiaveTassonomia"));
			} else {
				String keyTassonomia = "";
				if(response.getConfigutentetiposervizioente().getChiaveTassonomia() != null) {
					keyTassonomia = response.getConfigutentetiposervizioente().getChiaveTassonomia();
					if(keyTassonomia.equals("0")) {
						keyTassonomia = "";
					}
				}
				request.setAttribute("configutentetiposervizioente_chiaveTassonomia", keyTassonomia);
			}
			
			//gestione causali
			if(causaliOn(request)) {
				if(bSPOM) {
					if(bEditInCorso) {
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
						String causali = response.getConfigutentetiposervizioente().getCausali();
						
						listaCausali = new ArrayList<String>();
						if(causali != null && causali.trim().length() > 0) {
							String [] splitCausali = causali.split(tagCausale);
							for (String appo : splitCausali) {
								if(appo.length() > 0) {
									listaCausali.add(appo);
								}
							}
						}
					}
				} else {
					listaCausali = new ArrayList<String>();
				}
			    request.getSession().setAttribute("listaCausali", listaCausali);
				request.setAttribute("listaCausali", listaCausali);
			}
			
			String urlRendicontazione = "";
			try {
				EnteDetailResponse resEnte = getEnteDetailSearchResponse(companyCode, userCode, chiaveEnte, request);
				
				if(resEnte != null && resEnte.getEnte() != null) {
					urlRendicontazione = resEnte.getEnte().getUrlWebServiceFlussoRendicontazioneNodo();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("ente_urlRendicontazione", urlRendicontazione);
			
			ConfigurazioneModello3 configurazioneModello3 = null;
			try {
				if(bSPOM || tipoBoll.equalsIgnoreCase("CDSM"))
				{
					configurazioneModello3 = selectConfigurazioneModello3(companyCode, userCode, chiaveEnte, "XXX", request);	
				}else
				{
					configurazioneModello3 = selectConfigurazioneModello3(companyCode, userCode, chiaveEnte, codiceTipologiaServizio, request);	
				}
				
				//configurazioneModello3 = selectConfigurazioneModello3(companyCode, userCode, chiaveEnte, codiceTipologiaServizio, request);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(configurazioneModello3 != null) {
				request.setAttribute("modello3_auxDigit", configurazioneModello3.getAuxDigit());
				request.setAttribute("modello3_carattereDiServizio", configurazioneModello3.getCarattereDiServizio());
				request.setAttribute("modello3_idDominio", configurazioneModello3.getCodiceIdentificativoDominio());
				request.setAttribute("modello3_codiceSegregazione", configurazioneModello3.getCodiceSegregazione());
				request.setAttribute("modello3_urlIntegrazione", 	configurazioneModello3.getUrlIntegrazione());
			} else {
				request.setAttribute("modello3_auxDigit", "");
				request.setAttribute("modello3_carattereDiServizio", "");
				request.setAttribute("modello3_idDominio", "");
				request.setAttribute("modello3_codiceSegregazione", "");
				request.setAttribute("modello3_urlIntegrazione", "");
			}
			
			//servizioAttivo
			AbilitaCanalePagamentoTipoServizioEnteSearchResponse searchResponse = null;
			AbilitaCanalePagamentoTipoServizioEnteResponse abilitaCanalePagamentoTipoServizioEnteResponse = null;
			AbilitaCanalePagamentoTipoServizioEnteSearchRequest inAbilita = new AbilitaCanalePagamentoTipoServizioEnteSearchRequest();
			inAbilita.setPageNumber(1);
			inAbilita.setRowsPerPage(20);
			inAbilita.setCompanyCode(companyCode);
			inAbilita.setUserCode(userCode);
			inAbilita.setChiaveEnte(chiaveEnte);
			inAbilita.setCodiceTipologiaServizio(codiceTipologiaServizio);
							
			searchResponse = WSCache.abilitazTributiServer.getAbilitaCanalePagamentoTipoServizioEntes(inAbilita,request);
			abilitaCanalePagamentoTipoServizioEnteResponse = searchResponse.getResponse();
			WebRowSet listaRecord = null;
			listaRecord = Convert.stringToWebRowSet(abilitaCanalePagamentoTipoServizioEnteResponse.getListXml());
			listaRecord.first();
			
			String statoAttivo = "N";
			if (listaRecord != null) 
			{
				while (listaRecord.next()) 
				{
					statoAttivo = listaRecord.getString(6);
					if(statoAttivo.equals("Y")) 
						break; 
				}
			}
			
			if(statoAttivo.equals("Y")) 
				statoAttivo = "ATTIVO"; 
			if(statoAttivo.equals("N")) 
				statoAttivo = "NON ATTIVO";
			
			request.setAttribute("configutentetiposervizioente_statoAttivo", statoAttivo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.UPDT_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			FiredButton firedButtonChanged2 = getFiredButton(request);
			if(firedButtonChanged2.equals(FiredButton.TX_BUTTON_AGGIORNA)) {
				loadDdlChiave(request);
			} else if (firedButton!=null) {
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("configutentetiposervizioente_companyCode") !=null){
						//request.setAttribute("configutentetiposervizioente_companyCode", null);
						//request.setAttribute("configutentetiposervizioente_userCode", null);
						//request.setAttribute("configutentetiposervizioente_codiceTipologiaServizio", null);
						//request.setAttribute("configutentetiposervizioente_chiaveEnte", null);
						request.getSession().setAttribute("inEdit", "");
						ripristinaFiltriDiRicerca(request, request.getSession());
						search(request);
					}
				}
			} else if(firedButtonReset!=null) {
				if(firedButtonReset.equals("Reset")) {
					request.getSession().setAttribute("inEdit", "");
					edit(request);
				}
			} else {
				save(request);
			}
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	private void save(HttpServletRequest request) throws ActionException {
		ConfigUtenteTipoServizioEnteDetailResponse detailResponse = null;
		String codiceUtente = "";
		String chiaveEnte = "";
		String codiceTipologiaServizio = "";
		String companyCode = "";
		String cod = request.getParameter("cod");
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		/*
		String arrStr = request.getParameter("configutentetiposervizioente_strEntetiposervizios");
		String arrStr2 = request.getParameter("configutentetiposervizio_codiceTipologiaServizio");
		if (cod.equals("0")  && arrStr!=null && arrStr.length()>0)
		{
			  String[] strEntetiposervizios = arrStr.split("\\|");
			  companyCode = strEntetiposervizios[0];
 		      codiceUtente= strEntetiposervizios[1];
 		      chiaveEnte= strEntetiposervizios[2];
 		      strEntetiposervizios = arrStr2.split("\\|");
		      codiceTipologiaServizio= strEntetiposervizios[0];
 		     
 		}
		if (cod.equals("1"))
		{
		*/
			  companyCode = request.getParameter("configutentetiposervizioente_companyCode");
              codiceUtente= request.getParameter("configutentetiposervizioente_codiceUtente");
              chiaveEnte= request.getParameter("configutentetiposervizioente_chiaveEnte");
              codiceTipologiaServizio= request.getParameter("configutentetiposervizioente_codiceTipologiaServizio");
		/*
		 }
		*/
	    try {
			detailResponse = getConfigUtenteTipoServizioEnte(companyCode, codiceUtente, chiaveEnte, codiceTipologiaServizio, request);
			
			String numCc = detailResponse.getConfigutentetiposervizioente().getNumCc();
			String intestatarioCc= detailResponse.getConfigutentetiposervizioente().getIntestatarioCc(); 
			String tipoDoc = detailResponse.getConfigutentetiposervizioente().getTipoDoc();
			String flagConRange = detailResponse.getConfigutentetiposervizioente().getFlagConRange();
			String emailDest = detailResponse.getConfigutentetiposervizioente().getEmailDest();
			String emailCcn = detailResponse.getConfigutentetiposervizioente().getEmailCcNascosta();
			String emailMitt= detailResponse.getConfigutentetiposervizioente().getEmailMitt();
			String desMitt = detailResponse.getConfigutentetiposervizioente().getDesMittente();
			String flagAllegato = detailResponse.getConfigutentetiposervizioente().getFlagAllegato();
			String codiceSia = detailResponse.getConfigutentetiposervizioente().getCodiceSia();
			String codiceIban = detailResponse.getConfigutentetiposervizioente().getCodiceIban();
			String codiceSecondoIban = detailResponse.getConfigutentetiposervizioente().getSecondoCodiceIban();
			String funzionePag = detailResponse.getConfigutentetiposervizioente().getFunzionePag();
			String tipoBol = detailResponse.getConfigutentetiposervizioente().getTipoBollettino();
			String flagPagProtetta = detailResponse.getConfigutentetiposervizioente().getFlagPagProtetta();
			String urlServWeb = detailResponse.getConfigutentetiposervizioente().getUrlServWeb();
			String flagTipoPag = detailResponse.getConfigutentetiposervizioente().getFlagTipoPag();
			String flagIntegrazioneSeda = detailResponse.getConfigutentetiposervizioente().getFlagIntegrazioneSeda();
			String codiceUtenteSeda = detailResponse.getConfigutentetiposervizioente().getCodiceUtenteSeda();
			String flagNotificaPagamento = detailResponse.getConfigutentetiposervizioente().getFlagNotificaPagamento();
			String urlServizioWebNotificaPagamento = detailResponse.getConfigutentetiposervizioente().getUrlServizioWebNotificaPagamento();
			String flagPagoPA = detailResponse.getConfigutentetiposervizioente().getFlagPagoPA();
			String tipoDovuto = detailResponse.getConfigutentetiposervizioente().getTipoDovuto();
			String flagStampaPagoPaPdf = detailResponse.getConfigutentetiposervizioente().getFlagStampaAvvisoPagoPa();
			String giorniStampaAvvisoPagoPa = detailResponse.getConfigutentetiposervizioente().getGiorniStampaAvvisoPagoPa();
			String autorizzazioneStampaAvvisoPagoPa = detailResponse.getConfigutentetiposervizioente().getAutorizzazioneStampaAvvisoPagoPa();
		    String cbillStampaAvvisoPagoPa = detailResponse.getConfigutentetiposervizioente().getCbillStampaAvvisoPagoPa();
		    String infoEnteStampaAvvisoPagoPa = detailResponse.getConfigutentetiposervizioente().getInfoEnteStampaAvvisoPagoPa(); 
		    String chiaveTassonomiaOLD = detailResponse.getConfigutentetiposervizioente().getChiaveTassonomia();
		    String strutturaEnte = detailResponse.getConfigutentetiposervizioente().getStrutturaEnte();
		    String strutturaEnteFornitore = detailResponse.getConfigutentetiposervizioente().getStrutturaEnteFornitore();
		    String nomeEnte = detailResponse.getConfigutentetiposervizioente().getNomeEnte();
		    String nomeFornitore = detailResponse.getConfigutentetiposervizioente().getNomeFornitore();
		    String cognomeFornitore = detailResponse.getConfigutentetiposervizioente().getCognomeFornitore();
		    String telefonoFornitore = detailResponse.getConfigutentetiposervizioente().getTelefonoFornitore();
		    String mailFornitore = detailResponse.getConfigutentetiposervizioente().getMailFornitore();
		    String cognomeEnte = detailResponse.getConfigutentetiposervizioente().getCognomeEnte();
		    String telefonoEnte = detailResponse.getConfigutentetiposervizioente().getTelefonoEnte();
		    String mailEnte = detailResponse.getConfigutentetiposervizioente().getMailEnte();
			
		    
		    boolean bAggiorna = false;
		    String chiaveTassonomia = (String) request.getAttribute("configutentetiposervizioente_chiaveTassonomia");
		    if(chiaveTassonomia != null)
		    	chiaveTassonomia = chiaveTassonomia.trim();
		    if(chiaveTassonomia.length() == 0 || chiaveTassonomia.equals("0")) {
		    	//non dovrebbe succedere da interfaccia deve arrivare un valore =/= "0"
		    	chiaveTassonomia = null;
		    }
		    if(chiaveTassonomia != null && !chiaveTassonomia.equalsIgnoreCase(chiaveTassonomiaOLD)) {
		    	bAggiorna = true;
		    }
		    
		    String causaliOLD = detailResponse.getConfigutentetiposervizioente().getCausali();
		    String causali = null;
			if(causaliOn(request)) {
				boolean bSPOM = (tipoBol != null && tipoBol.equalsIgnoreCase("SPOM"));
				causali = "";
				if(bSPOM) {
				    String causalONE = "";
					if(listaCausali == null) {
						listaCausali = (ArrayList<String>) request.getSession().getAttribute("listaCausali");
					}
					if(listaCausali != null) {
					    // righe causali
						int k = listaCausali.size();
					    for (int i = 0; i < k; i++) {
					    	String curr = request.getParameter(String.format("causale_%d_testo", i));
					    	if(curr != null && curr.trim().length() > 0) {
					    		if(causalONE.length() > 0) {
					    			causalONE += tagCausale;
					    		}
					    		causalONE += curr.trim();
					    	}
					    }
					}
					causali = causalONE;
				    if(causali != null && (causaliOLD == null || !causali.equalsIgnoreCase(causaliOLD))) {
				    	bAggiorna = true;
				    }
				}
			}
		    
			addDDL(request);
			
//			if(!bAggiorna) {
//				//non serve eseguire l'aggiornamento
//				//reset parametri gestione causali
//			    listaCausali = new ArrayList<String>();
//			    request.getSession().setAttribute("listaCausali", listaCausali);
//				request.setAttribute("listaCausali", listaCausali);
//			    request.getSession().setAttribute("inEdit", "");
//				return;
//			}
			
			//inizio SB PG210140
			String articolo = "";
			String codiceContabilita = "";
			String capitolo = "";
			String annoCompetenza = "";
			if(tipoBol != null && tipoBol.equalsIgnoreCase("SPOM")) {
				articolo = (String) request.getAttribute("configutentetiposervizioente_articolo");
				codiceContabilita = (String) request.getAttribute("configutentetiposervizioente_codiceContabilita");
				capitolo = (String) request.getAttribute("configutentetiposervizioente_capitolo");
				annoCompetenza = (String) request.getAttribute("configutentetiposervizioente_annoCompetenza");
			}
			
			 String dataDicituraPagamento = (String)request.getAttribute("configutentetiposervizioente_datapagamento")==null?"":(String)request.getAttribute("configutentetiposervizioente_datapagamento"); //SB PG210170
			 //inizio cd pago438
			 strutturaEnte = (String)request.getAttribute("nome_struttura_ente")==null?"":(String)request.getAttribute("nome_struttura_ente"); 
			 strutturaEnteFornitore = (String)request.getAttribute("nome_struttura_ente_rag_soc")==null?"":(String)request.getAttribute("nome_struttura_ente_rag_soc");  
			 nomeEnte = (String)request.getAttribute("nome_ente")==null?"":(String)request.getAttribute("nome_ente");  
			 nomeFornitore = (String)request.getAttribute("Nome_ref_tec")==null?"":(String)request.getAttribute("Nome_ref_tec");  
			 cognomeEnte = (String)request.getAttribute("cognome_ente")==null?"":(String)request.getAttribute("cognome_ente");  
			 cognomeFornitore =(String)request.getAttribute("cognome_ref_tec")==null?"":(String)request.getAttribute("cognome_ref_tec"); 
			 telefonoEnte = (String)request.getAttribute("telefono_ente")==null?"":(String)request.getAttribute("telefono_ente"); 
			 telefonoFornitore = (String)request.getAttribute("Telefono_Referente_Tecnico_Ente")==null?"":(String)request.getAttribute("Telefono_Referente_Tecnico_Ente"); 
			 mailEnte = (String)request.getAttribute("mail_ente")==null?"":(String)request.getAttribute("mail_ente"); 
			 mailFornitore = (String)request.getAttribute("mail_ref_tecnico")==null?"":(String)request.getAttribute("mail_ref_tecnico"); 
			//fine cd pago 438 
			 
			ConfigUtenteTipoServizioEnte configUtente = new ConfigUtenteTipoServizioEnte(companyCode, codiceUtente, chiaveEnte,  codiceTipologiaServizio, tipoBol,
				    numCc, intestatarioCc, tipoDoc, flagConRange,  emailDest, emailCcn,emailMitt, desMitt, flagAllegato, codiceSia,  codiceIban,codiceSecondoIban,
				     funzionePag, flagPagProtetta, urlServWeb, flagTipoPag, flagIntegrazioneSeda, codiceUtenteSeda, usernameAutenticazione, flagNotificaPagamento,
				     urlServizioWebNotificaPagamento, flagPagoPA, tipoDovuto, flagStampaPagoPaPdf, giorniStampaAvvisoPagoPa, autorizzazioneStampaAvvisoPagoPa, cbillStampaAvvisoPagoPa, infoEnteStampaAvvisoPagoPa,
				     chiaveTassonomia, causali, articolo, codiceContabilita, capitolo, annoCompetenza, dataDicituraPagamento,
				     strutturaEnte,strutturaEnteFornitore,nomeEnte,nomeFornitore,cognomeEnte,cognomeFornitore,telefonoEnte,
				     telefonoFornitore,mailEnte,mailFornitore); 
			
			//fine SB PG210140
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
					else {
						//reset parametri gestione causali
					    listaCausali = new ArrayList<String>();
					    request.getSession().setAttribute("listaCausali", listaCausali);
						request.setAttribute("listaCausali", listaCausali);
					    request.getSession().setAttribute("inEdit", "");
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
			{
				request.setAttribute("error", "error"); 
				request.setAttribute("done", null);
				request.setAttribute("message", Messages.INS_ERR.format());
			}
		} catch (Exception e) {
			try {
				if(detailResponse == null) {
					detailResponse = getConfigUtenteTipoServizioEnte(companyCode,codiceUtente,chiaveEnte,codiceTipologiaServizio, request);
				}
				
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
				//inizio cd Pago438
				request.setAttribute("nome_struttura_ente", detailResponse.getConfigutentetiposervizioente().getStrutturaEnte());
				request.setAttribute("nome_struttura_ente_rag_soc", detailResponse.getConfigutentetiposervizioente().getStrutturaEnteFornitore());
				request.setAttribute("nome_ente", detailResponse.getConfigutentetiposervizioente().getNomeEnte());
				request.setAttribute("Nome_ref_tec", detailResponse.getConfigutentetiposervizioente().getNomeFornitore());
				request.setAttribute("cognome_ref_tec", detailResponse.getConfigutentetiposervizioente().getCognomeFornitore());
				request.setAttribute("Telefono_Referente_Tecnico_Ente", detailResponse.getConfigutentetiposervizioente().getTelefonoFornitore());
				request.setAttribute("mail_ref_tecnico", detailResponse.getConfigutentetiposervizioente().getMailFornitore());
				request.setAttribute("cognome_ente", detailResponse.getConfigutentetiposervizioente().getCognomeEnte());
				request.setAttribute("telefono_ente", detailResponse.getConfigutentetiposervizioente().getTelefonoEnte());
				request.setAttribute("mail_ente", detailResponse.getConfigutentetiposervizioente().getMailEnte());
				
				//fine cd pago438
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERR.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
		
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
	
	private ConfigUtenteTipoServizioEnteDetailResponse getConfigUtenteTipoServizioEnte(String companyCode, String codiceUtente, String chiaveEnte, String codiceTipologiaServizio, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
	{
		ConfigUtenteTipoServizioEnteDetailRequest in = new ConfigUtenteTipoServizioEnteDetailRequest(companyCode, codiceUtente, chiaveEnte, codiceTipologiaServizio);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setUserCode(codiceUtente == null ? "" : codiceUtente);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		
		return WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEnte(in, request);
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
	
	private ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEnteSearchResponse3(
			ConfigUtenteTipoServizioEnteSearchRequest3 configUtenteTipoServizioEnteSearchRequest3, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteSearchRequest3 in = new ConfigUtenteTipoServizioEnteSearchRequest3();
		in.setCompanyCode(configUtenteTipoServizioEnteSearchRequest3.getCompanyCode() == null ? "" : configUtenteTipoServizioEnteSearchRequest3.getCompanyCode());
		
		res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes3(in, request);
		return res;
	}
	
	private void loadDdlChiave(HttpServletRequest request) throws FaultType, RemoteException
	{
		/*
		ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse = getConfigUtenteTipoServizioEntes2(userCodiceSocieta,"","","", request);
		request.setAttribute("entetiposervizios", configUtenteTipoServizioEnteSearchResponse.getResponse().getListXml());
		*/	
		ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse2 = null;
		configUtenteTipoServizioEnteSearchResponse2 = getConfigUtenteTipoServizioEnteSearchResponse3((new ConfigUtenteTipoServizioEnteSearchRequest3(request.getParameter("configutentetiposervizioente_companyCode"))), request);
		request.setAttribute("utentetiposervizios2", configUtenteTipoServizioEnteSearchResponse2.getResponse().getListXml());

		String tipoBollettino = "";
		BollettinoSearchResponse bolSearchResponse = getBollettinoSearchResponse(tipoBollettino, "", 0, 0, "", request);
		request.setAttribute("bollettini", bolSearchResponse.getResponse().getListXml());
		
		if(request.getAttribute("tassonomie") == null) {
			ConfigTassonomiaListaResponse configTassonomiaListaResponse = getTassonomiaListaResponse(request);
			request.setAttribute("tassonomie", configTassonomiaListaResponse.getListaTassonomia().getListXml());
		}
	}
	
	private void salvaFiltriDiRicerca(HttpServletRequest request, HttpSession session) {
		svuotaFiltriDiRicercaDaSession(session);
		//session.setAttribute("configutentetiposervizioente_desSoc", request.getAttribute("configutentetiposervizioente_desSoc")!=null?request.getAttribute("configutentetiposervizioente_desSoc"):"");
		//session.setAttribute("configutentetiposervizioente_desUte", request.getAttribute("configutentetiposervizioente_desUte")!=null?request.getAttribute("configutentetiposervizioente_desUte"):"");
		//session.setAttribute("configutentetiposervizioente_desEnte", request.getAttribute("configutentetiposervizioente_desEnte")!=null?request.getAttribute("configutentetiposervizioente_desEnte"):"");
		//session.setAttribute("configutentetiposervizioente_desTp", request.getAttribute("configutentetiposervizioente_desTp")!=null?request.getAttribute("configutentetiposervizioente_desTp"):"");
		String strSocieta = (String) request.getAttribute("tx_societa");
		String strProv = (String) request.getAttribute("tx_provincia");
		String strUtenteEnte = (String) request.getAttribute("tx_UtenteEnte");
		String strServizio = (String) request.getAttribute("tx_tipologia_servizio");
		session.setAttribute("tx_societa", strSocieta != null ? strSocieta : "");
		session.setAttribute("tx_provincia", strProv != null ? strProv : "");
		session.setAttribute("tx_UtenteEnte", strUtenteEnte != null ? strUtenteEnte : "");
		session.setAttribute("tx_tipologia_servizio",  strServizio != null ? strServizio : "");
	}

	private HttpServletRequest ripristinaFiltriDiRicerca(HttpServletRequest request, HttpSession session) {
		/*
		request.setAttribute("configutentetiposervizioente_desSoc", session.getAttribute("configutentetiposervizioente_desSoc"));
		request.setAttribute("configutentetiposervizioente_desUte", session.getAttribute("configutentetiposervizioente_desUte"));
		request.setAttribute("configutentetiposervizioente_desEnte", session.getAttribute("configutentetiposervizioente_desEnte"));
		request.setAttribute("configutentetiposervizioente_desTp", session.getAttribute("configutentetiposervizioente_desTp"));
		*/
		request.setAttribute("tx_societa", session.getAttribute("tx_societa"));
		request.setAttribute("tx_provincia", session.getAttribute("tx_provincia"));
		request.setAttribute("tx_UtenteEnte", session.getAttribute("tx_UtenteEnte"));
		request.setAttribute("tx_tipologia_servizio", session.getAttribute("tx_tipologia_servizio"));
		svuotaFiltriDiRicercaDaSession(session);
		return request;
	}
	
	private void svuotaFiltriDiRicercaDaSession(HttpSession session) {
		/*
		session.setAttribute("configutentetiposervizioente_desSoc",null);
		session.setAttribute("configutentetiposervizioente_desUte",null);
		session.setAttribute("configutentetiposervizioente_desEnte",null);
		session.setAttribute("configutentetiposervizioente_desTp",null);
		 */
		session.setAttribute("tx_societa", null);
		session.setAttribute("tx_provincia", null);
		session.setAttribute("tx_UtenteEnte", null);
		session.setAttribute("tx_tipologia_servizio", null);
	}

	/*
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
	*/
	private ConfigurazioneModello3 selectConfigurazioneModello3(String companyCode, String userCode, String chiaveEnte, String codiceTipologiaServizio, HttpServletRequest request) throws DaoException 
	{
		ConfigurazioneModello3Dao configurazioneModello3DAO;
		ConfigurazioneModello3 configurazioneModello3 = new ConfigurazioneModello3();
		configurazioneModello3.setCodiceSocieta(companyCode);
		configurazioneModello3.setCodiceUtente(userCode);
		configurazioneModello3.setChiaveEnte(chiaveEnte);
		configurazioneModello3.setCodiceIdentificativoDominio("");
		configurazioneModello3.setAuxDigit("");
		configurazioneModello3.setCodiceSegregazione("");
		configurazioneModello3.setCarattereDiServizio("");
		configurazioneModello3.setUrlIntegrazione("");
		configurazioneModello3.setTipologiaServizio(codiceTipologiaServizio);	//SB PG210140  //"XXX"
		if(this.configurazioneModello3DataSource != null) {
			//inizio LP PG21XX04 Leak
			//configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(this.configurazioneModello3DataSource, this.configurazioneModello3DbSchema);
			//configurazioneModello3 = configurazioneModello3DAO.select(configurazioneModello3);
			Connection conn = null;
			try {
				conn = this.configurazioneModello3DataSource.getConnection();
				configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(conn, this.configurazioneModello3DbSchema);
				configurazioneModello3 = configurazioneModello3DAO.select(configurazioneModello3);
			} catch (Exception e) {
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
		return configurazioneModello3;
	}
	
	private EnteDetailResponse getEnteDetailSearchResponse(String companyCode, String userCode, String chiaveEnte, HttpServletRequest request) throws FaultType, RemoteException
	{
		 
		EnteDetailResponse res = null;
		EnteDetailRequest in = new EnteDetailRequest(companyCode, userCode, chiaveEnte);
		
		res = WSCache.enteServer.getEnte(in, request);
		return res;
	}

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
	
	//inizio SB PG210140
	private DownloadServiziAttiviCsvResponse dowloadServiziAttiviCsv(String societa, String utente, String ente, String tipologiaServizio, HttpServletRequest request) throws FaultType, RemoteException {
		DownloadServiziAttiviCsvRequest recuperaTransazioniRequest = new DownloadServiziAttiviCsvRequest(societa, utente, ente,tipologiaServizio);
		return WSCache.configUtenteTipoServizioEnteServer.getServiziAttiviDownloadCsv(recuperaTransazioniRequest, request); 
	}
	
	public String download(HttpServletRequest request) throws ActionException {
		String strSocieta = null;
		String strEnte = null;
		String strUtente = null;
		String strTipologiaServizio = null;
		String strCodSocieta = null;  

		strCodSocieta = (userCodiceSocieta != "" ? userCodiceSocieta : strSocieta);  
		try {
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButtonReset!=null){				
				resetParametri(request);		
			}
			
			DownloadServiziAttiviCsvResponse response = null;
			if (firedButton != null) {
				 if(firedButton.equals("Indietro")) {
					
					 if(strUtente == null) {
						strUtente = (String) request.getAttribute("tx_UtenteEnte");
						if(strUtente != null && strUtente.length() > 10) {
							strUtente = strUtente.substring(5, 10); 
						}
					 }
					 if(strSocieta == null) {
						 strSocieta = (String) request.getAttribute("tx_societa");
						 
					 }
					
					if(strEnte == null) {
						strEnte = (String) request.getAttribute("tx_UtenteEnte");
						if(strEnte != null && strEnte.length() > 10) {
							strEnte = strEnte.substring(10); 
						}
					}
					strTipologiaServizio = this.strTipologiaServizio != null ? this.strTipologiaServizio : ""; 
					strCodSocieta = (userCodiceSocieta != "" ? userCodiceSocieta : strSocieta);
					response =dowloadServiziAttiviCsv(strCodSocieta, strUtente, strEnte, strTipologiaServizio, request);
				 }
			}
			if (firedButton == null) {
				strCodSocieta = (userCodiceSocieta != "" ? userCodiceSocieta : strCodiceSocieta);  
				strTipologiaServizio = this.strTipologiaServizio != null ? this.strTipologiaServizio : ""; 
				response =dowloadServiziAttiviCsv(strCodSocieta, strCodiceUtente, strChiaveEnte, strTipologiaServizio, request);
			}
				
			return response.getFile();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//fine SB PG210140
	
	
}