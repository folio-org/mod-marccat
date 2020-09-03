package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import static org.folio.marccat.config.constants.Global.EMPTY_STRING;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class RemoteSensingImage extends PhysicalDescription {
  private char altitudeOfSensorCode;
  private char attitudeOfSensorCode;
  private char cloudCoverCode;
  private char platformConstructionTypeCode;
  private char platformUseCode;
  private char sensorTypeCode;
  private String dataTypeCode;

  public RemoteSensingImage() {
    super();
    setHeaderType(43);
  }

  /* (non-Javadoc)
   * @see FixedField#getDisplayString()
   */
  @Override
  public String getDisplayString() {
    return EMPTY_STRING
      + getGeneralMaterialDesignationCode()
      + getSpecificMaterialDesignationCode()
      + " "
      + getAltitudeOfSensorCode()
      + getAttitudeOfSensorCode()
      + getCloudCoverCode()
      + getPlatformConstructionTypeCode()
      + getPlatformUseCode()
      + getSensorTypeCode()
      + getDataTypeCode();
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  @Override
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("XB", session));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isRemoteSensingImage()
   */
  @Override
  public boolean isRemoteSensingImage() {
    return true;
  }


  public char getAltitudeOfSensorCode() {
    return altitudeOfSensorCode;
  }


  public void setAltitudeOfSensorCode(char c) {
    altitudeOfSensorCode = c;
  }


  public char getAttitudeOfSensorCode() {
    return attitudeOfSensorCode;
  }


  public void setAttitudeOfSensorCode(char c) {
    attitudeOfSensorCode = c;
  }


  public char getCloudCoverCode() {
    return cloudCoverCode;
  }


  public void setCloudCoverCode(char c) {
    cloudCoverCode = c;
  }


  public String getDataTypeCode() {
    return dataTypeCode;
  }


  public void setDataTypeCode(String string) {
    dataTypeCode = string;
  }


  public char getPlatformConstructionTypeCode() {
    return platformConstructionTypeCode;
  }


  public void setPlatformConstructionTypeCode(char c) {
    platformConstructionTypeCode = c;
  }


  public char getPlatformUseCode() {
    return platformUseCode;
  }


  public void setPlatformUseCode(char c) {
    platformUseCode = c;
  }


  public char getSensorTypeCode() {
    return sensorTypeCode;
  }


  public void setSensorTypeCode(char c) {
    sensorTypeCode = c;
  }



  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (s.length() > 3) setAltitudeOfSensorCode(s.charAt(3));
    if (s.length() > 4) setAttitudeOfSensorCode(s.charAt(4));
    if (s.length() > 5) setCloudCoverCode(s.charAt(5));
    if (s.length() > 6) setPlatformConstructionTypeCode(s.charAt(6));
    if (s.length() > 7) setPlatformUseCode(s.charAt(7));
    if (s.length() > 8) setSensorTypeCode(s.charAt(8));
    if (s.length() > 10) setDataTypeCode(s.substring(9, 11));
  }

}
