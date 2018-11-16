package org.folio.marccat.integration;

import org.folio.marccat.dao.persistence.NameAccessPoint;
import org.folio.marccat.dao.persistence.NameTitleAccessPoint;
import org.folio.marccat.dao.persistence.SubjectAccessPoint;
import org.folio.marccat.dao.persistence.TitleAccessPoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Global constants of storage service.
 *
 * @author natasciab
 * @since 1.0
 */
public class GlobalStorage {

  public final static String DOLLAR = "\u001f";


  public final static int HEADER_CATEGORY = 1;
  public final static int NAME_CATEGORY = 2;
  public final static int TITLE_CATEGORY = 3;
  public final static int SUBJECT_CATEGORY = 4;
  public final static int CONTROL_NUMBER_CATEGORY = 5;
  public final static int CLASSIFICATION_CATEGORY = 6;
  public final static int PUBLISHER_CATEGORY = 7;
  public final static int BIB_NOTE_CATEGORY = 7;

  public final static int LEADER_HEADER_TYPE = 15;

  public final static int MATERIAL_DESCRIPTION_HEADER_TYPE = 31;

  public final static int DEFAULT_AVAILABILITY_STATUS = 99;
  public final static String DEFAULT_LEVEL_CARD = "L01";
  public final static String DEFAULT_MOTHER_LEVEL = "001";
  public final static String DEFAULT_LEVEL_NATURE = "001";
  public final static String YES_FLAG = "S";
  public final static String NO_FLAG = "N";
  public final static String CHARSET_UTF8 = "UTF-8";

  public final static String TITLE_REQUIRED_PERMISSION = "editTitle";
  public final static String NAME_REQUIRED_PERMISSION = "editName";
  public final static String CNTL_NBR_REQUIRED_PERMISSION = "editControlNumber";
  public final static String PUBLISHER_REQUIRED_PERMISSION = "editNotes";
  public final static String CLASSIFICATION_REQUIRED_PERMISSION = "editClassNumber";
  public final static String SUBJECT_REQUIRED_PERMISSION = "editSubject";
  public final static String NOTE_REQUIRED_PERMISSION = "editNote";

  public final static String TITLE_VARIANT_CODES = "3civ5";
  public final static String TITLE_ISSN_SERIES_SUBFIELD_CODE = "x";
  public final static String TITLE_VOLUME_SUBFIELD_CODE = "v";

  public final static String NAME_VARIANT_SUBFIELD_CODES = "3eiuox45";
  public final static String NAME_WORK_REL_STRING_TEXT_SUBFIELD_CODES = "eju";
  public final static String NAME_OTHER_SUBFIELD_CODES = "iox";
  public final static String NAME_TITLE_INSTITUTION_SUBFIELD_CODE = "5";

  public final static String WORK_REL_SUBFIELD_CODE = "4";

  public final static int PUBLISHER_DEFAULT_NOTE_TYPE = 24;

  public final static String PUBLISHER_FAST_PRINTER_SUBFIELD_CODES = "368efg";
  public final static String PUBLISHER_VARIANT_CODES = "368cefg";
  public final static String PUBLISHER_OTHER_SUBFIELD_CODES = "cefg";

  public final static int DEWEY_TYPE_CODE = 12;
  public final static String SUBJECT_VARIANT_CODES = "34eu";
  public final static String SUBJECT_WORK_REL_STRING_TEXT_SUBFIELD_CODES = "eu";

  public final static int STANDARD_NOTE_MAX_LENGHT = 1024;
  public final static int OVERFLOW_NOTE_MAX_LENGHT = 1000;

  public final static String NAME_TITLE_VARIANT_CODES = "3v5";


  public final static Map<String, Class> BIBLIOGRAPHIC_ACCESS_POINT_CLASS_MAP = new HashMap<String, Class>() {
    {
      put("NH", NameAccessPoint.class);
      put("TH", TitleAccessPoint.class);
      put("SH", SubjectAccessPoint.class);
      put("MH", NameTitleAccessPoint.class);
    }
  };

  public final static Map<String, String> INDEX_AUTHORITY_TYPE_MAP = new HashMap<String, String>() {
    {
      put("NH", "NK");
      put("TH", "TK");
      put("SH", "SK");
      put("MH", "NTK");
    }
  };

}
