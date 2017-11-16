/*
 * (c) LibriCore
 * 
 * Created on Jun 11, 2004
 * 
 * ColgateChangePasswordRequest.java
 */
package librisuite.business.colgate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import librisuite.business.common.SocketMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * maps to the data structure used by Colgate to request a change of password
 * 
 * @author paulm
 */
public class ColgateChangePasswordRequest extends SocketMessage {
	static private Log logger = LogFactory.getLog(ColgateChangePasswordRequest.class);
	static private byte[] type = {58};
	private byte[] name;
	private byte[] oldPassword;
	private byte[] newPassword;
	
	/**
	 * 
	 * Class constructor
	 *
	 * @param name - byte array of ISO-Latin-1 bytes representing user name
	 * @param oldPassword - byte array of md5 , hex representation of old password
	 * @param newPassword - byte array of md5, hex representation of new password
	 */
	public ColgateChangePasswordRequest(byte[] name, byte[] oldPassword, byte[] newPassword) {
		setName(name);
		setOldPassword(oldPassword);
		setNewPassword(newPassword);
	}

	/**
	 * 
	 * Class constructor
	 *
	 * @param name - string of user name
	 * @param clearPassword - string of old password in clear text
	 * @param clearNewPassword - string of new password in clear text
	 */
	public ColgateChangePasswordRequest(String name, String clearPassword, String clearNewPassword) {
		setName(Colgate.asIsoByte(name));
		setOldPassword(Colgate.encryptPassword(clearPassword));
		setNewPassword(Colgate.encryptPassword(clearNewPassword));
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.business.ColgateMessage#asByteArray()
	 */
	public byte[] asByteArray() throws IOException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		
		s.write(getType());
		s.write(getName());
		s.write(getOldPassword());
		s.write(getNewPassword());
		return s.toByteArray();
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.business.ColgateMessage#fromByteArray(byte[])
	 */
	public void fromByteArray(byte[] msg) throws IOException {
	}

	/**
	 * @return the byte representing the colgate message type
	 */
	public static byte[] getType() {
		return type;
	}

	/**
	 * @return byte array representing the user name encoded in Iso-Latin-1
	 */
	public byte[] getName() {
		return name;
	}

	/**
	 * @return byte array of md5 and hex encoded new password
	 */
	public byte[] getNewPassword() {
		return newPassword;
	}

	/**
	 * @return byte array of md5 and hex encoded old password
	 */
	public byte[] getOldPassword() {
		return oldPassword;
	}

	/**
	 * sets the name member (pads byte array to length of 33)
	 */
	public void setName(byte [] bs) {
		name = SocketMessage.pad(bs, 33);
	}

	/**
	 * sets the type member
	 */
	public static void setType(byte[] bs) {
		type = bs;
	}

	/**
	 * sets the newPassword member (pads to length 33)
	 */
	public void setNewPassword(byte[] bs) {
		newPassword = SocketMessage.pad(bs, 33);
	}

	/**
	 * sets the oldPassword member (pads to length 33)
	 */
	public void setOldPassword(byte[] bs) {
		oldPassword = SocketMessage.pad(bs, 33);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.SocketMessage#isMessageComplete(byte[])
	 */
	public boolean isMessageComplete(byte[] b) {
		// TODO Auto-generated method stub
		return false;
	}

}
