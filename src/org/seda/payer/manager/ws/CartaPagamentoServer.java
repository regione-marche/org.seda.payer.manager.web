package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.cartapagamento.dati.*;
import com.seda.payer.pgec.webservice.cartapagamento.source.*;


public class CartaPagamentoServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CartaPagamentoImplementationStub cartaPagamentoCaller = null;
	
	private void setCodSocietaHeader (CartaPagamentoImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public CartaPagamentoServer(String endPoint) {
		CartaPagamentoServiceLocator lsService = new CartaPagamentoServiceLocator();
		lsService.setCartaPagamentoPortEndpointAddress(endPoint);
		try {
			cartaPagamentoCaller = (CartaPagamentoImplementationStub)lsService.getCartaPagamentoPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public CartaPagamentoSearchResponse getCartaPagamentos(CartaPagamentoSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(cartaPagamentoCaller, request);
		return cartaPagamentoCaller.getCartaPagamentos(in);
	}
    public CartaPagamentoDetailResponse getCartaPagamento(CartaPagamentoDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(cartaPagamentoCaller, request);
    	return cartaPagamentoCaller.getCartaPagamento(in);
    }
    public StatusResponse save(CartaPagamentoSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(cartaPagamentoCaller, request);
    	return cartaPagamentoCaller.save(in);
    }
    public StatusResponse cancel(CartaPagamentoCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(cartaPagamentoCaller, request);
    	return cartaPagamentoCaller.cancel(in);
    }
	
	

}
