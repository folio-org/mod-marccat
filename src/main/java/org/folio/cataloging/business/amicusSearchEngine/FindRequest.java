/*
 * (c) LibriCore
 *
 * Created on Jun 16, 2006
 *
 * FindRequest.java
 */
package org.folio.cataloging.business.amicusSearchEngine;

import org.folio.cataloging.business.authorisation.AuthorisationException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.common.SocketMessage;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.dao.common.HibernateUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2007/04/09 09:58:09 $
 * @since 1.0
 */
public class FindRequest extends SocketMessage {
  public static final int auth = Defaults.getInteger("default.mades.auth.level");
  public static final int lang = Defaults.getInteger("amicus.searchEngine.language1");
  private static String TRAILER = "</RPN>";
  private static int maxTempRecords;

  static {
		/*try {
			maxTempRecords = Integer.parseInt(new DAOGlobalVariable().getValueByName("ir_max_temp_rec_cnt"));
		} catch (Exception e) {*/
    maxTempRecords = 20000;
    //}
  }

  private String cclQuery;
  private Locale locale;
  private int searchingView;
  private UserProfile userProfile;

  public FindRequest(
    String cclQuery,
    Locale locale,
    int searchingView,
    UserProfile userProfile) {
    setCclQuery(cclQuery);
    setLocale(locale);
    setSearchingView(searchingView);
    setUserProfile(userProfile);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.SocketMessage#asByteArray()
   */
  public byte[] asByteArray() throws IOException {
    ByteArrayOutputStream bs = new ByteArrayOutputStream();
    DataOutputStream s = new DataOutputStream(bs);
    s.writeBytes("CCLFIND ");
    s.writeBytes("/u=" + userProfile.getName().trim());
    s.writeBytes("/s=" + new HibernateUtil().getUniqueSessionId());
    s.writeBytes("/Max=" + userProfile.getMaxRecordCount());
    s.writeBytes("/Temp=" + maxTempRecords);
    s.writeBytes("/Secauth=");
    try {
      userProfile.getAuthorisationAgent().checkPermission(
        "secondarySearching");
      s.writeBytes("1");
    } catch (AuthorisationException e) {
      s.writeBytes("0");
    }
    s.writeBytes("/Madauth=" + auth); //TODO replace with PrimeSource coding when available
    s.writeBytes("/Lang=");
		/*s.writeBytes(
			locale.getLanguage().equals(
				Defaults.getString("amicus.codeTable.language1")) ? "0" : "1");*/
    s.writeBytes("" + lang);

    s.writeBytes("/Bill=0");
    s.writeBytes("/View=" + searchingView);
    s.writeBytes("/Org=" + userProfile.getMainLibrary());
    s.writeBytes("/Charset=2");
    s.writeBytes("\r\n");
    byte[] textBuffer = cclQuery.getBytes("UTF-8");
    s.writeBytes("Text-length: " + textBuffer.length + "\r\n");
    //TODO include result sets
    int contentLength = textBuffer.length + TRAILER.length() + 4;
    s.writeBytes("Content-length: " + contentLength);
    s.writeBytes("\r\n\r\n");

    s.write(textBuffer);
    s.writeInt(0); // no result sets yet
    s.writeBytes(TRAILER);  // message trailer
    return bs.toByteArray();
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.SocketMessage#fromByteArray(byte[])
   */
  public void fromByteArray(byte[] msg) throws IOException {
    // TODO Auto-generated method stub

  }

  /**
   * @since 1.0
   */
  public String getCclQuery() {
    return cclQuery;
  }

  /**
   * @since 1.0
   */
  public void setCclQuery(String string) {
    cclQuery = string;
  }

  /**
   * @since 1.0
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * @since 1.0
   */
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  /**
   * @since 1.0
   */
  public int getSearchingView() {
    return searchingView;
  }

  /**
   * @since 1.0
   */
  public void setSearchingView(int i) {
    searchingView = i;
  }

  /**
   * @since 1.0
   */
  public UserProfile getUserProfile() {
    return userProfile;
  }

  /**
   * @since 1.0
   */
  public void setUserProfile(UserProfile profile) {
    userProfile = profile;
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.SocketMessage#isMessageComplete(byte[])
   */
  public boolean isMessageComplete(byte[] b) {
    // TODO Auto-generated method stub
    return false;
  }

}
