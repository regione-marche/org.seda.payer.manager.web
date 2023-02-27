/**
 * 
 */
package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchResponse;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizio;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioDetailRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioDetailResponse;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioSaveRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.Response;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.StatusResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

/**
 * @author lmontesi
 *
 */

@SuppressWarnings("serial")
public class RangeAbiUtenteTipoServizioActionEdit extends BaseManagerAction {
	
	private static String codop = "edit";
	private static String ritornaViewstate = "Rangeabiutentetiposervizio_search";
	private String usernameAutenticazione = null;
	private HashMap<String,Object> parametri = null;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			RangeAbiUtenteTipoServizioDetailResponse detailResponse = null;
			RangeAbiUtenteTipoServizioDetailRequest in = new RangeAbiUtenteTipoServizioDetailRequest(request.getParameter("rangeabiutentetiposervizio_chiaveRangeTipoServizio"));
			
			try {
				
				detailResponse = WSCache.rangeAbiUtenteTipoServizioServer.getRangeAbiUtenteTipoServizio(in, request);
				if (detailResponse != null)
				{
					if (detailResponse.getRangeabiutentetiposervizio() != null)
					{
					      request.setAttribute("rangeabiutentetiposervizio_chiaveRangeTipoServizio", detailResponse.getRangeabiutentetiposervizio().getChiaveRangeTipoServizio());
						  request.setAttribute("rangeabiutentetiposervizio_companyCode", detailResponse.getRangeabiutentetiposervizio().getCompanyCode());
						  request.setAttribute("rangeabiutentetiposervizio_codiceUtente", detailResponse.getRangeabiutentetiposervizio().getCodiceUtente());
						  request.setAttribute("rangeabiutentetiposervizio_codiceTipologiaServizio", detailResponse.getRangeabiutentetiposervizio().getCodiceTipologiaServizio());
						  request.setAttribute("rangeabiutentetiposervizio_inizioRangeDa", detailResponse.getRangeabiutentetiposervizio().getInizioRangeDa());
						  request.setAttribute("rangeabiutentetiposervizio_fineRangeA", detailResponse.getRangeabiutentetiposervizio().getFineRangeA());
						  request.setAttribute("rangeabiutentetiposervizio_inizioRangePer", detailResponse.getRangeabiutentetiposervizio().getInizioRangePer());
						  request.setAttribute("rangeabiutentetiposervizio_tipoRange", detailResponse.getRangeabiutentetiposervizio().getTipoRange());
						  request.setAttribute("rangeabiutentetiposervizio_flagCin", detailResponse.getRangeabiutentetiposervizio().getFlagCin());
						  request.setAttribute("tx_societa", detailResponse.getRangeabiutentetiposervizio().getCompanyCode());
						  request.setAttribute("tx_utente", detailResponse.getRangeabiutentetiposervizio().getCodiceUtente());
						  request.setAttribute("tx_codiceTipologiaServizio", detailResponse.getRangeabiutentetiposervizio().getCodiceTipologiaServizio());
						  
						  ConfigUtenteTipoServizioSearchResponse configUtenteTipoServizioSearchResponse = getConfigUtenteTipoServizioSearchResponse("","","", 0, 0, "", request);
						  request.setAttribute("configutentetiposervizios", configUtenteTipoServizioSearchResponse.getResponse().getListXml());
							
					}
					else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				}
				else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				} catch (FaultType e) {
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
				} catch (RemoteException e) {
				setFormMessage("var_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
				}
				
			
			break;
			
		case TX_BUTTON_EDIT:
			/*
			 * Recupero i parametri dalla form
			 */
			parametri = getRangeAbiUtenteTipoServizioParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			String esito = checkInputRangeAbiUtenteServizio(parametri);
			/*
			 * Se OK effttuo il salvataggio
			 */
			if (esito.equals("OK"))
			{
				//Recupero del codice operatore
				UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
				if( user!=null) 
				   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
				
				    usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
								
					String companyCode="";
					String codiceUtente = "";
					String codiceTipologiaServizio = "";
					String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
					request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
					request.setAttribute("error", null);
					String cod = request.getParameter("prova");
					String arrStr = request.getParameter("rangeabiutentetiposervizio_strConfigutentetiposervizios");
					if (cod.equals("0") && arrStr!=null && arrStr.length()>0)
					{
						  String[] strConfigutentetiposervizios = arrStr.split("\\|");
			              companyCode = strConfigutentetiposervizios[0];
			 		      codiceUtente= strConfigutentetiposervizios[1];
			 		      codiceTipologiaServizio= strConfigutentetiposervizios[2];
			 		}
					if (cod.equals("1"))
					{
			              companyCode = request.getParameter("rangeabiutentetiposervizio_companyCode");
			              codiceUtente= request.getParameter("rangeabiutentetiposervizio_codiceUtente");
			              codiceTipologiaServizio= request.getParameter("rangeabiutentetiposervizio_codiceTipologiaServizio");
			              companyCode = request.getParameter("tx_societa");
			              codiceUtente= request.getParameter("tx_utente");
			              codiceTipologiaServizio= request.getParameter("tx_codiceTipologiaServizio");

					}
					String chiaveRangeTipoServizio = request.getParameter("rangeabiutentetiposervizio_chiaveRangeTipoServizio");
					String inizioRangeDa = request.getParameter("rangeabiutentetiposervizio_inizioRangeDa");
					String fineRangeA = request.getParameter("rangeabiutentetiposervizio_fineRangeA");
					String inizioRangePer = request.getParameter("rangeabiutentetiposervizio_inizioRangePer");
					String tipoRange = request.getParameter("rangeabiutentetiposervizio_tipoRange");
					String flagCin = request.getParameter("rangeabiutentetiposervizio_flagCin");
			       
					RangeAbiUtenteTipoServizio rangeAbiUtente = new RangeAbiUtenteTipoServizio(chiaveRangeTipoServizio,companyCode,codiceUtente,codiceTipologiaServizio,tipoRange,inizioRangeDa,fineRangeA,inizioRangePer,flagCin,usernameAutenticazione);
					/* we prepare object for save */
					RangeAbiUtenteTipoServizioSaveRequest saveRequest = new RangeAbiUtenteTipoServizioSaveRequest(rangeAbiUtente,codOp);
					/* we save object */
						
					try {
						StatusResponse out = WSCache.rangeAbiUtenteTipoServizioServer.save(saveRequest, request);
						if (out != null)
						{
							Response response = out.getResponse();
							if (response != null)
							{
								String updateRetCode = response.getReturncode().toString();
								if (updateRetCode.equals(ResponseReturncode.value1.getValue()))
									updateRetCode = "00";
								request.setAttribute("updateRetCode", updateRetCode);
								if(response.getReturncode().toString().equals(ResponseReturncode.value1.getValue()))
								{
									session.setAttribute("recordUpdated", "OK");
									request.setAttribute("vista",ritornaViewstate);
								}
								else setFormMessage("var_form", response.getReturnmessage(), request);
							}
							else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
						}
						else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
						
					} catch (FaultType e) {
						setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
						e.printStackTrace();
					} catch (RemoteException e) {
						setFormMessage("var_form", "Errore: " + e.getMessage(), request);
						e.printStackTrace();
					} finally {
						try {
							ConfigUtenteTipoServizioSearchResponse configUtenteTipoServizioSearchResponse = getConfigUtenteTipoServizioSearchResponse("","","", 0, 0, "", request);
							request.setAttribute("configutentetiposervizios", configUtenteTipoServizioSearchResponse.getResponse().getListXml());
						} catch (Exception e) {}
					}
				}
				else
					setFormMessage("var_form", esito, request);
				break;
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("vista",ritornaViewstate);
			break;

		}
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","rangeabiutentetiposervizio_edit.do");
		request.setAttribute("codop",codop);
		return null;
	}
	
	private ConfigUtenteTipoServizioSearchResponse getConfigUtenteTipoServizioSearchResponse(
			String companyCode, String codiceTipologiaServizio, String codiceUtente,  int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigUtenteTipoServizioSearchResponse res = null;
		ConfigUtenteTipoServizioSearchRequest in = new ConfigUtenteTipoServizioSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setCodiceUtente(codiceUtente== null ? "" : codiceUtente);
		
		res = WSCache.configUtenteTipoServizioServer.getConfigUtenteTipoServizios(in, request);
		return res;
	}

}
