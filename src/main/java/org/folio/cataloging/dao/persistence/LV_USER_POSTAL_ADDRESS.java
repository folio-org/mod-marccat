/*
 * (c) LibriCore
 * 
 * Created on 08-feb-2005
 * 
 * LV_USER_POSTAL_ADDRESS.java
 */
package org.folio.cataloging.dao.persistence;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LV_USER_POSTAL_ADDRESS {

    private UserPostalAddressKey key;
    private Character userFloorNumber;
    private Character userRoomNumber;
    private String userStreetName;
    private Character userStreetNumber;
    private String userPostalCode;
    private String userCity;
    private Character userCountryCode;
    

    public UserPostalAddressKey getKey() {
        return key;
    }
    public void setKey(UserPostalAddressKey key) {
        this.key = key;
    }
    public String getUserCity() {
        return userCity;
    }
    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }
    public Character getUserCountryCode() {
        return userCountryCode;
    }
    public void setUserCountryCode(Character userCountryCode) {
        this.userCountryCode = userCountryCode;
    }
    public Character getUserFloorNumber() {
        return userFloorNumber;
    }
    public void setUserFloorNumber(Character userFloorNumber) {
        this.userFloorNumber = userFloorNumber;
    }
    public String getUserPostalCode() {
        return userPostalCode;
    }
    public void setUserPostalCode(String userPostalCode) {
        this.userPostalCode = userPostalCode;
    }
    public Character getUserRoomNumber() {
        return userRoomNumber;
    }
    public void setUserRoomNumber(Character userRoomNumber) {
        this.userRoomNumber = userRoomNumber;
    }
    public String getUserStreetName() {
        return userStreetName;
    }
    public void setUserStreetName(String userStreetName) {
        this.userStreetName = userStreetName;
    }
    public Character getUserStreetNumber() {
        return userStreetNumber;
    }
    public void setUserStreetNumber(Character userStreetNumber) {
        this.userStreetNumber = userStreetNumber;
    }
}
