package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.DescriptiveCatalogForm;
import org.folio.cataloging.resources.domain.DescriptiveCatalogFormCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Descriptive Catalog Forms RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Descriptive catalog forms resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class DescriptiveCatalogFormAPI extends BaseResource {

    private Function<Avp<String>, DescriptiveCatalogForm> toDescriptiveCatalogForm = source -> {
        final DescriptiveCatalogForm descriptiveCatalogForm = new DescriptiveCatalogForm();
        descriptiveCatalogForm.setCode(source.getValue());
        descriptiveCatalogForm.setDescription(source.getLabel());
        return descriptiveCatalogForm;
    };

    @ApiOperation(value = "Returns all forms associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested forms"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/descriptive-catalog-forms")
    public DescriptiveCatalogFormCollection getDescriptiveCatalogForms(
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
                final DescriptiveCatalogFormCollection container = new DescriptiveCatalogFormCollection();
                container.setDescriptiveCatalogForms(
                        storageService.getDescriptiveCatalogForms(lang)
                                .stream()
                                .map(toDescriptiveCatalogForm)
                                .collect(toList()));
                return container;
        }, tenant, configurator);
    }
}
