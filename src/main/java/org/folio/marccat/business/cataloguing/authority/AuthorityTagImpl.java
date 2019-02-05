package org.folio.marccat.business.cataloguing.authority;

import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.common.Catalog;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;
import org.folio.marccat.business.common.SubfieldCodeComparator;
import org.folio.marccat.dao.DAOAuthorityCorrelation;
import org.folio.marccat.dao.DAOAuthorityValidation;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.MarcCorrelationException;
import org.folio.marccat.shared.Validation;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class AuthorityTagImpl extends TagImpl {

  private static final DAOAuthorityCorrelation daoCorrelation = new DAOAuthorityCorrelation();
  private static final DAOAuthorityValidation daoValidation = new DAOAuthorityValidation();
  private int authorityNumber;

  public int getItemNumber() {
    return getAuthorityNumber();
  }

  public void setItemNumber(int itemNumber) {
    setAuthorityNumber(itemNumber);
  }

  /**
   * @since 1.0
   */
  public int getAuthorityNumber() {
    return authorityNumber;
  }

  /**
   * @since 1.0
   */
  public void setAuthorityNumber(int i) {
    authorityNumber = i;
  }

  public int hashCode() {
    return getAuthorityNumber();
  }

  @Deprecated
  public CorrelationKey getMarcEncoding(Tag t) throws DataAccessException {

    CorrelationKey key = daoCorrelation.getMarcEncoding(t.getCategory(), getHeadingType(t),
      t.getCorrelation(1), t.getCorrelation(2), t.getCorrelation(3));

    if (key == null) {
      throw new MarcCorrelationException();
    }

    return key;
  }


  public String getHeadingType(Tag t) {
    return ((AUT) ((PersistsViaItem) t).getItemEntity()).getHeadingType();
  }

  public Validation getValidation(Tag t) throws DataAccessException {
    CorrelationKey key = getMarcEncoding(t);
    return daoValidation.load(key.getMarcTag(), t.getHeadingType(), t.getCategory());
  }

  @Override
  public Validation getValidation(Tag t, Session session) throws DataAccessException {
    return null;
  }

  public Catalog getCatalog() {
    return new AuthorityCatalog();
  }

  @Override
  public CorrelationKey getMarcEncoding(Tag t, Session session) throws DataAccessException {
    return null;
  }

  public Set getValidEditableSubfields(int category) {
    Set set = new TreeSet(new SubfieldCodeComparator());
    switch (category) {
      case 6:
        set.add("d");
        break;
      case 15:
        set.addAll(Arrays.asList("d", "5"));
        break;
      default:
        set.addAll(Arrays.asList("i", "e", "j", "4"));
        break;
    }
    return set;
  }

  @Override
  public Correlation getCorrelation(String tagNumber, char indicator1,
                                    char indicator2, int category, Session session) throws DataAccessException {
    return daoCorrelation.getFirstAuthorityCorrelation(tagNumber, indicator1,
      indicator2, category);
  }

}
