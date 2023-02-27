package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.util.MAFAttributes;

import com.seda.payer.pgec.webservice.commons.dati.GetListaProvinceXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaProvinceXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetUtentiDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.GetUtentiDDLResponse;
import com.seda.payer.pgec.webservice.commons.dati.ListaBeneficiariByImpDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.ListaBeneficiariDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.ListaProvinceBenDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;

import com.seda.payer.pgec.webservice.commons.dati.ListaGatewayCanDDLRequest;

import com.seda.payer.pgec.webservice.commons.srv.FaultType;


/*
sono stati gestiti i nuovi campi

protected String codiceSocietaBen;
protected String codiceUtenteBen;
protected String codiceSocietaUtenteBen;
*/


public class BaseRiversamentoAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;

	//la classe BaseManagerAction non è ben strutturata --> viene ereditata, ma in parte bypassata da questa classe
	public static final String testoErrore = "errore generico";
	public static final String testoErroreColl = "errore: errore di collegamento al servizio web";
	
	protected String codiceSocieta;
	protected String codiceProvincia;
	protected String codiceUtente;
	protected String codiceSocietaUtente;
	protected String codiceAnagrafica;
    	
	protected String codiceEnteImpositore;
	protected String codiceUtenteEnteImpositore;	//aggiunto

	// MOD090
	protected String codiceSocietaBen;
	protected String codiceUtenteBen;
	protected String codiceProvinciaBen;
	protected String codiceSocietaUtenteBen;
    // fine MOD090
	
	protected String codiceEnteBeneficiario;
	protected String codiceCanale;

	protected int DDLType;
	protected String DDLChanged;
	
	protected boolean ddlEnteBeneficiarioDisabled = false;


	public void setProfile(HttpServletRequest request)  
	{
		super.setProfile(request);

		//mappatura variabili rendicontazione in riversamento
		// SET COMBO --> ci potremmo affidare alla superclasse che imposta le combo per rendicontazione
		/*
		request.setAttribute("societaDdlDisabled", request.getAttribute("ddlSocietaDisabled"));
    	request.setAttribute("provinciaDdlDisabled", request.getAttribute("ddlProvinciaDisabled"));
    	request.setAttribute("utenteDdlDisabled", request.getAttribute("ddlUtenteEnteDisabled"));
    	request.setAttribute("beneficiarioDdlDisabled", false);
		*/  	

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
    	codiceSocieta = (String)request.getAttribute("codSocieta") != null ? (String)request.getAttribute("codSocieta") : "";
		codiceProvincia = (String)request.getAttribute("codProvincia") != null? (String)request.getAttribute("codProvincia") : "";
		// il cod utente tornato dalle combo è ora la concatenazione società - cd utente
//		codiceUtente = (String)request.getAttribute("codUtente") != null? (String)request.getAttribute("codUtente") : "";
		codiceSocietaUtente = (String)request.getAttribute("codUtente") != null? (String)request.getAttribute("codUtente") : "";

		
		//if (request.getAttribute("codUtente") != null)
		//{
		  //codiceUtente = (String)request.getAttribute("codUtente");

		if (codiceSocietaUtente.length()==10) 
		{	
//mod090
//			  codiceUtente = codiceSocietaUtente.substring(5);
//		  	  codiceSocieta = codiceSocietaUtente.substring(0,5);
			 if ((codiceSocieta.equals("")&&(DDLChanged.equals("codUtente")||getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_CHANGED))||codiceSocieta.equals(codiceSocietaUtente.substring(0,5)))
				 {
				  codiceUtente = codiceSocietaUtente.substring(5);
			  	  codiceSocieta = codiceSocietaUtente.substring(0,5);
				 }
			 else
			 {
				 codiceSocietaUtente = ""; 
				 codiceUtente = "";
			 }
// fine mod090
		}
		//}
		else
		{
			 codiceUtente = "";
		}	
		// mod090
		codiceSocietaBen = (String)request.getAttribute("codSocietaBen") != null ? (String)request.getAttribute("codSocietaBen") : "";
		codiceProvinciaBen = (String)request.getAttribute("codProvinciaBen") != null? (String)request.getAttribute("codProvinciaBen") : "";
		codiceSocietaUtenteBen = (String)request.getAttribute("codUtenteBen") != null? (String)request.getAttribute("codUtenteBen") : "";
		
		if (codiceSocietaUtenteBen.length()==10) 
		{
			  if ((codiceSocietaBen.equals("")&&(DDLChanged.equals("codUtenteBen")||getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_BEN_CHANGED))||codiceSocietaBen.equals(codiceSocietaUtenteBen.substring(0,5)))
			  {
				  codiceUtenteBen = codiceSocietaUtenteBen.substring(5);
			  	  codiceSocietaBen = codiceSocietaUtenteBen.substring(0,5);				  
			  }
			  else
			  {
				  codiceSocietaUtenteBen = ""; 
				  codiceUtenteBen = "";				  
			  }	  
		  }
		//}
		else
			codiceUtenteBen = "";

		// fine mod090
		
		
		codiceEnteBeneficiario = (String)request.getAttribute("codBeneficiario") != null? (String)request.getAttribute("codBeneficiario") : "";	    	

		ddlSocietaDisabled = false;
    	ddlProvinciaDisabled = false;
    	ddlUtenteEnteDisabled = false;
    	ddlEnteBeneficiarioDisabled = false;

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
			    	codiceSocietaUtente = getUserBean().getCodiceSocieta() + getUserBean().getCodiceUtente();	
			    	ddlUtenteEnteDisabled = true;
			    }

	    	if (this.getUserProfile().equals("AMEN"))		    	
	    		{
			    	if (!getUserBean().getMultiUtenteEnabled())
			    		{
				    	 codiceProvincia="";
					     ddlProvinciaDisabled = true;
			    		 codiceEnteBeneficiario = getUserBean().getChiaveEnteConsorzio();
			 	    	 ddlEnteBeneficiarioDisabled = true;
			    		}
	    		}
		
		}

// mod090
/*
	    request.setAttribute("codSocieta", codiceSocieta);
    	request.setAttribute("codProvincia", codiceProvincia);
//    	request.setAttribute("codUtente", codiceUtente);
    	request.setAttribute("codUtente", codiceSocietaUtente);
    	request.setAttribute("codBeneficiario", codiceEnteBeneficiario);
*/
// fine mod090
	
    	
		request.setAttribute("societaDdlDisabled", ddlSocietaDisabled);
    	request.setAttribute("provinciaDdlDisabled", ddlProvinciaDisabled);
    	request.setAttribute("utenteDdlDisabled", ddlUtenteEnteDisabled);
    	request.setAttribute("beneficiarioDdlDisabled", ddlEnteBeneficiarioDisabled);

    	
//		abilitazione profilo riversamento
		if (getUserBean()!=null)
		{
	    	if (getUserBean().getProfiloRiversamentoEnabled())
	    		request.setAttribute("abilitazione", "Y");
	    	else
	    		request.setAttribute("abilitazione", "N");

//	    	request.setAttribute("operatore", getUserBean().getNome() + " " + getUserBean().getCognome());
	    	request.setAttribute("operatore", getUserBean().getUserName());
		}
		else
			request.setAttribute("operatore","Sconosciuto");

	    if (this.getUserProfile()!=null && this.getUserProfile().equals("AMMI"))
    		request.setAttribute("abilitazione", "Y");


		//proprietà che arrivano dalle pagine jsp
/*
		codiceAnagrafica = (String)session.getAttribute("codAnagrafica") != null? (String)session.getAttribute("codAnagrafica"): request.getParameter("codAnagrafica");
        codiceEnteImpositore = (String)session.getAttribute("codImpositore") != null? (String)session.getAttribute("codImpositore"): request.getParameter("codImpositore");
		codiceCanale = (String)session.getAttribute("codCanale") != null? (String)session.getAttribute("codCanale"): request.getParameter("codCanale");
*/		
		codiceAnagrafica = (String)request.getAttribute("codAnagrafica") != null? (String)request.getAttribute("codAnagrafica"): "";

//sostituito da codiceUtenteEnteImpositore		
//		codiceEnteImpositore = (String)request.getAttribute("codImpositore") != null? (String)request.getAttribute("codImpositore"): "";
		codiceUtenteEnteImpositore = (String)request.getAttribute("codImpositore") != null? (String)request.getAttribute("codImpositore"): "";
		//if (request.getAttribute("codImpositore") != null)
		//{
		 // codiceEnteImpositore = (String)request.getAttribute("codImpositore");

// mod090		
/*
		if (codiceUtenteEnteImpositore.length()==20){
			  codiceSocieta = codiceUtenteEnteImpositore.substring(0,5);
			  codiceUtente = codiceUtenteEnteImpositore.substring(5,10);
			  codiceEnteImpositore = codiceUtenteEnteImpositore.substring(10);
		  } else
			  codiceEnteImpositore="";
*/
		 if (codiceUtenteEnteImpositore.length()==20)
		 {
			 if ((codiceSocieta.equals("")&& (DDLChanged.equals("codImpositore")||getFiredButton(request)==FiredButton.TX_BUTTON_IMPOSITORE_CHANGED))||codiceSocieta.equals(codiceUtenteEnteImpositore.substring(0,5)))
			 {
				  codiceSocieta = codiceUtenteEnteImpositore.substring(0,5);				 
				  codiceEnteImpositore = codiceUtenteEnteImpositore.substring(10);

				  if ((codiceUtente.equals("")&& (DDLChanged.equals("codImpositore")||getFiredButton(request)==FiredButton.TX_BUTTON_IMPOSITORE_CHANGED))||codiceUtente.equals(codiceUtenteEnteImpositore.substring(5,10)))
					  {
				    	  codiceSocietaUtente = codiceUtenteEnteImpositore.substring(0,10);	
						  codiceUtente = codiceUtenteEnteImpositore.substring(5,10);
//						  codiceEnteImpositore = codiceUtenteEnteImpositore.substring(10);
					  }
					  else
					  {
						 codiceEnteImpositore = "";				 
						 codiceUtenteEnteImpositore = "";						  
					  }	  			 
			 }
			 else
			 {
				 codiceEnteImpositore = "";				 
				 codiceUtenteEnteImpositore = "";
			 }
		 }
		 else
			 codiceEnteImpositore = "";				 
			 
// fine mod090		  
		//}
		//else
			//codiceEnteImpositore = "";
		
		codiceCanale = (String)request.getAttribute("codCanale") != null? (String)request.getAttribute("codCanale"): "";

//		elimino il prefisso utente dal codice impositore (da aggiornare)
//		if (codiceEnteImpositore!=null && codiceEnteImpositore.length() >0) codiceEnteImpositore = codiceEnteImpositore.substring(5, codiceEnteImpositore.length());

		// mod090

		/*
		  test
		        request.setAttribute("codSocieta", codiceSocieta);
		    	request.setAttribute("codProvincia", codiceProvincia);
		    	request.setAttribute("codUtente", codiceSocietaUtente);
		    	request.setAttribute("codImpositore", codiceUtenteEnteImpositore);
		    	request.setAttribute("codBeneficiario", codiceEnteBeneficiario);
*/
		    	// fine mod090

		
	}

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		loadDDLStatic(request, session);

		//MOD090
//		setProfile(request);	
	//fine mod090
		
		if (request.getAttribute("DDLType")==null||request.getAttribute("DDLType").equals("")) 
			DDLType = 0;
		else
			DDLType = Integer.parseInt((String)request.getAttribute("DDLType"));
		
		/*** GESTIONE DDL CON JAVASCRIPT ***/

		if (request.getParameter("DDLChanged") == null)
			DDLChanged="";
		else
			DDLChanged = request.getParameter("DDLChanged");

		//mod090
		setProfile(request);	
//fine mod090
		return null;
	}
	
	
	protected void setAmbienteFromReq(HttpServletRequest request)
	{
		HttpSession session = request.getSession();

		if (this.getUserProfile()!=null && this.getUserProfile().equals("AMEN"))
	    {
	    	codiceSocieta = getUserBean().getCodiceSocieta();
	    	codiceProvincia="";
	    	codiceUtente = getUserBean().getCodiceUtente();	
	    	codiceSocietaUtente = getUserBean().getCodiceSocieta()+getUserBean().getCodiceUtente();	
	    	codiceEnteBeneficiario = getUserBean().getChiaveEnteConsorzio();
	    	request.setAttribute("societaDdlDisabled", true);
	    	request.setAttribute("provinciaDdlDisabled", true);
	    	request.setAttribute("utenteDdlDisabled", true);
	    	request.setAttribute("beneficiarioDdlDisabled", true);

	    	request.setAttribute("codSocieta", codiceSocieta);
	    	request.setAttribute("codProvincia", "");
//	    	request.setAttribute("codUtente", codiceUtente);
	    	request.setAttribute("codUtente", codiceSocietaUtente);
	    	request.setAttribute("codBeneficiario", codiceEnteBeneficiario);
	    	
	    }
	    else
	    {
	    	
	    	
			codiceSocieta = (String)request.getAttribute("codSocieta") != null ? (String)request.getAttribute("codSocieta"): "";
			codiceProvincia = (String)request.getAttribute("codProvincia") != null? (String)request.getAttribute("codProvincia"): "";
//			codiceUtente = (String)request.getAttribute("codUtente") != null? (String)request.getAttribute("codUtente"): "";
			codiceSocietaUtente = (String)request.getAttribute("codUtente") != null? (String)request.getAttribute("codUtente"): "";
			if (request.getAttribute("codUtente") != null)
			{
			  codiceUtente = (String)request.getAttribute("codUtente");
			  if (codiceUtente.length()==10)
				  codiceUtente = codiceUtente.substring(5);
			}
			else
				codiceUtente = "";

			codiceEnteBeneficiario = (String)request.getAttribute("codBeneficiario") != null? (String)request.getAttribute("codBeneficiario"): "";	    	

	    	
	    	session.setAttribute("societaDdlDisabled", false);
	    	session.setAttribute("provinciaDdlDisabled", false);
	    	session.setAttribute("utenteDdlDisabled", false);
	    	session.setAttribute("beneficiarioDdlDisabled", false);
	    }

	    
		//proprietà che arrivano dalle pagine jsp

	    codiceAnagrafica = (String)request.getAttribute("codAnagrafica") != null? (String)request.getAttribute("codAnagrafica"): "";

//	    codiceEnteImpositore = (String)request.getAttribute("codImpositore") != null? (String)request.getAttribute("codImpositore"): "";
		codiceUtenteEnteImpositore = (String)request.getAttribute("codImpositore") != null? (String)request.getAttribute("codImpositore"): "";
		if (request.getAttribute("codImpositore") != null)
		{
		  codiceEnteImpositore = (String)request.getAttribute("codImpositore");
		  if (codiceEnteImpositore.length()==20)
			  codiceEnteImpositore = codiceEnteImpositore.substring(10);
		}
		else
			codiceEnteImpositore = "";

	    codiceCanale = (String)request.getAttribute("codCanale") != null? (String)request.getAttribute("codCanale"): "";

		request.setAttribute("operatore","Sconosciuto");

		
		
		/*** GESTIONE DDL CON JAVASCRIPT ***/

	    if (request.getAttribute("DDLType")==null) 
			DDLType = 0;
		else
			DDLType = Integer.parseInt(request.getAttribute("DDLType").toString());
		
		if (request.getAttribute("DDLChanged") == null)
			DDLChanged="";
		else
			DDLChanged = request.getAttribute("DDLChanged").toString();

	}
	
	/*
	protected PageInfo getPageInfo(com.seda.payer.riversamento.webservice.dati.PageInfo rpi, int rowsPerPages) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rowsPerPages);
		return pageInfo;
	}*/

//	protected PageInfo getPageInfo(com.seda.payer.pgec.webservice.beneficiario.dati.PageInfo rpi, int rowsPerPages) {
	protected PageInfo getPageInfo(com.seda.payer.pgec.webservice.beneficiario.dati.PageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPage());
		return pageInfo;
	}

	protected PageInfo getPageInfo(com.seda.payer.pgec.webservice.associmpben.dati.PageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPage());
		return pageInfo;
	}

	protected PageInfo getPageInfo(com.seda.payer.pgec.webservice.convenzioneimp.dati.PageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPage());
		return pageInfo;
	}

	@SuppressWarnings("unchecked")
	protected void salvaStato(HttpServletRequest request) 
	{
			Enumeration<String> e = request.getParameterNames();
			String p = ""; 
			while (e.hasMoreElements()) 
			{
				p = e.nextElement();
				request.setAttribute(p, request.getParameter(p));
			}

		}
		
//caricamento DDL specifiche
	protected void loadDDLProvinciaBen(HttpServletRequest request, HttpSession session, 
			                           String codSocieta, boolean forceReload) 
	{
		//se ho già caricato le province convenzionate, le recupero dalla sessione, altrimenti dal db
		if (session.getAttribute("listaProvinceBen") != null  && !forceReload) {
			request.setAttribute("listaProvinceBen", (String)session.getAttribute("listaProvinceBen"));
		}
		else {
			try {
				ListaProvinceBenDDLRequest in = new ListaProvinceBenDDLRequest();
				in.setCodSocieta(codSocieta);
//				DDLResponseType res = WSCache.rivCommonsServer.getListaProvinceBenDDL(in);
				DDLResponseType res = WSCache.commonsServer.getListaProvinceBenDDL(in, request);
				request.setAttribute("listaProvinceBen", res.getXml());
				//le metto in sessione per non doverle ricaricare ogni volta
				session.setAttribute("listaProvinceBen", res.getXml());				
			} 
			catch (FaultType e) {
				e.printStackTrace();
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	protected void loadDDLBeneficiario(HttpServletRequest request, HttpSession session, 
									  String codSocieta, String codProvincia, String codUtente,
									  boolean forceReload) 
	{
		if (session.getAttribute("listaBeneficiari") != null  && !forceReload) 
		{
			request.setAttribute("listaBeneficiari", (String)session.getAttribute("listaBeneficiari"));
		}
		else 
		{
			try {
				ListaBeneficiariDDLRequest in = new ListaBeneficiariDDLRequest();
				in.setCodSocieta(codSocieta);
				in.setSiglaProvincia(codProvincia);
				in.setCodUtente(codUtente);

//				DDLResponseType res = WSCache.rivCommonsServer.getListaBeneficiariDDL(in);
				DDLResponseType res = WSCache.commonsServer.getListaBeneficiariDDL(in, request);

				request.setAttribute("listaBeneficiari", res.getXml());
				session.setAttribute("listaBeneficiari", res.getXml());				
			} 
			catch (FaultType e) {
				e.printStackTrace();
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
		
		
	protected void loadDDLBeneficiarioByImp(HttpServletRequest request, HttpSession session, 
			  String codSocieta, String codProvincia, String codUtente, String codImpositore,
			  String codSocietaBen, String codProvinciaBen, String codUtenteBen,
			  boolean forceReload) 
	{
		if (session.getAttribute("listaBeneficiariConv") != null  && !forceReload) 
		{
			request.setAttribute("listaBeneficiariConv", (String)session.getAttribute("listaBeneficiariConv"));
		}
		else 
		{
			try {
				ListaBeneficiariByImpDDLRequest in = new ListaBeneficiariByImpDDLRequest();
				in.setCodSocieta(codSocieta);
				in.setSiglaProvincia(codProvincia);
				in.setCodUtente(codUtente);
				in.setChiaveImpositore(codImpositore);
				in.setCodSocietaBen(codSocietaBen);
				in.setSiglaProvinciaBen(codProvinciaBen);
				in.setCodUtenteBen(codUtenteBen);
				

//				DDLResponseType res = WSCache.rivCommonsServer.getListaBeneficiariConvDDL(in);
				DDLResponseType res = WSCache.commonsServer.getListaBeneficiariByImpDDL(in, request);
				request.setAttribute("listaBeneficiariConv", res.getXml());
				session.setAttribute("listaBeneficiariConv", res.getXml());				
			} 
			catch (FaultType e) {
				e.printStackTrace();
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	protected void loadDDLGatewayCan(HttpServletRequest request, HttpSession session, 
			  String codSocieta, String codProvincia, String codUtente, String codImpositore,
			  String codCanale, boolean forceReload) 
	{
		if (session.getAttribute("listaGatewayCan") != null  && !forceReload) 
		{
			request.setAttribute("listaGatewayCan", (String)session.getAttribute("listaGatewayCan"));
		}
		else 
		{
			try {
				ListaGatewayCanDDLRequest in = new ListaGatewayCanDDLRequest();
				in.setCodSocieta(codSocieta);
				in.setSiglaProvincia(codProvincia);
				in.setCodUtente(codUtente);
				in.setChiaveImpositore(codImpositore);
                in.setCodCanale(codCanale);

                //                DDLResponseType res = WSCache.rivCommonsServer.getListaGatewayCanDDL(in);
				DDLResponseType res = WSCache.commonsServer.getListaGatewayCanDDL(in, request);
				
				request.setAttribute("listaGatewayCan", res.getXml());
				session.setAttribute("listaGatewayCan", res.getXml());				
			} 
			catch (FaultType e) {
				e.printStackTrace();
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	protected void LoadListaUtentiEntiAllXml_DDL(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String siglaProvincia,
			String codiceEnte, String codiceUtente, boolean forceReload) {
		GetListaUtentiEntiXml_DDLRequestType in = null;
		GetListaUtentiEntiXml_DDLResponseType res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "listaUtentiEntiAll";
		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new GetListaUtentiEntiXml_DDLRequestType();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceUtente(codiceUtente);
				in.setSiglaProvincia(siglaProvincia);
				in.setCodiceEnte(codiceEnte);
				res = WSCache.commonsServer.getListaUtentiEntiAllXml_DDL(in, request);
				response = res.getResponse();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento degli Utenti/Impositori: "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento degli Utenti/Impositori: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento degli Utenti/Impositori: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}
	
	protected void loadProvinciaBenXml_DDL(HttpServletRequest request,
			HttpSession session, String codiceSocieta, boolean forceReload) {
		GetListaProvinceXml_DDLRequestType in = null;
		GetListaProvinceXml_DDLResponseType res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "listaProvinceBen";
		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new GetListaProvinceXml_DDLRequestType();
				in.setCodiceSocieta(codiceSocieta);
				res = WSCache.commonsServer.getListaProvinceXml_DDL(in, request);
				response = res.getResponse();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento delle Province: "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Province: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Province: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}
	
	protected void loadDDLBenUtente(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String codiceProvincia,
			boolean forceReload) {
		// se ho già caricato le province convenzionate, le recupero dalla
		// sessione, altrimenti dal db
		if (session.getAttribute("listaUtentiBen") != null && !forceReload) {
			request.setAttribute("listaUtentiBen", (String) session
					.getAttribute("listaUtentiBen"));
		} else {
			try {
				GetUtentiDDLRequest in = new GetUtentiDDLRequest();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceProvincia(codiceProvincia);
				GetUtentiDDLResponse res = WSCache.commonsServer
						.getUtentiDDL(in, request);
				request.setAttribute("listaUtentiBen", res.getListXml());
				session.setAttribute("listaUtentiBen", res.getListXml());
			} catch (FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	
	protected void resetDDLSession(HttpServletRequest request)
	{
		HttpSession sessione = request.getSession();
		sessione.setAttribute("listaProvinceBen", null);
		sessione.setAttribute("listaUtentiBen", null);
		sessione.setAttribute("listaProvince", null);
		sessione.setAttribute("listaUtenti", null);
		sessione.setAttribute("listaEnti", null);
		sessione.setAttribute("listaBeneficiariConv", null);
		sessione.setAttribute("listaTipologieServizio", null);
		sessione.setAttribute("listaGatewayCan", null);
		sessione.setAttribute("listaProvinceBen", null);
		sessione.setAttribute("listaBeneficiari", null);
		sessione.setAttribute("listaUtentiEnti", null);		
		sessione.setAttribute("listaUtentiEntiAll", null);		
	}

	
	protected void resetValDDL(HttpServletRequest request)
	{
		//reset dei parametri combo in request
		request.setAttribute("codSocieta", "");
		request.setAttribute("codProvincia", "");
		request.setAttribute("codUtente", "");
		request.setAttribute("codImpositore", "");
		request.setAttribute("codBeneficiario", "");
		request.setAttribute("codTipologiaServizio", "");
		request.setAttribute("codGateway", "");
		request.setAttribute("codAnagrafica", "");
	}
	/*
	public void setMessage(HttpServletRequest request, String message) {
		super.setMessage(message);
		request.setAttribute("riv_message", message);
	}

	public void setErrorMessage(HttpServletRequest request, String message) {
		super.setErrorMessage(message);
		request.setAttribute("riv_error_message", message);
	}
	
*/
	
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

	public String decodeMessaggio(com.seda.payer.pgec.webservice.srv.FaultType fte)
	{
		if (fte.getCode() == 100 || fte.getCode() == 101 || fte.getCode() == 102)
			return fte.getMessage1();
		else
			return testoErrore;	
	}

	/*
	public String decodeMessaggio(com.seda.payer.riversamento.webservice.srv.FaultType fte)
	{
		if (fte.getCode() == 100 || fte.getCode() == 101 || fte.getCode() == 102)
			return fte.getMessage1();
		else
			return testoErrore;
	}*/
	
	public String decodeMessaggio(FaultType fte)
	{
		if (fte.getCode() == 100 || fte.getCode() == 101 || fte.getCode() == 102)
			return fte.getMessage1();
		else
			return testoErrore;
	}

}
