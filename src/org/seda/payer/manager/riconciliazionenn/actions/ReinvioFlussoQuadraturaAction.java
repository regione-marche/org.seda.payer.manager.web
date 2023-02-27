package org.seda.payer.manager.riconciliazionenn.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.RemoteException;
import java.lang.Long;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;

import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.j2ee5.maf.core.action.ActionException;

import com.seda.payer.integraente.webservice.dati.InviaFlussoNodoRequestType;
import com.seda.payer.integraente.webservice.dati.InviaFlussoNodoResponseType;
import com.seda.payer.integraente.webservice.source.RendicontaFlussoNodoSOAPBindingStub;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailRequest;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

import com.seda.payer.pgec.webservice.commons.dati.AggiornaEsitoQuadraturaNodoRequest;
import com.seda.payer.pgec.webservice.commons.dati.AggiornaEsitoQuadraturaNodoResponse;

public class ReinvioFlussoQuadraturaAction extends BaseManagerAction {
	
	private static final long serialVersionUID = 1L;
	private static PropertiesTree propertiesTree;
	
	private String cutecute = "", urlService = "", idFlusso = "", xmlread = "", fileName = "", idDominio="";
	private File cartellaOutput;//, cartellaInput;
	private Long keyQuadratura = null;
	
	protected String codiceSocieta="";
	protected String codiceProvincia="";
	protected String codiceUtente="";
	protected String dbSchemaCodSocieta;
	
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		HttpSession session = request.getSession();
		super.service(request);
		
		fileName = request.getParameter("FileNameFlusso");
		idDominio = request.getParameter("idDominio"); //Codice fiscale o Partita IVA
		cutecute = request.getParameter("cutecute");
		idFlusso = request.getParameter("idFlusso");
		keyQuadratura = Long.parseLong(request.getParameter("keyQuadratura"));
		System.out.println("keyQuadratura: "+keyQuadratura);
		String keyEnte = (String)request.getParameter("keyEnte");
		String codSoc = (String)request.getParameter("codSoc");
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA); 
		
		impostaFiltri(request,session);
		
		try {
			//EnteByCFResponse enteRes = WSCache.enteServer.getEnteByCF(new EnteByCFRequest(cutecute, idDominio), request);
			EnteDetailResponse enteRes = WSCache.enteServer.getEnte(new EnteDetailRequest(codSoc, cutecute, keyEnte), request);
			if(enteRes.getEnte() != null && !enteRes.getEnte().equals("")) {
				if(enteRes.getEnte().getUrlWebServiceFlussoRendicontazioneNodo() != null && !enteRes.getEnte().getUrlWebServiceFlussoRendicontazioneNodo().equals("")) {
					urlService = enteRes.getEnte().getUrlWebServiceFlussoRendicontazioneNodo();
					//inizio LP PG21XX04 Leak
					BufferedReader b1 = null;
					//fine LP PG21XX04 Leak
					try {
						RendicontaFlussoNodoSOAPBindingStub service = new RendicontaFlussoNodoSOAPBindingStub(new URL(urlService), null);
						System.out.println("Invio ad URL urlService: " + urlService);	
						propertiesTree = (PropertiesTree)context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
//						 if(propertiesTree.getProperty(PropertiesPath.quadraturaPSPInputPath.format(cutecute))!=null)
//							 cartellaInput = new File(propertiesTree.getProperty(PropertiesPath.quadraturaPSPInputPath.format(cutecute)));				
						if(propertiesTree.getProperty(PropertiesPath.quadraturaPSPOutputPath.format(dbSchemaCodSocieta))!=null) {
							cartellaOutput = new File(propertiesTree.getProperty(PropertiesPath.quadraturaPSPOutputPath.format(dbSchemaCodSocieta)));
					 	}
										
						String fileDaAllegare = cartellaOutput.getAbsolutePath().concat(File.separator).concat(fileName.concat(".bak"));
						
						//inizio conversione del file xml in stringa
						File inxml = new File(fileDaAllegare);
		    			long len = inxml.length();
		    			if(len > 0) {
							//FileReader r = new FileReader(fileDaAllegare);
							//BufferedReader b1 = new BufferedReader(r);
							FileInputStream r = new FileInputStream(inxml);
							//inizio LP PG21XX04 Leak
							//BufferedReader b1 = new BufferedReader(new InputStreamReader(r, "ISO-8859-1"));
							b1 = new BufferedReader(new InputStreamReader(r, "ISO-8859-1"));
							//fine LP PG21XX04 Leak
							int max_value_in_byte = Integer.MAX_VALUE;
							if(len <= max_value_in_byte) {
								//Dimensione file massima MB:2047 circa 2GB ma l'esecuzione senza errori
								//dipende dai limiti di memoria della VM e dell'heap.
								//Se si vogliono evitare problemi si puo' usare un valore < Integer.MAX_VALUE
								//Ad esempio x 100MB inserire max_value_in_byte = 100 * 1024 * 1024;
								char[] cbuf = new char[(int) len];
								b1.read(cbuf);
								b1.close();
								//inizio LP PG21XX04 Leak
								b1 = null;
								//fine LP PG21XX04 Leak
								r.close();
								xmlread = new String(cbuf);
							} else {
								System.out.println("Dimensione file flusso eccede i 2GB (" + len + ")");
							}
						}	
		    			
		    			//invio del file
						InviaFlussoNodoRequestType in = new InviaFlussoNodoRequestType(idFlusso, idDominio, xmlread);
						InviaFlussoNodoResponseType out = service.inviaFlussoNodo(in);
						
						System.out.println("ReinvioFlussoQuadraturaAction - InviaFlussoNodoResponseType - Retmessage : " + out.getResponse().getRetmessage());
				       	System.out.println("ReinvioFlussoQuadraturaAction - InviaFlussoNodoResponseType - RetCode : " + out.getResponse().getRetcode());
						if(out.getResponse().getRetcode().getValue().equals("00")) {
							String esitoQuadratura =  aggiornaEsitoQuadratura(keyQuadratura,"2","",request);
							System.out.println("EsitoQuadratura: "+esitoQuadratura);
							if(esitoQuadratura.equals("OK")) {
								setFormMessage("riconciliazioneTransazioniNodoForm", "Invio Eseguito" , request);
							}
						} else {
							String errore = "Invio Non Eseguito. RetCode : " + out.getResponse().getRetcode() + " - Retmessage :"+ out.getResponse().getRetmessage();
							setFormMessage("riconciliazioneTransazioniNodoForm", "Invio Non Eseguito" , request);
							aggiornaEsitoQuadratura(keyQuadratura,"1",errore,request);
						}
						//request.setAttribute("tx_button_cerca",1);
					} catch(Exception e){
						//Aggiorno l'esito in stato negativo
						String errore = "Invio Non Eseguito, Exception: " + e.getMessage();
		        		aggiornaEsitoQuadratura(keyQuadratura,"1", "Invio Non Eseguito. Errore generico",request); 
						e.printStackTrace();
						setFormMessage("riconciliazioneTransazioniNodoForm", errore , request);
					}
					//inizio LP PG21XX04 Leak
					finally {
						if(b1 != null) {
							try {
								b1.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					//fine LP PG21XX04 Leak
				} else {
					String errore = "Invio Non Eseguito - Url mancante";
					System.out.println("Manca Configurazione url destinatario per invio quadratura.");	
					//Aggiorno l'esito in stato negativo
	        		aggiornaEsitoQuadratura(keyQuadratura,"1", errore ,request); 
	        		setFormMessage("riconciliazioneTransazioniNodoForm", errore , request);
				}
			} else {

				String errore = "Invio Non Eseguito - Dati ente mancanti";
				System.out.println("Ente Response nullo");	
				//Aggiorno l'esito in stato negativo
        		aggiornaEsitoQuadratura(keyQuadratura,"1", errore,request); 
        		setFormMessage("riconciliazioneTransazioniNodoForm", errore , request);
			}	
		} catch (FaultType e) {
			e.printStackTrace();
			setFormMessage("riconciliazioneTransazioniNodoForm", e.getMessage(), request);
		} catch (RemoteException e) {
			e.printStackTrace();
			setFormMessage("riconciliazioneTransazioniNodoForm", e.getMessage() , request);
		}
		
		request.setAttribute(Field.ID_FLUSSO.format(), "");
		request.setAttribute(Field.KEY_QUADRATURA.format(), "");
		
		return null;
	}
	
	private String aggiornaEsitoQuadratura(Long chiaveQuadratura, String esito , String errore, HttpServletRequest request) {
		try {
			AggiornaEsitoQuadraturaNodoRequest in = new AggiornaEsitoQuadraturaNodoRequest();
			in.setChiaveQuadratura(chiaveQuadratura);
			in.setEsito(esito);
			in.setErrore(errore);
			
			AggiornaEsitoQuadraturaNodoResponse out = new AggiornaEsitoQuadraturaNodoResponse();
			out = WSCache.commonsServer.aggiornaEsitoQuadratura(in, request);
			System.out.println("aggiornaEsitoQuadratura return code: "+out.getResponse().getRetCode());
			System.out.println("aggiornaEsitoQuadratura return message: "+out.getResponse().getRetMessage()); 
			return out.getResponse().getRetMessage();
		} catch (Exception e) {
			System.out.println("aggiornaEsitoQuadratura exception: "+ e);
			e.printStackTrace();
			return "";
		}
	}
	
	
	protected void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{

		switch(getFiredButton(request)) 
		{
			case TX_BUTTON_SOCIETA_CHANGED:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				loadDDLUtente(request, session, getParamCodiceSocieta(), null, true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), null, true);
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, null,false);
				loadDDLUtente(request, session, getParamCodiceSocieta(), codiceProvincia,true);
				loadListaGatewayXml_DDL(request, session, null, null, false);
				break;
			
			case TX_BUTTON_UTENTE_CHANGED:
				loadProvinciaXml_DDL(request, session, null,false);
				loadDDLUtente(request, session, null, null,false);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				break;
			
			case TX_BUTTON_RESET:
				resetParametri(request);
				/* 20151001 Non sono nella pagina jsp
				request.setAttribute("data_pagamento_da", null);
				request.setAttribute("data_pagamento_a", null);
				request.setAttribute("data_creazione_da", null);
				request.setAttribute("data_creazione_a", null);
				request.setAttribute("data_valuta_da", null);
				request.setAttribute("data_valuta_a", null);
				request.setAttribute("data_contabile_da", null);
				request.setAttribute("data_contabile_a", null);
				*/
				request.setAttribute("dtFlusso_da", null);
				request.setAttribute("dtFlusso_a", null);
				
				codiceSocieta = "";
				codiceProvincia = "";
				codiceUtente = "";
				
				setParamCodiceSocieta("");
				setParamCodiceUtente("");
								
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				loadDDLUtente(request, session, getParamCodiceSocieta(), codiceProvincia,true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				break;
				
			default:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				loadDDLUtente(request, session, getParamCodiceSocieta(), codiceProvincia,true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
		}
	}
	
	private void impostaFiltri(HttpServletRequest request, HttpSession session) {
		try {
			System.out.println("Settaggio filtri...");
			request.setAttribute(Field.TX_SOCIETA.format(), session.getAttribute(Field.TX_SOCIETA.format()));
			request.setAttribute(Field.TX_PROVINCIA.format(), session.getAttribute(Field.TX_PROVINCIA.format()));
			request.setAttribute("tx_UtenteEnte", session.getAttribute("tx_UtenteEnte"));
			request.setAttribute(Field.DT_FLUSSO_DA_DAY.format(), session.getAttribute(Field.DT_FLUSSO_DA_DAY.format()));
			request.setAttribute(Field.DT_FLUSSO_DA_MONTH.format(), session.getAttribute(Field.DT_FLUSSO_DA_MONTH.format()));
			request.setAttribute(Field.DT_FLUSSO_DA_YEAR.format(), session.getAttribute(Field.DT_FLUSSO_DA_YEAR.format()));
			request.setAttribute(Field.DT_FLUSSO_A_DAY.format(), session.getAttribute(Field.DT_FLUSSO_A_DAY.format()));
			request.setAttribute(Field.DT_FLUSSO_A_MONTH.format(), session.getAttribute(Field.DT_FLUSSO_A_MONTH.format()));
			request.setAttribute(Field.DT_FLUSSO_A_YEAR.format(), session.getAttribute(Field.DT_FLUSSO_A_YEAR.format()));
			request.setAttribute(Field.ID_FLUSSO.format(), session.getAttribute(Field.ID_FLUSSO.format()));
			request.setAttribute(Field.STATO_SQUADRATURA.format(), session.getAttribute(Field.STATO_SQUADRATURA.format()));
			request.setAttribute(Field.NOME_FILE_TXT.format(), session.getAttribute(Field.NOME_FILE_TXT.format()));
			request.setAttribute(Field.KEY_QUADRATURA.format(), session.getAttribute(Field.KEY_QUADRATURA.format()));
		} catch (Exception e) {
			System.out.println("Errore Settaggio filtri : ");
			e.getMessage();
		}
	}
	
}
