package org.seda.payer.manager.rendicontazione.actions;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.action.ActionException;

import com.seda.payer.integraente.webservice.dati.InviaFlussoRendRequestType;
import com.seda.payer.integraente.webservice.dati.InviaFlussoRendResponseType;
import com.seda.payer.integraente.webservice.dati.Response;
import com.seda.payer.integraente.webservice.dati.ResponseRetcode;
import com.seda.payer.integraente.webservice.srv.FaultType;

import org.seda.payer.manager.util.*;
import org.seda.payer.manager.ws.WSCache;


public class InviaFlusso extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		String chiaveRendicontazione = request.getParameter("chiaveRendicontazione");
		if(chiaveRendicontazione.equals(""))
			chiaveRendicontazione = (String)request.getAttribute("chiaveRendicontazione");
		String tipoInvio = (String)request.getAttribute("tx_tipo_invio_flusso");

		String nomeflusso = request.getParameter("nomeFile");//Giulia
		if(chiaveRendicontazione == null || chiaveRendicontazione.equals(""))
		{
			setFormMessage("back_to_ricercaFlussi", Messages.CHIAVE_FLUSSO_NON_VALIDA.format(), request);
		}
		else
		{
			if(tipoInvio == null || (!tipoInvio.equals("EMAIL") && !tipoInvio.equals("FTP") &&  !tipoInvio.equals("WS")))
			{
				setFormMessage("back_to_ricercaFlussi", Messages.TIPO_INVIO_FLUSSO_NON_VALIDO.format(), request);
			}
			else
			{
				try {
					Response response = inviaFlusso(chiaveRendicontazione,tipoInvio, request);
					//String nomeflusso = "\"<b>" + chiaveRendicontazione + ".zip</b>\"";--Giulia
					if(response.getRetcode().equals(ResponseRetcode.value1))
					{
						setFormMessage("back_to_ricercaFlussi", Messages.INVIO_FLUSSO_OK.format(nomeflusso,tipoInvio), request);
					}
					else
					{
						setFormMessage("back_to_ricercaFlussi", Messages.INVIO_FLUSSO_KO.format(nomeflusso,tipoInvio,response.getRetmessage()), request);
					}
				} catch (FaultType e) {
					setFormMessage("back_to_ricercaFlussi", e.getMessage(), request);
					e.printStackTrace();
				} catch (MalformedURLException e) {
					setFormMessage("back_to_ricercaFlussi", e.getMessage(), request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("back_to_ricercaFlussi", e.getMessage(), request);
					e.printStackTrace();
				} catch (ServiceException e) {
					setFormMessage("back_to_ricercaFlussi", e.getMessage(), request);
					e.printStackTrace();
				}
			}
			
		}
		return null;
	}

	private Response inviaFlusso(String chiaveRendicontazione,String tipoInvio, HttpServletRequest request) throws MalformedURLException, ServiceException, FaultType, RemoteException
	{
		InviaFlussoRendRequestType in = new InviaFlussoRendRequestType();
		switch(tipoInvio)
		{
			case "EMAIL" : in.setTipoInvio("E") ;break;
			case "WS" : in.setTipoInvio("W") ;break;
			case "FTP" : in.setTipoInvio("F") ;break;
			default:break;
		}
		
		in.setChiaveRendicontazione(chiaveRendicontazione);
		InviaFlussoRendResponseType out = WSCache.rendicontaEnteServer.inviaFlussoRend(in, request);
		Response response = out.getResponse();
		return response;
	}
}

