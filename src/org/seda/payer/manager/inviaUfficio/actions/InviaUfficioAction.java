package org.seda.payer.manager.inviaUfficio.actions;

import com.seda.commons.logger.CustomLoggerManager;
import com.seda.commons.logger.LoggerWrapper;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.core.bean.ComunicazioneUfficio;
import com.seda.payer.core.bean.ComunicazioneUfficioPageList;
import com.seda.payer.core.bean.PrenotazioneFatturazione;
import com.seda.payer.core.bean.PrenotazioneFatturazionePagelist;
import com.seda.payer.core.dao.InserisciUfficioDao;
import com.seda.payer.core.dao.PrenotazioneFatturazioneDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.sun.rowset.WebRowSetImpl;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.entrate.actions.GestioneDocumentiCaricoAction;
import org.seda.payer.manager.entrate.actions.util.EntrateFilteredWs;
import org.seda.payer.manager.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.WebRowSet;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;
import java.util.Objects;


public class InviaUfficioAction extends BaseInviaUfficioAction{

    private static final long serialVersionUID = 1L;
    private String codop="add";
    private String templateName = "";
    private String payerDbSchema;
    private DataSource payerDataSource;
    private int rowsPerPage;
    private int pageNumber;
    private String order;
    protected LoggerWrapper logger = CustomLoggerManager.get(getClass());
    private String screen="";

    //fine LP PG1800XX_016

    public Object service(HttpServletRequest request) throws PayerCommandException {

        HttpSession session = request.getSession();
        //inizio LP PG1800XX_016
        MAFRequest mafReq = new MAFRequest(request);
        String applicationName = (String) mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
        UserBean userBean = (UserBean) session.getAttribute(SignOnKeys.USER_BEAN);
        templateName = userBean.getTemplate(applicationName);
        boolean passatoCancella=false;


        aggiornamentoCombo(request, session);
        loadSocietaXml_DDL(request);
        loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
        LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), "", getParamCodiceEnte(), getParamCodiceUtente(), false);

        request.setAttribute("tx_error_message","");
        request.setAttribute("tx_message","");

        FiredButton firedButton = getFiredButton(request);

        if (screen==null || screen.isEmpty()) {
            screen = Screen.SEARCH; // default
        }

        if (screen.equals(GestioneDocumentiCaricoAction.Screen.SEARCH)) {
            boolean ok = true;
            onGestioneDocumentiScreen(request);
            if(screen.equals(Screen.DELETE)) {
                try {
                    onDeleteDocumentoScreen(request);
                }catch(Throwable e) {
                    ok=false;
                    logger.info(e.getMessage());
                }
                if(ok) {
                    setFormMessage("inviaufficioForm", "prenotazione cancellata", request);
                    try{
                        request.setAttribute("tx_button_cerca","tx_button_cerca");
                        logger.info("clicco cerca da codice");
                        passatoCancella=true;
                    }catch (Throwable e) {
                        e.printStackTrace();
                        setFormMessage("inviaufficioForm", "errore visualizzazione lista", request);
                    }
                }else{
                    setFormMessage("inviaufficioForm", "errore rimozione prenotazione", request);
                }
            }
        }
        /*
         * Eseguo le azioni
         */
        switch (firedButton) {
            case TX_BUTTON_RESET:
                request.setAttribute("statoElaborazione", null);
                request.setAttribute("dtFlusso_da", null);
                request.setAttribute("dtFlusso_a", null);
                request.setAttribute("codop","ritorna");
                break;

            case TX_BUTTON_CERCA:
                rowsPerPage = (request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
                pageNumber = (request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));
                order = request.getParameter("order") == null ? "" : request.getParameter("order");
                request.setAttribute("do_command_name", "inviaufficio.do");
                request.setAttribute("codop", "search");
                boolean okConfInd = false;
                try {
                    request.setAttribute("statoElaborazione", "");
                    request.setAttribute("dtFlusso_da", "");
                    request.setAttribute("dtFlusso_a", "");
                    okConfInd = getConfigurazioni(request);
                    logger.info("stato conf " + okConfInd);
                } catch (Throwable e) {
                    e.printStackTrace();
                    setFormMessage("inviaufficioForm", "errore visualizzazione lista", request);
                }
                if (okConfInd && session.getAttribute("aggiuntaPrenotazione") != null && (boolean) session.getAttribute("aggiuntaPrenotazione")) {
                    setFormMessage("inviaufficioForm", "prenotazione di elaborazione aggiunta correttamente", request);
                }

                if (session.getAttribute("aggiuntaPrenotazione") != null && (boolean) session.getAttribute("aggiuntaPrenotazione")) {
                    session.setAttribute("aggiuntaPrenotazione", false);
                }

                break;

            case TX_BUTTON_NUOVO:
                request.setAttribute("do_command_name","inviaufficioactionadd.do");
                request.setAttribute("codop",codop);
                break;

            case TX_BUTTON_NULL:
                if(!passatoCancella) {
                    request.setAttribute("codop", "ritorna");
                }else {
                    if (session.getAttribute("aggiuntaPrenotazione") != null && (boolean) session.getAttribute("aggiuntaPrenotazione")) {
                        rowsPerPage = (request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
                        pageNumber = (request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));
                        order = request.getParameter("order") == null ? "" : request.getParameter("order");
                        request.setAttribute("do_command_name", "inviaufficio.do");
                        request.setAttribute("codop", "search");
                        boolean okConfInd = false;
                        try {
                            request.setAttribute("statoElaborazione", "");
                            request.setAttribute("dtFlusso_da", "");
                            request.setAttribute("dtFlusso_a", "");
                            okConfInd = getConfigurazioni(request);
                            logger.info("stato conf " + okConfInd);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            setFormMessage("inviaufficioForm", "errore visualizzazione lista", request);
                        }
                        if (okConfInd && session.getAttribute("aggiuntaPrenotazione") != null && (boolean) session.getAttribute("aggiuntaPrenotazione")) {
                            setFormMessage("inviaufficioForm", "prenotazione di elaborazione aggiunta correttamente", request);
                        }

                        if (session.getAttribute("aggiuntaPrenotazione") != null && (boolean) session.getAttribute("aggiuntaPrenotazione")) {
                            session.setAttribute("aggiuntaPrenotazione", false);
                        }
                    } else {
                        request.setAttribute("vista", "");
                    }
                }
                break;

            case TX_BUTTON_INDIETRO:
                if(session.getAttribute("aggiuntaPrenotazione") !=null && (boolean)session.getAttribute("aggiuntaPrenotazione")){
                    rowsPerPage = (request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
                    pageNumber = (request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));
                    order = request.getParameter("order")  == null ? "" : request.getParameter("order");
                    request.setAttribute("do_command_name","inviaufficio.do");
                    request.setAttribute("codop","search");
                    boolean okConfInd = false;
                    try{
                        request.setAttribute("statoElaborazione","");
                        request.setAttribute("dtFlusso_da", "");
                        request.setAttribute("dtFlusso_a", "");
                        okConfInd = getConfigurazioni(request);
                    }catch (Throwable e) {
                        e.printStackTrace();
                        setFormMessage("inviaufficioForm", "errore visualizzazione lista", request);
                    }
                    if(okConfInd && session.getAttribute("aggiuntaPrenotazione")!=null && (boolean)session.getAttribute("aggiuntaPrenotazione")) {
                        setFormMessage("inviaufficioForm", "prenotazione di elaborazione aggiunta correttamente", request);
                    }

                    if(session.getAttribute("aggiuntaPrenotazione") !=null && (boolean)session.getAttribute("aggiuntaPrenotazione")){
                        session.setAttribute("aggiuntaPrenotazione",false);
                    }

                    break;
                }else {
                    request.setAttribute("vista", "");
                }
                break;
        }

        return null;
    }


    /** Mi trovo nello screen di CONFERMA CANCELAZIONE documento */
    private void onDeleteDocumentoScreen(HttpServletRequest request) {

        try {
            EntrateFilteredWs.loadListaProvince(request);

            if (isFiredButton(request, "button_elimina")) {
                onDeleteDocumentCommit(request);
            }
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }

    }

    private void onDeleteDocumentCommit(HttpServletRequest request) {
        try {
            deleteRow(request.getParameterMap(), request);
            screen=Screen.SEARCH;
        }catch(Throwable e){
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    private void onGestioneDocumentiScreen(HttpServletRequest request) {
        if (isFiredButton(request, "button_elimina")) {
            onDeleteDocumentRequest(request);
        }
    }

    private void onDeleteDocumentRequest(HttpServletRequest request) {
        screen = Screen.DELETE;
    }


    /** button cliccato direttamente o simulato da javascript su DDL changed */
    static boolean isFiredButton(HttpServletRequest request, String buttonName) {
        return request.getParameter(buttonName) != null
                || buttonName.equals(request.getParameter("fired_button_hidden"));
    }


    private void deleteRow(Map parameterMap,HttpServletRequest request) throws ActionException, SQLException {
        createConnection(request);
        deleteRowMethod(parameterMap,request);
    }

    private void deleteRowMethod(Map parameterMap, HttpServletRequest request) throws SQLException {

        Connection connection = payerDataSource.getConnection();
        PrenotazioneFatturazioneDao prenotazioneFatturazioneDao = new PrenotazioneFatturazioneDao(connection,payerDbSchema);
        prenotazioneFatturazioneDao.cancellaPrenotazione(parameterMap.get("chiave").toString());

    }

    private boolean getConfigurazioni(HttpServletRequest request) throws ActionException, ParseException, SQLException {
        Connection connection = createConnection(request);
        PrenotazioneFatturazionePagelist listaufficio = getConfigurations(request);
        gestisciLista(listaufficio,request);
        return true;
    }


    private Connection createConnection(HttpServletRequest request) throws ActionException {
        PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
        String dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
        String dataSourceName =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
        this.payerDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
        try {
            this.payerDataSource = ServiceLocator.getInstance().getDataSource("java:/".concat(dataSourceName));
            return payerDataSource.getConnection();
        } catch (ServiceLocatorException | SQLException e) {
            throw new ActionException("ServiceLocator error " + e.getMessage(),e);
        }
    }

    private void getConfigurations(Connection connection, HttpServletRequest request) throws ParseException {
        InserisciUfficioDao ufficioDao = new InserisciUfficioDao(connection,payerDbSchema);
        ComunicazioneUfficio getInput = creaOgettoRequest(request);
        ComunicazioneUfficioPageList listaufficio = ufficioDao.getParametriPrenotazione(getInput,request.getAttribute("flagstato").toString());

    }


    private PrenotazioneFatturazionePagelist getConfigurations(HttpServletRequest request) throws SQLException {
        PrenotazioneFatturazionePagelist prenotazioneFatturazionePagelist = null;
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
                    request.getAttribute("statoElaborazione").toString(),
                    getDataByPrefix("dtFlusso_da", request),
                    getDataByPrefix("dtFlusso_a", request),
                    "UFF"
            );
            prenotazioneFatturazionePagelist = dao.getPrenotazioneFatturazioneList(prenotazione);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        return prenotazioneFatturazionePagelist;
    }

    private void gestisciLista(PrenotazioneFatturazionePagelist listaufficio, HttpServletRequest request) {

        PageInfo pageInfo = listaufficio.getPageInfo();
        if(!Objects.equals(listaufficio.getRetCode(), "00")) {
            setFormMessage("inviaufficioForm", "Errore generico - Impossibile recuperare i dati", request);
            request.setAttribute("tx_error_message","lista vuota");
        }else {
            if (pageInfo != null) {
                if (pageInfo.getNumRows() > 0) {
                    String listaInputUfficio = elaboraXmlList(listaufficio.getPrenotazioneFatturazioneListXml(), request);
                    if(!listaInputUfficio.isEmpty()) {
                        request.setAttribute("listaInputUfficio", listaInputUfficio);
                        request.setAttribute("listaInputUfficio.pageInfo", pageInfo);
                    } else {
                        request.setAttribute("listaInputUfficio", null);
                        setFormMessage("inviaufficio", Messages.NO_DATA_FOUND.format(), request);
                    }
                } else {
                    request.setAttribute("listaInputUfficio", null);
                    setFormMessage("inviaufficio", Messages.NO_DATA_FOUND.format(), request);
                }
            } else {
                setFormMessage("inviaufficio", "Errore generico - Impossibile recuperare i dati", request);
                request.setAttribute("tx_error_message","lista vuota");
            }
        }

    }

    private String elaboraXmlList(String comunicazioneUfficioListXml, HttpServletRequest request) {
        WebRowSet rowSetNew = null;
        CachedRowSet crsListaOriginale = null;
        try {
            crsListaOriginale = Convert.stringToWebRowSet(comunicazioneUfficioListXml);
            ResultSetMetaData rsMdOriginale = crsListaOriginale.getMetaData();
            int iCols = rsMdOriginale.getColumnCount();

            RowSetMetaDataImpl rsMdNew = new RowSetMetaDataImpl();
            rsMdNew.setColumnCount(iCols);

            for (int i = 1; i <= iCols; i++) {
                rsMdNew.setColumnName(i, rsMdOriginale.getColumnName(i));
                rsMdNew.setColumnType(i, rsMdOriginale.getColumnType(i));
                rsMdNew.setColumnTypeName(i, rsMdOriginale.getColumnTypeName(i));
            }
            rowSetNew = new WebRowSetImpl();
            rowSetNew.setMetaData(rsMdNew);
            String[]date = new String[0];

            if (crsListaOriginale != null) {
                while (crsListaOriginale.next()) {
                    rowSetNew.moveToInsertRow();
                    // inserisco i valori delle vecchie colonne della riga attuale
                    for (int i=1; i<=iCols; i++) {
                        if(Objects.equals(i,5)) {
                            date = crsListaOriginale.getObject(i).toString().split("-");
                            String[]dataScad = date[0].split("/");
                            date[0] = dataScad[1]+"/"+dataScad[0]+"/"+dataScad[2];
                            rowSetNew.updateObject(8, date[0]);
                            dataScad = date[1].split("/");
                            date[1] = dataScad[1]+"/"+dataScad[0]+"/"+dataScad[2];
                            rowSetNew.updateObject(9, date[1]);
                        }
                        rowSetNew.updateObject(i, crsListaOriginale.getObject(i));
                    }

                    String stato = crsListaOriginale.getString(6);
                    rowSetNew.updateString(6, stato.equals("1") ? "Da elaborare" : "Elaborata");

                    rowSetNew.insertRow();

                }
            }

            rowSetNew.moveToCurrentRow();
            return Convert.webRowSetToString(rowSetNew);
        } catch (Exception e)  {
            logger.info(e.getMessage());
            setFormMessage("richiesteElaborazioniForm", e.getMessage() , request);
            request.setAttribute("tx_error_message","errore generico");
            e.printStackTrace();
        } finally {
            try {
                if(crsListaOriginale != null)  crsListaOriginale.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
                setFormMessage("richiesteElaborazioniForm", e.getMessage() , request);
                request.setAttribute("tx_error_message","errore generico");
                e.printStackTrace();
            }
            try {
                if(rowSetNew != null)  rowSetNew.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
                setFormMessage("richiesteElaborazioniForm", e.getMessage() , request);
                request.setAttribute("tx_error_message","errore generico");
                e.printStackTrace();
            }
        }
        return "";
    }

    private ComunicazioneUfficio creaOgettoRequest(HttpServletRequest request) throws ParseException {

        ComunicazioneUfficio getInput = new ComunicazioneUfficio();

        int rowsPerPage = (request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
        int pageNumber = (request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));
        String order = request.getParameter("order")  == null ? "" : request.getParameter("order");

        getInput.setRowsPerPage(rowsPerPage);
        getInput.setPageNumber(pageNumber);
        getInput.setOrder(order);
        getInput.setDataesecuzione(getDataByPrefix("tx_data_da",request));

        return getInput;

    }

    static public class Screen {
        static public final String DELETE = "delete";
        static public final String SEARCH = "search";

        /** ritorna una costante utilizzabile con == */
        static public final String fromString(String s) {
            if (s == null)
                return null;

            for (String screen : new String[] {DELETE,SEARCH}) {
                if (screen.equals(s)) {
                    return screen;
                }
            }
            throw new IllegalArgumentException("screen=" + s);
        }
    };


}
