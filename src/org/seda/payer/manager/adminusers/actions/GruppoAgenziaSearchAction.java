
package org.seda.payer.manager.adminusers.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.seda.payer.manager.util.Messages;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.GruppoAgenzia;
import com.seda.payer.core.bean.GruppoAgenziaPageList;
import com.seda.payer.core.dao.GruppoAgenziaDAOFactory;
import com.seda.payer.core.dao.GruppoAgenziaDao;
import com.seda.payer.core.exception.DaoException;
@SuppressWarnings("serial")
public class GruppoAgenziaSearchAction extends GruppoAgenziaBaseManagerAction{

	private int rowsPerPage;
	private int pageNumber; 
	private String order;
	
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		super.service(request);
		
		HttpSession session = request.getSession();
		/*
		 * Resetto l'eventuale messaggio rimasto nel viewstate
		 */
		resetFormMessage(request);
		session.removeAttribute("listaGruppiAgenzia");
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);

		/*
		 * Azzero il campo hidden "fired_button_hidden" per evitare
		 * che possa essere riproposto nel caso in cui interviene 
		 * il "validator"
		 */
		request.setAttribute("fired_button_hidden", "");
		/*
		 * Se è presente in sessione uno degli attributi "addUserRetMessage" 
		 * o "editUserRetMessage" vuol dire che è stato inserito/modificato
		 * correttamente un utente e devo visualizzare il messaggio
		 */

		if ((String) session.getAttribute("recordInsert") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA; 
			if(session.getAttribute("recordInsert").toString().equals("OK"))
				setFormMessage("search_form", Messages.INS_OK.format(), request);
			else
				setFormMessage("search_form", session.getAttribute("messaggio.recordInsert").toString(), request);
			
			session.removeAttribute("recordInsert");
		}
		
		if ((String) session.getAttribute("recordUpdate") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA;
			if(session.getAttribute("recordUpdate").toString().equals("OK"))
				setFormMessage("search_form", Messages.UPDT_OK.format(), request);
			else
				setFormMessage("search_form", session.getAttribute("messaggio.recordUpdate").toString(), request);
			
			session.removeAttribute("recordUpdate");
		}
		if ((String) session.getAttribute("recordDelete") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA;
			if(session.getAttribute("recordDelete").toString().equals("OK"))
				setFormMessage("search_form", Messages.CANC_OK.format(), request);
			else
				setFormMessage("search_form", session.getAttribute("messaggio.recordDelete").toString(), request);
			
			session.removeAttribute("recordDelete");
			
		}
		/*
		 *  Ricavo i parametri della request
		 */
	
		
	
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		
		/*
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		switch(firedButton)
		{
			case TX_BUTTON_CERCA: 
				try {
					/*
					 * Recupero i dati
					 */
					GruppoAgenziaPageList gruppoAgenziaPageList = getGruppoAgenziaList(request);
					
					if (gruppoAgenziaPageList.getRetCode().equals("00"))
					{
						PageInfo wsPageInfo = gruppoAgenziaPageList.getPageInfo();
						if(wsPageInfo != null)
						{
							if(wsPageInfo.getNumRows()>0)
							{
								/*
								 * Recupero le informazioni di paginazione
								 * da passare alla jsp
								 */
								PageInfo pageInfo = new PageInfo();
								pageInfo.setFirstRow(wsPageInfo.getFirstRow());
								pageInfo.setLastRow(wsPageInfo.getLastRow());
								pageInfo.setNumPages(wsPageInfo.getNumPages());
								pageInfo.setNumRows(wsPageInfo.getNumRows());
								pageInfo.setPageNumber(wsPageInfo.getPageNumber());
								pageInfo.setRowsPerPage(wsPageInfo.getRowsPerPage());
								
								request.setAttribute("listaGruppiAgenzia", gruppoAgenziaPageList.getGruppoAgenziaListXml());
								request.setAttribute("listaUsers.pageInfo", pageInfo);
							}
							else setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
						}
						else setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
					}
					else
					{
						String messaggio = gruppoAgenziaPageList.getMessage(); 
						setFormMessage("search_form", (messaggio == null || messaggio.equals("")) ? "Errore generico" : "Errore: "+ messaggio, request);
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			request.setAttribute("tx_button_indietro", null);
			request.setAttribute("tx_button_cerca", "search");
			
			

				break;
			case TX_BUTTON_DOWNLOAD:
				String file="";
//				try {
//					ListaUtenzeCsvRequest reqCsv = new ListaUtenzeCsvRequest();
//					reqCsv.setUserName(userName);
//					reqCsv.setDataFineValiditaUtenza_da(dataFineValiditaUtenzaDa);
//					reqCsv.setDataFineValiditaUtenza_a(dataFineValiditaUtenzaA);
//					reqCsv.setUtenzaAttiva(utenzaAttiva);
//					reqCsv.setNumeroAutorizzazione(numeroAutorizzazione);
//					reqCsv.setEmailPersonaFisicaDelegato(emailPersonaFisicaDelegato);
//					reqCsv.setCognomeNomePersonaFisicaDelegato(cognomeNomePersonaFisicaDelegato);
//					reqCsv.setRagioneSociale(ragioneSociale);
//					
//					ListaUtenzeCsvResponse resCsv = WSCache.adminUsersServer.listaUtenzeCsv(reqCsv, request);
//					if (resCsv != null && resCsv.getResponse() != null && resCsv.getResponse().getRetCode().equals("00"))
//					{
//						//WSCache.logWriter.logInfo("Lunghezza stringa csv download: " + resCsv.getStringaCsv().length() + ". Lunghezza in Bytes: " + resCsv.getStringaCsv().getBytes().length);
//						file = resCsv.getStringaCsv();
//						//aggiustamento carattere \r perso nel webservice 
//						file = file.replaceAll("\n", "\r\n");
//						Calendar cal = Calendar.getInstance();
//						String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
//						request.setAttribute("filename",timestamp + "_ListaUtenze.csv");
//						request.setAttribute("fileCsv", file);
//					}
//					else
//					{
//						setFormMessage("search_form", "Si è verificato un errore durante l'esportazione dei dati" , request);
//					}
//				} catch (FaultType e1) {
//					setFormMessage("search_form", e1.getMessage() , request);
//					e1.printStackTrace();
//				} catch (RemoteException e1) {
//					setFormMessage("search_form", e1.getMessage() , request);
//					e1.printStackTrace();
//				}
				break;
			case TX_BUTTON_RESET:
				resetParametri(request);
				break;
				
			case TX_BUTTON_NULL: 
				break;
				
			case TX_BUTTON_INDIETRO:
				/*
				 * Resetto i campi di input nel caso in cui
				 * la richiesta (POST) giunga dalla form 
				 * di inserimento o di modifica
				 */
				request.setAttribute("tx_button_indietro", null);
				request.setAttribute("tx_button_cerca", "search");
				request.setAttribute("tx_codiceGruppoAgenzia", null);
				request.setAttribute("tx_descrizioneGruppoAgenzia", null);
				break;
		}
		return null;
	}
	
	private GruppoAgenziaPageList getGruppoAgenziaList(HttpServletRequest request) {
		GruppoAgenziaDao gruppoAgenziaDAO;
		
		String descrizioneGruppoAgenzia = ((String) request.getAttribute("tx_descrizioneGruppoAgenzia") == null ? "" : (String) request.getAttribute("tx_descrizioneGruppoAgenzia"));
		String codiceGruppoAgenzia = ((String)request.getAttribute("tx_codiceGruppoAgenzia") == null ? "" : (String)request.getAttribute("tx_codiceGruppoAgenzia"));
		
		GruppoAgenzia gruppoAgenzia = new GruppoAgenzia();
		gruppoAgenzia.setCodiceGruppoAgenzia(codiceGruppoAgenzia);
		gruppoAgenzia.setDescrizioneGruppoAgenzia(descrizioneGruppoAgenzia);
		
		GruppoAgenziaPageList gruppoAgenziaPageList = new GruppoAgenziaPageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(getGruppoAgenziaDataSource(), getGruppoAgenziaDbSchema());
			conn = getGruppoAgenziaDataSource().getConnection();
			gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(conn, getGruppoAgenziaDbSchema());
			//fine LP PG21XX04 Leak
			gruppoAgenziaPageList = gruppoAgenziaDAO.gruppoAgenziaList(gruppoAgenzia, rowsPerPage, pageNumber,"");
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		
		return gruppoAgenziaPageList;
		
	}


}