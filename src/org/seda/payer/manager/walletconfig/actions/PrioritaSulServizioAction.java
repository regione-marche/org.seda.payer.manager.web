package org.seda.payer.manager.walletconfig.actions;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;

import com.seda.data.page.Page;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.ConfigurazioneAttribuzionePagamentoServizio;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.bean.Tributo;
import com.seda.payer.core.wallet.bean.WalletPageList;
import com.seda.payer.core.wallet.dao.ConfigurazioneAttribuzionePagamentoServizioDAO;
import com.seda.payer.core.wallet.dao.ServizioDAO;
import com.seda.payer.core.wallet.dao.TributoDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
import com.seda.tag.core.DdlOption;



import org.seda.payer.manager.util.Messages;

import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;


public class PrioritaSulServizioAction extends WalletBaseManagerAction{

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber; 
	private String username;
	private String codiceSocieta="";
	private String cutecute="";
	private String chiaveEnte="";
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
			setProfile(request);
		// carico le DDL della società, cutecute ed Ente
		
		HttpSession sessionlocal = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		username = user.getUserName();
//		if ((String) session.getAttribute("recordInsert") != null) 
//		{
//			
//			if(session.getAttribute("recordInsert").toString().equals("OK"))
//				setFormMessage("form_selezione", Messages.INS_OK.format(), request);
//			else
//				setFormMessage("form_selezione", session.getAttribute("messaggio.recordInsert").toString(), request);
//			
//			session.removeAttribute("recordInsert");
//			firedButton = firedButton.TX_BUTTON_CERCA; 
//		}
		
//		if ((String) session.getAttribute("recordUpdate") != null) 
//		{
//			
//			if(session.getAttribute("recordUpdate").toString().equals("OK"))
//				setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
//			else
//				setFormMessage("form_selezione", session.getAttribute("messaggio.recordUpdate").toString(), request);
//			
//			session.removeAttribute("recordUpdate");
//			firedButton = firedButton.TX_BUTTON_CERCA; 
//		}
		if ((String) session.getAttribute("recordDelete") != null) 
		{
			
			if(session.getAttribute("recordDelete").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.CANC_OK.format(), request);
			else
				setFormMessage("form_selezione", session.getAttribute("messaggio.recordDelete").toString(), request);
			
			session.removeAttribute("recordDelete");
			firedButton = firedButton.TX_BUTTON_CERCA; 
		}
		
		loadSocietaUtenteEnteXml_DDL(request, session);
		
		getServiziDDL(request, session);
		getTributiDDL(request, session);
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		
		switch(firedButton)
		{
			
			case TX_BUTTON_CERCA: 
				try {
					dividiSocUtenteEnte(request);
					
					//WalletPageList walletPageListlist = listaConfigurazioneEvoluzioneIntimazione(request);
					WalletPageList walletPageListlist = listaConfigurazioneAttrPagServizio(request);
					
					PageInfo pageInfo = walletPageListlist.getPageInfo();
					
					if (walletPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								request.setAttribute("lista_configurazioni_evoluzioni", walletPageListlist.getWalletListXml());
								request.setAttribute("lista_configurazioni_evoluzioni.pageInfo", pageInfo);
							}
							else { 
								request.setAttribute("lista_configurazioni_evoluzioni", null);
								setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
							}
						}
						else { 
							setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
						}
					}
				} catch(Exception e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				}
				break;
				
			case TX_BUTTON_RESET:
				resetParametri(request);
				request.setAttribute("tx_data_pag_da", null);
				request.setAttribute("tx_data_pag_a", null);
				request.setAttribute("tx_data_cre_da", null);
				request.setAttribute("tx_data_cre_a", null);
				setProfile(request);
				
				break;
				
			case TX_BUTTON_SOCIETA_CHANGED:
				String societa = getParamCodiceSocieta();
				//LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvincia), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : getParamCodiceUtente()), true);
				request.setAttribute("tx_provincia","");
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : getParamCodiceUtente()), true);
				request.setAttribute("tx_UtenteEnte", "");
				break;
			
			case TX_BUTTON_ENTE_CHANGED:
				break;
				
				
			case TX_BUTTON_NULL: 
				break;
				
			case TX_BUTTON_STAMPA:
				break;
				
			case TX_BUTTON_DOWNLOAD:
				break;
		}
		return null;
	}
 
		
	private WalletPageList listaConfigurazioneAttrPagServizio(HttpServletRequest request) {
		ConfigurazioneAttribuzionePagamentoServizioDAO configurazioneAttribuzionePagamentoServizioDAO;
		ConfigurazioneAttribuzionePagamentoServizio  configurazioneAttribuzionePagamentoServizio = new ConfigurazioneAttribuzionePagamentoServizio();
		configurazioneAttribuzionePagamentoServizio.setCodiceSocieta(codiceSocieta);
		configurazioneAttribuzionePagamentoServizio.setCuteCute(cutecute);
		configurazioneAttribuzionePagamentoServizio.setChiaveEnte(chiaveEnte);
		configurazioneAttribuzionePagamentoServizio.setCodiceServizio((String)request.getAttribute("tx_tipologia_servizio"));
		configurazioneAttribuzionePagamentoServizio.setCodiceTributo((String)request.getAttribute("tx_codice_tributo"));
		
		WalletPageList walletPageList = new WalletPageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneAttribuzionePagamentoServizioDAO = WalletDAOFactory.getAttribuzionePagamentoServizio(getWalletDataSource(), getWalletDbSchema());
			conn = getWalletDataSource().getConnection();
			configurazioneAttribuzionePagamentoServizioDAO = WalletDAOFactory.getAttribuzionePagamentoServizio(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			walletPageList = configurazioneAttribuzionePagamentoServizioDAO.ListaConfigurazioneAttribuzionePagamentoServizio(configurazioneAttribuzionePagamentoServizio, rowsPerPage, pageNumber, "");
			
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
		return walletPageList;
		
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
			conn = getWalletDataSource().getConnection();
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

		request.setAttribute("tributiDDLList", tributiDDLList);
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
				chiaveEnte = codici[1];
				chiaveEnte = codici[2];
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", chiaveEnte);
				request.setAttribute("tx_ente", chiaveEnte);
			}
		
		}
	}
	
	
}

