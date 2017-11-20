package org.folio.cataloging.business.marchelper;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.marchelper.parser.PunctuationList;
import org.folio.cataloging.business.marchelper.parser.PunctuationParser;
import org.folio.cataloging.business.marchelper.parser.PunctuationParsingException;
import org.folio.cataloging.dao.persistence.TAG_GRP_MODEL;
import org.folio.cataloging.dao.persistence.TAG_MODEL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.util.StringText;
import org.folio.cataloging.dao.DAOGroupModel;

/**
 * This class is scope application, so it is 
 * accessed in multithreading mode
 * @author michelem
 *
 */
public class MarcHelperManager {
	private static final int MAIN_GROUP_LEVEL = 0;
	public static final int ADVANCED_GROUP_LEVEL = 1;
	public static final int BASIG_GROUP_LEVEL = 0;
	private static final char DB_PATTERN_SUBFIELD_DELIMITER = '$';
	
	private boolean enabled =  Defaults.getBoolean("marchelper.enabled");

	private static final Log logger = LogFactory.getLog(MarcHelperManager.class);

	private DAOGroupModel daoGroupModel = null;

	private Map groupsTable = null;

	private static MarcHelperManager instance = null;
	
	private MarcHelperManager() {
		super();
		daoGroupModel = new DAOGroupModel();
		groupsTable = Collections.synchronizedMap(new Hashtable());
	}

	/*
	 * initialized normally in LibrisuiteServlet
	 */
	public static void initInstance() {
		if(instance==null) {
			instance = new MarcHelperManager();
		}
//		logger.info(MarcHelperManager.class.getName()+" initialized");
	}

	public static MarcHelperManager getInstance(){
		if(instance==null){
			// used only for Test cases
			initInstance();
		}
		return instance;
	}
	
	public TAG_GRP_MODEL loadGroup(String tagNumber, int parent, String iso3Language) throws DataAccessException, PunctuationParsingException, NoMatchException {
		String groupKey = createGroupModelKey(parent, iso3Language, tagNumber);
		if(groupsTable.containsKey(groupKey)){
			return (TAG_GRP_MODEL) groupsTable.get(groupKey);
		}
		//ModelTagKey key = new ModelTagKey(, local.getISO3Language());
		TAG_GRP_MODEL group = TAG_GRP_MODEL.load(tagNumber, parent, iso3Language);
		if(group==null) {
			throw new NoMatchException("No TAG_GRP_MODEL loaded with key  ("+tagNumber+","+iso3Language+","+parent+")");
		}
		postProcess(group);
		return group;
	}

	/**
	 * MIKE: recursive procedure
	 * @param group
	 * @throws PunctuationParsingException
	 */
	private void postProcess(TAG_GRP_MODEL group) throws PunctuationParsingException {
		parseGroup(group);
		registerGroup(group);
		// MIKE: I assume child list always present
		Iterator childs = group.getChilds().iterator();
		while (childs.hasNext()) {
			TAG_GRP_MODEL subGroup = (TAG_GRP_MODEL) childs.next();
			postProcess(subGroup);
		}
	}

	private void parseGroup(TAG_GRP_MODEL group) throws PunctuationParsingException {
		List/*<TAG_MODEL>*/ models = group.getModels();
		Iterator it = models.iterator();
		while (it.hasNext()) {
			TAG_MODEL model = (TAG_MODEL) it.next();
			decorateModel(model);
		}
	}

	private void decorateModel(TAG_MODEL model)
			throws PunctuationParsingException {
		StringText patternStringText = model.getPatternStringText(DB_PATTERN_SUBFIELD_DELIMITER);
		PunctuationParser parser = new PunctuationParser(patternStringText);
		PunctuationList elements = parser.parse();
		model.setPunctuationElements(elements);
	}

	private void registerGroup(TAG_GRP_MODEL group) {
		String key = createGroupModelKey(group);
		if(!groupsTable .containsKey(key)){
			groupsTable.put(key,group);
		}
	}

	private String createGroupModelKey(TAG_GRP_MODEL group) {
		return "" + group.getParentSequence() + ":"
			+ group.getKey().getLanguage()+ ":"
			+ group.getCode();
	}
	
	private String createGroupModelKey(int parentSequence, String iso3Language, String tagNumber) {
		return parentSequence + ":"
			+ iso3Language+ ":"
			+ tagNumber;
	}

	public List getAllGroups() throws DataAccessException {
		return daoGroupModel.getAllBasicGroupModels();
	}
	
	/**
	 * 
	 * @param group
	 * @param tag
	 * @param local
	 * @param b 
	 * @return
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws TooManyModelsException 
	 * @throws NoMatchException 
	 * @throws MarcHelperException
	 */
	public TagModelItem createTagModelItem(TAG_GRP_MODEL group, StringText stringText, Locale local, String variantCodes, boolean isEditHeading) throws DataAccessException, MarcCorrelationException, NoMatchException, TooManyModelsException {
		TAG_MODEL matchingModel = group.getMatchingModel();
		return new TagModelItem(matchingModel, stringText, local, variantCodes, isEditHeading);
	}
	
	/**
	 * 
	 * @param tag
	 * @param local
	 * @return
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws MarcHelperException
	 */
	public TAG_GRP_MODEL selectGroup(MarcHelperTag tag, Locale local) throws DataAccessException, MarcCorrelationException, MarcHelperException{
		String tagNumber = tag.getKey();
		TAG_GRP_MODEL group = loadGroup(tagNumber, MAIN_GROUP_LEVEL, local.getISO3Language());
		group.createFilters(tag.getStringText());
		return group;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
