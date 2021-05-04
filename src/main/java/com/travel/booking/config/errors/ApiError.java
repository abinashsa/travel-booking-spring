package com.travel.booking.config.errors;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

public class ApiError {

	private HttpStatus status;

	private String message;

	@JsonInclude(Include.NON_NULL)
	@ApiModelProperty(hidden = true)
	private String debugMessage;

	@JsonInclude(Include.NON_NULL)
	@ApiModelProperty(hidden = true)
	private String debugStack;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime horodatage;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<ApiSubError> errors;

	private ApiError() {
		horodatage = LocalDateTime.now();
	}

	public ApiError(HttpStatus status) {
		this();
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public String getDebugStack() {
		return debugStack;
	}

	public void setDebugStack(String debugStack) {
		this.debugStack = debugStack;
	}

	public LocalDateTime getHorodatage() {
		return horodatage;
	}

	public void setHorodatage(LocalDateTime horodatage) {
		this.horodatage = horodatage;
	}

	public List<ApiSubError> getErrors() {
		return errors;
	}

	public void setErrors(List<ApiSubError> errors) {
		this.errors = errors;
	}

}
