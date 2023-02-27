/**
 * 
 */
package org.seda.payer.manager.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum ValidationMessages {
	msg_configurazione_descrizione_regex,
	msg_configurazione_ragionesociale_regex,
	msg_configurazione_descrizione_1,
	msg_configurazione_descrizione_2,
	msg_configurazione_descrizione_3,
	msg_configurazione_descrizione_4,
	msg_configurazione_alfanumerici,
	msg_configurazione_alfanumerici_maiuscolo,
	msg_configurazione_alfanumerici_spazio,
	msg_configurazione_alfanumerici_signature,
	msg_configurazione_alfanumerici_signature_API,
	msg_configurazione_testo,
	msg_configurazione_testo_spazio,
	msg_configurazione_testo_maiuscolo,
	msg_configurazione_numero_1_9,
	
	msg_configurazione_importo_3_2,
	msg_configurazione_importo_4_2,
	msg_configurazione_importo_6_2,
	msg_configurazione_importo_8_2,
	msg_configurazione_importo_N8_2,
	msg_configurazione_importo_13_2,
	
	msg_configurazione_IBAN,
	msg_configurazione_email,
	msg_configurazione_lista_email,
	msg_configurazione_ftp,
	msg_configurazione_directory_ftp,
	msg_configurazione_codicefiscale_piva,
	msg_configurazione_codicefiscale,
	msg_configurazione_indirizzo_ip,
	
	msg_configurazione_numero_maggiore_zero,
	
	msg_dataISO_valida,
	msg_configurazione_numero,
	msg_configurazione_AuxDigit,
	msg_configurazione_httpEndpoint

	, msg_configurazione_codice_fascia_tariffa //LP PG1800XX_016
	, msg_configurazione_codiceistat	//PG200390 GG
	, msg_configurazione_descrizioneGruppo_regex //PG210040
	;
	
    private static ResourceBundle rb;

    public String format( Object... args ) {
        synchronized(ValidationMessages.class) {
            if(rb==null)
                rb = ResourceBundle.getBundle(ValidationMessages.class.getName());
            return MessageFormat.format(rb.getString(name()),args);
        }
    }
}
