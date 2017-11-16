/*
 * (c) LibriCore
 * 
 * Created on Jun 15, 2004
 * 
 * NME_HDG_KEY.java
 */
package librisuite.hibernate;

import java.io.Serializable;

/**
 * Represents composite key for NME_HDG class
 * @author paulm
 * @version $Revision: 1.7 $, $Date: 2005/07/13 12:45:10 $
 * @since 1.0
 */
public class InventoryKey implements Serializable{
	private int inventoryNumber;
	private int mainLibraryNumber;

	/**
	 * Class constructor
	 *
	 * 
	 */
	/*public DescriptorKey() {
		super();
		headingNumber = -1;
		userViewString = "0000000000000000";
	}*/
	
	/**
	 * Class constructor
	 *
	 * 
	 */
	/*public DescriptorKey(int headingNumber, String view) {
		this.setHeadingNumber(headingNumber);
		this.setUserViewString(view);
	}*/

	/**
	 * override equals and hashcode for hibernate key comparison
	 */
	public boolean equals(Object anObject) {
		if (anObject instanceof InventoryKey) {
			InventoryKey aKey = (InventoryKey) anObject;
			return (
					inventoryNumber == aKey.getInventoryNumber()
					&& mainLibraryNumber==aKey.getMainLibraryNumber());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return inventoryNumber + mainLibraryNumber;
	}

	public int getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(int inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public int getMainLibraryNumber() {
		return mainLibraryNumber;
	}

	public void setMainLibraryNumber(int mainLibraryNumber) {
		this.mainLibraryNumber = mainLibraryNumber;
	}

	
}
