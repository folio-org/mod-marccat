/*
 * (c) LibriCore
 * 
 * Created on 14-jul-2004
 * 
 * S_NXT_NBR.java
 */
package librisuite.hibernate;

import java.io.Serializable;

/**
 * @author Maite
 * @version $Revision: 1.4 $, $Date: 2004/08/04 13:50:04 $
 * 
 */
public class S_NXT_NBR implements Serializable{
	
	private String keyFieldCode;
	private int keyFieldNextNumber;
	private String filled;

	public String getFilled() {
		return filled;
	}

	public String getKeyFieldCode() {
		return keyFieldCode;
	}

	public int getKeyFieldNextNumber() {
		return keyFieldNextNumber;
	}

	public void setFilled(String string) {
		filled = string;
	}

	public void setKeyFieldCode(String string) {
		keyFieldCode = string;
	}

	public void setKeyFieldNextNumber(int i) {
		keyFieldNextNumber = i;
	}

}
