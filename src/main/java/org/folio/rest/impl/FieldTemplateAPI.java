package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.folio.cataloging.Global;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.Validation;
import org.folio.rest.jaxrs.model.FieldTemplate;
import org.folio.rest.jaxrs.model.VariableField;
import org.folio.rest.jaxrs.resource.CatalogingFieldTemplateResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

public class FieldTemplateAPI implements CatalogingFieldTemplateResource {

    //protected static AbstractMapBackedFactory TAG_FACTORY = new MapBackedFactory();
    //protected static AbstractMapBackedFactory FIXED_FIELDS_FACTORY = new MapBackedFactory();

    /*static {
        final PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
        builder.load(
                "/org/folio/cataloging/business/cataloguing/bibliographic/tagFactory.properties",
                TAG_FACTORY);
        builder.load(
                "/org/folio/cataloging/business/cataloguing/bibliographic/fixedFieldFactory.properties",
                FIXED_FIELDS_FACTORY);
    }*/

    protected final Log logger = new Log(FieldTemplateAPI.class);


    /*public void getCatalogingFieldTemplate(
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
    }*/

    @Override
    public void getCatalogingFieldTemplate(final int categoryCode,
                                           final String ind1, final String ind2,
                                           final String code, final String lang,
                                           final Map<String, String> okapiHeaders,
                                           final Handler<AsyncResult<Response>> asyncResultHandler,
                                           final Context vertxContext) throws Exception {

        doGet((storageService, future) -> {
            try {
                return ofNullable(storageService.getFieldTemplate(categoryCode, ind1, ind2, code))
                        .map(correlationValues -> {
                            final short code1 = correlationValues.getValue(1);
                            final Class clazz = Global.firstCorrelationHeadingClassMap.get(Integer.toString(categoryCode));
                            final String description = storageService.getHeadingTypeDescription(code1, lang, clazz);
                            final Validation validation = storageService.getSubfieldsByCorrelations(Integer.toString(categoryCode), correlationValues.getValue(1),
                                    correlationValues.getValue(2), correlationValues.getValue(3));

                            final FieldTemplate fieldTemplate = new FieldTemplate();

                            injectVariableField(fieldTemplate, categoryCode, ind1, ind2, code, correlationValues, description, validation);
                            injectFixedField(fieldTemplate, categoryCode, code, correlationValues, description);

                            fieldTemplate.setCode(code);
                            return fieldTemplate;

                        }).orElse(null);

            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    //TODO: for fixed-fields
    private void injectFixedField(FieldTemplate fieldTemplate,
                                     final int categoryCode,
                                     final String code,
                                     final CorrelationValues correlations,
                                     final String description) {
        if (isFixedField(code)){
            fieldTemplate.setFixedField(null);
        }
    }

    private void injectVariableField(FieldTemplate fieldTemplate,
                             final int categoryCode,
                             final String ind1,
                             final String ind2,
                             final String code,
                             final CorrelationValues correlations,
                             final String description, final Validation validation) {

        if (!isFixedField(code))
        {
            VariableField variableField = new VariableField();
            variableField.setHeadingTypeCode(Short.toString(correlations.getValue(1)));
            variableField.setItemTypeCode(Short.toString(correlations.getValue(2)));
            variableField.setFunctionCode(Short.toString(correlations.getValue(3)));
            variableField.setCategoryCode(categoryCode);
            variableField.setCode(code);
            variableField.setInd1(ind1);
            variableField.setInd2(ind2);
            variableField.setDescription(description);

            variableField.setSubfields(stream(validation.getMarcValidSubfieldStringCode().split(""))
                                .collect(Collectors.toList()));
            variableField.setDefaultSubfieldCode(String.valueOf(validation.getMarcTagDefaultSubfieldCode()));

            fieldTemplate.setVariableField(variableField);
        }
    }

    private boolean isFixedField(final String tagNumber) {
        return Global.FIXED_FIELDS.contains(tagNumber);
    }

    @Override
    public void deleteCatalogingFieldTemplate(String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void putCatalogingFieldTemplate(String lang, FieldTemplate entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    public static <T> T doSync(Consumer<Future<T>> consumer, Integer wait) throws InterruptedException {

        AtomicReference<T> reference = new AtomicReference<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Future<T> callbackFuture = Future.future();
        consumer.accept(callbackFuture);

        callbackFuture.setHandler(
                e -> {
                    if (e.succeeded()) {
                        reference.set(e.result());
                    }
                    countDownLatch.countDown();
                });

        if (wait == null) {
            countDownLatch.await();
        } else {
            countDownLatch.await(wait, TimeUnit.SECONDS);
        }

        return reference.get();
    }
}
