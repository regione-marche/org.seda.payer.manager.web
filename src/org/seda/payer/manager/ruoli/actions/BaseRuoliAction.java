package org.seda.payer.manager.ruoli.actions;

//import java.rmi.RemoteException;
import java.rmi.RemoteException;
//import java.text.ParseException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
//import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;
//import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.util.MAFAttributes;


import com.seda.payer.bancadati.webservice.dati.RecuperaDDLImpServizioRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLImpServizioResponse;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLUffImpositoreRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLUffImpositoreResponse;
//import com.seda.payer.bancadati.webservice.dati.RecuperaDataUltAggiornamentoRequest;
//import com.seda.payer.bancadati.webservice.dati.RecuperaDataUltAggiornamentoResponse;
import com.seda.payer.bancadati.webservice.dati.RecuperaRuoliDataUltAggRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaRuoliDataUltAggResponse;
/*
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
*/
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class BaseRuoliAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;

	//la classe BaseManagerAction non è ben strutturata --> viene ereditata, ma in parte bypassata da questa classe
	public static final String testoErrore = "errore generico";
	public static final String testoErroreColl = "errore: errore di collegamento al servizio web";
	
	public static enum PAGINE {PAGRUOLI, PAGANAGRAFICHE, PAGPARTITE, ALTRO};

    protected String siglaProvincia;
	protected String paramCodiceTipologiaServizio;
	protected Calendar dataUltimoAgg;
    protected PAGINE chiamante;
	protected int DDLType;


//	protected String DDLChanged;
	
//	protected boolean ddlEnteBeneficiarioDisabled;

	
	public void setProfile(HttpServletRequest request)  
	{
		super.setProfile(request);

		//mappatura variabili rendicontazione in riversamento
		// SET COMBO --> ci potremmo affidare alla superclasse che imposta le combo per rendicontazione

		/*
			//AMMI TUTTO LIBERO

			//AMSO
			//  SOCIETA FISSA
			//  UTENTE LIBERO
			//  ENTE LIBERO

			//AMEN
			//  SOCIETA FISSA
			//  UTENTE FISSO
			//	    multiutente--> ENTE VARIABILE 
			//  	no multiutente --> ENTE FISSO

			//AMUT 
			//  SOCIETA FISSA
			//  UTENTE FISSO
			//  ENTE LIBERO
			
			 
    	 */

		
    	// prelievo parametri dalla request o impostazione di default
		//codiceUtente = isNull(request.getAttribute(Field.TX_UTENTE.format()));

/*		
		ddlSocietaDisabled = false;
    	ddlUtenteDisabled = false;
    	ddlUtenteEnteDisabled = false;

	    if (this.getUserProfile()!=null)	//condizione sempre vera
	    {
	    	
	    	if (this.getUserProfile().equals("AMEN")||this.getUserProfile().equals("AMUT")||this.getUserProfile().equals("AMSO"))		    	
			    {
			    	paramCodiceSocieta = getUserBean().getCodiceSocieta();
					ddlSocietaDisabled = true;
			    }

	    	if (this.getUserProfile().equals("AMUT")||this.getUserProfile().equals("AMEN"))		    	
			    {
			    	paramCodiceUtente = getUserBean().getCodiceUtente();
			    	this.ddlUtenteDisabled = true;
			    }

	    	if (this.getUserProfile().equals("AMEN"))		    	
	    		{
	    			ddlUtenteEnteDisabled = true;
	    		}
		
		}
	    
    	request.setAttribute(Field.TX_SOCIETA.format(), paramCodiceSocieta);    	
    	request.setAttribute(Field.TX_UTENTE.format(), paramCodiceUtente);
    	request.setAttribute(Field.TX_UTENTE_ENTE.format(), paramCodiceUtente);

		request.setAttribute("ddlSocietaDisabled", ddlSocietaDisabled);
    	request.setAttribute("ddlUtenteDisabled", ddlUtenteDisabled);
    	request.setAttribute("ddlUtenteEnteDisabled", ddlUtenteEnteDisabled);
*/

		if(this.getUserProfile().equals("AMEN")){
			loadDataUltimoAggiornamento(request);		
		}		
		paramCodiceTipologiaServizio = (String)request.getAttribute("tx_tipologia_servizio");
		
		if (getUserBean()!=null)
			request.setAttribute("operatore", getUserBean().getUserName());
		else
			request.setAttribute("operatore","Sconosciuto");
		
		
	}

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();

		loadDDLStatic(request, session);

		decodeChiamante(request);
		
		loadDettaglio(request);
	
 //	tutto in action
/*   
		if(session.getAttribute("listaImpostaServizio")== null){		
			loadListaImpostaServizio(request, session,paramCodiceSocieta,"",false);
			loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);
			loadTipologiaServizioXml_DDL(request,session, null, false);
		}
*/	
		setProfile(request);	
		
		if (request.getAttribute("DDLType")==null||request.getAttribute("DDLType").equals("")) 
			DDLType = 0;
		else
			DDLType = Integer.parseInt((String)request.getAttribute("DDLType"));
		
		return null;
	}

	
	protected PageInfo getPageInfo(com.seda.payer.bancadati.webservice.dati.PageInfo rpi, int rowsPerPages) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rowsPerPages);
		return pageInfo;
	}
	
	//graz
	protected void loadListaImpostaServizio(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String tipoServizio,
			boolean forceReload) {
		RecuperaDDLImpServizioRequest in = null;
		RecuperaDDLImpServizioResponse res = null;
		String xml = null;
		String lista = "listaImpostaServizio";
		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new RecuperaDDLImpServizioRequest();
				in.setCodiceSocieta(codiceSocieta);
				in.setTipologiaServizio(tipoServizio);
				res =  WSCache.entrateBDServer.recuperaDDLImpostaServizio(in, request);
				com.seda.payer.bancadati.webservice.dati.ResponseType response = res.getRisultato();
				if (response.getRetMessage().equals("OK")) {
					xml = res.getListXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento delle Imposte Servizio: "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Imposte Servizio: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Imposte Servizio: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}

	//graz
	protected void loadListaUffImpositore(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String codiceEnte, String codiceUtente,
			boolean forceReload) {
		RecuperaDDLUffImpositoreRequest in = null;
		RecuperaDDLUffImpositoreResponse res = null;
		String xml = null;
		String lista = "listaUfficioImpositore";
		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new RecuperaDDLUffImpositoreRequest();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceEnte(codiceEnte);
				in.setCodiceUtente(codiceUtente);
				res =  WSCache.entrateBDServer.recuperaDDLUffImpositore(in, request);
				com.seda.payer.bancadati.webservice.dati.ResponseType response = res.getRisultato();
				if (response.getRetMessage().equals("OK")) {
					xml = res.getListXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento degli Uffici Impositori: "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamentodegli Uffici Impositori: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento degli Uffici Impositori: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}
	protected void loadDataUltimoAggiornamento(HttpServletRequest request){
	RecuperaRuoliDataUltAggRequest in = new RecuperaRuoliDataUltAggRequest();
	RecuperaRuoliDataUltAggResponse out = null;
	Calendar dataUltimoAgg = null;
//	java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try {
			in.setCodiceSocieta(paramCodiceSocieta);
			in.setCodiceEnte(paramCodiceEnte);
			in.setCodiceUtente(paramCodiceUtente);
			out =  WSCache.ruoliBDServer.getDataUltimoAggiornamento(in, request);
/*
			if(out.getDataAggiornamento() == null){
				dataUltimoAgg = Calendar.getInstance();
				dataUltimoAgg.setTime(df.parse("1000-01-01"));
			}else
			*/
			dataUltimoAgg = out.getDataAggiornamento();
			request.setAttribute("dataAggiornamento", dataUltimoAgg);
		} 
		/*
		catch (ParseException e){
			e.printStackTrace();
			setErrorMessage("Errore nella formattazione della data ultimo aggiornamento "
					+ e.getLocalizedMessage());
					
		}
		*/ 
		catch (FaultType e) {
			e.printStackTrace();
			setErrorMessage("Errore nel caricamento della data ultimo aggiornamento: FaultType - "
					+ e.getLocalizedMessage());
		} catch (RemoteException e) {
			e.printStackTrace();
			setErrorMessage("Errore nel caricamento degli Uffici Impositori: RemoteException - "
					+ e.getLocalizedMessage());
		}
		/*
		try
		{
			if (dataUltimoAgg==null)
			{
				dataUltimoAgg = Calendar.getInstance();
				dataUltimoAgg.setTime(df.parse("1972-04-23"));
				request.setAttribute("dataAggiornamento", dataUltimoAgg);
			}			
		}
		catch (Exception e)
		{}
		*/
	}	
	
	//da sostituire non appena pronto la store procedure
	protected void loadRuoliDataUltimoAggiornamento(HttpServletRequest request){
		RecuperaRuoliDataUltAggRequest in = new RecuperaRuoliDataUltAggRequest();
		RecuperaRuoliDataUltAggResponse out = null;
		Calendar dataUltimoAgg = null;
//		java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			try {
				in.setCodiceSocieta(paramCodiceSocieta);
				in.setCodiceEnte(paramCodiceEnte);
				in.setCodiceUtente(paramCodiceUtente);
				out = WSCache.ruoliBDServer.getDataUltimoAggiornamento(in, request);
	/*
				if(out.getDataAggiornamento() == null){
					dataUltimoAgg = Calendar.getInstance();
					dataUltimoAgg.setTime(df.parse("1000-01-01"));
				}else
				*/
				dataUltimoAgg = out.getDataAggiornamento();
				request.setAttribute("dataAggiornamento", dataUltimoAgg);
			} 
			/*
			catch (ParseException e){
				e.printStackTrace();
				setErrorMessage("Errore nella formattazione della data ultimo aggiornamento "
						+ e.getLocalizedMessage());
						
			}
			*/ 
			catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento della data ultimo aggiornamento: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento degli Uffici Impositori: RemoteException - "
						+ e.getLocalizedMessage());
			}
			/*
			try
			{
				if (dataUltimoAgg==null)
				{
					dataUltimoAgg = Calendar.getInstance();
					dataUltimoAgg.setTime(df.parse("1972-04-23"));
					request.setAttribute("dataAggiornamento", dataUltimoAgg);
				}			
			}
			catch (Exception e)
			{}
			*/
		}	

	
	@SuppressWarnings("unchecked")
	protected void salvaStato(HttpServletRequest request) 
	{
			Enumeration e = request.getParameterNames();
			String p = ""; 
			while (e.hasMoreElements()) 
			{
				p = (String)e.nextElement();
				request.setAttribute(p, request.getParameter(p));
			}

		}

	protected void resetDDLSession(HttpServletRequest request)
	{
		HttpSession sessione = request.getSession();
		
		sessione.setAttribute("listaProvince", null);
		sessione.setAttribute("listaUtenti", null);
		sessione.setAttribute("listaEnti", null);
		sessione.setAttribute("listaBeneficiariConv", null);
		sessione.setAttribute("listaTipologieServizio", null);
		sessione.setAttribute("listaGatewayCan", null);
		sessione.setAttribute("listaProvinceBen", null);
		sessione.setAttribute("listaBeneficiari", null);
		sessione.setAttribute("listaUtentiEnti", null);		

		//Graz
		sessione.setAttribute("listaImpostaServizio",null);
		sessione.setAttribute("listaUfficioImpositore",null);
	
	}

	
	protected Calendar getCalendarS(HttpServletRequest request, String dataS) {

		Locale locale = (Locale) request.getSession().getAttribute(
				MAFAttributes.LOCALE);
		Calendar cal = null;
		if (dataS != null && !dataS.equals("")) {

			String giorno = dataS.substring(8,10);
			String mese = dataS.substring(5,7);
			String anno = dataS.substring(0,4);
			
			if (giorno != null && mese != null && anno != null
					&& !giorno.trim().equals("") && !mese.trim().equals("")
					&& !anno.trim().equals("")) {
				cal = Calendar.getInstance(locale);
				cal.set(Integer.parseInt(anno), Integer.parseInt(mese) - 1,
						Integer.parseInt(giorno));
			}
		}
		return cal;
	}

	/*
	public String decodeMessaggio(com.seda.payer.pgec.webservice.srv.FaultType fte)
	{
		if (fte.getCode() == 100 || fte.getCode() == 101)
			return fte.getMessage1();
		else
			return testoErrore;	
	}
*/
	public String decodeMessaggio(com.seda.payer.bancadati.webservice.srv.FaultType fte)
	{
		if (fte.getCode() == 100 || fte.getCode() == 101)
			return fte.getMessage1();
		else
			return testoErrore;
	}
	
	public String decodeMessaggio(FaultType fte)
	{
		if (fte.getCode() == 100 || fte.getCode() == 101)
			return fte.getMessage1();
		else
			return testoErrore;
	}

	private void decodeChiamante(HttpServletRequest request)
	{
		String valore = request.getAttribute("pagPrec")!=null?(String)request.getAttribute("pagPrec"):"";

		if (valore.equals(""))
		{
		 chiamante = PAGINE.ALTRO;
		 return;
		}

		if (valore.equals("ricercaPartite"))
		{
		 chiamante = PAGINE.PAGPARTITE;
		 return;
		}

		if (valore.equals("ricercaAnagrafica"))
		{
		 chiamante = PAGINE.PAGANAGRAFICHE;
		 return;
		}

		if (valore.equals("ricercaRuoli"))
			{
			 chiamante = PAGINE.PAGRUOLI;
			 return;
			}
			
	}
	
	private void loadDettaglio(HttpServletRequest request) throws ActionException
	{
		String loadDett = request.getParameter("loadDett")!=null?request.getParameter("loadDett"):"";
		
		if (loadDett.equals("false") || chiamante == PAGINE.ALTRO)
			return;
		try
		{
			DettaglioPartitaAction dettaglioPartita = new DettaglioPartitaAction();
			switch (chiamante)
			{
				case PAGRUOLI:
					DettaglioRuoloAction dettaglioRuolo = new DettaglioRuoloAction();
					dettaglioRuolo.ricercaDettaglioXmlRuolo(request);
					
					dettaglioPartita.ricercaDettaglioXmlPartita(request);
					break;
				case PAGANAGRAFICHE:
					RicercaPartiteAnagraficheAction anagraficaAct = new RicercaPartiteAnagraficheAction();
					anagraficaAct.ricercaDettaglioXmlAnagrafica(request);
					dettaglioPartita.ricercaDettaglioXmlPartita(request);
				case PAGPARTITE:
					dettaglioPartita.ricercaDettaglioXmlPartita(request);
					break;
			}	
		}
		catch (Exception e)
		{
			throw new ActionException(e);
		}
	
	}
}
