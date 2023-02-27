package org.seda.payer.manager.monitoraggio.actions;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;

import com.seda.payer.gateways.webservice.dati.RichiediStornoRequest;
import com.seda.payer.gateways.webservice.dati.RichiediStornoResponse;
import com.seda.payer.integraente.webservice.dati.Response;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneXmlRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneXmlResponse;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class RichiediStornoAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String idOperazione; 
		private BigDecimal importoTransazione;
		private String chiaveGateway;
		
	public Object service(HttpServletRequest request) throws ActionException {
		
		HttpSession session = request.getSession(false);
		
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		String operatore = user.getUserName();
		request.getParameter(operatore);
		String codiceTransazione = request.getParameter(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		request.setAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format(), codiceTransazione);
		String canalePagamento = request.getParameter(Field.TX_CANALE_PAGAMENTO.format());
		request.setAttribute(Field.TX_CANALE_PAGAMENTO.format(), canalePagamento);
		
		switch(getFiredButton(request)) {
			case TX_BUTTON_CONFERMA_STORNO: 
				/*
				 * Carico il record relativo alla transazione selezionata
				 */
				RecuperaTransazione(codiceTransazione, request);
				
				
				try {
						WSCache.logWriter.logDebug("richiesta storno: id transazione='"+codiceTransazione+"id operazione='"+idOperazione+"importo transazione='"+importoTransazione+"canale pagamento='"+canalePagamento+"operatore='"+operatore+"'");
						RichiediStornoResponse richiediStornoResponse;
						richiediStornoResponse = WSCache.gatewaysServer.richiediStorno (codiceTransazione, idOperazione, importoTransazione, canalePagamento, chiaveGateway, operatore, request);
						
						
						if(richiediStornoResponse.getRetCode().equals("0"))
						{
							//setFormMessage("", Messages.TX_RICHIESTA_STORNO_ESITO_OK.format(), request);
							request.setAttribute("message", Messages.TX_RICHIESTA_STORNO_ESITO_OK.format());
							request.setAttribute("tx_button_conferma_storno_visible", false);
						}
						else
						{
							//setFormMessage("lblCanc", Messages.TX_RICHIESTA_STORNO_ESITO_KO.format(richiediStornoResponse.getRetMessage()), request);
							request.setAttribute("message", Messages.TX_RICHIESTA_STORNO_ESITO_KO.format());
						}
					} 
					catch (Exception e) {
						//setFormMessage("lblCanc", Messages.TX_RICHIESTA_STORNO_KO.format(), request);
						request.setAttribute("message", Messages.TX_RICHIESTA_STORNO_KO.format());
						//setErrorMessage(e.getMessage());
						WSCache.logWriter.logError("richiesta storno fallita: id transazione='"+codiceTransazione+"id operazione='"+idOperazione+"importo transazione='"+importoTransazione+"canale pagamento='"+canalePagamento+"'", e);
					}
//				}
				
				break;

			case TX_BUTTON_NULL: 
				
				/*
				 * Carico il record relativo alla transazione selezionata
				 */
				RecuperaTransazione(codiceTransazione, request);
				request.setAttribute("tx_button_conferma_storno_visible", true);
				
				
				break;
		}
		
		return null;
	}
	
	
	private void RecuperaTransazione(String codiceTransazione, HttpServletRequest request){
		//inizio LP PG21XX04 Leak
		WebRowSet webRowSet = null;
		//fine LP PG21XX04 Leak
		try {
			RecuperaTransazioneXmlResponse listaRT;
			//listaRT = getListaTransazioni(1, 1, "", "","","","","", codiceTransazione, "", "", "", "","","","","", "", "", "", "", "", "",0);
			listaRT = getTransazioneXml(codiceTransazione, request);
			String lista = listaRT.getListXml();
			if (listaRT.getResponse().getRetCode().equals(ResponseTypeRetCode.value1)) {
				request.setAttribute(Field.TX_TRANSAZIONE.format(), lista);
				//inizio LP PG21XX04 Leak
				//WebRowSet webRowSet= Convert.stringToWebRowSet(lista);
				webRowSet= Convert.stringToWebRowSet(lista);
				//fine LP PG21XX04 Leak
				webRowSet.first();
				
				idOperazione = webRowSet.getString("TRA_CTRAIDBA");
				importoTransazione = webRowSet.getBigDecimal("TRA_ITRAITOT");
				BigDecimal costiTransazione = webRowSet.getBigDecimal("TRA_ITRACOTR");
				chiaveGateway = webRowSet.getString("TRA_KGTWKGTW");
				importoTransazione = importoTransazione.subtract(costiTransazione);
			//	request.setAttribute(Field.TX_ID_OPERAZIONE.format(), idOperazione);
			//	request.setAttribute(Field.TX_IMPORTO_NETTO.format(), importoTransazione);
			//	request.setAttribute(Field.TX_CHIAVE_GATEWAY.format(), chiaveGateway);
				
			}
			else {
//				setMessage(Messages.TX_NOT_FOUND.format(codiceTransazione));
				request.setAttribute("message", Messages.TX_NOT_FOUND.format());
				//setFormMessage("lblCanc", Messages.TX_NOT_FOUND.format(codiceTransazione), request);
				
			}
		} 
		catch (Exception e) {
			setErrorMessage(e.getMessage());
		}
		//inizio LP PG21XX04 Leak
		finally {
			try {
				if(webRowSet != null) {
					webRowSet.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
	}
	
	private RecuperaTransazioneXmlResponse getTransazioneXml (String chiaveTransazione, HttpServletRequest request) throws FaultType, RemoteException  {
		RecuperaTransazioneXmlRequest in=new RecuperaTransazioneXmlRequest(chiaveTransazione);
		RecuperaTransazioneXmlResponse out;
		
		out=WSCache.commonsServer.recuperaTransazioneXml(in, request);
		return out;
	}
	

}

