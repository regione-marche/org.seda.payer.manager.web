package org.seda.payer.manager.adminusers.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class SeEditFlowAction extends BaseManagerAction implements FlowManager{
	private FiredButton firedButton;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		String step = (String)request.getAttribute("Step");
		String tipologiaUtente = request.getAttribute("tipologiaUtente") == null ? (String)request.getAttribute("tx_tipologiautente_hidden") : (String)request.getAttribute("tipologiaUtente");
		
		switch(firedButton)
		{
			case TX_BUTTON_AVANTI:
				String esito = (String)request.getAttribute("inputFieldChecked");
				
				if (esito != null && esito.equals("OK")) {
					if (tipologiaUtente.equals("F")) 
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
						result = "edit";
				
				break;
		
			case TX_BUTTON_AGGIUNGI:
				String editUserRetCode = (String) request.getAttribute("editUserRetCode");
				if (editUserRetCode != null && editUserRetCode.equals("00"))
				{
					result="ritorna";
					request.setAttribute("vista","seusers_search");
				}
				else
					if (request.getAttribute("flagLepida") != null && request.getAttribute("flagLepida").equals(true))
						result = "edit";
					else
						result =  "pfisica";
				break;

			case TX_BUTTON_SCADENZA_CHANGED:
			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL:
				if (step!= null) {
					if (step.equals("seuseredit"))
						result = "edit";
					else if (step.equals("personafisica")) 
							result = "pfisica";
					else if (step.equals("personagiuridica"))
							result = "pgiuridica";
				} else
					result = "edit";
				
				break;

			case TX_BUTTON_CERCA:
				result = "ritorna";
				break;
			case TX_BUTTON_STEP1:
				if (tipologiaUtente.equals("F"))
					result =  "edit";
				else 
					if (request.getAttribute("Stepold").equals("personafisica"))
						result =  "pgiuridica";
					else
						result="edit";
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
			case TX_BUTTON_ProvinciaSedeOperativa:
				result =  "pgiuridica";
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
				
			default:
				result =  "edit";
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
