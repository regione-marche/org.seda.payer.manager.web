package org.seda.payer.manager.configurazione.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.configurazione.views.EnteImpostaServizioNotificaView;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.views.BaseView.BaseListName;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.core.bean.ConfigurazioneEnteISNotifica;
import com.seda.payer.core.bean.ConfigurazioneEnteISNotificaPagelist;
import com.seda.payer.core.dao.ConfigurazioneEnteISNotificaDAOFactory;
import com.seda.payer.core.dao.ConfigurazioneEnteISNotificaDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.EsitoRisposte;

public class EnteImpostaServizioNotificaAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private EnteImpostaServizioNotificaView view;
	private boolean bDdlCascade = false;

	private DataSource configurazioneEnteISNotificaDataSource;
	private String dbSchemaCodSocieta;
	private String configurazioneEnteISNotificaDbSchema;
	protected DataSource getConfigurazioneEnteISNotificaDataSource(){return this.configurazioneEnteISNotificaDataSource;}
	protected String getConfigurazioneEnteISNotificaDbSchema(){return this.configurazioneEnteISNotificaDbSchema;}
	
	private int rowsPerPage;
	private int pageNumber; 
	private String operatore = "";
	private String cutecute = "";
	private String chiaveEnte = "";
	private String codiceImpostaServizio = "";
	private String flagNotificaAllegato = "";
	
	@Override
	public void start(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		operatore = user.getUserName();
		
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String configurazioneEnteISNotificaDataSource = configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta)); 
		this.configurazioneEnteISNotificaDbSchema = configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		
		try {	
			this.configurazioneEnteISNotificaDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(configurazioneEnteISNotificaDataSource));
			
		} catch (ServiceLocatorException e) {
			e.printStackTrace();
		}
		
		view = new EnteImpostaServizioNotificaView(context, request);
		if (view.getFiredButton() != null) {
			if (view.getFiredButton().equals(EnteImpostaServizioNotificaView.NewButton)) {
				try { add(request, false);
				} catch (ActionException e) { e.printStackTrace(); }
			}
		}
		if (view.getFiredButtonBack() != null) {
			if (view.getFiredButtonBack().equals(EnteImpostaServizioNotificaView.BackButton)) {
				try { index(request);
				} catch (ActionException e) { e.printStackTrace(); }
			}
		}
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		bDdlCascade = false;
		search(request);
		return null;
	}

	public Object search(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, EnteImpostaServizioNotificaView.SearchScope);
			if (view.getFiredButton() != null)
				view.reset();

			if (view.getFiredButtonReset() != null)
				view.reset();

			if (!bDdlCascade)
			{
				view.setBaseList(BaseListName.listaUtenti, false);
				view.setBaseList(BaseListName.listaEntiGenerici, false);
			}
			cutecute = view.getUserCode();
			if (cutecute != null && (cutecute.length() > 0)) {
				//UserCode valorizzato come 00004000TO
				cutecute = cutecute.substring(5);
			}
			chiaveEnte = view.getChiaveEnte();
			if (chiaveEnte != null && (chiaveEnte.length() > 0)) {
				//ChiaveEnte valorizzato come 00004000TOANE0000009
				chiaveEnte = chiaveEnte.substring(10);
			}
			codiceImpostaServizio = view.getCodiceImpostaServizio();
			flagNotificaAllegato = view.getFlagNotificaAllegato();
			ConfigurazioneEnteISNotificaPagelist lst = getConfigurazioneEnteISNotificaPagelist(request);
			PageInfo pageInfoR = lst.getPageInfo();
			if (!lst.getRetCode().equals("00")) {
				setFormMessage("form_ricerca", "Errore generico - Impossibile recuperare i dati", request);
			} else {
				if(pageInfoR != null)
				{
					if(pageInfoR.getNumRows() > 0)
					{
						PageInfo pageInfo = new PageInfo();
						pageInfo.setFirstRow(pageInfoR.getFirstRow());
						pageInfo.setLastRow(pageInfoR.getLastRow());
						pageInfo.setNumPages(pageInfoR.getNumPages());
						pageInfo.setNumRows(pageInfoR.getNumRows());
						pageInfo.setPageNumber(pageInfoR.getPageNumber());
						pageInfo.setRowsPerPage(view.getRowsPerPage());
							
						view = view.setListaRecDB(lst.getConfigurazioneEnteISNotificaListXml()).setListaRecDBPageInfo(pageInfo);
					}
					else {
						view = view.setListaRecDB(null).setListaRecDBPageInfo(null);
						setFormMessage("form_ricerca", Messages.NO_DATA_FOUND.format(), request);
					}
				}
				else { 
					setFormMessage("form_ricerca", "Errore generico - Impossibile recuperare i dati", request);
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public Object add(HttpServletRequest request, boolean bOther) throws ActionException {
		try {
			if (bOther) {
				bDdlCascade = true;
				view = view.populate(request, EnteImpostaServizioNotificaView.AddScope);
				view.setBaseList(BaseListName.listaUtenti, false);
				view.setBaseList(BaseListName.listaEntiGenerici, false);
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}

	public Object richiestacanc(HttpServletRequest request) {
		view = view.populate(request, EnteImpostaServizioNotificaView.RichiestaCancScope);

		System.out.println("richiestacanc userCode - " + view.getUserCode());
		System.out.println("richiestacanc chiaveEnte - " + view.getChiaveEnte());
		System.out.println("richiestacanc codiceImpostaServizio - " + view.getCodiceImpostaServizio());

		return null; 
	}

	public Object edit(HttpServletRequest request) throws ActionException {
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			view = view.populate(request, EnteImpostaServizioNotificaView.EditScope);
			String cod = view.getCodiceUtente();
			String key = view.getChiaveEnte();
			if(cod.length() > 5) {
				cod = cod.substring(5);
			}
			
			if(key.length() > 10) {
				key = key.substring(10);
			}
			ConfigurazioneEnteISNotifica conf = new ConfigurazioneEnteISNotifica();
			conf.setCodiceUtente(cod);
			conf.setChiaveEnte(key);
			conf.setCodiceImpostaServizio(view.getCodiceImpostaServizio());
			//inizio LP PG21XX04 Leak
			//ConfigurazioneEnteISNotificaDao dao = ConfigurazioneEnteISNotificaDAOFactory.getConfigurazioneEnteISNotifica(getConfigurazioneEnteISNotificaDataSource(), getConfigurazioneEnteISNotificaDbSchema());
			conn = getConfigurazioneEnteISNotificaDataSource().getConnection();
			ConfigurazioneEnteISNotificaDao dao = ConfigurazioneEnteISNotificaDAOFactory.getConfigurazioneEnteISNotifica(conn, getConfigurazioneEnteISNotificaDbSchema());
			//fineLP PG21XX04 Leak
			ConfigurazioneEnteISNotifica confOut = dao.select(conf);
			view = view.setUserCode(confOut.getCodiceSocieta() + confOut.getCodiceUtente())
	       	       .setChiaveEnte(confOut.getCodiceSocieta() + confOut.getCodiceUtente() + confOut.getChiaveEnte())
			       .setCodiceImpostaServizio(confOut.getCodiceImpostaServizio())
			       .setFlagNotificaAllegato(confOut.getFlagNotificaAllegato());
			// we retry base list
			view.setBaseList(BaseListName.listaUtenti, false);
			view.setBaseList(BaseListName.listaEntiGenerici, false);
		} catch (Exception e) { e.printStackTrace(); }
		//inizio LP PG21XX04 Leak
		finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		return null;
	}

	public Object saveadd(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, EnteImpostaServizioNotificaView.SaveAddScope);
			view.setMessage(Messages.INS_OK.format());
			if (view.getFiredButton() != null) {
				if (view.getFiredButton().equals(EnteImpostaServizioNotificaView.BackButton)) {
					view.reset();
					index(request);					
				}
			} else if (view.getFiredButtonReset() != null) {
				if (view.getFiredButtonReset().equals(EnteImpostaServizioNotificaView.ResetButton)) {
					view.reset();
					view.setScope(EnteImpostaServizioNotificaView.AddScope);
					add(request, true);
				}
			} else if (view.isBaseListChanged()) {
				view.setScope(EnteImpostaServizioNotificaView.AddScope);
				add(request, true);
			} else 
				save(request);
			

		} catch (Exception ignore) { }
		return null;
	}

	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, EnteImpostaServizioNotificaView.SaveEditScope);
			view.setMessage(Messages.UPDT_OK.format());
			if (view.getFiredButton() != null) {
				if (view.getFiredButton().equals(EnteImpostaServizioNotificaView.BackButton)) {
					view.reset();
					index(request);					
				}
			} else if (view.getFiredButtonReset() != null) {
				if (view.getFiredButtonReset().equals(EnteImpostaServizioNotificaView.ResetButton)) {
					view.setScope(EnteImpostaServizioNotificaView.EditScope);
					edit(request);					
				}
			} else if (view.isBaseListChanged()) {
				view.setScope(EnteImpostaServizioNotificaView.EditScope);
				edit(request);
			} else 
				save(request);
		} catch (Exception ignore) { }
		return null;
	}

	private void save(HttpServletRequest request) throws Exception {
		bDdlCascade = false;
		view = view.populate(request, EnteImpostaServizioNotificaView.SaveScope);
		String userCode = view.getUserCode();
		String chiaveEnte = view.getChiaveEnte();
		String codiceImpostaServizio = view.getCodiceImpostaServizio();
		String esito = checkInputFields();
		if (userCode.length() == 10) {
			userCode = userCode.substring(5);
		}
		if (chiaveEnte.length() == 20) {
			chiaveEnte = chiaveEnte.substring(10);
		}
		if (esito.equals("")) {
			view.setSuccess(view.getTypeRequest());
			//inizio LP PG21XX04 Leak
			Connection conn = null;
			//fine LP PG21XX04 Leak
			try {
				ConfigurazioneEnteISNotifica conf = new ConfigurazioneEnteISNotifica();
				conf.setCodiceUtente(userCode);
				conf.setChiaveEnte(chiaveEnte);
				conf.setCodiceImpostaServizio(codiceImpostaServizio);
				conf.setFlagNotificaAllegato(view.getFlagNotificaAllegato());
				conf.setCodiceOperatore(operatore);
				//inizio LP PG21XX04 Leak
				//ConfigurazioneEnteISNotificaDao dao = ConfigurazioneEnteISNotificaDAOFactory.getConfigurazioneEnteISNotifica(getConfigurazioneEnteISNotificaDataSource(), getConfigurazioneEnteISNotificaDbSchema());
				conn = getConfigurazioneEnteISNotificaDataSource().getConnection();
				ConfigurazioneEnteISNotificaDao dao = ConfigurazioneEnteISNotificaDAOFactory.getConfigurazioneEnteISNotifica(conn, getConfigurazioneEnteISNotificaDbSchema());
				//fine LP PG21XX04 Leak

				if (view.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0) {

					System.out.println("userCode - " + userCode);
					System.out.println("chiaveEnte - " + chiaveEnte);
					System.out.println("codiceImpostaServizio - " + codiceImpostaServizio);
					System.out.println("flagNotificaAllegato- " + view.getFlagNotificaAllegato());
					System.out.println("operatore - " + operatore);

					EsitoRisposte esitoIns = dao.insert(conf);
					if(esitoIns.getCodiceMessaggio().equals("OK"))
					{
						view.setMessage(Messages.INS_OK.format());
					}
					else
					{
						view.setMessage(Messages.INS_ERRD.format());
					}
				} else {
	
					System.out.println("mod userCode - " + userCode);
					System.out.println("mod chiaveEnte - " + chiaveEnte);
					System.out.println("mod codiceImpostaServizio - " + codiceImpostaServizio);
					System.out.println("mod flagNotificaAllegato- " + view.getFlagNotificaAllegato());
					System.out.println("mod operatore - " + operatore);
					
					Integer nAgg = dao.update(conf);
					if(nAgg == 1)
					{
						view.setMessage(Messages.UPDT_OK.format());
					}
					else
					{
						view.setMessage(Messages.UPDT_ERR.format());
					}
				}
			} catch (Exception e) {
				if (view.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0) 
					view.setMessage(Messages.INS_ERRD.format());
				else if (view.getTypeRequest().compareTo(TypeRequest.EDIT_SCOPE.scope()) == 0) 
					view.setMessage(Messages.UPDT_ERR.format());
				view.setError(true);
				System.out.println(e.getMessage());
			}
			//inizio LP PG21XX04 Leak
			finally {
				if(conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			//fine LP PG21XX04 Leak
		}
		else
		{
			setFormMessage("frmAction", esito, request);
			
			view.setCodiceUtente(userCode);
			if (view.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0)
			{			
				view.setBaseList(BaseListName.listaUtenti, false, true, request);
				view.setBaseList(BaseListName.listaEntiGenerici, false, true, request);
			}
			else
			{
				view.setBaseList(BaseListName.listaUtenti, false);
				view.setBaseList(BaseListName.listaEntiGenerici, false);
			}
		}
	}

	private String checkInputFields()
	{
		String esito = "";
		//Per il momento non ci sono controlli sulla sintassi dei valori inseriti 
		return esito;
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		view = view.populate(request, EnteImpostaServizioNotificaView.CancelScope);
		view.setActionName(EnteImpostaServizioNotificaView.ActionName);
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			System.out.println("cancel userCode - " + view.getUserCode());
			System.out.println("cancel chiaveEnte - " + view.getChiaveEnte());
			System.out.println("cancel codiceImpostaServizio - " + view.getCodiceImpostaServizio());

			ConfigurazioneEnteISNotifica conf = new ConfigurazioneEnteISNotifica();
			conf.setCodiceUtente(view.getUserCode());
			conf.setChiaveEnte(view.getChiaveEnte());
			conf.setCodiceImpostaServizio(view.getCodiceImpostaServizio());
			//inizio LP PG21XX04 Leak
			//ConfigurazioneEnteISNotificaDao dao = ConfigurazioneEnteISNotificaDAOFactory.getConfigurazioneEnteISNotifica(getConfigurazioneEnteISNotificaDataSource(), getConfigurazioneEnteISNotificaDbSchema());
			conn = getConfigurazioneEnteISNotificaDataSource().getConnection();
			ConfigurazioneEnteISNotificaDao dao = ConfigurazioneEnteISNotificaDAOFactory.getConfigurazioneEnteISNotifica(conn, getConfigurazioneEnteISNotificaDbSchema());
			//fine LP PG21XX04 Leak
			EsitoRisposte esitoCanc = dao.delete(conf);
			if(esitoCanc.getCodiceMessaggio().equals("OK"))
			{
				view.setMessage(Messages.CANC_OK.format());
			}
			else
			{
				view.setMessage(Messages.CANCEL_ERR.format());
			}
		} catch (Exception e) { view.setMessage(Messages.CANCEL_ERRDIP.format()); }
		//inizio LP PG21XX04 Leak
		finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		return null;
	}

	private ConfigurazioneEnteISNotificaPagelist getConfigurazioneEnteISNotificaPagelist(HttpServletRequest request) {
		ConfigurazioneEnteISNotificaDao configurazioneEnteISNotificaDAO;
		ConfigurazioneEnteISNotifica configurazioneEnteISNotifica = new ConfigurazioneEnteISNotifica();
		configurazioneEnteISNotifica.setCodiceUtente(cutecute);
		configurazioneEnteISNotifica.setChiaveEnte(chiaveEnte);
		configurazioneEnteISNotifica.setCodiceImpostaServizio(codiceImpostaServizio);
		configurazioneEnteISNotifica.setFlagNotificaAllegato(flagNotificaAllegato);
		ConfigurazioneEnteISNotificaPagelist configurazioneEnteISNotificaPageList = new ConfigurazioneEnteISNotificaPagelist();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneEnteISNotificaDAO = ConfigurazioneEnteISNotificaDAOFactory.getConfigurazioneEnteISNotifica(getConfigurazioneEnteISNotificaDataSource(), getConfigurazioneEnteISNotificaDbSchema());
			conn = getConfigurazioneEnteISNotificaDataSource().getConnection();
			configurazioneEnteISNotificaDAO = ConfigurazioneEnteISNotificaDAOFactory.getConfigurazioneEnteISNotifica(conn, getConfigurazioneEnteISNotificaDbSchema());
			//fine LP PG21XX04 Leak
			configurazioneEnteISNotificaPageList = configurazioneEnteISNotificaDAO.configurazioneEnteISNotificaList(configurazioneEnteISNotifica, rowsPerPage, pageNumber,"");
		} catch (DaoException e1) {
			e1.printStackTrace();
		//inizio LP PG21XX04 Leak
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		return configurazioneEnteISNotificaPageList;
	}	
}