package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiCancelRequest;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiDetailRequest;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiDetailResponse;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiSaveRequest;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiSearchRequest;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiSearchResponse;
import com.seda.payer.pgec.webservice.configbancadati.dati.StatusResponse;
import com.seda.payer.pgec.webservice.configbancadati.source.ConfigBancaDatiImplementationStub;
import com.seda.payer.pgec.webservice.configbancadati.source.ConfigBancaDatiServiceLocator;


public class ConfigBancaDatiServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfigBancaDatiImplementationStub configBancaDatiCaller = null;
	
	private void setCodSocietaHeader(ConfigBancaDatiImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public ConfigBancaDatiServer(String endPoint) {
		ConfigBancaDatiServiceLocator lsService = new ConfigBancaDatiServiceLocator();
		
		lsService.setConfigBancaDatiPortEndpointAddress(endPoint);
		try {
			configBancaDatiCaller = (ConfigBancaDatiImplementationStub)lsService.getConfigBancaDatiPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
    public ConfigBancaDatiSearchResponse searchConfigBancaDati(ConfigBancaDatiSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
    	{	setCodSocietaHeader(configBancaDatiCaller, request);
    		return configBancaDatiCaller.searchConfigBancaDati(in);
    	}
        public ConfigBancaDatiDetailResponse getConfigBancaDati(ConfigBancaDatiDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
        {	setCodSocietaHeader(configBancaDatiCaller, request);
        	return configBancaDatiCaller.getConfigBancaDati(in);
        }
        public StatusResponse save(ConfigBancaDatiSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
        {	setCodSocietaHeader(configBancaDatiCaller, request);
        	return configBancaDatiCaller.save(in);
        }
        public StatusResponse cancel(ConfigBancaDatiCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
        {	setCodSocietaHeader(configBancaDatiCaller, request);
        	return configBancaDatiCaller.cancel(in);
        }

}
