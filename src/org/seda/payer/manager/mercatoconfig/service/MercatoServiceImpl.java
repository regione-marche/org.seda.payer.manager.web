package org.seda.payer.manager.mercatoconfig.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.seda.payer.manager.util.GenericsDateNumbers;

//import javax.servlet.http.HttpServletRequest;

//import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;

import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.ConfigurazionePiazzola;
import com.seda.payer.core.mercato.bean.ConfigurazionePrenotazioni;
import com.seda.payer.core.mercato.bean.ConfigurazioneTariffe;
import com.seda.payer.core.mercato.bean.ControlloPiazzoleResponse;
import com.seda.payer.core.mercato.bean.ControlloTariffeResponse;
import com.seda.payer.core.mercato.bean.EsitoRisposte;
import com.seda.payer.core.mercato.bean.GestioneTariffeResponse;
import com.seda.payer.core.mercato.dao.ConfigurazionePiazzolaDAO;
import com.seda.payer.core.mercato.dao.ConfigurazionePrenotazioniDAO;
import com.seda.payer.core.mercato.dao.ConfigurazioneTariffeDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;

//inizio LP PG21XX04 Leak2
//Nota. I potenziali leak qui segnalati, sono dei falsi leake
//      in tutto il codice di questa classe sulle connection
//      la close viene eseguita. 
//fine LP PG21XX04 Leak2

public class MercatoServiceImpl{

	private DataSource mercatoDataSource; 
	private String mercatoDbSchema;
	protected DataSource getMercatoDataSource(){return this.mercatoDataSource;}
	protected String getMercatoDbSchema(){return this.mercatoDbSchema;}	
	public void setMercatoDataSource(DataSource mercatoDataSource) {
		this.mercatoDataSource = mercatoDataSource;
	}
	public void setMercatoDbSchema(String mercatoDbSchema) {
		this.mercatoDbSchema = mercatoDbSchema;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Controllo Tariffe. Accetta in input il codice tariffa. Restituisce un oggetto con le date utili di inizio e fine validita, un boolean 
	// che indica se la tariffa è stata prenotata e pagata;
	//Le date utili sono calcolate sulla base dei seguenti casi:
	//.Se il giorno attuale è inferiore alla data inizio validità tariffa: la data inizio validità può assumere valori compresi tra il massimo fra il giorno 
	//attuale e la data inizio delle prenotazioni ed il minimo fra la data fine validità e la data ultima prenotazione.
	//.Se il giorno attuale è compreso fra data inizio e data fine tariffa: la data di inizio validita della tariffa non è modificabile, mentre la data fine
	//validità può assumere valori superiori fra il massimo fra il giorno attuale e la data ultima prenotazione e comunque inferiore alla data fine validità
	//della piazzola.
	//.Se la data attuale è superiore alla data fine validà della tariffa: nessuna data possibile.
	public ControlloTariffeResponse controlloTariffe(String codiceKeyTariffa) throws DaoException {
		ControlloTariffeResponse esito =  new ControlloTariffeResponse();
		esito.setPrenotato(false);
		//Select per key su tariffa per recupero dati originali 
		ConfigurazioneTariffeDAO configurazioneTariffeDAO;
		ConfigurazioneTariffe tariffe = new ConfigurazioneTariffe();
		
		tariffe.setCodiceKeyTariffa(codiceKeyTariffa);
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			
			tariffe = configurazioneTariffeDAO.getPerKey(tariffe);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
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
		
		//Cursore su tariffe per verifica adiacenze
		ArrayList <ConfigurazioneTariffe> listTariffe = new ArrayList<ConfigurazioneTariffe>();
		ConfigurazioneTariffe listtar = new ConfigurazioneTariffe();
		listtar.setCodiceSocieta(tariffe.getCodiceSocieta());
		listtar.setCuteCute(tariffe.getCuteCute());
		listtar.setChiaveEnte(tariffe.getChiaveEnte());
		listtar.setCodiceKeyAreaMercantile(tariffe.getCodiceKeyAreaMercantile());
		listtar.setCodiceKeyPiazzola(tariffe.getCodiceKeyPiazzola());
		listtar.setCodiceGiornoSettimana(tariffe.getCodiceGiornoSettimana());
		listtar.setCodiceKeyAutorizzazione("KK"+tariffe.getCodiceKeyAutorizzazione());
		listtar.setCodiceKeyTipologiaBanco(tariffe.getCodiceKeyTipologiaBanco());
		listtar.setCodiceKeyPeriodoGiornal(tariffe.getCodiceKeyPeriodoGiornal());
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			listTariffe = configurazioneTariffeDAO.listTariffe(listtar);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
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
		
		//Select su piazzole
		ConfigurazionePiazzolaDAO confPiazzolaDAO;
		ConfigurazionePiazzola piazzola = new ConfigurazionePiazzola();
		piazzola.setCodiceKeyPiazzola(tariffe.getCodiceKeyPiazzola());
		try {
			//inizio LP PG21XX04 Leak
			//confPiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			confPiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			piazzola = confPiazzolaDAO.getPerKey(piazzola);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
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
		//Select su prenotazioni
		ConfigurazionePrenotazioniDAO configurazionePrenotazioniDAO;
		ConfigurazionePrenotazioni configurazionePrenotazioni = new ConfigurazionePrenotazioni();
		configurazionePrenotazioni.setCodiceKeyTariffa(codiceKeyTariffa);
		ArrayList<ConfigurazionePrenotazioni> configurazionePrenotazioniList = null;
		try {
			//inizio LP PG21XX04 Leak
			//configurazionePrenotazioniDAO = MercatoDAOFactory.getPrenotazioni(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			configurazionePrenotazioniDAO = MercatoDAOFactory.getPrenotazioni(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			configurazionePrenotazioniList = configurazionePrenotazioniDAO.getAllPerTariffa(configurazionePrenotazioni);
		} catch (DaoException e1) {
			e1.printStackTrace();
			throw new DaoException(e1);
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
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
		Calendar minDataPren = Calendar.getInstance();
		Calendar maxDataPren = Calendar.getInstance();
		if (configurazionePrenotazioniList != null && !configurazionePrenotazioniList.isEmpty()) {
			ConfigurazionePrenotazioni item = configurazionePrenotazioniList.get(0);
			if (piazzola.getDataInizioValidita().compareTo(item.getDataPrenotazione())<0){
				minDataPren.setTimeInMillis(piazzola.getDataInizioValidita().getTimeInMillis());
			} else {
				minDataPren.setTimeInMillis(item.getDataPrenotazione().getTimeInMillis());
			}
			item = configurazionePrenotazioniList.get(configurazionePrenotazioniList.size()-1);
			for (Iterator iterator = configurazionePrenotazioniList.iterator(); iterator.hasNext();) {
				item = (ConfigurazionePrenotazioni) iterator.next();
				if (item.getImportoPagato()>0) {
					esito.setPrenotato(true);
					maxDataPren.setTimeInMillis(item.getDataPrenotazione().getTimeInMillis());
				}
			}
		}
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 00);
		today.set(Calendar.MINUTE, 00);
		today.set(Calendar.SECOND, 00);
		today.set(Calendar.MILLISECOND, 000);
		System.out.println("getDataFineValidita" + GenericsDateNumbers.formatCalendarData(tariffe.getDataFineValidita(),"ddMMyyyy"));
		if (today.compareTo(tariffe.getDataFineValidita())<=0) {
			if (today.compareTo(tariffe.getDataInizioValidita())<0){
				if (today.compareTo(minDataPren)>0) {
					esito.setDataMaxInizioValidita(today);
				} else {
					esito.setDataMaxInizioValidita(minDataPren);
				}
				if (today.compareTo(maxDataPren)>0) {
					esito.setDataMinFineValidita(maxDataPren);
				} else {
					esito.setDataMinFineValidita(today);
				}
			} else if (today.compareTo(tariffe.getDataInizioValidita())>0) {
				if (today.compareTo(tariffe.getDataFineValidita())<0) {
					esito.setDataMaxInizioValidita(tariffe.getDataInizioValidita());
					if (today.compareTo(maxDataPren)>0) {
						esito.setDataMinFineValidita(today);
					} else {
						esito.setDataMinFineValidita(maxDataPren);
					}
				}
			}
		} else {
			esito.setDataMaxInizioValidita(null);
			esito.setDataMinFineValidita(null);
		}
		boolean verificaSuccessivo = false;
		Calendar dataFinValPrec = Calendar.getInstance();
		dataFinValPrec.setTimeInMillis(31536000000L);	//Data fittizia per coprire il primo record
	//Adesso scorro il cursore delle Tariffe esistenti per eventualmente modificare le date se vi è qualcosa
		if (listTariffe!=null && !listTariffe.isEmpty()) {
			for (Iterator iterator = listTariffe.iterator(); iterator.hasNext();) {
				ConfigurazioneTariffe item = (ConfigurazioneTariffe) iterator.next();
				if (verificaSuccessivo) {
					if (esito.getDataMinFineValidita()!=null && esito.getDataMinFineValidita().compareTo(item.getDataInizioValidita())>0){
						esito.setDataMinFineValidita(item.getDataInizioValidita());
					}
				}
				if (item.getCodiceKeyTariffa().equals(codiceKeyTariffa)) {
					if (esito.getDataMaxInizioValidita()!=null && esito.getDataMaxInizioValidita().compareTo(dataFinValPrec)<0) {
						esito.setDataMaxInizioValidita(dataFinValPrec);
					}
					verificaSuccessivo = true;
				} else {
					dataFinValPrec.setTimeInMillis(item.getDataFineValidita().getTimeInMillis());
				}
			}
		}
		
		return esito;
	}

	// Aggiornamento Tariffe: Accetta in input codice tariffa, data inizio validita, data fine validita, importi tariffe e 
	// giorno della settimana (numerico) e codice periodo giornaliero. 
	//Effettua l'aggiornamento delle tariffe e rischedula le prenotazioni escludendo eventuali prenotazioni esistenti (pagate)
	//e rigenerando le prenotazioni precedenti e/o successive alle esistenti sulla base delle nuove date di inizio e fine validità
	//I campi modificabili variano a seconda di:
	//.Se il giorno attuale è inferiore alla data inizio validità tariffa: la data inizio validità può assumere valori compresi tra il massimo fra il giorno 
	//attuale e la data inizio delle prenotazioni ed il minimo fra la data fine validità e la data ultima prenotazione.
	//Se vi sono delle prenotazioni effettuate, non è possibile modificare ne importi, ne giorno, ne periodo.
	//se non vi sono delle prenotazioni effettuate, è possibile modificare gli importi.
	//.Se giorno attuale e compreso fra data inizio e data fine tariffa: la data di inizio validita della tariffa non è modificabile, mentre la data fine
	//validità può assumere valori superiori fra il massimo fra il giorno attuale e la data ultima prenotazione e comunque inferiore alla data fine validità
	//della piazzola. Non è possibile modificare ne importi, ne giorno, ne periodo.
	//.Se la data attuale è superiore alla data fine validà della tariffa: non è possibile modificare nulla.

	public ArrayList<GestioneTariffeResponse> gestisciTariffa(String codiceKeyTariffa, Calendar dataIniVal, Calendar dataFinVal, Double tariffaCosap, Double tariffaTari, int giornoSett, String codiceKeyPeriodoGiorn) throws DaoException{
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		boolean esitofin = true;
		//System.out.println("Inizio Gestione Tariffe");
		ArrayList<GestioneTariffeResponse> esito = new ArrayList<GestioneTariffeResponse>();
		ControlloTariffeResponse esitoCtrlTariffe = new ControlloTariffeResponse();
		GestioneTariffeResponse resp = new GestioneTariffeResponse();
		esitoCtrlTariffe=controlloTariffe(codiceKeyTariffa);
		if (esitoCtrlTariffe.getDataMaxInizioValidita()!=null && esitoCtrlTariffe.getDataMinFineValidita()!=null) {
			Calendar today = Calendar.getInstance();
			if (today.compareTo(dataIniVal)<0) {
				if (dataIniVal.compareTo(esitoCtrlTariffe.getDataMaxInizioValidita())<0) {
					//Non si modifica la data perchè si sta spostando indietro nel tempo
					esitofin=false;
					resp.setEsito(false);
					resp.setMessaggio("Data inizio inferiore alla data minima di inizio validità");
					esito.add(resp);
					//System.out.println("--BP01--");
				} else { 
					//if (dataIniVal.compareTo(esitoCtrlTariffe.getDataMinFineValidita())>0){
					if (dataIniVal.compareTo(dataFinVal)>0){
					//Non si modifica la data perchè va oltre la data fine validità
						esitofin=false;
						resp.setEsito(false);
						resp.setMessaggio("Data inizio superiore alla data di fine validità");
						esito.add(resp);
						//System.out.println("--BP02--");
					} else {
						// la data inizio validità è in un range corretto, Verifico la fine validità
						if (dataFinVal.compareTo(esitoCtrlTariffe.getDataMinFineValidita())>=0) {
							//System.out.println("--BP03--");
							//data fine validità è in un range corretto, si aggiorna
							if(esitoCtrlTariffe.isPrenotato()) {
								//System.out.println("--BP04--");
							//Recupero prima i vecchi dati per poter poi correttamente rischedulare le prenotazioni
								ConfigurazioneTariffe OldConfTar = new ConfigurazioneTariffe();
								OldConfTar.setCodiceKeyTariffa(codiceKeyTariffa);
								ConfigurazioneTariffeDAO configurazioneTariffeDAO;
								try {
									//inizio LP PG21XX04 Leak
									//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
									conn = getMercatoDataSource().getConnection();
									configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
									//fine LP PG21XX04 Leak
									
									OldConfTar = configurazioneTariffeDAO.getPerKey(OldConfTar);
								} catch (DaoException ex) {
									ex.printStackTrace();
									esitofin=false;
									resp.setEsito(false);
									resp.setMessaggio(ex.getErrorMessage());
									esito.add(resp);
								}								
								//inizio LP PG21XX04 Leak
								catch (SQLException ex) {
									ex.printStackTrace();
									esitofin=false;
									resp.setEsito(false);
									resp.setMessaggio(ex.getMessage());
									esito.add(resp);
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
							//Vi sono delle tariffe prenotate e pagate, si aggiorna solo le date
								ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
								configurazioneTariffe.setCodiceKeyTariffa(codiceKeyTariffa);
								configurazioneTariffe.setDataInizioValidita(dataIniVal);
								configurazioneTariffe.setDataFineValidita(dataFinVal);
								try {
									//inizio LP PG21XX04 Leak
									//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
									conn = getMercatoDataSource().getConnection();
									configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
									//fine LP PG21XX04 Leak
									int aggiornati = configurazioneTariffeDAO.updatePerKey(configurazioneTariffe);
									if (aggiornati<0) {
										//System.out.println("--BP05--");
										esitofin=false;
										resp.setEsito(false);
										resp.setMessaggio("Nessun record aggiornato");
										esito.add(resp);
									} else {
										//System.out.println("--BP06--");
										//Rischedulo le prenotazioni
										configurazioneTariffe = new ConfigurazioneTariffe();
										configurazioneTariffe.setCodiceKeyTariffa(codiceKeyTariffa);
										//inizio LP PG21XX04 Leak
										//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
										conn = getMercatoDataSource().getConnection();
										configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
										//fine LP PG21XX04 Leak
										configurazioneTariffe = configurazioneTariffeDAO.getPerKey(configurazioneTariffe);
										EsitoRisposte esRisp = new EsitoRisposte();
										esRisp = schedPrenotazioni(codiceKeyTariffa, OldConfTar.getCodiceSocieta(), OldConfTar.getCuteCute(), OldConfTar.getChiaveEnte(), OldConfTar.getDataInizioValidita(), OldConfTar.getDataFineValidita(), dataIniVal, dataFinVal, OldConfTar.getCodiceGiornoSettimana());
										if (esRisp.getCodiceMessaggio().equals("KO")) {
											//System.out.println("--BP07--");
											esitofin = false;
											resp.setEsito(false);
											resp.setMessaggio(esRisp.getDescrizioneMessaggio());
											esito.add(resp);
										}
									}
								} catch(DaoException ex) {
									ex.printStackTrace();
									esitofin=false;
									resp.setEsito(false);
									resp.setMessaggio(ex.getErrorMessage());
									esito.add(resp);
									throw new DaoException(ex);
								}
								//inizio LP PG21XX04 Leak
								catch (SQLException ex) {
									ex.printStackTrace();
									esitofin=false;
									resp.setEsito(false);
									resp.setMessaggio(ex.getMessage());
									esito.add(resp);
									throw new DaoException(ex);
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
							} else {
							//Recupero prima i vecchi dati per poter poi correttamente rischedulare le prenotazioni
								//System.out.println("--BP08--");
								ConfigurazioneTariffe OldConfTar = new ConfigurazioneTariffe();
								OldConfTar.setCodiceKeyTariffa(codiceKeyTariffa);
								ConfigurazioneTariffeDAO configurazioneTariffeDAO;
								try {
									//inizio LP PG21XX04 Leak
									//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
									conn = getMercatoDataSource().getConnection();
									configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
									//fine LP PG21XX04 Leak
									OldConfTar = configurazioneTariffeDAO.getPerKey(OldConfTar);
								} catch (DaoException ex) {
									ex.printStackTrace();
									esitofin=false;
									resp.setEsito(false);
									resp.setMessaggio(ex.getErrorMessage());
									esito.add(resp);
									throw new DaoException(ex);
								}								
								//inizio LP PG21XX04 Leak
								catch (SQLException ex) {
									ex.printStackTrace();
									esitofin=false;
									resp.setEsito(false);
									resp.setMessaggio(ex.getMessage());
									esito.add(resp);
									throw new DaoException(ex);
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
						//Non vi sono delle tariffe prenotate e pagate, posso aggiornare le tariffe	
								ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
								configurazioneTariffe.setCodiceKeyTariffa(codiceKeyTariffa);
								configurazioneTariffe.setDataInizioValidita(dataIniVal);
								configurazioneTariffe.setDataFineValidita(dataFinVal);
								configurazioneTariffe.setTariffaCosap(tariffaCosap);
								configurazioneTariffe.setTariffaTari(tariffaTari);
								try {
									//inizio LP PG21XX04 Leak
									//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
									conn = getMercatoDataSource().getConnection();
									configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
									//fine LP PG21XX04 Leak
									int aggiornati = configurazioneTariffeDAO.updatePerKey(configurazioneTariffe);
									if (aggiornati<0) {
										esitofin=false;
										resp.setEsito(false);
										resp.setMessaggio("Nessuna riga aggiornata");
										esito.add(resp);
									} else {
										//System.out.println("--BP09--");
										//Rischedulo le prenotazioni
										configurazioneTariffe = new ConfigurazioneTariffe();
										configurazioneTariffe.setCodiceKeyTariffa(codiceKeyTariffa);
										//inizio LP PG21XX04 Leak
										//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
										conn = getMercatoDataSource().getConnection();
										configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
										//fine LP PG21XX04 Leak
										configurazioneTariffe = configurazioneTariffeDAO.getPerKey(configurazioneTariffe);
										EsitoRisposte esRisp = new EsitoRisposte();
										esRisp = schedPrenotazioni(codiceKeyTariffa, OldConfTar.getCodiceSocieta(), OldConfTar.getCuteCute(), OldConfTar.getChiaveEnte(), OldConfTar.getDataInizioValidita(), OldConfTar.getDataFineValidita(), dataIniVal, dataFinVal, OldConfTar.getCodiceGiornoSettimana());
										if (esRisp.getCodiceMessaggio().equals("KO")) {
											//System.out.println("--BP10--");
											esitofin = false;
											resp.setEsito(false);
											resp.setMessaggio(esRisp.getDescrizioneMessaggio());
											esito.add(resp);
										}										
									}
								} catch(DaoException ex) {
									ex.printStackTrace();
									esitofin=false;
									resp.setEsito(false);
									resp.setMessaggio(ex.getErrorMessage());
									esito.add(resp);
									throw new DaoException(ex);
								}
								//inizio LP PG21XX04 Leak
								catch (SQLException ex) {
									ex.printStackTrace();
									esitofin=false;
									resp.setEsito(false);
									resp.setMessaggio(ex.getMessage());
									esito.add(resp);
									throw new DaoException(ex);
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
							}
							//System.out.println("--BP11--");
						}
						//System.out.println("--BP12--");
					}
					//System.out.println("--BP13--");
				}
				//System.out.println("--BP14--");
			} else {
				if (today.compareTo(dataFinVal)<0) {
					//System.out.println("--BP15--");
					if (dataFinVal.compareTo(esitoCtrlTariffe.getDataMinFineValidita())>=0) {
						//System.out.println("--BP16--");
				//La nuova data fine validità è superiore al limite minimo, si aggiorna solo la data fine validita e si rischedulano le prenotazioni
				//Recupero prima i vecchi dati per poter poi correttamente rischedulare le prenotazioni
						ConfigurazioneTariffe OldConfTar = new ConfigurazioneTariffe();
						OldConfTar.setCodiceKeyTariffa(codiceKeyTariffa);
						ConfigurazioneTariffeDAO configurazioneTariffeDAO;
						try {
							//inizio LP PG21XX04 Leak
							//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
							conn = getMercatoDataSource().getConnection();
							configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
							//fine LP PG21XX04 Leak
							OldConfTar = configurazioneTariffeDAO.getPerKey(OldConfTar);
						} catch (DaoException ex) {
							ex.printStackTrace();
							esitofin=false;
							resp.setEsito(false);
							resp.setMessaggio(ex.getErrorMessage());
							esito.add(resp);
						}								
						//inizio LP PG21XX04 Leak
						catch (SQLException ex) {
							ex.printStackTrace();
							esitofin=false;
							resp.setEsito(false);
							resp.setMessaggio(ex.getMessage());
							esito.add(resp);
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
					//Vi sono delle tariffe schedulate, posso aggiornare solo la data fine
						ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
						configurazioneTariffe.setCodiceKeyTariffa(codiceKeyTariffa);
						configurazioneTariffe.setDataInizioValidita(null);
						configurazioneTariffe.setDataFineValidita(dataFinVal);
						//System.out.println("--BP17--");
						try {
							//inizio LP PG21XX04 Leak
							//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
							conn = getMercatoDataSource().getConnection();
							configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
							//fine LP PG21XX04 Leak
							int aggiornati = configurazioneTariffeDAO.updatePerKey(configurazioneTariffe);
							if (aggiornati<0) {
								esitofin=false;
								resp.setEsito(false);
								resp.setMessaggio("Nessuna aggiornamento effettuato");
								esito.add(resp);
							} else {
								//System.out.println("--BP18--");
					//Rischedulo le prenotazioni con la data inizio validità vecchia.
								configurazioneTariffe = new ConfigurazioneTariffe();
								configurazioneTariffe.setCodiceKeyTariffa(codiceKeyTariffa);
								//inizio LP PG21XX04 Leak
								//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
								conn = getMercatoDataSource().getConnection();
								configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
								//fine LP PG21XX04 Leak
								configurazioneTariffe = configurazioneTariffeDAO.getPerKey(configurazioneTariffe);
								EsitoRisposte esRisp = new EsitoRisposte();
								esRisp = schedPrenotazioni(codiceKeyTariffa, OldConfTar.getCodiceSocieta(), OldConfTar.getCuteCute(), OldConfTar.getChiaveEnte(), OldConfTar.getDataInizioValidita(), OldConfTar.getDataFineValidita(), OldConfTar.getDataInizioValidita(), dataFinVal, OldConfTar.getCodiceGiornoSettimana());
								if (esRisp.getCodiceMessaggio().equals("KO")) {
									esitofin = false;
									resp.setEsito(false);
									resp.setMessaggio(esRisp.getDescrizioneMessaggio());
									esito.add(resp);
								}
								esitofin=false;
								resp.setEsito(false);
								//System.out.println("--BP19--");
								resp.setMessaggio("Aggiornata la sola data fine validità");
								esito.add(resp);
							}
						} catch(DaoException ex) {
							ex.printStackTrace();
							esitofin=false;
							resp.setEsito(false);
							resp.setMessaggio(ex.getErrorMessage());
							esito.add(resp);							
							throw new DaoException(ex);
						}			
						//inizio LP PG21XX04 Leak
						catch (SQLException ex) {
							ex.printStackTrace();
							esitofin=false;
							resp.setEsito(false);
							resp.setMessaggio(ex.getMessage());
							esito.add(resp);
							throw new DaoException(ex);
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
					} else {
						esitofin=false;
						resp.setEsito(false);
						//System.out.println("--BP20--");
						resp.setMessaggio("La data fine validità è sotto al limite minimo, non si può aggiornare");
						esito.add(resp);						
					}
				} else {
					esitofin=false;
					//System.out.println("--BP21--");
					resp.setEsito(false);
					resp.setMessaggio("Data Inizio e fine validità inferiori alla data attuale; impossibile aggiornare");
					esito.add(resp);							
				}
			}
		} else {
			//Recupero prima i vecchi dati per poter poi correttamente rischedulare le prenotazioni
			ConfigurazioneTariffe OldConfTar = new ConfigurazioneTariffe();
			OldConfTar.setCodiceKeyTariffa(codiceKeyTariffa);
			ConfigurazioneTariffeDAO configurazioneTariffeDAO;
			//System.out.println("--BP24--");
			try {
				//inizio LP PG21XX04 Leak
				//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
				conn = getMercatoDataSource().getConnection();
				configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
				//fine LP PG21XX04 Leak
				OldConfTar = configurazioneTariffeDAO.getPerKey(OldConfTar);
			} catch (DaoException ex) {
				ex.printStackTrace();
				esitofin=false;
				throw new DaoException(ex);
			}								
			//inizio LP PG21XX04 Leak
			catch (SQLException ex) {
				ex.printStackTrace();
				esitofin=false;
				throw new DaoException(ex);
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
			//Non vi sono delle tariffe prenotate e pagate, posso aggiornare le tariffe	
			ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
			configurazioneTariffe.setCodiceKeyTariffa(codiceKeyTariffa);
			configurazioneTariffe.setDataInizioValidita(dataIniVal);
			configurazioneTariffe.setDataFineValidita(dataFinVal);
			configurazioneTariffe.setTariffaCosap(tariffaCosap);
			configurazioneTariffe.setTariffaTari(tariffaTari);
			//System.out.println("--BP25--");
			try {
				//inizio LP PG21XX04 Leak
				//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
				conn = getMercatoDataSource().getConnection();
				configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
				//fine LP PG21XX04 Leak
				int aggiornati = configurazioneTariffeDAO.updatePerKey(configurazioneTariffe);
				if (aggiornati<0) {
					esitofin=false;
					resp.setEsito(false);
					//System.out.println("--BP26--");
					resp.setMessaggio("Nessun aggiornamento effettuato");
					esito.add(resp);						
				} else {
					//Rischedulo le prenotazioni
					configurazioneTariffe = new ConfigurazioneTariffe();
					configurazioneTariffe.setCodiceKeyTariffa(codiceKeyTariffa);
					//inizio LP PG21XX04 Leak
					//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
					conn = getMercatoDataSource().getConnection();
					configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
					//fine LP PG21XX04 Leak
					configurazioneTariffe = configurazioneTariffeDAO.getPerKey(configurazioneTariffe);
					EsitoRisposte esRisp = new EsitoRisposte();
					//System.out.println("--BP27--");
					esRisp = schedPrenotazioni(codiceKeyTariffa, OldConfTar.getCodiceSocieta(), OldConfTar.getCuteCute(), OldConfTar.getChiaveEnte(), OldConfTar.getDataInizioValidita(), OldConfTar.getDataFineValidita(), dataIniVal, dataFinVal, OldConfTar.getCodiceGiornoSettimana());
					if (esRisp.getCodiceMessaggio().equals("KO")) {
						esitofin = false;
						resp.setEsito(false);
						resp.setMessaggio(esRisp.getDescrizioneMessaggio());
						esito.add(resp);	
					}										
				}
			} catch(DaoException ex) {
				ex.printStackTrace();
				esitofin=false;
				resp.setEsito(false);
				resp.setMessaggio(ex.getMessage());
				esito.add(resp);					
				throw new DaoException(ex);
			}
			//inizio LP PG21XX04 Leak
			catch (SQLException ex) {
				ex.printStackTrace();
				esitofin=false;
				resp.setEsito(false);
				resp.setMessaggio(ex.getMessage());
				esito.add(resp);
				throw new DaoException(ex);
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
		}
		if (esitofin) {
			//System.out.println("--BP28--");
			resp.setEsito(true);
			resp.setMessaggio("Aggiornamento eseguito con successo");
			esito.add(resp);
		}
		//System.out.println("--BP29--");
		return esito;
	}

	//Accetta in input il codice della piazzola; restituisce in output un oggetto data max inizio validità, data min fine validità
	//ed un boolean che indica la presenza di prenotazioni (pagate). Verifica la data prenotazione ciclando su tutte le tariffe valide
	//Le date utili sono calcolate sulla base dei seguenti casi:
	//.Se la data attuale è minore della data inizio validità della piazzola, la data inizio può assumere valori
	//compresi fra la data attuale e la minima fra data fine validità e data prima prenotazione; la data fine validità
	//può assumere valori superiori al massimo fra data inizio validità e data ultima prenotazione.
	//.Se la data attuale è compresa fra la data inizio e la data fine validità della piazzola, la data inizio validità
	//non è modificabile (quindi è quella già presente), la data fine validità può assumere valori superiori al massimo fra
	//data attuale e la data ultima prenotazione
	//.Se data fine validità è inferiore alla data attuale, non vi sono valori possibili per le date inizio e fine validità (non modificabili).
	public ControlloPiazzoleResponse controlloPiazzole(String codiceKeyPiazzola) {
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		ConfigurazionePiazzolaDAO configurazionePiazzolaDAO;
		ConfigurazionePiazzola configurazionePiazzola = new ConfigurazionePiazzola();
		configurazionePiazzola.setCodiceKeyPiazzola(codiceKeyPiazzola);
		try {
			//inizio LP PG21XX04 Leak
			//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			configurazionePiazzola = configurazionePiazzolaDAO.getPerKey(configurazionePiazzola);
		} catch (DaoException e) {
			e.printStackTrace();
		} 
		//inizio LP PG21XX04 Leak
		catch (SQLException e) {
			e.printStackTrace();
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
		ControlloPiazzoleResponse esito = new ControlloPiazzoleResponse();
		esito.setPrenotato(false);
		Calendar datIniValPz = Calendar.getInstance();
		Calendar datFinValPz = Calendar.getInstance();

		if (configurazionePiazzola.getDataInizioValidita() != null) {
			datIniValPz.setTimeInMillis(configurazionePiazzola.getDataInizioValidita().getTimeInMillis());
		}
		if (configurazionePiazzola.getDataFineValidita() != null) {
			datFinValPz.setTimeInMillis(configurazionePiazzola.getDataFineValidita().getTimeInMillis());
		} else {
			//Al posto di Null imposto una data futura di default
			datFinValPz.setTimeInMillis(4733510401000L);
		}
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 00);
		today.set(Calendar.MINUTE, 00);
		today.set(Calendar.SECOND, 00);
		today.set(Calendar.MILLISECOND, 000);
		if (datFinValPz.compareTo(today)<0) {
		//Data attuale superiore alla data fine della piazzola, non vi sono valori possibili
		} else {
			ConfigurazioneTariffeDAO configurazioneTariffeDAO;
			ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
			configurazioneTariffe.setCodiceKeyPiazzola(codiceKeyPiazzola);
			ArrayList<ConfigurazioneTariffe> configurazioneTariffeList = null;
			try {
				//inizio LP PG21XX04 Leak
				//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
				conn = getMercatoDataSource().getConnection();
				configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
				//fine LP PG21XX04 Leak
				configurazioneTariffeList = configurazioneTariffeDAO.getAllPerPiazzola(configurazioneTariffe);
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
			Calendar minDataFinPiaz = Calendar.getInstance();
			minDataFinPiaz.set(Calendar.HOUR_OF_DAY, 00);
			minDataFinPiaz.set(Calendar.MINUTE, 00);
			minDataFinPiaz.set(Calendar.SECOND, 00);
			minDataFinPiaz.set(Calendar.MILLISECOND, 000);
			Calendar maxDataIniPiaz = Calendar.getInstance();
			maxDataIniPiaz.set(Calendar.HOUR_OF_DAY, 00);
			maxDataIniPiaz.set(Calendar.MINUTE, 00);
			maxDataIniPiaz.set(Calendar.SECOND, 00);
			maxDataIniPiaz.set(Calendar.MILLISECOND, 000);			
			if (configurazioneTariffeList != null && !configurazioneTariffeList.isEmpty()) {
				for (Iterator iterator = configurazioneTariffeList.iterator(); iterator.hasNext();) {
					ConfigurazioneTariffe item = (ConfigurazioneTariffe) iterator.next();
					//Select su prenotazioni
					Calendar minDataPren = Calendar.getInstance();
					Calendar maxDataPren = Calendar.getInstance();
					ConfigurazionePrenotazioniDAO configurazionePrenotazioniDAO;
					ConfigurazionePrenotazioni configurazionePrenotazioni = new ConfigurazionePrenotazioni();
					configurazionePrenotazioni.setCodiceKeyTariffa(item.getCodiceKeyTariffa());
					ArrayList<ConfigurazionePrenotazioni> configurazionePrenotazioniList = null;
					try {
						//inizio LP PG21XX04 Leak
						//configurazionePrenotazioniDAO = MercatoDAOFactory.getPrenotazioni(getMercatoDataSource(), getMercatoDbSchema());
						conn = getMercatoDataSource().getConnection();
						configurazionePrenotazioniDAO = MercatoDAOFactory.getPrenotazioni(conn, getMercatoDbSchema());
						//fine LP PG21XX04 Leak
						configurazionePrenotazioniList = configurazionePrenotazioniDAO.getAllPerTariffa(configurazionePrenotazioni);
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
					if (configurazionePrenotazioniList != null && !configurazionePrenotazioniList.isEmpty()) {
						ConfigurazionePrenotazioni itempr = configurazionePrenotazioniList.get(0);
						minDataPren.setTimeInMillis(itempr.getDataPrenotazione().getTimeInMillis());
						itempr = configurazionePrenotazioniList.get(configurazionePrenotazioniList.size()-1);
						maxDataPren.setTimeInMillis(itempr.getDataPrenotazione().getTimeInMillis());
						for (Iterator iteratorpr = configurazionePrenotazioniList.iterator(); iteratorpr.hasNext();) {
							itempr = (ConfigurazionePrenotazioni) iteratorpr.next();
							if (itempr.getImportoPagato()>0) {
								esito.setPrenotato(true);
							}
						}
					}
					if (today.compareTo(datIniValPz)<0) {
						if (datFinValPz.compareTo(minDataPren)<0) {
							maxDataIniPiaz.setTimeInMillis(datFinValPz.getTimeInMillis());
						} else {
							maxDataIniPiaz.setTimeInMillis(minDataPren.getTimeInMillis());
						}
						if (datIniValPz.compareTo(maxDataPren)<0) {
							minDataFinPiaz.setTimeInMillis(maxDataPren.getTimeInMillis());
						} else {
							minDataFinPiaz.setTimeInMillis(datIniValPz.getTimeInMillis());
						}
					} else {
						if (today.compareTo(datFinValPz)<0) {
							maxDataIniPiaz.setTimeInMillis(datIniValPz.getTimeInMillis());
							if (today.compareTo(maxDataPren)<0) {
								minDataFinPiaz.setTimeInMillis(maxDataPren.getTimeInMillis());
							} else {
								minDataFinPiaz.setTimeInMillis(today.getTimeInMillis());
							}
						}
					}
				}
			}
			esito.setDataMaxInizioValidita(maxDataIniPiaz);
			esito.setDataMinFineValidita(minDataFinPiaz);
		}

		return esito;
		
	}

	//Richiama il controllo Piazzola e se possibile modificare, prima aggiorna le date della piazzola e poi richiama l'aggiornamento delle tariffe, ciclando su tutte le 
	//tariffe valide presenti sulla piazzola che hanno data di inizio validità minore della nuova data di inizio validità della piazzola e 
	//data di fine validità maggiore della nuova data fine validità della piazzola.
	public ArrayList<GestioneTariffeResponse> gestisciPiazzola(String codiceKeyPiazzola, Calendar dataIniVal, Calendar dataFinVal) throws DaoException {
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		boolean esitofin = true;
		ArrayList<GestioneTariffeResponse> esito = new ArrayList<GestioneTariffeResponse>();
		GestioneTariffeResponse resp = new GestioneTariffeResponse();
		ControlloPiazzoleResponse esitoCtrlPiazzole = new ControlloPiazzoleResponse();
		esitoCtrlPiazzole=controlloPiazzole(codiceKeyPiazzola);
		if (esitoCtrlPiazzole.getDataMaxInizioValidita()!=null&&esitoCtrlPiazzole.getDataMinFineValidita()!=null) {
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 00);
			today.set(Calendar.MINUTE, 00);
			today.set(Calendar.SECOND, 00);
			today.set(Calendar.MILLISECOND, 000);			
			if (today.compareTo(dataIniVal)<0) {
				if (dataIniVal.compareTo(esitoCtrlPiazzole.getDataMaxInizioValidita())<0) {
					//Non si modifica la data perchè si sta spostando indietro nel tempo
					esitofin=false;
					resp.setEsito(false);
					resp.setMessaggio("Data Inizio Validità inferiore al limite minimo");
					esito.add(resp);
				} else { 
					if (dataIniVal.compareTo(esitoCtrlPiazzole.getDataMinFineValidita())>0){
					//Non si modifica la data perchè va oltre la data fine validità
						esitofin=false;
						resp.setEsito(false);
						resp.setMessaggio("Data Inizio Validità superiore alla data fine validità");
						esito.add(resp);
					} else {
						// la data inizio validità è in un range corretto, Verifico la fine validità
						if (dataFinVal.compareTo(esitoCtrlPiazzole.getDataMinFineValidita())>=0) {
							//data fine validità è in un range corretto, si aggiorna
							if(esitoCtrlPiazzole.isPrenotato()) {
							//Vi sono delle tariffe prenotate e pagate, si aggiorna solo le date 
								ConfigurazionePiazzolaDAO configurazionePiazzolaDAO;
								
								ConfigurazionePiazzola configurazionePiazzola = new ConfigurazionePiazzola();
								configurazionePiazzola.setCodiceKeyPiazzola(codiceKeyPiazzola);
								configurazionePiazzola.setDataInizioValidita(dataIniVal);
								configurazionePiazzola.setDataFineValidita(dataFinVal);
								try {
									//inizio LP PG21XX04 Leak
									//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
									conn = getMercatoDataSource().getConnection();
									configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
									//fine LP PG21XX04 Leak
									int aggiornati = configurazionePiazzolaDAO.updatePerKey(configurazionePiazzola);
									if (aggiornati<0) {
										esitofin=false;
										resp.setEsito(false);
										resp.setMessaggio("Nessun record aggiornato");
									}
								} catch(DaoException ex) {
									ex.printStackTrace();
									esitofin=false;
									resp.setEsito(false);
									resp.setMessaggio(ex.getErrorMessage());
									esito.add(resp);
								}
								//inizio LP PG21XX04 Leak
								catch (SQLException ex) {
									ex.printStackTrace();
									esitofin=false;
									resp.setEsito(false);
									resp.setMessaggio(ex.getMessage());
									esito.add(resp);
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
								
							//Ora aggiorno tutte le tariffe, ciclando e verificando quelle valide
								ConfigurazioneTariffeDAO configurazioneTariffeDAO;
								ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
								configurazioneTariffe.setCodiceKeyPiazzola(codiceKeyPiazzola);
								ArrayList<ConfigurazioneTariffe> configurazioneTariffeList = null;
								try {
									//inizio LP PG21XX04 Leak
									//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
									conn = getMercatoDataSource().getConnection();
									configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
									//fine LP PG21XX04 Leak
									configurazioneTariffeList = configurazioneTariffeDAO.getAllPerPiazzola(configurazioneTariffe);
								} catch (DaoException e1) {
									resp.setEsito(false);
									resp.setMessaggio(e1.getErrorMessage());
									esito.add(resp);
									e1.printStackTrace();
								}
								//inizio LP PG21XX04 Leak
								catch (SQLException e1) {
									resp.setEsito(false);
									resp.setMessaggio(e1.getMessage());
									esito.add(resp);
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
								if (configurazioneTariffeList != null && !configurazioneTariffeList.isEmpty()) {
									for (Iterator iterator = configurazioneTariffeList.iterator(); iterator.hasNext();) {
										ConfigurazioneTariffe item = (ConfigurazioneTariffe) iterator.next();
										//La data fine validità della tariffa è successiva ad oggi, quindi per me è valida ed aggiornabile
										if (item.getDataFineValidita().compareTo(today)>0) {
											ArrayList <GestioneTariffeResponse> esTariffe = new ArrayList<GestioneTariffeResponse>();
											esTariffe = gestisciTariffa(item.getCodiceKeyTariffa(), dataIniVal, dataFinVal, item.getTariffaCosap(), item.getTariffaTari(), item.getCodiceGiornoSettimana(), item.getCodiceKeyPeriodoGiornal());
											if (esTariffe==null||esTariffe.isEmpty()) {
												esitofin=false;
												resp.setEsito(false);
												resp.setMessaggio("Aggiornamento Tariffe non riuscito");
												esito.add(resp);
											}
										}
									}
								}
							} 
						}
					}
				}
			} else {
				if (today.compareTo(dataFinVal)<=0) {
					if (dataFinVal.compareTo(esitoCtrlPiazzole.getDataMinFineValidita())>=0) {
						//Posso aggiornare, ma solo la data fine validita
						ConfigurazionePiazzolaDAO configurazionePiazzolaDAO;
						
						ConfigurazionePiazzola configurazionePiazzola = new ConfigurazionePiazzola();
						configurazionePiazzola.setCodiceKeyPiazzola(codiceKeyPiazzola);
						//configurazionePiazzola.setDataInizioValidita(dataIniVal);
						configurazionePiazzola.setDataFineValidita(dataFinVal);
						try {
							//inizio LP PG21XX04 Leak
							//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
							conn = getMercatoDataSource().getConnection();
							configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
							//fine LP PG21XX04 Leak
							int aggiornati = configurazionePiazzolaDAO.updatePerKey(configurazionePiazzola);
							if (aggiornati<0) {
								esitofin=false;
								resp.setEsito(false);
								resp.setMessaggio("Nessun record aggiornato");
								esito.add(resp);
							}
						} catch(DaoException ex) {
							ex.printStackTrace();
							esitofin=false;
							resp.setEsito(false);
							resp.setMessaggio(ex.getErrorMessage());
							esito.add(resp);
						}
						//inizio LP PG21XX04 Leak
						catch (SQLException ex) {
							ex.printStackTrace();
							esitofin=false;
							resp.setEsito(false);
							resp.setMessaggio(ex.getMessage());
							esito.add(resp);
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
					//Ora aggiorno tutte le tariffe, ciclando e verificando quelle valide
						ConfigurazioneTariffeDAO configurazioneTariffeDAO;
						ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
						configurazioneTariffe.setCodiceKeyPiazzola(codiceKeyPiazzola);
						ArrayList<ConfigurazioneTariffe> configurazioneTariffeList = null;
						try {
							//inizio LP PG21XX04 Leak
							//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
							conn = getMercatoDataSource().getConnection();
							configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
							//fine LP PG21XX04 Leak
							configurazioneTariffeList = configurazioneTariffeDAO.getAllPerPiazzola(configurazioneTariffe);
						} catch (DaoException e1) {
							e1.printStackTrace();
							esitofin=false;
							resp.setEsito(false);
							resp.setMessaggio(e1.getErrorMessage());
							esito.add(resp);
						}
						//inizio LP PG21XX04 Leak
						catch (SQLException e1) {
							e1.printStackTrace();
							esitofin=false;
							resp.setEsito(false);
							resp.setMessaggio(e1.getMessage());
							esito.add(resp);
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
						if (configurazioneTariffeList != null && !configurazioneTariffeList.isEmpty()) {
							for (Iterator iterator = configurazioneTariffeList.iterator(); iterator.hasNext();) {
								ConfigurazioneTariffe item = (ConfigurazioneTariffe) iterator.next();
								//La data fine validità della tariffa è successiva ad oggi, quindi per me è valida ed aggiornabile
								if (item.getDataFineValidita().compareTo(today)>0) {
									ArrayList <GestioneTariffeResponse> Esito = new ArrayList<GestioneTariffeResponse>();
									Esito = gestisciTariffa(item.getCodiceKeyTariffa(), dataIniVal, dataFinVal, item.getTariffaCosap(), item.getTariffaTari(), item.getCodiceGiornoSettimana(), item.getCodiceKeyPeriodoGiornal());
									String Messaggi = "";
									boolean presenzaErrori=false;
									if (Esito!=null&&!Esito.isEmpty()) {
										for (Iterator iter = Esito.iterator(); iter.hasNext();) {
											GestioneTariffeResponse item2 = (GestioneTariffeResponse) iter.next();
											if (item2.isEsito()) {
											} else {
												presenzaErrori=true;
												Messaggi = Messaggi + item2.getMessaggio() + "\n\r";
											}
										}
									} else {
										resp.setEsito(false);
										resp.setMessaggio("Aggiornamento Tariffe non riuscito");
										esito.add(resp);
									}
									if (presenzaErrori) {
										resp.setEsito(false);
										resp.setMessaggio(Messaggi);
										esito.add(resp);
									}
								}
							}
						}
					} else {
						esitofin=false;
						resp.setEsito(false);
						resp.setMessaggio("Data fine validità inferiore alla data minima possibile");
						esito.add(resp);
					}
				} else {
					esitofin=false;
					resp.setEsito(false);
					resp.setMessaggio("Data fine validità inferiore alla data attuale");
					esito.add(resp);
				}
			}
		}
		if (esitofin) {
			resp.setEsito(true);
			resp.setMessaggio("OK");
		}
		return esito;
		
	}
	
//Effettua la schedulazione delle Prenotazioni. Accetta in input codice tariffe, data inizio validita precedente, data fine validita precedente,
//data inizio validità nuova, data fine validità nuova ed il giorno della settimana. Controlla se vi sono prenotazioni già pre esistenti nel periodo precedente, le elimina se 
//risultano non pagate e le rischedula sulla base delle nuove date.
	public EsitoRisposte schedPrenotazioni(String codiceKeyTariffa, String codSoc, String codUte, String codEnt, Calendar dataIniValPrec, Calendar dataFinValPrec, Calendar dataIniVal, Calendar dataFinVal, int giornoSett) throws DaoException {
		ConfigurazionePrenotazioniDAO prenotazioniDAO;
		//inizio LP PG21XX04 Leak
		//prenotazioniDAO = MercatoDAOFactory.getPrenotazioni(getMercatoDataSource(), getMercatoDbSchema());
		Connection conn = null;
		//fine LP PG21XX04 Leak
		
		//fine LP PG21XX04 Leak
		EsitoRisposte esitoPrn = new EsitoRisposte();
		ConfigurazionePrenotazioni prenotazioni = new ConfigurazionePrenotazioni();
		prenotazioni.setCodiceKeyTariffa(codiceKeyTariffa);
		Calendar dataPrenotDa = Calendar.getInstance();
		dataPrenotDa.setTimeInMillis(dataIniValPrec.getTimeInMillis());
		dataPrenotDa.set(Calendar.HOUR_OF_DAY, 00);
		dataPrenotDa.set(Calendar.MINUTE, 00);
		dataPrenotDa.set(Calendar.SECOND, 01);
		prenotazioni.setDataPrenotazione(dataPrenotDa);
		Calendar dataPrenotAl = Calendar.getInstance();
		dataPrenotAl.setTimeInMillis(dataFinValPrec.getTimeInMillis());
		dataPrenotAl.set(Calendar.HOUR_OF_DAY, 23);
		dataPrenotAl.set(Calendar.MINUTE, 59);
		dataPrenotAl.set(Calendar.SECOND, 59);
		prenotazioni.setDataPrenotazioneAl(dataPrenotAl);
		//inizio LP PG21XX04 Leak
		//esitoPrn = prenotazioniDAO.deletePerRange(prenotazioni);
		try {
			conn = getMercatoDataSource().getConnection();
			prenotazioniDAO = MercatoDAOFactory.getPrenotazioni(conn, getMercatoDbSchema());
			esitoPrn = prenotazioniDAO.deletePerRange(prenotazioni);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
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
		
		if (esitoPrn.getCodiceMessaggio().equals("OK")) {
			//Schedulo le nuove prenotazioni
			Calendar dataIniValCalendar  = Calendar.getInstance();
			dataIniValCalendar.setTimeInMillis(dataIniVal.getTimeInMillis());
			Calendar dataFinValCalendar = Calendar.getInstance();
			dataFinValCalendar.setTimeInMillis(dataFinVal.getTimeInMillis());
			//Seleziono le eventuali rimaste con pagamenti per escluderle dalla nuova insert
			ConfigurazionePrenotazioni configurazionePrenotazioni = new ConfigurazionePrenotazioni();
			configurazionePrenotazioni.setCodiceKeyTariffa(codiceKeyTariffa);
			ArrayList<ConfigurazionePrenotazioni> configurazionePrenotazioniList = null;
			try {
				//inizio LP PG21XX04 Leak
				//prenotazioniDAO = MercatoDAOFactory.getPrenotazioni(getMercatoDataSource(), getMercatoDbSchema());
				conn = getMercatoDataSource().getConnection();
				prenotazioniDAO = MercatoDAOFactory.getPrenotazioni(conn, getMercatoDbSchema());
				//fine LP PG21XX04 Leak
				configurazionePrenotazioniList = prenotazioniDAO.getAllPerTariffa(configurazionePrenotazioni);
			} catch (DaoException e1) {
				e1.printStackTrace();
				throw new DaoException(e1);
			}
			//inizio LP PG21XX04 Leak
			catch (SQLException e) {
				e.printStackTrace();
				throw new DaoException(e);
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
			String Escluse = "";
			Calendar DataPrenEsclusa = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
			if (configurazionePrenotazioniList!=null && !configurazionePrenotazioniList.isEmpty()) {
				//ConfigurazionePrenotazioni item = configurazionePrenotazioniList.get(0);
				for (Iterator iterator = configurazionePrenotazioniList.iterator(); iterator.hasNext();) {
					ConfigurazionePrenotazioni item = (ConfigurazionePrenotazioni) iterator.next();
					DataPrenEsclusa.setTimeInMillis(item.getDataPrenotazione().getTimeInMillis());
					DateFormat dfn = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String Convert = dfn.format(DataPrenEsclusa.getTime());
					try {
						DataPrenEsclusa.setTime(dfn.parse(Convert.replace("12:00", "00:00")));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					String add = Convert+";";
					Escluse = Escluse + add; 
				}
			}
			prenotazioniDAO=null;
			
			//inizio LP PG21XX04 Leak
			//prenotazioniDAO = MercatoDAOFactory.getPrenotazioni(getMercatoDataSource(), getMercatoDbSchema());
			try {
				conn = getMercatoDataSource().getConnection();
				prenotazioniDAO = MercatoDAOFactory.getPrenotazioni(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			esitoPrn = prenotazioniDAO.schedulaPrenotazioni(codiceKeyTariffa, codSoc, codUte, codEnt, dataIniValCalendar, dataFinValCalendar, giornoSett, Escluse);
			//inizio LP PG21XX04 Leak
			} catch (DaoException e1) {
				e1.printStackTrace();
				throw new DaoException(e1);
			}
			catch (SQLException e1) {
				e1.printStackTrace();
				throw new DaoException(e1);
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
		}
		return esitoPrn;
	}	
	
}
