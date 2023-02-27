package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.funzpagtpservente.dati.*;
import com.seda.payer.pgec.webservice.funzpagtpservente.source.*;


public class FunzPagTpServEnteServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FunzPagTpServEnteImplementationStub funzPagTpServCaller = null;

	private void setCodSocietaHeader(FunzPagTpServEnteImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public FunzPagTpServEnteServer(String endPoint) {
		FunzPagTpServEnteServiceLocator lsService = new FunzPagTpServEnteServiceLocator();
		lsService.setFunzPagTpServEntePortEndpointAddress(endPoint);
		try {
			funzPagTpServCaller = (FunzPagTpServEnteImplementationStub)lsService.getFunzPagTpServEntePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public FunzPagTpServEnteSearchResponse getFunzPagTpServs(FunzPagTpServEnteSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(funzPagTpServCaller, request);
		return funzPagTpServCaller.getFunzPagTpServEntes(in);
	}
	public FunzPagTpServEnteSearchAggregatoResponse getListaFunzPagTpServ_Aggregato(FunzPagTpServEnteSearchAggregatoRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(funzPagTpServCaller, request);
		return funzPagTpServCaller.getListFunzPagTpServEnte_Aggregato(in);
	}
    public FunzPagTpServEnteDetailResponse getFunzPagTpServ(FunzPagTpServEnteDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.getFunzPagTpServEnte(in);
    }
    public FunzPagTpServEnteDetailAggregatoResponse getFunzPagTpServ_Aggregato(FunzPagTpServEnteDetailAggregatoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.getFunzPagTpServEnte_Aggregato(in);
    }
    public StatusResponse save(FunzPagTpServEnteSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.save(in);
    }
    public StatusResponse save_Aggregato(FunzPagTpServEnteSaveAggregatoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.save_Aggregato(in);
    }
    public StatusResponse cancel(FunzPagTpServEnteCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.cancel(in);
    }
    public StatusResponse cancel_Aggregato(FunzPagTpServEnteCancelAggregatoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(funzPagTpServCaller, request);
    	return funzPagTpServCaller.cancel_Aggregato(in);
    }
	
	

}
