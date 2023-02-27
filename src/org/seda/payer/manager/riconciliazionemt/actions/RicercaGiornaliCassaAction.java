package org.seda.payer.manager.riconciliazionemt.actions;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.portlet.PortletRequestContext;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.reports.stampe.ReportsCreator;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesNodeException;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.compatibility.SystemVariable;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.taglibs.ViewStateManager;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.riconciliazionemt.bean.GiornaleDiCassaPageList;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAO;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAOFactory;


public class RicercaGiornaliCassaAction extends BaseRiconciliazioneTesoreriaAction {
	
	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String codUtenteEnte;
	public static PropertiesTree configuration;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		
		tx_SalvaStato(request);
		
		resetFiltriSession(request);

		super.service(request);
        aggiornamentoCombo(request, session);
        loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
        
        String siglaProvincia = ((String)request.getAttribute("tx_provincia") == null) ? "" : (String)request.getAttribute("tx_provincia");
               
        try {
        	codUtenteEnte = (String)request.getAttribute("tx_UtenteEnte");
        	listaPsp(getParamCodiceSocieta(), codUtenteEnte, 0, request);
		} catch (DaoException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
        
        rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));	
		order = request.getParameter("order");
		
		if (request.getAttribute("indietro") != null){
			request.setAttribute("lista_giornali_di_cassa", session.getAttribute("lista_giornali"));
			request.setAttribute("lista_giornali_di_cassa.pageInfo", session.getAttribute("lista_giornali.pageInfo"));
		}
		
        switch(getFiredButton(request)) {
			case TX_BUTTON_CERCA: 
				
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), false);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), false);
				
				try {				
					GiornaleDiCassaPageList gdcPageListlist = listaGiornaliDiCassa(request, rowsPerPage, pageNumber, order);
					
					PageInfo pageInfo = gdcPageListlist.getPageInfo();
					
					if (gdcPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								session.setAttribute("lista_giornali", gdcPageListlist.getGiornaleDiCassaListXml());
								session.setAttribute("lista_giornali.pageInfo", pageInfo);
								request.setAttribute("lista_giornali_di_cassa", gdcPageListlist.getGiornaleDiCassaListXml());
								request.setAttribute("lista_giornali_di_cassa.pageInfo", pageInfo);
							}
							else { 
								request.setAttribute("lista_giornali_di_cassa", null);
								setFormMessage("ricercaGiornaliCassaForm", Messages.NO_DATA_FOUND.format(), request);
							}
						}
						else { 
							setFormMessage("ricercaGiornaliCassaForm", "Errore generico - Impossibile recuperare i dati", request);
						}
					}
				} catch(Exception e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				}
				break;
			case TX_BUTTON_STAMPA:

				//inizio LP PG21XX04 Bug configuration
				//try {
				//	getConfiguration();
				//} catch (PropertiesNodeException e) {
				//	e.printStackTrace();
				//}
				getConfiguration(request);
				//fine LP PG21XX04 Bug configuration
				
				String pathNomeFileReport = "";
				String templateName="";
				
				MAFRequest mafReq = new MAFRequest(request);
				String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
				UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
				templateName = userBean.getTemplate(applicationName);	

				ReportsCreator reportsCreator = new ReportsCreator(
						getBirtOutputPath(templateName),
						getBirtDesignPath(templateName), 
						null);
				
				String ente = "";
				codUtenteEnte = (String)request.getAttribute("tx_UtenteEnte");
				
				if(!"".equals(codUtenteEnte))  ente = codUtenteEnte.substring(10);
				
				String dataGiornaleDa_day = (String)request.getAttribute("dtGiornale_da_day");
				String dataGiornaleDa_month = (String)request.getAttribute("dtGiornale_da_month");
				String dataGiornaleDa_year = (String)request.getAttribute("dtGiornale_da_year");
				String dataGiornaleA_day = (String)request.getAttribute("dtGiornale_a_day");
				String dataGiornaleA_month = (String)request.getAttribute("dtGiornale_a_month");
				String dataGiornaleA_year = (String)request.getAttribute("dtGiornale_a_year");		
				String dataGiornaleDa = dataGiornaleDa_year+dataGiornaleDa_month+dataGiornaleDa_day;
				String dataGiornaleA = dataGiornaleA_year+dataGiornaleA_month+dataGiornaleA_day;
				
				String dataMovimentoDa_day = (String)request.getAttribute("dtMovimento_da_day");
				String dataMovimentoDa_month = (String)request.getAttribute("dtMovimento_da_month");
				String dataMovimentoDa_year = (String)request.getAttribute("dtMovimento_da_year");
				String dataMovimentoA_day = (String)request.getAttribute("dtMovimento_a_day");
				String dataMovimentoA_month = (String)request.getAttribute("dtMovimento_a_month");
				String dataMovimentoA_year = (String)request.getAttribute("dtMovimento_a_year");		
				String dataMovimentoDa = dataMovimentoDa_year+dataMovimentoDa_month+dataMovimentoDa_day;
				String dataMovimentoA = dataMovimentoA_year+dataMovimentoA_month+dataMovimentoA_day;
					
				String idFlussoCassa = (String) request.getAttribute("idcassa");
				String numDocumento = (String)request.getAttribute("numDocumento");
				String psp = (String)request.getAttribute("psp");
				String paramPathLogoSocieta = getParamPathLogoSocieta(templateName);
				
				//inizio LP PG21XX04 Leak
				//GiornaleDiCassaDAO giornaleDiCassaDAO;
				//fine LP PG21XX04 Leak
				Connection connection = null;
				try {
				//inizio LP PG21XX04 Leak
				//	giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
				//	connection = giornaleDiCassaDAO.getGDCConnection();
				//} catch (DaoException e) {
				//	// TODO Auto-generated catch block
				//e.printStackTrace();
				//}
					connection = getGdcDataSource().getConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				//fine LP PG21XX04 Leak
				
				String schema = getGdcDbSchema();
				
				pathNomeFileReport = reportsCreator.generaPdfGiornaliDiCassa(ente,dataGiornaleDa,dataGiornaleA,idFlussoCassa,dataMovimentoDa,dataMovimentoA,numDocumento,psp,paramPathLogoSocieta, connection, schema);
				
				if(pathNomeFileReport!=null && new File( pathNomeFileReport ).exists() ){
					File f = new File(pathNomeFileReport);

					request.setAttribute("stampa", pathNomeFileReport);
					request.setAttribute("filename", f.getName());
				} else {
					setFormMessage("ricercaGiornaliCassaForm", "Errore nella creazione del file PDF", request);
				}
				
			case TX_BUTTON_RESET:
				session.setAttribute("lista_giornali", "");
				request.setAttribute("lista_giornali_di_cassa", "");
				
			default: 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), true);
				break;
        }
		
		return null;
	}
	
	public static String getBirtOutputPath(String template) {
		return configuration.getProperty(PropertiesPath.birtOutputPath.format(template)).trim();
	}
	public static String getBirtDesignPath(String template) {
		return configuration.getProperty(PropertiesPath.birtDesignPath.format(template)).trim();
	}
	public static String getParamPathLogoSocieta(String template) {
		return configuration.getProperty(PropertiesPath.paramPathLogoSocieta.format(template)).trim();	
	}

	
	//inizio LP PG21XX04 Bug configuration
	//public void getConfiguration() throws PropertiesNodeException {
	//	SystemVariable sv = new SystemVariable();
	//	String rootPath = sv.getSystemVariableValue(ManagerKeys.ENV_CONFIG_FILE);
	//	sv = null;
	//
	//	if (rootPath != null) {
	//		// caricamento configurazioni esterne
	//		configuration = new PropertiesTree(rootPath);
	//	}
	//}
	public void getConfiguration(HttpServletRequest request) {
		configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
	}
	//fine LP PG21XX04 Bug configuration
	
	private void resetFiltriSession(HttpServletRequest request){
		HttpSession session = request.getSession();
		
		session.removeAttribute("tx_UtenteEnte");
		session.removeAttribute("tx_provincia");
		session.removeAttribute("tx_societa");
		session.removeAttribute("dtGiornale_a");
		session.removeAttribute("dtGiornale_da");
		session.removeAttribute("idcassa");
		session.removeAttribute("provenienza");
		
		
	}

}
