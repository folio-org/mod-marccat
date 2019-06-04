package org.folio.marccat.resources;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.folio.common.pf.PartialFunction;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.exception.SystemInternalFailureException;
import org.folio.marccat.integration.Configuration;
import org.folio.marccat.resources.domain.ErrorCollection;
import org.folio.marccat.spring.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.function.Function;

import static org.folio.marccat.config.constants.Global.BASE_URI;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping(value = BASE_URI, produces = "application/json")
public abstract class BaseResource<K> implements BaseService<K> {
  protected Log logger = new Log(getClass());

  @Autowired
  protected Configuration configurator;

  @Autowired
  protected RestTemplate restTemplate;

  @Autowired @Qualifier("defaultExcHandler")
  private PartialFunction<Throwable, Response> exceptionHandler;

  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "System internal failure has occurred.")
  @ExceptionHandler(SystemInternalFailureException.class)
  public ResponseEntity<Object> systemInternalFailure(final Exception exception, final ErrorCollection errors) {
    logger.error(Message.MOD_MARCCAT_00011_NWS_FAILURE, exception);
    return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   *
   * @param result
   * @param mapper
   * @param asyncResultHandler
   * @param <T>
   */
  private <T> void responseHandler(Future<T> result, Function<T, Response> mapper,
                           Handler<AsyncResult<Response>> asyncResultHandler) {
    result.map(mapper)
      .otherwise(exceptionHandler)
      .setHandler(asyncResultHandler);
  }

}
