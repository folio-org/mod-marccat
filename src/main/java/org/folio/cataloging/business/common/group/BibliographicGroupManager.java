package org.folio.cataloging.business.common.group;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.regexp.RESyntaxException;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.filter.*;


/**
 * Implementa, per i bibliografici, sia il GroupManager che il FilterManager
 *
 * @author michele
 */
public class BibliographicGroupManager extends MarcGroupManager implements FilterManager {
  private static final Log logger =
    LogFactory.getLog (BibliographicGroupManager.class);

  /**
   * Filtro per tag che non possono essere ordinati. Il comportamento è sempre uguale
   * per tutti i tag non ordinabili
   */
  private static final TagFilter NO_TAG_FILTER = new NoTagFilter ( );

  private static BibliographicGroupManager instance = null;

  public BibliographicGroupManager() {
    super ( );
    try {
      add (new RegExpGroup (true, true, "0[1-4]\\d"));
      add (new RegExpGroup (true, true, "0[5-8]\\d"));
      add (new RegExpGroup (false, false, "1\\d\\d"));
      add (new RegExpGroup (false, false, "245"));
      add (new RegExpGroup (true, true, "210|222|240|242|243|246|247"));
      add (new RegExpGroup (false, false, "260"));
      add (new RegExpGroup (false, false, "264"));
      add (new RegExpGroup (true, true, "3\\d\\d"));
      add (new RegExpGroup (true, false, "4\\d\\d"));
      add (new RegExpGroup (true, false, "5\\d\\d"));
      add (new RegExpGroup (true, false, "6\\d\\d"));
      add (new RegExpGroup (true, false, "700|710|711|720"));
      add (new RegExpGroup (true, false, "730|740"));
      add (new RegExpGroup (true, false, "8[0-2]\\d|830"));
      add (new RegExpGroup (true, true, "856"));
      add (new RegExpGroup (true, false, "99\\d"));
    } catch (RESyntaxException e) {
      logger.error ("BibliographicGroupManager not properly initialized", e);
    }
  }

  @Deprecated
  public static BibliographicGroupManager getInstance() {
    if (instance == null) {
      instance = new BibliographicGroupManager ( );
    }
    return instance;
  }

  /* (non-Javadoc)
   * @see FilterManager#getFilter(Tag)
   */
  public TagFilter getFilter(Tag tag) throws DataAccessException {
    TagGroup group = getGroup (tag);
    if (group == null) return NO_TAG_FILTER;
    else if (group.isCanSort ( ) && group.isSingleSort ( )) {
      return new SingleTagFilter (tag);
    } else if (group.isCanSort ( ) && !group.isSingleSort ( )) {
      return new GroupTagFilter (group);
    } else return NO_TAG_FILTER;
  }

}
