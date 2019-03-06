package org.folio.marccat.resources.shared;

import org.folio.marccat.config.log.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.DuplicateTagException;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.BibliographicRecord;
import org.folio.marccat.resources.domain.Error;
import org.folio.marccat.resources.domain.ErrorCollection;
import org.folio.marccat.resources.domain.Field;
import org.folio.marccat.shared.Validation;
import org.folio.marccat.util.F;
import org.folio.marccat.util.StringText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.folio.marccat.resources.shared.FixedFieldUtils.isFixedField;
import static org.folio.marccat.resources.shared.RecordUtils.getCategory;

public class ValidationUtils {

  protected static Log logger = new Log(ValidationUtils.class);

  /**
   * Validates tags record.
   *
   * @param record         -- the record to validate.
   * @param storageService -- the storage service.
   * @return error collection.
   */
  public static ErrorCollection validate(BibliographicRecord record, StorageService storageService) {
    final ErrorCollection errors = new ErrorCollection();

    if (!checkMandatory(record)) {
      logger.error(Message.MOD_MARCCAT_00026_MANDATORY_FAILURE, record.getId());
      errors.getErrors().add(getError(Global.ERROR_MANDATORY_TAG));
    }

    String wrongTags = checkRepeatability(record, storageService);
    if (F.isNotNullOrEmpty(wrongTags)) {
      logger.error(Message.MOD_MARCCAT_00025_DUPLICATE_TAG, wrongTags);
      errors.getErrors().add(getError(Global.ERROR_DUPLICATE_TAG, wrongTags));
    }

    String emptyTags = checkEmptyTag(record);
    if (F.isNotNullOrEmpty(emptyTags)) {
      logger.error(Message.MOD_MARCCAT_00027_EMPTY_TAG, emptyTags);
      errors.getErrors().add(getError(Global.ERROR_EMPTY_TAG, emptyTags));
    }

    return errors;
  }


  /**
   * Create a new Error object.
   *
   * @param code   -- code of error.
   * @param values -- placeholders for description object.
   * @return a new error.
   */
  private static Error getError(final String code, final Object... values) {
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
  private static String checkEmptyTag(final BibliographicRecord record) {
    StringBuilder tags = new StringBuilder();
    record.getFields().forEach(field -> {
      if (!isFixedField(field)) {
        final StringText st = new StringText(field.getVariableField().getValue());
        if (st.isEmpty()) {
          tags.append(field.getCode()).append(",");
        }
      }
    });

    if (tags.length() > 0)
      tags.deleteCharAt(tags.length() - 1);
    return tags.toString();
  }

  /**
   * Checks tags repeatability.
   *
   * @param record  -- the record with tags to check.
   * @param storage -- the storage service.
   * @throws DuplicateTagException in case of duplicate tag exception.
   */
  private static String checkRepeatability(final BibliographicRecord record, final StorageService storage) {
    Map<String, List<Field>> fieldsGroupedByCode = record.getFields().stream().collect(Collectors.groupingBy(Field::getCode));
    List<String> duplicates =
      fieldsGroupedByCode.entrySet().stream()
        .filter(entry -> entry.getValue().size() > 1)
        .map(Map.Entry::getKey).collect(Collectors.toList());

    StringBuilder tags = new StringBuilder();
    duplicates.forEach(tagNbr -> {
      Validation bv = storage.getTagValidation(getCategory(fieldsGroupedByCode.get(tagNbr).get(0)), tagNbr);
      if (!bv.isMarcTagRepeatable()) {
        tags.append(tagNbr).append(",");
      }
    });

    if (tags.length() > 0)
      tags.deleteCharAt(tags.length() - 1);

    return tags.toString();
  }

  /**
   * Checks mandatory tags.
   *
   * @param record -- the record with tags to check.
   * @return check mandatory.
   */
  private static boolean checkMandatory(final BibliographicRecord record) {
    List<String> found = new ArrayList<>();
    if (record.getLeader() != null)
      found.add(record.getLeader().getCode());

    record.getFields().forEach(field -> found.add(field.getCode()));
    ArrayList<String> result = new ArrayList<>(Global.MANDATORY_FIELDS);
    result.retainAll(found);
    return result.size() == Global.MANDATORY_FIELDS.size() && !found.isEmpty();
  }
}
