package org.folio.marccat.config;

import net.sf.hibernate.cfg.Configuration;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Global constants.
 * With "Global" we mean a constant that
 *
 * <ul>
 * <li>is supposed to be shared at least between 2 modules.</li>
 * <li>needs to be used within this "shared" module</li>
 * </ul>
 *
 * @author paulm
 * @author agazzarini
 * @author natasciab
 * @since 1.0
 */
// TODO: clean up (lot of unused fields)
public abstract class Global {
  public static final String OKAPI_TENANT_HEADER_NAME = "x-okapi-tenant";
  public static final String EMPTY_STRING = "";
  public final static String SUBFIELD_DELIMITER_FOR_VIEW = "\\$";

  public final static String MATERIAL_TAG_CODE = "008";
  public final static String OTHER_MATERIAL_TAG_CODE = "006";

  //bibliographic
  public final static String ITEM_DATE_FIRST_PUBLICATION = "    ";
  public final static String ITEM_DATE_LAST_PUBLICATION = "    ";

  //default values for physical description (tag 007)
  public final static String MAP_CODE = "a";
  public final static String ELECTRONIC_RESOURCE = "c";
  public final static String GLOBE = "d";
  public final static String TACTILE_MATERIAL = "f";
  public final static String PROJECTED_GRAPHIC = "g";
  public final static String MICROFORM = "h";
  public final static String NON_PROJECTED_GRAPHIC = "k";
  public final static String MOTION_PICTURE = "m";
  public final static String KIT_CODE = "o";
  public final static String NOTATED_MUSIC = "q";
  public final static String REMOTE_SENSING_IMAGE = "r";
  public final static String SOUND_RECORDING = "s";
  public final static String TEXT_CODE = "t";
  public final static String VIDEO_RECORDING = "v";
  public final static String UNSPECIFIED = "z";
  public final static Map<Integer, String> PHYSICAL_TYPES_MAP = new HashMap<Integer, String>() {
    {
      put(23, GLOBE);
      put(24, MAP_CODE);
      put(25, MICROFORM);
      put(26, MOTION_PICTURE);
      put(27, NON_PROJECTED_GRAPHIC);
      put(28, PROJECTED_GRAPHIC);
      put(29, SOUND_RECORDING);
      put(30, VIDEO_RECORDING);
      put(42, ELECTRONIC_RESOURCE);
      put(43, REMOTE_SENSING_IMAGE);
      put(44, TEXT_CODE);
      put(45, UNSPECIFIED);
      put(46, TACTILE_MATERIAL);
      put(47, KIT_CODE);
      put(48, NOTATED_MUSIC);
    }
  };
  public final static List<Integer> SOURCES_ENABLED_TO_ALTERNATIVE_LABELS_SEARCH = new ArrayList<>(Arrays.asList(1, 2, 4, 5, 6));
  public final static String ERROR_MANDATORY_TAG = "-1";
  public final static String ERROR_DUPLICATE_TAG = "-2";
  public final static String ERROR_EMPTY_TAG = "-3";
  public final static String NO_RECORD_FOUND = "-4";
  public final static Map<String, String> ERRORS_MAP = new HashMap<String, String>() {
    {
      put(ERROR_MANDATORY_TAG, "Check mandatory tags failure.");
      put(ERROR_DUPLICATE_TAG, "Duplicate tags for : %s");
      put(ERROR_EMPTY_TAG, "Some tags appears empties: %s.");
      put(NO_RECORD_FOUND, "Record not found: %d.");
    }
  };
  public final static String LOADING_FILE_FILENAME = "filename";
  public final static String LOADING_FILE_IDS = "ids";
  public final static String LOADING_FILE_REJECTED = "rejected";
  public final static String LOADING_FILE_ADDED = "added";
  public final static String LOADING_FILE_ERRORS = "errors";
  public final static char BIBLIOGRAPHIC_INDICATOR_NOT_NUMERIC = 'S';
  public static String SUBFIELD_DELIMITER = "\u001f";
  public static String SCHEMA_CUSTOMER_KEY = "CUSTOM_KEY";
  public static String SCHEMA_SUITE_KEY = "SUITE_KEY";
  public static Configuration HCONFIGURATION = new Configuration();
  public static ThreadLocal<SimpleDateFormat> FORMATTERS = new ThreadLocal() {
    @Override
    protected SimpleDateFormat initialValue() {
      final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
      formatter.setLenient(false);
      return formatter;
    }
  };

  static {
    HCONFIGURATION.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
    HCONFIGURATION.setProperty("dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
    HCONFIGURATION.setProperty("show_sql", System.getProperty("show.sql", "false"));
    try {
      HCONFIGURATION.configure("/hibernate.cfg.xml");
    } catch (final Throwable failure) {
      throw new ExceptionInInitializerError(failure);
    }
  }


}
