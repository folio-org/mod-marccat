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
  public static String SUBFIELD_DELIMITER = "\u001f";
  public static Configuration HCONFIGURATION = new Configuration();
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

  //public final static String DEFAULT_CUSTOMER_COLLECTION = Defaults.getString("default.customer.collection", "");
  public final static String INTRODUCTION_$i = "i";
  public final static List <String> FIXED_FIELDS = new ArrayList <>(Arrays.asList("000", "001", "005", "006", "007", "008"));
  public final static List <String> MANDATORY_FIELDS = new ArrayList <>(Arrays.asList("000", "001", "008", "040"));
  public final static String HEADER_TYPE_LABEL = "HEADER_TYPE";
  public final static String FORM_OF_MATERIAL_LABEL = "FORM_OF_MATERIAL";
  public final static String MATERIAL_TYPE_CODE_LABEL = "MATERIAL_TYPE_CODE";
  public final static String LEADER_TAG_NUMBER = "000";
  public final static String CONTROL_NUMBER_TAG_CODE = "001";
  public final static String CATALOGING_SOURCE_TAG_CODE = "040";
  public final static String DATETIME_TRANSACTION_TAG_CODE = "005";
  public final static String OTHER_MATERIAL_TAG_CODE = "006";
  public final static String PHYSICAL_DESCRIPTION_TAG_CODE = "007";
  public final static int MATERIAL_FIELD_LENGTH = 40;
  public final static int OTHER_MATERIAL_FIELD_LENGTH = 18;
  
  public final static String ITEM_DATE_FIRST_PUBLICATION = "    ";
  public final static String ITEM_DATE_LAST_PUBLICATION = "    ";
  public final static String MATERIAL_TAG_CODE = "008";
  public final static int INT_CATEGORY = 1;
  public final static int PHYSICAL_UNSPECIFIED_HEADER_TYPE = 45;
  public final static int LEADER_LENGTH = 24;
//default values for leader
  public final static String FIXED_LEADER_LENGTH = "00000";
  public final static char RECORD_STATUS_CODE = 'n';
  public final static char RECORD_TYPE_CODE = 'a';
  public final static char BIBLIOGRAPHIC_LEVEL_CODE = 'm';
  public final static char CONTROL_TYPE_CODE = ' ';
  public final static char CHARACTER_CODING_SCHEME_CODE = ' ';
  public final static String FIXED_LEADER_BASE_ADDRESS = "2200000";
  public final static char ENCODING_LEVEL = ' ';
  public final static char DESCRIPTIVE_CATALOGUING_CODE = ' ';
  public final static char LINKED_RECORD_CODE = ' ';
  public final static String FIXED_LEADER_PORTION = "4500";
  //bibliographic
  public final static String LANGUAGE_CODE = "   ";

  
  public final static Map <Integer, String> PHYSICAL_TYPES_MAP = new HashMap <Integer, String>() {
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
