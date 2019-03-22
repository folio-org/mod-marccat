package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.SBJCT_HDG;

import java.util.List;

/**
 * Manages headings in the SBJCT_HDG table.
 *
 * @author paulm
 * @author carment
 */
public class SubjectDescriptorDAO extends DAODescriptor {


  /**
   * Gets the persistent class.
   *
   * @return the persistent class
   */
  public Class getPersistentClass() {
    return SBJCT_HDG.class;
  }


  /**
   * Supports authorities.
   *
   * @return true, if successful
   */

  @Override
  public boolean supportsAuthorities() {
    return true;
  }

  /**
   * Checks if is matching another heading(SBJCT_HDG).
   *
   * @param desc    the desc
   * @param session the session
   * @return true, if is matching another heading
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean isMatchingAnotherHeading(final Descriptor desc, final Session session)
    throws HibernateException {

    final SBJCT_HDG subjectHeading = (SBJCT_HDG) desc;
    final List<Integer> countList = session.find(
      "select count(*) from "
        + getPersistentClass().getName()
        + " as c "
        + " where c.stringText= ? "
        + " and c.accessPointLanguage = ?"
        + " and c.typeCode =? "
        + " and c.sourceCode =? "
        + " and c.key.userViewString = ?"
        + " and c.key.headingNumber <> ?",
      new Object[]{
        subjectHeading.getStringText(),
        subjectHeading.getAccessPointLanguage(),
        subjectHeading.getTypeCode(),
        subjectHeading.getSourceCode(),
        subjectHeading.getUserViewString(),
        subjectHeading.getKey().getHeadingNumber()},
      new Type[]{Hibernate.STRING,
        Hibernate.INTEGER,
        Hibernate.INTEGER,
        Hibernate.INTEGER,
        Hibernate.STRING,
        Hibernate.INTEGER});
    return countList.get(0) > 0;

  }


}
