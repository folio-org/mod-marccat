package org.folio.marccat.dao.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;

/**
 * @author elena
 *
 */
public class EquivalenceReference extends AuthorityReferenceTag {

  private static final long serialVersionUID = 1L;

  private static final Log logger = LogFactory.getLog(EquivalenceReference.class);

  public EquivalenceReference() {
    super();
  }

  @Override
  public CorrelationKey getMarcEncoding() {
    CorrelationKey key = super.getMarcEncoding();
    logger.debug("getMarcEncoding before source: " + key);
    key = key.changeAuthoritySourceIndicator(getDescriptor().getAuthoritySourceCode());
    logger.debug("getMarcEncoding after source: " + key);
    return key;
  }

  @Override
  public StringText getStringText() {
    String subw = "" + getReference().getLinkDisplay() + getReference().getReplacementComplexity();
    StringText result = super.getStringText();
    result.addSubfield(new Subfield("w", subw));
    return result;
  }

}
