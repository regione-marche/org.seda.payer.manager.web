package org.seda.payer.manager.riconciliazionemt.actions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.Messages;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.riconciliazionemt.bean.ContoList;
import com.seda.payer.core.riconciliazionemt.bean.GiornaleDiCassa;
import com.seda.payer.core.riconciliazionemt.bean.MovimentoDiCassa;
import com.seda.payer.core.riconciliazionemt.bean.MovimentoDiCassaPageList;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAO;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAOFactory;

public class DettaglioGiornaleCassaAction extends BaseRiconciliazioneTesoreriaAction{
	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private long idGiornaleDiCassa;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		super.service(request);
		
		String data = (String)request.getAttribute("data");
		if (data != null && data != "") {
			try {
				setGdcParametersSession(request, session);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		
		setGdcParameters(request, session);
		
		String IDgdc = (String)request.getAttribute("idgdc");
		if (IDgdc != null && IDgdc != "") {
			idGiornaleDiCassa = Long.parseLong(IDgdc);
		} else {
			idGiornaleDiCassa = 0;
		}
		
		try {
			listaPsp("", "", idGiornaleDiCassa, request);
			listaConto(request);
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));	
		order = request.getParameter("order");
		
		if (request.getAttribute("indietro") != null){
			request.setAttribute("lista_movimenti_di_cassa", session.getAttribute("lista_movimenti"));
			request.setAttribute("lista_movimenti_di_cassa.pageInfo", session.getAttribute("lista_movimenti.pageInfo"));
		}
		
		switch(getFiredButton(request)) {
			case TX_BUTTON_CERCA: 
				try {
					//dividiSocUtenteEnte(request);
					MovimentoDiCassaPageList mdcPageListlist = listaMovimentiDiCassa(request);
					
					PageInfo pageInfo = mdcPageListlist.getPageInfo();
					
					if (mdcPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								session.setAttribute("lista_movimenti", mdcPageListlist.getMovimentoDiCassaListXml());
								session.setAttribute("lista_movimenti.pageInfo", pageInfo);
								request.setAttribute("lista_movimenti_di_cassa", mdcPageListlist.getMovimentoDiCassaListXml());
								request.setAttribute("lista_movimenti_di_cassa.pageInfo", pageInfo);
							}
							else { 
								request.setAttribute("lista_movimenti_di_cassa", null);
								setFormMessage("dettaglioGiornaleCassaForm", Messages.NO_DATA_FOUND.format(), request);
							}
						}
						else { 
							setFormMessage("dettaglioGiornaleCassaForm", "Errore generico - Impossibile recuperare i dati", request);
						}
					}
				} catch(Exception e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				}
				
				break;
				
			case TX_BUTTON_RESET:
				session.setAttribute("lista_movimenti", "");
				request.setAttribute("lista_movimenti_di_cassa", "");
				request.setAttribute("sospRegolarizzati", "");
				request.setAttribute("statoSospeso", "");
				request.setAttribute("importoDa", "");
				request.setAttribute("importoA", "");
				request.setAttribute("sospRendicontati", "");
				request.setAttribute("numDocumento", "");
				request.setAttribute("squadratura", "");
				request.setAttribute("conto", "");
				request.setAttribute("psp", "");
		}
		
		return null; 
	}
	
	protected void listaConto(HttpServletRequest request) throws DaoException, ParseException {
		GiornaleDiCassaDAO giornaleDiCassaDAO;

		ContoList contoList = new ContoList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
			conn = getGdcDataSource().getConnection();
			giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
			//fine LP PG21XX04 Leak
			
			contoList = giornaleDiCassaDAO.ListConto(idGiornaleDiCassa);	
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
		request.setAttribute("lista_conto", contoList.getContoListXml());
	}
	
	private MovimentoDiCassaPageList listaMovimentiDiCassa(HttpServletRequest request) throws DaoException, ParseException {
		GiornaleDiCassaDAO giornaleDiCassaDAO;
		String sospRegolarizzati = (String)request.getAttribute("sospRegolarizzati");
		String sospRendicontati = (String)request.getAttribute("sospRendicontati");
		String numDocumento = (String)request.getAttribute("numDocumento");
		String psp = (String)request.getAttribute("psp");
		String statoSospeso = (String)request.getAttribute("statoSospeso");
		String importoDa = (String)request.getAttribute("importoDa");
		String importoA = (String)request.getAttribute("importoA");
		String squadratura = (String)request.getAttribute("squadratura");
		String conto = (String)request.getAttribute("conto");

		MovimentoDiCassa mdc = new MovimentoDiCassa();
		mdc.setRegolarizzato(sospRegolarizzati);
		mdc.setRendicontato(sospRendicontati);
		mdc.setNumDocumento(numDocumento);
		mdc.setCliente(psp);
		mdc.setStatoSospeso(statoSospeso);
		mdc.setSquadratura(squadratura);
		mdc.setConto(conto);
		if (importoDa!=null && !importoDa.equals("")) {
			BigDecimal impDa = new BigDecimal(importoDa.replace(',', '.'));
			impDa = impDa.setScale(2);
			mdc.setImportoDa(impDa);
		} else {
			BigDecimal impDa = new BigDecimal("0.00");
			mdc.setImportoDa(impDa);
		}
		if (importoA!=null && !importoA.equals("")) {
			BigDecimal impA = new BigDecimal(importoA.replace(',', '.'));
			impA = impA.setScale(2);
			mdc.setImportoA(impA);
		} else {
			BigDecimal impA = new BigDecimal("0.00");
			mdc.setImportoA(impA);
		}
		GiornaleDiCassa gdc = new GiornaleDiCassa();
		gdc.setId(idGiornaleDiCassa);
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
		return mdcPageList;	
	}
	
	private void setGdcParametersSession(HttpServletRequest request, HttpSession session) throws ParseException{
		String idgdc = (String)request.getAttribute("idgdc");
		String ente = (String)request.getAttribute("ente");
		String prov = (String)request.getAttribute("prov");
		String data = (String)request.getAttribute("data");
		String idflusso = (String)request.getAttribute("idflusso");
		String esercizio = (String)request.getAttribute("esercizio");
		String srego = (String)request.getAttribute("srego");
		String srend = (String)request.getAttribute("srend");






		data = data.substring(0, 10);
		SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd");
		Date date = parser.parse(data);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = formatter.format(date);

		session.setAttribute("idgdc", idgdc);
		session.setAttribute("gdc_ente", ente);
		session.setAttribute("gdc_prov", prov);
		session.setAttribute("gdc_data", formattedDate);
		session.setAttribute("gdc_idflusso", idflusso);
		session.setAttribute("gdc_esercizio", esercizio);
		session.setAttribute("gdc_srego", srego);
		session.setAttribute("gdc_srend", srend);

	}
	
}
