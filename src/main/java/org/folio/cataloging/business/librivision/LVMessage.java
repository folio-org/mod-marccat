/*
 * (c) LibriCore
 * 
 * Created on Jun 7, 2004
 * 
 * LVMessage.java
 */
package org.folio.cataloging.business.librivision;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.searching.SearchEngine;
import org.folio.cataloging.dao.DAOSortCriteriaDetails;
import org.folio.cataloging.dao.persistence.SortCriteriaDetails;
import org.folio.cataloging.exception.ModCatalogingException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * This class contains methods for calling LibriVision actions
 * 
 * @author Wim Crols
 * @version $Revision: 1.31 $, $Date: 2006/07/11 08:01:05 $
 * @since 1.0
 */
public class LVMessage {

	private static final Log logger = LogFactory.getLog(LVMessage.class);

	private static final int MSG_TYPE_NONE = 0;
	private static final int MSG_TYPE_PROCESS_MANAGER_ADMIN = 1 << 0;
	private static final int MSG_TYPE_PROCESS_MANAGER_SESSION = 1 << 1;
	private static final int MSG_TYPE_SESSIONDAEMON_ADMIN = 1 << 2;
	private static final int MSG_TYPE_SESSIONDAEMON_SESSION = 1 << 3;
	private static final int MSG_TYPE_Z3950_ADMIN = 1 << 4;
	private static final int MSG_TYPE_Z3950_SESSION = 1 << 5;
	private static final int MSG_TYPE_TUBE_ADMIN = 1 << 6;
	private static final int MSG_TYPE_TUBE_SESSION = 1 << 7;
	private static final int MSG_TYPE_TEST = 1 << 8;
	private static final int MSG_TYPE_SEARCH_ENGINE_DAEMON = 1 << 9;
	private static final int MSG_TYPE_SEARCH_ENGINE_PROCESS = 1 << 10;
	private static final int MSG_TYPE_SEARCH_ENGINE_THREAD = 1 << 11;
	private static final int MSG_TYPE_LISTEN = 1 << 12;
	private static final int MSG_TYPE_CLIENT = 1 << 13;
	private static final int MSG_TYPE_SERVER = 1 << 14;
	private static final int MSG_TYPE_HOLDINGS = 1 << 15;
	private static final int MSG_TYPE_TCP_IP = 1 << 31;

	private static final int MSG_HEADER_NONE = 0x01010101;
	private static final int MSG_HEADER_START_PROCESS_MANAGER = 0x01010102;
	private static final int MSG_HEADER_START_SESSION = 0x01010103;
	private static final int MSG_HEADER_STOP_PROCESS_MANAGER = 0x01010104;
	private static final int MSG_HEADER_SESSION_THREAD_STOPPED = 0x01010105;
	private static final int MSG_HEADER_SESSION_PROCESS_STOPPED = 0x01010106;
	private static final int MSG_HEADER_SHOW_SESSION_INFO = 0x01010108;
	private static final int MSG_HEADER_AUTHORISATION = 0x02010101;
	private static final int MSG_HEADER_LV_START_THREAD = 0x02010111;
	private static final int MSG_HEADER_LV_STOP_PROCESS = 0x02010112;
	private static final int MSG_HEADER_LV_ACTIVE_SESSIONS = 0x02010113;
	private static final int MSG_HEADER_LV_EXIT = 0x020101ff;
	private static final int MSG_HEADER_TUBE_START = 0x04010101;
	private static final int MSG_HEADER_TUBE_STOP = 0x04010102;
	private static final int MSG_HEADER_TUBE_QUERY_INIT = 0x04010103;
	private static final int MSG_HEADER_TUBE_QUERY_RESPONSE = 0x04010104;
	private static final int MSG_HEADER_TUBE_PING = 0x04010105;
	private static final int MSG_HEADER_TUBE_PONG = 0x04010106;
	private static final int MSG_HEADER_LIBRIVISION = 0x07000001;
	private static final int MSG_HEADER_HELLO = 0x1e010101;
	private static final int MSG_HEADER_COMMUNICATION = 0x4fffffff;

	private static final int PRS_NONE = 0;
	private static final int PRS_UNIMARC = 1;
	private static final int PRS_INTERMARC = 2;
	private static final int PRS_CCF = 3;
	private static final int PRS_USMARC = 10;
	private static final int PRS_UKMARC = 11;
	private static final int PRS_NORMARC = 12;
	private static final int PRS_LIBRISMARC = 13;
	private static final int PRS_DANMARC = 14;
	private static final int PRS_FINMARC = 15;
	private static final int PRS_MAB = 16;
	private static final int PRS_CANMARC = 17;
	private static final int PRS_SBN = 18;
	private static final int PRS_PICAMARC = 19;
	private static final int PRS_AUSMARC = 20;
	private static final int PRS_IBERMARC = 21;
	private static final int PRS_CATMARC = 22;
	private static final int PRS_MALMARC = 23;
	private static final int PRS_SUTRS = 101;
	private static final int PRS_GRS1 = 105;
	private static final int PRS_BLMARC = 1001;
	private static final int PRS_NSD = 10001;
	private static final int PRS_SD = 10002;
	private static final int PRS_OCTET = 10003;

	private Socket client_socket = null;
	private ServerSocket server_socket = null;
	private InputStream socket_input_stream = null;
	private OutputStream socket_output_stream = null;
	private byte[] snd_buffer = null;
	private byte[] rcv_buffer = null;
	private int snd_max_length = 0;
	private int rcv_max_length = 0;
	private int snd_length = 0;
	private int rcv_length = 0;
	private int rcv_offset = 0;
	private int socket_ip_port = 0;
	private InetAddress socket_ip_address = null;
	private static final SearchEngine searchEngine = null;

	private static ThreadLocal lvSessionId = new ThreadLocal() {
		protected synchronized Object initialValue() {
			return null;
		}
	};

	public static String getLVSessionId() {
		return (String) lvSessionId.get();
	}

	public static void setLVSessionId(String s) {
		lvSessionId.set(s);
	}

	private static String LVServerName =
		Defaults.getString("librivision.server.name");
	private static int LVServerPort =
		Defaults.getInteger("librivision.server.port");

	public LVMessage(String host, int port) throws IOException {
		this.socket_ip_port = 12345;
		this.socket_ip_address = InetAddress.getLocalHost();

		if ((host != null) && (InetAddress.getByName(host) != null)) {
			this.socket_ip_address = InetAddress.getByName(host);
		}

		if (port > 0) {
			this.socket_ip_port = port;
		}
	}

	private int byte_to_int(byte[] src) {
		int dst = 0;

		dst = (src[0] & 0xff);
		dst <<= 8;
		dst |= (src[1] & 0xff);
		dst <<= 8;
		dst |= (src[2] & 0xff);
		dst <<= 8;
		dst |= (src[3] & 0xff);

		return dst;
	}

	public int read_long() {
		byte[] buffer_char = new byte[4];
		int buffer_long = 0;

		if (this.read(buffer_char, 4) < 4) {
			return -1;
		}
		buffer_long = this.byte_to_int(buffer_char);

		return buffer_long;
	}

	public void write(int buffer_long) {
		byte[] buffer_char = new byte[4];

		buffer_char = this.int_to_byte(buffer_long);
		this.write(buffer_char, 4);
	}

	private byte[] int_to_byte(int src) {
		byte[] dst = new byte[4];

		dst[3] = (byte) (src & 0x000000ff);
		src >>= 8;
		dst[2] = (byte) (src & 0x000000ff);
		src >>= 8;
		dst[1] = (byte) (src & 0x000000ff);
		src >>= 8;
		dst[0] = (byte) (src & 0x000000ff);

		return dst;
	}

	public void write(String name, String value) {
		if (name != null) {
			this.write(name);
		} else {
			this.write("");
		}

		if (value != null) {
			this.write(value);
		} else {
			this.write("");
		}
	}

	public void write(String name, int value) {
		this.write(name, new String("" + value));
	}

	public void write(String buffer) {
		//	int buffer_length = buffer.length();
		byte[] buffer2;
		try {
			buffer2 = buffer.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		//		for (int klm = 0; klm < buffer_length; klm++) {
		//			buffer2[klm] = (byte) buffer.charAt(klm);
		//		}

		this.write(buffer2.length);
		this.write(buffer2, buffer2.length);
	}

	public void write(byte[] buffer, int length) {
		if (this.snd_buffer != null) {
			if ((this.snd_max_length - this.snd_length) < length) {
				byte[] snd_old = this.snd_buffer;
				this.snd_buffer = new byte[this.snd_max_length << 1];
				for (int klm = 0; klm < this.snd_length; klm++) {
					this.snd_buffer[klm] = snd_old[klm];
				}
				snd_old = null;
				this.snd_max_length = this.snd_max_length << 1;
				this.write(buffer, length);
			} else {
				for (int klm = 0; klm < length; klm++) {
					this.snd_buffer[klm + this.snd_length] = buffer[klm];
				}
				this.snd_length += length;
			}
		} else {
			this.snd_buffer = new byte[1024];
			this.snd_max_length = 1024;
			this.snd_length = 0;
			this.write(buffer, length);
		}
	}

	public String read_string() {
		byte[] buffer = null;
		long buffer_length = 0;

		buffer_length = this.read_long();
		buffer = new byte[(int) buffer_length];
		this.read(buffer, buffer_length);
		String buffer2 = new String(buffer);

		return buffer2;
	}

	private long read(byte[] buffer, long length) {
		int available_octets = 0;

		available_octets = this.rcv_length - this.rcv_offset;
		if (available_octets < length) {
			for (int klm = 0; klm < available_octets; klm++) {
				buffer[klm] = this.rcv_buffer[klm + this.rcv_offset];
			}
			this.rcv_offset = this.rcv_length;
			return (available_octets);
		} else {
			for (int klm = 0; klm < length; klm++) {
				buffer[klm] = this.rcv_buffer[klm + this.rcv_offset];
			}
			this.rcv_offset += length;
			return length;
		}
	}

	public void write(Hashtable hashtable) {
		if (hashtable != null) {
			for (Enumeration e = hashtable.keys(); e.hasMoreElements();) {
				Object element = e.nextElement();
				this.write(
					element.toString(),
					hashtable.get(element).toString());
			}
		}
	}

	private Hashtable read_name_value() {
//		logger.info("enter read_name_value()");
		Hashtable hashtable = new Hashtable();
		int name_length = 0;
		int value_length = 0;

		name_length = this.read_long();
		if (name_length == -1) {
			return null;
		}
		byte[] name = new byte[name_length];
		if ((this.read(name, name_length) == 0) && (name_length != 0)) {
			return null;
		}

		value_length = this.read_long();
		if (value_length == -1) {
			return null;
		}
		byte[] value = new byte[value_length];
		if ((this.read(value, value_length) == 0) && (value_length != 0)) {
			return null;
		}

		try {
			hashtable.put("NAME", new String(name, "ISO8859-1"));
			hashtable.put("VALUE", new String(value, "ISO8859-1"));

//			logger.info(
//				"name / stringValue = "
//					+ new String(name, "ISO8859-1")
//					+ " / "
//					+ new String(stringValue, "ISO8859-1"));
		} catch (Exception exception) {
		}

//		logger.info("exit read_name_value()");
		return hashtable;
	}

	private Hashtable readHashTable() {
//		logger.info("enter readHashTable()");
		Hashtable line = null;
		Hashtable hashtable = new Hashtable();

		this.get();
		line = this.read_name_value();
		while (line != null) {
			Object name = line.get("NAME");
			Object value = line.get("VALUE");
			hashtable.put(name, value);
			line = this.read_name_value();
		}
		this.close();

//		logger.info("exit readHashTable()");
		return hashtable;
	}

	private int get() {
//		logger.info("enter get()");
		int rcv_new_max_length = 0;
		int receive_length = 0;
		byte[] rcv_old = null;
		byte[] rcv_new = null;
		int read_bytes = 0;
		byte[] rcv_char_length = new byte[4];

		if (this.rcv_buffer == null) {
			this.rcv_buffer = new byte[1024];
			this.rcv_max_length = 1024;
			this.rcv_length = 0;
			return this.get();
		} else {
			try {
				read_bytes =
					this.socket_input_stream.read(rcv_char_length, 0, 4);
			} catch (Exception exception) {
				logger.debug(exception.getMessage());
			}
			if (read_bytes == -1) {
				return -1;
			} else if (read_bytes != 4) {
				return -2;
			}
			receive_length = this.byte_to_int(rcv_char_length);
			rcv_new_max_length = this.rcv_max_length;
			while (rcv_new_max_length < receive_length) {
				rcv_new_max_length = rcv_new_max_length << 1;
			}
			if (rcv_new_max_length > this.rcv_max_length) {
				rcv_old = this.rcv_buffer;
				rcv_new = new byte[rcv_new_max_length];
				this.rcv_buffer = rcv_new;
				this.rcv_length = 0;
				this.rcv_max_length = rcv_new_max_length;
			}
			this.rcv_length = 0;
			while (receive_length > 352) {
				try {
					read_bytes =
						this.socket_input_stream.read(
							this.rcv_buffer,
							this.rcv_length,
							352);
				} catch (Exception exception) {
					System.err.println(exception.getMessage());
				}
				if (read_bytes == -1) {
					return -3;
				} else if (read_bytes != 352) {
					//System.err.println ("UUGGHH read_bytes = " + read_bytes);
					//return -4;
				}
				this.rcv_length += read_bytes;
				receive_length -= read_bytes;
			}

			try {
				read_bytes =
					this.socket_input_stream.read(
						this.rcv_buffer,
						this.rcv_length,
						receive_length);
			} catch (Exception exception) {
				System.err.println(exception.getMessage());
			}
			if (read_bytes == -1) {
				return -5;
			}

			this.rcv_length += read_bytes;
			receive_length -= read_bytes;

			while (receive_length > 0) {
				try {
					read_bytes =
						this.socket_input_stream.read(
							this.rcv_buffer,
							this.rcv_length,
							receive_length);
				} catch (Exception exception) {
					System.err.println(exception.getMessage());
				}
				if (read_bytes == -1) {
					return -6;
				}
				this.rcv_length += read_bytes;
				receive_length -= read_bytes;
			}

			this.rcv_offset = 0;

			return this.rcv_length;
		}
	}

	public int send() {
		byte[] snd_char_length = new byte[4];

		snd_char_length = this.int_to_byte(this.snd_length);

		try {
			this.socket_output_stream.write(snd_char_length, 0, 4);
			this.socket_output_stream.flush();
		} catch (Exception exception) {
			System.err.println(exception.getMessage());
		}

		while (this.snd_length > 352) {
			try {
				this.socket_output_stream.write(this.snd_buffer, 0, 352);
				this.socket_output_stream.flush();
			} catch (Exception exception) {
				System.err.println(exception.getMessage());
			}
			for (int klm = 0; klm < this.snd_length - 352; klm++) {
				this.snd_buffer[klm] = this.snd_buffer[klm + 352];
			}
			this.snd_length -= 352;
		}
		try {
			this.socket_output_stream.write(
				this.snd_buffer,
				0,
				this.snd_length);
			this.socket_output_stream.flush();
		} catch (Exception exception) {
			System.err.println(exception.getMessage());
		}
		this.snd_length = 0;
		return 0;
	}

	public void open() throws IOException {
		this.client_socket =
			new Socket(this.socket_ip_address, this.socket_ip_port);
		socket_input_stream = client_socket.getInputStream();
		socket_output_stream = client_socket.getOutputStream();
	}

	public LVMessage accept() throws IOException {
		LVMessage queue_accept = new LVMessage(null, 0);
		queue_accept.client_socket = server_socket.accept();

		return queue_accept;
	}

	public int listen() {
		try {
			this.server_socket =
				new ServerSocket(
					this.socket_ip_port,
					5,
					this.socket_ip_address);
		} catch (Exception exception) {
			System.err.println(exception.getMessage());
		}

		return 0;
	}

	private void close() {
		if (this.client_socket != null) {
			try {
				client_socket.close();
			} catch (Exception exception) {
				System.err.println(exception.getMessage());
			}
		}

		if (this.server_socket != null) {
			try {
				server_socket.close();
			} catch (Exception exception) {
				System.err.println(exception.getMessage());
			}
		}
	}

	public static void checkLVSession(Locale l) throws ModCatalogingException {
		if (getLVSessionId() == null) {
//			logger.info("calling LVMessage.LVStartSession()");
			String lvSessionId = LVStartSession();
			setLVSessionId(lvSessionId);
//			logger.info("session ID = " + lvSessionId);
			LVMessage.LVInitialiseForLibrisuite(
				lvSessionId,
				Defaults.getString("librivision.login"),
				Defaults.getString("librivision.password"),
				0,
				l);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Session " + getLVSessionId() + " is still active");
			}
		}
	}

	public static String LVStartSession() throws ModCatalogingException {
//		logger.info("enter LVStartSession");
		String LVSessionId = null;
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_PROCESS_MANAGER_ADMIN);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_START_SESSION);
			lvmessage.write("HELLO", "Hello");
			lvmessage.send();

			Hashtable aHashtable = lvmessage.readHashTable();
			LVSessionId = (String) aHashtable.get("SESSION_ID");
			if (aHashtable.get("ERROR") != null) {
				logger.debug("ERROR");
				throw new ModCatalogingException("LVMessage::LVStartSession:: ERROR");
			} else if (aHashtable.get("MAX_CONCURRENT_SESSIONS") != null) {
				logger.debug("MAX_CONCURRENT_SESSIONS");
				throw new ModCatalogingException("LVMessage::LVStartSession:: MAX_CONCURRENT_SESSIONS");
			} else if (LVSessionId == null) {
				logger.debug("LVSessionId == null");
				throw new ModCatalogingException("LVMessage::LVStartSession:: LVSessionId == null");
			}
		} catch (IOException ioException) {
			logger.debug("", ioException);
			throw new ModCatalogingException(
				"LVMessage::LVStartSession:: " + ioException.getMessage());
		}

//		logger.info("LVSESSionId = " + LVSessionId);
//		logger.info("exit LVStartSession");

		return new String(LVSessionId);
	}

	/**
	 * This method calls the "AUTHORISATION" action from LibriVision.
	 * 
	 * @param lvSessionId LibriVision session ID
	 * @param login login
	 * @param password password
	 * @param sessionTimeOut session time out stringValue
	 * @param locale the locale in use
	 *
	 * @throws IOException
	 * @throws Exception
	 *
	 * @since 1.0
	 */
	public static int LVInitialiseForLibrisuite(
		String lvSessionId,
		String login,
		String password,
		int sessionTimeOut,
		Locale locale)
		throws ModCatalogingException {
		int defaultDbId = 1;
//		logger.info("enter LVInitialiseForLibrisuite()");
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("LANGUAGE_CODE", locale.getLanguage());
			lvmessage.write("COUNTRY_CODE", locale.getCountry().toLowerCase());
			lvmessage.write("HTML_SET_CODE", "default");
			if ((login != null) && (login.length() > 0)) {
				lvmessage.write("USER_LOGIN", login);
			}
			if ((password != null) && (password.length() > 0)) {
				lvmessage.write("USER_PASSWORD", password);
			}
			if (sessionTimeOut > 0) {
				lvmessage.write("SESSION_TIME_OUT", sessionTimeOut);
			}
			lvmessage.write("IP_ADDRESS", "127.0.0.1");
			lvmessage.write("lv_action", "LV_Initialise_for_LibriSuite");
			lvmessage.send();

			Hashtable aHashtable = lvmessage.readHashTable();
			if (aHashtable.get("ERROR_CODE") != null) {
				logger.debug("ERROR " + aHashtable.get("ERROR_MODULE"));
				throw new ModCatalogingException(
					(String) aHashtable.get("ERROR_MODULE"));
			} else if (aHashtable.get("DEFAULT_DB_ID") != null) {
				defaultDbId =
					Integer.parseInt((String) aHashtable.get("DEFAULT_DB_ID"));
			}
		} catch (IOException ioException) {
			logger.debug(ioException.getMessage());
			throw new ModCatalogingException(ioException.getMessage());
		}
//		logger.info("exit LVInitialiseForLibrisuite()");
		return defaultDbId;
	}

	/**
	 * This method calls the "AUTHORISATION" action from LibriVision.
	 * 
	 * @param lvSessionId LibriVision session ID
	 * @param login login
	 * @param password password
	 * @param locale the locale in use
	 *
	 * @throws IOException
	 * @throws Exception
	 *
	 * @since 1.0
	 */
	public static void LVAuthorise(
		String lvSessionId,
		String login,
		String password,
		Locale locale)
		throws Exception {
//		logger.info("enter LVAuthorise()");
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_AUTHORISATION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("LANGUAGE_CODE", locale.getLanguage());
			lvmessage.write("COUNTRY_CODE", locale.getCountry().toLowerCase());
			lvmessage.write("HTML_SET_CODE", "default");
			lvmessage.write("USER_LOGIN", login);
			lvmessage.write("USER_PASSWORD", password);
			lvmessage.write("IP_ADDRESS", "127.0.0.1");
			lvmessage.send();

			Hashtable aHashtable = lvmessage.readHashTable();
			if (aHashtable.get("ERROR_CODE") != null) {
				logger.debug("ERROR " + aHashtable.get("ERROR_MODULE"));
				throw new Exception((String) aHashtable.get("ERROR_MODULE"));
			}
		} catch (IOException ioException) {
			logger.debug(ioException.getMessage());
			throw ioException;
		}
//		logger.info("exit LVAuthorise()");
	}

	public static void LVMenu(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_Menu");
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	public static void LVScanForm(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_Scan_Form");
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	public static void LVScan(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_Scan");
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	public static void LVSearchForm(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_Search_Form");
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	/**
	 * This method calls the "LV_Search" action from LibriVision.
	 *
	 * @param lvmessage a lvmessage
	 * @return a result set
	 * @throws IOException
	 * @throws Exception
	 * @since 1.0
	 */
	public static LVResultSet LVSearch(
		Locale locale,
		LVMessage lvmessage,
		int view)
		throws ModCatalogingException {
//		logger.info("enter LVSearch()");
		checkLVSession(locale);
		String lvSessionId = getLVSessionId();
		LVResultSet aResultSet = null;
		String id = null;
		int size = 0;

		try {
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_Search");
			String databaseId;
			if (view == 0) {
				databaseId = Defaults.getString("librivision.databaseId");
			} else if (view == View.AUTHORITY) {
				databaseId =
					Defaults.getString("librivision.databaseIdAuthority");
			} else {
				databaseId =
					Defaults.getString("librivision.databaseIdView" + view);
			}
			lvmessage.write("DB_ID", databaseId);
			//lvmessage.write("NR_DBS", "");
			//lvmessage.write("DB_ID[klm]", "");
			//lvmessage.write("MAX_NR_LDBS", "");
			//lvmessage.write("NR_LDBS[klm]", "");
			//lvmessage.write("LDB_NAME[klm][lmn]", "");
			lvmessage.send();

			Hashtable aHashtable = lvmessage.readHashTable();
			if (aHashtable.get("ERROR_CODE") != null) {
				logger.debug("ERROR " + aHashtable.get("ERROR_MODULE"));
				throw new ModCatalogingException(
					(String) aHashtable.get("ERROR_MODULE"));
			}

			if ((aHashtable.get("SEARCH_AVAILABLE") != null)
				&& (!aHashtable.get("SEARCH_AVAILABLE").equals("0"))
				&& (aHashtable.get("SEARCH_FINISHED") != null)
				&& (!aHashtable.get("SEARCH_FINISHED").equals("0"))) {
				//   NR_SEARCHES
				//   DB_NAME[klm]
				//   DB_ID[klm]
				//   DB_FINISHED[klm]
				//   DB_CONNECTING_STATUS[klm]
				//   DB_INITIALISATION_STATUS[klm]
				//   DB_SEARCHING_STATUS[klm]
				//   DB_ERROR[klm]
				//   SEARCH_STATUS_PROGRESS
				size =
					Integer.parseInt(
						(String) aHashtable.get(
							"SEARCH_STATUS_RESULT_COUNT[0]"));
				id =
					(String) aHashtable.get("SEARCH_STATUS_RESULT_SET_NAME[0]");
				//   QUERY_ID
				aResultSet = new LVResultSet(searchEngine, lvSessionId, id, size, databaseId);
				aResultSet.setDisplayQuery(
					new String(
						((String) aHashtable.get("DISPLAY_QUERY")).getBytes(
							"ISO8859-1"),
						"UTF-8"));
				aResultSet.setSearchingView(view);
				short sortCriteria =
					Defaults.getShort("searching.defaultSortCriteria");
				if (sortCriteria > 0) {
					LVSort(aResultSet, sortCriteria);
				}
			}
		} catch (IOException ioException) {
			logger.debug(ioException.getMessage());
			throw new ModCatalogingException(ioException);
		}

//		logger.info("exit LVSearch()");

		return aResultSet;
	}

	public static LVResultSet LVSimpleSearch(
		Locale locale,
		String query,
		String use,
		int view)
		throws ModCatalogingException {
//		logger.info("enter LVSimpleSearch()");
		checkLVSession(locale);
		LVResultSet aResultSet = null;
		String id = null;
		int size = 0;

		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write("SEARCH_TYPE", "QUERY_SIMPLE");
			lvmessage.write("QUERY", query);
			lvmessage.write("USE", use);
			lvmessage.write("HTML_SEARCH_TYPE", "EXPERT");

			aResultSet = LVSearch(locale, lvmessage, view);

		} catch (IOException ioException) {
			logger.debug(ioException.getMessage());
			throw new ModCatalogingException(ioException);
		}

//		logger.info("exit LVSimpleSearch()");

		return aResultSet;
	}

	public static LVResultSet LVCclSearch(
		Locale locale,
		String CclQuery,
		int view)
		throws ModCatalogingException {
//		logger.info("enter LVCclSearch()");
		checkLVSession(locale);
		LVResultSet aResultSet = null;
		String id = null;
		int size = 0;

		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write("SEARCH_TYPE", "QUERY_CCL");
			lvmessage.write("CCL_QUERY", CclQuery);
			lvmessage.write("HTML_SEARCH_TYPE", "EXPERT");

			aResultSet = LVSearch(locale, lvmessage, view);

		} catch (IOException ioException) {
			logger.debug(ioException.getMessage());
			throw new ModCatalogingException(ioException);
		}

//		logger.info("exit LVCclSearch()");

		return aResultSet;
	}

	public static LVResultSet LVAdvancedSearch(
		Locale locale,
		List term,
		List relation,
		List use,
		List operator,
		int view)
		throws ModCatalogingException {
//		logger.info("enter LVAdvancedSearch()");
		checkLVSession(locale);
		LVResultSet aResultSet = null;
		String id = null;
		String messageString = null;
		int size = 0;

		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write("SEARCH_TYPE", "QUERY_FORM");

			for (int i = 0; i < term.size(); i++) {

				messageString = "TERM[" + i + "]";
				lvmessage.write(messageString, term.get(i).toString());
			}

			for (int i = 0; i < use.size(); i++) {
				messageString = "USE[" + i + "]";
				lvmessage.write(messageString, use.get(i).toString().trim());
			}

			for (int i = 0; i < operator.size(); i++) {

				messageString = "OPERATOR[" + i + "]";
				lvmessage.write(messageString, operator.get(i).toString());
			}

			for (int i = 0; i < relation.size(); i++) {

				messageString = "RELATION[" + i + "]";
				lvmessage.write(messageString, relation.get(i).toString());
			}

			lvmessage.write("MAX_TERMS", term.size());

			lvmessage.write("HTML_SEARCH_TYPE", "EXPERT");

			aResultSet = LVSearch(locale, lvmessage, view);

		} catch (IOException ioException) {
			logger.debug(ioException.getMessage());
			throw new ModCatalogingException(ioException);
		}

//		logger.info("exit LVAdvancedSearch()");

		return aResultSet;
	}

	public static void LVSort(LVResultSet aResultSet, short sortCriteria)
		throws ModCatalogingException {
		List l = new DAOSortCriteriaDetails().getDetails(sortCriteria);

		// convert the List of attribute, direction info from the DAO
		// into two String arrays for attribute and direction as required by
		// LVSort
		List attributes = new ArrayList();
		List directions = new ArrayList();
		SortCriteriaDetails aDetail;
		Iterator iter = l.iterator();
		while (iter.hasNext()) {
			aDetail = (SortCriteriaDetails) iter.next();
			attributes.add(String.valueOf(aDetail.getAttribute()));
			directions.add(String.valueOf(aDetail.getDirection()));
		}
		//sort the result set
		LVMessage.LVSort(
			aResultSet,
			(String[]) attributes.toArray(new String[0]),
			(String[]) directions.toArray(new String[0]));
		aResultSet.setSortCriteria(sortCriteria);
	}

	public static void LVSort(
		LVResultSet aResultSet,
		String[] attribute,
		String[] relation)
		throws ModCatalogingException {
		aResultSet.clearRecords();
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write(
				"COMMUNICATION__MSG_OPTION",
				aResultSet.getLvSessionId());
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", LVMessage.getLVSessionId());
			lvmessage.write("lv_action", "LV_Sort");
			lvmessage.write("INPUT_RESULT_SET_NAME", aResultSet.getId());
			for (int klm = 0; klm < attribute.length; klm++) {
				lvmessage.write("ATTRIBUTE[" + klm + "]", attribute[klm]);
			}
			for (int klm = 0; klm < relation.length; klm++) {
				lvmessage.write("RELATION[" + klm + "]", relation[klm]);
			}
			lvmessage.write("DB_ID", aResultSet.getDatabaseId());
			lvmessage.write("NR_RECORDS_TO_SHOW", 0);
			lvmessage.send();

			Hashtable aHashtable = lvmessage.readHashTable();
			if ((aHashtable.get("ERROR_CODE") != null)
				|| (aHashtable.get("SORT_ERROR") != null)) {
				logger.debug("ERROR " + aHashtable.get("ERROR_MODULE"));
				throw new ModCatalogingException(
					(String) aHashtable.get("ERROR_MODULE"));
			}

			if ((aHashtable.get("SORT_AVAILABLE") != null)
				//				&& (!aHashtable.get("SPRT_AVAILABLE").equals("0"))
				&& (aHashtable.get("SORT_FINISHED") != null)
				&& (!aHashtable.get("SORT_FINISHED").equals("0"))) {
				aResultSet.setId(
					(String) aHashtable.get("SORT_SORTED_RESULT_SET_NAME"));
			}
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
			throw new ModCatalogingException(ioException);
		}
	}

	public static void LVDatabaseMenu(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_Database_Menu");
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	public static void LVDatabaseSet(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_Database_Set");
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	/**
	 * This method calls the "LV_View_Records" action from LibriVision.
	 * 
	 * @param aResultSet the resultSet from whom to retrieve records.
	 * @param firstRecordNumber the record number of the first record to retrieve.
	 * @param lastRecordNumber the record number of the last record to retrieve.
	 * @since 1.0
	 */
	public static void LVViewRecords(
		LVResultSet aResultSet,
		String elementSetName,
		int firstRecordNumber,
		int lastRecordNumber) /*throws ModCatalogingException*/ {
//		logger.info("enter LVViewRecords()");
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write(
				"COMMUNICATION__MSG_OPTION",
				aResultSet.getLvSessionId());
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("lv_action", "LV_View_Records");
			lvmessage.write("RESULT_SET_NAME", aResultSet.getId());
			lvmessage.write(
				"NR_RECORDS_TO_SHOW",
				lastRecordNumber - firstRecordNumber + 1);
			//lvmessage.write("GOTO_LAST", "0");
			//lvmessage.write("GOTO_FIRST", "1");
			lvmessage.write("GOTO_RECORD", firstRecordNumber);
			lvmessage.write("SESSION_ID", LVMessage.getLVSessionId());
			lvmessage.write("ELEMENT_SET_NAME", elementSetName);
			//lvmessage.write("GET_RECORDS", "");
			lvmessage.write("DISPLAY_RECORD_XSLT", "xml");
			//lvmessage.write("DISPLAY_RECORD_XSLT", "original_xml");
			lvmessage.send();

			Hashtable aHashtable = lvmessage.readHashTable();
			if (aHashtable.get("ERROR_CODE") != null) {
				logger.debug("ERROR " + aHashtable.get("ERROR_MODULE"));
				//throw new ModCatalogingException((String) aHashtable.get("ERROR_MODULE"));
			}

			if ((aHashtable.get("PRESENT_FINISHED") != null)
				&& (!aHashtable.get("PRESENT_FINISHED").equals("0"))) {
				for (int klm = 0;
					klm < (lastRecordNumber - firstRecordNumber + 1);
					klm++) {
					Record aRecord =
						aResultSet.getRecord()[firstRecordNumber + klm - 1];
					if (aRecord == null) {
						aRecord = new XmlRecord();
						aResultSet.setRecord(
							firstRecordNumber + klm - 1,
							aRecord);
					}
					String xmlRecordString = "";
					if (aHashtable.get("RECORD[" + klm + "]") != null) {
						//   RESULT_COUNT
						//   RECORD_ID[klm]
						//   RECORD_NR[klm]
						//   DB_DESCRIPTION
						//   DB_ID
						//   DISPLAY_QUERY
						//   NR_RECORDS
						//   DISPLAY_RECORD_XSLT // html_marc, marc, html, xml
						//   ELEMENT_SET_NAME // B - F
						xmlRecordString =
							new String(
								(
									(String) aHashtable.get(
										"RECORD[" + klm + "]")).getBytes(
									"ISO8859-1"),
								"UTF-8");
					}
					try {
						((XmlRecord) aRecord).setContent(
							elementSetName,
							xmlRecordString);
					} catch (XmlUnsupportedEncodingException xmlDocumentException) {
						DocumentBuilderFactory documentBuilderFactory =
							DocumentBuilderFactory.newInstance();
						DocumentBuilder documentBuilder = null;
						try {
							documentBuilder =
								documentBuilderFactory.newDocumentBuilder();
							Document xmlRecordDocument =
								documentBuilder.newDocument();
							((XmlRecord) aRecord).setContent(
								elementSetName,
								xmlRecordDocument);
						} catch (ParserConfigurationException parserConfigurationException) {
							logger.error("", parserConfigurationException);
							//throw new XmlParserConfigurationException(parserConfigurationException);
						}
					} catch (XmlParserConfigurationException xmlDocumentException) {
						DocumentBuilderFactory documentBuilderFactory =
							DocumentBuilderFactory.newInstance();
						DocumentBuilder documentBuilder = null;
						try {
							documentBuilder =
								documentBuilderFactory.newDocumentBuilder();
							Document xmlRecordDocument =
								documentBuilder.newDocument();
							((XmlRecord) aRecord).setContent(
								elementSetName,
								xmlRecordDocument);
						} catch (ParserConfigurationException parserConfigurationException) {
							logger.error("", parserConfigurationException);
							//throw new XmlParserConfigurationException(parserConfigurationException);
						}
					}
				}
			}
		} catch (IOException ioException) {
			logger.debug(ioException.getMessage());
			//throw ioException;
		}
//		logger.info("exit LVViewRecords()");
	}

	public static void LVStoredQueries(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_Stored_Queries");
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	public static void LVStoredRecords(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_Stored_Records");
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	public static void LVRecord(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_Record");
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	public static void LVHistory(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_History");
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	public static void LVPersonInfo(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.write("lv_action", "LV_Person_Info");
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	/**
	 * This method calls the "LV_Exit" action from LibriVision.
	 * 
	 * @param lvSessionId LibriVision session ID
	 * @since 1.0
	 */
	public static void LVExit(String lvSessionId) {
//		logger.info("enter LVExit()");
		if (lvSessionId != null) {
			try {
				LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
				lvmessage.open();
				lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
				lvmessage.write(
					"COMMUNICATION__MSG_TYPE",
					LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
				lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
				lvmessage.write(
					"COMMUNICATION__MSG_HEADER",
					LVMessage.MSG_HEADER_LIBRIVISION);
				lvmessage.write("SESSION_ID", lvSessionId);
				lvmessage.write("lv_action", "LV_Exit");
				lvmessage.send();

				Hashtable aHashtable = lvmessage.readHashTable();
				if (aHashtable.get("ERROR_CODE") != null) {
					logger.debug("ERROR " + aHashtable.get("ERROR_MODULE"));
					//throw new Exception(
					//	(String) aHashtable.get("ERROR_MODULE"));
				}
			} catch (IOException ioException) {
				logger.debug(ioException.getMessage());
				//throw ioException;
			}
		}
//		logger.info("exit LVExit()");
	}

	public static void LARunProc(String lvSessionId) {
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_LIBRIVISION);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.send();

			Hashtable aa = lvmessage.readHashTable();
			/*
			if (aa.get("ERROR") != null) {
				return false;
			} else {
				return true;
			}
			*/
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
		}
	}

	/**
	 * This method calls the "lv_session_hello" method from LibriVision.
	 * 
	 * @param lvSessionId LibriVision session ID
	 * @since 1.0
	 */
	public static void LVHello(String lvSessionId) throws ModCatalogingException {
//		logger.info("enter LVHello()");
		try {
			LVMessage lvmessage = new LVMessage(LVServerName, LVServerPort);
			lvmessage.open();
			lvmessage.write(LVMessage.MSG_HEADER_COMMUNICATION);
			lvmessage.write(
				"COMMUNICATION__MSG_TYPE",
				LVMessage.MSG_TYPE_SESSIONDAEMON_SESSION);
			lvmessage.write("COMMUNICATION__MSG_OPTION", lvSessionId);
			lvmessage.write(
				"COMMUNICATION__MSG_HEADER",
				LVMessage.MSG_HEADER_HELLO);
			lvmessage.write("SESSION_ID", lvSessionId);
			lvmessage.send();

			Hashtable aHashtable = lvmessage.readHashTable();
			if (aHashtable.get("ERROR_CODE") != null) {
				logger.debug("ERROR " + aHashtable.get("ERROR_MODULE"));
				throw new ModCatalogingException(
					(String) aHashtable.get("ERROR_MODULE"));
			}
		} catch (IOException ioException) {
			logger.debug(ioException.getMessage());
			throw new ModCatalogingException(ioException.getMessage());
		}
//		logger.info("exit LVHello()");
	}

}
