package org.folio.cataloging.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.F;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.cataloguing.bibliographic.*;
import org.folio.cataloging.business.cataloguing.bibliographic.FixedField;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.cataloguing.common.CataloguingSourceTag;
import org.folio.cataloging.business.cataloguing.common.ControlNumberTag;
import org.folio.cataloging.business.cataloguing.common.DateOfLastTransactionTag;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.searching.InvalidBrowseIndexException;
import org.folio.cataloging.dao.*;
import org.folio.cataloging.dao.common.HibernateSessionProvider;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.exception.ModCatalogingException;
import org.folio.cataloging.exception.RecordInUseException;
import org.folio.cataloging.integration.log.MessageCatalogStorage;
import org.folio.cataloging.integration.record.BibliographicInputFile;
import org.folio.cataloging.integration.record.RecordParser;
import org.folio.cataloging.integration.search.Parser;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.resources.domain.*;
import org.folio.cataloging.resources.domain.Leader;
import org.folio.cataloging.search.SearchResponse;
import org.folio.cataloging.shared.*;
import org.folio.cataloging.util.StringText;
import org.springframework.web.multipart.MultipartFile;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.folio.cataloging.F.isNotNullOrEmpty;
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
            put(22, TitleFunction.class); //from heading
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
        final BibliographicCorrelationDAO bibliographicCorrelationDAO = new BibliographicCorrelationDAO();
        return bibliographicCorrelationDAO.getFirstCorrelationByNoteGroupCode(session, noteGroupTypeCode, locale(lang));
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
     * Returns the skip in filing associated with the given language.
     *
     * @param lang the language code, used here as a filter criterion.
     * @return a list of code / description tuples representing the skip in filing associated with the requested language.
     * @throws DataAccessException in case of data access failure.
     */
    public List<Avp<String>> getSkipInFiling(final String lang) throws DataAccessException {
        final DAOCodeTable dao = new DAOCodeTable();
        return dao.getList(session, T_SKP_IN_FLNG_CNT.class, locale(lang));
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

        final BibliographicCorrelationDAO daoBC = new BibliographicCorrelationDAO();
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
        final BibliographicCorrelationDAO daoBC = new BibliographicCorrelationDAO();
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
        final BibliographicValidationDAO daoBibliographicValidation = new BibliographicValidationDAO();
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
        final BibliographicCorrelationDAO bibliographicCorrelationDAO = new BibliographicCorrelationDAO();
        try {
            return ofNullable(
                    bibliographicCorrelationDAO.getBibliographicCorrelation(
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

        final RecordTypeMaterialDAO dao = new RecordTypeMaterialDAO();

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
        final RecordTypeMaterialDAO dao = new RecordTypeMaterialDAO();
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
    //todo: add second and third value wemi flag: consider if use record template also for authority
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
     * Updates wemi flag first group for bibliographic model.
     *
     * @param modelId -- the bibliographic model id.
     * @param frbrFirstGroupValue -- the first wemi flag to update.
     * @throws DataAccessException -- in case of data access exception.
     */
    public void updateWemiBibliographicFlag(final int modelId, final int frbrFirstGroupValue) throws DataAccessException {
        try {
            final BibliographicModelDAO dao = new BibliographicModelDAO();
            final BibliographicModel model = (BibliographicModel) dao.load(modelId, session);
            model.setFrbrFirstGroup(frbrFirstGroupValue);
            dao.update(model, session);
        } catch (final HibernateException exception) {
            logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
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
    public RecordTemplate getBibliographicRecordRecordTemplatesById(final Integer id) throws DataAccessException {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(
                    new BibliographicModelDAO().load(id, session).getRecordFields(),
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
    public RecordTemplate getAuthorityRecordRecordTemplatesById(final Integer id) throws DataAccessException {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(
                    new AuthorityModelDAO().load(id, session).getRecordFields(),
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

  /**
   * Returns the diacritics associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the diacritics associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List<MapDiacritic> getDiacritics(final String lang) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    try {

      return  dao.getDiacritics(session).stream().map(diacritic -> {
        final MapDiacritic diacriticObject = new MapDiacritic();
        diacriticObject.setCode(diacritic.getIdCharacter());
        diacriticObject.setDescription(diacritic.getCharacterName());
        diacriticObject.setCharacter(diacritic.getCharacter());
        diacriticObject.setCharacterSet(diacritic.getSetCharacter());
        diacriticObject.setUnicode(diacritic.getUnicodeCode());
        return diacriticObject;
      }).collect(Collectors.toList());

    }
    catch (HibernateException e) {
      throw new DataAccessException();
    }
  }

  /**
   * Return a list of headings for a specific a search query in the first browse
   *
   * @param query the query used here as filter criterion
   * @param view the view used here as filter criterion
   * @param mainLibrary the main library used here as filter criterion
   * @param pageSize the page size used here as filter criterion
   * @param lang the lang used here as filter criterion
   * @return a list of headings
   * @throws DataAccessException
   * @throws InvalidBrowseIndexException
   */
  public List<MapHeading> getFirstPage(final String query, final int view, final int mainLibrary, final int pageSize, final String lang)throws DataAccessException, InvalidBrowseIndexException {
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
        browseTerm = query.substring(query.indexOf((" ")), query.length() ).trim();
      }
      key = daoIndex.getIndexByAbreviation(index, session, locale(lang));
      final Class c = GlobalStorage.DAO_CLASS_MAP.get(key);
      if (c == null) {
        logger.error(MessageCatalog._00119_DAO_CLASS_MAP_NOT_FOUND, key);
        return Collections.emptyList();
      }
      final DAODescriptor dao = (DAODescriptor) c.newInstance();
      String filter = GlobalStorage.FILTER_MAP.get(key);
      if (dao instanceof ShelfListDAO) {
        filter = filter + " and hdg.mainLibraryNumber = " + mainLibrary;
      }
      browseTerm = dao.calculateSearchTerm(browseTerm, key, session);

      descriptorsList = dao.getHeadingsBySortform("<", "desc",browseTerm, filter, view, 1, session);
      if(!(dao instanceof PublisherDescriptorDAO)) {
        if (descriptorsList.size() > 0) {
          browseTerm = dao.getBrowsingSortForm(descriptorsList.get(0));
          descriptorsList.clear();
        }
      }
      descriptorsList.addAll(dao.getHeadingsBySortform(">=", "",browseTerm, filter, view, pageSize, session));
      return getMapHeadings(view, lang, descriptorsList, daoCodeTable, dao);

    } catch (final SQLException | HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final IllegalAccessException | InstantiationException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new InvalidBrowseIndexException(key);
    }
  }

  /**
    * Return a list of headings for a specific a search query in the next browse
   *
   * @param query the query used here as filter criterion
   * @param view the view used here as filter criterion
   * @param mainLibrary the main library used here as filter criterion
   * @param pageSize the page size used here as filter criterion
   * @param lang the lang used here as filter criterion
   * @return a list of headings
   * @throws DataAccessException
   * @throws InvalidBrowseIndexException
   */
  public List<MapHeading> getNextPage(final String query, final int view, final int mainLibrary, final int pageSize, final String lang){
        String key = null;
        try {
        String index = null;
        String browseTerm = null;
        final List<Descriptor> descriptorsList;
        final DAOIndexList daoIndex = new DAOIndexList();
        final DAOCodeTable daoCodeTable = new DAOCodeTable();
        String operator = ">";
        if(query != null) {
            index = query.substring(0, query.indexOf((" ")));
            index = F.fixedCharPadding(index, 9).toUpperCase();
            browseTerm = query.substring(query.indexOf((" ")), query.length()).trim();
        }

        key = daoIndex.getIndexByAbreviation(index, session, locale(lang));
        final Class c = GlobalStorage.DAO_CLASS_MAP.get(key);
        if (c == null) {
            logger.error(MessageCatalog._00119_DAO_CLASS_MAP_NOT_FOUND, key);
            return Collections.emptyList();
        }
        final DAODescriptor dao = (DAODescriptor) c.newInstance();
        String filter = GlobalStorage.FILTER_MAP.get(key);
        if (dao instanceof ShelfListDAO) {
            filter = filter + " and hdg.mainLibraryNumber = " + mainLibrary;
        }
        browseTerm = dao.calculateSearchTerm(browseTerm, key, session);
        if(dao instanceof PublisherDescriptorDAO || dao instanceof NameTitleNameDescriptorDAO)
            operator = ">=";
        descriptorsList = dao.getHeadingsBySortform(operator, "",browseTerm, filter, view, pageSize, session);
        return getMapHeadings(view, lang, descriptorsList, daoCodeTable, dao);


    } catch (final HibernateException | SQLException exception) {
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        throw new DataAccessException(exception);
    } catch (final IllegalAccessException | InstantiationException exception) {
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        throw new InvalidBrowseIndexException(key);
    }
    }

  /**
   * Return a list of headings for a specific a search query in the previous browse
   *
   * @param query the query used here as filter criterion
   * @param view the view used here as filter criterion
   * @param mainLibrary the main library used here as filter criterion
   * @param pageSize the page size used here as filter criterion
   * @param lang the lang used here as filter criterion
   * @return a list of headings
   * @throws DataAccessException
   * @throws InvalidBrowseIndexException
   */
  public List<MapHeading> getPreviousPage(final String query, final int view, final int mainLibrary, final int pageSize, final String lang){
    String key = null;
    try {
      String index = null;
      String browseTerm = null;
      final List<Descriptor> descriptorsList;
      final DAOIndexList daoIndex = new DAOIndexList();
      final DAOCodeTable daoCodeTable = new DAOCodeTable();
      String operator = "<";
      if(query != null) {
        index = query.substring(0, query.indexOf((" ")));
        index = F.fixedCharPadding(index, 9).toUpperCase();
        browseTerm = query.substring(query.indexOf((" ")), query.length()).trim();
      }

      key = daoIndex.getIndexByAbreviation(index, session, locale(lang));
      final Class c = GlobalStorage.DAO_CLASS_MAP.get(key);
      if (c == null) {
        logger.error(MessageCatalog._00119_DAO_CLASS_MAP_NOT_FOUND, key);
        return Collections.emptyList();
      }
      final DAODescriptor dao = (DAODescriptor) c.newInstance();
      String filter = GlobalStorage.FILTER_MAP.get(key);
      if (dao instanceof ShelfListDAO) {
        filter = filter + " and hdg.mainLibraryNumber = " + mainLibrary;
      }
      browseTerm = dao.calculateSearchTerm(browseTerm, key, session);
      if(dao instanceof PublisherDescriptorDAO || dao instanceof NameTitleNameDescriptorDAO)
        operator = "<=";
      descriptorsList = dao.getHeadingsBySortform(operator, "desc",browseTerm, filter, view, pageSize, session);
      List<MapHeading> mapHeading = getMapHeadings(view, lang, descriptorsList, daoCodeTable, dao);
      Collections.reverse(mapHeading);
      return mapHeading;

    } catch (final SQLException | HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final IllegalAccessException | InstantiationException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new InvalidBrowseIndexException(key);
    }
  }

  /**
   * Return a complete heading map with the data of the heding number, the text to display, the authority count,
   * the count of documents, the count of cross references, the count of name titles, the indexing language, the access point language
   * @param view
   * @param lang
   * @param descriptorsList
   * @param daoCodeTable
   * @param dao
   * @return a map headings
   */

  private List <MapHeading> getMapHeadings(int view, String lang, List <Descriptor> descriptorsList, DAOCodeTable daoCodeTable, DAODescriptor dao) throws DataAccessException {
    return descriptorsList.stream().map(heading -> {
      final MapHeading headingObject = new MapHeading();
      try {
          headingObject.setHeadingNumber(heading.getHeadingNumber());
          headingObject.setStringText(heading.getDisplayText());
          headingObject.setCountAuthorities(heading.getAuthorityCount());
          headingObject.setCountDocuments(dao.getDocCount(heading, view, session));
          headingObject.setCountCrossReferences(dao.getXrefCount(heading, view, session));
          headingObject.setCountTitleNameDocuments(dao.getDocCountNT(heading, view, session));
          headingObject.setIndexingLanguage(daoCodeTable.getLanguageOfIndexing(heading.getIndexingLanguage(), session));
          headingObject.setAccessPointlanguage(daoCodeTable.getAccessPointLanguage(heading.getAccessPointLanguage(), heading, session));
      } catch (HibernateException exception) {
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        throw new DataAccessException(exception);
      }
      if(heading.getVerificationLevel() != '\0')
        headingObject.setVerificationlevel(daoCodeTable.getLongText(session, heading.getVerificationLevel(), T_VRFTN_LVL.class, locale(lang)));
      headingObject.setDatabase(daoCodeTable.getLongText(session, view, DB_LIST.class, locale(lang)));
      return headingObject;
  }).collect(Collectors.toList());
  }

  /**
   * Returns the browse indexes types associated to the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the language type associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List<Avp<String>> getBrowseIndexes(final String lang) throws DataAccessException {
    final DAOIndexList daoIndex = new DAOIndexList();
    try {
      return  daoIndex.getBrowseIndex(new Locale(lang),session);

    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Returns the preferred view associated with the input data.
   *
   * @param itemNumber the record identifier.
   * @param databasePreferenceOrder the database preference order (for choosing among views).
   * @return the preferred view associated with the input data.
   * @throws DataAccessException in case of data access failure.
   */
  public int getPreferredView(final int itemNumber, final int databasePreferenceOrder) throws DataAccessException {
    return new DAOCache().getPreferredView(session, itemNumber, databasePreferenceOrder);
  }

  /**
   * Sorts a given {@link SearchResponse} instance.
   * The sort actually happens at docids level, if some record has been already fetched in the input response, it will
   * be removed.
   *
   * @param rs the search response.
   * @param attributes the sort attributes.
   * @param directions the sort orders.
   * @return a search response wrapping a docid array ordered according with the given criteria.
   * @throws DataAccessException in case of data access failure.
   */
  public SearchResponse sortResults(final SearchResponse rs, final String[] attributes, final String[] directions) throws DataAccessException {
    new DAOSortResultSets().sort(session, rs, attributes, directions);
    rs.clearRecords();
    return rs;
  }

  /**
   * Returns the content of a record associated with the given data.
   *
   * @param itemNumber the record identifier.
   * @param searchingView the view.
   * @return the content of a record associated with the given data.
   * @throws RecordNotFoundException in case nothing is found.
   */
  public String getRecordData(final int itemNumber, final int searchingView) throws RecordNotFoundException {
    final FULL_CACHE cache = new DAOFullCache().load(session, itemNumber, searchingView);
    return cache.getRecordData();
  }

  /**
   * Find the {@link CatalogItem} associated with the given data.
   *
   * @param itemNumber the record identifier.
   * @param searchingView the search view.
   * @return the {@link CatalogItem} associated with the given data.
   */
  public CatalogItem getCatalogItemByKey(final int itemNumber, final int searchingView) {
    switch (searchingView) {
      case View.AUTHORITY:
        return new AuthorityCatalogDAO().getCatalogItemByKey(session, itemNumber, searchingView);
      default:
        return new BibliographicCatalogDAO().getCatalogItemByKey(session, itemNumber, searchingView);
    }
  }

  /**
   * Get the record associated with given data.
   *
   * @param itemNumber -- the record identifier.
   * @param view -- the search view.
   * @return the {@link BibliographicRecord} associated with the given data.
   */
  public BibliographicRecord getBibliographicRecordById(final int itemNumber, final int view) {
    CatalogItem item = null;
    try {
      item = getCatalogItemByKey(itemNumber, view);
    }catch (RecordNotFoundException re){
     return null;
    }

    final BibliographicRecord bibliographicRecord = new BibliographicRecord();
    bibliographicRecord.setId(item.getAmicusNumber());
    bibliographicRecord.setRecordView(item.getUserView());

    org.folio.cataloging.resources.domain.Leader leader = new org.folio.cataloging.resources.domain.Leader();
    leader.setCode("000");
    leader.setValue(((org.folio.cataloging.dao.persistence.Leader) item.getTag(0)).getDisplayString());
    bibliographicRecord.setLeader(leader);
    final char canadianIndicator = ((BibliographicItem)item).getBibItmData().getCanadianContentIndicator();
    bibliographicRecord.setCanadianContentIndicator(String.valueOf(canadianIndicator));
    bibliographicRecord.setVerificationLevel(String.valueOf(item.getItemEntity().getVerificationLevel()));

    item.getTags().stream().skip(1).forEach(aTag -> {
      int keyNumber = 0;
      int sequenceNbr = 0;
      int skipInFiling = 0;

      if (aTag.isFixedField() && aTag instanceof MaterialDescription){
        final MaterialDescription materialTag = (MaterialDescription)aTag;
        keyNumber = materialTag.getMaterialDescriptionKeyNumber();
        final String tagNbr = materialTag.getMaterialDescription008Indicator().equals("1")?"008":"006";
        final Map<String, Object> map = getMaterialTypeInfosByLeaderValues(materialTag.getItemRecordTypeCode(), materialTag.getItemBibliographicLevelCode(), tagNbr);
        materialTag.setHeaderType((int) map.get(GlobalStorage.HEADER_TYPE_LABEL));
        materialTag.setMaterialTypeCode(tagNbr.equalsIgnoreCase("006")?(String) map.get(GlobalStorage.MATERIAL_TYPE_CODE_LABEL):null);
        materialTag.setFormOfMaterial((String) map.get(GlobalStorage.FORM_OF_MATERIAL_LABEL));
      }

      if (aTag.isFixedField() && aTag instanceof PhysicalDescription){
        final PhysicalDescription physicalTag = (PhysicalDescription)aTag;
        keyNumber = physicalTag.getKeyNumber();
      }

      if (!aTag.isFixedField() && aTag instanceof BibliographicAccessPoint){
        keyNumber = ((BibliographicAccessPoint)aTag).getDescriptor().getKey().getHeadingNumber();
        try {
          sequenceNbr = ((BibliographicAccessPoint)aTag).getSequenceNumber();
        }catch (Exception e){
          sequenceNbr = 0;
        }

        if (aTag instanceof TitleAccessPoint){
          skipInFiling = ((TitleAccessPoint)aTag).getDescriptor().getSkipInFiling();
        }
      }

      if (!aTag.isFixedField() && aTag instanceof BibliographicNoteTag){
        keyNumber = ((BibliographicNoteTag)aTag).getNoteNbr();
        try {
          sequenceNbr = ((BibliographicNoteTag)aTag).getSequenceNumber();
        }catch (Exception e){
          sequenceNbr = 0;
        }
      }

      if (!aTag.isFixedField() && aTag instanceof PublisherManager){
        keyNumber = ((PublisherManager)aTag).getPublisherTagUnits().get(0).getPublisherHeadingNumber(); //add gestione multi publisher
      }

      final CorrelationKey correlation = aTag.getTagImpl().getMarcEncoding(aTag, session);

      final String entry = aTag.isFixedField()
        ? (((FixedField) aTag).getDisplayString())
        : ((VariableField) aTag).getStringText().getMarcDisplayString(Subfield.SUBFIELD_DELIMITER);

      final org.folio.cataloging.resources.domain.Field field = new  org.folio.cataloging.resources.domain.Field();
      org.folio.cataloging.resources.domain.VariableField variableField;
      org.folio.cataloging.resources.domain.FixedField fixedField;
      String tagNumber = correlation.getMarcTag();
      if (aTag.isFixedField()){
        fixedField = new org.folio.cataloging.resources.domain.FixedField();
        fixedField.setSequenceNumber(ofNullable(sequenceNbr).isPresent()?sequenceNbr:0);
        fixedField.setCode(tagNumber);
        fixedField.setDisplayValue(entry);
        fixedField.setHeaderTypeCode(aTag.getCorrelation(1));
        fixedField.setCategoryCode(aTag.getCategory());
        fixedField.setKeyNumber(keyNumber);
        field.setFixedField(fixedField);
      } else {
        variableField = new org.folio.cataloging.resources.domain.VariableField();
        variableField.setSequenceNumber(ofNullable(sequenceNbr).isPresent()?sequenceNbr:0);
        variableField.setCode(correlation.getMarcTag());
        variableField.setInd1(""+correlation.getMarcFirstIndicator());
        variableField.setInd2(""+correlation.getMarcSecondIndicator());
        variableField.setHeadingTypeCode(Integer.toString(aTag.getCorrelation(1)));
        variableField.setItemTypeCode(Integer.toString(aTag.getCorrelation(2)));
        variableField.setFunctionCode(Integer.toString(aTag.getCorrelation(3)));
        variableField.setValue(entry);
        variableField.setCategoryCode(correlation.getMarcTagCategoryCode());
        variableField.setKeyNumber(keyNumber);
        variableField.setSkipInFiling(skipInFiling);
        if (variableField.getInd2().equals("S"))
          variableField.setInd2(""+skipInFiling);
        field.setVariableField(variableField);
      }

      field.setCode(tagNumber);

      bibliographicRecord.getFields().add(field);
    });

    return bibliographicRecord;
  }



  /**
   * Generate a new keyNumber for keyFieldCodeValue specified.
   *
   * @param keyCodeValue -- the key code of field value.
   * @return nextNumber
   *
   * @throws DataAccessException in case of data access exception.
   */
  public Integer generateNewKey(final String keyCodeValue) throws DataAccessException {
    try{
      SystemNextNumberDAO dao = new SystemNextNumberDAO();
      return dao.getNextNumber(keyCodeValue, session);
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }

  /**
   * Returns tag marc encoding using correlation values.
   *
   * @param marcCategory -- the tag marc category used as filter criterion.
   * @param headingTypeCode -- the heading type code used as filter criterion.
   * @param itemTypeCode -- the item type code used as filter criterion.
   * @param functionCode -- -- the function code used as filter criterion.
   * @return tagMarcEncoding -- {#linked@{@link TagMarcEncoding}}.
   */
  public TagMarcEncoding getTagMarcEncoding(final int marcCategory, final int headingTypeCode, final int itemTypeCode, final int functionCode) {
    final BibliographicCorrelationDAO dao = new BibliographicCorrelationDAO();
    try {
      final TagMarcEncoding tagMarcEncoding = new TagMarcEncoding();
      final CorrelationKey correlationKey = dao.getMarcEncoding(marcCategory, headingTypeCode, itemTypeCode, functionCode, session);
      if (ofNullable(correlationKey).isPresent()) {
        tagMarcEncoding.setTagCode(correlationKey.getMarcTag());
        tagMarcEncoding.setMarcCategory(marcCategory);
        tagMarcEncoding.setInd1(Character.toString(correlationKey.getMarcFirstIndicator()));
        tagMarcEncoding.setInd2(Character.toString(correlationKey.getMarcSecondIndicator()));
        tagMarcEncoding.setCode1(headingTypeCode);
        tagMarcEncoding.setCode2(itemTypeCode);
        tagMarcEncoding.setCode3(functionCode);
      }
      return tagMarcEncoding;
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }

  /**
   * Updates the full record cache table with the given item.
   *
   * @param item the catalog item.
   * @param view the related view.
   */
  public void updateFullRecordCacheTable(final CatalogItem item, final int view) {
    switch(view) {
      case View.AUTHORITY:
        new AuthorityCatalogDAO().updateFullRecordCacheTable(session, item);
        break;
      default:
        try {
          new BibliographicCatalogDAO().updateFullRecordCacheTable(session, item);
        } catch (final HibernateException exception) { throw new DataAccessException(exception); }
    }
  }

  /**
   * Executes a CCL query using the given data.
   *
   * @param cclQuery the CCL query.
   * @param mainLibraryId the main library identifier.
   * @param locale the current locale.
   * @param searchingView the target search view.
   * @return a list of docid matching the input query.
   */
  public List<Integer> executeQuery(final String cclQuery, final int mainLibraryId, final Locale locale, final int searchingView) {
    final Parser parser = new Parser(locale, mainLibraryId, searchingView, session);
    try (final Statement sql = stmt(connection());
         final ResultSet rs = executeQuery(sql, parser.parse(cclQuery))) {
      final ArrayList<Integer> results = new ArrayList<>();
      while (rs.next()) {
        results.add(rs.getInt(1));
      }

      logger.info(MessageCatalog._00023_SE_REQRES, cclQuery, results.size());

      return results;
    } catch (final HibernateException | SQLException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      return emptyList();
    }
  }

  /**
   * Returns a valid database connection associated with this service.
   *
   * @return a valid database connection associated with this service.
   * @throws HibernateException in case of data access failure.
   */
  public Connection connection() throws HibernateException {
    return session.connection();
  }

  /**
   * Creates a valid statement from the given connection.
   *
   * @param connection the database connection.
   * @return a valid statement from the given connection.
   */
  private Statement stmt(final Connection connection) {
    try {
      return connection.createStatement();
    } catch (final Exception exception) {
      throw new ModCatalogingException(exception);
    }
  }

  /**
   * Internal method fo executing a SQL query.
   *
   * @param stmt the statement.
   * @param query the SQL command.
   * @return the result of the query execution.
   */
  private ResultSet executeQuery(final Statement stmt, final String query) {
    try {
      return stmt.executeQuery(query);
    } catch (final Exception exception) {
      throw new ModCatalogingException(exception);
    }
  }

   //TODO modify method
  public List replaceEquivalentDescriptor(final int indexingLanguage,	final int cataloguingView) throws DataAccessException
  {
        /*final DAODescriptor dao = new DAOPublisherDescriptor();
        final PublisherManagerDAO daoPu = new PublisherManagerDAO();
        List newTags = new ArrayList();
        PUBL_TAG pu = null;
        PublisherManager aTag = (PublisherManager) (deepCopy(this));
        PublisherAccessPoint apf = aTag.getApf();
        List<PUBL_TAG> publisherTagApp = new ArrayList<>();

        for (int i = 0; i < getPublisherTagUnits().size(); i++) {
            pu = (PUBL_TAG) getPublisherTagUnits().get(i);
            Descriptor d = pu.getDescriptor();
            REF ref = dao.getCrossReferencesWithLanguage(d, cataloguingView,
                    indexingLanguage);
            if (ref != null) {
                aTag.markNew();
                int tagNumber = daoPu.getNextPublisherTagNumber();
                pu.setPublisherTagNumber(tagNumber);
                pu.setDescriptor((PUBL_HDG)dao.load(ref.getTarget(), cataloguingView));
                pu.setPublisherHeadingNumber(new Integer(pu.getDescriptor()
                        .getKey().getHeadingNumber()));
                publisherTagApp.add(pu);
                apf.markNew();
                apf.setHeadingNumber(new Integer(tagNumber));


            }
            else{
                aTag.markNew();
                int tagNumber = daoPu.getNextPublisherTagNumber();
                publisherTagApp.add(pu);
                apf.markNew();
                apf.setHeadingNumber(new Integer(tagNumber));
            }
        }
        if(aTag!=null){
            aTag.setPublisherTagUnits(publisherTagApp);
            newTags.add(aTag);
        }
        return newTags;*/

    return null;
  }

  /**
   * Gets Validation for tag field.
   *
   * @param marcCategory the marc category used here as filter criterion.
   * @param tagNumber the tag number used here as filter criterion.
   * @return Validation object containing subfield list.
   */
  public Validation getTagValidation(final int marcCategory,
                                     final String tagNumber) throws DataAccessException {
    final BibliographicValidationDAO daoBibliographicValidation = new BibliographicValidationDAO();
    try {
      return daoBibliographicValidation.load(session, tagNumber, marcCategory);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Checks if record is new then execute insert or update.
   *
   * @param record -- the bibliographic record to save.
   * @param view -- the view associated to user.
   * @param generalInformation -- @linked GeneralInformation for default values.
   * @throws DataAccessException in case of data access exception.
   */
  public void saveBibliographicRecord(final BibliographicRecord record, final int view, final GeneralInformation generalInformation) throws DataAccessException {

    CatalogItem item = null;
    try {
      item = getCatalogItemByKey(record.getId(), view);
    }catch (DataAccessException exception){
    }

    try {
      CasCache casCache = null;
      if (item == null || item.getTags().size() == 0){
        item = insertBibliographicRecord(record, view, generalInformation);
        casCache = new CasCache(item.getAmicusNumber());
        casCache.setLevelCard("L1");
        casCache.setStatusDisponibilit(99);

      }else{
        updateBibliographicRecord(record, item, view, generalInformation);
      }

      if (isNotNullOrEmpty(record.getVerificationLevel()))
        item.getItemEntity().setVerificationLevel(record.getVerificationLevel().charAt(0));
      if (isNotNullOrEmpty(record.getCanadianContentIndicator()))
        ((BibliographicItem)item).getBibItmData().setCanadianContentIndicator(record.getCanadianContentIndicator().charAt(0));

      final BibliographicCatalogDAO dao = new BibliographicCatalogDAO();
      dao.saveCatalogItem(item, casCache, session);

    } catch (Exception e) {
      logger.error(MessageCatalogStorage._00019_SAVE_RECORD_FAILURE, record.getId(), e);
      throw new DataAccessException(e);
    }
  }

  /**
   * Updates a bibliographic record.
   *
   * @param record -- the record to update.
   * @param item -- the catalog item associated to record.
   * @param view -- the current view associated to record.
   * @param generalInformation -- {@linked GeneralInformation} for default values.
   * @throws DataAccessException in case of data access exception.
   */
  private void updateBibliographicRecord(final BibliographicRecord record, final CatalogItem item, final int view,
                                         final GeneralInformation generalInformation) throws DataAccessException {

    final RecordParser recordParser = new RecordParser();
    final int bibItemNumber = item.getAmicusNumber();
    final String newLeader = record.getLeader().getValue();
    recordParser.changeLeader(item, newLeader);

     record.getFields().forEach(field -> {

      final String tagNbr = field.getCode();
      final Field.FieldStatus status = field.getFieldStatus();

      if (status == Field.FieldStatus.NEW
        || status == Field.FieldStatus.DELETED
        || status == Field.FieldStatus.CHANGED) {

        if (tagNbr.equals(GlobalStorage.MATERIAL_TAG_CODE) && status == Field.FieldStatus.CHANGED) {
          recordParser.changeMaterialDescriptionTag(item, field, session);
        }

        if (tagNbr.equals(GlobalStorage.OTHER_MATERIAL_TAG_CODE)){
          final Map<String, Object> mapRecordTypeMaterial = getMaterialTypeInfosByLeaderValues(newLeader.charAt(6), newLeader.charAt(7), tagNbr);
          final String formOfMaterial = (String) mapRecordTypeMaterial.get(GlobalStorage.FORM_OF_MATERIAL_LABEL);
          recordParser.changeMaterialDescriptionOtherTag(item, field, session, formOfMaterial, generalInformation);
        }

        if (tagNbr.equals(GlobalStorage.PHYSICAL_DESCRIPTION_TAG_CODE)){
            recordParser.changePhysicalDescriptionTag(item, field, bibItemNumber);
        }

        if (tagNbr.equals(GlobalStorage.CATALOGING_SOURCE_TAG_CODE) && status == Field.FieldStatus.CHANGED) {
          item.getTags().stream().filter(aTag -> !aTag.isFixedField() && aTag instanceof CataloguingSourceTag).forEach(aTag -> {
            final CataloguingSourceTag cst = (CataloguingSourceTag)aTag;
            cst.setStringText(new StringText(field.getVariableField().getValue()));
            cst.markChanged();
          });
        }

        if (field.getVariableField() != null && !tagNbr.equals(GlobalStorage.CATALOGING_SOURCE_TAG_CODE )){
          final org.folio.cataloging.resources.domain.VariableField variableField = field.getVariableField();
          final CorrelationValues correlationValues;
          if (ofNullable(variableField.getHeadingTypeCode()).isPresent() && isNotNullOrEmpty(variableField.getValue())){
            final int value1 = Integer.parseInt(variableField.getHeadingTypeCode());
            final int value2 = ofNullable(variableField.getItemTypeCode()).isPresent() ?Integer.parseInt(variableField.getItemTypeCode()) :CorrelationValues.UNDEFINED;
            final int value3 = ofNullable(variableField.getFunctionCode()).isPresent() ?Integer.parseInt(variableField.getFunctionCode()) :CorrelationValues.UNDEFINED;
            correlationValues = new CorrelationValues(value1, value2, value3);
          } else {
            logger.error(MessageCatalogStorage._00018_NO_HEADING_TYPE_CODE, variableField.getCode());
            throw new DataAccessException();
          }

          try {
            if (field.getVariableField().getCategoryCode() == GlobalStorage.BIB_NOTE_CATEGORY && correlationValues.getValue(1) != GlobalStorage.PUBLISHER_DEFAULT_NOTE_TYPE ){
              recordParser.changeNoteTag(item, field, correlationValues, bibItemNumber, view);
            } else if (field.getVariableField().getCategoryCode() == GlobalStorage.BIB_NOTE_CATEGORY && correlationValues.getValue(1) == GlobalStorage.PUBLISHER_DEFAULT_NOTE_TYPE ) {
              recordParser.changePublisherTag(item, field, correlationValues, bibItemNumber, view, session);
            }else {
              recordParser.changeAccessPointTag(item, field, correlationValues, bibItemNumber, view, session);
            }

          } catch (HibernateException | SQLException e) {
            throw new DataAccessException(e);
          }
        }
      }
    });

  }

  /**
   * Insert a new bibliographic record.
   *
   * @param record -- the record bibliographic.
   * @param view -- the current view associated to record.
   * @param giAPI -- {@linked GeneralInformation} for default values.
   * @throws DataAccessException in case of data access exception.
   */
  private CatalogItem insertBibliographicRecord(final BibliographicRecord record, final int view, final GeneralInformation giAPI) throws DataAccessException {
    final RecordParser recordParser = new RecordParser();
    final BibliographicCatalog catalog = new BibliographicCatalog();
    final int bibItemNumber = record.getId();
    final CatalogItem item = catalog.newCatalogItem(new Object[]{new Integer(view), new Integer(bibItemNumber)});

    Leader leader = record.getLeader();
    item.getItemEntity().setLanguageOfCataloguing("eng");

    if (leader != null) {
      final BibliographicLeader bibLeader = catalog.createRequiredLeaderTag(item);
      catalog.toBibliographicLeader(leader.getValue(), bibLeader);
      item.addTag(bibLeader);
    }

    ControlNumberTag cnt = catalog.createRequiredControlNumberTag(item);
    item.addTag(cnt);

    DateOfLastTransactionTag dateOfLastTransactionTag = catalog.createRequiredDateOfLastTransactionTag(item);
    item.addTag(dateOfLastTransactionTag);

    record.getFields().stream().skip(1).forEach(field -> {
      final String tagNbr = field.getCode();
      if (tagNbr.equals(GlobalStorage.MATERIAL_TAG_CODE) || tagNbr.equals(GlobalStorage.OTHER_MATERIAL_TAG_CODE)){
        final org.folio.cataloging.resources.domain.FixedField fixedField = field.getFixedField();
        final Map<String, Object> mapRecordTypeMaterial = getMaterialTypeInfosByLeaderValues(leader.getValue().charAt(6), leader.getValue().charAt(7), tagNbr);
        final String formOfMaterial = (String) mapRecordTypeMaterial.get(GlobalStorage.FORM_OF_MATERIAL_LABEL);
        recordParser.addMaterialDescriptionToCatalog(tagNbr, item, fixedField, giAPI, formOfMaterial);
      }

      if (tagNbr.equals(GlobalStorage.PHYSICAL_DESCRIPTION_TAG_CODE)) {
        final org.folio.cataloging.resources.domain.FixedField fixedField = field.getFixedField();
        recordParser.addPhysicalDescriptionTag(item, fixedField, bibItemNumber);
      }

      if (tagNbr.equals(GlobalStorage.CATALOGING_SOURCE_TAG_CODE)){
        final org.folio.cataloging.resources.domain.VariableField variableField = field.getVariableField();
        CataloguingSourceTag cst = catalog.createRequiredCataloguingSourceTag(item);
        cst.setStringText(new StringText(variableField.getValue()));
        item.addTag(cst);
      }

      if (field.getVariableField() != null && !tagNbr.equals(GlobalStorage.CATALOGING_SOURCE_TAG_CODE)){
        final org.folio.cataloging.resources.domain.VariableField variableField = field.getVariableField();
        final CorrelationValues correlationValues;
        if (ofNullable(variableField.getHeadingTypeCode()).isPresent() && isNotNullOrEmpty(variableField.getValue())){
          final int value1 = Integer.parseInt(variableField.getHeadingTypeCode());
          final int value2 = ofNullable(variableField.getItemTypeCode()).isPresent() ?Integer.parseInt(variableField.getItemTypeCode()) :CorrelationValues.UNDEFINED;
          final int value3 = ofNullable(variableField.getFunctionCode()).isPresent() ?Integer.parseInt(variableField.getFunctionCode()) :CorrelationValues.UNDEFINED;
          correlationValues = new CorrelationValues(value1, value2, value3);
        } else {
          logger.error(MessageCatalogStorage._00018_NO_HEADING_TYPE_CODE, variableField.getCode());
          throw new DataAccessException();
        }
        recordParser.insertNewVariableField(item, variableField, bibItemNumber, correlationValues, session, view);
      }

    });
    setDescriptors(item, item.getUserView(), view);
    return item;
  }

  /**
   * Delete a bibliographic record.
   *
   * @param itemNumber -- the amicus number associated to record.
   */
  public void deleteBibliographicRecordById(final Integer itemNumber, final int view, final String uuid, final String userName) throws DataAccessException {
    final BibliographicCatalog catalog = new BibliographicCatalog();

    try {
      CatalogItem item = getCatalogItemByKey(itemNumber, view);
      lockRecord(itemNumber, userName, uuid);
      catalog.deleteCatalogItem(item, session);
      unlockRecord(itemNumber, userName);
    }catch (RecordNotFoundException exception){
      //ignore
    }catch (Exception exception){
      logger.error(MessageCatalogStorage._00022_DELETE_RECORD_FAILURE, itemNumber, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Unlock a record or heading locked from user previously.
   *
   * @param id -- the key number or amicus number.
   * @param userName -- the username who unlock entity.
   */
  public void unlockRecord(final int id, final String userName) throws DataAccessException {
    try {
      final BibliographicCatalog catalog = new BibliographicCatalog();
      catalog.unlock(id, userName, session);
    }catch (RecordInUseException exception)
    {
      logger.error(MessageCatalogStorage._00021_UNLOCK_FAILURE, id, userName, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Lock a record or heading.
   *
   * @param id -- the key number or amicus number.
   * @param userName -- the username who unlock entity.
   * @param uuid -- the uuid associated to lock/unlock session.
   */
  public void lockRecord(final int id, final String userName, final String uuid) throws DataAccessException {
    try {
      final BibliographicCatalog catalog = new BibliographicCatalog();
      catalog.lock(id, userName, uuid, session);
    }catch (RecordInUseException exception)
    {
      logger.error(MessageCatalogStorage._00020_LOCK_FAILURE, id, userName, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Set descriptors for each tag.
   *
   * @param item -- the catalog item.
   * @param recordView -- the record view.
   * @param cataloguingView -- the cataloguing view.
   * @throws DataAccessException in case of data access exception.
   */
  public void setDescriptors(final CatalogItem item, final int recordView, final int cataloguingView) throws DataAccessException {

    item.getTags().forEach(aTag -> {
      if (aTag instanceof AccessPoint) {
        try {
          AccessPoint apf = ((AccessPoint) aTag);
          Descriptor d = apf.getDAODescriptor().findOrCreateMyView(((AccessPoint) aTag).getDescriptor().getHeadingNumber(), View.makeSingleViewString(recordView), cataloguingView, session);
          apf.setDescriptor(d);
        } catch (HibernateException e) {
          throw new DataAccessException(e);
        }
      } else if (aTag instanceof BibliographicRelationshipTag) {
        BibliographicRelationshipTag relTag = (BibliographicRelationshipTag) aTag;
        relTag.copyFromAnotherItem();
      }
    });
  }

  /**
   * Load records from files uploaded.
   *
   * @param file -- the current file.
   * @param startRecord -- the number start record.
   * @param numberOfRecords -- the number of records to load.
   * @param view -- the cataloguing view associated.
   * @return map with loading result.
   */
  public Map<String, Object> loadRecords(final MultipartFile file, final int startRecord, final int numberOfRecords,
    final int view, final Map<String, String> configuration){
    final Map<String, Object> result = new HashMap<>();
    List<Integer> ids = new ArrayList<>();
    try {
      if (!file.isEmpty()) {
        final InputStream input = file.getInputStream();
        final BibliographicInputFile bf = new BibliographicInputFile();
        bf.loadFile(input, file.getOriginalFilename(), view, startRecord, numberOfRecords, session, configuration);

        final DAOCodeTable dao = new DAOCodeTable();
        final LDG_STATS stats = dao.getStats(session, bf.getLoadingStatisticsNumber());
        if (stats.getRecordsAdded() > 0) {
          final List<LOADING_MARC_RECORDS> lmr = (dao.getResults(session, bf.getLoadingStatisticsNumber()));
          ids = lmr.stream().map(l -> l.getBibItemNumber()).collect(Collectors.toList());
        }
        result.put(Global.LOADING_FILE_FILENAME, file.getName());
        result.put(Global.LOADING_FILE_IDS, ids);
        result.put(Global.LOADING_FILE_REJECTED, stats.getRecordsRejected());
        result.put(Global.LOADING_FILE_ADDED, stats.getRecordsAdded());
        result.put(Global.LOADING_FILE_ERRORS, stats.getErrorCount());

      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return result;
  }

}
