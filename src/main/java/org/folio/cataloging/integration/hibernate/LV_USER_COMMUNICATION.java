/*
 * (c) LibriCore
 * 
 * Created on 08-feb-2005
 * 
 * LV_USER_COMMUNICATION.java
 */
package org.folio.cataloging.integration.hibernate;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LV_USER_COMMUNICATION {

    private UserCommunicationKey key;
	private String userText;
	private Short userTypeCode;


    public UserCommunicationKey getKey() {
        return key;
    }
    public void setKey(UserCommunicationKey key) {
        this.key = key;
    }
    public String getUserText() {
        return userText;
    }
    public void setUserText(String userText) {
        this.userText = userText;
    }
    public Short getUserTypeCode() {
        return userTypeCode;
    }
    public void setUserTypeCode(Short userTypeCode) {
        this.userTypeCode = userTypeCode;
    }
}
