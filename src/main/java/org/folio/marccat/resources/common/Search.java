package org.folio.marccat.resources.common;


import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.marccat.business.common.View;
import org.folio.rest.annotations.Validate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * @author: Christian Chiama
 * @date: 05/06/2019 01:03
 * <br/>
 * <br/>
 * @project: folio/mod-marccat 2019
 * <br/>
 * <br/>
 * @packageName: org.folio.marccat.resources.common
 * @className: Search
 * <br/>
 **/
@Path("/search")
public interface Search {
  /**
   * Return a list of marc record
   *
   * @param query                   A query expressed as a CQL string
   *                                (see [dev.folio.org/reference/glossary#cql](https://dev.folio.org/reference/glossary#cql))
   *                                using valid searchable fields.
   *                                The first example below shows the general form of a full CQL query,
   *                                but those fields might not be relevant in this context.
   * @param from                    Skip over a number of elements by specifying an offset value for the query
   *                                default value: "0"
   *                                minimum value: "0.0"
   *                                maximum value: "2.147483647E9"
   * @param to                      Limit the number of elements returned in the response
   *                                default value: "10"
   *                                minimum value: "0.0"
   *                                maximum value: "2.147483647E9"
   * @param mainLibraryId           a param to search
   * @param databasePreferenceOrder Limit the number of elements returned in the response
   *                                default value: "10"
   *                                minimum value: "0.0"
   *                                maximum value: "2.147483647E9"
   * @param sortAttributes          List of attribute for sort attributes
   *                                default value: "10"
   *                                minimum value: "0.0"
   *                                maximum value: "2.147483647E9"
   * @param sortOrders              List of attribute for sort orders
   *                                default value: "10"
   *                                minimum value: "0.0"
   *                                maximum value: "2.147483647E9"
   * @param asyncResultHandler      An AsyncResult<Response> Handler  {@link Handler} which must be called as follows
   *                                - Note the 'SearchResponse' should be replaced with
   *                                '[nameOfYourFunction]Response': (example only)
   *                                <code>asyncResultHandler.handle(io.vertx.core.Future.succeededFuture(SearchResponse.withJsonOK( new ObjectMapper().readValue(reply.result().body().toString(), SearchResponse.class))));
   *                                </code> in the final callback (most internal callback) of the function.
   * @param vertxSpringContext      The Vertx Spring Context Object <code>io.vertx.core.Context</code>
   * @param okapiHeaders            Case insensitive map of x-okapi-* headers passed in as part of the request <code>java.util.Map<String, String></code>
   */
  @GET
  @Produces({
    "application/json",
    "text/plain"
  })
  @Validate
  void search(
    @QueryParam("q") String query,
    @QueryParam("from") @DefaultValue("1") @Min(0) @Max(2147483647) int from,
    @QueryParam("to") @DefaultValue("10") @Min(0) @Max(2147483647) int to,
    @QueryParam("view") @DefaultValue(View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) String view,
    @QueryParam("ml") int mainLibraryId,
    @QueryParam("dpo") @DefaultValue("0") @Min(0) @Max(2147483647) int databasePreferenceOrder,
    @QueryParam("sortBy") @DefaultValue("0") @Min(0) @Max(2147483647) String[] sortAttributes,
    @QueryParam("sortOrder") @DefaultValue("0") @Min(0) @Max(2147483647) String[] sortOrders,
    Map<String, String> okapiHeaders,
    Handler<AsyncResult<Response>> asyncResultHandler,
    Context vertxSpringContext);
}
