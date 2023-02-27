package org.seda.payer.manager.ioitalia.actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.tools.ant.filters.StringInputStream;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.core.bean.IoItaliaFornitura;
import com.seda.payer.core.bean.IoItaliaMessaggiList;
import com.seda.payer.core.dao.IoItaliaDao;
import com.seda.payer.core.exception.DaoException;

public class IoItaliaEditAction extends IoItaliaBaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String societa = "";
	private String provincia = "";
	private String codiceUtente = "";
	private String utenteEnte = "";
	private String tipServ = "";
	private String impServ = "";
	private String codiceFiscale = "";
	private String scadenza;
	private String fornitura = "";
	private String esito;
	
	


	
	@SuppressWarnings("restriction")
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);
		String template = getTemplateCurrentApplication(request, request.getSession());
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
			setProfile(request);
		}
		
		loadSocietaXml_DDL(request);
		
		switch(firedButton){
			case TX_BUTTON_DELETE:
				if(request.getAttribute("tx_es").equals("0")) {
					try (Connection conn = payerDataSource.getConnection()) {
						if(conn.getAutoCommit()) {
							conn.setAutoCommit(false);
						}
						IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
						long id = Long.parseLong((String) request.getAttribute("tx_id"));
						ioItaliaDao.deleteMessaggio(id);
						conn.commit();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					request.setAttribute("disabilita", true);
					request.setAttribute("tx_message", "Messaggio cancellato correttamente.");
				} else {
					request.setAttribute("tx_message", "E' possibile cancellare esclusivamente messaggi non ancora inviati.");
				}
				//break;
			case TX_BUTTON_NULL:
				if(request.getAttribute("codop") != null && request.getAttribute("codop").equals("edit") && request.getAttribute("tx_id") != null) {
					try (Connection conn = payerDataSource.getConnection()) {
						
						IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
						session.setAttribute("tx_id_s", request.getAttribute("tx_id"));
						SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");
						// YLM PG22XX06 INI
						IoItaliaFornitura fornitura = new IoItaliaFornitura();
						if ( template.equals("aosta")) {
							fornitura = ioItaliaDao.selectFornitura(Long.parseLong((String)session.getAttribute("tx_id_s")));
						} else {
							fornitura = ioItaliaDao.selectFornituraTail(Long.parseLong((String)session.getAttribute("tx_id_s")), true);
						}
//						IoItaliaFornitura fornitura = ioItaliaDao.selectFornitura(Long.parseLong((String)session.getAttribute("tx_id_s")));
						// YLM PG22XX06 FINE
						
						session.setAttribute("idFornitura", fornitura.getIdFornitura());
						session.setAttribute("tx_societa", fornitura.getCodiceSocieta());
						session.setAttribute("tx_provincia", fornitura.getSiglaProvincia());
						session.setAttribute("tx_UtenteEnte", fornitura.getCodiceEnte());
						session.setAttribute("tx_tipServ", fornitura.getTipologiaServizio());
						session.setAttribute("tx_impServ", fornitura.getImpostaServizio());
						session.setAttribute("tx_cf", "");
						session.setAttribute("tx_data", FORMAT.format(fornitura.getDataInserimento()));
						session.setAttribute("tx_fornitura", fornitura.getCodiceFornitura());
						session.setAttribute("tx_esito", "");
						session.setAttribute("tx_societa_desc", fornitura.getDescrizioneSocieta());
						session.setAttribute("tx_UtenteEnte_desc", fornitura.getDescrizioneEnte());
						session.setAttribute("tx_tipServ_desc", fornitura.getDescrizioneTipologiaServizio());
						session.setAttribute("tx_impServ_desc", fornitura.getDescrizioneImpostaServizio());
						session.setAttribute("tx_provincia_desc", fornitura.getDescrizioneProvincia());
						
					} catch (SQLException | NumberFormatException | DaoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			case TX_BUTTON_AGGIUNGI:
			case TX_BUTTON_ADD:
			case TX_BUTTON_RINOTIFICA:
			case TX_BUTTON_INDIETRO:
			case TX_BUTTON_CERCA:
					mantieniFiltriRicerca(request);
					societa = (String) session.getAttribute("tx_societa");
					codiceUtente = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
					provincia = (String) session.getAttribute("tx_provincia");
					utenteEnte = (String) session.getAttribute("tx_UtenteEnte");
					tipServ = (String) session.getAttribute("tx_tipServ");
					impServ = (String) session.getAttribute("tx_impServ");
					codiceFiscale = (String) request.getAttribute("tx_cf");
					scadenza = (String) session.getAttribute("tx_data") ;
					fornitura = (String) session.getAttribute("tx_fornitura");
					esito = (String) request.getAttribute("tx_esito");
				
				try (Connection conn = payerDataSource.getConnection()) {
					IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
//					YLM PG22XX06 INI
					IoItaliaMessaggiList lista = new IoItaliaMessaggiList();
					if ( template.equals("aosta")) {
						lista = ioItaliaDao.messaggiList((long) session.getAttribute("idFornitura"), codiceFiscale, esito, pageNumber, rowsPerPage, order);
					} else {
						lista = ioItaliaDao.messaggiListAPPIO((long) session.getAttribute("idFornitura"), codiceFiscale, esito, pageNumber, rowsPerPage, order);
					}
//					IoItaliaMessaggiList lista = ioItaliaDao.messaggiList((long) session.getAttribute("idFornitura"), codiceFiscale, esito, pageNumber, rowsPerPage, order);
					
					//		YLM PG22XX06 FINE
					request.setAttribute("lista_messaggi", lista.getMessaggiListXml());
					request.setAttribute("lista_messaggi.pageInfo", lista);
				} catch (SQLException | NumberFormatException | DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				resettaSessione(session);
//				request.setAttribute("tx_cf", null);
//				request.setAttribute("tx_esito", null);
				request.setAttribute("disabilita", true);
				break;
			case TX_BUTTON_NUOVO:
				request.setAttribute("tx_cf", null);
				request.setAttribute("tx_esito", null);
				session.setAttribute("tx_cf", null);
				session.setAttribute("tx_esito", null);
				societa = (String) session.getAttribute("tx_societa");
				codiceUtente = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
				provincia = (String) session.getAttribute("tx_provincia");
				utenteEnte = (String) session.getAttribute("tx_UtenteEnte");
				tipServ = (String) session.getAttribute("tx_tipServ");
				impServ = (String) session.getAttribute("tx_impServ");
				request.setAttribute("disabilita", true);
				break;
			case TX_BUTTON_SOCIETA_CHANGED:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(), true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), (String)request.getAttribute("tx_provincia"), getParamCodiceUtente(), getParamCodiceEnte(), true);
				loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				break;
			case TX_BUTTON_RESET:
				request.setAttribute("tx_cf", null);
				request.setAttribute("tx_esito", null);
				session.setAttribute("tx_cf", null);
				session.setAttribute("tx_esito", null);
				request.setAttribute("disabilita", true);
				break;
			
			case TX_BUTTON_DOWNLOAD:
				if(request.getAttribute("lista_messaggi") == null) {
					societa = (String) session.getAttribute("tx_societa");
					codiceUtente = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
					provincia = (String) session.getAttribute("tx_provincia");
					utenteEnte = (String) session.getAttribute("tx_UtenteEnte");
					tipServ = (String) session.getAttribute("tx_tipServ");
					impServ = (String) session.getAttribute("tx_impServ");
					codiceFiscale = (String) request.getAttribute("tx_cf");
					scadenza = (String) session.getAttribute("tx_data") ;
					fornitura = (String) session.getAttribute("tx_fornitura");
					esito = (String) request.getAttribute("tx_esito");
					
					try (Connection conn = payerDataSource.getConnection()) {
						IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);

//						YLM PG22XX06 INI
						IoItaliaMessaggiList lista = new IoItaliaMessaggiList();
						if ( template.equals("aosta")) {
							lista = ioItaliaDao.messaggiList(Long.parseLong((String)session.getAttribute("tx_id_s")), codiceFiscale, esito, pageNumber, rowsPerPage, order);
						} else {
							lista = ioItaliaDao.messaggiListAPPIO(Long.parseLong((String)session.getAttribute("tx_id_s")), codiceFiscale, esito, pageNumber, rowsPerPage, order);
						}
//						IoItaliaMessaggiList lista = ioItaliaDao.messaggiList(Long.parseLong((String)session.getAttribute("tx_id_s")), codiceFiscale, esito, pageNumber, rowsPerPage, order);
						
						//		YLM PG22XX06 FINE
						
						session.setAttribute("lista_messaggi", lista.getMessaggiListXml());
						session.setAttribute("lista_messaggi.pageInfo", lista);
					} catch (SQLException | NumberFormatException | DaoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					request.setAttribute("disabilita", true);
				}
				ServletContext context = request.getSession().getServletContext();
				PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
				UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
				String pathAppoggio = configuration.getProperty(PropertiesPath.directoryIoItaliaCSV.format(userBean.getTemplate((String) new MAFRequest(request).getAttribute(MAFAttributes.CURRENT_APPLICATION))));
				
				try (com.sun.rowset.WebRowSetImpl wrs = new com.sun.rowset.WebRowSetImpl();
						BufferedWriter writer = Files.newBufferedWriter(
								Paths.get(pathAppoggio + "//file.csv"));
						CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
								.withHeader("MES_CUTECUTE", "MES_CMESIDDO",
										"MES_CMESTPSE", "MES_CMESTIME",
										"MES_CMESPOSI","MES_CMESCFIS",
										"MES_CMESOGME","MES_CMESCRME",
										"MES_GMESSCME","MES_IMESIMPO",
										"MES_CMESAVVP","MES_CMESSCPA",
										"MES_CMESMAIL","MES_CMESSTAT",
										"MES_CMESMOTI","MES_GMESGINV",
										"MES_CMESIDME","MES_KFORKFOR",
										"MES_CISECISE","MES_KMESKMES",
										"ESITO")
								.withDelimiter(';'))) {
					wrs.readXml(new StringInputStream((String)session.getAttribute("lista_messaggi"), "UTF-8"));
					List<String> array = new ArrayList<String>();
					while(wrs.next()) {
						for(int i = 1; i < 22; i++) {
							array.add((String)wrs.getString(i));
						}
						printer.printRecord(array);
						array.clear();
					}
					printer.flush();
					
					File file = new File(pathAppoggio + "//file.csv");
					request.setAttribute("filename", file.getName());
					request.setAttribute("downloadCSV", file.getAbsolutePath());
					session.setAttribute("lista_messaggi", null);
					session.setAttribute("lista_messaggi.pageInfo", file);
					
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;
			
			case TX_BUTTON_EDIT_END:
				request.setAttribute("tx_societa", session.getAttribute("tx_societa"));
				request.setAttribute("tx_provincia",session.getAttribute("tx_provincia"));
				request.setAttribute("tx_tipServ", session.getAttribute("tx_tipServ"));
				request.setAttribute("tx_UtenteEnte",(String) session.getAttribute("tx_societa") + session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA) + session.getAttribute("tx_UtenteEnte"));
				request.setAttribute("tx_fornitura_s", session.getAttribute("tx_fornitura"));
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
	
	private void mantieniFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();

		if(request.getAttribute("tx_esito")!=null)
			session.setAttribute("tx_esito", request.getAttribute("tx_esito"));
		if(request.getAttribute("tx_cf")!=null)
			session.setAttribute("tx_cf", request.getAttribute("tx_cf"));
	}
}
