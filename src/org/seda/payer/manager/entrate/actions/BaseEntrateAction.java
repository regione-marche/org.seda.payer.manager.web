package org.seda.payer.manager.entrate.actions;

//import java.rmi.RemoteException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLImpServizioRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLImpServizioResponse;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLTipServizioRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLTipServizioResponse;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLUffImpositoreRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLUffImpositoreResponse;
import com.seda.payer.bancadati.webservice.dati.RecuperaDataUltAggiornamentoRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDataUltAggiornamentoResponse;
import com.seda.payer.bancadati.webservice.dati.ResponseType;
import com.seda.payer.bancadati.webservice.dati.ResponseTypeRetCode;
import com.seda.payer.bancadati.webservice.srv.FaultType;

public class BaseEntrateAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;

	//la classe BaseManagerAction non � ben strutturata --> viene ereditata, ma in parte bypassata da questa classe
	public static final String testoErrore = "errore generico";
	public static final String testoErroreColl = "errore: errore di collegamento al servizio web";
	
	public static enum PAGINE {PAGEMISSIONI, PAGANAGRAFICHE, PAGDOCUMENTI, ALTRO};

    protected String siglaProvincia;
	protected String paramCodiceTipologiaServizio;
	protected Calendar dataUltimoAgg;
    protected PAGINE chiamante;
	protected int DDLType;


	public void setProfile(HttpServletRequest request)  
	{
		HttpSession session=request.getSession();
		
		super.setProfile(request);


		if(this.getUserProfile().equals("AMEN")){
			loadDataUltimoAggiornamento(request);		
		}		
		
		//paramCodiceTipologiaServizio = (String)request.getAttribute("tx_tipologia_servizio");
		paramCodiceTipologiaServizio = getTipologiaServizio(request, session);
		
		if (getUserBean()!=null)
			request.setAttribute("operatore", getUserBean().getUserName());
		else
			request.setAttribute("operatore","Sconosciuto");
	}

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		loadDDLStatic(request, session);
		decodeChiamante(request);
		loadDettaglio(request);
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
			HttpSession session, String codiceSocieta, String codiceUtente, String codiceEnte, String tipoServizio,
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
				in.setCodiceUtente(codiceUtente);
				in.setCodiceEnte(codiceEnte);
				//Giulia 8/05/2014 INIZIO
				if(tipoServizio.length()>3)
					tipoServizio = tipoServizio.substring(0,3);
				//Giulia 8/05/2014 FINE
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
	
	//PG170070 GG 20170530 - inizio
	protected void loadListaTipologiaServizio(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String codiceUtente, String codiceEnte, boolean forceReload) {
		RecuperaDDLTipServizioRequest in = null;
		RecuperaDDLTipServizioResponse res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "listaTipologieServizio";

		/* Nel caso di un utente di tipologia AMEN*/
		String listaTipologiaServizio="";
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		if (user.getProfile().equals("AMEN")) {
			listaTipologiaServizio=user.getListaTipologiaServizioString();
		}

		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new RecuperaDDLTipServizioRequest();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceUtente(codiceUtente);
				in.setCodiceEnte(codiceEnte);
				in.setTipologiaServizio(listaTipologiaServizio);

				res = WSCache.entrateBDServer.recuperaDDLTipologiaServizio(in, request);

				response = res.getRisultato();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getListXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento delle Tipologie Servizio: "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Servizio: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Servizio: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}
	//PG170070 GG 20170530 - fine
	
	protected void loadDataUltimoAggiornamento(HttpServletRequest request){
	RecuperaDataUltAggiornamentoRequest in = new RecuperaDataUltAggiornamentoRequest();
	RecuperaDataUltAggiornamentoResponse out = null;
	Calendar dataUltimoAgg = null;
//	java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try {
			in.setCodiceSocieta(paramCodiceSocieta);
			in.setCodiceEnte(paramCodiceEnte);
			in.setCodiceUtente(paramCodiceUtente);
			out =  WSCache.entrateBDServer.getDataUltimoAggiornamento(in, request);
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

		if (valore.equals("ricercaDocumenti") || valore.equals("gestioneDocumentiCarico"))
		{
		 chiamante = PAGINE.PAGDOCUMENTI;
		 return;
		}

		if (valore.equals("ricercaAnagrafica"))
		{
		 chiamante = PAGINE.PAGANAGRAFICHE;
		 return;
		}

		if (valore.equals("ricercaEmissioni"))
			{
			 chiamante = PAGINE.PAGEMISSIONI;
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
			DettaglioDocumentoAction documentiAct = new DettaglioDocumentoAction();
			switch (chiamante)
			{
				case PAGEMISSIONI:
					RicercaDocumentiEmiAction emissioniAct = new RicercaDocumentiEmiAction();
					emissioniAct.ricercaDettaglioEmi(request);
					documentiAct.ricercaDettaglioDocumento(request);
					break;
				case PAGANAGRAFICHE:
					RicercaAnagDettAction anagraficaAct = new RicercaAnagDettAction();
					anagraficaAct.ricercaDettaglioAnag(request);
				case PAGDOCUMENTI:
					documentiAct.ricercaDettaglioDocumento(request);
					break;
			}	
		}
		catch (Exception e)
		{
			throw new ActionException(e);
		}
		
	}
}
