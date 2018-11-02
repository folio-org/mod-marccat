package org.folio.marccat.business.common.group;

import org.folio.marccat.business.cataloguing.common.Tag;

import java.util.Collection;
import java.util.Iterator;

public interface TagContainer extends Comparable {
  Tag getLeaderTag();

  void add(Tag tag);

  Iterator iterator();

  Collection getList();

  void sort();
}
