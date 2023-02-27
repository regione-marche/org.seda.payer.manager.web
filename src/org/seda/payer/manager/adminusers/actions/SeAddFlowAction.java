package org.seda.payer.manager.adminusers.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class SeAddFlowAction extends BaseManagerAction implements FlowManager{
	private FiredButton firedButton;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		String step = (String)request.getAttribute("Step");
		String tipologiaUtente = request.getAttribute("tipologiaUtente") == null ? (String)request.getAttribute("tx_tipologiautente_hidden") : (String)request.getAttribute("tipologiaUtente");
		
		switch(firedButton)
		{
			case TX_BUTTON_NUOVO:
			case TX_BUTTON_PASSWORD_AUTOGENERATA_CHANGED:
			case TX_BUTTON_SCADENZA_CHANGED:
			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL:
				
				if (step != null)
				{
					if (step.equals("seuseradd"))
						result = "add";
					else if (step.equals("personafisica")) 
						result = "pfisica";
					else if (step.equals("personagiuridica"))
						result = "pgiuridica";
				}
				else
					result = "add";
				
				break;
			case TX_BUTTON_AVANTI:
				String esito = ((String)request.getAttribute("inputFieldChecked"));
				
				if ((esito != null && esito.equals("OK"))) {
					if (request.getAttribute("tipologiaUtente").equals("F")) 
						result =  "pfisica";
					else						
						result =  "pgiuridica";
					
					if (request.getAttribute("Stepold").equals("personagiuridica"))
						result = "pfisica";
				}
				else 
					if (step.equals("personagiuridica"))
						result = "pgiuridica";
					else
						result = "add";
				
				break;
			case TX_BUTTON_STEP1:
				if (request.getAttribute("tipologiaUtente").equals("F"))
					result =  "add";
				else 
					if (request.getAttribute("Stepold").equals("personafisica"))
						result =  "pgiuridica";
					else
						result="add";
				break;
			case TX_BUTTON_AGGIUNGI: 
				String addUserRetCode = (String) request.getAttribute("addUserRetCode");
				if (addUserRetCode != null && addUserRetCode.equals("00"))
				{
					result="ritorna";
					request.setAttribute("vista","seusers_search");
				}
				else
				{
					if (step != null)
						result = "pfisica";
					else 
						return "add";
				}
				break;
				
			case TX_BUTTON_CERCA:
				result = "ritorna";
				break;
			case TX_BUTTON_ComuneNascitaEstero:
			case TX_BUTTON_ProvinciaNascita:
			case TX_BUTTON_ProvinciaResidenza:
			case TX_BUTTON_ComuneResidenzaEstero:
			case TX_BUTTON_ProvinciaDomicilio:
			case TX_BUTTON_ComuneDomicilioEstero:
			case TX_BUTTON_ImpostaSoggiorno:
				result =  "pfisica";
				break;
			case TX_BUTTON_ProvinciaSedeLegale:
			case TX_BUTTON_ComuneSedeLegaleEstero:
			case TX_BUTTON_FamigliaMerceologica:
			case TX_BUTTON_TipologiaAlloggio:	
				if (step.equals("personafisica")) 
					result =  "pfisica";
				else						
					result =  "pgiuridica";
				break;
			case TX_BUTTON_ProvinciaSedeOperativa:	
				result =  "pgiuridica";
				break;
			case TX_BUTTON_TIPOLOGIA_CHANGED:
				result = "add";
				break;
		}
		return result;
	}

	public void start(HttpServletRequest request) {
		/*
		 * Individuo la richiesta dell'utente
		 */
		firedButton = getFiredButton(request);
	}

}
