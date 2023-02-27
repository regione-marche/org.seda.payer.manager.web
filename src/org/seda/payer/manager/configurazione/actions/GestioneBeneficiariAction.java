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
import com.seda.payer.pgec.webservice.beneficiario.dati.ResponseTypeRetCode;
//import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.beneficiario.dati.BeneficiarioCancelRequest;
import com.seda.payer.pgec.webservice.beneficiario.dati.BeneficiarioDetailRequest;
import com.seda.payer.pgec.webservice.beneficiario.dati.BeneficiarioDetailResponse;
import com.seda.payer.pgec.webservice.beneficiario.dati.BeneficiarioSaveRequest;
import com.seda.payer.pgec.webservice.beneficiario.dati.Paginazione;
import com.seda.payer.pgec.webservice.beneficiario.dati.BeneficiarioSearchRequest;
import com.seda.payer.pgec.webservice.beneficiario.dati.BeneficiarioSearchResponse;
import com.seda.payer.pgec.webservice.beneficiario.dati.Beneficiario;
import com.seda.payer.pgec.webservice.beneficiario.dati.StatusResponse;
import org.seda.payer.manager.util.iban.IBAN;

public class GestioneBeneficiariAction extends BaseRiversamentoAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		salvaStato(request);

		super.service(request);

		HttpSession session = request.getSession();
		


		if (DDLChanged.equals(""))
			switch(getFiredButton(request)) 
				{
				case TX_BUTTON_RESET:
					resetParametri(request);
					setProfile(request);
				case TX_BUTTON_NULL: 
					request.setAttribute("DDLType", "13");
					resetDDLSession(request);
				case TX_BUTTON_CERCA: 
					
					try {
							esecuzioneRicerca(request, true);
						}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("ricercaBeneficiariForm", "errore: " + decodeMessaggio(fte), request);
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("errore: " + re.getMessage(),re);
						re.printStackTrace();
						setFormMessage("ricercaAssocImpBenForm", testoErroreColl, request);					
					}
					catch (Exception e) {
						WSCache.logWriter.logError("errore: " + e.getMessage(),e);
						//setErrorMessage(request, e.getMessage());
						setFormMessage("ricercaBeneficiariForm", "errore: " + testoErrore, request);
					}
	
	//				resetDDLSession(request);
	
					break;
				case TX_BUTTON_AGGIUNGI:
	
					//creazione immagine rVariabili
					request.setAttribute("rCodSocieta", request.getAttribute("codSocieta"));
					request.setAttribute("rCodProvincia", request.getAttribute("codProvincia"));
					request.setAttribute("rCodUtente", request.getAttribute("codUtente"));
					request.setAttribute("rCodBeneficiario", request.getAttribute("codBeneficiario"));
					request.setAttribute("rDDLChanged", request.getAttribute("DDLChanged"));
					request.setAttribute("tastoIns", "ok");
					
								
					// set form vuota di inserimento
					request.setAttribute("codSocieta", "");
					request.setAttribute("codUtente", "");
					request.setAttribute("codAnagrafica", "");
	
					request.setAttribute("im_codSia", "");
					request.setAttribute("im_codFiscale", "");
					request.setAttribute("im_indirizzo", "");
					request.setAttribute("im_email", "");
					request.setAttribute("im_iban_codPaese", "");
					request.setAttribute("im_iban_codControllo", "");
					request.setAttribute("im_iban_cin", "");
					request.setAttribute("im_iban_abi", "");
					request.setAttribute("im_iban_cab", "");
					request.setAttribute("im_iban_cc", "");
					request.setAttribute("im_flagTipologiaFile", "");
					//aggiunta descrizione GG
					request.setAttribute("im_descBeneficiario", "");
					request.setAttribute("DDLType", "121");
	
					resetDDLSession(request);
	
					/*
					loadDDLUtente(request, session, null, null, true);			
					LoadListaUtentiEntiXml_DDL(request, session, null, null, null, null,true);
	*/
					
					break;
				case TX_BUTTON_RESET_INS:
					// set form vuota di inserimento
					request.setAttribute("codSocieta", "");
					request.setAttribute("codUtente", "");
					request.setAttribute("codAnagrafica", "");
	
					request.setAttribute("im_codSia", "");
					request.setAttribute("im_codFiscale", "");
					request.setAttribute("im_indirizzo", "");
					request.setAttribute("im_email", "");
					request.setAttribute("im_flagTipologiaFile", "");
					request.setAttribute("im_iban_codPaese", "");
					request.setAttribute("im_iban_codControllo", "");
					request.setAttribute("im_iban_cin", "");
					request.setAttribute("im_iban_abi", "");
					request.setAttribute("im_iban_cab", "");
					request.setAttribute("im_iban_cc", "");
					//GG
					request.setAttribute("im_descBeneficiario", "");
	
					resetDDLSession(request);
					
					break;
					
				case TX_BUTTON_AGGIUNGI_END:
					
					//ripresa dei campi
					try 
					{
						BeneficiarioSaveRequest in = new BeneficiarioSaveRequest();
						Beneficiario ben = new Beneficiario();
						StatusResponse out;
	

	
						ben.setCodSocieta(request.getParameter("codSocieta"));

						//						ben.setCodUtente(request.getParameter("codUtente"));
				        String codUtente = request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"";
				        if (codUtente.length()==10)
				        	codUtente = codUtente.substring(5);        	
						ben.setCodUtente(codUtente);

						String codBeneficiario = request.getParameter("codAnagrafica");
//						codBeneficiario = codBeneficiario.substring(5, codBeneficiario.length());
						if (codBeneficiario.length()==20) 
							codBeneficiario = codBeneficiario.substring(10);
						ben.setChiaveBeneficiario(codBeneficiario);
						
					    ben.setCodiceSia(request.getParameter("im_codSia"));
					    ben.setCodiceFiscale(request.getParameter("im_codFiscale"));
					    ben.setIndirizzo(request.getParameter("im_indirizzo"));
					    ben.setEmail(request.getParameter("im_email"));
					    ben.setFlagTipologiaFile(request.getParameter("im_flagTipologiaFile"));
					    //GG
					    ben.setDescBeneficiario(request.getParameter("im_descBeneficiario"));
					    //controllo coerenza IBAN
/*
					    ben.setIban(
				    		    (request.getParameter("im_iban_codPaese").equals("")? "  " : request.getParameter("im_iban_codPaese")) +
				    		    (request.getParameter("im_iban_codControllo").equals("")? "  " : request.getParameter("im_iban_codControllo")) +
				    		    (request.getParameter("im_iban_cin").equals("")? " " : request.getParameter("im_iban_cin")) +   
				    		    (request.getParameter("im_iban_abi").equals("")? "     " : request.getParameter("im_iban_abi")) +
				    		    (request.getParameter("im_iban_cab").equals("")? "     " : request.getParameter("im_iban_cab"))+
				    		    (request.getParameter("im_iban_cc").equals("")? "            " : request.getParameter("im_iban_cc")));
*/
					    String codIban = 	(request.getParameter("im_iban_codPaese").equals("")? "  " : request.getParameter("im_iban_codPaese")) +
						    		    (request.getParameter("im_iban_codControllo").equals("")? "  " : request.getParameter("im_iban_codControllo")) +
						    		    (request.getParameter("im_iban_cin").equals("")? " " : request.getParameter("im_iban_cin")) +   
						    		    (request.getParameter("im_iban_abi").equals("")? "     " : request.getParameter("im_iban_abi")) +
						    		    (request.getParameter("im_iban_cab").equals("")? "     " : request.getParameter("im_iban_cab"))+
						    		    (request.getParameter("im_iban_cc").equals("")? "            " : request.getParameter("im_iban_cc"));

					    if (!codIban.trim().equals(""))
					    {
						    IBAN iban = new IBAN(codIban);
						    if (!iban.isValid())
						    {
						    	throw new FaultType(101, "Codice Iban errato, " + iban.getInvalidCause());
						    }
					    }
					    
					    ben.setIban(codIban);

					    ben.setOperatorCode((String)request.getAttribute("operatore"));
					    
						in.setCodOp(TypeRequest.ADD_SCOPE.scope());
						in.setBeneficiario(ben);
		
						out = WSCache.beneficiarioServer.save(in, request);
						
						if (out.getResponse().getRetCode().equals(ResponseTypeRetCode.value1))
						{
	//						setMessage(request, "Inserimento eseguito");
							setFormMessage("schedaBeneficiarioForm", "Inserimento eseguito", request);
							
							request.setAttribute("im_codSia", "");
							request.setAttribute("im_codFiscale", "");
							request.setAttribute("im_indirizzo", "");
							request.setAttribute("im_email", "");
							request.setAttribute("im_flagTipologiaFile", "");
							request.setAttribute("im_iban_codPaese", "");
							request.setAttribute("im_iban_codControllo", "");
							request.setAttribute("im_iban_cin", "");
							request.setAttribute("im_iban_abi", "");
							request.setAttribute("im_iban_cab", "");
							request.setAttribute("im_iban_cc", "");
	
							request.setAttribute("codSocieta", "");
							request.setAttribute("codUtente", "");
							request.setAttribute("codAnagrafica", "");
							//GG
							request.setAttribute("im_descBeneficiario", "");
							
							resetDDLSession(request);
						}
						else 
	//						setErrorMessage(request, "Inserimento fallito: " + out.getResponse().getRetMessage());
							setFormMessage("schedaBeneficiarioForm", "Inserimento fallito: " + out.getResponse().getRetMessage(), request);
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("Inserimento fallito: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("schedaBeneficiarioForm", "Inserimento fallito: " + decodeMessaggio(fte), request);
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("errore: " + re.getMessage(),re);
						re.printStackTrace();
						setFormMessage("ricercaAssocImpBenForm", testoErroreColl, request);					
					}
					catch (Exception e) 
					{
						WSCache.logWriter.logError("Inserimento fallito: " + e.getMessage(),e);
						//setErrorMessage(request, e.getMessage());
						setFormMessage("schedaBeneficiarioForm", "Inserimento fallito: " + testoErrore, request);
					}
					
					break;
				case TX_BUTTON_RESET_MOD:
//					request.setAttribute("codBeneficiario", request.getAttribute("codAnagrafica").toString().substring(5));
					String codBeneficiario = request.getParameter("codAnagrafica");
					if (codBeneficiario.length()==20) 
						codBeneficiario = codBeneficiario.substring(10);
					request.setAttribute("codBeneficiario", codBeneficiario);

				case TX_BUTTON_EDIT:
					try
					{
											
						BeneficiarioDetailRequest in = new BeneficiarioDetailRequest();
						BeneficiarioDetailResponse out;
					
						
						in.setCodSocieta((String)request.getAttribute("codSocieta"));

//						in.setCodUtente((String)request.getAttribute("codUtente"));
				        String codUtente = request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"";
				        if (codUtente.length()==10)
				        	codUtente = codUtente.substring(5);        	
						in.setCodUtente(codUtente);

						in.setChiaveBeneficiario((String)request.getAttribute("codBeneficiario"));
/*
						String codBeneficiario = request.getParameter("codBeneficiario");
						if (codBeneficiario.length()==20) 
							codBeneficiario = codBeneficiario.substring(10);
						in.setChiaveBeneficiario(codBeneficiario);
*/						
						out = WSCache.beneficiarioServer.getBeneficiario(in, request);
						
						Beneficiario ben = out.getBeneficiario();
	
						request.setAttribute("codSocieta", ben.getCodSocieta().trim());

//						request.setAttribute("codUtente", ben.getCodUtente().trim());
						request.setAttribute("codUtente", ben.getCodSocieta() + ben.getCodUtente().trim());

						
//						request.setAttribute("codAnagrafica", ben.getCodUtente() + ben.getChiaveBeneficiario().trim());
						request.setAttribute("codAnagrafica", ben.getCodSocieta() + ben.getCodUtente() + ben.getChiaveBeneficiario().trim());

						//					request.setAttribute("codAnagrafica", ben.getChiaveBeneficiario().trim());
						request.setAttribute("im_codSia", ben.getCodiceSia().trim());
						request.setAttribute("im_codFiscale", ben.getCodiceFiscale().trim());
						request.setAttribute("im_indirizzo", ben.getIndirizzo().trim());
						request.setAttribute("im_email", ben.getEmail().trim());
						request.setAttribute("im_flagTipologiaFile", ben.getFlagTipologiaFile().trim());
						//GG
						request.setAttribute("im_descBeneficiario", ben.getDescBeneficiario().trim());
						String iban = ben.getIban();
						if (iban.length()<26)
							iban="                              ";
						request.setAttribute("im_iban_codPaese", iban.substring(0, 2).trim());
						request.setAttribute("im_iban_codControllo", iban.substring(2, 4).trim());
						request.setAttribute("im_iban_cin", iban.substring(4, 5).trim());
						request.setAttribute("im_iban_abi", iban.substring(5,10).trim());
						request.setAttribute("im_iban_cab", iban.substring(10, 15).trim());
						request.setAttribute("im_iban_cc", iban.substring(15).trim());
	
						request.setAttribute("DDLType", "121");
	
						resetDDLSession(request);
	
						/*
						request.setAttribute("societaDdlDisabled", true);
						request.setAttribute("utenteDdlDisabled", true);
						request.setAttribute("anagraficaDdlDisabled", true);
							*/				
	
						/*					
						//caricamento liste e blocco
						
						loadDDLUtente(request, session, request.getParameter("codSocieta"), null, true);			
						LoadListaUtentiEntiXml_DDL(request, session, request.getParameter("codSocieta"), null, null, request.getParameter("codUtente"),true);
	*/					
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("schedaBeneficiarioForm", "errore: " + decodeMessaggio(fte), request);
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
				//		setErrorMessage(request, e.getMessage());					
						setFormMessage("schedaBeneficiarioForm", "errore: " + testoErrore, request);
					}
					
					break;
				case TX_BUTTON_EDIT_END:
	
					try 
					{
						BeneficiarioSaveRequest in = new BeneficiarioSaveRequest();
						Beneficiario ben = new Beneficiario();
						StatusResponse out;
		
	
						ben.setCodSocieta(request.getParameter("codSocieta").trim());

//						ben.setCodUtente(request.getParameter("codUtente").trim());
				        String codUtente = request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"";
				        if (codUtente.length()==10)
				        	codUtente = codUtente.substring(5);        	
						ben.setCodUtente(codUtente);

						
						
						codBeneficiario = request.getParameter("codAnagrafica");
//						codBeneficiario = codBeneficiario.substring(5, codBeneficiario.length());
						if (codBeneficiario.length()==20) 
							codBeneficiario = codBeneficiario.substring(10);
						ben.setChiaveBeneficiario(codBeneficiario.trim());

						
						
					    ben.setCodiceSia(request.getParameter("im_codSia").trim());
					    ben.setCodiceFiscale(request.getParameter("im_codFiscale").trim());
					    ben.setIndirizzo(request.getParameter("im_indirizzo").trim());
					    ben.setEmail(request.getParameter("im_email").trim());
					    ben.setFlagTipologiaFile((String)request.getAttribute("im_flagTipologiaFile"));
					    //GG
					    ben.setDescBeneficiario(request.getParameter("im_descBeneficiario").trim());
					    /*
					    ben.setIban(request.getParameter("im_iban_codPaese").trim()+request.getParameter("im_iban_codControllo").trim()
					    		    +request.getParameter("im_iban_cin").trim()+request.getParameter("im_iban_abi").trim()
					    		    +request.getParameter("im_iban_cab").trim()+request.getParameter("im_iban_cc").trim());
					    */

					    
					    

					    //controllo coerenza IBAN
					    /*
					    ben.setIban(
				    		    (request.getParameter("im_iban_codPaese").equals("")? "  " : request.getParameter("im_iban_codPaese")) +
				    		    (request.getParameter("im_iban_codControllo").equals("")? "  " : request.getParameter("im_iban_codControllo")) +
				    		    (request.getParameter("im_iban_cin").equals("")? " " : request.getParameter("im_iban_cin")) +   
				    		    (request.getParameter("im_iban_abi").equals("")? "     " : request.getParameter("im_iban_abi")) +
				    		    (request.getParameter("im_iban_cab").equals("")? "     " : request.getParameter("im_iban_cab"))+
				    		    (request.getParameter("im_iban_cc").equals("")? "            " : request.getParameter("im_iban_cc")));
					    */
					    String codIban = 	(request.getParameter("im_iban_codPaese").equals("")? "  " : request.getParameter("im_iban_codPaese")) +
						    		    (request.getParameter("im_iban_codControllo").equals("")? "  " : request.getParameter("im_iban_codControllo")) +
						    		    (request.getParameter("im_iban_cin").equals("")? " " : request.getParameter("im_iban_cin")) +   
						    		    (request.getParameter("im_iban_abi").equals("")? "     " : request.getParameter("im_iban_abi")) +
						    		    (request.getParameter("im_iban_cab").equals("")? "     " : request.getParameter("im_iban_cab"))+
						    		    (request.getParameter("im_iban_cc").equals("")? "            " : request.getParameter("im_iban_cc"));

					    if (!codIban.trim().equals(""))
					    {
					    	IBAN iban = new IBAN(codIban);
						    if (!iban.isValid())
						    {
						    	throw new FaultType(101, "Codice Iban errato, " + iban.getInvalidCause());
						    }
					    }
					    ben.setIban(codIban);
					    
					    ben.setOperatorCode((String)request.getAttribute("operatore"));
					    
						in.setCodOp(TypeRequest.EDIT_SCOPE.scope());
						in.setBeneficiario(ben);
		
						out = WSCache.beneficiarioServer.save(in, request);
						
						if (out.getResponse().getRetCode().equals(ResponseTypeRetCode.value1))
							//setMessage(request, "Aggiornamento eseguito");
							setFormMessage("schedaBeneficiarioForm", "Aggiornamento eseguito", request);
						else 
							//setErrorMessage(request, "Aggiornamento fallito: " + out.getResponse().getRetMessage());
							setFormMessage("schedaBeneficiarioForm", "Aggiornamento fallito: " + out.getResponse().getRetMessage(), request);
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("Aggiornamento fallito: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("schedaBeneficiarioForm", "Aggiornamento fallito: " + decodeMessaggio(fte), request);
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
						//setErrorMessage(request, e.getMessage());
						setFormMessage("schedaBeneficiarioForm", "Aggiornamento fallito: " + testoErrore, request);
					}
					
					break;
				case TX_BUTTON_DELETE:
					try
					{
						BeneficiarioDetailRequest in = new BeneficiarioDetailRequest();
						BeneficiarioDetailResponse out;
						
						in.setCodSocieta(request.getParameter("codSocieta"));
//						in.setCodUtente(request.getParameter("codUtente"));
				        String codUtente = request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"";
				        if (codUtente.length()==10)
				        	codUtente = codUtente.substring(5);        	
						in.setCodUtente(codUtente);

						
						in.setChiaveBeneficiario(request.getParameter("codBeneficiario"));
/*
						String codBeneficiario = request.getParameter("codBeneficiario");
						if (codBeneficiario.length()==20) 
							codBeneficiario = codBeneficiario.substring(10);
						in.setChiaveBeneficiario(codBeneficiario);
*/	
						//caricamento liste e blocco
						
						loadDDLUtente(request, session, request.getParameter("codSocieta"), null, true);			
						LoadListaUtentiEntiXml_DDL(request, session, request.getParameter("codSocieta"), null, null, request.getParameter("codUtente"),true);
						
						
						out = WSCache.beneficiarioServer.getBeneficiario(in, request);
						
						Beneficiario ben = out.getBeneficiario();
	
						request.setAttribute("codSocieta", ben.getCodSocieta().trim());

//						request.setAttribute("codUtente", ben.getCodUtente().trim());
						request.setAttribute("codUtente", ben.getCodSocieta() + ben.getCodUtente().trim());
//						request.setAttribute("codAnagrafica", ben.getCodUtente() + ben.getChiaveBeneficiario().trim());
						request.setAttribute("codAnagrafica", ben.getCodSocieta() + ben.getCodUtente() + ben.getChiaveBeneficiario().trim());
	//					request.setAttribute("codAnagrafica", ben.getChiaveBeneficiario().trim());
						request.setAttribute("im_codSia", ben.getCodiceSia().trim());
						request.setAttribute("im_codFiscale", ben.getCodiceFiscale().trim());
						request.setAttribute("im_indirizzo", ben.getIndirizzo().trim());
						request.setAttribute("im_email", ben.getEmail().trim());
						request.setAttribute("im_flagTipologiaFile", ben.getFlagTipologiaFile().trim());
						request.setAttribute("im_descBeneficiario", ben.getDescBeneficiario().trim());
						String iban = ben.getIban();
						if (iban.length()<26)
							iban="                              ";
						request.setAttribute("im_iban_codPaese", iban.substring(0, 2).trim());
						request.setAttribute("im_iban_codControllo", iban.substring(2, 4).trim());
						request.setAttribute("im_iban_cin", iban.substring(4, 5).trim());
						request.setAttribute("im_iban_abi", iban.substring(5,10).trim());
						request.setAttribute("im_iban_cab", iban.substring(10, 15).trim());
						request.setAttribute("im_iban_cc", iban.substring(15).trim());
	
						/*
						request.setAttribute("societaDdlDisabled", true);
						request.setAttribute("utenteDdlDisabled", true);
						request.setAttribute("anagraficaDdlDisabled", true);
							*/	
	
						request.setAttribute("DDLType", "121");
	
						request.setAttribute("disableCancella", new Boolean(false));
	
						resetDDLSession(request);
	
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("schedaBeneficiarioForm", "errore: " + decodeMessaggio(fte), request);
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
				//		setErrorMessage(request, e.getMessage());					
						setFormMessage("schedaBeneficiarioForm", "errore: " + testoErrore, request);
					}
					
					break;
				case TX_BUTTON_DELETE_END:
					try 
					{
						BeneficiarioCancelRequest in = new BeneficiarioCancelRequest();
						StatusResponse out;
	
						/*
						in.setCodSocieta(request.getParameter("cCodSocieta"));
						in.setCodUtente(request.getParameter("cCodUtente"));
						in.setChiaveBeneficiario(request.getParameter("cCodBeneficiario"));
						*/
						in.setCodSocieta(request.getParameter("codSocieta"));

//						in.setCodUtente(request.getParameter("codUtente"));
				        String codUtente = request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"";
				        if (codUtente.length()==10)
				        	codUtente = codUtente.substring(5);        	
						in.setCodUtente(codUtente);
						
						codBeneficiario = request.getParameter("codAnagrafica");
//						codBeneficiario = codBeneficiario.substring(5, codBeneficiario.length());
						if (codBeneficiario.length()==20) 
							codBeneficiario = codBeneficiario.substring(10);

						in.setChiaveBeneficiario(codBeneficiario);
	
						out = WSCache.beneficiarioServer.cancel(in, request);
						
						if (out.getResponse().getRetCode().equals(ResponseTypeRetCode.value1))
						{
							setFormMessage("schedaBeneficiarioForm", "Cancellazione eseguita", request);
							request.setAttribute("disableCancella", new Boolean(true));
						}
						else 
						{
							setFormMessage("schedaBeneficiarioForm", "Cancellazione fallita: " + out.getResponse().getRetMessage(), request);
							request.setAttribute("disableCancella", new Boolean(false));
						}
	//					esecuzioneRicerca(request, false);
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("Cancellazione fallita: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("schedaBeneficiarioForm", "Cancellazione fallita: " + decodeMessaggio(fte), request);
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
						//setErrorMessage(request, e.getMessage());
						setFormMessage("schedaBeneficiarioForm", "Cancellazione fallita: " + testoErrore, request);
					}
					break;
				case TX_BUTTON_INDIETRO:
					// riporto variabili rVar
					//creazione immagine rVariabili
					request.setAttribute("codSocieta", request.getAttribute("rCodSocieta"));
					request.setAttribute("codProvincia", request.getAttribute("rCodProvincia"));
					request.setAttribute("codUtente", request.getAttribute("rCodUtente"));
					request.setAttribute("codBeneficiario", request.getAttribute("rCodBeneficiario"));
					request.setAttribute("DDLChanged", request.getAttribute("rDDLChanged"));
	
					request.setAttribute("DDLType", "13");
	
					/*
					request.setAttribute("rowsPerPage", request.getAttribute("rRowsPerPage"));
					request.setAttribute("pageNumber", request.getAttribute("rPageNumber"));
					request.setAttribute("order", request.getAttribute("rOrder"));
	*/				
	
					resetDDLSession(request);
	
				/*	
					//caricamento liste e blocco
					
					loadDDLUtente(request, session, request.getAttribute("codSocieta").toString(), null, true);			
					LoadListaUtentiEntiXml_DDL(request, session, request.getAttribute("codSocieta").toString(), null, null, request.getAttribute("codUtente").toString(),true);
				*/	
					
					break;
					
					
	// ------------------------------------------				
					
	/*				
				case TX_BUTTON_SOCIETA_CHANGED:
					if (DDLType==13)
					{
						loadDDLProvinciaBen(request, session, codiceSocieta, true);
						loadDDLUtente(request, session, codiceSocieta, null, true);
						loadDDLBeneficiario(request, session, codiceSocieta, null, null, true);
					}
					if (DDLType==131)
					{
						loadDDLUtente(request, session, codiceSocieta, null, true);
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, null,true);
					}
					break;
	*/				
	/*
				case TX_BUTTON_PROVINCIA_CHANGED:
					if (DDLType==13)
					{
						loadDDLProvinciaBen(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
						loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, null, true);
					}
					break;
	*/
	/*				
				case TX_BUTTON_UTENTE_CHANGED:
					if (DDLType==13)
					{
						loadDDLProvinciaBen(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
						loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, codiceUtente, true);
					}
					if (DDLType==131)
					{
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,true);
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
			case 13:
					if (DDLChanged.equals("codSocieta")||getFiredButton(request)==FiredButton.TX_BUTTON_SOCIETA_CHANGED) 
					{
						
						loadDDLProvinciaBen(request, session, codiceSocieta, true);
						loadDDLUtente(request, session, codiceSocieta, null, true);
						loadDDLBeneficiario(request, session, codiceSocieta, null, null, true);

						/*
					loadDDLProvinciaBen(request, session, codiceSocieta, true);
					loadDDLUtente(request, session, codiceSocieta, null, true);
					loadDDLBeneficiario(request, session, codiceSocieta, null, null, true);

						 */
					}
					else if (DDLChanged.equals("codProvincia")||getFiredButton(request)==FiredButton.TX_BUTTON_PROVINCIA_CHANGED) 
					{
						loadDDLProvinciaBen(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
						loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, null, true);
						
						/*
 					loadDDLProvinciaBen(request, session, codiceSocieta, false);
					loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
					loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, null, true);

						 */
					}
					else if (DDLChanged.equals("codUtente")||getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_CHANGED) 
					{
//mod090
//						loadDDLProvinciaBen(request, session, codiceSocieta, false);
//						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
						loadDDLProvinciaBen(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia, false);			
//fine mod090
						loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, codiceUtente, true);

						/*
 					loadDDLProvinciaBen(request, session, codiceSocieta, false);
					loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
					loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, codiceUtente, true);

						 */
					}
					else 
					{
						loadDDLProvinciaBen(request, session, codiceSocieta,false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);
						loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, codiceUtente, false);
					}
				break;
			case 131:
					if (DDLChanged.equals("codSocieta")||getFiredButton(request)==FiredButton.TX_BUTTON_SOCIETA_CHANGED) 
					{
						loadDDLUtente(request, session, codiceSocieta, null, true);
//						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, null, null, null,true);
						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, null, null, null,true);
					}
					else if (DDLChanged.equals("codUtente")||getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_CHANGED) 
					{
						//					loadDDLProvincia(request, session, codiceSocieta, false);
//mod 090
//						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
// fine mod090
						//						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,true);
						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,true);
					}
					else 
					{
						//			loadDDLProvincia(request, session, codiceSocieta,false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);
//						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
					}
					break;
			case 121:
//mod090
/*
				loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);
//						LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
*/
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);
						LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
						// fine mod090

						break;
		}
	}
	
	
	public void esecuzioneRicerca(HttpServletRequest request, boolean useMessage) throws FaultType, Exception
	{
		
		BeneficiarioSearchResponse out;
		BeneficiarioSearchRequest in;
		in = prepareRicerca(request);
					
		out =  WSCache.beneficiarioServer.getBeneficiari(in, request);

		
		if(out.getPInfo().getNumRows() > 0) 
		{
			request.setAttribute("listaBeneficiari2", out.getListXml());

			
//			request.setAttribute("listaBeneficiari2.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		
			request.setAttribute("listaBeneficiari2.pageInfo", getPageInfo(out.getPInfo()));		
		}
//		else setMessage(request, Messages.TX_NO_TRANSACTIONS.format());
		else
		{
			if (useMessage)
//				setMessage(request, "Nessun risultato");
				setFormMessage("ricercaBeneficiariForm", Messages.NO_DATA_FOUND.format(), request);
				}
		
		request.setAttribute("NumRighe", Integer.toString(out.getPInfo().getNumRows()));

	}
	
	private BeneficiarioSearchRequest prepareRicerca(HttpServletRequest request)
	{

		PropertiesTree configuration; 
		BeneficiarioSearchRequest ris;
		int rowsPerPage;
		
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		
		rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

		
        if (request.getAttribute("rowsPerPage")!=null&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
        {
        	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
        }

		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").toString().equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();
		
		ris = new BeneficiarioSearchRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		ris.setCodSocieta(request.getAttribute("codSocieta")!=null?request.getAttribute("codSocieta").toString():"");
        ris.setCodBelfiore(request.getAttribute("codProvincia")!=null?request.getAttribute("codProvincia").toString():"");
//        ris.setCodUtente(request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"");
        String codUtente = request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"";
        if (codUtente.length()==10)
        	codUtente = codUtente.substring(5);        	
        ris.setCodUtente(codUtente);

        
        ris.setChiaveBeneficiario(request.getAttribute("codBeneficiario")!=null?request.getAttribute("codBeneficiario").toString():"");

		
		return ris;
		
	}
	
	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
	}
}
