package org.folio.marccat.config;

import java.util.HashMap;
import java.util.Map;

import org.folio.marccat.dao.ClassificationDescriptorDAO;
import org.folio.marccat.dao.ControlNumberDescriptorDAO;
import org.folio.marccat.dao.NameDescriptorDAO;
import org.folio.marccat.dao.NameTitleNameDescriptorDAO;
import org.folio.marccat.dao.NameTitleTitleDescriptorDAO;
import org.folio.marccat.dao.PublisherNameDescriptorDAO;
import org.folio.marccat.dao.PublisherPlaceDescriptorDAO;
import org.folio.marccat.dao.ShelfListDAO;
import org.folio.marccat.dao.SubjectDescriptorDAO;
import org.folio.marccat.dao.TitleDescriptorDAO;
import org.folio.marccat.dao.persistence.NameAccessPoint;
import org.folio.marccat.dao.persistence.NameTitleAccessPoint;
import org.folio.marccat.dao.persistence.SubjectAccessPoint;
import org.folio.marccat.dao.persistence.TitleAccessPoint;

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

  public final static Map<String, String> OPERAND_QUERY = new HashMap<String, String>() {
    {
      put("MAJOR", ">");
      put("MINOR", "<");
      put("GRATER_THAN", ">=");
      put("LESS_THEN", "<=");
    }
  };

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
  
  public final static Map <String, Class> DAO_CLASS_MAP = new HashMap <String, Class> ( ) {
	    {
	      put ("2P0", NameDescriptorDAO.class);
	      put ("3P10", NameDescriptorDAO.class);
	      put ("2P0", NameDescriptorDAO.class);
	      put ("3P10", NameDescriptorDAO.class);
	      put ("4P10", NameDescriptorDAO.class);
	      put ("5P10", NameDescriptorDAO.class);
	      put ("7P0", TitleDescriptorDAO.class);
	      put ("9P0", SubjectDescriptorDAO.class);
	      put ("230P", PublisherNameDescriptorDAO.class);
	      put ("243P", PublisherPlaceDescriptorDAO.class);
	      put ("250S", NameTitleNameDescriptorDAO.class);
	      put ("251S", NameTitleTitleDescriptorDAO.class);
	      put ("16P30", ControlNumberDescriptorDAO.class);
	      put ("18P2", ControlNumberDescriptorDAO.class);
	      put ("19P2", ControlNumberDescriptorDAO.class);
	      put ("20P3", ControlNumberDescriptorDAO.class);
	      put ("21P2", ControlNumberDescriptorDAO.class);
	      put ("22P10", ControlNumberDescriptorDAO.class);
	      put ("29P20", ControlNumberDescriptorDAO.class);
	      put ("30P4", ControlNumberDescriptorDAO.class);
	      put ("31P3", ControlNumberDescriptorDAO.class);
	      put ("32P3", ControlNumberDescriptorDAO.class);
	      put ("33P3", ControlNumberDescriptorDAO.class);
	      put ("34P20", ControlNumberDescriptorDAO.class);
	      put ("35P20", ControlNumberDescriptorDAO.class);
	      put ("36P20", ControlNumberDescriptorDAO.class);
	      put ("51P3", ControlNumberDescriptorDAO.class);
	      put ("52P3", ControlNumberDescriptorDAO.class);
	      put ("53P3", ControlNumberDescriptorDAO.class);
	      put ("54P3", ControlNumberDescriptorDAO.class);
	      put ("55P3", ControlNumberDescriptorDAO.class);
	      put ("47P40", ClassificationDescriptorDAO.class);
	      put ("24P5", ClassificationDescriptorDAO.class);
	      put ("25P5", ClassificationDescriptorDAO.class);
	      put ("27P5", ClassificationDescriptorDAO.class);
	      put ("23P5", ClassificationDescriptorDAO.class);
	      put ("48P3", ClassificationDescriptorDAO.class);
	      put ("46P40", ClassificationDescriptorDAO.class);
	      put ("50P3", ClassificationDescriptorDAO.class);
	      put ("49P3", ClassificationDescriptorDAO.class);
	      put ("326P1", ClassificationDescriptorDAO.class);
	      put ("353P1", ClassificationDescriptorDAO.class);
	      put ("303P3", ClassificationDescriptorDAO.class);
	      put ("28P30", ShelfListDAO.class);
	      put ("244P30", ShelfListDAO.class);
	      put ("47P30", ShelfListDAO.class);
	      put ("37P30", ShelfListDAO.class);
	      put ("38P30", ShelfListDAO.class);
	      put ("39P30", ShelfListDAO.class);
	      put ("41P30", ShelfListDAO.class);
	      put ("42P30", ShelfListDAO.class);
	      put ("43P30", ShelfListDAO.class);
	      put ("44P30", ShelfListDAO.class);
	      put ("45P30", ShelfListDAO.class);
	      put ("46P30", ShelfListDAO.class);
	      put ("373P0", SubjectDescriptorDAO.class);
	    }
	  };

	  public final static Map <String, String> FILTER_MAP = new HashMap <String, String> ( ) {
		    {
		      put ("2P0", "");
		      put ("3P10", " and hdg.typeCode = 2 ");
		      put ("4P10", " and hdg.typeCode = 3 ");
		      put ("5P10", " and hdg.typeCode = 4 ");
		      put ("7P0", "");
		      put ("9P0", "");
		      put ("230P", "");
		      put ("243P", "");
		      put ("250S", "");
		      put ("251S", "");
		      put ("16P30", "");
		      put ("18P2", " and hdg.typeCode = 9 ");
		      put ("19P2", " and hdg.typeCode = 10 ");
		      put ("20P3", " and hdg.typeCode = 93 ");
		      put ("21P2", " and hdg.typeCode = 2 ");
		      put ("22P10", " and hdg.typeCode = 93 ");
		      put ("29P20", " and hdg.typeCode = 71 ");
		      put ("30P4", "");
		      put ("31P3", " and hdg.typeCode = 84 ");
		      put ("32P3", " and hdg.typeCode = 88 ");
		      put ("33P3", " and hdg.typeCode = 90 ");
		      put ("34P20", "");
		      put ("35P20", "");
		      put ("36P20", " and hdg.typeCode = 52 ");
		      put ("51P3", " and hdg.typeCode = 89 ");
		      put ("52P3", " and hdg.typeCode = 83 ");
		      put ("53P3", " and hdg.typeCode = 91 ");
		      put ("54P3", " and hdg.typeCode = 97 ");
		      put ("55P3", " and hdg.typeCode = 98 ");
		      put ("47P40", " and hdg.typeCode = 21");
		      put ("24P5", " and hdg.typeCode = 12");
		      put ("25P5", " and hdg.typeCode = 1");
		      put ("27P5", " and hdg.typeCode = 6");
		      put ("23P5", " and hdg.typeCode not in (1,6,10,11,12,14,15,29) ");
		      put ("48P3", " and hdg.typeCode = 10");
		      put ("46P40", " and hdg.typeCode = 11");
		      put ("50P3", " and hdg.typeCode = 14");
		      put ("49P3", " and hdg.typeCode = 15");
		      put ("326P1", " and hdg.typeCode = 29");
		      put ("28P30", " and hdg.typeCode = '@'");
		      put ("244P30", " and hdg.typeCode = 'N'");
		      put ("47P30", " and hdg.typeCode = 'M'");
		      put ("37P30", " and hdg.typeCode = '2'");
		      put ("38P30", " and hdg.typeCode = '3'");
		      put ("39P30", " and hdg.typeCode = '4'");
		      put ("41P30", " and hdg.typeCode = '6'");
		      put ("42P30", " and hdg.typeCode = 'A'");
		      put ("43P30", " and hdg.typeCode = 'C'");
		      put ("44P30", " and hdg.typeCode = 'E'");
		      put ("45P30", " and hdg.typeCode = 'F'");
		      put ("46P30", " and hdg.typeCode = 'G'");
		      put ("303P3", " and hdg.typeCode = 13");
		      put ("354P0", "");
		      put ("353P1", " and hdg.typeCode = 80");
		      put ("373P0", " and hdg.sourceCode = 4 ");
		    }
		  };
}
