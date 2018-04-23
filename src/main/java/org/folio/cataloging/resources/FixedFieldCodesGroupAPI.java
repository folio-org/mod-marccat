package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Fixed-Field Codes Groups RESTful APIs.
 *
 * @author nbianchini
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Fixed field code group resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class FixedFieldCodesGroupAPI extends BaseResource {

    @ApiOperation(value = "Returns all mandatory groups.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested mandatory groups"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/fixed-fields-code-groups")
    public FixedFieldCodesGroup getCatalogingFixedFieldCodesGroups(
            @RequestParam final String code,
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
                final FixedFieldCodesGroup fixedFieldCodesGroup = new FixedFieldCodesGroup();
                return ofNullable(code)
                        .map(tag -> {
                            if (tag.equals(Global.LEADER_TAG_NUMBER)){
                                fixedFieldCodesGroup.setRecordStatusTypes(
                                        storageService.getRecordStatusTypes(lang)
                                                .stream()
                                                .map(toRecordStatusType)
                                                .collect(toList()));
                                fixedFieldCodesGroup.setRecordTypes(
                                        storageService.getRecordTypes(lang)
                                                .stream()
                                                .map(toRecordType)
                                                .collect(toList()));
                                fixedFieldCodesGroup.setBibliographicLevels(
                                        storageService.getBibliographicLevels(lang)
                                                .stream()
                                                .map(toBibliographicLevel)
                                                .collect(toList()));
                            } else if (tag.equals(Global.MATERIAL_TAG_CODE)){

                                //TODO : use headertypecode to get right drop-down list
                                fixedFieldCodesGroup.setDateTypes(
                                        storageService.getDateTypes(lang)
                                                .stream()
                                                .map(toDateType)
                                                .collect(toList()));

                                fixedFieldCodesGroup.setModifiedRecordTypes(
                                        storageService.getModifiedRecordTypes(lang)
                                                .stream()
                                                .map(toModifiedRecordType)
                                                .collect(toList()));

                                fixedFieldCodesGroup.setCatalogSources(
                                        storageService.getCatalogSources(lang)
                                                .stream()
                                                .map(toCatalogSource)
                                                .collect(toList()));
                            } else {
                                logger.error(String.format(MessageCatalog._00017_CODES_GROUPS_NOT_AVAILABLE, code));
                                return null;
                            }
                            return fixedFieldCodesGroup;
                        }).orElse(null);
        }, tenant, configurator);
    }

    /**
     * Adapter that converts existing stringValue object in modified record type Okapi resource.
     */
    private Function<Avp<String>, ModifiedRecordType> toModifiedRecordType = source -> {
        final ModifiedRecordType modifiedRecordType = new ModifiedRecordType();
        modifiedRecordType.setCode(source.getValue());
        modifiedRecordType.setDescription(source.getLabel());
        return modifiedRecordType;
    };

    /**
     * Adapter that converts existing stringValue object in catalog source Okapi resource.
     */
    private Function<Avp<String>, CatalogSource> toCatalogSource = source -> {
        final CatalogSource catalogSource = new CatalogSource();
        catalogSource.setCode(source.getValue());
        catalogSource.setDescription(source.getLabel());
        return catalogSource;
    };

    /**
     * Adapter that converts existing stringValue object in date type Okapi resource.
     */
    private Function<Avp<String>, DateType> toDateType = source -> {
        final DateType dateType = new DateType();
        dateType.setCode(source.getValue());
        dateType.setDescription(source.getLabel());
        return dateType;
    };

    /**
     * Adapter that converts existing stringValue object in record status type Okapi resource.
     */
    private Function<Avp<String>, RecordStatusType> toRecordStatusType = source -> {
        final RecordStatusType recordStatusType = new RecordStatusType();
        recordStatusType.setCode(source.getValue());
        recordStatusType.setDescription(source.getLabel());
        return recordStatusType;
    };

    /**
     * Adapter that converts existing stringValue object in Record type Okapi resource.
     */
    private Function<Avp<String>, RecordType> toRecordType = source -> {
        final RecordType recordType = new RecordType();
        recordType.setCode(source.getValue());
        recordType.setDescription(source.getLabel());
        return recordType;
    };

    /**
     * Adapter that converts existing stringValue object in bibliographic level Okapi resource.
     */
    private Function<Avp<String>, BibliographicLevel> toBibliographicLevel = source -> {
        final BibliographicLevel bibliographicLevel = new BibliographicLevel();
        bibliographicLevel.setCode(source.getValue());
        bibliographicLevel.setDescription(source.getLabel());
        return bibliographicLevel;
    };
}
