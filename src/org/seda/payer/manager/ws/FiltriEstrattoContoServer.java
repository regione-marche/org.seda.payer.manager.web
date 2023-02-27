package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.FiltriEstrattoContoDeleteRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.FiltriEstrattoContoDetailRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.FiltriEstrattoContoDetailResponse;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.FiltriEstrattoContoSaveRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.FiltriEstrattoContoUpdateRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.RecuperaListaEntiDdlRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.RecuperaListaEntiDdlResponse;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.RecuperaListaFiltriEstrattoContoXmlRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.RecuperaListaFiltriEstrattoContoXmlResponse;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.StatusResponse;
import com.seda.payer.pgec.webservice.filtriestrattoconto.source.FiltriEstrattoContoSOAPBidingStub;
import com.seda.payer.pgec.webservice.filtriestrattoconto.source.FiltriEstrattoContoServiceLocator;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class FiltriEstrattoContoServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FiltriEstrattoContoSOAPBidingStub filtriEstrattoContoCaller = null;
	
	private void setCodSocietaHeader(FiltriEstrattoContoSOAPBidingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public FiltriEstrattoContoServer(String endPoint) {
		FiltriEstrattoContoServiceLocator lsService = new FiltriEstrattoContoServiceLocator();
		lsService.setFiltriEstrattoContoPortEndpointAddress(endPoint);
		try {
			filtriEstrattoContoCaller = (FiltriEstrattoContoSOAPBidingStub)lsService.getFiltriEstrattoContoPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public FiltriEstrattoContoDetailResponse recuperaFiltroEstrattoConto(FiltriEstrattoContoDetailRequest in, HttpServletRequest request) throws RemoteException,FaultType
	{
		setCodSocietaHeader(filtriEstrattoContoCaller, request);
		return filtriEstrattoContoCaller.recuperaFiltroEstrattoConto(in);
	}
    public RecuperaListaFiltriEstrattoContoXmlResponse recuperaListaFiltriEstrattoContoXml(RecuperaListaFiltriEstrattoContoXmlRequest in, HttpServletRequest request) throws RemoteException,FaultType
    {
    	setCodSocietaHeader(filtriEstrattoContoCaller, request);
		return filtriEstrattoContoCaller.recuperaListaFiltriEstrattoContoXml(in);
    }
    public StatusResponse save(FiltriEstrattoContoSaveRequest in, HttpServletRequest request) throws RemoteException,FaultType
    {
    	setCodSocietaHeader(filtriEstrattoContoCaller, request);
		return filtriEstrattoContoCaller.save(in);
    }
    public StatusResponse update(FiltriEstrattoContoUpdateRequest in, HttpServletRequest request) throws RemoteException,FaultType
    {
    	setCodSocietaHeader(filtriEstrattoContoCaller, request);
		return filtriEstrattoContoCaller.update(in);
    }
    public StatusResponse delete(FiltriEstrattoContoDeleteRequest in, HttpServletRequest request) throws RemoteException,FaultType
    {
    	setCodSocietaHeader(filtriEstrattoContoCaller, request);
		return filtriEstrattoContoCaller.delete(in);
    }
    public RecuperaListaEntiDdlResponse recuperaListEntiDdl(RecuperaListaEntiDdlRequest in, HttpServletRequest request) throws RemoteException,FaultType
    {
    	setCodSocietaHeader(filtriEstrattoContoCaller, request);
		return filtriEstrattoContoCaller.recuperaListEntiDdl(in);
    }
	
	

}
