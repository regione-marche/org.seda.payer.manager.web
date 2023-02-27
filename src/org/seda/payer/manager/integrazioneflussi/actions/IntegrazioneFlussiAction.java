package org.seda.payer.manager.integrazioneflussi.actions;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.ConfigurazionePosteBlackBoxPos;
import com.seda.payer.core.bean.FlussiPageList;
import com.seda.payer.core.bean.Flusso;
import com.seda.payer.core.bean.FlussoDettagliPageList;
import com.seda.payer.core.dao.BlackBoxPosteDao;
import com.seda.payer.core.dao.IntegrazioniFlussiDao;
import com.seda.payer.core.exception.DaoException;

public class IntegrazioneFlussiAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -199759309501510135L;

	private String blackBoxDbSchema = null;
	private DataSource blackBoxDataSource = null;
	private int rowsPerPage = 0;
	private int pageNumber = 0;
	
	@Override
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);

		PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		String sBlackBoxDataSource = configuration.getProperty(PropertiesPath.dataSourceBlackBox.format(dbSchemaCodSocieta));
		blackBoxDbSchema = configuration.getProperty(PropertiesPath.dataSourceSchemaBlackBox.format(dbSchemaCodSocieta));

		this.rowsPerPage = ((String) request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
		this.pageNumber = ((String) request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));

		try {
			this.blackBoxDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(sBlackBoxDataSource));
		} catch (ServiceLocatorException e) {
			throw new ActionException("blackBoxDataSource non recuperato", e);
		}
		
		int idFlusso = 0;
		try {
			idFlusso = Integer.parseInt(request.getParameter("idFlusso"));
			
			setProfile(request);
			loadSocietaUtenteEnteXml_DDL(request, request.getSession());
			
		} catch (NumberFormatException e) {	}
		
		String idDominio = request.getParameter("idDominio");
		String codiceEnte = request.getParameter("codiceEnte");
		String codiceIuv = request.getParameter("codiceIuv");
		
		FiredButton firedButton = getFiredButton(request);

		switch (firedButton) {
		
		case TX_BUTTON_CERCA:

			if (idFlusso == 0
				&& (idDominio == null || idDominio.trim().length() == 0)
				&& (codiceEnte == null || codiceEnte.trim().length() == 0)
				&& (codiceIuv == null || codiceIuv.trim().length() == 0)) {
				
				recuperaFlussiList(request);
			}
			
			if (idFlusso > 0) {
				recuperaFlussoDettagliList(request, idFlusso, false);
			}
			break;
			
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			resetAttribute(request);
			
			if (idFlusso > 0
				&& (idDominio == null || idDominio.trim().length() == 0)
				&& (codiceEnte == null || codiceEnte.trim().length() == 0)
				&& (codiceIuv == null || codiceIuv.trim().length() == 0)) {
				request.setAttribute("idFlusso", idFlusso);
				recuperaFlussoDettagliList(request, idFlusso, true);
			}
			
			if (idDominio != null && idDominio.trim().length() > 0
				&& codiceEnte != null && codiceEnte.trim().length() > 0
				&& codiceIuv != null && codiceIuv.trim().length() > 0) {
				request.setAttribute("idFlusso", idFlusso);
				recuperaFlussoDettaglio(request, idDominio, codiceEnte, codiceIuv);
			}
			break;
		case TX_BUTTON_INDIETRO:
			resetAttribute(request);
			if (idFlusso > 0
				&& idDominio != null && idDominio.trim().length() > 0
				&& codiceEnte != null && codiceEnte.trim().length() > 0
				&& codiceIuv != null && codiceIuv.trim().length() > 0) {
				request.setAttribute("idFlusso", idFlusso);
				recuperaFlussoDettagliList(request, idFlusso, true);
			}
			break;
			
		default:
			resetAttribute(request);
			break;
		}

		return null;
	}

	private void recuperaFlussoDettaglio(HttpServletRequest request, String idDominio, String codiceEnte, String codiceIuv) throws ActionException {
		
		ConfigurazionePosteBlackBoxPos flussoDettaglio = null;
		Flusso flusso = null;
		
		try (Connection conn = blackBoxDataSource.getConnection()) {
			BlackBoxPosteDao dao1 = new BlackBoxPosteDao(conn, blackBoxDbSchema);
			flussoDettaglio = dao1.select(idDominio, codiceEnte, codiceIuv);
			
			IntegrazioniFlussiDao dao2 = new IntegrazioniFlussiDao(conn, blackBoxDbSchema);
			flusso = dao2.select(flussoDettaglio.getChiaveTestata());
		} catch (SQLException e) {
			throw new ActionException("Connection blackBox non recuperata", e);
		} catch (DaoException e) {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			return;
		}
		
		if (flussoDettaglio != null) {
			
			NumberFormat df2 = new DecimalFormat("#0.00");
			
			Double importo = null;
			if(flussoDettaglio.getImporto() != null) {
				importo = new Double(flussoDettaglio.getImporto() / 100D);
			} else {
				importo = new Double("0.00");
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			request.setAttribute("tx_idFlusso", flusso.getIdFlusso());
			request.setAttribute("tx_codFiscAgg", flusso.getCodFiscAgg());
			request.setAttribute("tx_creazioneFlusso", flusso.getCreazioneFlusso() == null ? null : sdf.format(flusso.getCreazioneFlusso()));
			request.setAttribute("tx_nomeFile", flusso.getNomeFile());
			request.setAttribute("tx_timestampFlusso", flusso.getTimestampFlusso() == null ? null : sdf.format(flusso.getTimestampFlusso()));
			request.setAttribute("tx_tipoFlusso", flusso.getTipoFlusso());
			
			request.setAttribute("tx_idDominio", flussoDettaglio.getCodiceIdentificativoDominio());
			request.setAttribute("tx_idEnte", flussoDettaglio.getCodiceEnte());
			request.setAttribute("tx_numeroAvviso", flussoDettaglio.getNumeroAvviso());
			request.setAttribute("tx_codIdFlusso", flussoDettaglio.getCodiceIdentificativoFlusso());
			request.setAttribute("tx_dataCreazione", flussoDettaglio.getDataCreazione() == null ? null : sdf2.format(flussoDettaglio.getDataCreazione().getTime()));
			request.setAttribute("tx_tipoRecord", flussoDettaglio.getTipoRecord());
			request.setAttribute("tx_idDocumento", flussoDettaglio.getCodiceIdentificativoDocumento());
			request.setAttribute("tx_numRata", flussoDettaglio.getNumeroRata());
			request.setAttribute("tx_dataScadenza", flussoDettaglio.getDataScadenza() == null ? null : sdf.format(flussoDettaglio.getDataScadenza().getTime()));
			request.setAttribute("tx_codFisc", flussoDettaglio.getCodiceFiscale());
			request.setAttribute("tx_flagPagato", flussoDettaglio.getFlagPagato() != null && flussoDettaglio.getFlagPagato().equals("X") ? "Si" : "No");
			request.setAttribute("tx_importo", df2.format(importo));
			request.setAttribute("tx_denDeb", flussoDettaglio.getDenominazioneDebitore());
			request.setAttribute("tx_indContrib", flussoDettaglio.getIndirizzoContribuente());
			request.setAttribute("tx_locContrib", flussoDettaglio.getLocalitaContribuente());
			request.setAttribute("tx_provContrib", flussoDettaglio.getProvinciaContribuente());
			request.setAttribute("tx_flagAnnullamento", flussoDettaglio.getFlagAnnullamento());
			request.setAttribute("tx_dataAggRecord", flussoDettaglio.getDataAggiornamentoRecord());
			request.setAttribute("tx_ibanAccr", flussoDettaglio.getCodiceIbanAccredito());
			request.setAttribute("tx_idIuv", flussoDettaglio.getCodiceIuv());
			request.setAttribute("tx_tassonomia", flussoDettaglio.getTassonomia());
			request.setAttribute("tx_flagPoste", flussoDettaglio.getFlagPoste() ? "Si" : "No");
			request.setAttribute("tx_flagInviato", flussoDettaglio.getFlagInviato() ? "Si" : "No");
			
			request.setAttribute("idDominio_hidden", idDominio);
			request.setAttribute("codiceEnte_hidden", codiceEnte);
			request.setAttribute("codiceIuv_hidden", codiceIuv);
		}
	}

	private void recuperaFlussiList(HttpServletRequest request) throws ActionException {
		
		String tipoFlusso = request.getParameter("tx_tipoFlusso_s");
		String codFiscAgg = request.getParameter("tx_codFiscAgg_s");
		String nomeFile = request.getParameter("tx_nomeFile_s");

		String dataCreazioneDa = request.getParameter("data_creazione_da_year") + "-" + request.getParameter("data_creazione_da_month") + "-" + request.getParameter("data_creazione_da_day");
		String dataCreazioneA = request.getParameter("data_creazione_a_year") + "-" + request.getParameter("data_creazione_a_month") + "-" + request.getParameter("data_creazione_a_day");

		FlussiPageList flussiList = null;
		
		try (Connection conn = blackBoxDataSource.getConnection()) {
			IntegrazioniFlussiDao dao = new IntegrazioniFlussiDao(conn, blackBoxDbSchema);
			flussiList = dao.flussiList(tipoFlusso, codFiscAgg, nomeFile, dataCreazioneDa, dataCreazioneA, pageNumber, rowsPerPage, null);
		} catch (SQLException e) {
			throw new ActionException("Connection blackBox non recuperata", e);
		} catch (DaoException e) {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			return;
		}

		if (flussiList != null && flussiList.getNumRows() > 0) {
			request.setAttribute("lista_flussi", flussiList.getFlussiListXml());
			request.setAttribute("lista_flussi.pageInfo", flussiList);
		} else {
			request.setAttribute("lista_flussi", null);
			setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
		}
	}

	private void recuperaFlussoDettagliList(HttpServletRequest request, int idFlusso, boolean reset) throws ActionException {
		
		dividiSocUtenteEnte(request);
		
		String idDominio = request.getParameter("tx_idDominio_s");
		String codiceEnte = (String) request.getAttribute("tx_enteda5");
		String codiceIuv = request.getParameter("tx_codiceIuv_s");
		Boolean flagPagato = request.getParameter("tx_flagPagato_s") == null || request.getParameter("tx_flagPagato_s").equals("") ? null : request.getParameter("tx_flagPagato_s").equals("Y");
		
		if (reset) {
			idDominio = null;
			codiceEnte = null;
			codiceIuv = null;
			flagPagato = null;
		}
		
		Flusso flusso = null;
		FlussoDettagliPageList flussoDettagliList = null;
		
		try (Connection conn = blackBoxDataSource.getConnection()) {
			IntegrazioniFlussiDao dao = new IntegrazioniFlussiDao(conn, blackBoxDbSchema);
			flusso = dao.select(idFlusso);
			
			switch(flusso.getTipoFlusso()) {
			case "OPNA":
				flussoDettagliList = dao.flussoDettagliList(idFlusso, idDominio, codiceEnte, codiceIuv, flagPagato, 0, pageNumber, rowsPerPage, null);
				break;
			case "RTB01":
				flussoDettagliList = dao.flussoDettagliList(0, idDominio, codiceEnte, codiceIuv, flagPagato, idFlusso, pageNumber, rowsPerPage, null);
				break;
			}
		} catch (SQLException e) {
			throw new ActionException("Connection blackBox non recuperata", e);
		} catch (DaoException e) {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			return;
		}
		
		if (flusso != null) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			request.setAttribute("tx_tipoFlusso", flusso.getTipoFlusso());
			request.setAttribute("tx_codFiscAgg", flusso.getCodFiscAgg());
			request.setAttribute("tx_timestampFlusso", flusso.getTimestampFlusso() == null ? null : sdf.format(flusso.getTimestampFlusso()));
			request.setAttribute("tx_nomeFile", flusso.getNomeFile());
			request.setAttribute("tx_creazioneFlusso", flusso.getCreazioneFlusso() == null ? null : sdf.format(flusso.getCreazioneFlusso()));
		}
		
		if (flussoDettagliList != null && flussoDettagliList.getNumRows() > 0) {
			request.setAttribute("lista_flussodettagli", flussoDettagliList.getFlussoDettagliListXml());
			request.setAttribute("lista_flussodettagli.pageInfo", flussoDettagliList);
		} else {
			request.setAttribute("lista_flussodettagli", null);
			setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
		}
	}

	private void resetAttribute(HttpServletRequest request) {
		
		resetParametri(request);
	}

	private void dividiSocUtenteEnte(HttpServletRequest request) {

		if (request.getAttribute("ddlSocietaUtenteEnte") != null) {
			String ddlSocietaUtenteEnte = (String) request.getAttribute("ddlSocietaUtenteEnte");
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			if (!ddlSocietaUtenteEnte.equals("") && codici.length > 0) {

				request.setAttribute("tx_societa", codici[0]);
				request.setAttribute("tx_utente", codici[1]);
				request.setAttribute("tx_ente", codici[2]);
				request.setAttribute("tx_enteda5", codici[3].substring(1, 6));
			}
		}
	}
	
}
