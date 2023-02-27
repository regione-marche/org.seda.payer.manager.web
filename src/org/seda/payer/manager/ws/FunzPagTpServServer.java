package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.*;
import com.seda.payer.pgec.webservice.funzpagtpserv.source.*;


public class FunzPagTpServServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FunzPagTpServImplementationStub funzPagTpServCaller = null;

	private void setCodSocietaHeader(FunzPagTpServImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public FunzPagTpServServer(String endPoint) {
		FunzPagTpServServiceLocator lsService = new FunzPagTpServServiceLocator();
		lsService.setFunzPagTpServPortEndpointAddress(endPoint);
		try {
			funzPagTpServCaller = (FunzPagTpServImplementationStub)lsService.getFunzPagTpServPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public FunzPagTpServSearchResponse getFunzPagTpServs(FunzPagTpServSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(funzPagTpServCaller, request);
		return funzPagTpServCaller.getFunzPagTpServs(in);
	}
	public FunzPagTpServSearchAggregatoResponse getListaFunzPagTpServ_Aggregato(FunzPagTpServSearchAggregatoRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(funzPagTpServCaller, request);
		return funzPagTpServCaller.getListaFunzPagTpServ_Aggregato(in);
	}
    public FunzPagTpServDetailResponse getFunzPagTpServ(FunzPagTpServDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.getFunzPagTpServ(in);
    }
    public FunzPagTpServDetailAggregatoResponse getFunzPagTpServ_Aggregato(FunzPagTpServDetailAggregatoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.getFunzPagTpServ_Aggregato(in);
    }
    public StatusResponse save(FunzPagTpServSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.save(in);
    }
    public StatusResponse save_Aggregato(FunzPagTpServSaveAggregatoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.save_Aggregato(in);
    }
    public StatusResponse cancel(FunzPagTpServCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.cancel(in);
    }
    public StatusResponse cancel_Aggregato(FunzPagTpServCancelAggregatoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.cancel_Aggregato(in);
    }

}
