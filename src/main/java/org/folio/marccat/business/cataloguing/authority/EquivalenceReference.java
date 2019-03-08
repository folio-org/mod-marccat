package org.folio.marccat.business.cataloguing.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.dao.persistence.ReferenceType;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;
import org.w3c.dom.Element;


public class EquivalenceReference extends AuthorityReferenceTag {

  private static final long serialVersionUID = 1L;

  private static final Log logger = LogFactory.getLog(EquivalenceReference.class);

  public EquivalenceReference() {
    super();
  }


  @Override
  public CorrelationKey getMarcEncoding() throws DataAccessException {
    CorrelationKey key = super.getMarcEncoding();
    logger.debug("getMarcEncoding before source: " + key);
    key = key.changeAuthoritySourceIndicator(getDescriptor().getAuthoritySourceCode());
    logger.debug("getMarcEncoding after source: " + key);
    return key;
  }

  @Override
  public StringText getStringText() {
    String subw =
      ""
        + getReference().getLinkDisplay()
        + getReference().getReplacementComplexity();
    StringText result = super.getStringText();
    result.addSubfield(new Subfield("w", subw));
    return result;
  }

  /**
   * @param v
   * @return
   */
  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    if (!super.correlationChangeAffectsKey(v)) {
      return !ReferenceType.isEquivalence(v.getValue(getRefTypeCorrelationPosition()));
    } else {
      return false;
    }
  }

  @Override
  public void parseModelXmlElementContent(Element xmlElement) {
    StringText s = StringText.parseModelXmlElementContent(xmlElement);
    String subw = s.getSubfieldsWithCodes("w").getSubfield(0).getContent();
    getReference().setLinkDisplay(subw.charAt(0));
    getReference().setReplacementComplexity(subw.charAt(1));
  }

}
