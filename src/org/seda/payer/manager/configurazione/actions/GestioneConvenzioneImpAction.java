package org.seda.payer.manager.configurazione.actions;

import java.math.BigDecimal;
import java.rmi.RemoteException;

//import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.ws.WSCache;


import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.commons.bean.TypeRequest;
//import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.convenzioneimp.dati.ResponseTypeRetCode;
//import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.convenzioneimp.dati.ConvenzioneImpCancelRequest;
import com.seda.payer.pgec.webservice.convenzioneimp.dati.ConvenzioneImpDetailRequest;
import com.seda.payer.pgec.webservice.convenzioneimp.dati.ConvenzioneImpDetailResponse;
import com.seda.payer.pgec.webservice.convenzioneimp.dati.ConvenzioneImpSaveRequest;
import com.seda.payer.pgec.webservice.convenzioneimp.dati.Paginazione;
import com.seda.payer.pgec.webservice.convenzioneimp.dati.ConvenzioneImpSearchRequest;
import com.seda.payer.pgec.webservice.convenzioneimp.dati.ConvenzioneImpSearchResponse;
import com.seda.payer.pgec.webservice.convenzioneimp.dati.ConvenzioneImp;
import com.seda.payer.pgec.webservice.convenzioneimp.dati.StatusResponse;

public class GestioneConvenzioneImpAction extends BaseRiversamentoAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		salvaStato(request);

		super.service(request);


		HttpSession session = request.getSession();
		ConvenzioneImp convenzioneVuota = new ConvenzioneImp("","","","","",null,null,"",null,null,null,"","","",""); 
		convenzioneVuota.setCompPercRisc(new BigDecimal(0));
		convenzioneVuota.setCompQuotaFissa(new BigDecimal(0));


		if (DDLChanged.equals(""))
			switch(getFiredButton(request)) 
				{
			
				case TX_BUTTON_RESET:
					resetParametri(request);
					request.setAttribute("dataValidita", null);
					setProfile(request);
				case TX_BUTTON_NULL:
					request.setAttribute("DDLType", "12");
					resetDDLSession(request);
				case TX_BUTTON_CERCA: 
									
					try {					
							esecuzioneRicerca(request, true);
						}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("ricercaConvImpForm","errore: " + decodeMessaggio(fte), request);
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("errore: " + re.getMessage(),re);
						re.printStackTrace();
						setFormMessage("ricercaAssocImpBenForm", testoErroreColl, request);					
					}
					catch (Exception e) {
						WSCache.logWriter.logError("errore: " + e.getMessage(),e);
	//					setErrorMessage(request, e.getMessage());
						setFormMessage("ricercaConvImpForm", "errore: " + testoErrore , request);
					}
	//				resetDDLSession(request);
					
					break;
				case TX_BUTTON_AGGIUNGI:
	
					//creazione immagine rVariabili
					request.setAttribute("rCodSocieta", request.getAttribute("codSocieta"));
					request.setAttribute("rCodProvincia", request.getAttribute("codProvincia"));
					request.setAttribute("rCodUtente", request.getAttribute("codUtente"));
					request.setAttribute("rCodImpositore", request.getAttribute("codImpositore"));
	
					request.setAttribute("rCodCanale", request.getAttribute("codCanale"));
					request.setAttribute("rCodGateway", request.getAttribute("codGateway"));
	
					request.setAttribute("rDataValidita", request.getAttribute("dataValidita"));
					request.setAttribute("rDataValidita_day", request.getAttribute("dataValidita_day"));
					request.setAttribute("rDataValidita_month", request.getAttribute("dataValidita_month"));
					request.setAttribute("rDataValidita_year", request.getAttribute("dataValidita_year"));
					request.setAttribute("rDDLChanged", request.getAttribute("DDLChanged"));
					request.setAttribute("tastoIns", "ok");				
					
					convenzioneToRequest(request, convenzioneVuota);
					request.setAttribute("DDLType", "121");
	
					resetDDLSession(request);
	
	/*				
					loadDDLUtente(request, session, null, null, true);			
					loadDDLEnte(request, session, null, null, null, true);
					loadDDLGatewayCan(request, session, null, null, null, null, null, true);
	*/				
					break;
					
				case TX_BUTTON_RESET_INS:
					convenzioneToRequest(request, convenzioneVuota);
					request.setAttribute("dataValidita", null);
					request.setAttribute("dataValidita_year", null);
					request.setAttribute("dataValidita_month", null);
					request.setAttribute("dataValidita_day", null);
					request.setAttribute("rDataValidita", null);
					request.setAttribute("rDataValidita_year", null);
					request.setAttribute("rDataValidita_month", null);
					request.setAttribute("rDataValidita_day", null);
					resetDDLSession(request);
					
					break;
				case TX_BUTTON_AGGIUNGI_END:
	
					try 
					{
						ConvenzioneImpSaveRequest in = new ConvenzioneImpSaveRequest();
						ConvenzioneImp convenzione = new ConvenzioneImp("","","","","",null,null,"",null,null,null,"","","","");
						StatusResponse out;
						
						requestToConvenzione(request, convenzione);
					    
						in.setCodOp(TypeRequest.ADD_SCOPE.scope());
						in.setConvenzioneImp(convenzione);
		
						out = WSCache.convenzioneImpServer.save(in, request);
						
						if (out.getResponse().getRetCode().equals(ResponseTypeRetCode.value1))
						{
							//setMessage(request, "Inserimento eseguito");
							setFormMessage("schedaConvImpForm", "Inserimento eseguito", request);
							convenzioneToRequest(request, convenzioneVuota);
							resetDDLSession(request);												
						}
						else 
						{
			//				setErrorMessage(request, "Inserimento fallito: " + out.getResponse().getRetMessage());
							setFormMessage("schedaConvImpForm", "Inserimento fallito: " + out.getResponse().getRetMessage() , request);
							convenzioneToRequest(request, convenzione);
						}
					}
					catch (FaultType fte) 
					{
						if (fte.getCode()!=102&&fte.getCode()!=100&&fte.getCode()!=101)
						{
							fte.printStackTrace();
							WSCache.logWriter.logError("Inserimento fallito: " + decodeMessaggio(fte),fte);
					    	setFormMessage("schedaConvImpForm", "Inserimento fallito: " + decodeMessaggio(fte), request);							
						}
						else
						{
						    if (fte.getCode()==102)
						    {
						    	setFormMessage("schedaConvImpForm", "Inserimento eseguito : " + decodeMessaggio(fte), request);
								convenzioneToRequest(request, convenzioneVuota);
								resetDDLSession(request);
						    }
						    else
						    {
						    	//casi 101 - 102
						    	setFormMessage("schedaConvImpForm", "Inserimento fallito: " + decodeMessaggio(fte), request);
						    }
						}
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("Inserimento fallito: " + re.getMessage(),re);
						re.printStackTrace();
						setFormMessage("schedaConvImpForm", testoErroreColl, request);					
					}
					catch (Exception e) 
					{
						WSCache.logWriter.logError("Inserimento fallito: " + e.getMessage(),e);
					//	setErrorMessage(request, e.getMessage());
						setFormMessage("schedaConvImpForm", "Inserimento fallito: " + testoErrore, request);
					}
					break;
				case TX_BUTTON_RESET_MOD:
				case TX_BUTTON_EDIT:
	
					try
					{
						ConvenzioneImpDetailRequest in = new ConvenzioneImpDetailRequest();
						ConvenzioneImpDetailResponse out;
						ConvenzioneImp convenzione = new ConvenzioneImp("","","","","",null,null,"",null,null,null,"","","","");
						
						requestToConvenzione(request, convenzione); 
	
						in.setConvenzioneImp(convenzione);
						
						
						
						out = WSCache.convenzioneImpServer.getConvenzioneImp(in, request);
						
						convenzione = out.getConvenzioneImp();
	
						convenzioneToRequest(request, convenzione);
						request.setAttribute("DDLType", "121");
						
						resetDDLSession(request);
	
	/*
	 					//caricamento liste e blocco
	
						
						loadDDLUtente(request, session, request.getParameter("codSocieta"), null, true);			
	//					LoadListaUtentiEntiXml_DDL(request, session, request.getParameter("codSocieta"), null, null, request.getParameter("codUtente"),true);
						loadDDLEnte(request, session, request.getParameter("codSocieta"), null, request.getParameter("codUtente"), true);
						loadDDLGatewayCan(request, session, codiceSocieta, null, codiceUtente, codiceEnteImpositore, null, true);
						
	 */
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("schedaConvImpForm", "errore: " + decodeMessaggio(fte), request);
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("errore: " + re.getMessage(),re);
						re.printStackTrace();
						setFormMessage("ricercaAssocImpBenForm", testoErroreColl, request);					
					}
					catch (Exception e )
					{
						WSCache.logWriter.logError("errore: " + e.getMessage(),e);
		//				setErrorMessage(request, e.getMessage());					
						setFormMessage("schedaConvImpForm", "errore: " + testoErrore, request);
					}
					break;
				case TX_BUTTON_EDIT_END:
					try 
					{
						ConvenzioneImpSaveRequest in = new ConvenzioneImpSaveRequest();
						ConvenzioneImp convenzione = new ConvenzioneImp("","","","","",null,null,"",null,null,null,"","","","");
						StatusResponse out;
		
						
						requestToConvenzione(request, convenzione);
						
						
						in.setCodOp(TypeRequest.EDIT_SCOPE.scope());
						in.setConvenzioneImp(convenzione);
	
						out = WSCache.convenzioneImpServer.save(in, request);
						
						if (out.getResponse().getRetCode().equals(ResponseTypeRetCode.value1))
				//			setMessage(request, "Aggiornamento eseguito");
							setFormMessage("schedaConvImpForm", "Aggiornamento eseguito", request);
						else 
							//setErrorMessage(request, "Aggiornamento fallito: " + out.getResponse().getRetMessage());
							setFormMessage("schedaConvImpForm", "Aggiornamento fallito: " + out.getResponse().getRetMessage(), request);
	
						convenzioneToRequest(request, convenzione);
	
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("Aggiornamento fallito: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("schedaConvImpForm", "Aggiornamento fallito: " + decodeMessaggio(fte), request);
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("errore: " + re.getMessage(),re);
						re.printStackTrace();
						setFormMessage("ricercaAssocImpBenForm", testoErroreColl, request);					
					}
					catch (Exception e) 
					{
						WSCache.logWriter.logError("Aggiornamento fallito: " + e.getMessage(),e);
					//	setErrorMessage(request, e.getMessage());
						setFormMessage("schedaConvImpForm", "Aggiornamento fallito: " + testoErrore, request);
					}
					break;
				case TX_BUTTON_DELETE:
					try
					{
						ConvenzioneImpDetailRequest in = new ConvenzioneImpDetailRequest();
						ConvenzioneImpDetailResponse out;
						ConvenzioneImp convenzione = new ConvenzioneImp("","","","","",null,null,"",null,null,null,"","","","");
						
						requestToConvenzione(request, convenzione); 
	
						in.setConvenzioneImp(convenzione);
						
											
						out = WSCache.convenzioneImpServer.getConvenzioneImp(in, request);
						
						convenzione = out.getConvenzioneImp();
	
						convenzioneToRequest(request, convenzione);
						request.setAttribute("DDLType", "121");
						
						
						request.setAttribute("disableCancella", new Boolean(false));
	
						resetDDLSession(request);
	
		/*
		 					//caricamento liste e blocco
		
						loadDDLUtente(request, session, request.getParameter("codSocieta"), null, true);			
	//					LoadListaUtentiEntiXml_DDL(request, session, request.getParameter("codSocieta"), null, null, request.getParameter("codUtente"),true);
						loadDDLEnte(request, session, request.getParameter("codSocieta"), null, request.getParameter("codUtente"), true);
						loadDDLGatewayCan(request, session, codiceSocieta, null, codiceUtente, codiceEnteImpositore, null, true);
					
		 */
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("schedaConvImpForm", "errore: " + decodeMessaggio(fte), request);
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("errore: " + re.getMessage(),re);
						re.printStackTrace();
						setFormMessage("ricercaAssocImpBenForm", testoErroreColl, request);					
					}
					catch (Exception e )
					{
						WSCache.logWriter.logError("errore: " + e.getMessage(),e);
	//					setErrorMessage(request, e.getMessage());					
						setFormMessage("schedaConvImpForm", "errore: " + testoErrore, request);
					}
					break;
					
				case TX_BUTTON_DELETE_END:
					try 
					{
						ConvenzioneImpCancelRequest in = new ConvenzioneImpCancelRequest();
						StatusResponse out;
						ConvenzioneImp convenzione = new ConvenzioneImp("","","","","",null,null,"",null,null,null,"","","","");
	
						requestToConvenzione(request, convenzione);
	
						in.setConvenzioneImp(convenzione);
						
						out = WSCache.convenzioneImpServer.cancel(in, request);
						
						if (out.getResponse().getRetCode().equals(ResponseTypeRetCode.value1))
						{
							request.setAttribute("disableCancella", new Boolean(true));
							setFormMessage("schedaConvImpForm", "Cancellazione eseguita", request);
						}
						else 
						{
							setFormMessage("schedaConvImpForm", "Cancellazione fallita: " + out.getResponse().getRetMessage(), request);
							request.setAttribute("disableCancella", new Boolean(false));
						}
						convenzioneToRequest(request, convenzione);
	//					esecuzioneRicerca(request, false);
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("Cancellazione fallita: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("schedaConvImpForm", "Cancellazione fallita: " + decodeMessaggio(fte), request);
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("errore: " + re.getMessage(),re);
						re.printStackTrace();
						setFormMessage("ricercaAssocImpBenForm", testoErroreColl, request);					
					}
					catch (Exception e) 
					{
						WSCache.logWriter.logError("Cancellazione fallita: " + e.getMessage(),e);
					//	setErrorMessage(request, e.getMessage());
						setFormMessage("schedaConvImpForm", "Cancellazione fallita: " + testoErrore, request);
					}
					break;
				case TX_BUTTON_INDIETRO:
					// riporto variabili rVar
					//creazione immagine rVariabili
	
					request.setAttribute("codSocieta", request.getAttribute("rCodSocieta"));
					request.setAttribute("codProvincia", request.getAttribute("rCodProvincia"));
					request.setAttribute("codUtente", request.getAttribute("rCodUtente"));
					request.setAttribute("codImpositore", request.getAttribute("rCodImpositore"));
	
					request.setAttribute("codCanale", request.getAttribute("rCodCanale"));
					request.setAttribute("codGateway", request.getAttribute("rCodGateway"));
	
					request.setAttribute("dataValidita", request.getAttribute("rDataValidita"));
					request.setAttribute("dataValidita_day", request.getAttribute("rDataValidita_day"));
					request.setAttribute("dataValidita_month", request.getAttribute("rDataValidita_month"));
					request.setAttribute("dataValidita_year", request.getAttribute("rDataValidita_year"));
					request.setAttribute("DDLChanged", request.getAttribute("rDDLChanged"));
	/*
					request.setAttribute("rowsPerPage", request.getAttribute("rRowsPerPage"));
					request.setAttribute("pageNumber", request.getAttribute("rPageNumber"));
					request.setAttribute("order", request.getAttribute("rOrder"));
	*/				
					request.setAttribute("DDLType", "12");
	
					resetDDLSession(request);
	
	/*				
					loadDDLUtente(request, session, request.getAttribute("codSocieta").toString(), null, true);			
					loadDDLEnte(request, session, request.getAttribute("codSocieta").toString(), null, request.getAttribute("codUtente").toString(), true);
					loadDDLGatewayCan(request, session, request.getAttribute("codSocieta").toString(), null, request.getAttribute("codUtente").toString(), request.getAttribute("codImpositore").toString(), null, true);
	*/							
	
					
					
	// ---------------------------------------				
					
					/*
				case TX_BUTTON_SOCIETA_CHANGED:
					if (DDLType==12)
					{
						loadDDLProvincia(request, session, codiceSocieta, true);
						loadDDLUtente(request, session, codiceSocieta, null, true);
						loadDDLEnte(request, session, codiceSocieta, null, null,true);
						loadDDLGatewayCan(request, session, codiceSocieta, null, null, null, null, true);
					}
					else if (DDLType==121)
					{
						loadDDLUtente(request, session, codiceSocieta, null, true);
						loadDDLEnte(request, session, codiceSocieta, null, null,true);
						loadDDLGatewayCan(request, session, codiceSocieta, null, null, null, null, true);
					}
					break;
					*/
	/*
				case TX_BUTTON_PROVINCIA_CHANGED:
					if (DDLType==12)
					{
						loadDDLProvincia(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
						loadDDLEnte(request, session, codiceSocieta, codiceProvincia, null,true);
						loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, null, null, null, true);
					}
					break;
	*/
	/*
				case TX_BUTTON_UTENTE_CHANGED:
					if (DDLType==12)
					{
						loadDDLProvincia(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
						loadDDLEnte(request, session, codiceSocieta, codiceProvincia, codiceUtente,true);
						loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, codiceUtente, null, null, true);
					}
					else if (DDLType==121)
					{
						loadDDLUtente(request, session, codiceSocieta, null,false);			
						loadDDLEnte(request, session, codiceSocieta, null, codiceUtente,false);
						loadDDLGatewayCan(request, session, codiceSocieta, null, codiceUtente, null, null, true);
					}
					break;
	*/
	/*
				case TX_BUTTON_IMPOSITORE_CHANGED:
					if (DDLType==12)
					{
						loadDDLProvincia(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
						loadDDLEnte(request, session, codiceSocieta, codiceProvincia, codiceUtente,false);
						loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore, null, true);
					}
					else if (DDLType==121)
					{
						loadDDLUtente(request, session, codiceSocieta, null,false);			
						loadDDLEnte(request, session, codiceSocieta, null, codiceUtente,false);
						loadDDLGatewayCan(request, session, codiceSocieta, null, codiceUtente, codiceEnteImpositore, null, true);						
					}
					break;
	*/
	/*
				case TX_BUTTON_CANALE_CHANGED:
					if (DDLType==12)
					{
						loadDDLProvincia(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
						loadDDLEnte(request, session, codiceSocieta, codiceProvincia, codiceUtente,false);
						loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore, codiceCanale, true);						
					}
					break;
	*/
				}

		//aggiornamento Combo post set
//		setAmbienteFromReq(request);
		
		super.service(request);
        aggiornamentoCombo(request, session);		

//		request.setAttribute("message", getMessage());
//		request.setAttribute("error_message", getErrorMessage());
		return null;
	}

	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
		switch(DDLType)
		{	
			case 0:
			case 12:
					if (DDLChanged.equals("codSocieta")||getFiredButton(request)==FiredButton.TX_BUTTON_SOCIETA_CHANGED) 
					{
//						loadDDLProvincia(request, session, codiceSocieta, true);
						loadProvinciaXml_DDL(request, session, codiceSocieta, true);

						loadDDLUtente(request, session, codiceSocieta, null, true);

//						loadDDLEnte(request, session, codiceSocieta, null, null,true);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, null,true);
//						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, null, null, null,true);

						loadDDLGatewayCan(request, session, codiceSocieta, null, null, null, null, true);

/*
 					loadDDLProvincia(request, session, codiceSocieta, true);
					loadDDLUtente(request, session, codiceSocieta, null, true);
					loadDDLEnte(request, session, codiceSocieta, null, null,true);
					loadDDLGatewayCan(request, session, codiceSocieta, null, null, null, null, true);
						
 */
					}
					else if (DDLChanged.equals("codProvincia")||getFiredButton(request)==FiredButton.TX_BUTTON_PROVINCIA_CHANGED) 
					{
//						loadDDLProvincia(request, session, codiceSocieta, false);
						loadProvinciaXml_DDL(request, session, codiceSocieta, false);

						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);

//						loadDDLEnte(request, session, codiceSocieta, codiceProvincia, null,true);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, null,true);
//						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia, null, null,true);

						loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, null, null, null, true);

/*
 							loadDDLProvincia(request, session, codiceSocieta, false);
 
							loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
							loadDDLEnte(request, session, codiceSocieta, codiceProvincia, null,true);
							loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, null, null, null, true);
*/
					}
					else if (DDLChanged.equals("codUtente")||getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_CHANGED) 
					{
//						loadDDLProvincia(request, session, codiceSocieta, false);

						//mod090
//						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
//						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia, false);			
						// fine mod090	
						
//						loadDDLEnte(request, session, codiceSocieta, codiceProvincia, codiceUtente,true);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,true);
//						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,true);

						loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, codiceUtente, null, null, true);

					/*
					 					loadDDLProvincia(request, session, codiceSocieta, false);
					loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
					loadDDLEnte(request, session, codiceSocieta, codiceProvincia, codiceUtente,true);
					loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, codiceUtente, null, null, true);

					 */
					}
					else if (DDLChanged.equals("codImpositore")||getFiredButton(request)==FiredButton.TX_BUTTON_IMPOSITORE_CHANGED) 
					{
//						loadDDLProvincia(request, session, codiceSocieta, false);

						//mod090
/*
						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			

//						loadDDLEnte(request, session, codiceSocieta, codiceProvincia, codiceUtente,false);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
//						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
*/
						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia, false);			
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente, false);
//fine mod090
						loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore, null, true);

					/*
 					loadDDLProvincia(request, session, codiceSocieta, false);
					loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
					loadDDLEnte(request, session, codiceSocieta, codiceProvincia, codiceUtente,false);
					loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore, null, true);

					 */
					}
					else if (DDLChanged.equals("codCanale")||getFiredButton(request)==FiredButton.TX_BUTTON_CANALE_CHANGED) 
					{
//						loadDDLProvincia(request, session, codiceSocieta, false);
						loadProvinciaXml_DDL(request, session, codiceSocieta, false);

						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			

//						loadDDLEnte(request, session, codiceSocieta, codiceProvincia, codiceUtente,false);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
//						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);

						loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore, codiceCanale, true);						

/*
						loadDDLProvincia(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
						loadDDLEnte(request, session, codiceSocieta, codiceProvincia, codiceUtente,false);
						loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore, codiceCanale, true);						
*/
					}
					else 
					{
//						loadDDLProvincia(request, session, codiceSocieta,false);
						loadProvinciaXml_DDL(request, session, codiceSocieta, false);

						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);

//						loadDDLEnte(request, session, codiceSocieta, codiceProvincia, codiceUtente,false);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
//						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);

						loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore, codiceCanale, false);
					}
				break;
			case 121:
					if (DDLChanged.equals("codSocieta")||getFiredButton(request)==FiredButton.TX_BUTTON_SOCIETA_CHANGED) 
					{
						loadDDLUtente(request, session, codiceSocieta, null, true);

//						loadDDLEnte(request, session, codiceSocieta, null, null,true);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, null, true);
//						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, null, null, null, true);

						loadDDLGatewayCan(request, session, codiceSocieta, null, null, null, null, true);

					/*
 					loadDDLUtente(request, session, codiceSocieta, null, true);
					loadDDLEnte(request, session, codiceSocieta, null, null,true);
					loadDDLGatewayCan(request, session, codiceSocieta, null, null, null, null, true);

					 */
					}
					else if (DDLChanged.equals("codUtente")||getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_CHANGED) 
					{
//mod090
//						loadDDLUtente(request, session, codiceSocieta, null,false);			
						loadDDLUtente(request, session, codiceSocieta, null,false);			
// fine mod090
//						loadDDLEnte(request, session, codiceSocieta, null, codiceUtente,false);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, codiceUtente, true);
//						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, null, null, codiceUtente, false);
						
						loadDDLGatewayCan(request, session, codiceSocieta, null, codiceUtente, null, null, true);

					/*
 					loadDDLUtente(request, session, codiceSocieta, null,false);			
					loadDDLEnte(request, session, codiceSocieta, null, codiceUtente,false);
					loadDDLGatewayCan(request, session, codiceSocieta, null, codiceUtente, null, null, true);

					 */
					}
					else if (DDLChanged.equals("codImpositore")||getFiredButton(request)==FiredButton.TX_BUTTON_IMPOSITORE_CHANGED) 
					{
						//mod090
/*
						loadDDLUtente(request, session, codiceSocieta, null,false);			
						
//						loadDDLEnte(request, session, codiceSocieta, null, codiceUtente,false);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, codiceUtente, false);
//						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, null, null, codiceUtente, false);
*/
						loadDDLUtente(request, session, codiceSocieta, null,false);									
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, codiceUtente, false);
//fine mod090
						
						loadDDLGatewayCan(request, session, codiceSocieta, null, codiceUtente, codiceEnteImpositore, null, true);						

						/*
 					loadDDLUtente(request, session, codiceSocieta, null,false);			
					loadDDLEnte(request, session, codiceSocieta, null, codiceUtente,false);
					loadDDLGatewayCan(request, session, codiceSocieta, null, codiceUtente, codiceEnteImpositore, null, true);						

						 */
					}		
					else 
					{
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);
						
//						loadDDLEnte(request, session, codiceSocieta, codiceProvincia, codiceUtente,false);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente, false);
//						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente, false);
						
						loadDDLGatewayCan(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore, codiceCanale, false);						
						}
				break;
		}
	}
	
	
	public void esecuzioneRicerca(HttpServletRequest request, boolean useMessage) throws FaultType, Exception
	{
		
		ConvenzioneImpSearchResponse out;
		ConvenzioneImpSearchRequest in;
		in = prepareRicerca(request);
					
		out =  WSCache.convenzioneImpServer.getConvenzioniImp(in, request);

		
		if(out.getPInfo().getNumRows() > 0) 
		{
			request.setAttribute("listaConvenzioni", out.getListXml());

			request.setAttribute("listaConvenzioni.pageInfo", getPageInfo(out.getPInfo()));		
		}
		else
		{
			if (useMessage)
			//	setMessage(request, "Nessun risultato");
				setFormMessage("ricercaConvImpForm", Messages.NO_DATA_FOUND.format(), request);
			}
		
		request.setAttribute("NumRighe", Integer.toString(out.getPInfo().getNumRows()));

	}
	
	private ConvenzioneImpSearchRequest prepareRicerca(HttpServletRequest request)
	{

		PropertiesTree configuration; 
		ConvenzioneImpSearchRequest ris;
		ConvenzioneImp convenzione;	
		int rowsPerPage;
		
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		
		rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

        if (request.getAttribute("rowsPerPage")!=null&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
        {
        	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
        }

		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").toString().equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();


		ris = new ConvenzioneImpSearchRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		convenzione = new ConvenzioneImp();	
		
		requestToConvenzione(request, convenzione);

        ris.setConvenzioneImp(convenzione);
		return ris;
		
	}
	
	private void convenzioneToRequest(HttpServletRequest request, ConvenzioneImp convenzione)
	{
		request.setAttribute("codSocieta", convenzione.getCodSocieta());
		request.setAttribute("codProvincia", convenzione.getCodProvincia());

//		request.setAttribute("codUtente", convenzione.getCodUtente());
//		request.setAttribute("codUtente", convenzione.getCodSocieta() + convenzione.getCodUtente());
		if (convenzione.getCodUtente()!=null&&!convenzione.getCodUtente().equals(""))
			request.setAttribute("codUtente", convenzione.getCodSocieta() + convenzione.getCodUtente());
		else
			request.setAttribute("codUtente","");
		
//		request.setAttribute("codImpositore", request.getParameter("codUtente") + convenzione.getCodImpositore());
//		request.setAttribute("codImpositore", convenzione.getCodImpositore());
//		request.setAttribute("codImpositore", convenzione.getCodSocieta() + request.getParameter("codUtente") + convenzione.getCodImpositore());
		if (convenzione.getCodImpositore()!=null&&!convenzione.getCodImpositore().equals(""))
			request.setAttribute("codImpositore", convenzione.getCodSocieta() + convenzione.getCodUtente() + convenzione.getCodImpositore());
		else
			request.setAttribute("codImpositore","");
		
		//		request.setAttribute("codBeneficiario", request.getParameter("codUtente") + convenzione.getCodBeneficiario());
		request.setAttribute("codCanale", convenzione.getCodCanale());
		request.setAttribute("codGateway", convenzione.getCodGateway());
		
		//dall'associazione data normale
		java.util.Calendar cal = getCalendarS(request, convenzione.getDataValidita());		
		request.setAttribute("dataValidita", cal);

		//provvisorio
		if (convenzione.getDataValidita() != null && convenzione.getDataValidita().length()>0)
			request.setAttribute("dataValiditaS", convenzione.getDataValidita().substring(8,10) + "/" + 
					convenzione.getDataValidita().substring(5,7) + "/" + convenzione.getDataValidita().substring(0,4));
		else
			request.setAttribute("dataValiditaS", "");

		request.setAttribute("aggregPagamenti", (convenzione.getAggregPagamenti()==null) ? "" : convenzione.getAggregPagamenti().toString());

		request.setAttribute("giorniLatenza", (convenzione.getGiorniLatenza()==null) ? "" : convenzione.getGiorniLatenza().toString());
		request.setAttribute("indFesta", convenzione.getIndFesta());
		request.setAttribute("limMaxGiorni", (convenzione.getLimMaxGiorni()==null) ? "" : convenzione.getLimMaxGiorni().toString());

		request.setAttribute("compPercRisc", (convenzione.getCompPercRisc()==null) ? "" : visNumber(convenzione.getCompPercRisc().toString()));
		request.setAttribute("compQuotaFissa", (convenzione.getCompQuotaFissa()==null) ? "" : visNumber(convenzione.getCompQuotaFissa().toString()));
		request.setAttribute("tipoQuotaFissa", convenzione.getTipoQuotaFissa());
	}
	
	
	private void requestToConvenzione(HttpServletRequest request, ConvenzioneImp convenzione)
	{
		
		convenzione.setCodSocieta(request.getAttribute("codSocieta")!=null?request.getAttribute("codSocieta").toString():"");
        convenzione.setCodProvincia(request.getAttribute("codProvincia")!=null?request.getAttribute("codProvincia").toString():"");
//        convenzione.setCodUtente(request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"");
        String codUtente = request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"";
        if (codUtente.length()==10)
        	codUtente = codUtente.substring(5);        	
        convenzione.setCodUtente(codUtente);
        
		String codImpositore = request.getAttribute("codImpositore")!=null?request.getAttribute("codImpositore").toString():"";
//		codImpositore = codImpositore.substring(5, codImpositore.length());
		if (codImpositore.length()==20) 
			codImpositore = codImpositore.substring(10);

        convenzione.setCodImpositore(codImpositore);

        convenzione.setCodCanale(request.getAttribute("codCanale")!=null?request.getAttribute("codCanale").toString():"");
        convenzione.setCodGateway(request.getAttribute("codGateway")!=null?request.getAttribute("codGateway").toString():"");

        /*
        if (request.getAttribute("dataValidita")!=null && request.getAttribute("dataValidita").toString().length()>0)
        	convenzione.setDataValidita(request.getAttribute("dataValidita").toString().substring(0,10));
        else
        	convenzione.setDataValidita(request.getAttribute("dataValidita_day")!=null?getData(request.getAttribute("dataValidita_day").toString(),request.getAttribute("dataValidita_month").toString(),request.getAttribute("dataValidita_year").toString()):"");
*/
        if (request.getAttribute("dataValidita_day")!=null&&request.getAttribute("dataValidita_day").toString().length()>0)
        	convenzione.setDataValidita(request.getAttribute("dataValidita_day")!=null?getData(request.getAttribute("dataValidita_day").toString(),request.getAttribute("dataValidita_month").toString(),request.getAttribute("dataValidita_year").toString()):"");
        else
        	if (request.getAttribute("dataValidita")!=null&&request.getAttribute("dataValidita").toString().length()>10)
        	     convenzione.setDataValidita(request.getAttribute("dataValidita").toString().substring(0,10));
        	else
        		convenzione.setDataValidita("");

        //todo parse stringa valore
        convenzione.setAggregPagamenti(request.getAttribute("aggregPagamenti")!=null ? new BigDecimal(request.getAttribute("aggregPagamenti").toString()): new BigDecimal(10));
        convenzione.setGiorniLatenza((request.getAttribute("giorniLatenza")!=null&&request.getAttribute("giorniLatenza").toString().length()>0) ? new BigDecimal(request.getAttribute("giorniLatenza").toString()): new BigDecimal(0));
        convenzione.setIndFesta(request.getAttribute("indFesta")!=null?request.getAttribute("indFesta").toString():"");
        convenzione.setLimMaxGiorni((request.getAttribute("limMaxGiorni")!=null&&request.getAttribute("limMaxGiorni").toString().length()>0) ? new BigDecimal(request.getAttribute("limMaxGiorni").toString()): new BigDecimal(0));

        
        convenzione.setCompPercRisc((request.getAttribute("compPercRisc")!=null&&request.getAttribute("compPercRisc").toString().length()>0) ? new BigDecimal(request.getAttribute("compPercRisc").toString().replace(',','.')): new BigDecimal(0));
        convenzione.setCompQuotaFissa((request.getAttribute("compQuotaFissa")!=null&&request.getAttribute("compQuotaFissa").toString().length()>0) ? new BigDecimal(request.getAttribute("compQuotaFissa").toString().replace(',','.')): new BigDecimal(0));
        convenzione.setTipoQuotaFissa(request.getAttribute("tipoQuotaFissa")!=null?request.getAttribute("tipoQuotaFissa").toString():"");

        convenzione.setUtenteAgg((String)request.getAttribute("operatore"));
	}


	private String visNumber(String numero)
	{
		 String risultato;
		 java.text.NumberFormat formattatore = java.text.NumberFormat.getInstance(java.util.Locale.ITALY);
		 formattatore.setMaximumFractionDigits(2);
		 formattatore.setMinimumFractionDigits(2);
		 numero = numero.replace(',','.');
		 risultato = formattatore.format(Double.parseDouble(numero)).toString();
         return risultato;
	}
	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
	}
}
