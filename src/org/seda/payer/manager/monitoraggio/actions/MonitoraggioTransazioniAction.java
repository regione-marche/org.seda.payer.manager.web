package org.seda.payer.manager.monitoraggio.actions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.adminusers.util.Error;

import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;


import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.commons.utility.StringUtility;
import com.seda.payer.pgec.webservice.commons.dati.GeneraPdfTransazioniRequest;
import com.seda.payer.pgec.webservice.commons.dati.GeneraPdfTransazioniResponse;
import com.seda.payer.pgec.webservice.commons.dati.ModuloIntegrazionePagamentiOneriGrouped;
import com.seda.payer.pgec.webservice.commons.dati.ParametriRicercaMonitoraggio;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniCsvRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniCsvResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniGroupedOneriRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniGroupedOneriResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniGroupedRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniGroupedResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniGroupedSuccessRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniGroupedSuccessResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniRequestType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniResponseType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniResponseTypePageInfo;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.dati.TransazioniGrouped;
import com.seda.payer.pgec.webservice.commons.dati.TransazioniGroupedSuccess;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class MonitoraggioTransazioniAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;
	private String siglaProvincia;


	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession(false);

		//PREJAVA18_LUCAP_05082020
		String idMovimentoCassa = ((String)request.getAttribute("idMovimentoCassa") == null) ? ((String)request.getAttribute("idmdc") == null ? "" : (String)request.getAttribute("idmdc")) : (String)request.getAttribute("idMovimentoCassa");
		request.setAttribute("idMovimentoCassa", idMovimentoCassa);
		//FINE PREJAVA18_LUCAP_05082020
		
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		//		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		replyAttributes(false, request,"pageNumber","rowsPerPage","order","validator.message");

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
				
		if(getTemplateCurrentApplication(request, session).equals("regmarche") && request.getAttribute(Field.TX_DATA_DA.format())==null && request.getAttribute(Field.TX_DATA_ACCR_DA.format())==null)
			request.setAttribute(Field.TX_DATA_DA.format(), Calendar.getInstance());
		

		siglaProvincia = isNull(request.getAttribute(Field.TX_PROVINCIA.format()));
		
		if(request.getAttribute("indietro") != null) {				
			request.setAttribute(Field.TX_LISTA.format(), session.getAttribute("session_lista_transazioni"));
			request.setAttribute(Field.TX_LISTA_PAGEINFO.format(), session.getAttribute("session_lista_transazioni.pageInfo"));
			request.setAttribute(Field.TX_LISTA_GROUPED.format(), session.getAttribute("session_lista_transazioni.grouped"));					
		}
		

		switch(firedButton) {
		case TX_BUTTON_CERCA: 
		case TX_BUTTON_CONFERMA_RICONCILIAZIONE:
			
			try {


						System.out.println("[MANAGER - MonitoraggioTransazioniAction - getListaTransazioni()] INIZIO CHIAMATA");
						RecuperaTransazioniResponseType listaTransazioni = null;
						try {
							listaTransazioni = getListaTransazioni(request, session);
						} catch (FaultType e2) {
							e2.printStackTrace();
						} catch (RemoteException e2) {
							e2.printStackTrace();
						}
						System.out.println("[MANAGER - MonitoraggioTransazioniAction - getListaTransazioni()] FINE CHIAMATA");


				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), false);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), false);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
				String messageDate = controlloDate(request);
				
				if(getTemplateCurrentApplication(request, session).equals("regmarche") && messageDate !=null) {
					setFormMessage("monitoraggioTransazioniForm", messageDate , request);
				}else {
				
					if (isNull(request.getAttribute("tx_codice_IUR")).length()>0 && isNull(request.getAttribute(Field.TX_ID_TRANSAZ_ATM.format())).length()>0) {	//PG160230_001 GG 22112016
						setFormMessage("monitoraggioTransazioniForm", "Valorizzare solo uno dei campi Id.Trans.Atm o Codice I.U.R.", request);
					}
					else {	
							
							System.out.println("[MANAGER - MonitoraggioTransazioniAction - getListaTransazioni()] INIZIO CHIAMATA");
							listaTransazioni = getListaTransazioni(request, session);
							System.out.println("[MANAGER - MonitoraggioTransazioniAction - getListaTransazioni()] FINE CHIAMATA");
							
							String tipoQuery = isNull(request.getAttribute("tx_scelta_query"));

							if(tipoQuery.equals("") || tipoQuery==null) {
								tipoQuery = "C"; // C
							}
							
							if (tipoQuery.equals("A") || tipoQuery.equals("C")) {// PAGONET-437
								
								if(listaTransazioni != null)
								{
									ResponseType response = listaTransazioni.getResponse();
									ResponseTypeRetCode rc = response.getRetCode();
									
									
									if(rc.equals(ResponseTypeRetCode.value1))
									{
										PageInfo pageInfo = this.getPageInfo(listaTransazioni.getPageInfo());
										if(pageInfo != null)
										{
											if(pageInfo.getNumRows() > 0)
											{
												session.setAttribute("numRows", pageInfo.getNumRows());
												String lista = listaTransazioni.getListXml();					
												request.setAttribute(Field.TX_LISTA.format(), lista);
												request.setAttribute(Field.TX_LISTA_PAGEINFO.format(), pageInfo);
												session.setAttribute("session_lista_transazioni", lista);
												session.setAttribute("session_lista_transazioni.pageInfo", pageInfo);
												
										}
										else 
											setFormMessage("monitoraggioTransazioniForm", "Nessuna transazione trovata", request);
									}
									else
									{
										//String messaggio = response.getRetMessage(); 
										//setMessage((messaggio == null || messaggio.equals("")) ? "Errore generico" : messaggio);
										setFormMessage("monitoraggioTransazioniForm", response.getRetMessage() , request);
										
									}
				
									System.out.println("[MANAGER - MonitoraggioTransazioniAction -  FINE CICLI dentro IF");
								}
								else 
									setFormMessage("monitoraggioTransazioniForm", Messages.TX_NO_TRANSACTIONS.format() , request);
								//setMessage(Messages.TX_NO_TRANSACTIONS.format());
							}
							else 
								setFormMessage("monitoraggioTransazioniForm", Messages.NO_DATA_FOUND.format(), request);
							//setMessage(Messages.NO_DATA_FOUND.format());
							}
								
								
							if(tipoQuery.equals("B") || tipoQuery.equals("A")) {
									recuperaGruppoDa(request,session,tipoQuery);
						    }
								
					  }
				}

			} catch (FaultType e) {
				setFormMessage("monitoraggioTransazioniForm", e.getMessage() , request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("monitoraggioTransazioniForm", e.getMessage() , request);
				e.printStackTrace();
			} catch(Exception e) {
				setFormMessage("monitoraggioTransazioniForm", e.getMessage() , request);
				e.printStackTrace();
			}
			break;


		case TX_BUTTON_RESET:
			resetParametri(request);
			if(getTemplateCurrentApplication(request, session).equals("regmarche"))
				request.setAttribute(Field.TX_DATA_DA.format(), Calendar.getInstance());
			else
				request.setAttribute(Field.TX_DATA_DA.format(), null);
			request.setAttribute(Field.TX_DATA_A.format(), null);
			request.setAttribute(Field.TX_DATA_ACCR_DA.format(), null);
			request.setAttribute(Field.TX_DATA_ACCR_A.format(), null);
			setProfile(request);
			siglaProvincia = "";
			loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
			//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
			loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
			loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
			LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
			break;

		case TX_BUTTON_SOCIETA_CHANGED:
			loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
			//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
			LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), "", "", "", true);
			//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
			loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
			loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), "", true);
			break;

		case TX_BUTTON_PROVINCIA_CHANGED:
			loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
			//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
			LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, "", "", true);
			//loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
			loadListaGatewayXml_DDL(request, session, "", "", false);
			loadTipologiaServizioXml_DDL_2(request, session, "","","", false);
			break;

		case TX_BUTTON_ENTE_CHANGED:
			loadProvinciaXml_DDL(request, session, "",false);
			LoadListaUtentiEntiXml_DDL(request, session, "" , "", "", "", false);
			//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
			loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
			loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
			break;

		case TX_BUTTON_NULL: 
			loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
			//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
			loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
			loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
			LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
			break;

		case TX_BUTTON_DOWNLOAD:

			String file="";
			String pathFile="";
			request.setAttribute("download", "Y");
			
			try {
				ServletContext context = request.getSession(false).getServletContext();
				Integer maxRigheDownload = (Integer) context.getAttribute(ManagerKeys.MAXRIGHE_DOWNLOAD);
				if ((Integer) session.getAttribute("numRows") <= maxRigheDownload ){
			//Giulia 22/05/2014 INIZIO
			//Correzione dell' errore OutMemory--Botton Download csv Monitoraggio Transazioni
//					file = getListaTransazioniCsv(request,session);
					pathFile = getListaTransazioniDownloadCsv(request, session);
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
				}
				else {
					setFormMessage("monitoraggioTransazioniForm", Messages.SUPERATO_MAXRIGHE.format(), request);
					request.setAttribute("download", "N");
					break;
				}


			} catch (FaultType e1) {
				setFormMessage("monitoraggioTransazioniForm", e1.getMessage() , request);
				e1.printStackTrace();
			} catch (RemoteException e1) {
				setFormMessage("monitoraggioTransazioniForm", e1.getMessage() , request);
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
			System.out.println("File pronto per la JSP");
			//aggiustamento carattere \r perso ne webservice 
			file = file.replaceAll("\n", "\r\n");
			
			String template = getTemplateCurrentApplication(request, request.getSession());
			if(template.equals("aosta")||template.equals("aostaFR")) {
			    String[] specialChars = { "", "", "", "", "é","", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
			    String[] standardChars = { "a", "a", "a", "c", "e","e", "e", "e", "i", "i", "o", "o", "u", "u", "u", "a", "A", "A", "E", "E", "I", "I", "O", "O", "U", "U", "A", "C", "E", "E", "E", "O", "A"};
				for (int i = 0; i < specialChars.length; i++) {
			        file = file.replaceAll(specialChars[i], standardChars[i]);
			    }	
			}
			
			request.setAttribute("sinteticoTransazioniCsv", file);
			request.setAttribute("filename","sinteticoTransazioni.csv");

			File delfile = new File(pathFile);
			delfile.delete();
			break;
	//Giulia 22/05/2014 FINE
		case TX_BUTTON_STAMPA:
			String filename="";
			try {
				filename = getListaTransazioniPdf(request,session);
			} catch (FaultType e) {
				setFormMessage("monitoraggioTransazioniForm", e.getMessage() , request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("monitoraggioTransazioniForm", e.getMessage() , request);
				e.printStackTrace();
			}
			request.setAttribute("stampa", filename);
			request.setAttribute("filename", "monitoraggio_transazioni.pdf");
			break;

		}
		request.setAttribute(Field.TX_MESSAGE.format(), getMessage());
		request.setAttribute(Field.TX_ERROR_MESSAGE.format(), getErrorMessage());
		return null;
	}
	
	private void recuperaGruppoDa(HttpServletRequest request, HttpSession session,String tipoQuery)throws FaultType, RemoteException {
		

		/* Riepilogo statistico "Importi Totali sui rispettivi Gateway" */
		System.out.println("[MANAGER - MonitoraggioTransazioniAction - getListaTransazioniGrouped()] INIZIO CHIAMATA");
		RecuperaTransazioniGroupedResponse response1 = getListaTransazioniGrouped(request, session);
		System.out.println("[MANAGER - MonitoraggioTransazioniAction - getListaTransazioniGrouped()] FINE CHIAMATA");
		
		if(response1.getList()!=null && (tipoQuery.equals("A") || tipoQuery.equals("B"))) {
		
			List<TransazioniGrouped> listaGrouped = Arrays.asList(response1.getList());
	
			request.setAttribute(Field.TX_LISTA_GROUPED.format(), listaGrouped);
			
			session.setAttribute("session_lista_transazioni.grouped", listaGrouped);
	
			double totB = 0.0, totC = 0.0, totD = 0.0, totABC = 0.0, totACD = 0.0;
			for (TransazioniGrouped tg: listaGrouped)
			{
				totB = totB + Double.parseDouble(tg.getCostiTransazioni());
				totC = totC + Double.parseDouble(tg.getSpeseNotifica());
				totD = totD + Double.parseDouble(tg.getCostiBanca());
				totABC = totABC + Double.parseDouble(tg.getTotaleNettoBanca());
				totACD = totACD + Double.parseDouble(tg.getTotaleNettoBollettini());
			}
			request.setAttribute("TotB", new Double(totB));
			request.setAttribute("TotC", new Double(totC));
			request.setAttribute("TotD", new Double(totD));
			request.setAttribute("TotABC", new Double(totABC));
			request.setAttribute("TotACD", new Double(totACD));
	
			request.setAttribute(Field.GROUPED_TOTAL.format(), response1.getTotale());
		}
		
		/* Riepilogo statistico ripartizione oneri per ente */
		System.out.println("[MANAGER - MonitoraggioTransazioniAction - getListaTransazioniGroupedOneri()] INIZIO CHIAMATA");
		RecuperaTransazioniGroupedOneriResponse response3 = getListaTransazioniGroupedOneri(request, session);
		System.out.println("[MANAGER - MonitoraggioTransazioniAction - getListaTransazioniGroupedOneri()] FINE CHIAMATA");

		if(response3.getList()!=null && (tipoQuery.equals("A") || tipoQuery.equals("B"))) {							
			List<ModuloIntegrazionePagamentiOneriGrouped> listaOneriGrouped = Arrays.asList(response3.getList());

			request.setAttribute(Field.TX_LISTA_ONERI_GROUPED.format(), listaOneriGrouped);

			BigDecimal totImportoOnere = new BigDecimal(0);
			BigDecimal totImportoContabileInIngresso = new BigDecimal(0);
			BigDecimal totImportoContabileInUscita = new BigDecimal(0);
			for (ModuloIntegrazionePagamentiOneriGrouped tg: listaOneriGrouped)
			{
				totImportoOnere=totImportoOnere.add(tg.getImportoOnere());
				totImportoContabileInIngresso=totImportoContabileInIngresso.add(tg.getImportoContabileInIngresso());
				totImportoContabileInUscita=totImportoContabileInUscita.add(tg.getImportoContabileInUscita());
			}

			request.setAttribute(Field.IMPORTOONERE.format(), totImportoOnere);
			request.setAttribute(Field.IMPORTOCONTABILEININGRESSO.format(), totImportoContabileInIngresso);
			request.setAttribute(Field.IMPORTOCONTABILEINUSCITA.format(), totImportoContabileInUscita);
		}
		else {
			
			BigDecimal totImportoOnere = new BigDecimal(0);
			BigDecimal totImportoContabileInIngresso = new BigDecimal(0);
			BigDecimal totImportoContabileInUscita = new BigDecimal(0);
			
			request.setAttribute(Field.IMPORTOONERE.format(), totImportoOnere);
			request.setAttribute(Field.IMPORTOCONTABILEININGRESSO.format(), totImportoContabileInIngresso);
			request.setAttribute(Field.IMPORTOCONTABILEINUSCITA.format(), totImportoContabileInUscita);
			
			request.setAttribute(Field.TX_LISTA_ONERI_GROUPED.format(), new Integer(0));
		}
		
		
		/* Riepilogo statistico transazioni completate con successo */
		System.out.println("[MANAGER - MonitoraggioTransazioniAction - getListaTransazioniGroupedSuccess()] INIZIO CHIAMATA");
		RecuperaTransazioniGroupedSuccessResponse response2 = getListaTransazioniGroupedSuccess(request, session);
		System.out.println("[MANAGER - MonitoraggioTransazioniAction - getListaTransazioniGroupedSuccess()] FINE CHIAMATA");
		TransazioniGroupedSuccess[] listTraSucc = response2.getList();

		if (listTraSucc!=null && listTraSucc.length>0 && (tipoQuery.equals("A") || tipoQuery.equals("B")))
		{
			List<TransazioniGroupedSuccess> listaGroupedSuccess =Arrays.asList(listTraSucc);
			request.setAttribute(Field.TX_LISTA_GROUPED_SUCCESS.format(), listaGroupedSuccess);

			int totPGNum = 0, totECNum = 0, totBol = 0;
			double totPGImp = 0.0, totECImp = 0.0;
			for (TransazioniGroupedSuccess tgs: listaGroupedSuccess)
			{
				totPGNum = totPGNum + tgs.getNumeroBollettiniPG();
				totPGImp = totPGImp + Double.parseDouble(tgs.getImportoPG());
				totECNum = totECNum + tgs.getNumeroBollettiniEC();
				totECImp = totECImp + Double.parseDouble(tgs.getImportoEC());
				totBol = totBol + tgs.getNumeroBollettini();
			}
			request.setAttribute("TotPGNum", new Integer(totPGNum));
			request.setAttribute("TotPGImp", new Double(totPGImp));
			request.setAttribute("TotECNum", new Integer(totECNum));
			request.setAttribute("TotECImp", new Double(totECImp));
			request.setAttribute("TotBol", new Integer(totBol));

			request.setAttribute(Field.GROUPED_SUCCESS_TOTAL.format(), response2.getTotale());

		}
		else
		{
			request.setAttribute(Field.TX_LISTA_GROUPED_SUCCESS.format(), null);

			request.setAttribute("TotPGNum", new Integer(0));
			request.setAttribute("TotPGImp", new Double(0.0));
			request.setAttribute("TotECNum", new Integer(0));
			request.setAttribute("TotECImp", new Double(0.0));
			request.setAttribute("TotBol", new Integer(0));

			request.setAttribute(Field.GROUPED_SUCCESS_TOTAL.format(), new Integer(0));
		}
		
	}
	
	private RecuperaTransazioniResponseType getListaTransazioni(HttpServletRequest request, HttpSession session) throws FaultType, RemoteException {

		RecuperaTransazioniRequestType recuperaTransazioniRequest = new RecuperaTransazioniRequestType();

		int rowsPerPage = request.getAttribute(Field.ROWS_PER_PAGE.format()) == null ? getDefaultListRows(request) : isNullInt(request.getAttribute(Field.ROWS_PER_PAGE.format()));
		int pageNumber = request.getAttribute(Field.PAGE_NUMBER.format()) == null ? 1 : isNullInt(request.getAttribute(Field.PAGE_NUMBER.format()));
		String order = isNull(request.getAttribute(Field.ORDER_BY.format()));
		String codiceFiscale = isNull(request.getAttribute("tx_codice_fiscale"));

		recuperaTransazioniRequest.setOrder(order);
		recuperaTransazioniRequest.setPageNumber(pageNumber);
		recuperaTransazioniRequest.setRowsPerPage(rowsPerPage);
		recuperaTransazioniRequest.setTx_societa(getParamCodiceSocieta());
		recuperaTransazioniRequest.setTx_provincia(siglaProvincia);
		recuperaTransazioniRequest.setTx_utente(getParamCodiceUtente());
		recuperaTransazioniRequest.setTx_ente(getParamCodiceEnte());
		recuperaTransazioniRequest.setTx_canale_pagamento(isNull(request.getAttribute(Field.TX_CANALE_PAGAMENTO.format())));
		recuperaTransazioniRequest.setTx_codice_transazione(isNull(request.getAttribute(Field.TX_CODICE_TRANSAZIONE.format())));
		recuperaTransazioniRequest.setTx_data_a(getDataByPrefix("tx_data_a",request));
		recuperaTransazioniRequest.setTx_data_da(getDataByPrefix("tx_data_da",request));
		recuperaTransazioniRequest.setTx_codice_fiscale(codiceFiscale); // PAGONET-437
		
		if (request.getAttribute("tx_data_accr_da")==null ||
				request.getAttribute("tx_data_accr_a")==null) {
			recuperaTransazioniRequest.setTx_data_accr_a("");
			recuperaTransazioniRequest.setTx_data_accr_da("");
		} else {
			recuperaTransazioniRequest.setTx_data_accr_a(getDataByPrefix("tx_data_accr_a",request));
			recuperaTransazioniRequest.setTx_data_accr_da(getDataByPrefix("tx_data_accr_da",request));	
		}		
		recuperaTransazioniRequest.setTx_id_bollettino(isNull(request.getAttribute(Field.TX_ID_BOLLETTINO.format())));
		String importo_mon_da = isNull(request.getAttribute(Field.TX_IMPORTO_DA.format()));
		String importo_mon_a = isNull(request.getAttribute(Field.TX_IMPORTO_A.format()));
		importo_mon_da = importo_mon_da.replaceAll("\\.", "");
		importo_mon_da = importo_mon_da.replaceAll(",", ".");
		importo_mon_a = importo_mon_a.replaceAll("\\.", "");
		importo_mon_a = importo_mon_a.replaceAll(",", ".");
		recuperaTransazioniRequest.setTx_importo_a(importo_mon_a);
		recuperaTransazioniRequest.setTx_importo_da(importo_mon_da);
		recuperaTransazioniRequest.setTx_indirizzo_ip(isNull(request.getAttribute(Field.TX_INDIRIZZO_IP.format())));
		recuperaTransazioniRequest.setTx_mostra(isNull(request.getAttribute(Field.TX_MOSTRA.format())));
		recuperaTransazioniRequest.setTx_stato_rendicontazione(isNull(request.getAttribute(Field.TX_STATO_RENDICONTAZIONE.format())));
		recuperaTransazioniRequest.setTx_stato_riconciliazione(isNull(request.getAttribute(Field.TX_STATO_RICONCILIAZIONE.format())));
		recuperaTransazioniRequest.setTx_strumento(isNull(request.getAttribute(Field.TX_STRUMENTO.format())));
		//Giulia 8/05/2014 INIZIO
		//recuperaTransazioniRequest.setTx_servizio(getTipologiaServizio(request,session));
		if(getTipologiaServizio(request,session)!=null && !getTipologiaServizio(request,session).equals("") && userBean.getProfile().equals("AMMI")){
		recuperaTransazioniRequest.setTx_servizio(getTipologiaServizio(request,session).substring(0, 3));
		if((getTipologiaServizio(request,session)).length()>0){
			recuperaTransazioniRequest.setTx_societa(getTipologiaServizio(request,session).substring(4));
		}
		}
		else {
			if (getTipologiaServizio(request,session)!=null && !getTipologiaServizio(request,session).equals("") && getTipologiaServizio(request,session).indexOf("_", 0) != -1) {
				String serv = getTipologiaServizio(request,session);
				serv= serv.replace("'", "");
				recuperaTransazioniRequest.setTx_servizio(serv.substring(0, 3));
			}
			else
			recuperaTransazioniRequest.setTx_servizio(getTipologiaServizio(request,session));
		}
		//Giulia 8/05/2014 FINE
		recuperaTransazioniRequest.setTx_tipo_carta(isNull(request.getAttribute(Field.TX_TIPO_CARTA.format())));
		recuperaTransazioniRequest.setTx_user_email(isNull(request.getAttribute(Field.TX_USER_EMAIL.format())));
		recuperaTransazioniRequest.setTx_user_sms(isNull(request.getAttribute(Field.TX_USER_SMS.format())));
		//PG160170_001 GG 15022017 - inizio
		//recuperaTransazioniRequest.setTx_chiave_quadratura(isNullInt(request.getAttribute(Field.TX_CHIAVE_QUADRATURA.format())));
		recuperaTransazioniRequest.setTx_chiave_quadratura(0);
		//PG160170_001 GG 15022017 - fine
	

		String tx_id_transaz_atm = isNull(request.getAttribute(Field.TX_ID_TRANSAZ_ATM.format()));
		if (tx_id_transaz_atm.trim().length()>0) {	//PG160230_001 GG 22112016
			recuperaTransazioniRequest.setTx_idTerminaleAtm(recuperaIdTerminaleAtm(tx_id_transaz_atm));
			recuperaTransazioniRequest.setTx_idTransazioneAtm(recuperaIdTransazioneAtm(tx_id_transaz_atm));
		}
		//PG160230_001 GG 22112016 - inizio
		String tx_codice_IUR = isNull(request.getAttribute(Field.TX_CODICE_IUR.format()));
		if (tx_codice_IUR.trim().length()>0) {
			recuperaTransazioniRequest.setTx_idTransazioneAtm(tx_codice_IUR);
		}
		//PG160230_001 GG 22112016 - fine

		recuperaTransazioniRequest.setTx_chiaveRendicontazione(isNull(request.getAttribute(Field.TX_CHIAVE_RENDICONTAZIONE.format())));
		recuperaTransazioniRequest.setTx_id_terminale_pos_fisico(isNull(request.getAttribute(Field.TX_ID_TERMINALE_POS_FISICO.format())));
		recuperaTransazioniRequest.setTx_codice_IUV(isNull(request.getAttribute("tx_codice_IUV")));		//PG150180_001 GG
		
		//PG160170_001 GG 15022017 - inizio
		//Il campo di ricerca Chiave Quadratura contiene l'identificativo del flusso di quadratura
		recuperaTransazioniRequest.setTx_idFlussoQuadratura(isNull(request.getAttribute(Field.TX_CHIAVE_QUADRATURA.format())));
		recuperaTransazioniRequest.setTx_recuperate(isNull(request.getAttribute(Field.TX_RECUPERATE.format())));   //PG200050_001 SB
		//PG160170_001 GG 15022017 - fine

		return WSCache.commonsServer.recuperaTransazioni(recuperaTransazioniRequest, request);
	}


	private RecuperaTransazioniGroupedResponse getListaTransazioniGrouped(HttpServletRequest request, HttpSession session) throws FaultType, RemoteException {

		RecuperaTransazioniGroupedRequest recuperaTransazioniRequest = new RecuperaTransazioniGroupedRequest();

		recuperaTransazioniRequest.setParametriRicerca(getParametriRicerca(request, session));

		return WSCache.commonsServer.recuperaTransazioniGrouped(recuperaTransazioniRequest, request);
	}

	private RecuperaTransazioniGroupedOneriResponse getListaTransazioniGroupedOneri(HttpServletRequest request, HttpSession session) throws FaultType, RemoteException {

		RecuperaTransazioniGroupedOneriRequest recuperaTransazioniGroupedOneriRequest = new RecuperaTransazioniGroupedOneriRequest();

		recuperaTransazioniGroupedOneriRequest.setParametriRicerca(getParametriRicerca(request,session));

		return WSCache.commonsServer.recuperaTransazioniGroupedOneri(recuperaTransazioniGroupedOneriRequest, request);
	}

	private RecuperaTransazioniGroupedSuccessResponse getListaTransazioniGroupedSuccess(HttpServletRequest request, HttpSession session) throws FaultType, RemoteException {
		RecuperaTransazioniGroupedSuccessRequest recuperaTransazioniRequest = new RecuperaTransazioniGroupedSuccessRequest();

		recuperaTransazioniRequest.setParametriRicerca(getParametriRicerca(request, session));
		return WSCache.commonsServer.recuperaTransazioniGroupedSuccess(recuperaTransazioniRequest, request);
	}


	private String getListaTransazioniPdf(HttpServletRequest request, HttpSession session) throws FaultType, RemoteException {
		GeneraPdfTransazioniResponse generaPdfTransazioniResponse;
		GeneraPdfTransazioniRequest recuperaTransazioniRequest = new GeneraPdfTransazioniRequest();

		recuperaTransazioniRequest.setParametriRicerca(getParametriRicerca(request,session));
		generaPdfTransazioniResponse = WSCache.commonsServer.generaPdfTransazioni(recuperaTransazioniRequest, request);
		return generaPdfTransazioniResponse.getFileName();
	}

	private String getListaTransazioniCsv(HttpServletRequest request,  HttpSession session) throws FaultType, RemoteException {
		RecuperaTransazioniCsvResponse recuperaTransazioniCsvResponse;
		RecuperaTransazioniCsvRequest recuperaTransazioniRequest = new RecuperaTransazioniCsvRequest();

		recuperaTransazioniRequest.setParametriRicerca(getParametriRicerca(request, session));
		recuperaTransazioniCsvResponse = WSCache.commonsServer.recuperaTransazioniCsv(recuperaTransazioniRequest, request);
		return recuperaTransazioniCsvResponse.getFile();
		
	}

	//Giulia
		private String getListaTransazioniDownloadCsv(HttpServletRequest request,  HttpSession session) throws FaultType, RemoteException {
			RecuperaTransazioniCsvResponse recuperaTransazioniCsvResponse;
			RecuperaTransazioniCsvRequest recuperaTransazioniRequest = new RecuperaTransazioniCsvRequest();
			recuperaTransazioniRequest.setParametriRicerca(getParametriRicerca(request, session));
			recuperaTransazioniCsvResponse = WSCache.commonsServer.recuperaTransazioniCsv(recuperaTransazioniRequest, request);
			return recuperaTransazioniCsvResponse.getNomeFileDwn();
		}


	private String recuperaIdTerminaleAtm(String tx_id_transaz_atm) {
		if (StringUtility.isEmpty(tx_id_transaz_atm))
			return null;

		String[] values = tx_id_transaz_atm.trim().split("-"); 
		if (values == null || values.length <= 0)
			return null;

		return values[0].trim();
	}

	private String recuperaIdTransazioneAtm(String tx_id_transaz_atm) {
		if (StringUtility.isEmpty(tx_id_transaz_atm))
			return null;

		String[] values = tx_id_transaz_atm.trim().split("-"); 
		if (values == null || values.length < 2)
			return null;

		return values[1].trim();
	}

	private PageInfo getPageInfo(RecuperaTransazioniResponseTypePageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPages());
		return pageInfo;
	}



	private ParametriRicercaMonitoraggio getParametriRicerca (HttpServletRequest request, HttpSession session) {
		ParametriRicercaMonitoraggio parametriRicerca = new ParametriRicercaMonitoraggio();

		parametriRicerca.setTx_societa(getParamCodiceSocieta());
		parametriRicerca.setTx_provincia(siglaProvincia);
		parametriRicerca.setTx_utente(getParamCodiceUtente());
		parametriRicerca.setTx_ente(getParamCodiceEnte());
		parametriRicerca.setTx_canale_pagamento(isNull(request.getAttribute(Field.TX_CANALE_PAGAMENTO.format())));
		parametriRicerca.setTx_codice_transazione(isNull(request.getAttribute(Field.TX_CODICE_TRANSAZIONE.format())));
		parametriRicerca.setTx_data_a(getDataByPrefix("tx_data_a",request));
		parametriRicerca.setTx_data_da(getDataByPrefix("tx_data_da",request));
		if (request.getAttribute("tx_data_accr_da")==null ||
				request.getAttribute("tx_data_accr_a")==null) {
			parametriRicerca.setTx_data_accr_a("");
			parametriRicerca.setTx_data_accr_da("");
		} else {
			parametriRicerca.setTx_data_accr_a(getDataByPrefix("tx_data_accr_a",request));
			parametriRicerca.setTx_data_accr_da(getDataByPrefix("tx_data_accr_da",request));	
		}			
		parametriRicerca.setTx_id_bollettino(isNull(request.getAttribute(Field.TX_ID_BOLLETTINO.format())));
		String importo_mon_da = isNull(request.getAttribute(Field.TX_IMPORTO_DA.format()));
		String importo_mon_a = isNull(request.getAttribute(Field.TX_IMPORTO_A.format()));
		importo_mon_da = importo_mon_da.replaceAll("\\.", "");
		importo_mon_da = importo_mon_da.replaceAll(",", ".");
		importo_mon_a = importo_mon_a.replaceAll("\\.", "");
		importo_mon_a = importo_mon_a.replaceAll(",", ".");
		parametriRicerca.setTx_importo_a(importo_mon_a);
		parametriRicerca.setTx_importo_da(importo_mon_da);
		parametriRicerca.setTx_indirizzo_ip(isNull(request.getAttribute(Field.TX_INDIRIZZO_IP.format())));
		parametriRicerca.setTx_mostra(isNull(request.getAttribute(Field.TX_MOSTRA.format())));
		parametriRicerca.setTx_stato_rendicontazione(isNull(request.getAttribute(Field.TX_STATO_RENDICONTAZIONE.format())));
		parametriRicerca.setTx_stato_riconciliazione(isNull(request.getAttribute(Field.TX_STATO_RICONCILIAZIONE.format())));
		parametriRicerca.setTx_strumento(isNull(request.getAttribute(Field.TX_STRUMENTO.format())));
		//Giulia 8/05/2014 INIZIO
		//parametriRicerca.setTx_servizio(getTipologiaServizio(request,session));
		if(getTipologiaServizio(request,session)!=null && !getTipologiaServizio(request,session).equals("") && userBean.getProfile().equals("AMMI")){
		parametriRicerca.setTx_servizio(getTipologiaServizio(request,session).substring(0, 3));
		if((getTipologiaServizio(request,session)).length()>0){
			parametriRicerca.setTx_societa(getTipologiaServizio(request,session).substring(4));
		}
		}
		else {
			if (getTipologiaServizio(request,session)!=null && !getTipologiaServizio(request,session).equals("") && getTipologiaServizio(request,session).indexOf("_", 0) != -1) {
				String serv = getTipologiaServizio(request,session);
				serv= serv.replace("'", "");
				parametriRicerca.setTx_servizio(serv.substring(0, 3));
			}
			else
			parametriRicerca.setTx_servizio(getTipologiaServizio(request,session));
		}
		//Giulia 8/05/2014 FINE
		parametriRicerca.setTx_tipo_carta(isNull(request.getAttribute(Field.TX_TIPO_CARTA.format())));
		parametriRicerca.setTx_user_email(isNull(request.getAttribute(Field.TX_USER_EMAIL.format())));
		parametriRicerca.setTx_user_sms(isNull(request.getAttribute(Field.TX_USER_SMS.format())));

		//PG170010_001 GG 15022017 - inizio
		//parametriRicerca.setTx_chiave_quadratura(isNullInt(request.getAttribute(Field.TX_CHIAVE_QUADRATURA.format())));
		parametriRicerca.setTx_chiave_quadratura(0);
		//PG170010_001 GG 15022017 - fine
		
		String tx_id_transaz_atm = isNull(request.getAttribute(Field.TX_ID_TRANSAZ_ATM.format()));
		if (tx_id_transaz_atm.trim().length()>0) {	//PG160230_001 GG 22112016
			parametriRicerca.setTx_idTerminaleAtm(recuperaIdTerminaleAtm(tx_id_transaz_atm));
			parametriRicerca.setTx_idTransazioneAtm(recuperaIdTransazioneAtm(tx_id_transaz_atm));
		}
		//PG160230_001 GG 22112016 - inizio
		parametriRicerca.setTx_idTerminaleTransazioneAtm(tx_id_transaz_atm);
		String tx_codice_IUR = isNull(isNull(request.getAttribute(Field.TX_CODICE_IUR.format())));
		if (tx_codice_IUR.trim().length()>0) {
			parametriRicerca.setTx_idTransazioneAtm(tx_codice_IUR);
		}
		//PG160230_001 GG 22112016 - fine

		parametriRicerca.setTx_chiave_rendicontazione(isNull(request.getAttribute(Field.TX_CHIAVE_RENDICONTAZIONE.format())));
		parametriRicerca.setTx_id_terminale_pos_fisico(isNull(request.getAttribute(Field.TX_ID_TERMINALE_POS_FISICO.format())));
		parametriRicerca.setTx_codice_IUV(isNull(request.getAttribute(Field.TX_CODICE_IUV.format())));	//PG150180_001 GG
		
		//PG170010_001 GG 15022017 - inizio
		parametriRicerca.setTx_idFlussoQuadratura(isNull(request.getAttribute(Field.TX_CHIAVE_QUADRATURA.format())));
		//PG170010_001 GG 15022017 - fine
		
		parametriRicerca.setTx_recuperate(isNull(request.getAttribute(Field.TX_RECUPERATE.format())));  //PG200050_001 SB
		
		parametriRicerca.setTx_codice_fiscale(isNull(request.getAttribute(Field.TX_CODICE_FISCALE.format()))); // PAGONET-437
		
		return parametriRicerca;
	}

	private String controlloDate(HttpServletRequest request)
	{
		
			String sDataDa = getDataByPrefix("tx_data_da",request);
			String sDataA = getDataByPrefix("tx_data_a",request);
			boolean sDataDaIsNullOrEmpty = sDataDa==null || sDataDa.equals("");
			boolean sDataAIsNullOrEmpty = sDataA==null || sDataA.equals("");
			
			Calendar dataDa = parseDate(sDataDa, "yyyy-MM-dd");
			Calendar dataA = parseDate(sDataA, "yyyy-MM-dd");
			
	//		Calendar dataDa = getCalendar(request, Field.TX_DATA_DA.format());
	//      Calendar dataA = getCalendar(request, Field.TX_DATA_A.format());
			String sDataAccrDa = getDataByPrefix("tx_data_accr_da",request);
			String sDataAccrA = getDataByPrefix("tx_data_accr_a",request);
			
			boolean sDataAccrDaIsNullOrEmpty = sDataAccrDa==null || sDataAccrDa.equals("");
			boolean sDataAccrAIsNullOrEmpty = sDataAccrA==null || sDataAccrA.equals("");
			
			Calendar dataAccrDa = parseDate(sDataAccrDa, "yyyy-MM-dd");
			Calendar dataAccrA = parseDate(sDataAccrA, "yyyy-MM-dd");
			
		
//	        Calendar dataAccrDa =getCalendar(request, Field.TX_DATA_ACCR_DA.format());
//	        Calendar dataAccrA = getCalendar(request, Field.TX_DATA_ACCR_A.format());
	        
	        if(sDataDaIsNullOrEmpty && sDataAccrDaIsNullOrEmpty)
	        	return "Data Transazione da e/o Data Accredito obbligatoria";
	        if(sDataAIsNullOrEmpty)
	        	dataA = Calendar.getInstance();               
	        if (!sDataDaIsNullOrEmpty && dataDa.after(dataA))
	        	return "La Data Transazione da deve essere antecedente o uguale alla Data Transazione a";
	        if(!sDataDaIsNullOrEmpty) {
		        dataDa.add(Calendar.DAY_OF_MONTH, 90);
		        if (dataDa.before(dataA))
		        	return "Il massimo range di giorni consentito  di 90 giorni";
	        }
	        
	        
	        if(!sDataAccrDaIsNullOrEmpty && sDataAccrAIsNullOrEmpty)
	        	dataAccrA = Calendar.getInstance();               
	        if (!sDataAccrDaIsNullOrEmpty && dataAccrDa.after(dataAccrA))
	        	return "La Data Accredito da deve essere antecedente o uguale alla Data Accredito a";
	        if(!sDataAccrDaIsNullOrEmpty) {
	        	dataAccrDa.add(Calendar.DAY_OF_MONTH, 90);
		        if (dataAccrDa.before(dataAccrA))
		        	return "Il massimo range di giorni consentito  di 90 giorni";
	        }
        
        return null;
        
	}

}
