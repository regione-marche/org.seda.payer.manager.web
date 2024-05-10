package org.seda.payer.manager.richiesteelaborazioni.actions;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.PrenotazioneFatturazione;
import com.seda.payer.core.bean.PrenotazioneFatturazionePagelist;
import com.seda.payer.core.dao.PrenotazioneFatturazioneDao;
import com.seda.payer.core.exception.DaoException;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;

public class RichiesteElaborazioniAction extends BaseFatturazioneAction {
    private static final long serialVersionUID = 1L;
    private int rowsPerPage;
    private int pageNumber;
    private String order;

    public Object service(HttpServletRequest request) throws ActionException {
        super.service(request);
        HttpSession session = request.getSession();
        replyAttributes(false, request,"pageNumber","rowsPerPage","order");

        aggiornamentoCombo(request, session);
        loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
        LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), "", getParamCodiceEnte(), getParamCodiceUtente(), false);

        switch (getFiredButton(request)) {
            case TX_BUTTON_CERCA: {
                rowsPerPage = (request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
                pageNumber = (request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));
                order = request.getParameter("order")  == null ? "" : request.getParameter("order");

                try {
                    PrenotazioneFatturazionePagelist prenotazioneFatturazionePagelist = getPrenotazioneFatturazioneList(request);
                    PageInfo pageInfo = prenotazioneFatturazionePagelist.getPageInfo();
                    if (prenotazioneFatturazionePagelist.getRetCode() != "00") {
                        setFormMessage("richiesteElaborazioniForm", "Errore generico - Impossibile recuperare i dati", request);
                    } else {
                        if (pageInfo != null) {
                            if (pageInfo.getNumRows() > 0) {
                                String lista = elaboraXmlList(prenotazioneFatturazionePagelist.getPrenotazioneFatturazioneListXml(), request);
                                if(lista != "") {
                                    request.setAttribute("listaPrenotazioni", lista);
                                    request.setAttribute("listaPrenotazioni.pageInfo", pageInfo);
                                } else {
                                    request.setAttribute("listaPrenotazioni", null);
                                    setFormMessage("richiesteElaborazioniForm", Messages.NO_DATA_FOUND.format(), request);
                                }
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
        }

        return null;
    }

    private PrenotazioneFatturazionePagelist getPrenotazioneFatturazioneList(HttpServletRequest request) throws SQLException {
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
                    getDataByPrefix("dtPeriodo_da", request),
                    getDataByPrefix("dtPeriodo_a", request),
                    request.getAttribute(Field.DT_FLAG_FATTURAZIONE.format()).toString(),
                    getDataByPrefix("dtFlusso_da", request),
                    getDataByPrefix("dtFlusso_a", request)
            );
            prenotazioneFatturazionePagelist = dao.getPrenotazioneFatturazioneList(prenotazione);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        return prenotazioneFatturazionePagelist;
    }
}