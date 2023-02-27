package org.seda.payer.manager.util.iban;

/* 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
*/


/**
 * Helper class to validate ISO Country codes
 * 
 * @author mgriffa
 * @since 3.3
 * @version $Id: ISOCountries.java,v 1.3 2007/06/24 17:39:35 zubri Exp $
 */
// TODO make usage of property file optional
public class ISOCountries extends PropertyResource {
	private static final ISOCountries instance = new ISOCountries();
	
	/**
	 * Default constructor
	 */
	protected ISOCountries() {
		super();
	}
	
	/**
	 * Get the unique instance of this object
	 * @return the object instance
	 */
	public static ISOCountries getInstance() {
		return instance;
	}
	
	protected String getResourceName() {
		return "org/seda/payer/manager/util/iban/countries.properties";
	}

}