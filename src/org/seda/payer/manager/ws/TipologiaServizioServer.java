package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.tipologiaservizio.dati.*;
import com.seda.payer.pgec.webservice.tipologiaservizio.source.*;


public class TipologiaServizioServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TipologiaServizioImplementationStub tipologiaServizioCaller = null;
	
	private void setCodSocietaHeader(TipologiaServizioImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	

	public TipologiaServizioServer(String endPoint) {
		TipologiaServizioServiceLocator lsService = new TipologiaServizioServiceLocator();
		lsService.setTipologiaServizioPortEndpointAddress(endPoint);
		try {
			tipologiaServizioCaller = (TipologiaServizioImplementationStub)lsService.getTipologiaServizioPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public TipologiaServizioSearchResponse getTipologiaServizios(TipologiaServizioSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(tipologiaServizioCaller, request);
		return tipologiaServizioCaller.getTipologiaServizios(in);
	}
    public TipologiaServizioDetailResponse getTipologiaServizio(TipologiaServizioDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(tipologiaServizioCaller, request);
    	return tipologiaServizioCaller.getTipologiaServizio(in);
    }
    public StatusResponse save(TipologiaServizioSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(tipologiaServizioCaller, request);
    	return tipologiaServizioCaller.save(in);
    }
    public StatusResponse cancel(TipologiaServizioCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(tipologiaServizioCaller, request);
    	return tipologiaServizioCaller.cancel(in);
    }
	
	

}
