package org.folio.cataloging.business.cataloguing.bibliographic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.dao.persistence.BIB_ITM;
import org.folio.cataloging.dao.persistence.ItemEntity;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

public class LanguageCodeTag extends VariableHeaderUsingItemEntity 
{
	//TODO saveUndoSave doesn't work
	//TODO subfield a should be validated against code table 
	
	private static final long serialVersionUID = -8659240725828529186L;
	
	private static final Log logger = LogFactory.getLog(LanguageCodeTag.class);

	public LanguageCodeTag() 
	{
		super();
		setHeaderType((short) 2);
	}

	public StringText getStringText() 
	{
		StringText result = null;
		String source = ((BIB_ITM)getItemEntity()).getLanguageStringText();

		if (source == null) {
			/* Bug 4115 inizio modifiche Carmen */
			/* result = new StringText(Subfield.SUBFIELD_DELIMITER + "a");*/
			result = new StringText(Subfield.SUBFIELD_DELIMITER + "a"+((BIB_ITM)getItemEntity()).getLanguageCode());
			/* Bug 4115 fine modifiche Carmen */			
		} else {
			result = new StringText(source);
		}
		return result;
	}

	public void setStringText(StringText st) 
	{
		//TODO need a more definitive way to set to null		
		if (st.toString().equals(Subfield.SUBFIELD_DELIMITER + "a")) {
			((BIB_ITM)getItemEntity()).setLanguageStringText(null);
			((BIB_ITM)getItemEntity()).setTranslationCode(null);
		} else {
			((BIB_ITM)getItemEntity()).setLanguageStringText(st.toString());
		}
	}

	/* (non-Javadoc)
	 * @see VariableHeader#setBibHeader(short)
	 */
	public void setHeaderType(short s) 
	{
		super.setHeaderType(s);
		if (getItemEntity() != null) {
			try {
				((BIB_ITM)getItemEntity()).setTranslationCode(String.valueOf(getMarcEncoding().getMarcFirstIndicator()));
			} catch (Exception e) {
				logger.warn("Error setting bib_itm.translationCode, using null");
				((BIB_ITM)getItemEntity()).setTranslationCode(null);
			}
		}
	}

	private void setBibItm(BIB_ITM bib_itm) 
	{
		super.setItemEntity(bib_itm);
		/* Bug 4115 inizio modifiche Carmen */
		//TODO Carmen , fare reactoring mettendo un metodo che dice se è presente il $2 (Source of code)
		/*		
			if ("0".equals(bib_itm.getTranslationCode())) {
				setHeaderType((short)2);
			} else if ("1".equals(bib_itm.getTranslationCode())) {
				setHeaderType((short)3);
			}
		*/
		if ("0".equals(bib_itm.getTranslationCode()) && ((BIB_ITM)getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2")==-1) {
			setHeaderType((short)2);
		}
		else if ("1".equals(bib_itm.getTranslationCode()) && ((BIB_ITM)getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2")==-1) {
			setHeaderType((short)3);
		}
		else if (" ".equals(bib_itm.getTranslationCode())&& ((BIB_ITM)getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2")==-1) {
			setHeaderType((short)49);
		}
		else if ("0".equals(bib_itm.getTranslationCode())&& ((BIB_ITM)getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2")!=-1) {
			setHeaderType((short)50);
		}
		else if ("1".equals(bib_itm.getTranslationCode())&& ((BIB_ITM)getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2")!=-1) {
			setHeaderType((short)52);
		}
		else if (" ".equals(bib_itm.getTranslationCode())&& ((BIB_ITM)getItemEntity()).getLanguageStringText().indexOf(Subfield.SUBFIELD_DELIMITER + "2")!=-1) {
			setHeaderType((short)51);
		}
		/* Bug 4115 fine modifiche Carmen */
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) 
	{
		/* Bug 4115 inizio modifiche Carmen */
//		return v.isValueDefined(1) && (v.getValue(1) != 2) && (v.getValue(1) != 3);
		return v.isValueDefined(1) && (v.getValue(1) != 2) && (v.getValue(1) != 3) && (v.getValue(1) != 49) && (v.getValue(1) != 50) &&  (v.getValue(1) != 51) && (v.getValue(1) != 52);
		/* Bug 4115 fine modifiche Carmen */
	}


	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) 
	{
		super.setCorrelationValues(v);
		setHeaderType(v.getValue(1));
	}

	/* (non-Javadoc)
	 * @see PersistsViaItem#setItemEntity(ItemEntity)
	 */
	public void setItemEntity(ItemEntity item) 
	{
		setBibItm((BIB_ITM)item);
	}
}