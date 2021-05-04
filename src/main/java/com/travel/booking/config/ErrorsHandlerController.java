package com.travel.booking.config;

import java.util.ArrayList;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.travel.booking.config.errors.ApiBusinessError;
import com.travel.booking.config.errors.ApiError;

import com.travel.booking.domain.exception.AgeException;
import com.travel.booking.domain.exception.FunctionalException;

/**
 * Class ErrorsHandlerController
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorsHandlerController {

	/** Constant : TYPE_ERROR. */
	private static final String TYPE_ERROR = "Type Error";

	/** Constant : VALIDATION_ERROR. */
	private static final String VALIDATION_ERROR = "Validation Error";

	/** Constant : ERREUR_APPEL. */
	private static final String REQUEST_ERROR = "Request Error";

	@ExceptionHandler(FunctionalException.class)
	public ResponseEntity<Object> handleGenericException(FunctionalException ctrlEx) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(ctrlEx.getMessage());
		addSubErrors(apiError, ctrlEx);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleTypeException(MethodArgumentTypeMismatchException mismatchEx) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(TYPE_ERROR);
		addSubErrors(apiError, mismatchEx);
		return buildResponseEntity(apiError);
	}
	@ExceptionHandler(AgeException.class)
	public ResponseEntity<Object> handleTypeException(AgeException ageException) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(VALIDATION_ERROR);
		addSubErrors(apiError, ageException);
		return buildResponseEntity(apiError);
	}

	
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleTypeException(ConstraintViolationException constraintEx) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(VALIDATION_ERROR);
		addSubErrors(apiError, constraintEx);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleTypeException(HttpMessageNotReadableException httpEx) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(TYPE_ERROR);
		addSubErrors(apiError, httpEx);
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException argumentEx) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(REQUEST_ERROR);
		addSubErrors(apiError, argumentEx);
		return buildResponseEntity(apiError);
	}

	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleValidationException(HttpRequestMethodNotSupportedException httpEx) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(REQUEST_ERROR);
		addSubErrors(apiError, httpEx);
		return buildResponseEntity(apiError);
	}


	private void addSubErrors(ApiError apiError, AgeException exception) {

		apiError.setErrors(exception.getExceptions().stream().map(excep ->{
			return new ApiBusinessError(excep.getMessage());	
		}).collect(Collectors.toList()));
	}
	private void addSubErrors(ApiError apiError, FunctionalException exception) {

		apiError.setErrors(new ArrayList<>());
		apiError.getErrors().add(new ApiBusinessError(exception.getMessage()));
	}

	private void addSubErrors(ApiError apiError, ConstraintViolationException constraintEx) {
		apiError.setErrors(constraintEx.getConstraintViolations().stream().map(violation -> {

			return new ApiBusinessError(violation.getMessage());
		}).collect(Collectors.toList()));
	}

	private void addSubErrors(ApiError apiError, MethodArgumentNotValidException argumentEx) {
		apiError.setErrors(argumentEx.getBindingResult().getFieldErrors().stream().map(violation -> {

			return new ApiBusinessError(violation.getDefaultMessage());
		}).collect(Collectors.toList()));
	}

	private void addSubErrors(ApiError apiError, HttpMessageNotReadableException httpEx) {
		if (httpEx.getCause() instanceof InvalidFormatException) {
			InvalidFormatException exception = (InvalidFormatException) httpEx.getCause();
			String errorMessage = generateMessage(exception);

			apiError.setErrors(new ArrayList<>());
			apiError.getErrors().add(new ApiBusinessError(errorMessage));
		}
		if (httpEx.getCause() instanceof UnrecognizedPropertyException) {
			UnrecognizedPropertyException exception = (UnrecognizedPropertyException) httpEx.getCause();
			String errorMessage = generateMessageUnkonwnField(exception);
			apiError.setErrors(new ArrayList<>());
			apiError.getErrors().add(new ApiBusinessError(errorMessage));
		}
		if (httpEx.getCause() instanceof JsonMappingException) {
			JsonMappingException exception = (JsonMappingException) httpEx.getCause();
			String errorMessage = generateMessageTypeError(exception);

			apiError.setErrors(new ArrayList<>());
			apiError.getErrors().add(new ApiBusinessError(errorMessage));
		}

	}

	private void addSubErrors(ApiError apiError, HttpRequestMethodNotSupportedException httpEx)

	{
		String errorMessage = generateMessage(httpEx);

		apiError.setErrors(new ArrayList<>());
		apiError.getErrors().add(new ApiBusinessError(errorMessage));

	}

	private void addSubErrors(ApiError apiError, MethodArgumentTypeMismatchException mismatchEx) {
		String errorMessage = generateMessage(mismatchEx);

		apiError.setErrors(new ArrayList<>());
		apiError.getErrors().add(new ApiBusinessError(errorMessage));

	}

	private String generateMessage(InvalidFormatException formatEx) {
		return String.format("Field %s (value : %s) is not correct", formatEx.getPath().get(0).getFieldName(),
				formatEx.getValue());
	}

	private String generateMessageTypeError(JsonMappingException ex) {
		String value = null;
		String message = ex.getMessage();
		if (message.contains("ACCEPT_FLOAT_AS_INT")) {
			return String.format("Field %s format is not valid", ex.getPath().get(0).getFieldName());
		} else if (message.contains("Unrecognized field")) {
			return String.format("Field %s is not an expected field", ex.getPath().get(0).getFieldName());
		}

		return String.format("Field %s (value : %s) is not correct", ex.getPath().get(0).getFieldName(), value);

	}

	private String generateMessageUnkonwnField(UnrecognizedPropertyException propertyEx) {
		return "Field " + propertyEx.getPropertyName() + " is not an expected field";
	}

	private String generateMessage(HttpRequestMethodNotSupportedException httpEx) {
		return String.format(" VERB %s is not supported for this resource", httpEx.getMethod());
	}

	private String generateMessage(MethodArgumentTypeMismatchException mismatchEx) {
		return String.format("Field %s (value : %s) is not correct", mismatchEx.getName(), mismatchEx.getValue());
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}
