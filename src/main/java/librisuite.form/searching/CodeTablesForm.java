package librisuite.form.searching;

import librisuite.form.LibrisuiteForm;



public class CodeTablesForm extends LibrisuiteForm {

	private String codeTable = new String();
	
	private String codeTableValue = new String();
	
	private String linkURL = new String();
	
	private String returnString = new String();
	
	private int index;
    
	private String currentFieldName;
	
	private String closeOperation;
	
	 public String getCloseOperation() {
		return closeOperation;
	}

	public void setCloseOperation(String closeOperation) {
		this.closeOperation = closeOperation;
	}

	private String subfieldIndex;
		
		public String getSubfieldIndex() {
			return subfieldIndex;
		}

		public void setSubfieldIndex(String subfieldIndex) {
			this.subfieldIndex = subfieldIndex;
		}
		
	
	public String getCurrentFieldName() {
		return currentFieldName;
	}

	public void setCurrentFieldName(String currentFieldName) {
		this.currentFieldName = currentFieldName;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getCodeTable() {
		return codeTable;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setCodeTable(String string) {
		codeTable = string;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setIndex(int i) {
		index = i;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getCodeTableValue() {
		return codeTableValue;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setCodeTableValue(String string) {
		codeTableValue = string;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getLinkURL() {
		return linkURL;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setLinkURL(String string) {
		linkURL = string;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getReturnString() {
		return returnString;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setReturnString(String string) {
		returnString = string;
	}

}
