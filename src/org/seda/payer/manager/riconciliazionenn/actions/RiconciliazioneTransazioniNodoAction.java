package org.seda.payer.manager.riconciliazionenn.actions;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.commons.security.TokenGenerator;
import com.seda.data.helper.Helper;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.ibm.icu.math.BigDecimal;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiNodoResponse;
import com.seda.payer.pgec.webservice.commons.dati.RiepilogoMovimentiCBI;
import com.seda.payer.pgec.webservice.commons.dati.StampaMovimentiPdfRequest;
import com.seda.payer.pgec.webservice.commons.dati.StampaMovimentiPdfResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class RiconciliazioneTransazioniNodoAction extends BaseRiconciliazioneNodoAction {

	private static final long serialVersionUID = 1L;
	private static String exp;
	private static boolean actExp;
	private static int totMov;
	//double totImpMov = 0.0;
	private static int totImpMov;
	private static int totTran;
	private static BigDecimal totImpQun;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();

		tx_SalvaStato(request);

		super.service(request);
        aggiornamentoCombo(request, session);
        loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
        LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), "", getParamCodiceEnte(), getParamCodiceUtente(), false);
		
        exp="0";
    	actExp = false;
    	totMov = 0;
    	//double totImpMov = 0.0;
    	totImpMov = 0;
    	totTran =0;
		// QF inserimento del totale degl importi Quadratura
		totImpQun = new BigDecimal("0");
		
		if(getTemplateCurrentApplication(request, session).equals("regmarche") && request.getAttribute("dtFlusso_da")==null && request.getAttribute("dtMakeFlusso_da")==null)
			request.setAttribute("dtFlusso_da", Calendar.getInstance());
		        
		if(request.getAttribute("indietro") != null) {				
			request.setAttribute(Field.LISTA_MOVIMENTI.format(), session.getAttribute("session_lista_movimenti"));
			request.setAttribute(Field.LISTA_MOVIMENTI_PAGEINFO.format(), session.getAttribute("session_lista_movimenti.pageInfo"));
			request.setAttribute(Field.LISTA_MOVIMENTI_GROUPED.format(), session.getAttribute("session_lista_movimenti.grouped"));					
			//inizio LP PG21XX05 - Bug si perdevano i valori dei totali
			request.setAttribute("totMov", session.getAttribute("totMov"));
			request.setAttribute("totImpMov", session.getAttribute("totImpMov"));
			request.setAttribute("totTran", session.getAttribute("totTran"));
			request.setAttribute("totImpQun", session.getAttribute("totImpQun"));
			//fine LP PG21XX05
		}
		
        switch(getFiredButton(request)) {
			case TX_BUTTON_CERCA_EXP: 
				if (request.getParameter("ext") != null && request.getParameter("ext").equals("0"))
					exp = "1";
				else
					exp = "0";
				actExp = true;

				request.setAttribute("rowsPerPage", request.getParameter("hRowsPerPage"));
				request.setAttribute("pageNumber", request.getParameter("hPageNumber"));
				request.setAttribute("order", request.getParameter("hOrder"));
			case TX_BUTTON_CERCA: {
					salvaFiltri(request, session);
					ricerca(request, session);
				};break;
			case TX_BUTTON_STAMPA:
				try 
				{
					String filename = getListaMovimentiPdf(request);
					request.setAttribute("pdfMovimenti", filename);
				}
				catch (Exception e) 
				{
					setFormMessage("riconciliazioneTransazioniNodoForm", e.getMessage() , request);
				}
				break;
			case TX_BUTTON_NULL: {
					impostaFiltri(request, session);
					//ricerca(request, session);
				}; break;

			// inizio SR PGNTMGR-56
			case TX_BUTTON_ESPORTADATI: {
				salvaFiltri(request, session);
				try {
					EsitoRisposte esito = inserisciPrenotazione(request);
					if(esito.getCodiceMessaggio() != null && esito.getCodiceMessaggio().equals("OK")) {
						setFormMessage("riconciliazioneTransazioniNodoForm", Messages.INS_OK.format(), request);
					}
				} catch (DaoException e) {
					e.printStackTrace();
					setFormMessage("riconciliazioneTransazioniNodoForm", "Errore nell'inserimento della prenotazione.", request);
				}
            } break;
			// fine SR PGNTMGR-56
        }	

		return null;
	}

	private String getListaMovimentiPdf(HttpServletRequest request) throws FaultType, RemoteException {
		StampaMovimentiPdfResponse stampaMovimentiPdfResponse;
		StampaMovimentiPdfRequest  stampaMovimentiPdfRequest = new StampaMovimentiPdfRequest();

		// stampa singolo gateway o multigateway	0 no	1 si
		String multiGTW = request.getAttribute("multiGTW")!=null?(String)request.getAttribute("multiGTW"):"0";
		
		
		
		String chiaveQuad = (request.getParameter(Field.CHIAVE_MOVIMENTO.format()) == null) ? "0" : request.getParameter(Field.CHIAVE_MOVIMENTO.format());
		if (chiaveQuad.equals("")) chiaveQuad = "0"; 

		stampaMovimentiPdfRequest.setChiaveMovimento(Integer.parseInt(chiaveQuad));
		stampaMovimentiPdfRequest.setCodiceSocieta(getParamCodiceSocieta());
		stampaMovimentiPdfRequest.setCodiceProvincia(codiceProvincia);
		stampaMovimentiPdfRequest.setCodiceUtente(getParamCodiceUtente());
		stampaMovimentiPdfRequest.setMostraMovimenti(request.getParameter(Field.MOSTRA_MOVIMENTI.format()));
		stampaMovimentiPdfRequest.setTipoCarta(request.getParameter(Field.TX_TIPO_CARTA.format()));
		stampaMovimentiPdfRequest.setCodiceABI(request.getParameter(Field.CODICE_ABI.format()));
		stampaMovimentiPdfRequest.setCodiceCAB(request.getParameter(Field.CODICE_CAB.format()));
		stampaMovimentiPdfRequest.setCodiceSIA(request.getParameter(Field.CODICE_SIA.format()));
		stampaMovimentiPdfRequest.setCRO(request.getParameter(Field.CRO.format()));
		stampaMovimentiPdfRequest.setCCB(request.getParameter(Field.CCB.format()));
		stampaMovimentiPdfRequest.setNomeSupporto(request.getParameter(Field.NOME_SUPPORTO.format()));
		String importo_mon_da = request.getParameter(Field.IMPORTO_MOVIMENTO_DA.format()) == null ? "" : request.getParameter(Field.IMPORTO_MOVIMENTO_DA.format());
		String importo_mon_a = request.getParameter(Field.IMPORTO_MOVIMENTO_A.format()) == null ? "" : request.getParameter(Field.IMPORTO_MOVIMENTO_A.format());
		importo_mon_da = importo_mon_da.replaceAll("\\.", "");
		importo_mon_da = importo_mon_da.replaceAll(",", ".");
		importo_mon_a = importo_mon_a.replaceAll("\\.", "");
		importo_mon_a = importo_mon_a.replaceAll(",", ".");
		stampaMovimentiPdfRequest.setImportoA(importo_mon_a);
		stampaMovimentiPdfRequest.setImportoDa(importo_mon_da);
	
		stampaMovimentiPdfRequest.setDataCreazioneDa(getData(request.getParameter(Field.DATA_CREAZIONE_DA_DAY.format()),request.getParameter(Field.DATA_CREAZIONE_DA_MONTH.format()),request.getParameter(Field.DATA_CREAZIONE_DA_YEAR.format())));
		stampaMovimentiPdfRequest.setDataCreazioneA(getData(request.getParameter(Field.DATA_CREAZIONE_A_DAY.format()),request.getParameter(Field.DATA_CREAZIONE_A_MONTH.format()),request.getParameter(Field.DATA_CREAZIONE_A_YEAR.format())));
		stampaMovimentiPdfRequest.setDataValutaDa(getData(request.getParameter(Field.DATA_VALUTA_DA_DAY.format()),request.getParameter(Field.DATA_VALUTA_DA_MONTH.format()),request.getParameter(Field.DATA_VALUTA_DA_YEAR.format())));
		stampaMovimentiPdfRequest.setDataValutaA(getData(request.getParameter(Field.DATA_VALUTA_A_DAY.format()),request.getParameter(Field.DATA_VALUTA_A_MONTH.format()),request.getParameter(Field.DATA_VALUTA_A_YEAR.format())));
		stampaMovimentiPdfRequest.setDataContabileDa(getData(request.getParameter(Field.DATA_CONTABILE_DA_DAY.format()),request.getParameter(Field.DATA_CONTABILE_DA_MONTH.format()),request.getParameter(Field.DATA_CONTABILE_DA_YEAR.format())));
		stampaMovimentiPdfRequest.setDataContabileA(getData(request.getParameter(Field.DATA_CONTABILE_A_DAY.format()),request.getParameter(Field.DATA_CONTABILE_A_MONTH.format()),request.getParameter(Field.DATA_CONTABILE_A_YEAR.format())));
		
		stampaMovimentiPdfRequest.setMultiGTW(multiGTW);
		
		stampaMovimentiPdfResponse = WSCache.commonsServer.stampaMovimentiPdf(stampaMovimentiPdfRequest, request);
		return stampaMovimentiPdfResponse.getFileName();
	}

	private void ricerca(HttpServletRequest request, HttpSession session) {
		// gestione compatibilita' richiamo tramite eventuale hyperlink				
		if (request.getAttribute("ext")==null)
			request.setAttribute("ext","0");

		totMov = 0;
		//double totImpMov = 0.0;
		totImpMov = 0;
		totTran =0;
		
		// QF inserimento del totale degl importi Quadratura
		totImpQun = new BigDecimal("0");
		

		//if (request.getAttribute("ext")=="" || actExp)
		if (request.getAttribute("ext").toString().equals("") || actExp)
			request.setAttribute("ext",exp);
		
		String messageDate = controlloDate(request);
		if(getTemplateCurrentApplication(request, session).equals("regmarche") && messageDate != null)
		{
			setFormMessage("riconciliazioneTransazioniNodoForm", messageDate , request);
		}else {
	
			try {
				RecuperaMovimentiNodoResponse movimentiNodo = getListaMovimentiNodo(request);
				PageInfo pageInfo = getMovimentiPageInfo(movimentiNodo.getPageInfo());
				if(pageInfo.getNumRows() > 0) {
					String lista = movimentiNodo.getMovimentiCBIXml();					
					request.setAttribute(Field.LISTA_MOVIMENTI.format(), lista);
					request.setAttribute(Field.LISTA_MOVIMENTI_PAGEINFO.format(), pageInfo);
					RiepilogoMovimentiCBI[] listaGrouped= movimentiNodo.getRiepilogoMovimentiCBI();
					request.setAttribute(Field.LISTA_MOVIMENTI_GROUPED.format(), listaGrouped);
					
					session.setAttribute("session_lista_movimenti", lista);
					session.setAttribute("session_lista_movimenti.pageInfo", pageInfo);
					session.setAttribute("session_lista_movimenti.grouped", listaGrouped);
	
					//todo calcolo totali
					for (RiepilogoMovimentiCBI i: listaGrouped)
					{
						totMov = totMov + Integer.parseInt(i.getNumeroMovimenti());
						totImpMov = totImpMov + Integer.parseInt(i.getImportoMovimenti());
						
						totImpQun = totImpQun.add(new BigDecimal(i.getImportoQuadratura()));
						totTran = totTran + Integer.parseInt(i.getNumeroTransazioni());
					}
						
					request.setAttribute("totMov",totMov);
					request.setAttribute("totImpMov",totImpMov);
					request.setAttribute("totTran",totTran);
					request.setAttribute("totImpQun",totImpQun.toString().replace(".", ","));
					//inizio LP PG21XX05 - Bug si perdevano i valori dei totali
					session.setAttribute("totMov", request.getAttribute("totMov"));
					session.setAttribute("totImpMov", request.getAttribute("totImpMov"));
					session.setAttribute("totTran", request.getAttribute("totTran"));
					session.setAttribute("totImpQun", request.getAttribute("totImpQun"));
					//fine LP PG21XX05
				}
				else setFormMessage("riconciliazioneTransazioniNodoForm", Messages.TX_NO_TRANSACTIONS.format() , request);
	
			} 
			catch (Exception e) {
				setFormMessage("riconciliazioneTransazioniNodoForm", e.getMessage() , request);
			}
		}
	}

	
	private void salvaFiltri(HttpServletRequest request, HttpSession session) {
		try {
			System.out.println("Salvataggio filtri...");
			session.setAttribute(Field.TX_SOCIETA.format(), request.getAttribute(Field.TX_SOCIETA.format()));
			session.setAttribute(Field.TX_PROVINCIA.format(), request.getAttribute(Field.TX_PROVINCIA.format()));
			session.setAttribute("tx_UtenteEnte", request.getAttribute("tx_UtenteEnte"));
			session.setAttribute(Field.DT_FLUSSO_DA_DAY.format(), request.getAttribute(Field.DT_FLUSSO_DA_DAY.format()));
			session.setAttribute(Field.DT_FLUSSO_DA_MONTH.format(), request.getAttribute(Field.DT_FLUSSO_DA_MONTH.format()));
			session.setAttribute(Field.DT_FLUSSO_DA_YEAR.format(), request.getAttribute(Field.DT_FLUSSO_DA_YEAR.format()));
			session.setAttribute(Field.DT_FLUSSO_A_DAY.format(), request.getAttribute(Field.DT_FLUSSO_A_DAY.format()));
			session.setAttribute(Field.DT_FLUSSO_A_MONTH.format(), request.getAttribute(Field.DT_FLUSSO_A_MONTH.format()));
			session.setAttribute(Field.DT_FLUSSO_A_YEAR.format(), request.getAttribute(Field.DT_FLUSSO_A_YEAR.format()));
			session.setAttribute(Field.ID_FLUSSO.format(), request.getAttribute(Field.ID_FLUSSO.format()));
			session.setAttribute(Field.STATO_SQUADRATURA.format(), request.getAttribute(Field.STATO_SQUADRATURA.format()));
			session.setAttribute(Field.NOME_FILE_TXT.format(), request.getAttribute(Field.NOME_FILE_TXT.format()));
			session.setAttribute(Field.KEY_QUADRATURA.format(), request.getAttribute(Field.KEY_QUADRATURA.format()));
			//inizio LP PG200200
			session.setAttribute(Field.IMP_PAGAMENTO_DA.format(), request.getAttribute(Field.IMP_PAGAMENTO_DA.format()));
			session.setAttribute(Field.IMP_PAGAMENTO_A.format(), request.getAttribute(Field.IMP_PAGAMENTO_A.format()));
			session.setAttribute(Field.DT_MAKEFLUSSO_DA_DAY.format(), request.getAttribute(Field.DT_MAKEFLUSSO_DA_DAY.format()));
			session.setAttribute(Field.DT_MAKEFLUSSO_DA_MONTH.format(), request.getAttribute(Field.DT_MAKEFLUSSO_DA_MONTH.format()));
			session.setAttribute(Field.DT_MAKEFLUSSO_DA_YEAR.format(), request.getAttribute(Field.DT_MAKEFLUSSO_DA_YEAR.format()));
			session.setAttribute(Field.DT_MAKEFLUSSO_A_DAY.format(), request.getAttribute(Field.DT_MAKEFLUSSO_A_DAY.format()));
			session.setAttribute(Field.DT_MAKEFLUSSO_A_MONTH.format(), request.getAttribute(Field.DT_MAKEFLUSSO_A_MONTH.format()));
			session.setAttribute(Field.DT_MAKEFLUSSO_A_YEAR.format(), request.getAttribute(Field.DT_MAKEFLUSSO_A_YEAR.format()));
			session.setAttribute(Field.TIPOLOGIA_FLUSSO.format(), request.getAttribute(Field.TIPOLOGIA_FLUSSO.format()));
			session.setAttribute(Field.SCARTATE_FLUSSO.format(), request.getAttribute(Field.SCARTATE_FLUSSO.format()));
			session.setAttribute(Field.RECUPERATE_FLUSSO.format(), request.getAttribute(Field.RECUPERATE_FLUSSO.format()));
			//fine LP PG200200
			session.setAttribute(Field.IMP_PAGAMENTO_DA.format(), request.getAttribute(Field.IMP_PAGAMENTO_DA.format()));
			session.setAttribute(Field.IMP_PAGAMENTO_A.format(), request.getAttribute(Field.IMP_PAGAMENTO_A.format()));
			session.setAttribute(Field.TX_CODICE_IUV.format(), request.getAttribute(Field.TX_CODICE_IUV.format()));
		} catch (Exception e) {
			System.out.println("Errore salvataggio filtri : ");
			e.getMessage();
		}
	}
	
	private void impostaFiltri(HttpServletRequest request, HttpSession session) {
		try {
			System.out.println("Settaggio filtri...");
			request.setAttribute(Field.TX_SOCIETA.format(), session.getAttribute(Field.TX_SOCIETA.format()));
			request.setAttribute(Field.TX_PROVINCIA.format(), session.getAttribute(Field.TX_PROVINCIA.format()));
			request.setAttribute("tx_UtenteEnte", session.getAttribute("tx_UtenteEnte"));
			request.setAttribute(Field.DT_FLUSSO_DA_DAY.format(), session.getAttribute(Field.DT_FLUSSO_DA_DAY.format()));
			request.setAttribute(Field.DT_FLUSSO_DA_MONTH.format(), session.getAttribute(Field.DT_FLUSSO_DA_MONTH.format()));
			request.setAttribute(Field.DT_FLUSSO_DA_YEAR.format(), session.getAttribute(Field.DT_FLUSSO_DA_YEAR.format()));
			request.setAttribute(Field.DT_FLUSSO_A_DAY.format(), session.getAttribute(Field.DT_FLUSSO_A_DAY.format()));
			request.setAttribute(Field.DT_FLUSSO_A_MONTH.format(), session.getAttribute(Field.DT_FLUSSO_A_MONTH.format()));
			request.setAttribute(Field.DT_FLUSSO_A_YEAR.format(), session.getAttribute(Field.DT_FLUSSO_A_YEAR.format()));
			request.setAttribute(Field.ID_FLUSSO.format(), session.getAttribute(Field.ID_FLUSSO.format()));
			request.setAttribute(Field.STATO_SQUADRATURA.format(), session.getAttribute(Field.STATO_SQUADRATURA.format()));
			request.setAttribute(Field.NOME_FILE_TXT.format(), session.getAttribute(Field.NOME_FILE_TXT.format()));
			request.setAttribute(Field.KEY_QUADRATURA.format(), session.getAttribute(Field.KEY_QUADRATURA.format()));
			//inizio LP PG200200
			request.setAttribute(Field.IMP_PAGAMENTO_DA.format(), session.getAttribute(Field.IMP_PAGAMENTO_DA.format()));
			request.setAttribute(Field.IMP_PAGAMENTO_A.format(), session.getAttribute(Field.IMP_PAGAMENTO_A.format()));
			request.setAttribute(Field.DT_MAKEFLUSSO_DA_DAY.format(), session.getAttribute(Field.DT_MAKEFLUSSO_DA_DAY.format()));
			request.setAttribute(Field.DT_MAKEFLUSSO_DA_MONTH.format(), session.getAttribute(Field.DT_MAKEFLUSSO_DA_MONTH.format()));
			request.setAttribute(Field.DT_MAKEFLUSSO_DA_YEAR.format(), session.getAttribute(Field.DT_MAKEFLUSSO_DA_YEAR.format()));
			request.setAttribute(Field.DT_MAKEFLUSSO_A_DAY.format(), session.getAttribute(Field.DT_MAKEFLUSSO_A_DAY.format()));
			request.setAttribute(Field.DT_MAKEFLUSSO_A_MONTH.format(), session.getAttribute(Field.DT_MAKEFLUSSO_A_MONTH.format()));
			request.setAttribute(Field.DT_MAKEFLUSSO_A_YEAR.format(), session.getAttribute(Field.DT_MAKEFLUSSO_A_YEAR.format()));
			request.setAttribute(Field.TIPOLOGIA_FLUSSO.format(), session.getAttribute(Field.TIPOLOGIA_FLUSSO.format()));
			request.setAttribute(Field.SCARTATE_FLUSSO.format(), session.getAttribute(Field.SCARTATE_FLUSSO.format()));
			request.setAttribute(Field.RECUPERATE_FLUSSO.format(), session.getAttribute(Field.RECUPERATE_FLUSSO.format()));
			//fine LP PG200200
			request.setAttribute(Field.TX_CODICE_IUV.format(), session.getAttribute(Field.TX_CODICE_IUV.format()));
		} catch (Exception e) {
			System.out.println("Errore Settaggio filtri : ");
			e.getMessage();
		}
	}
	
	
	
	private String controlloDate(HttpServletRequest request)
	{
		
		String sDataFlussoDa = getDataByPrefix("dtFlusso_da",request);
		String sDataFlussoA = getDataByPrefix("dtFlusso_a",request);
		
		boolean sDataFlussoDaIsNullOrEmpty = sDataFlussoDa==null || sDataFlussoDa.equals("");
		boolean sDataFlussoAIsNullOrEmpty = sDataFlussoA==null || sDataFlussoA.equals("");
		        
        String sDataCreaDa = getDataByPrefix("dtMakeFlusso_da",request);
		String sDataCreaA = getDataByPrefix("dtMakeFlusso_a",request);
		
		boolean sDataCreaDaIsNullOrEmpty = sDataCreaDa==null || sDataCreaDa.equals("");
		boolean sDataCreaAIsNullOrEmpty = sDataCreaA==null || sDataCreaA.equals("");
		
        Calendar dataFlussoDa = parseDate(sDataFlussoDa, "yyyy-MM-dd");
        Calendar dataFlussoA = parseDate(sDataFlussoA, "yyyy-MM-dd");
        
        Calendar dataCreaDa = parseDate(sDataCreaDa, "yyyy-MM-dd");
        Calendar dataCreaA = parseDate(sDataCreaA, "yyyy-MM-dd");
        
        if(sDataFlussoDaIsNullOrEmpty && sDataCreaDaIsNullOrEmpty)
        	return "Data Flusso da e/o Data Creazione Movimento da obbligatoria";
        if(sDataFlussoAIsNullOrEmpty)
        	dataFlussoA = Calendar.getInstance();        
        if (!sDataFlussoDaIsNullOrEmpty && dataFlussoDa.after(dataFlussoA))
        	return "La Data Flusso da deve essere antecedente o uguale alla Data Flusso a";
        if(!sDataFlussoDaIsNullOrEmpty) {
	        dataFlussoDa.add(Calendar.DAY_OF_MONTH, 360);
	        if (dataFlussoDa.before(dataFlussoA))
	        	return "Il massimo range di giorni consentito è di 360 giorni";
        }
        
        if(!sDataCreaDaIsNullOrEmpty && sDataCreaAIsNullOrEmpty)
        	dataCreaA = Calendar.getInstance();                 
        if (!sDataCreaDaIsNullOrEmpty && dataCreaDa.after(dataCreaA))
        	return "La Data Flusso da deve essere antecedente o uguale alla Data Flusso a";
        if(!sDataCreaDaIsNullOrEmpty) {
	        dataCreaDa.add(Calendar.DAY_OF_MONTH, 360);
	        if (dataCreaDa.before(dataCreaA))
	        	return "Il massimo range di giorni consentito è di 360 giorni";
	     }
        
        return null;
        
	}

	// inizio SR PGNTMGR-56
	private EsitoRisposte inserisciPrenotazione(HttpServletRequest request) throws DaoException {
		EsitoRisposte esitoRisposte = new EsitoRisposte();
		String messageDate = controlloDate(request);
		if(messageDate != null) {
			esitoRisposte.setCodiceMessaggio("KO");
			esitoRisposte.setDescrizioneMessaggio(messageDate);
			setFormMessage("riconciliazioneTransazioniNodoForm", messageDate , request);
		} else {
			CallableStatement callableStatement;
			try (Connection conn = payerDataSource.getConnection()) {
				callableStatement = Helper.prepareCall(conn, payerDbSchema, "PYPRESP_INS");
				callableStatement.setString(1, TokenGenerator.generateUUIDToken());
				callableStatement.setString(2, getParamCodiceSocieta() != null ? getParamCodiceSocieta() : "");
				callableStatement.setString(3, getParamCodiceUtente() != null ? getParamCodiceUtente() : "");
				callableStatement.setString(4, getParamCodiceEnte() != null ? getParamCodiceEnte() : "");
				callableStatement.setString(5, new Timestamp(System.currentTimeMillis()).toString());
				callableStatement.setString(6, userBean.getCodiceFiscale() != null ? userBean.getCodiceFiscale() : "");
				callableStatement.setString(7, "FAT");
				callableStatement.setString(8, getDataByPrefix("dtFlusso_da", request) != null ? getDataByPrefix("dtFlusso_da", request) : "");
				callableStatement.setString(9, getDataByPrefix("dtFlusso_a", request) != null ? getDataByPrefix("dtFlusso_a", request) : "" );
				callableStatement.setString(10, "1"); // in attesa
				callableStatement.setString(11, ""); // nome flusso (per batch)
				callableStatement.registerOutParameter(12, Types.VARCHAR);
				callableStatement.registerOutParameter(13, Types.VARCHAR);
				callableStatement.execute();
				esitoRisposte.setCodiceMessaggio(callableStatement.getString(12));
				esitoRisposte.setDescrizioneMessaggio(callableStatement.getString(13));
			} catch (SQLException e) {
				setErrorMessage(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				setErrorMessage(e.getMessage());
				e.printStackTrace();
			}
		}
		return esitoRisposte;
    }
	// fine SR PGNTMGR-56
}
