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
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.ConfigurazioneEvoIntimazioni;
import com.seda.payer.core.wallet.bean.ConfigurazionePagamentoServizio;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.dao.ConfigurazioneEvoIntimazioniDAO;
import com.seda.payer.core.wallet.dao.ConfigurazioneAttribuzionePagamentoServizioDAO;
import com.seda.payer.core.wallet.dao.ConfigurazionePagamentoServizioDAO;
import com.seda.payer.core.wallet.dao.ServizioDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
import com.seda.tag.core.DdlOption;
import com.thoughtworks.xstream.converters.basic.IntConverter;

@SuppressWarnings("serial")
public class PagamentoPerServizioEditAction extends WalletBaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "pagamentoperservizio";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
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
			dividiSocUtenteEnte(request);
			request.setAttribute("tx_tipologia_servizio_h",chiaverecord);
			
			
			if ( !chiaverecord.equals("") && !chiaverecord.equals("")) {
				//ConfigurazioneSolleciti  configurazioneSolleciti = null;
				ConfigurazionePagamentoServizio  configurazionePagamentoServizio = null; 
				try {
					configurazionePagamentoServizio = selectConfigurazionePagamentoPerServizio(request);
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				}
				getServiziDDL(request, session);
				request.setAttribute("tx_tipologia_servizio", configurazionePagamentoServizio.getCodiceServizio());
				request.setAttribute("tx_priorita", configurazionePagamentoServizio.getPriorita());
				request.setAttribute("tx_ente_srv", configurazionePagamentoServizio.getEnteServizio());
				request.setAttribute("tx_is_srv", configurazionePagamentoServizio.getImpostaServizio());
				
			
			}
			
			break;
			
		case TX_BUTTON_EDIT:
			try {
				updatePagamentoPerServizio(request);
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
				esito = inserisciPagamentoperServizio(request);
				if(esito.getCodiceMessaggio().equals("OK"))
				{
					session.setAttribute("recordInsert", "OK");
					//setFormMessage("form_selezione", Messages.INS_OK.format(), request);
					
				}
				else
				{
					session.setAttribute("recordInsert", "KO");
					session.setAttribute("messaggio.recordInsert", esito.getDescrizioneMessaggio());
					//setFormMessage("form_selezione", esito.getDescrizioneMessaggio(), request);
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
			String chiaveRecord = (String)request.getAttribute("tx_tipologia_servizio");
			request.setAttribute("tx_tipologia_servizio_h",chiaveRecord);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deletePagamentoPerServizio(request);
					if(esito.getCodiceMessaggio().equals("OK"))
					{
						session.setAttribute("recordDelete", "OK");
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

	private ConfigurazionePagamentoServizio selectConfigurazionePagamentoPerServizio(HttpServletRequest request) throws DaoException {
		
		String codiceServizio  = (String)request.getAttribute("tx_tipologia_servizio");
		ConfigurazionePagamentoServizioDAO configurazionePagamentoServizioDAO;
		ConfigurazionePagamentoServizio configurazionePagamentoServizio = new ConfigurazionePagamentoServizio();
		configurazionePagamentoServizio.setCodiceSocieta(codiceSocieta);
		configurazionePagamentoServizio.setCuteCute(codiceUtente);
		configurazionePagamentoServizio.setChiaveEnte(chiaveEnte);
		configurazionePagamentoServizio.setCodiceServizio(codiceServizio);
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		try {
			//inizio LP PG21XX04 Leak
			//configurazionePagamentoServizioDAO = WalletDAOFactory.getPagamentoPerservizio(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			configurazionePagamentoServizioDAO = WalletDAOFactory.getPagamentoPerservizio(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			configurazionePagamentoServizio = configurazionePagamentoServizioDAO.select(configurazionePagamentoServizio);

			return configurazionePagamentoServizio;
		//inizio LP PG21XX04 Leak
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
	
	private Integer updatePagamentoPerServizio(HttpServletRequest request) throws DaoException {
		
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		//Date dataValString =  Date.valueOf((String)request.getAttribute("tx_dval_sms_h").toString().substring(0,10));
		String codiceServizio = (String)request.getAttribute("tx_tipologia_servizio_h");
		String prioritastr = (String)request.getAttribute("tx_priorita");
		Integer priorita = Integer.parseInt(prioritastr);
		String enteServ = (String)request.getAttribute("tx_ente_srv");
		String imposta_servizio = (String)request.getAttribute("tx_is_srv");

		ConfigurazionePagamentoServizioDAO configurazionePagamentoServizioDAO;
		
		ConfigurazionePagamentoServizio configurazionePagamentoServizio = new ConfigurazionePagamentoServizio();
		configurazionePagamentoServizio.setCodiceSocieta(societa);
		configurazionePagamentoServizio.setCuteCute(utente);
		configurazionePagamentoServizio.setChiaveEnte(ente);
		configurazionePagamentoServizio.setCodiceServizio(codiceServizio);
		configurazionePagamentoServizio.setPriorita(priorita);
		configurazionePagamentoServizio.setOperatore(operatore);
		configurazionePagamentoServizio.setEnteServizio(enteServ);
		configurazionePagamentoServizio.setImpostaServizio(imposta_servizio);
		
		//inizio LP PG21XX04 Leak
		//configurazionePagamentoServizioDAO = WalletDAOFactory.getPagamentoPerservizio(getWalletDataSource(), getWalletDbSchema());
		//int aggiornati = configurazionePagamentoServizioDAO.update(configurazionePagamentoServizio);
		//return aggiornati;
		Connection conn = null;
		try {
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			configurazionePagamentoServizioDAO = WalletDAOFactory.getPagamentoPerservizio(conn, getWalletDbSchema());
			int aggiornati = configurazionePagamentoServizioDAO.update(configurazionePagamentoServizio);
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
	
private EsitoRisposte inserisciPagamentoperServizio(HttpServletRequest request) throws DaoException {
		
		String codiceServizio = (String)request.getAttribute("tx_tipologia_servizio");
		String prioritastr = (String)request.getAttribute("tx_priorita");
		Integer priorita = Integer.parseInt(prioritastr);
		
		String enteSrv = (String)request.getAttribute("tx_ente_srv");
		String impostaSrv = (String)request.getAttribute("tx_is_srv");
		
		ConfigurazionePagamentoServizioDAO configurazionePagamentoServizioDAO;
		
		ConfigurazionePagamentoServizio configurazionePagamentoServizio = new ConfigurazionePagamentoServizio();
		configurazionePagamentoServizio.setCodiceSocieta(codiceSocieta);
		configurazionePagamentoServizio.setCuteCute(codiceUtente);
		configurazionePagamentoServizio.setChiaveEnte(chiaveEnte);
		configurazionePagamentoServizio.setCodiceServizio(codiceServizio);
		configurazionePagamentoServizio.setPriorita(priorita);
		configurazionePagamentoServizio.setEnteServizio(enteSrv);
		configurazionePagamentoServizio.setImpostaServizio(impostaSrv);
		UserBean user = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
		configurazionePagamentoServizio.setOperatore(user.getUserName());
		
		//inizio LP PG21XX04 Leak
		//configurazionePagamentoServizioDAO = WalletDAOFactory.getPagamentoPerservizio(getWalletDataSource(), getWalletDbSchema());
		//return configurazionePagamentoServizioDAO.insert(configurazionePagamentoServizio);
		Connection conn = null;
		try {
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			configurazionePagamentoServizioDAO = WalletDAOFactory.getPagamentoPerservizio(conn, getWalletDbSchema());
			return configurazionePagamentoServizioDAO.insert(configurazionePagamentoServizio);
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

private EsitoRisposte deletePagamentoPerServizio(HttpServletRequest request) throws DaoException {
	String  societa = (String)request.getAttribute("tx_societa");
	String  utente = (String)request.getAttribute("tx_utente");
	String  ente = (String)request.getAttribute("tx_ente");
	
	String codiceServizio = (String)request.getAttribute("tx_tipologia_servizio_h");
	
	ConfigurazionePagamentoServizioDAO configurazionePagamentoServizioDAO;
	
	ConfigurazionePagamentoServizio configurazionePagamentoServizio = new ConfigurazionePagamentoServizio();
	configurazionePagamentoServizio.setCodiceSocieta(societa);
	configurazionePagamentoServizio.setCuteCute(utente);
	configurazionePagamentoServizio.setChiaveEnte(ente);
	configurazionePagamentoServizio.setCodiceServizio(codiceServizio);
	
	//inizio LP PG21XX04 Leak
	//configurazionePagamentoServizioDAO = WalletDAOFactory.getPagamentoPerservizio(getWalletDataSource(), getWalletDbSchema());
	//return configurazionePagamentoServizioDAO.delete(configurazionePagamentoServizio);
	Connection conn = null;
	try {
		conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
		configurazionePagamentoServizioDAO = WalletDAOFactory.getPagamentoPerservizio(conn, getWalletDbSchema());
		return configurazionePagamentoServizioDAO.delete(configurazionePagamentoServizio);
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

	@SuppressWarnings("unchecked")
	private void getServiziDDL(HttpServletRequest request, HttpSession session) {
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
}

