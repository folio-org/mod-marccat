/*
 * Created on Apr 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.folio.cataloging.integration.hibernate;

/**
 * @author wimc
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ORG_HRCHY {
    private int ORG_NBR;
    private int PARNT_ORG_NBR;
    /**
     * @return ORG_NBR
     */
    public int getORG_NBR() {
        return ORG_NBR;
    }

    /**
     * @return PARNT_ORG_NBR
     */
    public int getPARNT_ORG_NBR() {
        return PARNT_ORG_NBR;
    }

    /**
     * @param i
     */
    public void setORG_NBR(int i) {
        ORG_NBR = i;
    }

    /**
     * @param i
     */
    public void setPARNT_ORG_NBR(int i) {
        PARNT_ORG_NBR = i;
    }

}
