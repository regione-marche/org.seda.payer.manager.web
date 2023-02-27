package org.seda.payer.manager.configurazione.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.UfficiPageList;
import com.seda.payer.core.bean.Ufficio;
import com.seda.payer.core.dao.UfficiDAOFactory;
import com.seda.payer.core.dao.UfficiDao;

public class GestioneUfficiAction extends BaseManagerAction {

	private DataSource ufficiDataSource = null;
	private String ufficiDbSchema = "";
	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		super.service(request);
		rowsPerPage = (request.getParameter("rowsPerPage") == null ) ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		pageNumber = (request.getParameter("pageNumber") == null ) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		replyAttributes(false, request,"pageNumber","rowsPerPage","order","action");
		
		HttpSession session = request.getSession();
		
		FiredButton firedButton = getFiredButton(request);
		
		//Inizializzazione del connettore al database per il recupero e inserimento degli uffici
		initDataBaseConnection(request, session);
		
		switch(firedButton) {
			case TX_BUTTON_CERCA: {
				salvaFiltriDiRicerca(request, session);
				list(request, session);
			}; break;
			case TX_BUTTON_ADD: {
				initUfficio(request);
			}; break;
			case TX_BUTTON_AGGIUNGI: {
				insert(request, session);
			}; break;
			case TX_BUTTON_EDIT: {
				edit(request, session);
			}; break;
			case TX_BUTTON_DELETE: {
				delete(request, session);
			}; break;
			case TX_BUTTON_RESET: {
				resetFiltriDiRicerca(request, session);
			}; break;
			case TX_BUTTON_INDIETRO: {
				reImpostaFiltriDiRicerca(request, session);
				list(request, session);
			}; break;
			case TX_BUTTON_NULL: {
				System.out.println("codop : " + (String)request.getAttribute("codop"));
				System.out.println("bottone nullo");
				String codop = "";
				if(request.getAttribute("codop")!=null) {
					codop = (String)request.getAttribute("codop");
				}
				if(codop.trim().equals("edit")) {
					select(request, session);					
				}
			}; break;
		}
		
		mantieniFiltriDiRicerca(request);
		
		return null;
	}
	
	protected int getDefaultListRows() {
		int defaultListRows = 4;
		PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		if(configuration != null)
		{
			String s_defaultListRows = configuration.getProperty(PropertiesPath.defaultListRows.format());
			if (s_defaultListRows != null) defaultListRows = Integer.parseInt(s_defaultListRows);
		}
		return defaultListRows;
	}
	
	public Object list(HttpServletRequest request, HttpSession session) throws ActionException {
		try {
			Ufficio ufficio = new Ufficio();
			if(request.getAttribute("tx_codufficio")!=null && 
					!request.getAttribute("tx_codufficio").equals("")) {
				ufficio.setCodiceUfficio((String)request.getAttribute("tx_codufficio"));
			}
			ufficio.setDescrizioneIT((String)request.getAttribute("tx_descrit"));
			ufficio.setDescrizioneDE((String)request.getAttribute("tx_descrde"));
			UfficiPageList listUffici = getGestioneUfficiList(ufficio);
			PageInfo pageInfo = listUffici.getPageInfo();
			if (listUffici.getRetCode()!="00") {
				setFormMessage("form_uffici", "Errore generico - Impossibile recuperare i dati", request);
			} else {
				if(pageInfo != null) {
					if(pageInfo.getNumRows() > 0) {
						request.setAttribute("lista_uffici", listUffici.getUfficiListXml());
						request.setAttribute("lista_uffici.pageInfo", pageInfo);
					} else {
						request.setAttribute("lista_uffici", null);
						setFormMessage("form_uffici", Messages.NO_DATA_FOUND.format(), request);
					}
				} else { 
					setFormMessage("form_uffici", "Errore generico - Impossibile recuperare i dati", request);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object delete(HttpServletRequest request, HttpSession session) throws ActionException {
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			if(request.getAttribute("idufficio")!=null) {
				String idUfficio = (String)request.getAttribute("idufficio");
				Ufficio ufficio = new Ufficio();
				ufficio.setIdUfficio(idUfficio);
				//inizio LP PG21XX04 Leak
				//UfficiDao ufficiDao = UfficiDAOFactory.getUffici(ufficiDataSource, ufficiDbSchema);
				conn = ufficiDataSource.getConnection();
				UfficiDao ufficiDao = UfficiDAOFactory.getUffici(conn, ufficiDbSchema);
				//fine LP PG21XX04 Leak
				
				int eliminato = ufficiDao.delete(ufficio);
				if(eliminato==1) {
					setFormMessage("form_uffici", "Eliminazione record riuscita", request);
				} else {
					setFormMessage("form_uffici", "Errore - Eliminazione fallita", request);
				}
			} else {
				setFormMessage("form_uffici", "Errore - ID Ufficio mancante", request);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			if (conn != null) {
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
	
	private UfficiPageList getGestioneUfficiList(Ufficio ufficio) {
		UfficiPageList ufficiPageList = new UfficiPageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//UfficiDao ufficiDao = UfficiDAOFactory.getUffici(ufficiDataSource, ufficiDbSchema);
			conn = ufficiDataSource.getConnection();
			UfficiDao ufficiDao = UfficiDAOFactory.getUffici(conn, ufficiDbSchema);
			//fine LP PG21XX04 Leak
			ufficiPageList = ufficiDao.ufficiList(ufficio, rowsPerPage, pageNumber,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		return ufficiPageList;
	}

	protected void initDataBaseConnection(HttpServletRequest request, HttpSession session) {
		String dbSchemaCodSocieta="", dataSource="";
		PropertiesTree configuration = null; 
		if(ufficiDataSource == null || ufficiDbSchema == null) {
			try {	
				dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
				configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
				dataSource = configuration.getProperty(PropertiesPath.dataSourceUffici.format(dbSchemaCodSocieta));
				ufficiDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSource));
				ufficiDbSchema = configuration.getProperty(PropertiesPath.dataSourceSchemaUffici.format(dbSchemaCodSocieta));
				System.out.println("initDataBaseConnection - ufficiDataSource e ufficiDbSchema acquisiti");
			} catch (ServiceLocatorException e) {
				System.out.println("ServiceLocator error " + e.getMessage());
			} catch (Exception e) {
				System.out.println("Error " + e.getMessage());
			}
		} else {
			System.out.println("initDataBaseConnection - ufficiDataSource e ufficiDbSchema gia' impostati");
		}
	}
	
	private Object insert(HttpServletRequest request, HttpSession session) {
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {	
			if(request.getAttribute("idufficio")!=null) {
				Ufficio ufficio = new Ufficio();
				ufficio.setIdUfficio((String)request.getAttribute("idufficio"));
				ufficio.setCodiceUfficio((String)request.getAttribute("codufficio"));
				ufficio.setDescrizioneIT((String)request.getAttribute("descrit"));
				ufficio.setDescrizioneDE((String)request.getAttribute("descrde"));
				//inizio LP PG21XX04 Leak
				//UfficiDao ufficiDao = UfficiDAOFactory.getUffici(ufficiDataSource, ufficiDbSchema);
				conn = ufficiDataSource.getConnection();
				UfficiDao ufficiDao = UfficiDAOFactory.getUffici(conn, ufficiDbSchema);
				//fine LP PG21XX04 Leak
				int inserito = ufficiDao.insert(ufficio);
				switch(inserito) {
					case 1: 
						setFormMessage("form_uffici", "Inserimento record riuscito", request); 
						break;
					case -803:
						setFormMessage("form_uffici", "Errore - Inserimento fallito. Chiave gia' presente", request);
						break;
					default:
						setFormMessage("form_uffici", "Errore - Inserimento fallito", request);
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			if (conn != null) {
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
	
	private Object edit(HttpServletRequest request, HttpSession session) {
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {				
			if(request.getAttribute("idufficio")!=null) {
				Ufficio ufficio = new Ufficio();
				ufficio.setIdUfficio((String)request.getAttribute("idufficio"));
				ufficio.setCodiceUfficio((String)request.getAttribute("codufficio"));
				ufficio.setDescrizioneIT((String)request.getAttribute("descrit"));
				ufficio.setDescrizioneDE((String)request.getAttribute("descrde"));
				//inizio LP PG21XX04 Leak
				//UfficiDao ufficiDao = UfficiDAOFactory.getUffici(ufficiDataSource, ufficiDbSchema);
				conn = ufficiDataSource.getConnection();
				UfficiDao ufficiDao = UfficiDAOFactory.getUffici(conn, ufficiDbSchema);
				//fine LP PG21XX04 Leak
				int aggiornato = ufficiDao.update(ufficio);
				if(aggiornato==1) {
					setFormMessage("form_uffici", "Aggiornamento record riuscito", request);
				} else {
					setFormMessage("form_uffici", "Errore - Aggiornamento fallito", request);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			if (conn != null) {
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
	
	private Object select (HttpServletRequest request, HttpSession session) {
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			String idUfficio =(String)request.getAttribute("idufficio");
			Ufficio ufficio = new Ufficio();
			ufficio.setIdUfficio(idUfficio);
			//inizio LP PG21XX04 Leak
			//UfficiDao ufficiDao = UfficiDAOFactory.getUffici(ufficiDataSource, ufficiDbSchema);
			conn = ufficiDataSource.getConnection();
			UfficiDao ufficiDao = UfficiDAOFactory.getUffici(conn, ufficiDbSchema);
			//fine LP PG21XX04 Leak
			ufficio = ufficiDao.select(ufficio);
			request.setAttribute("idufficio", ufficio.getIdUfficio());
			request.setAttribute("codufficio", ufficio.getCodiceUfficio());
			request.setAttribute("descrit", ufficio.getDescrizioneIT());
			request.setAttribute("descrde", ufficio.getDescrizioneDE());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			if (conn != null) {
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
	
	private void initUfficio (HttpServletRequest request) {
		request.setAttribute("idufficio", "");
		request.setAttribute("codufficio", "");
		request.setAttribute("descrit", "");
		request.setAttribute("descrde", "");
	}
	
	private void reImpostaFiltriDiRicerca(HttpServletRequest request, HttpSession session) {
		request.setAttribute("tx_codufficio", session.getAttribute("tx_codufficio"));
		request.setAttribute("tx_descrit", session.getAttribute("tx_descrit"));
		request.setAttribute("tx_descrde", session.getAttribute("tx_descrde"));
	}
	
	private void salvaFiltriDiRicerca(HttpServletRequest request, HttpSession session) {
		session.setAttribute("tx_codufficio", request.getAttribute("tx_codufficio"));
		session.setAttribute("tx_descrit", request.getAttribute("tx_descrit"));
		session.setAttribute("tx_descrde", request.getAttribute("tx_descrde"));
	}
	
	private void mantieniFiltriDiRicerca(HttpServletRequest request) {
		request.setAttribute("tx_codufficio", request.getAttribute("tx_codufficio")!=null?request.getAttribute("tx_codufficio"):"");
		request.setAttribute("tx_descrit", request.getAttribute("tx_descrit")!=null?request.getAttribute("tx_descrit"):"");
		request.setAttribute("tx_descrde",  request.getAttribute("tx_descrde")!=null?request.getAttribute("tx_descrde"):"");
	}
	
	private void resetFiltriDiRicerca(HttpServletRequest request, HttpSession session) {
		request.setAttribute("tx_codufficio", "");
		request.setAttribute("tx_descrit", "");
		request.setAttribute("tx_descrde", "");
		session.setAttribute("tx_codufficio", "");
		session.setAttribute("tx_descrit", "");
		session.setAttribute("tx_descrde", "");
	}
	
}

