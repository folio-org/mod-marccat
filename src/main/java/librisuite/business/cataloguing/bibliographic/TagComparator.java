/*
 * (c) LibriCore
 * 
 * Created on Aug 28, 2004
 * 
 * TagComparator.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.Comparator;

import librisuite.business.cataloguing.common.OrderedTag;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.group.BibliographicGroupManager;
import librisuite.business.common.group.GroupManager;
import librisuite.business.common.group.TagGroup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class TagComparator implements Comparator {
	private static Log logger = LogFactory.getLog(TagComparator.class);
	
	private GroupManager groupManager = BibliographicGroupManager.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	/*public int compare(Object arg0, Object arg1) {
		if (arg0 instanceof Tag && arg1 instanceof Tag) {
			if (arg0 instanceof NameAccessPoint
				&& arg1 instanceof NameAccessPoint
				&& ((NameAccessPoint) arg0).getSequenceNumber() != null
				&& ((NameAccessPoint) arg1).getSequenceNumber() != null) {
				return ((NameAccessPoint) arg0)
					.getSequenceNumber()
					.compareTo(
					((NameAccessPoint) arg1).getSequenceNumber());
			}

			if (arg0 instanceof SubjectAccessPoint
				&& arg1 instanceof SubjectAccessPoint) {
				return ((SubjectAccessPoint) arg0)
					.getSequenceNumber()
					.compareTo(
					((SubjectAccessPoint) arg1).getSequenceNumber());
			}*/
			/*if (arg0 instanceof BibliographicNoteTag
					&& arg1 instanceof BibliographicNoteTag) {
					return ((BibliographicNoteTag) arg0).getNote().
						getSequenceNumber()
						.compareTo(
						((BibliographicNoteTag) arg1).getNote().getSequenceNumber());
				}*/
			/*if (arg0 instanceof TitleAccessPoint
					&& arg1 instanceof TitleAccessPoint) {
					return ((TitleAccessPoint) arg0)
						.getSequenceNumber()
						.compareTo(
						((TitleAccessPoint) arg1).getSequenceNumber());
				}*/
			/*if (arg0 instanceof ClassificationAccessPoint
					&& arg1 instanceof ClassificationAccessPoint) {
					return ((ClassificationAccessPoint) arg0)
						.getSequenceNumber()
						.compareTo(
						((ClassificationAccessPoint) arg1).getSequenceNumber());
				}*/
			/*if (arg0 instanceof ControlNumberAccessPoint
					&& arg1 instanceof ControlNumberAccessPoint) {
					return ((ControlNumberAccessPoint) arg0)
						.getSequenceNumber()
						.compareTo(
						((ControlNumberAccessPoint) arg1).getSequenceNumber());
				}*/
			/*if (arg0 instanceof BibliographicRelationshipTag
					&& arg1 instanceof BibliographicRelationshipTag) {
					return ((BibliographicRelationshipTag) arg0).getSourceRelationship().
						getSequenceNumber()
						.compareTo(
						((BibliographicRelationshipTag) arg1).getSourceRelationship().getSequenceNumber());
				}*/
		/*	return compareTagNumber(arg0, arg1);
		}
		return 0;
	}*/
	
		
	
	public int compare(Object arg0, Object arg1) {
	if(arg0 == arg1) return 0;
	if (arg0 instanceof Tag && arg1 instanceof Tag) {
		Tag t0 = (Tag) arg0;
		Tag t1 = (Tag) arg1;
		try {
			TagGroup group = groupManager.getGroup(t0);
			if(!group.isCanSort()) return compareTagNumber(arg0, arg1);
			else if(group.isCanSort() && group.isSingleSort()){
				return ((OrderedTag)t0).getSequenceNumber().compareTo(((OrderedTag)t1).getSequenceNumber());
			}
			if(groupManager.isSameGroup(t0, t1)) {
				if(t0 instanceof OrderedTag && t1 instanceof OrderedTag){
					return ((OrderedTag)t0).getSequenceNumber().compareTo(((OrderedTag)t1).getSequenceNumber());
				}
			}
			return compareTagNumber(arg0, arg1);
		} catch (Exception e) {
			return 0;
		}
	}
	return 0;
}
	protected int compareTagNumber(Object arg0, Object arg1) {
		try {
		return ((Tag) arg0).getMarcEncoding().getMarcTag().compareTo(
			((Tag) arg1).getMarcEncoding().getMarcTag());
		}
		catch (MarcCorrelationException e) {
			logger.warn("MarcCorrelationException sorting in tag order");
			return 0;
		} 
		catch (DataAccessException e) {
			logger.warn("DataAccessException sorting in tag order");
			return 0;
		} 
	}
}
