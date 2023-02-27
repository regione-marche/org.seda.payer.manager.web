package org.seda.payer.manager.monitoraggiocup.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMICRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMICResponse;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMIPRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMIPResponse;

public class MonitoraggioCupAction extends BaseMonitoraggioCupAction {

	private static final long serialVersionUID = 1L;
	private String siglaProvincia;
	
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession(false);
		
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Chiamo il metodo per i settaggi relativi al profilo utente
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) setProfile(request);
		/*
		 * Carico le DDl statiche
		 */
		loadStaticXml_DDL(request, session);	
		
		resetFormMessage(request);
		siglaProvincia = isNull(request.getAttribute(Field.TX_PROVINCIA.format()));
		
		switch(firedButton) {
			case TX_BUTTON_CERCA: 
				
				try {
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);

					RecuperaListaMICResponse listaMIC = getListaMIC(request);
					if(listaMIC != null)
					{
						if(listaMIC.getRetCode().equals("00"))
						{
							PageInfo pageInfo = getPageInfo(listaMIC.getPageInfo());
							if(pageInfo != null)
							{
								if(pageInfo.getNumRows() > 0)
								{
									//String lista = listaMIP.getListMIPXml();
									String lista = elaboraXmlListaMip(listaMIC.getListMICXml(), request, "monitoraggioExtForm");
									request.setAttribute("listaMIC", lista);
									request.setAttribute("listaMIC.pageInfo", pageInfo);
								}
								else 
									setFormMessage("monitoraggioExtForm", Messages.NO_DATA_FOUND.format(), request);
							}
							else 
								setFormMessage("monitoraggioTransazioniForm", "Errore generico - PageInfo null", request);
						}
						else
							setFormMessage("monitoraggioExtForm", listaMIC.getRetMessage() , request);
					}
					else 
						setFormMessage("monitoraggioExtForm", "Errore durante il recupero dei dati richiesti" , request);
					
				} catch (FaultType e) {
					setFormMessage("monitoraggioExtForm", e.getMessage() , request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("monitoraggioExtForm", e.getMessage() , request);
					e.printStackTrace();
				} catch(Exception e) {
					setFormMessage("monitoraggioExtForm", e.getMessage() , request);
					e.printStackTrace();
				}
				break;
				
				
			case TX_BUTTON_RESET:
				resetParametri(request);
				request.setAttribute(Field.TX_DATA_DA.format(), null);
				request.setAttribute(Field.TX_DATA_A.format(), null);
				setProfile(request);
				siglaProvincia = "";
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				break;
				
			case TX_BUTTON_SOCIETA_CHANGED:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				break;
				
			case TX_BUTTON_NULL: 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				break;
			
		}

		return null;
	}

	private RecuperaListaMICResponse getListaMIC(HttpServletRequest request) throws FaultType, RemoteException {
		
		RecuperaListaMICRequest listaReq = new RecuperaListaMICRequest();
		
		int rowsPerPage = request.getAttribute(Field.ROWS_PER_PAGE.format()) == null ? getDefaultListRows(request) : isNullInt(request.getAttribute(Field.ROWS_PER_PAGE.format()));
		int pageNumber = request.getAttribute(Field.PAGE_NUMBER.format()) == null ? 1 : isNullInt(request.getAttribute(Field.PAGE_NUMBER.format()));
		String order = isNull(request.getAttribute(Field.ORDER_BY.format()));
		
		listaReq.setOrder(order);
		listaReq.setPageNumber(pageNumber);
		listaReq.setRowsPerPage(rowsPerPage);
		
		listaReq.setCodiceSocieta(getParamCodiceSocieta());
		//listaReq.setProvincia(siglaProvincia);
		listaReq.setCodiceUtente(getParamCodiceUtente());
		listaReq.setChiaveEnte(getParamCodiceEnte());
		listaReq.setChiaveTransazione(isNull(request.getAttribute("txtIdTransazione")));
		listaReq.setCodiceFiscale(isNull(request.getAttribute("txtCodiceFiscale")));
		listaReq.setCodicePagamento(isNull(request.getAttribute("txtCodicePagamento")));
		listaReq.setNumeroOperazione(isNull(request.getAttribute("txtNumeroOperazione")));
		listaReq.setNumeroDocumento(isNull(request.getAttribute("txtNumeroDocumento")));
		listaReq.setEsitoNotifica(isNull(request.getAttribute("ddlEsitoNotifica")).trim());
		listaReq.setDataA(getDataByPrefix("tx_data_a",request));
		listaReq.setDataDa(getDataByPrefix("tx_data_da",request));
		
		return WSCache.mipServer.recuperaListaMIC(listaReq, request);
	}

	
	

}
