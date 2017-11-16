/*
 * (c) LibriCore
 * 
 * Created on Jul 14, 2005
 * 
 * T_ITM_DSPLY.java
 */
package librisuite.hibernate;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/07/14 13:32:57 $
 * @since 1.0
 */
public class T_ITM_DSPLY implements Serializable {
	private short code = 2;
	private String frmt = "Full";
	private boolean labelled = false;


	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public T_ITM_DSPLY() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @since 1.0
	 */
	public short getCode() {
		return code;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getFrmt() {
		return frmt;
	}

	public boolean isFull() {
		return getFrmt().equals("Full");
	}
	
	public boolean isBrief() {
		return !isFull();
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public boolean isLabelled() {
		return labelled;
	}

	public boolean isMarc() {
		return !isLabelled();
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	public void setCode(short s) {
		code = s;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setFrmt(String string) {
		frmt = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setLabelled(boolean b) {
		labelled = b;
	}

}
