package org.seda.payer.manager.inviaUfficio.actions;

import com.seda.commons.logger.CustomLoggerManager;
import com.seda.commons.logger.LoggerWrapper;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.core.bean.ComunicazioneUfficio;
import com.seda.payer.core.bean.PrenotazioneFatturazione;
import com.seda.payer.core.bean.PrenotazioneFatturazionePagelist;
import com.seda.payer.core.dao.InserisciUfficioDao;
import com.seda.payer.core.dao.PrenotazioneFatturazioneDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PayerCommandException;
import org.seda.payer.manager.util.PropertiesPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.seda.data.dao.ModelObjectFactory.createObject;

public class InviaUfficioActionAdd extends BaseManagerAction {

    private static final Logger log = LoggerFactory.getLogger(InviaUfficioActionAdd.class);
    private String templateName="";
    private String codop="";
    private String payerDbSchema;
    private DataSource payerDataSource;
    protected LoggerWrapper logger = CustomLoggerManager.get(getClass());
    private UserBean userBean;
    private int rowsPerPage;
    private int pageNumber;
    private String order;

    public Object service(HttpServletRequest request) throws PayerCommandException, ActionException {

        HttpSession session = request.getSession();
        MAFRequest mafReq = new MAFRequest(request);
        String applicationName = (String) mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
        userBean = (UserBean) session.getAttribute(SignOnKeys.USER_BEAN);
        templateName = userBean.getTemplate(applicationName);

        FiredButton firedButton = getFiredButton(request);

        gestisciAzioni(firedButton,request);

        request.setAttribute("tx_error_message","");

        request.setAttribute("tx_message","");

        return null;
    }

    private void gestisciAzioni(FiredButton firedButton,HttpServletRequest request) {

        switch (firedButton) {
            case TX_BUTTON_RESET:
                resetParametri(request);
                break;

            case TX_BUTTON_AGGIUNGI:
                rowsPerPage = (request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
                pageNumber = (request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));
                order = request.getParameter("order")  == null ? "" : request.getParameter("order");
                try {
                    aggiungiConfigurazione(request);
                }catch(Throwable e){
                    logger.info(e.getMessage());
                    logger.error(e.toString());
                    e.printStackTrace();
                    request.setAttribute("tx_error_message","errore aggiunta configurazione");
                }
                request.setAttribute("tx_message","configurazione aggiunta correttamente");
                break;

            case TX_BUTTON_NULL:
                request.setAttribute("codop","ritorna");
                break;
        }
    }

    private boolean aggiungiConfigurazione(HttpServletRequest request) throws Exception {
        createConnection(request);
        addConfiguration(request);
        return true;
    }

    private Connection createConnection(HttpServletRequest request) throws ActionException, SQLException {

        PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
        String dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
        String dataSourceName =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
        this.payerDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
        try {
            this.payerDataSource = ServiceLocator.getInstance().getDataSource("java:/".concat(dataSourceName));
            return payerDataSource.getConnection();
        } catch (ServiceLocatorException e) {
            throw new ActionException("ServiceLocator error " + e.getMessage(),e);
        }
    }

    private void addConfiguration(Connection connection,HttpServletRequest request) {

        InserisciUfficioDao ufficioDao = new InserisciUfficioDao(connection,payerDbSchema);
        ComunicazioneUfficio inputBatch = createObjectUfficio(request);
        ufficioDao.insertParametriUfficio(inputBatch);
    }

    private EsitoRisposte addConfiguration(HttpServletRequest request) throws Exception {

        if((getDataByPrefix("dtFlusso_da", request)==null || getDataByPrefix("dtFlusso_da", request).isEmpty()) ||
                (getDataByPrefix("dtFlusso_a", request)==null || getDataByPrefix("dtFlusso_a", request).isEmpty())) {
            request.setAttribute("tx_error_message","valorizzare entrambe le date");
            throw new Exception();
        }

        try {
            Connection connection =  payerDataSource.getConnection();
            PrenotazioneFatturazioneDao dao = new PrenotazioneFatturazioneDao(connection, payerDbSchema);
            PrenotazioneFatturazione prenotazione = new PrenotazioneFatturazione(
                    rowsPerPage,
                    pageNumber,
                    order,
                    getParamCodiceSocieta(),
                    getParamCodiceUtente(),
                    getParamCodiceEnte(),
                    "",
                    "",
                    "1",
                    getDataByPrefix("dtFlusso_da", request),
                    getDataByPrefix("dtFlusso_a", request),
                    (String) request.getAttribute("flagSubentro")
            );
           return dao.inserisciPrenotazione(prenotazione,userBean.getUserName());
        } catch (DaoException e) {
            request.setAttribute("tx_error_message","errore inserimento nel database");
            throw new RuntimeException(e);
        }
    }

    private ComunicazioneUfficio createObjectUfficio(HttpServletRequest request) {
        ComunicazioneUfficio inputBatch = new ComunicazioneUfficio();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.ITALIAN);


        String dataScadenza = getDataByPrefix("dtFlusso_da",request);
        String dataConferma = getDataByPrefix("dtFlusso_a",request);

        LocalDate dateScadenza = LocalDate.parse(dataScadenza, formatter);
        LocalDate dateConferma = LocalDate.parse(dataConferma, formatter);

        inputBatch.setDataScadenza(dateScadenza);
        inputBatch.setDataConferma(dateConferma);
        inputBatch.setDataesecuzione("");
        inputBatch.setStato("Da Elaborare");
        inputBatch.setTipoRichiesta((String) request.getAttribute("flagSubentro"));
        inputBatch.setOperatore(userBean.getUserName());

        return inputBatch;

    }

}



