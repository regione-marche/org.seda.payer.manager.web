package org.seda.payer.manager.entrate.actions;



import java.rmi.RemoteException;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.ws.WSCache;
import java.util.Calendar;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.bancadati.webservice.srv.FaultType;
import com.seda.payer.bancadati.webservice.dati.Paginazione;
import com.seda.payer.bancadati.webservice.dati.RicercaPagamentiRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaPagamentiResponse;
import com.seda.payer.bancadati.webservice.dati.EstraiPagamentiRequest;
import com.seda.payer.bancadati.webservice.dati.EstraiPagamentiResponse;

public class RicercaPagamentiAction extends BaseEntrateAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		salvaStato(request);

		super.service(request);

		HttpSession session = request.getSession();
		
		String exp="0";
        boolean actExp = false;

		switch(getFiredButton(request)) 
			{
			case TX_BUTTON_NULL: {
				resetDDLSession(request);
				resetFiltri(request); //TK2020121188000041
				}; break;
			case TX_BUTTON_RESET: { 
				resetReqEDDL(request); 
				resetFiltri(request); //TK2020121188000041
				}; break;
			case TX_BUTTON_CERCA_EXP: 
				if(request.getParameter("hRowsPerPage") != null && request.getParameter("hRowsPerPage").length() >0 ) {
					request.setAttribute("rowsPerPage", request.getAttribute("hRowsPerPage"));
					request.setAttribute("pageNumber", request.getAttribute("hPageNumber"));
					request.setAttribute("order", request.getAttribute("hOrder"));
					request.setAttribute("ext", request.getParameter("hExt"));
				}

				if (request.getAttribute("ext") != null && request.getAttribute("ext").equals("0"))
					exp = "1";
				else
					exp = "0";
				actExp = true;


			case TX_BUTTON_CERCA: 
				if (request.getAttribute("ext")==null)
					request.setAttribute("ext","0");

				if (request.getAttribute("ext")=="" || actExp)
					request.setAttribute("ext",exp);
				
				try {
					String errore = controlloDate(request);
//					ricercaPagamenti(request);
						if (errore!=null)
							setFormMessage("ricercaPagamentiForm", "errore: " + errore, request);
						else{
							salvaFiltriRicerca(request); //TK2020121188000041
							recuperaFiltriRicerca(request); //TK2020121188000041
							ricercaPagamenti(request);
						}
					}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaPagamentiForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaPagamentiForm", testoErroreColl, request);					
				}
				catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					e.printStackTrace();
					setFormMessage("ricercaPagamentiForm", "errore ricerca: " + testoErrore, request);
				}
				break;

			case TX_BUTTON_DOWNLOAD:
				
				String nomeFile="";
				try {
					nomeFile = getListaPagamentiCSV(request);
				}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaPagamentiForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaPagamentiForm", testoErroreColl, request);					
				}
				catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					e.printStackTrace();
					setFormMessage("ricercaPagamentiForm", "errore ricerca: " + testoErrore, request);
				}

				request.setAttribute("filename", "pagamenti.zip");		//passaggio zip fisico
				request.setAttribute("pagamentiCSVZIP", nomeFile);		//passaggio zip fisico
				break;
				
				//TK2020121188000041
			case TX_BUTTON_TIPO_SERVIZIO_CHANGED: {
			    	salvaFiltriRicerca(request);
			    	recuperaFiltriRicerca(request);
				}
			    //FINE TK2020121188000041
			}

		
		super.service(request);
        aggiornamentoCombo(request, session);				

		return null;
	}

	
    public void ricercaPagamenti(HttpServletRequest request) throws Exception
    {
    	
			RicercaPagamentiResponse out;
			RicercaPagamentiRequest in;
			in = prepareRicerca(request);
						
			WSCache.logWriter.logDebug("Pagina interrogazione pagamenti");
			out =  WSCache.entrateBDServer.ricercaPagamenti(in, request);

			
			if(out.getPInfo().getNumRows() > 0) 
			{
				request.setAttribute("listaPagamenti", out.getListXml());

				request.setAttribute("listaPagamenti.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		
			}
			else
			{
				String messaggio="";
				if (request.getAttribute("messaggioRis")!=null)
					messaggio = request.getAttribute("messaggioRis").toString() + " - ";
				setFormMessage("ricercaPagamentiForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
			}
		
	
    }

    private void resetReqEDDL(HttpServletRequest request)
    {
		resetParametri(request);
		request.setAttribute("data_pagamento_da", null);
		request.setAttribute("data_pagamento_a", null);
		setProfile(request);
		resetDDLSession(request);
    }
    
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
		switch(getFiredButton(request)) 
		{
			   case TX_BUTTON_SOCIETA_CHANGED:
					//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
				    loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
					loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, true);
					loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
					LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
					loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
					break;
				case TX_BUTTON_UTENTE_CHANGED:
					//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
					loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
					loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
					loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
					LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
					loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
					break;
				case TX_BUTTON_IMPOSITORE_CHANGED:
					//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
					loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
					loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
					loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
					LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
					loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
					break;
				case TX_BUTTON_TIPO_SERVIZIO_CHANGED:
					//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,false);
					loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,false);
					loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
					loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
					LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
					loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);
					break;
				default:
					switch (getProfiloUtente(userProfile)) {
					case AMMI: 
						paramCodiceUtente = null;
						paramCodiceEnte = null;
						break;
					case AMUT:
						paramCodiceEnte = null;
						break;
					}
					//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,false);
					loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,false);
					loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
					loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, false);
					LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
					loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);			
		}		
	}

	
	private RicercaPagamentiRequest prepareRicerca(HttpServletRequest request)
	{
		PropertiesTree configuration; 
		RicercaPagamentiRequest ris;

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new RicercaPagamentiRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		
	 	ris.setCodiceSocieta(paramCodiceSocieta);
		ris.setCodiceUtente(paramCodiceUtente);
		ris.setCodiceEnte(paramCodiceEnte);			
		//ris.setImpostaServizio(request.getParameter("impostaServ"));
		ris.setCodiceFiscale(request.getParameter("codFiscale"));
		ris.setModalita(request.getParameter("modalita"));
		ris.setTipoMovimento(request.getParameter("tipoMovimento"));
		
        //TKTK2020121188000041
        String impServ = "";
        if(request.getParameter("impostaServ")!=null) {
        	impServ = request.getParameter("impostaServ");
        } else if (request.getSession().getAttribute("tx_imposta_servizio_value")!=null) {
        	impServ = request.getSession().getAttribute("tx_imposta_servizio_value").toString();
        }
        //FINE TKTK2020121188000041
        
        if(impServ != null && impServ.length() > 0){
        	ris.setCodiceSocieta(impServ.substring(0,5));
        	ris.setTipologiaServizio(impServ.substring(5,8));
        	ris.setImpostaServizio(impServ.substring(8,impServ.length()));
        }

		
		String ufficioImpositore = request.getParameter("ufficioImpositore");
		if(ufficioImpositore != null && !ufficioImpositore.equals("")){
			ris.setTipoUfficio(ufficioImpositore.substring(0,ufficioImpositore.indexOf('-')));
			ris.setCodiceUfficio(ufficioImpositore.substring(ufficioImpositore.indexOf('-')+1));
		}else{
			ris.setTipoUfficio("");
			ris.setCodiceUfficio("");
		}
		
		ris.setAnnoEmissione(request.getParameter("annoEmissione"));
		ris.setNumeroEmissione(request.getParameter("numEmissione"));

        ris.setDataPagamentoDa(getData(request.getParameter("data_pagamento_da_day"),request.getParameter("data_pagamento_da_month"),request.getParameter("data_pagamento_da_year")));
		ris.setDataPagamentoA(getData(request.getParameter("data_pagamento_a_day"),request.getParameter("data_pagamento_a_month"),request.getParameter("data_pagamento_a_year")));
        
		//Giulia 8/05/2014 INIZIO
		if(paramCodiceTipologiaServizio!=null && !paramCodiceTipologiaServizio.equals("") && userBean.getProfile().equals("AMMI")){
		ris.setTipologiaServizio(paramCodiceTipologiaServizio.substring(0, 3));
		if((paramCodiceTipologiaServizio).length()>0){
			ris.setCodiceSocieta(paramCodiceTipologiaServizio.substring(4));
		}
		}
		else {
			if (paramCodiceTipologiaServizio!=null && !paramCodiceTipologiaServizio.equals("") && paramCodiceTipologiaServizio.indexOf("_", 0) != -1) {
				String serv = paramCodiceTipologiaServizio;
				serv= serv.replace("'", "");
				ris.setTipologiaServizio(serv.substring(0, 3));
			}
			else
				ris.setTipologiaServizio(paramCodiceTipologiaServizio);
		}
		//ris.setTipologiaServizio(paramCodiceTipologiaServizio);
		//Giulia 8/05/2014 FINE
		ris.setNumeroDocumento(request.getParameter("id_documento"));
		ris.setProgRiscossione(request.getParameter("progRiscossione"));

		return ris;
		
	}

	private String controlloDate(HttpServletRequest request)
	{
//		String messaggio = null;
		Calendar dataDa = getCalendar(request, "data_pagamento_da");
        Calendar dataA = getCalendar(request, "data_pagamento_a");
        
        if (dataDa==null||dataA==null)
        	return null;
        if (dataDa.equals(dataA))
        	return null;
        if (!dataDa.before(dataA))
        	return "La data pagamento da deve essere antecedente o uguale alla data pagamento a";
        dataDa.add(Calendar.DAY_OF_MONTH, 30);
        if (dataDa.before(dataA))
        	return "Il massimo range di giorni consentito è di 30 giorni";
        
        return null;
        
	}
	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
		 // Salvo le date che richiedono l'utilizzo di un tipo "Calendar"

		request.setAttribute("data_pagamento_da", getCalendar(request, "data_pagamento_da"));
		request.setAttribute("data_pagamento_a", getCalendar(request, "data_pagamento_a"));
	}

	private String getListaPagamentiCSV(HttpServletRequest request) throws FaultType, RemoteException {
		EstraiPagamentiResponse out;
		EstraiPagamentiRequest in = new EstraiPagamentiRequest();
		
		in = prepareRicercaCSV(request);
		out = WSCache.entrateBDServer.estraiPagamenti(in, request);
		return out.getFile();
	}

	private EstraiPagamentiRequest prepareRicercaCSV(HttpServletRequest request)
	{
		EstraiPagamentiRequest ris;

		ris = new EstraiPagamentiRequest();
		
		//set campi
		
	 	ris.setCodiceSocieta(paramCodiceSocieta);
		ris.setCodiceUtente(paramCodiceUtente);
		ris.setCodiceEnte(paramCodiceEnte);			
        String impServ = request.getParameter("impostaServ");
        if(impServ != null && impServ.length() > 0){
        	ris.setCodiceSocieta(impServ.substring(0,5));
        	ris.setTipologiaServizio(impServ.substring(5,8));
        	ris.setImpostaServizio(impServ.substring(8,impServ.length()));
        }
		//ris.setImpostaServizio(request.getParameter("impostaServ"));
		ris.setCodiceFiscale(request.getParameter("codFiscale"));
		ris.setModalita(request.getParameter("modalita"));
		ris.setTipoMovimento(request.getParameter("tipoMovimento"));

		String ufficioImpositore = request.getParameter("ufficioImpositore");
		if(ufficioImpositore != null && !ufficioImpositore.equals("")){
			ris.setTipoUfficio(ufficioImpositore.substring(0,ufficioImpositore.indexOf('-')));
			ris.setCodiceUfficio(ufficioImpositore.substring(ufficioImpositore.indexOf('-')+1));
		}else{
			ris.setTipoUfficio("");
			ris.setCodiceUfficio("");
		}
		
		ris.setAnnoEmissione(request.getParameter("annoEmissione"));
		ris.setNumeroEmissione(request.getParameter("numEmissione"));

        ris.setDataPagamentoDa(getData(request.getParameter("data_pagamento_da_day"),request.getParameter("data_pagamento_da_month"),request.getParameter("data_pagamento_da_year")));
		ris.setDataPagamentoA(getData(request.getParameter("data_pagamento_a_day"),request.getParameter("data_pagamento_a_month"),request.getParameter("data_pagamento_a_year")));

		ris.setTipologiaServizio(paramCodiceTipologiaServizio);
		ris.setNumeroDocumento(request.getParameter("id_documento"));
		ris.setProgRiscossione(request.getParameter("progRiscossione"));

		return ris;
		
	}
	
	//TK2020121188000041
	private void salvaFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(request.getParameter("impostaServVal")!=null) {
			session.setAttribute("tx_imposta_servizio_value", request.getParameter("impostaServVal"));
		}
		if(request.getParameter("tipServVal")!=null) {
			session.setAttribute("tx_tipologia_servizio_value", request.getParameter("tipServVal"));
		}
	}
	private void recuperaFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		request.setAttribute("tx_tipologia_servizio_value", session.getAttribute("tx_tipologia_servizio_value")!=null?session.getAttribute("tx_tipologia_servizio_value"):"");
		request.setAttribute("tx_imposta_servizio_value", session.getAttribute("tx_imposta_servizio_value")!=null?session.getAttribute("tx_imposta_servizio_value"):"");
	}
	private void resetFiltri(HttpServletRequest request) {
		HttpSession session = request.getSession();
		request.setAttribute("tx_tipologia_servizio_value", "");
		session.setAttribute("tx_tipologia_servizio_value", "");
		request.setAttribute("tx_imposta_servizio_value", "");
		session.setAttribute("tx_imposta_servizio_value", "");
	}
	//FINE TK2020121188000041
	
}
