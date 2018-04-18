package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.DetailLevel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Detail levels RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Detail level resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class DetailLevelsAPI implements CatalogingDetailLevelsResource {
    private Function<Avp<String>, DetailLevel> toDetailLevel = source -> {
        final DetailLevel detailLevel = new DetailLevel();
        detailLevel.setCode(source.getValue());
        detailLevel.setDescription(source.getLabel());
        return detailLevel;
    };

    @Override
    public void getCatalogingDetailLevels(final String lang,
                                final Map<String, String> okapiHeaders,
                                final Handler<AsyncResult<Response>> asyncResultHandler,
                                final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final DetailLevelCollection container = new DetailLevelCollection();
                container.setDetailLevels(
                        storageService.getDetailLevels(lang)
                                .stream()
                                .map(toDetailLevel)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingDetailLevels(String lang, DetailLevel entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }
}
