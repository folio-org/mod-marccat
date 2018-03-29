package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.CharacterEncodingSchema;
import org.folio.rest.jaxrs.model.CharacterEncodingSchemaCollection;
import org.folio.rest.jaxrs.resource.CatalogingCharacterEncodingSchemasResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Character Encoding Schemas RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class CharacterEncodingSchemasAPI implements CatalogingCharacterEncodingSchemasResource {

    protected final Log logger = new Log(CharacterEncodingSchemasAPI.class);

    private Function<Avp<String>, CharacterEncodingSchema> toCharacterEncodingSchema = source -> {
        final CharacterEncodingSchema characterEncodingSchema = new CharacterEncodingSchema();
        characterEncodingSchema.setCode(source.getValue());
        characterEncodingSchema.setDescription(source.getLabel());
        return characterEncodingSchema;
    };

    @Override
    public void getCatalogingCharacterEncodingSchemas(
            final String lang, Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final CharacterEncodingSchemaCollection container = new CharacterEncodingSchemaCollection();
                container.setCharacterEncodingSchemas(
                        storageService.getCharacterEncodingSchemas(lang)
                                .stream()
                                .map(toCharacterEncodingSchema)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }


    @Override
    public void postCatalogingCharacterEncodingSchemas(String lang, CharacterEncodingSchema entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
