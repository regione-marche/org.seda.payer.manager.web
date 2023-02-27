package org.seda.payer.manager.entrate.actions;

import java.io.Serializable;

public class ArchivioCarichiTributoDiscarico implements Serializable {

  private static final long serialVersionUID = 1L;
  private int progressivoTributo;
  private String codiceTributo;
  //inizio LP PG22XX05
  private String idDominio;
  //fine LP PG22XX05
  private String annoTributo;
  private String impTributo;
  private String impPagatoCompresiSgravi;
  private String impResiduo;
  private String impDiscarico;
  private boolean impDiscaricoDisabled;

  public ArchivioCarichiTributoDiscarico() {
  }

  public int getProgressivoTributo() {
    return progressivoTributo;
  }

  public void setProgressivoTributo(int progressivoTributo) {
    this.progressivoTributo = progressivoTributo;
  }

  public String getCodiceTributo() {
    return codiceTributo;
  }

  public void setCodiceTributo(String codiceTributo) {
    this.codiceTributo = codiceTributo;
  }

  public String getAnnoTributo() {
    return annoTributo;
  }

  public void setAnnoTributo(String annoTributo) {
    this.annoTributo = annoTributo;
  }

  public String getImpTributo() {
    return impTributo;
  }

  public void setImpTributo(String impTributo) {
    this.impTributo = impTributo;
  }

  public String getImpPagatoCompresiSgravi() {
    return impPagatoCompresiSgravi;
  }

  public void setImpPagatoCompresiSgravi(String impPagatoCompresiSgravi) {
    this.impPagatoCompresiSgravi = impPagatoCompresiSgravi;
  }

  public String getImpResiduo() {
    return impResiduo;
  }

  public void setImpResiduo(String impResiduo) {
    this.impResiduo = impResiduo;
  }

  public String getImpDiscarico() {
    return impDiscarico;
  }

  public void setImpDiscarico(String impDiscarico) {
    this.impDiscarico = impDiscarico;
  }

  public boolean isImpDiscaricoDisabled() {
    return impDiscaricoDisabled;
  }

  public void setImpDiscaricoDisabled(boolean impDiscaricoDisabled) {
    this.impDiscaricoDisabled = impDiscaricoDisabled;
  }

  //inizio LP PG22XX05
  public String getIdDominio() {
	return idDominio;
  }

  public void setIdDominio(String idDominio) {
	this.idDominio = idDominio;
  }
  //fine LP PG22XX05

}
