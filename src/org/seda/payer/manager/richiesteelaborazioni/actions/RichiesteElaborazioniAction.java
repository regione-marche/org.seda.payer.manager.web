package org.seda.payer.manager.richiesteelaborazioni.actions;

import com.seda.commons.string.Convert;
import com.seda.data.dao.DAOHelper;
import com.seda.data.helper.Helper;
import com.seda.data.helper.HelperException;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.PrenotazioneFatturazionePagelist;
import com.sun.rowset.WebRowSetImpl;
import org.seda.payer.manager.riconciliazionenn.actions.BaseRiconciliazioneNodoAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;

public class RichiesteElaborazioniAction  extends BaseRiconciliazioneNodoAction {
    private static final long serialVersionUID = 1L;
    private int rowsPerPage;
    private int pageNumber;
    WebRowSetImpl webRowSetImpl = null;

    public Object service(HttpServletRequest request) throws ActionException {
        HttpSession session = request.getSession();
        tx_SalvaStato(request);
        super.service(request);
        aggiornamentoCombo(request, session);
        loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(), false);
        LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), "", getParamCodiceEnte(), getParamCodiceUtente(), false);

        rowsPerPage = ((String) request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
        pageNumber = ((String) request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));

        switch (getFiredButton(request)) {
            case TX_BUTTON_CERCA_PRENOTAZIONE: {
                salvaFiltri(request, session);

                try {
                    PrenotazioneFatturazionePagelist prenotazioneFatturazionePagelist = getPrenotazioneFatturazioneList(request);
                    PageInfo pageInfo = prenotazioneFatturazionePagelist.getPageInfo();
                    if (prenotazioneFatturazionePagelist.getRetCode() != "00") {
                        setFormMessage("richiesteElaborazioniForm", "Errore generico - Impossibile recuperare i dati", request);
                    } else {
                        if (pageInfo != null) {
                            if (pageInfo.getNumRows() > 0) {
                                request.setAttribute("listaPrenotazioni", prenotazioneFatturazionePagelist.getPrenotazioneFatturazioneListXml());
                                request.setAttribute("listaPrenotazioni.pageInfo", pageInfo);
                            } else {
                                request.setAttribute("listaPrenotazioni", null);

                                setFormMessage("richiesteElaborazioniForm", Messages.NO_DATA_FOUND.format(), request);
                            }
                        } else {
                            setFormMessage("richiesteElaborazioniForm", "Errore generico - Impossibile recuperare i dati", request);
                        }
                    }
                } catch (Exception e) {
                    setErrorMessage(e.getMessage());
                    e.printStackTrace();
                }
            }
            break;
            case TX_BUTTON_NULL: {
                salvaFiltri(request, session);
            }
            break;
        }

        return null;
    }

    private void salvaFiltri(HttpServletRequest request, HttpSession session) {
        try {
            System.out.println("Salvataggio filtri...");
            session.setAttribute(Field.TX_SOCIETA.format(), request.getAttribute(Field.TX_SOCIETA.format()));
            session.setAttribute(Field.TX_PROVINCIA.format(), request.getAttribute(Field.TX_PROVINCIA.format()));
            session.setAttribute("tx_UtenteEnte", request.getAttribute("tx_UtenteEnte"));
            session.setAttribute(Field.DT_PERIODO_PRENOTAZIONE_DA_DAY.format(), request.getAttribute(Field.DT_PERIODO_PRENOTAZIONE_DA_DAY.format()));
            session.setAttribute(Field.DT_PERIODO_PRENOTAZIONE_DA_MONTH.format(), request.getAttribute(Field.DT_PERIODO_PRENOTAZIONE_DA_MONTH.format()));
            session.setAttribute(Field.DT_PERIODO_PRENOTAZIONE_DA_YEAR.format(), request.getAttribute(Field.DT_PERIODO_PRENOTAZIONE_DA_YEAR.format()));
            session.setAttribute(Field.DT_PERIODO_PRENOTAZIONE_A_DAY.format(), request.getAttribute(Field.DT_PERIODO_PRENOTAZIONE_A_DAY.format()));
            session.setAttribute(Field.DT_PERIODO_PRENOTAZIONE_A_MONTH.format(), request.getAttribute(Field.DT_PERIODO_PRENOTAZIONE_A_MONTH.format()));
            session.setAttribute(Field.DT_DATA_RICHIESTA_DA_DAY.format(), request.getAttribute(Field.DT_DATA_RICHIESTA_DA_DAY.format()));
            session.setAttribute(Field.DT_DATA_RICHIESTA_DA_MONTH.format(), request.getAttribute(Field.DT_DATA_RICHIESTA_DA_MONTH.format()));
            session.setAttribute(Field.DT_DATA_RICHIESTA_DA_YEAR.format(), request.getAttribute(Field.DT_DATA_RICHIESTA_DA_YEAR.format()));
            session.setAttribute(Field.DT_DATA_RICHIESTA_A_DAY.format(), request.getAttribute(Field.DT_DATA_RICHIESTA_A_DAY.format()));
            session.setAttribute(Field.DT_DATA_RICHIESTA_A_MONTH.format(), request.getAttribute(Field.DT_DATA_RICHIESTA_A_MONTH.format()));
            session.setAttribute(Field.DT_DATA_RICHIESTA_A_YEAR.format(), request.getAttribute(Field.DT_DATA_RICHIESTA_A_YEAR.format()));
            session.setAttribute(Field.DT_FLAG_FATTURAZIONE.format(), request.getAttribute(Field.DT_FLAG_FATTURAZIONE.format()));
            session.setAttribute(Field.DT_FLAG_FATTURAZIONE.format(), request.getAttribute(Field.DT_FLAG_FATTURAZIONE.format()));
        } catch (Exception e) {
            System.out.println("Errore salvataggio filtri : ");
            e.getMessage();
        }
    }

    private PrenotazioneFatturazionePagelist getPrenotazioneFatturazioneList(HttpServletRequest request) {
        CallableStatement callableStatement = null;
        Connection connection = null;
        ResultSet data = null;
        PageInfo pageInfo = null;
        PrenotazioneFatturazionePagelist prenotazionePagelist = null;
        String[] prenotazioneList = new String[2];

        try {
            connection = payerDataSource.getConnection();
            callableStatement = Helper.prepareCall(connection, payerDbSchema, "PYPRESP_LST");

            callableStatement.setInt(1, pageNumber);
            callableStatement.setInt(2, rowsPerPage);
            callableStatement.setString(3, "");

//            callableStatement.setString(4, getParamCodiceSocieta() != null ? getParamCodiceSocieta() : "");
//            callableStatement.setString(5, getParamCodiceUtente() != null ? getParamCodiceUtente() : "");
//            callableStatement.setString(6, getParamCodiceEnte() != null ? getParamCodiceEnte() : "");
            callableStatement.setString(4, "");
            callableStatement.setString(5, "");
            callableStatement.setString(6, "");
            callableStatement.setString(7, getDataByPrefix("dtPeriodo_da", request) != null ? getDataByPrefix("dtPeriodo_da", request) : "");
            callableStatement.setString(8, getDataByPrefix("dtPeriodo_a", request) != null ? getDataByPrefix("dtPeriodo_a", request) : "");
            callableStatement.setString(9, request.getParameter(Field.DT_FLAG_FATTURAZIONE.format()));
            callableStatement.setString(10, getDataByPrefix("dtFlusso_da", request) != null ? getDataByPrefix("dtFlusso_da", request) : "");
            callableStatement.setString(11, getDataByPrefix("dtFlusso_a", request) != null ? getDataByPrefix("dtFlusso_a", request) : "");

            callableStatement.registerOutParameter(12, Types.VARCHAR);
            callableStatement.registerOutParameter(13, Types.INTEGER);
            callableStatement.registerOutParameter(14, Types.INTEGER);
            callableStatement.registerOutParameter(15, Types.INTEGER);
            callableStatement.registerOutParameter(16, Types.SMALLINT);

            if (callableStatement.execute()) {
                pageInfo = new PageInfo();
                pageInfo.setPageNumber(pageNumber);
                pageInfo.setRowsPerPage(rowsPerPage);
                pageInfo.setFirstRow(callableStatement.getInt(13));
                pageInfo.setLastRow(callableStatement.getInt(14));
                pageInfo.setNumRows(callableStatement.getInt(15));
                pageInfo.setNumPages(callableStatement.getInt(16));
                data = callableStatement.getResultSet();
                loadWebRowSet(data);
                prenotazioneList[0] = Convert.webRowSetToString(webRowSetImpl);

                if (callableStatement.getMoreResults()) {
                    if (data != null) {
                        try {
                            data.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    data = callableStatement.getResultSet();
                    loadWebRowSet(data);
                    prenotazioneList[1] = Convert.webRowSetToString(webRowSetImpl);
                }
            }
            prenotazionePagelist = new PrenotazioneFatturazionePagelist(pageInfo, "00", "", prenotazioneList);
            return prenotazionePagelist;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (HelperException e) {
            throw new RuntimeException(e);
        }
        return prenotazionePagelist;
    }

    private void loadWebRowSet(ResultSet resultSet) throws SQLException {
        // if rs is null means that there are no more results
        if (resultSet == null) {
            throw new SQLException(com.seda.data.helper.Messages.ARGUMENT_NULL.format("ResultSet"));
        }
        try {
            // define a new cached rowset
            webRowSetImpl = new WebRowSetImpl();
            // fill data
            webRowSetImpl.populate(resultSet);
        } finally {
            DAOHelper.closeIgnoringException(resultSet);
        }
    }

}