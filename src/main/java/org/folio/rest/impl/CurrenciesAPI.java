package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.Currency;
import org.folio.rest.jaxrs.model.CurrencyCollection;
import org.folio.rest.jaxrs.resource.CatalogingCurrenciesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Currency RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class CurrenciesAPI implements CatalogingCurrenciesResource {
    protected final Log logger = new Log(CurrenciesAPI.class);


    private Function<Avp<String>, Currency> toCurrency = source -> {
        final Currency currency = new Currency();
        //TODO: handle type Integer for stringValue element or null stringValue in Integer.parseInt
        currency.setCode(Integer.parseInt(source.getValue()));
        currency.setDescription(source.getLabel());
        return currency;
    };

    @Override
    public void getCatalogingCurrencies(final String lang,
                             final Map<String, String> okapiHeaders,
                             final Handler<AsyncResult<Response>> asyncResultHandler,
                             final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final CurrencyCollection container = new CurrencyCollection();
                container.setCurrencies (
                        storageService.getCurrencies(lang)
                                .stream()
                                .map(toCurrency)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingCurrencies(String lang, Currency entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }


}
