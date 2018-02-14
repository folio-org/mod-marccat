package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.FieldTemplate;
import org.folio.rest.jaxrs.resource.CatalogingFieldTemplateResource;

import javax.ws.rs.core.Response;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

public class FieldTemplateAPI implements CatalogingFieldTemplateResource {

    protected final Log logger = new Log(FrbrTypesAPI.class);

    @Override
    public void getCatalogingFieldTemplate(
            final String headingType,
            final String itemType,
            final String subItemType,
            final String functionCode,
            final String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
         doGet((storageService, future) -> {
            try {
                FieldTemplate template = new FieldTemplate();
                template.setCode("001");
                template.setInd1(1);
                template.setInd2(0);
                template.setSubfields(asList("a","b","c","d","x","0","1"));
                return template;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void deleteCatalogingFieldTemplate(String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }

    @Override
    public void putCatalogingFieldTemplate(String lang, FieldTemplate entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }
}
