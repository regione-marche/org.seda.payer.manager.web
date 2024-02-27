package org.seda.payer.manager.richiesteelaborazioni.actions;

import com.seda.j2ee5.maf.core.action.ActionException;
import org.seda.payer.manager.actions.BaseManagerAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RichiesteElaborazioniAction  extends BaseManagerAction {
    private static final long serialVersionUID = 1L;

    public Object service(HttpServletRequest request) throws ActionException {
        HttpSession session = request.getSession();

        String nomeForm = "";
        if (request.getAttribute("form") != null)
            nomeForm = (String)request.getAttribute("form");
        try {
            // TODO
        }
        catch (Exception e) {
            e.printStackTrace();
            setFormMessage(nomeForm, e.getMessage(), request);
        }
        return null;
    }

}
