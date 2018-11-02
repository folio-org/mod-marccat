package org.folio.marccat.business.common.group;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.DataAccessException;


public class RegExpGroup extends MarcTagGroup {
  private RE regexp;

  public RegExpGroup(boolean canSort, boolean singleSort, String pattern) throws RESyntaxException {
    super(canSort, singleSort);
    regexp = new RE(pattern);
  }

  public static void main(String[] args) {
    String pattern = "^52[0124] ";
    RE regexp = new RE(pattern);

    System.out.println("Value: " + regexp.match("520 "));
  }

  public boolean contains(Tag t) throws DataAccessException {
    return regexp.match(t.getMarcEncoding().getMarcTag());
  }

}
