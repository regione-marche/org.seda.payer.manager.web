package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.integraente.webservice.dati.InviaFlussoRendRequestType;
import com.seda.payer.integraente.webservice.dati.InviaFlussoRendResponseType;
import com.seda.payer.integraente.webservice.source.RendicontaEnteSOAPBinding;
import com.seda.payer.integraente.webservice.source.RendicontaEnteServicePort;
import com.seda.payer.pgec.webservice.srv.FaultType;

public class RendicontaEnteServer extends BaseServer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RendicontaEnteSOAPBinding rendicontaEnteCaller;
	
	private void setCodSocietaHeader(RendicontaEnteSOAPBinding stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public RendicontaEnteServer(String endPoint)
	{
		RendicontaEnteServicePort lsService = new RendicontaEnteServicePort();
		lsService.setRendicontaEntePortEndpointAddress(endPoint);
		try {
			rendicontaEnteCaller = (RendicontaEnteSOAPBinding)lsService.getRendicontaEntePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public InviaFlussoRendResponseType inviaFlussoRend(InviaFlussoRendRequestType in, HttpServletRequest request)throws RemoteException, FaultType
	{	setCodSocietaHeader(rendicontaEnteCaller, request);
		return rendicontaEnteCaller.inviaFlussoRend(in);
	}


}
