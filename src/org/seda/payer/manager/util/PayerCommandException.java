/**
 * 
 */
package org.seda.payer.manager.util;

/**
 * @author barnocchi
 *
 */
public class PayerCommandException extends RuntimeException {

	public PayerCommandException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PayerCommandException(String arg0) {
		super(arg0);
	}

	public PayerCommandException(Throwable arg0) {
		super(arg0);
	}

}
