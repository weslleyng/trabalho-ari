package dev.weslley.trabalho.infra.controller.advice;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.weslley.trabalho.infra.controller.advice.model.ExceptionResponse;
import dev.weslley.trabalho.infra.controller.exceptions.RecursoNotFoundException;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	public final ExceptionResponse handleAllException(Throwable ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
				request.getDescription(false));
		LOGGER.error("CustomizedResponseEntity", ex);
		return exceptionResponse;

	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(RecursoNotFoundException.class)
	public final ExceptionResponse handleResourceNotFound(RecursoNotFoundException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
				request.getDescription(false));

		return exceptionResponse;
	}
}