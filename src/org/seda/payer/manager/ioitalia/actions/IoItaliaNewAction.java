package org.seda.payer.manager.ioitalia.actions;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.ManagerKeys;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.IoItaliaMessaggio;
import com.seda.payer.core.dao.IoItaliaDao;
import com.seda.payer.core.exception.DaoException;

public class IoItaliaNewAction extends IoItaliaBaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String societa;
	private String provincia;
	private String utenteEnte;
	private String tipServ;
	private String impServ;
	private Calendar scadenza;
	private String codiceFiscale;
//	private String esito;
	private String oggettoMessaggio;
	private String corpoMessaggio;
	private String avvisoPagoPA;
	private BigDecimal importo;
	private String flag;
	private String codiceUtente;
	private String email;
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
			setProfile(request);
		}
		//codiceUtente = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		codiceUtente = getParamCodiceUtente();
		societa = (String) request.getAttribute("tx_societa");
		provincia = (String) request.getAttribute("tx_provincia");
		utenteEnte = (String) request.getAttribute("tx_UtenteEnte");
		tipServ = (String) request.getAttribute("tx_tipServ");
		loadSocietaXml_DDL(request);
		loadProvinciaXml_DDL(request, session, societa, true);
		//loadDDLEnte(request, session, societa, provincia, codiceUtente, true);
		LoadListaUtentiEntiXml_DDL(request, session, societa, provincia, "", codiceUtente, true);//		YLM PG22XX06 INI

//		YLM PG22XX06 ini
		String template = getTemplateCurrentApplication(request, request.getSession());
		if ( template.equals("aosta")) {
			loadTipologiaServizioXml_DDL_2(request, session, societa, codiceUtente, getParamCodiceEnte(), true);
		} else {
			loadTipologiaServizioAPPIOXml_DDL(request, session, societa, codiceUtente, getParamCodiceEnte(), true);	
		}
//		YLM PG22XX06 FINE
		loadListaImpostaServizio(request, session, societa, codiceUtente, getParamCodiceEnte(), tipServ, true);
		switch(firedButton) {

		case TX_BUTTON_RINOTIFICA:
			// Se da IOItalia
			if(session.getAttribute("ioitaliacheck") != null && session.getAttribute("ioitaliacheck").equals("ioitalia")) {
				societa = (String) request.getAttribute("tx_societa");
				provincia = (String) request.getAttribute("tx_provincia");
				utenteEnte = getParamCodiceEnte();
				tipServ = (String) request.getAttribute("tx_tipServ");
				impServ = (String) request.getAttribute("tx_impServ");
			} else {
				// Se da IOItalia Forniture
				societa = (String) session.getAttribute("tx_societa");
				provincia = (String) session.getAttribute("tx_provincia");
				utenteEnte = (String) session.getAttribute("tx_UtenteEnte");
				tipServ = (String) session.getAttribute("tx_tipServ");
				impServ = (String) session.getAttribute("tx_impServ");
			}
			codiceFiscale = (String) request.getAttribute("tx_cf_s");
			scadenza = (Calendar) request.getAttribute("tx_data_da");
			oggettoMessaggio = (String) request.getAttribute("tx_oggetto_s");
			corpoMessaggio = (String) request.getAttribute("tx_corpo_s");
			System.out.println("Corpo prima: " +corpoMessaggio);
			try {
                byte[] stringBytesSource = corpoMessaggio.
                        getBytes("ISO-8859-1");
                corpoMessaggio = new String(stringBytesSource, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
			//corpoMessaggio = corpoMessaggio.replace("€", "&euro;");
			System.out.println("Corpo dopo: "+corpoMessaggio);
			avvisoPagoPA = (String) request.getAttribute("tx_avviso_s");
			if(request.getAttribute("tx_importo_s") != null && !request.getAttribute("tx_importo_s").equals("")) {
				String imp = ((String)request.getAttribute("tx_importo_s")).trim().replace(',', '.');
				importo = BigDecimal.valueOf(Double.parseDouble(imp));
			} 
			flag = (String) request.getAttribute("tx_flag");
//			esito = (String) request.getAttribute("tx_esito_s");
			email = (String) request.getAttribute("tx_email_s");
			
			try (Connection conn = payerDataSource.getConnection()) {
					if (conn.getAutoCommit())
						conn.setAutoCommit(false);
					IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
					long idFornitura = 0;
					if(session.getAttribute("ioitaliacheck") != null && session.getAttribute("ioitaliacheck").equals("ioitalia")) {
						idFornitura = ioItaliaDao.insertFornitura(societa, codiceUtente, utenteEnte, 
								tipServ, impServ, "MAN-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
					} else {
						idFornitura = (long) session.getAttribute("idFornitura");
					}
					String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
					IoItaliaMessaggio messaggio = new IoItaliaMessaggio(
							codiceUtente, 
							getIdDominio(societa, codiceUtente, utenteEnte, request), 
							tipServ, 
							data, 
							1, 
							codiceFiscale, 
							oggettoMessaggio, 
							corpoMessaggio, 
							new Date(scadenza.getTimeInMillis()), 
							importo, 
							avvisoPagoPA, 
							flag, 
							email, 
							"0", 
							idFornitura, 
							impServ);
					
					long idMessaggio = ioItaliaDao.insertMessaggio(messaggio);
					session.setAttribute("id_messaggio", idMessaggio);
					conn.commit();
				} catch (SQLException | DaoException e) {
					request.setAttribute("tx_error_message", "Errore durante il salvataggio del messaggio.");
				}
				

	//			YLM PG22XX06 ini
				if ( template.equals("aosta")) {
					inviaMessaggioAppIO(String.valueOf(session.getAttribute("id_messaggio")), request);
				} else {
					inviaMessaggioAppIOTail(String.valueOf(session.getAttribute("id_messaggio")),true, request);
				}
	//			YLM PG22XX06 FINE
				
				if(session.getAttribute("id_messaggio") != null && !session.getAttribute("id_messaggio").equals(0)) {
					request.setAttribute("tx_message", "Messaggio salvato e inviato.");
					
				} else {
					request.setAttribute("tx_error_message", "Errore durante il salvataggio del messaggio.");
				}
				if(session.getAttribute("ioitaliacheck").equals("ioitalia")) {
					request.setAttribute("tx_fornitura_s", "MAN-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
				} else {
					request.setAttribute("disabilita", true);
					resetNoIntestazione(request, session);
				}
				
				session.setAttribute("id_messaggio", null);
				break;
				
			case TX_BUTTON_RESET:
				if(session.getAttribute("ioitaliacheck").equals("ioitalia")) {
					resettaTuttiIParametri(request, session);
				} else {
					request.setAttribute("tx_cf_s", null);
					request.setAttribute("tx_flag", null);
					request.setAttribute("tx_data", null);
					request.setAttribute("tx_data_da", null);
					request.setAttribute("tx_oggetto", null);
					request.setAttribute("tx_corpo", null);
					request.setAttribute("tx_avviso", null);
					request.setAttribute("tx_importo", null);
					request.setAttribute("tx_codice_ente", null);
					request.setAttribute("tx_esito", null);
					request.setAttribute("tx_fornitura_s", null);
					request.setAttribute("tx_fornitura", null);
					request.setAttribute("tx_data_scadenza_aggiornata", null) ;
					request.setAttribute("tx_email", null);
					request.setAttribute("tx_oggetto_s", null);
					request.setAttribute("tx_corpo_s", null);
					request.setAttribute("tx_avviso_s", null);
					request.setAttribute("tx_importo_s", null);
					request.setAttribute("tx_esito_s", null);
					request.setAttribute("tx_fornitura_s", null);
					request.setAttribute("tx_email_s", null);
					session.setAttribute("tx_cf_s", null);
					session.setAttribute("tx_data_scadenza", null);
					session.setAttribute("tx_data_scadenza_aggiornata", null);
					session.setAttribute("tx_fornitura", null);
					session.setAttribute("tx_esito", null);
					session.setAttribute("tx_oggetto", null);
					session.setAttribute("tx_corpo", null);
					session.setAttribute("tx_avviso", null);
					session.setAttribute("tx_importo", null);
					session.setAttribute("tx_flag", null);
					session.setAttribute("tx_email", null);
					request.setAttribute("disabilita", true);
				}
				break;
			case TX_BUTTON_TIPO_SERVIZIO_CHANGED:
			case TX_BUTTON_ENTE_CHANGED:
			case TX_BUTTON_SOCIETA_CHANGED:
				societa = (String) request.getAttribute("tx_societa");
				provincia = (String) request.getAttribute("tx_provincia");
				utenteEnte = (String) request.getAttribute("tx_UtenteEnte");
				tipServ = (String) request.getAttribute("tx_tipServ");
				loadProvinciaXml_DDL(request, session, societa, true);
				//loadDDLEnte(request, session, societa, provincia, codiceUtente, true);
				LoadListaUtentiEntiXml_DDL(request, session, societa, provincia, "", codiceUtente, true);

//				YLM PG22XX06 ini
				if ( template.equals("aosta")) {
					loadTipologiaServizioXml_DDL_2(request, session, societa, codiceUtente, getParamCodiceEnte(), true);
				} else {
					loadTipologiaServizioAPPIOXml_DDL(request, session, societa, codiceUtente, getParamCodiceEnte(), true);
				}
//				YLM PG22XX06 FINE
				loadListaImpostaServizio(request, session, societa, codiceUtente, getParamCodiceEnte(), tipServ, true);
				break;
				
			case TX_BUTTON_NULL: 
				resettaTuttiIParametri(request, session);
				session.setAttribute("codop", null);
				loadProvinciaXml_DDL(request, session, codiceUtente, true);
				break;
				
			case TX_BUTTON_ADD:
				
//				YLM PG22XX06 ini disalinneamento metodo  "salva" vs "salva e invia", recupero codice settaggio variabili
				if(session.getAttribute("ioitaliacheck") != null && session.getAttribute("ioitaliacheck").equals("ioitalia")) {
					societa = (String) request.getAttribute("tx_societa");
					provincia = (String) request.getAttribute("tx_provincia");
					utenteEnte = getParamCodiceEnte();
					tipServ = (String) request.getAttribute("tx_tipServ");
					impServ = (String) request.getAttribute("tx_impServ");
				} else {
					societa = (String) session.getAttribute("tx_societa");
					provincia = (String) session.getAttribute("tx_provincia");
					utenteEnte = (String) session.getAttribute("tx_UtenteEnte");
					tipServ = (String) session.getAttribute("tx_tipServ");
					impServ = (String) session.getAttribute("tx_impServ");
				}
				
//				societa = (String) session.getAttribute("tx_societa");
//				provincia = (String) session.getAttribute("tx_provincia");
//				
//				if(session.getAttribute("ioitaliacheck") != null && session.getAttribute("ioitaliacheck").equals("ioitalia")) {
//					utenteEnte = getParamCodiceEnte();
//				} else {
//					
//					utenteEnte = (String) session.getAttribute("tx_UtenteEnte");
//				}
//				tipServ = (String) session.getAttribute("tx_tipServ");
//				impServ = (String) session.getAttribute("tx_impServ");
				
//				YLM PG22XX06 FINE disalinneamento metodo "salva" vs "salva e invia", recupero codice settaggio variabili
				codiceFiscale = (String) request.getAttribute("tx_cf_s");
				scadenza = (Calendar) request.getAttribute("tx_data_da");
				oggettoMessaggio = (String) request.getAttribute("tx_oggetto_s");
				corpoMessaggio = (String) request.getAttribute("tx_corpo_s");
				System.out.println("Corpo prima: " +corpoMessaggio);
				try {
	                byte[] stringBytesSource = corpoMessaggio.
	                        getBytes("ISO-8859-1");
	                corpoMessaggio = new String(stringBytesSource, "UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                throw new RuntimeException(e);
	            }
				//corpoMessaggio = corpoMessaggio.replace("€", "&euro;");
				System.out.println("Corpo dopo: "+corpoMessaggio);
				avvisoPagoPA = (String) request.getAttribute("tx_avviso_s");
				if(request.getAttribute("tx_importo_s") != null && !request.getAttribute("tx_importo_s").equals("")) {
					String imp = ((String)request.getAttribute("tx_importo_s")).trim().replace(',', '.');
					importo = BigDecimal.valueOf(Double.parseDouble(imp));
				}
				flag = (String) request.getAttribute("tx_flag");
//				esito = (String) request.getAttribute("tx_esito_s");
				email = (String) request.getAttribute("tx_email_s");
				long idFornitura = 0;
				try (Connection conn = payerDataSource.getConnection()) {
					if (conn.getAutoCommit())
						conn.setAutoCommit(false);
					IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
					if(session.getAttribute("ioitaliacheck") != null && session.getAttribute("ioitaliacheck").equals("ioitalia")) {
						idFornitura = ioItaliaDao.insertFornitura(societa, codiceUtente, utenteEnte, 
								tipServ, impServ, "MAN-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
					} else {
						idFornitura = (long)session.getAttribute("idFornitura");
					}
					String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
					IoItaliaMessaggio messaggio = new IoItaliaMessaggio(
							codiceUtente, 
							getIdDominio(societa, codiceUtente, utenteEnte, request), 
							tipServ, 
							data, 
							1, 
							codiceFiscale, 
							oggettoMessaggio, 
							corpoMessaggio, 
							new Date(scadenza.getTimeInMillis()), 
							importo, 
							avvisoPagoPA, 
							flag, 
							email, 
							"0", 
							idFornitura, 
							impServ);
					
					ioItaliaDao.insertMessaggio(messaggio);
					conn.commit();
					
				} catch (SQLException | DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				request.setAttribute("tx_message", "Messaggio Salvato: " + societa + " " +
//										provincia + " " + utenteEnte + " " + tipServ + " " +
//										impServ + " " + codiceFiscale + " " + new Date(scadenza.getTimeInMillis()) + " " +
//										oggettoMessaggio + " " + corpoMessaggio + " " + avvisoPagoPA + " " +
//										importo + " " + flag + " " + esito);
				if (idFornitura != 0) {
					request.setAttribute("tx_message", "MESSAGGIO SALVATO CORRETTAMENTE.");
					request.setAttribute("tx_fornitura_s", "MAN-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
				} else  {
					request.setAttribute("tx_message", "ERRORE: MESSAGGIO NON SALVATO.");
				}
				//resettaTuttiIParametri(request);
				break;
			case TX_BUTTON_NUOVO:
				session.setAttribute("codop", null);
				if(session.getAttribute("ioitaliacheck").equals("forniture")) {
					societa = (String) session.getAttribute("tx_societa");
					provincia = (String) session.getAttribute("tx_provincia");
					utenteEnte = (String) session.getAttribute("tx_UtenteEnte");
					tipServ = (String) session.getAttribute("tx_tipServ");
					impServ = (String) session.getAttribute("tx_impServ");
					
					request.setAttribute("tx_societa", societa );
					request.setAttribute("tx_provincia", provincia);
					request.setAttribute("tx_UtenteEnte", utenteEnte );
					request.setAttribute("tx_tipServ", tipServ );
					request.setAttribute("tx_impServ", impServ );
				}
				break;
			default:
				break;
		}
			return null;
	}
	
	public void resettaTuttiIParametri(HttpServletRequest request) {
		request.setAttribute("tx_societa", null);
		request.setAttribute("tx_provincia", null);
		request.setAttribute("tx_UtenteEnte", null);
		request.setAttribute("tx_tipServ", null);
		request.setAttribute("tx_impServ", null);
		request.setAttribute("tx_cf_s", null);
		
		request.setAttribute("tx_data_da", null) ;
		request.setAttribute("tx_oggetto_s", null);
		request.setAttribute("tx_corpo_s", null);
		request.setAttribute("tx_avviso_s", null);
		request.setAttribute("tx_importo_s", null);
		request.setAttribute("tx_esito_s", null);
		request.setAttribute("tx_fornitura_s", null);
		setProfile(request);
	}

	public void resettaTuttiIParametri(HttpServletRequest request, HttpSession session) {
		request.setAttribute("tx_societa", null);
		request.setAttribute("tx_provincia", null);
		request.setAttribute("tx_UtenteEnte", null);
		request.setAttribute("tx_tipServ", null);
		request.setAttribute("tx_impServ", null);
		request.setAttribute("tx_cf_s", null);
		request.setAttribute("tx_flag", null);
		request.setAttribute("tx_data", null);
		request.setAttribute("tx_data_da", null);
		request.setAttribute("tx_oggetto", null);
		request.setAttribute("tx_corpo", null);
		request.setAttribute("tx_avviso", null);
		request.setAttribute("tx_importo", null);
		request.setAttribute("tx_codice_ente", null);
		request.setAttribute("tx_esito", null);
		request.setAttribute("tx_fornitura_s", null);
		request.setAttribute("tx_fornitura", null);
		request.setAttribute("tx_data_scadenza_aggiornata", null) ;
		request.setAttribute("tx_email", null);
		request.setAttribute("tx_oggetto_s", null);
		request.setAttribute("tx_corpo_s", null);
		request.setAttribute("tx_avviso_s", null);
		request.setAttribute("tx_importo_s", null);
		request.setAttribute("tx_esito_s", null);
		request.setAttribute("tx_fornitura_s", null);
		request.setAttribute("tx_email_s", null);
		
		session.setAttribute("tx_societa", null);
		session.setAttribute("tx_provincia", null);
		session.setAttribute("tx_ente", null);
		session.setAttribute("tx_tipServ", null);
		session.setAttribute("tx_impServ", null);
		session.setAttribute("tx_cf_s", null);
		session.setAttribute("tx_data_scadenza", null);
		session.setAttribute("tx_data_scadenza_aggiornata", null);
		session.setAttribute("tx_fornitura", null);
		session.setAttribute("tx_esito", null);
		session.setAttribute("tx_oggetto", null);
		session.setAttribute("tx_corpo", null);
		session.setAttribute("tx_avviso", null);
		session.setAttribute("tx_importo", null);
		session.setAttribute("tx_flag", null);
		session.setAttribute("tx_email", null);
		session.setAttribute("id_messaggio", null);
		session.setAttribute("tx_societa_desc", null);
		session.setAttribute("tx_ente_desc", null);
		session.setAttribute("tx_codice_ente_desc", null);
		session.setAttribute("tx_UtenteEnte_desc", null);
		session.setAttribute("tx_tipServ_desc", null);
		session.setAttribute("tx_impServ_desc", null);
		session.setAttribute("tx_provincia_desc", null);
		setProfile(request);
	}
	
	public void resetNoIntestazione(HttpServletRequest request, HttpSession session) {
		
		request.setAttribute("tx_cf_s", null);
		request.setAttribute("tx_flag", null);
		request.setAttribute("tx_data_da", null);
		request.setAttribute("tx_oggetto", null);
		request.setAttribute("tx_corpo", null);
		request.setAttribute("tx_avviso", null);
		request.setAttribute("tx_importo", null);
		request.setAttribute("tx_esito", null);
		request.setAttribute("tx_data_scadenza_aggiornata", null) ;
		request.setAttribute("tx_email", null);
		request.setAttribute("tx_oggetto_s", null);
		request.setAttribute("tx_corpo_s", null);
		request.setAttribute("tx_avviso_s", null);
		request.setAttribute("tx_importo_s", null);
		request.setAttribute("tx_esito_s", null);
		request.setAttribute("tx_email_s", null);

		session.setAttribute("tx_cf_s", null);
		session.setAttribute("tx_data_scadenza", null);
		session.setAttribute("tx_data_scadenza_aggiornata", null);
		session.setAttribute("tx_esito", null);
		session.setAttribute("tx_oggetto", null);
		session.setAttribute("tx_corpo", null);
		session.setAttribute("tx_avviso", null);
		session.setAttribute("tx_importo", null);
		session.setAttribute("tx_flag", null);
		session.setAttribute("tx_email", null);
		session.setAttribute("id_messaggio", null);
		setProfile(request);
	}
	
}
