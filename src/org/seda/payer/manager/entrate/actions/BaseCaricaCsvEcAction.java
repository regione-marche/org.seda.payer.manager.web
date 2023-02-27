package org.seda.payer.manager.entrate.actions;

import java.rmi.RemoteException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class BaseCaricaCsvEcAction extends BaseManagerAction {
	
	private static final long serialVersionUID = 1L;
	
	protected String codiceSocieta="";
	protected String codiceProvincia="";
	protected String codiceUtente="";
	
	public void setProfile(HttpServletRequest request) {
		super.setProfile(request);			
		codiceProvincia = (String)request.getAttribute("tx_provincia") != null ? (String)request.getAttribute("tx_provincia"): "";
	}
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		loadDDLStatic(request, session);
		return null;
	}

	protected PageInfo getPageInfo(com.seda.payer.pgec.webservice.commons.dati.PageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPages());
		return pageInfo;
	}
	
	protected PageInfo getMovimentiPageInfo(com.seda.payer.pgec.webservice.commons.dati.PageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPages());
		return pageInfo;
	}
	
	protected void tx_SalvaStato(HttpServletRequest request) {
		super.tx_SalvaStato(request);
		request.setAttribute(Field.DATA_PAGAMENTO_DA.format(), getCalendar(request, Field.DATA_PAGAMENTO_DA.format()));
		request.setAttribute(Field.DATA_PAGAMENTO_A.format(), getCalendar(request, Field.DATA_PAGAMENTO_A.format()));
		request.setAttribute(Field.DATA_CREAZIONE_DA.format(), getCalendar(request, Field.DATA_CREAZIONE_DA.format()));
		request.setAttribute(Field.DATA_CREAZIONE_A.format(), getCalendar(request, Field.DATA_CREAZIONE_A.format()));
	}
	
	protected void resetDDLSession(HttpServletRequest request){
		HttpSession sessione = request.getSession();
		
		sessione.setAttribute("listaProvince", null);
		sessione.setAttribute("listaUtenti", null);
	}
	
	protected void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
	switch(getFiredButton(request)) 
	{
		case TX_BUTTON_SOCIETA_CHANGED:
			loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
			loadDDLUtente(request, session, getParamCodiceSocieta(), null, true);
			loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), null, true);
			break;
			
		case TX_BUTTON_PROVINCIA_CHANGED:
			loadProvinciaXml_DDL(request, session, null,false);
			loadDDLUtente(request, session, getParamCodiceSocieta(), codiceProvincia,true);
			loadListaGatewayXml_DDL(request, session, null, null, false);
			break;
		
		case TX_BUTTON_UTENTE_CHANGED:
			loadProvinciaXml_DDL(request, session, null,false);
			loadDDLUtente(request, session, null, null,false);
			loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
			break;
		
		case TX_BUTTON_RESET:
			resetParametri(request);

			request.setAttribute("dtGiornale_da", null);
			request.setAttribute("dtGiornale_a", null);
			
			request.setAttribute("dtMovimento_da", null);
			request.setAttribute("dtMovimento_a", null);
			
			codiceSocieta = "";
			codiceProvincia = "";
			codiceUtente = "";
			
			setParamCodiceSocieta("");
			setParamCodiceUtente("");
							
			loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
			loadDDLUtente(request, session, getParamCodiceSocieta(), codiceProvincia,true);
			loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
			break;
			
		default:
			loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
			loadDDLUtente(request, session, getParamCodiceSocieta(), codiceProvincia,true);
			loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
	}
	}	
}
