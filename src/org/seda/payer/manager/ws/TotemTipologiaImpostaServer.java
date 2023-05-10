package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.TotemTipologiaImposta.source.*;
import com.seda.payer.pgec.webservice.TotemTipologiaImposta.dati.*;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class TotemTipologiaImpostaServer extends BaseServer {
	
	private static final long serialVersionUID = 1L;
	private TotemTipologiaImpostaImplementationStub totemTipologiaImpostaCaller = null;

	public TotemTipologiaImpostaServer(String endPoint) {
		TotemTipologiaImpostaServiceLocator lsService = new TotemTipologiaImpostaServiceLocator();
		lsService.setTotemTipologiaImpostaPortEndpointAddress(endPoint);
		try {
			totemTipologiaImpostaCaller = (TotemTipologiaImpostaImplementationStub)lsService.getTotemTipologiaImpostaPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	private void setCodSocietaHeader (TotemTipologiaImpostaImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public TotemTipologiaImpostaSearchResponse getTotemTipologiaImpostaList(TotemTipologiaImpostaSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType{	
		setCodSocietaHeader(totemTipologiaImpostaCaller, request);
		return totemTipologiaImpostaCaller.getTotemTipologiaImpostaList(in);
	}
	
    public TotemTipologiaImpostaDetailResponse getTotemTipologiaImpostaSelect(TotemTipologiaImpostaDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType{	
    	setCodSocietaHeader(totemTipologiaImpostaCaller, request);
    	return totemTipologiaImpostaCaller.getTotemTipologiaImpostaSelect(in);
    } 
    
    public StatusResponse save(TotemTipologiaImpostaSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType{	
    	setCodSocietaHeader(totemTipologiaImpostaCaller, request);
    	return totemTipologiaImpostaCaller.save(in);
    }
    
    public StatusResponse cancel(TotemTipologiaImpostaCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType{	
    	setCodSocietaHeader(totemTipologiaImpostaCaller, request);
    	return totemTipologiaImpostaCaller.cancel(in);
    }
	
	
}
