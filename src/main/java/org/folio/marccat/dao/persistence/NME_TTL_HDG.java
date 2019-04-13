package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.NameTitleDescriptorDAO;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

/**
 * @author paulm
 * @version $Revision: 1.6 $, $Date: 2006/07/12 15:42:56 $
 * @since 1.0
 */
public class NME_TTL_HDG extends Descriptor {
  private static final long serialVersionUID = 1L;
  private char copyToSubjectIndicator;

  private NME_HDG nameHeading = new NME_HDG();
  private int nameHeadingNumber;
  private TTL_HDG titleHeading = new TTL_HDG();
  private int titleHeadingNumber;


  public NME_TTL_HDG() {
    super();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof NME_TTL_HDG) {
      return getKey().equals(obj);
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see Descriptor#getAccessPointClass()
   */
  public Class getAccessPointClass() {
    return NameTitleAccessPoint.class;
  }

  /* (non-Javadoc)
   * @see Descriptor#getCategory()
   */
  public int getCategory() {
    return 11;
  }


  @Override
  public char getCopyToSubjectIndicator() {
    return copyToSubjectIndicator;
  }


  @Override
  public void setCopyToSubjectIndicator(char c) {
    copyToSubjectIndicator = c;
  }

  /* (non-Javadoc)
   * @see Descriptor#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    CorrelationValues v = new CorrelationValues();
    v = v.change(1, getNameHeading().getTypeCode());
    v = v.change(2, getNameHeading().getSubTypeCode());
    return v;
  }

  /* (non-Javadoc)
   * @see Descriptor#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    /* even though the user cannot change the name type/subtype via a name/title
     * we do alter the name heading instance here for the proper behaviour of model
     * editing and of correlation list setting on the edit screen.  In the case of
     * models, the actual name heading is not stored.  In the case of the edit screen,
     * the attached heading is also not modified.
     */
    getNameHeading().setCorrelationValues(v);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#getDAO()
   */
  public AbstractDAO getDAO() {
    return new NameTitleDescriptorDAO();
  }

  /* (non-Javadoc)
   * @see Descriptor#getDefaultBrowseKey()
   */
  public String getDefaultBrowseKey() {
    return "250S";
  }


  public NME_HDG getNameHeading() {
    return nameHeading;
  }


  public void setNameHeading(NME_HDG nme_hdg) {
    nameHeading = nme_hdg;
    setNameHeadingNumber(nme_hdg.getKey().getKeyNumber());
  }


  public int getNameHeadingNumber() {
    return nameHeadingNumber;
  }


  public void setNameHeadingNumber(int i) {
    nameHeadingNumber = i;
  }

  /* (non-Javadoc)
   * @see Descriptor#getNextNumberKeyFieldCode()
   */
  public String getNextNumberKeyFieldCode() {
    return "MH";
  }

  /* (non-Javadoc)
   * @see Descriptor#getReferenceClass()
   */
  public Class getReferenceClass(Class targetClazz) {
    if (targetClazz == this.getClass()) {
      return NME_TTL_REF.class;
    } else if (targetClazz == NME_HDG.class) {
      return NME_NME_TTL_REF.class;
    } else if (targetClazz == TTL_HDG.class) {
      return TTL_NME_TTL_REF.class;
    } else {
      return null;
    }
  }

  /* (non-Javadoc)
   * @see Descriptor#getSortFormParameters()
   */
  public SortFormParameters getSortFormParameters() {
    /*
     * Could be implemented similar to Publishers (name and place) but then you
     * would need to look in two places if you were looking for name usage (for ex.)
     */
    return getTitleHeading().getSortFormParameters();
  }


  public TTL_HDG getTitleHeading() {
    return titleHeading;
  }


  public void setTitleHeading(TTL_HDG ttl_hdg) {
    titleHeading = ttl_hdg;
    setTitleHeadingNumber(ttl_hdg.getKey().getKeyNumber());
  }


  public int getTitleHeadingNumber() {
    return titleHeadingNumber;
  }


  public void setTitleHeadingNumber(int i) {
    titleHeadingNumber = i;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return getKey().hashCode();
  }

  /* (non-Javadoc)
   * @see Descriptor#getDisplayValue()
   */
  @Override
  public String getDisplayValue() {
    StringText result = new StringText(getNameHeading().getDisplayValue());
    StringText title = new StringText(getTitleHeading().getDisplayValue());
    StringText subA = title.getSubfieldsWithCodes("a");
    if (subA.getNumberOfSubfields() > 0) {
      subA.getSubfield(0).setCode("t");
    }
    result.add(subA);
    result.add(title.getSubfieldsWithoutCodes("a"));
    return result.toString();
  }

  /* (non-Javadoc)
   * @see Descriptor#setDisplayValue(java.lang.String)
   */
  @Override
  public void setDisplayValue(String string) {
    /*
     * Puts all subfields before $t into name and all after into title  --
     * this may result in invalid subfields if input does not follow this
     * convention
     */
    if (getNameHeading() != null) {
      /*
       * if the name heading is null then we are being called from the
       * Descriptor constructor (before init) so bypass this activity
       */
      StringText s = new StringText(string);
      StringText name = new StringText();
      StringText title = new StringText();
      int i;
      for (i = 0; i < s.getNumberOfSubfields(); i++) {
        Subfield f = s.getSubfield(i);
        if (f.getCode().equals("t")) {
          f.setCode("a");
          break;
        }
        name.addSubfield(f);
      }
      for (int j = i; j < s.getNumberOfSubfields(); j++) {
        title.addSubfield(s.getSubfield(j));
      }
      getNameHeading().setDisplayValue(name.toString());
      getTitleHeading().setDisplayValue(title.toString());
    }
  }

  /* (non-Javadoc)
   * @see Descriptor#getDisplayText()
   */
  @Override
  public String getDisplayText() {
    String name = getNameHeading().getDisplayText();
    String title = getTitleHeading().getDisplayText();
    String spacer;
    if (name.endsWith(".")) {
      spacer = " ";
    } else {
      spacer = ". ";
    }
    return name + spacer + title;
  }

  /* (non-Javadoc)
   * @see Descriptor#getHeadingNumberSearchIndex()
   */
  public String getHeadingNumberSearchIndexKey() {
    return "252P";
  }

  public String getLockingEntityType() {
    return "MH";
  }

  @Override
  public String buildBrowseTerm() {
    return getNameHeading().getDisplayText() + " : " + getTitleHeading().getDisplayText();
  }

}
