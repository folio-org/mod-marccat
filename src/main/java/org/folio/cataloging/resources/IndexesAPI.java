package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.Constraint;
import org.folio.cataloging.resources.domain.Index;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Browsing and searching indexes API
 *
 * @author carment
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Index resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class IndexesAPI extends BaseResource {

    private Function<Avp<String>, Index> convertValueLabelToIndex = source -> {
        final Index index = new Index();
        index.setCode(source.getValue());
        index.setDescription(source.getLabel());
        return index;
    };

    private Function<Avp<String>, Constraint> convertValueLabelToConstraint = source -> {
        final Constraint constraint = new Constraint();
        constraint.setCode(source.getValue());
        constraint.setLabel(source.getLabel());
        return constraint;
    };

    @ApiOperation(value = "Returns all indexes associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested indexes."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/indexes")
    public void getCatalogingIndexesByCode(
            final String code,
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final Index container = new Index();
                container.setCode(code);
                container.setDescription(storageService.getIndexDescription(code, lang));
                container.setConstraints(
                        storageService.getIndexesByCode(code,lang)
                                .stream()
                                .map(convertValueLabelToConstraint)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void getCatalogingIndexes(
            final CatalogingIndexesResource.CategoryType categoryType,
            final int categoryCode,
            final String lang,
            final Map<String,String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final IndexCollection container = new IndexCollection();
                container.setIndexes(
                        storageService.getIndexes(categoryType.name(), categoryCode, lang)
                                .stream()
                                .map(convertValueLabelToIndex)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingIndexes(String lang, Index entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
        throw new IllegalArgumentException();
    }

    @Override
    public void deleteCatalogingIndexesByCode(String code, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
        throw new IllegalArgumentException();
    }

    @Override
    public void putCatalogingIndexesByCode(String code, String lang, Index entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        doPut((storageService, configuration, future) -> {
            try {
                // Here we have to manage the update of the entity associated with the incoming id, using the given state
                // (the entity attribute).
                // Note that the validation predicate (the last parameter) will be invoked before entering here so at
                // this time the incoming state (the entity parameter) is supposed to be valid.

                // So: here there's the update logic...

                // And here, since the interface require something to be returned, in case everything goes well
                // we will return the same entity passed in input.
                return null;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext, () -> {
            // Here each service should provide a body where the incoming entity is validated.
            // The function return true or false depending on the "congruency" of the entity.
            // Note that if you return false here, no other method will be called, a 400 BAD request will be returned.
            return false;
        });
    }
}