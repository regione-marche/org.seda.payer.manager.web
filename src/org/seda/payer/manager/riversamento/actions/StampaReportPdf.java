/**
 * 
 */
package org.seda.payer.manager.riversamento.actions;
/*
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import org.seda.payer.manager.ws.WSCache;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.riversamento.webservice.dati.ElaboraNuoveStampeRequest;
import com.seda.payer.riversamento.webservice.dati.ElaboraNuoveStampeResponse;
import com.seda.payer.riversamento.webservice.srv.FaultType;

/**
 * @author rcarnicelli
 *
 */
/*
public class StampaReportPdf extends BaseRiversamentoAction {

	private static final long serialVersionUID = 1L;
	public StampaReportPdf() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object service(HttpServletRequest request) throws ActionException {
		// TODO Auto-generated method stub
		super.service(request);
//		setProfile(request);	

		ElaboraNuoveStampeRequest elaboraNuoveStampeRequest = new ElaboraNuoveStampeRequest();
		ElaboraNuoveStampeResponse elaboraNuoveStampeResponse;
		try {
			elaboraNuoveStampeResponse = WSCache.riversamentoServer.elaboraNuoveStampe(elaboraNuoveStampeRequest, request);
			request.setAttribute("retCode",elaboraNuoveStampeResponse.getRisultato().getRetCode());
			request.setAttribute("retMessage", elaboraNuoveStampeResponse.getRisultato().getRetMessage());
		} catch (FaultType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
*/