package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.shared.Validation;
import org.folio.rest.jaxrs.model.SubfieldsTag;
import org.folio.rest.jaxrs.resource.CatalogingSubfieldsTagResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Subfield codes for Tag entity RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */

public class SubfieldsTagAPI implements CatalogingSubfieldsTagResource {

    protected final Log logger = new Log(SubfieldsTagAPI.class);

    @Override
    public void getCatalogingSubfieldsTag(  final String marcCategory,
                                            final String code1,
                                            final String code2,
                                            final String code3,
                                            final String lang,
                                            final Map<String, String> okapiHeaders,
                                            final Handler<AsyncResult<Response>> asyncResultHandler,
                                            final Context vertxContext) throws Exception {

            doGet((StorageService storageService, Future future) -> {
                try {
                    final int intCode1 = Integer.parseInt(code1);
                    final int intCode2 = Integer.parseInt(code2);
                    final int intCode3 = Integer.parseInt(code3);

                    final Validation validation = storageService.getSubfieldsByCorrelations(marcCategory, intCode1, intCode2, intCode3);
                    final SubfieldsTag subfieldsTag = new SubfieldsTag();

                    subfieldsTag.setCategory(Integer.parseInt(marcCategory));
                    subfieldsTag.setDefaultSubfield(String.valueOf(validation.getMarcTagDefaultSubfieldCode()));
                    subfieldsTag.setSubfields(stream(validation.getMarcValidSubfieldStringCode().split("")).collect(Collectors.toList()));
                    subfieldsTag.setRepeatable(stream(validation.getRepeatableSubfieldStringCode().split("")).collect(Collectors.toList()));
                    subfieldsTag.setTag(validation.getKey().getMarcTag());


                    return subfieldsTag;

                } catch (final Exception exception) {
                    logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                    return null;
                }
            }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingSubfieldsTag(String lang, SubfieldsTag entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
