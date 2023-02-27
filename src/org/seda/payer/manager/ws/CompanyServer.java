package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.company.dati.*;
import com.seda.payer.pgec.webservice.company.source.*;


public class CompanyServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CompanyImplementationStub companyCaller = null;
	
	private void setCodSocietaHeader(CompanyImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public CompanyServer(String endPoint) {
		CompanyServiceLocator lsService = new CompanyServiceLocator();
		lsService.setCompanyPortEndpointAddress(endPoint);
		try {
			companyCaller = (CompanyImplementationStub)lsService.getCompanyPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public CompanySearchResponse getCompanys(CompanySearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(companyCaller, request);
		return companyCaller.getCompanys(in);
	}
    public CompanyDetailResponse getCompany(CompanyDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(companyCaller, request);
    	return companyCaller.getCompany(in);
    }
    public StatusResponse save(CompanySaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(companyCaller, request);
    	return companyCaller.save(in);
    }
    public StatusResponse cancel(CompanyCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(companyCaller, request);
    	return companyCaller.cancel(in);
    }
	
	

}
