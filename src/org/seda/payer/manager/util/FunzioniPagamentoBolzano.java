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
public enum FunzioniPagamentoBolzano {
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
        synchronized(FunzioniPagamentoBolzano.class) {
            if(rb==null)
                rb = ResourceBundle.getBundle(FunzioniPagamentoBolzano.class.getName());
            return MessageFormat.format(rb.getString(name()),args);
        }
    }
}
