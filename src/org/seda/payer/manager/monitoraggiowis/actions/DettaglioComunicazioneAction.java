package org.seda.payer.manager.monitoraggiowis.actions;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.impostasoggiorno.webservice.dati.DettaglioComunicazioneImpostaSoggiorno;
import com.seda.payer.impostasoggiorno.webservice.dati.TestataComunicazioneImpostaSoggiorno;
import com.seda.payer.impostasoggiorno.webservice.wis.dati.RecuperaDettaglioComunicazioneAggregatoRequest;
import com.seda.payer.impostasoggiorno.webservice.wis.dati.RecuperaDettaglioComunicazioneAggregatoResponse;
import com.sun.rowset.WebRowSetImpl;



public class DettaglioComunicazioneAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);

		//recupero la comunicazione dal db
		String chiaveTestataComunicazione = (String)request.getAttribute("chiave_comunicazione");
		String comune = (String)request.getAttribute("comune");
		String numeroAutorizzazione = (String)request.getAttribute("numaut"); 
		
		recuperaComunicazione(chiaveTestataComunicazione, request);
		
		request.setAttribute("comune", comune);
		request.setAttribute("numeroautorizzazione", numeroAutorizzazione);
		return null;
	}
	
	private void recuperaComunicazione(String chiaveTestataComunicazione, HttpServletRequest request)
	{
		try {
			RecuperaDettaglioComunicazioneAggregatoRequest req = new RecuperaDettaglioComunicazioneAggregatoRequest();
			req.setChiaveTestataComunicazione(chiaveTestataComunicazione);
			
			RecuperaDettaglioComunicazioneAggregatoResponse res = WSCache.impostaSoggiornoServer.recuperaDettaglioComunicazione(req, request);
			
			if (res != null)
			{
				if (res.getRetCode().equals("00"))
				{
					request.setAttribute("testatacomunicazione", res.getTestataComunicazione());
					request.setAttribute("tariffacomunicazione", res.getTariffaImpostaSoggiorno());
					request.setAttribute("tipologiastrutturacomunicazione", res.getTipologiaStrutturaRicettiva());
					request.setAttribute("anagraficastrutturacomunicazione", res.getAnagraficaStrutturaRicettiva());
					setDateToRequest(res.getTestataComunicazione(), request);
					
					String listDettaglioComunicazione = elaboraDettagli(res.getListDettagliComunicazioneXml(), res.getListDettagliComunicazione());
					request.setAttribute("listdettaglicomunicazione", listDettaglioComunicazione);
				}	
				else
					WSCache.logWriter.logInfo("recuperaComunicazione - ERRORE: " + res.getRetMessage());
			}
			else
				WSCache.logWriter.logInfo("recuperaComunicazione - ERRORE");
			
		} catch (Exception e) {
			WSCache.logWriter.logError("recuperaComunicazione - ERRORE", e);
		}
	}
	
	private void setDateToRequest(TestataComunicazioneImpostaSoggiorno testata, HttpServletRequest request)
	{
		request.setAttribute("dataInizioComunicazione", GenericsDateNumbers.formatCalendarData(GenericsDateNumbers.getCalendarFromDate(testata.getDataInizioComunicazione()), "dd/MM/yyyy"));
		request.setAttribute("dataFineComunicazione", GenericsDateNumbers.formatCalendarData(GenericsDateNumbers.getCalendarFromDate(testata.getDataFineComunicazione()), "dd/MM/yyyy"));
		request.setAttribute("dataInserimentoComunicazione", GenericsDateNumbers.formatCalendarData(testata.getDataInserimentoComunicazione(), "dd/MM/yyyy"));
		request.setAttribute("dataScadenzaComunicazione", GenericsDateNumbers.formatCalendarData(GenericsDateNumbers.getCalendarFromDate(testata.getDataScadenzaComunicazione()), "dd/MM/yyyy"));
		String dataPagamento = GenericsDateNumbers.formatCalendarData(GenericsDateNumbers.getCalendarFromDate(testata.getDataPagamento()), "dd/MM/yyyy");
		request.setAttribute("dataPagamento", dataPagamento.equals("01/01/1000") ? "" : dataPagamento);
		String dataConfermaCom = GenericsDateNumbers.formatCalendarData(testata.getDataConfermaComunicazione(), "dd/MM/yyyy");
		request.setAttribute("dataConfermaCom", dataConfermaCom.equals("01/01/1000") ? "" : dataConfermaCom);
	}
	
	private String elaboraDettagli(String listDettagliComunicazioneXml, DettaglioComunicazioneImpostaSoggiorno[] aDettagli)
	{
		int iTotaleGiorniPermanenza = 0;
		int iTotaleGiorniPagamento = 0;
		int iNumeroPersone = 0;
		BigDecimal bdImporto = BigDecimal.ZERO;
		
		//calcolo i totali
		for (DettaglioComunicazioneImpostaSoggiorno dettaglio : aDettagli)
		{
			iTotaleGiorniPermanenza += dettaglio.getNumeroGiorniPermanenzaTotale();
			iTotaleGiorniPagamento += dettaglio.getNumeroGiorniPermanenzaPagamento();
			iNumeroPersone += dettaglio.getNumeroPersone();
			bdImporto = bdImporto.add(dettaglio.getImportoDaPagare());
		}
		
		//inizio LP PG21XX04 Leak
		WebRowSet rowSetNew = null;
		WebRowSet wrs = null;;
		//fine LP PG21XX04 Leak
		//rielaboro il webRowSet
		try {
		
			//inizio LP PG21XX04 Leak
			//WebRowSet rowSetNew = new WebRowSetImpl();
			rowSetNew = new WebRowSetImpl();
			//fine LP PG21XX04 Leak
			RowSetMetaDataImpl rsMdDataNew = new RowSetMetaDataImpl();	
			//inizio LP PG1800XX_016
			//rsMdDataNew.setColumnCount(6);
			rsMdDataNew.setColumnCount(8);
			//fine LP PG1800XX_016
			rsMdDataNew.setColumnType(1, Types.VARCHAR);
			rsMdDataNew.setColumnType(2, Types.VARCHAR);
			rsMdDataNew.setColumnType(3, Types.VARCHAR);
			rsMdDataNew.setColumnType(4, Types.VARCHAR);
			rsMdDataNew.setColumnType(5, Types.VARCHAR);
			rsMdDataNew.setColumnType(6, Types.VARCHAR);
			//inizio LP PG1800XX_016
			rsMdDataNew.setColumnType(7, Types.VARCHAR);
			rsMdDataNew.setColumnType(8, Types.VARCHAR);
			//fine LP PG1800XX_016
			rsMdDataNew.setColumnTypeName(1, "VARCHAR");
			rsMdDataNew.setColumnTypeName(2, "VARCHAR");
			rsMdDataNew.setColumnTypeName(3, "VARCHAR");
			rsMdDataNew.setColumnTypeName(4, "VARCHAR");
			rsMdDataNew.setColumnTypeName(5, "VARCHAR");
			rsMdDataNew.setColumnTypeName(6, "VARCHAR");
			//inizio LP PG1800XX_016
			rsMdDataNew.setColumnTypeName(7, "VARCHAR");
			rsMdDataNew.setColumnTypeName(8, "VARCHAR");
			//fine LP PG1800XX_016
			rowSetNew.setMetaData(rsMdDataNew);	
			
			//inizio LP PG21XX04 Leak
			//WebRowSet wrs = Convert.stringToWebRowSet(listDettagliComunicazioneXml);
			wrs = Convert.stringToWebRowSet(listDettagliComunicazioneXml);
			//fine LP PG21XX04 Leak
			while (wrs.next())
			{
				rowSetNew.moveToInsertRow();
				rowSetNew.updateString(1, wrs.getString(13)); //descrizione soggetto
				rowSetNew.updateString(2, String.valueOf(wrs.getInt(4))); //giorni permanenza
				rowSetNew.updateString(3, String.valueOf(wrs.getInt(5))); //giorni pagamento
				rowSetNew.updateString(4, String.valueOf(wrs.getInt(6))); //numero persone
				rowSetNew.updateString(5, GenericsDateNumbers.formatDecimalNumber(wrs.getBigDecimal(7)));
				rowSetNew.updateString(6, wrs.getString(1)); //chiave comunicazione
				//inizio LP PG1800XX_016
				if(wrs.getString(19).equals("")) {
					rowSetNew.updateString(7, ""); //importo fascia tariffa
					rowSetNew.updateString(8, ""); //codice fascia tariffa
				} else {
					rowSetNew.updateString(7, GenericsDateNumbers.formatDecimalNumber(wrs.getBigDecimal(18))); //importo fascia tariffa
					rowSetNew.updateString(8, wrs.getString(19)); //codice fascia tariffa
				}
				//fine LP PG1800XX_016
				rowSetNew.insertRow();
			}
			
			rowSetNew.moveToInsertRow();
			rowSetNew.updateString(1, "<b>Totali</b>");
			rowSetNew.updateString(2, "<b>" + String.valueOf(iTotaleGiorniPermanenza) + "</b>");
			rowSetNew.updateString(3, "<b>" + String.valueOf(iTotaleGiorniPagamento) + "</b>");
			rowSetNew.updateString(4, "<b>" + String.valueOf(iNumeroPersone) + "</b>");
			rowSetNew.updateString(5, "<b>" + GenericsDateNumbers.formatDecimalNumber(bdImporto) + "</b>");
			rowSetNew.updateString(6, "");
			//inizio LP PG1800XX_016
			rowSetNew.updateString(7, "");
			rowSetNew.updateString(8, "");
			//fine LP PG1800XX_016
			rowSetNew.insertRow();
			
			rowSetNew.moveToCurrentRow();
			
			return Convert.webRowSetToString(rowSetNew);
			
		} catch (Exception e) {
			WSCache.logWriter.logError("elaboraDettagli", e);
		}
		//inizio LP PG21XX04 Leak
		finally {
			try {
				if(wrs != null) {
					wrs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(rowSetNew != null) {
					rowSetNew.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return "";
		
	}
}
