package org.seda.payer.manager.configurazione.actions;

import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnte;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteCancelRequest;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteDetailRequest;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteDetailResponse;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteResponse;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteResponsePageInfo;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteSaveRequest;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteSearchRequest;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteSearchResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;

public class BaseAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String payerDbSchema = null;
	protected DataSource payerDataSource = null;
	protected int rowsPerPage = 0;
	protected int pageNumber = 0;
	protected String order;
	
	@Override
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		
		PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		String dataSourceName =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
		this.payerDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		try {	
			this.payerDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));
		} catch (ServiceLocatorException e) {
			throw new ActionException("ServiceLocator error " + e.getMessage(),e);
		}

		this.rowsPerPage = ((String) request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
		this.pageNumber = ((String) request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));
		this.order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		
		
		return null;
	}
	
	public void init(ServletContext context) throws ActionException{
		super.init(context);
	}

	protected ConfigRtEnteSearchResponse getConfigRtEnteSearchResponse(
			String companyCode, String userCode, String chiaveEnte, String codiceIdpa, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigRtEnteSearchResponse res = null;
		ConfigRtEnteSearchRequest in = new ConfigRtEnteSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setUserCode(userCode == null ? "" : userCode);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setCodiceIdpa(codiceIdpa == null ? "" : codiceIdpa);
		
		res = WSCache.configRtEnteServer.getConfigRtEntes(in, request);
		return res;
	}
	
	protected Object search(HttpServletRequest request) throws ActionException {
		try {
			String companyCode = ((String)request.getAttribute("tx_societa")==null ? "":  (String)request.getAttribute("tx_societa"));
			String userCode = ((String)request.getAttribute("tx_utente")==null ? "":  (String)request.getAttribute("tx_utente"));
			String chiaveEnte = ((String)request.getAttribute("tx_ente")==null ? "":  (String)request.getAttribute("tx_ente"));
			
			
			ConfigRtEnteSearchResponse searchResponse = getConfigRtEnteSearchResponse(companyCode, userCode, chiaveEnte, "", rowsPerPage, pageNumber, order, request);
			ConfigRtEnteResponse configrtenteResponse = searchResponse.getResponse();
			ConfigRtEnteResponsePageInfo responsePageInfo = configrtenteResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			request.setAttribute("tx_societa", companyCode);
			request.setAttribute("tx_utente", userCode);
			request.setAttribute("tx_ente", chiaveEnte);
			
			request.setAttribute("listaEnti", configrtenteResponse.getListXml());
			request.setAttribute("listaEnti.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	protected Object richiestacanc(HttpServletRequest request) {	  
			request.setAttribute("richiestacanc", request.getAttribute("richiestacanc"));
			request.setAttribute("tx_societa",request.getAttribute("tx_societa"));
			request.setAttribute("tx_utente",request.getAttribute("tx_utente"));		  
			request.setAttribute("tx_ente",request.getAttribute("tx_ente"));
			
		
		return null; 
	}
	
	
	protected Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			
			String codSocieta = ((String)request.getAttribute("tx_societa")==null ? "":  (String)request.getAttribute("tx_societa"));
			String userCode = ((String)request.getAttribute("tx_utente")==null ? "":  (String)request.getAttribute("tx_utente"));
			String chiaveEnte = ((String)request.getAttribute("tx_ente")==null ? "":  (String)request.getAttribute("tx_ente"));
			String codiceIdpa = ((String)request.getAttribute("tx_codiceIPA")==null ? "": (String)request.getAttribute("tx_codiceIPA"));
			ConfigRtEnteDetailResponse response = getConfigRtEnteDetailSearchResponse(codSocieta, chiaveEnte,userCode, codiceIdpa, request);
			
			//
			request.setAttribute("tx_societa", response.getConfigRtEnte().getCompanyCode());
			request.setAttribute("tx_utente", response.getConfigRtEnte().getUserCode());
			request.setAttribute("tx_ente", response.getConfigRtEnte().getChiaveEnte());
			request.setAttribute("tx_codiceIPA", response.getConfigRtEnte().getCodiceIdpa());
			request.setAttribute("tx_utentelogin", response.getConfigRtEnte().getUserInvioRt());
			//Base64 base64 = new Base64();
			//String PwdTemp = new String(base64.decode(response.getConfigRtEnte().getPasswInvioRt().getBytes()));
			request.setAttribute("tx_password", response.getConfigRtEnte().getPasswInvioRt());
			request.setAttribute("tx_URL", response.getConfigRtEnte().getUrlInvioRt());
			request.setAttribute("tx_mail", response.getConfigRtEnte().getMailInvioEsitoInvioRt());
			if (response.getConfigRtEnte().getFlagAbilitato()) {
				request.setAttribute("tx_ente_abilitato", "1");
			} else {
				request.setAttribute("tx_ente_abilitato", "0");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	protected void save(HttpServletRequest request) throws ActionException {
		//String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		String codOp = (String) request.getAttribute("codop");
		if(codOp == null || codOp.equals("")) codOp = "add";
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		String userCode = (String) request.getAttribute("tx_utente");
		String companyCode = (String) request.getAttribute("tx_societa");
		String chiaveEnte = (String) request.getAttribute("tx_ente");
		String codiceIdpa = (String)request.getAttribute("tx_codiceIPA");
		String userInvioRt = (String)request.getAttribute("tx_utentelogin");
		String passwInvioRt = (String)request.getAttribute("tx_password");
//		Base64 base64 = new Base64();
//		String passwInvioRt = new String(base64.encode(tmpPass.getBytes()));
		String urlInvioRt = (String)request.getAttribute("tx_URL");
		String mailInvioEsitoInvioRt = (String)request.getAttribute("tx_mail");
		 
		Boolean flagAbilitato = false;
		String flAbil = (String)request.getAttribute("tx_ente_abilitato");
		if (flAbil.toUpperCase().equals("1")) {
			flagAbilitato = true;//
		}
	    
		try {
			ConfigRtEnte configrtente = new ConfigRtEnte(companyCode, userCode, chiaveEnte, 
					codiceIdpa, userInvioRt, passwInvioRt, urlInvioRt, mailInvioEsitoInvioRt,
					"", "", 
					true, flagAbilitato, userBean.getUserName().trim());
			/* we prepare object for save */
			ConfigRtEnteSaveRequest saveRequest = new ConfigRtEnteSaveRequest(configrtente,codOp);
			/* we save object */
			saveConfigRtEnte(saveRequest, request);
			if(codOp != null && codOp.equals("add")) {
				request.setAttribute("tx_message", "Configurazione inserita correttamente."); 
			} else if (codOp != null && codOp.equals("edit")){
				request.setAttribute("tx_message", "Configurazione aggiornata correttamente.");
			}
		} catch (Exception e) {
				if (codOp != null && codOp.equals("add")) request.setAttribute("tx_error_message", Messages.INS_ERR.format());
				else if (codOp != null && codOp.equals("edit")) request.setAttribute("tx_error_message", Messages.UPDT_ERR.format());
				else request.setAttribute("tx_error_message", "Errore generico."); 
			}
	}
		
	protected Object cancel(HttpServletRequest request) throws ActionException {
		String codSocieta = (String) request.getAttribute("tx_societa");
		String userCode = (String) request.getAttribute("tx_utente");
		String chiaveEnte = (String) request.getAttribute("tx_ente");
		String codiceIdpa = (String) request.getAttribute("tx_codiceIPA");
		request.setAttribute("configrtente_codiceIdpa", codiceIdpa);
		request.setAttribute("varname", "configrtente");
		/* we prepare object for cancel */
		ConfigRtEnteCancelRequest cancelRequest = new ConfigRtEnteCancelRequest(codSocieta, userCode, chiaveEnte, codiceIdpa);
		/* we cancel object */			
		try {
			cancelConfigRtEnte(cancelRequest, request);
			request.setAttribute("tx_message", Messages.CANC_OK.format());
		} catch (FaultType e) {
			request.setAttribute("tx_error_message", Messages.CANCEL_ERR.format());
		} catch (RemoteException e) {
			request.setAttribute("tx_error_message", Messages.CANCEL_ERR.format());
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
	
	
	protected void  saveConfigRtEnte(ConfigRtEnteSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException {
		
		WSCache.configRtEnteServer.save(saveRequest, request);
		
	}
	
	protected void  cancelConfigRtEnte(ConfigRtEnteCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException {
		
		WSCache.configRtEnteServer.cancel(cancelRequest, request);
		
	}
	
	protected ConfigRtEnteDetailResponse getConfigRtEnteDetailSearchResponse(String codSocieta, String chiaveEnte, String userCode, String codiceIdpa, HttpServletRequest request) throws FaultType, RemoteException {
		 
		ConfigRtEnteDetailResponse res = null;
		ConfigRtEnteDetailRequest in = new ConfigRtEnteDetailRequest(codSocieta, chiaveEnte, userCode, codiceIdpa);
		in.setCodiceSocieta(codSocieta == null ? "" : codSocieta);
		in.setUserCode(userCode == null ? "" : userCode);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setCodiceIdpa(codiceIdpa == null ? "": codiceIdpa);
		res = WSCache.configRtEnteServer.getConfigRtEnte(in, request);
		return res;
	}


	protected ConfigRtEnteDetailResponse getConfigRtEnte(String codSocieta, String chiaveEnte, String userCode, String codiceIdpa, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException {
		ConfigRtEnteDetailRequest detailRequest = new ConfigRtEnteDetailRequest(codSocieta, chiaveEnte, userCode, codiceIdpa);
		detailRequest.setCodiceSocieta(codSocieta == null ? "" : codSocieta);
		detailRequest.setUserCode(userCode == null ? "" : userCode);
		detailRequest.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		detailRequest.setCodiceIdpa(codiceIdpa == null ? "" : codiceIdpa);
	
		
		return WSCache.configRtEnteServer.getConfigRtEnte(detailRequest, request);
	}
	
	protected ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEntes2(String codiceSocieta,String codiceUtente,String chiaveEnte, String procName, HttpServletRequest request) throws FaultType, RemoteException{
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteSearchRequest2 in = new ConfigUtenteTipoServizioEnteSearchRequest2();
		
		
		in.setCompanyCode(codiceSocieta == null ? "" : codiceSocieta);
		in.setCodiceUtente(codiceUtente == null ? "" : codiceUtente);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setProcName(procName == null ? "" : procName);
		
		res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes2(in, request);
		
		return res;
	}
	
	protected boolean checkConfigurazioneEsistente(String soc, String ute, String ente, String ipa, HttpServletRequest request) {
		boolean check = false;
		
		try {
			ConfigRtEnteDetailResponse response = getConfigRtEnte(soc,ente,ute,ipa,request);
			check = true;
		} catch(Exception e) {
			check = false;
		}
		
		return check;
	}

}

	

