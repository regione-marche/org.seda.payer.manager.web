package org.seda.payer.manager.eccedenze.actions;

/*
//import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
//import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.util.MAFAttributes;


import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class BaseEccedenzeAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;

	//la classe BaseManagerAction non è ben strutturata --> viene ereditata, ma in parte bypassata da questa classe
	public static final String testoErrore = "errore generico";
	public static final String testoErroreColl = "errore: errore di collegamento al servizio web";
	
	protected String siglaProvincia;

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
		/*
    	siglaProvincia = isNull(request.getAttribute(Field.TX_PROVINCIA.format()));

		ddlSocietaDisabled = false;
    	ddlProvinciaDisabled = false;
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
			    }

	    	if (this.getUserProfile().equals("AMEN"))		    	
	    		{
	    			ddlUtenteEnteDisabled = true;
			    	if (!getUserBean().getMultiUtenteEnabled())
			    		{
			    		 siglaProvincia="";
			    		 ddlProvinciaDisabled = true;
			    		}
	    		}
		
		}
	    
    	request.setAttribute(Field.TX_SOCIETA.format(), paramCodiceSocieta);
    	request.setAttribute(Field.TX_PROVINCIA.format(), siglaProvincia);
    	request.setAttribute(Field.TX_UTENTE.format(), paramCodiceUtente);

		request.setAttribute("ddlSocietaDisabled", ddlSocietaDisabled);
    	request.setAttribute("ddlProvinciaDisabled", ddlProvinciaDisabled);
    	request.setAttribute("ddlUtenteEnteDisabled", ddlUtenteEnteDisabled);


		if (getUserBean()!=null)
			request.setAttribute("operatore", getUserBean().getUserName());
		else
			request.setAttribute("operatore","Sconosciuto");
		
		
	}

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();

		loadDDLStatic(request, session);
		
		setProfile(request);	
		
		if (request.getAttribute("DDLType")==null||request.getAttribute("DDLType").equals("")) 
			DDLType = 0;
		else
			DDLType = Integer.parseInt((String)request.getAttribute("DDLType"));
		
		return null;
	}

	
	protected PageInfo getPageInfo(com.seda.payer.eccedenze.webservice.dati.PageInfo rpi, int rowsPerPages) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rowsPerPages);
		return pageInfo;
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
/*
	public String decodeMessaggio(com.seda.payer.eccedenze.webservice.srv.FaultType fte)
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

}
*/
