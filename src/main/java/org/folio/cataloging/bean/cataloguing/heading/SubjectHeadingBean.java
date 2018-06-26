package org.folio.cataloging.bean.cataloguing.heading;

import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListsBean;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.dao.BibliographicCorrelationDAO;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.dao.DAOSubjectTerm;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.CorrelationValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unchecked")
public class SubjectHeadingBean extends HeadingBean {

	private List skipInFilingList = new ArrayList();
	private List subjectSourceList = new ArrayList();
	private List subjectSecondarySourceList = new ArrayList();
	private List subjectTypeList = new ArrayList();
	private List languageOfAccessPointList = new ArrayList();
	private List subjectTermTypList = new ArrayList();
	private int codeTerm;
    //SUGGERIMENTO
	private String tagSubject;
	private List tagGroupSubject = new ArrayList();
   
	public int getCodeTerm() {
		return codeTerm;
	}

	public void setCodeTerm(int codeTerm) {
		this.codeTerm = codeTerm;
	}

	public List getSubjectTermTypList() {
		return subjectTermTypList;
	}

	public void setSubjectTermTypList(List subjectTermTypList) {
		this.subjectTermTypList = subjectTermTypList;
	}

	public List getLanguageOfAccessPointList() {
		return languageOfAccessPointList;
	}

	public void setLanguageOfAccessPointList(List languageOfAccessPointList) {
		this.languageOfAccessPointList = languageOfAccessPointList;
	}
	private SBJCT_HDG subjectHeading = new SBJCT_HDG(); 

//	TODO Carmen Non sono convinta della scelta di mettere fisso il valore = 4
	private short tagCategory=4;

	public SubjectHeadingBean() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see HeadingBean#getHeading()
	 */
	public Descriptor getHeading() {
		return subjectHeading;
	}

	/**
	 * @return
	 */
	public Integer getCopyFromHeadingNumber() {
		return subjectHeading.getCopyFromHeadingNumber();
	}

	/**
	 * @return
	 */
	public String getCopyFromHeadingType() {
		return subjectHeading.getCopyFromHeadingType();
	}

	/**
	 * @return
	 */
	public String getSecondarySourceCode() {
		String secondarySourceCode = subjectHeading.getSecondarySourceCode();
		if ((secondarySourceCode != null) && (!("".equals(secondarySourceCode)))) {
			return (new Subfield(secondarySourceCode)).getContent();
		} else {
			return "";
		}
	}

	/**
	 * @return
	 */
	public int getSkipInFiling() {
		return subjectHeading.getSkipInFiling();
	}

	/**
	 * @return
	 */
	public int getSourceCode() {
		return subjectHeading.getSourceCode();
	}

	/**
	 * @return
	 */
	public int getTypeCode() {
		return subjectHeading.getTypeCode();
	}

	/**
	 * @param integer
	 */
	public void setCopyFromHeadingNumber(Integer integer) {
		subjectHeading.setCopyFromHeadingNumber(integer);
	}

	/**
	 * @param string
	 */
	public void setCopyFromHeadingType(String string) {
		subjectHeading.setCopyFromHeadingType(string);
	}

	/**
	 * @param string
	 */
	public void setSecondarySourceCode(String string) {
		if ((string != null) && (!("".equals(string)))) {
			subjectHeading.setSecondarySourceCode((new Subfield("2",string)).toString());
		}
	}

	/**
	 * @param i
	 */
	public void setSkipInFiling(int i) {
		subjectHeading.setSkipInFiling(i);
	}

	/**
	 * @param s
	 */
	public void setSourceCode(int s) {
		subjectHeading.setSourceCode(s);
	}

	/**
	 * @param s
	 */
	public void setTypeCode(int s) {
		subjectHeading.setTypeCode(s);
	}

	/**
	 * @return
	 */
	public List getSkipInFilingList() {
		return skipInFilingList;
	}

	/**
	 * @return
	 */
	public SBJCT_HDG getSubjectHeading() {
		return subjectHeading;
	}

	/**
	 * @param list
	 */
	public void setSkipInFilingList(List list) {
		skipInFilingList = list;
	}

	protected void setHeading(Descriptor d) {
		if (!(d instanceof SBJCT_HDG)) throw new IllegalArgumentException("I can only set descriptors of type SBJCT_HDG");
		subjectHeading = (SBJCT_HDG) d;
	}
	
	/*protected void onPopulateLists(DAOCodeTable dao, Locale l)
		throws DataAccessException {
		setSkipInFilingList(dao.getOptionList(T_SKP_IN_FLNG_CNT.class, l));
		setSubjectSourceList(dao.getOptionList(SubjectSource.class, l));
		setSubjectTypeList(dao.getOptionList(SubjectType.class, l));
		setSubjectSecondarySourceList(
			dao.getOptionList(T_SBJCT_HDG_SCDRY_SRC.class, l));
	}*/
	protected void onPopulateLists(DAOCodeTable dao, Locale l)
	  throws DataAccessException {
		setLanguageOfAccessPointList(
				dao.getOptionList(T_LANG_OF_ACS_PNT_SBJCT.class, l));
	  setSkipInFilingList(dao.getOptionList(T_SKP_IN_FLNG_CNT.class, l));
	  setSubjectTypeList(dao.getOptionList(SubjectType.class, l));
	  setSubjectTermTypList(dao.getOptionList(T_SBJCT_TRM_TYP.class, l));
	  BibliographicCorrelationDAO daoBib = new BibliographicCorrelationDAO();
	  setSubjectSourceList(dao.getOptionList(SubjectSource.class, l));
	  setSubjectSecondarySourceList(
	  dao.getOptionList(T_SBJCT_HDG_SCDRY_SRC.class, l));
	  setTagGroupSubject(getTagGroupSubject(l));
   }

	/**
	 * @return
	 */
	public List getSubjectSourceList() {
		return subjectSourceList;
	}

	/**
	 * @return
	 */
	public List getSubjectTypeList() {
		return subjectTypeList;
	}

	/**
	 * @param list
	 */
	public void setSubjectSourceList(List list) {
		subjectSourceList = list;
	}

	/**
	 * @param list
	 */
	public void setSubjectTypeList(List list) {
		subjectTypeList = list;
	}

	/**
	 * @return
	 */
	public List getSubjectSecondarySourceList() {
		return subjectSecondarySourceList;
	}

	/**
	 * @param list
	 */
	public void setSubjectSecondarySourceList(List list) {
		subjectSecondarySourceList = list;
	}
	
	public boolean isOtherSource() {
		return SubjectSource.isOtherSource(getSourceCode());
	}
	  public void setTypeValues(CorrelationValues correlationValues, short category) {
		   /*Pagina di edit della heading serve solamente la I e la III correlation*/
			setTypeCode(correlationValues.getValue(1));
		    setSourceCode(correlationValues.getValue(3));
			tagCategory=category;
		}
	   
	  public void saveSubjectTerm(int headingNumber,SubjectTerm  subjectTerm) throws DataAccessException{
		  DAOSubjectTerm dao = new DAOSubjectTerm();
		  //SubjectTerm  subjectTerm= new SubjectTerm(); 
		  dao.persist(headingNumber, subjectTerm);
		  
	  }
      
	  public SubjectTerm loadSubjectTerm(int headingNumber) throws DataAccessException{
		  SubjectTerm subjectTerm = null;
		  DAOSubjectTerm dao = new DAOSubjectTerm();
		  //SubjectTerm  subjectTerm= new SubjectTerm(); 
		  List subjList= dao.loadSubjectTerm(headingNumber);
		  if(subjList.size()==1)
			  subjectTerm= (SubjectTerm)subjList.get(0);
		  return subjectTerm;
		  
	  }
	  
	  public void refreshQualifier() throws DataAccessException{
		  SubjectTerm subjectTerm = loadSubjectTerm(getHeading().getKey().getHeadingNumber());
		  if (subjectTerm != null)
				setCodeTerm(subjectTerm.getCodeTerm());
		  
	  }
	  
	//SUGGERIMENTO
	   
	   public List getTagGroupSubject(Locale l) {
			return CodeListsBean.getTagGroupSubjectType().getCodeList(l);
		}

		public List getTagGroupSubject() {
			return tagGroupSubject;
		}

		public void setTagGroupSubject(List tagGroupSubject) {
			this.tagGroupSubject = tagGroupSubject;
		}
			 
		public String getTagSubject() {
			return tagSubject;
		}

		public void setTagSubject(String tagSubject) {
			this.tagSubject = tagSubject;
		}
}
