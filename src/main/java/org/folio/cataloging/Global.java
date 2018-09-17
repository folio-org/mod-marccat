package org.folio.cataloging;

import net.sf.hibernate.cfg.Configuration;
import org.folio.cataloging.business.codetable.Avp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Global constants.
 * With "Global" we mean a constant that 
 * 
 * <ul>
 * 		<li>is supposed to be shared at least between 2 modules.</li>
 * 		<li>needs to be used within this "shared" module</li>
 * </ul>
 *
 * @author paulm
 * @author agazzarini
 * @author natasciab
 * @since 1.0
 */
// TODO: clean up (lot of unused fields)
public abstract class Global  {
    public static final String OKAPI_TENANT_HEADER_NAME = "x-okapi-tenant";
    public static final String EMPTY_STRING = "";
    public static String SUBFIELD_DELIMITER = "\u001f";
	public static String SCHEMA_CUSTOMER_KEY = "CUSTOM_KEY";
	public static String SCHEMA_SUITE_KEY = "SUITE_KEY";

	public static final String CONFIGURATION_CLIENT = "configuration.client";

	public static Configuration HCONFIGURATION = new Configuration();
	static
	{
		HCONFIGURATION.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
		HCONFIGURATION.setProperty("dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
		HCONFIGURATION.setProperty("show_sql", System.getProperty("show.sql", "false"));
		try {
			HCONFIGURATION.configure("/hibernate.cfg.xml");
		} catch (final Throwable failure) {
			throw new ExceptionInInitializerError(failure);
		}
	}

	public static ThreadLocal<SimpleDateFormat> FORMATTERS = new ThreadLocal() {
		@Override
		protected SimpleDateFormat initialValue() {
			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			formatter.setLenient(false);
			return formatter;
		}
	};

	public final static String NOT_APPLICABLE = "NA";
	public final static String COMMA_DELIMITER = ",";
	public final static String SEMICOLON_DELIMITER = ";";

	public final static String DEFAULT_LEVEL_CARD = "L01";

	public final static String DEFAULT_WORKING_CODE = "999";
	public final static String DEFAULT_MANAGERIAL_LEVEL = "L09";
	public final static String DEFAULT_LIST_TYPE = "01";

	public final static List<String> LEVEL_CARD_DB =  new ArrayList<>(Arrays.asList("L05","L06"));
	public final static List<String> LEVEL_CARD_CURRENT =  new ArrayList<>(Arrays.asList("L01","L02","L03"));

	public final static List<String> LEVEL_CARD_DB_1 =  new ArrayList<>(Arrays.asList("L08"));
	public final static List<String> LEVEL_CARD_CURRENT_1 =  new ArrayList<>(Arrays.asList("L01","L02","L03","L05","L06", "L09"));

	public final static List<String> LEVEL_CARD_DB_2 =  new ArrayList<>(Arrays.asList("L03"));
	public final static List<String> LEVEL_CARD_CURRENT_2 =  new ArrayList<>(Arrays.asList("L01","L02"));

	public final static Map<String,String> LEVEL_CARD_AND_ENCODING_LEVEL = new HashMap<String, String>(){
		{
			put("L01","3"); put("L02","3"); put("L03","5"); put("L04","5"); put("L05","7");
			put("L06","7"); put("L07"," "); put("L08","4"); put("L09","7");
		}
	};

	public final static List<String> WORKING_CODE_DB_1 =  new ArrayList<>(Arrays.asList("000","001","004"));
	public final static List<String> WORKING_CODE_CURRENT_1 =  new ArrayList<>(Arrays.asList("002","003","005","006","007","008","009","999"));
	public final static List<String> WORKING_CODE_DB_2 =  new ArrayList<>(Arrays.asList("005"));
	public final static List<String> WORKING_CODE_CURRENT_2 =  new ArrayList<>(Arrays.asList("007","008","009"));

	public final static String FRENCH = "fre";
	public final static String DEUTSCH = "ger";
	public final static String SPANISH = "spa";
	public final static String PORTOGUESE = "por";
	public final static String CATALANO 	 = "cat";
	public final static String GREEK = "gre";
	public final static String HUNGARIAN = "hun";
	public final static String CZECH = "cze";
	public final static String POLISH = "pol";
	public final static String SERBIAN = "scr";

	public final static String CHECK_DIGITS = "0123456789X0";
	public final static String SPACE = " ";
	public final static String OPEN_PARENTHESIS = "(";
	public final static String CLOSE_PARENTHESIS = ")";
	public final static String BN_INDEX = "BN";
	public final static String SPACE_OR_BN_INDEX_SPACE = " OR BN ";

	public final static String TAG095_NO_LEVEL       = "0000";
	public final static String TAG095_MOTHER_LEVEL   = "M001";
	public final static String TAG095_DAUGHTER_LEVEL = "F002";
	public final static String CLASSIFICATION_INDEX  = "OC";

	public final static List<String> REMAINING_SUBFIELDS_FOR_991 =  new ArrayList<>(Arrays.asList("b","c","d"));
	public final static String SUBFIELDS_FOR_SORTING =  "abcdef";

	public final static DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols();
	public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    public final static DecimalFormat DECIMAL_FORMAT_AN = new DecimalFormat("000000000000");

	public final String INDEX_FOR_BROWSE = "'OC', 'NN', 'ZC', 'DC', 'LC', 'MC', 'LX', 'BN', 'SN', 'NA', 'PU', 'NM', 'NC', 'NP', 'NTN', 'NTT', 'SU', 'TI', 'SC'";

	public final static int MAX_LENGHT_FOR_TAG045_SUBFIELD_A = 192;
	public final static int MAX_LENGHT_FOR_RULE_YEARS_LIST = 4000;

	//public final static String DEFAULT_CUSTOMER_COLLECTION = Defaults.getString("default.customer.collection", "");

	public final static String DEFAULT_TYPOLOGY_FOR_CUSTOMER_COLLECTION = "PDC";
	public final static String DEFAULT_TYPOLOGY_FOR_MASTER_COLLECTION = "PDM";

	public final static String SUBFIELDS_FOR_TAG_490 = "apvx368";

	public final static String SUBFIELD_DELIMITER_FOR_VIEW = "\\$";
	public final static List<String> STAMPER_SUBFIELDS =  new ArrayList<String>(Arrays.asList("3","6","8","e","f","g"));

	public final static List<String> WEMI_CODE_LIST_FOR_ACCESS_POINT =  new ArrayList<>(Arrays.asList("3","4","5"));
	public final static String RELATOR_TERM_$e = "e";
	public final static String INTRODUCTION_$i = "i";

	public final static List<String> FIXED_FIELDS = new ArrayList<>(Arrays.asList("000","001","005","006","007","008"));
	public final static List<String> MANDATORY_FIELDS = new ArrayList<>(Arrays.asList("000","001","008","040"));
	public final static String HEADER_TYPE_LABEL = "HEADER_TYPE";
	public final static String FORM_OF_MATERIAL_LABEL = "FORM_OF_MATERIAL";
    public final static String MATERIAL_TYPE_CODE_LABEL = "MATERIAL_TYPE_CODE";

    public final static String LEADER_TAG_NUMBER = "000";
	public final static String CONTROL_NUMBER_TAG_CODE = "001";
	public final static String CATALOGING_SOURCE_TAG_CODE = "040";
	public final static String DATETIME_TRANSACION_TAG_CODE = "005";
	public final static String MATERIAL_TAG_CODE = "008";
	public final static String OTHER_MATERIAL_TAG_CODE = "006";
	public final static String PHYSICAL_DESCRIPTION_TAG_CODE = "007";

	public final static Map<String,Integer> HEADING_TYPE_MAP = new HashMap<String, Integer>()
	{
		{
			put("2",1); put("17",1);
			put("4",3); put("18",3);
			put("3",4); put("22",4);
			put("6",6); put("20",6);
			put("11",8);
		}
	};

	public final static Avp<String> URI_SOURCE_LOCAL = new Avp<>("0","Local");
	public final static String URI_LOCAL_HTTP = "http://";

	public final static Map<String,String> HEADING_DESCRIPTION_TEXT_MAP = new HashMap<String, String>(){
		{
			put("1","names");
			put("3","subjects");
			put("4","titles");
			put("6","classifications");
			put("8","nameTitles");
		}
	};

	public final static int INT_CATEGORY = 1;
	public final static int NAME_CATEGORY_DEFAULT = 2;
	public final static short CORRELATION_UNDEFINED = -1;

	public final static int CATALOGING_SOURCE_HEADER_TYPE = 1;
	public final static int LEADER_HEADER_TYPE = 15;
    public final static int CONTROL_NUMBER_HEADER_TYPE = 39;
	public final static int DATETIME_TRANSACION_HEADER_TYPE = 41;
    public final static int MATERIAL_DESCRIPTION_HEADER_TYPE = 31;
	public final static int PHYSICAL_UNSPECIFIED_HEADER_TYPE = 45;

	public final static int LEADER_LENGTH = 24;
	public final static int MATERIAL_FIELD_LENGTH = 40;
	public final static int OTHER_MATERIAL_FIELD_LENGTH = 18;


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
    public final static String ITEM_DATE_FIRST_PUBLICATION = "    ";
    public final static String ITEM_DATE_LAST_PUBLICATION = "    ";
	public final static String LANGUAGE_CODE = "   ";

	//default values for material description (tag 008)
	// book type
	public final static String BOOKFORM_OF_MATERIAL = "bk";
    public final static char MATERIAL_TYPE_CODE = 'a';
	public final static char FORM_OF_ITEM_CODE = ' ';

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

	public final static Map<Integer, String> PHYSICAL_TYPES_MAP = new HashMap<Integer, String>(){
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

	public final static List<Integer> SOURCES_ENABLED_TO_ALTERNATIVE_LABELS_SEARCH =  new ArrayList<>(Arrays.asList(1,2,4,5,6));

	public final static String AN_KEY_CODE_FIELD = "BI";

	public final static String ERROR_MANDATORY_TAG = "-1";
	public final static String ERROR_DUPLICATE_TAG = "-2";
	public final static String ERROR_EMPTY_TAG = "-3";
	public final static Map<String, String> ERRORS_MAP = new HashMap<String, String>(){
		{
			put(ERROR_MANDATORY_TAG, "Check mandatory tags failure.");
			put(ERROR_DUPLICATE_TAG, "Duplicate tags for : %s");
			put(ERROR_EMPTY_TAG, "Some tags appears empties: %s.");
		}
	};

}