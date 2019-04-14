package org.folio.marccat.business.common.group;

import org.apache.regexp.RE;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.config.log.Log;


public class RegExpGroup extends MarcTagGroup {
  private static final Log logger = new Log(RegExpGroup.class);

  private RE regexp;

  public RegExpGroup(boolean canSort, boolean singleSort, String pattern) {
    super(canSort, singleSort);
    regexp = new RE(pattern);
  }

  public static void main(String[] args) {
    String pattern = "^52[0124] ";
    RE regexp = new RE(pattern);
    logger.info("Value: " + regexp.match("520 "));
  }

  public boolean contains(Tag t) {
    return regexp.match(t.getMarcEncoding().getMarcTag());
  }

}
