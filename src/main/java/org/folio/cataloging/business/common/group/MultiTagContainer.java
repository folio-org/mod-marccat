package org.folio.cataloging.business.common.group;

import org.folio.cataloging.business.cataloguing.common.Tag;

import java.util.*;

public class MultiTagContainer implements TagContainer {
  private List tags = new ArrayList ( );

  public void add(Tag tag) {
    tags.add (tag);
  }

  public Tag getLeaderTag() {
    if (tags.isEmpty ( )) return null;
    return (Tag) tags.get (0);
  }

  public Iterator iterator() {
    return tags.iterator ( );
  }

  public int compareTo(Object arg0) {
    try {
      TagContainer tc0 = (TagContainer) arg0;
      return this.getLeaderTag ( ).getMarcEncoding ( ).getMarcTag ( )
        .compareTo (tc0.getLeaderTag ( ).getMarcEncoding ( ).getMarcTag ( ));
    } catch (Exception e) {
      return 0;
    }
  }

  public Collection getList() {
    return tags;
  }

  public void sort() {
    Collections.sort (tags, new SequenceNumberComparator ( ));
  }

}
