package org.seda.payer.manager.monitoraggiocup.actions;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.WebRowSet;

import org.apache.commons.lang.StringEscapeUtils;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.string.Convert;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMCSRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMCSResponse;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMICRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMICResponse;
import com.sun.rowset.WebRowSetImpl;

public class BaseMonitoraggioCupAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		
		return null;
	}
	
	public String elaboraXmlListaMip(String listXml, HttpServletRequest request, String nomeForm)
	{
		//inizio LP PG21XX04 Leak
		WebRowSet rowSetNew = null;
		CachedRowSet crsListaOriginale = null;;
		//fine LP PG21XX04 Leak
		try
		{
			//inizio LP PG21XX04 Leak
			//CachedRowSet crsListaOriginale = Convert.stringToWebRowSet(listXml);
			crsListaOriginale = Convert.stringToWebRowSet(listXml);
			//fine LP PG21XX04 Leak
			
			ResultSetMetaData rsMdOriginale = crsListaOriginale.getMetaData();
			int iCols = rsMdOriginale.getColumnCount();
			
			//clono il metadata del rowset originale e aggiungo 4 colonne
			RowSetMetaDataImpl rsMdNew = new RowSetMetaDataImpl();			
			rsMdNew.setColumnCount(iCols+ 2);
			
			for (int i = 1; i <= iCols; i++) {
				rsMdNew.setColumnName(i, rsMdOriginale.getColumnName(i));
				rsMdNew.setColumnType(i, rsMdOriginale.getColumnType(i));
				rsMdNew.setColumnTypeName(i, rsMdOriginale.getColumnTypeName(i));
			}		
			rsMdNew.setColumnName(iCols+1, "PaymentRequestHTML");
			rsMdNew.setColumnType(iCols+1, Types.VARCHAR);
			rsMdNew.setColumnTypeName(iCols+1, "VARCHAR");
			
			rsMdNew.setColumnName(iCols+2, "PaymentDataHTML");
			rsMdNew.setColumnType(iCols+2, Types.VARCHAR);
			rsMdNew.setColumnTypeName(iCols+2, "VARCHAR");
			
			//creo un nuovo webrowSet
			//inizio LP PG21XX04 Leak
			//WebRowSet rowSetNew = new WebRowSetImpl();
			rowSetNew = new WebRowSetImpl();
			//fine LP PG21XX04 Leak
			rowSetNew.setMetaData(rsMdNew);		
					
			if (crsListaOriginale != null) 
			{
				while (crsListaOriginale.next()) 
				{
					rowSetNew.moveToInsertRow();
					// inserisco i valori delle vecchie colonne della riga attuale
					for (int i=1; i<=iCols; i++)
						rowSetNew.updateObject(i, crsListaOriginale.getObject(i));
					
					String paymentRequest = crsListaOriginale.getString(10);
					String paymentData = crsListaOriginale.getString(11);
					
					rowSetNew.updateString(iCols+1, StringEscapeUtils.escapeHtml(getSubstringXml(paymentRequest, 100)).replace("&gt;", "&gt;<br/>").replace("&lt;/", "<br/>&lt;/")); //17.PaymentRequestHTML
					rowSetNew.updateString(iCols+2, StringEscapeUtils.escapeHtml(getSubstringXml(paymentData, 100)).replace("&gt;", "&gt;<br/>").replace("&lt;/", "<br/>&lt;/")); //18.PaymentDataHTML

					rowSetNew.insertRow();
				}
			}
			
			rowSetNew.moveToCurrentRow();
			
			return Convert.webRowSetToString(rowSetNew);
		}
		catch (Exception e)
		{
			setFormMessage(nomeForm, e.getMessage() , request);
		}
		//inizio LP PG21XX04 Leak
		finally {
			try {
				if(crsListaOriginale != null) {
					crsListaOriginale.close();
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
	
	public String elaboraXmlListaMps(String listXml, HttpServletRequest request, String nomeForm)
	{
		//inizio LP PG21XX04 Leak
		WebRowSet rowSetNew = null;
		CachedRowSet crsListaOriginale = null;;
		//fine LP PG21XX04 Leak
		try
		{
			//inizio LP PG21XX04 Leak
			//CachedRowSet crsListaOriginale = Convert.stringToWebRowSet(listXml);
			crsListaOriginale = Convert.stringToWebRowSet(listXml);
			//fine LP PG21XX04 Leak
			
			ResultSetMetaData rsMdOriginale = crsListaOriginale.getMetaData();
			int iCols = rsMdOriginale.getColumnCount();
		
			RowSetMetaDataImpl rsMdNew = new RowSetMetaDataImpl();			
			rsMdNew.setColumnCount(iCols);
			
			for (int i = 1; i <= iCols; i++) {
				rsMdNew.setColumnName(i, rsMdOriginale.getColumnName(i));
				rsMdNew.setColumnType(i, rsMdOriginale.getColumnType(i));
				rsMdNew.setColumnTypeName(i, rsMdOriginale.getColumnTypeName(i));
			}		
			
			//creo un nuovo webrowSet
			//inizio LP PG21XX04 Leak
			//WebRowSet rowSetNew = new WebRowSetImpl();
			rowSetNew = new WebRowSetImpl();
			//fine LP PG21XX04 Leak
			rowSetNew.setMetaData(rsMdNew);		
					
			if (crsListaOriginale != null) 
			{
				while (crsListaOriginale.next()) 
				{
					rowSetNew.moveToInsertRow();
					// inserisco i valori delle vecchie colonne della riga attuale
					for (int i=1; i<=iCols; i++)
					{
						if (i != 9 && i != 10)
							rowSetNew.updateObject(i, crsListaOriginale.getObject(i));
					}
					
					String paymentRequest = crsListaOriginale.getString(9);
					String commitMsg = crsListaOriginale.getString(10);
					
					rowSetNew.updateString(9, StringEscapeUtils.escapeHtml(getSubstringXml(paymentRequest, 100)).replace("&gt;", "&gt;<br/>").replace("&lt;/", "<br/>&lt;/")); 
					rowSetNew.updateString(10, StringEscapeUtils.escapeHtml(getSubstringXml(commitMsg,100)).replace("&gt;", "&gt;<br/>").replace("&lt;/", "<br/>&lt;/")); 

					rowSetNew.insertRow();
				}
			}
			
			rowSetNew.moveToCurrentRow();
			
			return Convert.webRowSetToString(rowSetNew);
		}
		catch (Exception e)
		{
			setFormMessage(nomeForm, e.getMessage() , request);
		}
		//inizio LP PG21XX04 Leak
		finally {
			try {
				if(crsListaOriginale != null) {
					crsListaOriginale.close();
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
	private String getSubstringXml(String sValue, int iLenght)
	{
		if (sValue != null && !sValue.equals("")) 
		{
			if (sValue.startsWith("<?xml"))
			{
				int index = sValue.indexOf("?>");
				sValue = sValue.substring(index + 2, sValue.length());
			}
			if(sValue.length() > iLenght)
				return sValue.substring(0, iLenght) + "...";
		}
		
		return sValue;
	}
	
	public PageInfo getPageInfo(com.seda.payer.pgec.webservice.mip.dati.PageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPage());
		return pageInfo;
	}
	
	/*private String encodeXml(String xmlToEncode)
	{
		//return URLEncoder.encode(xmlToEncode, "utf-8"));
		//return Base64.encode(xmlToEncode.getBytes())
		
		try
		{
			TripleDESChryptoService desChrypto = new TripleDESChryptoService();
			desChrypto.setIv(WSCache.securityIV);
			desChrypto.setKeyValue(WSCache.securityKey);
			return URLEncoder.encode(desChrypto.encryptBASE64(xmlToEncode), "utf-8");
		} catch (Exception e) {}
		
		return "";
	}*/
	
	public void recuperaDettaglioMIC(String chiaveTransazione, String codiceFiscale, String codicePagamento, HttpServletRequest request)
	{
		RecuperaListaMICRequest listaReq = new RecuperaListaMICRequest();
	
		//devo recuperare un solo record
		listaReq.setOrder("");
		listaReq.setPageNumber(1);
		listaReq.setRowsPerPage(100);
		
		listaReq.setCodiceSocieta("");
		//listaReq.setProvincia("");
		listaReq.setCodiceUtente("");
		listaReq.setChiaveEnte("");
		listaReq.setChiaveTransazione(chiaveTransazione);
		listaReq.setCodiceFiscale(codiceFiscale);
		listaReq.setCodicePagamento(codicePagamento);
		listaReq.setNumeroOperazione("");
		listaReq.setNumeroDocumento("");
		listaReq.setEsitoNotifica("ALL");
		listaReq.setDataA("");
		listaReq.setDataDa("");
		
		try {
			RecuperaListaMICResponse res = WSCache.mipServer.recuperaListaMIC(listaReq, request);
			
			if (res != null)
			{
				String lista = elaboraXmlListaMip(res.getListMICXml(), request, "frmMonitoraggioExtDetails");
				request.setAttribute("listaMIC", lista);
			}
			else
				setFormMessage("frmMonitoraggioExtDetails", "Impossibile recuperare i dati della notifica selezionata: " + chiaveTransazione, request);
		} 
		catch (Exception e) {
			setFormMessage("frmMonitoraggioExtDetails", e.getMessage() , request);
		}
	}
	
	public void recuperaListaMCS(String chiaveTransazione, String codiceFiscale, String codicePagamento, HttpServletRequest request)
	{
		RecuperaListaMCSRequest listaReq = new RecuperaListaMCSRequest();
	
		int rowsPerPage = request.getAttribute(Field.ROWS_PER_PAGE.format()) == null ? getDefaultListRows(request) : isNullInt(request.getAttribute(Field.ROWS_PER_PAGE.format()));
		int pageNumber = request.getAttribute(Field.PAGE_NUMBER.format()) == null ? 1 : isNullInt(request.getAttribute(Field.PAGE_NUMBER.format()));
		String order = isNull(request.getAttribute(Field.ORDER_BY.format()));
		
		listaReq.setOrder(order);
		listaReq.setPageNumber(pageNumber);
		listaReq.setRowsPerPage(rowsPerPage);

		listaReq.setChiaveTransazione(chiaveTransazione);
		listaReq.setCodiceFiscale(codiceFiscale);
		listaReq.setCodicePagamento(codicePagamento);
		
		try {
			RecuperaListaMCSResponse listaMCS = WSCache.mipServer.recuperaListaMCS(listaReq, request);
			
			if(listaMCS != null)
			{
				if(listaMCS.getRetCode().equals("00"))
				{
					PageInfo pageInfo = getPageInfo(listaMCS.getPageInfo());
					if(pageInfo != null)
					{
						if(pageInfo.getNumRows() > 0)
						{
							String lista = elaboraXmlListaMps(listaMCS.getListMCSXml(), request, "frmMonitoraggioExtDetails");
							request.setAttribute("listaMCS", lista);
							request.setAttribute("listaMCS.pageInfo", pageInfo);
						}
						//else 
							//setFormMessage("frmMonitoraggioExtDetails", Messages.NO_DATA_FOUND.format(), request);
					}
					else 
						setFormMessage("frmMonitoraggioExtDetails", "Errore generico - PageInfo null", request);
				}
				else
					setFormMessage("frmMonitoraggioExtDetails", listaMCS.getRetMessage() , request);
			}
			else 
				setFormMessage("frmMonitoraggioExtDetails", "Errore durante il recupero dei dati richiesti." , request);

		} 
		catch (Exception e) {
			e.printStackTrace();
			setFormMessage("frmMonitoraggioExtDetails", e.getMessage() , request);
		}
	}
}
