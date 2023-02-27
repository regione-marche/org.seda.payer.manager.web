package org.seda.payer.manager.ioitalia.actions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.IoItaliaFornitureList;
import com.seda.payer.core.dao.IoItaliaDao;
import com.seda.payer.core.exception.DaoException;

public class IoItaliaAction extends IoItaliaBaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String societa = "";
	private String codiceUtente = "";
	private String provincia = "";
	private String ente = "";
	private String tipServ = "";
	private String impServ = "";
	private Calendar dataDa;
	private Calendar dataA;
	private String esito = "";
	private String fornitura = "";
	private String codiceFiscale = "";
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
			setProfile(request);
		}
		loadSocietaXml_DDL(request);
		//codiceUtente = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		codiceUtente = getParamCodiceUtente();
		societa = (String) request.getAttribute("tx_societa");
		provincia = (String) request.getAttribute("tx_provincia");
		tipServ = (String) request.getAttribute("tx_tipServ");
		
		loadProvinciaXml_DDL(request, session, societa, true);
		LoadListaUtentiEntiXml_DDL(request, session, societa, provincia, "", codiceUtente, true);
//		YLM PG22XX06 INI
		String template = getTemplateCurrentApplication(request, request.getSession());
		if ( template.equals("aosta")) {
			loadTipologiaServizioXml_DDL_2(request, session, societa, codiceUtente, getParamCodiceEnte(), true);
		} else {
			loadTipologiaServizioAPPIOXml_DDL(request, session, societa, codiceUtente, getParamCodiceEnte(), true);
		}
//		YLM PG22XX06 FINE
		loadListaImpostaServizio(request, session, societa, codiceUtente, getParamCodiceEnte(), tipServ, true);
		switch(firedButton) {
			case TX_BUTTON_ADD:
			case TX_BUTTON_EDIT_END:
			case TX_BUTTON_INDIETRO:
			case TX_BUTTON_CERCA:
			case TX_BUTTON_RINOTIFICA:
				mantieniFiltriRicerca(request);
				societa = (String) session.getAttribute("tx_societa");
				provincia = (String) session.getAttribute("tx_provincia");
				ente = getParamCodiceEnte();
				//ente = (String) session.getAttribute("tx_UtenteEnte");
				tipServ = (String) session.getAttribute("tx_tipServ");
				impServ = (String) session.getAttribute("tx_impServ");
				dataDa = (Calendar) session.getAttribute("tx_data_da");
				dataA = (Calendar) session.getAttribute("tx_data_a");
				esito = (String) session.getAttribute("tx_esito");
				fornitura = (String) session.getAttribute("tx_fornitura_s");
				if(fornitura == null) {
					fornitura = "";
				}
				codiceFiscale = (String) session.getAttribute("tx_codiceFiscale_s");
				if(codiceFiscale == null) {
					codiceFiscale = "";
				}
				String dataDaFormattata = formatDate(dataDa, "yyyy-MM-dd");
				String dataAFormattata = formatDate(dataA, "yyyy-MM-dd");
				
				try (Connection conn = payerDataSource.getConnection()) {
					
					IoItaliaDao ioItaliaDao = new IoItaliaDao(conn, payerDbSchema);
//					YLM PG22XX06 INI
					IoItaliaFornitureList list = new IoItaliaFornitureList(); 
					if ( template.equals("aosta")) {
						list = ioItaliaDao.fornitureList(societa,
								codiceUtente,
								provincia,
								ente,
								tipServ,
								impServ,
								fornitura.toUpperCase(),
								codiceFiscale.trim().toUpperCase(),
								dataDaFormattata,
								dataAFormattata,
								esito,
								pageNumber, rowsPerPage, order);
					} else {
						list = ioItaliaDao.fornitureListTail(societa,
								codiceUtente,
								provincia,
								ente,
								tipServ,
								impServ,
								fornitura.toUpperCase(),
								codiceFiscale.trim().toUpperCase(),
								dataDaFormattata,
								dataAFormattata,
								esito,
								pageNumber, rowsPerPage, order, true);
					}
//					YLM PG22XX06 FINE			
					request.setAttribute("lista_forniture", list.getFornitureListXml());
					request.setAttribute("lista_forniture.pageInfo", list);
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				loadProvinciaXml_DDL(request, session, societa, true);
//				YLM PG22XX06 INI
				if ( template.equals("aosta")) {
					loadTipologiaServizioXml_DDL_2(request, session, societa, codiceUtente, getParamCodiceEnte(), true);
				} else {
					loadTipologiaServizioAPPIOXml_DDL(request, session, societa, codiceUtente, getParamCodiceEnte(), true);
				}
//				YLM PG22XX06 FINE
				loadListaImpostaServizio(request, session, societa, codiceUtente, getParamCodiceEnte(), tipServ, true);
				
				break;
				
			case TX_BUTTON_RESET:
				resettaTuttiIParametri(request, session);
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
			case TX_BUTTON_ENTE_CHANGED:
			case TX_BUTTON_TIPOLOGIA_CHANGED:
			case TX_BUTTON_SOCIETA_CHANGED:
				societa = (String) request.getAttribute("tx_societa");
				provincia = (String) request.getAttribute("tx_provincia");
				tipServ = (String) request.getAttribute("tx_tipServ");
				loadProvinciaXml_DDL(request, session, societa, true);
				LoadListaUtentiEntiXml_DDL(request, session, societa, provincia, "", codiceUtente, true);
				
//				YLM PG22XX06 INI
				if ( template.equals("aosta")) {
					loadTipologiaServizioXml_DDL_2(request, session, societa, codiceUtente, getParamCodiceEnte(), true);
				} else {
					loadTipologiaServizioAPPIOXml_DDL(request, session, societa, codiceUtente, getParamCodiceEnte(), true);	
				}
//				YLM PG22XX06 FINE
				loadListaImpostaServizio(request, session, societa, codiceUtente, getParamCodiceEnte(), tipServ, true);
				break;
			case TX_BUTTON_NULL: 
				loadSocietaXml_DDL(request);
				loadProvinciaXml_DDL(request, session, societa, true);
				LoadListaUtentiEntiXml_DDL(request, session, societa, provincia, getParamCodiceEnte(), codiceUtente, true);
//				YLM PG22XX06 INI
				if ( template.equals("aosta")) {
					loadTipologiaServizioXml_DDL_2(request, session, societa, codiceUtente, getParamCodiceEnte(), true);
				} else {
					loadTipologiaServizioAPPIOXml_DDL(request, session, societa, codiceUtente, getParamCodiceEnte(), true);
				}
//				YLM PG22XX06 FINE
				loadListaImpostaServizio(request, session, societa, codiceUtente, getParamCodiceEnte(), tipServ, true);
				resettaTuttiIParametri(request, session);
				break;
			
			default:
				break;
		}
			return null;
	}
	
	public void resettaTuttiIParametri(HttpServletRequest request) {
		request.setAttribute("tx_societa", null);
		request.setAttribute("tx_provincia", null);
		request.setAttribute("tx_ente", null);
		request.setAttribute("tx_tipServ", null);
		request.setAttribute("tx_impServ", null);
		request.setAttribute("tx_cf_s", null);
		
		request.setAttribute("tx_data_da", null) ;
		request.setAttribute("tx_oggetto_s", null);
		request.setAttribute("tx_corpo_s", null);
		request.setAttribute("tx_avviso_s", null);
		request.setAttribute("tx_importo_s", null);
		request.setAttribute("tx_esito_s", null);
		setProfile(request);
	}
	
	private void mantieniFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(request.getAttribute("tx_societa")!=null)
			session.setAttribute("tx_societa", request.getAttribute("tx_societa"));
		if(request.getAttribute("tx_provincia")!=null)
			session.setAttribute("tx_provincia", request.getAttribute("tx_provincia"));
		if(request.getAttribute("tx_UtenteEnte")!=null) 
			session.setAttribute("tx_UtenteEnte", request.getAttribute("tx_UtenteEnte"));			
		if(request.getAttribute("tx_tipServ")!=null)
			session.setAttribute("tx_tipServ", request.getAttribute("tx_tipServ"));
		if(request.getAttribute("tx_impServ")!=null)
			session.setAttribute("tx_impServ", request.getAttribute("tx_impServ"));
		if(request.getAttribute("tx_data_da")!=null)
			session.setAttribute("tx_data_da", request.getAttribute("tx_data_da"));
		if(request.getAttribute("tx_data_a")!=null)
			session.setAttribute("tx_data_a", request.getAttribute("tx_data_a"));
		if(request.getAttribute("tx_esito")!=null)
			session.setAttribute("tx_esito", request.getAttribute("tx_esito"));
		if(request.getAttribute("tx_fornitura_s")!=null)
			session.setAttribute("tx_fornitura_s", request.getAttribute("tx_fornitura_s"));
		if(request.getAttribute("tx_codiceFiscale_s")!=null)
			session.setAttribute("tx_codiceFiscale_s", request.getAttribute("tx_codiceFiscale_s"));
	}
	public void resettaTuttiIParametri(HttpServletRequest request, HttpSession session) {
		request.setAttribute("tx_societa", null);
		request.setAttribute("tx_provincia", null);
		request.setAttribute("tx_UtenteEnte", null);
		request.setAttribute("tx_ente", null);
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
		request.setAttribute("tx_codiceFiscale_s", null);
		request.setAttribute("tx_data_scadenza_aggiornata", null) ;
		request.setAttribute("tx_email", null);
		request.setAttribute("tx_data_da", null);
		request.setAttribute("tx_data_a", null);
		request.setAttribute("tx_message", null);
		
		session.setAttribute("tx_societa", null);
		session.setAttribute("tx_provincia", null);
		session.setAttribute("tx_ente", null);
		session.setAttribute("tx_UtenteEnte", null);
		session.setAttribute("tx_tipServ", null);
		session.setAttribute("tx_societa_desc", null);
		session.setAttribute("tx_ente_desc", null);
		session.setAttribute("tx_codice_ente_desc", null);
		session.setAttribute("tx_UtenteEnte_desc", null);
		session.setAttribute("tx_tipServ_desc", null);
		session.setAttribute("tx_impServ_desc", null);
		session.setAttribute("tx_provincia_desc", null);
		session.setAttribute("tx_impServ", null);
		session.setAttribute("tx_cf_s", null);
		session.setAttribute("tx_data_scadenza", null);
		session.setAttribute("tx_data_scadenza_aggiornata", null);
		session.setAttribute("tx_fornitura", null);
		session.setAttribute("tx_fornitura_s", null);
		session.setAttribute("tx_codiceFiscale_s", null);
		session.setAttribute("tx_esito", null);
		session.setAttribute("tx_oggetto", null);
		session.setAttribute("tx_corpo", null);
		session.setAttribute("tx_avviso", null);
		session.setAttribute("tx_importo", null);
		session.setAttribute("tx_flag", null);
		session.setAttribute("tx_email", null);
		session.setAttribute("idFornitura", null);
		session.setAttribute("tx_data_da", null);
		session.setAttribute("tx_data_a", null);
		session.setAttribute("tx_message", null);
		setProfile(request);
	}
	
	
}
