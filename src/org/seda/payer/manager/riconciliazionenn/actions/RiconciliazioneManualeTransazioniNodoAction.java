package org.seda.payer.manager.riconciliazionenn.actions;

//import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;

import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
//import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
//import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiApertiRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiApertiNodoResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiApertiResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioniNonQuadrateResponse;
import com.seda.payer.pgec.webservice.commons.dati.RiepilogoTransazioniNonQuadrate;
//import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class RiconciliazioneManualeTransazioniNodoAction extends BaseRiconciliazioneNodoAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		
		replyAttributes(false, request,"pageNumber","rowsPerPage","order","validator.message");
		
		tx_SalvaStato(request);

		super.service(request);
		super.aggiornamentoCombo(request, session);	
	
		String exp="0";
        boolean actExp = false;
		switch(getFiredButton(request)) {
			case TX_BUTTON_CERCA_EXP: 

				if (request.getParameter("ext") != null && request.getParameter("ext").equals("0"))
					exp = "1";
				else
					exp = "0";
				actExp = true;

				request.setAttribute("rowsPerPage", request.getParameter("hRowsPerPage"));
				request.setAttribute("pageNumber", request.getParameter("hPageNumber"));
				request.setAttribute("order", request.getParameter("hOrder"));
			
			case TX_BUTTON_CERCA: 
				// gestione compatibilita' richiamo tramite eventuale hyperlink				
				if (request.getAttribute("ext")==null)
					request.setAttribute("ext","0");
	
				if (request.getAttribute("ext")=="" || actExp)
					request.setAttribute("ext",exp);
				try {
					String chiaveMovimento = (request.getParameter(Field.CHIAVE_MOVIMENTO.format())==null) ? "0" : request.getParameter(Field.CHIAVE_MOVIMENTO.format());
					if (chiaveMovimento.equals(""))
						chiaveMovimento = "0";
					RecuperaTransazioniNonQuadrateResponse listaTransazioni = getTransazioniNonQuadrate(request,chiaveMovimento);
					PageInfo pageInfo = getPageInfo(listaTransazioni.getPageInfo());
					if(pageInfo.getNumRows() > 0) {
						String lista = listaTransazioni.getTransazioniNonQuadrateXml();					
						request.setAttribute(Field.LISTA_TRANSAZIONI_NQ.format(), lista);
						request.setAttribute(Field.LISTA_TRANSAZIONI_NQ_PAGEINFO.format(), pageInfo);
						RiepilogoTransazioniNonQuadrate riepilogo = listaTransazioni.getRiepilogoTransazioniNonQuadrate();
						request.setAttribute(Field.RIEPILOGO_TRANSAZIONI_NQ.format(), riepilogo);
					}
					else {
						setMessage(Messages.TX_NO_TRANSACTIONS.format());
					}
				} 
				catch (Exception e) {
					e.printStackTrace();
					setErrorMessage(e.getMessage());
				}
				
				break;
			case TX_BUTTON_MOVIMENTO_CHANGED:
			case TX_BUTTON_MOVIMENTO_CHANGED_NO_JS:
				try {
					String chiaveMovimento = request.getParameter(Field.CHIAVE_MOVIMENTO.format());
					RecuperaTransazioniNonQuadrateResponse listaTransazioni = getTransazioniNonQuadrate(request, chiaveMovimento);
					PageInfo pageInfo = getPageInfo(listaTransazioni.getPageInfo());
//					if(pageInfo.getNumRows() > 0) {
						String lista = listaTransazioni.getTransazioniNonQuadrateXml();					
						request.setAttribute(Field.LISTA_TRANSAZIONI_NQ.format(), lista);
						request.setAttribute(Field.LISTA_TRANSAZIONI_NQ_PAGEINFO.format(), pageInfo);
						RiepilogoTransazioniNonQuadrate riepilogo = listaTransazioni.getRiepilogoTransazioniNonQuadrate();
						request.setAttribute(Field.RIEPILOGO_TRANSAZIONI_NQ.format(), riepilogo);
/*
				}
					else {
						setMessage(Messages.TX_NO_TRANSACTIONS.format());
						//session.removeAttribute(Field.LISTA_TRANSAZIONI_NQ.format());
						//session.removeAttribute(Field.LISTA_TRANSAZIONI_NQ_PAGEINFO.format());
						//session.removeAttribute(Field.RIEPILOGO_TRANSAZIONI_NQ.format());
					}
*/
//					request.setAttribute(Field.LISTA_MOVIMENTI_APERTI.format(), session.getAttribute(Field.LISTA_MOVIMENTI_APERTI.format()));
				} 
				catch (Exception e) {
					e.printStackTrace();
					setErrorMessage(e.getMessage());
				}
				break;
		}

		
		try {
			RecuperaMovimentiApertiNodoResponse movimentiAperti = getListaMovimentiAperti(request);
			request.setAttribute(Field.LISTA_MOVIMENTI_APERTI.format(), movimentiAperti.getMovimentiCBIXml());
		} 
		catch (Exception e) {
			setErrorMessage(e.getMessage());
			return null;
		}
		

		return null;
	}

	

	protected void tx_SalvaStato(HttpServletRequest request) 
	{

		
		super.tx_SalvaStato(request);

/*		
		request.setAttribute(Field.DATA_PAGAMENTO_DA.format(), getCalendar(request, Field.DATA_PAGAMENTO_DA.format()));
		request.setAttribute(Field.DATA_PAGAMENTO_A.format(), getCalendar(request, Field.DATA_PAGAMENTO_A.format()));
		request.setAttribute(Field.DATA_CREAZIONE_DA.format(), getCalendar(request, Field.DATA_CREAZIONE_DA.format()));
		request.setAttribute(Field.DATA_CREAZIONE_A.format(), getCalendar(request, Field.DATA_CREAZIONE_A.format()));
*/

		if (request.getParameter("data_pagamento_daS")!=null)
			request.setAttribute("data_pagamento_da", parseDate(request.getParameter("data_pagamento_daS"), "yyyy-MM-dd hh:mm:ss"));
	    if (request.getParameter("data_pagamento_aS")!=null)
			request.setAttribute("data_pagamento_a", parseDate(request.getParameter("data_pagamento_a"), "yyyy-MM-dd hh:mm:ss"));
		if (request.getParameter("data_creazioneS")!=null)
		{ 
			Calendar dataC = parseDate(request.getParameter("data_creazione"), "yyyy-MM-dd hh:mm:ss");
			request.setAttribute("data_creazione_da", dataC);
			request.setAttribute("data_creazione_a", dataC);
		}
		
	    resetPartialDate(request, "data_pagamento_da");
		resetPartialDate(request, "data_pagamento_a");
		resetPartialDate(request, "data_creazione_da");
		resetPartialDate(request, "data_creazione_a");
		
	}

	private void resetPartialDate(HttpServletRequest request, String radice)
	{
		if ((request.getAttribute(radice + "_day")!=null &&((String)request.getAttribute(radice + "_day")).equals(""))||(request.getAttribute(radice + "_month")!=null && ((String)request.getAttribute(radice + "_month")).equals(""))|| (request.getAttribute(radice + "_year")!=null && ((String)request.getAttribute(radice + "_year")).equals("")))
		{
			request.setAttribute(radice + "_day", "");
			request.setAttribute(radice + "_month", "");
			request.setAttribute(radice + "_year", "");
		}
	}
}
