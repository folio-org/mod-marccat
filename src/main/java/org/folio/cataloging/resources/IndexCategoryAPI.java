package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.Category;
import org.folio.cataloging.resources.domain.CategoryType;
import org.folio.cataloging.resources.domain.IndexCategoryCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Logical views RESTful APIs.
 *
 * @author carment
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Index Category resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class IndexCategoryAPI extends BaseResource {

    private Function<Avp<Integer>, Category> convertValueLabelToCategory = source -> {
        final Category category = new Category();
        category.setCode(source.getValue());
        category.setDescription(source.getLabel());
        return category;
    };

    @ApiOperation(value = "Returns all index categories associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested index categories."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/index-categories")
    public IndexCategoryCollection getIndexCategories(
            @RequestParam final CategoryType type,
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
                final IndexCategoryCollection container = new IndexCategoryCollection();
                container.setCategories(
                        storageService.getIndexCategories(type.name(),lang)
                                .stream()
                                .map(convertValueLabelToCategory)
                                .collect(toList()));
                return container;
        }, tenant, configurator);
    }
}