/*
 * (c) LibriCore
 * 
 * Created on 21-jun-2004
 * 
 * SHLF_LIST.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.business.marchelper.DAOGroupModel;
import librisuite.business.marchelper.MatcherPredicate;
import librisuite.business.marchelper.NoMatchException;
import librisuite.business.marchelper.TagModelComparator;
import librisuite.business.marchelper.TagModelPredicate;
import librisuite.business.marchelper.TooManyModelsException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.libricore.librisuite.common.StringText;

public class TAG_GRP_MODEL implements Serializable {

	public class TagModelComplexityPredicate implements Predicate {
		private int complexity = 0;
		public TagModelComplexityPredicate(TAG_MODEL refModel){
			complexity = refModel.getComplexity();
		}

		public boolean evaluate(Object tagModel) {
			return complexity == ((TAG_MODEL)tagModel).getComplexity();
		}
	}

	private static DAOGroupModel daoGroupModel = new DAOGroupModel();
	//private Predicate predicate = null;
	
	private List/*<TAG_MODEL>*/ models;
	private List/*<TAG_GRP_MODEL>*/ childs = new ArrayList();
	private List/*<TAG_MODEL>*/ allModelsCache;

	private String descriptionText;
	private int parentSequence;
	private ModelTagKey key;
	private char obsoleteIndicator;
	private String code;
	private Predicate tagFilter = null;
	private MatcherPredicate matchCondition = null;

	private boolean tagFilterActive;
	private boolean tagMatchOnlyOneModel;
	private boolean tagMatchMoreModels;
	
//	/**
//	 * @param code
//	 * @param parentSequence
//	 * @param locale
//	 */
//	public TAG_GRP_MODEL(int code, int parentSequence, String lang) {
//		super();
//		this.parentSequence = parentSequence;
//		this.key.setCode(code);
//		this.key.setLanguage(lang);
//	}
	
	public TAG_GRP_MODEL() {
		super();
		
	}
	
	
	public TAG_GRP_MODEL(ModelTagKey key, int parentSequence) {
		super();
		this.parentSequence = parentSequence;
		this.key = key;
	}
	
	public TAG_GRP_MODEL(ModelTagKey key) {
		super();
		this.key = key;
	}
	public char getObsoleteIndicator() {
		return obsoleteIndicator;
	}
	public void setObsoleteIndicator(char obsoleteIndicator) {
		this.obsoleteIndicator = obsoleteIndicator;
	}
	public String getDescriptionText() {
		return descriptionText;
	}
	public void setDescriptionText(String descriptionText) {
		this.descriptionText = descriptionText;
	}
	public int getParentSequence() {
		return parentSequence;
	}
	public void setParentSequence(int parentSequence) {
		this.parentSequence = parentSequence;
	}
	
	protected DAOGroupModel getDao() {
		return daoGroupModel;
	}
	
	/**
	 * Load another sample using this object as key
	 * @param parent 
	 * @param key2 
	 * @param key
	 * @return
	 * @throws DataAccessException
	 */
	public static TAG_GRP_MODEL load(String codeTag,int parent,String iso3Language) throws DataAccessException{
		TAG_GRP_MODEL group = daoGroupModel.getGroupModel(codeTag, parent, iso3Language);
		if(group==null) return null;
		group.setModels(daoGroupModel.getModelList(codeTag, parent, iso3Language));
		group.setChilds(daoGroupModel.getChildList(codeTag,iso3Language));
		return group;
	}
	
	public List getModels() {
		if(!tagFilterActive){
			return getAllModels();
		}
		return (List) CollectionUtils.select(models, tagFilter);
	}

	public List/*<TAG_MODEL>*/ getAllModels() {
		List allModels = generateAllModelsList();
		if(!tagFilterActive){
			return allModels;
		}
		return (List) CollectionUtils.select(allModels, tagFilter);
	}
	
	/**
	 * Recursive method
	 * @return
	 */
	private List/*<TAG_MODEL>*/ generateAllModelsList(){
		if(allModelsCache!=null) {
			return allModelsCache;
		}
		List allModelsCache = new ArrayList();
		allModelsCache.addAll(models);
		Iterator it = childs.iterator();
		while (it.hasNext()) {
			TAG_GRP_MODEL group = (TAG_GRP_MODEL) it.next();
			allModelsCache.addAll(group.getAllModels()); //recursion
		}
		return allModelsCache;
	}

	public TAG_MODEL getMatchingModel() throws NoMatchException, TooManyModelsException {
		List l = (List) CollectionUtils.select(models, matchCondition);
		if(l.size()==0) {
			throw new NoMatchException("No models match with "+matchCondition); // TODO _MIKE: change message!
		}
		else if(l.size()>1) {
			List moreComplex = selectMoreSimple(l);
			if(moreComplex.size()>1) {
				throw new TooManyModelsException("Too samples match: "+moreComplex.toString()); // TODO _MIKE: change message!
			} else {
				l = moreComplex;
			}
		}
		return (TAG_MODEL) l.get(0);
	}

	private List selectMoreSimple(List unsortedlist) {
		Comparator tmComparator = new TagModelComparator();
		Collections.sort(unsortedlist, tmComparator);
		Collections.reverse(unsortedlist);// comment this method to obtain the more complex instead
		// check for multiple models with same complexity:
		Predicate cplxPredicate = new TagModelComplexityPredicate((TAG_MODEL) unsortedlist.get(0));
		List selectedList = (List) CollectionUtils.select(unsortedlist, cplxPredicate);
		return selectedList;
	}

	public void createFilters(StringText stringText) {
		matchCondition = new MatcherPredicate(stringText);
		tagFilter = new TagModelPredicate(stringText);
	}
	
	public void createFilters(StringText stringText, String excludeCodes) {
		matchCondition = new MatcherPredicate(stringText, excludeCodes);
		tagFilter = new TagModelPredicate(stringText);
	}

	public void checkMatchingModels(){
		try {
			tagMatchOnlyOneModel = getMatchingModel()!=null;
		} catch (NoMatchException e) {
			// no match
			tagMatchOnlyOneModel = false;
			tagMatchMoreModels = false;
		} catch (TooManyModelsException e) {
			tagMatchOnlyOneModel = false;
			tagMatchMoreModels = true;
		}
	}
	
   public void setModels(List models) {
		this.models = models;
	}

	protected DAOGroupModel getDaoGroupModel() {
		return daoGroupModel;
	}
	
	public void setTagFilterActive(boolean tagFilterActive) {
		this.tagFilterActive = tagFilterActive;
	}
	public String toString() {
		return "" + key.getSequence() + ": "
		+ getCode() + ", "
		+ key.getLanguage() + ", "
		+ parentSequence + ", "
		+ descriptionText;
	}
	public List getChilds() {
		return childs;
	}
	public void setChilds(List childs) {
		this.childs = childs;
	}
	public ModelTagKey getKey() {
		return key;
	}
	public void setKey(ModelTagKey key) {
		this.key = key;
	}


	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}


	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TAG_GRP_MODEL other = (TAG_GRP_MODEL) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public boolean isTagMatchOnlyOneModel() {
		return tagMatchOnlyOneModel;
	}


	public void setTagMatchOnlyOneModel(boolean tagMatchOnlyOneModel) {
		this.tagMatchOnlyOneModel = tagMatchOnlyOneModel;
	}


	public boolean isTagMatchMoreModels() {
		return tagMatchMoreModels;
	}


	public void setTagMatchMoreModels(boolean tagMatchMoreModels) {
		this.tagMatchMoreModels = tagMatchMoreModels;
	}


	public boolean isTagFilterActive() {
		return tagFilterActive;
	}	
}
