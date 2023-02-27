package org.seda.payer.manager.riversamento.actions;
/*
import java.rmi.RemoteException;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.riversamento.webservice.srv.FaultType;
import com.seda.payer.riversamento.webservice.dati.Paginazione;
import com.seda.payer.riversamento.webservice.dati.RecuperaRiversamentiRequest;
import com.seda.payer.riversamento.webservice.dati.RecuperaRiversamentiResponse;

import com.seda.payer.riversamento.webservice.dati.AvanzamentoStatoRequest;
import com.seda.payer.riversamento.webservice.dati.AvanzamentoStatoResponse;

public class RicercaRiversamentiAction extends BaseRiversamentoAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		salvaStato(request);

		super.service(request);

		HttpSession session = request.getSession();
		
//        aggiornamentoCombo(request, session);				

		String exp="0";
        boolean actExp = false;

		switch(getFiredButton(request)) 
			{
			case TX_BUTTON_NULL: 
				resetDDLSession(request);
				break;
			case TX_BUTTON_RESET:
				resetReqEDDL(request);			
			//	request.setAttribute("DDLType", "21");
				break;
			case TX_BUTTON_CERCA_EXP: 
				if (request.getParameter("ext") != null && request.getParameter("ext").equals("0"))
					exp = "1";
				else
					exp = "0";
				actExp = true;

				request.setAttribute("rowsPerPage", request.getParameter("hRowsPerPage"));
				request.setAttribute("pageNumber", request.getParameter("hPageNumber"));
				request.setAttribute("order", request.getParameter("hOrder"));

			case TX_BUTTON_SET_FLAG_CBI:
			case TX_BUTTON_AVANZA_STATO:
			case TX_BUTTON_CERCA: 
				if (request.getAttribute("ext")==null)
					request.setAttribute("ext","0");

				if (request.getAttribute("ext")=="" || actExp)
					request.setAttribute("ext",exp);
				
				try {
						ricercaRiversamenti(request);
					}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaRiversamentiForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaRiversamentiForm", testoErroreColl, request);					
				}
				catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					e.printStackTrace();
					setFormMessage("ricercaRiversamentiForm", "errore ricerca: " + testoErrore, request);
				}
				break;

			case TX_BUTTON_MEGA_RIV: 
				if (request.getAttribute("ext")==null)
					request.setAttribute("ext","0");

				if (request.getAttribute("ext")=="" || actExp)
					request.setAttribute("ext",exp);
				
				request.setAttribute("rowsPerPage", request.getParameter("hRowsPerPage"));
				request.setAttribute("pageNumber", request.getParameter("hPageNumber"));
				request.setAttribute("order", request.getParameter("hOrder"));
				
				if (((String)request.getAttribute("megaBottoneAlert")).equals("0"))
				{
					try {
						    AvanzamentoStatoResponse out = avanzamentoStato(request);
							ricercaRiversamenti(request);
							setFormMessage("ricercaRiversamentiForm", "Procedura Avanzamento Stato eseguita", request);

//							ricercaRiversamenti(request);

							request.setAttribute("tx_button_mega_riv",null);
							request.setAttribute("tx_button_cerca","");
						}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
						fte.printStackTrace();
						setFormMessage("ricercaRiversamentiForm", "errore: " + decodeMessaggio(fte), request);
					}
					catch (RemoteException af)
					{
						WSCache.logWriter.logError("errore: " + af.getMessage(),af);
						af.printStackTrace();
						setFormMessage("ricercaRiversamentiForm", testoErroreColl, request);					
					}
					catch (Exception e) {
						WSCache.logWriter.logError("errore: " + e.getMessage(),e);
						e.printStackTrace();
						setFormMessage("ricercaRiversamentiForm", "errore ricerca: " + testoErrore, request);
					}
				}
				break;
				
/*
			case TX_BUTTON_RESET:
				resetReqEDDL(request);

				/*
				resetParametri(request);
				request.setAttribute("data_riversamento_da", null);
				request.setAttribute("data_riversamento_a", null);
				request.setAttribute("statoRiversamento", "X");

				resetDDLSession(request);

				loadDDLProvinciaBen(request, session, null, true);
				loadDDLUtente(request, session, null, null, true);
				loadDDLBeneficiario(request, session, null, null, null, true);
*/
				
//				break;
/*
			case TX_BUTTON_NULL:

				//				salvaStato(request);
				
				break;
*/
/*
			case TX_BUTTON_SOCIETA_CHANGED:
				loadDDLProvinciaBen(request, session, codiceSocieta, true);
				loadDDLUtente(request, session, codiceSocieta, null, true);
				loadDDLBeneficiario(request, session, codiceSocieta, null, null, true);
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadDDLProvinciaBen(request, session, codiceSocieta, false);
				loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
				loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, null, true);
				break;

			case TX_BUTTON_UTENTE_CHANGED:
				loadDDLProvinciaBen(request, session, codiceSocieta, false);
				loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
				loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, codiceUtente, true);
				break;
*/
/*				
			case TX_BUTTON_DETAIL_IMP:
				try 
					{
			         preparazioneDatiDettaglio(request);
			         ricercaDettagliRiv(request, "R");
//				     setMessage("Nessun dettaglio disponibile");
					}
				catch (Exception e) 
				   {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					//setErrorMessage(request, e.getMessage());
					setFormMessage("ricercaRiversamentiForm", "errore: " + e.getMessage(), request);

					}
				break;
			case TX_BUTTON_DETAIL_CONTR:
				try 
					{
			         preparazioneDatiDettaglio(request);
			         ricercaDettagliRiv(request, "C");
					}
				catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
//					setErrorMessage(request, e.getMessage());
					setFormMessage("ricercaRiversamentiForm", "errore: " + e.getMessage(), request);
				}
		        
				break;
*//*
			}

		
		super.service(request);
        aggiornamentoCombo(request, session);				

//		request.setAttribute("riv_message", getMessage());
//		request.setAttribute("riv_error_message", getErrorMessage());
		return null;
	}

    public AvanzamentoStatoResponse avanzamentoStato(HttpServletRequest request) throws Exception
    {
		AvanzamentoStatoResponse out;
		AvanzamentoStatoRequest in;
		in = prepareAvanzamentoStato(request);
					
		WSCache.logWriter.logDebug("Pagina interrogazione di test");
		out =  WSCache.riversamentoServer.avanzamentoStato(in, request);
		
		return out;
		
    	
    }	
	
    public void ricercaRiversamenti(HttpServletRequest request) throws Exception
    {
			
			RecuperaRiversamentiResponse out;
			RecuperaRiversamentiRequest in;
			in = prepareRicerca(request);
						
			WSCache.logWriter.logDebug("Pagina interrogazione di test");
			out =  WSCache.riversamentoServer.recuperaRiversamenti(in, request);

			int numeroRivFuturi = out.getNumeroRivFuturi();
			if (numeroRivFuturi> 0) 
				request.setAttribute("megaBottoneAlert", "1");
			else
				request.setAttribute("megaBottoneAlert", "0");
			
			if(out.getPInfo().getNumRows() > 0) 
			{
				request.setAttribute("listaRiversamenti", out.getListXml());

				request.setAttribute("listaRiversamenti.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		
//				request.setAttribute("listaRiversamenti.pageInfo", out.getPInfo());
				request.setAttribute("TotCommissioneGestore", out.getTotCommissioneGestore().toString());
				request.setAttribute("TotCommissioniGateway", out.getTotCommissioniGateway().toString());
				request.setAttribute("TotImportoCittadino", out.getTotImportoCittadino().toString());
				request.setAttribute("TotImportoContribuenti", out.getTotImportoContribuenti().toString());
				request.setAttribute("TotImportoRiversare", out.getTotImportoRiversare().toString());
				request.setAttribute("TotSpeseNotifica", out.getTotSpeseNotifica().toString());
				request.setAttribute("TotImportoRecuperare", out.getTotImportoRecuperare().toString());
				request.setAttribute("NumRighe", Integer.toString(out.getPInfo().getNumRows()));
			}
//			else setMessage(request, Messages.TX_NO_TRANSACTIONS.format());
//			else setMessage(request, "Nessun risultato");
			else
			{
				String messaggio="";
				if (request.getAttribute("messaggioRis")!=null)
					messaggio = request.getAttribute("messaggioRis").toString() + " - ";
				setFormMessage("ricercaRiversamentiForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
			}
		
	
    }

    private void resetReqEDDL(HttpServletRequest request)
    {
		resetParametri(request);
		request.setAttribute("data_riversamento_da", null);
		request.setAttribute("data_riversamento_a", null);
		request.setAttribute("statoRiversamento", "X");
		setProfile(request);
		resetDDLSession(request);
    }
    
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
/*
		switch(DDLType)
		{		    
		    case 0:   //comportamento di default
			case 21:

				if (DDLChanged.equals("codSocieta")) 
					{
						loadDDLProvinciaBen(request, session, codiceSocieta, true);
						loadDDLUtente(request, session, codiceSocieta, null, true);
						loadDDLBeneficiario(request, session, codiceSocieta, null, null, true);
					}
					else if (DDLChanged.equals("codProvincia")) 
					{
						loadDDLProvinciaBen(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
						loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, null, true);
					}
					else if (DDLChanged.equals("codUtente")) 
					{
						loadDDLProvinciaBen(request, session, codiceSocieta, false);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
						loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, codiceUtente, true);
					}
					else 
					{
						loadDDLProvinciaBen(request, session, codiceSocieta,true);
						loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
						loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, codiceUtente, true);
					}
				break;
*//*
		
		
		switch(getFiredButton(request)) 
		{
  		    case TX_BUTTON_SOCIETA_CHANGED:
//				loadDDLProvinciaBen(request, session, codiceSocieta, true);
				loadDDLProvinciaByConv(request, session, codiceSocieta, true);
				loadDDLUtente(request, session, codiceSocieta, null, true);
//				loadDDLBeneficiario(request, session, codiceSocieta, null, null, true);
				loadDDLBeneficiarioByConv(request, session, codiceSocieta, null, null, null, true);
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
//				loadDDLProvinciaBen(request, session, codiceSocieta, false);
				loadDDLProvinciaByConv(request, session, codiceSocieta, false);
				loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
//				loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, null, true);
				loadDDLBeneficiarioByConv(request, session, codiceSocieta, codiceProvincia, null, codiceEnte, true);
				break;

			case TX_BUTTON_UTENTE_CHANGED:
//				loadDDLProvinciaBen(request, session, codiceSocieta, false);
				loadDDLProvinciaByConv(request, session, codiceSocieta, false);
				loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
//				loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, codiceUtente, true);
				loadDDLBeneficiarioByConv(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnte, true);
				break;
			default:
//				loadDDLProvinciaBen(request, session, codiceSocieta,false);
				loadDDLProvinciaByConv(request, session, codiceSocieta,false);
				loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);
//				loadDDLBeneficiario(request, session, codiceSocieta, codiceProvincia, codiceUtente, false);
				loadDDLBeneficiarioByConv(request, session, codiceSocieta, codiceProvincia, codiceUtente, codiceEnte, false);
				
		}		
	}
	
	
	private RecuperaRiversamentiRequest prepareRicerca(HttpServletRequest request)
	{

		PropertiesTree configuration; 
		RecuperaRiversamentiRequest ris;
//		int rowsPerPage;
		
//		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new RecuperaRiversamentiRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		
		//controllo amministratore ente

/*		
		ris.setCodiceSocieta(request.getParameter("codSocieta"));
        ris.setCodiceBelfiore(request.getParameter("codProvincia"));
        ris.setCodiceUtente(request.getParameter("codUtente"));
*//*
		
		ris.setCodiceSocieta(codiceSocieta);
        ris.setCodiceBelfiore(codiceProvincia);
        ris.setCodiceUtente(codiceUtente);
        ris.setEnteBeneficiario(codiceEnte);

        /*
        if (this.getUserProfile()!=null && this.getUserProfile().equals("AMEN") && ==null)
	    {	
	    	ris.setEnteBeneficiario("NESSUNO");
	    	setErrorMessage(request, "L'ente Beneficiario dell'utente non è stato trovato in anagrafica");
	    }
	    else
	    {	
	    	ris.setEnteBeneficiario(request.getParameter("codBeneficiario"));
	    }
        *//*
        
//        ris.setDataRiversamentoDa(request.getParameter("data_riversamento_da")!=null ? (String)request.getParameter("data_riversamento_da"):"");
//        ris.setDataRiversamentoA(request.getParameter("data_riversamento_a")!=null ? request.getParameter("data_riversamento_a"):"");
        ris.setTipoRendicontazione(request.getParameter("tipoRendicontazione"));
        ris.setTipoRiversamento(request.getParameter("strumRiversamento"));
        
//        ris.setStatoRiversamento(request.getParameter("statoRiversamento"));
        String statoRiversamento = (String)request.getParameter("statoRiversamento");
        if (statoRiversamento.equals("T"))
        	statoRiversamento = "";
        ris.setStatoRiversamento(statoRiversamento);
		
        /*
        //verifica stato X
        if (ris.getStatoRiversamento().equals("X"))
        	ris.setStatoRiversamento("");
        
        if (ris.getStatoRiversamento().equals(""))
        	ris.setStatoRiversamento("X");
        *//*
		ris.setDataRiversamentoDa(getData(request.getParameter("data_riversamento_da_day"),request.getParameter("data_riversamento_da_month"),request.getParameter("data_riversamento_da_year")));
		ris.setDataRiversamentoA(getData(request.getParameter("data_riversamento_a_day"),request.getParameter("data_riversamento_a_month"),request.getParameter("data_riversamento_a_year")));

		//sezione per cambio automatico del filtro sullo stato
/*
		String test = ris.getCodiceSocieta()+ris.getCodiceBelfiore()+ris.getCodiceUtente()+ris.getEnteBeneficiario()+ris.getTipoRendicontazione()+
		              ris.getTipoRiversamento()+ris.getStatoRiversamento()+ris.getDataRiversamentoDa()+ris.getDataRiversamentoA();
		
		
		if (test.equals(""))
		{
			ris.setStatoRiversamento("X");
			request.setAttribute("statoRiversamento", "X");
		}
*//*
		return ris;
		
	}

	private AvanzamentoStatoRequest prepareAvanzamentoStato(HttpServletRequest request)
	{

		PropertiesTree configuration; 
		AvanzamentoStatoRequest ris;
//		int rowsPerPage;
		
//		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new AvanzamentoStatoRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		
		//controllo amministratore ente

/*		
		ris.setCodiceSocieta(request.getParameter("codSocieta"));
        ris.setCodiceBelfiore(request.getParameter("codProvincia"));
        ris.setCodiceUtente(request.getParameter("codUtente"));
*//*
		
		ris.setCodiceSocieta(codiceSocieta);
        ris.setCodiceBelfiore(codiceProvincia);
        ris.setCodiceUtente(codiceUtente);
        ris.setEnteBeneficiario(request.getParameter("codBeneficiario"));

        /*
        if (this.getUserProfile()!=null && this.getUserProfile().equals("AMEN") && ==null)
	    {	
	    	ris.setEnteBeneficiario("NESSUNO");
	    	setErrorMessage(request, "L'ente Beneficiario dell'utente non è stato trovato in anagrafica");
	    }
	    else
	    {	
	    	ris.setEnteBeneficiario(request.getParameter("codBeneficiario"));
	    }
        *//*
        
//        ris.setDataRiversamentoDa(request.getParameter("data_riversamento_da")!=null ? (String)request.getParameter("data_riversamento_da"):"");
//        ris.setDataRiversamentoA(request.getParameter("data_riversamento_a")!=null ? request.getParameter("data_riversamento_a"):"");
        ris.setTipoRendicontazione(request.getParameter("tipoRendicontazione"));
        ris.setTipoRiversamento(request.getParameter("strumRiversamento"));

        // fatto per pulizia --> il caso non si verifica mai
//        ris.setStatoRiversamento(request.getParameter("statoRiversamento"));
        String statoRiversamento = (String)request.getParameter("statoRiversamento");
        if (statoRiversamento.equals("T"))
        	statoRiversamento = "";
        ris.setStatoRiversamento(statoRiversamento);

		
        /*
        //verifica stato X
        if (ris.getStatoRiversamento().equals("X"))
        	ris.setStatoRiversamento("");
        
        if (ris.getStatoRiversamento().equals(""))
        	ris.setStatoRiversamento("X");
        *//*
		ris.setDataRiversamentoDa(getData(request.getParameter("data_riversamento_da_day"),request.getParameter("data_riversamento_da_month"),request.getParameter("data_riversamento_da_year")));
		ris.setDataRiversamentoA(getData(request.getParameter("data_riversamento_a_day"),request.getParameter("data_riversamento_a_month"),request.getParameter("data_riversamento_a_year")));

		//sezione per cambio automatico del filtro sullo stato
/*
		String test = ris.getCodiceSocieta()+ris.getCodiceBelfiore()+ris.getCodiceUtente()+ris.getEnteBeneficiario()+ris.getTipoRendicontazione()+
		              ris.getTipoRiversamento()+ris.getStatoRiversamento()+ris.getDataRiversamentoDa()+ris.getDataRiversamentoA();
		
		
		if (test.equals(""))
		{
			ris.setStatoRiversamento("X");
			request.setAttribute("statoRiversamento", "X");
		}
*//*
        
		ris.setFlagTipoRicerca("A");
		ris.setFlagAVStato(3);
		ris.setOperatore((String)request.getAttribute("operatore"));
		ris.setEmailUser(getUserBean().getEmailNotifiche());
		return ris;
		
	}

	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
		/*
		 * Salvo le date che richiedono l'utilizzo di un tipo "Calendar"
		 *//*
		request.setAttribute("data_riversamento_da", getCalendar(request, "data_riversamento_da"));
		request.setAttribute("data_riversamento_a", getCalendar(request, "data_riversamento_a"));
	}

/*	
	private void requestData(HttpServletRequest request, String chiaveS, String chiaveD)
	{
		String data = request.getParameter(chiaveS);
		if (data != null && data.length()>0 && !data.contains("1000-01-01"))
		{
			data = data.substring(0,10);
			request.setAttribute(chiaveD, data.substring(8,10) + "/" + 
					data.substring(5,7) + "/" + data.substring(0,4));
		}
		else
			request.setAttribute(chiaveD, "");

//		java.util.Calendar cal = getCalendarS(request, data);		
//		request.setAttribute("c1d", cal);

	}
*/	
	/*    
    public void preparazioneDatiDettaglio(HttpServletRequest request) 
    {
		requestData(request, "revData", "c1S");	//c1
		requestData(request, "c14", "c14S");
		requestData(request, "c15", "c15S");
		requestData(request, "c16", "c16S");
//		requestData(request, "c17", "c17S");

		String valore = request.getParameter("revTipo");	//c5
        if (valore.equals("R"))
        	request.setAttribute("c5S", "Rivers.");
        else
        	request.setAttribute("c5S", "Rendic.");

		valore = request.getParameter("revRive");	
        if (valore.equals("B"))
        	request.setAttribute("c6S", "Bonifico");
        else
        	request.setAttribute("c6S", "Altro");

        int c = (int)(request.getParameter("c7").charAt(0));
        switch (c)
        {
        case (int)'A': valore = "Aperto";
        		  	   break;
        case (int)'C': valore = "Chiuso";
        			   break;
        case (int)'S': valore = "Sospeso";
        			   break;
        case (int)'E': valore = "Eseguito";
        			   break;
        }
        
        request.setAttribute("c7S", valore);

        valore = request.getParameter("c3") + "/" + request.getParameter("c4");
        if (valore.equals(" /      "))
        	valore = "";
        
        request.setAttribute("c3S", valore);
    		
    }

    public void ricercaDettagliRiv(HttpServletRequest request, String tipoDettaglio) throws Exception
    {
			RecuperaTransazioniRiversateResponse out;
			RecuperaTransazioniRiversateRequest in;
			in = prepareRicercaDettaglio(request, tipoDettaglio);
						
			WSCache.logWriter.logDebug("Pagina dettaglio riversamento");
			out =  WSCache.riversamentoServer.recuperaRiversamenti(in);

			
			if(out.getPInfo().getNumRows() > 0) 
			{
				if (tipoDettaglio.equals("R")) 
				{
					request.setAttribute("listaDettaglioImp", out.getListXml());
					request.setAttribute("listaDettaglioImp.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		
				}
				else
				{
				//	request.setAttribute("listaDettaglioImp", out.getListXml());
				//	request.setAttribute("listaDettaglioImp.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));				
					request.setAttribute("NumTransazioni", out.getNumTransazioni().toString());

//					setMessage("Nessun risultato");				
					setFormMessage("ricercaRiversamentiForm", Messages.NO_DATA_FOUND.format(), request);

				}

				request.setAttribute("TotCommissioneGestore", out.getTotCommissioneGestore().toString());
				request.setAttribute("TotCommissioniGateway", out.getTotCommissioniGateway().toString());
				request.setAttribute("TotImportoCittadino", out.getTotImportoCittadino().toString());
				request.setAttribute("TotImportoContribuenti", out.getTotImportoContribuenti().toString());
				request.setAttribute("TotSpeseNotifica", out.getTotSpeseNotifica().toString());

//				request.setAttribute("TotImportoRiversare", out.getTotImportoRiversare().toString());
//				request.setAttribute("TotImportoRecuperare", out.getTotImportoRecuperare().toString());
//				request.setAttribute("NumRighe", Integer.toString(out.getPInfo().getNumRows()));
			}
//			else setMessage(request, Messages.TX_NO_TRANSACTIONS.format());
//			else setMessage("Nessun risultato");
			setFormMessage("ricercaRiversamentiForm", Messages.NO_DATA_FOUND.format(), request);
			
		
	
    }
 */   

	/*	
	private RecuperaTransazioniRiversateRequest prepareRicercaDettaglio(HttpServletRequest request, String tipoDettaglio)
	{

		PropertiesTree configuration; 
		RecuperaTransazioniRiversateRequest ris;
		int rowsPerPage;
		
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		
		rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

        if (request.getParameter("rowsPerPage")!=null)	
        {
        	rowsPerPage = Integer.parseInt(request.getParameter("rowsPerPage"));
        }
         
		int pageNumber = (request.getParameter("pageNumber") == null) || (request.getParameter("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));

		String order = (request.getParameter("order") == null) ? "" : request.getParameter("order");

		ris = new RecuperaTransazioniRiversateRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
    	// preparazione 
	    //revSoc
		//revUte
	    //revData
	    //revBen
	    //revTipo
	    //revRive
	
		ris.setCodiceSocieta(request.getParameter("revSoc"));
        ris.setCodiceUtente(request.getParameter("revUte"));
        ris.setDataRiversamento(request.getParameter("revData").substring(0,10));
        ris.setEnteBeneficiario(request.getParameter("revBen"));
        ris.setTipoRendicontazione(request.getParameter("revTipo"));
        ris.setTipoRiversamento(request.getParameter("revRive"));
		ris.setTipoDettaglio(tipoDettaglio);
		
		return ris;
		
	}
*/	
//}
