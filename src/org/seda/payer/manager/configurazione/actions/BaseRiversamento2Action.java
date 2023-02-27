package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.taglibs.ViewStateManager;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.payer.pgec.webservice.commons.dati.DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetStaticDDLListsRequest;
import com.seda.payer.pgec.webservice.commons.dati.GetStaticDDLListsResponse;
import com.seda.payer.pgec.webservice.commons.dati.ListaBeneficiariBollDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.ListaBeneficiariDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.ListaProvinceBenDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class BaseRiversamento2Action extends BaseManagerAction {
	private static final long serialVersionUID = 1L;
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	protected NumberFormat inf = NumberFormat.getIntegerInstance();
	
	public static final String testoErrore = "errore generico";
	public static final String testoErroreColl = "errore: errore di collegamento al servizio web";

	protected String codiceSocieta;
	protected String codiceProvincia;
	protected String codiceUtente;
	protected String codiceSocietaUtente;
	protected String codiceAnagrafica;
    
	protected String codiceEnteBeneficiario;
	
	protected boolean ddlEnteBeneficiarioDisabled = false;

	
	protected FiredButton firedButton;

	public static String[] labelBottoni = {"validator.message", "tx_button_default", "tx_button_reset", "tx_button_set_flag_cbi",
		"tx_button_avanza_stato", "tx_button_mega_riv", "tx_button_reset_ins", 
		"tx_button_reset_mod", "tx_button_reset", "tx_button_cerca_exp", "tx_button_cerca",
        "tx_button_edit", "tx_button_stampa", "tx_button_download", "tx_button_provincia_changed",
        "fired_button_hidden", "tx_button_utente_changed", "tx_button_tipo_servizio_changed", 
        "tx_button_aggiungi", "tx_button_delete", "tx_button_delete_end", "tx_button_avanti",
        "tx_button_step1", "tx_button_edit_end", "tx_button_aggiungi_end", "tx_button_cancel", 
        "tx_button_password_autogenerata_changed", "tx_button_profilo_utente_changed",
        "tx_button_scadenza_changed", "tx_button_movimento_changed",
        "tx_button_movimento_changed_no_js", "tx_button_indietro", "tx_button_nuovo", 
        "tx_button_detail_imp", "tx_button_detail_contr", "tx_button_login", "tx_button_cerca_scad",
        "tx_button_cerca_pag", "tx_button_cerca_trib", "tx_button_send_pdf_mav", "tx_button_mega_riv",
        "tx_button_conferma_storno", "tx_button_add", "tx_button_remove", "btnWis",
    	"tx_button_salva", "btnComuneNascitaEstero", "btnAvantiProvinciaNascita",
    	"btnAvantiProvinciaResidenza", "btnComuneResidenzaEstero", "btnAvantiProvinciaDomicilio",
    	"btnComuneDomicilioEstero","tx_button_societa_ben_changed","tx_button_utente_ben_changed","tx_button_provincia_ben_changed"};
	
	public void setProfile(HttpServletRequest request)  
	{
		super.setProfile(request);
		
    	// prelievo parametri dalla request o impostazione di default
    	codiceSocieta = (String)request.getAttribute("codSocieta") != null ? (String)request.getAttribute("codSocieta") : "";
		codiceProvincia = (String)request.getAttribute("codProvincia") != null? (String)request.getAttribute("codProvincia") : "";
		// il cod utente tornato dalle combo è ora la concatenazione società - cd utente
//		codiceUtente = (String)request.getAttribute("codUtente") != null? (String)request.getAttribute("codUtente") : "";
		codiceSocietaUtente = (String)request.getAttribute("codUtente") != null? (String)request.getAttribute("codUtente") : "";
		
		//if (request.getAttribute("codUtente") != null)
		//{
		  //codiceUtente = (String)request.getAttribute("codUtente");
		//inizio 090
		if (codiceSocietaUtente.length()==10) 
		{	
			 if ((codiceSocieta.equals("")&&(getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_CHANGED))||codiceSocieta.equals(codiceSocietaUtente.substring(0,5)))
				 {
				  codiceUtente = codiceSocietaUtente.substring(5);
			  	  codiceSocieta = codiceSocietaUtente.substring(0,5);
				 }
			 else
			 {
				 codiceSocietaUtente = ""; 
				 codiceUtente = "";
			 }
		}
		else
		{
			 codiceUtente = "";
		}	
		//fine 090
		
		//old
		/*if (codiceSocietaUtente.length()==10 && codiceSocieta != null && codiceSocieta.length() >0 && codiceSocietaUtente.substring(0,5).equals(codiceSocieta)) {
			  codiceUtente = codiceSocietaUtente.substring(5);
		  	  codiceSocieta = codiceSocietaUtente.substring(0,5);
		  }
		//}
		else
			codiceUtente = "";*/
		//fine old
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
	    
    	request.setAttribute("codSocieta", codiceSocieta);
    	request.setAttribute("codProvincia", codiceProvincia);
    	request.setAttribute("codUtente", codiceSocietaUtente);
    	request.setAttribute("codBeneficiario", codiceEnteBeneficiario);

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

	    	request.setAttribute("operatore", getUserBean().getUserName());
		}
		else
			request.setAttribute("operatore","Sconosciuto");

	    if (this.getUserProfile()!=null && this.getUserProfile().equals("AMMI"))
    		request.setAttribute("abilitazione", "Y");

		codiceAnagrafica = (String)request.getAttribute("codAnagrafica") != null? (String)request.getAttribute("codAnagrafica"): "";


	}
	
	public Object service(HttpServletRequest request, String vista) throws ActionException {
		HttpSession session = request.getSession();
		super.service(request);

		firedButton = getFiredButton(request);
        
		if (request.getParameter("start")==null&&(request.getAttribute("start")==null||request.getAttribute("start").equals("")))
		{
			replyAttributes(vista, false, request, labelBottoni);	
		}
		
//		tx_SalvaStato(request);
		if(session.getAttribute("noStato")==null||session.getAttribute("noStato").equals("")){
			salvaStato(request);
		}
		session.setAttribute("noStato","");
		request.setAttribute("start", "");

		setProfile(request);
//		HttpSession session = request.getSession();
		loadDDLStatic(request);
		
		return null;
	}
	
	protected FiredButton getFiredButton(HttpServletRequest request) {

		if (request.getAttribute("tx_button_default") != null)
			return FiredButton.TX_BUTTON_DEFAULT;

		if (request.getAttribute("tx_button_reset") != null)
			return FiredButton.TX_BUTTON_RESET;

		if (request.getAttribute("tx_button_set_flag_cbi") != null)
			return FiredButton.TX_BUTTON_SET_FLAG_CBI;

		if (request.getAttribute("tx_button_avanza_stato") != null)
			return FiredButton.TX_BUTTON_AVANZA_STATO;

		if (request.getAttribute("tx_button_mega_riv") != null)
			return FiredButton.TX_BUTTON_MEGA_RIV;
		
		if (request.getAttribute("tx_button_reset_ins") != null)
			return FiredButton.TX_BUTTON_RESET_INS;

		if (request.getAttribute("tx_button_reset_mod") != null)
			return FiredButton.TX_BUTTON_RESET_MOD;

		if (request.getAttribute("tx_button_reset") != null)
			return FiredButton.TX_BUTTON_RESET;

		if (request.getAttribute("tx_button_cerca_exp") != null)
			return FiredButton.TX_BUTTON_CERCA_EXP;


		if (request.getAttribute("tx_button_edit") != null)
			return FiredButton.TX_BUTTON_EDIT;
		
		if (request.getAttribute("tx_button_stampa") != null)
			return FiredButton.TX_BUTTON_STAMPA;
		
		if (request.getAttribute("tx_button_download") != null)
			return FiredButton.TX_BUTTON_DOWNLOAD;
		
		
		if (request.getAttribute("tx_button_aggiungi") != null)
			return FiredButton.TX_BUTTON_AGGIUNGI;
		
		if (request.getAttribute("tx_button_delete") != null)
			return FiredButton.TX_BUTTON_DELETE;

		if (request.getAttribute("tx_button_delete_end") != null)
			return FiredButton.TX_BUTTON_DELETE_END;

		if (request.getAttribute("tx_button_avanti") != null)
			return FiredButton.TX_BUTTON_AVANTI;

		if (request.getAttribute("tx_button_step1") != null)
			return FiredButton.TX_BUTTON_STEP1;

		if (request.getAttribute("tx_button_edit_end") != null)
			return FiredButton.TX_BUTTON_EDIT_END;
		
		if (request.getAttribute("tx_button_aggiungi_end") != null)
			return FiredButton.TX_BUTTON_AGGIUNGI_END;
		
		if (request.getAttribute("tx_button_cancel") != null)
			return FiredButton.TX_BUTTON_CANCEL;

		

		if (request.getAttribute("tx_button_movimento_changed") != null && ((String)request.getAttribute("tx_button_movimento_changed")).equals("Selezione"))
			return FiredButton.TX_BUTTON_MOVIMENTO_CHANGED;

		if (request.getAttribute("tx_button_movimento_changed_no_js") != null)
			return FiredButton.TX_BUTTON_MOVIMENTO_CHANGED_NO_JS;

		if (request.getAttribute("tx_button_indietro") != null)
			return FiredButton.TX_BUTTON_INDIETRO;
		
		if (request.getAttribute("tx_button_nuovo") != null)
			return FiredButton.TX_BUTTON_NUOVO;

		if (request.getAttribute("tx_button_detail_imp") != null)
			return FiredButton.TX_BUTTON_DETAIL_IMP;

		if (request.getAttribute("tx_button_detail_contr") != null)
			return FiredButton.TX_BUTTON_DETAIL_CONTR;

		if (request.getAttribute("tx_button_login") != null)
			return FiredButton.TX_BUTTON_LOGIN;
		
		if (request.getAttribute("tx_button_cerca_scad") != null)
			return FiredButton.TX_BUTTON_CERCA_SCAD;

		if (request.getAttribute("tx_button_cerca_pag") != null)
			return FiredButton.TX_BUTTON_CERCA_PAG;

		if (request.getAttribute("tx_button_cerca_trib") != null)
			return FiredButton.TX_BUTTON_CERCA_TRIB;

		if (request.getAttribute("tx_button_send_pdf_mav") != null)
			return FiredButton.TX_BUTTON_SEND_PDF_MAV;
		
		if (request.getAttribute("tx_button_mega_riv") != null)
			return FiredButton.TX_BUTTON_MEGA_RIV;
		
		if (request.getAttribute("tx_button_conferma_storno") != null)
			return FiredButton.TX_BUTTON_CONFERMA_STORNO;
		
		if (request.getAttribute("tx_button_add") != null)
			return FiredButton.TX_BUTTON_ADD;
		
		if (request.getAttribute("tx_button_remove") != null)
			return FiredButton.TX_BUTTON_REMOVE;
		
		if (request.getAttribute("btnWis") != null)
			return FiredButton.TX_BUTTON_WIS;
		if (request.getAttribute("tx_button_salva") != null)
			return FiredButton.TX_BUTTON_AGGIUNGI;
		
		if (request.getAttribute("btnComuneNascitaEstero") != null)
			return FiredButton.TX_BUTTON_ComuneNascitaEstero;
		if (request.getAttribute("btnAvantiProvinciaNascita") != null)
			return FiredButton.TX_BUTTON_ProvinciaNascita;
		if (request.getAttribute("btnAvantiProvinciaResidenza") != null)
			return FiredButton.TX_BUTTON_ProvinciaResidenza;
		if (request.getAttribute("btnComuneResidenzaEstero") != null)
			return FiredButton.TX_BUTTON_ComuneResidenzaEstero;
		if (request.getAttribute("btnAvantiProvinciaDomicilio") != null)
			return FiredButton.TX_BUTTON_ProvinciaDomicilio;
		if (request.getAttribute("btnComuneDomicilioEstero") != null)
			return FiredButton.TX_BUTTON_ComuneDomicilioEstero;

		
		
		if (request.getAttribute(Field.TX_BUTTON_PROVINCIA_CHANGED.format()) != null
				|| Field.TX_BUTTON_PROVINCIA_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_PROVINCIA_CHANGED;
		
		if (request.getAttribute("tx_button_utente_changed") != null
				|| "tx_button_utente_changed".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_UTENTE_CHANGED;
		
		
		if (request.getAttribute(Field.TX_BUTTON_ENTE_CHANGED.format()) != null
				|| Field.TX_BUTTON_ENTE_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_ENTE_CHANGED;
			
		
		if (request.getAttribute(Field.TX_BUTTON_IMPOSITORE_CHANGED.format()) != null
				|| Field.TX_BUTTON_IMPOSITORE_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_IMPOSITORE_CHANGED;
		
		if (request.getAttribute("tx_button_tipo_servizio_changed") != null
				|| "tx_button_tipo_servizio_changed".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_TIPO_SERVIZIO_CHANGED;
		
		if (request.getAttribute("tx_button_password_autogenerata_changed") != null
				|| "tx_button_password_autogenerata_changed".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_PASSWORD_AUTOGENERATA_CHANGED;
		
		if (request.getAttribute("tx_button_profilo_utente_changed") != null
				|| "tx_button_profilo_utente_changed".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_PROFILO_UTENTE_CHANGED;
		
		if (request.getAttribute("tx_button_scadenza_changed") != null
				|| "tx_button_scadenza_changed".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_SCADENZA_CHANGED;
		
		if (request.getAttribute("tx_button_cerca") != null
				|| request.getAttribute("rowsPerPage") != null
				|| request.getAttribute("pageNumber") != null
				|| request.getAttribute("order") != null)
			return FiredButton.TX_BUTTON_CERCA;

		if (request.getAttribute(Field.TX_BUTTON_SOCIETA_CHANGED.format()) != null
				|| Field.TX_BUTTON_SOCIETA_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_SOCIETA_CHANGED;
		
		
		return FiredButton.TX_BUTTON_NULL;

	}
	
	
	protected void loadDDLStatic(HttpServletRequest request) {
		if (request.getAttribute("listaCartePagamento") == null) {
			try {
				GetStaticDDLListsResponse res = WSCache.commonsServer
						.getStaticDDLLists(new GetStaticDDLListsRequest(), request);
				request
						.setAttribute("listaCartePagamento", res
								.getListaCarte());
				request.setAttribute("listaCanaliPagamento", res
						.getListaCanali());
				request.setAttribute("listaStrumenti", res.getListaStrumenti());
				request.setAttribute("listaTipologieServizio", res
						.getListaTipologieServizio());
				request.setAttribute("listaSocieta", res.getListaSocieta());
				//request.setAttribute("listaGateway", res.getListaGateway());
				request.setAttribute("listaBollettini", res
						.getListaBollettini());
			} catch (FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	protected void replyAttributes(String viewStateId, boolean replace,
			HttpServletRequest request, String... ignoredStrings) {

		ViewStateManager viewStateManager = null;

		if (!viewStateId.equals("")) {
			viewStateManager = new ViewStateManager(request,viewStateId);
			try {
				viewStateManager.replyAttributes(replace, ignoredStrings);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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

	/**
	 * Questo metodo restituisce un oggetto calendar a partire dal valore di un
	 * tag SEDA "date" individuato dal parametro "prefix" (lo stesso che viene
	 * utilizzato nel tag "date" di SEDA)
	 * 
	 * @param request
	 * @param prefix
	 * @return calendar
	 */
	protected Calendar getCalendar(HttpServletRequest request, String prefix) 
	{
		Locale locale = (Locale) request.getSession().getAttribute(
				MAFAttributes.LOCALE);
		Calendar cal = null;
		String anno = null;
		if (prefix != null && prefix != "") {
/*
			String giorno = request.getParameter(prefix + "_day");
			String mese = request.getParameter(prefix + "_month");
			String anno = request.getParameter(prefix + "_year");
*/
			String giorno = (String)request.getAttribute(prefix + "_day");
			String mese = (String)request.getAttribute(prefix + "_month");
			
			Object annoObj = request.getAttribute(prefix + "_year");
			if(annoObj != null)
				anno = annoObj.toString();
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

	
	//DOM
	protected Calendar getCalendarByDate(HttpServletRequest request, java.sql.Date data) 
	{
		if (data==null)
			return null;
		Locale locale = (Locale) request.getSession().getAttribute(
				MAFAttributes.LOCALE);
		Calendar cal = null;
		cal = Calendar.getInstance(locale);
		cal.setTimeInMillis(data.getTime());
		return cal;
	}
	
	//DOM
	protected java.sql.Date getDateByTag(HttpServletRequest request, String prefix) 
	{
		java.sql.Date data = null;
		Calendar cal = getCalendar(request, prefix);
		if (cal==null)
			return null;
		data = new java.sql.Date(cal.getTimeInMillis());
		return data;
	}


	protected Calendar getCalendarByString(HttpServletRequest request, String dataS) {

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

	protected boolean compareDateStringISO(String data1, String prefix, HttpServletRequest request)
	{
		boolean risultato = false;

		String giorno2 = (String)request.getAttribute(prefix + "_day");
		String mese2 = (String)request.getAttribute(prefix + "_month");
		String anno2 = (String)request.getAttribute(prefix + "_year");
		
		if (giorno2.length()==1)
			giorno2 =  "0" + giorno2;

		if (mese2.length()==1)
			mese2 =  "0" + mese2;

		String data2 = anno2 + "-" + mese2 + "-" + giorno2;
	
		if (data1.equals(data2))
			risultato = true;
		return risultato;
		
	}

	//caricamento DDL specifiche
	protected void loadDDLProvinciaBen(HttpServletRequest request, HttpSession session, 
			                           String codSocieta, boolean forceReload) 
	{
		if (forceReload || request.getAttribute("listaProvinceBen") == null) {
			try {
				ListaProvinceBenDDLRequest in = new ListaProvinceBenDDLRequest();
				in.setCodSocieta(codSocieta);
//				DDLResponseType res = WSCache.rivCommonsServer.getListaProvinceBenDDL(in);
				DDLResponseType res = WSCache.commonsServer.getListaProvinceBenDDL(in, request);
				request.setAttribute("listaProvinceBen", res.getXml());
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
		if (forceReload || request.getAttribute("listaBeneficiari") == null) {
			try {
				ListaBeneficiariDDLRequest in = new ListaBeneficiariDDLRequest();
				in.setCodSocieta(codSocieta);
				in.setSiglaProvincia(codProvincia);
				in.setCodUtente(codUtente);

//				DDLResponseType res = WSCache.rivCommonsServer.getListaBeneficiariDDL(in);
				DDLResponseType res = WSCache.commonsServer.getListaBeneficiariDDL(in, request);

				request.setAttribute("listaBeneficiari", res.getXml());
			} 
			catch (FaultType e) {
				e.printStackTrace();
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void loadDDLBeneficiarioBoll(HttpServletRequest request, HttpSession session, 
			  String codSocieta, String codProvincia, String codUtente,boolean forceReload) 
	{
		if (forceReload || request.getAttribute("listaBeneficiariBoll") == null) {
			try {
				ListaBeneficiariBollDDLRequest in = new ListaBeneficiariBollDDLRequest();
				in.setCodSocieta(codSocieta);
				in.setSiglaProvincia(codProvincia);
				in.setCodUtente(codUtente);

//				DDLResponseType res = WSCache.rivCommonsServer.getListaBeneficiariConvDDL(in);
				DDLResponseType res = WSCache.commonsServer.getListaBeneficiariBollDDL(in, request);
				request.setAttribute("listaBeneficiariBoll", res.getXml());
			} 
			catch (FaultType e) {
				e.printStackTrace();
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
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
	
	protected void resetGrid(HttpServletRequest request)
	{
		request.setAttribute("listaAssociazioni", null);
		request.setAttribute("listaAssociazioni.pageInfo", null);

		
		request.setAttribute("pageNumber", null);
		request.setAttribute("rowsPerPage", null);
		request.setAttribute("order", null);

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

	
}
