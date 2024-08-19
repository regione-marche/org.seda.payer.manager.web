package org.seda.payer.manager.monitoraggiosoap.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.jndi.JndiProxy;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.NotificheSoapList;
import com.seda.payer.core.dao.NotificheSoap;
import com.seda.payer.core.exception.DaoException;

public class MonitoraggioSoapAction extends BaseMonitoraggioSoapAction {

	private static final long serialVersionUID = 1L;

	private String idTrasizione="";
	private String codiceSocieta="";
	private String codiceUtente="";
	private String codiceEnte="";
	private String codiceFiscale="";
	private String codAvvisoPagoPA="";
	private String esitoNotifica="";
	private String numeroDocumento="";

	private String dataDa;
	private String dataA;

	private String dataNotificaDa;
	private String dataNotificaA;
	
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
//		loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
//		LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
//		
		switch(firedButton) {
			case TX_BUTTON_CERCA: 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
			
				rowsPerPage = request.getAttribute(Field.ROWS_PER_PAGE.format()) == null ? getDefaultListRows(request) : isNullInt(request.getAttribute(Field.ROWS_PER_PAGE.format()));
				pageNumber = request.getAttribute(Field.PAGE_NUMBER.format()) == null ? 1 : isNullInt(request.getAttribute(Field.PAGE_NUMBER.format()));
				order = isNull(request.getAttribute(Field.ORDER_BY.format()));
				
				codiceSocieta = isNull(getParamCodiceSocieta());
				codiceUtente = isNull(getParamCodiceUtente());
				codiceEnte = isNull(getParamCodiceEnte());
				
				if (getParamCodiceEnte() == null) {
					request.setAttribute(Field.TX_PROVINCIA.format(),"");
				}
				
				idTrasizione = isNull(request.getAttribute("txtIdTransazione"));
				codiceFiscale = isNull(request.getAttribute("txtCodiceFiscale"));
				codAvvisoPagoPA = isNull(request.getAttribute("txtCodAvvisoPagoPA"));
				numeroDocumento = isNull(request.getAttribute("txtNumeroDocumento"));
				esitoNotifica = isNull(request.getAttribute("ddlEsitoNotifica")).trim();
				
				dataDa = getDataByPrefix("tx_data_da",request);
				dataA = getDataByPrefix("tx_data_a",request);
				dataNotificaDa = getDataByPrefix("tx_data_notifica_da",request);
				dataNotificaA = getDataByPrefix("tx_data_notifica_a",request);

				System.out.println("MonitoraggioSoapAction - codiceSocieta: " + codiceSocieta);
				System.out.println("MonitoraggioSoapAction - codiceUtente: " + codiceUtente);
				System.out.println("MonitoraggioSoapAction - codiceEnte: " + codiceEnte);
				System.out.println("MonitoraggioSoapAction - idTrasizione: " + idTrasizione);
				System.out.println("MonitoraggioSoapAction - codiceFiscale: " + codiceFiscale);
				System.out.println("MonitoraggioSoapAction - codAvvisoPagoPA: " + codAvvisoPagoPA);
				System.out.println("MonitoraggioSoapAction - numeroDocumento: " + numeroDocumento);
				System.out.println("MonitoraggioSoapAction - esitoNotifica: " + esitoNotifica);
				System.out.println("MonitoraggioSoapAction - dataDa: " + dataDa);
				System.out.println("MonitoraggioSoapAction - dataA: " + dataA);
				System.out.println("MonitoraggioSoapAction - dataNotificaDa: " + dataNotificaDa);
				System.out.println("MonitoraggioSoapAction - dataNotificaA: " + dataNotificaA);


				try  {
					Connection conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);

					if(dataDa != "" && dataA != "" && Integer.parseInt(dataDa.replace("-","")) > Integer.parseInt(dataA.replace("-",""))) {
							throw new Exception("'data pagamento Da' non puo' essere maggiore di 'data pagamento A'"); 
					}
					
					if(dataNotificaDa != "" && dataNotificaA != "" && Integer.parseInt(dataNotificaDa.replace("-","")) > Integer.parseInt(dataNotificaA.replace("-","")) )  {
							throw new Exception("'data notifica Da' non puo' essere maggiore di 'data notifica A'"); 
					}
					
					NotificheSoap notificheSoap= new NotificheSoap(conn, payerDbSchema);
					NotificheSoapList listaNotificheSoap = notificheSoap.doList(
							codiceSocieta, 
							codiceUtente, 
							codiceEnte, 
							idTrasizione, 
							codiceFiscale, 
							codAvvisoPagoPA,
							numeroDocumento,
							esitoNotifica,
							dataDa,
							dataA,
							dataNotificaDa,
							dataNotificaA,
							order,
							rowsPerPage, 
							pageNumber
							);
					
					
					if(listaNotificheSoap != null)
					{
						if(listaNotificheSoap.getNumRows() > 0)
						{
							request.setAttribute("lista_notifiche_soap", listaNotificheSoap.getMonitoraggioNotificheSoapXml());
							request.setAttribute("lista_notifiche_soap.pageInfo", listaNotificheSoap);
						}
						else {
							setFormMessage("monitoraggioSoapForm", Messages.NO_DATA_FOUND.format(), request);
						}
					} else {
							setFormMessage("monitoraggioSoapForm", "Errore generico - listaNotificheSoap null", request);
					}
				} catch (DaoException e) {
					setFormMessage("monitoraggioSoapForm", e.getMessage() , request);
					e.printStackTrace();
				} catch (SQLException e1) {
					setFormMessage("monitoraggioSoapForm", e1.getMessage() , request);
					e1.printStackTrace();
				} catch(Exception e) {
					setFormMessage("monitoraggioSoapForm", e.getMessage() , request);
					e.printStackTrace();
				}
				break;
				
				
			case TX_BUTTON_RESET:
				resetParametri(request);
				request.setAttribute(Field.TX_DATA_DA.format(), null);
				request.setAttribute(Field.TX_DATA_A.format(), null);
				request.setAttribute("tx_data_notifica_da", null);
				request.setAttribute("tx_data_notifica_a", null);
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
				
//			case TX_BUTTON_NULL: 
			default : 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				break;
			
		}

		return null;
	}
	
	

}
