package org.seda.payer.manager.configurazione.actions;


import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;


import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvCom;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComCancelRequest;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComDetailRequest;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComDetailResponse;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComResponse;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComResponsePageInfo;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComSaveRequest;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComSearchRequest;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComSearchResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;





public class AnagProvComAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String usernameAutenticazione = null;
	
	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
				   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		
		//FiredButton firedButton = getFiredButton(request);
		/*
		 * Chiamo il metodo per i settaggi relativi al profilo utente
		 */
		//if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) setProfile(request);
		
		if( user!=null) 
		   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
		usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		
		String firedButton = (String)request.getAttribute("tx_button_indietro");
		//System.out.println(firedButton+"index");
		if (firedButton!=null){
			if(firedButton.equals("Indietro")){
				if(request.getAttribute("anagprovcom_codiceBelfiore") !=null){
				request.setAttribute("anagprovcom_codiceBelfiore", "");
				request.setAttribute("anagprovcom_codiceProvincia", "");
				request.setAttribute("anagprovcom_codiceComune", "");
				}
			}
		}
		String firedButtonNew = (String)request.getAttribute("tx_button_nuovo");
		//System.out.println(firedButton+"index");
		if (firedButtonNew!=null){
				request.setAttribute("anagprovcom_codiceBelfiore", "");
				request.setAttribute("anagprovcom_codiceProvincia", "");
				request.setAttribute("anagprovcom_codiceComune", "");
		}
		String firedButtonReset = (String)request.getAttribute("tx_button_reset");
		//System.out.println(firedButtonReset);
		if (firedButtonReset!=null){
			if(firedButtonReset.equals("Reset")){
				if(request.getAttribute("anagprovcom_codiceBelfiore") !=null){
				request.setAttribute("anagprovcom_codiceBelfiore", "");
				request.setAttribute("anagprovcom_codiceProvincia", "");
				request.setAttribute("anagprovcom_codiceComune", "");
				}
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
		//saveadd(request);
		search(request);
		
		return null;
	}
	
	public Object search(HttpServletRequest request) throws ActionException {
		try {
			String anagProvComCodeBelfiore = null;
			String anagProvComcodiceProvincia = null;
			String anagProvComcodiceComune = null;
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonR = (String)request.getAttribute("tx_button_reset");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			//System.out.println(firedButton+"search");
			if (firedButton!=null||firedButtonR!=null||firedButtonN!=null){
					anagProvComCodeBelfiore = request.getParameter("");
					anagProvComcodiceProvincia = request.getParameter("");
					anagProvComcodiceComune = request.getParameter("");
				}
				/*if(firedButton.equals("Indietro")||firedButtonR.equals("Reset")){
					anagProvComCodeBelfiore = request.getParameter("");
					anagProvComcodiceProvincia = request.getParameter("");
					anagProvComcodiceComune = request.getParameter("");
				}*/
			else{
				anagProvComCodeBelfiore = ((String)request.getAttribute("anagprovcom_codiceBelfiore")==null ? "":  (String)request.getAttribute("anagprovcom_codiceBelfiore"));
				anagProvComcodiceProvincia = ((String)request.getAttribute("anagprovcom_codiceProvincia")==null ? "":  (String)request.getAttribute("anagprovcom_codiceProvincia"));
				anagProvComcodiceComune = ((String)request.getAttribute("anagprovcom_codiceComune")==null ? "":  (String)request.getAttribute("anagprovcom_codiceComune"));
			}
			AnagProvComSearchResponse searchResponse = null;
			
			if (firedButton!=null||firedButtonR!=null)
			 //if(firedButton.equals("Indietro")||firedButtonR.equals("Reset"))
				 searchResponse = getAnagProvComSearchResponse(null,null,null, rowsPerPage, pageNumber, order, request);
			 /*if (firedButton==null)*/ else  searchResponse = getAnagProvComSearchResponse(anagProvComCodeBelfiore,anagProvComcodiceProvincia,anagProvComcodiceComune, rowsPerPage, pageNumber, order, request);
			
			AnagProvComResponse anagProvComResponse = searchResponse.getResponse();
			AnagProvComResponsePageInfo responsePageInfo = anagProvComResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("company", searchRequest);
			request.setAttribute("anagprovcom_codiceBelfiore", anagProvComCodeBelfiore);
			//request.setAttribute("company_companyDescription", companyDescription);
			
			request.setAttribute("anagprovcoms", anagProvComResponse.getListXml());
			request.setAttribute("anagprovcoms.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public Object add(HttpServletRequest request) throws ActionException {
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		  request.setAttribute("codiceBelfiore",request.getParameter("anagprovcom_codiceBelfiore"));		  
		return null; 
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			AnagProvComDetailResponse response = getAnagProvComDetailSearchResponse(request.getParameter("anagprovcom_codiceBelfiore"), request);
			
			request.setAttribute("anagprovcom_codiceBelfiore", response.getAnagprovcom().getCodiceBelfiore());
			request.setAttribute("anagprovcom_codiceProvincia", response.getAnagprovcom().getCodiceProvincia());
			request.setAttribute("anagprovcom_codiceComune", response.getAnagprovcom().getCodiceComune());
			request.setAttribute("anagprovcom_descrizioneComune", response.getAnagprovcom().getDescrizioneComune());
			request.setAttribute("anagprovcom_cap", response.getAnagprovcom().getCap());
			request.setAttribute("anagprovcom_siglaProvincia", response.getAnagprovcom().getSiglaProvincia());
			request.setAttribute("anagprovcom_descrizioneProvincia", response.getAnagprovcom().getDescrizioneProvincia());
			request.setAttribute("anagprovcom_descrizioneRegione", response.getAnagprovcom().getDescrizioneRegione());
			request.setAttribute("anagprovcom_codiceCatastale", response.getAnagprovcom().getCodiceCatastale());
		    request.setAttribute("anagprovcom_descrizioneProvinciaDE",response.getAnagprovcom().getDescrizioneProvinciaDE());
		    request.setAttribute("anagprovcom_descrizioneComuneDE",response.getAnagprovcom().getDescrizioneComuneDE());
		    request.setAttribute("anagprovcom_flagComuneAssociato",response.getAnagprovcom().getFlagComuneAssociato().equals("Y"));
		    //PG200390 GG - inizio
		    if (response.getAnagprovcom().getCodiceIstat()!=null) 
		    	request.setAttribute("anagprovcom_codiceIstat", response.getAnagprovcom().getCodiceIstat());
		    //PG200390 GG - fine
			
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
			//System.out.println(firedButton+" saveedit");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("anagprovcom_codiceBelfiore") !=null){
						request.setAttribute("anagprovcom_codiceBelfiore", null);
						request.setAttribute("anagprovcom_codiceProvincia", null);
						request.setAttribute("anagprovcom_codiceComune", null);
						index(request);
					}
				}
			}else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){					
					resetParametri(request);
					request.setAttribute("action", "add");
				}
			}else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
		
	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.UPDT_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			//System.out.println(firedButton+" saveedit");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("anagprovcom_codiceBelfiore") !=null){
						request.setAttribute("anagprovcom_codiceBelfiore", null);
						request.setAttribute("anagprovcom_codiceProvincia", null);
						request.setAttribute("anagprovcom_codiceComune", null);
						index(request);
					}
				}
			}else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){					
					edit(request);	
				}
			}else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	
	private void save(HttpServletRequest request) throws ActionException {
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		String descrizioneProvinciaDE = "";
		String descrizioneComuneDE = "";
		String flagComuneAssociato = "";
		String codiceIstat = "";
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		/*String anagProvComCodeBelfiore = request.getParameter("anagProvCom_anagProvComCodeBelfiore");
		String anagProvComCodeProv = request.getParameter("anagProvCom_anagProvComCodeProv");
		String anagProvComCodeCom = request.getParameter("anagProvCom_anagProvComCodeCom");
		String anagProvComCap = request.getParameter("anagProvCom_anagProvComCap");
		String anagProvComSiglaProv = request.getParameter("anagProvCom_anagProvComSiglaProv");
		String anagProvComDesProv = request.getParameter("anagProvCom_anagProvComDesProv");
		String anagProvComDesReg = request.getParameter("anagProvCom_anagProvComDesReg");
		String anagProvComCodeCat = request.getParameter("anagProvCom_anagProvComCodeCat");
	    */
		String codiceBelfiore = request.getParameter("anagprovcom_codiceBelfiore");
	    String codiceProvincia = request.getParameter("anagprovcom_codiceProvincia");
	    String codiceComune = request.getParameter("anagprovcom_codiceComune");
	    String descrizioneComune = request.getParameter("anagprovcom_descrizioneComune");
	    String cap = request.getParameter("anagprovcom_cap");
	    String siglaProvincia = request.getParameter("anagprovcom_siglaProvincia");
	    String descrizioneProvincia = request.getParameter("anagprovcom_descrizioneProvincia");
	    String descrizioneRegione = request.getParameter("anagprovcom_descrizioneRegione");
	    String codiceCatastale = request.getParameter("anagprovcom_codiceCatastale");
	    if (request.getParameter("anagprovcom_descrizioneProvinciaDE")!=null)
	    	descrizioneProvinciaDE = request.getParameter("anagprovcom_descrizioneProvinciaDE");
	    if (request.getParameter("anagprovcom_descrizioneComuneDE")!=null)
	    	descrizioneComuneDE = request.getParameter("anagprovcom_descrizioneComuneDE");
	    if (request.getParameter("anagprovcom_flagComuneAssociato")!=null)
	    	flagComuneAssociato = request.getParameter("anagprovcom_flagComuneAssociato");
	    //PG200390 GG - inizio
	    if (request.getParameter("anagprovcom_codiceIstat")!=null)
	    	codiceIstat = request.getParameter("anagprovcom_codiceIstat");
	    //PG200390 GG - fine
	    
		//AnagProvComImplementationStub stub = null;
		try {
					
			AnagProvCom anagProvCom = new AnagProvCom(codiceBelfiore,codiceProvincia,codiceComune,descrizioneComune,
					cap,siglaProvincia,descrizioneProvincia,descrizioneRegione,codiceCatastale, usernameAutenticazione,descrizioneProvinciaDE,descrizioneComuneDE,flagComuneAssociato,codiceIstat);
			/* we prepare object for save */
			AnagProvComSaveRequest saveRequest = new AnagProvComSaveRequest(anagProvCom,codOp);
			/* we save object */
			saveAnagProvCom(saveRequest, request);

		} catch (Exception e) {
			try {
				//AnagProvComDetailRequest detailRequest = new AnagProvComDetailRequest(codiceBelfiore);
				AnagProvComDetailResponse detailResponse = getAnagProvCom(codiceBelfiore, request);
				//request.setAttribute("company", detailResponse.getCompany());
				//request.setAttribute("anagprovcom_companyCode", detailResponse.getAnagprovcom().getCompanyCode());
				request.setAttribute("anagprovcom_codiceBelfiore", detailResponse.getAnagprovcom().getCodiceBelfiore());
				request.setAttribute("anagprovcom_codiceProvincia", detailResponse.getAnagprovcom().getCodiceProvincia());
				request.setAttribute("anagprovcom_descrizioneProvincia", detailResponse.getAnagprovcom().getDescrizioneProvincia());
				request.setAttribute("anagente_siglaProvincia", detailResponse.getAnagprovcom().getSiglaProvincia());
				request.setAttribute("anagente_codiceComune", detailResponse.getAnagprovcom().getCodiceComune());
				request.setAttribute("anagente_codiceCatastale", detailResponse.getAnagprovcom().getCodiceCatastale());
				request.setAttribute("anagente_cap", detailResponse.getAnagprovcom().getCap());
				request.setAttribute("anagente_descrizioneRegione", detailResponse.getAnagprovcom().getDescrizioneRegione());
				if (detailResponse.getAnagprovcom().getDescrizioneComune()!=null)
					request.setAttribute("anagente_descrizioneComunale", detailResponse.getAnagprovcom().getDescrizioneComune());
				else
					request.setAttribute("anagente_descrizioneComunale", "");
				
				} catch (Exception ignore) { }
		
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String codiceBelfiore = request.getParameter("anagprovcom_codiceBelfiore");
		try {
			request.setAttribute("varname", "anagprovcom");
			/* we prepare object for cancel */
			AnagProvComCancelRequest cancelRequest = new AnagProvComCancelRequest(codiceBelfiore);
			/* we cancel object */			
			cancelAnagProvCom(cancelRequest, request);
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

private AnagProvComSearchResponse getAnagProvComSearchResponse(
		String anagProvComCodeBelfiore,String anagProvComcodiceProvincia, String anagProvComcodiceComune, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
{
	
	AnagProvComSearchResponse res = null;
	AnagProvComSearchRequest in = new AnagProvComSearchRequest();
	in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
	in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
	in.setOrder(order == null ? "" : order);
	in.setCodiceBelfiore(anagProvComCodeBelfiore == null ? "" : anagProvComCodeBelfiore);
	in.setCodiceProvincia(anagProvComcodiceProvincia == null ? "" :anagProvComcodiceProvincia );
	in.setCodiceComune(anagProvComcodiceComune == null ? "" : anagProvComcodiceComune);	
	res = WSCache.anagProvComServer.getAnagProvComs(in, request);
	return res;
}
private void  saveAnagProvCom(AnagProvComSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.anagProvComServer.save(saveRequest, request);
	
}

private void  cancelAnagProvCom(AnagProvComCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.anagProvComServer.cancel(cancelRequest, request);
	
}

private AnagProvComDetailResponse getAnagProvComDetailSearchResponse(String anagProvComCodeBelfiore, HttpServletRequest request) throws FaultType, RemoteException
{
	 
	AnagProvComDetailResponse res = null;
	AnagProvComDetailRequest in = new AnagProvComDetailRequest(anagProvComCodeBelfiore);
	in.setCodiceBelfiore(anagProvComCodeBelfiore == null ? "" : anagProvComCodeBelfiore);
	res = WSCache.anagProvComServer.getAnagProvCom(in, request);
	return res;
}
/*
private void cancelAnagProvComResponse (String codiceBelfiore) throws FaultType, RemoteException
{
	 
	
	AnagProvComCancelRequest in = new AnagProvComCancelRequest(codiceBelfiore);
	in.setCodiceBelfiore(codiceBelfiore == null ? "" : codiceBelfiore);
	WSCache.anagProvComServer.cancel(in);
	
}
*/
private AnagProvComDetailResponse getAnagProvCom(String codiceBelfiore, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
{
	AnagProvComDetailRequest detailRequest = new AnagProvComDetailRequest(codiceBelfiore);
	detailRequest.setCodiceBelfiore(codiceBelfiore == null ? "" : codiceBelfiore);
	return WSCache.anagProvComServer.getAnagProvCom(detailRequest, request);
}

protected void resetParametri(HttpServletRequest request) {
	Enumeration e = request.getParameterNames();
	String p = "";
	while (e.hasMoreElements()) {
		
		p = (String) e.nextElement();
		System.out.println(p);
		request.setAttribute(p, "");
	}
	return;
}

}
