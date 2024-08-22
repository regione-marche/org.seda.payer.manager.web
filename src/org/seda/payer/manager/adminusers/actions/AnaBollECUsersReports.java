package org.seda.payer.manager.adminusers.actions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.AnaBollLogReportsPageList;
import com.seda.payer.core.bean.AnagraficaBollettinoPageList;
import com.seda.payer.core.bean.AnagraficaBollettinoECReports;
import com.seda.payer.core.dao.AnagraficaBollettinoDAO;
import com.seda.payer.core.dao.AnagraficaBollettinoDAOFactory;

import com.seda.payer.core.exception.DaoException;

public class AnaBollECUsersReports extends AnaBollECUsersReportsBaseAction {
	private static final long serialVersionUID = 1L;

	private String codiceSocieta="";
	private String cutecute="";
	private String chiaveEnte="";

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		try
		{	 
			this.setErrorMessage("");
			HttpSession session = request.getSession();
			FiredButton firedButton = getFiredButton(request);
			if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
				setProfile(request);
			
			loadSocietaUtenteEnteXml_DDL(request, session);
			
			
			switch(firedButton)
			{
				case TX_BUTTON_CERCA: 
					try {
							dividiSocUtenteEnte(request);
							//if (!this.codiceSocieta.equals("")){
								AnagraficaBollettinoECReports res = getReportsAnaBoll(request);
								if (res!=null)
									request.setAttribute("anaboll", res.getAnaBollLogListXml());
								AnaBollLogReportsPageList anags = getLista(request,false);
								PageInfo pageInfo = anags.getPageInfo();
								
								if (anags.getRetCode()!="00") {
									setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
								} 
								else 
								{
										if(pageInfo != null)
										{
											if(pageInfo.getNumRows() > 0)
											{
												session.setAttribute("numRows",pageInfo.getNumRows()); 
												request.setAttribute("lista_anaboll", anags.getAnaBollLogReportsListXml());
												request.setAttribute("lista_anaboll.pageInfo", pageInfo);
											}
											else setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
										}
								}
							//}

						} catch(Exception e) {
							setErrorMessage(e.getMessage());
							e.printStackTrace();
						}
						break;

					
				case TX_BUTTON_RESET:
					resetParametri(request);
					request.setAttribute("tx_codfisc", null);
					session.setAttribute("tx_codfisc",null);
					request.setAttribute("tx_denom", null);
					session.setAttribute("tx_denom",null);
					request.setAttribute("rowsPerPage",null);
					session.setAttribute("rowsPerPage",null);
					session.setAttribute("order",null);
					request.setAttribute("order",null);
					request.setAttribute("tx_periodo_a", null);
					session.setAttribute("tx_periodo_a",null);
					request.setAttribute("tx_periodo_da", null);
					session.setAttribute("tx_periodo_da",null);
					request.setAttribute("tx_servizio", null);
					session.setAttribute("tx_servizio",null);
					session.setAttribute("ddlSocietaUtenteEnte",null);
					request.setAttribute("ddlSocietaUtenteEnte",null);
					setProfile(request);
					break;
					
				case TX_BUTTON_SOCIETA_CHANGED:
					break;
					
				case TX_BUTTON_PROVINCIA_CHANGED:
					break;
				
				case TX_BUTTON_ENTE_CHANGED:
					break;
					
					
				case TX_BUTTON_NULL: 
					session.setAttribute("tx_codfisc",null);
					session.setAttribute("tx_denom",null);
					session.setAttribute("rowsPerPage",null);
					session.setAttribute("order",null);
					session.setAttribute("tx_periodo_a",null);
					session.setAttribute("tx_periodo_da",null);
					session.setAttribute("tx_servizio",null);
					resetParametri(request);
					break;
					
				case TX_BUTTON_STAMPA:
					break;
					
				case TX_BUTTON_DOWNLOAD:
					try {
						request.setAttribute("download", "Y");
						dividiSocUtenteEnte(request);
						loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
						
						Integer maxRigheDownload = (Integer) context.getAttribute(ManagerKeys.MAXRIGHE_DOWNLOAD_BORSELLINO);
						if ((Integer) session.getAttribute("numRows") <= maxRigheDownload ){
							getLista(request,true);
						} else {
							setFormMessage("form_selezione", Messages.SUPERATO_MAXRIGHE.format(), request);
							request.setAttribute("download", "N");
							break;
						}
					} catch(Exception e) {
						setErrorMessage(e.getMessage());
						e.printStackTrace();
					}
					break;
				
				
			}
			
			request.setAttribute("tx_error_message", this.getErrorMessage());
			return null;
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private AnaBollLogReportsPageList getLista(HttpServletRequest request, boolean download) {
		AnagraficaBollettinoDAO controller;
		AnaBollLogReportsPageList res = null;
		String codiceFiscale = ((String)request.getAttribute("tx_codfisc") == null) ? "" : (String)request.getAttribute("tx_codfisc").toString().toUpperCase();
		String Denominazione = ((String)request.getAttribute("tx_denom") == null) ? "" : (String)request.getAttribute("tx_denom").toString().toUpperCase();
		String Servizio = ((String)request.getAttribute("tx_servizio") == null) ? "" : (String)request.getAttribute("tx_servizio").toString().toUpperCase();
		if (Servizio.length()>0 ){
			if (Servizio.equals("EC"))
				Servizio="estrattoconto";
			else
				Servizio="wallet";
		}

		String dataDa = "1900-01-01";
		String dataA = "01-01-2100";
		if(request.getAttribute("tx_periodo_da") !="")
		{
			dataDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_da"), "yyyy-MM-dd");
		}
			
		if(request.getAttribute("tx_periodo_a") !="")
		{
			dataA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_a"), "yyyy-MM-dd");
		}
		
		// paginazione ed ordinamento
		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(getAnagraficaDataSource(), getAnagraficaDbSchema());
			connection = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(connection, getAnagraficaDbSchema());
			//fine LP PG21XX04 Leak
			res = controller.doLogReportsList(codiceSocieta, cutecute, chiaveEnte, codiceFiscale, Denominazione,Servizio,dataDa,dataA, rowsPerPage, pageNumber, order);
			
		} catch (DaoException e1) {
			e1.printStackTrace();
		//inizio LP PG21XX04 Leak
		//}
		} catch (JndiProxyException e) {
			e.printStackTrace();
		}
		finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		return res;
	}
	
	private AnagraficaBollettinoECReports getReportsAnaBoll(HttpServletRequest request) {
		AnagraficaBollettinoDAO controller;
		AnagraficaBollettinoECReports res = null;
		String codiceFiscale = ((String)request.getAttribute("tx_codfisc") == null) ? "" : (String)request.getAttribute("tx_codfisc").toString().toUpperCase();
		String Denominazione = ((String)request.getAttribute("tx_denom") == null) ? "" : (String)request.getAttribute("tx_denom").toString().toUpperCase();
		String Servizio = ((String)request.getAttribute("tx_servizio") == null) ? "" : (String)request.getAttribute("tx_servizio").toString().toUpperCase();
		if (Servizio.length()>0 ){
			if (Servizio.equals("EC"))
				Servizio="estrattoconto";
			else
				Servizio="wallet";
		}

		String dataDa = "1900-01-01";
		String dataA = "01-01-2100";
		if(request.getAttribute("tx_periodo_da") !="")
		{
			dataDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_da"), "yyyy-MM-dd");
		}
			
		if(request.getAttribute("tx_periodo_a") !="")
		{
			dataA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_a"), "yyyy-MM-dd");
		}
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(getAnagraficaDataSource(), getAnagraficaDbSchema());
			connection = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(connection, getAnagraficaDbSchema());
			//fine LP PG21XX04 Leak
			res = controller.doReportECABRS(codiceSocieta, cutecute, chiaveEnte,codiceFiscale, Denominazione,Servizio,dataDa,dataA);
		} catch (DaoException e1) {
			e1.printStackTrace();
			this.setErrorMessage("Errore nel recupero dei dati!");
		} catch (IOException e) {
			e.printStackTrace();
			this.setErrorMessage("Errore nel recupero dei dati!");
		//inizio LP PG21XX04 Leak
		//}
		} catch (JndiProxyException e) {
			e.printStackTrace();
			this.setErrorMessage("Errore nel recupero dei dati!");
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		return res;
	}
	
	private void dividiSocUtenteEnte(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String ddlSocietaUtenteEnte ="";
		if (request.getAttribute("ddlSocietaUtenteEnte")!=null && !request.getAttribute("ddlSocietaUtenteEnte").toString().equals("")){
			ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			if (session.getAttribute("ddlSocietaUtenteEnte")==null){
				session.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
			if (!request.getAttribute("ddlSocietaUtenteEnte").toString().equals(session.getAttribute("ddlSocietaUtenteEnte").toString())){
				session.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
		}
		else{
			if (session.getAttribute("ddlSocietaUtenteEnte")!=null && !session.getAttribute("ddlSocietaUtenteEnte").toString().equals("")){
				ddlSocietaUtenteEnte = (String)session.getAttribute("ddlSocietaUtenteEnte");
				request.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
			
		}
		
		if (!ddlSocietaUtenteEnte.equals(""))
		{
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			codiceSocieta = codici[0];
			cutecute = codici[1];
			chiaveEnte = codici[2];
		}
		request.setAttribute("tx_societa", codiceSocieta);
		request.setAttribute("tx_utente", cutecute);
		request.setAttribute("tx_ente", chiaveEnte);
		
		//Mantenimento di variabili in caso di paginazione La paginazione viene gfestita da un'altro FORM
		if (request.getAttribute("tx_codfisc") != null)
			session.setAttribute("tx_codfisc",request.getAttribute("tx_codfisc"));
		else if(session.getAttribute("tx_codfisc")!=null)
				request.setAttribute("tx_codfisc",session.getAttribute("tx_codfisc"));

		if (request.getAttribute("tx_denom") != null)
			session.setAttribute("tx_denom",request.getAttribute("tx_denom"));
		else if(session.getAttribute("tx_denom")!=null)
				request.setAttribute("tx_denom",session.getAttribute("tx_denom"));
		
		if (request.getAttribute("rowsPerPage") != null)
			session.setAttribute("rowsPerPage",request.getAttribute("rowsPerPage"));
		else if(session.getAttribute("rowsPerPage")!=null)
				request.setAttribute("rowsPerPage",session.getAttribute("rowsPerPage"));
		
		if (request.getAttribute("order") != null)
			session.setAttribute("order",request.getAttribute("order"));
		else if(session.getAttribute("order")!=null)
				request.setAttribute("order",session.getAttribute("order"));

		if (request.getAttribute("tx_servizio") != null)
			session.setAttribute("tx_servizio",request.getAttribute("tx_servizio"));
		else if(session.getAttribute("tx_servizio")!=null)
				request.setAttribute("tx_servizio",session.getAttribute("tx_servizio"));
		
		if (request.getAttribute("tx_periodo_da") != null)
			session.setAttribute("tx_periodo_da",request.getAttribute("tx_periodo_da"));
		else if(session.getAttribute("tx_periodo_da")!=null)
				request.setAttribute("tx_periodo_da",session.getAttribute("tx_periodo_da"));
			
		if (request.getAttribute("tx_periodo_a") != null)
			session.setAttribute("tx_periodo_a",request.getAttribute("tx_periodo_a"));
		else if(session.getAttribute("tx_periodo_a")!=null)
				request.setAttribute("tx_periodo_a",session.getAttribute("tx_periodo_a"));
	}
}
