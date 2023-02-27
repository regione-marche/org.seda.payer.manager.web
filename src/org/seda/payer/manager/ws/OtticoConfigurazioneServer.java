package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.ottico.webservice.configurazione.dati.CreaAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.CreaAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.CreaParametriOtticoRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.CreaParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioParametriOtticoRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.EliminaAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.EliminaAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.EliminaParametriOtticoRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.EliminaParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.ListaAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.ListaAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.ListaParametriOtticoRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.ListaParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.ModificaAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.ModificaAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.ModificaParametriOtticoRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.ModificaParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.VerificaAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.VerificaAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneSOAPBindingStub;
import com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneServiceLocator;
import com.seda.payer.ottico.webservice.configurazione.srv.FaultType;
/**
 * @author marco.montisano
 */
public class OtticoConfigurazioneServer extends BaseServer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#verificaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.VerificaAssociaTemplateRequest)
	 */
	public VerificaAssociaTemplateResponse verificaAssociaTemplate(
			VerificaAssociaTemplateRequest arg0) throws RemoteException,
			FaultType {
		return configurazioneCaller.verificaAssociaTemplate(arg0);
	}
	private ConfigurazioneSOAPBindingStub configurazioneCaller = null;
	
	private void setCodSocietaHeader(ConfigurazioneSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	/**
	 * @param endPoint
	 */
	public OtticoConfigurazioneServer(String endPoint) {
		ConfigurazioneServiceLocator serviceLocator = new ConfigurazioneServiceLocator();
		serviceLocator.setConfigurazionePortEndpointAddress(endPoint);
		try { configurazioneCaller = (ConfigurazioneSOAPBindingStub) serviceLocator.getConfigurazionePort();
		} catch (ServiceException e) { e.printStackTrace(); }
	}
	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#creaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.CreaAssociaTemplateRequest)
	 */
	public CreaAssociaTemplateResponse creaAssociaTemplate(
			CreaAssociaTemplateRequest arg0, HttpServletRequest request) throws RemoteException, FaultType {
		setCodSocietaHeader(configurazioneCaller, request);
		return configurazioneCaller.creaAssociaTemplate(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#creaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.CreaParametriOtticoRequest)
	 */
	public CreaParametriOtticoResponse creaParametriOttico(
			CreaParametriOtticoRequest arg0, HttpServletRequest request) throws RemoteException, FaultType {
		setCodSocietaHeader(configurazioneCaller, request);
		return configurazioneCaller.creaParametriOttico(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#eliminaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.EliminaAssociaTemplateRequest)
	 */
	public EliminaAssociaTemplateResponse eliminaAssociaTemplate(
			EliminaAssociaTemplateRequest arg0, HttpServletRequest request) throws RemoteException,
			FaultType {
		setCodSocietaHeader(configurazioneCaller, request);
		return configurazioneCaller.eliminaAssociaTemplate(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#eliminaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.EliminaParametriOtticoRequest)
	 */
	public EliminaParametriOtticoResponse eliminaParametriOttico(
			EliminaParametriOtticoRequest arg0, HttpServletRequest request) throws RemoteException,
			FaultType {
		setCodSocietaHeader(configurazioneCaller, request);
		return configurazioneCaller.eliminaParametriOttico(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#listaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.ListaAssociaTemplateRequest)
	 */
	public ListaAssociaTemplateResponse listaAssociaTemplate(
			ListaAssociaTemplateRequest arg0, HttpServletRequest request) throws RemoteException, FaultType {
		setCodSocietaHeader(configurazioneCaller, request);
		return configurazioneCaller.listaAssociaTemplate(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#listaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.ListaParametriOtticoRequest)
	 */
	public ListaParametriOtticoResponse listaParametriOttico(
			ListaParametriOtticoRequest arg0, HttpServletRequest request) throws RemoteException, FaultType {
		setCodSocietaHeader(configurazioneCaller, request);
		return configurazioneCaller.listaParametriOttico(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#modificaAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.ModificaAssociaTemplateRequest)
	 */
	public ModificaAssociaTemplateResponse modificaAssociaTemplate(
			ModificaAssociaTemplateRequest arg0, HttpServletRequest request) throws RemoteException,
			FaultType {
		setCodSocietaHeader(configurazioneCaller, request);
		return configurazioneCaller.modificaAssociaTemplate(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#modificaParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.ModificaParametriOtticoRequest)
	 */
	public ModificaParametriOtticoResponse modificaParametriOttico(
			ModificaParametriOtticoRequest arg0, HttpServletRequest request) throws RemoteException,
			FaultType {
		setCodSocietaHeader(configurazioneCaller, request);
		return configurazioneCaller.modificaParametriOttico(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#dettaglioAssociaTemplate(com.seda.payer.ottico.webservice.configurazione.dati.DettaglioAssociaTemplateRequest)
	 */
	public DettaglioAssociaTemplateResponse dettaglioAssociaTemplate(
			DettaglioAssociaTemplateRequest arg0, HttpServletRequest request) throws RemoteException,
			FaultType {
		setCodSocietaHeader(configurazioneCaller, request);
		return configurazioneCaller.dettaglioAssociaTemplate(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 * @throws RemoteException
	 * @throws FaultType
	 * @see com.seda.payer.ottico.webservice.configurazione.source.ConfigurazioneInterface#dettaglioParametriOttico(com.seda.payer.ottico.webservice.configurazione.dati.DettaglioParametriOtticoRequest)
	 */
	public DettaglioParametriOtticoResponse dettaglioParametriOttico(
			DettaglioParametriOtticoRequest arg0, HttpServletRequest request) throws RemoteException,
			FaultType {
		setCodSocietaHeader(configurazioneCaller, request);
		return configurazioneCaller.dettaglioParametriOttico(arg0);
	}
}