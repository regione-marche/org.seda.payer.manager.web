package org.seda.payer.manager.richiesteelaborazioni.actions;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.data.dao.DAOHelper;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.dati.*;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.sun.rowset.WebRowSetImpl;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.WebRowSet;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class BaseFatturazioneAction extends BaseManagerAction {
	private static final long serialVersionUID = 1L;

	protected String codiceSocieta="";
	protected String codiceProvincia="";
	protected String codiceUtente="";
	protected String payerDbSchema = null;
	protected DataSource payerDataSource = null;
	protected WebRowSetImpl webRowSetImpl = null;

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		setProfile(request);
		HttpSession session = request.getSession();
		loadDDLStatic(request, session);

		PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		String dataSourceName =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
		this.payerDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		try {
			this.payerDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));
		} catch (ServiceLocatorException e) {
			throw new ActionException("ServiceLocator error " + e.getMessage(),e);
		}
		return null;
	}

	protected void tx_SalvaStato(HttpServletRequest request) {
		super.tx_SalvaStato(request);
	}

	protected void aggiornamentoCombo(HttpServletRequest request, HttpSession session) {

		switch(getFiredButton(request)) {
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

				setParamCodiceSocieta("");
				setParamCodiceUtente("");
								
				loadProvinciaXml_DDL(request, session, "",true);
				loadDDLUtente(request, session, getParamCodiceSocieta(), "",true);
				loadListaGatewayXml_DDL(request, session, "", "", true);
				break;
				
			default:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				loadDDLUtente(request, session, getParamCodiceSocieta(), codiceProvincia,true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
		}
	}

	protected void loadWebRowSet(ResultSet resultSet) throws SQLException {
		// if rs is null means that there are no more results
		if (resultSet == null) {
			throw new SQLException(com.seda.data.helper.Messages.ARGUMENT_NULL.format("ResultSet"));
		}
		try {
			// define a new cached rowset and fill data
			webRowSetImpl = new WebRowSetImpl();
			webRowSetImpl.populate(resultSet);
		} finally {
			DAOHelper.closeIgnoringException(resultSet);
		}
	}

	protected String elaboraXmlList(String listXml, HttpServletRequest request, String nomeForm) {
		WebRowSet rowSetNew = null;
		CachedRowSet crsListaOriginale = null;
		try {
			crsListaOriginale = Convert.stringToWebRowSet(listXml);
			ResultSetMetaData rsMdOriginale = crsListaOriginale.getMetaData();
			int iCols = rsMdOriginale.getColumnCount();

			RowSetMetaDataImpl rsMdNew = new RowSetMetaDataImpl();
			rsMdNew.setColumnCount(iCols);

			for (int i = 1; i <= iCols; i++) {
				rsMdNew.setColumnName(i, rsMdOriginale.getColumnName(i));
				rsMdNew.setColumnType(i, rsMdOriginale.getColumnType(i));
				rsMdNew.setColumnTypeName(i, rsMdOriginale.getColumnTypeName(i));
			}
			rowSetNew = new WebRowSetImpl();
			rowSetNew.setMetaData(rsMdNew);

			if (crsListaOriginale != null) {
				while (crsListaOriginale.next()) {
					rowSetNew.moveToInsertRow();
					// inserisco i valori delle vecchie colonne della riga attuale
					for (int i=1; i<=iCols; i++) {
						if (i != 6)
							rowSetNew.updateObject(i, crsListaOriginale.getObject(i));
					}

					String stato = crsListaOriginale.getString(6);
					rowSetNew.updateString(6, stato.equals("1") ? "In elaborazione" : "Terminata");
					rowSetNew.insertRow();
				}
			}
			rowSetNew.moveToCurrentRow();
			return Convert.webRowSetToString(rowSetNew);
		} catch (Exception e)  {
			setFormMessage(nomeForm, e.getMessage() , request);
		} finally {
			try {
				if(crsListaOriginale != null)  crsListaOriginale.close();
			} catch (SQLException e) {
				setFormMessage(nomeForm, e.getMessage() , request);
			}
			try {
				if(rowSetNew != null)  rowSetNew.close();
			} catch (SQLException e) {
				setFormMessage(nomeForm, e.getMessage() , request);
			}
		}
		return "";
	}

}
