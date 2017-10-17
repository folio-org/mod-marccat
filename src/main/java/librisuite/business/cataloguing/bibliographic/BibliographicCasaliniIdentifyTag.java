package librisuite.business.cataloguing.bibliographic;

import com.libricore.librisuite.common.StringText;

import librisuite.business.cataloguing.common.CasaliniSourceTag;

public class BibliographicCasaliniIdentifyTag extends CasaliniSourceTag {
	
	public String getDisplayString() {

 		String catSourceStringTxt = getItemEntity().getCataloguingSourceStringText(); 		
 		StringText a = new StringText(catSourceStringTxt);
 		String dollaro_a = a.getSubfieldsWithCodes("a").getDisplayText();
 		
 		return dollaro_a;
	}


}
