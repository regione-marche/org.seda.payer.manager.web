package org.seda.payer.manager.entrate.exceptions;

public class ConfigurazioneException extends Exception {
	
	/**
	 * Constructor of ConfigurazioneException.
	 * @param message - The message that we pass in case of this exception.
	 * @param cause - The {@link Throwable} class is the superclass of all errors and exceptions in the Java language.
	 */
	public ConfigurazioneException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * Constructor of ConfigurazioneException.
	 * @param message - The message that we pass in case of this exception.
	 */
	public ConfigurazioneException(String message) {
		super(message);
	}
	/**
	 * Constructor of ConfigurazioneException.
	 * @param cause - The {@link Throwable} class is the superclass of all errors and exceptions in the Java language.
	 */
	public ConfigurazioneException(Throwable cause) {
		super(cause);
	}

}
