package org.seda.payer.manager.views;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.taglibs.ViewStateManager;
import com.seda.payer.commons.utility.StringUtility;
import com.seda.payer.pgec.webservice.commons.dati.GetListaProvinceXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaSocietaXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipologiaServizioXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiGenericiXml_DDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetUtentiDDLRequest;
/**
 * @author marco.montisano
 */
public abstract class BaseView implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String Name = "nd";
	public static final String StartScope = "start";
	public static final String SearchScope = "search";
	public static final String IndexScope = "index";
	public static final String AddScope = "add";
	public static final String BaseListScope = "baselist";
	public static final String EditScope = "edit";
	public static final String RichiestaCancScope = "richiestacanc";
	public static final String CancelScope = "cancel";
	public static final String SaveAddScope = "saveadd";
	public static final String SaveEditScope = "saveedit";
	public static final String SaveScope = "save";
	public static final String ResetButton = "Reset";
	public static final String BackButton = "Indietro";
	public static final String NewButton = "Nuovo";
	public static final String DefaulDatePattern = "yyyy-MM-dd";
	public static enum BaseListName {
		listaSocieta, listaProvince, listaUtenti, listaEnti, listaEntiGenerici, listaUtentiEnti, listaTipologiaServizi
	}
	public static enum Profile {
		AMMI, AMSO, AMUT, AMEN, AMNULL
	}
	protected HttpServletRequest request;
	protected int rowsPerPage;
	protected int pageNumber;
	protected String order;
	protected String scope;
	protected String firedButton;
	protected String firedButtonBack;
	protected String firedButtonReset;
	protected String firedButtonCerca;
	protected String codiceSocieta;
	protected String codiceOperatore;
	protected String codiceProvincia;
	protected String codiceUtente;
	protected String codiceEnte;  //SVILUPPO_001
	protected String typeRequest;
	protected String success;
	protected String error;
	protected String message;
	protected String actionName;
	protected String datePattern;
	protected String listaSocieta;
	protected String listaProvince;
	protected String listaUtenteEnte;
	protected String listaUtente;
	protected String listaTipologieServizio;
	private Calendar dataDa;
	private Calendar dataA;
	protected com.seda.data.spi.PageInfo pageInfo;

	public BaseView(ServletContext context, HttpServletRequest request) {
		this.request = request;
		this.rowsPerPage = (request.getParameter("rowsPerPage") == null) ? getDefaultListRows(context) : Integer.parseInt(request.getParameter("rowsPerPage"));
		this.pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		this.order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		this.firedButton = (String) request.getAttribute("tx_button_nuovo");
		this.firedButtonBack = (String) request.getAttribute("tx_button_indietro");
		this.firedButtonReset = (String) request.getAttribute("tx_button_reset");
		this.firedButtonCerca = (String) request.getAttribute("tx_button_cerca");
		this.datePattern = (String) request.getAttribute("datePattern");
		this.dataDa = (Calendar) request.getAttribute("dataDa"); //formatDate(tmp_dataDa, "yyyy-MM-dd");
		this.dataA = (Calendar) request.getAttribute("dataA"); //formatDate(tmp_dataA, "yyyy-MM-dd");
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute(SignOnKeys.USER_BEAN);
		replyAttributes(false, request, "pageNumber", "rowsPerPage", "order");
		if (user != null) {
			this.codiceSocieta = (user.getProfile().equals(Profile.AMMI.toString()) 
					? (String) request.getAttribute("codiceSocieta") : user.getCodiceSocieta());
			request.setAttribute("codiceSocieta", this.codiceSocieta);
			request.setAttribute("userAppl_usernameAutenticazione", user.getUserName().trim());
			if(user.getProfile().equals(Profile.AMEN.toString())){
				this.codiceSocieta = user.getCodiceSocieta();
				this.codiceUtente = user.getCodiceUtente();
				this.codiceEnte= user.getChiaveEnteConsorzio();
				setValue("elaborazioni_codiceSocieta", codiceSocieta);
				setValue("elaborazioni_searchuserCode", codiceSocieta+codiceUtente);
				setValue("elaborazioni_searchchiaveEnte", codiceSocieta+codiceUtente+codiceEnte);
			}
			if(user.getProfile().equals(Profile.AMUT.toString())){
				this.codiceSocieta = user.getCodiceSocieta();
				this.codiceUtente = user.getCodiceUtente();
				setValue("elaborazioni_codiceSocieta", codiceSocieta);
				setValue("elaborazioni_searchuserCode", codiceSocieta+codiceUtente);
			}
			
		} else this.codiceSocieta = "XXXXX";
		this.codiceOperatore = (String) request.getAttribute("userAppl_usernameAutenticazione");
		// we enable company DDL if user profile is Profile.AMMI
		request.setAttribute("enableListaSocieta", !getProfile().equals(Profile.AMMI));
		// we disable region DDL if user profile is Profile.AMEN
		request.setAttribute("enableListaProvince", !getProfile().equals(Profile.AMMI) && !getProfile().equals(Profile.AMSO));
		// we disable users if user profile is Profile.AMUT without option multiuser
		request.setAttribute("enableListaUtenti", (getProfile().equals(Profile.AMUT) || getProfile().equals(Profile.AMEN)) && !getUser().getMultiUtenteEnabled());
		// we disable factory's if user profile is Profile.AMEN without option multiuser
		request.setAttribute("enableListaEnti", getProfile().equals(Profile.AMEN) && !getUser().getMultiUtenteEnabled());

//		if (getSession().getAttribute("listaSocieta") != null) {
//			request.setAttribute("listaSocieta", (String) session.getAttribute("listaSocieta"));
//			request.setAttribute("listaTipologieServizio", (String) session.getAttribute("listaTipologieServizio"));
//		} else {
//			try {
//				GetStaticDDLListsResponse res = WSCache.commonsServer.getStaticDDLLists(new GetStaticDDLListsRequest());
//				request.setAttribute("listaSocieta", res.getListaSocieta());
//				request.setAttribute("listaTipologieServizio", res.getListaTipologieServizio());
//				session.setAttribute("listaSocieta", res.getListaSocieta());
//				session.setAttribute("listaTipologieServizio", res.getListaTipologieServizio());
//			} catch (FaultType e) { e.printStackTrace();
//			} catch (RemoteException e) { e.printStackTrace(); }
//		}
	}

	public BaseView populate(HttpServletRequest request, String scope) {
		if (RichiestaCancScope.equals(scope)) {
			this.request = request;
		} else if (EditScope.equals(scope)) {
			this.request = request;
			this.dataDa = (Calendar) request.getAttribute("dataDa");
			this.dataA = (Calendar) request.getAttribute("dataA");
//			this.dataDa = formatDate(tmp_dataDa, "yyyy-MM-dd");
//			this.dataA = formatDate(tmp_dataA, "yyyy-MM-dd");
		} else if (CancelScope.equals(scope)) {
			this.request = request;
		} else if (SaveAddScope.equals(scope) || SaveEditScope.equals(scope)) {
			this.typeRequest = request.getParameter("codop");
		} else if (SaveScope.equals(scope)) {
			this.typeRequest = request.getParameter("codop");
			this.dataDa = (Calendar) request.getAttribute("dataDa"); //formatDate(tmp_dataDa, "yyyy-MM-dd");
			this.dataA = (Calendar) request.getAttribute("dataA"); //formatDate(tmp_dataA, "yyyy-MM-dd");
		} else {
			this.request = request;
			this.firedButton = (String) request.getAttribute("tx_button_indietro");
			this.firedButtonReset = (String) request.getAttribute("tx_button_reset");
		}
		return this;
	}

	public HttpSession getSession() {
		return request.getSession(false);
	}

	public UserBean getUser() {
		return (UserBean) getSession().getAttribute(SignOnKeys.USER_BEAN);
	}
	
	public Profile getProfile() {
		String profile = getUser().getUserProfile();
		if (profile.equals("AMMI"))
			return Profile.AMMI;
		if (profile.equals("AMSO"))
			return Profile.AMSO;
		if (profile.equals("AMUT"))
			return Profile.AMUT;
		if (profile.equals("AMEN"))
			return Profile.AMEN;

		return Profile.AMNULL;
	}

	public String getCodiceSocieta() {
		return codiceSocieta != null ? codiceSocieta : "";
	}

	public void setCodiceSocieta(String codiceSocieta) {
		this.codiceSocieta = codiceSocieta;
	}

	public String getCodiceOperatore() {
		return codiceOperatore;
	}

	public void setCodiceOperatore(String codiceOperatore) {
		this.codiceOperatore = codiceOperatore;
	}

	public String getFiredButton() {
		return firedButton;
	}

	public void setFiredButton(String firedButton) {
		this.firedButton = firedButton;
	}

	public String getFiredButtonReset() {
		return firedButtonReset;
	}

	public void setFiredButtonReset(String firedButtonReset) {
		this.firedButtonReset = firedButtonReset;
	}

	public String getFiredButtonCerca() {
		return firedButtonCerca;
	}

	public void setFiredButtonCerca(String firedButtonCerca) {
		this.firedButtonCerca = firedButtonCerca;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setPageInfo(com.seda.data.spi.PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public com.seda.data.spi.PageInfo getPageInfo() {
		return pageInfo;
	}

	public int getRowsPerPage() {
		int rpg = rowsPerPage < 0 ? 0 : rowsPerPage;
		return rpg;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public int getPageNumber() {
		int pg = pageNumber < 0 ? 0 : pageNumber; 
		return pg;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getOrder() {
		String o = order == null ? "" : order;
		return o;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setScope(String scope) {
		this.scope = scope;
		request.setAttribute("action", scope);
	}

	public String getScope() {
		return scope;
	}

	public String getFiredButtonBack() {
		return firedButtonBack;
	}

	public void setFiredButtonBack(String firedButtonBack) {
		this.firedButtonBack = firedButtonBack;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
		request.setAttribute("done", success);
		setError(false);
	}

	public String getError() {
		return error;
	}

	public void setError(boolean b) {
		this.error = b ? "error" : null;
		request.setAttribute("error", this.error);
	}

	public void setValidation(String success) {
		request.setAttribute("validation", success);
	}

	public String getTypeRequest() {
		return typeRequest;
	}

	public void setTypeRequest(String typeRequest) {
		this.typeRequest = typeRequest;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		request.setAttribute("message", message);
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
		request.setAttribute("varname", actionName);
	}

	public String getCodiceProvincia() {
		return codiceProvincia != null ? codiceProvincia : "";
	}

	public void setCodiceProvincia(String codiceProvincia) {
		this.codiceProvincia = codiceProvincia;
	}

	public String getCodiceUtente() {
		return codiceUtente != null ? codiceUtente : "";
	}

	public void setCodiceUtente(String codiceUtente) {
		this.codiceUtente = codiceUtente;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getParam(String name) {
		return request.getParameter(name);
	}

	public String getValue(String name) {
		return (String) request.getAttribute(name) == null ? "" : (String) request.getAttribute(name);
	}

	public void setValue(String name, Serializable value) {
		request.setAttribute(name, value);
	}

	public void setCachedValue(String name, Serializable value) {
		getSession().setAttribute(name, value);
	}
	
	public Calendar getDataDa() throws Exception {
		return this.dataDa;
	}
	
	public Calendar getDataA() throws Exception {
		return this.dataA;
	}

	public void setDataDa(Calendar dataDa) {
		this.dataDa = dataDa;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.sql.Date(dataDa.getTimeInMillis()));
		setValue("dataDa", calendar);
		
		setValue("dataDa_day", calendar.get(Calendar.DAY_OF_MONTH));
		setValue("dataDa_month", calendar.get(Calendar.MONTH)+1);
		setValue("dataDa_year", calendar.get(Calendar.YEAR));
	}

	public void setDataA(Calendar dataA) {
		this.dataA = dataA;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.sql.Date(dataA.getTimeInMillis()));
		setValue("dataA", calendar);
		
		setValue("dataA_day", calendar.get(Calendar.DAY_OF_MONTH));
		setValue("dataA_month", calendar.get(Calendar.MONTH)+1);
		setValue("dataA_year", calendar.get(Calendar.YEAR));
	}

	public boolean isBaseListChanged() {
		return (request.getAttribute("tx_button_societa_changed") != null
					|| request.getAttribute("tx_button_utente_changed") != null
					|| (request.getAttribute("fired_button_hidden") != null
							&&  (((String) request.getAttribute("fired_button_hidden")).equals("tx_button_societa_changed")
									|| ((String) request.getAttribute("fired_button_hidden")).equals("tx_button_utente_changed"))));
	}

	public int getDefaultListRows(ServletContext context) {
		int defaultListRows = 4;
		PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		if (configuration != null) {
			String s_defaultListRows = configuration.getProperty(PropertiesPath.defaultListRows.format());
			if (s_defaultListRows != null) defaultListRows = Integer.parseInt(s_defaultListRows);
		}
		return defaultListRows;
	}

	@SuppressWarnings("unchecked")
	public void reset() {
		Enumeration e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			p = (String) e.nextElement();
			request.setAttribute(p, "");
		}
		this.dataDa = null;
		this.dataA = null;
		this.setValue("dataDa", this.dataDa);
		this.setValue("dataA", this.dataA);
	}

	protected String replyAttributes(boolean replace, HttpServletRequest request, String... ignoredStrings) {
		/* we manage view-state for all data-grid actions without parameters */
		ViewStateManager viewStateManager = null;
		String viewStateId = request.getAttribute("vista") == null ? "" : request.getAttribute("vista").toString();
		if (!viewStateId.equals("")) {
			viewStateManager = new ViewStateManager(request,viewStateId);
			try { viewStateManager.replyAttributes(replace, ignoredStrings);
			} catch (Exception e) { e.printStackTrace(); }
		}
		return viewStateId;
	}

	protected String formatDate(Calendar data, String formato) {
		String sdata = "";
		if (data != null) {
		    SimpleDateFormat sdf = new SimpleDateFormat(StringUtility.isEmpty(formato) ? DefaulDatePattern : formato);
		    sdata = sdf.format(data.getTime());
		}
	    return sdata;
	}

	protected Calendar parseDate(String data, String formato) {
		Calendar cal = new GregorianCalendar(); 
		java.util.Date sdate = null;
		if (data != null) {
		    SimpleDateFormat sdf = new SimpleDateFormat(formato);
		    try {
				sdate = sdf.parse(data);
			} catch (ParseException e) {
				sdate = new java.util.Date();
			}
		    cal.setTime(sdate);
		}
	    return cal;
	}

	protected void splitDate(HttpServletRequest request, String paramName, String format) {
		String date = request.getParameter(paramName);
		Calendar cal = parseDate(date, format); 
		request.setAttribute(paramName+"_year", cal.get(Calendar.YEAR));
		request.setAttribute(paramName+"_month", cal.get(Calendar.MONTH));
		request.setAttribute(paramName+"_day", cal.get(Calendar.DAY_OF_MONTH));
	}

	public void setBaseList(BaseListName baseListName, boolean cache) throws Exception
	{
		setBaseList(baseListName, cache, false, request);
	}
	
	public void setBaseList(BaseListName baseListName, boolean cache, boolean bCascade, HttpServletRequest request) throws Exception {
		switch (baseListName) {
			case listaSocieta:
				setValue(baseListName.toString(), 
						!cache ? WSCache.commonsServer.getListaSocietaXml_DDL(new GetListaSocietaXml_DDLRequestType(), request).getXml()
							   : (getSession().getAttribute(baseListName.toString()) != null 
									   ? (String) getSession().getAttribute(baseListName.toString())
									   : WSCache.commonsServer.getListaSocietaXml_DDL(new GetListaSocietaXml_DDLRequestType(), request).getXml()));
				setCachedValue(baseListName.toString(), cache ? getValue(baseListName.toString()) : null);
				break;
			case listaProvince:
				if (!bCascade || (getCodiceSocieta() != null && !getCodiceSocieta().equals("")))
				{
					String xml = WSCache.commonsServer.getListaProvinceXml_DDL(new GetListaProvinceXml_DDLRequestType(this.getCodiceSocieta()), request).getXml();
					setValue(baseListName.toString(), 
							!cache ? xml
								   : (getSession().getAttribute(baseListName.toString()) != null 
										   ? (String) getSession().getAttribute(baseListName.toString())
										   : xml));
					setCachedValue(baseListName.toString(), cache ? getValue(baseListName.toString()) : null);
					//inizio LP PG21XX04 Leak
					//WebRowSet wrs = Convert.stringToWebRowSet(xml);
					WebRowSet wrs = null;
					try {
						wrs = Convert.stringToWebRowSet(xml);
					//fine LP PG21XX04 Leak
					boolean provinciaPresente=false;
					if(getCodiceProvincia()!=null && !getCodiceProvincia().equals("")){
						while(wrs.next()){
							if(wrs.getString("APC_CAPCSIGL").equals(getCodiceProvincia())){
								provinciaPresente=true;
							}
						}
						
						if(!provinciaPresente){
							setCodiceSocieta(null);
							setValue("elaborazioni_siglaProvincia", null);
						}
					}
					//inizio LP PG21XX04 Leak
					} catch (Exception e) {
						throw new Exception(e);
					} finally {
						try {
							if(wrs != null) {
								wrs.close();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					//fine LP PG21XX04 Leak
				}
				else
				{
					setValue(baseListName.toString(), null);
					setCachedValue(baseListName.toString(), null);
				}
				break;
			case listaUtenti:
				if (!bCascade || (getCodiceSocieta() != null && !getCodiceSocieta().equals("")))
				{
					setValue(baseListName.toString(), 
							!cache ? WSCache.commonsServer.getUtentiDDL(new GetUtentiDDLRequest(getCodiceSocieta(), getCodiceProvincia()), request).getListXml()
								   : (getSession().getAttribute(baseListName.toString()) != null 
										   ? (String) getSession().getAttribute(baseListName.toString())
										   : WSCache.commonsServer.getUtentiDDL(new GetUtentiDDLRequest(getCodiceSocieta(), getCodiceProvincia()), request).getListXml()));
					setCachedValue(baseListName.toString(), cache ? getValue(baseListName.toString()) : null);
				}
				else
				{
					setValue(baseListName.toString(), null);
					setCachedValue(baseListName.toString(), null);
				}
				break;
			case listaEnti:
				if (!bCascade || (getCodiceSocieta() != null && !getCodiceSocieta().equals("") && 
						codiceUtente != null && !codiceUtente.equals("")))
				{
					String codiceUtente = getCodiceUtente();
					if (codiceUtente != null && (codiceUtente.length()>0))
						codiceUtente = codiceUtente.substring(5);
					setValue(baseListName.toString(), 
	//						!cache ? WSCache.commonsServer.getEntiDDL(new GetEntiDDLRequest(getCodiceSocieta(), getCodiceProvincia(), getCodiceUtente())).getListXml()
							!cache ? WSCache.commonsServer.getListaUtentiEntiXml_DDL(new GetListaUtentiEntiXml_DDLRequestType(getCodiceSocieta(), getCodiceProvincia(), null, codiceUtente), request).getXml()
								   : (getSession().getAttribute(baseListName.toString()) != null 
										   ? (String) getSession().getAttribute(baseListName.toString())
	//									   : WSCache.commonsServer.getEntiDDL(new GetEntiDDLRequest(getCodiceSocieta(), getCodiceProvincia(), getCodiceUtente())).getListXml()));
										   : WSCache.commonsServer.getListaUtentiEntiXml_DDL(new GetListaUtentiEntiXml_DDLRequestType(getCodiceSocieta(), getCodiceProvincia(), null, codiceUtente), request).getXml()));											   
					setCachedValue(baseListName.toString(), cache ? getValue(baseListName.toString()) : null);
				}
				else
				{
					setValue(baseListName.toString(), null);
					setCachedValue(baseListName.toString(), null);
				}
				break;
			case listaEntiGenerici:
				if (!bCascade || (getCodiceSocieta() != null && !getCodiceSocieta().equals("") && 
						codiceUtente != null && !codiceUtente.equals("")))
				{
					String codiceUtente = getCodiceUtente();
					if (codiceUtente != null && (codiceUtente.length()>0))
						codiceUtente = codiceUtente.substring(5);
					setValue(baseListName.toString(), 
							!cache ? WSCache.commonsServer.getListaUtentiEntiGenericiXml_DDL(new GetListaUtentiEntiGenericiXml_DDLRequest(getCodiceSocieta(), getCodiceProvincia(), codiceUtente), request).getXml()
								   : (getSession().getAttribute(baseListName.toString()) != null 
										   ? (String) getSession().getAttribute(baseListName.toString())
										   : WSCache.commonsServer.getListaUtentiEntiGenericiXml_DDL(new GetListaUtentiEntiGenericiXml_DDLRequest(getCodiceSocieta(), getCodiceProvincia(), codiceUtente), request).getXml()));											   
					setCachedValue(baseListName.toString(), cache ? getValue(baseListName.toString()) : null);
				}
				else
				{
					setValue(baseListName.toString(), null);
					setCachedValue(baseListName.toString(), null);
				}
				break;
			case listaTipologiaServizi:
				if (!bCascade || (getCodiceSocieta() != null && !getCodiceSocieta().equals("")))
				{
					String codSoc = null;
					if ((getCodiceSocieta()==null || !(getCodiceSocieta().length()>0)) && (getCodiceUtente()!=null && !(getCodiceUtente().length()==0)))
						codSoc = getCodiceUtente().substring(0,5);
					else
						codSoc = getCodiceSocieta();
					setValue(baseListName.toString(), 
	//						!cache ? WSCache.commonsServer.getListaTipologiaServizioXml_DDL(new GetListaTipologiaServizioXml_DDLRequestType(getCodiceSocieta())).getXml()
							!cache ? WSCache.commonsServer.getListaTipologiaServizioXml_DDL(new GetListaTipologiaServizioXml_DDLRequestType(codSoc), request).getXml()
								   : (getSession().getAttribute(baseListName.toString()) != null 
										   ? (String) getSession().getAttribute(baseListName.toString())
	//									   : WSCache.commonsServer.getListaTipologiaServizioXml_DDL(new GetListaTipologiaServizioXml_DDLRequestType(getCodiceSocieta())).getXml()));
												   : WSCache.commonsServer.getListaTipologiaServizioXml_DDL(new GetListaTipologiaServizioXml_DDLRequestType(codSoc), request).getXml()));
					setCachedValue(baseListName.toString(), cache ? getValue(baseListName.toString()) : null);
				}
				else
				{
					setValue(baseListName.toString(), null);
					setCachedValue(baseListName.toString(), null);
				}
				break;
			default:
				break;
		}
	}
	
}