/*
 * (c) LibriCore
 * 
 * Created on 10-ott-2005
 * 
 * ModelKey.java
 */
package librisuite.hibernate;

import java.io.Serializable;

/**
 * @author Carmen
 * @version $Revision: 1.1 $, $Date: 2007/10/18 10:28:23 $
 * @since 1.0
 */
public class ModelTagKey implements Serializable {
	private int sequence;
	private String language;

	/**
	 * Class constructor
	 *
	 * 
	 */
	public ModelTagKey() {
		super();
	}

	public ModelTagKey(int sequence, String language) {
		this.setSequence(sequence);
		this.setLanguage(language);
   }
	
	

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((language == null) ? 0 : language.hashCode());
		result = PRIME * result + sequence;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ModelTagKey other = (ModelTagKey) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (sequence != other.sequence)
			return false;
		return true;
	}

	
	
    
}
