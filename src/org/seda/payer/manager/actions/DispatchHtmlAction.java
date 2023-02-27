package org.seda.payer.manager.actions;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.validator.ValidationMessage;
import com.seda.j2ee5.maf.components.validation.ValidationContext;
import com.seda.j2ee5.maf.components.validation.ValidationErrorMap;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.HtmlAction;
import com.seda.j2ee5.maf.taglibs.ViewStateManager;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipoCartaXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipoCartaXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
/**
 * 
 * @author mmontisano
 *
 */
public abstract class DispatchHtmlAction extends HtmlAction{

	private static final long serialVersionUID = 1L;
    @SuppressWarnings("unchecked")
	protected Class clazz;
    @SuppressWarnings("unchecked")
	protected HashMap methods;
    @SuppressWarnings("unchecked")
	protected Class types[];
    
    public static enum FiredButton {
		TX_BUTTON_INDIETRO, TX_BUTTON_NUOVO, TX_BUTTON_NULL, TX_BUTTON_CHANGED, TX_BUTTON_WIS, TX_BUTTON_PROVINCIA_CHANGED, TX_BUTTON_DOWNLOAD
	}
	
    @SuppressWarnings("unchecked")
	public DispatchHtmlAction() {
        clazz = getClass();
        methods = new HashMap();
        types = (new Class[] { javax.servlet.http.HttpServletRequest.class });
    }

    @SuppressWarnings("unchecked")
	protected Method getMethod(String name) throws NoSuchMethodException {
	    Method method1;
	    synchronized(methods) {
	        Method method = (Method) methods.get(name);
	        if (method == null) {
	            method = clazz.getMethod(name, types);
	            methods.put(name, method);
	        }
	        method1 = method;
	    }
	    return method1;
    }

    public abstract void start(HttpServletRequest request);

    public abstract Object index(HttpServletRequest request) throws ActionException;
    
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		start(request);
		String action = request.getParameter("action");
        Method method = null;
        try {
        	method = getMethod(action);
            Object args[] = { request };
            method.invoke(this, args);
        } catch(Exception e) {
        	index(request);
        }
		return null;
	}
	
	public FiredButton getFiredButton(HttpServletRequest request) {

		if (request.getAttribute("tx_button_indietro") != null)
			return FiredButton.TX_BUTTON_INDIETRO;
		
		if (request.getAttribute("tx_button_nuovo") != null)
			return FiredButton.TX_BUTTON_NUOVO;
		
		if (request.getAttribute("tx_button_changed") != null || 
				"tx_button_changed".equals((String)request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_CHANGED;
		
		if (request.getAttribute("btnWis") != null)
			return FiredButton.TX_BUTTON_WIS;
		
		if (request.getAttribute(Field.TX_BUTTON_PROVINCIA_CHANGED.format()) != null
				|| Field.TX_BUTTON_PROVINCIA_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_PROVINCIA_CHANGED;
		if (request.getAttribute(Field.TX_BUTTON_DOWNLOAD.format()) != null
				|| Field.TX_BUTTON_DOWNLOAD.format().equals(
						request.getAttribute("download")))
			return FiredButton.TX_BUTTON_DOWNLOAD;
			
		return FiredButton.TX_BUTTON_NULL;
	}

	/*protected FiredButton getFiredButton(HttpServletRequest request) {

		if (request.getAttribute("tx_button_indietro") != null)
			return FiredButton.TX_BUTTON_INDIETRO;
		
		if (request.getAttribute("tx_button_nuovo") != null)
			return FiredButton.TX_BUTTON_NUOVO;

		return FiredButton.TX_BUTTON_NULL;
	}*/
	protected String replyAttributes(boolean replace,
			HttpServletRequest request, String... ignoredStrings) {
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo
		 * dei parameter.
		 */
		ViewStateManager viewStateManager = null;
		String viewStateId = request.getAttribute("vista") == null ? ""
				: request.getAttribute("vista").toString();
		if (!viewStateId.equals("")) {
			viewStateManager = new ViewStateManager(request,viewStateId);
			try {
				viewStateManager.replyAttributes(replace, ignoredStrings);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return viewStateId;
	}
	
	/**
	 * Recupera la lista dei Gateway (codice gateway, descrizione utente +
	 * descrizione gateway) --- Le cerca in sessione e, se non ci sono le carica
	 * tramite il WS e poi le mette in sessione --- "
	 * 
	 * @param request
	 * @param session
	 * @param codiceSocieta
	 * @param codiceUtente
	 * @param forceReload
	 */
	protected void loadListaGatewayXml_DDL(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String codiceUtente,
			boolean forceReload) {
		GetListaTipoCartaXml_DDLRequestType in = null;
		GetListaTipoCartaXml_DDLResponseType res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "gatewaypagamentos";
		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new GetListaTipoCartaXml_DDLRequestType();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceUtente(codiceUtente);
				res = WSCache.commonsServer.getListaTipoCartaXml_DDL(in, request);
				response = res.getResponse();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);					
				}
			} catch (FaultType e) {
				e.printStackTrace();
				
			} catch (RemoteException e) {
				e.printStackTrace();
				
			}
		}
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
	
	protected void loadSocietaUtenteServizioXml_DDL(HttpServletRequest request,HttpSession session)
	{
		ConfigUtenteTipoServizioSearchRequest in = null;
		ConfigUtenteTipoServizioSearchResponse res = null;
		com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioResponse response = null;
		
		String xml = null;
		String lista = "listaSocietaUtenteServizio";
		/*
		 * Cerco la lista  in sessione
		 */
		if (session.getAttribute(lista) != null)
		{
			request.setAttribute(lista, (String)session.getAttribute(lista));
		}
		else
		{
			/*
			 * Se non c'è lo carico tramite il WS e poi lo metto in sessione
			 */
			in = new ConfigUtenteTipoServizioSearchRequest();
			in.setCompanyCode("");
			in.setCodiceUtente("");
			in.setCodiceTipologiaServizio("");
			in.setOrder("");
			in.setPageNumber(0);
			in.setRowsPerPage(0);
			in.setStrDescrSocieta("");
			in.setStrDescrUtente("");
			in.setStrDescrTipologiaServizio("");
			try {
				res = WSCache.configUtenteTipoServizioServer.getConfigUtenteTipoServizios(in, request);
				if (res != null)
				{
					response = res.getResponse();
					if (response != null)
					{
						xml = response.getListXml();
					}
				}
				request.setAttribute(lista, xml);
				session.setAttribute(lista, xml);
			} catch (com.seda.payer.pgec.webservice.srv.FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	protected void loadSocietaUtenteServizioEnteXml_DDL(HttpServletRequest request,HttpSession session)
	{
		ConfigUtenteTipoServizioEnteSearchRequest in = null;
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteResponse response = null;
		
		String xml = null;
		String lista = "listaSocietaUtenteServizioEnte";
		/*
		 * Cerco la lista  in sessione
		 */
		if (session.getAttribute(lista) != null)
		{
			request.setAttribute(lista, (String)session.getAttribute(lista));
		}
		else
		{
			/*
			 * Se non c'è lo carico tramite il WS e poi lo metto in sessione
			 */
			in = new ConfigUtenteTipoServizioEnteSearchRequest();
			in.setCompanyCode("");
			in.setCodiceUtente("");
			in.setChiaveEnte("");
			in.setCodiceTipologiaServizio("");
			in.setOrder("");
			in.setPageNumber(0);
			in.setRowsPerPage(0);
			in.setStrDescrSocieta("");
			in.setStrDescrUtente("");
			in.setStrEnte("");
			in.setStrDescrTipologiaServizio("");
			try {
				res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes(in, request);
				if (res != null)
				{
					response = res.getResponse();
					if (response != null)
					{
						xml = response.getListXml();
					}
				}
				request.setAttribute(lista, xml);
				session.setAttribute(lista, xml);
			} catch (com.seda.payer.pgec.webservice.srv.FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

}