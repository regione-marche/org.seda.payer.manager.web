package org.seda.payer.manager.ioitalia.actions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.IoItaliaConfigurazione;
import com.seda.payer.core.dao.IoItaliaDao;
import com.seda.payer.core.exception.DaoException;
public class IoItaliaConfEditAction extends IoItaliaBaseManagerAction {

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
	private String wsChiavePrimaria="";
	private String wsChiaveSecondaria="";
	private String ioChiavePrimaria="";
	private String ioChiaveSecondaria="";
	private String mail="";
	private BigDecimal importoMinimo;
	private BigDecimal importoMassimo;
	private boolean abilitato;
	private String codop = "";
	

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);
		codop = (String)request.getAttribute("codop");
		String template = getTemplateCurrentApplication(request, request.getSession());
		if(codop != null) {
			if (codop.equalsIgnoreCase("edit")) {
				try (Connection conn = payerDataSource.getConnection()) {
					
					IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
					
					// YLM PG22XX06 INI
					IoItaliaConfigurazione ioItaliaConfigurazione = new IoItaliaConfigurazione();
					if ( template.equals("aosta")) {
						ioItaliaConfigurazione = ioItaliaDao.selectConfigurazione(Long.parseLong((String) request.getAttribute("tx_id")));
					} else {
						ioItaliaConfigurazione = ioItaliaDao.selectConfigurazioneTail(Long.parseLong((String) request.getAttribute("tx_id")), true);
					}
					// YLM PG22XX06 FINE
					
					session.setAttribute("tx_id_s", request.getAttribute("tx_id"));
					session.setAttribute("tx_societa", ioItaliaConfigurazione.getCodiceSocieta());
					session.setAttribute("tx_desc_societa", ioItaliaConfigurazione.getDescrizioneSocieta());
					session.setAttribute("tx_ente", ioItaliaConfigurazione.getCodiceEnte());
					session.setAttribute("tx_desc_ente", ioItaliaConfigurazione.getDescrizioneEnte());
					session.setAttribute("tx_utente", ioItaliaConfigurazione.getCodiceUtente());
					session.setAttribute("tx_desc_utente", ioItaliaConfigurazione.getDescrizioneUtente());
					session.setAttribute("tx_tipServ", ioItaliaConfigurazione.getTipologiaServizio());
					session.setAttribute("tx_desc_tipServ", ioItaliaConfigurazione.getDescrizioneTipologiaServizio());
					session.setAttribute("tx_impServ_s", ioItaliaConfigurazione.getImpostaServizio());
					session.setAttribute("tx_desc_impServ_s", ioItaliaConfigurazione.getDescrizioneImpostaServizio());
					request.setAttribute("tx_firstKey_s", ioItaliaConfigurazione.getWsKey1());
					request.setAttribute("tx_secondKey_s", ioItaliaConfigurazione.getWsKey2()); 
					request.setAttribute("tx_firstKey2_s" ,ioItaliaConfigurazione.getIoKey1());
					request.setAttribute("tx_secondKey2_s", ioItaliaConfigurazione.getIoKey2());
					request.setAttribute("tx_mail_s", ioItaliaConfigurazione.getEmail());
					request.setAttribute("tx_impMin_s", ioItaliaConfigurazione.getImportoDa().toString().replace(".", ","));
					request.setAttribute("tx_impMax_s", ioItaliaConfigurazione.getImportoA().toString().replace(".", ","));
					request.setAttribute("tx_abilitato_s", ioItaliaConfigurazione.isAbilitato());
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		loadSocietaUtenteEnteXml_DDL(request, session);
		dividiSocUtenteEnte(request);

		//		YLM PG22XX06 INI
		if ( template.equals("aosta")) {
			loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, cutecute, chiaveEnte, true);
		} else {
			loadTipologiaServizioAPPIOXml_DDL(request, session, null, null, null, true);
		}
		//		YLM PG22XX06 FINE
		
		tipServ = (String) request.getAttribute("tx_tipServ");
		if(tipServ != null) loadListaImpostaServizio(request, session, codiceSocieta, cutecute, chiaveEnte, tipServ, true);
		
		switch(firedButton) {
			case TX_BUTTON_RESET:
				if(session.getAttribute("codop") != null && session.getAttribute("codop").equals("edit")) {
					request.setAttribute("tx_firstKey_s", null);
					request.setAttribute("tx_secondKey_s", null);
					request.setAttribute("tx_firstKey2_s", null);
					request.setAttribute("tx_secondKey2_s", null);
					request.setAttribute("tx_mail_s", null);
					request.setAttribute("tx_impMin_s", null);
					request.setAttribute("tx_impMax_s", null);
					request.setAttribute("tx_abilitato_s", null);
				} else {
					request.setAttribute("tx_tipServ", null);
					request.setAttribute("tx_impServ_s", null);
					request.setAttribute("ddlSocietaUtenteEnte", null);
					resetParamsSessioneRequest(session, request);
					session.setAttribute("codop", null);
				}
				break;
			case TX_BUTTON_NULL:
				if(request.getAttribute("codop").equals("edit")) {
					session.setAttribute("codop", request.getAttribute("codop"));
				}
				break;
			case TX_BUTTON_EDIT:
				int idEdit = 0;
				try (Connection conn = payerDataSource.getConnection()) {
					if (conn.getAutoCommit())
						conn.setAutoCommit(false); 
					
					IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
					
					codiceSocieta = (String) session.getAttribute("tx_societa");
					cutecute = (String) session.getAttribute("tx_utente");
					chiaveEnte = (String) session.getAttribute("tx_ente");
					tipServ = (String) session.getAttribute("tx_tipServ");
					impServ = (String) session.getAttribute("tx_impServ_s");
					wsChiavePrimaria = (String) request.getAttribute("tx_firstKey_s");
					wsChiaveSecondaria = (String) request.getAttribute("tx_secondKey_s");
					ioChiavePrimaria = (String) request.getAttribute("tx_firstKey2_s");
					ioChiaveSecondaria = (String) request.getAttribute("tx_secondKey2_s");
					mail = (String) request.getAttribute("tx_mail_s");
					if(!request.getAttribute("tx_abilitato_s").equals("")) {
						abilitato = Boolean.parseBoolean((String)request.getAttribute("tx_abilitato_s"));
					}
					if(request.getAttribute("tx_impMin_s") != null && !request.getAttribute("tx_impMin_s").equals("")) {
						String impMin = ((String)request.getAttribute("tx_impMin_s")).trim().replace(',', '.');
						importoMinimo = BigDecimal.valueOf(Double.parseDouble(impMin));
					}
					if(request.getAttribute("tx_impMax_s") != null && !request.getAttribute("tx_impMax_s").equals("")) {
						String impMax = ((String)request.getAttribute("tx_impMax_s")).trim().replace(',', '.');
						importoMassimo = BigDecimal.valueOf(Double.parseDouble(impMax));
					}
					
					if(importoMinimo.compareTo(importoMassimo) < 0) {
						IoItaliaConfigurazione configurazione = new IoItaliaConfigurazione(Long.parseLong((String) session.getAttribute("tx_id_s")), codiceSocieta, cutecute, chiaveEnte, tipServ, impServ, wsChiavePrimaria, wsChiaveSecondaria, ioChiavePrimaria, ioChiaveSecondaria, mail, abilitato, importoMinimo, importoMassimo);
//						YLM PG22XX06 INI
						IoItaliaConfigurazione conf = new IoItaliaConfigurazione();
						
						if ( template.equals("aosta")) {
							conf = ioItaliaDao.selectConfigurazione((String) request.getAttribute("tx_firstKey_s"));
						} else {
							conf = ioItaliaDao.selectConfigurazioneTail((String) request.getAttribute("tx_firstKey_s"), true);
						}
//						conf = ioItaliaDao.selectConfigurazione((String) request.getAttribute("tx_firstKey_s"));
//						YLM PG22XX06 INI					
						
						if(conf != null && conf.getIdConfigurazione() != configurazione.getIdConfigurazione() && conf.getWsKey1().equals(configurazione.getWsKey1())) {
							request.setAttribute("tx_messageKey", "La chiave primaria è già stata utilizzata, riprova.");
							request.setAttribute("tx_firstKey_s", null);
						} else {
							idEdit = ioItaliaDao.updateConfigurazione(configurazione);
						}
						
					} else {
						request.setAttribute("tx_error_message", "ERRORE: L'importo minimo non può superare l'importo massimo.");
						request.setAttribute("tx_messageKey", "");
						session.setAttribute("codop", "add");
					}
					
					conn.commit();
					
				} catch (DaoException | SQLException e) {
					// TODO INSERIRE MESSAGGIO DI ERRORE
					request.setAttribute("tx_message_error", "ERRORE");
					e.printStackTrace();
				}
				if (idEdit != 0) {
					request.setAttribute("tx_message", "Configurazione aggiornata correttamente.");
					request.setAttribute("tx_societa", session.getAttribute("tx_societa"));
					request.setAttribute("tx_ente", session.getAttribute("tx_ente"));
					request.setAttribute("tx_tipServ", session.getAttribute("tx_tipServ"));
					request.setAttribute("tx_impServ", session.getAttribute("tx_impServ_s"));
					session.setAttribute("tx_societa", null);
					session.setAttribute("tx_ente", null);
					session.setAttribute("tx_tipServ", null);
					session.setAttribute("tx_impServ_s", null);
					session.setAttribute("codop", "add");
					request.setAttribute("tx_messageKey", "");
				}
				request.setAttribute("ddlSocietaUtenteEnte", codiceSocieta + "|" + cutecute + "|" + chiaveEnte);
				break;
			case TX_BUTTON_SOCIETA_CHANGED:
				if((String)request.getAttribute("codop") != "edit") {					
					session.setAttribute("codop", "add");
				}
				dividiSocUtenteEnte(request);
				// YLM PG22XX06 INI
				if ( template.equals("aosta")) {
					loadTipologiaServizioXml_DDL_2(request, session, codiceSocieta, cutecute, chiaveEnte, true);
				} else {
					loadTipologiaServizioAPPIOXml_DDL(request, session, null, null, null, true);
				}
				// YLM PG22XX06 FINE
				tipServ = (String) request.getAttribute("tx_tipServ");
				if(tipServ != null)loadListaImpostaServizio(request, session, codiceSocieta, cutecute, chiaveEnte, tipServ, true);
				break;
			case TX_BUTTON_AGGIUNGI:
				dividiSocUtenteEnte(request);
				tipServ = (String) request.getAttribute("tx_tipServ");
				impServ = (String) request.getAttribute("tx_impServ_s");
				wsChiavePrimaria = (String) request.getAttribute("tx_firstKey_s");
				wsChiaveSecondaria = (String) request.getAttribute("tx_secondKey_s");
				ioChiavePrimaria = (String) request.getAttribute("tx_firstKey2_s");
				ioChiaveSecondaria = (String) request.getAttribute("tx_secondKey2_s");
				mail = (String) request.getAttribute("tx_mail_s");
				long id = 0;
				if(!request.getAttribute("tx_abilitato_s").equals("")) {
					abilitato = Boolean.parseBoolean((String)request.getAttribute("tx_abilitato_s"));
				}
				
				if(!request.getAttribute("tx_impMin_s").equals("")) {
					String impMin = ((String)request.getAttribute("tx_impMin_s")).trim().replace(',', '.');
					importoMinimo = BigDecimal.valueOf(Double.parseDouble(impMin));
				}
				if(!request.getAttribute("tx_impMax_s").equals("")) {
					String impMax = ((String)request.getAttribute("tx_impMax_s")).trim().replace(',', '.');
					importoMassimo = BigDecimal.valueOf(Double.parseDouble(impMax));
				}
				
				if(importoMinimo.compareTo(importoMassimo) < 0) {
					try (Connection conn = payerDataSource.getConnection()) {
						if (conn.getAutoCommit())
							conn.setAutoCommit(false);
						
						IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
						IoItaliaConfigurazione conf = ioItaliaDao.selectConfigurazione((String) request.getAttribute("tx_firstKey_s"));
						IoItaliaConfigurazione conf1 = ioItaliaDao.selectConfigurazione(codiceSocieta, cutecute, chiaveEnte, tipServ, impServ);
						
						if(conf != null) {
							request.setAttribute("tx_messageKey", "La chiave primaria &egrave; gi&agrave; stata utilizzata, riprova.");
							request.setAttribute("tx_firstKey_s", null);
						} else if(conf1 != null) {
							request.setAttribute("tx_error_message", "Errore: Configurazione gi&agrave; esistente.");
						}else {
						id = ioItaliaDao.insertConfigurazione(codiceSocieta, cutecute, chiaveEnte, tipServ, impServ, wsChiavePrimaria, wsChiaveSecondaria, ioChiavePrimaria, ioChiaveSecondaria, mail, abilitato, importoMinimo, importoMassimo);
						}
						conn.commit();
						if(id != 0) {
							request.setAttribute("tx_message", "Configurazione inserita correttamente.");
							request.setAttribute("tx_messageKey", "");
							session.setAttribute("codop", "add");
						}
					} catch (DaoException | SQLException e) {
						e.printStackTrace();
					} 
				} else {
					request.setAttribute("tx_error_message", "ERRORE: L'importo minimo non può superare l'importo massimo.");
					request.setAttribute("tx_messageKey", "");
					session.setAttribute("codop", "add");
				}
				request.setAttribute("ddlSocietaUtenteEnte", (String) request.getAttribute("tx_societa") + "|" + request.getAttribute("tx_utente") + "|" + request.getAttribute("tx_ente"));
				break;
			case TX_BUTTON_INDIETRO:
				request.setAttribute("ddlSocietaUtenteEnte", (String) session.getAttribute("tx_societa") + "|" + session.getAttribute("tx_utente") + "|" + session.getAttribute("tx_ente"));
				session.setAttribute("codop", null);
				request.setAttribute("tx_societa", session.getAttribute("tx_societa"));
				request.setAttribute("tx_ente", session.getAttribute("tx_ente"));
				request.setAttribute("tx_tipServ", session.getAttribute("tx_tipServ"));
				request.setAttribute("tx_impServ", session.getAttribute("tx_impServ_s"));
				session.setAttribute("tx_societa", null);
				session.setAttribute("tx_ente", null);
				session.setAttribute("tx_tipServ", null);
				session.setAttribute("tx_impServ_s", null);
				//resetParamsSessioneRequest(session, request);
				break;
			case TX_BUTTON_CANCEL:
				break;
			case TX_BUTTON_DELETE:
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
			session.setAttribute("tx_societa_desc", null);
			session.setAttribute("tx_ente_desc", null);
			session.setAttribute("tx_codice_ente_desc", null);
			session.setAttribute("tx_UtenteEnte_desc", null);
			session.setAttribute("tx_tipServ_desc", null);
			session.setAttribute("tx_impServ_desc", null);
			session.setAttribute("tx_provincia_desc", null);
			session.setAttribute("tx_desc_societa", null);
			session.setAttribute("tx_desc_utente", null);
			session.setAttribute("tx_desc_ente", null);
			session.setAttribute("tx_desc_tipServ", null);
			session.setAttribute("tx_desc_impServ_s", null);
			
			request.setAttribute("tx_firstKey_s", null);
			request.setAttribute("tx_secondKey_s", null);
			request.setAttribute("tx_firstKey2_s", null);
			request.setAttribute("tx_secondKey2_s", null);
			request.setAttribute("tx_mail_s", null);
			request.setAttribute("tx_impMin_s", null);
			request.setAttribute("tx_impMax_s", null);
			request.setAttribute("tx_abilitato_s", null);
			
		}
	
	
	private void dividiSocUtenteEnte(HttpServletRequest request) {
		
		
		if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
		{
			String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			if (!ddlSocietaUtenteEnte.equals(""))
			{
				String[] codici = ddlSocietaUtenteEnte.split("\\|");
				codiceSocieta = codici[0];
				cutecute = codici[1];
				chiaveEnte = codici[2];
				if(codici.length > 3) chiaveEnteda5 = codici[3].substring(1, 6);
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", cutecute);
				request.setAttribute("tx_ente", chiaveEnte);
				request.setAttribute("tx_enteda5", chiaveEnteda5);
				
			}
		}
	}
	
}


