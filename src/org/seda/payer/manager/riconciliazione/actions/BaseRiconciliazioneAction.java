package org.seda.payer.manager.riconciliazione.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiApertiRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiApertiResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiCBIRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiCBIResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniNonQuadrateRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniNonQuadrateResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class BaseRiconciliazioneAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;

	protected String codiceSocieta="";
	protected String codiceProvincia="";
	protected String codiceUtente="";
	
	public void setProfile(HttpServletRequest request)  
	{
		super.setProfile(request);
				
		codiceProvincia = (String)request.getAttribute("tx_provincia") != null? (String)request.getAttribute("tx_provincia"): "";
		
		/*
		
		String tx_Utente = (String) request.getAttribute("tx_utente");
		
		String tx_societa = "";
		String tx_utente = "";
		
		
		boolean isTxtUtenteSelected=false;
		
		if (tx_Utente != null && tx_Utente.trim().length() > 0 && tx_Utente.length() == 10) {
			isTxtUtenteSelected=true;
			tx_societa = tx_Utente.substring(0, 5);
			tx_utente = tx_Utente.substring(5);
		}
		
		switch (getProfiloUtente(userProfile)) {
		case AMEN:
			request.setAttribute("societaDdlDisabled", true);
			request.setAttribute("provinciaDdlDisabled", true);
			request.setAttribute("utenteDdlDisabled", true);
			
			paramCodiceSocieta = userBean.getCodiceSocieta();
			request.setAttribute("tx_societa", paramCodiceSocieta);
			
			paramCodiceEnte = userBean.getChiaveEnteConsorzio();
			

			paramCodiceUtente = userBean.getCodiceUtente();
			request.setAttribute("tx_utente", paramCodiceSocieta+paramCodiceUtente);

			
			break;
		case AMUT:
			request.setAttribute("societaDdlDisabled", true);
			request.setAttribute("provinciaDdlDisabled", true);
			request.setAttribute("utenteDdlDisabled", true);
			
			paramCodiceSocieta = userBean.getCodiceSocieta();
			paramCodiceUtente = userBean.getCodiceUtente();
			request.setAttribute("tx_societa", paramCodiceSocieta);
			request.setAttribute("tx_utente", paramCodiceSocieta+paramCodiceUtente);
			
			break;
		case AMSO:
			request.setAttribute("societaDdlDisabled", true);
			
			paramCodiceSocieta = userBean.getCodiceSocieta();
			request.setAttribute("tx_societa", paramCodiceSocieta);
			
			paramCodiceUtente = tx_utente;
			
			break;
		case AMMI:
			if (request.getAttribute("tx_societa")!=null && ((String)request.getAttribute("tx_societa")).trim().length()>0)
				paramCodiceSocieta = (String)request.getAttribute("tx_societa");
			else
				paramCodiceSocieta = isTxtUtenteSelected ? tx_societa : "";
			
			
			paramCodiceUtente = tx_utente;
			
			break;
		}
		*/
		
		/*
		codiceSocieta = (String)request.getAttribute("tx_societa") != null? (String)request.getAttribute("tx_societa"): "";
		codiceProvincia = (String)request.getAttribute("tx_provincia") != null? (String)request.getAttribute("tx_provincia"): "";

		String codiceSocietaUtente=(String)request.getAttribute("tx_utente");
		if (codiceSocietaUtente!= null && codiceSocietaUtente.trim().length() > 0)
		{
			codiceSocieta=codiceSocietaUtente.substring(0,5);
			codiceUtente=codiceSocietaUtente.substring(5);
		}

		
		
		ddlSocietaDisabled = false;
    	ddlProvinciaDisabled = false;
    	ddlUtenteEnteDisabled = false;

	    if (this.getUserProfile()!=null)	//condizione sempre vera
	    {
	    	
	    	if (this.getUserProfile().equals("AMEN")||this.getUserProfile().equals("AMUT")||this.getUserProfile().equals("AMSO"))		    	
			    {
			    	codiceSocieta = getUserBean().getCodiceSocieta();
					ddlSocietaDisabled = true;
			    }

	    	if (this.getUserProfile().equals("AMUT")||this.getUserProfile().equals("AMEN"))		    	
			    {
			    	codiceUtente = getUserBean().getCodiceUtente();	
			    	ddlUtenteEnteDisabled = true;
			    }

	    	if (this.getUserProfile().equals("AMEN"))		    	
	    		{
			    	 codiceProvincia="";
				     ddlProvinciaDisabled = true;

	    		}
		
		}
		
    	request.setAttribute("tx_societa", codiceSocieta);
    	request.setAttribute("tx_provincia", codiceProvincia);
    	request.setAttribute("tx_utente", codiceSocieta+codiceUtente);

		request.setAttribute("societaDdlDisabled", ddlSocietaDisabled);
    	request.setAttribute("provinciaDdlDisabled", ddlProvinciaDisabled);
    	request.setAttribute("utenteDdlDisabled", ddlUtenteEnteDisabled);
        */
	}

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		 
		HttpSession session = request.getSession();
		loadDDLStatic(request, session);

	    setProfile(request);
	    
		/*** GESTIONE DDL CON JAVASCRIPT ***/

		/*
		String DDLChanged = request.getParameter("DDLChanged");
		if (DDLChanged != null && DDLChanged.length()>0) {
			if (DDLChanged.equals("societa")) {
				loadDDLProvincia(request, session, codiceSocieta, true);
				loadDDLUtente(request, session, codiceSocieta, null, true);
			}
			else if (DDLChanged.equals("provincia")) {
				loadDDLProvincia(request, session, codiceSocieta, false);
				loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
			}
		}
		else {
			loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
			loadDDLProvincia(request, session, codiceSocieta,true);
		}
		*/
		return null;
	}


	protected RecuperaTransazioniNonQuadrateResponse getTransazioniNonQuadrate(HttpServletRequest request, String rifMovimento) throws FaultType,
			RemoteException {
				int chiaveMovimento = Integer.parseInt(rifMovimento);

/*				
				int rowsPerPage = (request.getParameter(Field.ROWS_PER_PAGE.format()) == null) ? 5: Integer.parseInt(request.getParameter(Field.ROWS_PER_PAGE.format()));
				int pageNumber = (request.getParameter(Field.PAGE_NUMBER.format()) == null) || (request.getParameter(Field.PAGE_NUMBER.format()).equals("")) ? 1 : Integer.parseInt(request.getParameter(Field.PAGE_NUMBER.format()));
				String order = (request.getParameter(Field.ORDER_BY.format()) == null) ? "" : request.getParameter(Field.ORDER_BY.format());
*/
				
				PropertiesTree configuration; 
				configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

				int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//		      if (request.getParameter("rowsPerPage")!=null)	
			      if (request.getAttribute("rowsPerPage")!=null&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
			      {
			      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
			      }
		       
				int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

				String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

				RecuperaTransazioniNonQuadrateRequest recuperaTransazioniNonQuadrateRequest = new RecuperaTransazioniNonQuadrateRequest();
				recuperaTransazioniNonQuadrateRequest.setOrder(order);
				recuperaTransazioniNonQuadrateRequest.setPageNumber(pageNumber);
				recuperaTransazioniNonQuadrateRequest.setRowsPerPage(rowsPerPage);
				recuperaTransazioniNonQuadrateRequest.setCodiceSocieta(getParamCodiceSocieta());
				recuperaTransazioniNonQuadrateRequest.setCodiceProvincia(codiceProvincia);
				recuperaTransazioniNonQuadrateRequest.setCodiceUtente(getParamCodiceUtente());
				recuperaTransazioniNonQuadrateRequest.setCodiceTransazione(request.getParameter(Field.CHIAVE_TRANSAZIONE.format()));
				recuperaTransazioniNonQuadrateRequest.setChiaveGateway(request.getParameter(Field.TX_TIPO_CARTA.format()));
				recuperaTransazioniNonQuadrateRequest.setCanalePagamento(request.getParameter(Field.TX_CANALE_PAGAMENTO.format()));
				recuperaTransazioniNonQuadrateRequest.setStrumento(request.getParameter(Field.TX_STRUMENTO.format()));
				recuperaTransazioniNonQuadrateRequest.setCodiceABI(request.getParameter(Field.CODICE_ABI.format()));
				recuperaTransazioniNonQuadrateRequest.setCodiceCAB(request.getParameter(Field.CODICE_CAB.format()));
				recuperaTransazioniNonQuadrateRequest.setCodiceSIA(request.getParameter(Field.CODICE_SIA.format()));
				recuperaTransazioniNonQuadrateRequest.setContoCorrente(request.getParameter(Field.CCB.format()));
				recuperaTransazioniNonQuadrateRequest.setCRO(request.getParameter(Field.CRO.format()));
				recuperaTransazioniNonQuadrateRequest.setNomeSupporto(request.getParameter(Field.NOME_SUPPORTO.format()));
				recuperaTransazioniNonQuadrateRequest.setDataPagamentoDa(getData(request.getParameter(Field.DATA_PAGAMENTO_DA_DAY.format()),request.getParameter(Field.DATA_PAGAMENTO_DA_MONTH.format()),request.getParameter(Field.DATA_PAGAMENTO_DA_YEAR.format())));
				recuperaTransazioniNonQuadrateRequest.setDataPagamentoA(getData(request.getParameter(Field.DATA_PAGAMENTO_A_DAY.format()),request.getParameter(Field.DATA_PAGAMENTO_A_MONTH.format()),request.getParameter(Field.DATA_PAGAMENTO_A_YEAR.format())));
				recuperaTransazioniNonQuadrateRequest.setDataTransazioneDa(getData(request.getParameter(Field.DATA_CREAZIONE_DA_DAY.format()),request.getParameter(Field.DATA_CREAZIONE_DA_MONTH.format()),request.getParameter(Field.DATA_CREAZIONE_DA_YEAR.format())));
				recuperaTransazioniNonQuadrateRequest.setDataTransazioneA(getData(request.getParameter(Field.DATA_CREAZIONE_A_DAY.format()),request.getParameter(Field.DATA_CREAZIONE_A_MONTH.format()),request.getParameter(Field.DATA_CREAZIONE_A_YEAR.format())));
				recuperaTransazioniNonQuadrateRequest.setChiaveMovimento(chiaveMovimento);
				return WSCache.commonsServer.recuperaTransazioniNonQuadrate(recuperaTransazioniNonQuadrateRequest, request);
		}

	protected RecuperaMovimentiCBIResponse getListaMovimenti(HttpServletRequest request) throws FaultType, RemoteException {

/*		
		int rowsPerPage = (request.getParameter(Field.ROWS_PER_PAGE.format()) == null) ? 5: Integer.parseInt(request.getParameter(Field.ROWS_PER_PAGE.format()));
		int pageNumber = (request.getParameter(Field.PAGE_NUMBER.format()) == null) || (request.getParameter(Field.PAGE_NUMBER.format()).equals("")) ? 1 : Integer.parseInt(request.getParameter(Field.PAGE_NUMBER.format()));
		String order = (request.getParameter(Field.ORDER_BY.format()) == null) ? "" : request.getParameter(Field.ORDER_BY.format());
*/
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();
		
		
		RecuperaMovimentiCBIRequest recuperaMovimentiRequest = new RecuperaMovimentiCBIRequest();
		recuperaMovimentiRequest.setOrder(order);
		recuperaMovimentiRequest.setPageNumber(pageNumber);
		recuperaMovimentiRequest.setRowsPerPage(rowsPerPage);
		String chiaveQuad = (request.getParameter(Field.CHIAVE_MOVIMENTO.format()) == null) ? "0" : request.getParameter(Field.CHIAVE_MOVIMENTO.format());
		if (chiaveQuad.equals("")) chiaveQuad = "0"; 
		recuperaMovimentiRequest.setChiaveMovimento(Integer.parseInt(chiaveQuad));
		recuperaMovimentiRequest.setCodiceSocieta((getParamCodiceSocieta() == null) ? "" : getParamCodiceSocieta());
		
		recuperaMovimentiRequest.setCodiceProvincia((codiceProvincia == null) ? "" : codiceProvincia);
		
		recuperaMovimentiRequest.setCodiceUtente((getParamCodiceUtente() == null) ? "" : getParamCodiceUtente());
		
		recuperaMovimentiRequest.setMostraMovimenti((request.getParameter(Field.MOSTRA_MOVIMENTI.format()) == null) ? "" : request.getParameter(Field.MOSTRA_MOVIMENTI.format()));

		recuperaMovimentiRequest.setTipoCarta((request.getParameter(Field.TX_TIPO_CARTA.format()) == null) ? "" : request.getParameter(Field.TX_TIPO_CARTA.format()));

		recuperaMovimentiRequest.setCodiceABI((request.getParameter(Field.CODICE_ABI.format()) == null) ? "" : request.getParameter(Field.CODICE_ABI.format()));

		recuperaMovimentiRequest.setCodiceCAB((request.getParameter(Field.CODICE_CAB.format()) == null) ? "" : request.getParameter(Field.CODICE_CAB.format()));
		

		recuperaMovimentiRequest.setCodiceSIA((request.getParameter(Field.CODICE_SIA.format()) == null) ? "" : request.getParameter(Field.CODICE_SIA.format()));
		
		recuperaMovimentiRequest.setCRO((request.getParameter(Field.CRO.format()) == null) ? "" : request.getParameter(Field.CRO.format()));

		recuperaMovimentiRequest.setCCB((request.getParameter(Field.CCB.format()) == null) ? "" : request.getParameter(Field.CCB.format()));

		recuperaMovimentiRequest.setNomeSupporto((request.getParameter(Field.NOME_SUPPORTO.format()) == null) ? "" : request.getParameter(Field.NOME_SUPPORTO.format()));

		String importo_mov_da = (request.getParameter(Field.IMPORTO_MOVIMENTO_DA.format()) == null) ? "" : request.getParameter(Field.IMPORTO_MOVIMENTO_DA.format());
		
		String importo_mov_a = (request.getParameter(Field.IMPORTO_MOVIMENTO_A.format()) == null) ? "" : request.getParameter(Field.IMPORTO_MOVIMENTO_A.format());

		importo_mov_da = importo_mov_da.replaceAll("\\.", "");
		importo_mov_da = importo_mov_da.replaceAll(",", ".");
		importo_mov_a = importo_mov_a.replaceAll("\\.", "");
		importo_mov_a = importo_mov_a.replaceAll(",", ".");
		recuperaMovimentiRequest.setImportoA(importo_mov_a);
		recuperaMovimentiRequest.setImportoDa(importo_mov_da);
		
	

		recuperaMovimentiRequest.setDataCreazioneDa(getData((request.getParameter(Field.DATA_CREAZIONE_DA_DAY.format()) == null) ? "" : request.getParameter(Field.DATA_CREAZIONE_DA_DAY.format()),
															(request.getParameter(Field.DATA_CREAZIONE_DA_MONTH.format()) == null) ? "" : request.getParameter(Field.DATA_CREAZIONE_DA_MONTH.format()),
															(request.getParameter(Field.DATA_CREAZIONE_DA_YEAR.format()) == null) ? "" : request.getParameter(Field.DATA_CREAZIONE_DA_YEAR.format())));

		recuperaMovimentiRequest.setDataCreazioneA(getData((request.getParameter(Field.DATA_CREAZIONE_A_DAY.format()) == null) ? "" : request.getParameter(Field.DATA_CREAZIONE_A_DAY.format()),
															(request.getParameter(Field.DATA_CREAZIONE_A_MONTH.format()) == null) ? "" : request.getParameter(Field.DATA_CREAZIONE_A_MONTH.format()),
															(request.getParameter(Field.DATA_CREAZIONE_A_YEAR.format()) == null) ? "" : request.getParameter(Field.DATA_CREAZIONE_A_YEAR.format())));

		recuperaMovimentiRequest.setDataValutaDa(getData((request.getParameter(Field.DATA_VALUTA_DA_DAY.format()) == null) ? "" : request.getParameter(Field.DATA_VALUTA_DA_DAY.format()),
														 (request.getParameter(Field.DATA_VALUTA_DA_MONTH.format()) == null) ? "" : request.getParameter(Field.DATA_VALUTA_DA_MONTH.format()),
														 (request.getParameter(Field.DATA_VALUTA_DA_YEAR.format()) == null) ? "" : request.getParameter(Field.DATA_VALUTA_DA_YEAR.format())));

		recuperaMovimentiRequest.setDataValutaA(getData((request.getParameter(Field.DATA_VALUTA_A_DAY.format()) == null) ? "" : request.getParameter(Field.DATA_VALUTA_A_DAY.format()),
														 (request.getParameter(Field.DATA_VALUTA_A_MONTH.format()) == null) ? "" : request.getParameter(Field.DATA_VALUTA_A_MONTH.format()),
														 (request.getParameter(Field.DATA_VALUTA_A_YEAR.format()) == null) ? "" : request.getParameter(Field.DATA_VALUTA_A_YEAR.format())));

		recuperaMovimentiRequest.setDataContabileDa(getData((request.getParameter(Field.DATA_CONTABILE_DA_DAY.format()) == null) ? "" : request.getParameter(Field.DATA_CONTABILE_DA_DAY.format()),
															 (request.getParameter(Field.DATA_CONTABILE_DA_MONTH.format()) == null) ? "" : request.getParameter(Field.DATA_CONTABILE_DA_MONTH.format()),
															 (request.getParameter(Field.DATA_CONTABILE_DA_YEAR.format()) == null) ? "" : request.getParameter(Field.DATA_CONTABILE_DA_YEAR.format())));

		recuperaMovimentiRequest.setDataContabileDa(getData((request.getParameter(Field.DATA_CONTABILE_A_DAY.format()) == null) ? "" : request.getParameter(Field.DATA_CONTABILE_DA_DAY.format()),
															 (request.getParameter(Field.DATA_CONTABILE_A_MONTH.format()) == null) ? "" : request.getParameter(Field.DATA_CONTABILE_A_MONTH.format()),
															 (request.getParameter(Field.DATA_CONTABILE_A_YEAR.format()) == null) ? "" : request.getParameter(Field.DATA_CONTABILE_A_YEAR.format())));

		
		return WSCache.commonsServer.recuperaMovimentiCBI(recuperaMovimentiRequest, request);
	}

	protected PageInfo getPageInfo(com.seda.payer.pgec.webservice.commons.dati.PageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPages());
		return pageInfo;
	}

	protected PageInfo getMovimentiPageInfo(com.seda.payer.pgec.webservice.commons.dati.PageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPages());
		return pageInfo;
	}

	protected void tx_SalvaStato(HttpServletRequest request) {
		super.tx_SalvaStato(request);
		request.setAttribute(Field.DATA_PAGAMENTO_DA.format(), getCalendar(request, Field.DATA_PAGAMENTO_DA.format()));
		request.setAttribute(Field.DATA_PAGAMENTO_A.format(), getCalendar(request, Field.DATA_PAGAMENTO_A.format()));
		request.setAttribute(Field.DATA_CREAZIONE_DA.format(), getCalendar(request, Field.DATA_CREAZIONE_DA.format()));
		request.setAttribute(Field.DATA_CREAZIONE_A.format(), getCalendar(request, Field.DATA_CREAZIONE_A.format()));
	}
	
	protected RecuperaMovimentiApertiResponse getListaMovimentiAperti(HttpServletRequest request) throws FaultType, RemoteException {
		RecuperaMovimentiApertiRequest in = new RecuperaMovimentiApertiRequest();
		return WSCache.commonsServer.recuperaMovimentiAperti(in, request);	
	}

	protected void resetDDLSession(HttpServletRequest request)
	{
		HttpSession sessione = request.getSession();

		sessione.setAttribute("listaProvince", null);
		sessione.setAttribute("listaUtenti", null);
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
				request.setAttribute("data_pagamento_da", null);
				request.setAttribute("data_pagamento_a", null);
				request.setAttribute("data_creazione_da", null);
				request.setAttribute("data_creazione_a", null);
				request.setAttribute("data_valuta_da", null);
				request.setAttribute("data_valuta_a", null);
				request.setAttribute("data_contabile_da", null);
				request.setAttribute("data_contabile_a", null);
				
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


}
