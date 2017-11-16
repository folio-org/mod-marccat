package com.casalini.cataloguing.bean;

import librisuite.bean.LibrisuiteBean;
import librisuite.bean.cataloguing.bibliographic.codelist.CarrierType;
import librisuite.bean.cataloguing.bibliographic.codelist.CollectionCst;
import librisuite.bean.cataloguing.bibliographic.codelist.CollectionMst;
import librisuite.bean.cataloguing.bibliographic.codelist.CollectionPub;
import librisuite.bean.cataloguing.bibliographic.codelist.CollectionPubLvl;
import librisuite.bean.cataloguing.bibliographic.codelist.CollectionType;
import librisuite.bean.cataloguing.bibliographic.codelist.ContentType;
import librisuite.bean.cataloguing.bibliographic.codelist.CustomerType;
import librisuite.bean.cataloguing.bibliographic.codelist.DigitalCurcyType;
import librisuite.bean.cataloguing.bibliographic.codelist.DigitalItemSalType;
import librisuite.bean.cataloguing.bibliographic.codelist.DigitalLevelType;
import librisuite.bean.cataloguing.bibliographic.codelist.DigitalSalType;
import librisuite.bean.cataloguing.bibliographic.codelist.GeographicSubdivisionType;
import librisuite.bean.cataloguing.bibliographic.codelist.LevelType;
import librisuite.bean.cataloguing.bibliographic.codelist.ManagerialLevelType;
import librisuite.bean.cataloguing.bibliographic.codelist.MediaType;
import librisuite.bean.cataloguing.bibliographic.codelist.NoteDefaultTranslation;
import librisuite.bean.cataloguing.bibliographic.codelist.NoteStandardType;
import librisuite.bean.cataloguing.bibliographic.codelist.NoteTranslation;
import librisuite.bean.cataloguing.bibliographic.codelist.OnlinePolicyType;
import librisuite.bean.cataloguing.bibliographic.codelist.SpecialSubdivisionType;
import librisuite.bean.cataloguing.bibliographic.codelist.StatusAvailable;
import librisuite.bean.cataloguing.bibliographic.codelist.StatusCollectionType;
import librisuite.bean.cataloguing.bibliographic.codelist.TimeSubdivisionType;

public class CasaliniCodeListsBean extends LibrisuiteBean 
{
	private static SpecialSubdivisionType specialSubdivisionType = new SpecialSubdivisionType();
	public static SpecialSubdivisionType getSpecialSubdivisionType() {
		return specialSubdivisionType;
	}

	private static GeographicSubdivisionType geographicSubdivisionType = new GeographicSubdivisionType();
	public static GeographicSubdivisionType getGeographicSubdivisionType() {
		return geographicSubdivisionType;
	}
	
	private static NoteStandardType noteStndardType = new NoteStandardType();
	public static NoteStandardType getNoteStandardType() {
		return noteStndardType;
	}
	
	private static ManagerialLevelType managerialLevelType = new ManagerialLevelType();
	public static ManagerialLevelType getManagerialLevelType() {
		return managerialLevelType;
	}
	
	private static StatusCollectionType statusCollectionType = new StatusCollectionType();
	public static StatusCollectionType getStatusCollectionType() {
		return statusCollectionType;
	}
	
	private static CollectionMst collectionMst = new CollectionMst();
	public static CollectionMst getCollectionMstType() {
		return collectionMst;
	}
	
	private static CollectionCst collectionCst = new CollectionCst();
	public static CollectionCst getCollectionCstType() {
		return collectionCst;
	}
	
	private static CollectionType collectionType = new CollectionType();
	public static CollectionType getCollectionType() {
		return collectionType;
	}
	
	private static CustomerType customerType = new CustomerType();
	public static CustomerType getCustomerType() {
		return customerType;
	}
	
	private static DigitalLevelType digitalLevelType = new DigitalLevelType();
	public static DigitalLevelType getDigitalLevelType() {
		return digitalLevelType;
	}
	
	private static LevelType levelType = new LevelType();
	public static LevelType getLevelType() {
		return levelType;
	}

	private static DigitalSalType digitalSalType = new DigitalSalType();
	public static DigitalSalType getDigitalSalType() {
		return digitalSalType;
	}
	
	private static DigitalCurcyType digitalCurcyType = new DigitalCurcyType();
	public static DigitalCurcyType getDigitalCurcyType() {
		return digitalCurcyType;
	}
	
	private static DigitalItemSalType digitalItemSalType = new DigitalItemSalType();
	public static DigitalItemSalType getDigitalItemSalType() {
		return digitalItemSalType;
	}
	
	private static TimeSubdivisionType timeSubdivisionType = new TimeSubdivisionType();
	public static TimeSubdivisionType getTimeSubdivisionType() {
		return timeSubdivisionType;
	}
	
	private static NoteTranslation translation = new NoteTranslation();
	public static NoteTranslation getNoteTranslation() {
		return translation;
	}
	
	private static NoteDefaultTranslation defaultTranslation = new NoteDefaultTranslation();
	public static NoteDefaultTranslation getNoteDefaultTranslation() {
		return defaultTranslation;
	}
	
	private static CollectionPub collectionPub = new CollectionPub();
	public static CollectionPub getCollectionPubType() {
		return collectionPub;
	}
	
	private static CollectionPubLvl collectionPubLvl = new CollectionPubLvl();
	public static CollectionPubLvl getCollectionPubLvlType() {
		return collectionPubLvl;
	}

	private static OnlinePolicyType onlinePolicyType = new OnlinePolicyType();
	public static OnlinePolicyType getOnlinePolicyType() {
		return onlinePolicyType;
	}
	
	private static StatusAvailable statusAvailable = new StatusAvailable();
	public static StatusAvailable getStatusAvailable() {
		return statusAvailable;
	}
	
//	private static WorkingCodeType workingCodeType = new WorkingCodeType();
//	public static WorkingCodeType getWorkingCodeType() {
//		return workingCodeType;
//	}
	
//	20130805: aggiunta tabella di decodifica T_RDA_CONTENT
	private static ContentType contentType = new ContentType();
	
	public static ContentType getContentType() {
		return contentType;
	}
//	20130805: aggiunta tabella di decodifica T_RDA_MEDIA
	private static MediaType mediaType = new MediaType();
	
	public static MediaType getMediaType() {
		return mediaType;
	}
//	20130805: aggiunta tabella di decodifica T_RDA_CARRIER
	private static CarrierType carrierType = new CarrierType();
	
	public static CarrierType getCarrierType() {
		return carrierType;
	}
}
