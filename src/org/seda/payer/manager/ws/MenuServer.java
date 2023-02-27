package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.menu.dati.*;
import com.seda.payer.pgec.webservice.menu.source.*;


public class MenuServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MenuImplementationStub menuCaller = null;
	
	private void setCodSocietaHeader(MenuImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public MenuServer(String endPoint) {
		MenuServiceLocator lsService = new MenuServiceLocator();
		lsService.setMenuPortEndpointAddress(endPoint);
		try {
			menuCaller = (MenuImplementationStub)lsService.getMenuPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public MenuGetResponse getMenu(MenuGetRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(menuCaller, request);
		return menuCaller.getMenu(in);
	}
    
	
	

}
