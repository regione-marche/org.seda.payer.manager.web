package org.seda.payer.manager.monitoraggiosoap.actions;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import com.seda.j2ee5.jndi.JndiProxy;
import org.seda.payer.manager.util.LoggerUtil;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.ws.WSCache;

import com.esed.log.req.dati.LogWin;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.helper.Helper;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.NotificaSoap;
import com.seda.payer.core.dao.NotificheSoap;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.integraente.webservice.dati.NotificaPagamentoRequest;
import com.seda.payer.integraente.webservice.dati.PagamentoSpontaneo;
import com.seda.payer.integraente.webservice.dati.TipoCDS;
import com.seda.payer.integraente.webservice.dati.TipoSpontaneo;
import com.seda.payer.integraente.webservice.source.IntegraEnteNotificaSOAPBindingStub;
import com.seda.payer.pgec.webservice.commons.dati.BeanIV;
import com.seda.payer.pgec.webservice.commons.dati.ConfigPagamentoSingleRequest;
import com.seda.payer.pgec.webservice.commons.dati.ConfigPagamentoSingleResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneRequestType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneResponseType; 

public class NotificaAction extends BaseMonitoraggioSoapAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException 
	{
		super.service(request);
		
		String nomeForm = "";
		if (request.getAttribute("form") != null)
			nomeForm = (String)request.getAttribute("form");
	
		if (request.getAttribute("notificaChiaveTransazione") != null && !request.getAttribute("notificaChiaveTransazione").equals(""))
		{
			
			String chiaveTransazione = (String)request.getAttribute("notificaChiaveTransazione");
			String chiaveDettaglioTransazione = (String)request.getAttribute("notificaChiaveDettaglioTransazione");
			
			try (Connection conn = new JndiProxy().getSqlConnection(null, dataSourceName, true)) {

				NotificheSoap notificheSoap= new NotificheSoap(conn, payerDbSchema);
				NotificaSoap detailNotificaSoap = new NotificaSoap();
				
				detailNotificaSoap = notificheSoap.doDetail(chiaveTransazione,chiaveDettaglioTransazione);
				
				if ( detailNotificaSoap == null ) {
					setFormMessage("monitoraggioSoapDetailForm","Errore caricamento dettaglio notifica soap" , request);
				}
			
				if (!chiaveTransazione.equals("")){

					com.seda.payer.integraente.webservice.dati.NotificaPagamentoResponse response = null;
					
					RecuperaTransazioneResponseType res = recuperaTransazione(detailNotificaSoap.getChiaveTransazione(),request);
					BeanIV[] listIV = res.getListIV();

					int contatore_rinotifiche = 0; 
					for (BeanIV beanIV : listIV) {
						// devo inviare la notifica solamente al bollettino che sto ciclando sulla tabella NEX
						if(detailNotificaSoap.getChiaveDettaglioTransazione().equals(beanIV.getChiave_transazione_dettaglio()))
						{ 
							contatore_rinotifiche++; 
							System.err.println("beanIV.getChiave_transazione_dettaglio() = "+ beanIV.getChiave_transazione_dettaglio());
							String numeroAvvisoPagoPA = "";
							PagamentoSpontaneo pagamentoSpontaneo = null;
							if(beanIV.getTipo_bollettino().equals("SPOM") 
									|| beanIV.getTipo_bollettino().equals("CDSM") 
									|| (beanIV.getTipo_bollettino().equals("CDSA") && (beanIV.getCodice_bollettino_premarcato_mav() == null || beanIV.getCodice_bollettino_premarcato_mav().length() < 15))
								) { 
								
								//Blindatura su codice bollettino valorizzato erroneamente
								if(beanIV.getTipo_bollettino().equals("CDSA") && beanIV.getCodice_bollettino_premarcato_mav() != null && beanIV.getCodice_bollettino_premarcato_mav().length() < 15)
									beanIV.setCodice_bollettino_premarcato_mav("");
								System.out.println("Trovato bollettino SPONTANEO o MULTA SENZA AVVISO");
								
								pagamentoSpontaneo = new PagamentoSpontaneo();
								String denominazione = beanIV.getDenominazione();
								String indirizzo = beanIV.getIndirizzo();
				                String codiceBelfioreComune = beanIV.getCodice_ente_comune_domicilio_fiscale();
				                String siglaProvincia = beanIV.getProvincia();
				                String CAP = beanIV.getCap();
				                
				                pagamentoSpontaneo.setDenominazione(denominazione);
				                pagamentoSpontaneo.setIndirizzo(indirizzo);
				                if(!codiceBelfioreComune.trim().equals("")) {
				                	pagamentoSpontaneo.setCodiceBelfioreComune(codiceBelfioreComune);
				                }
				                pagamentoSpontaneo.setSiglaProvincia(siglaProvincia);
				                pagamentoSpontaneo.setCAP(CAP);
				                
				                ConfigPagamentoSingleRequest configPagamentoSingleRequest = new ConfigPagamentoSingleRequest(beanIV.getCodice_societa(), beanIV.getCodice_utente(), beanIV.getChiave_ente_con(), beanIV.getCodice_tipologia_servizio(), "WEB");
								ConfigPagamentoSingleResponse  configPagamentoSingleResponse = recuperaFunzioneEnte(configPagamentoSingleRequest, request);
				                String descrizioneTipologiaServizio = (configPagamentoSingleResponse.getConfigPagamento()!=null && configPagamentoSingleResponse.getConfigPagamento().getDescFunzionePagamento()!=null)?configPagamentoSingleResponse.getConfigPagamento().getDescFunzionePagamento():"";
				                pagamentoSpontaneo.setDescrizioneTipologiaServizio(descrizioneTipologiaServizio);
				             
								if (beanIV.getTipo_bollettino().equals("CDSM")) {
									String numeroVerbale = beanIV.getCodice_bollettino_premarcato_mav(); // len 15
									String targa = beanIV.getTarga(); // len 10
									SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
									
									TipoCDS tipoCDS = new TipoCDS(numeroVerbale, sdf.format(beanIV.getData_sanzione().getTime()), targa);
									pagamentoSpontaneo.setTipoCDS(tipoCDS);
								} else if (beanIV.getTipo_bollettino().equals("CDSA")) {
									String numeroVerbale = beanIV.getNumero_documento();
									String targa = beanIV.getTarga();
									SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
									TipoCDS tipoCDS = new TipoCDS(numeroVerbale, sdf.format(beanIV.getData_sanzione().getTime()), targa);
									pagamentoSpontaneo.setTipoCDS(tipoCDS);
									
									System.out.println("Trovato CDSA: " + numeroVerbale + " - " +  sdf.format(beanIV.getData_sanzione().getTime())+ " - " + targa);

								} else {
									 System.out.println("inizio Spontaneo");
									 String note = beanIV.getNote_premarcato();
								  
									  System.out.println("note = " + note);
									  
									  String annoRiferimento = "";
									  String cespite ="";
									  
									  String cosa = "\\|";
									  String[] notesplit = note.split(cosa);
	
									  String causaleServizio = notesplit[0]; //len 256
									  
									  if(notesplit.length>1)
										  annoRiferimento = notesplit[1]; // len 4
									  if(notesplit.length>2)
										  cespite = notesplit[2]; //len 256
								  
									  System.out.println("annoRiferimento = " + annoRiferimento);
									  System.out.println("causaleServizio = " + causaleServizio);
									  System.out.println("cespite = " + cespite);
									  
									  TipoSpontaneo tipoSpontaneo = new TipoSpontaneo();
									  if(!annoRiferimento.trim().equals("")) {
										  tipoSpontaneo.setAnnoRiferimento(annoRiferimento.trim());
									  }
									  if(!causaleServizio.trim().equals("")) {
										  tipoSpontaneo.setCausaleServizio(causaleServizio.trim());
									  }
									  if(!cespite.trim().equals("")) {
										  tipoSpontaneo.setCespite(cespite.trim());
									  }
									  pagamentoSpontaneo.setTipoSpontaneo(tipoSpontaneo);
								}
							
							
							} else {
								numeroAvvisoPagoPA = beanIV.getCodice_bollettino_premarcato_mav();
							}
							
	
							//Preparo chiamata a WS Esterno (Sanit√†)
							
							if (detailNotificaSoap.getUrlPortale() == null || detailNotificaSoap.getUrlPortale().equals("")) {
								throw new Exception("Errore: impossibile inviare una chiamata al ws esterno ( non configurato ) "); 
							}
							
							IntegraEnteNotificaSOAPBindingStub serviceIntegraEnteNotifica = new IntegraEnteNotificaSOAPBindingStub(new java.net.URL(detailNotificaSoap.getUrlPortale()), null);
							NotificaPagamentoRequest notificaPagamentoRequest = new NotificaPagamentoRequest();
							notificaPagamentoRequest.setCodiceTransazionePagonet(detailNotificaSoap.getChiaveTransazione());
							notificaPagamentoRequest.setCodiceDettaglioTransazionePagonet(beanIV.getChiave_transazione_dettaglio());//In update non serve aggiornare questo campo
							notificaPagamentoRequest.setCodiceEnte(detailNotificaSoap.getCodiceEnte().trim());
							notificaPagamentoRequest.setCodiceFiscale(detailNotificaSoap.getCodiceFiscale());
							notificaPagamentoRequest.setNumeroDocumento(detailNotificaSoap.getNumeroDocumento());
							
							 
							notificaPagamentoRequest.setImportoPagato(detailNotificaSoap.getImportoPagato().longValue());
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
							notificaPagamentoRequest.setDataPagamento(sdf1.format(detailNotificaSoap.getDataPagamento().getTime()));
							if(pagamentoSpontaneo!=null)
								notificaPagamentoRequest.setPagamentoSpontaneo(pagamentoSpontaneo);
							else
								notificaPagamentoRequest.setNumeroAvvisoPagoPA(numeroAvvisoPagoPA);
							 
							if(!detailNotificaSoap.getUrlPortale().toLowerCase().contains("integraentesanita")) {
							 notificaPagamentoRequest.setIdentificativoUnivocoRiscossione(beanIV.getCodiceIUR());
							 notificaPagamentoRequest.setTassonomia(beanIV.getTassonomia());  
							 notificaPagamentoRequest.setIdentificativoUnivocoVersamento(beanIV.getNodoSpcIuv()); 
							} 
							
							//PG22XX09_SB3 - inizio
							if(detailNotificaSoap.getUrlPortale().contains("jcitygov-pagopa")) {
								notificaPagamentoRequest.setIuvRpt(beanIV.getNodoSpcIuv());
							}
							//PG22XX09_SB3 - fine
							
							//forzatura UTF-8
							notificaPagamentoRequest.setRtXML(new String(detailNotificaSoap.getXmlRicevuta().getBytes("UTF-8"), "UTF-8"));
							System.out.println("RT presente in DB e letta in java = " + notificaPagamentoRequest.getRtXML());
							
							
							System.out.println("notificaPagamentoRequest.getCodiceDettaglioTransazionePagonet = " + notificaPagamentoRequest.getCodiceDettaglioTransazionePagonet());

							java.util.Date dataInizio = new java.util.Date();
				            Timestamp dIni = new Timestamp(dataInizio.getTime());
				            
				    		LogWin notificaLog = new LogWin();
				            String esitoLog = "";
				            String errorMessageLog = "";
				            String xmlInputLog = "";
				            String xmlOutputLog = "";
				            
			            	try {
					        	
			            		xmlInputLog = getStringXMLofObject(notificaPagamentoRequest) != null ? 
			            				getStringXMLofObject(notificaPagamentoRequest) :  
			            					"getStringXMLofObject(notificaPagamentoRequest) vuota";
					        	
			            		response = serviceIntegraEnteNotifica.notificaPagamento(notificaPagamentoRequest);

					        	System.out.println("Notifica pagamento esterno eseguita con esito: " + response.getRisultato().getRetcode().getValue());
					        	
								esitoLog = response.getRisultato().getRetcode().getValue() + " : " + response.getRisultato().getRetmessage().toString(); 
					    		
								String retCode = response.getRisultato().getRetcode().getValue();
								
								//Aggiorno tabella delle notifiche 
								boolean bOk= notificheSoap.doUpdatePostRinotifica(detailNotificaSoap,numeroAvvisoPagoPA, retCode);
		
								if (!bOk) {
									setErrorMessage("Errore durante aggiornamento tabella notifiche");
								}
								
								pagamentoSpontaneo = null;
								notificaPagamentoRequest = null;
							
							} catch (Exception e) {
								esitoLog= "KO : Exception";
								setErrorMessage(e.getMessage());
								errorMessageLog = e.getMessage();
							} finally {

								xmlOutputLog = response != null && getStringXMLofObject(response) != null ? 
										getStringXMLofObject(response) : "Errore: mancanza xml output";
								
								notificaLog.setDataInizioChiamata(dIni);
								notificaLog.setTipoChiamata("notificaPagamento");
								notificaLog.setOperatoreInserimento("notificaPagamentoManager");
								notificaLog.setEsito(esitoLog);
								notificaLog.setMessaggioErrore(errorMessageLog);
								notificaLog.setXmlRequest(xmlInputLog);
								notificaLog.setXmlResponse(xmlOutputLog);
								notificaLog.setCodiceUtente(beanIV.getCodice_utente());
								notificaLog.setCodiceSocieta(beanIV.getCodice_societa());
								notificaLog.setCodiceEnte(beanIV.getChiave_ente_ent().trim());
								notificaLog.setIdDominio("");
								notificaLog.setBollettino(beanIV.getCodice_bollettino_premarcato_mav());
								notificaLog.setCodiceFiscale(beanIV.getCodice_fiscale());
								notificaLog.setMessaggioErrore(errorMessageLog);
								notificaLog.setEsito(notificaLog.getEsito());
								notificaLog.setDbSchemaCodSocieta(notificaLog.getCodiceUtente());
								
								
								inizializzaSalvaLoggingWinProcessNotificaPagamenti(detailNotificaSoap, notificaLog, detailNotificaSoap.getCodiceUtente(),request);
							}
							
						}
				
					}
					
					if (contatore_rinotifiche == 0) { 
						setErrorMessage("Attenzione non esiste match per la chiave di dettaglio transazione per questa notifica");
					}
				} else {
					setErrorMessage("Chiave transazione non valida");				}
					
				} catch (DaoException e) {
					setErrorMessage(e.getMessage());	
					e.printStackTrace();
				} catch (SQLException e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				} catch(Exception e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				}
			} else {
				setErrorMessage("Parametri per la rinotifica mancanti");
			}
			
			//se non ho settato nessun messaggio
			if ((getMessage() == null || getMessage().equals("")) &&
					(getErrorMessage() == null || getErrorMessage().equals(""))) {
				setFormMessage(nomeForm, "La rinotifica Ë stata inizializzata correttamente per la transazione selezionata.", request);
			} else {
				setFormMessage(nomeForm, getErrorMessage(), request);

			}
				
			request.setAttribute("txtIdTransazione", request.getAttribute("notificaChiaveTransazione"));
			request.setAttribute("action", (String)request.getAttribute("action"));
			request.setAttribute("vista", (String)request.getAttribute("vista"));

		return null;
		
		
	}

	private RecuperaTransazioneResponseType recuperaTransazione(String chiaveTransazione, HttpServletRequest httpRequest) throws Exception {
    	RecuperaTransazioneResponseType sRes = null;
    	try {
    		
    		RecuperaTransazioneRequestType req = new RecuperaTransazioneRequestType();
			req.setChiave_transazione(chiaveTransazione);
		
			sRes = WSCache.commonsServer.recuperaTransazione(req, httpRequest);
			
    	} catch (Exception e) {
    		throw new Exception("Errore in recuperaTransazione: " + e.getMessage());
    	}
    	return sRes;
    }
	

	private ConfigPagamentoSingleResponse recuperaFunzioneEnte(ConfigPagamentoSingleRequest in , HttpServletRequest httpRequest) throws Exception {
		ConfigPagamentoSingleResponse sRes = null;
    	try {
			
			sRes = WSCache.commonsServer.recuperaFunzioneEnte(in, httpRequest);
				
    	} catch (Exception e) {
    		throw new Exception("Errore in recuperaFunzioneEnte: " + e.getMessage());
    	}
    	return sRes;
    }
	
	public void inizializzaSalvaLoggingWinProcessNotificaPagamenti( Object res, LogWin notificaLog , String modelCodUtente, HttpServletRequest request)  {

		CallableStatement callableStatement;
		ResultSet data = null;
		
		String codiceFiscale = "";
		String cutecute = "";
		String codiceEnte = "";
		String numeroBollettino = "";
		String codiceSocieta = "";
		
		try {

			PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
			
			java.util.Date dataFine = new java.util.Date();
			Timestamp dFin = new Timestamp(dataFine.getTime());
			
			if (res != null ) {
				Method[] methods = res.getClass().getMethods();
				
				for (int i = 0; i < methods.length; i++) { 
					Method m = methods[i];
	//				printRow(myPrintingKeyPAG_SYSOUT, "METODO: " + m.getName());
					
					if (m.getName() == "getCodiceSocieta" || m.getName() == "getCodice_societa") {
						
						Object c = m.invoke(res);
						codiceSocieta = c.toString().trim().length()>0 && c.toString() != null
								? c.toString().trim() 
								: "";
					}
	
					if (m.getName() == "getCodiceUtente" || m.getName() == "getCodice_utente") {
						
						Object c = m.invoke(res);
						cutecute =  c.toString().trim().length()>5 
								? c.toString().trim().substring(0, 5) 
								: c.toString().trim();
					}
	//				attenzione chiave a 5 cifre necessaria questo Ë il codice pubblico
					if (m.getName() == "getCodiceEnte" || m.getName() == "getChiave_ente_ent") {
						
						Object c = m.invoke(res);
						codiceEnte =  c.toString().trim().length()>0 && c.toString() != null
								? c.toString().trim()
								: "";
					}
					if (m.getName() == "getCodiceFiscale" || m.getName() == "getCodice_fiscale") {
						
						Object c = m.invoke(res);
						codiceFiscale =  c.toString().trim().length()>0 && c.toString() != null
								? c.toString().trim() 
								: "";
					}
					if (m.getName() == "getIdentificativoBollettino" || m.getName() == "getCodice_bollettino_premarcato_mav" || m.getName() == "getNumeroAvvisoPagoPA") {
						
						Object c = m.invoke(res);
						numeroBollettino =  c.toString().trim().length()>0 && c.toString() != null
								? c.toString().trim() 
								: "";
					}
				
				
				}
			} else {
				cutecute = modelCodUtente;
			}
		
			
			if ( codiceEnte != null && codiceEnte != "" && codiceEnte.length() == 10) {

				try (Connection conn = new JndiProxy().getSqlConnection(null, dataSourceName, true)) {
					callableStatement = Helper.prepareCall(conn, payerDbSchema, "PYANESP_SEL");
					callableStatement.setString(1, codiceEnte);
					
					if (callableStatement.execute()) {
					    data = callableStatement.getResultSet();
					    if (data.next())
					    	codiceEnte =  data.getString("ANE_CANECENT");
					}
				} catch (SQLException e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				} catch(Exception e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				}
				
				
				
			} 
			
			LogWin logWin = new LogWin();
			logWin.setTipoChiamata(notificaLog.getTipoChiamata());
			logWin.setCodiceUtente(cutecute);
			logWin.setCodiceSocieta(codiceSocieta);
			logWin.setCodiceEnte(codiceEnte);
			logWin.setIdDominio("");
			logWin.setBollettino(numeroBollettino);
			logWin.setCodiceFiscale(codiceFiscale);
			logWin.setXmlRequest(notificaLog.getXmlRequest());
			logWin.setXmlResponse(notificaLog.getXmlResponse());
			logWin.setDataInizioChiamata(notificaLog.getDataInizioChiamata());
			logWin.setDataFineChiamata(dFin);
			logWin.setMessaggioErrore(notificaLog.getMessaggioErrore() != null && notificaLog.getMessaggioErrore().trim().length() > 0? notificaLog.getMessaggioErrore().trim() : "");
			logWin.setEsito(notificaLog.getEsito());
			logWin.setOperatoreInserimento(notificaLog.getOperatoreInserimento());
			logWin.setDbSchemaCodSocieta(cutecute);

			LoggerUtil winLogger = new LoggerUtil();
			winLogger.saveWinLog(logWin, configuration);
		    
		}  
		catch (Exception e) {
		    
		    e.printStackTrace();
		    System.out.println("Errore in saveWinLog: " + e.getMessage());
			
		}catch (Throwable e) {
		    
		    e.printStackTrace();
		    System.out.println("Errore in saveWinLog: " + e.getMessage());	
		} 
	}
	
	

}
