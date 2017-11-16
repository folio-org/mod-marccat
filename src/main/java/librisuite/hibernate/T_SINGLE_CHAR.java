/*
 * (c) LibriCore
 * 
 * Created on 06-jul-2004
 * 
 * T_SINGLE_CHAR.java
 */
package librisuite.hibernate;

import librisuite.business.common.DataAccessException;


/**
 * @author elena
 * @version $Revision: 1.11 $, $Date: 2007/05/04 08:14:58 $
 * @since 1.0
 */
public class T_SINGLE_CHAR extends CodeTable {

	private char code;
	public char getCode() {
		return code;
	}

	public void setCode(char c) {
		code = c;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.CodeTable#getCodeString()
	 */
	public String getCodeString() {
		return String.valueOf(code);
	}
	
	public boolean equals(Object obj) {
			if (!(obj.getClass().equals(this.getClass())))
				return false;
			return (((T_SINGLE_CHAR) obj).getCode() == getCode())
				&& (((T_SINGLE_CHAR) obj).getLanguage().equals(getLanguage()));			
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return getCode() + getLanguage().hashCode();
		} 
		
	public void setExternalCode(Object extCode) {
		if(extCode instanceof String){
			code = ((String)extCode).charAt(0);
		} else if(extCode instanceof Character){
			code = ((Character)extCode).charValue();
		}
	}

	public int getNextNumber() throws DataAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

}
