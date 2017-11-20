/*
 * Created on May 13, 2004
 * 
 * ColgateLoginResponse.java
 */
package org.folio.cataloging.business.colgate;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.folio.cataloging.business.authentication.AuthenticationException;
import org.folio.cataloging.business.authentication.PasswordExpiredException;
import org.folio.cataloging.business.authentication.TooManyUsersException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.SocketMessage;

/**
 * Represents a Login Response structure from Colgate
 * 
 * @author paulm
 */
public class ColgateLoginResponse extends SocketMessage {
	private static Log logger = LogFactory.getLog(ColgateLoginResponse.class);
	private static int expectedLength = 44;
	private static byte type = 57;
	public int errorNumber;
	public byte[] encodedPassword;

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.business.ColgateMessage#fromByteArray(byte[])
	 */
	public void fromByteArray(byte[] msg) throws IOException {
		if (msg.length != expectedLength) {
			logger.warn("Wrong length message returned from Colgate");
			throw new IOException("Wrong length message returned from Colgate");
		}
		
		if (msg[0] != getType()) {
			logger.warn("Unexpected message type received from Colgate");
			throw new IOException("Unexpected message type received from Colgate");
		}
		
		ByteArrayInputStream bs = new ByteArrayInputStream(msg);
		DataInputStream ds = new DataInputStream(bs);
		ds.skipBytes(4);
		setErrorNumber(ds.readInt());
		logger.info("Colgate error number is :" + getErrorNumber());
		byte[] encodedPassword = new byte[33];
		ds.read(encodedPassword, 0, 33);
		setEncodedPassword(encodedPassword);
	}

	/**
	 * Examines the returned integer from Colgate and throws appropriate exceptions
	 * 
	 * 
	 * @throws AuthenticationException
	 * concurrent users
	 */
	public void testExceptions() throws AuthenticationException {
		switch (getErrorNumber()) {
			case 0:
				return;
			case 9:
				throw new PasswordExpiredException("Colgate returned error number 9");
			case 13:
				throw new TooManyUsersException("Colgate returned error number 13");
			default:
				throw new AuthenticationException("Colgate returned error number: " + getErrorNumber());    			
		}
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.business.ColgateMessage#asByteArray()
	 */
	public byte[] asByteArray() {
		return null;
	}

	/**
	 * @return the type of this message
	 */
	public static byte getType() {
		return type;
	}

	/**
	 * @return the encodedPassword member for this message
	 */
	public byte[] getEncodedPassword() {
		return encodedPassword;
	}

	/**
	 * @return the errorNumber member for this message
	 */
	public int getErrorNumber() {
		return errorNumber;
	}

	/**
	 * sets the encodedPassword member for this message
	 */
	public void setEncodedPassword(byte[] bs) {
		encodedPassword = bs;
	}

	/**
	 * sets the errorNumber member for this message
	 */
	public void setErrorNumber(int e) {
		errorNumber = e;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.SocketMessage#isMessageComplete(byte[])
	 */
	public boolean isMessageComplete(byte[] b) {
		return b.length >= expectedLength;
	}

}
