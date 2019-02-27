package org.folio.marccat.integration.record;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicCatalog;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicTagImpl;
import org.folio.marccat.business.cataloguing.bibliographic.FixedField;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.log.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.dao.BibliographicCatalogDAO;
import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.dao.RecordTypeMaterialDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.util.StringText;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.marc.*;
import org.marc4j.marc.Leader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * Represents a physical file of Marc Bibliographic records and provides methods for loading the file into the database.
 *
 * @author paulm
 * @author nbianchini
 */
public class BibliographicInputFile {

  private static final Log logger = new Log(BibliographicInputFile.class);
  private LDG_STATS stats = new LDG_STATS();


  /**
   * Loads a file identified through an input stream.
   *
   * @param inputStream     -- the input file stream.
   * @param fileName        -- the file name.
   * @param cataloguingView -- the cataloguing view associated.
   * @param startAt         -- position in file to start loading records.
   * @param recCount        -- record count.
   * @param session         -- the hibernate current session.
   * @throws DataAccessException in case of exception.
   */
  public void loadFile(final InputStream inputStream, final String fileName, final int cataloguingView, final int startAt,
                       final int recCount, final Session session, final Map<String, String> configuration) throws DataAccessException {
    try {
      loadFileFromStream(fileName, inputStream, cataloguingView, startAt, recCount, session, configuration);
    } catch (Exception e) {
      logger.error(MessageCatalog._00031_LOAD_FROM_FILE_FAILURE, fileName, e);
      throw new DataAccessException(e);
    }
  }

  /**
   * Loads a single record into the database
   *
   * @param fileName        -- the file name.
   * @param cataloguingView -- the cataloguing view associated.
   * @param startAt         -- position in file to start loading records.
   * @param recCount        -- record count.
   * @param record          -- the marc4j record to load.
   * @param session         -- the hibernate current session.
   * @throws DataAccessException in case of exception.
   */
  public void loadFile(final String fileName, final int cataloguingView, final int startAt,
                       final int recCount, final Record record, final Session session, final Map<String, String> configuration) throws DataAccessException {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      MarcWriter writer = new MarcStreamWriter(bos);
      writer.write(record);
      writer.close();
      bos.close();
      InputStream input = new ByteArrayInputStream(bos.toByteArray());

      loadFileFromStream(fileName, input, cataloguingView, startAt, recCount, session, configuration);
    } catch (IOException e) {
      logger.error(MessageCatalog._00032_LOAD_REC_BY_REC_FAILURE, record.getControlFields().get(0) + " - " + record.getLeader().toString(), e);
      throw new DataAccessException(e);
    }
  }

  /**
   * Loads records reading from input stream.
   *
   * @param fileName        -- the file name selected.
   * @param input           -- the input stream to read.
   * @param cataloguingView -- the cataloguing view associated.
   * @param startAt         -- position in file to start loading records.
   * @param recCount        -- record count.
   * @param session         -- the hibernate session associated.
   * @throws DataAccessException in case of data access exception.
   */
  private void loadFileFromStream(final String fileName, final InputStream input, final int cataloguingView, final int startAt,
                                  final int recCount, final Session session, final Map<String, String> configuration) throws DataAccessException {

    try {
      final LOADING_MARC_FILE run = new LOADING_MARC_FILE();
      run.setFileName(fileName);
      stats.generateNewKey(session);
      stats.getDAO().save(stats, session);
      run.setLoadingStatisticsNumber(stats.getLoadingStatisticsNumber());
      run.getDAO().save(run, session);
      final MarcReader reader = new MarcStreamReader(input);
      final TagImpl impl = new BibliographicTagImpl();
      final BibliographicCatalog catalog = new BibliographicCatalog();
      int count = 0;
      int processed = 0;

      while (reader.hasNext()) {
        final Record record = reader.next();
        count++;
        if (count >= startAt && processed <= recCount) {
          processed++;
          try {
            CatalogItem item = catalog.newCatalogItemWithoutAmicusNumber();
            catalog.applyKeyToItem(item, new Object[]{cataloguingView});
            Leader leader = record.getLeader();
            final BibliographicLeader leaderTag = catalog.createRequiredLeaderTag(item);
            setLeaderValues(leaderTag, leader);
            leaderTag.setCorrelationKey(impl.getMarcEncoding(leaderTag, session));
            leaderTag.setValidation(impl.getValidation(leaderTag, session));
            item.addTag(leaderTag);

            List<ControlField> controlFields = record.getControlFields();
            addControlFieldToItem(item, controlFields, impl, session, catalog, leaderTag);

            List<DataField> dataFields = record.getDataFields();
            addDataFieldToItem(item, dataFields, impl, session, catalog, cataloguingView, configuration);

            item.getItemEntity().setAmicusNumber(new SystemNextNumberDAO().getNextNumber("BI", session));

            final BibliographicCatalogDAO dao = new BibliographicCatalogDAO();

            item.validate();
            dao.saveCatalogItem(item, session);

            stats.setRecordsAdded(stats.getRecordsAdded() + 1);
            LOADING_MARC_RECORDS result = new LOADING_MARC_RECORDS();
            result.setSequence(processed);
            result.setLoadingStatisticsNumber(stats.getLoadingStatisticsNumber());
            result.setBibItemNumber(item.getAmicusNumber());
            result.getDAO().save(result, session);

          } catch (Exception e) {
            stats.setRecordsRejected(stats.getRecordsRejected() + 1);
            //throw new DataAccessException(e);
          } finally {
            stats.markChanged();
            stats.getDAO().persistByStatus(stats, session);
          }
        }
      }
    } catch (DataAccessException | HibernateException exception) {
      logger.error(MessageCatalog._00030_LOAD_RECORDS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Adds variable fileds to catalog item.
   *
   * @param item       -- the catalog item.
   * @param dataFields -- list of variable/data field marc4j.
   * @param impl       -- the implementation tag.
   * @param session    -- the hibernate session associated.
   * @param catalog    -- the bibliographic catalog.
   */
  private void addDataFieldToItem(final CatalogItem item, final List<DataField> dataFields,
                                  final TagImpl impl, final Session session, final BibliographicCatalog catalog, final int view,
                                  final Map<String, String> configuration) {

    dataFields.forEach(df -> {
      Correlation corr = impl.getCorrelation(df.getTag(), df.getIndicator1(), df.getIndicator2(), 0, session);
      Tag newTag = null;
      try {
        newTag = catalog.getNewTag(item, corr.getKey().getMarcTagCategoryCode(), corr.getValues());
      } catch (RuntimeException e) {
      }

      if (newTag != null) {
        StringText st = stringTextFromSubfield(df.getSubfields());
        ((VariableField) newTag).setStringText(st);
        if (newTag instanceof Browsable) {

          ((Browsable) newTag).setDescriptorStringText(st);
          Descriptor d = ((Browsable) newTag).getDescriptor();
          d.setUserViewString(View.makeSingleViewString(view));
          Descriptor dup;
          try {
            dup = ((DAODescriptor) (d.getDAO())).getMatchingHeading(d, session);
            if (dup == null) {
              d.setConfigValues(configuration);
              d.generateNewKey(session);
              d.getDAO().save(d, session);
            } else {
              ((Browsable) newTag).setDescriptor(dup);
            }
          } catch (HibernateException | SQLException e) {
            throw new DataAccessException(e);
          }
        }
        newTag.setCorrelationKey(impl.getMarcEncoding(newTag, session));
        newTag.setValidation(impl.getValidation(newTag, session));
        item.addTag(newTag);
      }
    });
  }

  /**
   * Adds variable fileds to catalog item.
   *
   * @param item          -- the catalog item.
   * @param controlFields -- list of fixed/control field marc4j.
   * @param impl          -- the implementation tag.
   * @param session       -- the hibernate session associated.
   * @param catalog       -- the bibliographic catalog.
   */
  private void addControlFieldToItem(final CatalogItem item, final List<ControlField> controlFields,
                                     final TagImpl impl, final Session session, final BibliographicCatalog catalog, final BibliographicLeader leaderTag) {
    controlFields.forEach(field -> {
      Tag newTag = null;
      final Correlation corr = impl.getCorrelation(field.getTag(), ' ', ' ', 0, session);
      if (corr == null) {
        if ("006".equals(field.getTag()) || "008".equals(field.getTag())) {

          final RecordTypeMaterial rtm = detectMaterial(session, leaderTag.getItemRecordTypeCode(), leaderTag.getItemBibliographicLevelCode());
          final String formOfMaterial = ofNullable(rtm).map(material -> rtm.getAmicusMaterialTypeCode()).orElse(" ");

          final MaterialDescription md = new MaterialDescription();
          md.setFormOfMaterial(formOfMaterial);
          md.setMaterialDescription008Indicator(("006".equals(field.getTag()) ? "0" : "1"));
          md.setItemEntity(item.getItemEntity());
          md.setCorrelation(1, (field.getTag().equals(Global.MATERIAL_TAG_CODE) ? rtm.getBibHeader008() : rtm.getBibHeader006()));

          newTag = md;
        } else if ("007".equals(field.getTag())) {
          char gmd = field.getData().charAt(0);
          newTag = PhysicalDescription.getInstanceByGMD(gmd);
        }
      } else {
        if (!"003".equals(field.getTag())) {
          newTag = catalog.getNewHeaderTag(item, corr.getDatabaseFirstValue());
        }
      }
      if (!"003".equals(field.getTag())) {
        final String content = field.getData();
        ((FixedField) newTag).setContentFromMarcString(content);
        newTag.setCorrelationKey(impl.getMarcEncoding(newTag, session));
        newTag.setValidation(impl.getValidation(newTag, session));
        item.addTag(newTag);
      }
    });
  }

  private RecordTypeMaterial detectMaterial(final Session session, final char recordTypeCode, final char bibliographicLevel) {
    try {
      final RecordTypeMaterialDAO dao = new RecordTypeMaterialDAO();
      final RecordTypeMaterial rtm = dao.getMaterialHeaderCode(session, recordTypeCode, bibliographicLevel);
      return rtm;
    } catch (HibernateException e) {
      //ignore
      return null;
    }
  }

  /**
   * Sets leader values.
   *
   * @param leaderTag -- the bibliographic leader tag.
   * @param leader    -- the leader marc4j tag.
   */
  private void setLeaderValues(final BibliographicLeader leaderTag, final Leader leader) {
    leaderTag.setCharacterCodingSchemeCode(leader.getCharCodingScheme());
    leaderTag.setControlTypeCode(leader.getImplDefined1()[1]);
    leaderTag.setDescriptiveCataloguingCode(leader.getImplDefined2()[1]);
    leaderTag.setEncodingLevel(leader.getImplDefined2()[0]);
    leaderTag.setItemBibliographicLevelCode(leader.getImplDefined1()[0]);
    leaderTag.setItemRecordTypeCode(leader.getTypeOfRecord());
    leaderTag.setLinkedRecordCode(leader.getImplDefined2()[2]);
    leaderTag.setRecordStatusCode(leader.getRecordStatus());
  }

  /**
   * @param subfields -- the field subfields marc4j.
   * @return the string text.
   */
  private StringText stringTextFromSubfield(List<Subfield> subfields) {
    return new StringText(subfields.stream().map(s -> (s.getCode() + s.getData())).collect(Collectors.joining()));
  }

  public int getLoadingStatisticsNumber() {
    return stats.getLoadingStatisticsNumber();
  }


  public LDG_STATS getStats() {
    return stats;
  }

  public void setStats(LDG_STATS stats) {
    this.stats = stats;
  }
}
