package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.AbstractMapBackedFactory;
import org.folio.cataloging.business.common.MapBackedFactory;
import org.folio.cataloging.business.common.PropertyBasedFactoryBuilder;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.FieldTemplate;
import org.folio.rest.jaxrs.resource.CatalogingFieldTemplateResource;

import javax.ws.rs.core.Response;
import java.util.Map;

import static org.folio.cataloging.integration.CatalogingHelper.doGet;

public class FieldTemplateAPI implements CatalogingFieldTemplateResource {

    protected static AbstractMapBackedFactory TAG_FACTORY = new MapBackedFactory();
    protected static AbstractMapBackedFactory FIXED_FIELDS_FACTORY = new MapBackedFactory();

    static {
        final PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
        builder.load(
                "/org/folio/cataloging/business/cataloguing/bibliographic/tagFactory.properties",
                TAG_FACTORY);
        builder.load(
                "/org/folio/cataloging/business/cataloguing/bibliographic/fixedFieldFactory.properties",
                FIXED_FIELDS_FACTORY);
    }

    protected final Log logger = new Log(FrbrTypesAPI.class);

    @Override
    public void getCatalogingFieldTemplate(
            final int categoryCode,
            final String headingTypeCode,
            final String itemType,
            final String functionCode,
            final String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
         doGet((storageService, future) -> {
            try {
                Tag tag = (Tag) TAG_FACTORY.create(categoryCode);
                return null;
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
