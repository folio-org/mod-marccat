package org.folio.cataloging;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.business.common.Defaults;

public interface IGlobalConst
{
	final static String COLLECTION_DIGITAL_STANDARD = "70"; 
	final static String WORKING_CODE_FOR_APPROVAL = "000,005,009,";
	final static String AVAILABILITY_STATUS_CODE_FOR_APPROVAL = "0,9,";
	final boolean approvalEnabled =  Defaults.getBoolean("customer.casalini.approval", false);
	final static String NOT_APPLICABLE = "NA";
	final static String COMMA_DELIMITER = ",";
	final static String SEMICOLON_DELIMITER = ";";
	
	final static String START_DOI_FOR_TAG856_42 = "http://digital.casalini.it/";
	final static String START_DOI_FOR_TAG856_42_FROM_TAG024 = "http://dx.medra.org/";
	final static String END_DOI_FOR_TAG856_42_FROM_TAG024 = " $xNBN";
	final static String DOI_TEXT = "DOI";
	
	final static String DEFAULT_LEVEL_CARD = "L01";
	
	final static String DEFAULT_WORKING_CODE = "999";
	final static String DEFAULT_MANAGERIAL_LEVEL = "L09";
	final static String DEFAULT_LIST_TYPE = "01";

	/* Range per controllo Livello gestionale DLA-DGA */
	final static List<String> levelCardDb  =  new ArrayList<String>(Arrays.asList("L05","L06"));
	final static List<String> levelCardCurrent  =  new ArrayList<String>(Arrays.asList("L01","L02","L03"));
	
	final static List<String> levelCardDb1 =  new ArrayList<String>(Arrays.asList("L08"));
	final static List<String> levelCardCurrent1  =  new ArrayList<String>(Arrays.asList("L01","L02","L03","L05","L06", "L09"));
	
	final static List<String> levelCardDb2 =  new ArrayList<String>(Arrays.asList("L03"));
	final static List<String> levelCardCurrent2  =  new ArrayList<String>(Arrays.asList("L01","L02"));

	final static Map<String,String> levelCardAndEncodingLevel = new HashMap<String, String>(){
		{
	       put("L01","3"); put("L02","3"); put("L03","5"); put("L04","5"); put("L05","7"); 
	       put("L06","7"); put("L07"," "); put("L08","4"); put("L09","7");
	    }
	};
	
	/* Range per controllo Codice lavorazione DLA-DGA */
	final static List<String> workingCodeDb1 =  new ArrayList<String>(Arrays.asList("000","001","004"));
	final static List<String> workingCodeCurrent1  =  new ArrayList<String>(Arrays.asList("002","003","005","006","007","008","009","999"));
	final static List<String> workingCodeDb2  =  new ArrayList<String>(Arrays.asList("005"));
	final static List<String> workingCodeCurrent2  =  new ArrayList<String>(Arrays.asList("007","008","009"));
	
	/* Codici lingue gestite nella funzione "Diacritici" */
	final static String FRANCESE	 = "fre"; 
	final static String TEDESCO 	 = "ger";
	final static String SPAGNOLO 	 = "spa";
	final static String PORTOGHESE   = "por";
	final static String CATALANO 	 = "cat";
	final static String GRECO 		 = "gre";
	final static String UNGHERESE    = "hun";
	final static String CECO 	 	 = "cze";
	final static String POLACCO 	 = "pol";
	final static String SERBO_CROATO = "scr";
	
	/* Campi per conversione isbn 10/13 caratteri */
	final static String CheckDigits = new String("0123456789X0");
	final static String SPACE = " ";
	final static String OPEN_PARENTHESIS = "(";
	final static String CLOSE_PARENTHESIS = ")";
	final static String BN_INDEX = "BN";
	final static String SPACE_OR_BN_INDEX_SPACE = " OR BN ";
	
	/* Campi per tag095 */
	final static String TAG095_NO_LEVEL       = "0000";
	final static String TAG095_MOTHER_LEVEL   = "M001";
	final static String TAG095_DAUGHTER_LEVEL = "F002";
	final static String CLASSIFICATION_INDEX  = "OC";
	
	/* Sottocampi aggiungibili tag991 */
	final static List<String> remainingSubfieldsFor991  =  new ArrayList<String>(Arrays.asList("b","c","d"));
	/* Ordinamento sottocampi tag991 */
	final static String subfieldsForSorting  =  new String("abcdef");
	
	final static DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols();
	final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
	
	/* Bug 3937 */
	final String INDEX_FOR_BROWSE = "'OC', 'NN', 'ZC', 'DC', 'LC', 'MC', 'LX', 'BN', 'SN', 'NA', 'PU', 'NM', 'NC', 'NP', 'NTN', 'NTT', 'SU', 'TI', 'SC'";
	
	final static int MAX_LENGHT_FOR_TAG045_SUBFIELD_A = 192;
	final static int MAX_LENGHT_FOR_RULE_YEARS_LIST = 4000;

	/* Bug 4099 COLLECTIONS - Anagrafica collection Custom create da Master: Descrizione Cliente */
	final static String DEFAULT_CUSTOMER_COLLECTION = Defaults.getString("default.customer.collection", "");
	
	/* Bug 4097	 COLLECTIONS - Anagrafica collection: DEFAULT PER CAMPO TIPOLOGIA ---> Tabella T_CLCTN_TYP */
	final static String DEFAULT_TYPOLOGY_FOR_CUSTOMER_COLLECTION = "PDC";
	final static String DEFAULT_TYPOLOGY_FOR_MASTER_COLLECTION = "PDM";
	
	/* Bug 4370 */
	final static String subfieldsForTag490 = new String("apvx368");
	
	final static String SUBFIELD_DELIMITER_FOR_VIEW = "\\$";
	final static String SUBFIELD_DELIMITER = "\u001f";
	final static List<String> stamperSubfields =  new ArrayList<String>(Arrays.asList("3","6","8","e","f","g"));

	final static List<String> WEMI_CODE_LIST_FOR_ACCESS_POINT =  new ArrayList<String>(Arrays.asList("3","4","5"));
	final static String RELATOR_TERM_$e = "e";
	final static String INTRODUCTION_$i = "i";

	final static Map<String,Integer> headingTypeMap = new HashMap<String, Integer>(){
		{
			/* Corrispondenza tra categoria del descrittore (key) della heading e il tipo di heading della tabella AMICUS.T_HDG_DTE (value) */
			put("2",1); put("17",1);	/* NME_HDG     */
//			put("7",2); put("21",2);	/* PUBL_HDG    */
			put("4",3); put("18",3);	/* SBJCT_HDG   */
			put("3",4); put("22",4);	/* TTL_HDG     */
//			put("5",5); put("19",5);	/* CNTL_NBR    */
			put("6",6); put("20",6); 	/* CLSTN       */
//			put("23",7);				/* THS_HDG     */	       
			put("11",8); 				/* NME_TTL_HDG */
	    }
	};
	
	final static DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	final static ValueLabelElement URI_SOURCE_LOCAL = new ValueLabelElement("0","Local");
	final static String DEFAULT_URI_CLIENT = Defaults.getString("uri.client", "N/A client");
	
	final static String URI_LOCAL_HTTP = "http://";
	
	final static Map<String,String> headingDescriptionTextMap = new HashMap<String, String>(){
		{
			put("1","names"); 			/* NME_HDG     */
//			put("2","Publishers");		/* PUBL_HDG    */
			put("3","subjects");	    /* SBJCT_HDG   */
			put("4","titles");			/* TTL_HDG     */
//			put("5","ControlNumbers");	/* CNTL_NBR    */
			put("6","classifications");	/* CLSTN       */
//			put("7","Thesaurus");		/* THS_HDG     */
			put("8","nameTitles");		/* NME_TTL_HDG */
	    }
	};
	
	/* Fonti LC_NAF, LCSH, ISNI, VIAF, FAST abilitate alla ricerca delle forme varianti */ 
	final static List<Integer> sourcesEnabledToAlternativeLabelsSearch  =  new ArrayList<Integer>(Arrays.asList(1,2,4,5,6));
	
	final static String RDF_SERVICE = Defaults.getString("url.rdf.service");
	
	final static int ROW_FOR_PAGE = 9;
}