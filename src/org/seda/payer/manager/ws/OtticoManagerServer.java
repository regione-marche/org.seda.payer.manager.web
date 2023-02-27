package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.ottico.webservice.manager.dati.RicercaListaElabOtticoRequest;
import com.seda.payer.ottico.webservice.manager.dati.RicercaListaElabOtticoResponse;
import com.seda.payer.ottico.webservice.manager.source.ManagerSOAPBindingStub;
import com.seda.payer.ottico.webservice.manager.source.ManagerServiceLocator;
import com.seda.payer.ottico.webservice.manager.srv.FaultType;
/**
 * @author marco.montisano
 */
public class OtticoManagerServer extends BaseServer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ManagerSOAPBindingStub managerCaller = null;
	
	private void setCodSocietaHeader(ManagerSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	/**
	 * @param endPoint
	 */
	public OtticoManagerServer(String endPoint) {
		ManagerServiceLocator serviceLocator = new ManagerServiceLocator();
		serviceLocator.setManagerPortEndpointAddress(endPoint);
		try { managerCaller = (ManagerSOAPBindingStub) serviceLocator.getManagerPort();
		} catch (ServiceException e) { e.printStackTrace(); }
	}
	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.manager.source.ManagerInterface#ricercaListaElabOttico(com.seda.payer.ottico.webservice.manager.dati.RicercaListaElabOtticoRequest)
	 */
	public RicercaListaElabOtticoResponse ricercaListaElabOttico(
			RicercaListaElabOtticoRequest arg0, HttpServletRequest request) throws RemoteException,
			FaultType {
		setCodSocietaHeader(managerCaller, request);
		return managerCaller.ricercaListaElabOttico(arg0);
	}
}