package org.folio.marccat.dao;


import org.folio.marccat.dao.persistence.SBJCT_HDG;



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
