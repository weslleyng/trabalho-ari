package dev.weslley.trabalho.infra.controller.exceptions;

public class RecursoNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RecursoNotFoundException(String message) {
		super(message);
	}

}