/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2004
 * 
 * AuthorisationAgent.java
 */
package librisuite.business.authorisation;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/11/19 16:48:32 $
 * @since 1.0
 */
public interface AuthorisationAgent {
	public abstract void checkPermission(String permissionName) throws AuthorisationException;

	public abstract void checkPermission(Permission aPermission) throws AuthorisationException;

	public abstract void checkPermission(Permission[] somePermissions) throws AuthorisationException;

	public abstract void checkPermission(String[] someNames) throws AuthorisationException;

	public abstract boolean isPermitted(String permissionName);

	public abstract boolean isPermitted(Permission aPermission);

	public abstract boolean isPermitted(Permission[] somePermissions);

	public abstract boolean isPermitted(String[] someNames);

}