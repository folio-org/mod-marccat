/*
 * (c) LibriCore
 * 
 * Created on 16-may-2005
 * 
 * CIR_INVTRY_LGL_SRCH.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

 /**
 * @author Ruben
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class CIR_INVTRY_LGL_SRCH implements Serializable {

    private InventoryLogicalSearchKey key; 
    
    
    
    
    /**
     * @return Returns the key.
     * @exception
     * @since 1.0
     */
    public InventoryLogicalSearchKey getKey() {
        return key;
    }
    /**
     * @param key The key to set.
     * @exception
     * @since 1.0
     */
    public void setKey(InventoryLogicalSearchKey key) {
        this.key = key;
    }
}
