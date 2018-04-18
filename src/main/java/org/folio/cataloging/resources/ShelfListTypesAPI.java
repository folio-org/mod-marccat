package org.folio.cataloging.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.ShelfListType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * ShelfList type RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Shelflist type resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class ShelfListTypesAPI extends BaseResource {

    private Function<Avp<String>, ShelfListType> toShelfListType = source -> {
        final ShelfListType shelfListType = new ShelfListType();
        shelfListType.setCode(source.getValue());
        shelfListType.setDescription(source.getLabel());
        return shelfListType;
    };

    @ApiOperation(value = "Returns all types associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested types."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/shelflisy-types")
    public void getShelfListTypes(final String lang,
                                  final Map<String, String> okapiHeaders,
                                  final Handler<AsyncResult<Response>> asyncResultHandler,
                                  final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final ShelfListTypeCollection container = new ShelfListTypeCollection();
                container.setShelfListTypes(
                        storageService.getShelfListTypes(lang)
                                .stream()
                                .map(toShelfListType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingShelfListTypes(String lang, ShelfListType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
