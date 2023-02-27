package org.seda.payer.manager.adminusers.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.util.Messages;

import com.seda.j2ee5.maf.components.defender.bfl.BFLContext;
import com.seda.j2ee5.maf.components.defender.bfl.BFLUser;
import com.seda.j2ee5.maf.core.action.ActionException;

@SuppressWarnings("serial")
public class SeUsersSbloccaAction extends BaseAdminusersAction{
	String username = null;

	public Object service(HttpServletRequest request) throws ActionException {
		BFLContext bflContext = null;
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);

		switch(firedButton)
		{
		case TX_BUTTON_EDIT:
			/*
			 * Recupero lo username dell'utente da sbloccare
			 */
			username = (String)request.getAttribute("tx_username_hidden");
			if (username == null || username.equals(""))
			{
				request.setAttribute("confermaDisabilitata", true);
				setFormMessage("sblocca_form", Messages.UTENZA_NON_VALIDA.format(), request);
			}
			else
			{
				try {
					bflContext = BFLContext.getInstance();
					BFLUser bflUser = bflContext.show(username.trim());
					/*
					 * Controllo che lo user sia bloccato
					 */
					if (bflUser != null && bflUser.isLocked())
					{
						bflContext.remove(username);
						request.setAttribute("confermaDisabilitata", true);
						setFormMessage("sblocca_form", Messages.SBLOCCO_UTENTE_OK.format(username), request);
					}
					else
						setFormMessage("sblocca_form", Messages.UTENTE_NON_BLOCCATO.format(username), request);
				} catch (Exception e) {
					setFormMessage("sblocca_form", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				}
			}
			break;

		case TX_BUTTON_NULL:
			/*
			 * Recupero lo username dell'utente da cancellare
			 */
			username = (String)request.getAttribute("tx_username");
			if (username == null || username.equals(""))
			{
				request.setAttribute("confermaDisabilitata", true);
				setFormMessage("sblocca_form", Messages.UTENZA_NON_VALIDA.format(), request);
			}
			else
			{
				request.setAttribute("confermaDisabilitata", false);
				request.setAttribute("tx_username_hidden", username);
				setFormMessage("sblocca_form", Messages.SBLOCCA_O_ABBANDONA.format(username), request);
			}
			break;
			
			/*
			 * Torno al form di ricerca	
			 */
			case TX_BUTTON_INDIETRO:
				request.setAttribute("vista","seusers_search");
				break;
			
		}
		return null;
	}

}
