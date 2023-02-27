/**
 * 
 */
package org.seda.payer.manager.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;
/**
 * @author mmontisano
 *
 */
public enum FunzioniPagamentoTrentrisc {
	ICI,
	ISCOP,
	BOLLO,
	PREMARCATO,
	FRECCIA,
	MAV,
	CDS,
	SPONTANEO
	;
	
    private static ResourceBundle rb;

    public String format( Object... args ) {
        synchronized(FunzioniPagamentoTrentrisc.class) {
            if(rb==null)
                rb = ResourceBundle.getBundle(FunzioniPagamentoTrentrisc.class.getName());
            return MessageFormat.format(rb.getString(name()),args);
        }
    }
}
