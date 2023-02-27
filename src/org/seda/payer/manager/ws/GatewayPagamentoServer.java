package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.*;
import com.seda.payer.pgec.webservice.gatewaypagamento.source.*;


public class GatewayPagamentoServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GatewayPagamentoImplementationStub gatewayPagamentoCaller = null;
	
	private void setCodSocietaHeader(GatewayPagamentoImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public GatewayPagamentoServer(String endPoint) {
		GatewayPagamentoServiceLocator lsService = new GatewayPagamentoServiceLocator();
		lsService.setGatewayPagamentoPortEndpointAddress(endPoint);
		try {
			gatewayPagamentoCaller = (GatewayPagamentoImplementationStub)lsService.getGatewayPagamentoPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public GatewayPagamentoSearchResponse getGatewayPagamentos(GatewayPagamentoSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(gatewayPagamentoCaller, request);
		return gatewayPagamentoCaller.getGatewayPagamentos(in);
	}
    public GatewayPagamentoDetailResponse getGatewayPagamento(GatewayPagamentoDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(gatewayPagamentoCaller, request);
    	return gatewayPagamentoCaller.getGatewayPagamento(in);
    }
    public StatusResponse save(GatewayPagamentoSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(gatewayPagamentoCaller, request);
    	return gatewayPagamentoCaller.save(in);
    }
    public StatusResponse cancel(GatewayPagamentoCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(gatewayPagamentoCaller, request);
    	return gatewayPagamentoCaller.cancel(in);
    }
	
	

}
