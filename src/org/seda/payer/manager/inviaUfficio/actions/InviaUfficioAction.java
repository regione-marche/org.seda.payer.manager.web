package org.seda.payer.manager.inviaUfficio.actions;

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
import com.sun.rowset.WebRowSetImpl;
import org.seda.payer.manager.components.security.UserBean;
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

    //fine LP PG1800XX_016

    public Object service(HttpServletRequest request) throws PayerCommandException {

        HttpSession session = request.getSession();
        //inizio LP PG1800XX_016
        MAFRequest mafReq = new MAFRequest(request);
        String applicationName = (String) mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
        UserBean userBean = (UserBean) session.getAttribute(SignOnKeys.USER_BEAN);
        templateName = userBean.getTemplate(applicationName);

        aggiornamentoCombo(request, session);
        loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
        LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), "", getParamCodiceEnte(), getParamCodiceUtente(), false);

        request.setAttribute("tx_error_message","");
        request.setAttribute("tx_message","");

        FiredButton firedButton = getFiredButton(request);
        /*
         * Eseguo le azioni
         */
        switch (firedButton) {
            case TX_BUTTON_RESET:
                resetParametri(request);
                break;

            case TX_BUTTON_CERCA:
                rowsPerPage = (request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
                pageNumber = (request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));
                order = request.getParameter("order")  == null ? "" : request.getParameter("order");
                request.setAttribute("do_command_name","inviaufficio.do");
                request.setAttribute("codop","search");
                try{
                    getConfigurazioni(request);
                }catch (Throwable e) {
                    e.printStackTrace();
                }
                break;

            case TX_BUTTON_NUOVO:
                request.setAttribute("do_command_name","inviaufficioactionadd.do");
                request.setAttribute("codop",codop);
                break;

            case TX_BUTTON_NULL:
                request.setAttribute("codop","ritorna");
                break;

            case TX_BUTTON_INDIETRO:
                request.setAttribute("vista", "");
                break;
        }

        return null;
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
                        setFormMessage("inviaufficioForm", Messages.NO_DATA_FOUND.format(), request);
                    }
                } else {
                    request.setAttribute("listaInputUfficio", null);
                    setFormMessage("inviaufficioForm", Messages.NO_DATA_FOUND.format(), request);
                }
            } else {
                setFormMessage("inviaufficioForm", "Errore generico - Impossibile recuperare i dati", request);
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
                            rowSetNew.updateObject(8, date[0]);
                            rowSetNew.updateObject(9, date[1]);
                        }
                        rowSetNew.updateObject(i, crsListaOriginale.getObject(i));
                    }

                    String stato = crsListaOriginale.getString(6);
                    rowSetNew.updateString(6, stato.equals("1") ? "Da elaborare" : "Terminata");
                    rowSetNew.insertRow();

                }
            }

            rowSetNew.moveToCurrentRow();
            return Convert.webRowSetToString(rowSetNew);
        } catch (Exception e)  {
            setFormMessage("richiesteElaborazioniForm", e.getMessage() , request);
            request.setAttribute("tx_error_message","errore generico");
            e.printStackTrace();
        } finally {
            try {
                if(crsListaOriginale != null)  crsListaOriginale.close();
            } catch (SQLException e) {
                setFormMessage("richiesteElaborazioniForm", e.getMessage() , request);
                request.setAttribute("tx_error_message","errore generico");
                e.printStackTrace();
            }
            try {
                if(rowSetNew != null)  rowSetNew.close();
            } catch (SQLException e) {
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

}
