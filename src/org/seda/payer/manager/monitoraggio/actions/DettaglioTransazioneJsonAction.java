package org.seda.payer.manager.monitoraggio.actions;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.dati.BeanFreccia;
import com.seda.payer.pgec.webservice.commons.dati.BeanIV;
import com.seda.payer.pgec.webservice.commons.dati.BeanIci;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneFiltrataRequestType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneFiltrataResponseType;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaOneriRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaOneriResponse;

import flexjson.JSONSerializer;
import flexjson.transformer.AbstractTransformer;


public class DettaglioTransazioneJsonAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		//inizio LP PG180290
		String template = getTemplateCurrentApplication(request, session); 
		//fine LP PG180290
		
		UserBean user = (UserBean)session.getAttribute("j_user_bean");
		String codiceTransazione = request.getParameter(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		request.setAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format(), codiceTransazione);

		
		//test ente
		String codEnte = "";
		if (user.getUserProfile().equals("AMEN"))
				{
					codEnte = user.getChiaveEnteConsorzio();
				}
		/*
		 * Recupero il record dalla tabella transazioni
		 */
		try {
			WSCache.logWriter.logDebug("dettaglio transazione: id transazione='"+codiceTransazione+"' utente='"+user.getUserName()+"'");

			RecuperaTransazioneFiltrataRequestType requestRecuperaTransazioneFiltrataRequestType=new RecuperaTransazioneFiltrataRequestType();
			requestRecuperaTransazioneFiltrataRequestType.setChiave_transazione(codiceTransazione);
			requestRecuperaTransazioneFiltrataRequestType.setCodice_ente(codEnte);
			requestRecuperaTransazioneFiltrataRequestType.setListaTipologieServizio(getTipologiaServizio(request, session));
			
			RecuperaTransazioneFiltrataResponseType res = WSCache.commonsServer.recuperaTransazioneFiltrata(requestRecuperaTransazioneFiltrataRequestType, request);
			//inizio LP PG180290
			if(template.equalsIgnoreCase("trentrisc")) {
				BeanFreccia[] fre = res.getListFreccia();
				for(int ik = 0; ik < fre.length; ik++) {
					if (fre[ik].getNodoSpcIuv().startsWith("MP")) {
						fre[ik].setNodoSpcIuv("");
					}
				}
				BeanIci[] ici = res.getListIci();
				for(int ik = 0; ik < ici.length; ik++) {
					if (ici[ik].getNodoSpcIuv().startsWith("MP")) {
						ici[ik].setNodoSpcIuv("");
					}
				}
				BeanIV[] iv = res.getListIV();
				for(int ik = 0; ik < iv.length; ik++) {
					if (iv[ik].getNodoSpcIuv().startsWith("MP")) {
						iv[ik].setNodoSpcIuv("");
					}
				}
			}
			//fine LP PG180290
			
			//clono l'oggetto attuale per aggiungere la lista oneri
			RecuperaTransazioniFiltrataResponseTypeApp res1 = new RecuperaTransazioniFiltrataResponseTypeApp();
			res1.setBeanTransazioni(res.getBeanTransazioni());
			res1.setListFreccia(res.getListFreccia());
			res1.setListIci(res.getListIci());
			res1.setListIV(res.getListIV());
			res1.setResponse(res.getResponse());
			
			List<DettaglioOnere> listDettOneri = null;
			//recupero la lista oneri
			//inizio LP PG21XX04 Leak
			WebRowSet wrsOneri = null;
			//fine LP PG21XX04 Leak
			try {

				RecuperaListaOneriRequest req = new RecuperaListaOneriRequest(codiceTransazione);
				RecuperaListaOneriResponse resOneri = WSCache.mipServer.recuperaListaOneri(req, request);
				
				if (resOneri.getListOneriXml() != null && !resOneri.getListOneriXml().equals(""))
				{
					//inizio LP PG21XX04 Leak
					//WebRowSet wrsOneri = Convert.stringToWebRowSet(resOneri.getListOneriXml());
					wrsOneri = Convert.stringToWebRowSet(resOneri.getListOneriXml());
					//fine LP PG21XX04 Leak
					
					DettaglioOnere dett = null;
					while (wrsOneri.next())
					{
						if (listDettOneri == null)
							listDettOneri = new ArrayList<DettaglioOnere>();
						dett = new DettaglioOnere();
						dett.setCodiceEntePortaleEsterno(wrsOneri.getString(2));
						dett.setBeneficiario("[" + wrsOneri.getString(9) + " - " + wrsOneri.getString(11) + " - " + wrsOneri.getString(12) + " - " + wrsOneri.getString(13) + "]<br />" + wrsOneri.getString(14));
						dett.setDescrizioneEntePortaleEsterno(wrsOneri.getString(3));
						dett.setImportoTotale(wrsOneri.getBigDecimal(4));
						dett.setCausale(wrsOneri.getString(5));
						dett.setImportoContabileIngresso(wrsOneri.getBigDecimal(6));
						dett.setImportoContabileUscita(wrsOneri.getBigDecimal(7));
						listDettOneri.add(dett);
					}
				}
			} 
			catch (Exception e) {
				setErrorMessage(e.getMessage());
			}
			//inizio LP PG21XX04 Leak
			finally {
				try {
					if(wrsOneri != null) {
						wrsOneri.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			//fine LP PG21XX04 Leak
			
			res1.setListDettaglioOnere(listDettOneri);
			
			String jsonObject = new JSONSerializer()
				.transform(new CalendarTransformer(), GregorianCalendar.class)
				.transform(new BigDecimalTransformer(), BigDecimal.class)
				.deepSerialize(res1);
			
			request.setAttribute("dettaglioTransazioneJson", jsonObject);
		} 
		catch (Exception e) {
			setErrorMessage(e.getMessage());
		}

		
		
		request.setAttribute(Field.TX_MESSAGE.format(), getMessage());
		request.setAttribute(Field.TX_ERROR_MESSAGE.format(), getErrorMessage());
		return null;
	}
	
	class CalendarTransformer extends AbstractTransformer  {
		public void transform(Object object) { 
			GregorianCalendar cal = (GregorianCalendar)object;
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			getContext().writeQuoted(format.format(cal.getTime()));
		} 
	}
	
	class BigDecimalTransformer extends AbstractTransformer  {
		public void transform(Object object) { 
			BigDecimal bd = (BigDecimal)object;
            DecimalFormat dcFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.ITALIAN));
			getContext().writeQuoted(dcFormat.format(bd.doubleValue()));
		} 
	}
	
	public class RecuperaTransazioniFiltrataResponseTypeApp extends RecuperaTransazioneFiltrataResponseType
	{
		private static final long serialVersionUID = 1L;
		
		private List<DettaglioOnere> listDettaglioOnere;

		public void setListDettaglioOnere(List<DettaglioOnere> listDettaglioOnere) {
			this.listDettaglioOnere = listDettaglioOnere;
		}

		public List<DettaglioOnere> getListDettaglioOnere() {
			return listDettaglioOnere;
		} 
	}
	
	public class DettaglioOnere implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String codiceEntePortaleEsterno;
		private String beneficiario;
		private String descrizioneEntePortaleEsterno;
		private BigDecimal importoTotale;
		private BigDecimal importoContabileIngresso;
		private BigDecimal importoContabileUscita;
		private String causale;

		public void setCodiceEntePortaleEsterno(String codiceEntePortaleEsterno) {
			this.codiceEntePortaleEsterno = codiceEntePortaleEsterno;
		}

		public String getCodiceEntePortaleEsterno() {
			return codiceEntePortaleEsterno;
		}

		public void setBeneficiario(String beneficiario) {
			this.beneficiario = beneficiario;
		}

		public String getBeneficiario() {
			return beneficiario;
		}

		public void setImportoTotale(BigDecimal importoTotale) {
			this.importoTotale = importoTotale;
		}

		public BigDecimal getImportoTotale() {
			return importoTotale;
		}

		public void setDescrizioneEntePortaleEsterno(
				String descrizioneEntePortaleEsterno) {
			this.descrizioneEntePortaleEsterno = descrizioneEntePortaleEsterno;
		}

		public String getDescrizioneEntePortaleEsterno() {
			return descrizioneEntePortaleEsterno;
		}

		public void setImportoContabileIngresso(BigDecimal importoContabileIngresso) {
			this.importoContabileIngresso = importoContabileIngresso;
		}

		public BigDecimal getImportoContabileIngresso() {
			return importoContabileIngresso;
		}

		public void setImportoContabileUscita(BigDecimal importoContabileUscita) {
			this.importoContabileUscita = importoContabileUscita;
		}

		public BigDecimal getImportoContabileUscita() {
			return importoContabileUscita;
		}

		public void setCausale(String causale) {
			this.causale = causale;
		}

		public String getCausale() {
			return causale;
		}
	}
	
}
