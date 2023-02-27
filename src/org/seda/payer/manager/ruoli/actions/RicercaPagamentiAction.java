package org.seda.payer.manager.ruoli.actions;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.bancadati.webservice.srv.FaultType;
import com.seda.payer.bancadati.webservice.dati.EstraiPagamentiRuoliRequest;
import com.seda.payer.bancadati.webservice.dati.EstraiPagamentiRuoliResponse;
import com.seda.payer.bancadati.webservice.dati.Paginazione;
import com.seda.payer.bancadati.webservice.dati.RicercaPagamentiRuoliRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaPagamentiRuoliResponse;

public class RicercaPagamentiAction extends BaseRuoliAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		salvaStato(request);

		super.service(request);
		
		HttpSession session = request.getSession();
		
		String exp="0";
        boolean actExp = false;

		switch(getFiredButton(request)) 
			{
			case TX_BUTTON_NULL: 
				resetDDLSession(request);
				break;
			case TX_BUTTON_RESET:
				resetReqEDDL(request);			
				break;
			case TX_BUTTON_CERCA_EXP: 
				if (request.getParameter("ext") != null && request.getParameter("ext").equals("0"))
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
//				ricercaPagamenti(request);
				if (errore!=null)
					setFormMessage("ricercaPagamentiForm", "errore: " + errore, request);
				else
					ricercaRuoli(request);
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

				request.setAttribute("pagamentiCSVZIP", nomeFile);		//passaggio zip fisico
				request.setAttribute("pagamentiCSVZIPD", "pagamentiCSV.zip");		
				break;
			
			}

		
		super.service(request);
        aggiornamentoCombo(request, session);				
		//visuDataDefault(request);
		return null;
	}

    public void ricercaRuoli(HttpServletRequest request) throws Exception
    {
    	
			RicercaPagamentiRuoliResponse out;
			RicercaPagamentiRuoliRequest in;
			in = prepareRicerca(request);
						
			WSCache.logWriter.logDebug("Pagina interrogazione di test");
			out =  WSCache.ruoliBDServer.ricercaPagamenti(in, request);
			
			if(out.getPInfo().getNumRows() > 0) 
			{
				request.setAttribute("listaPagamenti", out.getListXml());

				request.setAttribute("listaPagamenti.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		

				request.setAttribute("totImportoArticoli", out.getTotImpArticoli());
				request.setAttribute("totImportoMora", out.getTotImpMora());
				
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
		request.setAttribute("data_registrazione_da", null);
		request.setAttribute("data_registrazione_a", null);
		request.setAttribute("data_pagamento_da", null);
		request.setAttribute("data_pagamento_a", null);
		setProfile(request);
		resetDDLSession(request);
    }
    
    private String getFormatDate(java.util.Calendar data){
    	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    	return dateformat.format(data.getTime());
    } 
    
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
		switch(getFiredButton(request)) 
		{
		   case TX_BUTTON_SOCIETA_CHANGED:
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			case TX_BUTTON_UTENTE_CHANGED:
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			case TX_BUTTON_IMPOSITORE_CHANGED:
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			default:
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);			
		
		}		
	}
	
	private RicercaPagamentiRuoliRequest prepareRicerca(HttpServletRequest request)
	{
		PropertiesTree configuration; 
		RicercaPagamentiRuoliRequest ris;

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new RicercaPagamentiRuoliRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi

		ris.setCodiceSocieta(paramCodiceSocieta);
        ris.setCodiceUtente(paramCodiceUtente);
        ris.setCodiceEnte(paramCodiceEnte);

        ris.setAnnoRuolo(request.getParameter("annoRuolo"));
        ris.setNumeroRuolo(request.getParameter("numRuolo"));
		
        ris.setDataRegistrazioneDa(getData(request.getParameter("data_registrazione_da_day"),request.getParameter("data_registrazione_da_month"),request.getParameter("data_registrazione_da_year")));
        ris.setDataRegistrazioneA(getData(request.getParameter("data_registrazione_a_day"),request.getParameter("data_registrazione_a_month"),request.getParameter("data_registrazione_a_year")));
        ris.setDataEffettivaDa(getData(request.getParameter("data_pagamento_da_day"),request.getParameter("data_pagamento_da_month"),request.getParameter("data_pagamento_da_year")));
        ris.setDataEffettivaA(getData(request.getParameter("data_pagamento_a_day"),request.getParameter("data_pagamento_a_month"),request.getParameter("data_pagamento_a_year")));

        ris.setCodiceFiscale(request.getParameter("codFisc"));
        return ris;
		
	}

	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
		/*
		 * Salvo le date che richiedono l'utilizzo di un tipo "Calendar"
		 */
		request.setAttribute("data_registrazione_da", getCalendar(request, "data_registrazione_da"));
		request.setAttribute("data_registrazione_a", getCalendar(request, "data_registrazione_a"));
		request.setAttribute("data_pagamento_da", getCalendar(request, "data_pagamento_da"));
		request.setAttribute("data_pagamento_a", getCalendar(request, "data_pagamento_a"));

	}

	private String controlloDate(HttpServletRequest request)
	{
//		String messaggio = null;
		Calendar dataDaR = getCalendar(request, "data_registrazione_da");
        Calendar dataAR = getCalendar(request, "data_registrazione_a");
		Calendar dataDaP = getCalendar(request, "data_pagamento_da");
        Calendar dataAP = getCalendar(request, "data_pagamento_a");

        
        if (dataDaR==null||dataAR==null)
        	return null;
        if (dataDaR.equals(dataAR))
        	return null;
        if (!dataDaR.before(dataAR))
        	return "La data registrazione da deve essere antecedente o uguale alla data registrazione a";
        if (dataDaP==null||dataAP==null)
        	return null;
        if (dataDaP.equals(dataAP))
        	return null;
        if (!dataDaP.before(dataAP))
        	return "La data effettivo pagamento da deve essere antecedente o uguale alla data effettivo pagamento a";

        
        dataDaR.add(Calendar.DAY_OF_MONTH, 30);
        if (dataDaR.before(dataAR))
        	return "Il massimo range di giorni consentito per la data registrazione è di 30 giorni";
        
        dataDaP.add(Calendar.DAY_OF_MONTH, 30);
        if (dataDaP.before(dataAP))
        	return "Il massimo range di giorni consentito per la data effettivo pagamento è di 30 giorni";
        
        return null;
        
	}
	
	private void visuDataDefault(HttpServletRequest request){
		//date default
		if(request.getParameter("reloadPagamenti") == null){
			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
			cal2.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
			
			request.setAttribute("data_registrazione_da",cal);
			request.setAttribute("data_pagamento_da",cal);

			request.setAttribute("data_registrazione_a",cal2);
			request.setAttribute("data_pagamento_a",cal2);
		}
		
	}
	
	private String getListaPagamentiCSV(HttpServletRequest request) throws FaultType, RemoteException {
		EstraiPagamentiRuoliResponse out;
		EstraiPagamentiRuoliRequest in = new EstraiPagamentiRuoliRequest();
		
		in = prepareRicercaCSV(request);
		out = WSCache.ruoliBDServer.estraiPagamenti(in, request);
		return out.getFile();
	}

	private EstraiPagamentiRuoliRequest prepareRicercaCSV(HttpServletRequest request)
	{
		EstraiPagamentiRuoliRequest ris;

		ris = new EstraiPagamentiRuoliRequest();
		
		//set campi
		
	 	ris.setCodiceSocieta(paramCodiceSocieta);
		ris.setCodiceUtente(paramCodiceUtente);
		ris.setCodiceEnte(paramCodiceEnte);			
		ris.setCodiceFiscale(request.getParameter("codFiscale"));

        ris.setAnnoRuolo(request.getParameter("annoRuolo"));
        ris.setNumeroRuolo(request.getParameter("numRuolo"));
		
        ris.setDataRegistrazioneDa(getData(request.getParameter("data_registrazione_da_day"),request.getParameter("data_registrazione_da_month"),request.getParameter("data_registrazione_da_year")));
        ris.setDataRegistrazioneA(getData(request.getParameter("data_registrazione_a_day"),request.getParameter("data_registrazione_a_month"),request.getParameter("data_registrazione_a_year")));
        ris.setDataEffettivaDa(getData(request.getParameter("data_pagamento_da_day"),request.getParameter("data_pagamento_da_month"),request.getParameter("data_pagamento_da_year")));
        ris.setDataEffettivaA(getData(request.getParameter("data_pagamento_a_day"),request.getParameter("data_pagamento_a_month"),request.getParameter("data_pagamento_a_year")));

        ris.setCodiceFiscale(request.getParameter("codFisc"));

		return ris;
		
	}

	

}
