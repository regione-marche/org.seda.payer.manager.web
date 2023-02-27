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
import com.seda.payer.core.riconciliazionemt.bean.Flusso;
import com.seda.payer.core.riconciliazionemt.bean.FlussoPageList;
import com.seda.payer.core.riconciliazionemt.bean.GiornaleDiCassa;
import com.seda.payer.core.riconciliazionemt.bean.GiornaleDiCassaPageList;
import com.seda.payer.core.riconciliazionemt.bean.MittenteList;
import com.seda.payer.core.riconciliazionemt.bean.MovimentoDiCassa;
import com.seda.payer.core.riconciliazionemt.bean.MovimentoDiCassaPageList;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAO;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAOFactory;

public class AssociazioniFlussiMovimentoCassaAction extends BaseRiconciliazioneTesoreriaAction{

	private static final long serialVersionUID = 1L;
	private long idMovimentoDiCassa=0;
	
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		
		tx_SalvaStato(request);

		super.service(request);
        aggiornamentoCombo(request, session);
        loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
        LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), "", getParamCodiceEnte(), getParamCodiceUtente(), false);
        
        System.out.println("AssociazioniProvvisorieRiconciliazionemtEnabled(): "+userBean.getAssociazioniProvvisorieRiconciliazionemtEnabled());
        
        GiornaleDiCassaDAO giornaleDiCassaDAO;
		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));	
		String order = request.getParameter("order");
		
		
		String numeroDocumento = (String) request.getAttribute("numeroDocumento");
		System.out.println("[riga48] - numeroDocumento: "+ numeroDocumento);
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
		
		// action aggiungi e elimin flussi a movimenti
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
				
				long idFlusso = Long.parseLong((String) request.getAttribute("chiaveFlusso"));
				if(request.getAttribute("action").equals("aggiungi")) {
					giornaleDiCassaDAO.AggiungiFlusso(idMovimentoDiCassa, idFlusso);
				} else if (request.getAttribute("action").equals("elimina")) {
					giornaleDiCassaDAO.EliminaFlusso(idMovimentoDiCassa, idFlusso);
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
			
			listaFlussi(getFlusso(request), rowsPerPage, pageNumber, order, request);
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
				System.out.println("[riga118] - Errore: "+ e1.getMessage());
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
			
			System.out.println("[riga122] - importo: " + mdc.getImporto());
			System.out.println("[riga123] - chiaveEnte: "+ mdc.getChiaveEnte());
			
			request.setAttribute("mdc_importo", new String(mdc.getImporto().toString().replace('.', ',')));
			request.setAttribute("mdc_squadratura", new String(mdc.getImportoSquadratura().toString().replace('.', ',')));
			BigDecimal importoAbbinato = mdc.getImporto().subtract(mdc.getImportoSquadratura());
			request.setAttribute("mdc_importo_abbinato", new String(importoAbbinato.toString().replace('.', ',')));
			
			setFiltri(mdc, request);
		
			//populate select mittente
			MittenteList listMittente;
			//inizio LP PG21XX04 Leak2
			Connection connS = null;
			//fine LP PG21XX04 Leak2
			try {
				//inizio LP PG21XX04 Leak
				//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
				//inizio LP PG21XX04 Leak2
				//conn = getGdcDataSource().getConnection();
				//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
				connS = getGdcDataSource().getConnection();
				giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(connS, getGdcDbSchema());
				//fine LP PG21XX04 Leak2
				//fine LP PG21XX04 Leak
				listMittente = giornaleDiCassaDAO.ListMittente(idMovimentoDiCassa);
				request.setAttribute("lista_mittente", listMittente.getMittenteListXml());
			} catch (DaoException e1) {
				e1.printStackTrace();
			}
			//inizio LP PG21XX04 Leak
			catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				//inizio LP PG21XX04 Leak2
				//if (conn != null) {
				//	try {
				//		conn.close();
				//	} catch (SQLException e) {
				//		e.printStackTrace();
				//	}
				//}
				if (connS != null) {
					try {
						connS.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				//fine LP PG21XX04 Leak2
			}
			//fine LP PG21XX04 Leak

		} else {
			request.setAttribute("message", "Selezionare un Numero Documento");
		}
		
		
		switch(getFiredButton(request)) {		
		case TX_BUTTON_CERCA: 
			
			if (numeroDocumento != null && !numeroDocumento.isEmpty()) {		
				
				listaFlussi(getFlusso(request), rowsPerPage, pageNumber, order, request);
				
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

	private void listaFlussi(Flusso flusso, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) {
		GiornaleDiCassaDAO giornaleDiCassaDAO;
		FlussoPageList flussoPageList = new FlussoPageList();
		
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
			conn = getGdcDataSource().getConnection();
			giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
			//fine LP PG21XX04 Leak
			flussoPageList = giornaleDiCassaDAO.ListFlusso(flusso, rowsPerPage, pageNumber, order);
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
	
		PageInfo pageInfo = flussoPageList.getPageInfo();
		
		if (flussoPageList.getRetCode()!="00") {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
		} else {
			if(pageInfo != null)
			{
				if(pageInfo.getNumRows() > 0)
				{
					request.setAttribute("lista_flussi", flussoPageList.getFlussoListXml());
					request.setAttribute("lista_flussi.pageInfo", pageInfo);
				}
				else { 
					request.setAttribute("lista_flussi", null);
					setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
				}
			}
			else { 
				setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			}
		}
	}
	
	private Flusso getFlusso(HttpServletRequest request) {
		String dataFlussoDa_day = (String)request.getAttribute("dtFlusso_da_day");
		String dataFlussoDa_month = (String)request.getAttribute("dtFlusso_da_month");
		String dataFlussoDa_year = (String)request.getAttribute("dtFlusso_da_year");
		String dataFlussoA_day = (String)request.getAttribute("dtFlusso_a_day");
		String dataFlussoA_month = (String)request.getAttribute("dtFlusso_a_month");
		String dataFlussoA_year = (String)request.getAttribute("dtFlusso_a_year");
		String idFlusso = (String)request.getAttribute("idFlusso");
		String mittente = (String)request.getAttribute("mittente");
		String importoDa = (String)request.getAttribute("importoDa");
		String importoA = (String)request.getAttribute("importoA");
		
		String dataFlussoDa = getData(dataFlussoDa_day,dataFlussoDa_month,dataFlussoDa_year);
		String dataFlussoA = getData(dataFlussoA_day,dataFlussoA_month,dataFlussoA_year);
		
		Flusso flusso = new Flusso();
		flusso.setIdMdc(idMovimentoDiCassa);
		if (dataFlussoDa != null && dataFlussoDa != "") flusso.setDataFlussoDa(parseDate(dataFlussoDa, "yyyy-MM-dd"));
		if (dataFlussoA != null && dataFlussoA != "") flusso.setDataFlussoA(parseDate(dataFlussoA, "yyyy-MM-dd"));
		flusso.setFlusso(idFlusso);
		flusso.setCodiceMittente(mittente);
		if (importoDa!=null && !importoDa.equals("")) {
			BigDecimal impDa = new BigDecimal(importoDa.replace(',', '.'));
			impDa = impDa.setScale(2);
			flusso.setImportoDa(impDa);
		} else {
			BigDecimal impDa = new BigDecimal("0.00");
			flusso.setImportoDa(impDa);
		}
		if (importoA!=null && !importoA.equals("")) {
			BigDecimal impA = new BigDecimal(importoA.replace(',', '.'));
			impA = impA.setScale(2);
			flusso.setImportoA(impA);
		} else {
			BigDecimal impA = new BigDecimal("0.00");
			flusso.setImportoA(impA);
		}
		
		return flusso;
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
		
//		session.removeAttribute("tx_UtenteEnte");
//		session.removeAttribute("tx_provincia");
//		session.removeAttribute("tx_societa");
//		session.removeAttribute("dtGiornale_a");
//		session.removeAttribute("dtGiornale_da");
//		session.removeAttribute("idcassa");
//		session.removeAttribute("provenienza");
		
		
		
	}

}
