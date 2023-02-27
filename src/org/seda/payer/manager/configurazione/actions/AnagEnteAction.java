/**
 * 
 */
package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.validator.ValidationMessage;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.validation.ValidationContext;
import com.seda.j2ee5.maf.components.validation.ValidationErrorMap;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnte;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteCancelRequest;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteDetailRequest;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteDetailResponse;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteResponse;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteResponsePageInfo;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteSaveRequest;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteSearchRequest;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteSearchResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaProvinceResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;


public class AnagEnteAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	//private String strcodiceBelfiore;
	private String usernameAutenticazione;	
	
	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
		   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		   pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		   order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		
		   HttpSession session = request.getSession();
			UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
			
		
			/*
			 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
			 */
			replyAttributes(false, request,"pageNumber","rowsPerPage","order");
			
			if( user!=null) 
			   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
			usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
			String firedButton = (String)request.getAttribute("tx_button_nuovo");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			//System.out.println(firedButton+"index");
			if (firedButton!=null||firedButtonReset!=null){
					request.setAttribute("anagente_descrizioneEnte", "");
					request.setAttribute("anagente_codiceEnte", "");
					request.setAttribute("anagente_codiceBelfiore", "");
					request.setAttribute("anagente_descrizioneEnte", "");
					try {
						add(request);
					} catch (ActionException e) {
						e.printStackTrace();
					}
			}
			firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					try {
						index(request);
					} catch (ActionException e) {
						e.printStackTrace();
					}
				}
			}
			
			
	}

		

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		search(request);
		return null;
	}

	public Object search(HttpServletRequest request) throws ActionException {
		try {
			
			
			//String codiceSocieta = (strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:request.getParameter("anagente_companyCode");
			String codiceEnte = ((String)request.getAttribute("anagente_codiceEnte")==null ? "":  (String)request.getAttribute("anagente_codiceEnte"));
			String descrizioneEnte = ((String)request.getAttribute("anagente_descrizioneEnte")==null ? "":  (String)request.getAttribute("anagente_descrizioneEnte"));
			
			String codiceBelfiore = ((String)request.getAttribute("anagente_codiceBelfiore")==null ? "":  (String)request.getAttribute("anagente_codiceBelfiore"));
			
			
			//session.setAttribute("anagente_descrizioneEnte", descrizioneEnte);
			//session.setAttribute("anagente_descrizioneEnte", codiceBelfiore);
			
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			if (firedButtonReset!=null){				
				resetParametri(request);
				codiceEnte="";
				descrizioneEnte="";
				codiceBelfiore="";
				request.setAttribute("tx_data_pag_da_year", "");
				request.setAttribute("tx_data_pag_da_month", "");
				request.setAttribute("tx_data_pag_da_day", "");
				
			}
			if (firedButton!=null||firedButtonN!=null){
					codiceEnte = request.getParameter("");
					descrizioneEnte = request.getParameter("");
					codiceBelfiore = request.getParameter("");
			}
			AnagEnteSearchResponse searchResponse = null;
			if (firedButton!=null)
				 if(firedButton.equals("Indietro"))
					 searchResponse = getAnagEnteSearchResponse (null,null,null, rowsPerPage, pageNumber, order, request);
				 if (firedButton==null)  searchResponse = getAnagEnteSearchResponse (codiceBelfiore,codiceEnte,descrizioneEnte.replace("'", "''"), rowsPerPage, pageNumber, order, request);
			
			//AnagEnteImplementationStub stub = new AnagEnteImplementationStub(new URL(getAnagenteManagerEndpointUrl()), null);
			//AnagEnteSearchResponse searchResponse = getAnagEnteSearchResponse (codiceBelfiore,codiceEnte,descrizioneEnte, rowsPerPage, pageNumber, order);
			//AnagEnteSearchResponse searchResponse = stub.getAnagEntes(searchRequest);
			AnagEnteResponse anagenteServizioResponse = searchResponse.getResponse();
			AnagEnteResponsePageInfo responsePageInfo = anagenteServizioResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("anagente", searchRequest);
			//request.setAttribute("anagente_companyCode", searchRequest.getCompanyCode());
			request.setAttribute("anagente_descrizioneEnte",descrizioneEnte);
			request.setAttribute("anagente_codiceBelfiore", codiceBelfiore);
			
			
			request.setAttribute("anagentes", anagenteServizioResponse.getListXml());
			request.setAttribute("anagentes.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			
			
			/*String codiceBelfiore = (strcodiceBelfiore!=null && strcodiceBelfiore.length()>0)?strcodiceBelfiore:"";
			AnagProvComSearchResponse anagprovcomSearchResponse = getAnagProvComSearchResponse(codiceBelfiore, "","", 0, 0, "");
			request.setAttribute("anagprovcoms", anagprovcomSearchResponse.getResponse().getListXml());*/
			
			loadListaProvince(request);
			request.setAttribute("action", "add");
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
        request.setAttribute("chiaveEnte",request.getParameter("chiaveEnte"));
		return null; 
	}	
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			AnagEnteDetailResponse response = getAnagEnteDetailSearchResponse(request.getParameter("anagente_chiaveEnte"), request);
			//request.setAttribute("anagente", response.getAnagente());
			//request.setAttribute("anagente_companyCode", response.getAnagente().getCompanyCode());
			request.setAttribute("anagente_descrizioneEnte", response.getAnagente().getDescrizioneEnte());
			request.setAttribute("anagente_codiceBelfiore", response.getAnagente().getCodiceBelfiore());
			request.setAttribute("anagente_codiceEnte", response.getAnagente().getCodiceEnte());
			request.setAttribute("anagente_tipoUfficio", response.getAnagente().getTipoUfficio());
			request.setAttribute("anagente_codiceUfficio", response.getAnagente().getCodiceUfficio());
			request.setAttribute("anagente_codiceTipoEnte", response.getAnagente().getCodiceTipoEnte());
			request.setAttribute("anagente_descrizioneUfficio", response.getAnagente().getDescrizioneUfficio());
			request.setAttribute("anagente_codiceRuoliErariali", response.getAnagente().getCodiceRuoliErariali());
			request.setAttribute("anagente_ufficioStatale", response.getAnagente().getUfficioStatale().substring(0));
			String siglaProvincia = response.getAnagente().getSiglaProvincia();
			request.setAttribute("anagente_siglaprovincia", siglaProvincia);
			//request.setAttribute("anagente_dataDecorrenza", response.getAnagente().getDataDecorrenza());
			Date date=response.getAnagente().getDataDecorrenza();
			//System.out.println(date.toString());			
			
			Calendar cal=Calendar.getInstance();
		    cal.setTime(date);
			request.setAttribute("tx_data_pag_da", cal);
			request.setAttribute("tx_data_pag_da_year", cal.get(Calendar.YEAR));
			request.setAttribute("tx_data_pag_da_month", cal.get(Calendar.MONTH) + 1 );
			request.setAttribute("tx_data_pag_da_day", cal.get(Calendar.DAY_OF_MONTH));
						
			//add(request);
			loadBelfiore(siglaProvincia, request);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object saveadd(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.INS_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			FiredButton firedButtonChanged = getFiredButton(request);
			if(firedButtonChanged.equals(FiredButton.TX_BUTTON_CHANGED))
			{
				String siglaProvincia = request.getParameter("anagente_siglaprovincia");
				loadBelfiore(siglaProvincia, request);
			}
			else if (firedButton!=null)
			{
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("anagente_codiceEnte") !=null){
						request.setAttribute("anagente_codiceEnte", null);
						request.setAttribute("anagente_descrizioneEnte", null);
						request.setAttribute("anagente_codiceBelfiore", null);
						index(request);
					}
				}
			} 
			else if(firedButtonReset!=null)
			{
				if(firedButtonReset.equals("Reset")){
					resetParametri(request);
					request.setAttribute("action", "add");
					add(request);
				}
			}
			else save(request);
			
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}

	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.UPDT_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			FiredButton firedButtonChanged = getFiredButton(request);
			if(firedButtonChanged.equals(FiredButton.TX_BUTTON_CHANGED))
			{
				String siglaProvincia = request.getParameter("anagente_siglaprovincia");
				loadBelfiore(siglaProvincia, request);
			}
			else if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("anagente_codiceEnte") !=null){
						request.setAttribute("anagente_codiceEnte", null);
						request.setAttribute("anagente_descrizioneEnte", null);
						request.setAttribute("anagente_codiceBelfiore", null);
						index(request);
					}
				}
			}else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){	
					request.setAttribute("action", "edit");
					edit(request);	
				}
			}else save(request);

		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	private void loadListaProvince(HttpServletRequest request) throws FaultType, RemoteException
	{
		RecuperaProvinceResponse getProvinceRes = WSCache.commonsServer.recuperaProvince(request);
		request.setAttribute("listprovince", getProvinceRes.getListXml());
	}
	
	private void loadBelfiore(String siglaProvincia, HttpServletRequest request) throws FaultType, RemoteException
	{
		loadListaProvince(request);
		if (siglaProvincia != null && siglaProvincia.length() > 0)
		{
			RecuperaBelfioreResponse getBelfioreRes = WSCache.commonsServer.recuperaBelfiore(new RecuperaBelfioreRequest(siglaProvincia), request);
			request.setAttribute("listbelfiore", getBelfioreRes.getListXml());
		}
	}
	
	private void save(HttpServletRequest request) throws ActionException {
		//String companyCode="";
		String codiceBelfiore = "";
		String chiaveEnte;
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		if(codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0)
			chiaveEnte = "XXXXXXXXXX";
		else chiaveEnte = request.getParameter("anagente_chiaveEnte");
		
		
		codiceBelfiore = request.getParameter("anagente_codiceBelfiore");
		String codiceEnte = request.getParameter("anagente_codiceEnte");
		String descrizioneEnte = request.getParameter("anagente_descrizioneEnte");
		String tipoUfficio = request.getParameter("anagente_tipoUfficio");
		String codiceUfficio = request.getParameter("anagente_codiceUfficio");
		String codiceTipoEnte = request.getParameter("anagente_codiceTipoEnte");
		String descrizioneUfficio = request.getParameter("anagente_descrizioneUfficio");
		String codiceRuoliErariali = request.getParameter("anagente_codiceRuoliErariali");
		String ufficioStatale = request.getParameter("anagente_ufficioStatale");
		Calendar cal = (Calendar)request.getAttribute("tx_data_pag_da");
		
		
		Date date=null;
		/*if(cal==null){
			setFormMessage("frmAction", "Impostare la data", request);
			System.out.println("Impostare la data");
			
		}
		else {
			//esito = Messages.SPECIFICARE_DATA_SCADENZA.format();
*/		
		date = cal.getTime();
		try {
			
			//stub = new AnagEnteImplementationStub(new URL(getAnagenteManagerEndpointUrl()), null);
			AnagEnte rangeAbiUtente = new AnagEnte(chiaveEnte,codiceEnte,tipoUfficio,codiceUfficio,codiceTipoEnte,descrizioneEnte,descrizioneUfficio,codiceBelfiore,codiceRuoliErariali,date,ufficioStatale,usernameAutenticazione, null);
			/* we prepare object for save */
			AnagEnteSaveRequest saveRequest = new AnagEnteSaveRequest(rangeAbiUtente,codOp);
			/* we save object */
			saveAnagEnte(saveRequest, request);
			
		} catch (Exception e) {
			try {
				//AnagEnteDetailRequest detailRequest = new AnagEnteDetailRequest(chiaveEnte);
				AnagEnteDetailResponse detailResponse = getEnte(chiaveEnte, request);
				//request.setAttribute("anagente", detailResponse.getAnagente());
				//request.setAttribute("anagente_companyCode", detailResponse.getAnagente().getCompanyCode());
				request.setAttribute("anagente_descrizioneEnte", detailResponse.getAnagente().getDescrizioneEnte());
				request.setAttribute("anagente_userCode", detailResponse.getAnagente().getCodiceBelfiore());
				request.setAttribute("anagente_codiceEnte", detailResponse.getAnagente().getChiaveEnte());
				request.setAttribute("anagente_codiceTipologiaServizio", detailResponse.getAnagente().getTipoUfficio());
				request.setAttribute("anagente_codiceImpostaServizio", detailResponse.getAnagente().getCodiceUfficio());
				request.setAttribute("anagente_urlSistemaEsterno", detailResponse.getAnagente().getCodiceTipoEnte());
				request.setAttribute("anagente_flagAttivazione", detailResponse.getAnagente().getDescrizioneUfficio());
				request.setAttribute("anagente_userCode", detailResponse.getAnagente().getCodiceRuoliErariali());
				request.setAttribute("anagente_codiceTipologiaServizio", detailResponse.getAnagente().getUfficioStatale());
				request.setAttribute("anagente_codiceImpostaServizio", detailResponse.getAnagente().getDataDecorrenza());
			
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String chiaveEnte = request.getParameter("anagente_chiaveEnte");
		//AnagEnteImplementationStub stub = null;
		try {
			request.setAttribute("varname", "anagente");
			//stub = new AnagEnteImplementationStub(new URL(getAnagenteManagerEndpointUrl()), null);
			/* we prepare object for cancel */
			AnagEnteCancelRequest cancelRequest = new AnagEnteCancelRequest(chiaveEnte);
			/* we cancel object */
			cancelAnagEnte(cancelRequest, request);
			request.setAttribute("message", Messages.CANC_OK.format());
		} catch (FaultType f) {
			request.setAttribute("message", Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) {
			request.setAttribute("message", Messages.CANCEL_ERRDIP.format());
		}
		return null;
	}
	
	protected int getDefaultListRows()
	{
		int defaultListRows = 4;
		PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		if(configuration != null)
		{
			String s_defaultListRows = configuration.getProperty(PropertiesPath.defaultListRows.format());
			if (s_defaultListRows != null) defaultListRows = Integer.parseInt(s_defaultListRows);
		}
		return defaultListRows;
	}
	
	private AnagEnteSearchResponse getAnagEnteSearchResponse(
			String codiceBelfiore, String codiceEnte,String descrizioneEnte, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		AnagEnteSearchResponse res = null;
		AnagEnteSearchRequest in = new AnagEnteSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCodiceBelfiore(codiceBelfiore == null ? "" : codiceBelfiore);
		in.setCodiceEnte(codiceEnte == null ? "" : codiceEnte);
		in.setDescrizioneEnte(descrizioneEnte == null ? "" : descrizioneEnte);
		in.setSiglaProvincia("");
		
		res = WSCache.anagEnteServer.getAnagEnti(in, request);
		return res;
	}
	
	private AnagEnteDetailResponse getAnagEnteDetailSearchResponse(String chiaveEnte, HttpServletRequest request) throws FaultType, RemoteException
	{
		 
		AnagEnteDetailResponse res = null;
		AnagEnteDetailRequest in = new AnagEnteDetailRequest(chiaveEnte);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		res = WSCache.anagEnteServer.getAnagEnte(in, request);
		return res;
	}
	
	private void  saveAnagEnte(AnagEnteSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.anagEnteServer.save(saveRequest, request);
		
	}
	
	private void  cancelAnagEnte(AnagEnteCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.anagEnteServer.cancel(cancelRequest, request);
		
	}
	
	/*private AnagProvComSearchResponse getAnagProvComSearchResponse(
			String anagProvComCodeBelfiore,String anagProvComcodiceProvincia, String anagProvComcodiceComune, int rowsPerPage, int pageNumber, String order) throws FaultType, RemoteException
	{
		AnagProvComSearchResponse res = null;
		AnagProvComSearchRequest in = new AnagProvComSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCodiceBelfiore(anagProvComCodeBelfiore == null ? "" : anagProvComCodeBelfiore);
		in.setCodiceProvincia(anagProvComcodiceProvincia == null ? "" :anagProvComcodiceProvincia );
		in.setCodiceComune(anagProvComcodiceComune == null ? "" : anagProvComcodiceComune);	
		res = WSCache.anagProvComServer.getAnagProvComs(in);
		return res;
	}*/
	
	private AnagEnteDetailResponse getEnte(String chiaveEnte, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
	{
		AnagEnteDetailRequest detailRequest = new AnagEnteDetailRequest();
		detailRequest.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		return WSCache.anagEnteServer.getAnagEnte(detailRequest, request);
	}
	
	@SuppressWarnings("unchecked")
	protected void resetParametri(HttpServletRequest request) {
		Enumeration e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			
			p = (String) e.nextElement();
			request.setAttribute(p, "");
		}
		
		//ROMERIO
		request.setAttribute("tx_data_pag_da", null);
		
		return;
	}
	
	protected Calendar parseDate(String data, String formato)
	{
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
	
	public void setFormMessage(String formName, String message, HttpServletRequest request) {
		if (ValidationContext.getInstance().getValidationMessage() != null) 
		{
			ValidationErrorMap vem=new ValidationErrorMap();
			ArrayList<ValidationMessage> messages=new ArrayList<ValidationMessage>();
			ValidationMessage validationMessage=new ValidationMessage("", "", message);
			messages.add(validationMessage);
			
			vem.setForm(formName);
			vem.setMessages(messages);
			
			request.setAttribute(ValidationContext.getInstance().getValidationMessage(), vem);
			
			messages=null;
			validationMessage=null;
			vem=null;
		}
	}
}