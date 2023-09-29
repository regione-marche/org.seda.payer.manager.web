package org.seda.payer.manager.ioitalia.actions;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.ManagerKeys;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.IoItaliaFornitura;
import com.seda.payer.core.bean.IoItaliaMessaggio;
import com.seda.payer.core.dao.IoItaliaDao;
import com.seda.payer.core.exception.DaoException;

public class IoItaliaMessageEditAction extends IoItaliaBaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String societa = "";
	private String provincia = "";
	private String utenteEnte = "";
	private String tipServ = "";
	private String impServ = "";
	private String esito = "";
	private String fornitura = "";
	private String codiceUtente;
	private String codiceFiscale;
	private java.util.Date scadenza;
	private java.util.Date inserimento;
	private String oggettoMessaggio;
	private String corpoMessaggio;
	private String avvisoPagoPA;
	private BigDecimal importo;
	private String flag;

	private String email;
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);
		String template = getTemplateCurrentApplication(request, request.getSession());
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
			setProfile(request);
		}
		SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");
		//codiceUtente = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);		//PAGONET-392
		
		switch(firedButton) {
			case TX_BUTTON_NULL:
				if(request.getAttribute("codop").equals("edit") && request.getAttribute("tx_id") != null) {
					try (Connection conn = payerDataSource.getConnection()) {
						
						IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
						session.setAttribute("tx_id_s", request.getAttribute("tx_id"));
						long id = Long.parseLong((String)request.getAttribute("tx_id"));
						 
						IoItaliaMessaggio messaggio = ioItaliaDao.selectMessaggio(id);
//						YLM PG22XX06 INI
						IoItaliaFornitura fornitura = new IoItaliaFornitura();
						if ( template.equals("aosta")) {
							fornitura = ioItaliaDao.selectFornitura(messaggio.getIdFornitura());
						} else {
							fornitura = ioItaliaDao.selectFornituraTail(messaggio.getIdFornitura(), true);
						}
//						IoItaliaFornitura fornitura = ioItaliaDao.selectFornitura(messaggio.getIdFornitura());
//						YLM PG22XX06 FINE					
						session.setAttribute("tx_id_fornitura", messaggio.getIdFornitura());
						session.setAttribute("io_codiceUtente", fornitura.getCodiceUtente());	//PAGONET-392
						session.setAttribute("tx_societa", fornitura.getCodiceSocieta());
						session.setAttribute("tx_provincia", fornitura.getSiglaProvincia());
						session.setAttribute("tx_codice_ente", fornitura.getCodiceEnte());
						session.setAttribute("tx_tipServ", messaggio.getTipologiaServizio());
						session.setAttribute("tx_impServ", messaggio.getImpostaServizio());
						session.setAttribute("tx_cf_s", messaggio.getCodiceFiscale());
						session.setAttribute("tx_data", FORMAT.format(fornitura.getDataInserimento()));
						session.setAttribute("tx_email", messaggio.getEmail());
						session.setAttribute("tx_societa_desc", fornitura.getDescrizioneSocieta());
						session.setAttribute("tx_codice_ente_desc", fornitura.getDescrizioneEnte());
						session.setAttribute("tx_tipServ_desc", fornitura.getDescrizioneTipologiaServizio());
						session.setAttribute("tx_impServ_desc", fornitura.getDescrizioneImpostaServizio());
						session.setAttribute("tx_provincia_desc", fornitura.getDescrizioneProvincia());
//						java.util.Date date = messaggio.getDataScadenzaMessaggio();
//						Calendar calendario = null;
//						calendario.setTime(date);
						
						session.setAttribute("tx_data_scadenza", FORMAT.format(messaggio.getDataScadenzaMessaggio()));
						session.setAttribute("tx_data_scadenza_aggiornata", null);
						session.setAttribute("tx_fornitura", fornitura.getCodiceFornitura());
						session.setAttribute("tx_esito", messaggio.getStato());
						session.setAttribute("tx_oggetto", messaggio.getOggettoMessaggio());
						session.setAttribute("tx_corpo", messaggio.getCorpoMessaggio());
						session.setAttribute("tx_avviso", messaggio.getAvvisoPagoPa());
						//YLM PG22XX06 INI
						String importoMessaggio= messaggio.getImporto() != null ? messaggio.getImporto().toString().replace(".", ",") : "";
						session.setAttribute("tx_importo",importoMessaggio);
						//	session.setAttribute("tx_importo", messaggio.getImporto().toString().replace(".", ","));
						//YLM PG22XX06 FINE
						session.setAttribute("tx_id_messaggio", messaggio.getIdMessaggio());				
					} catch (SQLException | NumberFormatException | DaoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case TX_BUTTON_RESET:
			case TX_BUTTON_INDIETRO:				
				break;
			case TX_BUTTON_AGGIUNGI:
				
				String error_message1 = "";
				try (Connection conn = payerDataSource.getConnection()) {
					if(conn.getAutoCommit()) {
						conn.setAutoCommit(false);
					}
					long idFornitura = (long) session.getAttribute("tx_id_fornitura");
					long idMessaggio = Long.parseLong((String) session.getAttribute("tx_id_s"));
					codiceUtente = (String) session.getAttribute("io_codiceUtente");	//PAGONET-392
					utenteEnte = (String) session.getAttribute("tx_codice_ente");
					societa = (String) session.getAttribute("tx_societa");
					provincia = (String) session.getAttribute("tx_provincia");
					tipServ = (String) session.getAttribute("tx_tipServ");
					impServ = (String) session.getAttribute("tx_impServ");
					codiceFiscale = (String) request.getAttribute("tx_cf_s");
					Calendar scadenzaAggiornata = (Calendar) request.getAttribute("tx_data_scadenza_aggiornata");
					
					scadenza = (scadenzaAggiornata == null) ?
							(FORMAT.parse((String) session.getAttribute("tx_data_scadenza"))) :
								scadenzaAggiornata.getTime();
					
					oggettoMessaggio = (String) request.getAttribute("tx_oggetto");
					corpoMessaggio = (String) request.getAttribute("tx_corpo");
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
					avvisoPagoPA = (String) request.getAttribute("tx_avviso");
					if(request.getAttribute("tx_importo") != null && !request.getAttribute("tx_importo").equals("")) {
						String imp = ((String)request.getAttribute("tx_importo")).trim().replace(',', '.');
						importo = BigDecimal.valueOf(Double.parseDouble(imp));
					}
					flag = (String) request.getAttribute("tx_flag");
					esito = (String) request.getAttribute("tx_esito");
					email = (String) request.getAttribute("tx_email");
					IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
					IoItaliaMessaggio messaggio = new IoItaliaMessaggio(
							codiceUtente,
							getIdDominio(societa, codiceUtente, utenteEnte, request),
							tipServ,
							(String) LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")),
							1,
							codiceFiscale,
							oggettoMessaggio,
							corpoMessaggio, 
							scadenza,
							importo,
							avvisoPagoPA,
							flag,
							email,
							esito,
							idFornitura,
							impServ);
					messaggio.setIdMessaggio(idMessaggio);
					ioItaliaDao.updateMessaggio(messaggio);
					conn.commit();
				} catch (SQLException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				// YLM PG22XX06 INI
					error_message1 = e.getMessage();
				}
			
				if (request.getAttribute("tx_error_message") != null && request.getAttribute("tx_error_message") != "") {
					error_message1 += " " + request.getAttribute("tx_error_message").toString() + " ";
				}
				if (error_message1 != "") {
					request.setAttribute("tx_error_message", error_message1);
				} else {
					request.setAttribute("tx_message", "Messaggio Inviato.");
				}
				// YLM PG22XX06 FINE
				request.setAttribute("disabilita", true);
				break;
			case TX_BUTTON_RINOTIFICA:
				
				String error_message2 = "";
				String id = String.valueOf(session.getAttribute("tx_id_s"));
//				YLM PG22XX06 ini
				if ( template.equals("aosta")) {
					inviaMessaggioAppIO(id, request);
				} else {
					inviaMessaggioAppIOTail(id,true, request);
				}
//				inviaMessaggioAppIO(id, request);
				
				if (request.getAttribute("tx_error_message") != null && request.getAttribute("tx_error_message") != "") {
					error_message2 += " " + request.getAttribute("tx_error_message").toString() + " ";
				}
				if (error_message2 != "") {
					request.setAttribute("tx_error_message", error_message2);
				} else {
					request.setAttribute("tx_message", "Messaggio Inviato.");
				}
//				request.setAttribute("tx_message", "Messaggio Inviato.");
				//YLM PG22XX06 FINE

				request.setAttribute("disabilita", true);
				break;
			case TX_BUTTON_ADD:
				

				String error_message3= "";
				
				try (Connection conn = payerDataSource.getConnection()) {
					if(conn.getAutoCommit()) {
						conn.setAutoCommit(false);
					}
					long idFornitura = (long) session.getAttribute("tx_id_fornitura");
					long idMessaggio = Long.parseLong((String) session.getAttribute("tx_id_s"));
					codiceUtente = (String) session.getAttribute("io_codiceUtente");	//PAGONET-392
					utenteEnte = (String) session.getAttribute("tx_codice_ente");
					societa = (String) session.getAttribute("tx_societa");
					provincia = (String) session.getAttribute("tx_provincia");
					tipServ = (String) session.getAttribute("tx_tipServ");
					impServ = (String) session.getAttribute("tx_impServ");
					codiceFiscale = (String) request.getAttribute("tx_cf_s");
					Calendar scadenzaAggiornata = (Calendar) request.getAttribute("tx_data_scadenza_aggiornata");
					
					scadenza = (scadenzaAggiornata == null) ?
							(FORMAT.parse((String) session.getAttribute("tx_data_scadenza"))) :
								scadenzaAggiornata.getTime();
					
					oggettoMessaggio = (String) request.getAttribute("tx_oggetto");
					corpoMessaggio = (String) request.getAttribute("tx_corpo");
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
					avvisoPagoPA = (String) request.getAttribute("tx_avviso");
					if(request.getAttribute("tx_importo") != null && !request.getAttribute("tx_importo").equals("")) {
						String imp = ((String)request.getAttribute("tx_importo")).trim().replace(',', '.');
						importo = BigDecimal.valueOf(Double.parseDouble(imp));
					}
					flag = (String) request.getAttribute("tx_flag");
					esito = (String) request.getAttribute("tx_esito");
					email = (String) request.getAttribute("tx_email");
					IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
					IoItaliaMessaggio messaggio = new IoItaliaMessaggio(
							codiceUtente,
							getIdDominio(societa, codiceUtente, utenteEnte, request),
							tipServ,
							(String) LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")),
							1,
							codiceFiscale,
							oggettoMessaggio,
							corpoMessaggio, 
							scadenza,
							importo,
							avvisoPagoPA,
							flag,
							email,
							esito,
							idFornitura,
							impServ);
					messaggio.setIdMessaggio(idMessaggio);
					ioItaliaDao.updateMessaggio(messaggio);
					conn.commit();
				} catch (SQLException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					error_message3 = e.getMessage();
				}
//				YLM PG22XX06 ini
				if ( template.equals("aosta")) {
					inviaMessaggioAppIO((String) session.getAttribute("tx_id_s"), request);
				} else {
					inviaMessaggioAppIOTail((String) session.getAttribute("tx_id_s"),true, request);
				}
//				inviaMessaggioAppIO((String) session.getAttribute("tx_id_s"), request);
		
				if (request.getAttribute("tx_error_message") != null && request.getAttribute("tx_error_message") != "") {
					error_message3 += " " + request.getAttribute("tx_error_message").toString() + " ";
				}
				if (error_message3 != "") {
					request.setAttribute("tx_error_message", error_message3);
				} else {
					request.setAttribute("tx_message", "Messaggio salvato e inviato.");
				}
//				request.setAttribute("tx_message", "Messaggio salvato e inviato.");
//				YLM PG22XX06 FINE
				
				request.setAttribute("disabilita", true);
				break;
			default:
				break;
		}
			return null;
	}
	
	public void resettaTuttiIParametri(HttpServletRequest request, HttpSession session) {
		request.setAttribute("io_codiceUtente", null);	//PAGONET-392
		request.setAttribute("tx_provincia", null);
		request.setAttribute("tx_UtenteEnte", null);
		request.setAttribute("tx_societa", null);
		request.setAttribute("tx_tipServ", null);
		request.setAttribute("tx_impServ", null);
		request.setAttribute("tx_cf_s", null);
		request.setAttribute("tx_flag", null);
		request.setAttribute("tx_data", null) ;
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
		setProfile(request);
	}

}
