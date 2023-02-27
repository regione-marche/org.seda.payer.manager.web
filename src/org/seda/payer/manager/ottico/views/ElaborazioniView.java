package org.seda.payer.manager.ottico.views;

import java.io.Serializable;
import java.util.Calendar;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.views.BaseView;
import com.seda.payer.commons.utility.CalendarUtility;
import com.seda.payer.commons.utility.StringUtility;
/**
 * @author marco.montisano
 */
public class ElaborazioniView extends BaseView implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String Name = "elaborazioni";
	private String companyCode;
	private String userCode;
	private String chiaveEnte;
	private String siglaProvincia;
	private String dataElaborazioneDa;
	private String dataElaborazioneA;
	private String dataCreazioneDa;
	private String dataCreazioneA;
	private String tipologiaFlusso;
	private String listaOttico;
	private String statoFlusso; //PG22XX09_YL5

	public ElaborazioniView(ServletContext context, HttpServletRequest request) {
		super(context, request);
		super.populate(request, scope);
		this.userCode = getValue(Name + "_searchuserCode");
		this.companyCode = getValue(Name + "_codiceSocieta");
		this.chiaveEnte = getValue(Name + "_searchchiaveEnte");
		this.siglaProvincia = getValue(Name + "_siglaProvincia");
		Calendar tmp_dataElaborazioneDa = (Calendar) request.getAttribute("dataElaborazioneDa");
		Calendar tmp_dataElaborazioneA = (Calendar) request.getAttribute("dataElaborazioneA");
		this.dataElaborazioneDa = formatDate(tmp_dataElaborazioneDa, "yyyy-MM-dd");
		this.dataElaborazioneA = formatDate(tmp_dataElaborazioneA, "yyyy-MM-dd");
		Calendar tmp_dataCreazioneDa = (Calendar) request.getAttribute("dataCreazioneDa");
		Calendar tmp_dataCreazioneA = (Calendar) request.getAttribute("dataCreazioneA");
		this.dataCreazioneDa = formatDate(tmp_dataCreazioneDa, "yyyy-MM-dd");
		this.dataCreazioneA = formatDate(tmp_dataCreazioneA, "yyyy-MM-dd");
		this.tipologiaFlusso = getValue(Name + "_tipologiaFlusso");
		super.setCodiceProvincia(this.siglaProvincia);
		super.setCodiceUtente(this.userCode);
		this.statoFlusso = getValue(Name + "_statoFlusso"); //PG22XX09_YL5
	}

	public String getCompanyCode() {
		return companyCode != null ? companyCode : "";
	}

	public ElaborazioniView setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
		request.setAttribute(Name + "_searchcompanyCode", this.companyCode);
		return this;
	}

	public String getUserCode() {
		return userCode != null ? userCode : "";
	}

	public ElaborazioniView setUserCode(String userCode) {
		this.userCode = userCode;
		request.setAttribute(Name + "_searchuserCode", this.userCode);
		super.setCodiceUtente(this.userCode);
		return this;
	}

	public String getChiaveEnte() {
		return chiaveEnte != null ? chiaveEnte : "";
	}

	public ElaborazioniView setChiaveEnte(String chiaveEnte) {
		this.chiaveEnte = chiaveEnte;
		request.setAttribute(Name + "_searchchiaveEnte", this.chiaveEnte);
		return this;
	}

	public ElaborazioniView setListaOttico(String listXml) {
		this.listaOttico = listXml;
		request.setAttribute("listaOttico", listXml);
		return this;
	}

	public ElaborazioniView setListaOtticoPageInfo(com.seda.data.spi.PageInfo pageInfo) {
		super.setPageInfo(pageInfo);
		request.setAttribute("listaOttico.pageInfo", pageInfo);
		return this;
	}
	
	public String getListaOttico() {
		return listaOttico;
	}

	public void setEntetiposervizios(String listXml) {
		request.setAttribute("entetiposervizios", listXml);
	}

	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	public ElaborazioniView setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
		setValue(Name + "_siglaProvincia", siglaProvincia);
		super.setCodiceProvincia(this.siglaProvincia);
		return this;
	}

	public Calendar getDataElaborazioneDa() throws Exception {
		return !StringUtility.isEmpty(dataElaborazioneDa) ? CalendarUtility.stringToCalendar(dataElaborazioneDa, this.getDatePattern()) : null;
	}

	public void setDataElaborazioneDa(String dataElaborazioneDa) {
		this.dataElaborazioneDa = dataElaborazioneDa;
	}

	public Calendar getDataElaborazioneA() throws Exception {
		return !StringUtility.isEmpty(dataElaborazioneA) ? CalendarUtility.stringToCalendar(dataElaborazioneA, this.getDatePattern()) : null;
	}

	public void setDataElaborazioneA(String dataElaborazioneA) {
		this.dataElaborazioneA = dataElaborazioneA;
	}

	public Calendar getDataCreazioneDa() throws Exception {
		return !StringUtility.isEmpty(dataCreazioneDa) ? CalendarUtility.stringToCalendar(dataCreazioneDa, this.getDatePattern()) : null;
	}

	public void setDataCreazioneDa(String dataCreazioneDa) {
		this.dataCreazioneDa = dataCreazioneDa;
	}

	public Calendar getDataCreazioneA() throws Exception {
		return !StringUtility.isEmpty(dataCreazioneA) ? CalendarUtility.stringToCalendar(dataCreazioneA, this.getDatePattern()) : null;
	}

	public void setDataCreazioneA(String dataCreazioneA) {
		this.dataCreazioneA = dataCreazioneA;
	}

	public String getTipologiaFlusso() {
		return tipologiaFlusso;
	}

	public void setTipologiaFlusso(String tipologiaFlusso) {
		this.tipologiaFlusso = tipologiaFlusso;
	}

	@Override
	public void reset() {
		super.reset();
		this.codiceSocieta = "";
		this.companyCode = "";
		this.userCode = "";
		this.chiaveEnte = "";
		this.siglaProvincia = "";
		this.dataElaborazioneDa = "";
		this.dataElaborazioneA = "";
		this.dataCreazioneDa = "";
		this.dataCreazioneA = "";
		this.setValue("dataElaborazioneDa", null);
		this.setValue("dataElaborazioneA", null);
		this.setValue("dataCreazioneDa", null);
		this.setValue("dataCreazioneA", null);
		this.tipologiaFlusso = "";
 		super.setCodiceUtente(this.userCode);
		super.setCodiceProvincia(this.siglaProvincia);
//		resetDDLSession(request);
		this.statoFlusso = ""; //PG22XX09_YL5
	}
	
	protected void resetDDLSession(HttpServletRequest request)
	{
		HttpSession sessione = request.getSession();
		
		sessione.setAttribute("listaProvince", null);
		sessione.setAttribute("listaUtenti", null);
		sessione.setAttribute("listaEnti", null);
		
	}
	// INI PG22XX09_YL5
	/**
	 * @return the statoFlusso
	 */
	public String getStatoFlusso() {
		return statoFlusso;
	}

	/**
	 * @param statoFlusso the statoFlusso to set
	 */
	public void setStatoFlusso(String statoFlusso) {
		this.statoFlusso = statoFlusso;
	}
	// FINE PG22XX09_YL5
}