package org.folio.marccat.business.common.group;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.regexp.RESyntaxException;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.filter.*;
import org.folio.marccat.exception.DataAccessException;


/**
 * Implements, for bibliographic, both the GroupManager and the FilterManager
 *
 * @author carment
 */
public class BibliographicGroupManager extends MarcGroupManager implements FilterManager {
  private static final Log logger =
    LogFactory.getLog(BibliographicGroupManager.class);

  /**
   * Filter by tags that can not be ordered. The behavior is always the same
   * for all non-orderable tags
   */
  private static final TagFilter NO_TAG_FILTER = new NoTagFilter();

  private static BibliographicGroupManager instance = null;

  public BibliographicGroupManager() {
    super();
    try {
      add(new RegExpGroup(true, true, "0[1-4]\\d"));
      add(new RegExpGroup(true, true, "0[5-8]\\d"));
      add(new RegExpGroup(false, false, "1\\d\\d"));
      add(new RegExpGroup(false, false, "245"));
      add(new RegExpGroup(true, true, "210|222|240|242|243|246|247"));
      add(new RegExpGroup(false, false, "260"));
      add(new RegExpGroup(false, false, "264"));
      add(new RegExpGroup(true, true, "3\\d\\d"));
      add(new RegExpGroup(true, false, "4\\d\\d"));
      add(new RegExpGroup(true, false, "5\\d\\d"));
      add(new RegExpGroup(true, false, "6\\d\\d"));
      add(new RegExpGroup(true, false, "700|710|711|720"));
      add(new RegExpGroup(true, false, "730|740"));
      add(new RegExpGroup(true, false, "8[0-2]\\d|830"));
      add(new RegExpGroup(true, true, "856"));
      add(new RegExpGroup(true, false, "99\\d"));
    } catch (RESyntaxException e) {
      logger.error("BibliographicGroupManager not properly initialized", e);
    }
  }

  @Deprecated
  public static BibliographicGroupManager getInstance() {
    if (instance == null) {
      instance = new BibliographicGroupManager();
    }
    return instance;
  }

  /* (non-Javadoc)
   * @see FilterManager#getFilter(Tag)
   */
  public TagFilter getFilter(Tag tag) throws DataAccessException {
    TagGroup group = getGroup(tag);
    if (group == null) return NO_TAG_FILTER;
    else if (group.isCanSort() && group.isSingleSort()) {
      return new SingleTagFilter(tag);
    } else if (group.isCanSort() && !group.isSingleSort()) {
      return new GroupTagFilter(group);
    } else return NO_TAG_FILTER;
  }

}
