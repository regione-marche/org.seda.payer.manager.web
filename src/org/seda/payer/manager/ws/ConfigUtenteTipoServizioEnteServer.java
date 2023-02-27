package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.*;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.source.*;


public class ConfigUtenteTipoServizioEnteServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfigUtenteTipoServizioEnteImplementationStub configUtenteTipoServizioEnteCaller = null;
	
	private void setCodSocietaHeader(ConfigUtenteTipoServizioEnteImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public ConfigUtenteTipoServizioEnteServer(String endPoint) {
		ConfigUtenteTipoServizioEnteServiceLocator lsService = new ConfigUtenteTipoServizioEnteServiceLocator();
		lsService.setConfigUtenteTipoServizioEntePortEndpointAddress(endPoint);
		try {
			configUtenteTipoServizioEnteCaller = (ConfigUtenteTipoServizioEnteImplementationStub)lsService.getConfigUtenteTipoServizioEntePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEntes(ConfigUtenteTipoServizioEnteSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(configUtenteTipoServizioEnteCaller, request);
		return configUtenteTipoServizioEnteCaller.getConfigUtenteTipoServizioEntes(in);
	}
	
	public ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEntes2(ConfigUtenteTipoServizioEnteSearchRequest2 in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(configUtenteTipoServizioEnteCaller, request);
		return configUtenteTipoServizioEnteCaller.getConfigUtenteTipoServizioEntes2(in);
	}
	public ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEntes3(ConfigUtenteTipoServizioEnteSearchRequest3 in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(configUtenteTipoServizioEnteCaller, request);
		return configUtenteTipoServizioEnteCaller.getConfigUtenteTipoServizioEntes3(in);
	}
    public ConfigUtenteTipoServizioEnteDetailResponse getConfigUtenteTipoServizioEnte(ConfigUtenteTipoServizioEnteDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configUtenteTipoServizioEnteCaller, request);
    	return configUtenteTipoServizioEnteCaller.getConfigUtenteTipoServizioEnte(in);
    }
    public StatusResponse save(ConfigUtenteTipoServizioEnteSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configUtenteTipoServizioEnteCaller, request);
    	return configUtenteTipoServizioEnteCaller.save(in);
    }
    public StatusResponse cancel(ConfigUtenteTipoServizioEnteCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configUtenteTipoServizioEnteCaller, request);
    	return configUtenteTipoServizioEnteCaller.cancel(in);
    }
    public DownloadServiziAttiviCsvResponse getServiziAttiviDownloadCsv(DownloadServiziAttiviCsvRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configUtenteTipoServizioEnteCaller, request);
    	return configUtenteTipoServizioEnteCaller.downloadServiziAttiviCsv(in);
    }
    
	

}
