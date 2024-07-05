package org.seda.payer.manager.util;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

//import com.seda.payer.commons.bean.TypeRequest;

public class EnteViewState implements Serializable {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private String paramPrefix;
	private String companyCode;
	private String userCode;
	private String chiaveEnte;
	
	public EnteViewState(HttpServletRequest request, String paramPrefix) {
		this.request = request;
		this.paramPrefix = paramPrefix;
	}

	public String getParamPrefix() {
		return paramPrefix;
	}

	public void setParamPrefix(String paramPrefix) {
		this.paramPrefix = paramPrefix;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getChiaveEnte() {
		return chiaveEnte;
	}

	public void setChiaveEnte(String chiaveEnte) {
		this.chiaveEnte = chiaveEnte;
	}

	public String getTipoEnte() {
		return request.getParameter(paramPrefix + "_" + "tipoEnte");
	}

	public String getEmailAdmin() {
		return request.getParameter(paramPrefix + "_" + "emailAdmin");
	}

	public String getCFiscalePIva() {
		return request.getParameter(paramPrefix + "_" + "cFiscalePIva");
	}

	public String getCodSia() {
		return request.getParameter(paramPrefix + "_" + "codiceSia");
	}

	public String getFlagAutInvSMS() {
		return request.getParameter(paramPrefix + "_" + "flagAutInvSMS");
	}

//DOM
	public String getFlagBeneficiario() {
		return request.getParameter(paramPrefix + "_" + "flagBeneficiario");
	}

// fine DOM
	public String getDirFlussiEccInput() {
		return request.getParameter(paramPrefix + "_" + "dirFlussiEccInput");
	}

	public String getDirSaveFlussiEccInput() {
		return request.getParameter(paramPrefix + "_" + "dirSaveFlussiEccInput");
	}

	public String getDirFlussiRimbOutput() {
		return request.getParameter(paramPrefix + "_" + "dirFlussiRimbOutput");
	}

	public String getDirSaveFlussiRimbOutput() {
		return request.getParameter(paramPrefix + "_" + "dirSaveFlussiRimbOutput");
	}

	public String getSiteFtpFlussiRimbEcc() {
		return request.getParameter(paramPrefix + "_" + "siteFtpFlussiRimbEcc");
	}
	
	public String getUserSiteFtpFlussiRimbEcc() {
		return request.getParameter(paramPrefix + "_" + "userSiteFtpFlussiRimbEcc");
	}
	
	public String getPswSiteFtpFlussiRimbEcc() {
		return request.getParameter(paramPrefix + "_" + "pswSiteFtpFlussiRimbEcc");
	}
	
	public String getDirFlussiCbiInput() {
		return request.getParameter(paramPrefix + "_" + "dirFlussiCbiInput");
	}
	
	public String getDirSaveFlussiCbiInput() {
		return request.getParameter(paramPrefix + "_" + "dirSaveFlussiCbiInput");
	}
	
	public String getDirFlussiRimbEccInput() {
		return request.getParameter(paramPrefix + "_" + "dirFlussiRimbEccInput");
	}
	
	public String getDirSaveFlussiRimbEccInput() {
		return request.getParameter(paramPrefix + "_" + "dirSaveFlussiRimbEccInput");
	}
	
	public String getUserSiteFtpFlussiCbi() {
		return request.getParameter(paramPrefix + "_" + "userSiteFtpFlussiCbi");
	}
	
	public String getPswSiteFtpFlussiCbi() {
		return request.getParameter(paramPrefix + "_" + "pswSiteFtpFlussiCbi");
	}
	
	public String getSiteFtpFlussiCbi() {
		return request.getParameter(paramPrefix + "_" + "siteFtpFlussiCbi");
	}

	public String getFlagModello() {
		return request.getParameter(paramPrefix + "_" + "flagModello");
	}

	public String getEmailModello() {
		return request.getParameter(paramPrefix + "_" + "emailModello");
	}
	
	public String getFlagRuoli() {
		return request.getParameter(paramPrefix + "_" + "flagRuoli");
	}
	
	public String getDirectoryFtpFlussiCbi() {
		return request.getParameter(paramPrefix + "_" + "directoryFtpFlussiCbi");
	}
	
	public String getDirectoryFtpFlussiRimbEcc() {
		return request.getParameter(paramPrefix + "_" + "directoryFtpFlussiRimbEcc");
	}
	
	//inizio LP 20180628
	public String getFlagFlussoRendicontazioneNodo() {
		return request.getParameter(paramPrefix + "_" + "flagFlussoRendicontazioneNodo");
	}

	public String getListaEmailFlussoRendicontazioneNodo() {
		return request.getParameter(paramPrefix + "_" + "listaEmailFlussoRendicontazioneNodo");
	}

	public String getUrlWebServiceFlussoRendicontazioneNodo() {
		return request.getParameter(paramPrefix + "_" + "urlWebServiceFlussoRendicontazioneNodo");
	}
	//fine LP 20180628

	/* inizio PG18010 */
	public String getFlagGDC() {
		return request.getParameter(paramPrefix + "_" + "flagGDC");
	}
	public String getDirGDCInput() {
		return request.getParameter(paramPrefix + "_" + "dirGDCInput");
	}
	public String getDirGDCSave() {
		return request.getParameter(paramPrefix + "_" + "dirGDCSave");
	}
	public String getEndpointUniit() {
		return request.getParameter(paramPrefix + "_" + "endpointUniit");
	}

	/* fine PG180010*/

	//inizio LP PG180290
	public String getCodIpaEnte() {
		return request.getParameter(paramPrefix + "_" + "codIpaEnte");
	}
	public String getPasswordEnte() {
		return request.getParameter(paramPrefix + "_" + "passwordEnte");
	}
	public String getPasswordSha256Ente() {
		return request.getParameter(paramPrefix + "_" + "passwordSha256Ente");
	}
	//fine LP PG180290

	//inizio LP PG210040
	public String getCodiceGruppo() {
		return request.getParameter(paramPrefix + "_" + "codiceGruppo");
	}
	//inizio LP PG210040

	public String getFlagIntegrazioneJPPA() {
		return request.getParameter(paramPrefix + "_" + "integrazioneJPPA");
	}
	
	
	public void prepareData(String scope) {
		if (scope.equals("0")) {
			String ente = request.getParameter("ente_selected");
			String[] enteSplit = ente.split("\\|");
			companyCode = enteSplit[1];
			userCode = enteSplit[0];
			String ente2 = request.getParameter("ente_selected2");
			String[] enteSplit2 = ente2.split("\\|");
			chiaveEnte = enteSplit2[0];
		} else if (scope.equals("1")) {
			companyCode = request.getParameter(paramPrefix + "_" + "companyCode");
			userCode = request.getParameter(paramPrefix + "_" + "userCode");
			chiaveEnte = request.getParameter(paramPrefix + "_" + "chiaveEnte");
		}
	}
}