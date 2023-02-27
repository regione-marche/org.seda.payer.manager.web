package org.seda.payer.manager.riconciliazionemt.actions;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.riconciliazionemt.bean.GiornaleDiCassa;
import com.seda.payer.core.riconciliazionemt.bean.GiornaleDiCassaPageList;
import com.seda.payer.core.riconciliazionemt.bean.MovimentoDiCassa;
import com.seda.payer.core.riconciliazionemt.bean.NumeroDocumentoList;
import com.seda.payer.core.riconciliazionemt.bean.PspList;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAO;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAOFactory;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiApertiNodoRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiApertiNodoResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class BaseRiconciliazioneTesoreriaAction extends BaseManagerAction{
	
	private static final long serialVersionUID = 1L;
	
	protected String codiceSocieta="";
	protected String codiceProvincia="";
	protected String codiceUtente="";
	
	private DataSource gdcDataSource;
	private String dbSchemaCodSocieta;
	private String gdcDbSchema;
	protected DataSource getGdcDataSource(){return this.gdcDataSource;}
	protected String getGdcDbSchema(){return this.gdcDbSchema;}
	
	public void setProfile(HttpServletRequest request) {
		super.setProfile(request);			
		codiceProvincia = (String)request.getAttribute("tx_provincia") != null? (String)request.getAttribute("tx_provincia"): "";

	}
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		 
		HttpSession session = request.getSession();
		loadDDLStatic(request, session);

	    setProfile(request);
	    
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String dataSourceName =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
		//String dataSourceNameSepa =  configuration.getProperty(PropertiesPath.dataSourceSepa.format(dbSchemaCodSocieta));
		this.gdcDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		
		System.out.println("gdcDbSchema = " + this.gdcDbSchema);
		
		try {
			System.out.println("istanzia gdcDataSource ");
			this.gdcDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));
			
		} catch (ServiceLocatorException e) {
			System.out.println("ServiceLocator error " + e.getMessage());
			throw new ActionException("ServiceLocator error " + e.getMessage(),e);
		}

		return null;
	}

	protected PageInfo getPageInfo(com.seda.payer.pgec.webservice.commons.dati.PageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPages());
		return pageInfo;
	}
	
	protected PageInfo getMovimentiPageInfo(com.seda.payer.pgec.webservice.commons.dati.PageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPages());
		return pageInfo;
	}
	
	protected void tx_SalvaStato(HttpServletRequest request) {
		super.tx_SalvaStato(request);
		request.setAttribute(Field.DATA_PAGAMENTO_DA.format(), getCalendar(request, Field.DATA_PAGAMENTO_DA.format()));
		request.setAttribute(Field.DATA_PAGAMENTO_A.format(), getCalendar(request, Field.DATA_PAGAMENTO_A.format()));
		request.setAttribute(Field.DATA_CREAZIONE_DA.format(), getCalendar(request, Field.DATA_CREAZIONE_DA.format()));
		request.setAttribute(Field.DATA_CREAZIONE_A.format(), getCalendar(request, Field.DATA_CREAZIONE_A.format()));
	}
	
	protected RecuperaMovimentiApertiNodoResponse getListaMovimentiAperti(HttpServletRequest request) throws FaultType, RemoteException {
		RecuperaMovimentiApertiNodoRequest in = new RecuperaMovimentiApertiNodoRequest();
		return WSCache.commonsServer.recuperaMovimentiApertiNodo(in, request);	
	}
	
	protected void resetDDLSession(HttpServletRequest request){
		HttpSession sessione = request.getSession();
		
		sessione.setAttribute("listaProvince", null);
		sessione.setAttribute("listaUtenti", null);
	}
	
	protected void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
	
	switch(getFiredButton(request)) 
	{
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

			request.setAttribute("dtGiornale_da", null);
			request.setAttribute("dtGiornale_a", null);
			
			request.setAttribute("dtMovimento_da", null);
			request.setAttribute("dtMovimento_a", null);
			
			codiceSocieta = "";
			codiceProvincia = "";
			codiceUtente = "";
			
			setParamCodiceSocieta("");
			setParamCodiceUtente("");
							
			loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
			loadDDLUtente(request, session, getParamCodiceSocieta(), codiceProvincia,true);
			loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
			break;
			
		default:
			loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
			loadDDLUtente(request, session, getParamCodiceSocieta(), codiceProvincia,true);
			loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
	}
	}
	
	protected void listaPsp(String societa, String ente, long idGdc, HttpServletRequest request) throws DaoException, ParseException {
		GiornaleDiCassaDAO giornaleDiCassaDAO;
		
		GiornaleDiCassa gdc = new GiornaleDiCassa();
		gdc.setCodSocieta(societa);
		if (ente != null && !ente.equals("")) {
			System.out.println("ente: "+ente);
			gdc.setCodUtente(ente.substring(5, 10));
			gdc.setCodEnte(ente.substring(10));
		} else {
			gdc.setCodUtente("");
			gdc.setCodEnte("");
		}
		gdc.setId(idGdc);
		
		PspList pspList = new PspList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
			conn = getGdcDataSource().getConnection();
			giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
			//fine LP PG21XX04 Leak
			pspList = giornaleDiCassaDAO.ListPsp(gdc);	
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
		request.setAttribute("lista_psp", pspList.getPspListXml());
	}
	
	protected void dettaglioMovimentoCassa(HttpServletRequest request) {		
		GiornaleDiCassaDAO giornaleDiCassaDAO;
		MovimentoDiCassa mdc = new MovimentoDiCassa();
		String idMdc = (String)request.getAttribute("idmdc");
		long idMovimentoDiCassa;
		if (idMdc != null && !idMdc.equals("")) {
			idMovimentoDiCassa = Long.parseLong(idMdc);
		} else {
			idMovimentoDiCassa = 0;
		}
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
		
		
		HttpSession session = request.getSession();
		session.removeAttribute("tx_UtenteEnte");
		session.removeAttribute("tx_provincia");
		session.removeAttribute("tx_societa");
		session.removeAttribute("dtGiornale_a");
		session.removeAttribute("dtGiornale_da");
		session.removeAttribute("idcassa");
		session.removeAttribute("provenienza");
		
		request.setAttribute("mdc_id", mdc.getId());
		request.setAttribute("mdc_conto", mdc.getConto());
		request.setAttribute("mdc_statoSospeso", mdc.getStatoSospeso());
		request.setAttribute("mdc_numDocumento", mdc.getNumDocumento());
		request.setAttribute("mdc_cliente", mdc.getCliente());
		request.setAttribute("mdc_importo", new String(mdc.getImporto().toString().replace('.', ',')));
		request.setAttribute("mdc_squadratura", mdc.getImportoSquadratura());
		request.setAttribute("mdc_rendicontato", mdc.getRendicontato());
		request.setAttribute("mdc_regolarizzato", mdc.getRegolarizzato());
		request.setAttribute("mdc_progressivoDoc", mdc.getProgressivoDoc());
		request.setAttribute("mdc_numBolletta", mdc.getNumBolletta());
		request.setAttribute("mdc_dataMovimento", mdc.getDataMovimento());
		request.setAttribute("mdc_dataValuta", mdc.getDataValuta());
		request.setAttribute("mdc_tipoEsecuzione", mdc.getTipoEsecuzione());
		request.setAttribute("mdc_codiceRiferimento", mdc.getCodiceRiferimento());
		request.setAttribute("mdc_causale", mdc.getCausale());
		
		request.setAttribute("mdc_idgdc", mdc.getIdGiornale());
		request.setAttribute("mdc_ente", mdc.getEnte());
		request.setAttribute("mdc_prov", mdc.getProvenienza());
		request.setAttribute("mdc_data", mdc.getDataGiornale());
		request.setAttribute("mdc_idflusso", mdc.getIdFlusso());
		request.setAttribute("mdc_esercizio", mdc.getEsercizio());
		request.setAttribute("mdc_srego", mdc.getSospRegolarizzati());
		request.setAttribute("mdc_srend", mdc.getSospRendicontati());
		
		request.setAttribute("mdc_nota", mdc.getNota());
		request.setAttribute("mdc_dataRendicontazione", mdc.getDataRendicontazione());
	}
	
	protected GiornaleDiCassa getGiornaleDiCassa(HttpServletRequest request) {
		String societa = (String)request.getAttribute("tx_societa");
		String provincia = (String)request.getAttribute("tx_provincia");
		String codUtenteEnte = (String)request.getAttribute("tx_UtenteEnte");
		String provenienza = (String)request.getAttribute("provenienza");
		String dataGiornaleDa_day = (String)request.getAttribute("dtGiornale_da_day");
		String dataGiornaleDa_month = (String)request.getAttribute("dtGiornale_da_month");
		String dataGiornaleDa_year = (String)request.getAttribute("dtGiornale_da_year");
		String dataGiornaleA_day = (String)request.getAttribute("dtGiornale_a_day");
		String dataGiornaleA_month = (String)request.getAttribute("dtGiornale_a_month");
		String dataGiornaleA_year = (String)request.getAttribute("dtGiornale_a_year");
		String dataMovimentoDa_day = (String)request.getAttribute("dtMovimento_da_day");
		String dataMovimentoDa_month = (String)request.getAttribute("dtMovimento_da_month");
		String dataMovimentoDa_year = (String)request.getAttribute("dtMovimento_da_year");
		String dataMovimentoA_day = (String)request.getAttribute("dtMovimento_a_day");
		String dataMovimentoA_month = (String)request.getAttribute("dtMovimento_a_month");
		String dataMovimentoA_year = (String)request.getAttribute("dtMovimento_a_year");
		String sospRegolarizzati = (String)request.getAttribute("sospRegolarizzati");
		String sospRendicontati = (String)request.getAttribute("sospRendicontati");
		String numDocumento = (String)request.getAttribute("numDocumento");
		String psp = (String)request.getAttribute("psp");
		String idCassa = (String) request.getAttribute("idcassa");
		String chiaveRen = (String) request.getAttribute("chiaveRen");
		
		
		String dataGiornaleDa = getData(dataGiornaleDa_day,dataGiornaleDa_month,dataGiornaleDa_year);
		String dataGiornaleA = getData(dataGiornaleA_day,dataGiornaleA_month,dataGiornaleA_year);
		String dataMovimentoDa = getData(dataMovimentoDa_day,dataMovimentoDa_month,dataMovimentoDa_year);
		String dataMovimentoA = getData(dataMovimentoA_day,dataMovimentoA_month,dataMovimentoA_year);

		GiornaleDiCassa gdc = new GiornaleDiCassa();
		gdc.setCodSocieta(societa);
		if (codUtenteEnte != null && !codUtenteEnte.equals("")) {
			gdc.setCodUtente(codUtenteEnte.substring(5, 10));
			gdc.setCodEnte(codUtenteEnte.substring(10));
		} else {
			gdc.setCodUtente("");
			gdc.setCodEnte("");
		}
		gdc.setProvenienza(provenienza);
		if (dataGiornaleDa != null && !dataGiornaleDa.equals("")) gdc.setDataGiornaleDa(parseDate(dataGiornaleDa, "yyyy-MM-dd"));
		if (dataGiornaleA != null && !dataGiornaleA.equals("")) gdc.setDataGiornaleA(parseDate(dataGiornaleA, "yyyy-MM-dd"));
		if (dataMovimentoDa != null && !dataMovimentoDa.equals("")) gdc.setDataMovimentoDa(parseDate(dataMovimentoDa, "yyyy-MM-dd"));
		if (dataMovimentoA != null && !dataMovimentoA.equals("")) gdc.setDataMovimentoA(parseDate(dataMovimentoA, "yyyy-MM-dd"));
		
		gdc.setSospRegolarizzati(sospRegolarizzati);
		gdc.setSospRendicontati(sospRendicontati);
		
		gdc.setNumDocumento(numDocumento);
		gdc.setPsp(psp);
		gdc.setIdFlusso(idCassa);
		gdc.setChiaveRen(chiaveRen);
		
		return gdc;
	}
	
	protected GiornaleDiCassaPageList listaGiornaliDiCassa(HttpServletRequest request, int rowsPerPage,int pageNumber, String order) throws DaoException, ParseException {
		GiornaleDiCassaDAO giornaleDiCassaDAO;

		GiornaleDiCassa gdc = getGiornaleDiCassa(request);

		GiornaleDiCassaPageList gdcPageList = new GiornaleDiCassaPageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
			conn = getGdcDataSource().getConnection();
			giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
			//fine LP PG21XX04 Leak
			gdcPageList = giornaleDiCassaDAO.ListGiornaliDiCassa(gdc, rowsPerPage, pageNumber, order);	
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
	
		return gdcPageList;	
	}
	
	protected void listaNumeroDocumento(HttpServletRequest request) throws DaoException, ParseException {
		GiornaleDiCassaDAO giornaleDiCassaDAO;
		
		GiornaleDiCassa gdc = getGiornaleDiCassa(request);
		
		NumeroDocumentoList numdocList = new NumeroDocumentoList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
			conn = getGdcDataSource().getConnection();
			giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
			//fine LP PG21XX04 Leak
			numdocList = giornaleDiCassaDAO.ListNumeroDocumento(gdc);	
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
	
		request.setAttribute("lista_numero_documento", numdocList.getNumeroDocumentoListXml());
	}
	
	protected void setGdcParameters(HttpServletRequest request, HttpSession session) {		
		request.setAttribute("idgdc", session.getAttribute("idgdc"));
		request.setAttribute("gdc_ente", session.getAttribute("gdc_ente"));
		request.setAttribute("gdc_prov", session.getAttribute("gdc_prov"));
		request.setAttribute("gdc_data", session.getAttribute("gdc_data"));
		request.setAttribute("gdc_idflusso", session.getAttribute("gdc_idflusso"));
		request.setAttribute("gdc_esercizio", session.getAttribute("gdc_esercizio"));
		request.setAttribute("gdc_srego", session.getAttribute("gdc_srego"));
		request.setAttribute("gdc_srend", session.getAttribute("gdc_srend"));

	}
	
	
}
