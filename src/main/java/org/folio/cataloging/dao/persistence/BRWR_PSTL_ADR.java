/*
 * (c) LibriCore
 * 
 * Created on 19-nov-2004
 * 
 * BRWR_PSTL_ADR.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Maite
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class BRWR_PSTL_ADR implements Serializable {
	
    private BorrowerPostalAdrKey key;
	private String postalAddressCityShortForm; 
	private String postalAddressStreetNumber;
	private String postalAddressFloorNumber;
	private String postalAddressRoomNumber;
	private String postalAddressUnitDsgtrNumber;
	private String postalAddressDeliveryDsgtrNumber;
	private short postalAddressMdeCode;
	private short postalAddressStreetTypeCode;
	private short postalAddressStreetDirectionCode;
	private short postalAddressUnitDsgtrCode; 
	private short postalAddressDeliveryDsgtrCode;
	private short postalAddressIntlnTypeCode;
	private String postalAddressDepartmentNameCode;
	private short postalAddressRegionCode;
	private short postalAddressGeographyCode;
	private String postalAddressProvincialTerritoryStateCode;
	private String postalAddressPostalCode;
	private short postalAddressCountryCode;
	private String postalAddressIntlnQualigyDescriptor;
	private String postalAddressReferenceDescriptor;
	private String postalAddressPersonalAttentionName;
	private String postalAddressOrganisationName;
	private String postalAddressStreetName;
	private String postalAddressBldgName; 
	private String postalAddressCityName;


    /**
     * @return Returns the key.
     * @exception
     * @since 1.0
     */
    public BorrowerPostalAdrKey getKey() {
        return key;
    }
    /**
     * @param key The key to set.
     * @exception
     * @since 1.0
     */
    public void setKey(BorrowerPostalAdrKey key) {
        this.key = key;
    }
    /**
     * @return Returns the postalAddressBldgName.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressBldgName() {
        return postalAddressBldgName;
    }
    /**
     * @param postalAddressBldgName The postalAddressBldgName to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressBldgName(String postalAddressBldgName) {
        this.postalAddressBldgName = postalAddressBldgName;
    }
    /**
     * @return Returns the postalAddressCityName.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressCityName() {
        return postalAddressCityName;
    }
    /**
     * @param postalAddressCityName The postalAddressCityName to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressCityName(String postalAddressCityName) {
        this.postalAddressCityName = postalAddressCityName;
    }
    /**
     * @return Returns the postalAddressCityShortForm.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressCityShortForm() {
        return postalAddressCityShortForm;
    }
    /**
     * @param postalAddressCityShortForm The postalAddressCityShortForm to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressCityShortForm(String postalAddressCityShortForm) {
        this.postalAddressCityShortForm = postalAddressCityShortForm;
    }
    /**
     * @return Returns the postalAddressCountryCode.
     * @exception
     * @since 1.0
     */
    public short getPostalAddressCountryCode() {
        return postalAddressCountryCode;
    }
    /**
     * @param postalAddressCountryCode The postalAddressCountryCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressCountryCode(
            short postalAddressCountryCode) {
        this.postalAddressCountryCode = postalAddressCountryCode;
    }
    /**
     * @return Returns the postalAddressDeliveryDsgtrCode.
     * @exception
     * @since 1.0
     */
    public short getPostalAddressDeliveryDsgtrCode() {
        return postalAddressDeliveryDsgtrCode;
    }
    /**
     * @param postalAddressDeliveryDsgtrCode The postalAddressDeliveryDsgtrCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressDeliveryDsgtrCode(
            short postalAddressDeliveryDsgtrCode) {
        this.postalAddressDeliveryDsgtrCode = postalAddressDeliveryDsgtrCode;
    }
    /**
     * @return Returns the postalAddressDeliveryDsgtrNumber.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressDeliveryDsgtrNumber() {
        return postalAddressDeliveryDsgtrNumber;
    }
    /**
     * @param postalAddressDeliveryDsgtrNumber The postalAddressDeliveryDsgtrNumber to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressDeliveryDsgtrNumber(
            String postalAddressDeliveryDsgtrNumber) {
        this.postalAddressDeliveryDsgtrNumber = postalAddressDeliveryDsgtrNumber;
    }
    /**
     * @return Returns the postalAddressDepartmentNameCode.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressDepartmentNameCode() {
        return postalAddressDepartmentNameCode;
    }
    /**
     * @param postalAddressDepartmentNameCode The postalAddressDepartmentNameCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressDepartmentNameCode(
            String postalAddressDepartmentNameCode) {
        this.postalAddressDepartmentNameCode = postalAddressDepartmentNameCode;
    }
    /**
     * @return Returns the postalAddressFloorNumber.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressFloorNumber() {
        return postalAddressFloorNumber;
    }
    /**
     * @param postalAddressFloorNumber The postalAddressFloorNumber to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressFloorNumber(String postalAddressFloorNumber) {
        this.postalAddressFloorNumber = postalAddressFloorNumber;
    }
    /**
     * @return Returns the postalAddressGeographyCode.
     * @exception
     * @since 1.0
     */
    public short getPostalAddressGeographyCode() {
        return postalAddressGeographyCode;
    }
    /**
     * @param postalAddressGeographyCode The postalAddressGeographyCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressGeographyCode(
            short postalAddressGeographyCode) {
        this.postalAddressGeographyCode = postalAddressGeographyCode;
    }
    /**
     * @return Returns the postalAddressIntlnQualigyDescriptor.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressIntlnQualigyDescriptor() {
        return postalAddressIntlnQualigyDescriptor;
    }
    /**
     * @param postalAddressIntlnQualigyDescriptor The postalAddressIntlnQualigyDescriptor to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressIntlnQualigyDescriptor(
            String postalAddressIntlnQualigyDescriptor) {
        this.postalAddressIntlnQualigyDescriptor = postalAddressIntlnQualigyDescriptor;
    }
    /**
     * @return Returns the postalAddressIntlnTypeCode.
     * @exception
     * @since 1.0
     */
    public short getPostalAddressIntlnTypeCode() {
        return postalAddressIntlnTypeCode;
    }
    /**
     * @param postalAddressIntlnTypeCode The postalAddressIntlnTypeCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressIntlnTypeCode(
            short postalAddressIntlnTypeCode) {
        this.postalAddressIntlnTypeCode = postalAddressIntlnTypeCode;
    }
    /**
     * @return Returns the postalAddressMdeCode.
     * @exception
     * @since 1.0
     */
    public short getPostalAddressMdeCode() {
        return postalAddressMdeCode;
    }
    /**
     * @param postalAddressMdeCode The postalAddressMdeCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressMdeCode(short postalAddressMdeCode) {
        this.postalAddressMdeCode = postalAddressMdeCode;
    }
    /**
     * @return Returns the postalAddressOrganisationName.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressOrganisationName() {
        return postalAddressOrganisationName;
    }
    /**
     * @param postalAddressOrganisationName The postalAddressOrganisationName to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressOrganisationName(
            String postalAddressOrganisationName) {
        this.postalAddressOrganisationName = postalAddressOrganisationName;
    }
    /**
     * @return Returns the postalAddressPersonalAttentionName.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressPersonalAttentionName() {
        return postalAddressPersonalAttentionName;
    }
    /**
     * @param postalAddressPersonalAttentionName The postalAddressPersonalAttentionName to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressPersonalAttentionName(
            String postalAddressPersonalAttentionName) {
        this.postalAddressPersonalAttentionName = postalAddressPersonalAttentionName;
    }
    /**
     * @return Returns the postalAddressPostalCode.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressPostalCode() {
        return postalAddressPostalCode;
    }
    /**
     * @param postalAddressPostalCode The postalAddressPostalCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressPostalCode(String postalAddressPostalCode) {
        this.postalAddressPostalCode = postalAddressPostalCode;
    }
    /**
     * @return Returns the postalAddressProvincialTerritoryStateCode.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressProvincialTerritoryStateCode() {
        return postalAddressProvincialTerritoryStateCode;
    }
    /**
     * @param postalAddressProvincialTerritoryStateCode The postalAddressProvincialTerritoryStateCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressProvincialTerritoryStateCode(
            String postalAddressProvincialTerritoryStateCode) {
        this.postalAddressProvincialTerritoryStateCode = postalAddressProvincialTerritoryStateCode;
    }
    /**
     * @return Returns the postalAddressReferenceDescriptor.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressReferenceDescriptor() {
        return postalAddressReferenceDescriptor;
    }
    /**
     * @param postalAddressReferenceDescriptor The postalAddressReferenceDescriptor to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressReferenceDescriptor(
            String postalAddressReferenceDescriptor) {
        this.postalAddressReferenceDescriptor = postalAddressReferenceDescriptor;
    }
    /**
     * @return Returns the postalAddressRegionCode.
     * @exception
     * @since 1.0
     */
    public short getPostalAddressRegionCode() {
        return postalAddressRegionCode;
    }
    /**
     * @param postalAddressRegionCode The postalAddressRegionCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressRegionCode(
            short postalAddressRegionCode) {
        this.postalAddressRegionCode = postalAddressRegionCode;
    }
    /**
     * @return Returns the postalAddressRoomNumber.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressRoomNumber() {
        return postalAddressRoomNumber;
    }
    /**
     * @param postalAddressRoomNumber The postalAddressRoomNumber to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressRoomNumber(String postalAddressRoomNumber) {
        this.postalAddressRoomNumber = postalAddressRoomNumber;
    }
    /**
     * @return Returns the postalAddressStreetDirectionCode.
     * @exception
     * @since 1.0
     */
    public short getPostalAddressStreetDirectionCode() {
        return postalAddressStreetDirectionCode;
    }
    /**
     * @param postalAddressStreetDirectionCode The postalAddressStreetDirectionCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressStreetDirectionCode(
            short postalAddressStreetDirectionCode) {
        this.postalAddressStreetDirectionCode = postalAddressStreetDirectionCode;
    }
    /**
     * @return Returns the postalAddressStreetName.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressStreetName() {
        return postalAddressStreetName;
    }
    /**
     * @param postalAddressStreetName The postalAddressStreetName to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressStreetName(String postalAddressStreetName) {
        this.postalAddressStreetName = postalAddressStreetName;
    }
    /**
     * @return Returns the postalAddressStreetNumber.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressStreetNumber() {
        return postalAddressStreetNumber;
    }
    /**
     * @param postalAddressStreetNumber The postalAddressStreetNumber to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressStreetNumber(String postalAddressStreetNumber) {
        this.postalAddressStreetNumber = postalAddressStreetNumber;
    }
    /**
     * @return Returns the postalAddressStreetTypeCode.
     * @exception
     * @since 1.0
     */
    public short getPostalAddressStreetTypeCode() {
        return postalAddressStreetTypeCode;
    }
    /**
     * @param postalAddressStreetTypeCode The postalAddressStreetTypeCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressStreetTypeCode(
            short postalAddressStreetTypeCode) {
        this.postalAddressStreetTypeCode = postalAddressStreetTypeCode;
    }
    /**
     * @return Returns the postalAddressUnitDsgtrCode.
     * @exception
     * @since 1.0
     */
    public short getPostalAddressUnitDsgtrCode() {
        return postalAddressUnitDsgtrCode;
    }
    /**
     * @param postalAddressUnitDsgtrCode The postalAddressUnitDsgtrCode to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressUnitDsgtrCode(
            short postalAddressUnitDsgtrCode) {
        this.postalAddressUnitDsgtrCode = postalAddressUnitDsgtrCode;
    }
    /**
     * @return Returns the postalAddressUnitDsgtrNumber.
     * @exception
     * @since 1.0
     */
    public String getPostalAddressUnitDsgtrNumber() {
        return postalAddressUnitDsgtrNumber;
    }
    /**
     * @param postalAddressUnitDsgtrNumber The postalAddressUnitDsgtrNumber to set.
     * @exception
     * @since 1.0
     */
    public void setPostalAddressUnitDsgtrNumber(
            String postalAddressUnitDsgtrNumber) {
        this.postalAddressUnitDsgtrNumber = postalAddressUnitDsgtrNumber;
    }
}
