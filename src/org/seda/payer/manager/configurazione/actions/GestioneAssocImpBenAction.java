package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;

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
import com.seda.payer.pgec.webservice.associmpben.dati.ResponseTypeRetCode;
//import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.associmpben.dati.AssocImpBenCancelRequest;
import com.seda.payer.pgec.webservice.associmpben.dati.AssocImpBenDetailRequest;
import com.seda.payer.pgec.webservice.associmpben.dati.AssocImpBenDetailResponse;
import com.seda.payer.pgec.webservice.associmpben.dati.AssocImpBenSaveRequest;
import com.seda.payer.pgec.webservice.associmpben.dati.Paginazione;
import com.seda.payer.pgec.webservice.associmpben.dati.AssocImpBenSearchRequest;
import com.seda.payer.pgec.webservice.associmpben.dati.AssocImpBenSearchResponse;
import com.seda.payer.pgec.webservice.associmpben.dati.AssocImpBen;
import com.seda.payer.pgec.webservice.associmpben.dati.StatusResponse;

public class GestioneAssocImpBenAction extends BaseRiversamentoAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {

		salvaStato(request);
		
		super.service(request);

		HttpSession session = request.getSession();
		AssocImpBen assocVuota = new AssocImpBen("","","","","","","","","","","","","","","","",""); 
	   
		
		if (DDLChanged.equals(""))
		
			switch(getFiredButton(request)) 
				{
				case TX_BUTTON_RESET:
					resetParametri(request);
					request.setAttribute("dataValidita", null);
					setProfile(request);
				case TX_BUTTON_NULL: 
					request.setAttribute("DDLType", "11");
					resetDDLSession(request);				
				case TX_BUTTON_CERCA: 	
					try {					
							esecuzioneRicerca(request, true);
						}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
						fte.printStackTrace();
	//					setErrorMessage(request, fte.getMessage1());
						setFormMessage("ricercaAssocImpBenForm", "errore: " + decodeMessaggio(fte), request);
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("errore: " + re.getMessage(),re);
						re.printStackTrace();
	//					setErrorMessage(request, fte.getMessage1());
						setFormMessage("ricercaAssocImpBenForm", testoErroreColl, request);
						
					}
					catch (Exception e) {
						WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					//	setErrorMessage(request, e.getMessage());
						setFormMessage("ricercaAssocImpBenForm", "errore: " + testoErrore, request);
					}
	
	//				resetDDLSession(request);
	
					break;
				case TX_BUTTON_AGGIUNGI:
	
					//creazione immagine rVariabili
					request.setAttribute("rCodSocieta", request.getAttribute("codSocieta"));
					request.setAttribute("rCodProvincia", request.getAttribute("codProvincia"));
					request.setAttribute("rCodUtente", request.getAttribute("codUtente"));
					request.setAttribute("rCodImpositore", request.getAttribute("codImpositore"));
					request.setAttribute("rCodSocietaBen", request.getAttribute("codSocietaBen"));
					request.setAttribute("rCodProvinciaBen", request.getAttribute("codProvinciaBen"));
					request.setAttribute("rCodUtenteBen", request.getAttribute("codUtenteBen"));
					request.setAttribute("rCodBeneficiario", request.getAttribute("codBeneficiario"));
					request.setAttribute("rCodTipologiaServizio", request.getAttribute("codTipologiaServizio"));
					request.setAttribute("rAnnoRifDa", request.getAttribute("annoRifDa"));
					request.setAttribute("rAnnoRifA", request.getAttribute("annoRifA"));
					request.setAttribute("rDataValidita", request.getAttribute("dataValidita"));
					request.setAttribute("rDataValidita_day", request.getAttribute("dataValidita_day"));
					request.setAttribute("rDataValidita_month", request.getAttribute("dataValidita_month"));
					request.setAttribute("rDataValidita_year", request.getAttribute("dataValidita_year"));
					request.setAttribute("rDDLChanged", request.getAttribute("DDLChanged"));
					request.setAttribute("tastoIns", "ok");
	
//					associazioneToRequest(request, assocVuota, true);
					associazioneToRequest(request, assocVuota);
					request.setAttribute("DDLType", "111");
		
					resetDDLSession(request);
					
					break;
				case TX_BUTTON_RESET_INS:
//					associazioneToRequest(request, assocVuota, true);
					associazioneToRequest(request, assocVuota);
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
						AssocImpBenSaveRequest in = new AssocImpBenSaveRequest();
						AssocImpBen associazione = new AssocImpBen("","","","","","","","","","","","","","","","","");
						StatusResponse out;
						
						requestToAssociazione(request, associazione);
					    
						in.setCodOp(TypeRequest.ADD_SCOPE.scope());
						in.setAssocImpBen(associazione);
		
						out = WSCache.assocImpBenServer.save(in, request);
						
						if (out.getResponse().getRetCode().equals(ResponseTypeRetCode.value1))
						{
							//setMessage(request, "Inserimento eseguito");
							setFormMessage("schedaAssocImpBenForm", "Inserimento eseguito", request);
//							associazioneToRequest(request, assocVuota, true);
							associazioneToRequest(request, assocVuota);
							resetDDLSession(request);						
						}
						else 
						{
						//	setErrorMessage(request, "Inserimento fallito: " + out.getResponse().getRetMessage());
							setFormMessage("schedaAssocImpBenForm", "Inserimento fallito: " + out.getResponse().getRetMessage(), request);
//							associazioneToRequest(request, associazione, true);
							associazioneToRequest(request, associazione);
						}
						}
					catch (FaultType fte) 
					{
						if (fte.getCode()!=102&&fte.getCode()!=100&&fte.getCode()!=101)
						{
							fte.printStackTrace();
							WSCache.logWriter.logError("Inserimento fallito: " + decodeMessaggio(fte),fte);
					    	setFormMessage("schedaAssocImpBenForm", "Inserimento fallito: " + decodeMessaggio(fte), request);
						}
						else
						{
						    if (fte.getCode()==102)
						    {
						    	setFormMessage("schedaAssocImpBenForm", "Inserimento eseguito : " + decodeMessaggio(fte), request);
//								associazioneToRequest(request, assocVuota, true);
								associazioneToRequest(request, assocVuota);
								resetDDLSession(request);
						    }
						    else		
						    {
						    	setFormMessage("schedaAssocImpBenForm", "Inserimento fallito: " + decodeMessaggio(fte), request);
						    }
						}
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("Inserimento fallito: " + re.getMessage(),re);
						re.printStackTrace();
						setFormMessage("ricercaAssocImpBenForm", testoErroreColl, request);					
					}
					catch (Exception e) 
					{
						WSCache.logWriter.logError("Inserimento fallito: " + e.getMessage(),e);
	//					setErrorMessage(request, e.getMessage());
						setFormMessage("schedaAssocImpBenForm", "Inserimento fallito: " + testoErrore, request);
					}
					break;
				case TX_BUTTON_RESET_MOD:
				case TX_BUTTON_EDIT:
	
					try
					{
						AssocImpBenDetailRequest in = new AssocImpBenDetailRequest();
						AssocImpBenDetailResponse out;
						AssocImpBen associazione = new AssocImpBen("","","","","","","","","","","","","","","","","");
						
						requestToAssociazione(request, associazione); 
	
						in.setAssocImpBen(associazione);
						
						//caricamento liste e blocco
	
						
						out = WSCache.assocImpBenServer.getAssociazioneImpBen(in, request);
						
						associazione = out.getAssocImpBen();
	
//						associazioneToRequest(request, associazione, true);
						associazioneToRequest(request, associazione);
						request.setAttribute("DDLType", "111");
						
						resetDDLSession(request);
	
						/*					
						loadDDLUtente(request, session, request.getParameter("codSocieta"), null, true);			
						loadDDLEnte(request, session, request.getParameter("codSocieta"), null, request.getParameter("codUtente"), true);
						 */					
	
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
						fte.printStackTrace();
	//					setErrorMessage(request, fte.getMessage1());
						setFormMessage("schedaAssocImpBenForm", "errore: " + decodeMessaggio(fte), request);
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
						//setErrorMessage(request, e.getMessage());					
						setFormMessage("schedaAssocImpBenForm", "errore: " + testoErrore, request);
					}
					break;
				case TX_BUTTON_EDIT_END:
					try 
					{
						AssocImpBenSaveRequest in = new AssocImpBenSaveRequest();
						AssocImpBen associazione = new AssocImpBen("","","","","","","","","","","","","","","","","");
						StatusResponse out;
		
						requestToAssociazione(request, associazione);
						
						in.setCodOp(TypeRequest.EDIT_SCOPE.scope());
						in.setAssocImpBen(associazione);
		
						out = WSCache.assocImpBenServer.save(in, request);
						
						if (out.getResponse().getRetCode().equals(ResponseTypeRetCode.value1))
	//						setMessage(request, "Aggiornamento eseguito");
							setFormMessage("schedaAssocImpBenForm", "Aggiornamento eseguito", request);
						else 
							//setErrorMessage(request, "Aggiornamento fallito: " + out.getResponse().getRetMessage());
							setFormMessage("schedaAssocImpBenForm", "Aggiornamento fallito: " + out.getResponse().getRetMessage(), request);

//						associazioneToRequest(request, associazione, true);
						associazioneToRequest(request, associazione);
					
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("Aggiornamento fallito: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						//setErrorMessage(request, fte.getMessage1());
						setFormMessage("schedaAssocImpBenForm", "Aggiornamento fallito: " + decodeMessaggio(fte), request);
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("errore: " + re.getMessage(),re);
						re.printStackTrace();
						setFormMessage("schedaAssocImpBenForm", testoErroreColl, request);					
					}
					catch (Exception e) 
					{
						WSCache.logWriter.logError("Aggiornamento fallito: " + e.getMessage(),e);
						//setErrorMessage(request, e.getMessage());
						setFormMessage("schedaAssocImpBenForm", "Aggiornamento fallito: " + testoErrore, request);
					}
					break;
				case TX_BUTTON_DELETE:
					try
					{
						AssocImpBenDetailRequest in = new AssocImpBenDetailRequest();
						AssocImpBenDetailResponse out;
						AssocImpBen associazione = new AssocImpBen("","","","","","","","","","","","","","","","","");
						
						requestToAssociazione(request, associazione); 
	
						in.setAssocImpBen(associazione);
						
						//caricamento liste e blocco
	
						
						out = WSCache.assocImpBenServer.getAssociazioneImpBen(in, request);
						
						associazione = out.getAssocImpBen();
	
//						associazioneToRequest(request, associazione, true);
						associazioneToRequest(request, associazione);
						request.setAttribute("DDLType", "111");
						
						request.setAttribute("disableCancella", new Boolean(false));
	
						resetDDLSession(request);
	
						/*
						loadDDLUtente(request, session, request.getParameter("codSocieta"), null, true);			
	//					LoadListaUtentiEntiXml_DDL(request, session, request.getParameter("codSocieta"), null, null, request.getParameter("codUtente"),true);
						loadDDLEnte(request, session, request.getParameter("codSocieta"), null, request.getParameter("codUtente"), true);
						*/
	
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
						fte.printStackTrace();
				//		setErrorMessage(request, fte.getMessage1());
						setFormMessage("schedaAssocImpBenForm", "errore: " + decodeMessaggio(fte), request);
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
					//	setErrorMessage(request, e.getMessage());					
						setFormMessage("schedaAssocImpBenForm", "errore: " + testoErrore, request);
					}
					break;
				case TX_BUTTON_DELETE_END:
					try 
					{
						AssocImpBenCancelRequest in = new AssocImpBenCancelRequest();
						StatusResponse out;
						AssocImpBen associazione = new AssocImpBen("","","","","","","","","","","","","","","","","");
	
						requestToAssociazione(request, associazione);
	
						in.setAssocImpBen(associazione);
						
						out = WSCache.assocImpBenServer.cancel(in, request);
						
						if (out.getResponse().getRetCode().equals(ResponseTypeRetCode.value1))
						{
							setFormMessage("schedaAssocImpBenForm", "Cancellazione eseguita", request);
							request.setAttribute("disableCancella", new Boolean(true));
						}
						else
						{
							setFormMessage("schedaAssocImpBenForm", "Cancellazione fallita: " + out.getResponse().getRetMessage(), request);
							request.setAttribute("disableCancella", new Boolean(false));
						}
//						associazioneToRequest(request, associazione, true);
						associazioneToRequest(request, associazione);
	//					esecuzioneRicerca(request, false);
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("Cancellazione fallita: " + fte.getMessage1(),fte);
						fte.printStackTrace();
		//				setErrorMessage(request, fte.getMessage1());
						setFormMessage("schedaAssocImpBenForm", "Cancellazione fallita: " + decodeMessaggio(fte), request);
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
			//			setErrorMessage(request, e.getMessage());
						setFormMessage("schedaAssocImpBenForm", "Cancellazione fallita: " + testoErrore, request);
					}
					break;
				case TX_BUTTON_INDIETRO:
					// riporto variabili rVar
					//creazione immagine rVariabili
					request.setAttribute("codSocieta", request.getAttribute("rCodSocieta"));
					request.setAttribute("codProvincia", request.getAttribute("rCodProvincia"));
					request.setAttribute("codUtente", request.getAttribute("rCodUtente"));
					request.setAttribute("codImpositore", request.getAttribute("rCodImpositore"));
					request.setAttribute("codSocietaBen", request.getAttribute("rCodSocietaBen"));
					request.setAttribute("codProvinciaBen", request.getAttribute("rCodProvinciaBen"));
					request.setAttribute("codUtenteBen", request.getAttribute("rCodUtenteBen"));					
					request.setAttribute("codBeneficiario", request.getAttribute("rCodBeneficiario"));
					request.setAttribute("codTipologiaServizio", request.getAttribute("rCodTipologiaServizio"));
					request.setAttribute("annoRifDa", request.getAttribute("rAnnoRifDa"));
					request.setAttribute("annoRifA", request.getAttribute("rAnnoRifA"));
					request.setAttribute("dataValidita", request.getAttribute("rDataValidita"));
					request.setAttribute("dataValidita_day", request.getAttribute("rDataValidita_day"));
					request.setAttribute("dataValidita_month", request.getAttribute("rDataValidita_month"));
					request.setAttribute("dataValidita_year", request.getAttribute("rDataValidita_year"));
	//				request.setAttribute("DDLChanged", request.getAttribute("rDDLChanged"));
	/*
					request.setAttribute("rowsPerPage", request.getAttribute("rRowsPerPage"));
					request.setAttribute("pageNumber", request.getAttribute("rPageNumber"));
					request.setAttribute("order", request.getAttribute("rOrder"));
	*/				
					request.setAttribute("DDLType", "11");
					
					resetDDLSession(request);
	
	/*
	 				loadDDLUtente(request, session, request.getAttribute("codSocieta").toString(), null, true);			
					loadDDLEnte(request, session, request.getAttribute("codSocieta").toString(), null, request.getAttribute("codUtente").toString(), true);
					loadDDLBeneficiarioConv(request, session, request.getAttribute("codSocieta").toString(), request.getAttribute("codProvincia").toString(), request.getAttribute("codUtente").toString(), request.getAttribute("codImpositore").toString(), true);
					
	 */
					break;

		}

		
		super.service(request);
        aggiornamentoCombo(request, session);		
			

		return null;
	}


	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
		switch(DDLType)
		{		    
			case 0:
			case 11:
					if (DDLChanged.equals("codSocieta")||getFiredButton(request)==FiredButton.TX_BUTTON_SOCIETA_CHANGED) 
					{
						loadProvinciaXml_DDL(request, session, codiceSocieta, true);
						loadDDLUtente(request, session, codiceSocieta, null, true);
						
						loadProvinciaBenXml_DDL(request, session, codiceSocietaBen, false);
						loadDDLBenUtente(request, session, codiceSocietaBen, codiceProvinciaBen, false);
						
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, null,true);
						loadDDLBeneficiarioByImp(request, session, codiceSocieta, null, null, null,codiceSocietaBen,codiceProvinciaBen,codiceUtenteBen, true);
						//loadTipologiaServizioXml_DDL(request, session, codiceSocieta,true);
						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, null, null, true);
					}
					else 
					if (DDLChanged.equals("codProvincia")||getFiredButton(request)==FiredButton.TX_BUTTON_PROVINCIA_CHANGED) 
					{
						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);

						loadProvinciaBenXml_DDL(request, session, codiceSocietaBen, false);
						loadDDLBenUtente(request, session, codiceSocietaBen, codiceProvinciaBen, false);
						
						
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, null,true);
						loadDDLBeneficiarioByImp(request, session, codiceSocieta, codiceProvincia, null, null,codiceSocietaBen,codiceProvinciaBen,codiceUtenteBen, true);
					}
					else if (DDLChanged.equals("codUtente")||getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_CHANGED) 
					{
						//mod090
//						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
//						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			

						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia, false);			
						
						loadProvinciaBenXml_DDL(request, session, codiceSocietaBen, false);
						loadDDLBenUtente(request, session, codiceSocietaBen, codiceProvinciaBen, false);
						
						// fine mod090
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,true);
						loadDDLBeneficiarioByImp(request, session, codiceSocieta, codiceProvincia, codiceUtente, null,codiceSocietaBen,codiceProvinciaBen,codiceUtenteBen, true);
						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, codiceUtente, null, true);
					}
					else if (DDLChanged.equals("codImpositore")||getFiredButton(request)==FiredButton.TX_BUTTON_IMPOSITORE_CHANGED) 
					{
						//MOD090 selezionando l'impositore posso dover ridefinire società e utente e ricaricare le combo utente e impositore
//						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
//						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
//						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			

						loadProvinciaBenXml_DDL(request, session, codiceSocietaBen, false);
						loadDDLBenUtente(request, session, codiceSocietaBen, codiceProvinciaBen, false);
						
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
						// fine mod090
						loadDDLBeneficiarioByImp(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore,codiceSocietaBen,codiceProvinciaBen,codiceUtenteBen, true);
						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, codiceUtente, codiceEnteImpositore, true);
					}
					else if(DDLChanged.equals("codSocietaBen") || getFiredButton(request)==FiredButton.TX_BUTTON_SOCIETA_BEN_CHANGED)
					{
						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			

						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);

						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, codiceUtente, codiceEnteImpositore, false);

						
						loadProvinciaBenXml_DDL(request, session, codiceSocietaBen, true);
						loadDDLBenUtente(request, session, codiceSocietaBen, null, true);
						loadDDLBeneficiarioByImp(request, session, codiceSocieta,codiceProvincia, codiceUtente,codiceEnteImpositore,codiceSocietaBen,null,null, true);
					}
					else if (DDLChanged.equals("codProvinciaBen")||getFiredButton(request)==FiredButton.TX_BUTTON_PROVINCIA_BEN_CHANGED) 
					{
						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			

						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);

						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, codiceUtente, codiceEnteImpositore, false);

						loadProvinciaBenXml_DDL(request, session, codiceSocietaBen, false);
						loadDDLBenUtente(request, session, codiceSocietaBen, codiceProvinciaBen,true);
						loadDDLBeneficiarioByImp(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore,codiceSocietaBen,codiceProvinciaBen,null, true);
					}
					else if (DDLChanged.equals("codUtenteBen")||getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_BEN_CHANGED) 
					{
						//mod090
//						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
//						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			

						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			

						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);

						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, codiceUtente, codiceEnteImpositore, false);

						loadProvinciaBenXml_DDL(request, session, codiceSocietaBen, false);
						loadDDLBenUtente(request, session, codiceSocietaBen, codiceProvinciaBen, false);			
						// fine mod090
						//LoadListaUtentiEntiXml_DDL(request, session, codiceSocietaBen, codiceProvinciaBen, null, codiceUtenteBen,true);
						loadDDLBeneficiarioByImp(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore,codiceSocietaBen,codiceProvinciaBen,codiceUtenteBen, true);
					}
					
					else 
					{
						loadProvinciaXml_DDL(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);
						loadProvinciaBenXml_DDL(request, session, codiceSocietaBen, false);
						loadDDLBenUtente(request, session, codiceSocietaBen, codiceProvinciaBen,false);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
						//loadProvinciaXml_DDL(request, session, codiceSocietaBen, false);
						//loadDDLUtente(request, session, codiceSocietaBen, codiceProvinciaBen, false);			
						loadDDLBeneficiarioByImp(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnteImpositore,codiceSocietaBen,codiceProvinciaBen,codiceUtenteBen, false);
						//loadTipologiaServizioXml_DDL(request, session, codiceSocieta,false);
						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, codiceUtente, codiceEnteImpositore, false);
					}
				break;
			case 111:
					if (DDLChanged.equals("codSocieta")||getFiredButton(request)==FiredButton.TX_BUTTON_SOCIETA_CHANGED) 
					{
						
						loadDDLUtente(request, session, codiceSocieta, null, true);
						
						loadDDLBenUtente(request, session, codiceSocietaBen, null, false);

						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, null, true);
						
						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocietaBen, null, null, codiceUtenteBen, false);
						//loadTipologiaServizioXml_DDL(request, session, codiceSocieta,true);
						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, codiceUtente, codiceEnteImpositore, true);
					}
					else if (DDLChanged.equals("codUtente")||getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_CHANGED) 
					{
						//MOD090
//						loadDDLUtente(request, session, codiceSocieta, null ,false);			
						loadDDLUtente(request, session, codiceSocieta, null ,false);
						loadDDLBenUtente(request, session, codiceSocietaBen, null, false);
						//fine mod090
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, codiceUtente, true);
						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocietaBen, null, null, codiceUtenteBen, false);
						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, codiceUtente, codiceEnteImpositore, true);

					}else if(DDLChanged.equals("codSocietaBen")||getFiredButton(request)==FiredButton.TX_BUTTON_SOCIETA_BEN_CHANGED)
					{
					
						loadDDLUtente(request, session, codiceSocieta, null, false);
						
						loadDDLBenUtente(request, session, codiceSocietaBen, null, true);

						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, codiceUtente, false);
						
						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocietaBen, null, null, null, true);
						//loadTipologiaServizioXml_DDL(request, session, codiceSocieta,true);
						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, codiceUtente, codiceEnteImpositore, false);
					
					}
					else if (DDLChanged.equals("codUtenteBen")||getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_BEN_CHANGED) 
					{
						//MOD090
//						loadDDLUtente(request, session, codiceSocieta, null ,false);			
						loadDDLUtente(request, session, codiceSocieta, null ,false);
						loadDDLBenUtente(request, session, codiceSocietaBen, null, false);
						//fine mod090
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, codiceUtente, false);
						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocietaBen, null, null, codiceUtenteBen, true);
						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, codiceUtente, codiceEnteImpositore, false);
					
					}else {

						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);
						loadDDLBenUtente(request, session, codiceSocietaBen, null, false);
						
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocietaBen, codiceProvincia, null, codiceUtenteBen,false);
						//loadTipologiaServizioXml_DDL(request, session, codiceSocieta,false);
						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, codiceUtente, codiceEnteImpositore, true);
						}
				break;
		}
	}
	
	public void esecuzioneRicerca(HttpServletRequest request, boolean useMessage) throws FaultType, Exception
	{
		
		AssocImpBenSearchResponse out;
		AssocImpBenSearchRequest in;
		in = prepareRicerca(request);
					
		out =  WSCache.assocImpBenServer.getAssociazioniImpBen(in, request);

		
		if(out.getPInfo().getNumRows() > 0) 
		{
			request.setAttribute("listaAssociazioni", out.getListXml());

//			request.setAttribute("listaAssociazioni.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		
			request.setAttribute("listaAssociazioni.pageInfo", getPageInfo(out.getPInfo()));		
		}
//		else setMessage(request, Messages.TX_NO_TRANSACTIONS.format());
		else
		{
			if (useMessage)
//				setMessage(request, "Nessun risultato");
				setFormMessage("ricercaAssocImpBenForm", Messages.NO_DATA_FOUND.format(), request);
		}
		
		request.setAttribute("NumRighe", Integer.toString(out.getPInfo().getNumRows()));

	}
	
	private AssocImpBenSearchRequest prepareRicerca(HttpServletRequest request) throws Exception
	{

		PropertiesTree configuration; 
		AssocImpBenSearchRequest ris;
		AssocImpBen associazione;	
		int rowsPerPage;
		
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		
		rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

        if (request.getAttribute("rowsPerPage")!=null&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
        {
        	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
        }
         
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").toString().equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new AssocImpBenSearchRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		associazione = new AssocImpBen();	
		
		requestToAssociazione(request, associazione);

		/*
		associazione.setCodSocieta(request.getParameter("codSocieta"));
        associazione.setCodBelfiore(request.getParameter("codProvincia"));
        associazione.setCodUtente(request.getParameter("codUtente"));
        associazione.setCodImpositore(request.getParameter("codImpositore"));
        associazione.setCodBeneficiario(request.getParameter("codBeneficiario"));

        associazione.setCodTipologiaServizio(request.getParameter("codTipologiaServizio"));
        associazione.setAnnoRifDa(request.getParameter("annoRifDa"));
        associazione.setAnnoRifA(request.getParameter("annoRifA"));
        associazione.setDataValidita(getData(request.getParameter("dataValidita_day"),request.getParameter("dataValidita_month"),request.getParameter("dataValidita_year")));

        associazione.setEmail("");

        associazione.setTipoRendicontazione("");
        associazione.setMetodoRend("");
        associazione.setStrumRiversamento("");

        associazione.setOperatorCode("");
*/
        ris.setAssocImpBen(associazione);
		return ris;
		
	}
	
//	private void associazioneToRequest(HttpServletRequest request, AssocImpBen associazione, boolean completo)
	private void associazioneToRequest(HttpServletRequest request, AssocImpBen associazione)
	{
		request.setAttribute("codSocieta", associazione.getCodSocieta());
		request.setAttribute("codProvincia", associazione.getCodBelfiore());

//		request.setAttribute("codUtente", associazione.getCodUtente());
//		request.setAttribute("codUtente", associazione.getCodSocieta() + associazione.getCodUtente());
		if (associazione.getCodUtente()!=null&&!associazione.getCodUtente().equals(""))
			request.setAttribute("codUtente", associazione.getCodSocieta() + associazione.getCodUtente());
		else
			request.setAttribute("codUtente","");
		
//		request.setAttribute("codImpositore", request.getParameter("codUtente") + associazione.getCodImpositore());
//		request.setAttribute("codImpositore", associazione.getCodImpositore());	
//		request.setAttribute("codImpositore", associazione.getCodSocieta() + associazione.getCodUtente() + associazione.getCodImpositore());
		if (associazione.getCodImpositore()!=null&&!associazione.getCodImpositore().equals(""))	
			request.setAttribute("codImpositore", associazione.getCodSocieta() + associazione.getCodUtente() + associazione.getCodImpositore());
		else
			request.setAttribute("codImpositore","");
		

		
		request.setAttribute("codSocietaBen", associazione.getCodSocietaBen());
		request.setAttribute("codProvinciaBen", associazione.getCodBelfioreBen());

//		request.setAttribute("codUtente", associazione.getCodUtente());
//		request.setAttribute("codUtente", associazione.getCodSocieta() + associazione.getCodUtente());
		if (associazione.getCodUtenteBen()!=null&&!associazione.getCodUtenteBen().equals(""))
			request.setAttribute("codUtenteBen", associazione.getCodSocietaBen() + associazione.getCodUtenteBen());
		else
			request.setAttribute("codUtenteBen","");
		
		
		//		request.setAttribute("codBeneficiario", request.getParameter("codUtente") + associazione.getCodBeneficiario());
/*
		if (completo)        	
    		request.setAttribute("codBeneficiario", associazione.getCodSocieta() + associazione.getCodUtente() + associazione.getCodBeneficiario());
        else
        	request.setAttribute("codBeneficiario", associazione.getCodBeneficiario());
*/
		if (associazione.getCodBeneficiario()!=null&&!associazione.getCodBeneficiario().equals(""))
    		request.setAttribute("codBeneficiario", associazione.getCodSocietaBen() + associazione.getCodUtenteBen() + associazione.getCodBeneficiario());
		else
			request.setAttribute("codBeneficiario","");
			
        request.setAttribute("codTipologiaServizio", associazione.getCodTipologiaServizio());
		
		request.setAttribute("annoRifDa", associazione.getAnnoRifDa());
		request.setAttribute("annoRifA", associazione.getAnnoRifA());
	//	request.setAttribute("dataValidita", associazione.getDataValidita());
	 
		
		java.util.Calendar cal = getCalendarS(request, associazione.getDataValidita());
		request.setAttribute("dataValidita", cal);

		//provvisorio
		if (associazione.getDataValidita() != null && associazione.getDataValidita().length()>0)
			request.setAttribute("dataValiditaS", associazione.getDataValidita().substring(8,10) + "/" + 
					associazione.getDataValidita().substring(5,7) + "/" + associazione.getDataValidita().substring(0,4));
		else
			request.setAttribute("dataValiditaS", "");

		
		//request.setAttribute("email", associazione.getEmail());

		request.setAttribute("tipoRendicontazione", associazione.getTipoRendicontazione());
		request.setAttribute("strumRiversamento", associazione.getStrumRiversamento());
		request.setAttribute("metodoRend", associazione.getMetodoRend());
		
	}

	
	/*
	private void requestToAssociazione(HttpServletRequest request, AssocImpBen associazione) throws Exception
	{
		String dataA = null;
		String dataDa = null;
		associazione.setCodSocieta(request.getParameter("codSocieta")!=null?request.getParameter("codSocieta"):"");
        associazione.setCodBelfiore(request.getParameter("codProvincia")!=null?request.getParameter("codProvincia"):"");
        associazione.setCodUtente(request.getParameter("codUtente")!=null?request.getParameter("codUtente"):"");
        
		String codImpositore = request.getParameter("codImpositore")!=null?request.getParameter("codImpositore"):"";
//		codImpositore = codImpositore.substring(5, codImpositore.length());

		String codBeneficiario = request.getParameter("codBeneficiario")!=null?request.getParameter("codBeneficiario"):"";
//		codBeneficiario = codBeneficiario.substring(5, codBeneficiario.length());

        
        associazione.setCodImpositore(codImpositore);
        associazione.setCodBeneficiario(codBeneficiario);

        associazione.setCodTipologiaServizio(request.getParameter("codTipologiaServizio")!=null?request.getParameter("codTipologiaServizio"):"");
        
        dataDa = request.getParameter("annoRifDa")!=null?request.getParameter("annoRifDa"):"";
        dataA = request.getParameter("annoRifA")!=null?request.getParameter("annoRifA"):"";
        
        
        associazione.setAnnoRifDa(dataDa);
        associazione.setAnnoRifA(dataA);
       
        
        if (!dataDa.equals("")&&!dataA.equals("")&&dataDa.compareTo(dataA)>0)
        {       	
        	throw new FaultType(100, "l'intervallo settato per l'anno di riferimento non è corretto");
        }
        
        if (request.getParameter("dataValidita")!=null && request.getParameter("dataValidita").length()>0)
            associazione.setDataValidita(request.getParameter("dataValidita").substring(0,10));
        else
        	associazione.setDataValidita(request.getParameter("dataValidita_day")!=null?getData(request.getParameter("dataValidita_day"),request.getParameter("dataValidita_month"),request.getParameter("dataValidita_year")):"");
        
//        associazione.setEmail(request.getParameter("email")!=null?request.getParameter("email"):"");
        associazione.setEmail("");

        associazione.setTipoRendicontazione(request.getParameter("tipoRendicontazione")!=null?request.getParameter("tipoRendicontazione"):"");
        associazione.setStrumRiversamento(request.getParameter("strumRiversamento")!=null?request.getParameter("strumRiversamento"):"");
        associazione.setMetodoRend(request.getParameter("metodoRend")!=null?request.getParameter("metodoRend"):"");

        associazione.setOperatorCode((String)request.getAttribute("operatore"));
	}
*/
	
	private void requestToAssociazione(HttpServletRequest request, AssocImpBen associazione) throws Exception
	{
		String dataA = null;
		String dataDa = null;
		associazione.setCodSocieta(request.getAttribute("codSocieta")!=null?request.getAttribute("codSocieta").toString():"");
        associazione.setCodBelfiore(request.getAttribute("codProvincia")!=null?request.getAttribute("codProvincia").toString():"");
     
//        associazione.setCodUtente(request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"");
        String codUtente = request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"";
        if (codUtente.length()==10)
        	codUtente = codUtente.substring(5);        	
        associazione.setCodUtente(codUtente);
        
		associazione.setCodSocietaBen(request.getAttribute("codSocietaBen")!=null?request.getAttribute("codSocietaBen").toString():"");
        associazione.setCodBelfioreBen(request.getAttribute("codProvinciaBen")!=null?request.getAttribute("codProvinciaBen").toString():"");
     
//        associazione.setCodUtente(request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"");
        String codUtenteBen = request.getAttribute("codUtenteBen")!=null?request.getAttribute("codUtenteBen").toString():"";
        if (codUtenteBen.length()==10)
        	codUtenteBen = codUtenteBen.substring(5);        	
        associazione.setCodUtenteBen(codUtenteBen);

        
        String codImpositore = request.getAttribute("codImpositore")!=null?request.getAttribute("codImpositore").toString():"";
//agg
		if (codImpositore.length()==20) 
			codImpositore = codImpositore.substring(10);

		String codBeneficiario = request.getAttribute("codBeneficiario")!=null?request.getAttribute("codBeneficiario").toString():"";
//		codBeneficiario = codBeneficiario.substring(5, codBeneficiario.length());
		//agg
		if (codBeneficiario.length()==20) 
			codBeneficiario = codBeneficiario.substring(10);

        
        associazione.setCodImpositore(codImpositore);
        associazione.setCodBeneficiario(codBeneficiario);
        //Giulia 8/05/2014 INIZIO
        String serv = request.getAttribute("codTipologiaServizio")!=null?request.getAttribute("codTipologiaServizio").toString():"";
        if(serv!=null && !serv.equals("")){
        	associazione.setCodTipologiaServizio(serv.substring(0, 3));
    		if(serv.length()>3){
    			associazione.setCodSocieta(serv.substring(4));
    		}
    		else {
    			if (serv!=null && !serv.equals("") && serv.indexOf("_", 0) != -1) {
    				
    				serv= serv.replace("'", "");
    				associazione.setCodTipologiaServizio(serv.substring(0, 3));
    			}
    			else
    				associazione.setCodTipologiaServizio(serv);
    		}
        } else
        {
        	associazione.setCodTipologiaServizio("");
        }
        //Giulia 8/05/2014 FINE
        
       // associazione.setCodTipologiaServizio(request.getAttribute("codTipologiaServizio")!=null?request.getAttribute("codTipologiaServizio").toString():"");
        
        dataDa = request.getAttribute("annoRifDa")!=null?request.getAttribute("annoRifDa").toString():"";
        dataA = request.getAttribute("annoRifA")!=null?request.getAttribute("annoRifA").toString():"";
        
        
        associazione.setAnnoRifDa(dataDa);
        associazione.setAnnoRifA(dataA);
       
        
        if (!dataDa.equals("")&&!dataA.equals("")&&dataDa.compareTo(dataA)>0)
        {       	
        	throw new FaultType(100, "l'intervallo settato per l'anno di riferimento non è corretto");
        }
  
       /* 
        if (request.getAttribute("dataValidita")!=null && request.getAttribute("dataValidita").toString().length()>0)
            associazione.setDataValidita(request.getAttribute("dataValidita").toString().substring(0,10));
        else
        	associazione.setDataValidita(request.getAttribute("dataValidita_day")!=null?getData(request.getAttribute("dataValidita_day").toString(),request.getAttribute("dataValidita_month").toString(),request.getAttribute("dataValidita_year").toString()):"");
        */
        if (request.getAttribute("dataValidita_day")!=null&&request.getAttribute("dataValidita_day").toString().length()>0)
        	associazione.setDataValidita(request.getAttribute("dataValidita_day")!=null?getData(request.getAttribute("dataValidita_day").toString(),request.getAttribute("dataValidita_month").toString(),request.getAttribute("dataValidita_year").toString()):"");
        else
        	if (request.getAttribute("dataValidita")!=null&&request.getAttribute("dataValidita").toString().length()>10)
                 associazione.setDataValidita(request.getAttribute("dataValidita").toString().substring(0,10));
        	else 
        		associazione.setDataValidita("");
//        associazione.setEmail(request.getAttribute("email")!=null?request.getAttribute("email"):"");
        associazione.setEmail("");

        associazione.setTipoRendicontazione(request.getAttribute("tipoRendicontazione")!=null?request.getAttribute("tipoRendicontazione").toString():"");
        associazione.setStrumRiversamento(request.getAttribute("strumRiversamento")!=null?request.getAttribute("strumRiversamento").toString():"");
        associazione.setMetodoRend(request.getAttribute("metodoRend")!=null?request.getAttribute("metodoRend").toString():"");

        associazione.setOperatorCode((String)request.getAttribute("operatore"));
	}
	
	
	/*
	private void requestCToAssociazione(HttpServletRequest request, AssocImpBen associazione)
	{
		associazione.setCodSocieta(request.getParameter("cCodSocieta"));
		associazione.setCodBelfiore("");
        associazione.setCodUtente(request.getParameter("cCodUtente"));
        
		String codImpositore = request.getParameter("cCodImpositore");
//		codImpositore = codImpositore.substring(5, codImpositore.length());

//		String codBeneficiario = request.getParameter("cCodBeneficiario");
//		codBeneficiario = codBeneficiario.substring(5, codBeneficiario.length());

        
        associazione.setCodImpositore(codImpositore);
        associazione.setCodBeneficiario("");

        associazione.setCodTipologiaServizio(request.getParameter("cCodTipologiaServizio"));
        associazione.setAnnoRifDa(request.getParameter("cAnnoRifDa"));
        associazione.setAnnoRifA(request.getParameter("cAnnoRifA"));
        associazione.setDataValidita(request.getParameter("cDataValidita").substring(0,10));
        
        associazione.setEmail("");

        associazione.setTipoRendicontazione("");
        associazione.setStrumRiversamento("");
        associazione.setMetodoRend("");

        associazione.setOperatorCode("test");
	}
	
	*/

	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
	}
}
