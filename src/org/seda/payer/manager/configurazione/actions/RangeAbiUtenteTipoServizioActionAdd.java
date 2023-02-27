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
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioSaveRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.Response;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.StatusResponse;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizioente.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.srv.FaultType;

/**
 * @author lmontesi
 *
 */
@SuppressWarnings("serial")
public class RangeAbiUtenteTipoServizioActionAdd extends BaseManagerAction  {

	private static String codop = "add";
	private static String ritornaViewstate = "Rangeabiutentetiposervizio_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
	private String strCodiceSocieta,strCodiceUtente;
	
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Eseguo le azioni
		 */
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
			resetParametri(request);
			break;
			
		case TX_BUTTON_NULL:
			break;
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("vista",ritornaViewstate);
			break;

		case TX_BUTTON_AGGIUNGI:	
			/*
			 * Recupero i parametri dalla form
			 */
			parametri = getRangeAbiUtenteTipoServizioParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			String esito = checkInputRangeAbiUtenteServizio(parametri);
			/*
			 * Se OK effettuo il salvataggio
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
							String insertRetCode = response.getReturncode().toString();
							if (insertRetCode.equals(ResponseReturncode.value1.getValue()))
								insertRetCode = "00";
							request.setAttribute("insertRetCode", insertRetCode);
							if(response.getReturncode().toString().equals(ResponseReturncode.value1.getValue()))
							{
								session.setAttribute("recordInserted", "OK");
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
				}
			}
			else
				setFormMessage("var_form", esito, request);
			break;
		
		}
		
		if (!firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
			add(request);

		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","rangeabiutentetiposervizio_add.do");
		request.setAttribute("codop",codop);
		return null;
		
	}
	
	public Object add(HttpServletRequest request) throws ActionException {
		try {
			/* we retry all configutentetiposervizio */ 
			String codiceSocieta = (strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:"";
			String codiceUtente = (strCodiceUtente!=null && strCodiceUtente.length()>0)?strCodiceUtente:"";
			//ConfigUtenteTipoServizioImplementationStub stubConfigUtenteTipoServizio = new ConfigUtenteTipoServizioImplementationStub(new URL(getConfigutentetiposervizioManagerEndpointUrl()), null);
			ConfigUtenteTipoServizioSearchResponse configUtenteTipoServizioSearchResponse = getConfigUtenteTipoServizioSearchResponse(codiceSocieta,codiceUtente,"", 0, 0, "", request);
			request.setAttribute("configutentetiposervizios", configUtenteTipoServizioSearchResponse.getResponse().getListXml());
		} catch (Exception e) {
			e.printStackTrace(); 
		}
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
