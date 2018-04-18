package org.folio.cataloging.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.SeriesTreatmentType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Series treatment type RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Series treatment type resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class SeriesTreatmentTypesAPI extends BaseResource {

    private Function<Avp<String>, SeriesTreatmentType> toSeriesTreatmentType = source -> {
        final SeriesTreatmentType seriesTreatmentType = new SeriesTreatmentType();
        seriesTreatmentType.setCode(source.getValue());
        seriesTreatmentType.setDescription(source.getLabel());
        return seriesTreatmentType;
    };

    @ApiOperation(value = "Returns all types associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested types."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/series.treatment-types")
    public void getSeriesTreatmentTypes(final String lang,
                                        final Map<String, String> okapiHeaders,
                                        final Handler<AsyncResult<Response>> asyncResultHandler,
                                        final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final SeriesTreatmentTypeCollection container = new SeriesTreatmentTypeCollection();
                container.setSeriesTreatmentTypes(
                        storageService.getSeriesTreatmentTypes(lang)
                                .stream()
                                .map(toSeriesTreatmentType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingSeriesTreatmentTypes(String lang, SeriesTreatmentType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
