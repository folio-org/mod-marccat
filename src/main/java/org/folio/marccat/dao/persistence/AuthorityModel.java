package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.AuthorityModelDAO;

/**
 * Represents a Model/Template for of authority type
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class AuthorityModel extends Model {

  private String frbrSecondGroup;
  private String frbrThirdGroup;

  /**
   * Gets the authority model DAO.
   *
   * @return the authority model DAO
   */
  public AuthorityModelDAO getAuthorityModelDAO() {
    return new AuthorityModelDAO();
  }

  /**
   * Gets the frbr second group.
   *
   * @return the frbr second group
   */
  public String getFrbrSecondGroup() {
    return frbrSecondGroup;
  }

  /**
   * Sets the frbr second group.
   * This group is related to the FRBR entity: Persons, Families and Corporate Bodies.
   *
   * @param frbrSecondGroup the new frbr second group
   */
  public void setFrbrSecondGroup(final String frbrSecondGroup) {
    this.frbrSecondGroup = frbrSecondGroup;
  }

  /**
   * Gets the frbr third group.
   * This group is related to the FRBR entity: Concepts, Objcts, Places, Events.
   *
   * @return the frbr third group
   */
  public String getFrbrThirdGroup() {
    return frbrThirdGroup;
  }

  /**
   * Sets the frbr third group.
   *
   * @param frbrThirdGroup the new frbr third group
   */
  public void setFrbrThirdGroup(final String frbrThirdGroup) {
    this.frbrThirdGroup = frbrThirdGroup;
  }

  /**
   * Gets the dao.
   *
   * @return the dao
   */
  @Deprecated
  public AbstractDAO getDAO() {
    return null;
  }
}
