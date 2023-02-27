package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.*;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.source.*;


public class RangeAbiUtenteTipoServizioServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RangeAbiUtenteTipoServizioImplementationStub rangeAbiUtenteTipoServizioCaller = null;
	
	private void setCodSocietaHeader(RangeAbiUtenteTipoServizioImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}

	public RangeAbiUtenteTipoServizioServer(String endPoint) {
		RangeAbiUtenteTipoServizioServiceLocator lsService = new RangeAbiUtenteTipoServizioServiceLocator();
		lsService.setRangeAbiUtenteTipoServizioPortEndpointAddress(endPoint);
		try {
			rangeAbiUtenteTipoServizioCaller = (RangeAbiUtenteTipoServizioImplementationStub)lsService.getRangeAbiUtenteTipoServizioPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public RangeAbiUtenteTipoServizioSearchResponse getRangeAbiUtenteTipoServizios(RangeAbiUtenteTipoServizioSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(rangeAbiUtenteTipoServizioCaller, request);
		return rangeAbiUtenteTipoServizioCaller.getRangeAbiUtenteTipoServizios(in);
	}
    public RangeAbiUtenteTipoServizioDetailResponse getRangeAbiUtenteTipoServizio(RangeAbiUtenteTipoServizioDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(rangeAbiUtenteTipoServizioCaller, request);
    	return rangeAbiUtenteTipoServizioCaller.getRangeAbiUtenteTipoServizio(in);
    }
    public StatusResponse save(RangeAbiUtenteTipoServizioSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(rangeAbiUtenteTipoServizioCaller, request);
    	return rangeAbiUtenteTipoServizioCaller.save(in);
    }
    public StatusResponse cancel(RangeAbiUtenteTipoServizioCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(rangeAbiUtenteTipoServizioCaller, request);
    	return rangeAbiUtenteTipoServizioCaller.cancel(in);
    }
	
	

}
