package org.folio.cataloging.business.common.group;

import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.filter.FilterManager;
import org.folio.cataloging.business.common.filter.GroupTagFilter;
import org.folio.cataloging.business.common.filter.NoTagFilter;
import org.folio.cataloging.business.common.filter.SingleTagFilter;
import org.folio.cataloging.business.common.filter.TagFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.regexp.RESyntaxException;


/**
 * Implementa, per i bibliografici, sia il GroupManager che il FilterManager
 * @author michele
 *
 */
public class BibliographicGroupManager extends MarcGroupManager implements FilterManager {
	private static final Log logger =
		LogFactory.getLog(BibliographicGroupManager.class);

	/**
	 * Filtro per tag che non possono essere ordinati. Il comportamento è sempre uguale
	 * per tutti i tag non ordinabili
	 */
	private static final TagFilter NO_TAG_FILTER = new NoTagFilter();

	private static BibliographicGroupManager instance = null;
	
	private BibliographicGroupManager() {
		super();
		try {
			add(new RegExpGroup(true,true,"0[1-4]\\d"));
			add(new RegExpGroup(true,true,"0[5-8]\\d"));
			add(new RegExpGroup(false,false,"1\\d\\d"));
			add(new RegExpGroup(false,false,"245"));
			add(new RegExpGroup(true,true,"210|222|240|242|243|246|247"));//titoli
		//	add(new RegExpGroup(true,true,"2[0-4]"));//titoli
			//aggiunto tag 260
			add(new RegExpGroup(false,false,"260"));
			add(new RegExpGroup(false,false,"264"));
			//add(new RegExpGroup(true,true,"2[5-6]\\d|270"));//250-270
			add(new RegExpGroup(true,true,"3\\d\\d"));
			add(new RegExpGroup(true,false,"4\\d\\d"));
			add(new RegExpGroup(true,false,"5\\d\\d"));
	//		add(new RegExpGroup(true,false,"^52[0124] "));
			add(new RegExpGroup(true,false,"6\\d\\d"));
			add(new RegExpGroup(true,false,"700|710|711|720"));//nomi
			add(new RegExpGroup(true,false,"730|740"));//titoli
			/*Per adesso le relazioni sono state disattivate 76X-78X*/
			//add(new RegExpGroup(false,false,"77\\d"));//titoli
			add(new RegExpGroup(true,false,"8[0-2]\\d|830"));//800-830
			add(new RegExpGroup(true,true,"856"));
			add(new RegExpGroup(true,false,"99\\d"));
		} catch (RESyntaxException e) {
			logger.error("BibliographicGroupManager not properly initialized", e);
			//throw new IllegalArgumentException(e);
		}
	}

	public static BibliographicGroupManager getInstance() {
		if(instance==null) {
			instance = new BibliographicGroupManager();
		}
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see FilterManager#getFilter(Tag)
	 */
	public TagFilter getFilter(Tag tag) throws MarcCorrelationException, DataAccessException {
		TagGroup group = getGroup(tag);
		if(group==null)return NO_TAG_FILTER;
		else if(group.isCanSort() && group.isSingleSort()) {
			return new SingleTagFilter(tag);
		} else if(group.isCanSort() && !group.isSingleSort()){
			return new GroupTagFilter(group);
		} else return NO_TAG_FILTER;
	}

}
