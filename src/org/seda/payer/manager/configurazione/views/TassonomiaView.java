package org.seda.payer.manager.configurazione.views;

import java.io.Serializable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.views.BaseView;
import org.seda.payer.manager.ws.WSCache;

import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaVerificaRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaVerificaResponse;

public class TassonomiaView extends BaseView implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String Name = "tassonomia";
	public static final String ActionName = "tassonomia";
	
	private String chiaveTassonomia;
	private String codiceTipoEnteCreditore;
	private String tipoEnteCreditore;
	private String progressivoMacroAreaPerEnteCreditore;
	private String nomeMacroArea;
	private String descrizioneMacroArea;
	private String codiceTipologiaServizio;
	private String tipoServizio;
	private String descrizioneServizio;
	private String motivoGiuridicoDellaRiscossione;
	private String versioneTassonomia;
	private String datiSpecificiIncasso;
	private String dataInizioValidita;
	private String dataFineValidita;
	private String listaTassonomia;

	public TassonomiaView(ServletContext context, HttpServletRequest request) {
		super(context, request);
	}

	public TassonomiaView populate(HttpServletRequest request, String scope) {
		super.populate(request, scope);
		if (RichiestaCancScope.equals(scope)) {
			setValue("richiestacanc", request.getParameter("richiestacanc"));
			setValue(Name + "_chiaveTassonomia", request.getParameter(Name + "_chiaveTassonomia"));
			this.chiaveTassonomia = getValue(Name + "_chiaveTassonomia");
		} else if (AddScope.equals(scope)) {
			this.codiceTipoEnteCreditore = getValue(Name + "_codiceTipoEnteCreditore");
			this.codiceTipologiaServizio = getValue(Name + "_codiceTipologiaServizio");
			//this.datiSpecificiIncasso = getValue(Name + "_datiSpecificiIncasso");
			this.descrizioneMacroArea = getValue(Name + "_descrizioneMacroArea");
			this.descrizioneServizio = getValue(Name + "_descrizioneServizio");
			this.motivoGiuridicoDellaRiscossione = getValue(Name + "_motivoGiuridicoDellaRiscossione");
			this.nomeMacroArea = getValue(Name + "_nomeMacroArea");
			this.progressivoMacroAreaPerEnteCreditore = getValue(Name + "_progressivoMacroAreaPerEnteCreditore");
			this.tipoEnteCreditore = getValue(Name + "_tipoEnteCreditore");
			this.tipoServizio = getValue(Name + "_tipoServizio");
			this.versioneTassonomia = getValue(Name + "_versioneTassonomia");
		} else if (EditScope.equals(scope)) {
			setValue(Name + "_chiaveTassonomia", request.getParameter(Name + "_chiaveTassonomia"));
			this.chiaveTassonomia = getValue(Name + "_chiaveTassonomia");
		} else if (CancelScope.equals(scope)) {
			this.chiaveTassonomia = getValue(Name + "_chiaveTassonomia");
		} else if (SaveScope.equals(scope) || SaveEditScope.equals(scope) || SaveAddScope.equals(scope)) {
			this.chiaveTassonomia = getValue(Name + "_chiaveTassonomia");
			this.codiceTipoEnteCreditore = getValue(Name + "_codiceTipoEnteCreditore");
			this.codiceTipologiaServizio = getValue(Name + "_codiceTipologiaServizio");
			//this.datiSpecificiIncasso = getValue(Name + "_datiSpecificiIncasso");
			this.descrizioneMacroArea = getValue(Name + "_descrizioneMacroArea");
			this.descrizioneServizio = getValue(Name + "_descrizioneServizio");
			this.motivoGiuridicoDellaRiscossione = getValue(Name + "_motivoGiuridicoDellaRiscossione");
			this.nomeMacroArea = getValue(Name + "_nomeMacroArea");
			this.progressivoMacroAreaPerEnteCreditore = getValue(Name + "_progressivoMacroAreaPerEnteCreditore");
			this.tipoEnteCreditore = getValue(Name + "_tipoEnteCreditore");
			this.tipoServizio = getValue(Name + "_tipoServizio");
			this.versioneTassonomia = getValue(Name + "_versioneTassonomia");
		} else if (BaseListScope.equals(scope)) {
			this.codiceTipoEnteCreditore = getValue(Name + "_codiceTipoEnteCreditore");
			this.codiceTipologiaServizio = getValue(Name + "_codiceTipologiaServizio");
			//this.datiSpecificiIncasso = getValue(Name + "_datiSpecificiIncasso");
			this.descrizioneMacroArea = getValue(Name + "_descrizioneMacroArea");
			this.descrizioneServizio = getValue(Name + "_descrizioneServizio");
			this.motivoGiuridicoDellaRiscossione = getValue(Name + "_motivoGiuridicoDellaRiscossione");
			this.nomeMacroArea = getValue(Name + "_nomeMacroArea");
			this.progressivoMacroAreaPerEnteCreditore = getValue(Name + "_progressivoMacroAreaPerEnteCreditore");
			this.tipoEnteCreditore = getValue(Name + "_tipoEnteCreditore");
			this.tipoServizio = getValue(Name + "_tipoServizio");
			this.versioneTassonomia = getValue(Name + "_versioneTassonomia");
		} else {
			this.codiceTipoEnteCreditore = getValue(Name + "_searchcodiceTipoEnteCreditore");
			this.codiceTipologiaServizio = getValue(Name + "_searchcodiceTipologiaServizio");
			//this.datiSpecificiIncasso = getValue(Name + "_searchdatiSpecificiIncasso");
			//this.descrizioneMacroArea = getValue(Name + "_searchdescrizioneMacroArea");
			//this.descrizioneServizio = getValue(Name + "_searchdescrizioneServizio");
			this.motivoGiuridicoDellaRiscossione = getValue(Name + "_searchmotivoGiuridicoDellaRiscossione");
			this.nomeMacroArea = getValue(Name + "_searchnomeMacroArea");
			this.progressivoMacroAreaPerEnteCreditore = getValue(Name + "_searchprogressivoMacroAreaPerEnteCreditore");
			this.tipoEnteCreditore = getValue(Name + "_searchtipoEnteCreditore");
			this.tipoServizio = getValue(Name + "_searchtipoServizio");
			this.versioneTassonomia = getValue(Name + "_searchversioneTassonomia");
		}
		return this;
	}

	public TassonomiaView setListaTassonomia(String listXml) {
		this.listaTassonomia = listXml;
		request.setAttribute("listaTassonomia", listXml);
		return this;
	}

	public TassonomiaView setListaTassonomiaPageInfo(com.seda.data.spi.PageInfo pageInfo) {
		super.setPageInfo(pageInfo);
		request.setAttribute("listaTassonomia.pageInfo", pageInfo);
		return this;
	}

	public String getListaTassonomia() {
		return listaTassonomia;
	}

	public String getChiaveTassonomia() {
		return chiaveTassonomia;
	}

	public TassonomiaView setChiaveTassonomia(String chiaveTassonomia) {
		this.chiaveTassonomia = chiaveTassonomia;
		setValue(Name + "_chiaveTassonomia", chiaveTassonomia);
		return this;
	}

	public String getCodiceTipoEnteCreditore() {
		return codiceTipoEnteCreditore;
	}

	public TassonomiaView setCodiceTipoEnteCreditore(String codiceTipoEnteCreditore) {
		this.codiceTipoEnteCreditore = codiceTipoEnteCreditore;
		setValue(Name + "_codiceTipoEnteCreditore", codiceTipoEnteCreditore);
		return this;
	}

	public String getTipoEnteCreditore() {
		return tipoEnteCreditore;
	}

	public TassonomiaView setTipoEnteCreditore(String tipoEnteCreditore) {
		this.tipoEnteCreditore = tipoEnteCreditore;
		setValue(Name + "_tipoEnteCreditore", tipoEnteCreditore);
		return this;
	}

	public String getProgressivoMacroAreaPerEnteCreditore() {
		return progressivoMacroAreaPerEnteCreditore;
	}

	public TassonomiaView setProgressivoMacroAreaPerEnteCreditore(String progressivoMacroAreaPerEnteCreditore) {
		this.progressivoMacroAreaPerEnteCreditore = progressivoMacroAreaPerEnteCreditore;
		setValue(Name + "_progressivoMacroAreaPerEnteCreditore", progressivoMacroAreaPerEnteCreditore);
		return this;
	}

	public String getNomeMacroArea() {
		return nomeMacroArea;
	}

	public TassonomiaView setNomeMacroArea(String nomeMacroArea) {
		this.nomeMacroArea = nomeMacroArea;
		setValue(Name + "_nomeMacroArea", nomeMacroArea);
		return this;
	}

	public String getDescrizioneMacroArea() {
		return descrizioneMacroArea;
	}

	public TassonomiaView setDescrizioneMacroArea(String descrizioneMacroArea) {
		this.descrizioneMacroArea = descrizioneMacroArea;
		setValue(Name + "_descrizioneMacroArea", descrizioneMacroArea);
		return this;
	}

	public String getCodiceTipologiaServizio() {
		return codiceTipologiaServizio;
	}

	public TassonomiaView setCodiceTipologiaServizio(String codiceTipologiaServizio) {
		this.codiceTipologiaServizio = codiceTipologiaServizio;
		setValue(Name + "_codiceTipologiaServizio", codiceTipologiaServizio);
		return this;
	}

	public String getTipoServizio() {
		return tipoServizio;
	}

	public TassonomiaView setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
		setValue(Name + "_tipoServizio", tipoServizio);
		return this;
	}

	public String getDescrizioneServizio() {
		return descrizioneServizio;
	}

	public TassonomiaView setDescrizioneServizio(String descrizioneServizio) {
		this.descrizioneServizio = descrizioneServizio;
		setValue(Name + "_descrizioneServizio", descrizioneServizio);
		return this;
	}

	public String getMotivoGiuridicoDellaRiscossione() {
		return motivoGiuridicoDellaRiscossione;
	}

	public TassonomiaView setMotivoGiuridicoDellaRiscossione(String motivoGiuridicoDellaRiscossione) {
		this.motivoGiuridicoDellaRiscossione = motivoGiuridicoDellaRiscossione;
		setValue(Name + "_motivoGiuridicoDellaRiscossione", motivoGiuridicoDellaRiscossione);
		return this;
	}

	public String getVersioneTassonomia() {
		return versioneTassonomia;
	}

	public TassonomiaView setVersioneTassonomia(String versioneTassonomia) {
		this.versioneTassonomia = versioneTassonomia;
		setValue(Name + "_versioneTassonomia", versioneTassonomia);
		return this;
	}

	public String getDatiSpecificiIncasso() {
		return datiSpecificiIncasso;
	}

	public TassonomiaView setDatiSpecificiIncasso(String datiSpecificiIncasso) {
		this.datiSpecificiIncasso = datiSpecificiIncasso;
		setValue(Name + "_datiSpecificiIncasso", datiSpecificiIncasso);
		return this;
	}

	public String getDataInizioValidita() {
		return dataInizioValidita;
	}

	public TassonomiaView setDataInizioValidita(String dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
		setValue(Name + "_dataInizioValidita", dataInizioValidita);
		return this;
	}

	public String getDataFineValidita() {
		return dataFineValidita;
	}

	public TassonomiaView setDataFineValidita(String dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
		setValue(Name + "_dataFineValidita", dataFineValidita);
		return this;
	}

	public boolean validate() throws Exception {
		ConfigTassonomiaVerificaRequest in = new ConfigTassonomiaVerificaRequest();
		in.setChiaveTassonomia(this.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0 ? null : this.getChiaveTassonomia());
		in.setCodiceTipoEnteCreditore(this.codiceTipoEnteCreditore);
		in.setCodiceTipologiaServizio(this.codiceTipologiaServizio);
		in.setMotivoGiuridicoDellaRiscossione(this.motivoGiuridicoDellaRiscossione);
		in.setProgressivoMacroAreaPerEnteCreditore(this.progressivoMacroAreaPerEnteCreditore);
		//in.setVersioneTassonomia(this.versioneTassonomia);
		ConfigTassonomiaVerificaResponse res = WSCache.configTassonomiaServer.verifica(in, request);
		if (res.isWasExist()) {
			this.setMessage("Tassonomia con chiave: 'Dati Specifici Incasso' già presente!");
			this.setError(true);
			return false;
		}
		if (this.getDataDa() == null
			|| this.getDataA() == null
			|| this.getDataDa().after(this.getDataA())) {
			this.setMessage("'Data Fine Validità' deve essere successiva a 'Data Inizio Validità'!");
			this.setError(true);
			return false;
		}
		return true;
	}

	@Override
	public void reset() {
		super.reset();
		setCodiceTipoEnteCreditore("");
		setTipoEnteCreditore("");
		setProgressivoMacroAreaPerEnteCreditore("");
		setNomeMacroArea("");
		setDescrizioneMacroArea("");
		setCodiceTipologiaServizio("");
		setTipoServizio("");
		setDescrizioneServizio("");
		setMotivoGiuridicoDellaRiscossione("");
		setVersioneTassonomia("");
		setDatiSpecificiIncasso("");
	}
}