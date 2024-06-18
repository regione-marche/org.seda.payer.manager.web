package org.seda.payer.manager.ecmanager.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.ecmanager.actions.util.NotSel;
import org.seda.payer.manager.ecmanager.actions.util.Notifica;
import org.seda.payer.manager.ecmanager.actions.util.NotificaResponseWrapper;
import org.seda.payer.manager.ecmanager.actions.util.NotificatoreHelper;
import org.seda.payer.manager.ecmanager.actions.util.Notifiche;
import org.seda.payer.manager.ecmanager.actions.util.NotificheLis;
import org.seda.payer.manager.ecmanager.actions.util.NotificheResponseWrapper;
import org.seda.payer.manager.ecmanager.actions.util.ReinvioNotifica;
import org.seda.payer.manager.entrate.actions.util.EntrateUtils;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.seda.commons.logger.CustomLoggerManager;
import com.seda.commons.logger.LoggerWrapper;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.AnagraficaBollettino;
import com.seda.payer.core.bean.EcNotifichePageList;
import com.seda.payer.core.bean.ScadenzaUrlNotifica;
import com.seda.payer.core.dao.AnagraficaBollettinoDAO;
import com.seda.payer.core.dao.AnagraficaBollettinoDAOFactory;
import com.seda.payer.core.dao.ScadenzaUrlNotificaDAOFactory;
import com.seda.payer.core.dao.ScadenzaUrlNotificaDao;
import com.seda.payer.core.exception.DaoException;
import com.sun.rowset.WebRowSetImpl;

public class EcNotificheListaAction extends AnaBollettinoManagerBaseManagerAction {
	private static final long serialVersionUID = 1L;
	private static LoggerWrapper log = CustomLoggerManager.get(EcNotificheListaAction.class);
	private String codiceSocieta="";
	private String cutecute="";
	private String chiaveEnte="";
	private String codiceEnte="";
	private String descEnte="";
	private PropertiesTree configuration;
	private String dbSchema;
	//inizio LP PG21XX04
	//private ScadenzaUrlNotificaDao scadenzaUrlNotificaDao;
	//private AnagraficaBollettinoDAO anagraficaBollettinoDao;
	//fine LP PG21XX04
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //PREJAVA18_LUCAP_03082020
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		try
		{	 
			String template = getTemplateCurrentApplication(request, request.getSession()); //PREJAVA18_LUCAP_15072020
			
			configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
			
			this.setErrorMessage("");

			HttpSession session = request.getSession();
			FiredButton firedButton = getFiredButton(request);
			if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
				setProfile(request);
			
			
			loadStaticXml_DDL(request, session);
			loadSocietaUtenteEnteXml_DDL(request, session);
			
			if (session.getAttribute("vista")!=null){
				if (session.getAttribute("vista").equals("anagraficanotifiche")){
					firedButton=FiredButton.TX_BUTTON_CERCA;
					if (session.getAttribute("tx_error_message")!=null)
						this.setErrorMessage(session.getAttribute("tx_error_message").toString());
					if (session.getAttribute("tx_message")!=null)
						this.setMessage(session.getAttribute("tx_message").toString());
					session.setAttribute("tx_error_message",null);
					session.setAttribute("tx_message",null);
					session.setAttribute("vista",null);

					if(session.getAttribute("pageNumber_")!=null)
						request.setAttribute("pageNumber",session.getAttribute("pageNumber_"));

				}
			}
			
			switch(firedButton)
			{
				case TX_BUTTON_CERCA: 
					try {
							dividiSocUtenteEnte(request);
							EcNotifichePageList anags = getLista(request);
							
							PageInfo pageInfo = anags.getPageInfo();
							
							//PREJAVA18_LUCAP_06082020
							String newXml = formattaDataddmmyyyy(anags.getEcnotificheListXml());
							if(newXml!=null) {
								anags.setEcnotificheListXml(newXml);
							}
							//FINE PREJAVA18_LUCAP_06082020
							
							if (anags.getRetCode()!="00") {
								setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
							} else {
								if (pageInfo != null) {
									if (pageInfo.getNumRows() > 0) {
										session.setAttribute("numRows",pageInfo.getNumRows()); 
										request.setAttribute("lista_notifiche", anags.getEcnotificheListXml());
										request.setAttribute("lista_notifiche.pageInfo", pageInfo);
									} else {
										setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
									}
								}
							}

						} catch(Exception e) {
							setErrorMessage(e.getMessage());
							e.printStackTrace();
						}
						break;

					
				case TX_BUTTON_RESET:
					resetParametri(request);
					resetSession(session);
					resetFiltriDiRicerca(request); //PREJAVA18_LUCAP_03082020
					break;
					
				case TX_BUTTON_SOCIETA_CHANGED:
					break;
					
				case TX_BUTTON_PROVINCIA_CHANGED:
					break;
				
				case TX_BUTTON_ENTE_CHANGED:
					break;
					
					
				case TX_BUTTON_NULL: 
					resetSession(session);
					break;
					
				case TX_BUTTON_STAMPA:
					break;
					
				case TX_BUTTON_DOWNLOAD:
					
					Notifica not = getDetail(request);
					
					byte[] decodedBytes = Base64.getDecoder().decode(not.getPdfFileBase64());

					
					
					String filePath = (String)context.getAttribute(ManagerKeys.NOTIFICHE_PDF_ROOT);	
					String fileName = not.getCodiceUtente()+"_"+(String)request.getAttribute("chiaveTabellaNotifica")+".pdf";	
					String filePathName = filePath+"/"+fileName;
					File file = new File(filePathName);
					FileOutputStream fop = new FileOutputStream(file);

					fop.write(decodedBytes);
					fop.flush();
					fop.close();
					
					request.setAttribute("filename", fileName);
					request.setAttribute("filePath", filePathName);
					request.setAttribute("download", "OK");

					break;
					
					
					//					try {
//						request.setAttribute("download", "Y");
//						dividiSocUtenteEnte(request);
//						loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
//						
//						/* Tolto il controllo sul numero di righe di download TK 2015100188000055 di Soris perch� query di estrazione non complessa
//						Integer maxRigheDownload = (Integer) context.getAttribute(ManagerKeys.MAXRIGHE_DOWNLOAD_BORSELLINO);
//						if ((Integer) session.getAttribute("numRows") <= maxRigheDownload ){
//							getLista(request,true);
//						} else {
//							setFormMessage("form_selezione", Messages.SUPERATO_MAXRIGHE.format(), request);
//							request.setAttribute("download", "N");
//							break;
//						}
//						*/
//						getLista(request,true);
//					} catch(Exception e) {
//					}
//						setErrorMessage(e.getMessage());
//						e.printStackTrace();
					
				case TX_BUTTON_RINOTIFICA:
					
					ReinvioNotifica rNotifica = new ReinvioNotifica();
					try {
						
						//Recuperto la notifica dal servizio REST
						Notifica notifica = getDetail(request);
						
						//Recupero la notifica dal DB
						String numeroDocumento = (String)request.getAttribute("numerodocumento");
						ScadenzaUrlNotifica urlNotifica = getScadenzaUrlNotificaFromDB(request,notifica.getCodiceUtente(), "00000", notifica.getCodiceFiscale(), numeroDocumento );
						boolean scaduto = false;
						if(urlNotifica != null){
							Calendar cal = Calendar.getInstance();
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							System.out.println("Data scadenzaOld: "+ dateFormat.format(urlNotifica.getDataScadenza().getTime()));
							System.out.println(cal.compareTo(urlNotifica.getDataScadenza()));
							//Se il link di download � scaduto, incremento la data di scadenza di altri 7 giorni
							if(cal.compareTo(urlNotifica.getDataScadenza())>0){
								scaduto=true;
								cal.add(Calendar.DATE, 7);
								String newDataScadenza = dateFormat.format(cal.getTime());
								urlNotifica.setDataScadenza(cal);
								rNotifica.setAttachmentEndValidityDate(newDataScadenza);
							}else{
								rNotifica.setAttachmentEndValidityDate(dateFormat.format(urlNotifica.getDataScadenza().getTime()));
							}
						}else{
							rNotifica.setAttachmentEndValidityDate("");
						}
						
						//Recupero il codiceSocieta
						//String codiceSocieta = getCodiceSocietaFromDB(notifica); 
						String codiceSocieta = "000TR";
						
						//Recupero la mail dall'anagrafica di contatto
						AnagraficaBollettino anagrafica = getAnagraficaDiContatto(notifica.getCodiceFiscale(), notifica.getCodiceUtente(), codiceSocieta);
						//Se la mail � presente nell'anagrafica di contatto uso quella, altrimenti quella della notifica
						if(anagrafica!=null){
							if(notifica.getFlagTipoMessaggio().equals("M")){
								if(anagrafica.getMail()!=null && !anagrafica.getMail().trim().equals("")){
									notifica.setDestinatario(anagrafica.getMail());
									notifica.setConoscenzaNascosta(anagrafica.getMail());
								}
							}
							else if(notifica.getFlagTipoMessaggio().equals("P")){
								if(anagrafica.getMailPec()!=null && !anagrafica.getMailPec().trim().equals("")){
									notifica.setDestinatario(anagrafica.getMailPec());
									notifica.setConoscenzaNascosta(anagrafica.getMailPec());
								}
							}
						}

						rNotifica.setChiaveNotifica((String)request.getAttribute("chiaveTabellaNotifica"));
						rNotifica.setMessageType((String)request.getAttribute("tipoInvio"));
						rNotifica.setReceiver(notifica.getDestinatario());
						rNotifica.setProperties(getProperties(notifica));
						
						String urlNotificatore = configuration.getProperty(ManagerKeys.NOTIFICHE_URL);
						String operationNotificatore = "/gestioneNotifiche/notifiche/reinvio.json";		
						String bearer = (String) context.getAttribute(ManagerKeys.NOTIFICHE_BEARER);
						
						if(configuration.getProperty(PropertiesPath.notifiche_Url.format().concat("."+ notifica.getCodiceUtente()))!=null)
							urlNotificatore =  configuration.getProperty(PropertiesPath.notifiche_Url.format().concat("."+ notifica.getCodiceUtente()));
						
						if(configuration.getProperty(PropertiesPath.notifiche_Bearer.format().concat("."+ notifica.getCodiceUtente()))!=null)
							bearer =configuration.getProperty(PropertiesPath.notifiche_Bearer.format().concat("."+ notifica.getCodiceUtente()));
						
						System.out.println("Chiamata a servizio json " + urlNotificatore.concat(operationNotificatore));
				
						Notifiche notifiche = new Notifiche();
						List<ReinvioNotifica> lst = new ArrayList<ReinvioNotifica>();
						lst.add(rNotifica);
						notifiche.setNotifiche(lst);
								
						NotificatoreHelper notificatore = new NotificatoreHelper(urlNotificatore.trim(), operationNotificatore, false, "", 0);
						ResponseEntity<NotificheResponseWrapper> rssResponse = notificatore.reSendNotifica(notifiche, bearer);
						if (rssResponse!=null)
							System.out.println( "Esito chiamata a servizio json: " + rssResponse.toString());
						NotificheResponseWrapper out = rssResponse.getBody();
						if(out!=null){
							System.out.println( "Status: " + out.getWebServiceOutput().getStatus());
							System.out.println("ErrorCode: " + out.getWebServiceOutput().getErrorCode());
						}
						if(rssResponse.getStatusCode()==HttpStatus.OK){
							System.out.println("res: OK");
							if(urlNotifica!=null && scaduto){
								//inizio LP PG21XX04 Leak
								//Integer res = scadenzaUrlNotificaDao.update(urlNotifica);
								Integer res = updateScadenzaUrlNotifica(urlNotifica,notifica.getCodiceUtente());
								//fine LP PG21XX04 Leak
								if(res>0){
									SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
									String formatted = format1.format(urlNotifica.getDataScadenza().getTime());
									System.out.println("Aggiornamento Record scadenza effettuato. Nuova data scadeza: "+ formatted);
									session.setAttribute("tx_message", "Notifica inviata con successo. Data scadenza Url download aggiornata al "+formatted);
								}
							}else{
								session.setAttribute("tx_message", "Notifica inviata con successo.");
								System.out.println("Notifica inviata con successo.");
							}
						}
						this.setMessage(session.getAttribute("tx_message").toString());

						//PREJAVA18_LUCAP_15072020
						if(template.equals("trentrisc")) {
							dividiSocUtenteEnte(request);
							EcNotifichePageList anags = getLista(request);
							PageInfo pageInfo = anags.getPageInfo();
							if (anags.getRetCode()!="00") {
								setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
							} else {
								if (pageInfo != null) {
									if (pageInfo.getNumRows() > 0) {
										session.setAttribute("numRows",pageInfo.getNumRows()); 
										request.setAttribute("lista_notifiche", anags.getEcnotificheListXml());
										request.setAttribute("lista_notifiche.pageInfo", pageInfo);
									} else {
										setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
									}
								}
							}
						}
						//FINE PREJAVA18_LUCAP_15072020
						
					} catch (Throwable e) {
						
						System.out.println("Errore in chiamata a servizio json: " + e.getCause());
						throw new Exception("Errore in chiamata a servizio json " + e.getCause());
					}
					
					break;
					
				
				
			}
			
			request.setAttribute("tx_error_message", this.getErrorMessage());
			request.setAttribute("tx_message", this.getMessage());
			return null;
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void resetSession(HttpSession session) {
		session.setAttribute("ddlSocietaUtenteEnte",null);
		session.setAttribute("tx_codfisc",null);
		session.setAttribute("tx_numdocumento",null);
		session.setAttribute("tx_email",null);
		session.setAttribute("tx_emailpec",null);
		session.setAttribute("tx_sms",null);
		session.setAttribute("tx_tiponotifica",null);
		session.setAttribute("tx_tipologiaservizio",null);
		session.setAttribute("data_invio_da",null);
		session.setAttribute("data_invio_a",null);
		session.setAttribute("rowsPerPage",null);
		session.setAttribute("order",null);
	}
	
	//PREJAVA18_LUCAP_03082020
	private void resetFiltriDiRicerca(HttpServletRequest request) {
		request.setAttribute("ddlSocietaUtenteEnte",null);
		request.setAttribute("tx_codfisc",null);
		request.setAttribute("tx_numdocumento",null);
		request.setAttribute("tx_email",null);
		request.setAttribute("tx_emailpec",null);
		request.setAttribute("tx_sms",null);
		request.setAttribute("tx_tiponotifica",null);
		request.setAttribute("tx_tipologiaservizio",null);
		request.setAttribute("data_invio_da",null);
		request.setAttribute("data_invio_a",null);
		request.setAttribute("rowsPerPage",null);
		request.setAttribute("order",null);
	}
	//FINE PREJAVA18_LUCAP_03082020

	private EcNotifichePageList getLista(HttpServletRequest request) {
		 
		HttpSession session = request.getSession(); //PREJAVA18_LUCAP_15072020
		
		EcNotifichePageList ecnpl = new EcNotifichePageList();
		PageInfo pageInfo = new PageInfo();
		
		NotSel notSel = new NotSel();

		notSel.setCodiceSocieta((String) context.getAttribute(ManagerKeys.NOTIFICHE_SOCIETA+"."+(String)request.getAttribute("tx_utente")));
		if(notSel.getCodiceSocieta()!=null && !notSel.getCodiceSocieta().equals("")){
			notSel.setCodiceUtente(notSel.getCodiceSocieta().substring(3,5));
		}
		
		if(notSel.getCodiceSocieta()==null || notSel.getCodiceSocieta().equals("")){
			String codSoc = (String)request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
			notSel.setCodiceSocieta(codSoc.substring(3,5));
		}
		
		notSel.setCodiceUtente((String)request.getAttribute("tx_utente"));
		if(notSel.getCodiceUtente()==null || notSel.getCodiceUtente().equals("")){
			notSel.setCodiceUtente((String)request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA));
		}
		
		// filtri della JSP
		notSel.setCodiceEnte((String)request.getAttribute("tx_ente"));
		//PREJAVA18_LUCAP_15072020
		notSel.setCodiceFiscale((request.getAttribute("tx_codfisc")==null?(session.getAttribute("tx_codfisc")==null?"":session.getAttribute("tx_codfisc")):request.getAttribute("tx_codfisc")).toString().toUpperCase());
		notSel.setNumeroDocumento((request.getAttribute("tx_numdocumento")==null?(session.getAttribute("tx_numdocumento")==null?"":session.getAttribute("tx_numdocumento")):request.getAttribute("tx_numdocumento")).toString());
		notSel.setEmail((request.getAttribute("tx_email")==null?(session.getAttribute("tx_email")==null?"":session.getAttribute("tx_email")):request.getAttribute("tx_email")).toString());
		notSel.setEmailPec((request.getAttribute("tx_emailpec")==null?(session.getAttribute("tx_emailpec")==null?"":session.getAttribute("tx_emailpec")):request.getAttribute("tx_emailpec")).toString());
		notSel.setNumTelefono((request.getAttribute("tx_sms")==null?(session.getAttribute("tx_sms")==null?"":session.getAttribute("tx_sms")):request.getAttribute("tx_sms")).toString());
		notSel.setTipologiaNotifica((request.getAttribute("tx_tiponotifica")==null?(session.getAttribute("tx_tiponotifica")==null?"":session.getAttribute("tx_tiponotifica")):request.getAttribute("tx_tiponotifica")).toString());
		notSel.setTipologiaServizio((request.getAttribute("tx_tipologiaservizio")==null?(session.getAttribute("tx_tipologiaservizio")==null?"":session.getAttribute("tx_tipologiaservizio")):request.getAttribute("tx_tipologiaservizio")).toString());
		notSel.setDataNotificaDa(EntrateUtils.dateYYYYMMDDFromFields(request, "data_invio_da"));
		notSel.setDataNotificaA(EntrateUtils.dateYYYYMMDDFromFields(request, "data_invio_a"));
		//FINE PREJAVA18_LUCAP_15072020
		
		// paginazione
		String rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? ((Integer)getDefaultListRows(request)).toString() : (String)request.getAttribute("rowsPerPage"); 		
		notSel.setRowsPerPage(rowsPerPage); 		
		Integer pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		notSel.setPageNumber(pageNumber.toString());
		
		// ordinamento
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		
		
		NotificheLis notificheLis = new NotificheLis();
		
//			String urlNotificatore = "http://10.10.80.12:8280/notificatore-service";
//			String bearer = "Bearer eyJ0eXAiOiAiSldUIiwiYWxnIjogIkhTMjU2In0=.eyAiaXNzIjogIkRZVmlXbVlwdlUrTFRpWHN0SVlMZ2dpWDZYTy9ScnlRa3l2TERYUnJjMUE9IDY0NzcyNTZmLThmOWEtNGNkMi1hMWY4LWRiOGIyMDgyNDZkOSIsImp0aSI6IjcrZzlKazlyNFNDSmxIU3VrTTJQeDFWaU9MS3J1aU9BQldlYnNUQ0E5QVRvSDBacHUrUzVsbE1JMjc1ZDBLZDJzbVJiSlNGNUNUVWJwT0FLSUp3L2k5dWxQbStzNGNQU3ZrU1JadGtrZkxjPSIsImV4cCI6IjI1MzQ1MDU1MTYwMDAwMCJ9.Qz1vdxlG9uCu+q14/4mYvZSifhKnFCECHzRP0vWea/w=";
		String urlNotificatore = (String) context.getAttribute(ManagerKeys.NOTIFICHE_URL);
		String bearer = (String) context.getAttribute(ManagerKeys.NOTIFICHE_BEARER);
		
		String operationNotificatore = "/gestioneNotifiche/cerca2.json";
		//inizio LP PG21XX04 Leak
		WebRowSetImpl resulSet = null;
		//fine LP PG21XX04 Leak
		try {			
			NotificatoreHelper notificatore = new NotificatoreHelper(urlNotificatore, operationNotificatore, false, "", 0);

			ResponseEntity<NotificheResponseWrapper> rssResponse = notificatore.listNotifiche(notificheLis,bearer,getParameterSel(notSel,order,request)); //PREJAVA18_LUCAP_03082020 - aggiunta request
			if (rssResponse!=null)
				System.out.println("Esito chiamata a servizio json: " + rssResponse.toString());
			NotificheResponseWrapper out = rssResponse.getBody();
			if(out!=null){
				System.out.println("Status: " + out.getWebServiceOutput().getStatus());
				System.out.println("ErrorCode: " + out.getWebServiceOutput().getErrorCode());
			}
			if(rssResponse.getStatusCode()==HttpStatus.OK){
//				pageInfo = new PageInfo();
				pageInfo.setPageNumber(pageNumber);
				pageInfo.setRowsPerPage(Integer.parseInt(rowsPerPage));
				
				pageInfo.setFirstRow((pageNumber * Integer.parseInt(rowsPerPage))-Integer.parseInt(rowsPerPage)+1);
				pageInfo.setLastRow(rssResponse.getBody().getWebServiceOutput().getTotalRows()<(pageNumber * Integer.parseInt(rowsPerPage))?rssResponse.getBody().getWebServiceOutput().getTotalRows():(pageNumber * Integer.parseInt(rowsPerPage)));
				pageInfo.setNumRows(rssResponse.getBody().getWebServiceOutput().getTotalRows());
				Double numPages = (double)(pageInfo.getNumRows() / (double)pageInfo.getRowsPerPage());
				
				if ((numPages.intValue() * (double)pageInfo.getRowsPerPage()) < pageInfo.getNumRows()){
					pageInfo.setNumPages(numPages.intValue()+1);
				} else {
					pageInfo.setNumPages(numPages.intValue());
				}
				
				System.out.println("PageInfo ");
				System.out.println(" pageNumber : " + pageInfo.getPageNumber());
				System.out.println(" rowsPerPage: " + pageInfo.getRowsPerPage());
				System.out.println(" FirstRow   : " + pageInfo.getFirstRow());
				System.out.println(" LastRow    : " + pageInfo.getLastRow());
				System.out.println(" NumRows    : " + pageInfo.getNumRows());
				System.out.println(" NumPages   : " + pageInfo.getNumPages());
				
//				ecnpl = new EcNotifichePageList();
				ecnpl.setPageInfo(pageInfo);
				ecnpl.setRetCode("00");
				//inizio LP PG21XX04 Leak
				//WebRowSetImpl resulSet = convertRssResponseToWebRowSet(rssResponse);
				resulSet = convertRssResponseToWebRowSet(rssResponse);
				//fine LP PG21XX04 Leak
				
				ecnpl.setEcnotificheListXml(Convert.webRowSetToString(resulSet));
			}

		} catch (Throwable e) {
			System.out.println("Errore in chiamata a servizio json: " + e.getCause());
		}
		//inizio LP PG21XX04 Leak
		finally {
	    	try {
	    		if(resulSet != null) {
	    			resulSet.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return ecnpl;
	}
	
	private WebRowSetImpl convertRssResponseToWebRowSet(ResponseEntity<NotificheResponseWrapper> rssResponse) {

		int rssSice = rssResponse.getBody().getWebServiceOutput().getElementList().size();
		if (rssSice>0){
	
				try {
					WebRowSetImpl rowSet = new WebRowSetImpl();
					RowSetMetaDataImpl rsMdData = new RowSetMetaDataImpl();
					// setto le colonne del presenti
					//int iNumCols = 44;
					int iNumCols = 14;
					rsMdData.setColumnCount(iNumCols);
					for (int i=1; i<=iNumCols; i++)
						rsMdData.setColumnType(i, Types.VARCHAR);
					
					rowSet.setMetaData(rsMdData);
					
					// ciclo 
					// muovo il puntatore all'inizio
					rowSet.beforeFirst();
					rowSet.moveToInsertRow();
					String chiaveTabellaNotifica = "";
					String codiceEnte="";
					String codiceSocieta="";
					String codiceUtente="";
					String codiceFiscale="";
					String tipologiaNotifica="";
					String tipologiaServizio="";
					String numeroDocumento="";
					String email="";
					String emailPec="";
					String numTelefono="";
					String esitoNotifica="";
					String dataRicezione="";
					String dataInvio="";
					
					for (int i=0;i<rssSice;i++) {
						
						chiaveTabellaNotifica = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getChiaveTabellaNotifica();
						codiceEnte = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getCodiceEnte();
						codiceSocieta = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getCodiceSocieta();
						codiceUtente = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getCodiceUtente();
						codiceFiscale = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getCodiceFiscale();
						tipologiaNotifica = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getTipologiaNotifica();
						tipologiaServizio = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getTipologiaServizio();
						numeroDocumento = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getNumeroDocumento();
						email = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getEmail();
						emailPec = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getEmailPec();
						numTelefono = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getNumTelefono();
						esitoNotifica = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getEsitoNotifica();
						dataRicezione = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getDataRicezione();
						dataInvio = rssResponse.getBody().getWebServiceOutput().getElementList().get(i).getDataInvio();

						rowSet.updateString(1,chiaveTabellaNotifica==null?"":chiaveTabellaNotifica);
						rowSet.updateString(2,codiceEnte==null?"":codiceEnte);
						rowSet.updateString(3,codiceSocieta==null?"":codiceSocieta);
						rowSet.updateString(4,codiceUtente==null?"":codiceUtente);
						rowSet.updateString(5,codiceFiscale==null?"":codiceFiscale);
						rowSet.updateString(6,tipologiaNotifica==null?"":tipologiaNotifica);
						rowSet.updateString(7,tipologiaServizio==null?"":tipologiaServizio);
						rowSet.updateString(8,numeroDocumento==null?"":numeroDocumento);
						rowSet.updateString(9,email==null?"":email);
						rowSet.updateString(10,emailPec==null?"":emailPec);
						rowSet.updateString(11,numTelefono==null?"":numTelefono);
						rowSet.updateString(12,esitoNotifica==null?"":esitoNotifica);
						rowSet.updateString(13,dataRicezione==null?"":dataRicezione);
						rowSet.updateString(14,dataInvio==null?"":dataInvio);
						
						rowSet.insertRow();
						
						// riposiziono all'inizio perch� vengono invertiti gli elementi 
						rowSet.afterLast();
						rowSet.moveToInsertRow();
					}
					
					// rimette il puntatore all'inzio
					rowSet.moveToCurrentRow();
					rowSet.beforeFirst();
					return rowSet;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		return null;
	}
	
	
	private void dividiSocUtenteEnte(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String ddlSocietaUtenteEnte ="";
		if (request.getAttribute("ddlSocietaUtenteEnte")!=null){
			ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			if (session.getAttribute("ddlSocietaUtenteEnte")==null){
				session.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
			if (!request.getAttribute("ddlSocietaUtenteEnte").toString().equals(session.getAttribute("ddlSocietaUtenteEnte").toString())){
				session.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
		}
		else{
			if (session.getAttribute("ddlSocietaUtenteEnte")!=null){
				ddlSocietaUtenteEnte = (String)session.getAttribute("ddlSocietaUtenteEnte");
				request.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
		}
		
		if (!ddlSocietaUtenteEnte.equals("")) {
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			codiceSocieta = codici[0];
			cutecute = codici[1];
			chiaveEnte = codici[2];
			codiceEnte = codici[3].substring(1,6);
			descEnte = codici[3].substring(10);
			
//			1) mettere qui il codice ente da 5
//			2) verificare la paginazione con una sola pagina ad esempio mettendo mail pec
//			3) verificare perch� non filtra per documento
			
			request.setAttribute("tx_societa", codiceSocieta);
			request.setAttribute("tx_utente", cutecute);
			request.setAttribute("tx_ente", codiceEnte);
		}
		
		//Mantenimento di variabili in caso di paginazione La paginazione viene gfestita da un'altro FORM
		
		//PREJAVA18_LUCAP_03082020 - revisione dei nomi degli attributi
		if (request.getAttribute("tx_codfisc") != null)
			session.setAttribute("tx_codfisc",request.getAttribute("tx_codfisc"));
		else if(session.getAttribute("tx_codfisc")!=null)
				request.setAttribute("tx_codfisc",session.getAttribute("tx_codfisc"));

		if (request.getAttribute("tx_numdocumento") != null)
			session.setAttribute("tx_numdocumento",request.getAttribute("tx_numdocumento"));
		else if(session.getAttribute("tx_numdocumento")!=null)
			request.setAttribute("tx_numdocumento",session.getAttribute("tx_numdocumento"));
		
		if (request.getAttribute("tx_email") != null)
			session.setAttribute("tx_email",request.getAttribute("tx_email"));
		else if(session.getAttribute("tx_email")!=null)
			request.setAttribute("tx_email",session.getAttribute("tx_email"));
		
		if (request.getAttribute("tx_emailpec") != null)
			session.setAttribute("tx_emailpec",request.getAttribute("tx_emailpec"));
		else if(session.getAttribute("tx_emailpec")!=null)
			request.setAttribute("tx_emailpec",session.getAttribute("tx_emailpec"));
		
		if (request.getAttribute("tx_tiponotifica") != null)
			session.setAttribute("tx_tiponotifica",request.getAttribute("tx_tiponotifica"));
		else if(session.getAttribute("tx_tiponotifica")!=null)
			request.setAttribute("tx_tiponotifica",session.getAttribute("tx_tiponotifica"));
		
		if (request.getAttribute("tx_tipologiaservizio") != null)
			session.setAttribute("tx_tipologiaservizio",request.getAttribute("tx_tipologiaservizio"));
		else if(session.getAttribute("tx_tipologiaservizio")!=null)
			request.setAttribute("tx_tipologiaservizio",session.getAttribute("tx_tipologiaservizio"));
		
		if (request.getAttribute("data_invio_da") != null)
			session.setAttribute("data_invio_da",request.getAttribute("data_invio_da"));
		else if(session.getAttribute("data_invio_da")!=null)
			request.setAttribute("data_invio_da",session.getAttribute("data_invio_da"));
		
		if (request.getAttribute("data_invio_a") != null)
			session.setAttribute("data_invio_a",request.getAttribute("data_invio_a"));
		else if(session.getAttribute("data_invio_a")!=null)
				request.setAttribute("data_invio_a",session.getAttribute("data_invio_a"));
		
		if (request.getAttribute("rowsPerPage") != null)
			session.setAttribute("rowsPerPage",request.getAttribute("rowsPerPage"));
		else if(session.getAttribute("rowsPerPage")!=null)
				request.setAttribute("rowsPerPage",session.getAttribute("rowsPerPage"));
		
		if (request.getAttribute("order") != null)
			session.setAttribute("order",request.getAttribute("order"));
		else if(session.getAttribute("order")!=null)
				request.setAttribute("order",session.getAttribute("order"));
		
		if (request.getAttribute("pageNumber") != null)
			session.setAttribute("pageNumber",request.getAttribute("pageNumber"));
	
		//FINE PREJAVA18_LUCAP_03082020 - revisione dei nomi degli attributi
		
	}
	private String getParameterSel(NotSel notSel, String order, HttpServletRequest request) {
	
		HttpSession session = request.getSession();  //PREJAVA18_LUCAP_03082020
		
		if (order.equals("DTIN_A")) {notSel.setDataInvioOrder("ASC");}; 		
		if (order.equals("DTIN_D")) {notSel.setDataInvioOrder("DESC");};
	
		if (order.equals("TPNO_A")) {notSel.setTipologiaNotificaOrder("ASC");}; 		
		if (order.equals("TPNO_D")) {notSel.setTipologiaNotificaOrder("DESC");}; 		
		
		if (order.equals("TPSE_A")) {notSel.setTipologiaServizioOrder("ASC");}; 		
		if (order.equals("TPSE_D")) {notSel.setTipologiaServizioOrder("DESC");}; 		
		
		if (order.equals("CFIS_A")) {notSel.setCfOrder("ASC");}; 		
		if (order.equals("CFIS_D")) {notSel.setCfOrder("DESC");}; 		
		
		if (order.equals("NRDO_A")) {notSel.setNumeroDocumentoOrder("ASC");}; 		
		if (order.equals("NRDO_D")) {notSel.setNumeroDocumentoOrder("DESC");}; 		
		
		if (order.equals("MAIL_A")) {notSel.setEmailOrder("ASC");}; 		
		if (order.equals("MAIL_D")) {notSel.setEmailOrder("DESC");}; 		
		
		if (order.equals("MAIP_A")) {notSel.setEmailPecOrder("ASC");}; 		
		if (order.equals("MAIP_D")) {notSel.setEmailPecOrder("DESC");}; 		
		
		if (order.equals("TELE_A")) {notSel.setNumTelefonoOrder("ASC");}; 		
		if (order.equals("TELE_D")) {notSel.setNumTelefonoOrder("DESC");}; 		
		
		if (order.equals("ESNO_A")) {notSel.setEsitoNotificaOrder("ASC");}; 		
		if (order.equals("ESNO_D")) {notSel.setEsitoNotificaOrder("DESC");}; 		
		
		if (order.equals("DTNO_A")) {notSel.setDataEsitoNotificaOrder("ASC");}; 		
		if (order.equals("DTNO_D")) {notSel.setDataEsitoNotificaOrder("DESC");}; 		

		//		String url = endPoint.trim().concat(operation.trim()).concat("?codAttoOrder=ASC&cfOrder=ASC&pageNumber=5&rowsPerPage=1");

		//PREJAVA18_LUCAP_03082020
		if(notSel.getDataNotificaDa()==null&&session.getAttribute("data_invio_da")!=null) {
			Calendar cal = (Calendar) session.getAttribute("data_invio_da");
			notSel.setDataNotificaDa(sdf.format(cal.getTime()));
		}
		if(notSel.getDataNotificaA()==null&&session.getAttribute("data_invio_a")!=null) {
			Calendar cal = (Calendar) session.getAttribute("data_invio_a");
			notSel.setDataNotificaA(sdf.format(cal.getTime()));
		}
		//FINE PREJAVA18_LUCAP_03082020
		
		Integer numPage = Integer.parseInt(notSel.getPageNumber())-1;
		String parameterSel = "&pageNumber="+(numPage.toString())+"&rowsPerPage="+notSel.getRowsPerPage();
		parameterSel = parameterSel.concat((notSel.getCodiceFiscale()!=null&&!notSel.getCodiceFiscale().trim().equals(""))?"&codiceFiscale=".concat(notSel.getCodiceFiscale().trim()):"");
		parameterSel = parameterSel.concat((notSel.getCodiceSocieta()!=null&&!notSel.getCodiceSocieta().trim().equals(""))?"&codiceSocieta=".concat(notSel.getCodiceSocieta().trim()):"");
		parameterSel = parameterSel.concat((notSel.getCodiceUtente()!=null&&!notSel.getCodiceUtente().trim().equals(""))?"&codiceUtente=".concat(notSel.getCodiceUtente().trim()):"");
		parameterSel = parameterSel.concat((notSel.getCodiceEnte()!=null&&!notSel.getCodiceEnte().trim().equals(""))?"&codiceEnte=".concat(notSel.getCodiceEnte().trim()):"");
		parameterSel = parameterSel.concat((notSel.getNumeroDocumento()!=null&&!notSel.getNumeroDocumento().trim().equals(""))?"&numeroDocumento=".concat(notSel.getNumeroDocumento().trim()):"");
		parameterSel = parameterSel.concat((notSel.getEmail()!=null&&!notSel.getEmail().trim().equals(""))?"&email=".concat(notSel.getEmail().trim()):"");
		parameterSel = parameterSel.concat((notSel.getEmailPec()!=null&&!notSel.getEmailPec().trim().equals(""))?"&emailPec=".concat(notSel.getEmailPec().trim()):"");
		parameterSel = parameterSel.concat((notSel.getNumTelefono()!=null&&!notSel.getNumTelefono().trim().equals(""))?"&numTelefono=".concat(notSel.getNumTelefono().trim()):"");
		parameterSel = parameterSel.concat((notSel.getTipologiaNotifica()!=null&&!notSel.getTipologiaNotifica().trim().equals(""))?"&tipologiaNotifica=".concat(notSel.getTipologiaNotifica().trim()):"");
		parameterSel = parameterSel.concat((notSel.getTipologiaServizio()!=null&&!notSel.getTipologiaServizio().trim().equals(""))?"&tipologiaServizio=".concat(notSel.getTipologiaServizio().trim()):"");
		parameterSel = parameterSel.concat((notSel.getDataNotificaDa()!=null&&!notSel.getDataNotificaDa().trim().equals(""))?"&dataNotificaDa=".concat(notSel.getDataNotificaDa().trim()):"");
		parameterSel = parameterSel.concat((notSel.getDataNotificaA()!=null&&!notSel.getDataNotificaA().trim().equals(""))?"&dataNotificaA=".concat(notSel.getDataNotificaA().trim()):"");

//		parameterSel = parameterSel.concat((notSel.getCodAttoOrder()!=null&&!notSel.getCodAttoOrder().trim().equals(""))?"&codAttoOrder=".concat(notSel.getCodAttoOrder().trim()):"");
		parameterSel = parameterSel.concat((notSel.getCfOrder()!=null&&!notSel.getCfOrder().trim().equals(""))?"&cfOrder=".concat(notSel.getCfOrder().trim()):"");
		parameterSel = parameterSel.concat((notSel.getDataInvioOrder()!=null&&!notSel.getDataInvioOrder().trim().equals(""))?"&dataInvioOrder=".concat(notSel.getDataInvioOrder().trim()):"");
		parameterSel = parameterSel.concat((notSel.getTipologiaNotificaOrder()!=null&&!notSel.getTipologiaNotificaOrder().trim().equals(""))?"&tipologiaNotificaOrder=".concat(notSel.getTipologiaNotificaOrder().trim()):"");
		parameterSel = parameterSel.concat((notSel.getTipologiaServizioOrder()!=null&&!notSel.getTipologiaServizioOrder().trim().equals(""))?"&tipologiaServizioOrder=".concat(notSel.getTipologiaServizioOrder().trim()):"");
		parameterSel = parameterSel.concat((notSel.getNumeroDocumentoOrder()!=null&&!notSel.getNumeroDocumentoOrder().trim().equals(""))?"&numeroDocumentoOrder=".concat(notSel.getNumeroDocumentoOrder().trim()):"");
		parameterSel = parameterSel.concat((notSel.getEmailOrder()!=null&&!notSel.getEmailOrder().trim().equals(""))?"&emailOrder=".concat(notSel.getEmailOrder().trim()):"");
		parameterSel = parameterSel.concat((notSel.getEmailPecOrder()!=null&&!notSel.getEmailPecOrder().trim().equals(""))?"&emailPecOrder=".concat(notSel.getEmailPecOrder().trim()):"");
		parameterSel = parameterSel.concat((notSel.getNumTelefonoOrder()!=null&&!notSel.getNumTelefonoOrder().trim().equals(""))?"&numTelefonoOrder=".concat(notSel.getNumTelefonoOrder().trim()):"");
		parameterSel = parameterSel.concat((notSel.getEsitoNotificaOrder()!=null&&!notSel.getEsitoNotificaOrder().trim().equals(""))?"&esitoNotificaOrder=".concat(notSel.getEsitoNotificaOrder().trim()):"");
		parameterSel = parameterSel.concat((notSel.getDataEsitoNotificaOrder()!=null&&!notSel.getDataEsitoNotificaOrder().trim().equals(""))?"&dataEsitoNotificaOrder=".concat(notSel.getDataEsitoNotificaOrder().trim()):"");
	
		return parameterSel;

	}

	private Notifica getDetail(HttpServletRequest request) {
		
		String chiaveTabellaNotifica = (String)request.getAttribute("chiaveTabellaNotifica");		
		
		Notifica notifica = new Notifica();
		
//			String urlNotificatore = "http://10.10.80.12:8280/notificatore-service";
//			String bearer = "Bearer eyJ0eXAiOiAiSldUIiwiYWxnIjogIkhTMjU2In0=.eyAiaXNzIjogIkRZVmlXbVlwdlUrTFRpWHN0SVlMZ2dpWDZYTy9ScnlRa3l2TERYUnJjMUE9IDY0NzcyNTZmLThmOWEtNGNkMi1hMWY4LWRiOGIyMDgyNDZkOSIsImp0aSI6IjcrZzlKazlyNFNDSmxIU3VrTTJQeDFWaU9MS3J1aU9BQldlYnNUQ0E5QVRvSDBacHUrUzVsbE1JMjc1ZDBLZDJzbVJiSlNGNUNUVWJwT0FLSUp3L2k5dWxQbStzNGNQU3ZrU1JadGtrZkxjPSIsImV4cCI6IjI1MzQ1MDU1MTYwMDAwMCJ9.Qz1vdxlG9uCu+q14/4mYvZSifhKnFCECHzRP0vWea/w=";
		String urlNotificatore = (String) context.getAttribute(ManagerKeys.NOTIFICHE_URL);
		String bearer = (String) context.getAttribute(ManagerKeys.NOTIFICHE_BEARER);
		
		String operationNotificatore = "/gestioneNotifiche/cercaDettaglio/"+chiaveTabellaNotifica+".json";
		try {			
			NotificatoreHelper notificatore = new NotificatoreHelper(urlNotificatore, operationNotificatore, false, "", 0);

			ResponseEntity<NotificaResponseWrapper> rssResponse = notificatore.selNotifica(notifica,bearer);
			if (rssResponse!=null)
				System.out.println("Esito chiamata a servizio json: " + rssResponse.toString());
			NotificaResponseWrapper out = rssResponse.getBody();
			if(out!=null){
				System.out.println("Status: " + out.getWebServiceOutput().getStatus());
				System.out.println("ErrorCode: " + out.getWebServiceOutput().getErrorCode());
			}
			if(rssResponse.getStatusCode()==HttpStatus.OK){
				notifica = convertRssResponseToNotifica(rssResponse);
			}

		} catch (Throwable e) {
			System.out.println("Errore in chiamata a servizio json: " + e.getCause());
		}
		return notifica;
	}

	private Notifica convertRssResponseToNotifica(ResponseEntity<NotificaResponseWrapper> rssResponse) {

		Notifica not = new Notifica();

		not.setCodiceEnte(rssResponse.getBody().getWebServiceOutput().getElement().getCodiceEnte());
		not.setCodiceSocieta(rssResponse.getBody().getWebServiceOutput().getElement().getCodiceSocieta());
		not.setCodiceUtente(rssResponse.getBody().getWebServiceOutput().getElement().getCodiceUtente());
		not.setFlagTipoMessaggio(rssResponse.getBody().getWebServiceOutput().getElement().getFlagTipoMessaggio());
		not.setFlagTipoServizio(rssResponse.getBody().getWebServiceOutput().getElement().getFlagTipoServizio());
		not.setCodiceFiscale(rssResponse.getBody().getWebServiceOutput().getElement().getCodiceFiscale());
		not.setMittente(rssResponse.getBody().getWebServiceOutput().getElement().getMittente());
		not.setDestinatario(rssResponse.getBody().getWebServiceOutput().getElement().getDestinatario());
		not.setConoscenzaNascosta(rssResponse.getBody().getWebServiceOutput().getElement().getConoscenzaNascosta());
		not.setMessaggio(rssResponse.getBody().getWebServiceOutput().getElement().getMessaggio());
		not.setDataInvio(rssResponse.getBody().getWebServiceOutput().getElement().getDataInvio());
		not.setPdfFileBase64(rssResponse.getBody().getWebServiceOutput().getElement().getPdfFileBase64());

		return not;
	}
	
	private ScadenzaUrlNotifica getScadenzaUrlNotificaFromDB(HttpServletRequest request, String cutecute, String codiceEnte, String codiceFiscale, String numeroDocumento){
		ScadenzaUrlNotifica scadenzaUrl = null;
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try{
			String dbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(cutecute));  
			String dataSourceName = configuration.getProperty(PropertiesPath.dataSourceWallet.format(cutecute));
			DataSource dataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));

			//inizio LP PG21XX04 Leak
			//ScadenzaUrlNotificaDao scadenzaUrlNotificaDao = ScadenzaUrlNotificaDAOFactory.getScadenzaUrlNotifica(dataSource.getConnection(), dbSchema);
			connection = dataSource.getConnection();
			ScadenzaUrlNotificaDao scadenzaUrlNotificaDao = ScadenzaUrlNotificaDAOFactory.getScadenzaUrlNotifica(connection, dbSchema);
			//fine LP PG21XX04 Leak
			scadenzaUrl = scadenzaUrlNotificaDao.select("00000", codiceFiscale, numeroDocumento);

		}catch(Exception e){
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		
		return scadenzaUrl;
	}
	
	//inizio LP PG21XX04 Leak
	private Integer updateScadenzaUrlNotifica(ScadenzaUrlNotifica urlNotifica, String cutecute) throws DaoException {
		Integer out = null;
		Connection connection = null;
		try{
			System.out.println("cutecute = " + cutecute);
			String dbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(cutecute));  
			String dataSourceName = configuration.getProperty(PropertiesPath.dataSourceWallet.format(cutecute));
			DataSource dataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));

			connection = dataSource.getConnection();
			ScadenzaUrlNotificaDao scadenzaUrlNotificaDao = ScadenzaUrlNotificaDAOFactory.getScadenzaUrlNotifica(connection, dbSchema);
			out = scadenzaUrlNotificaDao.update(urlNotifica);

		} catch(SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		} catch (ServiceLocatorException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return out;
	}
	//fine LP PG21XX04 Leak
	
	private Map<String,List<String>> getProperties(Notifica notifica){

		Map<String,List<String>> properties = new HashMap<String,List<String>>();	
		if ( configuration.getProperty(PropertiesPath.urlSito.format(notifica.getCodiceUtente())) !=null ) { 
			List<String> prop=new ArrayList<String>();
			prop.add(configuration.getProperty(PropertiesPath.urlSito.format(notifica.getCodiceUtente())));
			properties.put("urlSito", prop);
		}
		
		String personaFisica = "true";
		if (notifica.getCodiceFiscale().trim().length()<16) {
			personaFisica = "false";
		}
		List<String> prop1=new ArrayList<String>();
		prop1.add(personaFisica);
		properties.put("personaFisica", prop1);
		
		List<String> prop2=new ArrayList<String>();
		prop2.add("");
		properties.put("denominazione", prop2);
		properties.put("ragioneSociale", prop2);
		
		String urlDefault = "";
		String urlSceltaAut = "";
		
		if (configuration.getProperty(PropertiesPath.urlDefault.format(notifica.getCodiceUtente())) != null) { 
			urlDefault = configuration.getProperty(PropertiesPath.urlDefault.format(notifica.getCodiceUtente()));
		}
		List<String> prop3=new ArrayList<String>();
		prop3.add(urlDefault);
		properties.put("url_default", prop3);
		
		if (configuration.getProperty(PropertiesPath.urlSceltaAut.format(notifica.getCodiceUtente())) !=null) { 
			urlSceltaAut = configuration.getProperty(PropertiesPath.urlDefault.format(notifica.getCodiceUtente()));
		}
		List<String> prop4=new ArrayList<String>();
		prop4.add(urlSceltaAut);
		properties.put("url_sceltaaut", prop4);
		
		return properties;
		
	}
	
	private AnagraficaBollettino getAnagraficaDiContatto(String codiceFiscale, String codiceUtente, String codiceSocieta){
		
		AnagraficaBollettino anagrafica = null;
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try{
			String dbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(codiceUtente));  
			String dataSourceName = configuration.getProperty(PropertiesPath.dataSourceWallet.format(codiceUtente));
			DataSource dataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));

			//inizio LP PG21XX04 Leak
			//anagraficaBollettinoDao = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(dataSource, dbSchema);
			connection = getAnagraficaDataSource().getConnection();
			AnagraficaBollettinoDAO anagraficaBollettinoDao = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(connection, dbSchema);
			//fine LP PG21XX04 Leak

			anagrafica = anagraficaBollettinoDao.selByCF(codiceSocieta, codiceUtente, codiceFiscale);

		}catch(Exception e){
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		
		return anagrafica;
		
	}
	
	private String getCodiceSocietaFromDB(Notifica notifica){
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try{
			
			//inizio LP PG21XX04 Leak
			//if(scadenzaUrlNotificaDao==null){
			//fine LP PG21XX04 Leak
				String dbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(cutecute));  
				String dataSourceName = configuration.getProperty(PropertiesPath.dataSourceWallet.format(cutecute));
				DataSource dataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));

				//inizio LP PG21XX04 Leak
				//scadenzaUrlNotificaDao = ScadenzaUrlNotificaDAOFactory.getScadenzaUrlNotifica(dataSource.getConnection(), dbSchema);
				connection = getAnagraficaDataSource().getConnection();
				ScadenzaUrlNotificaDao scadenzaUrlNotificaDao = ScadenzaUrlNotificaDAOFactory.getScadenzaUrlNotifica(connection, dbSchema);
				//fine LP PG21XX04 Leak
			//inizio LP PG21XX04 Leak
			//}
			//fine LP PG21XX04 Leak
			
			return scadenzaUrlNotificaDao.getCodiceSocietaByEnteUtente(notifica.getCodiceUtente(), notifica.getCodiceEnte());

		}catch(Exception e){
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		return "";
	}
	
	//PREJAVA18_LUCAP_06082020
	private String formattaDataddmmyyyy(String ecNotificheListXml) {
		//inizio LP PG21XX04 Leak
		//WebRowSet rowSetFull;
		WebRowSet rowSetFull = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//rowSetFull = new WebRowSetImpl();
			//fine LP PG21XX04 Leak
			rowSetFull = Convert.stringToWebRowSet(ecNotificheListXml);
			while (rowSetFull.next()) {

				if(rowSetFull.getMetaData().getColumnCount() >= 13 && rowSetFull.getString(13) != null){
					String[] data = rowSetFull.getString(13).split("-");
					rowSetFull.updateString(13, data.length == 3 ? data[2]+"/"+data[1]+"/"+data[0] : "");
				}

				if(rowSetFull.getMetaData().getColumnCount() >= 14 && rowSetFull.getString(14) != null){
					String[] data = rowSetFull.getString(14).split("-");
					rowSetFull.updateString(14, data.length == 3 ? data[2]+"/"+data[1]+"/"+data[0] : "");
				}

			}
			return Convert.webRowSetToString(rowSetFull);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			try {
				if(rowSetFull != null) {
					rowSetFull.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return null;
	}
	//FINE PREJAVA18_LUCAP_06082020
	
}
