package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.F;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.exception.DuplicateTagException;
import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.*;
import org.folio.cataloging.resources.domain.Error;
import org.folio.cataloging.shared.GeneralInformation;
import org.folio.cataloging.shared.Validation;
import org.folio.cataloging.util.StringText;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.folio.cataloging.F.isNotNullOrEmpty;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;
import static org.folio.cataloging.integration.CatalogingHelper.doPost;

/**
 * Bibliographic records API.
 *
 * @author nbianchini
 * @since 1.0
 */

@RestController
@Api(value = "modcat-api", description = "Get bibliographic record API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class BibliographicRecordAPI extends BaseResource {

    @ApiOperation(value = "Returns the bibliographic record associated with a given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested bibliographic record"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/bibliographic-record/{id}")
    public BibliographicRecord getRecord(@RequestParam final Integer id,
                                         @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
                                         @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {

        return doGet((storageService, configuration) -> {
           return storageService.getBibliographicRecordById(id, view);
        }, tenant, configurator);
    }

    @ApiOperation(value = "Returns a new empty bibliographic record with id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the empty bibliographic record"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/bibliographic-record/from-template/{id}")
    public BibliographicRecord getEmptyRecord(@RequestParam final Integer idTemplate,
                                                         @RequestParam final String lang,
                                                         @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
                                                         @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {

        return doGet((storageService, configuration) -> {

            BibliographicRecord bibliographicRecord = new BibliographicRecord();
            final RecordTemplate template = storageService.getBibliographicRecordRecordTemplatesById(idTemplate);
            bibliographicRecord.setId(storageService.generateNewKey(Global.AN_KEY_CODE_FIELD));
            bibliographicRecord.setLeader(ofNullable(template.getLeader()).map(leader -> template.getLeader())
                    .orElseGet(() -> {
                        Leader leader = new Leader();
                        leader.setCode(Global.LEADER_TAG_NUMBER);
                        leader.setValue(getLeaderValue());
                        return leader;
                    }));

            for (Field field : template.getFields()) {
                if (field.isMandatory()){
                    if (field.getCode().equals(Global.CONTROL_NUMBER_TAG_CODE)){
                        FixedField controlNumber = field.getFixedField();
                        controlNumber.setDisplayValue(F.padNumber("0", 11, bibliographicRecord.getId()));
                        field.setFixedField(controlNumber);
                    }
                    field.setFieldStatus(Field.FieldStatus.NEW);
                    bibliographicRecord.getFields().add(field);
                }
            }

            FixedField fixed005 = new FixedField();
            fixed005.setHeaderTypeCode(Global.DATETIME_TRANSACION_HEADER_TYPE);
            fixed005.setCode(Global.DATETIME_TRANSACION_TAG_CODE);
            fixed005.setDisplayValue(F.getFormattedDate("yyyyMMddHHmmss."));
            fixed005.setCategoryCode(Global.INT_CATEGORY);
            fixed005.setDescription(storageService.getHeadingTypeDescription(Global.DATETIME_TRANSACION_HEADER_TYPE, lang, Global.INT_CATEGORY));

            final Field field = new Field();
            field.setCode(Global.DATETIME_TRANSACION_TAG_CODE);
            field.setMandatory(true);
            field.setFixedField(fixed005);
            field.setFieldStatus(Field.FieldStatus.NEW);
            bibliographicRecord.getFields().add(1, field);

            return bibliographicRecord;

        }, tenant, configurator);

    }


    @ApiOperation(value = "Updates an existing record.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Method successfully updated the record."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @PostMapping("/bibliographic-record/{id}")
    public ResponseEntity<Object> save(
            @PathVariable final String id,
            @RequestBody final BibliographicRecord record,
            @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {

        return doPost((storageService, configuration) -> {
            try {

                final ErrorCollection errors = validate(record, storageService);
                if (errors.getErrors().size() > 0)
                    return systemInternalFailure(new DataAccessException(), errors);

                //conversione da bibbliographic-record a catalogItem
                final GeneralInformation gi = new GeneralInformation();
                gi.setDefaultValues(configuration);

                storageService.saveBibliographicRecord(record, view, gi);


                return new ResponseEntity<Object>(record, HttpStatus.OK);
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return record;
            }

        }, tenant, configurator, () -> isNotNullOrEmpty(id),  new String[]{record.getLeader().getValue(), "bibliographic", "material"});
    }

    /**
     * Validates tags record.
     *
     * @param record -- the record to validate.
     * @param storageService -- the storage service.
     * @return error collection.
     */
    private ErrorCollection validate(BibliographicRecord record, StorageService storageService) {
        final ErrorCollection errors = new ErrorCollection();

        if (!checkMandatory(record)){
            logger.error(MessageCatalog._00026_MANDATORY_FAILURE, record.getId());
            errors.getErrors().add(getError(Global.ERROR_MANDATORY_TAG));
        }

        String wrongTags = checkRepeatability(record, storageService);
        if (F.isNotNullOrEmpty(wrongTags)){
            logger.error(MessageCatalog._00025_DUPLICATE_TAG, wrongTags);
            errors.getErrors().add(getError(Global.ERROR_DUPLICATE_TAG, wrongTags));
        }

        String emptyTags = checkEmptyTag(record);
        if (F.isNotNullOrEmpty(emptyTags)){
            logger.error(MessageCatalog._00027_EMPTY_TAG, emptyTags);
            errors.getErrors().add(getError(Global.ERROR_EMPTY_TAG, emptyTags));
        }

        return errors;
    }

    private Error getError(final String code, final Object ... values){
        Error e = new Error();
        e.setCode(code);
        e.setDescription(values == null ? Global.ERRORS_MAP.get(code) : String.format(Global.ERRORS_MAP.get(code), values));
        return e;
    }

    /**
     * Checks if there is one or more empty tag.
     *
     * @param record -- the record with tags to check.
     * @throws DataAccessException in case of data access exception.
     */
    private String checkEmptyTag(final BibliographicRecord record) {
        StringBuilder tags = new StringBuilder();
        record.getFields().stream().forEach( field -> {
            if (!isFixedField(field)){ // && (field.getFieldStatus() != Field.FieldStatus.NEW && field.getFieldStatus() != Field.FieldStatus.CHANGED)
                final StringText st = new StringText(field.getVariableField().getValue());
                if (st.isEmpty()){
                    tags.append(field.getCode()).append(",");
                }
            }
        });

        if (tags.length() > 0)
            tags.deleteCharAt(tags.length()-1);
        return tags.toString();
    }

    /**
     * Checks tags repeatability.
     *
     * @param record -- the record with tags to check.
     * @param storage -- the storage service.
     * @throws DuplicateTagException in case of duplicate tag exception.
     */
    public String checkRepeatability(final BibliographicRecord record, final StorageService storage) {
        Map<String, List<Field>> fieldsGroupedByCode = record.getFields().stream().collect(Collectors.groupingBy(Field::getCode));
        List<String> duplicates =
                fieldsGroupedByCode.entrySet().stream()
                        .filter(entry -> entry.getValue().size() > 1)
                        .map(entry -> entry.getKey()).collect(Collectors.toList());

        StringBuilder tags = new StringBuilder();
        duplicates.stream().forEach( tagNbr -> {
            Validation bv = storage.getTagValidation(getCategory(fieldsGroupedByCode.get(tagNbr).get(0)), tagNbr);
            if (!bv.isMarcTagRepeatable()){
                tags.append(tagNbr).append(",");
            }
        });

        if (tags.length() > 0)
            tags.deleteCharAt(tags.length()-1);

        return tags.toString();
    }

    /**
     * Checks mandatory tags.
     *
     * @param record -- the record with tags to check.
     * @return check mandatory.
     */
    private boolean checkMandatory(final BibliographicRecord record) {
        List<String> found = new ArrayList<>();
        if (record.getLeader() != null)
            found.add(record.getLeader().getCode());

        record.getFields().stream().forEach(field -> {
            if (field.isMandatory()){
                found.add(field.getCode());
            }
        });
        ArrayList<String> result = new ArrayList<>(Global.MANDATORY_FIELDS);
        result.retainAll(found);
        return result.size() == Global.MANDATORY_FIELDS.size() && found.size() > 0;
    }
    /**
     * Check if is a fixed field or not.
     *
     * @param field the tag entity.
     * @return true if is fixedfield, false otherwise.
     */
    private boolean isFixedField(final Field field) {
        return Global.FIXED_FIELDS.contains(field.getCode());
    }

    /**
     * Utility to get category code.
     *
     * @param field -- the field containing category.
     * @return category.
     */
    private int getCategory(final Field field){
        if (isFixedField(field))
            return field.getFixedField().getCategoryCode();

        return field.getVariableField().getCategoryCode();
    }

    private Comparator<String> tagComparator = new Comparator<String>() {
       public int compare(final String f1, final String f2) {
           return f1.compareTo(f2);
       }
    };


   /* @GetMapping("/search")
    public SearchResponse search(
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
            @RequestParam("q") final String q,
            @RequestParam(name = "from", defaultValue = "1") final int from,
            @RequestParam(name = "to", defaultValue = "10") final int to,
            @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
            @RequestParam("ml") final int mainLibraryId,
            @RequestParam(name = "dpo", defaultValue = "1") final int databasePreferenceOrder,
            @RequestParam(name = "sortBy", required = false) final String[] sortAttributes,
            @RequestParam(name = "sortOrder", required = false) final String[] sortOrders) {
        return doGet((storageService, configuration) -> {
            final SearchEngine searchEngine =
                    SearchEngineFactory.create(
                            SearchEngineType.LIGHTWEIGHT,
                            mainLibraryId,
                            databasePreferenceOrder,
                            storageService);

            return searchEngine.fetchRecords(
                    (sortAttributes != null && sortOrders != null && sortAttributes.length == sortOrders.length)
                            ? searchEngine.sort(searchEngine.expertSearch(q, locale(lang), view), sortAttributes, sortOrders)
                            : searchEngine.expertSearch(q, locale(lang), view),
                    "F",
                    from,
                    to);
        }, tenant, configurator);
    }*/

    /**
     * Sets the default leader value.
     *
     * @return the default leader value.
     */
    private String getLeaderValue() {
        return new StringBuilder(Global.FIXED_LEADER_LENGTH)
                .append(Global.RECORD_STATUS_CODE)
                .append(Global.RECORD_TYPE_CODE)
                .append(Global.BIBLIOGRAPHIC_LEVEL_CODE)
                .append(Global.CONTROL_TYPE_CODE)
                .append(Global.CHARACTER_CODING_SCHEME_CODE)
                .append(Global.FIXED_LEADER_BASE_ADDRESS)
                .append(Global.ENCODING_LEVEL)
                .append(Global.DESCRIPTIVE_CATALOGUING_CODE)
                .append(Global.LINKED_RECORD_CODE)
                .append(Global.FIXED_LEADER_PORTION)
                .toString();
    }
}
