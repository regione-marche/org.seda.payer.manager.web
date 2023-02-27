package org.seda.payer.manager.blackboxconfig.actions;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;


import com.seda.commons.string.Convert;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.bancadati.webservice.srv.FaultType;
import com.seda.payer.core.bean.BlackBoxPosLog;
import com.seda.payer.core.bean.BlackBoxPosPagelist;
import com.seda.payer.core.bean.ConfigurazioneBlackBoxPos;
import com.seda.payer.core.dao.BlackBoxDAOFactory;
import com.seda.payer.core.dao.ConfigurazioneBlackBoxDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaEnteUtenteSocietaAneRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaEnteUtenteSocietaAneResponse;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaResponse;
import com.sun.rowset.WebRowSetImpl;

public class BlackBoxPosAction extends BlackBoxBaseManagerAction{

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber; 
	
	NumberFormat df2 = new DecimalFormat("#0.00");    
	
	ConfigurazioneBlackBoxPos configurazioneBlackBoxPosAppoggio = new ConfigurazioneBlackBoxPos();
	String updateMes = "";
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		super.service(request);
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
			setProfile(request);
		
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		
		System.out.println("paramCodiceEnte=" + paramCodiceEnte);
		
		if(paramCodiceEnte!=null && !paramCodiceEnte.equals("")) {
			try
			{
				RecuperaEnteUtenteSocietaAneResponse res = WSCache.commonsServer.recuperaEnteUtenteSocieta_Ane(new RecuperaEnteUtenteSocietaAneRequest(paramCodiceEnte), request);
				if (res != null)
				{
					if (res.getRetCode().equals("00") && !res.getListXml().equals(""))
					{
						WebRowSet wrs = Convert.stringToWebRowSet(res.getListXml());
						if (wrs != null) 
						{
							if (wrs.next()) 
							{
								request.setAttribute("tx_idEnte",wrs.getString(4).trim()); //codice ente
							}
							else
							{
								System.out.println("Chiave ENTE " + paramCodiceEnte + " non configurata");
							}
							//inizio LP PG21XX04 Leak pagonet web
							try {
								wrs.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}	
							wrs = null;
							
						}
					}
					else 
					{
						System.out.println("Chiave ENTE " + paramCodiceEnte + " non configurata");
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				
			}	
		}
		
		
		
		switch(firedButton) {
		
			case TX_BUTTON_CERCA: {
				mantieniFiltriRicerca(request);
				//inizio LP PG22XX02
				recuperaFiltriRicerca(request);
				//fine LP PG22XX02
				list(request);
				//inizio LP PG200370
				//recuperaFiltriRicerca(request);
				//fine LP PG200370
			}; break;
				
			case TX_BUTTON_INDIETRO: {
				request.setAttribute("codOp", "");
				request.setAttribute("tx_idDominio_hidden", "");
				request.setAttribute("tx_idEnte_hidden", "");
				request.setAttribute("tx_numeroAvviso_hidden", "");
				request.setAttribute("tx_idIuv_hidden", "");
				recuperaFiltriRicerca(request);
				list(request);
			}; break;
				
			case TX_BUTTON_RESET: {
				resetParametri(request);
				setProfile(request);
				resetFiltriRicerca(request);
				list(request);
			}; break;
				
			case TX_BUTTON_NULL: {
				//inizio LP PG200370
				getTassonomie(request);
				//fine LP PG200370
				String codOp = request.getParameter("codOp")!=null?((String)request.getParameter("codOp")).trim():"";
				//passaggio dei dati hidden per Edit e Delete
				request.setAttribute("codOp", codOp);
				request.setAttribute("tx_idDominio_hidden", request.getAttribute("tx_idDominio_hidden")!=null?request.getAttribute("tx_idDominio_hidden"):"");
				request.setAttribute("tx_idEnte_hidden", request.getAttribute("tx_idEnte_hidden")!=null?request.getAttribute("tx_idEnte_hidden"):"");
				request.setAttribute("tx_numeroAvviso_hidden", request.getAttribute("tx_numeroAvviso_hidden")!=null?request.getAttribute("tx_numeroAvviso_hidden"):"");
				request.setAttribute("tx_idIuv_hidden", request.getAttribute("tx_idIuv_hidden")!=null?request.getAttribute("tx_idIuv_hidden"):"");
				/*if(codOp.equals("")) {
					recuperaFiltriRicerca(request);
					list(request);
				} else */if(codOp.equals("edit")) {
					select(request);
				};
			}; break;
				
			case TX_BUTTON_EDIT: {
				update(request);
				//inizio LP PG200360
				setFiltriRicerca(request);
				//fine LP PG200360
				recuperaFiltriRicerca(request);
				list(request);
			}; break;
			
			case TX_BUTTON_DELETE : {
				delete(request);
				recuperaFiltriRicerca(request);
				list(request);
			}; break;
			
		}
		return null;
	}
	
	private void select(HttpServletRequest request) {
		ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO;
		ConfigurazioneBlackBoxPos configurazioneBlackBoxPos = new ConfigurazioneBlackBoxPos();
		configurazioneBlackBoxPos.setCodiceIdentificativoDominio(((String)request.getParameter("tx_idDominio_hidden")).trim());
		configurazioneBlackBoxPos.setCodiceEnte(((String)request.getParameter("tx_idEnte_hidden")).trim());
		configurazioneBlackBoxPos.setNumeroAvviso(((String)request.getParameter("tx_numeroAvviso_hidden")).trim());
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
			connection = getBlackBoxDataSource().getConnection();
			configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
			//fine LP PG21XX04 Leak
			configurazioneBlackBoxPos = configurazioneBlackBoxDAO.select(configurazioneBlackBoxPos);
		} catch (DaoException e1) {
			e1.printStackTrace();
		//inizio LP PG21XX04 Leak
		//}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		//inizio LP PG200370
		Double importo = null;
		if(configurazioneBlackBoxPos.getImporto() != null) {
			importo = new Double(configurazioneBlackBoxPos.getImporto() / 100D);
		} else {
			importo = new Double("0.00");
		}
		//fine LP PG200370
		request.setAttribute("tx_idDominio", configurazioneBlackBoxPos.getCodiceIdentificativoDominio());
		request.setAttribute("tx_idEnte", configurazioneBlackBoxPos.getCodiceEnte());
		request.setAttribute("tx_numeroAvviso", configurazioneBlackBoxPos.getNumeroAvviso());
		request.setAttribute("tx_idFlusso", configurazioneBlackBoxPos.getCodiceIdentificativoFlusso());
		request.setAttribute("tx_dataCreazione", configurazioneBlackBoxPos.getDataCreazione());
		request.setAttribute("tx_tipoRecord", configurazioneBlackBoxPos.getTipoRecord());
		request.setAttribute("tx_idDocumento", configurazioneBlackBoxPos.getCodiceIdentificativoDocumento());
		request.setAttribute("tx_numRata", configurazioneBlackBoxPos.getNumeroRata());
		request.setAttribute("tx_dataScadenza", configurazioneBlackBoxPos.getDataScadenza());
		request.setAttribute("tx_codFisc", configurazioneBlackBoxPos.getCodiceFiscale());
		//inizio LP PG200370
		//request.setAttribute("tx_importo", df2.format(configurazioneBlackBoxPos.getImporto()));
		request.setAttribute("tx_importo", df2.format(importo));
		//fine LP PG200370
		request.setAttribute("tx_denDeb", configurazioneBlackBoxPos.getDenominazioneDebitore());
		request.setAttribute("tx_indContrib", configurazioneBlackBoxPos.getIndirizzoContribuente());
		request.setAttribute("tx_locContrib", configurazioneBlackBoxPos.getLocalitaContribuente());
		request.setAttribute("tx_provContrib", configurazioneBlackBoxPos.getProvinciaContribuente());
		request.setAttribute("tx_flagAnnullamento", configurazioneBlackBoxPos.getFlagAnnullamento());
		request.setAttribute("tx_dataAggRecord", configurazioneBlackBoxPos.getDataAggiornamentoRecord());
		request.setAttribute("tx_ibanAccr", configurazioneBlackBoxPos.getCodiceIbanAccredito());
		request.setAttribute("tx_idIuv", configurazioneBlackBoxPos.getCodiceIuv());
		//inizio LP PG200370
		request.setAttribute("tx_tassonomia", configurazioneBlackBoxPos.getTassonomia());
		//fine LP PG200370
		
		/*Dopo la select salvo i valori in un oggetto d' appoggio per fare dei compare durante la creazione del log*/
		salvaDatiAppoggio(configurazioneBlackBoxPos, request);
		
	}
	
	private void delete(HttpServletRequest request){
		ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO;
		ConfigurazioneBlackBoxPos configurazioneBlackBoxPos = new ConfigurazioneBlackBoxPos();
		configurazioneBlackBoxPos.setCodiceIdentificativoDominio(((String)request.getParameter("tx_idDominio_hidden")).trim());
		configurazioneBlackBoxPos.setCodiceEnte(((String)request.getParameter("tx_idEnte_hidden")).trim());
		configurazioneBlackBoxPos.setNumeroAvviso(((String)request.getAttribute("tx_numeroAvviso_hidden")).trim());
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
			connection = getBlackBoxDataSource().getConnection();
			configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
			//fine LP PG21XX04 Leak

			EsitoRisposte esito = configurazioneBlackBoxDAO.delete(configurazioneBlackBoxPos);
			
			if(esito!=null) {
				setFormMessage("form_selezione", esito.getDescrizioneMessaggio(), request);
			} else {
				setFormMessage("form_selezione", "Errore nell'eliminazione della posizione", request);
			}
			
			/*Quando viene eliminata una posizione, viene inserito un log che indica quale posizione e' stata eliminata*/
			if(esito.getCodiceMessaggio().toString().equals("OK")) {
				String message = "Posizione con Codice Identificativo Dominio "+configurazioneBlackBoxPos.getCodiceIdentificativoDominio()+", Codice Ente "+configurazioneBlackBoxPos.getCodiceEnte()+" e Numero Avviso "+configurazioneBlackBoxPos.getNumeroAvviso()+" Eliminata.";
				insertLog(configurazioneBlackBoxPos, message);
			}
			
		} catch (DaoException e1) {
			e1.printStackTrace();
		//inizio LP PG21XX04 Leak
		//}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
	}
	
	private void update(HttpServletRequest request) {
		
		ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO;
		ConfigurazioneBlackBoxPos configurazioneBlackBoxPos = new ConfigurazioneBlackBoxPos();
		
		configurazioneBlackBoxPos.setCodiceIdentificativoDominio((String)request.getAttribute("tx_idDominio_hidden"));
		configurazioneBlackBoxPos.setCodiceEnte((String)request.getAttribute("tx_idEnte_hidden"));
		configurazioneBlackBoxPos.setNumeroAvviso((String)request.getAttribute("tx_numeroAvviso_hidden"));
		configurazioneBlackBoxPos.setCodiceIuv((String)request.getAttribute("tx_idIuv_hidden"));
		
		configurazioneBlackBoxPos.setCodiceIdentificativoFlusso(request.getAttribute("tx_idFlusso")!=null?((String)request.getAttribute("tx_idFlusso")).trim():"");
		configurazioneBlackBoxPos.setDataCreazione(getDate(request, "tx_dataCreazione"));
		configurazioneBlackBoxPos.setTipoRecord(request.getAttribute("tx_tipoRecord")!=null?((String)request.getAttribute("tx_tipoRecord")).trim():"");
		configurazioneBlackBoxPos.setCodiceIdentificativoDocumento(request.getAttribute("tx_idDocumento")!=null?((String)request.getAttribute("tx_idDocumento")).trim():"");
		configurazioneBlackBoxPos.setNumeroRata(request.getAttribute("tx_numRata")!=null?((String)request.getAttribute("tx_numRata")).trim():"");
		configurazioneBlackBoxPos.setDataScadenza(getDate(request, "tx_dataScadenza"));
		configurazioneBlackBoxPos.setCodiceFiscale(request.getAttribute("tx_codFisc")!=null?((String)request.getAttribute("tx_codFisc")).trim():"");
		//inizio LP PG200360
		//configurazioneBlackBoxPos.setImporto(request.getAttribute("tx_importo")!=null?new Double(((String)request.getAttribute("tx_importo")).replace(",", ".")):new Double("0.00"));
		Double importo = null;
		if(request.getAttribute("tx_importo") != null) {
			String appo = (String) request.getAttribute("tx_importo");
			appo = appo.replace(",", ".");
			importo = new Double(Double.valueOf(appo).doubleValue() * 100D);
		} else {
			importo = new Double("0.00");
		}
		configurazioneBlackBoxPos.setImporto(importo);
		configurazioneBlackBoxPos.setDenominazioneDebitore(request.getAttribute("tx_denDeb")!=null?((String)request.getAttribute("tx_denDeb")).trim():"");
		configurazioneBlackBoxPos.setIndirizzoContribuente(request.getAttribute("tx_indContrib")!=null?((String)request.getAttribute("tx_indContrib")).trim():"");
		configurazioneBlackBoxPos.setLocalitaContribuente(request.getAttribute("tx_locContrib")!=null?((String)request.getAttribute("tx_locContrib")).trim():"");
		configurazioneBlackBoxPos.setProvinciaContribuente(request.getAttribute("tx_provContrib")!=null?((String)request.getAttribute("tx_provContrib")).trim():"");
		configurazioneBlackBoxPos.setFlagAnnullamento(request.getAttribute("tx_flagAnnullamento")!=null?((String)request.getAttribute("tx_flagAnnullamento")).trim():"");
		//configurazioneBlackBoxPos.setDataAggiornamentoRecord(getDate(request, "tx_dataAggRecord"));
		configurazioneBlackBoxPos.setCodiceIbanAccredito(request.getAttribute("tx_ibanAccr")!=null?((String)request.getAttribute("tx_ibanAccr")).trim():"");
		configurazioneBlackBoxPos.setFlagPagato(request.getAttribute("tx_flagPagato")!=null?((String)request.getAttribute("tx_flagPagato")).trim():"");
		//inizio LP PG200370
		configurazioneBlackBoxPos.setTassonomia(request.getAttribute("tx_tassonomia") != null ? ((String) request.getAttribute("tx_tassonomia")).trim() : "");
		//fine LP PG200370
		

		//Prima di eseguire l'update, controllo se ci sono differenze tra i vecchi e i nuovi dati
		compareDatiAppoggio(configurazioneBlackBoxPos, request);
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
			connection = getBlackBoxDataSource().getConnection();
			configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
			//fine LP PG21XX04 Leak
			int esito = configurazioneBlackBoxDAO.update(configurazioneBlackBoxPos);
			
			if(esito > 0) {
				setFormMessage("form_selezione", "Aggiornamento posizione eseguito", request);
				/*Quando viene eliminata una posizione, viene inserito un log che indica quale posizione e' stata eliminata*/
				if(updateMes.length()>0) {
					insertLog(configurazioneBlackBoxPos, updateMes);
					//Inizializzazione per update futuro
					updateMes="";
				}
			} else {
				setFormMessage("form_selezione", "Errore nell'aggiornamento della posizione", request);
			}
			
		} catch (DaoException e1) {
			e1.printStackTrace();
		//inizio LP PG21XX04 Leak
		//}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
	}
	
	private void list(HttpServletRequest request){
		try {	
			
			ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO;
			ConfigurazioneBlackBoxPos configurazioneBlackBoxPos = new ConfigurazioneBlackBoxPos();
			
			if(getUserBean().getUserBeanSupport(request).getProfile().equals("AMEN")){
				System.out.println("AMEN");
				configurazioneBlackBoxPos.setCodiceIdentificativoDominio(request.getAttribute("tx_idDominio")!=null?((String)request.getAttribute("tx_idDominio")).trim():"");
				configurazioneBlackBoxPos.setCodiceIdentificativoDocumento(request.getAttribute("tx_idDocumento")!=null?((String)request.getAttribute("tx_idDocumento")).trim():"");
				configurazioneBlackBoxPos.setCodiceEnte(request.getAttribute("tx_idEnte")!=null?((String)request.getAttribute("tx_idEnte")).trim():"");
				configurazioneBlackBoxPos.setCodiceFiscale(request.getAttribute("tx_codFisc")!=null?((String)request.getAttribute("tx_codFisc")).trim():"");
				configurazioneBlackBoxPos.setNumeroAvviso(request.getAttribute("tx_numeroAvviso")!=null?((String)request.getAttribute("tx_numeroAvviso")).trim():"");
				configurazioneBlackBoxPos.setDataCreazione(getDate(request, "tx_dataCreazione"));
				configurazioneBlackBoxPos.setFlagPagato(request.getAttribute("tx_flagPagato")!=null?((String)request.getAttribute("tx_flagPagato")).trim():"");
				configurazioneBlackBoxPos.setAnnoRiferimento((request.getAttribute("tx_annoRif")!=null && request.getAttribute("tx_annoRif").toString().trim().length()>0) ? new Integer(request.getAttribute("tx_annoRif").toString().trim()):0);
			} else {
				configurazioneBlackBoxPos.setCodiceIdentificativoDominio(request.getAttribute("tx_idDominio")!=null?((String)request.getAttribute("tx_idDominio")).trim():"");
				configurazioneBlackBoxPos.setCodiceIdentificativoDocumento(request.getAttribute("tx_idDocumento")!=null?((String)request.getAttribute("tx_idDocumento")).trim():"");
				configurazioneBlackBoxPos.setCodiceEnte(request.getAttribute("tx_idEnte")!=null?((String)request.getAttribute("tx_idEnte")).trim():"");
				configurazioneBlackBoxPos.setCodiceFiscale(request.getAttribute("tx_codFisc")!=null?((String)request.getAttribute("tx_codFisc")).trim():"");
				configurazioneBlackBoxPos.setNumeroAvviso(request.getAttribute("tx_numeroAvviso")!=null?((String)request.getAttribute("tx_numeroAvviso")).trim():"");
				configurazioneBlackBoxPos.setDataCreazione(getDate(request, "tx_dataCreazione"));
				configurazioneBlackBoxPos.setFlagPagato(request.getAttribute("tx_flagPagato")!=null?((String)request.getAttribute("tx_flagPagato")).trim():"T");
				configurazioneBlackBoxPos.setAnnoRiferimento((request.getAttribute("tx_annoRif")!=null && request.getAttribute("tx_annoRif").toString().trim().length()>0) ? new Integer(request.getAttribute("tx_annoRif").toString().trim()):0);
			}
			
			BlackBoxPosPagelist blackBoxPosPagelist = new BlackBoxPosPagelist();
			//inizio LP PG21XX04 Leak
			Connection connection = null;
			//fine LP PG21XX04 Leak
			try {
				//inizio LP PG21XX04 Leak
				//configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
				connection = getBlackBoxDataSource().getConnection();
				configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
				//fine LP PG21XX04 Leak
				blackBoxPosPagelist = configurazioneBlackBoxDAO.blackboxposList(configurazioneBlackBoxPos, rowsPerPage, pageNumber,"");
			} catch (DaoException e1) {
				e1.printStackTrace();
			//inizio LP PG21XX04 Leak
			//}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			//fine LP PG21XX04 Leak
			
			PageInfo pageInfo = blackBoxPosPagelist.getPageInfo();
			if (blackBoxPosPagelist.getRetCode()!="00") {
				setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			} else {
				if(pageInfo != null) {
					if(pageInfo.getNumRows() > 0) {
						//inizio LP PG200370
						String newlist = rielaboraListaBlackBox(blackBoxPosPagelist.getblackboxposListXml());
						blackBoxPosPagelist.setblackboxposListXml(newlist);
						//fine LP PG200370
						request.setAttribute("lista_blackbox", blackBoxPosPagelist.getblackboxposListXml());
						request.setAttribute("lista_blackbox.pageInfo", pageInfo);
					} else {
						request.setAttribute("lista_blackbox", null);
						
						setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
					}
				} else { 
					setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
				}
			}
		} catch(Exception e) {
			setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private Calendar getDate(HttpServletRequest request, String fieldName) {
		Calendar cal = new GregorianCalendar();
//inizio LP PG22XX02		
//		String gg="", mm="", aaaa="";
//fine LP PG22XX02
		//inizio LP PG200370
		Calendar appo = (Calendar) request.getAttribute(fieldName); 
		if(appo != null) { 
		//fine LP PG200370
//inizio LP PG22XX02
//		gg = request.getAttribute(fieldName+"_day")!=null?(String)request.getAttribute(fieldName+"_day"):"";
//		mm = request.getAttribute(fieldName+"_month")!=null?(String)request.getAttribute(fieldName+"_month"):"";
//		aaaa = request.getAttribute(fieldName+"_year")!=null?(String)request.getAttribute(fieldName+"_year"):"";
//		//inizio LP PG200370
//		}
//		//fine LP PG200370
//		if(!gg.equals("") && !mm.equals("") && !aaaa.equals("")) {
//	        java.sql.Date date = java.sql.Date.valueOf(aaaa+"-"+mm+"-"+gg);
//	        cal.setTime(date);
			cal.setTime(appo.getTime());
//fine LP PG22XX02
		} else {
			cal.clear();
		}
        return cal;
	}

	private void mantieniFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(request.getAttribute("tx_idDominio")!=null)
			session.setAttribute("tx_idDominio", request.getAttribute("tx_idDominio"));
		if(request.getAttribute("tx_idDocumento")!=null)
			session.setAttribute("tx_idDocumento", request.getAttribute("tx_idDocumento"));
		if(request.getAttribute("tx_idEnte")!=null)
			session.setAttribute("tx_idEnte", request.getAttribute("tx_idEnte"));
		if(request.getAttribute("tx_codFisc")!=null)
			session.setAttribute("tx_codFisc", request.getAttribute("tx_codFisc"));
		if(request.getAttribute("tx_numeroAvviso")!=null)
			session.setAttribute("tx_numeroAvviso", request.getAttribute("tx_numeroAvviso"));
		if(request.getAttribute("tx_dataCreazione")!=null)
			session.setAttribute("tx_dataCreazione", request.getAttribute("tx_dataCreazione"));
		if(request.getAttribute("tx_flagPagato")!=null)
			session.setAttribute("tx_flagPagato", request.getAttribute("tx_flagPagato"));
		if(request.getAttribute("tx_annoRif")!=null)
			session.setAttribute("tx_annoRif", request.getAttribute("tx_annoRif"));
	}

	private void recuperaFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		request.setAttribute("tx_idDominio", session.getAttribute("tx_idDominio")!=null?session.getAttribute("tx_idDominio"):"");
		request.setAttribute("tx_idDocumento", session.getAttribute("tx_idDocumento")!=null?session.getAttribute("tx_idDocumento"):"");
		request.setAttribute("tx_idEnte", session.getAttribute("tx_idEnte")!=null?session.getAttribute("tx_idEnte"):"");
		request.setAttribute("tx_codFisc", session.getAttribute("tx_codFisc")!=null?session.getAttribute("tx_codFisc"):"");
		request.setAttribute("tx_numeroAvviso", session.getAttribute("tx_numeroAvviso")!=null?session.getAttribute("tx_numeroAvviso"):"");
		//inizio LP PG200370
		//request.setAttribute("tx_dataCreazione", session.getAttribute("tx_dataCreazione")!=null?session.getAttribute("tx_dataCreazione"):"");
		if(session.getAttribute("tx_dataCreazione") != null) {
			request.setAttribute("tx_dataCreazione", session.getAttribute("tx_dataCreazione"));
		} else {
			request.setAttribute("tx_dataCreazione", null);
		}
		request.setAttribute("tx_flagPagato", session.getAttribute("tx_flagPagato")!=null?session.getAttribute("tx_flagPagato"):"");
		request.setAttribute("tx_annoRif", session.getAttribute("tx_annoRif")!=null?session.getAttribute("tx_annoRif"):"");
	}
	
	private void resetFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		request.setAttribute("tx_idDominio", "");
		session.setAttribute("tx_idDominio", "");
		request.setAttribute("tx_idDocumento", "");
		session.setAttribute("tx_idDocumento", "");
		request.setAttribute("tx_idEnte", "");
		session.setAttribute("tx_idEnte", "");
		request.setAttribute("tx_codFisc", "");
		session.setAttribute("tx_codFisc", "");
		request.setAttribute("tx_numeroAvviso", "");
		session.setAttribute("tx_numeroAvviso", "");
		//inizio LP PG200360
		//request.setAttribute("tx_dataCreazione", "");
		//session.setAttribute("tx_dataCreazione", "");
		//request.setAttribute("tx_flagPagato", "");
		//session.setAttribute("tx_flagPagato", "");
		request.setAttribute("tx_dataCreazione", null);
		session.setAttribute("tx_dataCreazione", null);
		request.setAttribute("tx_flagPagato", "T");
		session.setAttribute("tx_flagPagato", "T");
		//fine LP PG200360
		request.setAttribute("tx_annoRif", "");
		session.setAttribute("tx_annoRif", "");
		
		session.setAttribute("objAppoggio", "");
		
	}
	
	
	
	
	private void salvaDatiAppoggio(ConfigurazioneBlackBoxPos configurazioneBlackBoxPos, HttpServletRequest request) {
		configurazioneBlackBoxPosAppoggio.setCodiceIdentificativoDominio(configurazioneBlackBoxPos.getCodiceIdentificativoDominio());
		configurazioneBlackBoxPosAppoggio.setCodiceEnte(configurazioneBlackBoxPos.getCodiceEnte());
		configurazioneBlackBoxPosAppoggio.setNumeroAvviso(configurazioneBlackBoxPos.getNumeroAvviso());
		configurazioneBlackBoxPosAppoggio.setCodiceIdentificativoFlusso(configurazioneBlackBoxPos.getCodiceIdentificativoFlusso());
		configurazioneBlackBoxPosAppoggio.setDataCreazione(configurazioneBlackBoxPos.getDataCreazione());
		configurazioneBlackBoxPosAppoggio.setTipoRecord(configurazioneBlackBoxPos.getTipoRecord());
		configurazioneBlackBoxPosAppoggio.setCodiceIdentificativoDocumento(configurazioneBlackBoxPos.getCodiceIdentificativoDocumento());
		configurazioneBlackBoxPosAppoggio.setNumeroRata(configurazioneBlackBoxPos.getNumeroRata());
		configurazioneBlackBoxPosAppoggio.setDataScadenza(configurazioneBlackBoxPos.getDataScadenza());
		configurazioneBlackBoxPosAppoggio.setCodiceFiscale(configurazioneBlackBoxPos.getCodiceFiscale());
		configurazioneBlackBoxPosAppoggio.setImporto(configurazioneBlackBoxPos.getImporto());
		configurazioneBlackBoxPosAppoggio.setDenominazioneDebitore(configurazioneBlackBoxPos.getDenominazioneDebitore());
		configurazioneBlackBoxPosAppoggio.setIndirizzoContribuente(configurazioneBlackBoxPos.getIndirizzoContribuente());
		configurazioneBlackBoxPosAppoggio.setLocalitaContribuente(configurazioneBlackBoxPos.getLocalitaContribuente());
		configurazioneBlackBoxPosAppoggio.setProvinciaContribuente(configurazioneBlackBoxPos.getProvinciaContribuente());
		configurazioneBlackBoxPosAppoggio.setFlagAnnullamento(configurazioneBlackBoxPos.getFlagAnnullamento());
		configurazioneBlackBoxPosAppoggio.setDataAggiornamentoRecord(configurazioneBlackBoxPos.getDataAggiornamentoRecord());
		configurazioneBlackBoxPosAppoggio.setCodiceIbanAccredito(configurazioneBlackBoxPos.getCodiceIbanAccredito());
		configurazioneBlackBoxPosAppoggio.setCodiceIuv(configurazioneBlackBoxPos.getCodiceIuv());
		configurazioneBlackBoxPosAppoggio.setFlagPagato(configurazioneBlackBoxPos.getFlagPagato());
		//inizio LP PG200370
		configurazioneBlackBoxPosAppoggio.setTassonomia(configurazioneBlackBoxPos.getTassonomia());
		//fine LP PG200370
		
		HttpSession session = request.getSession();
		session.setAttribute("objAppoggio", configurazioneBlackBoxPosAppoggio);
	}
	
	
	
	
	private void compareDatiAppoggio(ConfigurazioneBlackBoxPos configurazioneBlackBoxPos, HttpServletRequest request) {
		
		
		HttpSession session = request.getSession();
		configurazioneBlackBoxPosAppoggio = (ConfigurazioneBlackBoxPos)session.getAttribute("objAppoggio");
		
		String app = "";
		
		if(configurazioneBlackBoxPosAppoggio.getCodiceIdentificativoFlusso()!=null && !configurazioneBlackBoxPosAppoggio.getCodiceIdentificativoFlusso().equals(configurazioneBlackBoxPos.getCodiceIdentificativoFlusso())) {
			app += String.format("Codice Identificativo Flusso modificato. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getCodiceIdentificativoFlusso(), configurazioneBlackBoxPos.getCodiceIdentificativoFlusso());
		}
		if(configurazioneBlackBoxPosAppoggio.getDataCreazione()!=null && !configurazioneBlackBoxPosAppoggio.getDataCreazione().equals(configurazioneBlackBoxPos.getDataCreazione())) {
			app += String.format("Data Creazione modificata. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getDataCreazione(), configurazioneBlackBoxPos.getDataCreazione());
		}
		if(configurazioneBlackBoxPosAppoggio.getTipoRecord()!=null && !configurazioneBlackBoxPosAppoggio.getTipoRecord().equals(configurazioneBlackBoxPos.getTipoRecord())) {
			app += String.format("Tipo Record modificato. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getTipoRecord(), configurazioneBlackBoxPos.getTipoRecord());
		}
		if(configurazioneBlackBoxPosAppoggio.getCodiceIdentificativoDocumento()!=null && !configurazioneBlackBoxPosAppoggio.getCodiceIdentificativoDocumento().equals(configurazioneBlackBoxPos.getCodiceIdentificativoDocumento())) {
			app += String.format("Codice Identificativo Documento modificato. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getCodiceIdentificativoDocumento(), configurazioneBlackBoxPos.getCodiceIdentificativoDocumento());
		}
		if(configurazioneBlackBoxPosAppoggio.getNumeroRata()!=null && !configurazioneBlackBoxPosAppoggio.getNumeroRata().equals(configurazioneBlackBoxPos.getNumeroRata())) {
			app += String.format("Numero Rata modificato. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getNumeroRata(), configurazioneBlackBoxPos.getNumeroRata());
		}
		if(configurazioneBlackBoxPosAppoggio.getDataScadenza()!=null && !configurazioneBlackBoxPosAppoggio.getDataScadenza().equals(configurazioneBlackBoxPos.getDataScadenza())) {
			app += String.format("Data Scadenza modificata. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getDataScadenza(), configurazioneBlackBoxPos.getDataScadenza());
		}
		if(configurazioneBlackBoxPosAppoggio.getCodiceFiscale()!=null && !configurazioneBlackBoxPosAppoggio.getCodiceFiscale().equals(configurazioneBlackBoxPos.getCodiceFiscale())) {
			app += String.format("Codice Fiscale modificato. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getCodiceFiscale(), configurazioneBlackBoxPos.getCodiceFiscale());
		}
		if(configurazioneBlackBoxPosAppoggio.getImporto()!=null && !configurazioneBlackBoxPosAppoggio.getImporto().equals(configurazioneBlackBoxPos.getImporto())) {
			app += String.format("Importo modificato. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getImporto(), configurazioneBlackBoxPos.getImporto());
		}
		if(configurazioneBlackBoxPosAppoggio.getDenominazioneDebitore()!=null && !configurazioneBlackBoxPosAppoggio.getDenominazioneDebitore().equals(configurazioneBlackBoxPos.getDenominazioneDebitore())) {
			app += String.format("Denominazione Debitore modificata. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getDenominazioneDebitore(), configurazioneBlackBoxPos.getDenominazioneDebitore());
		}
		if(configurazioneBlackBoxPosAppoggio.getIndirizzoContribuente()!=null && !configurazioneBlackBoxPosAppoggio.getIndirizzoContribuente().equals(configurazioneBlackBoxPos.getIndirizzoContribuente())) {
			app += String.format("Indirizzo Contribuente modificato. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getIndirizzoContribuente(), configurazioneBlackBoxPos.getIndirizzoContribuente());
		}
		if(configurazioneBlackBoxPosAppoggio.getLocalitaContribuente()!=null && !configurazioneBlackBoxPosAppoggio.getLocalitaContribuente().equals(configurazioneBlackBoxPos.getLocalitaContribuente())) {
			app += String.format("Localita' Contribuente modificata. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getLocalitaContribuente(), configurazioneBlackBoxPos.getLocalitaContribuente());
		}
		if(configurazioneBlackBoxPosAppoggio.getProvinciaContribuente()!=null && !configurazioneBlackBoxPosAppoggio.getProvinciaContribuente().equals(configurazioneBlackBoxPos.getProvinciaContribuente())) {
			app += String.format("Provincia Contribuente modificata. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getProvinciaContribuente(), configurazioneBlackBoxPos.getProvinciaContribuente());
		}
		if(configurazioneBlackBoxPosAppoggio.getFlagAnnullamento()!=null && !configurazioneBlackBoxPosAppoggio.getFlagAnnullamento().equals(configurazioneBlackBoxPos.getFlagAnnullamento())) {
			//inizio LP PG200370
			//app += String.format("Flag Annullamento modificato. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getFlagAnnullamento(), configurazioneBlackBoxPos.getFlagAnnullamento());
			String o = configurazioneBlackBoxPosAppoggio.getFlagAnnullamento().trim();
			String n = configurazioneBlackBoxPos.getFlagAnnullamento().trim();
			if(!o.equals(n)) {
				app += String.format("Flag Annullamento modificato. Vecchio valore: %s ; Nuovo Valore: %s. ", o, n);
			} else {
				configurazioneBlackBoxPos.setFlagAnnullamento(o);
			}
			//fine LP PG200370
		}
		if(configurazioneBlackBoxPosAppoggio.getCodiceIbanAccredito()!=null && !configurazioneBlackBoxPosAppoggio.getCodiceIbanAccredito().equals(configurazioneBlackBoxPos.getCodiceIbanAccredito())) {
			app += String.format("Codice Iban Accredito modificato. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getCodiceIbanAccredito(), configurazioneBlackBoxPos.getCodiceIbanAccredito());
		}
		if(configurazioneBlackBoxPosAppoggio.getFlagPagato()!=null && !configurazioneBlackBoxPosAppoggio.getFlagPagato().equals(configurazioneBlackBoxPos.getFlagPagato())) {
			app += String.format("Flag Pagato modificato. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getFlagPagato(), configurazioneBlackBoxPos.getFlagPagato());
		}
		//inizio LP PG200370
		if(configurazioneBlackBoxPosAppoggio.getTassonomia() != null
		  && !configurazioneBlackBoxPosAppoggio.getTassonomia().equals(configurazioneBlackBoxPos.getTassonomia())) {
			app += String.format("Tassonomia modificata. Vecchio valore: %s ; Nuovo Valore: %s. ", configurazioneBlackBoxPosAppoggio.getTassonomia(), configurazioneBlackBoxPos.getTassonomia());
		}
		//fine LP PG200370
		
		if(app.length()>0) {
			updateMes = String.format("Posizione con Codice Identificativo Dominio %s, Codice Ente %s e Numero Avviso %s aggiornata : ", configurazioneBlackBoxPos.getCodiceIdentificativoDominio(), configurazioneBlackBoxPos.getCodiceEnte(), configurazioneBlackBoxPos.getNumeroAvviso()) + app;
		}
		
	}
	
	private void insertLog(ConfigurazioneBlackBoxPos configurazioneBlackBoxPos, String message) {
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try {
			BlackBoxPosLog configurazioneBlackBoxPoslog = new BlackBoxPosLog();
			configurazioneBlackBoxPoslog.setCodiceIdentificativoDominio(configurazioneBlackBoxPos.getCodiceIdentificativoDominio());
			configurazioneBlackBoxPoslog.setCodiceEnte(configurazioneBlackBoxPos.getCodiceEnte());
			configurazioneBlackBoxPoslog.setNumeroAvviso(configurazioneBlackBoxPos.getNumeroAvviso());
			configurazioneBlackBoxPoslog.setDataInserimento(Calendar.getInstance());
			configurazioneBlackBoxPoslog.setOperazioneEseguita(message);
			//inizio LP PG21XX04 Leak
			//ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
			connection = getBlackBoxDataSource().getConnection();
			ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
			//fine LP PG21XX04 Leak
			int esitoLog = configurazioneBlackBoxDAO.insert(configurazioneBlackBoxPoslog);
			System.out.println("esito inserimento log : " + esitoLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
	}
	
    //inizio LP PG200370
	private void getTassonomie(HttpServletRequest request) throws ActionException
	{
	  try {
		if(request.getAttribute("tassonomie") == null) {
			ConfigTassonomiaListaResponse configTassonomiaListaResponse = getTassonomiaListaResponse(request);
			request.setAttribute("tassonomie", configTassonomiaListaResponse.getListaTassonomia().getListXml());
		}
	  } catch (Exception e) {
		throw new ActionException("Errore nel caricamento delle tassonomie", e);
	  }
	}
	
	private ConfigTassonomiaListaResponse getTassonomiaListaResponse(HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigTassonomiaListaResponse res = null;
		ConfigTassonomiaListaRequest in = new ConfigTassonomiaListaRequest();
		in.setRowsPerPage(0);
		in.setPageNumber(0);
		in.setOrder("SPIA"); // ordina per "dati specifici incasso" 
		Date oggi = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dataValidita = df.format(oggi);
		in.setDataInizioValidita_da(dataValidita);
		in.setDataFineValidita_a(dataValidita);
		
		res = WSCache.configTassonomiaServer.lista(in, request);
		return res;
	}

	private void setFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(request.getAttribute("tx_idDominio_hidden") != null)
			session.setAttribute("tx_idDominio", request.getAttribute("tx_idDominio_hidden"));
		if(request.getAttribute("tx_idDocumento") != null)
			session.setAttribute("tx_idDocumento", request.getAttribute("tx_idDocumento"));
		if(request.getAttribute("tx_idEnte_hidden") != null)
			session.setAttribute("tx_idEnte", request.getAttribute("tx_idEnte_hidden"));
		if(request.getAttribute("tx_codFisc") != null)
			session.setAttribute("tx_codFisc", request.getAttribute("tx_codFisc"));
		if(request.getAttribute("tx_numeroAvviso_hidden")!=null)
			session.setAttribute("tx_numeroAvviso", request.getAttribute("tx_numeroAvviso_hidden"));
		if(request.getAttribute("tx_dataCreazione_hidden") != null)
			session.setAttribute("tx_dataCreazione", request.getAttribute("tx_dataCreazione_hidden"));
		if(request.getAttribute("tx_flagPagato") != null)
			session.setAttribute("tx_flagPagato", request.getAttribute("tx_flagPagato"));
		if(request.getAttribute("tx_annoRif") != null)
			session.setAttribute("tx_annoRif", request.getAttribute("tx_annoRif"));
	}
	
	private String rielaboraListaBlackBox(String lstBlackBoxPos)
	{
		String sXML = "";
		//inizio LP PG21XX04 Leak
		WebRowSet rowSetFull = null;
		WebRowSetImpl rowSet = null;
		//fine LP PG21XX04 Leak
		try {
			if (lstBlackBoxPos != null && lstBlackBoxPos.length() > 0) {
				//inizio LP PG21XX04 Leak
				//WebRowSet rowSetFull = Convert.stringToWebRowSet(lstBlackBoxPos);
				//WebRowSetImpl rowSet = new WebRowSetImpl();
				rowSetFull = Convert.stringToWebRowSet(lstBlackBoxPos);
				rowSet = new WebRowSetImpl();
				//fine LP PG21XX04 Leak
				RowSetMetaDataImpl rsMdData = new RowSetMetaDataImpl();

				rsMdData.setColumnCount(21);
				for (int i = 1; i <= 21; i++) {
					rsMdData.setColumnType(i, Types.VARCHAR);
				}
				rowSet.setMetaData(rsMdData);
				while (rowSetFull.next()) {
					rowSet.moveToInsertRow();
					for(int i = 1; i <= 21; i++) {
						rowSet.updateString(i, rowSetFull.getString(i) == null ? "" : rowSetFull.getString(i));
					}
					Double appo = rowSetFull.getDouble(11);
					if(appo != null) {
						appo = appo / 100D;
						rowSet.updateString(11, formatDecimalNumber(appo));
					}
					appo = rowSetFull.getDouble(21);
					if(appo != null) {
						appo = appo / 100D;
						rowSet.updateString(21, formatDecimalNumber(appo));
					}
					rowSet.insertRow();
				}
				// rimette il puntatore all'inzio
				rowSet.moveToCurrentRow();
				sXML = Convert.webRowSetToString(rowSet);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally
		{
	    	try {
	    		if(rowSet != null) {
	    			rowSet.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
	    	try {
	    		if(rowSetFull != null) {
	    			rowSetFull.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return sXML;
	}
	
	private String formatDecimalNumber(Double bdValue)
	{
		if (bdValue != null && bdValue != 0.D) {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols(); 
			symbols.setDecimalSeparator(',');
			symbols.setGroupingSeparator('.');
			DecimalFormat dcFormat = new DecimalFormat("#,###,###,##0.00", symbols);
			return dcFormat.format(bdValue);
		}
		else {
			return "0,00";
		}
	}
	//fine LP PG200370
}

