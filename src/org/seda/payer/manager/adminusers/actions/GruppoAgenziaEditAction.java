
package org.seda.payer.manager.adminusers.actions;


import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.Messages;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.GruppoAgenzia;
import com.seda.payer.core.dao.GruppoAgenziaDAOFactory;
import com.seda.payer.core.dao.GruppoAgenziaDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
@SuppressWarnings("serial")
public class GruppoAgenziaEditAction  extends GruppoAgenziaBaseManagerAction {



	private static String codop = "edit";
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		codop = (String)request.getAttribute("codop");
		FiredButton firedButton = getFiredButton(request);

		//		if (request.getAttribute("fired_button_hidden")!= null){
//			if (((String)request.getAttribute("fired_button_hidden")).equals("tx_button_tipo_sms")){
//				//firedButton =  firedButton.TX_BUTTON_ADD;
//				firedButton =  firedButton.TX_BUTTON_EDIT_END;
//			}			
//		}
		
		
		switch(firedButton)
		{
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			codop = "edit";
			try {
				GruppoAgenzia gruppoAgenzia = null;
				gruppoAgenzia = selectGruppoAgenzia(request);
				request.setAttribute("tx_codiceGruppoAgenzia", 	gruppoAgenzia.getCodiceGruppoAgenzia());
				request.setAttribute("tx_descrizioneGruppoAgenzia", gruppoAgenzia.getDescrizioneGruppoAgenzia());
			} catch (DaoException e) {
				e.printStackTrace();
				setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
			}

			break;
			
		case TX_BUTTON_EDIT:
			

			try {
				updateGruppoAgenzia(request);
			} catch (DaoException e) {
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
				request.setAttribute("tx_button_edit", null);
			}
			session.setAttribute("recordUpdate", "OK");
			request.setAttribute("tx_codiceGruppoAgenzia", null);
			request.setAttribute("tx_descrizioneGruppoAgenzia", null);
			request.setAttribute("tx_button_edit", null);
			request.setAttribute("tx_button_indietro", "ritorna");	
			break;
		
		case TX_BUTTON_NUOVO:
			request.setAttribute("tx_codiceGruppoAgenzia", null);
			request.setAttribute("tx_descrizioneGruppoAgenzia", null);
			break;
		case TX_BUTTON_ADD:
			codop = "add";
			break;
		case TX_BUTTON_AGGIUNGI:
			try {
									
				EsitoRisposte esito = new EsitoRisposte();
				esito = inserisciGruppoAgenzia(request);
				if(esito.getCodiceMessaggio().equals("OK"))
				{
					session.setAttribute("recordInsert", "OK");
				}
				else
				{
					session.setAttribute("recordInsert", "KO");
					session.setAttribute("messaggio.recordInsert", esito.getDescrizioneMessaggio());
					setFormMessage("var_form",  esito.getDescrizioneMessaggio(), request);
					request.setAttribute("tx_button_edit", null);
				}
			}
			catch (DaoException e) {
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
				request.setAttribute("tx_button_edit", null);
				request.setAttribute("tx_button_edit_end", "ok");
			}
			
			request.setAttribute("tx_codiceGruppoAgenzia", null);
			request.setAttribute("tx_descrizioneGruppoAgenzia", null);
			
			break;
			
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("tx_button_edit", null);
			request.setAttribute("tx_button_cerca", "ritorna");	
			request.setAttribute("tx_codiceGruppoAgenzia", null);
			request.setAttribute("tx_descrizioneGruppoAgenzia", null);
			break;
		
		case TX_BUTTON_CANCEL:
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteGruppoAgenzia(request);
					if(esito.getCodiceMessaggio().equals("OK"))
					{
						session.setAttribute("recordDelete", "OK");
					}
					else
					{
						session.setAttribute("recordDelete", "KO");
					}
					session.setAttribute("messaggio.recordDelete", esito.getDescrizioneMessaggio());
				}
			catch (DaoException e) {
						e.printStackTrace();
						setFormMessage("search_form", "Errore: " + e.getLocalizedMessage(), request);
					}
			finally{
				request.setAttribute("tx_codiceGruppoAgenzia", null);
				request.setAttribute("tx_descrizioneGruppoAgenzia", null);
			}
			break;
			
		}
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("codop",codop);
		return null;
	}

	private GruppoAgenzia selectGruppoAgenzia(HttpServletRequest request) throws DaoException 
	{
		GruppoAgenziaDao gruppoAgenziaDAO;
		GruppoAgenzia gruppoAgenzia = new GruppoAgenzia();
		String codiceGruppoAgenzia = (String) request.getAttribute("tx_codiceGruppoAgenzia");
	
		//inizio LP PG21XX04 Leak
		//gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(getGruppoAgenziaDataSource(), getGruppoAgenziaDbSchema());
		//gruppoAgenzia = gruppoAgenziaDAO.select(codiceGruppoAgenzia);
		Connection conn = null;
		try {
			conn = getGruppoAgenziaDataSource().getConnection();
			gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(conn, getGruppoAgenziaDbSchema());
			gruppoAgenzia = gruppoAgenziaDAO.select(codiceGruppoAgenzia);
		} catch (Exception e) {
			throw new DaoException(e);
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
		return gruppoAgenzia;
	}
	
	private Integer updateGruppoAgenzia(HttpServletRequest request) throws DaoException {
	
		GruppoAgenziaDao gruppoAgenziaDAO;
		GruppoAgenzia gruppoAgenzia = new GruppoAgenzia();
		
		gruppoAgenzia.setCodiceGruppoAgenzia((String) request.getAttribute("tx_codiceGruppoAgenzia"));
		gruppoAgenzia.setDescrizioneGruppoAgenzia((String) request.getAttribute("tx_descrizioneGruppoAgenzia"));
		
		//inizio LP PG21XX04 Leak
		//gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(getGruppoAgenziaDataSource(), getGruppoAgenziaDbSchema());
		//int aggiornati = gruppoAgenziaDAO.update(gruppoAgenzia);
		//
		//return aggiornati;
		Connection conn = null;
		try {
			conn = getGruppoAgenziaDataSource().getConnection();
			gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(conn, getGruppoAgenziaDbSchema());
			int aggiornati = gruppoAgenziaDAO.update(gruppoAgenzia);
			
			return aggiornati;
		} catch (Exception e) {
			throw new DaoException(e);
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
	}

	private EsitoRisposte inserisciGruppoAgenzia(HttpServletRequest request) throws DaoException 
	{
		
		String codGruppoAgenzia = (request.getAttribute("tx_codiceGruppoAgenzia")	== null? "" : (String)request.getAttribute("tx_codiceGruppoAgenzia"));
		String descGruppoAgenzia = (request.getAttribute("tx_descrizioneGruppoAgenzia")			== null? "" : (String)request.getAttribute("tx_descrizioneGruppoAgenzia"));
		
		GruppoAgenziaDao gruppoAgenziaDAO;
		GruppoAgenzia gruppoAgenzia = new GruppoAgenzia();
		gruppoAgenzia.setCodiceGruppoAgenzia(codGruppoAgenzia);
		gruppoAgenzia.setDescrizioneGruppoAgenzia(descGruppoAgenzia);
		
		//inizio LP PG21XX04 Leak
		//gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(getGruppoAgenziaDataSource(), getGruppoAgenziaDbSchema());
		//return gruppoAgenziaDAO.insert(gruppoAgenzia);
		Connection conn = null;
		try {
			conn = getGruppoAgenziaDataSource().getConnection();
			gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(conn, getGruppoAgenziaDbSchema());
			return gruppoAgenziaDAO.insert(gruppoAgenzia);
		} catch (Exception e) {
			throw new DaoException(e);
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
	}

	private EsitoRisposte deleteGruppoAgenzia(HttpServletRequest request) throws DaoException 
	{
		String codGruppoAgenzia = (String)request.getAttribute("tx_codiceGruppoAgenzia");
		
		GruppoAgenziaDao gruppoAgenziaDAO;
		
		//inizio LP PG21XX04 Leak
		//gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(getGruppoAgenziaDataSource(), getGruppoAgenziaDbSchema());
		//return gruppoAgenziaDAO.delete(codGruppoAgenzia);
		Connection conn = null;
		try {
			conn = getGruppoAgenziaDataSource().getConnection();
			gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(conn, getGruppoAgenziaDbSchema());
			return gruppoAgenziaDAO.delete(codGruppoAgenzia);
		} catch (Exception e) {
			throw new DaoException(e);
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
		
	}
}