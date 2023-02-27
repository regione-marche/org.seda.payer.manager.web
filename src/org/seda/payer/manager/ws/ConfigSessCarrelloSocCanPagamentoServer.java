package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.configsesscarrellosoccanpagamento.dati.*;
import com.seda.payer.pgec.webservice.configsesscarrellosoccanpagamento.source.*;


public class ConfigSessCarrelloSocCanPagamentoServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfigSessCarrelloSocCanPagamentoImplementationStub configSessCarrelloSocCanPagamentoCaller = null;
	
	private void setCodSocietaHeader(ConfigSessCarrelloSocCanPagamentoImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public ConfigSessCarrelloSocCanPagamentoServer(String endPoint) {
		ConfigSessCarrelloSocCanPagamentoServiceLocator lsService = new ConfigSessCarrelloSocCanPagamentoServiceLocator();
		lsService.setConfigSessCarrelloSocCanPagamentoPortEndpointAddress(endPoint);
		try {
			configSessCarrelloSocCanPagamentoCaller = (ConfigSessCarrelloSocCanPagamentoImplementationStub)lsService.getConfigSessCarrelloSocCanPagamentoPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public ConfigSessCarrelloSocCanPagamentoSearchResponse getConfigSessCarrelloSocCanPagamentos(ConfigSessCarrelloSocCanPagamentoSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(configSessCarrelloSocCanPagamentoCaller, request);
		return configSessCarrelloSocCanPagamentoCaller.getConfigSessCarrelloSocCanPagamentos(in);
	}
    public ConfigSessCarrelloSocCanPagamentoDetailResponse getConfigSessCarrelloSocCanPagamento(ConfigSessCarrelloSocCanPagamentoDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configSessCarrelloSocCanPagamentoCaller, request);
    	return configSessCarrelloSocCanPagamentoCaller.getConfigSessCarrelloSocCanPagamento(in);
    }
    public StatusResponse save(ConfigSessCarrelloSocCanPagamentoSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configSessCarrelloSocCanPagamentoCaller, request);
    	return configSessCarrelloSocCanPagamentoCaller.save(in);
    }
    public StatusResponse cancel(ConfigSessCarrelloSocCanPagamentoCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configSessCarrelloSocCanPagamentoCaller, request);
    	return configSessCarrelloSocCanPagamentoCaller.cancel(in);
    }
	
	

}
