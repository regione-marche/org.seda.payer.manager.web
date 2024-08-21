package org.seda.payer.manager.ecmanager.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.WebRowSet;

import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.bean.AnagraficaBollettino;
import com.seda.payer.core.bean.CodiceAttivazione;
import com.seda.payer.core.dao.AnagraficaBollettinoDAO;
import com.seda.payer.core.dao.AnagraficaBollettinoDAOFactory;
import com.seda.payer.core.dao.CodiceAttivazioneDao;
import com.seda.payer.core.exception.DaoException;

@SuppressWarnings("serial")
public class AnagraficaBollettinoEditAction extends AnaBollettinoManagerBaseManagerAction  {
private static String codop = "init";
	
	private String codiceSocieta = "";
	private String codiceUtente = "";
	private String chiaveEnte = "";
	//inizio LP PG200130
	private String operatore = "";
	
	private DataSource anagraficaDataSource;
	private String dbSchemaCodSocieta;
	private String anagraficaDbSchema;

	protected DataSource getAnagraficaDataSource(HttpServletRequest request) {
	    if (anagraficaDataSource == null) {
	      dbSchemaCodSocieta = (String) request.getSession().getAttribute(
	          ManagerKeys.DBSCHEMA_CODSOCIETA);
	      PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext()
	          .getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
	      String dataSourceName = configuration.getProperty(PropertiesPath.dataSourceWallet
	          .format(dbSchemaCodSocieta));
	      try {
	        this.anagraficaDataSource = ServiceLocator.getInstance().getDataSource(
	            "java:comp/env/".concat(dataSourceName));
	      } catch (ServiceLocatorException e) {
	        throw new RuntimeException(e);
	      }
	    }
	    return this.anagraficaDataSource;
	  }

	  protected String getAnagraficaDbSchema(HttpServletRequest request) {
	    if (anagraficaDbSchema == null) {
	      PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext()
	          .getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
	      this.anagraficaDbSchema = configuration.getProperty(PropertiesPath.dataSourceSchemaWallet
	          .format(dbSchemaCodSocieta));
	    }

	    return this.anagraficaDbSchema;
	  }

	  public String getDbSchemaCodSocieta(HttpServletRequest request) {
	    if (dbSchemaCodSocieta == null) {
	      HttpSession session = request.getSession();
	      dbSchemaCodSocieta = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
	    }

	    return dbSchemaCodSocieta;
	  }
	//fine LP PG200130
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		
		HttpSession session = request.getSession();
		//inizio LP PG200130
		String template = getTemplateCurrentApplication(request, session);
		//fine LP PG200130
		/*
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		operatore = user.getUserName();
		*/
		//inizio LP PG200130
		operatore = "-";
		try {
			UserBean user = (UserBean) session.getAttribute(SignOnKeys.USER_BEAN);
			if(user != null) {
				operatore = user.getUserName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//fine LP PG200130
		
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			String codfisc = (String)request.getAttribute("codfisc");
			dividiSocUtenteEnte(request);
			if ( !codfisc.equals("")) {
				AnagraficaBollettino ana = null;
				//inizio LP PG21XX04 Leak
				Connection connection = null;
				//fine LP PG21XX04 Leak
				try {
					ana = select(request,codfisc);
					request.setAttribute("anagrafica", ana);
					//inizio LP PG200130
					//Carico il vettore dei codici di attivazione
					if(template.equalsIgnoreCase("soris")||template.equalsIgnoreCase("newsoris")) {
						//inizio LP PG21XX04 Leak
						//CodiceAttivazioneDao cod = new CodiceAttivazioneDao(getAnagraficaDataSource().getConnection(), getAnagraficaDbSchema());
						connection = new JndiProxy().getSqlConnection(null, dataSourceName, true);
						CodiceAttivazioneDao cod = new CodiceAttivazioneDao(connection, getAnagraficaDbSchema());
						//fine LP PG21XX04 Leak
						Object[] vettoreCodici = cod.doList(ana.getCodiceSocieta(), ana.getCodiceUtente(), ana.getChiaveEnte(), ana.getCodiceFiscale());
						request.setAttribute("vettoreCodici", vettoreCodici);
						session.setAttribute("vettoreCodici", vettoreCodici);
					}
					//fine LP PG200130
				} catch (DaoException e) {
					e.printStackTrace();
					session.setAttribute("tx_error_message", "Errore: " + e.getLocalizedMessage());
				//inizio LP PG200130
				} catch (JndiProxyException e) {
					e.printStackTrace();
					session.setAttribute("tx_error_message", "Errore: " + e.getLocalizedMessage());
				//fine LP PG200130
				}
				//inizio LP PG21XX04 Leak
				finally {
			    	try {
			    		if(connection != null) {
			    			connection.close();
			    		}
			    	} catch (SQLException e) {
			    		e.printStackTrace();
					}
				}
				//fine LP PG21XX04 Leak
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
		
		//inizio LP PG200130
		case TX_BUTTON_RIGENERACODICEATTIVAZIONE:
			codop="edit"; //TODO: devo rimanere sulla stesa pagina
			codiceSocieta = (String)request.getAttribute("tx_societa");
			codiceUtente  = (String)request.getAttribute("tx_utente");
			chiaveEnte = (String)request.getAttribute("tx_ente");
			String codiceFiscale = (String)request.getAttribute("tx_codfis");
			CodiceAttivazione codiceAttivazione = new CodiceAttivazione();
			codiceAttivazione.setCodiceSocieta(codiceSocieta);
			codiceAttivazione.setCodiceUtente(codiceUtente);
			codiceAttivazione.setChiaveEnte(chiaveEnte);
			codiceAttivazione.setCodiceFiscale(codiceFiscale);
			codiceAttivazione.setOperatore(operatore);			
			//inizio LP PG21XX04 Leak
			Connection connection = null;
			//fine LP PG21XX04 Leak
			try {
				//inizio LP PG21XX04 Leak
				//CodiceAttivazioneDao cod = new CodiceAttivazioneDao(getAnagraficaDataSource().getConnection(), getAnagraficaDbSchema());
				connection = new JndiProxy().getSqlConnection(null, dataSourceName, true);
				CodiceAttivazioneDao cod = new CodiceAttivazioneDao(connection, getAnagraficaDbSchema());
				//fine LP PG21XX04 Leak
				cod.doSave(codiceAttivazione);
				AnagraficaBollettino ana = null;
				ana = select(request, codiceFiscale);
				request.setAttribute("anagrafica", ana);
				cod = new CodiceAttivazioneDao(new JndiProxy().getSqlConnection(null, dataSourceName, true), getAnagraficaDbSchema());
				//Carico il vettore dei codici di attivazione
				Object[] vettoreCodici = cod.doList(ana.getCodiceSocieta(), ana.getCodiceUtente(), ana.getChiaveEnte(), ana.getCodiceFiscale());
				request.setAttribute("vettoreCodici", vettoreCodici);
				session.setAttribute("vettoreCodici", vettoreCodici);
			} catch (DaoException e) {
				session.setAttribute("tx_error_message", "Errore: " + e.getLocalizedMessage());
				e.printStackTrace();
			} catch (JndiProxyException e) {
				session.setAttribute("tx_error_message", "Errore: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
			//inizio LP PG21XX04 Leak
			finally {
		    	try {
		    		if(connection != null) {
		    			connection.close();
		    		}
		    	} catch (SQLException e) {
		    		e.printStackTrace();
				}
			}
			//fine LP PG21XX04 Leak
			break;
		//fine LP PG200130
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
		String codiceSDI = (String)request.getAttribute("tx_codiceSDI");
		

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
		ana.setCodiceSDI(codiceSDI);
		
		
		
		AnagraficaBollettinoDAO controller;
		//inizio LP PG21XX04 Leak
		//controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(getAnagraficaDataSource(), getAnagraficaDbSchema());
		//controller.updateAnaBollettino(ana);	
		Connection connection = null;
		try {
			connection = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(connection, getAnagraficaDbSchema());
			controller.updateAnaBollettino(ana, operatore);	
		} catch (DaoException e) {
			throw new DaoException(e);
		} catch (JndiProxyException e) {
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
			connection = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(connection, getAnagraficaDbSchema());
			res = controller.doDetail(codiceSocieta,codiceUtente,chiaveEnte,CodiceFicale);
		} catch (DaoException e) {
			throw new DaoException(e);
		} catch (JndiProxyException e) {
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
