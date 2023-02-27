package org.seda.payer.manager.riconciliazionemt.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAO;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAOFactory;

public class RegolarizzaSospesoAction extends BaseRiconciliazioneTesoreriaAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		super.service(request);
		
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		String username = user.getUserName();
		
		String nota = (String)request.getAttribute("nota");
		
		String idMdc = (String)request.getAttribute("idmdc");
		long idMovimentoDiCassa;
		if (idMdc != null && idMdc != "") {
			idMovimentoDiCassa = Long.parseLong(idMdc);
			
			
		} else {
			idMovimentoDiCassa = 0;
		}
	
		switch(getFiredButton(request)) {
		case TX_BUTTON_CERCA: 
			if (nota != null && !nota.equals("")) {
				GiornaleDiCassaDAO giornaleDiCassaDAO;
				//inizio LP PG21XX04 Leak
				Connection conn = null;
				//fine LP PG21XX04 Leak
				try {
					//inizio LP PG21XX04 Leak
					//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
					conn = getGdcDataSource().getConnection();
					giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
					//fine LP PG21XX04 Leak
					giornaleDiCassaDAO.RegolarizzaSospeso(idMovimentoDiCassa, username, nota);
					request.setAttribute("regolarizzato", "Y");
					setFormMessage("regolarizzaSospesoForm", "Sospeso regolarizzato con successo!", request);
				} catch (DaoException e) {
					e.printStackTrace();
					setFormMessage("regolarizzaSospesoForm", "Il sospeso non &egrave; stato regolarizzato", request);
				}
				//inizio LP PG21XX04 Leak
				catch (SQLException e1) {
					e1.printStackTrace();
					setFormMessage("regolarizzaSospesoForm", "Il sospeso non &egrave; stato regolarizzato", request);
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
			} else {
				setFormMessage("regolarizzaSospesoForm", "Il campo Nota è obbligatorio", request);
			}
		}
		return null;
	}
	
}

