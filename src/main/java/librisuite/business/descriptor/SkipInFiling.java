/*
 * (c) LibriCore
 * 
 * Created on Aug 26, 2004
 * 
 * SkipInFiling.java
 */
package librisuite.business.descriptor;

/**
 * Interface for descriptors that have a skip-in-filing indicator (Titles, Subjects)
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/08/27 08:04:46 $
 * @since 1.0
 */
public interface SkipInFiling {

	public short getSkipInFiling();
	
	public void setSkipInFiling(short i);
}
