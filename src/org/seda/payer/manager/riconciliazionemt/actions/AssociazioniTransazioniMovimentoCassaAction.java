package org.seda.payer.manager.riconciliazionemt.actions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.Messages;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.riconciliazionemt.bean.Transazione;
import com.seda.payer.core.riconciliazionemt.bean.TransazionePageList;
import com.seda.payer.core.riconciliazionemt.bean.GiornaleDiCassa;
import com.seda.payer.core.riconciliazionemt.bean.GiornaleDiCassaPageList;
import com.seda.payer.core.riconciliazionemt.bean.MittenteList;
import com.seda.payer.core.riconciliazionemt.bean.MovimentoDiCassa;
import com.seda.payer.core.riconciliazionemt.bean.MovimentoDiCassaPageList;
import com.seda.payer.core.riconciliazionemt.bean.Transazione;
import com.seda.payer.core.riconciliazionemt.bean.TransazionePageList;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAO;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAOFactory;

public class AssociazioniTransazioniMovimentoCassaAction extends BaseRiconciliazioneTesoreriaAction{

	private static final long serialVersionUID = 1L;
	private long idMovimentoDiCassa=0;
	
	
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		
		tx_SalvaStato(request);

		super.service(request);
        aggiornamentoCombo(request, session);
        loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
        LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), "", getParamCodiceEnte(), getParamCodiceUtente(), false);
        
        GiornaleDiCassaDAO giornaleDiCassaDAO;
		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));	
		String order = request.getParameter("order");
		
		String numeroDocumento = (String) request.getAttribute("numeroDocumento");
		if (numeroDocumento != null && !numeroDocumento.equals("")) {
			idMovimentoDiCassa = Long.parseLong(numeroDocumento);
		}else{
			setFiltriFromSession(request);
			numeroDocumento = (String) request.getAttribute("numeroDocumento");
			System.out.println("[riga54] - numeroDocumento: "+ numeroDocumento);
			if (numeroDocumento != null && !numeroDocumento.equals("")) {
				idMovimentoDiCassa = Long.parseLong(numeroDocumento);
			}
		}
		
		// action aggiungi e elimina transazioni a movimenti
		if (request.getAttribute("action") != null) {
			//inizio LP PG21XX04 Leak
			Connection conn = null;
			//fine LP PG21XX04 Leak
			try {
				//inizio LP PG21XX04 Leak
				//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
				conn = getGdcDataSource().getConnection();
				giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
				//fine LP PG21XX04 Leak
				
				long idTransazione = Long.parseLong((String) request.getAttribute("chiaveTransazione"));
				if(request.getAttribute("action").equals("aggiungi")) {
					giornaleDiCassaDAO.AggiungiTransazione(idMovimentoDiCassa, idTransazione);
				} else if (request.getAttribute("action").equals("elimina")) {
					giornaleDiCassaDAO.EliminaTransazione(idMovimentoDiCassa, idTransazione);
				}
			} catch (DaoException e) {
				e.printStackTrace();
			}
			//inizio LP PG21XX04 Leak
			catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			//fine LP PG21XX04 Leak
			
			listaTransazioni(getTransazione(request), rowsPerPage, pageNumber, order, request);
		}

		
        //populate select identificativo cassa e numero documento
        GiornaleDiCassaPageList gdcPageListlist;
        request.setAttribute("sospRendicontati", "N");
		try {
			listaNumeroDocumento(request);
			gdcPageListlist = listaGiornaliDiCassa(request, 0, 0, "");
			PageInfo pageInfo = gdcPageListlist.getPageInfo();
			
			if (gdcPageListlist.getRetCode()!="00") {
				setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			} else {
				if(pageInfo != null)
				{
					if(pageInfo.getNumRows() > 0)
					{
						request.setAttribute("lista_giornali_di_cassa", gdcPageListlist.getGiornaleDiCassaListXml());
					}
					else { 
						request.setAttribute("lista_giornali_di_cassa", null);
						setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
					}
				}
				else { 
					setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
				}
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
				
		// on change numero documento
		if (idMovimentoDiCassa != 0) {
			MovimentoDiCassa mdc = new MovimentoDiCassa();

			//inizio LP PG21XX04 Leak
			Connection conn = null;
			//fine LP PG21XX04 Leak
			try {
				//inizio LP PG21XX04 Leak
				//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
				conn = getGdcDataSource().getConnection();
				giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
				//fine LP PG21XX04 Leak
				mdc = giornaleDiCassaDAO.dettaglioMovimentoDiCassa(idMovimentoDiCassa);
			} catch (DaoException e1) {
				e1.printStackTrace();
			}
			//inizio LP PG21XX04 Leak
			catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			//fine LP PG21XX04 Leak
			
			request.setAttribute("mdc_importo", new String(mdc.getImporto().toString().replace('.', ',')));
			request.setAttribute("mdc_squadratura", new String(mdc.getImportoSquadratura().toString().replace('.', ',')));
			BigDecimal importoAbbinato = mdc.getImporto().subtract(mdc.getImportoSquadratura());
			request.setAttribute("mdc_importo_abbinato", new String(importoAbbinato.toString().replace('.', ',')));
			
			setFiltri(mdc, request);

		} else {
			request.setAttribute("message", "Selezionare un Numero Documento");
		}
		
		
		switch(getFiredButton(request)) {		
		case TX_BUTTON_CERCA: 
			
			if (numeroDocumento != null && !numeroDocumento.isEmpty()) {		
				
				listaTransazioni(getTransazione(request), rowsPerPage, pageNumber, order, request);
				
			} else {
				GiornaleDiCassa gdc = getGiornaleDiCassa(request);	
				MovimentoDiCassa mdc = new MovimentoDiCassa();
				mdc.setRendicontato("D");
				MovimentoDiCassaPageList mdcPageList = new MovimentoDiCassaPageList();
				//inizio LP PG21XX04 Leak
				Connection conn = null;
				//fine LP PG21XX04 Leak
				try {
					//inizio LP PG21XX04 Leak
					//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
					conn = getGdcDataSource().getConnection();
					giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
					//fine LP PG21XX04 Leak
					mdcPageList = giornaleDiCassaDAO.ListMovimentiDiCassa(gdc, mdc, rowsPerPage, pageNumber, order);	
				} catch (DaoException e1) {
					e1.printStackTrace();
				}
				//inizio LP PG21XX04 Leak
				catch (SQLException e1) {
					e1.printStackTrace();
				} finally {
					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				//fine LP PG21XX04 Leak
				PageInfo pageInfo = mdcPageList.getPageInfo();
				
				if (mdcPageList.getRetCode()!="00") {
					setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
				} else {
					if(pageInfo != null)
					{
						if(pageInfo.getNumRows() > 0)
						{
							request.setAttribute("lista_movimenti_di_cassa_nonrend", mdcPageList.getMovimentoDiCassaListXml());
							request.setAttribute("lista_movimenti_di_cassa_nonrend.pageInfo", pageInfo);
						}
						else { 
							request.setAttribute("lista_movimenti_di_cassa", null);
							setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
						}
					}
					else { 
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					}
				}
			}
			break;
			
		//case TX_BUTTON_RESET:

    }
			
		return null;
	}

	private void listaTransazioni(Transazione transazione, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) {
		GiornaleDiCassaDAO giornaleDiCassaDAO;
		TransazionePageList transazionePageList = new TransazionePageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
			conn = getGdcDataSource().getConnection();
			giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
			//fine LP PG21XX04 Leak
			transazionePageList = giornaleDiCassaDAO.ListTransazione(transazione, rowsPerPage, pageNumber, order);
		} catch (DaoException e) {
			e.printStackTrace();
		}	
		//inizio LP PG21XX04 Leak
		catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		PageInfo pageInfo = transazionePageList.getPageInfo();
		
		if (transazionePageList.getRetCode()!="00") {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
		} else {
			if(pageInfo != null)
			{
				if(pageInfo.getNumRows() > 0)
				{
					request.setAttribute("lista_transazioni", transazionePageList.getTransazioneListXml());
					request.setAttribute("lista_transazioni.pageInfo", pageInfo);
				}
				else { 
					request.setAttribute("lista_transazioni", null);
					setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
				}
			}
			else { 
				setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			}
		}
	}
	
	private Transazione getTransazione(HttpServletRequest request) {
		String dataTransazioneDa_day = (String)request.getAttribute("dtTransazione_da_day");
		String dataTransazioneDa_month = (String)request.getAttribute("dtTransazione_da_month");
		String dataTransazioneDa_year = (String)request.getAttribute("dtTransazione_da_year");
		String dataTransazioneA_day = (String)request.getAttribute("dtTransazione_a_day");
		String dataTransazioneA_month = (String)request.getAttribute("dtTransazione_a_month");
		String dataTransazioneA_year = (String)request.getAttribute("dtTransazione_a_year");
		String idTransazione = (String)request.getAttribute("idTransazione");
		String idIUV = (String)request.getAttribute("idIUV");
		String idIUR = (String)request.getAttribute("idIUR");
		String importoDa = (String)request.getAttribute("importoDa");
		String importoA = (String)request.getAttribute("importoA");
		
		String dataTransazioneDa = getData(dataTransazioneDa_day,dataTransazioneDa_month,dataTransazioneDa_year);
		String dataTransazioneA = getData(dataTransazioneA_day,dataTransazioneA_month,dataTransazioneA_year);
		
		Transazione transazione = new Transazione();
		transazione.setIdMdc(idMovimentoDiCassa);
		if (dataTransazioneDa != null && dataTransazioneDa != "") transazione.setDataTransazioneDa(parseDate(dataTransazioneDa, "yyyy-MM-dd"));
		if (dataTransazioneA != null && dataTransazioneA != "") transazione.setDataTransazioneA(parseDate(dataTransazioneA, "yyyy-MM-dd"));
		transazione.setId(idTransazione);
		transazione.setIuv(idIUV);
		transazione.setIur(idIUR);
		if (importoDa!=null && !importoDa.equals("")) {
			BigDecimal impDa = new BigDecimal(importoDa.replace(',', '.'));
			impDa = impDa.setScale(2);
			transazione.setImportoDa(impDa);
		} else {
			BigDecimal impDa = new BigDecimal("0.00");
			transazione.setImportoDa(impDa);
		}
		if (importoA!=null && !importoA.equals("")) {
			BigDecimal impA = new BigDecimal(importoA.replace(',', '.'));
			impA = impA.setScale(2);
			transazione.setImportoA(impA);
		} else {
			BigDecimal impA = new BigDecimal("0.00");
			transazione.setImportoA(impA);
		}
		
		return transazione;
	}

private void setFiltri(MovimentoDiCassa mdc, HttpServletRequest request){
		
		request.setAttribute("tx_UtenteEnte", mdc.getCodiceSocieta()+mdc.getCutecute()+mdc.getChiaveEnte());
		request.setAttribute("tx_provincia", mdc.getSiglaProvincia());
		request.setAttribute("tx_societa", mdc.getCodiceSocieta());
		request.setAttribute("dtGiornale_a", mdc.getDataMovimento());
		request.setAttribute("dtGiornale_da", mdc.getDataMovimento());
		request.setAttribute("idcassa", mdc.getIdFlusso());
		request.setAttribute("provenienza", mdc.getProvenienza());
		
		request.setAttribute("ddlUtenteEnteDisabled", true);
		request.setAttribute("provinciaDdlDisabled", true);
		request.setAttribute("societaDdlDisabled", true);
		request.setAttribute("dtGiornale_da_disabled", true);
		request.setAttribute("dtGiornale_a_disabled", true);
		request.setAttribute("idcassaDisabled", true);
		request.setAttribute("provenienzaDisabled", true);
		request.setAttribute("numeroDocumento_disabled", true);
		
		
		HttpSession session = request.getSession();
		
		session.setAttribute("tx_UtenteEnte", mdc.getCodiceSocieta()+mdc.getCutecute()+mdc.getChiaveEnte());
		session.setAttribute("tx_provincia", mdc.getSiglaProvincia());
		session.setAttribute("tx_societa", mdc.getCodiceSocieta());
		session.setAttribute("dtGiornale_a", mdc.getDataMovimento());
		session.setAttribute("dtGiornale_da", mdc.getDataMovimento());
		session.setAttribute("idcassa", mdc.getIdFlusso());
		session.setAttribute("provenienza", mdc.getProvenienza());
		session.setAttribute("numeroDocumento", request.getAttribute("numeroDocumento"));
		session.setAttribute("ddlUtenteEnteDisabled", true);
		session.setAttribute("provinciaDdlDisabled", true);
		session.setAttribute("societaDdlDisabled", true);
		session.setAttribute("dtGiornale_da_disabled", true);
		session.setAttribute("dtGiornale_a_disabled", true);
		session.setAttribute("idcassaDisabled", true);
		session.setAttribute("provenienzaDisabled", true);
		session.setAttribute("numeroDocumento_disabled", true);
	}
	
	private void setFiltriFromSession(HttpServletRequest request){
		HttpSession session = request.getSession();
		
		request.setAttribute("tx_UtenteEnte", session.getAttribute("tx_UtenteEnte"));
		request.setAttribute("tx_provincia",session.getAttribute("tx_provincia"));
		request.setAttribute("tx_societa",session.getAttribute("tx_societa"));
		request.setAttribute("dtGiornale_a", session.getAttribute("dtGiornale_a"));
		request.setAttribute("dtGiornale_da", session.getAttribute("dtGiornale_da"));
		request.setAttribute("idcassa", session.getAttribute("idcassa"));
		request.setAttribute("provenienza", session.getAttribute("provenienza"));
		request.setAttribute("numeroDocumento", session.getAttribute("numeroDocumento"));
		request.setAttribute("ddlUtenteEnteDisabled", true);
		request.setAttribute("provinciaDdlDisabled", true);
		request.setAttribute("societaDdlDisabled", true);
		request.setAttribute("dtGiornale_da_disabled", true);
		request.setAttribute("dtGiornale_a_disabled", true);
		request.setAttribute("idcassaDisabled", true);
		request.setAttribute("provenienzaDisabled", true);
		request.setAttribute("numeroDocumento_disabled", true);
		
		
	}
}
