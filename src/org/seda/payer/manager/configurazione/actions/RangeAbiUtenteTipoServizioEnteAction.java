/**
 * 
 */
package org.seda.payer.manager.configurazione.actions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;

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

import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioSearchResponse;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.dati.RangeAbiUtenteTipoServizioEnte;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.dati.RangeAbiUtenteTipoServizioEnteCancelRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.dati.RangeAbiUtenteTipoServizioEnteDetailRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.dati.RangeAbiUtenteTipoServizioEnteDetailResponse;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.dati.RangeAbiUtenteTipoServizioEnteResponse;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.dati.RangeAbiUtenteTipoServizioEnteResponsePageInfo;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.dati.RangeAbiUtenteTipoServizioEnteSaveRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.dati.RangeAbiUtenteTipoServizioEnteSearchRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.dati.RangeAbiUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;



public class RangeAbiUtenteTipoServizioEnteAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String strCodiceSocieta,strCodiceUtente,strChiaveEnte,usernameAutenticazione;
	private String userCodiceSocieta = null;
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
			
			
			
			if (user != null)
				   userCodiceSocieta = (user.getProfile().equals("AMMI") ? "" : user.getCodiceSocieta());
			   else
				   userCodiceSocieta = "XXXXX";
			if(user!=null) {
				   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
				   request.setAttribute("userAppl_codiceSocieta",user.getCodiceSocieta().trim());
				   request.setAttribute("userAppl_codiceUtente",user.getCodiceUtente().trim());
				   //request.setAttribute("userAppl_tipoBollettino",user.getT.getChiaveEnteCon().trim());
				   //request.setAttribute("userAppl_chiaveEnteEnt",userAppl.getChiaveEnteEnt().trim());
				}
			usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
			strCodiceSocieta = (String)request.getAttribute("userAppl_codiceSocieta");
			strCodiceUtente = (String)request.getAttribute("userAppl_codiceUtente");
			strChiaveEnte = (String)request.getAttribute("userAppl_chiaveEnteCon");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			String firedButton = (String)request.getAttribute("tx_button_nuovo");
			//System.out.println(firedButton+"index");
			if (firedButton!=null || firedButtonReset !=null){			
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
			//String codiceSocieta = (strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:request.getParameter("rangeabiutentetiposervizioente_companyCode");
			String codiceUtente = (strCodiceUtente!=null && strCodiceUtente.length()>0)?strCodiceUtente:request.getParameter("rangeabiutentetiposervizioente_companyCode");
			String chiaveEnte = (strChiaveEnte!=null && strChiaveEnte.length()>0)?strChiaveEnte:request.getParameter("rangeabiutentetiposervizioente_chiaveEnte");
			String strEnte = ((String)request.getAttribute("ente_strEnte")==null ? "":  (String)request.getAttribute("ente_strEnte"));
			String codiceTipologiaServizio = ((String)request.getAttribute("rangeabiutentetiposervizioente_codiceTipologiaServizio")==null ? "":  (String)request.getAttribute("rangeabiutentetiposervizioente_codiceTipologiaServizio"));
			String tipoRange = request.getParameter("rangeabiutentetiposervizioente_tipoRange");
			String inizioRangeDa = request.getParameter("rangeabiutentetiposervizioente_inizioRangeDa");
			String fineRangeA = request.getParameter("rangeabiutentetiposervizioente_fineRangeA");
			String inizioRangePer = request.getParameter("rangeabiutentetiposervizioente_inizioRangePer");
			String flagCin = request.getParameter("rangeabiutentetiposervizioente_flagCin");
			String strSocieta = ((String)request.getAttribute("rangeabiutentetiposervizioente_company")==null ? "":  (String)request.getAttribute("rangeabiutentetiposervizioente_company"));
			String strUtente = ((String)request.getAttribute("rangeabiutentetiposervizioente_utente")==null ? "":  (String)request.getAttribute("rangeabiutentetiposervizioente_utente"));
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				strSocieta="";
				strUtente="";
				codiceTipologiaServizio="";
				/*inizioRangeDa="";
				fineRangeA="";
				inizioRangePer="";
				chiaveEnte="";*/
				strEnte="";
				//tipoRange="";
				
			}
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					codiceTipologiaServizio = request.getParameter("");
					strSocieta = request.getParameter("");
					strUtente = request.getParameter("");
					/*inizioRangeDa=request.getParameter("");
					fineRangeA=request.getParameter("");
					inizioRangePer= request.getParameter("");
					chiaveEnte= request.getParameter("");*/
					strEnte=request.getParameter("");
					//tipoRange=request.getParameter(""); 
				}
			}
			RangeAbiUtenteTipoServizioEnteSearchResponse searchResponse = null;
			if (firedButton!=null)
				 if(firedButton.equals("Indietro"))
//					 searchResponse = getRangeAbiUtenteTipoServizioEnteSearchResponse("",userCodiceSocieta,"",null,null,chiaveEnte,null,null,null,null,null,null,null,"","","", rowsPerPage, pageNumber, order);
					 searchResponse = getRangeAbiUtenteTipoServizioEnteSearchResponse("",userCodiceSocieta,"",null,null,null,null,null,null,null,null,null,null,"","","", rowsPerPage, pageNumber, order, request);
				 if (firedButton==null)   
					 searchResponse = getRangeAbiUtenteTipoServizioEnteSearchResponse("",userCodiceSocieta,codiceUtente,strSocieta, strUtente,chiaveEnte,codiceTipologiaServizio,tipoRange,inizioRangeDa,fineRangeA,inizioRangePer,flagCin,strEnte,"","","", rowsPerPage, pageNumber, order, request);
			
			
			//RangeAbiUtenteTipoServizioEnteImplementationStub stub = new RangeAbiUtenteTipoServizioEnteImplementationStub(new URL(getRangeabiutentetiposervizioenteManagerEndpointUrl()), null);
			//RangeAbiUtenteTipoServizioEnteSearchResponse searchResponse = getRangeAbiUtenteTipoServizioEnteSearchResponse("",codiceSocieta,codiceUtente,chiaveEnte,codiceTipologiaServizio,tipoRange,inizioRangeDa,fineRangeA,inizioRangePer,flagCin,strEnte,"","","", rowsPerPage, pageNumber, order);
			//RangeAbiUtenteTipoServizioEnteSearchResponse searchResponse = stub.getRangeAbiUtenteTipoServizioEntes(searchRequest);
			RangeAbiUtenteTipoServizioEnteResponse rangeabiutentetiposervizioenteServizioResponse = searchResponse.getResponse();
			RangeAbiUtenteTipoServizioEnteResponsePageInfo responsePageInfo = rangeabiutentetiposervizioenteServizioResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("rangeabiutentetiposervizioente", searchRequest);
		      request.setAttribute("rangeabiutentetiposervizioente_chiaveRange", request.getParameter("rangeabiutentetiposervizioente_chiaveRange"));
			  request.setAttribute("rangeabiutentetiposervizioente_company", strSocieta);
			  request.setAttribute("rangeabiutentetiposervizioente_utente", strUtente);
			  request.setAttribute("rangeabiutentetiposervizioente_codiceTipologiaServizio", codiceTipologiaServizio);
			  request.setAttribute("rangeabiutentetiposervizioente_tipoRange", tipoRange);
			  request.setAttribute("rangeabiutentetiposervizioente_inizioRangeDa",inizioRangeDa);
			  request.setAttribute("rangeabiutentetiposervizioente_fineRangeA", fineRangeA);
			  request.setAttribute("rangeabiutentetiposervizioente_inizioRangePer", inizioRangePer);
			  request.setAttribute("rangeabiutentetiposervizioente_flagCin", flagCin);	
			
			request.setAttribute("rangeabiutentetiposervizioentes", rangeabiutentetiposervizioenteServizioResponse.getListXml());
			request.setAttribute("rangeabiutentetiposervizioentes.pageInfo", pageInfo);
			request.setAttribute("ente_strEnte", strEnte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			/* we retry all configutentetiposervizioente */ 
			//String codiceSocieta = (strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:"";
			String codiceUtente = (strCodiceUtente!=null && strCodiceUtente.length()>0)?strCodiceUtente:"";
			String chiaveEnte = (strChiaveEnte!=null && strChiaveEnte.length()>0)?strChiaveEnte:"";
			
			//ConfigUtenteTipoServizioEnteImplementationStub stubConfigUtenteTipoServizioEnte = new ConfigUtenteTipoServizioEnteImplementationStub(new URL(getConfigutentetiposervizioenteManagerEndpointUrl()), null);
			ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse = getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta,codiceUtente,chiaveEnte, "", "", 0, 0, "", request);
			request.setAttribute("configutentetiposervizioentes", configUtenteTipoServizioEnteSearchResponse.getResponse().getListXml());
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {
		  request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		  request.setAttribute("chiaveRange",request.getParameter("chiaveRange"));
		return null; 
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			//ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse = getConfigUtenteTipoServizioEntesSearchResponse(userCodiceSocieta,"","", "", "", 0, 0, "");
			//request.setAttribute("configutentetiposervizioentes", configUtenteTipoServizioEnteSearchResponse.getResponse().getListXml());
		
			//RangeAbiUtenteTipoServizioEnteImplementationStub stub = new RangeAbiUtenteTipoServizioEnteImplementationStub(new URL(getRangeabiutentetiposervizioenteManagerEndpointUrl()), null);
			RangeAbiUtenteTipoServizioEnteDetailResponse response = 
				getRangeAbiUtenteTipoServizioEnteDetailSearchResponse(request.getParameter("rangeabiutentetiposervizioente_chiaveRange"), request);
			request.setAttribute("rangeabiutentetiposervizioente", response.getRangeabiutentetiposervizioente());
			//request.setAttribute("rangeabidescrizioneente", request.getParameter("rangeabiutentetiposervizioente_descrizioneEnte"));
		      request.setAttribute("rangeabiutentetiposervizioente_chiaveRange", response.getRangeabiutentetiposervizioente().getChiaveRange());
			  request.setAttribute("rangeabiutentetiposervizioente_companyCode", response.getRangeabiutentetiposervizioente().getCompanyCode());
			  request.setAttribute("rangeabiutentetiposervizioente_codiceUtente", response.getRangeabiutentetiposervizioente().getCodiceUtente());
			  request.setAttribute("rangeabiutentetiposervizioente_chiaveEnte", response.getRangeabiutentetiposervizioente().getChiaveEnte());
			  request.setAttribute("rangeabiutentetiposervizioente_tipoRange", response.getRangeabiutentetiposervizioente().getTipoRange());
			  request.setAttribute("rangeabiutentetiposervizioente_codiceTipologiaServizio", response.getRangeabiutentetiposervizioente().getCodiceTipologiaServizio());
			  request.setAttribute("rangeabiutentetiposervizioente_inizioRangeDa", response.getRangeabiutentetiposervizioente().getInizioRangeDa());
			  request.setAttribute("rangeabiutentetiposervizioente_fineRangeA", response.getRangeabiutentetiposervizioente().getFineRangeA());
			  request.setAttribute("rangeabiutentetiposervizioente_inizioRangePer", response.getRangeabiutentetiposervizioente().getInizioRangePer());	
			  request.setAttribute("rangeabiutentetiposervizioente_flagCin", response.getRangeabiutentetiposervizioente().getFlagCin());
			  add(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*public Object saveaddOLD(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.INS_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			String tipoRange = request.getParameter("rangeabiutentetiposervizioente_tipoRange");
			String inizioRangeDa = request.getParameter("rangeabiutentetiposervizioente_inizioRangeDa");
			if(inizioRangeDa.equals("")) inizioRangeDa="0";
			String fineRangeA = request.getParameter("rangeabiutentetiposervizioente_fineRangeA");
			if(fineRangeA.equals("")) fineRangeA="0";
			String inizioRangePer = request.getParameter("rangeabiutentetiposervizioente_inizioRangePer");
			String flagCin = request.getParameter("rangeabiutentetiposervizioente_flagCin");
			if((tipoRange.equals("D")||tipoRange.equals("V")) && flagCin.equals("Y")){
				setFormMessage("frmAction", "Flag Cin: Non Impostato Correttamente", request);
				add(request);
			}
			else if(!inizioRangePer.equals("") && (!inizioRangeDa.equals("0") || !fineRangeA.equals("0"))){
				setFormMessage("frmAction", "Inizio Range e Fine Range: Non Impostati Correttamente", request);
				add(request);
			}
			else if(inizioRangePer.equals("") && (Integer.parseInt(inizioRangeDa)>=Integer.parseInt(fineRangeA))){
				setFormMessage("frmAction", "Inizio Range e Fine Range: Non Impostati Correttamente", request);
				add(request);
			}
			else if(inizioRangePer.equals("") && (inizioRangeDa.equals("0") || fineRangeA.equals("0"))){
				setFormMessage("frmAction", "Inizio Range e Fine Range: Non Impostati Correttamente", request);
				add(request);
			}
			else if (firedButton!=null){
				if(firedButton.equals("Indietro")){
						request.setAttribute("rangeabiutentetiposervizioente_codiceTipologiaServizio", null);
						request.setAttribute("rangeabiutentetiposervizioente_companyCode", null);
						request.setAttribute("rangeabiutentetiposervizioente_codiceUtente", null);
						request.setAttribute("ente_strente", null);
						index(request);
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset"))					
					resetParametri(request);
			}else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}*/
	
	public Object saveadd(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.INS_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			
			String tipoRange = request.getParameter("rangeabiutentetiposervizioente_tipoRange");
			String inizioRangeDa = request.getParameter("rangeabiutentetiposervizioente_inizioRangeDa");
			if(inizioRangeDa.equals("")) inizioRangeDa="0";
			String fineRangeA = request.getParameter("rangeabiutentetiposervizioente_fineRangeA");
			if(fineRangeA.equals("")) fineRangeA="0";
			boolean errore=false;
			String inizioRangePer = request.getParameter("rangeabiutentetiposervizioente_inizioRangePer");
			String flagCin = request.getParameter("rangeabiutentetiposervizioente_flagCin");
			if(firedButtonReset==null){
				if((tipoRange.equals("D")||tipoRange.equals("V")) && flagCin.equals("Y")){
					setFormMessage("frmAction", "Flag Cin: Non Impostato Correttamente", request);
					errore=true;
					add(request);
				}
				if(!inizioRangePer.equals("") && ((!inizioRangeDa.equals("0")&& request.getParameter("rangeabiutentetiposervizioente_inizioRangeDa").equals("")) || (!fineRangeA.equals("0"))&& request.getParameter("rangeabiutentetiposervizioente_inizioRangeDa").equals(""))){
					setFormMessage("frmAction", "Inizio Range e Fine Range: Non Impostati Correttamente", request);
					errore=true;
					add(request);
				}
				if(inizioRangePer.equals("")){
					BigInteger a = new BigInteger(inizioRangeDa);
					BigInteger b = new BigInteger(fineRangeA);
						if ( a.compareTo(b)>=0 ){
							errore=true;
							setFormMessage("frmAction", "Inizio Range e Fine Range: Non Impostati Correttamente", request);
							add(request);
						}
				}
			}
			if (firedButton!=null){
			
				if(firedButton.equals("Indietro")){
						request.setAttribute("rangeabiutentetiposervizioente_codiceTipologiaServizio", null);
						request.setAttribute("rangeabiutentetiposervizioente_company", null);
						request.setAttribute("rangeabiutentetiposervizioente_utente", null);
						request.setAttribute("ente_strente", null);
						index(request);
				}
			}
			else if(firedButtonReset!=null)					
				resetParametri(request);
				
			else if(errore==false && firedButton==null && firedButtonReset==null) 
				save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.UPDT_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			
			String tipoRange = request.getParameter("rangeabiutentetiposervizioente_tipoRange");
			String inizioRangeDa = request.getParameter("rangeabiutentetiposervizioente_inizioRangeDa");
			if(inizioRangeDa.equals("")) inizioRangeDa="0";
			String fineRangeA = request.getParameter("rangeabiutentetiposervizioente_fineRangeA");
			if(fineRangeA.equals("")) fineRangeA="0";
			boolean errore=false;
			String inizioRangePer = request.getParameter("rangeabiutentetiposervizioente_inizioRangePer");
			String flagCin = request.getParameter("rangeabiutentetiposervizioente_flagCin");
			if(firedButtonReset==null){
				if((tipoRange.equals("D")||tipoRange.equals("V")) && flagCin.equals("Y")){
					setFormMessage("frmAction", "Flag Cin: Non Impostato Correttamente", request);
					errore=true;
					//add(request);
				}
				if(!inizioRangePer.equals("") && (!inizioRangeDa.equals("0") || !fineRangeA.equals("0"))){
					setFormMessage("frmAction", "Inizio Range e Fine Range: Non Impostati Correttamente", request);
					errore=true;
					//add(request);
				}
				if(inizioRangePer.equals("")){
					BigInteger a = new BigInteger(inizioRangeDa);
					BigInteger b = new BigInteger(fineRangeA);
						if ( a.compareTo(b)>=0 ){
							errore=true;
							setFormMessage("frmAction", "Inizio Range e Fine Range: Non Impostati Correttamente", request);
							add(request);
						}
				}
				/*if(inizioRangePer.equals("")){
					int a = Integer.parseInt(inizioRangeDa);
					int b = Integer.parseInt(fineRangeA);
						if ( a >= b ){
							errore=true;
							setFormMessage("frmAction", "Inizio Range e Fine Range: Non Impostati Correttamente", request);
							//add(request);
						}
				}*/
			}
			if (firedButton!=null){
			
				if(firedButton.equals("Indietro")){
						request.setAttribute("rangeabiutentetiposervizioente_codiceTipologiaServizio", null);
						request.setAttribute("rangeabiutentetiposervizioente_companyCode", null);
						request.setAttribute("rangeabiutentetiposervizioente_codiceUtente", null);
						request.setAttribute("ente_strente", null);
						index(request);
				}
			}
			else if(firedButtonReset!=null)					
				edit(request);
				
			else if(errore==false && firedButton==null && firedButtonReset==null) 
				save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	private void save(HttpServletRequest request) throws ActionException {
		String companyCode="";
		String codiceUtente = "";
		String chiaveEnte = "";
		String codiceTipologiaServizio = "";
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		String cod = request.getParameter("prova");
		String arrStr = request.getParameter("rangeabiutentetiposervizioente_strConfigUtenteTipoServizioEntes");
		if (cod.equals("0") && arrStr!=null && arrStr.length()>0)
		{
			  String[] strConfigUtenteTipoServizioEntes = arrStr.split("\\|");
              companyCode = strConfigUtenteTipoServizioEntes[0];
 		      codiceUtente = strConfigUtenteTipoServizioEntes[1];
 		      chiaveEnte = strConfigUtenteTipoServizioEntes[2];
 		      codiceTipologiaServizio = strConfigUtenteTipoServizioEntes[3]; 
 		}
		if (cod.equals("1"))
		{
              companyCode = request.getParameter("rangeabiutentetiposervizioente_companyCode");
              codiceUtente= request.getParameter("rangeabiutentetiposervizioente_codiceUtente");
              chiaveEnte= request.getParameter("rangeabiutentetiposervizioente_chiaveEnte");
              codiceTipologiaServizio= request.getParameter("rangeabiutentetiposervizioente_codiceTipologiaServizio");
		}
		String chiaveRange = request.getParameter("rangeabiutentetiposervizioente_chiaveRange");
		String tipoRange = request.getParameter("rangeabiutentetiposervizioente_tipoRange");
		String inizioRangeDa = request.getParameter("rangeabiutentetiposervizioente_inizioRangeDa");
		String fineRangeA = request.getParameter("rangeabiutentetiposervizioente_fineRangeA");
		String inizioRangePer = request.getParameter("rangeabiutentetiposervizioente_inizioRangePer");
		String flagCin = request.getParameter("rangeabiutentetiposervizioente_flagCin");
		try {
			//stub = new RangeAbiUtenteTipoServizioEnteImplementationStub(new URL(getRangeabiutentetiposervizioenteManagerEndpointUrl()), null);
			RangeAbiUtenteTipoServizioEnte rangeAbiUtente = new RangeAbiUtenteTipoServizioEnte(chiaveRange,companyCode,codiceUtente,chiaveEnte,codiceTipologiaServizio,tipoRange,inizioRangeDa,fineRangeA,inizioRangePer, flagCin,usernameAutenticazione);
			/* we prepare object for save */
			RangeAbiUtenteTipoServizioEnteSaveRequest saveRequest = new RangeAbiUtenteTipoServizioEnteSaveRequest(rangeAbiUtente,codOp);
			/* we save object */
			saveRangeAbiUtenteTipoServizioEnte(saveRequest, request);
			
		} catch (Exception e) {
			try {
				RangeAbiUtenteTipoServizioEnteDetailRequest detailRequest = new RangeAbiUtenteTipoServizioEnteDetailRequest(chiaveRange);
				RangeAbiUtenteTipoServizioEnteDetailResponse detailResponse = getRangeAbiUtenteTipoServizioEnte(detailRequest, request);
				//request.setAttribute("rangeabiutentetiposervizioente", detailResponse.getRangeabiutentetiposervizioente());
			      request.setAttribute("rangeabiutentetiposervizioente_chiaveRange", detailResponse.getRangeabiutentetiposervizioente().getChiaveRange());
				  request.setAttribute("rangeabiutentetiposervizioente_companyCode", detailResponse.getRangeabiutentetiposervizioente().getCompanyCode());
				  request.setAttribute("rangeabiutentetiposervizioente_codiceUtente", detailResponse.getRangeabiutentetiposervizioente().getCodiceUtente());
				  request.setAttribute("rangeabiutentetiposervizioente_chiaveEnte", detailResponse.getRangeabiutentetiposervizioente().getChiaveEnte());
				  request.setAttribute("rangeabiutentetiposervizioente_tipoRange", detailResponse.getRangeabiutentetiposervizioente().getTipoRange());
				  request.setAttribute("rangeabiutentetiposervizioente_chiaveEnte", detailResponse.getRangeabiutentetiposervizioente().getCodiceTipologiaServizio());
				  request.setAttribute("rangeabiutentetiposervizioente_inizioRangeDa", detailResponse.getRangeabiutentetiposervizioente().getInizioRangeDa());
				  request.setAttribute("rangeabiutentetiposervizioente_fineRangeA", detailResponse.getRangeabiutentetiposervizioente().getFineRangeA());
				  request.setAttribute("rangeabiutentetiposervizioente_inizioRangePer", detailResponse.getRangeabiutentetiposervizioente().getInizioRangePer());
				  request.setAttribute("rangeabiutentetiposervizioente_flagCin", detailResponse.getRangeabiutentetiposervizioente().getFlagCin());
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERR.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String chiaveRange = request.getParameter("rangeabiutentetiposervizioente_chiaveRange");
		try {
			request.setAttribute("varname", "rangeabiutentetiposervizioente");
			//stub = new RangeAbiUtenteTipoServizioEnteImplementationStub(new URL(getRangeabiutentetiposervizioenteManagerEndpointUrl()), null);
			/* we prepare object for cancel */
			RangeAbiUtenteTipoServizioEnteCancelRequest cancelRequest = new RangeAbiUtenteTipoServizioEnteCancelRequest(chiaveRange);
			/* we cancel object */
			cancelRangeAbiUtenteTipoServizioEnte(cancelRequest, request);
			request.setAttribute("message", Messages.CANC_OK.format());
		} catch (FaultType f) {
			request.setAttribute("message", Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) {
			request.setAttribute("message", Messages.GENERIC_ERR.format());
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
	
	private RangeAbiUtenteTipoServizioEnteSearchResponse getRangeAbiUtenteTipoServizioEnteSearchResponse(
			String chiaveRange,String codiceSocieta, String codiceUtente,String strSocieta, String strUtente, String chiaveEnte,String codiceTipologiaServizio,
			String tipoRange, String inizioRangeDa,String fineRangeA,String inizioRangePer, String flagCin, String strEnte,String strDescrSocieta, 
			String strDescrUtente, String strDescrTipologiaServizio,  int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		RangeAbiUtenteTipoServizioEnteSearchResponse res = null;
		RangeAbiUtenteTipoServizioEnteSearchRequest in = new RangeAbiUtenteTipoServizioEnteSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		
		
		in.setChiaveEnte(chiaveRange == null ? "" : chiaveRange);
		in.setCompanyCode(codiceSocieta == null ? "": codiceSocieta);
		in.setCodiceUtente(codiceUtente == null ? "": codiceUtente);
		in.setChiaveEnte(chiaveEnte == null ? "": chiaveEnte);
		//in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setTipoRange(tipoRange == null ? "": tipoRange);
		in.setInizioRangeDa(inizioRangeDa == null ? "" :inizioRangeDa);
		in.setFineRangeA(fineRangeA == null ? "": fineRangeA);
		in.setInizioRangePer(inizioRangePer == null ? "": inizioRangePer);
		in.setFlagCin(flagCin == null ? "": flagCin);
		in.setStrEnte(strEnte == null ? "": strEnte);
		in.setStrDescrSocieta(strSocieta == null ? "" : strSocieta);
		in.setStrDescrUtente(strUtente == null ? "" : strUtente);
		in.setStrDescrTipologiaServizio(codiceTipologiaServizio == null ? "" :codiceTipologiaServizio);
		
		res = WSCache.rangeAbiUtenteTipoServizioEnteServer.getRangeAbiUtenteTipoServizioEntes(in, request);
		return res;
	}
	
	private RangeAbiUtenteTipoServizioEnteDetailResponse getRangeAbiUtenteTipoServizioEnteDetailSearchResponse(String chiaveRange, HttpServletRequest request) throws FaultType, RemoteException
	{
		 
		RangeAbiUtenteTipoServizioEnteDetailResponse res = null;
		RangeAbiUtenteTipoServizioEnteDetailRequest in = new RangeAbiUtenteTipoServizioEnteDetailRequest(chiaveRange);
		in.setChiaveRange(chiaveRange == null ? "" : chiaveRange);
		
		res = WSCache.rangeAbiUtenteTipoServizioEnteServer.getRangeAbiUtenteTipoServizioEnte(in, request);
		return res;
	}
	private void  saveRangeAbiUtenteTipoServizioEnte(RangeAbiUtenteTipoServizioEnteSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.rangeAbiUtenteTipoServizioEnteServer.save(saveRequest, request);
		
	}

	private void  cancelRangeAbiUtenteTipoServizioEnte(RangeAbiUtenteTipoServizioEnteCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.rangeAbiUtenteTipoServizioEnteServer.cancel(cancelRequest, request);
		
	}
	
	private ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEntesSearchResponse(String codiceSocieta,String codiceUtente,String chiaveEnte, String codiceTipologiaServizio,String strEnte,int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException{
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteSearchRequest in = new ConfigUtenteTipoServizioEnteSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		
		in.setCompanyCode(codiceSocieta == null ? "" : codiceSocieta);
		in.setCodiceUtente(codiceUtente == null ? "" : codiceUtente);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		
		//VERIFICARE SE AGGIUINGERE ALTRI in.set
		
		res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes(in, request);
		return res;
	}
	
	private RangeAbiUtenteTipoServizioEnteDetailResponse getRangeAbiUtenteTipoServizioEnte (RangeAbiUtenteTipoServizioEnteDetailRequest detailRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		return WSCache.rangeAbiUtenteTipoServizioEnteServer.getRangeAbiUtenteTipoServizioEnte(detailRequest, request);
	}
	
	protected void resetParametri(HttpServletRequest request) {
		Enumeration e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			
			p = (String) e.nextElement();
			request.setAttribute(p, "");
		}
		return;
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