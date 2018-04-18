package org.folio.cataloging.resources;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.shared.Validation;
import org.folio.rest.jaxrs.model.SubfieldsTag;
import org.folio.rest.jaxrs.resource.CatalogingSubfieldsTagResource;

import javax.ws.rs.core.Response;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

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
        doGet((storageService, configuration, future) -> {
                try {
                    final int category = Integer.parseInt(marcCategory);

                    final Validation validation = storageService.getSubfieldsByCorrelations(
                            category,
                            Integer.parseInt(code1),
                            Integer.parseInt(code2),
                            Integer.parseInt(code3));

                    final SubfieldsTag subfieldsTag = new SubfieldsTag();
                    subfieldsTag.setCategory(category);
                    subfieldsTag.setDefaultSubfield(String.valueOf(validation.getMarcTagDefaultSubfieldCode()));
                    subfieldsTag.setSubfields(stream(validation.getMarcValidSubfieldStringCode().split("")).collect(toList()));
                    subfieldsTag.setRepeatable(stream(validation.getRepeatableSubfieldStringCode().split("")).collect(toList()));
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
