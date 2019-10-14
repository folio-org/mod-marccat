package org.folio.marccat.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.dao.DAOCache;
import org.folio.marccat.dao.DAOCodeTable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import net.sf.hibernate.Session;

@RunWith(MockitoJUnitRunner.class)
public class StorageServiceTest {

  @Mock
  private Session session;

  @Mock
  private DAOCodeTable daoCodeTable;

  @Mock
  private DAOCache daoCache;


  @InjectMocks
	private StorageService storageService;

	@Before
	public void init() {
    MockitoAnnotations.initMocks(this);

	}


	@Test
	public void testGetSkipInFiling() {
    List<Avp<String>> list = new ArrayList<>();
    Avp<String> obj1 = new Avp<String>("1", "1");
    Avp<String> obj2 = new Avp<String>("2", "2");
    list.add(obj1);
    list.add(obj2);

    Mockito.when(daoCodeTable.getList(any(Session.class), any(Class.class), any(Locale.class)))
          .thenReturn(list);

    List<Avp<String>> response = storageService.getSkipInFiling("ita");
    assertEquals("1", response.get(0).getLabel());
    assertEquals("2", response.get(1).getValue());
	}


//	@Test
//	public void testClose() {
//		fail("Not yet implemented");
//	}
//
	@Test
	public void testGetPreferredView() {
    List<Avp<String>> list = new ArrayList<>();
    Avp<String> obj1 = new Avp<String>("1", "1");
    Avp<String> obj2 = new Avp<String>("2", "2");
    list.add(obj1);
    list.add(obj2);

    /*Mockito.when(daoCache.getPreferredView(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
      .thenReturn(list);*/

    List<Avp<String>> response = storageService.getSkipInFiling("ita");
    assertEquals("1", response.get(0).getLabel());
    assertEquals("2", response.get(1).getValue());

  }
//
//	@Test
//	public void testSortResults() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetRecordData() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetCatalogItemByKey() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetBibliographicRecordTemplates() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAuthorityRecordRecordTemplatesById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSaveAuthorityRecordTemplate() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeleteBibliographicRecordTemplate() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeleteAuthorityRecordTemplate() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdateBibliographicRecordTemplate() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdateAuthorityRecordTemplate() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSaveBibliographicRecordTemplate() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetBibliographicRecordRecordTemplatesById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testLoadRecords() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGenerateNewKey() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdateFullRecordCacheTable() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testExecuteQuery() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testConnection() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAuthorityRecordTemplates() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetCountDocumentByAutNumber() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFirstPage() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetNextPage() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPreviousPage() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetCodesList() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetMaterialTypeInfosByHeaderCode() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetCorrelationVariableField() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSubfieldsByCorrelations() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetRecordTypes() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetEncodingLevels() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetMaterialTypeInfosByLeaderValues() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetMultipartResourceLevels() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetDescriptiveCatalogForms() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetBibliographicLevels() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetCharacterEncodingSchemas() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetControlTypes() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetRecordStatusTypes() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetHeadingTypeDescription() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetBrowseKey() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetHeadingsByTag() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetBibliographicRecordById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetTagCategory() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSaveBibliographicRecord() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetDescriptors() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetTagValidation() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeleteBibliographicRecordById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUnlockRecord() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testLockRecord() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSaveHeading() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testCreatePublisherDescriptor() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testCreateNameAndTitleDescriptor() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testCreateOrReplaceDescriptor() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFirstCorrelation() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdateHeading() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeleteHeadingById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetCountDocumentByQuery() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFilteredTagsList() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFilteredTag() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetDistinctIndicators() {
//		fail("Not yet implemented");
//	}

}
