package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.CharacterEncodingSchema;
import org.folio.cataloging.resources.domain.CharacterEncodingSchemaCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Character Encoding Schemas RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Char Encoding Schema resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class CharacterEncodingSchemasAPI extends BaseResource {

    private Function<Avp<String>, CharacterEncodingSchema> toCharacterEncodingSchema = source -> {
        final CharacterEncodingSchema characterEncodingSchema = new CharacterEncodingSchema();
        characterEncodingSchema.setCode(source.getValue());
        characterEncodingSchema.setDescription(source.getLabel());
        return characterEncodingSchema;
    };

    @ApiOperation(value = "Returns all MARC categories associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested MARC categories"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("encoding-schemas")
    public CharacterEncodingSchemaCollection getCharacterEncodingSchemas(
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
            return doGet((storageService, configuration) -> {
                final CharacterEncodingSchemaCollection container = new CharacterEncodingSchemaCollection();
                container.setCharacterEncodingSchemas(
                        storageService.getCharacterEncodingSchemas(lang)
                                .stream()
                                .map(toCharacterEncodingSchema)
                                .collect(toList()));
                return container;
        }, tenant, configurator);
    }
}
