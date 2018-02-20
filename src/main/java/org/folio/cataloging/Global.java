package org.folio.cataloging;

import net.sf.hibernate.cfg.Configuration;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.dao.persistence.*;

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
 * @since 1.0
 */
// TODO: clean up (lot of unused fields)
public abstract class Global  {
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

	public final static String COLLECTION_DIGITAL_STANDARD = "70";
	public final static String WORKING_CODE_FOR_APPROVAL = "000,005,009,";
	public final static String AVAILABILITY_STATUS_CODE_FOR_APPROVAL = "0,9,";
	public final static String NOT_APPLICABLE = "NA";
	public final static String COMMA_DELIMITER = ",";
	public final static String SEMICOLON_DELIMITER = ";";

	public final static String START_DOI_FOR_TAG856_42 = "http://digital.casalini.it/";
	public final static String START_DOI_FOR_TAG856_42_FROM_TAG024 = "http://dx.medra.org/";
	public final static String END_DOI_FOR_TAG856_42_FROM_TAG024 = " $xNBN";
	public final static String DOI_TEXT = "DOI";

	public final static String DEFAULT_LEVEL_CARD = "L01";

	public final static String DEFAULT_WORKING_CODE = "999";
	public final static String DEFAULT_MANAGERIAL_LEVEL = "L09";
	public final static String DEFAULT_LIST_TYPE = "01";

	public final static List<String> levelCardDb  =  new ArrayList<String>(Arrays.asList("L05","L06"));
	public final static List<String> levelCardCurrent  =  new ArrayList<String>(Arrays.asList("L01","L02","L03"));

	public final static List<String> levelCardDb1 =  new ArrayList<String>(Arrays.asList("L08"));
	public final static List<String> levelCardCurrent1  =  new ArrayList<String>(Arrays.asList("L01","L02","L03","L05","L06", "L09"));

	public final static List<String> levelCardDb2 =  new ArrayList<String>(Arrays.asList("L03"));
	public final static List<String> levelCardCurrent2  =  new ArrayList<String>(Arrays.asList("L01","L02"));

	public final static Map<String,String> levelCardAndEncodingLevel = new HashMap<String, String>(){
		{
			put("L01","3"); put("L02","3"); put("L03","5"); put("L04","5"); put("L05","7");
			put("L06","7"); put("L07"," "); put("L08","4"); put("L09","7");
		}
	};

	public final static List<String> workingCodeDb1 =  new ArrayList<String>(Arrays.asList("000","001","004"));
	public final static List<String> workingCodeCurrent1  =  new ArrayList<String>(Arrays.asList("002","003","005","006","007","008","009","999"));
	public final static List<String> workingCodeDb2  =  new ArrayList<String>(Arrays.asList("005"));
	public final static List<String> workingCodeCurrent2  =  new ArrayList<String>(Arrays.asList("007","008","009"));

	public final static String FRANCESE	 = "fre";
	public final static String TEDESCO 	 = "ger";
	public final static String SPAGNOLO 	 = "spa";
	public final static String PORTOGHESE   = "por";
	public final static String CATALANO 	 = "cat";
	public final static String GRECO 		 = "gre";
	public final static String UNGHERESE    = "hun";
	public final static String CECO 	 	 = "cze";
	public final static String POLACCO 	 = "pol";
	public final static String SERBO_CROATO = "scr";

	public final static String CheckDigits = new String("0123456789X0");
	public final static String SPACE = " ";
	public final static String OPEN_PARENTHESIS = "(";
	public final static String CLOSE_PARENTHESIS = ")";
	public final static String BN_INDEX = "BN";
	public final static String SPACE_OR_BN_INDEX_SPACE = " OR BN ";

	public final static String TAG095_NO_LEVEL       = "0000";
	public final static String TAG095_MOTHER_LEVEL   = "M001";
	public final static String TAG095_DAUGHTER_LEVEL = "F002";
	public final static String CLASSIFICATION_INDEX  = "OC";

	public final static List<String> remainingSubfieldsFor991  =  new ArrayList<String>(Arrays.asList("b","c","d"));
	public final static String subfieldsForSorting  =  new String("abcdef");

	public final static DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols();
	public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

	public final String INDEX_FOR_BROWSE = "'OC', 'NN', 'ZC', 'DC', 'LC', 'MC', 'LX', 'BN', 'SN', 'NA', 'PU', 'NM', 'NC', 'NP', 'NTN', 'NTT', 'SU', 'TI', 'SC'";

	public final static int MAX_LENGHT_FOR_TAG045_SUBFIELD_A = 192;
	public final static int MAX_LENGHT_FOR_RULE_YEARS_LIST = 4000;

	//public final static String DEFAULT_CUSTOMER_COLLECTION = Defaults.getString("default.customer.collection", "");

	public final static String DEFAULT_TYPOLOGY_FOR_CUSTOMER_COLLECTION = "PDC";
	public final static String DEFAULT_TYPOLOGY_FOR_MASTER_COLLECTION = "PDM";

	public final static String subfieldsForTag490 = new String("apvx368");

	public final static String SUBFIELD_DELIMITER_FOR_VIEW = "\\$";
	public final static List<String> stamperSubfields =  new ArrayList<String>(Arrays.asList("3","6","8","e","f","g"));

	public final static List<String> WEMI_CODE_LIST_FOR_ACCESS_POINT =  new ArrayList<String>(Arrays.asList("3","4","5"));
	public final static String RELATOR_TERM_$e = "e";
	public final static String INTRODUCTION_$i = "i";

	//TODO: move to configuration module
	public final static Map<String,Integer> headingTypeMap = new HashMap<String, Integer>()
	{
		{
			put("2",1); put("17",1);
			put("4",3); put("18",3);
			put("3",4); put("22",4);
			put("6",6); put("20",6);
			put("11",8);
		}
	};

	public final static Avp URI_SOURCE_LOCAL = new Avp("0","Local");
	public final static String URI_LOCAL_HTTP = "http://";

	public final static Map<String,String> headingDescriptionTextMap = new HashMap<String, String>(){
		{
			put("1","names");
			put("3","subjects");
			put("4","titles");
			put("6","classifications");
			put("8","nameTitles");
		}
	};

	public final static Map<String, Class> thirdCorrelationHeadingClassMap = new HashMap<String, Class>(){
		{
			put("2", NameFunction.class);
			put("4", SubjectSource.class);
			put("11", NameSubType.class);
		}
	};

	public final static Map<String, Class> secondCorrelationClassMap = new HashMap<String, Class>(){
		{
			put("2", NameSubType.class);
			put("3", TitleSecondaryFunction.class);
			put("4", SubjectFunction.class);
			put("5", ControlNumberFunction.class);
			put("6", ClassificationFunction.class);
			put("11", NameType.class);
		}
	};

	public final static Map<String, Class> firstCorrelationHeadingClassMap = new HashMap<String, Class>(){
		{
			put("2", NameType.class);
			put("17", NameType.class); //from heading
			put("3", TitleFunction.class);
			put("4", SubjectType.class);
			put("18", SubjectType.class); //from heading
			put("5", ControlNumberType.class);
			put("19", ControlNumberType.class); //from heading
			put("6", ClassificationType.class);
			put("20", ClassificationType.class); //from heading
			put("7", BibliographicNoteType.class); //note
			put("8", BibliographicRelationType.class);//relationship
			put("11", T_NME_TTL_FNCTN.class); //nt
		}
	};

	public final static String NAME_CATEGORY_DEFAULT = "2";

	public final static List<Integer> sourcesEnabledToAlternativeLabelsSearch  =  new ArrayList<Integer>(Arrays.asList(1,2,4,5,6));
}