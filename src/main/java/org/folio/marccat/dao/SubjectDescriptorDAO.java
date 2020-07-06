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
public class SubjectDescriptorDAO extends DescriptorDAO {


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




}
