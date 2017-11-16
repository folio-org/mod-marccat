/*
 * (c) LibriCore
 * 
 * Created on 16-may-2005
 * 
 * InventoryLogicalSearchKey.java
 */
package librisuite.hibernate;

import java.io.Serializable;

 /**
 * @author Ruben
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:23 $
 * @since 1.0
 */
public class InventoryLogicalSearchKey implements Serializable{

	private int copyIdNumber;
	private int inventorySearchNumber;
	
	

	/**
	 * Class constructor
	 *
	 * 
	 */
	public InventoryLogicalSearchKey() {
		super();
	}
	
	
    /**
     * @return Returns the copyIdNumber.
     * @exception
     * @since 1.0
     */
    public int getCopyIdNumber() {
        return copyIdNumber;
    }
    /**
     * @param copyIdNumber The copyIdNumber to set.
     * @exception
     * @since 1.0
     */
    public void setCopyIdNumber(int copyIdNumber) {
        this.copyIdNumber = copyIdNumber;
    }
    /**
     * @return Returns the inventorySearchNumber.
     * @exception
     * @since 1.0
     */
    public int getInventorySearchNumber() {
        return inventorySearchNumber;
    }
    /**
     * @param inventorySearchNumber The inventorySearchNumber to set.
     * @exception
     * @since 1.0
     */
    public void setInventorySearchNumber(int inventorySearchNumber) {
        this.inventorySearchNumber = inventorySearchNumber;
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        // TODO Auto-generated method stub
        return super.equals(arg0);
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }
}
