package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.configtassonomia.dati.*;
import com.seda.payer.pgec.webservice.configtassonomia.source.*;

public class ConfigTassonomiaServer extends BaseServer
{
	private static final long serialVersionUID = 1L;
	private ConfigTassonomiaSOAPBindingStub configTassonomiaCaller = null;
	
	private void setCodSocietaHeader(ConfigTassonomiaSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("", DBSCHEMACODSOCIETA, getCodSocieta(request));		
	}
	
	public ConfigTassonomiaServer(String endPoint) {
		ConfigTassonomiaServiceLocator lsService = new ConfigTassonomiaServiceLocator();
		lsService.setConfigTassonomiaPortEndpointAddress(endPoint);
		try {
			configTassonomiaCaller = (ConfigTassonomiaSOAPBindingStub)lsService.getConfigTassonomiaPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public ConfigTassonomiaListaResponse lista(ConfigTassonomiaListaRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{
		setCodSocietaHeader(configTassonomiaCaller, request);
		return configTassonomiaCaller.lista(in);
	}

    public ConfigTassonomiaDettaglioResponse dettaglio(ConfigTassonomiaDettaglioRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(configTassonomiaCaller, request);
    	return configTassonomiaCaller.dettaglio(in);
    }

    public ConfigTassonomiaCreaResponse crea(ConfigTassonomiaCreaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(configTassonomiaCaller, request);
    	return configTassonomiaCaller.crea(in);
    }

    public ConfigTassonomiaModificaResponse modifica(ConfigTassonomiaModificaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(configTassonomiaCaller, request);
    	return configTassonomiaCaller.modifica(in);
    }

    public ConfigTassonomiaEliminaResponse elimina(ConfigTassonomiaEliminaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(configTassonomiaCaller, request);
    	return configTassonomiaCaller.elimina(in);
    }

    public ConfigTassonomiaVerificaResponse verifica(ConfigTassonomiaVerificaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(configTassonomiaCaller, request);
    	return configTassonomiaCaller.verifica(in);
    }
}
