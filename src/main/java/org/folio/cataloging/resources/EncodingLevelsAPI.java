package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;

import org.folio.cataloging.log.MessageCatalog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Encoding Levels RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Encoding level resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class EncodingLevelsAPI implements CatalogingEncodingLevelsResource {

    private Function<Avp<String>, EncodingLevel> toEncodingLevel = source -> {
        final EncodingLevel encodingLevel = new EncodingLevel();
        encodingLevel.setCode(source.getValue());
        encodingLevel.setDescription(source.getLabel());
        return encodingLevel;
    };

    @Override
    public void getCatalogingEncodingLevels(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final EncodingLevelCollection container = new EncodingLevelCollection();
                container.setEncodingLevels(
                        storageService.getEncodingLevels(lang)
                                .stream()
                                .map(toEncodingLevel)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }
}
