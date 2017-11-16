package librisuite.business.cataloguing.bibliographic;

import com.libricore.librisuite.common.StringText;

import librisuite.business.cataloguing.common.AccessPoint;
import librisuite.business.cataloguing.common.Browsable;
import librisuite.business.cataloguing.common.CatalogItem;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.LibrisuiteUtils;
import librisuite.business.common.SortFormException;
import librisuite.business.descriptor.DAODescriptor;
import librisuite.business.descriptor.Descriptor;
import librisuite.business.descriptor.PublisherTagDescriptor;
import librisuite.hibernate.CorrelationKey;
import librisuite.hibernate.PUBL_HDG;
import librisuite.hibernate.PUBL_TAG;

/**
 * Command library for operation about tags
 * @author michelem
 *
 */
public class MarcCommandLibrary {
	
	/**
	 * 
	 * Moved here from {@link ReplaceDescriptorCommand} 
	 * @param catalogItem
	 * @param srcTag
	 * @param replacingDescriptor
	 * @return
	 * @throws DataAccessException
	 */
	public static Tag replaceDescriptor(CatalogItem catalogItem, Tag srcTag, Descriptor replacingDescriptor) throws DataAccessException {
		Tag newTag = (Tag) LibrisuiteUtils.deepCopy(srcTag);
		if (newTag instanceof PersistsViaItem) {
			((PersistsViaItem) newTag).setItemEntity(
				((PersistsViaItem) srcTag).getItemEntity());
		}
		newTag.markNew();
		if (!srcTag.isNew()) {
			srcTag.markDeleted();
			catalogItem.addDeletedTag(srcTag);
		}
		
		replaceDescriptor((Browsable)newTag, replacingDescriptor);
		
		catalogItem.setTag(srcTag, newTag);
		return newTag;
	}

	/**
	 * Moved here from {@link ReplaceDescriptorCommand} 
	 * @param tag
	 * @param newDescriptor
	 * @throws DataAccessException 
	 */
	private static void replaceDescriptor(Browsable tag, Descriptor newDescriptor) throws DataAccessException {
		// backup to prevent losing after catching exception
		Descriptor oldDescriptor = tag.getDescriptor();
		
		tag.setDescriptor(newDescriptor);
		/*TODO SubjectAccessPoint.setDescriptor has been modified to verify
		 * that the encoding of the resultant tag after updating the descriptor
		 * is still valid.  If not, it will re-assign a valid value2.  It may
		 * be that similar resets are required for other categories, but for now 
		 * others seem to be OK.
		 */
		
		/*
		 * -- Start block -- Natascia 3/07/2007
		 * getMarcEncoding verify the new correlation:
		 * if it is a wrong correlation a MarcCorrelationException occurs.
		 * See the Subject with correlation 19, ?, 14
		 */
		try{
			if ((CorrelationKey) ((Tag)tag).getMarcEncoding() == null)
				throw new MarcCorrelationException();		
		}
		catch (MarcCorrelationException me){
			// revert descriptor with the original
			tag.setDescriptor(oldDescriptor);
			throw new DataAccessException();
		}
		catch (DataAccessException dae) {
			// revert descriptor with the original
			tag.setDescriptor(oldDescriptor);
			throw dae;
		}
		
		/* -- End block -- */

		((Browsable) tag).setHeadingNumber(
			new Integer(newDescriptor.getKey().getHeadingNumber()));
	}

	public static Tag replaceTagWithClone(CatalogItem catalogItem, Tag srcTag){
		Tag newTag = (Tag) LibrisuiteUtils.deepCopy(srcTag);
		if (newTag instanceof PersistsViaItem) {
			((PersistsViaItem) newTag).setItemEntity(
				((PersistsViaItem) srcTag).getItemEntity());
		}
		newTag.markNew();
		if (!srcTag.isNew()) {
			srcTag.markDeleted();
			catalogItem.addDeletedTag(srcTag);
		}
		
		catalogItem.setTag(srcTag, newTag);
		return newTag;
	}

	public static Descriptor createNewDescriptor(Descriptor currDescriptor, String headingView) throws SortFormException, DataAccessException{
		if(!currDescriptor.isNew()) return currDescriptor;
		Descriptor matchDescriptor = ((DAODescriptor) currDescriptor
				.getDAO()).getMatchingHeading(currDescriptor);
		if (matchDescriptor == null) {
			if(currDescriptor.getKey().getHeadingNumber()==-1){// key is not null by default
				currDescriptor.generateNewKey();
				currDescriptor.getKey().setUserViewString(headingView);
			}
			((DAODescriptor)currDescriptor.getDAO()).persistByStatus(currDescriptor);
			return currDescriptor;
		} else {
			return matchDescriptor;
		}
	}
	
	public static void setNewStringText(AccessPoint tag, StringText text, String headingView) throws SortFormException, DataAccessException{
		if(!tag.isNew()) throw new IllegalArgumentException("this method can be used only for new tags");
		tag.getDescriptor().setUserViewString(headingView);
		tag.setDescriptorStringText(text);
		Descriptor newDescriptor =null;
		//Aggiunto da Carmen per tag 260
		if(tag.getDescriptor() instanceof PublisherTagDescriptor){
			PUBL_TAG pu =(PUBL_TAG)(((PublisherTagDescriptor)tag.getDescriptor()).getPublisherTagUnits().get(0));
			newDescriptor = createNewDescriptor(pu.getDescriptor(), headingView);
			pu.setDescriptor((PUBL_HDG)newDescriptor);
		}
		else{
		  newDescriptor = createNewDescriptor(tag.getDescriptor(), headingView);
		  tag.setDescriptor(newDescriptor);
		  tag.setHeadingNumber(new Integer(newDescriptor.getKey().getHeadingNumber()));
		}
	}
	
	/*public static void setNewStringText(PublisherTag tag, StringText text, String headingView) throws SortFormException, DataAccessException{
		if(!tag.isNew()) throw new IllegalArgumentException("this method can be used only for new publisher tags");
		PublisherAccessPoint pap = tag.getAnyPublisher();
		setNewStringText(pap, text, headingView);
	}*/
	public static void setNewStringText(PublisherManager tag, StringText text, String headingView) throws SortFormException, DataAccessException{
		if(!tag.isNew()) throw new IllegalArgumentException("this method can be used only for new publisher tags");
		PublisherAccessPoint pap = tag.getApf();
		setNewStringText(pap, text, headingView);
	}

}
