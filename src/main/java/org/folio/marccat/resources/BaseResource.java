package org.folio.marccat.resources;

import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.exception.SystemInternalFailureException;
import org.folio.marccat.integration.Configuration;
import org.folio.marccat.resources.domain.ErrorCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

public abstract class BaseResource {
  protected Log logger = new Log(getClass());

  @Autowired
  protected Configuration configurator;

  @Autowired
  protected RestTemplate restTemplate;

  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "System internal failure has occurred.")
  @ExceptionHandler(SystemInternalFailureException.class)
  public ResponseEntity<Object> systemInternalFailure(final Exception exception, final ErrorCollection errors) {
    logger.error(Message.MOD_MARCCAT_00011_NWS_FAILURE, exception);
    return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public ResponseEntity<Object> notFoundFailure(final ErrorCollection errors, final String message) {
    logger.error(message, errors);
    return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
  }

}
