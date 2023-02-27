package org.seda.payer.manager.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class PresenzeAbilitate implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String giorno;
	private String causale;
	private String progressivo;
	private String dataPresenza;
	private String servizioAbilitato;
	private String descrizioneTributo;
	private String dataPresenzaPerDatagrid;
	private String maxProgressivo;
	private String operatoreInserimento;
	private String importo;
	private Date dataPresenzaComparator;
	private String flagRendicontazione;
	private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdfdg=new SimpleDateFormat("dd/MM/yyyy");
	
	public PresenzeAbilitate(Map.Entry pair) {
		super();
		this.giorno=String.valueOf(((Calendar)pair.getKey()).get(Calendar.DAY_OF_MONTH));
		String concat=(String) pair.getValue();
		//C,D,R,S
		this.causale=concat.split("\\|")[0];
		//1,2,3...
		this.progressivo=concat.split("\\|")[1];
		//Y,N
		this.servizioAbilitato=concat.split("\\|")[2];
		//MAX PROG 
		this.maxProgressivo=concat.split("\\|")[3];
		//Operatore Inserimento
		this.operatoreInserimento=(concat.split("\\|")[4]).trim();
		//importo
		this.importo=(concat.split("\\|")[5]).trim();
		//...
		//14102015 GG Rif. segnalazione mail di Polenta - inizio
		//this.descrizioneTributo=concat.substring(8+progressivo.length()+maxProgressivo.length()+operatoreInserimento.length()+importo.length());
		this.descrizioneTributo=(concat.split("\\|")[6]).trim();
		//14102015 GG Rif. segnalazione mail di Polenta - fine
		
		this.dataPresenza=sdf.format(((Calendar)pair.getKey()).getTime());
		this.dataPresenzaPerDatagrid=sdfdg.format(((Calendar)pair.getKey()).getTime());
		// TODO Auto-generated constructor stub
		this.dataPresenzaComparator=((Calendar)pair.getKey()).getTime();
		this.flagRendicontazione=(concat.split("\\|")[7]).trim();
	}


	
	public String getGiorno() {
		return giorno;
	}



	public void setGiorno(String giorno) {
		this.giorno = giorno;
	}



	



	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}


	public String getProgressivo() {
		return progressivo;
	}


	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}


	public String getDataPresenza() {
		return dataPresenza;
	}



	public void setDataPresenza(String dataPresenza) {
		this.dataPresenza = dataPresenza;
	}

    

	public String getServizioAbilitato() {
		return servizioAbilitato;
	}



	public void setServizioAbilitato(String servizioAbilitato) {
		this.servizioAbilitato = servizioAbilitato;
	}


	 

	public String getDescrizioneTributo() {
		return descrizioneTributo;
	}



	public void setDescrizioneTributo(String descrizioneTributo) {
		this.descrizioneTributo = descrizioneTributo;
	}



	public String getDataPresenzaPerDatagrid() {
		return dataPresenzaPerDatagrid;
	}



	public void setDataPresenzaPerDatagrid(String dataPresenzaPerDatagrid) {
		this.dataPresenzaPerDatagrid = dataPresenzaPerDatagrid;
	}
  
   

	public String getMaxProgressivo() {
		return maxProgressivo;
	}



	public void setMaxProgressivo(String maxProgressivo) {
		this.maxProgressivo = maxProgressivo;
	}

	
	
	


	public Date getDataPresenzaComparator() {
		return dataPresenzaComparator;
	}



	public void setDataPresenzaComparator(Date dataPresenzaComparator) {
		this.dataPresenzaComparator = dataPresenzaComparator;
	}



	public String getOperatoreInserimento() {
		return operatoreInserimento;
	}



	public void setOperatoreInserimento(String operatoreInserimento) {
		this.operatoreInserimento = operatoreInserimento;
	}


	public String getImporto() {
		return importo;
	}


	public void setImporto(String importo) {
		this.importo = importo;
	}

	public String getFlagRendicontazione() {
		return flagRendicontazione;
	}

	public void setFlagRendicontazione(String flagRendicontazione) {
		this.flagRendicontazione = flagRendicontazione;
	}



	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PresenzeAbilitate [causale=");
		builder.append(causale);
		builder.append(", dataPresenza=");
		builder.append(dataPresenza);
		builder.append(", dataPresenzaComparator=");
		builder.append(dataPresenzaComparator);
		builder.append(", dataPresenzaPerDatagrid=");
		builder.append(dataPresenzaPerDatagrid);
		builder.append(", descrizioneTributo=");
		builder.append(descrizioneTributo);
		builder.append(", giorno=");
		builder.append(giorno);
		builder.append(", importo=");
		builder.append(importo);
		builder.append(", maxProgressivo=");
		builder.append(maxProgressivo);
		builder.append(", operatoreInserimento=");
		builder.append(operatoreInserimento);
		builder.append(", progressivo=");
		builder.append(progressivo);
		builder.append(", servizioAbilitato=");
		builder.append(servizioAbilitato);
		builder.append(", flagRendicontazione=");
		builder.append(flagRendicontazione);
		builder.append("]");
		return builder.toString();
	}



	


	



	
 
 
 
	



	



	
	


	
	
}
