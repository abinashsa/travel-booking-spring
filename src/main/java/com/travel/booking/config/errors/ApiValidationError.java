package com.travel.booking.config.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ApiValidationError implements ApiSubError {

	@JsonInclude(Include.NON_NULL)
	private String value;

	@JsonInclude(Include.NON_NULL)
	private Object rejected;
	@JsonInclude(Include.NON_NULL)
	private String message;

	public ApiValidationError(String value, Object rejected, String message) {
		super();
		this.value = value;
		this.rejected = rejected;
		this.message = message;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Object getRejected() {
		return rejected;
	}

	public void setRejected(Object rejected) {
		this.rejected = rejected;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
