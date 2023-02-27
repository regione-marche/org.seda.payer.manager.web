package org.seda.payer.manager.walletconfig.actions;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.ConfigurazioneRaccordoPagonet;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.dao.ConfigurazioneRaccordoPagonetDAO;
import com.seda.payer.core.wallet.dao.ServizioDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest3;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.tag.core.DdlOption;

@SuppressWarnings("serial")
public class RaccordoPagonetEditAction extends WalletBaseManagerAction{
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
			if(codiceSocieta!=""){
				try {
					loadDdlChiave(request);
				} catch (FaultType e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			request.setAttribute("tx_tipologia_servizio_h",chiaverecord);
			
			
			
			if ( !chiaverecord.equals("") && !chiaverecord.equals("")) {
				//ConfigurazioneSolleciti  configurazioneSolleciti = null;
				 
				//ConfigurazioneAttribuzionePagamentoServizio  configurazioneAttribuzionePagamentoServizio = null;
				ConfigurazioneRaccordoPagonet  configurazioneRaccordoPagonet = null;
				try {
					
					configurazioneRaccordoPagonet = selectConfigurazioneRaccordoPagonet(request);
					
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				}
				getServiziDDL(request, session);
				// parametri per valorizzare FORM di modifica
				request.setAttribute("tx_tipologia_servizio", configurazioneRaccordoPagonet.getTipologiaServizio());
				request.setAttribute("tx_autbollettino",configurazioneRaccordoPagonet.getDescrAutBollettini());
				request.setAttribute("tx_autbollettino_2",configurazioneRaccordoPagonet.getDescrAutBollettini_2());
			}
			
			try {
				loadDdlChiave(request);
			} catch (FaultType e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			break;
			
		case TX_BUTTON_EDIT:
			try {
				updateRaccordoPagonet(request);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			session.setAttribute("recordUpdate", "OK");
			//setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
			break;
		
		case TX_BUTTON_ADD:
		case TX_BUTTON_SOCIETA_CHANGED:
			codop = "add";
			try {
				dividiSocUtenteEnte(request);
				if(codiceSocieta!="")
				{
					loadDdlChiave(request);
				}
			} catch (FaultType e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			//request.setAttribute("tx_button_cerca", "Search");	
			break;
		case TX_BUTTON_AGGIUNGI:
			try {
				dividiSocUtenteEnte(request);
				EsitoRisposte esito = new EsitoRisposte();
				esito = inserisciRaccordoPagonet(request);
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
			String tx_tipologia_servizio_h = (String)request.getAttribute("tx_tipologia_servizio");
			String tx_ordine_scuola_h = (String)request.getAttribute("tx_ordine_scuola");
			request.setAttribute("tx_tipologia_servizio_h",tx_tipologia_servizio_h);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteRaccordoPagonet(request);
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
		if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
		{
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
	}

	private ConfigurazioneRaccordoPagonet selectConfigurazioneRaccordoPagonet(HttpServletRequest request) throws DaoException {
		
		String tipologiaServizio  = (String)request.getAttribute("tx_tipologia_servizio");
		ConfigurazioneRaccordoPagonetDAO configurazioneRaccordoPagonetDAO;
		ConfigurazioneRaccordoPagonet  configurazioneRaccordoPagonet = new ConfigurazioneRaccordoPagonet();
		configurazioneRaccordoPagonet.setCodiceSocieta(codiceSocieta);
		configurazioneRaccordoPagonet.setCuteCute(codiceUtente);
		configurazioneRaccordoPagonet.setChiaveEnte(chiaveEnte);
		configurazioneRaccordoPagonet.setTipologiaServizio(tipologiaServizio);
		//inizio LP PG21XX04 Leak
		//configurazioneRaccordoPagonetDAO = WalletDAOFactory.getRaccordoPagonet(getWalletDataSource(), getWalletDbSchema());
		//configurazioneRaccordoPagonet = configurazioneRaccordoPagonetDAO.select(configurazioneRaccordoPagonet);
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			configurazioneRaccordoPagonetDAO = WalletDAOFactory.getRaccordoPagonet(conn, getWalletDbSchema());
			configurazioneRaccordoPagonet = configurazioneRaccordoPagonetDAO.select(configurazioneRaccordoPagonet);
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
		return configurazioneRaccordoPagonet;
	}
	
	private Integer updateRaccordoPagonet(HttpServletRequest request) throws DaoException {
		
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		String tipologiaServizio = (String)request.getAttribute("tx_tipologia_servizio");
		//Date dataValString =  Date.valueOf((String)request.getAttribute("tx_dval_sms_h").toString().substring(0,10));
		String descrAutBollettini = (String)request.getAttribute("tx_autbollettino");
		String descrAutBollettini_2 = (String)request.getAttribute("tx_autbollettino_2");
		

		ConfigurazioneRaccordoPagonetDAO configurazioneRaccordoPagonetDAO;
		
		ConfigurazioneRaccordoPagonet configurazioneRaccordoPagonet = new ConfigurazioneRaccordoPagonet();
		configurazioneRaccordoPagonet.setCodiceSocieta(societa);
		configurazioneRaccordoPagonet.setCuteCute(utente);
		configurazioneRaccordoPagonet.setChiaveEnte(ente);
		configurazioneRaccordoPagonet.setTipologiaServizio(tipologiaServizio);
		configurazioneRaccordoPagonet.setOperatore(operatore);
		configurazioneRaccordoPagonet.setDescrAutBollettini(descrAutBollettini);
		configurazioneRaccordoPagonet.setDescrAutBollettini_2(descrAutBollettini_2);
		//inizio LP PG21XX04 Leak
		//configurazioneRaccordoPagonetDAO = WalletDAOFactory.getRaccordoPagonet(getWalletDataSource(), getWalletDbSchema());
		//int aggiornati = configurazioneRaccordoPagonetDAO.update(configurazioneRaccordoPagonet);
		//return aggiornati;
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			configurazioneRaccordoPagonetDAO = WalletDAOFactory.getRaccordoPagonet(conn, getWalletDbSchema());
			int aggiornati = configurazioneRaccordoPagonetDAO.update(configurazioneRaccordoPagonet);
			return aggiornati;
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
	
private EsitoRisposte inserisciRaccordoPagonet(HttpServletRequest request) throws DaoException {
		
		String tipologiaServizio = (String)request.getAttribute("tx_tipologia_servizio");
		String descrAutBollettini = (String)request.getAttribute("tx_autbollettino");
		String descrAutBollettini_2 = (String)request.getAttribute("tx_autbollettino_2");
		ConfigurazioneRaccordoPagonetDAO configurazioneRaccordoPagonetDAO;
		ConfigurazioneRaccordoPagonet configurazioneRaccordoPagonet = new ConfigurazioneRaccordoPagonet(); 
		configurazioneRaccordoPagonet.setCodiceSocieta(codiceSocieta);
		configurazioneRaccordoPagonet.setCuteCute(codiceUtente);
		configurazioneRaccordoPagonet.setChiaveEnte(chiaveEnte);
		configurazioneRaccordoPagonet.setTipologiaServizio(tipologiaServizio);
		configurazioneRaccordoPagonet.setOperatore(operatore);
		configurazioneRaccordoPagonet.setDescrAutBollettini(descrAutBollettini);
		configurazioneRaccordoPagonet.setDescrAutBollettini_2(descrAutBollettini_2);
		//inizio LP PG21XX04 Leak
		//configurazioneRaccordoPagonetDAO = WalletDAOFactory.getRaccordoPagonet(getWalletDataSource(), getWalletDbSchema());
		//return configurazioneRaccordoPagonetDAO.insert(configurazioneRaccordoPagonet);
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			configurazioneRaccordoPagonetDAO = WalletDAOFactory.getRaccordoPagonet(conn, getWalletDbSchema());
			return configurazioneRaccordoPagonetDAO.insert(configurazioneRaccordoPagonet);
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

private EsitoRisposte deleteRaccordoPagonet(HttpServletRequest request) throws DaoException {
	String  societa = (String)request.getAttribute("tx_societa");
	String  utente = (String)request.getAttribute("tx_utente");
	String  ente = (String)request.getAttribute("tx_ente");
	String tipologiaServizio = (String)request.getAttribute("tx_tipologia_servizio_h");
	
	ConfigurazioneRaccordoPagonetDAO configurazioneRaccordoPagonetDAO;
	ConfigurazioneRaccordoPagonet configurazioneRaccordoPagonet = new ConfigurazioneRaccordoPagonet();
	configurazioneRaccordoPagonet.setCodiceSocieta(societa);
	configurazioneRaccordoPagonet.setCuteCute(utente);
	configurazioneRaccordoPagonet.setChiaveEnte(ente);
	configurazioneRaccordoPagonet.setTipologiaServizio(tipologiaServizio);

	//inizio LP PG21XX04 Leak
	//configurazioneRaccordoPagonetDAO = WalletDAOFactory.getRaccordoPagonet(getWalletDataSource(), getWalletDbSchema());
	//return configurazioneRaccordoPagonetDAO.delete(configurazioneRaccordoPagonet);
	Connection conn = null;
	try {
		conn = getWalletDataSource().getConnection();
		configurazioneRaccordoPagonetDAO = WalletDAOFactory.getRaccordoPagonet(conn, getWalletDbSchema());
		return configurazioneRaccordoPagonetDAO.delete(configurazioneRaccordoPagonet);
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
		conn = getWalletDataSource().getConnection();
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
	request.setAttribute("serviziDDLList", serviziDDLList);
	//session.setAttribute("serviziDDLList", serviziDDLList);
	}
	
	private void loadDdlChiave(HttpServletRequest request) throws FaultType, RemoteException
	{
		
		ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse2 = null;
		configUtenteTipoServizioEnteSearchResponse2 = getConfigUtenteTipoServizioEnteSearchResponse3((new ConfigUtenteTipoServizioEnteSearchRequest3(codiceSocieta)), request);
		request.setAttribute("utentetiposervizios2", configUtenteTipoServizioEnteSearchResponse2.getResponse().getListXml());
		
		
	}
	
	private ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEntes2(String codiceSocieta,String codiceUtente,String chiaveEnte, String procName, HttpServletRequest request) throws FaultType, RemoteException{
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteSearchRequest2 in = new ConfigUtenteTipoServizioEnteSearchRequest2();
		
		in.setCompanyCode(codiceSocieta == null ? "" : codiceSocieta);
		in.setCodiceUtente(codiceUtente == null ? "" : codiceUtente);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setProcName(procName == null ? "" : procName);
		
		res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes2(in, request);
		
		return res;
	}
	private ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEnteSearchResponse3(
			ConfigUtenteTipoServizioEnteSearchRequest3 configUtenteTipoServizioEnteSearchRequest3, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteSearchRequest3 in = new ConfigUtenteTipoServizioEnteSearchRequest3();
		in.setCompanyCode(configUtenteTipoServizioEnteSearchRequest3.getCompanyCode() == null ? "" : configUtenteTipoServizioEnteSearchRequest3.getCompanyCode());
		res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes3(in, request);
		return res;
	}
	
}

