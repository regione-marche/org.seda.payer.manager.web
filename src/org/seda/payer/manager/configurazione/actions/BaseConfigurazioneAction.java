package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.taglibs.ViewStateManager;
import com.seda.j2ee5.maf.util.MAFAttributes;

import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ListaBeneficiariByImpDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.ListaProvinceByConvDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.ListaBeneficiariDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.ListaBeneficiariByConvDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.ListaProvinceBenDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaProvinceResponse;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;

import com.seda.payer.pgec.webservice.commons.dati.ListaGatewayCanDDLRequest;

import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.tag.core.DdlOption;
import com.sun.xml.bind.v2.runtime.reflect.ListIterator;

public class BaseConfigurazioneAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	protected NumberFormat inf = NumberFormat.getIntegerInstance();
	
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
    	"btnComuneDomicilioEstero"};
	
	
	public void setProfile(HttpServletRequest request)  
	{
		super.setProfile(request);
	}

	public Object service(HttpServletRequest request, String vista) throws ActionException {
		super.service(request);

		setProfile(request);
		
		firedButton = getFiredButton(request);
        
		if (request.getParameter("start")==null&&(request.getAttribute("start")==null||request.getAttribute("start").equals("")))
		{
			replyAttributes(vista, false, request, labelBottoni);	
		}
		
//		tx_SalvaStato(request);
		salvaStato(request);
		
		request.setAttribute("start", "");
		
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
		if (prefix != null && prefix != "") {
/*
			String giorno = request.getParameter(prefix + "_day");
			String mese = request.getParameter(prefix + "_month");
			String anno = request.getParameter(prefix + "_year");
*/
			String giorno = (String)request.getAttribute(prefix + "_day");
			String mese = (String)request.getAttribute(prefix + "_month");
			String anno = (String)request.getAttribute(prefix + "_year");

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

	protected void loadDDLProvince(HttpServletRequest request)
	{
	    if (request.getAttribute("listProvince")!=null)
	    	return;
		RecuperaProvinceResponse getProvinceRes = new RecuperaProvinceResponse();
		try {
			getProvinceRes = WSCache.commonsServer.recuperaProvince(request);
		} catch (com.seda.payer.pgec.webservice.commons.srv.FaultType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("listProvince", getProvinceRes.getListXml());
	}

	protected void loadDDLComuni(String siglaProvincia, HttpServletRequest request) {
		if (siglaProvincia != null && siglaProvincia.length() > 0)
		{
			RecuperaBelfioreResponse getBelfioreRes = new RecuperaBelfioreResponse();
			try {
				getBelfioreRes = WSCache.commonsServer.recuperaBelfiore(new RecuperaBelfioreRequest(siglaProvincia), request);
			} catch (com.seda.payer.pgec.webservice.commons.srv.FaultType e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("listbelfiore", getBelfioreRes.getListXml());
		}
	}
	
	
	protected void loadDDLTipologiaStruttura(HttpServletRequest request) 
	{
		
		    if (request.getAttribute("listStrutture")!=null)
		    	return;
			RecuperaListaTipologiaStrutturaRicettivaResponse getStruttureRes = new RecuperaListaTipologiaStrutturaRicettivaResponse();
			try {
				RecuperaListaTipologiaStrutturaRicettivaRequest ric = new RecuperaListaTipologiaStrutturaRicettivaRequest();
				ric.setDescrizioneTipologiaStruttura("");
				getStruttureRes = WSCache.impostaSoggiornoConfigServer.ricercaTipologiaStrutture(ric, request);
			} catch (com.seda.payer.pgec.webservice.commons.srv.FaultType e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("listStrutture", getStruttureRes.getListXml());
	}

//MC
	protected void loadDDLDataValiditaAnno(HttpServletRequest request) 
	{
		
		    if (request.getAttribute("listDataValiditaAnno")!=null)
		    	return;

//		    String[] annoList = {"2010","2011","2012","2013",""};
		    String annoDa = (String)request.getSession().getServletContext().getAttribute(ManagerKeys.DDL_DATE_ANNO_DA);
		    String annoA  = (String)request.getSession().getServletContext().getAttribute(ManagerKeys.DDL_DATE_ANNO_A);
		    List<DdlOption> annoList  = new ArrayList<DdlOption>();

		    int iannoDa = Integer.parseInt(annoDa);		    
		    int iannoA  = Integer.parseInt(annoA);
//		    annoList.add("");
		    DdlOption option = new DdlOption();
	    	option.setSValue("");
	    	option.setSText("");
	    	annoList.add(option);
		    for(;iannoDa <= iannoA; iannoDa++){
//		    	annoList.add(String.valueOf(iannoDa));
		    	option = new DdlOption();
		    	option.setSValue(String.valueOf(iannoDa));
		    	option.setSText(String.valueOf(iannoDa));
		    	annoList.add(option); 	
		    }
		    request.setAttribute("listDataValiditaAnno", annoList);
		    

	}	
//MC
	
	protected void resetGrid(HttpServletRequest request)
	{
		request.setAttribute("listaTariffe", null);
		request.setAttribute("listaTariffe.pageInfo", null);

		
		request.setAttribute("pageNumber", null);
		request.setAttribute("rowsPerPage", null);
		request.setAttribute("order", null);

	}
	
	protected void loadDDLStatic(HttpServletRequest request)
	{
		loadDDLTipologiaStruttura(request);	
		loadDDLProvince(request);
		loadDDLDataValiditaAnno(request);
	}

	/*
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
		sessione.setAttribute("listaUtentiEntiAll", null);		
		
	}
*/
}
