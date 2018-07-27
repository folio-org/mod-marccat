package org.folio.cataloging.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.F;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.codetable.IndexListElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.searching.InvalidBrowseIndexException;
import org.folio.cataloging.dao.*;
import org.folio.cataloging.dao.common.HibernateSessionProvider;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.Heading;
import org.folio.cataloging.resources.domain.RecordTemplate;
import org.folio.cataloging.shared.CodeListsType;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.MapHeading;
import org.folio.cataloging.shared.Validation;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.folio.cataloging.F.locale;

/**
 * Storage layer service.
 * This is the interface towards our storage service. Any R/W access to the persistence layer needs to pass through
 * this interface.
 *
 * @author agazzarini
 * @author carment
 * @author nbianchini
 * @since 1.0
 */
public class StorageService implements Closeable {

    private final Session session;
    private static final Log logger = new Log(StorageService.class);

    private final static Map<Integer, Class> FIRST_CORRELATION_HEADING_CLASS_MAP = new HashMap<Integer, Class>(){
        {
            put(1, T_BIB_HDR.class);
            put(2, NameType.class);
            put(17, NameType.class); //from heading
            put(3, TitleFunction.class);
            put(4, SubjectType.class);
            put(18, SubjectType.class); //from heading
            put(5, ControlNumberType.class);
            put(19, ControlNumberType.class); //from heading
            put(6, ClassificationType.class);
            put(20, ClassificationType.class); //from heading
            put(7, BibliographicNoteType.class); //note
            put(8, BibliographicRelationType.class);//relationship
            put(11, T_NME_TTL_FNCTN.class); //nt
        }
    };

    private final static Map<Integer, Class> SECOND_CORRELATION_CLASS_MAP = new HashMap<Integer, Class>(){
        {
            put(2, NameSubType.class);
            put(3, TitleSecondaryFunction.class);
            put(4, SubjectFunction.class);
            put(5, ControlNumberFunction.class);
            put(6, ClassificationFunction.class);
            put(11, NameType.class);
        }
    };

    private final static Map<Integer, Class> THIRD_CORRELATION_HEADING_CLASS_MAP = new HashMap<Integer, Class>(){
        {
            put(2, NameFunction.class);
            put(4, SubjectSource.class);
            put(11, NameSubType.class);
        }
    };

    /**
     * Builds a new {@link StorageService} with the given session.
     *
     * @param session the Hibernate session, which will be used for gathering a connection to the RDBMS.
     */
    StorageService(final Session session) {
        this.session = session;
    }

    /**
     * Returns the logical views associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the logical view associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getLogicalViews(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, DB_LIST.class, locale(lang));
    }

    /**
     * Returns the note types associated to the given language
     * and with the given note group type code.
     *
     * @param noteGroupTypeCode the note group type used here as filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the note type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getNoteTypesByGroupTypeCode(final String noteGroupTypeCode, final String lang) throws DataAccessException {
        final DAOBibliographicCorrelation daoBibliographicCorrelation = new DAOBibliographicCorrelation();
        return daoBibliographicCorrelation.getFirstCorrelationByNoteGroupCode(session, noteGroupTypeCode, locale(lang));
    }

    /**
     * Returns the record status types associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the record status type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getRecordStatusTypes(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_ITM_REC_STUS.class, locale(lang));
    }

    /**
     * Returns the acquisition types associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the acquisition type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getAcquisitionTypes(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_ORDR_AQSTN_TYP.class, locale(lang));
    }

    /**
     * Returns the multipart resource level associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the multipart resource level associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getMultipartResourceLevels(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_ITM_LNK_REC.class, locale(lang));
    }

    /**
     * Returns the record types associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the record type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getRecordTypes(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_ITM_REC_TYP.class, locale(lang));
    }

    /**
     * Returns the currencies associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the currency associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getCurrencies(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_CURCY_TYP.class, locale(lang));
    }

    /**
     * Returns the subscription associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the subscription associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getSubscriptions(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_HLDG_SBCPT_STUS.class, locale(lang));
    }

    /**
     * Returns the record display format associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the record display format associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getRecordDisplayFormats(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, RecordItemDisplay.class, locale(lang));
    }

    /**
     * Returns the verification levels associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the verification level associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getVerificationLevels(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_VRFTN_LVL.class, locale(lang));
    }

    /**
     * Returns the shelf list types associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the shelf list types associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getShelfListTypes(final String lang) {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_SHLF_LIST_TYP.class, locale(lang));
    }

    /**
     * Returns the detail levels associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the detail levels associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getDetailLevels(final String lang) {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_HLDG_LVL_OF_DTL.class, locale(lang));
    }

    /**
     * Returns the retentions associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the retentions associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getRetentions(final String lang) {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_SRL_GENRETENTION.class, locale(lang));
    }

    /**
     * Returns the status types associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the status types associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getStatusTypes(final String lang) {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_HLDG_STUS_TYP.class, locale(lang));
    }

    /**
     * Returns the series treatment types associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the series treatment types associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getSeriesTreatmentTypes(String lang) {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_HLDG_SRS_TRMT.class, locale(lang));
    }

    /**
     * Returns the authority sources associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the suthority sources associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getAuthoritySources(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_AUT_HDG_SRC.class, locale(lang));
    }

    /**
     * Returns the language types associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the language type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getLanguageTypes(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_LANG.class, locale(lang));
    }

    /**
     * Returns the material types associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the material type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getMaterialTypes(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_MTRL_TYP .class, locale(lang));
    }

    /**
     * Returns a list of all categories belonging to the requested type.
     *
     * @param type the index type, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return a list of categories by index type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<Integer>> getIndexCategories(final String type, final String lang) throws DataAccessException {
        final DAOSearchIndex searchIndexDao = new DAOSearchIndex();
        return searchIndexDao.getIndexCategories(session, type, locale(lang));
    }

    /**
     * Returns the categories belonging to the requested type.
     *
     * @param type the index type, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return a list of categories by index type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getIndexes(final String type, final int categoryCode, final String lang) throws DataAccessException {
        DAOSearchIndex searchIndexDao = new DAOSearchIndex();
        return searchIndexDao.getIndexes(session, type, categoryCode, locale(lang));
    }

    /**
     * Return the item type list.
     *
     * @param code the heading type code (first correlation), used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @param category the category of field, used here as a filter criterion.
     * @return a list of item type codes associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getSecondCorrelation(final int category, final int code, final String lang) throws DataAccessException {

        final DAOBibliographicCorrelation daoBC = new DAOBibliographicCorrelation();
        final Class subTypeClass = SECOND_CORRELATION_CLASS_MAP.get(category);
        return daoBC.getSecondCorrelationList(session, category, code, subTypeClass, locale(lang));
    }

    /**
     * Return the function code list.
     *
     * @param category the category of field, used here as a filter criterion.
     * @param code1 the heading type code (first correlation), used here as a filter criterion.
     * @param code2 the item type code (second correlation), used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return a list of function codes associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getThirdCorrelation(final int category,
                                                 final int code1,
                                                 final int code2,
                                                 final String lang) {
        final Class clazz = THIRD_CORRELATION_HEADING_CLASS_MAP.get(category);
        final DAOBibliographicCorrelation daoBC = new DAOBibliographicCorrelation();
        return daoBC.getThirdCorrelationList(session, category, code1, code2, clazz, locale(lang));
    }

    /**
     * Returns the constraints (optional) to the requested index.
     *
     * @param code the index code, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return a list of all constraints (optional) to the requested index.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getIndexesByCode(final String code, final String lang) throws DataAccessException {
        final DAOIndexList daoIndex = new DAOIndexList();
        try{
            final String tableName = daoIndex.getCodeTableName(session, code, locale(lang));
            final DAOCodeTable dao = new DAOCodeTable();
            final Optional<Class> className  = ofNullable(HibernateSessionProvider.getHibernateClassName(tableName));
            return className.isPresent()
                    ? dao.getList(session, className.get(), locale(lang))
                    : Collections.emptyList();
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }

    /**
     * Returns the description for index code.
     *
     * @param code the index code, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return the description for index code associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public String getIndexDescription(final String code, final String lang) throws DataAccessException {
        DAOSearchIndex searchIndexDao = new DAOSearchIndex();
        return searchIndexDao.getIndexDescription(session, code, locale(lang));
    }

    /**
     * Returns the frbr types associated to the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of frbr type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getFrbrTypes(String lang) {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_FRBR.class, locale(lang));
    }

    /**
     * Returns the description for frbr type entity.
     *
     * @param code the frbr code, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @return the description for index code associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public String getFrbrDescriptionByCode(final String code, final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        final short s_code = Short.parseShort(code);
        return dao.getLongText(session, s_code, T_FRBR.class, locale(lang));
    }

    @Override
    public void close() throws IOException {
        try {
            session.close();
        } catch (final HibernateException exception) {
            throw new IOException(exception);
        }
    }

    /**
     * Returns a list of {@link Avp} which represents a short version of the available bibliographic templates.
     *
     * @return a list of {@link Avp} which represents a short version of the available bibliographic templates.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<Integer>> getBibliographicRecordTemplates() throws DataAccessException {
        final BibliographicModelDAO dao = new BibliographicModelDAO();
        try {
            return dao.getBibliographicModelList(session);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }

    /**
     * Returns a list of {@link Avp} which represents a short version of the available bibliographic templates.
     *
     * @return a list of {@link Avp} which represents a short version of the available bibliographic templates.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<Integer>> getAuthorityRecordTemplates() throws DataAccessException {
        final AuthorityModelDAO dao = new AuthorityModelDAO();
        try {
            return dao.getAuthorityModelList(session);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }

    /**
     * Updates db with object c.
     *
     * @param c the object to be persisted.
     * @throws DataAccessException in case of data access failure.
     */

    public void updateCodeTable(final Object c) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        dao.updateCodeTable(c, session);
    }

    /**
     * Gets a single row from db by language and code.
     *
     * @param c table class name
     * @return a single row from db by language and code.
     * @throws DataAccessException in case of data access failure.
     */

    public CodeTable getCodeTableByCode (final Class c, final CodeTable codeTable) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.loadCodeTableEntry(session, c, codeTable);
    }

    /**
     * Gets the heading category.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return the heading category.
     */
    public List<Avp<String>> getMarcCategories(final String lang) {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_BIB_TAG_CAT.class, locale(lang));
    }

    /**
     *
     * @param lang the language code, used here as a filter criterion.
     * @param category the category, used here as a filter criterion.
     * @return  a list of heading item types by marc category code associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getFirstCorrelation(final String lang, final int category) throws DataAccessException {
        final DAOCodeTable daoCT = new DAOCodeTable();
        return daoCT.getList(session, FIRST_CORRELATION_HEADING_CLASS_MAP.get(category), locale(lang));
    }

    /**
     * Returns the descriptive catalog forms associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the descriptive catalog forms associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getDescriptiveCatalogForms(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_ITM_DSCTV_CTLG.class, locale(lang));
    }

    /**
     * Returns the control types associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the control type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getControlTypes(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_ITM_CNTL_TYP.class, locale(lang));
    }

    /**
     * Returns the encoding levels associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the encoding level associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getEncodingLevels(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_ITM_ENCDG_LVL.class, locale(lang));
    }

    /**
     * Returns the character encoding schemas associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the character encoding schema associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getCharacterEncodingSchemas(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_ITM_CCS.class, locale(lang));
    }

    /**
     * Returns the bibliographic levels associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the bibliographic level associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getBibliographicLevels(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_ITM_BIB_LVL.class, locale(lang));
    }

    /**
     * Gets the note group type list.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return the note group type list.
     */
    public List<Avp<String>> getNoteGroupTypeList(String lang) {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, BibliographicNoteGroupType.class, locale(lang));
    }
    
     /**
      * Gets Validation for variable field.
      * Validation is related to sub-fields: valid, mandatory and default subfield
      *
      * @param marcCategory the marc category used here as filter criterion.
      * @param code1 the first correlation used here as filter criterion.
      * @param code2 the second correlation used here as filter criterion.
      * @param code3 the third correlation used here as filter criterion.
      * @return Validation object containing subfield list.
      */
    public Validation getSubfieldsByCorrelations(final int marcCategory,
                                                 final int code1,
                                                 final int code2,
                                                 final int code3) throws DataAccessException {
        final DAOBibliographicValidation daoBibliographicValidation = new DAOBibliographicValidation();
        try {
            final CorrelationValues correlationValues = new CorrelationValues(code1, code2, code3);
            return daoBibliographicValidation.load(session, marcCategory, correlationValues);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }
    
   /**
     * Returns the date types associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the date type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getDateTypes(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_ITM_DTE_TYP.class, locale(lang));
    }


    /**
     * Returns the codes list associated with the given language and key.
     *
     * @param lang the language code, used here as a filter criterion.
     * @param codeListType the code list type key.
     * @return a list of code / description tuples representing the date type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getCodesList(final String lang, final CodeListsType codeListType)throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, GlobalStorage.MAP_CODE_LISTS.get(codeListType.toString()), locale(lang));
    }


    /**
     * Returns the music forms of composition associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the date type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getMusicFormsOfComposition(final String lang)throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_MSC_FORM_OR_TYP.class, locale(lang));
    }

    /**
     * Returns the music formats associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the date type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getMusicFormats(final String lang)throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_MSC_FRMT.class, locale(lang));
    }

    /**
     * Returns the music parts associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the date type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getMusicParts(final String lang)throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_MSC_PRT.class, locale(lang));
    }

    /**
     * Returns the modified record types associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the modified record type associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getModifiedRecordTypes(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_REC_MDFTN.class, locale(lang));
    }

    /**
     * Returns the catalog source associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the catalog source associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getCatalogSources(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_REC_CTLGG_SRC.class, locale(lang));
    }

    /**
     * Return a correlation values for a specific tag number.
     *
     * @param category the category code used here as filter criterion.
     * @param indicator1 the first indicator used here as filter criterion.
     * @param indicator2 the second indicator used here as filter criterion.
     * @param code the tag number code used here as filter criterion.
     * @return correlation values
     * @throws DataAccessException in case of data access failure.
     */
    public CorrelationValues getCorrelationVariableField(final Integer category,
                                              final String indicator1,
                                              final String indicator2,
                                              final String code) throws DataAccessException {
        final DAOBibliographicCorrelation daoBibliographicCorrelation = new DAOBibliographicCorrelation();
        try {
            return ofNullable(
                    daoBibliographicCorrelation.getBibliographicCorrelation(
                            session, code, indicator1.charAt(0), indicator2.charAt(0), category))
                    .map(BibliographicCorrelation::getValues).orElse(null);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }

    /**
     * Gets the Material description information.
     * The values depend on mtrl_dsc and bib_itm data (leader).
     *
     * @param recordTypeCode the record type code (leader 05) used here as filter criterion.
     * @param bibliographicLevel the bibliographic level (leader 06) used here as filter criterion.
     * @param code the tag number code used here as filter criterion.
     * @return a map with RecordTypeMaterial info.
     * @throws DataAccessException in case of data access failure.
     */
    public Map<String, Object> getMaterialTypeInfosByLeaderValues(final char recordTypeCode, final char bibliographicLevel, final String code)  throws DataAccessException {

        final DAORecordTypeMaterial dao = new DAORecordTypeMaterial();

        try {
            final Map<String, Object> mapRecordTypeMaterial = new HashMap<>();
            final RecordTypeMaterial rtm = dao.getMaterialHeaderCode(session, recordTypeCode, bibliographicLevel);

            mapRecordTypeMaterial.put(GlobalStorage.HEADER_TYPE_LABEL, (code.equals(GlobalStorage.MATERIAL_TAG_CODE) ? rtm.getBibHeader008() : rtm.getBibHeader006()));
            mapRecordTypeMaterial.put(GlobalStorage.FORM_OF_MATERIAL_LABEL, rtm.getAmicusMaterialTypeCode());
            return mapRecordTypeMaterial;
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        }
	}

    /**
     * Gets the material type information.
     * Used for 006 field.
     *
     * @param headerCode the header code used here as filter criterion.
     * @param code the tag number code used here as filter criterion.
     * @return a string representing form of material.
     * @throws DataAccessException in case of data access failure.
     */
    public Map<String, Object> getMaterialTypeInfosByHeaderCode(final int headerCode, final String code) throws DataAccessException {

        final Map<String, Object> mapRecordTypeMaterial = new HashMap<>();
        final DAORecordTypeMaterial dao = new DAORecordTypeMaterial();
        try {
            return ofNullable(dao.getDefaultTypeByHeaderCode(session, headerCode, code))
                    .map( rtm -> {
                        mapRecordTypeMaterial.put(GlobalStorage.FORM_OF_MATERIAL_LABEL, rtm.getAmicusMaterialTypeCode());
                        mapRecordTypeMaterial.put(GlobalStorage.MATERIAL_TYPE_CODE_LABEL, rtm.getRecordTypeCode());

                        return mapRecordTypeMaterial;
                }).orElse(null);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }

    /**
     * Returns the description for heading type entity.
     *
     * @param code the heading marc category code, used here as a filter criterion.
     * @param lang the language code, used here as a filter criterion.
     * @param category the category field.
     * @return the description for index code associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public String getHeadingTypeDescription(final int code, final String lang, final int category) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getLongText(session, code, FIRST_CORRELATION_HEADING_CLASS_MAP.get(category), locale(lang));
    }

    /**
     * Save the new Bibliographic Record Template.
     *
     * @param template the record template.
     * @throws DataAccessException in case of data access failure.
     */
    public void saveBibliographicRecordTemplate(final RecordTemplate template) throws DataAccessException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final BibliographicModelDAO dao = new BibliographicModelDAO();
            final BibliographicModel model = new BibliographicModel();
            model.setLabel(template.getName());
            model.setFrbrFirstGroup(template.getGroup());
            model.setRecordFields(mapper.writeValueAsString(template));
            dao.save(model, session);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        } catch (final JsonProcessingException exception) {
            logger.error(MessageCatalog._00013_IO_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }
    /**
     * Save the new Authority record template.
     *
     * @param template the record template.
     * @throws DataAccessException in case of data access failure.
     */
    public void saveAuthorityRecordTemplate(final RecordTemplate template) throws DataAccessException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final AuthorityModelDAO dao = new AuthorityModelDAO();
            final AuthorityModel model = new AuthorityModel();
            model.setLabel(template.getName());
            model.setFrbrFirstGroup(template.getGroup());
            model.setRecordFields(mapper.writeValueAsString(template));
            dao.save(model, session);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        } catch (final JsonProcessingException exception) {
            logger.error(MessageCatalog._00013_IO_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }

    /**
     * Update the Bibliographic Record Template.
     *
     * @param template the record template.
     * @throws DataAccessException in case of data access failure.
     */
    public void updateBibliographicRecordTemplate(final RecordTemplate template) throws DataAccessException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final BibliographicModelDAO dao = new BibliographicModelDAO();
            final BibliographicModel model = new BibliographicModel();
            model.setId(template.getId());
            model.setLabel(template.getName());
            model.setFrbrFirstGroup(template.getGroup());
            model.setRecordFields(mapper.writeValueAsString(template));
            dao.update(model, session);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        } catch (final JsonProcessingException exception) {
            logger.error(MessageCatalog._00013_IO_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }

    /**
     * Update the Authority Record Template.
     *
     * @param template the record template.
     * @throws DataAccessException in case of data access failure.
     */
    public void updateAuthorityRecordTemplate(final RecordTemplate template) throws DataAccessException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final AuthorityModelDAO dao = new AuthorityModelDAO();
            final AuthorityModel model = new AuthorityModel();
            model.setId(template.getId());
            model.setLabel(template.getName());
            model.setFrbrFirstGroup(template.getGroup());
            model.setRecordFields(mapper.writeValueAsString(template));
            dao.update(model, session);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        } catch (final JsonProcessingException exception) {
            logger.error(MessageCatalog._00013_IO_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }


    /**
     * Deletes a Bibliographic record template.
     *
     * @param id the record template id.
     * @throws DataAccessException in case of data access failure.
     */
    public void deleteBibliographicRecordTemplate(final String id) throws DataAccessException {
        try {
            final BibliographicModelDAO dao = new BibliographicModelDAO();
            final Model model = dao.load(Integer.valueOf(id), session);
            dao.delete(model, session);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }

    /**
     * Delete an Authority record template.
     *
     * @param id the record template id.
     * @throws DataAccessException in case of data access failure.
     */
    public void deleteAuthorityRecordTemplate(final String id) throws DataAccessException {
        try {
            final AuthorityModelDAO dao = new AuthorityModelDAO();
            final Model model = dao.load(Integer.valueOf(id), session);
            dao.delete(model, session);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }

    /**
     * Return a Bibliographic Record Template by id.
     *
     * @param id the record template id.
     * @return the bibliographic record template associated with the given id.
     * @throws DataAccessException in case of data access failure.
     */
    public RecordTemplate getBibliographicRecordRecordTemplatesById(final String id) throws DataAccessException {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(
                    new BibliographicModelDAO().load(Integer.valueOf(id), session).getRecordFields(),
                    RecordTemplate.class);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        } catch (final IOException exception) {
            logger.error(MessageCatalog._00013_IO_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }

    /**
     * Return a Authority Record Template by id
     *
     * @param id the record template id.
     *
     * @throws DataAccessException in case of data access failure.
     */
    public RecordTemplate getAuthorityRecordRecordTemplatesById(final String id) throws DataAccessException {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(
                    new AuthorityModelDAO().load(Integer.valueOf(id), session).getRecordFields(),
                    RecordTemplate.class);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        } catch (final IOException exception) {
            logger.error(MessageCatalog._00013_IO_FAILURE, exception);
            throw new DataAccessException(exception);
        }
    }

    /**
     * Check if exist the first correlation list for the category code.
     *
     * @param category the category associated to field/tag.
     * @return a true if exist, false otherwise.
     */
    public boolean existHeadingTypeByCategory(final int category){
        return ofNullable(FIRST_CORRELATION_HEADING_CLASS_MAP.get(category)).isPresent();
    }

    /**
     * Check if exist the second correlation list for the category code.
     *
     * @param category the category associated to field/tag.
     * @return a true if exist, false otherwise.
     */
    public boolean existItemTypeByCategory(final int category){
        return ofNullable(SECOND_CORRELATION_CLASS_MAP.get(category)).isPresent();
    }

    /**
     * Check if exist the third correlation list for the category code.
     *
     * @param category the category associated to field/tag.
     * @return a true if exist, false otherwise.
     */
    public boolean existFunctionCodeByCategory(final int category){
        return ofNullable(THIRD_CORRELATION_HEADING_CLASS_MAP.get(category)).isPresent();
    }


    public List<MapHeading> getFirstPage(final String query, final int view, final int mainLibrary, final String lang)throws DataAccessException, InvalidBrowseIndexException {
           //TODO lista degli indici da visualizzare nella pagina del browse
        //List <IndexListElement> indexes =  dao.getBrowseIndex(new Locale(lang),session);
        String key = null;
        try {
            String index = null;
            String browseTerm = null;
            final List<Descriptor> descriptorsList;
            final DAOIndexList daoIndex = new DAOIndexList();
            final DAOCodeTable daoCodeTable = new DAOCodeTable();
            if(query != null) {
                index = query.substring(0, query.indexOf((" ")));
                index = F.fixedCharPadding(index, 9).toUpperCase();
                browseTerm = query.substring(query.indexOf((" ")), query.length() );
            }
            key = daoIndex.getIndexByAbreviation(index, session, locale(lang));
            final Class c = GlobalStorage.DAO_CLASS_MAP.get(key);
            if (c == null) {
                logger.error(MessageCatalog._00119_DAO_CLASS_MAP_NOT_FOUND, key);
                return Collections.emptyList();
            }
            final DAODescriptor dao = (DAODescriptor) c.newInstance();
            final String filter = GlobalStorage.FILTER_MAP.get(key);
            browseTerm = dao.calculateSearchTerm(browseTerm, key, session);
            descriptorsList = dao.getHeadingsBySortform("<", "",browseTerm, "", view, 1, session);
            descriptorsList.addAll(dao.getHeadingsBySortform(">=", filter,query, "", view, 10, session));
            //TODO refactoring del metodo getIndexingLanguage e getAccessPointLanguage
            return descriptorsList.stream().map( heading -> {
                  final MapHeading headingObject = new MapHeading();
                  headingObject.setHeadingNumber(heading.getHeadingNumber());
                  headingObject.setStringText(heading.getDisplayText());
                  headingObject.setCountAuthorities(heading.getAuthorityCount());
                try {
                    headingObject.setCountDocuments(dao.getDocCount(heading, view, session));
                    headingObject.setCountCrossReferences(dao.getXrefCount(heading, view, session));
                    headingObject.setCountTitleNameDocuments(dao.getDocCountNT(heading, view, session));
                    headingObject.setIndexingLanguage(daoCodeTable.getLanguageOfIndexing(heading.getIndexingLanguage(), session));
                    headingObject.setAccessPointlanguage(daoCodeTable.getAccessPointLanguage(heading.getAccessPointLanguage(), heading, session));
                } catch (HibernateException e) {
                    e.printStackTrace();
                }
                  headingObject.setVerificationlevel(daoCodeTable.getLongText(session, heading.getVerificationLevel(), T_VRFTN_LVL.class, locale(lang)));
                  headingObject.setDatabase(daoCodeTable.getLongText(session, view, DB_LIST.class, locale(lang)));
                  return headingObject;
              }).collect(Collectors.toList());

       } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        } catch (SQLException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new DataAccessException(exception);
        } catch (InstantiationException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new InvalidBrowseIndexException(key);
        } catch (IllegalAccessException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
            throw new InvalidBrowseIndexException(key);
        }
    }

    public List<Heading> getNextHeadings(final String searchBrowseTerm, final int cataloguingView, final int mainLibrary, final String lang){
        //DAODescriptor dao =
        return null;
    }
    public List<Heading> getPreviousHeadings(final String searchBrowseTerm, final int cataloguingView, final int mainLibrary, final String lang){
        //DAODescriptor dao =
        return null;
    }


}