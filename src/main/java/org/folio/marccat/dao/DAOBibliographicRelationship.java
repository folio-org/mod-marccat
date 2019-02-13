package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicItem;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.dao.persistence.BibliographicRelationship;
import org.folio.marccat.dao.persistence.NameAccessPoint;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;

import java.util.List;

/**
 * @author hansv
 * @version $Revision: 1.5 $, $Date: 2006/01/19 14:43:57 $
 * @since 1.0
 */
public class DAOBibliographicRelationship extends AbstractDAO {
  private static final Log logger = LogFactory.getLog(DAOBibliographicRelationship.class);

  public short getReciprocalBibItem(int bibItemNumber, int targetBibItemNumber, int userView) throws DataAccessException {
    Session s = currentSession();
    List l = null;
    try {
      l =
        s.find(
          " select count(*) from "
            + "BibliographicRelationship t "
            + " where t.bibItemNumber = ? and "
            + " t.targetBibItemNumber = ? and "
            + " substr(t.userViewString, ?, 1) = '1'",
          new Object[]{
            new Integer(bibItemNumber),
            new Integer(targetBibItemNumber),
            new Integer(userView)},
          new Type[]{
            Hibernate.INTEGER,
            Hibernate.INTEGER,
            Hibernate.INTEGER});
      if (((Integer) l.get(0)).shortValue() > 0) {
        return 1;
      }
    } catch (HibernateException e) {
      logAndWrap(e);
    }

    return 2;
  }

  public BibliographicRelationship loadReciprocalBibItem(int bibItemNumber, int targetBibItemNumber, int userView) throws DataAccessException {
    BibliographicRelationship relationship = null;
    try {
      List multiView =
        currentSession().find(
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

      List singleView = isolateViewForList(multiView, userView);

      if (singleView.size() > 0) {
        return (BibliographicRelationship) singleView.get(0);
      }
    } catch (HibernateException e) {
      logAndWrap(e);
    }

    return relationship;
  }

  public StringText buildRelationStringText(int bibItemNumber, int userView) throws DataAccessException {
    StringText stringText = new StringText();
    BibliographicCatalogDAO catalog = new BibliographicCatalogDAO();

    BibliographicItem item = catalog.getBibliographicItemWithoutRelationships(bibItemNumber, userView);

    /* Name Tags */

    VariableField t = (VariableField) item.findFirstTagByNumber("1");
    if (t != null && t instanceof NameAccessPoint) {
      stringText.addSubfield(
        new Subfield("a", t.getStringText().toDisplayString()));
    }

    /* Title Tags */

    t = (VariableField) item.findFirstTagByNumber("130");

    if (t != null) {
      stringText.addSubfield(
        new Subfield("t", t.getStringText().toDisplayString()));
    } else {
      t = (VariableField) item.findFirstTagByNumber("245");
      if (t != null) {
        stringText.addSubfield(
          new Subfield("t", t.getStringText().toDisplayString()));
      }
      // TODO _MIKE: This code is evil! Cavolo, non va per niente bene...
      else logger.error("245 is a required tag");
    }

    t = (VariableField) item.findFirstTagByNumber("210");
    if (t != null) {
      stringText.addSubfield(
        new Subfield("p", t.getStringText().toDisplayString()));
    }

    /* Note Tags */

    t = (VariableField) item.findFirstTagByNumber("250");
    if (t != null) {
      stringText.addSubfield(
        new Subfield("b", t.getStringText().toDisplayString()));
    }

    /* Publisher Tags */

    t = (VariableField) item.findFirstTagByNumber("260");
    if (t != null) {
      stringText.addSubfield(
        new Subfield("d", t.getStringText().toDisplayString()));
    }

    /* ControlNumber Tags */

    t = (VariableField) item.findFirstTagByNumber("020");
    if (t != null) {
      stringText.addSubfield(
        new Subfield("z", t.getStringText().toDisplayString()));
    } else {
      t = (VariableField) item.findFirstTagByNumber("022");
      if (t != null) {
        stringText.addSubfield(
          new Subfield("x", t.getStringText().toDisplayString()));
      }
    }

    /* ClassificationNumber Tags */

    t = (VariableField) item.findFirstTagByNumber("088");
    if (t != null) {
      stringText.addSubfield(
        new Subfield("r", t.getStringText().toDisplayString()));
    }

    t = (VariableField) item.findFirstTagByNumber("027");
    if (t != null) {
      stringText.addSubfield(
        new Subfield("u", t.getStringText().toDisplayString()));
    }

    t = (VariableField) item.findFirstTagByNumber("030");
    if (t != null) {
      stringText.addSubfield(
        new Subfield("y", t.getStringText().toDisplayString()));
    }

    /* BibItem data */
    stringText.addSubfield(
      new Subfield("e", item.getBibItmData().getLanguageCode()));
    stringText.addSubfield(
      new Subfield("f", item.getBibItmData().getMarcCountryCode()));

    //TODO add missing subfields s and k

    return stringText;
  }
}
