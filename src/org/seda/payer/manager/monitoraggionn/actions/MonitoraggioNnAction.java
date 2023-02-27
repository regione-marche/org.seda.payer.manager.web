package org.seda.payer.manager.monitoraggionn.actions;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMINRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMINResponse;

public class MonitoraggioNnAction extends BaseMonitoraggioNnAction {

	private static final long serialVersionUID = 1L;
	private String siglaProvincia;
	
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession(false);

		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, session); 
		//fine LP PG200060
		
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
		//inizio LP PG200060
		Calendar cal = null;		
		if(template.equalsIgnoreCase("regmarche")) {
			Locale locale = (Locale) request.getSession().getAttribute(MAFAttributes.LOCALE);
			cal = Calendar.getInstance(locale);
		}
		//fine LP PG200060
		
		switch(firedButton) {
			case TX_BUTTON_CERCA: 
				
				try {
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);

					RecuperaListaMINResponse listaMIP = getListaMIN(request);
					// TODO Aggiungere campo per tx_esitoinvio 
					if(listaMIP != null)
					{
						if(listaMIP.getRetCode().equals("00"))
						{
							PageInfo pageInfo = getPageInfo(listaMIP.getPageInfo());
							if(pageInfo != null)
							{
								if(pageInfo.getNumRows() > 0)
								{
									//String lista = listaMIP.getListMIPXml();
									String lista = elaboraXmlListaMip(listaMIP.getListMIPXml(), request, "MonitoraggioNnForm");
									request.setAttribute("listaMIP", lista);
									request.setAttribute("listaMIP.pageInfo", pageInfo);
								}
								else 
									setFormMessage("monitoraggioNnForm", Messages.NO_DATA_FOUND.format(), request);
							}
							else 
								setFormMessage("monitoraggioNnForm", "Errore generico - PageInfo null", request);
						}
						else
							setFormMessage("monitoraggioNnForm", listaMIP.getRetMessage() , request);
					}
					else 
						setFormMessage("monitoraggioNnForm", "Errore durante il recupero dei dati richiesti" , request);
					
				} catch (FaultType e) {
					setFormMessage("monitoraggioNnForm", e.getMessage() , request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("monitoraggioNnForm", e.getMessage() , request);
					e.printStackTrace();
				} catch(Exception e) {
					setFormMessage("monitoraggioNnForm", e.getMessage() , request);
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
				//inizio LP PG200060
				if(template.equalsIgnoreCase("regmarche")) {
					cal.add(Calendar.DATE, -1); //ieri
					request.setAttribute(Field.TX_DATA_DA.format(), cal);
				}
				//fine LP PG200060
				break;
			
		}

		//inizio LP PG200060
		if(!template.equalsIgnoreCase("regmarche")) {
		//fine LP PG200060
		request.setAttribute(Field.TX_MESSAGE.format(), getMessage());
		request.setAttribute(Field.TX_ERROR_MESSAGE.format(), getErrorMessage());
		//inizio LP PG200060
		}
		//fine LP PG200060
		return null;
	}

	private RecuperaListaMINResponse getListaMIN(HttpServletRequest request) throws FaultType, RemoteException {
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, request.getSession()); 
		//fine LP PG200060
		
		RecuperaListaMINRequest listaReq = new RecuperaListaMINRequest();
		
		int rowsPerPage = request.getAttribute(Field.ROWS_PER_PAGE.format()) == null ? getDefaultListRows(request) : isNullInt(request.getAttribute(Field.ROWS_PER_PAGE.format()));
		int pageNumber = request.getAttribute(Field.PAGE_NUMBER.format()) == null ? 1 : isNullInt(request.getAttribute(Field.PAGE_NUMBER.format()));
		String order = isNull(request.getAttribute(Field.ORDER_BY.format()));
		
		listaReq.setOrder(order);
		listaReq.setPageNumber(pageNumber);
		listaReq.setRowsPerPage(rowsPerPage);
		
		listaReq.setCodiceSocieta(getParamCodiceSocieta());
		listaReq.setProvincia(siglaProvincia);
		listaReq.setCodiceUtente(getParamCodiceUtente());
		listaReq.setChiaveEnte(getParamCodiceEnte());
		listaReq.setChiaveTransazione(isNull(request.getAttribute("txtIdTransazione")));
		listaReq.setNumeroOperazione(isNull(request.getAttribute("codiceIUV")));
//		listaReq.setNumeroDocumento(isNull(request.getAttribute("txtNumeroDocumento")));
		listaReq.setNumeroDocumento("");
		listaReq.setEsitoNotifica(isNull(request.getAttribute("ddlEsitoNotifica")).trim());
		//inizio LP PG200060
		if(template.equalsIgnoreCase("regmarche")) {
			listaReq.setEsitoNotifica("ALL");
			listaReq.setEsitoInvioRT(isNull(request.getAttribute("tx_EsitoInvioRT")).trim());
			listaReq.setStatoInvioRT(isNull(request.getAttribute("tx_StatoInvioRT")).trim());
			listaReq.setNumProtocolloRT(isNull(request.getAttribute("tx_NumeroProtocolloRT")).trim());
		} else {
			listaReq.setEsitoInvioRT("ALL");
			listaReq.setStatoInvioRT("ALL");
			listaReq.setNumProtocolloRT("");
		}
		//fine LP PG200060
		//inizio LP PG180290
		if(template.equalsIgnoreCase("trentrisc")) {
			listaReq.setTipoGateways(isNull(request.getAttribute("tx_TipoGateways")).trim());
		} else {
			listaReq.setTipoGateways("");
		}
		//fine LP PG180290
		
		listaReq.setDataA(getDataByPrefix("tx_data_a",request));
		listaReq.setDataDa(getDataByPrefix("tx_data_da",request));
		listaReq.setEsitoInvioConservazione(isNull(request.getAttribute("tx_esitoinvio")));
		
		return WSCache.mipServer.recuperaListaMIN(listaReq, request);
	}

	
	

}
