package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicItem;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicTagImpl;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.dao.persistence.BibliographicRelationship;
import org.folio.marccat.dao.persistence.NameAccessPoint;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;
import java.util.List;

/**
 * Data access object to relation.
 *
 * @author carment
 */
public class BibliographicRelationshipDAO extends AbstractDAO {

  /**
   * Gets the reciprocal bib item.
   *
   * @param bibItemNumber the bib item number
   * @param targetBibItemNumber the target bib item number
   * @param userView the user view
   * @param session the hibernate session
   * @return the reciprocal bib item
   * @throws HibernateException the hibernate exception
   */
  public int getReciprocalBibItem(final int bibItemNumber, final int targetBibItemNumber, final int userView, final Session session) throws HibernateException {
        final List l = session.find(
        " select count(*) from "
          + "BibliographicRelationship t "
          + " where t.bibItemNumber = ? and "
          + " t.targetBibItemNumber = ? and "
          + " substr(t.userViewString, ?, 1) = '1'",
        new Object[]{
          bibItemNumber,
          targetBibItemNumber,
          userView},
        new Type[]{
          Hibernate.INTEGER,
          Hibernate.INTEGER,
          Hibernate.INTEGER});
        return (((Integer) l.get(0)) > 0) ? 1 : 2;
      }

  /**
   * Load the reciprocal bib item
   *
   * @param bibItemNumber the bib item number
   * @param targetBibItemNumber the target bib item number
   * @param userView the user view
   * @param session the hibernate session
   * @return the bibliographic relationship
   * @throws HibernateException the hibernate exception
   */
  public BibliographicRelationship loadReciprocalBibItem(final int bibItemNumber, final int targetBibItemNumber, final int userView, final Session session) throws HibernateException  {
    final List multiView =
        session.find(
          "from BibliographicRelationship t "
            + "where t.bibItemNumber = ? and t.targetBibItemNumber = ? and substr(t.userViewString,?,1) = '1'",
          new Object[]{
            bibItemNumber,
            targetBibItemNumber,
            userView},
          new Type[]{
            Hibernate.INTEGER,
            Hibernate.INTEGER,
            Hibernate.INTEGER});

    final List singleView = isolateViewForList(multiView, userView, session);
    return (!singleView.isEmpty()) ? (BibliographicRelationship) singleView.get(0) : null;
  }


  /**
   * Builds the relation string text.
   *
   * @param bibItemNumber the bib item number
   * @param userView the user view
   * @param session the session
   * @return the string text
   */
  public StringText buildRelationStringText(final int bibItemNumber, final int userView, final Session session) throws HibernateException {
    final StringText stringText = new StringText();
    final BibliographicCatalogDAO catalog = new BibliographicCatalogDAO();
    final BibliographicItem item = catalog.getBibliographicItemWithoutRelationships(bibItemNumber, userView, session);
    item.getTags().forEach(tag -> {
      tag.setTagImpl(new BibliographicTagImpl());
      catalog.addHeaderType(session, tag);
      tag.setCorrelationKey(tag.getTagImpl().getMarcEncoding(tag, session));
    });
    VariableField t = (VariableField) item.findFirstTagByNumber("1");
    if (t instanceof NameAccessPoint) {
      stringText.addSubfield(new Subfield("a", t.getStringText().toDisplayString()));
    }
    boolean isAddedTitle = appendSubfield(stringText, item, "130", "t");
    if(!isAddedTitle)
      appendSubfield(stringText, item, "245", "t");
    appendSubfield(stringText, item, "210", "p");
    appendSubfield(stringText, item, "250", "b");
    appendSubfield(stringText, item, "260", "d");
    boolean isAddedISBN = appendSubfield(stringText, item, "020", "z");
    if(!isAddedISBN)
      appendSubfield(stringText, item, "022", "x");
    appendSubfield(stringText, item, "088", "r");
    appendSubfield(stringText, item, "027", "u");
    appendSubfield(stringText, item, "030", "y");
    stringText.addSubfield(new Subfield("e", item.getBibItmData().getLanguageCode()));
    stringText.addSubfield(new Subfield("f", item.getBibItmData().getMarcCountryCode()));
    return stringText;
  }


  /**
   * Append subfield.
   *
   * @param stringText the string text
   * @param item the item
   * @param tagNumber the tag number
   * @param code the code
   * @return true, if successful
   */
  private boolean appendSubfield(final StringText stringText, final BibliographicItem item, final String tagNumber, final String code) {
    VariableField t;
    t = (VariableField) item.findFirstTagByNumber(tagNumber);
    if (t != null) {
      stringText.addSubfield(new Subfield(code, t.getStringText().toDisplayString()));
      return true;
    }
    return false;
  }


}
