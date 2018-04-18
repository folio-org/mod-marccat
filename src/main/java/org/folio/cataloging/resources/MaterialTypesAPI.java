package org.folio.cataloging.resources;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.MaterialType;
import org.folio.rest.jaxrs.model.MaterialTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingMaterialTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Material type RESTful APIs.
 *
 * @author carment
 * @since 1.0
 */

public class MaterialTypesAPI implements CatalogingMaterialTypesResource {

    protected final Log logger = new Log(MaterialTypesAPI.class);

    private Function<Avp<String>, MaterialType> toMaterialType = source -> {
        final MaterialType materialType = new MaterialType();
        materialType.setCode(source.getValue());
        materialType.setDescription(source.getLabel());
        return materialType;
    };

    @Override
    public void getCatalogingMaterialTypes(final String lang,
                                  final Map<String, String> okapiHeaders,
                                  final Handler<AsyncResult<Response>> asyncResultHandler,
                                  final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final MaterialTypeCollection container = new MaterialTypeCollection();
                container.setMaterialTypes(
                        storageService.getMaterialTypes(lang)
                                .stream()
                                .map(toMaterialType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingMaterialTypes(String lang, MaterialType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
