package org.seda.payer.manager.ioitalia.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.ManagerKeys;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.IoItaliaConfigurazioniList;
import com.seda.payer.core.dao.IoItaliaDao;
import com.seda.payer.core.exception.DaoException;

public class IoItaliaConfAction extends IoItaliaBaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codiceSocieta="";
	private String cutecute="";
	private String chiaveEnte="";
	private String chiaveEnteda5="";
	private String tipServ="";
	private String impServ="";
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
			setProfile(request);
		}
		if(firedButton.equals(FiredButton.TX_BUTTON_EDIT) || firedButton.equals(FiredButton.TX_BUTTON_INDIETRO)){
			codiceSocieta = (String) request.getAttribute("tx_societa");
			cutecute = (String) request.getAttribute("tx_utente");
			chiaveEnte = (String) request.getAttribute("tx_ente");
			tipServ = (String) request.getAttribute("tx_tipServ");
			impServ = (String) request.getAttribute("tx_impServ");			
		}
		loadSocietaUtenteEnteXml_DDL(request, session);
		
		//		YLM PG22XX06 INI
		String template = getTemplateCurrentApplication(request, request.getSession());
		if ( template.equals("aosta")) {
			loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, cutecute, chiaveEnte, true);
		} else {
			loadTipologiaServizioAPPIOXml_DDL(request, session, codiceSocieta, cutecute, chiaveEnte, true);
		}
		//		YLM PG22XX06 FINE
		loadListaImpostaServizio(request, session, codiceSocieta, cutecute, chiaveEnte, tipServ, true);
		switch(firedButton)
		{
			case TX_BUTTON_DELETE:
				try (Connection conn = payerDataSource.getConnection()) {
					if (conn.getAutoCommit())
						conn.setAutoCommit(false);
					IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
					ioItaliaDao.deleteConfigurazione(Long.parseLong((String) request.getAttribute("tx_id")));
					request.setAttribute("tx_message", "Configurazione cancellata con successo.");
					conn.commit();
				} catch (SQLException | NumberFormatException | DaoException e) {
					e.printStackTrace();
				} 
				resetParamsSessioneRequest(session, request);
			
			case TX_BUTTON_AGGIUNGI:
			case TX_BUTTON_EDIT:
			case TX_BUTTON_INDIETRO:
				if(codiceSocieta == null || codiceSocieta.equals("")) codiceSocieta = (String) request.getAttribute("tx_societa");
				if(cutecute == null || cutecute.equals("")) cutecute = (String) request.getAttribute("tx_utente");
				if(chiaveEnte == null || chiaveEnte.equals("")) chiaveEnte = (String) request.getAttribute("tx_ente");
				if(tipServ == null || tipServ.equals("")) tipServ = (String) request.getAttribute("tx_tipServ");
				if(impServ == null || impServ.equals("")) {
					impServ = (String) request.getAttribute("tx_impServ_s");
					request.setAttribute("tx_impServ", request.getAttribute("tx_impServ_s"));
				}
				try (Connection conn = payerDataSource.getConnection()) {
					
					IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
//					YLM PG22XX06 INI
					IoItaliaConfigurazioniList list = new IoItaliaConfigurazioniList();
					if ( template.equals("aosta")) {
						list = ioItaliaDao.configurazioniList(codiceSocieta, cutecute, chiaveEnte, tipServ, impServ, pageNumber, rowsPerPage, "");
					} else {
						list = ioItaliaDao.configurazioniListTail(codiceSocieta, cutecute, chiaveEnte, tipServ, impServ, pageNumber, rowsPerPage, "", true);
					}
//					YLM PG22XX06 FINE

					request.setAttribute("lista_configurazioni", list.getConfigurazioniListXml());
					request.setAttribute("lista_configurazioni.pageInfo", list);
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				break;
			case TX_BUTTON_CERCA: 
					mantieniFiltriRicerca(request);
					dividiSocUtenteEnte(request);
					codiceSocieta = (String) request.getAttribute("tx_societa");
					cutecute = (String) request.getAttribute("tx_utente");
					chiaveEnte = (String) request.getAttribute("tx_ente");
					tipServ = (String) request.getAttribute("tx_tipServ");
					impServ = (String) request.getAttribute("tx_impServ");
					try (Connection conn = payerDataSource.getConnection()) {
						
						IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);

						//		YLM PG22XX06 INI
						IoItaliaConfigurazioniList list = new IoItaliaConfigurazioniList();
						if ( template.equals("aosta")) {
							list = ioItaliaDao.configurazioniList(codiceSocieta, cutecute, chiaveEnte, tipServ, impServ, pageNumber, rowsPerPage, "");
						} else {
							list = ioItaliaDao.configurazioniListTail(codiceSocieta, cutecute, chiaveEnte, tipServ, impServ, pageNumber, rowsPerPage, "", true);
						}
						//		YLM PG22XX06 FINE
						request.setAttribute("lista_configurazioni", list.getConfigurazioniListXml());
						request.setAttribute("lista_configurazioni.pageInfo", list);
					} catch (DaoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					YLM PG22XX06 INI
					if ( template.equals("aosta")) {
						loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, cutecute, chiaveEnte, true);
					} else {
						loadTipologiaServizioAPPIOXml_DDL(request, session, codiceSocieta, cutecute, chiaveEnte, true);
					}
//					YLM PG22XX06 FINE
					loadListaImpostaServizio(request, session, codiceSocieta, cutecute, chiaveEnte, tipServ, true);
					break;
			case TX_BUTTON_SOCIETA_CHANGED:
				dividiSocUtenteEnte(request);
				codiceSocieta = (String) request.getAttribute("tx_societa");
				cutecute = (String) request.getAttribute("tx_utente");
				chiaveEnte = (String) request.getAttribute("tx_ente");
				tipServ = (String) request.getAttribute("tx_tipServ");
//				YLM PG22XX06 INI
				if ( template.equals("aosta")) {
					loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, cutecute, chiaveEnte, true);
				} else {
					loadTipologiaServizioAPPIOXml_DDL(request, session, codiceSocieta, cutecute, chiaveEnte, true);
				}
//				YLM PG22XX06 FINE
				loadListaImpostaServizio(request, session, codiceSocieta, cutecute, chiaveEnte, tipServ, true);
				break;
					
			case TX_BUTTON_RESET:
				resetParamsSessioneRequest(session, request);
				setProfile(request);				
				break;
				
			case TX_BUTTON_NULL: 
				session.setAttribute("codop", null);
				resetParamsSessioneRequest(session, request);
				break;
			
			default:
				break;
		}
		
		return null;
	}
	
	private void resetParamsSessioneRequest(HttpSession session, HttpServletRequest request) {
		session.setAttribute("tx_societa", null);
		session.setAttribute("tx_utente", null);
		session.setAttribute("tx_ente", null);
		session.setAttribute("tx_tipServ", null);
		session.setAttribute("tx_impServ_s", null);
		session.setAttribute("tx_id_s", null);
		request.setAttribute("tx_firstKey_s", null);
		request.setAttribute("tx_secondKey_s", null);
		request.setAttribute("tx_firstKey2_s", null);
		request.setAttribute("tx_secondKey2_s", null);
		request.setAttribute("tx_mail_s", null);

		request.setAttribute("tx_tipServ", null);
		request.setAttribute("tx_impServ", null);
		request.setAttribute("tx_tipServ_s", null);
		request.setAttribute("tx_impServ_s", null);
		request.setAttribute("ddlSocietaUtenteEnte", null);
		session.setAttribute("ddlSocietaUtenteEnte", null);
		session.setAttribute("tx_tipServ_s", null);
		session.setAttribute("tx_impServ_s", null);	
		session.setAttribute("tx_impServ", null);
		session.setAttribute("tx_societa_desc", null);
		session.setAttribute("tx_ente_desc", null);
		session.setAttribute("tx_codice_ente_desc", null);
		session.setAttribute("tx_UtenteEnte_desc", null);
		session.setAttribute("tx_tipServ_desc", null);
	}
	
	
	
	
	private void dividiSocUtenteEnte(HttpServletRequest request) {
		if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
		{
			String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			if (!ddlSocietaUtenteEnte.equals("")&&codici.length>0) 
			{
				codiceSocieta = codici[0];
				cutecute = codici[1];
				chiaveEnte = codici[2];
				if(codici.length > 3) chiaveEnteda5 = codici[3].substring(1, 6);
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", cutecute);
				request.setAttribute("tx_ente", chiaveEnte);
				request.setAttribute("tx_enteda5", chiaveEnteda5);

			} else {
				request.setAttribute("tx_societa", null);
				request.setAttribute("tx_utente", null);
				request.setAttribute("tx_ente", null);
				request.setAttribute("tx_enteda5", null);
			}
		}
	}
	
	private void mantieniFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
			session.setAttribute("ddlSocietaUtenteEnte", request.getAttribute("ddlSocietaUtenteEnte"));
		if(request.getAttribute("tx_tipServ_s")!=null)
			session.setAttribute("tx_tipServ_s", request.getAttribute("tx_tipServ_s"));
		if(request.getAttribute("tx_impServ_s")!=null) 
			session.setAttribute("tx_impServ_s", request.getAttribute("tx_impServ_s"));	
		
	}
	
}
