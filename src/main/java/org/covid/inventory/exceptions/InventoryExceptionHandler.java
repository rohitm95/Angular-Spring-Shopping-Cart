package org.covid.inventory.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class InventoryExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	MessageSource messageSource;

	/**
	 * Handle EntityNotFound exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<InventoryError> handleNotFoundException(EntityNotFoundException exception) {
		return createErrorObject(exception, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Handle SessionTimeoutException
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(SessionTimeoutException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<InventoryError> handleSessionTimeoutException(SessionTimeoutException exception) {
		return createErrorObject(exception, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * handle UniqueConstraintException
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(UniqueConstraintException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<InventoryError> handleUniqueConstraintException(UniqueConstraintException exception) {
		return createErrorObject(exception, HttpStatus.NOT_FOUND);
	}

	/**
	 * handle constraint voilation exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<InventoryError> constraintViolationException(ConstraintViolationException exception) {
		return createErrorObject(exception, HttpStatus.BAD_REQUEST);
	}

	/**
	 * error handle for @Valid
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", status.value());

		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("errors", errors);

		InventoryError inventoryException = new InventoryError();
		inventoryException.setMessage(errors);
		inventoryException.setStatus(HttpStatus.BAD_REQUEST.value());
		inventoryException.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
		return new ResponseEntity<>(inventoryException, HttpStatus.BAD_REQUEST);
	}

	/**
	 * handleHttpRequestMethodNotSupported
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder message = new StringBuilder();
		message.append(ex.getMethod());
		message.append(" method is not supported for this request. Supported methods are ");
		ex.getSupportedHttpMethods().forEach(t -> message.append(t + " "));

		InventoryError inventoryError = new InventoryError();
		inventoryError.setMessage(new ArrayList<String>() {
			{
				add(message.toString());
			}
		});
		inventoryError.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
		inventoryError.setError(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());

		return new ResponseEntity<Object>(inventoryError, HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 * handleHttpMediaTypeNotSupported
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder message = new StringBuilder();
		message.append(ex.getContentType());
		message.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> message.append(t + ", "));

		InventoryError inventoryException = new InventoryError();
		inventoryException.setMessage(new ArrayList<String>() {
			{
				add(message.toString());
			}
		});
		inventoryException.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
		inventoryException.setError(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase());

		return new ResponseEntity<Object>(inventoryException, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		StringBuilder message = new StringBuilder();
		message.append(ex.getRequestURL());
		message.append(" request URL is not supported.");

		InventoryError inventoryException = new InventoryError();
		inventoryException.setMessage(new ArrayList<String>() {
			{
				add(message.toString());
			}
		});
		inventoryException.setStatus(HttpStatus.NOT_FOUND.value());
		inventoryException.setError(HttpStatus.NOT_FOUND.getReasonPhrase());

		return new ResponseEntity<Object>(inventoryException, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handle Bad request exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(BadRequestException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<InventoryError> handleBadRequestException(BadRequestException exception) {
		InventoryError inventoryException = new InventoryError();
		inventoryException
				.setMessage(Arrays.asList(messageSource.getMessage(exception.getMessage(), exception.getArgs(), null)));
		inventoryException.setStatus(HttpStatus.BAD_REQUEST.value());
		inventoryException.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());

		return new ResponseEntity<InventoryError>(inventoryException, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle Bad request exception
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<InventoryError> handleServiceException(ServiceException exception) {
		InventoryError inventoryException = new InventoryError();
		inventoryException
				.setMessage(Arrays.asList(messageSource.getMessage(exception.getMessage(), exception.getArgs(), null)));
		inventoryException.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		inventoryException.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

		return new ResponseEntity<InventoryError>(inventoryException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		InventoryError inventoryException = new InventoryError();
		inventoryException.setMessage(Arrays.asList(messageSource.getMessage(ex.getMessage(), null, null)));
		inventoryException.setStatus(HttpStatus.BAD_REQUEST.value());
		inventoryException.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
		return new ResponseEntity<Object>(inventoryException, HttpStatus.BAD_REQUEST);
	}

	public static ResponseEntity<InventoryError> createErrorObject(Exception exception, HttpStatus status) {
		InventoryError inventoryException = new InventoryError();
		inventoryException.setMessage(Arrays.asList(exception.getMessage()));
		inventoryException.setStatus(status.value());
		inventoryException.setError(status.getReasonPhrase());

		return new ResponseEntity<InventoryError>(inventoryException, status);
	}
}
