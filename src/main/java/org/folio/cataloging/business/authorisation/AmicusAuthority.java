/*
 * (c) LibriCore
 * 
 * Created on Nov 16, 2004
 * 
 * AmicusAuthority.java
 */
package org.folio.cataloging.business.authorisation;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.DAOUserAccount;
import org.folio.cataloging.dao.persistence.USR_ACNT;

import java.util.*;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/07/11 08:01:05 $
 * @since 1.0
 */
public class AmicusAuthority implements AuthorisationAgent {
	private static Map tagPermissions = new HashMap();

	static {
		tagPermissions.put(new Short((short) 1), "editHeader");
		tagPermissions.put(new Short((short) 2), "editName");
		tagPermissions.put(new Short((short) 3), "editTitle");
		tagPermissions.put(new Short((short) 4), "editSubject");
		tagPermissions.put(new Short((short) 5), "editControlNumber");
		tagPermissions.put(new Short((short) 6), "editClassNumber");
		tagPermissions.put(new Short((short) 7), "editNote");
		tagPermissions.put(new Short((short) 8), "editRelationship");
		tagPermissions.put(new Short((short) 9), "editCopy");
		tagPermissions.put(new Short((short) 10), "editSummaryHolding");
		tagPermissions.put(new Short((short) 12), "editNameTitle");
		tagPermissions.put(new Short((short) 19784), "editAuthorityNameTitle");
		tagPermissions.put(new Short((short) 20040), "editAuthorityName");
		tagPermissions.put(new Short((short) 21320), "editAuthoritySubject");
		tagPermissions.put(new Short((short) 21576), "editAuthorityTitle");
	}

	private Set permissions = new HashSet();

	public AmicusAuthority(String userAccount) throws DataAccessException {
		DAOUserAccount dao = new DAOUserAccount();
		List l = dao.getModuleAuthorisations(userAccount);
		Iterator iter = l.iterator();
		ModuleAuthorisation anAuth;
		while (iter.hasNext()) {
			anAuth = (ModuleAuthorisation) iter.next();
			//TODO add a column/table to join the permissionName to the stringValue of the auth table
			if ("cat".equals(anAuth.getModuleCode())) {
				if (anAuth.getAuthorisationLevel() > 0) {
					addPermission("basicCataloguing");
					addPermission("catReport");
					addPermission("editCodeTableItem");
					addPermission("editAuthority");
				}
				if (anAuth.getAuthorisationLevel() > 100) {
					addPermission("advancedCataloguing");
					USR_ACNT acnt = dao.load(userAccount);
					//paulm aut add permission for edit authority
					if (acnt.getCataloguingView() == 1) {
						addPermission("editAuthority");
					}
				}
			}
			if ("src".equals(anAuth.getModuleCode())) {
				if (anAuth.getAuthorisationLevel() >= 32767) {
					addPermission("secondarySearching");
				}
			}
			if ("mad".equals(anAuth.getModuleCode())) {
				if (anAuth.getAuthorisationLevel() > 0) {
					addPermission("basicCataloguing");
					addPermission("madReport");
					addPermission("usePredefined");
					addPermission("editCopies");
					addPermission("editHeadings");
					addPermission("editXRefs");
					addPermission("editAuthority");
					addPermission("showAuthority");
				}
				if (anAuth.getAuthorisationLevel() > 100) {
					addPermission("advancedCataloguing");
				}
				if (anAuth.getAuthorisationLevel() == 32767) {
					addPermission(Permission.TO_EDIT_CODE_TABLE_ITEM);
					addPermission("moveHierarchy");
				}
			}		
		}

		l = dao.getTagAuthorisations(userAccount);
		iter = l.iterator();
		TagAuthorisation authB;
		while (iter.hasNext()) {
			authB = (TagAuthorisation) iter.next();
			Short permCode = new Short(authB.getTagCategory());
			if(tagPermissions.containsKey(permCode)){
				addPermission((String) tagPermissions.get(permCode));
			}
		}
	}


	public AmicusAuthority(String userAccount,List modAuth,List tagAuth,int cataloguingView) throws DataAccessException {
		
		List l = modAuth;
		Iterator iter = l.iterator();
		ModuleAuthorisation anAuth;
		while (iter.hasNext()) {
			anAuth = (ModuleAuthorisation) iter.next();
			//TODO add a column/table to join the permissionName to the stringValue of the auth table
			if ("cat".equals(anAuth.getModuleCode())) {
				if (anAuth.getAuthorisationLevel() > 0) {
					addPermission("basicCataloguing");
					addPermission("catReport");
					addPermission("editCodeTableItem");
					addPermission("editAuthority");
				}
				if (anAuth.getAuthorisationLevel() > 100) {
					addPermission("advancedCataloguing");
					
					//paulm aut add permission for edit authority
					if (cataloguingView == 1) {
						addPermission("editAuthority");
					}
				}
			}
			if ("src".equals(anAuth.getModuleCode())) {
				if (anAuth.getAuthorisationLevel() >= 32767) {
					addPermission("secondarySearching");
				}
			}
			if ("mad".equals(anAuth.getModuleCode())) {
				if (anAuth.getAuthorisationLevel() > 0) {
					addPermission("basicCataloguing");
					addPermission("madReport");
					addPermission("usePredefined");
					addPermission("editCopies");
					addPermission("editHeadings");
					addPermission("editXRefs");
					addPermission("editAuthority");
					addPermission("showAuthority");
				}
				if (anAuth.getAuthorisationLevel() > 100) {
					addPermission("advancedCataloguing");
				}
				if (anAuth.getAuthorisationLevel() == 32767) {
					addPermission(Permission.TO_EDIT_CODE_TABLE_ITEM);
					addPermission("moveHierarchy");
				}
			}		
		}

		l = tagAuth;
		iter = l.iterator();
		TagAuthorisation authB;
		while (iter.hasNext()) {
			authB = (TagAuthorisation) iter.next();
			Short permCode = new Short(authB.getTagCategory());
			if(tagPermissions.containsKey(permCode)){
				addPermission((String) tagPermissions.get(permCode));
			}
		}
	}
	
	
	//TODO implement implies so that one permission can imply others 	
	private void addPermission(String permissionName) {
		getPermissions().add(new Permission(permissionName));
	}
	
	void removePermission(String permissionName) {
		getPermissions().remove(new Permission(permissionName));
	}

	public boolean isPermitted(String permissionName) {
		return isPermitted(new Permission(permissionName));
	}
	
	public void checkPermission(String permissionName) throws AuthorisationException {
		checkPermission(new Permission(permissionName));
	}

	public void checkPermission(Permission aPermission) throws AuthorisationException {
		if (!getPermissions().contains(aPermission)) {
			throw new AuthorisationException(aPermission.getName());
		}
	}

	public boolean isPermitted(Permission aPermission) {
		return getPermissions().contains(aPermission);
	}

	public void checkPermission(Permission[] somePermissions) throws AuthorisationException {
		if (!getPermissions().containsAll(Arrays.asList(somePermissions))) {
			throw new AuthorisationException(somePermissions[0].getName() + "...");
		}
	}

	public boolean isPermitted(Permission[] somePermissions) {
        return getPermissions().containsAll(Arrays.asList(somePermissions));
    }

	public boolean isPermitted(String[] someNames) {
		try {
			checkPermission(someNames);
			return true;
		} catch (AuthorisationException e) {
			return false;
		}
	}

	public void checkPermission(String[] someNames) throws AuthorisationException {
		List somePermissions = new ArrayList();
		for (int i = 0; i < someNames.length; i++) {
			somePermissions.add(new Permission(someNames[i]));
		}
		checkPermission((Permission[]) somePermissions.toArray(new Permission[] {}));
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Set getPermissions() {
		return permissions;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setPermissions(Set set) {
		permissions = set;
	}

	Map getTagPermissions() {
		return tagPermissions;
	}
}
