package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteCancelRequest;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteDetailRequest;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteDetailResponse;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteSaveRequest;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteSearchRequest;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.StatusResponse;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.source.AbilitaCanalePagamentoTipoServizioEnteImplementationStub;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.source.AbilitaCanalePagamentoTipoServizioEnteServiceLocator;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class AbilitaCanalePagamentoTipoServizioEnteServer extends BaseServer 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AbilitaCanalePagamentoTipoServizioEnteImplementationStub abilitaCanalePagamentoTipoServizioEnteCaller = null;

	private void setCodSocietaHeader (AbilitaCanalePagamentoTipoServizioEnteImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
		
	public AbilitaCanalePagamentoTipoServizioEnteServer(String endPoint) {
		AbilitaCanalePagamentoTipoServizioEnteServiceLocator lsService = new AbilitaCanalePagamentoTipoServizioEnteServiceLocator();
		lsService.setAbilitaCanalePagamentoTipoServizioEntePortEndpointAddress(endPoint);
		try {
			abilitaCanalePagamentoTipoServizioEnteCaller = (AbilitaCanalePagamentoTipoServizioEnteImplementationStub)lsService.getAbilitaCanalePagamentoTipoServizioEntePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public AbilitaCanalePagamentoTipoServizioEnteSearchResponse getAbilitaCanalePagamentoTipoServizioEntes(AbilitaCanalePagamentoTipoServizioEnteSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{
		setCodSocietaHeader(abilitaCanalePagamentoTipoServizioEnteCaller, request);
		return abilitaCanalePagamentoTipoServizioEnteCaller.getAbilitaCanalePagamentoTipoServizioEntes(in);
	}
	
	public AbilitaCanalePagamentoTipoServizioEnteSearchResponse getAbilitaCanalePagamentoTipoServizioEntes2(AbilitaCanalePagamentoTipoServizioEnteSearchRequest2 in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(abilitaCanalePagamentoTipoServizioEnteCaller, request);
		return abilitaCanalePagamentoTipoServizioEnteCaller.getAbilitaCanalePagamentoTipoServizioEntes2(in);
	}
	
    public AbilitaCanalePagamentoTipoServizioEnteDetailResponse getAbilitaCanalePagamentoTipoServizioEnte(AbilitaCanalePagamentoTipoServizioEnteDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(abilitaCanalePagamentoTipoServizioEnteCaller, request);
    	return abilitaCanalePagamentoTipoServizioEnteCaller.getAbilitaCanalePagamentoTipoServizioEnte(in);
    }
    public StatusResponse save(AbilitaCanalePagamentoTipoServizioEnteSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(abilitaCanalePagamentoTipoServizioEnteCaller, request);
    	return abilitaCanalePagamentoTipoServizioEnteCaller.save(in);
    }
    public StatusResponse cancel(AbilitaCanalePagamentoTipoServizioEnteCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(abilitaCanalePagamentoTipoServizioEnteCaller, request);
    	return abilitaCanalePagamentoTipoServizioEnteCaller.cancel(in);
    }
	
	

}
