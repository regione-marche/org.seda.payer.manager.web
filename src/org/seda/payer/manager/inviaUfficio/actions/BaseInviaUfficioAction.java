package org.seda.payer.manager.inviaUfficio.actions;

import com.seda.j2ee5.maf.core.action.ActionException;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.PayerCommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;


public class BaseInviaUfficioAction extends BaseManagerAction {

    protected String codiceSocieta="";
    protected String codiceProvincia="";
    protected String codiceUtente="";
    protected String chiaveEnte="";
    protected String payerDbSchema = null;
    protected DataSource payerDataSource = null;

    public Object service(HttpServletRequest request) throws PayerCommandException, ActionException {
        super.service(request);
        setProfile(request);
        HttpSession session = request.getSession();
        loadDDLStatic(request, session);
        return null;
    }

    protected void tx_SalvaStato(HttpServletRequest request) {
        super.tx_SalvaStato(request);
    }

    protected void aggiornamentoCombo(HttpServletRequest request, HttpSession session) {

        switch(getFiredButton(request)) {
            case TX_BUTTON_SOCIETA_CHANGED:
                loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
                loadDDLUtente(request, session, getParamCodiceSocieta(), null, true);
                loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), null, true);
                break;

            case TX_BUTTON_PROVINCIA_CHANGED:
                loadProvinciaXml_DDL(request, session, null,false);
                loadDDLUtente(request, session, getParamCodiceSocieta(), codiceProvincia,true);
                loadListaGatewayXml_DDL(request, session, null, null, false);
                break;

            case TX_BUTTON_UTENTE_CHANGED:
                loadProvinciaXml_DDL(request, session, null,false);
                loadDDLUtente(request, session, null, null,false);
                loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
                break;

            case TX_BUTTON_RESET:
                resetParametri(request);

                setParamCodiceSocieta("");
                setParamCodiceUtente("");
                setParamCodiceEnte("");
                loadProvinciaXml_DDL(request, session, "",true);
                loadDDLUtente(request, session, "", "",true);
                loadListaGatewayXml_DDL(request, session, "", "", true);
                break;

            default:
                loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
                loadDDLUtente(request, session, getParamCodiceSocieta(), codiceProvincia,true);
                loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
        }
    }

}
