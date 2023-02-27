package org.seda.payer.manager.blackboxconfig.actions;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.Messages;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.BlackBoxPosLog;
import com.seda.payer.core.bean.BlackBoxPosLogPagelist;
import com.seda.payer.core.bean.BlackBoxPosPagelist;
import com.seda.payer.core.bean.ConfigurazioneBlackBoxPos;
import com.seda.payer.core.dao.BlackBoxDAOFactory;
import com.seda.payer.core.dao.ConfigurazioneBlackBoxDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.EsitoRisposte;

public class BlackBoxPosLogAction extends BlackBoxBaseManagerAction{

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber; 
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		super.service(request);
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
			setProfile(request);
		
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		
		switch(firedButton) {
		
			case TX_BUTTON_CERCA: {
				mantieniFiltriRicerca(request);
				list(request);
				recuperaFiltriRicerca(request);
			}; break;
				
			case TX_BUTTON_INDIETRO: {
				request.setAttribute("codOp", "");
				request.setAttribute("idLog", "");
				recuperaFiltriRicerca(request);
				list(request);
			}; break;
				
			case TX_BUTTON_RESET: {
				resetParametri(request);
				setProfile(request);
				resetFiltriRicerca(request);
				list(request);
			}; break;
				
			case TX_BUTTON_NULL: {
				String codOp = request.getParameter("codOp")!=null?((String)request.getParameter("codOp")).trim():"";
				//passaggio dei dati hidden per Edit e Delete
				request.setAttribute("codOp", codOp);
				request.setAttribute("idLog", request.getAttribute("idLog")!=null?request.getAttribute("idLog"):"");
			}; break;
			
			case TX_BUTTON_DELETE : {
				delete(request);
				recuperaFiltriRicerca(request);
				list(request);
			}; break;
			
		}
		return null;
	}
	
	private void list(HttpServletRequest request){
		try {	
			ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO;
			BlackBoxPosLog configurazioneBlackBoxPosLog = new BlackBoxPosLog();
			configurazioneBlackBoxPosLog.setCodiceIdentificativoDominio(request.getAttribute("tx_idDominio")!=null?((String)request.getAttribute("tx_idDominio")).trim():"");
			configurazioneBlackBoxPosLog.setCodiceEnte(request.getAttribute("tx_idEnte")!=null?((String)request.getAttribute("tx_idEnte")).trim():"");
			configurazioneBlackBoxPosLog.setNumeroAvviso(request.getAttribute("tx_numeroAvviso")!=null?((String)request.getAttribute("tx_numeroAvviso")).trim():"");
			
			BlackBoxPosLogPagelist blackBoxPosLogPagelist = new BlackBoxPosLogPagelist();
			//inizio LP PG21XX04 Leak
			Connection connection = null;
			//fine LP PG21XX04 Leak
			try {
				//inizio LP PG21XX04 Leak
				//configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
				connection = getBlackBoxDataSource().getConnection();
				configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
				//fine LP PG21XX04 Leak
				blackBoxPosLogPagelist = configurazioneBlackBoxDAO.blackboxposlogList(configurazioneBlackBoxPosLog, rowsPerPage, pageNumber,"");
			} catch (DaoException e1) {
				e1.printStackTrace();
			//inizio LP PG21XX04 Leak
			//}
			} catch (SQLException e) {
				e.printStackTrace();
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
			PageInfo pageInfo = blackBoxPosLogPagelist.getPageInfo();
			if (blackBoxPosLogPagelist.getRetCode()!="00") {
				setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			} else {
				if(pageInfo != null) {
					if(pageInfo.getNumRows() > 0) {
						request.setAttribute("lista_blackboxlog", blackBoxPosLogPagelist.getblackBoxPosLogPagelist());
						request.setAttribute("lista_blackboxlog.pageInfo", pageInfo);
					} else {
						request.setAttribute("lista_blackboxlog", null);
						
						setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
					}
				} else { 
					setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
				}
			}
		} catch(Exception e) {
			setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void delete(HttpServletRequest request){
		ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO;
		BlackBoxPosLog configurazioneBlackBoxPos = new BlackBoxPosLog();
		configurazioneBlackBoxPos.setIdLog(new Integer(request.getParameter("idLog").trim()));
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
			connection = getBlackBoxDataSource().getConnection();
			configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
			//fine LP PG21XX04 Leak
			EsitoRisposte esito = configurazioneBlackBoxDAO.delete(configurazioneBlackBoxPos);
			
			if(esito!=null) {
				setFormMessage("form_selezione", esito.getDescrizioneMessaggio(), request);
			} else {
				setFormMessage("form_selezione", "Errore nell'eliminazione del log", request);
			}
			
		} catch (DaoException e1) {
			e1.printStackTrace();
		//inizio LP PG21XX04 Leak
		//}
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	private void mantieniFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(request.getAttribute("tx_idDominio")!=null)
			session.setAttribute("tx_idDominio", request.getAttribute("tx_idDominio"));
		if(request.getAttribute("tx_idEnte")!=null)
			session.setAttribute("tx_idEnte", request.getAttribute("tx_idEnte"));
		if(request.getAttribute("tx_numeroAvviso")!=null)
			session.setAttribute("tx_numeroAvviso", request.getAttribute("tx_numeroAvviso"));
	}
	
	private void recuperaFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		request.setAttribute("tx_idDominio", session.getAttribute("tx_idDominio")!=null?session.getAttribute("tx_idDominio"):"");
		request.setAttribute("tx_idEnte", session.getAttribute("tx_idEnte")!=null?session.getAttribute("tx_idEnte"):"");
		request.setAttribute("tx_numeroAvviso", session.getAttribute("tx_numeroAvviso")!=null?session.getAttribute("tx_numeroAvviso"):"");
	}
	
	private void resetFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		request.setAttribute("tx_idDominio", "");
		session.setAttribute("tx_idDominio", "");
		request.setAttribute("tx_idEnte", "");
		session.setAttribute("tx_idEnte", "");
		request.setAttribute("tx_numeroAvviso", "");
		session.setAttribute("tx_numeroAvviso", "");
	}
	
}

