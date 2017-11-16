/*
 * (c) LibriCore
 * 
 * Created on Jun 22, 2006
 * 
 * FindResponse.java
 */
package librisuite.business.amicusSearchEngine;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import librisuite.business.common.SocketMessage;
import librisuite.business.exception.ConnectException;
import librisuite.business.exception.LibrisuiteException;
import librisuite.business.exception.OperatorNotSupportedException;
import librisuite.business.exception.PrimaryNotCompatibleException;
import librisuite.business.exception.QueryNotSupportedException;
import librisuite.business.exception.QueryParsingException;
import librisuite.business.exception.ResourceLimitsExceededException;
import librisuite.business.exception.SearchFailedException;
import librisuite.business.exception.TooManyResultsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2007/04/09 09:58:09 $
 * @since 1.0
 */
public class FindResponse extends SocketMessage {
	private static final Log logger = LogFactory.getLog(FindResponse.class);
	private int returnCode;
	private int resultCount;
	private int[] records;

	public void checkReturnCode() throws LibrisuiteException {
		logger.warn(
			"Got return code " + getReturnCode() + " from searchEngine");
		switch (getReturnCode()) {
			case 0:
				return;
			case 1:
			case 2:
				throw new TooManyResultsException();
			case -4 :
			case -7 :
			case -8 :
				throw new QueryParsingException();
			case -5 :
				throw new ConnectException();
			case -2 :
				throw new QueryNotSupportedException();
			case -6 :
				throw new OperatorNotSupportedException();
			case -9 :
				throw new SearchFailedException();
			case -10 :
				throw new PrimaryNotCompatibleException();
			case -11 :
				throw new ResourceLimitsExceededException();
			default :
				throw new SearchFailedException();
		}
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.SocketMessage#asByteArray()
	 */
	public byte[] asByteArray() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.SocketMessage#fromByteArray(byte[])
	 */
	public void fromByteArray(byte[] msg) throws IOException {
		ByteArrayInputStream bs = new ByteArrayInputStream(msg);
		DataInputStream ds = new DataInputStream(bs);
		BufferedReader br =
			new BufferedReader(new InputStreamReader(bs, "US-ASCII"));

		try {
			String line = br.readLine(); // NLSEC/1.0 <returnCode> ...
			String[] tokens = line.split(" ");
			setReturnCode(Integer.parseInt(tokens[1]));

			line = br.readLine(); // content-length 
			tokens = line.split(" ");
			int contentLength = Integer.parseInt(tokens[1]);

			line = br.readLine(); // RPN-hits: <count>
			tokens = line.split(" ");
			setResultCount(Integer.parseInt(tokens[1]));

			/* BufferedReader is a convenient tool for accessing the header info and
			 * it is the only tool that provides a readLine with Charset support.
			 * However, the buffered aspect means that the underlying stream may already
			 * be in the buffer and not accessible for the binary portion of the message.
			 * Hence, after parsing the required header information, we re-open the
			 * DataInputStream to process the binary data
			 */

			bs.reset();
			ds = new DataInputStream(bs);
			ds.skipBytes(msg.length - contentLength);

			if (getResultCount() > 0) {
				setRecords(new int[getResultCount()]);
				for (int i = 0; i < getResultCount(); i++) {
					getRecords()[i] = ds.readInt();
				}
			}
		} catch (Exception e) {
			throw new IOException();
		}
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.SocketMessage#isMessageComplete(byte[])
	 */
	public boolean isMessageComplete(byte[] b) {
		try {
			String s = new String(b, "US-ASCII");
		//	logger.debug(s);
			return s.endsWith("</RPN>");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("US-ASCII is not supported???");
		}
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int[] getRecords() {
		return records;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getResultCount() {
		return resultCount;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getReturnCode() {
		return returnCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setRecords(int[] is) {
		records = is;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setResultCount(int i) {
		resultCount = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setReturnCode(int i) {
		returnCode = i;
	}

}
