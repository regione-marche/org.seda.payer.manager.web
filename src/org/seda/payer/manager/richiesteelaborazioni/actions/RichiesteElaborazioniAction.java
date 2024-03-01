package org.seda.payer.manager.richiesteelaborazioni.actions;

import com.seda.j2ee5.maf.core.action.ActionException;
import org.seda.payer.manager.riconciliazionenn.actions.BaseRiconciliazioneNodoAction;
import org.seda.payer.manager.util.Field;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RichiesteElaborazioniAction  extends BaseRiconciliazioneNodoAction {
    private static final long serialVersionUID = 1L;

    public Object service(HttpServletRequest request) throws ActionException {
        HttpSession session = request.getSession();
        tx_SalvaStato(request);
        super.service(request);
        aggiornamentoCombo(request, session);
        loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
        LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), "", getParamCodiceEnte(), getParamCodiceUtente(), false);

        switch(getFiredButton(request)) {

            case TX_BUTTON_CERCA: {
                // TODO
            } break;

            case TX_BUTTON_NULL: {
                impostaFiltri(request, session);
            } break;
        }

        return null;
    }

    private void impostaFiltri(HttpServletRequest request, HttpSession session) {
        session.setAttribute(Field.TX_SOCIETA.format(), request.getAttribute(Field.TX_SOCIETA.format()));
        session.setAttribute(Field.TX_PROVINCIA.format(), request.getAttribute(Field.TX_PROVINCIA.format()));
        session.setAttribute("tx_UtenteEnte", request.getAttribute("tx_UtenteEnte"));
        session.setAttribute(Field.DT_FLUSSO_DA_DAY.format(), request.getAttribute(Field.DT_FLUSSO_DA_DAY.format()));
        session.setAttribute(Field.DT_FLUSSO_DA_MONTH.format(), request.getAttribute(Field.DT_FLUSSO_DA_MONTH.format()));
        session.setAttribute(Field.DT_FLUSSO_DA_YEAR.format(), request.getAttribute(Field.DT_FLUSSO_DA_YEAR.format()));
        session.setAttribute(Field.DT_FLUSSO_A_DAY.format(), request.getAttribute(Field.DT_FLUSSO_A_DAY.format()));
        session.setAttribute(Field.DT_FLUSSO_A_MONTH.format(), request.getAttribute(Field.DT_FLUSSO_A_MONTH.format()));
        session.setAttribute(Field.DT_FLUSSO_A_YEAR.format(), request.getAttribute(Field.DT_FLUSSO_A_YEAR.format()));
    }
}
