package librisuite.business.searching;

public class SearchIndexElement {
	private int key;
	private int subKey;
	private String value;
	private String label;	
	
	public SearchIndexElement() {
	}
	
	public SearchIndexElement(int key, int subKey, String value, String label){
			setKey(key);
			setSubKey(subKey);
			setValue(value);
			setLabel(label);			
		}
	
	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getKey() {
		return key;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getSubKey() {
		return subKey;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setKey(int i) {
		key = i;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setLabel(String string) {
		label = string;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setSubKey(int i) {
		subKey = i;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setValue(String string) {
		value = string;
	}

}
