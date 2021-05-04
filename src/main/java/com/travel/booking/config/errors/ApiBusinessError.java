package com.travel.booking.config.errors;

public class ApiBusinessError implements ApiSubError {

	private String message;

	public ApiBusinessError( String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
