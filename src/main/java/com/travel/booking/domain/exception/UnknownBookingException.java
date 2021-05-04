package com.travel.booking.domain.exception;

public class UnknownBookingException extends FunctionalException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7785704276807103210L;

	public UnknownBookingException(String message) {
	super(message);
	}
}
