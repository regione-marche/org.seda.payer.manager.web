/**
 * 
 */
package org.seda.payer.manager.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author barnocchi
 *
 */
public interface PayerCommand {

	
	public Object execute(HttpServletRequest request);
	
	
}
