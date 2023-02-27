package org.seda.payer.manager.riconciliazione.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.pgec.webservice.commons.dati.ScaricaMovimentoRequest;

public class ScaricaMovimentoAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		String fileName = request.getParameter("FileNameFlusso").concat(".bak");
		String fileNameDownload = request.getParameter("FileNameFlusso").concat(".bak");
		
		try {
			WSCache.logWriter.logDebug("download flusso quadrature in corso: utente='"+user.getUserName()+"'");
			String filename = WSCache.commonsServer.downloadFlussoQuadrature(new ScaricaMovimentoRequest(fileName), request).getFile();
			request.setAttribute("flussoCBI", filename);
			request.setAttribute("flussoCBID", fileNameDownload);
		} 
		catch (Exception e) {
			setErrorMessage(e.getMessage());
			WSCache.logWriter.logError("download flusso quadrature fallito: utente='"+user.getUserName()+"'", e);
		}
		return null;
	}

}
