package org.folio.marccat.integration;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.enumaration.CodeListsType;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.InvalidBrowseIndexException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.resources.domain.*;
import org.folio.marccat.search.SearchResponse;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.shared.GeneralInformation;
import org.folio.marccat.shared.MapHeading;
import org.folio.marccat.shared.Validation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface IStorageService {

  List<Avp<String>> getSkipInFiling(String lang) throws DataAccessException;

  void close() throws IOException;

  int getPreferredView(int itemNumber, int databasePreferenceOrder) throws DataAccessException;

 // SearchResponse sortResults(SearchResponse rs, String[] attributes, String[] directions) throws DataAccessException;

  String getRecordData(int itemNumber, int searchingView) throws RecordNotFoundException;

  CatalogItem getCatalogItemByKey(int itemNumber, int searchingView);

  List<Avp<Integer>> getBibliographicRecordTemplates() throws DataAccessException;

  RecordTemplate getAuthorityRecordRecordTemplatesById(Integer id) throws DataAccessException;

  void saveAuthorityRecordTemplate(RecordTemplate template) throws DataAccessException;

  void deleteBibliographicRecordTemplate(String id) throws DataAccessException;

  void deleteAuthorityRecordTemplate(String id) throws DataAccessException;

  void updateBibliographicRecordTemplate(RecordTemplate template) throws DataAccessException;

  void updateAuthorityRecordTemplate(RecordTemplate template) throws DataAccessException;

  void saveBibliographicRecordTemplate(RecordTemplate template) throws DataAccessException;

  RecordTemplate getBibliographicRecordRecordTemplatesById(Integer id) throws DataAccessException;

  Map<String, Object> loadRecords(MultipartFile file, int startRecord, int numberOfRecords,
                                  int view, Map <String, String> configuration);

  Integer generateNewKey(String keyCodeValue) throws DataAccessException;

  void updateFullRecordCacheTable(CatalogItem item, int view);

  List<Integer> executeQuery(String cclQuery, int mainLibraryId, Locale locale, int searchingView, int firstRecord, int lastRecord, String[] attributes, String[] directions);

  Connection connection() throws HibernateException;

  List<Avp<Integer>> getAuthorityRecordTemplates() throws DataAccessException;

  CountDocument getCountDocumentByAutNumber(int id, int view) throws HibernateException;

  List<MapHeading> getFirstPage(String query, int view, int mainLibrary, int pageSize, String lang) throws DataAccessException, InvalidBrowseIndexException;

  List<MapHeading> getNextPage(String query, int view, int mainLibrary, int pageSize, String lang);

  List<MapHeading> getPreviousPage(String query, int view, int mainLibrary, int pageSize, String lang);

  List<Avp<String>> getCodesList(String lang, CodeListsType codeListType) throws DataAccessException;

  Map<String, Object> getMaterialTypeInfosByHeaderCode(int headerCode, String code) throws DataAccessException;

  CorrelationValues getCorrelationVariableField(Integer category,
                                                String indicator1,
                                                String indicator2,
                                                String code) throws DataAccessException;

  Validation getSubfieldsByCorrelations(int marcCategory,
                                        int code1,
                                        int code2,
                                        int code3) throws DataAccessException;

  List<Avp<String>> getRecordTypes(String lang) throws DataAccessException;

  List<Avp<String>> getEncodingLevels(String lang) throws DataAccessException;

  Map<String, Object> getMaterialTypeInfosByLeaderValues(char recordTypeCode, char bibliographicLevel, String code) throws DataAccessException;

  List<Avp<String>> getMultipartResourceLevels(String lang) throws DataAccessException;

  List<Avp<String>> getDescriptiveCatalogForms(String lang) throws DataAccessException;

  List<Avp<String>> getBibliographicLevels(String lang) throws DataAccessException;

  List<Avp<String>> getCharacterEncodingSchemas(String lang) throws DataAccessException;

  List<Avp<String>> getControlTypes(String lang) throws DataAccessException;

  List<Avp<String>> getRecordStatusTypes(String lang) throws DataAccessException;

  String getHeadingTypeDescription(int code, String lang, int category) throws DataAccessException;

  String getBrowseKey(Descriptor descriptor, Session session) throws HibernateException;

  List<MapHeading> getHeadingsByTag(String tag, String indicator1, String indicator2, String stringText, int view, int mainLibrary, int pageSize);

  ContainerRecordTemplate getBibliographicRecordById(int itemNumber, int view);

  int getTagCategory(String tag,
                     char firstIndicator,
                     char secondIndicator,
                     boolean hasTitle) throws DataAccessException;

  void saveBibliographicRecord(BibliographicRecord record, RecordTemplate template, int view, GeneralInformation generalInformation, String lang, Map <String, String> configuration) throws DataAccessException;

  void setDescriptors(CatalogItem item, int recordView, int cataloguingView) throws DataAccessException;

  Validation getTagValidation(int marcCategory,
                              String tagNumber) throws DataAccessException;

  void deleteBibliographicRecordById(Integer itemNumber, int view) throws DataAccessException;

  void unlockRecord(int id, String userName) throws DataAccessException;

  void lockRecord(int id, String userName, String uuid) throws DataAccessException;

  void saveHeading(Heading heading, int view,
                   Map <String, String> configuration) throws DataAccessException;

  void createPublisherDescriptor(Heading heading, int view, Map <String, String> configuration, Tag newTag) throws HibernateException, SQLException;

  void createNameAndTitleDescriptor(Map <String, String> configuration, Descriptor descriptor, int view) throws HibernateException, SQLException;

  int createOrReplaceDescriptor(Map <String, String> configuration, Descriptor descriptor, int view) throws HibernateException, SQLException;

  List<Avp<String>> getFirstCorrelation(String lang, int category) throws DataAccessException;

  void updateHeading(Heading heading, int view) throws DataAccessException;

  void deleteHeadingById(Heading heading, int view) throws DataAccessException;

  int getCountDocumentByQuery(String cclQuery, String[] attributes, String[] directions, int mainLibraryId, Locale locale, int searchingView);

  List <String> getFilteredTagsList (String tagNumber) throws DataAccessException;

  FilteredTag getFilteredTag(String tagNumber) throws DataAccessException;

  List <String> getDistinctIndicators(List <String> indicators);
}
