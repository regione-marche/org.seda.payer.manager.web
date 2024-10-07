
package org.seda.payer.manager.walletconfig.actions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.ConfigurazioneAttribuzionePagamentoServizio;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.bean.Tributo;
import com.seda.payer.core.wallet.dao.ConfigurazioneAttribuzionePagamentoServizioDAO;
import com.seda.payer.core.wallet.dao.ServizioDAO;
import com.seda.payer.core.wallet.dao.TributoDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
import com.seda.tag.core.DdlOption;

@SuppressWarnings("serial")
public class PrioritaSulServizioEditAction extends WalletBaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "pagamentoperservizio";
	private HashMap<String,Object> parametri = null;
	private String codiceSocieta = "";
	private String codiceUtente = "";
	private String chiaveEnte = "";
	private String operatore = "";
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		
		HttpSession sessionlocal = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		operatore = user.getUserName();
		
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			codop = "edit";
			String chiaverecord = (String)request.getAttribute("tx_tipologia_servizio");
			String priorita= (String)request.getAttribute("tx_priorita");
			dividiSocUtenteEnte(request);
			request.setAttribute("tx_tipologia_servizio_h",chiaverecord);
			
			
			
			if ( !chiaverecord.equals("") && chiaverecord!=null && !priorita.equals("") && priorita != null) {
				//ConfigurazioneSolleciti  configurazioneSolleciti = null;
				 
				ConfigurazioneAttribuzionePagamentoServizio  configurazioneAttribuzionePagamentoServizio = null;
				try {
					configurazioneAttribuzionePagamentoServizio = selectConfigurazioneAttribuzionePagamentoServizio(request);
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				}
				getServiziDDL(request, session);
				getTributiDDL(request, session);
				// parametri per valorizzare FORM di modifica
				request.setAttribute("tx_tipologia_servizio", configurazioneAttribuzionePagamentoServizio.getCodiceServizio());
				request.setAttribute("tx_priorita", configurazioneAttribuzionePagamentoServizio.getPriorita());
				request.setAttribute("tx_codice_tributo", configurazioneAttribuzionePagamentoServizio.getCodiceTributo().trim());
				request.setAttribute("tx_codice_tributo2", configurazioneAttribuzionePagamentoServizio.getCodiceTributo2().trim());
				request.setAttribute("tx_codice_tributo3", configurazioneAttribuzionePagamentoServizio.getCodiceTributo3().trim());
				
			}
			
			break;
			
		case TX_BUTTON_EDIT:
			try {
				//QUI
				EsitoRisposte esito = new EsitoRisposte();
				esito = controlli(request);
				if (esito.getCodiceMessaggio().equals("OK")) {
					esito = updatePrioritaSulServizio(request);
					if(esito.getCodiceMessaggio().equals("OK"))
					{
//						session.setAttribute("recordUpdate", "OK");
						//setFormMessage("form_selezione", Messages.INS_OK.format(), request);
						setFormMessage("var_form", "Record Aggiornato Correttamente", request);
					}
					else
					{
//						session.setAttribute("recordInsert", "KO");
//						session.setAttribute("messaggio.recordUpdate", esito.getDescrizioneMessaggio());
						setFormMessage("var_form", esito.getDescrizioneMessaggio(), request);
						
					}
				 } else {
//					session.setAttribute("recordInsert", "KO");
//					session.setAttribute("messaggio.recordInsert", esito.getDescrizioneMessaggio());
					setFormMessage("var_form", esito.getDescrizioneMessaggio(), request);
				 }
						
				
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			session.setAttribute("recordUpdate", "OK");
			//setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
			break;
		
		case TX_BUTTON_ADD:
			codop = "add";
			
			//request.setAttribute("tx_button_cerca", "Search");	
			break;
		case TX_BUTTON_AGGIUNGI:
			try {
				dividiSocUtenteEnte(request);
				EsitoRisposte esito = new EsitoRisposte();
				esito = controlli(request);
				if (esito.getCodiceMessaggio().equals("OK")) {
					esito = inserisciAttribuzionePagamentoServizio(request);
					if(esito.getCodiceMessaggio().equals("OK"))
					{
						//session.setAttribute("recordInsert", "OK");
						//setFormMessage("form_selezione", Messages.INS_OK.format(), request);
						setFormMessage("var_form", "Record Inserito Correttamente", request);
					}
					else
					{
						//session.setAttribute("recordInsert", "KO");
						//session.setAttribute("messaggio.recordInsert", esito.getDescrizioneMessaggio());
						setFormMessage("var_form", esito.getDescrizioneMessaggio(), request);
						
					}
				} else {
//					session.setAttribute("recordInsert", "KO");
//					session.setAttribute("messaggio.recordInsert", esito.getDescrizioneMessaggio());
					setFormMessage("var_form", esito.getDescrizioneMessaggio(), request);
				}
			}
			catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			
			break;
			
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("tx_button_cerca", "Search");	
			break;
		
		case TX_BUTTON_CANCEL:
			dividiSocUtenteEnte(request);
			String tx_tipologia_servizio_h = (String)request.getAttribute("tx_tipologia_servizio");
			request.setAttribute("tx_tipologia_servizio_h",tx_tipologia_servizio_h);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteAttribuzionePagamentoServizio(request);
					if(esito.getCodiceMessaggio().equals("OK"))
					{
						session.setAttribute("recordDelete", "OK");
						session.setAttribute("messaggio.recordDelete", "");
						//setFormMessage("form_selezione", Messages.CANC_OK.format(), request);
					}
					else
					{
						session.setAttribute("recordDelete", "KO");
						session.setAttribute("messaggio.recordDelete", esito.getDescrizioneMessaggio());
						//setFormMessage("form_selezione", esito.getDescrizioneMessaggio(), request);
					}
				}
			catch (DaoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						setFormMessage("form_selezione", "Errore: " + e.getLocalizedMessage(), request);
					}
			break;
			
		}
		if (!firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
		{
			
			loadSocietaUtenteEnteXml_DDL(request, session);
			getServiziDDL(request, session);
			getTributiDDL(request, session);
		}
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("codop",codop);
		return null;
	}

	private void dividiSocUtenteEnte(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
		if (!ddlSocietaUtenteEnte.equals(""))
		{
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			codiceSocieta = codici[0];
			codiceUtente = codici[1];
			chiaveEnte = codici[2];
			request.setAttribute("tx_societa", codiceSocieta);
			request.setAttribute("tx_utente", codiceUtente);
			request.setAttribute("tx_ente", chiaveEnte);
		}
	}

	private ConfigurazioneAttribuzionePagamentoServizio selectConfigurazioneAttribuzionePagamentoServizio(HttpServletRequest request) throws DaoException {
		
		String codiceServizio  = (String)request.getAttribute("tx_tipologia_servizio");
		String prioritastr  = (String)request.getAttribute("tx_priorita");
		Integer priorita = Integer.parseInt(prioritastr);
		ConfigurazioneAttribuzionePagamentoServizioDAO configurazioneAttribuzionePagamentoServizioDAO;
		ConfigurazioneAttribuzionePagamentoServizio configurazioneAttribuzionePagamentoServizio = new ConfigurazioneAttribuzionePagamentoServizio();
		configurazioneAttribuzionePagamentoServizio.setCodiceSocieta(codiceSocieta);
		configurazioneAttribuzionePagamentoServizio.setCuteCute(codiceUtente);
		configurazioneAttribuzionePagamentoServizio.setChiaveEnte(chiaveEnte);
		configurazioneAttribuzionePagamentoServizio.setCodiceServizio(codiceServizio);
		configurazioneAttribuzionePagamentoServizio.setPriorita(priorita);
		//inizio LP PG21XX04 Leak
		//configurazioneAttribuzionePagamentoServizioDAO = WalletDAOFactory.getAttribuzionePagamentoServizio(getWalletDataSource(), getWalletDbSchema());
		//configurazioneAttribuzionePagamentoServizio = configurazioneAttribuzionePagamentoServizioDAO.select(configurazioneAttribuzionePagamentoServizio);
		Connection conn = null;
		try {
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			configurazioneAttribuzionePagamentoServizioDAO = WalletDAOFactory.getAttribuzionePagamentoServizio(conn, getWalletDbSchema());
			configurazioneAttribuzionePagamentoServizio = configurazioneAttribuzionePagamentoServizioDAO.select(configurazioneAttribuzionePagamentoServizio);
		} catch (Exception e1) {
			throw new DaoException(e1);
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
		return configurazioneAttribuzionePagamentoServizio;
	}
	
	private EsitoRisposte updatePrioritaSulServizio(HttpServletRequest request) throws DaoException {
		EsitoRisposte esito = new EsitoRisposte();
		esito.setCodiceMessaggio("OK");
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		String codiceServizio = (String)request.getAttribute("tx_tipologia_servizio_h");
		String prioritastr = (String)request.getAttribute("tx_priorita");
		Integer priorita = Integer.parseInt(prioritastr);
		String codiceTributo = (String)request.getAttribute("tx_codice_tributo");
		String codiceTributo2 = (String)request.getAttribute("tx_codice_tributo2");
		String codiceTributo3 = (String)request.getAttribute("tx_codice_tributo3");

		ConfigurazioneAttribuzionePagamentoServizioDAO configurazioneAttribuzionePagamentoServizioDAO;
		
		ConfigurazioneAttribuzionePagamentoServizio configurazioneAttribuzionePagamentoServizio = new ConfigurazioneAttribuzionePagamentoServizio();
		configurazioneAttribuzionePagamentoServizio.setCodiceSocieta(societa);
		configurazioneAttribuzionePagamentoServizio.setCuteCute(utente);
		configurazioneAttribuzionePagamentoServizio.setChiaveEnte(ente);
		configurazioneAttribuzionePagamentoServizio.setCodiceServizio(codiceServizio);
		configurazioneAttribuzionePagamentoServizio.setPriorita(priorita);
		configurazioneAttribuzionePagamentoServizio.setCodiceTributo(codiceTributo.trim());
		configurazioneAttribuzionePagamentoServizio.setCodiceTributo2(codiceTributo2.trim());
		configurazioneAttribuzionePagamentoServizio.setCodiceTributo3(codiceTributo3.trim());
		
		configurazioneAttribuzionePagamentoServizio.setOperatore(operatore);
		
		//inizio LP PG21XX04 Leak
		//configurazioneAttribuzionePagamentoServizioDAO = WalletDAOFactory.getAttribuzionePagamentoServizio(getWalletDataSource(), getWalletDbSchema());
		//esito = configurazioneAttribuzionePagamentoServizioDAO.update(configurazioneAttribuzionePagamentoServizio);
		//return esito;
		Connection conn = null;
		try {
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			configurazioneAttribuzionePagamentoServizioDAO = WalletDAOFactory.getAttribuzionePagamentoServizio(conn, getWalletDbSchema());
			esito = configurazioneAttribuzionePagamentoServizioDAO.update(configurazioneAttribuzionePagamentoServizio);
			return esito;
		} catch (Exception e1) {
			throw new DaoException(e1);
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
	
	private EsitoRisposte inserisciAttribuzionePagamentoServizio(HttpServletRequest request) throws DaoException 
	{
		String codiceServizio = (String)request.getAttribute("tx_tipologia_servizio");
		String prioritastr = (String)request.getAttribute("tx_priorita");
		Integer priorita = Integer.parseInt(prioritastr);
		String codiceTributo = (String)request.getAttribute("tx_codice_tributo");
		String codiceTributo2 = (String)request.getAttribute("tx_codice_tributo2");
		String codiceTributo3 = (String)request.getAttribute("tx_codice_tributo3");
		
		
		ConfigurazioneAttribuzionePagamentoServizioDAO configurazioneAttribuzionePagamentoServizioDAO;
		
		ConfigurazioneAttribuzionePagamentoServizio configurazioneAttribuzionePagamentoServizio = new ConfigurazioneAttribuzionePagamentoServizio();
		
		configurazioneAttribuzionePagamentoServizio.setCodiceSocieta(codiceSocieta);
		configurazioneAttribuzionePagamentoServizio.setCuteCute(codiceUtente);
		configurazioneAttribuzionePagamentoServizio.setChiaveEnte(chiaveEnte);
		configurazioneAttribuzionePagamentoServizio.setCodiceServizio(codiceServizio);
		configurazioneAttribuzionePagamentoServizio.setPriorita(priorita);
		configurazioneAttribuzionePagamentoServizio.setCodiceTributo(codiceTributo.trim());
		configurazioneAttribuzionePagamentoServizio.setOperatore(operatore);
		configurazioneAttribuzionePagamentoServizio.setCodiceTributo2(codiceTributo2.trim());
		configurazioneAttribuzionePagamentoServizio.setCodiceTributo3(codiceTributo3.trim());
		
		//inizio LP PG21XX04 Leak
		//configurazioneAttribuzionePagamentoServizioDAO = WalletDAOFactory.getAttribuzionePagamentoServizio(getWalletDataSource(), getWalletDbSchema());
		//return configurazioneAttribuzionePagamentoServizioDAO.insert(configurazioneAttribuzionePagamentoServizio);
		Connection conn = null;
		try {
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			configurazioneAttribuzionePagamentoServizioDAO = WalletDAOFactory.getAttribuzionePagamentoServizio(conn, getWalletDbSchema());
			return configurazioneAttribuzionePagamentoServizioDAO.insert(configurazioneAttribuzionePagamentoServizio);
		} catch (Exception e1) {
			throw new DaoException(e1);
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

	private EsitoRisposte controlli(HttpServletRequest request) {
		EsitoRisposte esito = new EsitoRisposte();
		esito.setCodiceMessaggio("OK");
		String codiceTributo = (String)request.getAttribute("tx_codice_tributo");
		String codiceTributo2 = (String)request.getAttribute("tx_codice_tributo2");
		String codiceTributo3 = (String)request.getAttribute("tx_codice_tributo3");
		if (!codiceTributo.equals(codiceTributo2))   {
			if (!codiceTributo2.equals(codiceTributo3)) {
				if (!codiceTributo.equals(codiceTributo3)) {
					
				} else {
					esito.setCodiceMessaggio("KO");
					esito.setDescrizioneMessaggio("Errore: Le periodicità non possono essere uguali");
				}
			} else {
				esito.setCodiceMessaggio("KO");
				esito.setDescrizioneMessaggio("Errore: Le periodicità non possono essere uguali");
			}
			
			
		} else {
			esito.setCodiceMessaggio("KO");
			esito.setDescrizioneMessaggio("Errore: Le periodicità non possono essere uguali");
		}
		return esito;
	}

	private EsitoRisposte deleteAttribuzionePagamentoServizio(HttpServletRequest request) throws DaoException 
	{
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		String codiceServizio = (String)request.getAttribute("tx_tipologia_servizio_h");
		String prorita = (String)request.getAttribute("tx_priorita");
		Integer prioritaInt = Integer.parseInt(prorita); 
		ConfigurazioneAttribuzionePagamentoServizioDAO configurazioneAttribuzionePagamentoServizioDAO;
		
		ConfigurazioneAttribuzionePagamentoServizio configurazioneAttribuzionePagamentoServizio = new ConfigurazioneAttribuzionePagamentoServizio();
		configurazioneAttribuzionePagamentoServizio.setCodiceSocieta(societa);
		configurazioneAttribuzionePagamentoServizio.setCuteCute(utente);
		configurazioneAttribuzionePagamentoServizio.setChiaveEnte(ente);
		configurazioneAttribuzionePagamentoServizio.setCodiceServizio(codiceServizio);
		configurazioneAttribuzionePagamentoServizio.setPriorita(prioritaInt);
		
		
		
		//inizio LP PG21XX04 Leak
		//configurazioneAttribuzionePagamentoServizioDAO = WalletDAOFactory.getAttribuzionePagamentoServizio(getWalletDataSource(), getWalletDbSchema());
		//return configurazioneAttribuzionePagamentoServizioDAO.delete(configurazioneAttribuzionePagamentoServizio);	 
		Connection conn = null;
		try {
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			configurazioneAttribuzionePagamentoServizioDAO = WalletDAOFactory.getAttribuzionePagamentoServizio(conn, getWalletDbSchema());
			return configurazioneAttribuzionePagamentoServizioDAO.delete(configurazioneAttribuzionePagamentoServizio);	 
		} catch (Exception e1) {
			throw new DaoException(e1);
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

	@SuppressWarnings("unchecked")
	private void getServiziDDL(HttpServletRequest request, HttpSession session) 
	{		
		//creo ddl option:
		ArrayList<Servizio> listaServizi = null;
		List<DdlOption> serviziDDLList  = new ArrayList<DdlOption>();
		ServizioDAO servizioDAO;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//servizioDAO = WalletDAOFactory.getServizioDAO(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			servizioDAO = WalletDAOFactory.getServizioDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			listaServizi = servizioDAO.listServizi();
			
			for (Iterator iterator = listaServizi.iterator(); iterator.hasNext();) {
				Servizio servizio = (Servizio) iterator.next();
				DdlOption optionServizio = new DdlOption();
				optionServizio.setSValue(servizio.getCodiceServizio());
				optionServizio.setSText(servizio.getDescrizioneServizio());
				serviziDDLList.add(optionServizio);
			}
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (JndiProxyException e1) {
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
		request.setAttribute("serviziDDLList", serviziDDLList);
		//session.setAttribute("serviziDDLList", serviziDDLList);
	}

	@SuppressWarnings("unchecked")
	private void getTributiDDL(HttpServletRequest request, HttpSession session) {
		//creo ddl option:
		ArrayList<Tributo> listaTributi = null;
		List<DdlOption> tributiDDLList  = new ArrayList<DdlOption>();
		TributoDAO tributoDAO;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//tributoDAO = WalletDAOFactory.getTributoDAO(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			tributoDAO = WalletDAOFactory.getTributoDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			listaTributi = tributoDAO.listTributo();
			
			for (Iterator iterator = listaTributi.iterator(); iterator.hasNext();) {
				Tributo tribito = (Tributo) iterator.next();
				DdlOption optionServizio = new DdlOption();
				optionServizio.setSValue(tribito.getCodiceTributo());
				optionServizio.setSText(tribito.getDescrizioneTributo());
				tributiDDLList.add(optionServizio);
			}
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (JndiProxyException e1) {
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
		request.setAttribute("tributiDDLList", tributiDDLList);
		
	}
	
}

