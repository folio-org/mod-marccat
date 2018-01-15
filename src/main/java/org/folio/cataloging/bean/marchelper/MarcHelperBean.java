package org.folio.cataloging.bean.marchelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.action.cataloguing.bibliographic.SaveTagException;
import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.bean.cataloguing.common.EditBean;
import org.folio.cataloging.bean.cataloguing.heading.HeadingBean;
import org.folio.cataloging.business.authorisation.AuthorisationException;
import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.marchelper.*;
import org.folio.cataloging.business.marchelper.parser.PunctuationParsingException;
import org.folio.cataloging.business.marchelper.parser.SampleMatcher;
import org.folio.cataloging.dao.persistence.TAG_GRP_MODEL;
import org.folio.cataloging.dao.persistence.TAG_MODEL;
import org.folio.cataloging.exception.DescriptorHasEmptySubfieldsException;
import org.folio.cataloging.exception.NoHeadingSetException;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.StringText;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MarcHelperBean extends LibrisuiteBean {
	private static final Log logger = LogFactory.getLog(MarcHelperBean.class);

	/**
	 * List Mode ADVANCED
	 */
	public static final String ADVANCED = "advanced";
	
	/**
	 * List Mode BASIC
	 */
	public static final String BASIC = "basic";
	/**
	 * List Mode FILTRO
	 */
	public static final String FILTRO = "filtro";
	/**
	 * List Mode OPEN
	 */
	public static final String OPEN = "open";
	/**
	 * List Mode CLOSE
	 */
	public static final String CLOSE = "close";

	/**
	 * Heading Mode: MarcHelper Close
	 */
	public static final String HMHCLOSE = "hmhClose";
	
	/**
	 * Tag Mode: MarcHelper Close
	 */
	public static final String MHCLOSE = "mhClose";
	/**
	 * List Mode CHANGE
	 */
	public static final String CHANGE = "change";

	public static final String SEARCH = "searchModel";

	/**
	 * MarcHelper List mode
	 */
	private String mode = BASIC;
	
	/**
	 * true if the tag editor should be marcHelper
	 * false if the tag editor should be the LibriCat standard editor
	 */
	private boolean smartHelperSelected = false;
	 
	/**
	 * true close the tag editor of marcHelper
	 * false open the tag editor of marcHelper
	 */
	private boolean closeHelperSelected = false;
	/**
	 * Current group for the current tag
	 */
	private TAG_GRP_MODEL currentGroup = null;
	
	Locale locale = null;

	private MarcHelperManager manager = null;
	private MarcHelperEditBean editor = null;
	private EditBean editBean = null;
	private HeadingBean headingBean = null;
	
	private String otherSubfieldCode;
	
	public String getOtherSubfieldCode() {
		return otherSubfieldCode;
	}

	public void setOtherSubfieldCode(String otherSubfieldCode) {
		this.otherSubfieldCode = otherSubfieldCode;
	}

	/**
	 * Public Testing Constructor
	 */
	public MarcHelperBean() {}

	public MarcHelperBean(MarcHelperManager manager, Locale locale) {
		super();
		this.locale = locale;
		this.manager = manager;
	}

	/**
	 * if the HeadingBean is present 
	 * then we are edit the Heading, 
	 * else we are edit the Tag
	 * @param request
	 * @return
	 */
	public static MarcHelperBean getInstance(HttpServletRequest request) {
		MarcHelperBean bean =
			(MarcHelperBean) MarcHelperBean.getSessionAttribute(
				request,
				MarcHelperBean.class);
		if (bean == null) {
			bean = new MarcHelperBean(MarcHelperManager.getInstance(), SessionUtils.getCurrentLocale(request));
			MarcHelperEditBean marcHelperEditBean = new MarcHelperEditBean(bean.getManager());
			bean.setEditor(marcHelperEditBean);
			bean.setSessionAttribute(request, MarcHelperBean.class);
		}
		// MIKE: MarcHelper is active for authority and mades too and they can be toggled
		bean.editBean = EditBean.getInstance(request);
		// MIKE: always checks if HeadingBean is present: 
		// TODO bean.headingBean = (HeadingBean) request.getHttpSession(false).getAttribute(EditHeadingAction.HEADING_BEAN_SESSION_NAME);
		return bean;
	}

	/**
	 * isEditTag = !isEditHeading
	 * @return true if we are in edit 'heading', false if we are in edit 'tag'
	 */
	public boolean isEditHeading(){
		return headingBean != null;
	}
	
	private void setMode(String modalita) {
		this.mode  = modalita;
	}

	public String getMode() {
		return mode;
	}

	/* public only for testing */
	public MarcHelperManager getManager() {
		return manager;
	}
	
	public List getAvailableModels(){
		if(!getCurrentGroup().isTagFilterActive()){
			return getCurrentGroup().getAllModels();
		} else {
			return getCurrentGroup().getModels();
		}
	}
	
	private void loadCurrentGroup() throws DataAccessException, NoMatchException, PunctuationParsingException{
		loadGroup(getMarcHelperTag());
	}
	
	/* public only for test */
	public void loadGroup(MarcHelperTag currentTag) throws DataAccessException, NoMatchException, PunctuationParsingException {
		String tagNumber = currentTag.getKey();
		int level = MarcHelperManager.BASIG_GROUP_LEVEL;
//		if(ADVANCED.equals(mode)) {
//			level = MarcHelperManager.ADVANCED_GROUP_LEVEL;
//		} 
		currentGroup = manager.loadGroup(tagNumber, level, locale.getISO3Language());
		createFilters();
	}

	public TAG_GRP_MODEL getCurrentGroup() {
		return currentGroup;
	}

	/**
	 * remove latest loaded group from the bean
	 */
	private void purgeGroup() {
		setCurrentGroup(null);
	}

	private boolean isTagMatchingOnlyOneModel(){
		createFilters();
		currentGroup.checkMatchingModels();
		return currentGroup.isTagMatchOnlyOneModel();
	}
	

	private void setCurrentGroup(TAG_GRP_MODEL currentGroup) {
		this.currentGroup = currentGroup;
	}

	public MarcHelperEditBean getEditor() {
		return editor;
	}

	private void setEditor(MarcHelperEditBean editor) {
		this.editor = editor;
	}

	private Locale getLocale() {
		return locale;
	}

	/**
	 * Create a new TagModelItem using an existent tag
	 * and searching the appropriate model
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws TooManyModelsException 
	 * @throws NoMatchException 
	 * @throws MarcHelperException
	 */
	private void createNewTagModelItem(TAG_GRP_MODEL filteredGroup) throws DataAccessException, NoMatchException, TooManyModelsException {
		TagModelItem tmi = manager.createTagModelItem(filteredGroup,getCurrentStringText(), getLocale(), getCurrentVariantCodes(), isEditHeading());
		editor.setItem(tmi);
	}
	
	/**
	 * Create a new TagModelItem when a sample is picked 
	 * @param groupNumber
	 * @param modelNumber
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws MarcHelperException
	 */
	private void createEmptyTagModelItem(int groupNumber, int modelNumber) throws DataAccessException {
		TAG_GRP_MODEL grp = currentGroup;
		if(ADVANCED.equals(mode)){
			grp = (TAG_GRP_MODEL) currentGroup.getChilds().get(groupNumber);
		} 
		TAG_MODEL model = (TAG_MODEL) grp.getModels().get(modelNumber);
		TagModelItem tmi = new TagModelItem(model, locale, getCurrentVariantCodes());
		editor.setItem(tmi);
	}
	
	/**
	 *
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws TooManyModelsException 
	 * @throws NoMatchException 
	 * @throws MarcHelperException
	 */
	private void registerNewTagModelItem() throws DataAccessException, NoMatchException, TooManyModelsException {
		Tag tag = getCurrentTag();
		if(tag instanceof MarcHelperTag && isTagMatchingOnlyOneModel()) {
			createNewTagModelItem(getCurrentGroup());
		} 
	}

	public boolean isSmartHelperSelected() {
		return smartHelperSelected;
	}

	private void setSmartHelperSelected(boolean smartHelper) {
		this.smartHelperSelected = smartHelper;
	}
	
	public boolean isMarcHelperAvailable(){
		return currentGroup != null && manager.isEnabled() && (getCurrentTag() instanceof MarcHelperTag); 
	}
	
	public boolean isItemAvailable(){
		return editor.isItemAvailable();
	}
	
	
	private void changeModel(int groupNumber, int modelNumber) throws DataAccessException, NoMatchException, TooManyModelsException {
		createEmptyTagModelItem(groupNumber, modelNumber);
		Map values = editor.getTempValues();
		if(values.isEmpty()){
			editor.getItem().forceStringTextValues(getCurrentStringText(), getCurrentVariantCodes());
		} else {
			editor.getItem().refresh(values);
		}
	}
	
	private void currentChanged() throws DataAccessException {
		if (isMarcHelperAvailable()) {
			createFilters();
		}
	}

	private void reset() {
		setSmartHelperSelected(false);
		purgeGroup();
		editor.reset();
	}

	public void toAdvanced() throws DataAccessException, NoMatchException, PunctuationParsingException {
		setMode(MarcHelperBean.ADVANCED);
		loadCurrentGroup();
	}

	public void toBasic() throws DataAccessException, NoMatchException, PunctuationParsingException {
		setMode(MarcHelperBean.BASIC);
		loadCurrentGroup();
	}

	private void createFilters() {
		if(isEditHeading()){
			getCurrentGroup().createFilters(getCurrentStringText(), getCurrentVariantCodes());
		} else {
			getCurrentGroup().createFilters(getCurrentStringText());
		}
	}

	public Tag getCurrentTag(){
		return editBean.getCurrentTag();
	}
	
	/**
	 * Extract the StringText from the tag(editTag) or from the heading(editHeading)
	 * @return
	 */
	private StringText getElementStringText(){
		if(isEditHeading()){
			return new StringText(headingBean.getHeading().getStringText());
		} else {
			return getMarcHelperTag().getStringText();
		}
	}
	
	/**
	 * TODO _MIKE: This method should be called only when the tag change (category or correlations)
	 * to minimize complexity
	 * @return
	 */
	private String getCurrentVariantCodes(){
		return getMarcHelperTag().getVariantCodes();
	}
	
	private MarcHelperTag getMarcHelperTag(){
		return (MarcHelperTag) getCurrentTag();
	}
	
	/**
	 * @param text
	 * @throws SaveTagException
	 */
	private void checkSubfieldsEmpty(StringText text) throws SaveTagException {
		List subfield = text.getSubfieldList();
		Iterator iter = subfield.iterator();
		while (iter.hasNext()) {
			Subfield sub = (Subfield) iter.next();
			if (sub.isEmpty()) {
				throw new SaveTagException("error.cataloguing.bibliographic.subfiledEmpty.save");
			}
		}
		
	}
	

	public void toTagMode(){
		// TODO: implement it
//		logger.info("toTagMode");
	}
	
	public void toHeadingMode()  {
//		logger.info("toHeadingMode");

	}


	/**
	 * @throws AuthorisationException
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws NoMatchException
	 * @throws TooManyModelsException
	 */
	private void processMatch(StringText text) throws AuthorisationException, DataAccessException, NoMatchException, TooManyModelsException {
		updateCurrentStringText(text);
		currentChanged();
	    registerNewTagModelItem();
	    setSmartHelperSelected(true);
	}

	/**
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws NoMatchException
	 * @throws PunctuationParsingException
	 */
	private void processNoMatch() throws DataAccessException, NoMatchException, PunctuationParsingException {
		currentChanged();
		currentGroup.setTagFilterActive(false);
		// MIKE: why do not registerNewTagModelItem() ?
		toBasic();
	}


	/* *****************************************************
	 * * ACTION EVENTS
	 * *****************************************************/
	
	/**
	 * Update specific StringText depending on isEditHeading to preserve user modified form data
	 * @param newStringText
	 * @throws AuthorisationException
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 */
	private void updateCurrentStringText(StringText newStringText) throws AuthorisationException, DataAccessException {
		if(isEditHeading()){
			StringText hdgStringText = newStringText.getSubfieldsWithoutCodes(getCurrentVariantCodes());
			headingBean.setStringText(hdgStringText);
			
		} else {
			editBean.changeText(newStringText);
		}
	}

	/**
	 * @throws MarcCorrelationException 
	 * @throws DataAccessException 
	 * @throws AuthorisationException 
	 * @throws PunctuationParsingException 
	 * @throws NoMatchException 
	 */
	private void beforeExitEditor(Map newValues) throws AuthorisationException, DataAccessException, NoMatchException, PunctuationParsingException {
		// si lasciano i dati inseriti, è necessario dunque aggiornare:
		// l'item corrente...
		if(getEditor().isItemAvailable()) {
			getEditor().refreshItem(newValues);
			// ... e poi l'oggetto corrente che ne deriva...
			StringText text = getEditor().getStringText();
			//updateCurrentStringText(text);
			if(isEditHeading()){
				StringText hdgStringText = text.getSubfieldsWithoutCodes(getCurrentVariantCodes());
				headingBean.setStringText(hdgStringText);
				
			}
			// after the changeText, tag and editor are sinchronized on stringText
			createFilters();
		}
	}

	/**
	 * @throws SaveTagException
	 * @throws DescriptorHasEmptySubfieldsException 
	 */
	private void checkHeadingFieldsEmpty() throws DescriptorHasEmptySubfieldsException {
		if(getEditor().getItem().isEmpty(false)) {
		  throw new DescriptorHasEmptySubfieldsException("error.cataloguing.bibliographic.subfiledEmpty.save");
		}
	}

	/**
	 * @return the specific StringText depending on isEditHeading
	 */
	private StringText getCurrentStringText() {
		StringText st = null;
		if(isEditHeading()){
			st = headingBean.getStringText();
			//st = new StringText(headingBean.getHeading().getStringText());
		} else {
			st = getMarcHelperTag().getStringText();
		}
		return st;
	}

	/**
	 * @throws SaveTagException
	 */
	private void checkValueModelItemEmpty() throws SaveTagException {
		if(getEditor().getItem().isEmpty(true)){
			throw new SaveTagException("error.cataloguing.bibliographic.subfiledEmpty.save");
		}
	}

	/**
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws NoMatchException
	 * @throws TooManyModelsException
	 * @throws PunctuationParsingException 
	 * @throws AuthorisationException 
	 */
	public void onBrowse(Map newValues) throws DataAccessException, NoMatchException, TooManyModelsException, AuthorisationException, PunctuationParsingException {
		if(isSmartHelperSelected()){
//		  currentChanged();
//		  registerNewTagModelItem();
			beforeExitEditor(newValues);
			toHeadingMode();
		}
	}

	/**
	 * match only the descriptor part of model/StringText
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws NoMatchException
	 * @throws TooManyModelsException
	 * @throws AuthorisationException 
	 * @throws PunctuationParsingException 
	 */
	public void onPickHdg() throws DataAccessException, NoMatchException, TooManyModelsException, AuthorisationException, PunctuationParsingException {
//		 //TODO: ottimizzare questo metodo in quanto viene fatto prima il checkMatchHdg();
		if (isSmartHelperSelected()) {
			// MIKE: it's correct to use getMarcHelperTag().getStringText()
			createFilters();
			StringText tagStringText = getMarcHelperTag().getStringText();
			StringText descriptorStringText = tagStringText.getSubfieldsWithoutCodes(getCurrentVariantCodes());
			TagModelItem item = editor.getItem();
			if(item==null) throw new NoMatchException("model not selected");
			boolean match = new SampleMatcher().deepMatch(descriptorStringText, item.getSelectedModel().getPunctuationElements().getPartialList(getCurrentVariantCodes()));
			if(!match) {
				if(isCloseHelperSelected()==false){
				   setCloseHelperSelected(true);
				  throw new NoMatchException();
				}
				else{
					setCloseHelperSelected(false);
					setSmartHelperSelected(false);
				}
			}
			if (isSmartHelperSelected())
			 item.populate(tagStringText, getCurrentVariantCodes(), true);
		}
		toTagMode();
	}

	
	
	 
	public void checkMatchHdg(Descriptor d) throws DataAccessException, NoMatchException, TooManyModelsException, AuthorisationException, PunctuationParsingException {
		if (isSmartHelperSelected()) {
			createFilters();
			StringText descriptorStringText = new StringText(d.getStringText());
			TagModelItem item = editor.getItem();
			if(item==null) throw new NoMatchException("model not selected");
			boolean match = new SampleMatcher().deepMatch(descriptorStringText, item.getSelectedModel().getPunctuationElements().getPartialList(getCurrentVariantCodes()));
			if(!match) {
				if(isCloseHelperSelected()==false){
				   setCloseHelperSelected(true);
				  throw new NoMatchException();
				}
				else{
					setCloseHelperSelected(false);
					setSmartHelperSelected(false);
				}
			}
			
		}
		toTagMode();
	}

	/**
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 */
	public void onChangeCategory() throws DataAccessException {
		if (isCanLoadGroup()) {
			try {
				loadCurrentGroup();
			} catch (NoMatchException e) {
				purgeGroup();
			} catch (PunctuationParsingException e) {
				purgeGroup();
			}
		} else {
			reset();
		}
	}
	
	private boolean isCanLoadGroup(){
		return manager.isEnabled() 
			&& getCurrentTag() instanceof MarcHelperTag; 
	}

	/**
	 * On saving we should update Editor and Tag
	 * @return
	 * @throws SaveTagException
	 * @throws AuthorisationException
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws NoHeadingSetException 
	 */
	public void onChangeText(Map valori) throws SaveTagException, AuthorisationException,
			DataAccessException, NoHeadingSetException {
		// update editor...
		getEditor().refreshItem(valori);
		StringText mhStringText = getEditor().getStringText();
		checkValueModelItemEmpty();
		checkSubfieldsEmpty(mhStringText);
		// update the StringText on specific object...
		updateCurrentStringText(mhStringText);
		checkHeading();
		// close and clean...
		setSmartHelperSelected(false);
		getEditor().reset();
	}

	/**
	 * Check only in tag mode
	 * @throws NoHeadingSetException 
	 */
	private void checkHeading() throws NoHeadingSetException {
		if(!isEditHeading()){
			editBean.checkHeading();
		}
	}

	/**
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws MarcHelperException
	 */
	public void onNewTag() throws DataAccessException, MarcHelperException {
		onEditTag();
	}

	/**
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws MarcHelperException
	 */
	public void onEditTag() throws DataAccessException, MarcHelperException {
		reset();
		Tag tag = getCurrentTag();
		if (tag instanceof MarcHelperTag) {
			try {
				TAG_GRP_MODEL group = manager.selectGroup((MarcHelperTag) tag, getLocale());
				setCurrentGroup(group);
				group.setTagFilterActive(false);
			} catch (NoMatchException e) {
				purgeGroup();
			}
		}
		setMode(BASIC);
		// force classic Libricat editor
		//setSmartHelperSelected(false);
	}

	public void onModelChanged(int groupNumber, int modelNumber) throws DataAccessException, NoMatchException, PunctuationParsingException, TooManyModelsException, AuthorisationException {
		changeModel(groupNumber, modelNumber) ;
		StringText  text = getEditor().getStringText();
		//updateCurrentStringText(text);
		if(isEditHeading()){
			StringText hdgStringText = text.getSubfieldsWithoutCodes(getCurrentVariantCodes());
			headingBean.setStringText(hdgStringText);
		} 
		getCurrentGroup().setTagFilterActive(false);
	}

	public void onChangeModel(Map valori) throws AuthorisationException, DataAccessException, NoMatchException, PunctuationParsingException {
		beforeExitEditor(valori);
		getCurrentGroup().setTagFilterActive(true);
	}

	public void onNewModelSelected(int groupNumber, int modelNumber) throws DataAccessException,
            NoMatchException, TooManyModelsException {
		if(getCurrentStringText().isEmpty()) {
	    	createEmptyTagModelItem(groupNumber, modelNumber);
	    } else {
	    	changeModel(groupNumber, modelNumber);
	    }
	    setSmartHelperSelected(true);
	    getCurrentGroup().setTagFilterActive(false);
	}

	/**
	 */
	public void onSearchRecord() {
		/*Carmen pulizia MarcHelper*/
		reset();
	}
	
	/**
	 * @throws PunctuationParsingException 
	 * @throws NoMatchException 
	 * @throws MarcCorrelationException 
	 * @throws DataAccessException 
	 */
	public void onSearchModel() throws DataAccessException, NoMatchException, PunctuationParsingException {
		toBasic();
		getCurrentGroup().setTagFilterActive(false);
	}

	/**
	 */
	public void onClose() {
		setSmartHelperSelected(false);
		setMode(BASIC);
	}


	public void onSaveHeading(Map newValues) throws AuthorisationException, DataAccessException, NoMatchException, PunctuationParsingException, DescriptorHasEmptySubfieldsException {
		beforeExitEditor(newValues);
		checkHeadingFieldsEmpty();
	}
	
	/**
	 * @param newStringText
	 * @return true if current tag match only one model
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws NoMatchException
	 * @throws PunctuationParsingException
	 * @throws AuthorisationException
	 * @throws TooManyModelsException
	 */
	public boolean onOpen(StringText newStringText) throws DataAccessException, NoMatchException, PunctuationParsingException, AuthorisationException, TooManyModelsException {
		boolean isMatchingOnlyOneModel = false;
		// TODO _MIKE: la prima volta gli editor.tempValues sono assenti.
		// Popolarli con lo StringText
		updateCurrentStringText(newStringText);
		loadCurrentGroup();
		// TODO _MIKE: use getCurrentStringText() or newStringText?
		boolean isEmptyCurrent = getCurrentStringText().isEmpty();
		if (!isEmptyCurrent) {
			isMatchingOnlyOneModel = isTagMatchingOnlyOneModel();
			if (isMatchingOnlyOneModel) {
				processMatch(newStringText);
			} else {
				processNoMatch();
			}
		} else if (isEmptyCurrent) {
			currentGroup.setTagFilterActive(false);
			toBasic();
		}
		return isMatchingOnlyOneModel;
	}

	public void onEditHeading() throws AuthorisationException, DataAccessException,
            NoMatchException, TooManyModelsException, PunctuationParsingException {
		if (isMarcHelperAvailable() && isItemAvailable()) {
			if (isMatchingSelectedModel()) {
				 getEditor().getItem().populateExcludingVariants(getCurrentStringText(), getCurrentVariantCodes());
			} else {
				 getEditor().getItem().forceStringTextValues(getCurrentStringText(), getCurrentVariantCodes());
				 if(isSmartHelperSelected()){
					 setSmartHelperSelected(false);
					// throw new NoMatchException();
				 }
			}
		}
	}
	
	private boolean isMatchingSelectedModel() {
		String variantCodes = isEditHeading()?getCurrentVariantCodes():null;
		return getEditor().getItem().isMatchingSelected(getCurrentStringText(), variantCodes);
	}

	/* *****************************************************
	 * * OTHER FEATURES
	 * *****************************************************/

	/**
	 * This method is here to centralize the logic in this bean.
	 * I don't like the 'request' here, but the getInstance use it too.
	 * @param request
	 * @return
	 */
	public Map extractValuesFromRequest(HttpServletRequest request) {
		Map valori = getEditor().getTempValues();
		SessionUtils.updateMap(request, valori);
		return valori;
	}
	
	/**
	 * @return the current category if we are in editHeading mode,
	 *  -1 if we are in tag mode
	 */
	public short getCurrentCategory() {
		if(isEditHeading()){
			return headingBean.getCategory();
		}
		return -1;
	}

	
	/* *****************************************************
	 * * DEAD CODE
	 * *****************************************************/

	/* MIKE: who use it? */
	public boolean isTagMatchingMoreModels(){
		createFilters();
		currentGroup.checkMatchingModels();
		return currentGroup.isTagMatchMoreModels();
	}

	public boolean isCloseHelperSelected() {
		return closeHelperSelected;
	}

	public void setCloseHelperSelected(boolean closeHelperSelected) {
		this.closeHelperSelected = closeHelperSelected;
	}

	public StringText createHeadingStringText(Map values, String variantCodes) {
		return getEditor().getItem().getStringText(values).getSubfieldsWithoutCodes(variantCodes);
	}

	/**
	 * @deprecated currently unused. Use {@link MarcHelperBean#changeCategory} instead
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws MarcHelperException
	 */
//	private void changeGroup() throws DataAccessException, MarcCorrelationException, MarcHelperException{
//		changeCategory();
//	}

	/**
	 * Extract the StringText from the descriptor(currentTag;editTag) or from the heading(editHeading)
	 * @return the tag.descriptor.stringText
	 */
//	public StringText getHeadingStringText(){
//		String st = null;
//		if(isEditHeading()){
//			st = headingBean.getHeading().getStringText();
//		} else {
//			st = getCurrentAccessPoint().getDescriptor().getStringText();
//		}
//		return new StringText(st);
//	}
	
	
	/**
	 * @deprecated attualmente non viene utilizzato in quanto viene chiamato direttamente
	 * il changeModel() che comunque crea un nuovo item e giustifica
	 * il fatto che non viene testata la presenza di un precedente item
	 */
//	private void selectModel(int groupNumber, int modelNumber) throws DataAccessException, MarcCorrelationException, TooManyModelsException, NoMatchException{
//		if(isItemAvailable()){
//			changeModel(groupNumber, modelNumber);
//		} else {
//			createEmptyTagModelItem(groupNumber, modelNumber);
//			editor.getItem().forceStringTextValues(getCurrentStringText(), getCurrentVariantCodes());
//		}
//	}

	 /**
	  * Use only for check 
	  * @param st
	  * @return
	  */
	private boolean isStringTextMatchingOnlyOneModel(StringText st){
		//currentGroup.createFilters(st, getCurrentVariantCodes());
		currentGroup.createFilters(st);
		currentGroup.checkMatchingModels();
		return currentGroup.isTagMatchOnlyOneModel();
	}

/**
	 * Use only for check 
	 * @param newStringText
	 * @return
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 * @throws NoMatchException
	 * @throws PunctuationParsingException
	 * @throws AuthorisationException
	 * @throws TooManyModelsException
	 */
	public boolean isMatching(StringText newStringText) throws DataAccessException, NoMatchException, PunctuationParsingException, AuthorisationException, TooManyModelsException {
		loadCurrentGroup();
		if (!newStringText.isEmpty()) {
			return isStringTextMatchingOnlyOneModel(newStringText);
		} 
		return false;
	}

}
