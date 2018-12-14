package org.folio.marccat.business.searching;

import org.folio.marccat.dao.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for the management of a browse session
 *
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class BrowseManager {
  public static final int SORTFORM_LENGTH = 1080;
  private static Map daoMap = new HashMap();
  private static Map filterMap = new HashMap();
  private DAODescriptor dao;
  private String filter;

  public BrowseManager() {

    daoMap.put("2P0", NameDescriptorDAO.class);
    filterMap.put("2P0", "");

    daoMap.put("3P10", NameDescriptorDAO.class);
    filterMap.put("3P10", " and hdg.typeCode = 2 ");

    daoMap.put("4P10", NameDescriptorDAO.class);
    filterMap.put("4P10", " and hdg.typeCode = 3 ");

    daoMap.put("5P10", NameDescriptorDAO.class);
    filterMap.put("5P10", " and hdg.typeCode = 4 ");

    daoMap.put("7P0", TitleDescriptorDAO.class);
    filterMap.put("7P0", "");

    daoMap.put("9P0", SubjectDescriptorDAO.class);
    filterMap.put("9P0", "");

    daoMap.put("230P", PublisherNameDescriptorDAO.class);
    filterMap.put("230P", "");

    daoMap.put("243P", PublisherPlaceDescriptorDAO.class);
    filterMap.put("243P", "");

    daoMap.put("250S", NameTitleNameDescriptorDAO.class);
    filterMap.put("250S", "");

    daoMap.put("251S", NameTitleTitleDescriptorDAO.class);
    filterMap.put("251S", "");

    daoMap.put("16P30", ControlNumberDescriptorDAO.class);
    filterMap.put("16P30", "");

    daoMap.put("18P2", ControlNumberDescriptorDAO.class);
    filterMap.put("18P2", " and hdg.typeCode = 9 ");

    daoMap.put("19P2", ControlNumberDescriptorDAO.class);
    filterMap.put("19P2", " and hdg.typeCode = 10 ");

    daoMap.put("20P3", ControlNumberDescriptorDAO.class);
    filterMap.put("20P3", " and hdg.typeCode = 93 ");

    daoMap.put("21P2", ControlNumberDescriptorDAO.class);
    filterMap.put("21P2", " and hdg.typeCode = 2 ");

    daoMap.put("22P10", ControlNumberDescriptorDAO.class);
    filterMap.put("22P10", " and hdg.typeCode = 93 ");

    daoMap.put("29P20", ControlNumberDescriptorDAO.class);
    filterMap.put("29P20", " and hdg.typeCode = 71 ");

    daoMap.put("30P4", ControlNumberDescriptorDAO.class);
    filterMap.put("30P4", "");

    daoMap.put("31P3", ControlNumberDescriptorDAO.class);
    filterMap.put("31P3", " and hdg.typeCode = 84 ");

    daoMap.put("32P3", ControlNumberDescriptorDAO.class);
    filterMap.put("32P3", " and hdg.typeCode = 88 ");

    daoMap.put("33P3", ControlNumberDescriptorDAO.class);
    filterMap.put("33P3", " and hdg.typeCode = 90 ");

    daoMap.put("34P20", ControlNumberDescriptorDAO.class);
    filterMap.put("34P20", "");

    daoMap.put("35P20", ControlNumberDescriptorDAO.class);
    filterMap.put("35P20", "");

    daoMap.put("36P20", ControlNumberDescriptorDAO.class);
    filterMap.put("36P20", " and hdg.typeCode = 52 ");

    daoMap.put("51P3", ControlNumberDescriptorDAO.class);
    filterMap.put("51P3", " and hdg.typeCode = 89 ");

    daoMap.put("52P3", ControlNumberDescriptorDAO.class);
    filterMap.put("52P3", " and hdg.typeCode = 83 ");

    daoMap.put("53P3", ControlNumberDescriptorDAO.class);
    filterMap.put("53P3", " and hdg.typeCode = 91 ");

    daoMap.put("54P3", ControlNumberDescriptorDAO.class);
    filterMap.put("54P3", " and hdg.typeCode = 97 ");

    daoMap.put("55P3", ControlNumberDescriptorDAO.class);
    filterMap.put("55P3", " and hdg.typeCode = 98 ");


    daoMap.put("47P40", ClassificationDescriptorDAO.class);
    filterMap.put("47P40", " and hdg.typeCode = 21");

    daoMap.put("24P5", ClassificationDescriptorDAO.class);
    filterMap.put("24P5", " and hdg.typeCode = 12");

    daoMap.put("25P5", ClassificationDescriptorDAO.class);
    filterMap.put("25P5", " and hdg.typeCode = 1");

    daoMap.put("27P5", ClassificationDescriptorDAO.class);
    filterMap.put("27P5", " and hdg.typeCode = 6");

    daoMap.put("23P5", ClassificationDescriptorDAO.class);
    filterMap.put("23P5", " and hdg.typeCode not in (1,6,10,11,12,14,15,29) ");


    daoMap.put("48P3", ClassificationDescriptorDAO.class);
    filterMap.put("48P3", " and hdg.typeCode = 10");

    daoMap.put("46P40", ClassificationDescriptorDAO.class);
    filterMap.put("46P40", " and hdg.typeCode = 11");

    daoMap.put("50P3", ClassificationDescriptorDAO.class);
    filterMap.put("50P3", " and hdg.typeCode = 14");

    daoMap.put("49P3", ClassificationDescriptorDAO.class);
    filterMap.put("49P3", " and hdg.typeCode = 15");

    daoMap.put("326P1", ClassificationDescriptorDAO.class);
    filterMap.put("326P1", " and hdg.typeCode = 29");

    daoMap.put("28P30", ShelfListDAO.class);
    filterMap.put("28P30", " and hdg.typeCode = '@'");

    daoMap.put("244P30", ShelfListDAO.class);
    filterMap.put("244P30", " and hdg.typeCode = 'N'");

    daoMap.put("47P30", ShelfListDAO.class);
    filterMap.put("47P30", " and hdg.typeCode = 'M'");

    daoMap.put("37P30", ShelfListDAO.class);
    filterMap.put("37P30", " and hdg.typeCode = '2'");

    daoMap.put("38P30", ShelfListDAO.class);
    filterMap.put("38P30", " and hdg.typeCode = '3'");

    daoMap.put("39P30", ShelfListDAO.class);
    filterMap.put("39P30", " and hdg.typeCode = '4'");

    daoMap.put("41P30", ShelfListDAO.class);
    filterMap.put("41P30", " and hdg.typeCode = '6'");

    daoMap.put("42P30", ShelfListDAO.class);
    filterMap.put("42P30", " and hdg.typeCode = 'A'");

    daoMap.put("43P30", ShelfListDAO.class);
    filterMap.put("43P30", " and hdg.typeCode = 'C'");

    daoMap.put("44P30", ShelfListDAO.class);
    filterMap.put("44P30", " and hdg.typeCode = 'E'");

    daoMap.put("45P30", ShelfListDAO.class);
    filterMap.put("45P30", " and hdg.typeCode = 'F'");

    daoMap.put("46P30", ShelfListDAO.class);
    filterMap.put("46P30", " and hdg.typeCode = 'G'");

    daoMap.put("303P3", ClassificationDescriptorDAO.class);
    filterMap.put("303P3", " and hdg.typeCode = 13");

    daoMap.put("354P0", DAOThesaurusDescriptor.class);
    filterMap.put("354P0", "");

    daoMap.put("353P1", ClassificationDescriptorDAO.class);
    filterMap.put("353P1", " and hdg.typeCode = 80");


    daoMap.put("373P0", SubjectDescriptorDAO.class);
    filterMap.put("373P0", " and hdg.sourceCode = 4 ");
  }


  public DAODescriptor getDao() {
    return dao;
  }

  public void setDao(DAODescriptor descriptor) {
    dao = descriptor;
  }


  public String getFilter() {
    return filter;
  }

  public void setFilter(String string) {
    filter = string;
  }

}
