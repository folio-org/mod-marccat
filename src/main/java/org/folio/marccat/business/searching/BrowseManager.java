package org.folio.marccat.business.searching;

import net.sf.hibernate.HibernateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.common.BrowseFailedException;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.business.common.Defaults;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.*;
import org.folio.marccat.dao.persistence.Descriptor;

import java.sql.SQLException;
import java.util.*;

/**
 * Responsible for the management of a browse session
 *
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class BrowseManager {
  public static final int MAX_BROWSE_TERM_LENGTH = Defaults.getInteger("browse.max.term.lenght");
  public static final int MAX_SORTFORM_LENGTH = MAX_BROWSE_TERM_LENGTH;
  public static final int SORTFORM_LENGTH = 1080;
  private static final Log logger = LogFactory.getLog(BrowseManager.class);
  private static Map daoMap = new HashMap();
  private static Map filterMap = new HashMap();
  private int pageSize = 10;
  private String browseIndex;
  private DAODescriptor dao;
  private String filter;
  private boolean supportsCrossReferences;
  private boolean supportsAuthorities;

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

  public List getXrefCounts(List descriptors, int searchingView) throws DataAccessException, HibernateException {
    int cataloguingView = getBrowsingViewBasedOnSearchingView(searchingView);
    List result = new ArrayList();

    Iterator iter = descriptors.iterator();
    Descriptor aDescriptor = null;
    while (iter.hasNext()) {
      aDescriptor = (Descriptor) iter.next();
      result.add(new Integer(((DAODescriptor) aDescriptor.getDAO()).getXrefCount(aDescriptor, cataloguingView, null)));
    }
    return result;
  }


  public List getDocCounts(List descriptors, int searchingView)
    throws DataAccessException, HibernateException {
    int cataloguingView = getBrowsingViewBasedOnSearchingView(searchingView);
    List result = new ArrayList();

    Iterator iter = descriptors.iterator();
    Descriptor aDescriptor = null;

    while (iter.hasNext()) {
      aDescriptor = (Descriptor) iter.next();

      result.add(
        new Integer(
          ((DAODescriptor) aDescriptor.getDAO()).getDocCount(
            aDescriptor,
            cataloguingView, null)));
    }

    return result;
  }

  public List getDocCountNT(List descriptors, int searchingView) throws DataAccessException,
    HibernateException {
    int cataloguingView = getBrowsingViewBasedOnSearchingView(searchingView);
    List result = new ArrayList();
    Iterator iter = descriptors.iterator();
    Descriptor aDescriptor = null;

    while (iter.hasNext()) {
      aDescriptor = (Descriptor) iter.next();
      result.add(new Integer(((DAODescriptor) aDescriptor.getDAO()).getDocCountNT(aDescriptor, cataloguingView, null)));
    }
    return result;
  }


  public List getAuthCounts(List descriptors) throws DataAccessException, HibernateException {
    List result = new ArrayList();

    Iterator iter = descriptors.iterator();
    Descriptor aDescriptor = null;

    while (iter.hasNext()) {
      aDescriptor = (Descriptor) iter.next();
      //TODO passare la session
      result.add(
        new Integer(
          ((DAODescriptor) aDescriptor.getDAO()).getAuthCount(
            aDescriptor, null)));
    }

    return result;
  }

  /**
   * Convert the users searching view into the appropriate view for browsing
   * (for now this basically means that Authority == 1)
   *
   * @param searchingView
   * @return
   */
  public int getBrowsingViewBasedOnSearchingView(int searchingView) {
    if (searchingView == View.AUTHORITY) {
      return 1;
    } else {
      return searchingView;
    }
  }

  /**
   * Get the first page of a browse
   */

  //TODO get authority counts, etc.
  public List getFirstPage(String term, int searchingView, int termsToDisplay) throws DataAccessException, HibernateException, SQLException {
    int cataloguingView = getBrowsingViewBasedOnSearchingView(searchingView);
    DAODescriptor dao = getDao();
    setPageSize(termsToDisplay);

    // calculate the sortform of the search term
    //TODO passare la session
    String searchTerm = dao.calculateSearchTerm(term, getBrowseIndex(), null);

    // MIKE: moved Publisher block into PublisherDescriptorDAO to calculate first temp searchTerm stringValue
    List l = null;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("looking for a heading < " + searchTerm);
      }
      logger.debug("BrowseManager 1");
      l = dao.getHeadingsBySortform(
        "<",
        "desc",
        searchTerm,
        getFilter(),
        cataloguingView,
        1, null);
      logger.debug("BrowseManager 2");
      /* Natasha:  si verificava problema con con DaoPublisherName/Place
       * Azzerava la List e ripeteva ricerca sul termine precedente prendendone x e non 1 */
      if (!((dao instanceof PublisherNameDescriptorDAO) && !(dao instanceof PublisherPlaceDescriptorDAO))) {
        // MIKE: siccome e' specifica del publisher, eliminare il controllo quando
        // si ha la possibilita'� di eseguire i test di non regressione sugli stessi publisher.
        // Non ha senso eliminare questo blocco nelle altre headings e
        // soprattutto nei Nomi/Titolo nei quali il termine di ricerca
        // equivale alla sortform recuperata dalla query appena eseguita
        logger.debug("BrowseManager 3");
        if (l.size() > 0) {
          searchTerm = dao.getBrowsingSortForm((Descriptor) l.get(0));
          l.clear();
        }
        logger.debug("BrowseManager 4");
      }

      if (logger.isDebugEnabled()) {
        logger.debug("looking for headings >= " + searchTerm);
      }
      logger.debug("BrowseManager 5");
      //TODo passare la Session
      l.addAll(
        dao.getHeadingsBySortform(
          ">=",
          "",
          searchTerm,
          getFilter(),
          cataloguingView,
          getPageSize(),
          null));
      logger.debug("BrowseManager 6");
    } catch (DataAccessException e) {
      throw new BrowseFailedException();
    }
    return l;
  }

  public void setBrowseIndex(String key, int mainLibrary)
    throws InvalidBrowseIndexException {
    browseIndex = key;

    try {
      Class c = (Class) daoMap.get(key);
      if (c == null) {
        throw new InvalidBrowseIndexException(key);
      }
      setDao((DAODescriptor) c.newInstance());
    } catch (InstantiationException e) {
      throw new InvalidBrowseIndexException(key);
    } catch (IllegalAccessException e) {
      throw new InvalidBrowseIndexException(key);
    }
    setSupportsCrossReferences(getDao().supportsCrossReferences());
    setSupportsAuthorities(getDao().supportsAuthorities());
    setFilter((String) filterMap.get(key));
    if (getDao() instanceof ShelfListDAO) {
      setFilter(getFilter() + " and hdg.mainLibraryNumber = " + mainLibrary);
    }
  }

  /**
   * get the following page of browse terms
   *
   * @param d             the descriptor displayed at the bottom of the current page
   * @param searchingView the view being displayed
   * @since 1.0
   */
  public List getNextPage(Descriptor d, int searchingView) throws DataAccessException, HibernateException {
    int cataloguingView = getBrowsingViewBasedOnSearchingView(searchingView);
    DAODescriptor dao = getDao();
    List l = null;
    String operator = ">";

    /* Natascia 06/06/2007
     * se esegue una ricerca per PublisherPlace/Name ed e' una ricerca composta l'operatore diventa
     * >= e non >
     */
    if ((dao instanceof PublisherNameDescriptorDAO || dao instanceof PublisherPlaceDescriptorDAO) && dao.getBrowsingSortForm(d).indexOf(":") > 0) {
      operator = ">=";
    }
    if (dao instanceof NameTitleNameDescriptorDAO) {
      operator = ">=";
    }
    //TODo passare la session
    try {
      l = dao.getHeadingsBySortform(
        operator,
        "",
        dao.getBrowsingSortForm(d),
        getFilter(),
        cataloguingView,
        getPageSize(),
        null);
    } catch (DataAccessException e) {
      throw new BrowseFailedException();
    }
    return l;
  }

  public List getPreviousPage(Descriptor d, int searchingView) throws DataAccessException, HibernateException {
    int cataloguingView = getBrowsingViewBasedOnSearchingView(searchingView);
    DAODescriptor dao = getDao();
    List l = null;
    List result = new ArrayList();
    String operator = "<";

    /* Natascia 06/06/2007
     * se esegue una ricerca per PublisherPlace/Name ed e' una ricerca composta l'operatore diventa
     * <= e non <
     */
    if ((dao instanceof PublisherNameDescriptorDAO || dao instanceof PublisherPlaceDescriptorDAO) && dao.getBrowsingSortForm(d).indexOf(":") > 0) {
      operator = "<=";
    }
    if (dao instanceof NameTitleNameDescriptorDAO) {
      operator = "<=";
    }

    try {
      l = dao.getHeadingsBySortform(
        operator,
        "desc",
        dao.getBrowsingSortForm(d),
        getFilter(),
        cataloguingView,
        getPageSize(),
        null);

      // reverse the order of the list
      for (int i = l.size() - 1; i >= 0; i--) {
        result.add(l.get(i));
      }

    } catch (DataAccessException e) {
      throw new BrowseFailedException();
    }
    return result;
  }

  public DAODescriptor getDao() {
    return dao;
  }

  public void setDao(DAODescriptor descriptor) {
    dao = descriptor;
  }

  private int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int i) {
    pageSize = i;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String string) {
    filter = string;
  }

  public boolean isSupportsCrossReferences() {
    return supportsCrossReferences;
  }

  public void setSupportsCrossReferences(boolean b) {
    supportsCrossReferences = b;
  }

  public boolean isSupportsAuthorities() {
    return supportsAuthorities;
  }

  public void setSupportsAuthorities(boolean b) {
    supportsAuthorities = b;
  }

  public String getBrowseIndex() {
    return browseIndex;
  }

  /**
   * pm 2011
   * Get a list of viewText's for the given Descriptors
   *
   * @param list
   * @param locale
   * @return
   */
  public List getViewTexts(List list, Locale locale) {
    List result = new ArrayList();
    Descriptor aDescriptor = null;
    Iterator iter = list.iterator();
    while (iter.hasNext()) {
      aDescriptor = (Descriptor) iter.next();
      result.add(View.getViewText(aDescriptor.getUserViewString(), locale));
    }
    return result;
  }


  public List getFirstElement(String term, int searchingView, int termsToDisplay) throws DataAccessException, HibernateException, SQLException {
    int cataloguingView = getBrowsingViewBasedOnSearchingView(searchingView);
    DAODescriptor dao = getDao();
    setPageSize(termsToDisplay);
    //TODO passare la Session

    String searchTerm = dao.calculateSearchTerm(term, getBrowseIndex(), null);
//		System.out.println("SEARCH TERM: "+searchTerm);

    // MIKE: moved Publisher block into PublisherDescriptorDAO to calculate first temp searchTerm stringValue
    List l = null;

    try {
      if (logger.isDebugEnabled()) {
        logger.debug("looking for a heading = " + searchTerm);
      }

      l = dao.getHeadingsBySortform(
        "=",
        "",
        searchTerm,
        getFilter(),
        cataloguingView,
        getPageSize(),
        null);

    } catch (DataAccessException e) {
      throw new BrowseFailedException();
    }
    return l;
  }
}
