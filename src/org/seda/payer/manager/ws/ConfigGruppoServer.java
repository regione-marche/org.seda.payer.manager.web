package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.configgruppo.dati.*;
import com.seda.payer.pgec.webservice.configgruppo.source.*;

public class ConfigGruppoServer extends BaseServer
{
	private static final long serialVersionUID = 1L;
	private ConfigGruppoSOAPBindingStub configGruppoCaller = null;
	
	private void setCodSocietaHeader(ConfigGruppoSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("", DBSCHEMACODSOCIETA, getCodSocieta(request));		
	}
	
	public ConfigGruppoServer(String endPoint) {
		ConfigGruppoServiceLocator lsService = new ConfigGruppoServiceLocator();
		lsService.setConfigGruppoPortEndpointAddress(endPoint);
		try {
			configGruppoCaller = (ConfigGruppoSOAPBindingStub)lsService.getConfigGruppoPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public ConfigGruppoListaResponse lista(ConfigGruppoListaRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{
		setCodSocietaHeader(configGruppoCaller, request);
		return configGruppoCaller.lista(in);
	}

    public ConfigGruppoDettaglioResponse dettaglio(ConfigGruppoDettaglioRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(configGruppoCaller, request);
    	return configGruppoCaller.dettaglio(in);
    }

    public ConfigGruppoCreaResponse crea(ConfigGruppoCreaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(configGruppoCaller, request);
    	return configGruppoCaller.crea(in);
    }

    public ConfigGruppoModificaResponse modifica(ConfigGruppoModificaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(configGruppoCaller, request);
    	return configGruppoCaller.modifica(in);
    }

    public ConfigGruppoEliminaResponse elimina(ConfigGruppoEliminaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(configGruppoCaller, request);
    	return configGruppoCaller.elimina(in);
    }

    public ConfigGruppoVerificaResponse verifica(ConfigGruppoVerificaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(configGruppoCaller, request);
    	return configGruppoCaller.verifica(in);
    }
}
