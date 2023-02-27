package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.dati.*;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.source.*;


public class RangeAbiUtenteTipoServizioEnteServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RangeAbiUtenteTipoServizioEnteImplementationStub rangeAbiUtenteTipoServizioEnteCaller = null;
	
	private void setCodSocietaHeader(RangeAbiUtenteTipoServizioEnteImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}

	public RangeAbiUtenteTipoServizioEnteServer(String endPoint) {
		RangeAbiUtenteTipoServizioEnteServiceLocator lsService = new RangeAbiUtenteTipoServizioEnteServiceLocator();
		lsService.setRangeAbiUtenteTipoServizioEntePortEndpointAddress(endPoint);
		try {
			rangeAbiUtenteTipoServizioEnteCaller = (RangeAbiUtenteTipoServizioEnteImplementationStub)lsService.getRangeAbiUtenteTipoServizioEntePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public RangeAbiUtenteTipoServizioEnteSearchResponse getRangeAbiUtenteTipoServizioEntes(RangeAbiUtenteTipoServizioEnteSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(rangeAbiUtenteTipoServizioEnteCaller, request);
		return rangeAbiUtenteTipoServizioEnteCaller.getRangeAbiUtenteTipoServizioEntes(in);
	}
    public RangeAbiUtenteTipoServizioEnteDetailResponse getRangeAbiUtenteTipoServizioEnte(RangeAbiUtenteTipoServizioEnteDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(rangeAbiUtenteTipoServizioEnteCaller, request);
    	return rangeAbiUtenteTipoServizioEnteCaller.getRangeAbiUtenteTipoServizioEnte(in);
    }
    public StatusResponse save(RangeAbiUtenteTipoServizioEnteSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(rangeAbiUtenteTipoServizioEnteCaller, request);
    	return rangeAbiUtenteTipoServizioEnteCaller.save(in);
    }
    public StatusResponse cancel(RangeAbiUtenteTipoServizioEnteCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(rangeAbiUtenteTipoServizioEnteCaller, request);
    	return rangeAbiUtenteTipoServizioEnteCaller.cancel(in);
    }
	
	

}
