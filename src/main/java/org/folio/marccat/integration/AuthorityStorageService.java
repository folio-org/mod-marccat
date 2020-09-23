package org.folio.marccat.integration;

import static org.folio.marccat.config.constants.Global.EMPTY_VALUE;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.folio.marccat.business.cataloguing.authority.AuthorityCatalog;
import org.folio.marccat.business.cataloguing.authority.AuthorityItem;
import org.folio.marccat.business.cataloguing.authority.AuthorityTagImpl;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.business.cataloguing.common.CataloguingSourceTag;
import org.folio.marccat.business.cataloguing.common.ControlNumberTag;
import org.folio.marccat.business.cataloguing.common.DateOfLastTransactionTag;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.AuthorityCatalogDAO;
import org.folio.marccat.dao.CodeTableDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.dao.persistence.Authority008Tag;
import org.folio.marccat.dao.persistence.AuthorityLeader;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.LDG_STATS;
import org.folio.marccat.dao.persistence.LOADING_MARC_RECORDS;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.integration.record.BibliographicInputFile;
import org.folio.marccat.resources.domain.AuthorityRecord;
import org.folio.marccat.resources.domain.Heading;
import org.folio.marccat.resources.domain.Leader;
import org.folio.marccat.resources.shared.RecordUtils;
import org.folio.marccat.util.StringText;
import org.springframework.web.multipart.MultipartFile;

import net.sf.hibernate.HibernateException;

/**
 * @author Elena
 *
 */
public class AuthorityStorageService {
	private static final Log logger = new Log(AuthorityStorageService.class);
	StorageService storageService;

	public StorageService getStorageService() {
		return storageService;
	}

	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}

	/**
	 * Find the {@link CatalogItem} associated with the given data.
	 *
	 * @param itemNumber    the record identifier.
	 * @param searchingView the search view.
	 * @return the {@link CatalogItem} associated with the given data.
	 */
	public CatalogItem getCatalogItemByKey(final int itemNumber, final int searchingView) {
		return new AuthorityCatalogDAO().getCatalogItemByKey(getStorageService().getSession(), itemNumber,
				searchingView);
	}

	/**
	 * Updates the full record cache table with the given item.
	 *
	 * @param item the catalog item.
	 * @param view the related view.
	 */
	public void updateFullRecordCacheTable(final CatalogItem item, final int view) {
		try {
			new AuthorityCatalogDAO().updateFullRecordCacheTable(getStorageService().getSession(), item);
		} catch (final HibernateException exception) {
			throw new DataAccessException(exception);
		}

	}

	/**
	 * Checks if authority record is new then execute insert or update.
	 *
	 * @param record             -- the authority record to save.
	 * @param view               -- the view associated to user.
	 * @param generalInformation -- @linked GeneralInformation for default values.
	 * @throws DataAccessException in case of data access exception.
	 */
	public void saveAuthorityRecord(final AuthorityRecord record,final int view,
			final String lang, final Map<String, String> configuration) {
		CatalogItem item = null;
		try {
			item = getCatalogItemByKey(record.getId(), view);
		} catch (DataAccessException exception) {
			// do not put any exception here!!!!!!!!!!!!! , because the microservice doesn't
			// insert the record
		}

		try {

			if (item == null || item.getTags().isEmpty()) {
				item = insertAuthorityRecord(record, view, lang, configuration);
			} else {
				updateAuthorityRecord(record, item, view, configuration);
			}

			final AuthorityCatalogDAO dao = new AuthorityCatalogDAO();
			dao.saveCatalogItem(item, getStorageService().getSession());

		} catch (Exception e) {
			logger.error(Message.MOD_MARCCAT_00019_SAVE_AUT_RECORD_FAILURE, record.getId(), e);
			throw new DataAccessException(e);
		}
	}

	/**
	 * Insert a new authority record.
	 *
	 * @param record -- the authority record.
	 * @param view   -- the current view associated to record.
	 * @param giAPI  -- {@linked GeneralInformation} for default values.
	 * @throws DataAccessException in case of data access exception.
	 * @throws HibernateException
	 */
	private CatalogItem insertAuthorityRecord(final AuthorityRecord record, final int view, final String lang,
			final Map<String, String> configuration) throws HibernateException {

		final AuthorityCatalog catalog = new AuthorityCatalog();
		final int autItemNumber = new SystemNextNumberDAO().getNextNumber("AA", getStorageService().getSession());
		final CatalogItem item = catalog.newCatalogItem(new Object[] { view, autItemNumber });

		AuthorityTagImpl tagImpl = new AuthorityTagImpl();

		item.getItemEntity().setAmicusNumber(autItemNumber);
		record.setId(autItemNumber);

		Leader leader = record.getLeader();
		item.getItemEntity().setLanguageOfCataloguing(lang);
		if (leader != null) {
			final AuthorityLeader autLeader = catalog.createRequiredLeaderTag(item);
			autLeader.setCorrelationKey(tagImpl.getMarcEncoding(autLeader, getStorageService().getSession()));
			if (!item.getTags().contains(autLeader))
				item.addTag(autLeader);
		}

		ControlNumberTag cnt = catalog.createRequiredControlNumberTag(item);
		if (!item.getTags().contains(cnt))
			item.addTag(cnt);

		DateOfLastTransactionTag dateOfLastTransactionTag = catalog.createRequiredDateOfLastTransactionTag(item);
		if (!item.getTags().contains(dateOfLastTransactionTag))
			item.addTag(dateOfLastTransactionTag);

		Authority008Tag a008 = catalog.createRequired008Tag(item);
		if (!item.getTags().contains(a008))
			item.addTag(a008);

		record.getFields().stream().skip(1).forEach(field -> {
			final String tagNbr = field.getCode();
			if (tagNbr.equals(Global.CATALOGING_SOURCE_TAG_CODE)) {
				final org.folio.marccat.resources.domain.VariableField variableField = field.getVariableField();
				CataloguingSourceTag cst = catalog.createRequiredCataloguingSourceTag(item);
				cst.setStringText(new StringText(variableField.getValue()));
				if (!item.getTags().contains(cst))
					item.addTag(cst);
			}

			if (Global.NAMES.contains(tagNbr)) {
				final org.folio.marccat.resources.domain.VariableField variableField = field.getVariableField();
				Heading heading = new Heading();
				heading.setTag(tagNbr);
				heading.setInd1(variableField.getInd1());
				heading.setInd2(variableField.getInd2());
				heading.setDisplayValue(variableField.getValue());

				heading.setCategoryCode(RecordUtils.getTagCategory(heading, this.getStorageService()));
				int headingNumber = 0;
				final boolean isInd1IsEmpty = heading.getInd1().equals(EMPTY_VALUE);
				final boolean isInd2IsEmpty = heading.getInd2().equals(EMPTY_VALUE);
				final Correlation corr = tagImpl.getCorrelation(heading.getTag(),
						(isInd1IsEmpty) ? " ".charAt(0) : heading.getInd1().charAt(0),
						(isInd2IsEmpty) ? " ".charAt(0) : heading.getInd2().charAt(0), heading.getCategoryCode(),
						getStorageService().getSession());
				final Tag newTag = catalog.getNewTag(item, corr.getKey().getMarcTagCategoryCode(), corr.getValues());
				if (newTag != null) {
					newTag.getMarcEncoding(getStorageService().getSession());
					final StringText st = new StringText(heading.getDisplayValue());
					((VariableField) newTag).setStringText(st);
					if (newTag instanceof Browsable) {
						final int skipInFiling = getStorageService().updateIndicatorNotNumeric(corr.getKey(),
								heading.getInd1(), heading.getInd2());
						((Browsable) newTag).setDescriptorStringText(st);
						final Descriptor descriptor = ((Browsable) newTag).getDescriptor();
						descriptor.setSkipInFiling(skipInFiling);
						try {
							headingNumber = getStorageService().createOrReplaceDescriptor(configuration, descriptor,
									View.DEFAULT_BIBLIOGRAPHIC_VIEW);
						} catch (Exception e) {
							e.printStackTrace();
						}
						heading.setKeyNumber(headingNumber);
					}

				}

				((AuthorityItem) item).getAutItmData().setHeadingNumber(headingNumber);
				((AuthorityItem) item).getAutItmData().setHeadingType("NH");
				item.addTag(newTag);
			}


		});
		return item;
	}

	/**
	 * Updates a bibliographic record.
	 *
	 * @param record             -- the record to update.
	 * @param item               -- the catalog item associated to record.
	 * @param view               -- the current view associated to record.
	 * @param generalInformation -- {@linked GeneralInformation} for default values.
	 * @throws DataAccessException in case of data access exception.
	 */
	private void updateAuthorityRecord(final AuthorityRecord record, final CatalogItem item, final int view,
			final Map<String, String> configuration) {
		// It is not implemented
	}

	/**
	 * Load records from files uploaded.
	 *
	 * @param file            -- the current file.
	 * @param startRecord     -- the number start record.
	 * @param numberOfRecords -- the number of records to load.
	 * @param view            -- the cataloguing view associated.
	 * @return map with loading result.
	 */
	public Map<String, Object> loadAuthRecords(final MultipartFile file, final int startRecord,
			final int numberOfRecords, final int view, final Map<String, String> configuration) {
		final Map<String, Object> result = new HashMap<>();
		List<Integer> ids = new ArrayList<>();
		try {
			if (!file.isEmpty()) {
				final InputStream input = file.getInputStream();
				final BibliographicInputFile bf = new BibliographicInputFile();
				bf.loadFile(input, file.getOriginalFilename(), view, startRecord, numberOfRecords,
						getStorageService().getSession(), configuration);

				final CodeTableDAO dao = new CodeTableDAO();
				final LDG_STATS stats = dao.getStats(getStorageService().getSession(), bf.getLoadingStatisticsNumber());
				if (stats.getRecordsAdded() > 0) {
					final List<LOADING_MARC_RECORDS> lmr = (dao.getResults(getStorageService().getSession(),
							bf.getLoadingStatisticsNumber()));
					ids = lmr.stream().map(LOADING_MARC_RECORDS::getBibItemNumber).collect(Collectors.toList());
				}
				result.put(Global.LOADING_FILE_FILENAME, file.getName());
				result.put(Global.LOADING_FILE_IDS, ids);
				result.put(Global.LOADING_FILE_REJECTED, stats.getRecordsRejected());
				result.put(Global.LOADING_FILE_ADDED, stats.getRecordsAdded());
				result.put(Global.LOADING_FILE_ERRORS, stats.getErrorCount());

			}
		} catch (IOException e) {
			throw new ModMarccatException(e);
		}

		return result;
	}
}
