package org.seda.payer.manager.ecmanager.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;


import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.payer.core.bean.AnagraficaBollettino;
import com.seda.payer.core.dao.AnagraficaBollettinoDAO;
import com.seda.payer.core.dao.AnagraficaBollettinoDAOFactory;
import com.seda.payer.core.exception.DaoException;

@SuppressWarnings("serial")
public class EcManagerEditAction extends AnaBollettinoManagerBaseManagerAction  {
private static String codop = "init";
	
	private String codiceSocieta = "";
	private String codiceUtente = "";
	private String chiaveEnte = "";
	private String operatore = "";
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		
		HttpSession session = request.getSession();
		
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		operatore = user.getUserName();
		
		
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			String codfisc = (String)request.getAttribute("codfisc");
			dividiSocUtenteEnte(request);
			if ( !codfisc.equals("")) {
				AnagraficaBollettino ana = null;
				try {
					ana = select(request,codfisc);
					request.setAttribute("anagrafica", ana);
				} catch (DaoException e) {
					e.printStackTrace();
					session.setAttribute("tx_error_message", "Errore: " + e.getLocalizedMessage());
				}
			}
			break;
			
		case TX_BUTTON_EDIT:
			codop="edit";
			session.setAttribute("vista", "anagraficacontribuenti");
			try {
				updateAnagrafica(request);
				session.setAttribute("tx_message", "Aggiornamento effettuato correttamente!");
			} catch (DaoException e) {
				e.printStackTrace();
				session.setAttribute("tx_error_message", "Errore: " + e.getLocalizedMessage());
			}
			
			break;
		
			
		case TX_BUTTON_INDIETRO:
			codop="edit";
			session.setAttribute("vista", "anagraficacontribuenti");
			break;
		
		}
		request.setAttribute("codop",codop);
		//request.setAttribute(ManagerKeys.REQUEST_ACTION_FLOW,codop);
		return null;
	}
	private void updateAnagrafica(HttpServletRequest request) throws DaoException{
		String societa = (String)request.getAttribute("tx_societa");
		String utente = (String)request.getAttribute("tx_utente");
		String ente = (String)request.getAttribute("tx_ente");
		String codFisc = (String)request.getAttribute("tx_codfis");
		String cognome = (String)request.getAttribute("tx_Cognome");
		String nome = (String)request.getAttribute("tx_Nome");
		String numCell = (String)request.getAttribute("tx_numeroCell");
		String indirizzoMail = (String)request.getAttribute("tx_indirizzoEmail");
		String indirizzoMailPec = (String)request.getAttribute("tx_indirizzoEmailPec");
		String indirizzo = (String)request.getAttribute("tx_indirizzo");
		String cap = (String)request.getAttribute("tx_cap");
		String provincia = (String)request.getAttribute("tx_provincia");
		String comune = (String)request.getAttribute("tx_comune");

		AnagraficaBollettino ana =new AnagraficaBollettino();
		ana.setCodiceSocieta(societa);
		ana.setCodiceUtente(utente);
		ana.setChiaveEnte(ente);
		ana.setCodiceFiscale(codFisc);
		ana.setCognome(cognome);
		ana.setNome(nome);
		ana.setCellulare(numCell);
		ana.setMail(indirizzoMail);
		ana.setMailPec(indirizzoMailPec);
		ana.setIndirizzo(indirizzo);
		ana.setCap(cap);
		ana.setProvincia(provincia);
		ana.setComune(comune);
		
		AnagraficaBollettinoDAO controller;
		//inizio LP PG21XX04 Leak
		//controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(getAnagraficaDataSource(), getAnagraficaDbSchema());
		//controller.updateAnaBollettino(ana);	
		Connection connection = null;
		try {
			connection = getAnagraficaDataSource().getConnection();
			controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(connection, getAnagraficaDbSchema());
			controller.updateAnaBollettino(ana, operatore);	
		} catch (DaoException e) {
			throw new DaoException(e);
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
	}
	
	private void dividiSocUtenteEnte(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String ddlSocietaUtenteEnte ="";
		if (request.getAttribute("ddlSocietaUtenteEnte")!=null){
			ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			if (session.getAttribute("ddlSocietaUtenteEnte")==null){
				session.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
			if (!request.getAttribute("ddlSocietaUtenteEnte").toString().equals(session.getAttribute("ddlSocietaUtenteEnte").toString())){
				session.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
		}
		else{
			if (session.getAttribute("ddlSocietaUtenteEnte")!=null){
				ddlSocietaUtenteEnte = (String)session.getAttribute("ddlSocietaUtenteEnte");
				request.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
		}
		
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
	public AnagraficaBollettino select(HttpServletRequest request,String CodiceFicale) throws DaoException {
		AnagraficaBollettinoDAO controller;
		AnagraficaBollettino res = null;
		//inizio LP PG21XX04 Leak
		//controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(getAnagraficaDataSource(), getAnagraficaDbSchema());
		//res = controller.doDetail(codiceSocieta,codiceUtente,chiaveEnte,CodiceFicale);
		Connection connection = null;
		try {
			connection = getAnagraficaDataSource().getConnection();
			controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(connection, getAnagraficaDbSchema());
			res = controller.doDetail(codiceSocieta,codiceUtente,chiaveEnte,CodiceFicale);
		} catch (DaoException e) {
			throw new DaoException(e);
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		return res;
	}
}
