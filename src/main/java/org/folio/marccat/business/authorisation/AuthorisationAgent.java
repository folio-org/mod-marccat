package org.folio.marccat.business.authorisation;

/**
 * @author paulm
 * @since 1.0
 */
public interface AuthorisationAgent {
  void checkPermission(String permissionName) throws AuthorisationException;

  void checkPermission(Permission aPermission) throws AuthorisationException;

  void checkPermission(Permission[] somePermissions) throws AuthorisationException;

  void checkPermission(String[] someNames) throws AuthorisationException;

  boolean isPermitted(String permissionName);

  boolean isPermitted(Permission aPermission);

  boolean isPermitted(Permission[] somePermissions);

  boolean isPermitted(String[] someNames);

}
