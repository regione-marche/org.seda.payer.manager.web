package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.canalepagamento.dati.*;
import com.seda.payer.pgec.webservice.canalepagamento.source.*;


public class CanPagamentoServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CanalePagamentoImplementationStub canPagamentoCaller = null;
	
	private void setCodSocietaHeader (CanalePagamentoImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public CanPagamentoServer(String endPoint) {
		CanalePagamentoServiceLocator lsService = new CanalePagamentoServiceLocator();
		lsService.setCanalePagamentoPortEndpointAddress(endPoint);
		try {
			canPagamentoCaller = (CanalePagamentoImplementationStub)lsService.getCanalePagamentoPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public CanalePagamentoSearchResponse getCanalePagamentos(CanalePagamentoSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(canPagamentoCaller, request);
		return canPagamentoCaller.getCanalePagamentos(in);
	}
    public CanalePagamentoDetailResponse getCanalePagamento(CanalePagamentoDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(canPagamentoCaller, request);
    	return canPagamentoCaller.getCanalePagamento(in);
    }
    public StatusResponse save(CanalePagamentoSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(canPagamentoCaller, request);
    	return canPagamentoCaller.save(in);
    }
    public StatusResponse cancel(CanalePagamentoCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(canPagamentoCaller, request);
    	return canPagamentoCaller.cancel(in);
    }
	
	

}
