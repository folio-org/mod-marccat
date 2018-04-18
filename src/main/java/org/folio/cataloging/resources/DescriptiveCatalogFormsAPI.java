package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.DescriptiveCatalogForm;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Descriptive Catalog Forms RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Descriptive catalog forms resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class DescriptiveCatalogFormsAPI extends BaseResource {

    private Function<Avp<String>, DescriptiveCatalogForm> toDescriptiveCatalogForm = source -> {
        final DescriptiveCatalogForm descriptiveCatalogForm = new DescriptiveCatalogForm();
        descriptiveCatalogForm.setCode(source.getValue());
        descriptiveCatalogForm.setDescription(source.getLabel());
        return descriptiveCatalogForm;
    };

    @Override
    public void getCatalogingDescriptiveCatalogForms(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final DescriptiveCatalogFormCollection container = new DescriptiveCatalogFormCollection();
                container.setDescriptiveCatalogForms(
                        storageService.getDescriptiveCatalogForms(lang)
                                .stream()
                                .map(toDescriptiveCatalogForm)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingDescriptiveCatalogForms(String lang, DescriptiveCatalogForm entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
