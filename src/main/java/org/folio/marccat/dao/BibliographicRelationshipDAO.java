package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicItem;
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

    VariableField t = (VariableField) item.findFirstTagByNumber("1", session);
    if (t instanceof NameAccessPoint) {
      stringText.addSubfield(new Subfield("a", t.getStringText().toDisplayString()));
    }

    t = (VariableField) item.findFirstTagByNumber("130", session);

    if (t != null) {
      stringText.addSubfield(new Subfield("t", t.getStringText().toDisplayString()));
    } else {
      t = (VariableField) item.findFirstTagByNumber("245", session);
      if (t != null) {
        stringText.addSubfield(new Subfield("t", t.getStringText().toDisplayString()));
      }
    }

    t = (VariableField) item.findFirstTagByNumber("210", session);
    if (t != null) {
      stringText.addSubfield(new Subfield("p", t.getStringText().toDisplayString()));
    }

    t = (VariableField) item.findFirstTagByNumber("250", session);
    if (t != null) {
      stringText.addSubfield(new Subfield("b", t.getStringText().toDisplayString()));
    }

    t = (VariableField) item.findFirstTagByNumber("260", session);
    if (t != null) {
      stringText.addSubfield(new Subfield("d", t.getStringText().toDisplayString()));
    }

    t = (VariableField) item.findFirstTagByNumber("020", session);
    if (t != null) {
      stringText.addSubfield(new Subfield("z", t.getStringText().toDisplayString()));
    } else {
      t = (VariableField) item.findFirstTagByNumber("022", session);
      if (t != null) {
        stringText.addSubfield(new Subfield("x", t.getStringText().toDisplayString()));
      }
    }

    t = (VariableField) item.findFirstTagByNumber("088", session);
    if (t != null) {
      stringText.addSubfield(new Subfield("r", t.getStringText().toDisplayString()));
    }

    t = (VariableField) item.findFirstTagByNumber("027", session);
    if (t != null) {
      stringText.addSubfield(new Subfield("u", t.getStringText().toDisplayString()));
    }

    t = (VariableField) item.findFirstTagByNumber("030", session);
    if (t != null) {
      stringText.addSubfield(new Subfield("y", t.getStringText().toDisplayString()));
    }

    stringText.addSubfield(new Subfield("e", item.getBibItm().getLanguageCode()));
    stringText.addSubfield(new Subfield("f", item.getBibItm().getMarcCountryCode()));
    return stringText;
  }



}
