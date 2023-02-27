package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioCancelRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioDetailRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioDetailResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSaveRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchRequest3;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.StatusResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizio.source.ConfigUtenteTipoServizioImplementationStub;
import com.seda.payer.pgec.webservice.configutentetiposervizio.source.ConfigUtenteTipoServizioServiceLocator;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class ConfigUtenteTipoServizioServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfigUtenteTipoServizioImplementationStub configUtenteTipoServizioCaller = null;

	private void setCodSocietaHeader(ConfigUtenteTipoServizioImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public ConfigUtenteTipoServizioServer(String endPoint) {
		ConfigUtenteTipoServizioServiceLocator lsService = new ConfigUtenteTipoServizioServiceLocator();
		lsService.setConfigUtenteTipoServizioPortEndpointAddress(endPoint);
		try {
			configUtenteTipoServizioCaller = (ConfigUtenteTipoServizioImplementationStub)lsService.getConfigUtenteTipoServizioPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public ConfigUtenteTipoServizioSearchResponse getConfigUtenteTipoServizios(ConfigUtenteTipoServizioSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(configUtenteTipoServizioCaller, request);
		return configUtenteTipoServizioCaller.getConfigUtenteTipoServizios(in);
	}
	public ConfigUtenteTipoServizioSearchResponse getConfigUtenteTipoServizios2(ConfigUtenteTipoServizioSearchRequest2 in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(configUtenteTipoServizioCaller, request);
		return configUtenteTipoServizioCaller.getConfigUtenteTipoServizios2(in);
	}
	public ConfigUtenteTipoServizioSearchResponse getConfigUtenteTipoServizios3(ConfigUtenteTipoServizioSearchRequest3 in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(configUtenteTipoServizioCaller, request);
		return configUtenteTipoServizioCaller.getConfigUtenteTipoServizios3(in);
	}
    public ConfigUtenteTipoServizioDetailResponse getConfigUtenteTipoServizio(ConfigUtenteTipoServizioDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configUtenteTipoServizioCaller, request);
    	return configUtenteTipoServizioCaller.getConfigUtenteTipoServizio(in);
    }
    public StatusResponse save(ConfigUtenteTipoServizioSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configUtenteTipoServizioCaller, request);
    	return configUtenteTipoServizioCaller.save(in);
    }
    public StatusResponse cancel(ConfigUtenteTipoServizioCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configUtenteTipoServizioCaller, request);
    	return configUtenteTipoServizioCaller.cancel(in);
    }
	
	

}
