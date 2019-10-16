package org.folio.marccat.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.hibernate.HibernateException;
import org.folio.marccat.business.cataloguing.authority.AuthorityItem;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicItem;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.*;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import net.sf.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;


@RunWith(MockitoJUnitRunner.class)
public class StorageServiceTest {

  @Mock
  private Session session;

  @Mock
  private DAOCodeTable codeTableDao;

  @Mock
  private DAOCache cacheDao;

  @Mock
  private DAOFullCache fullCacheDao;

  @Mock
  private AuthorityCatalogDAO authorityCatalogDao;

  @Mock
  private BibliographicCatalogDAO bibliographicCatalogDao;

  @Mock
  private BibliographicModelDAO bibliographicModelDao;

  @Mock
  private AuthorityModelDAO authorityModelDao;

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

    Mockito.when(codeTableDao.getList(any(Session.class), any(Class.class), any(Locale.class)))
          .thenReturn(list);

    List<Avp<String>> response = storageService.getSkipInFiling("ita");
    assertEquals("1", response.get(0).getLabel());
    assertEquals("2", response.get(1).getValue());
	}


	@Test
	public void testGetPreferredView() {
   int view = 1;

    Mockito.when(cacheDao.getPreferredView(any(Session.class), anyInt(), anyInt()))
      .thenReturn(view);

    int response = storageService.getPreferredView(10, 1);
    assertEquals(1, response);
  }


	@Test
  public void testGetRecordData() {
    FULL_CACHE cache = new FULL_CACHE();
    String recordData = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><record><controlfield tag=\"000\">00000nam  2200000 u 4500</controlfield><controlfield tag=\"001\">000000000100</controlfield><controlfield tag=\"005\">20070226104316.0</controlfield><datafield ind1=\" \" ind2=\" \" tag=\"040\"><subfield code=\"a\">BIB</subfield><subfield code=\"b\">ita</subfield></datafield><controlfield tag=\"008\">940920s1949    it           u000 u ita u</controlfield><datafield ind1=\"1\" ind2=\"2\" tag=\"245\"><subfield code=\"a\">I Fioretti di San Francesco e le considerazioni delle stimmate</subfield></datafield></record>";
    cache.setItemNumber(100);
    cache.setRecordData(recordData);
    cache.setUserView(1);

    Mockito.when(fullCacheDao.load(any(Session.class), anyInt(), anyInt()))
      .thenReturn(cache);

    String response = storageService.getRecordData(100, 1);
    assertEquals(recordData, response);
  }

  @Test
  public void testGetCatalogItemByKey_bibliographic() {
    BIB_ITM bibItmData = new BIB_ITM();
    bibItmData.setAmicusNumber(100);
    BibliographicItem item = new BibliographicItem();
    item.setBibItmData(bibItmData);
    item.setUserView(1);

    Mockito.when(bibliographicCatalogDao.getCatalogItemByKey(any(Session.class), anyInt(), anyInt()))
      .thenReturn(item);

    CatalogItem response = storageService.getCatalogItemByKey(100, 1);
    assertEquals(item, response);
  }

  @Test
  public void testGetCatalogItemByKey_authority() {
    AUT autItemData = new AUT();
    autItemData.setAmicusNumber(100);
    AuthorityItem item = new AuthorityItem();
    item.setAutItmData(autItemData);

    Mockito.when(bibliographicCatalogDao.getCatalogItemByKey(any(Session.class), anyInt(), anyInt()))
      .thenReturn(item);

    CatalogItem response = storageService.getCatalogItemByKey(100, 1);
    assertEquals(item, response);
  }


	@Test
	public void testGetBibliographicRecordTemplates() throws Exception{
    List<Avp<Integer>> list = new ArrayList<>();
    Avp<Integer> obj1 = new Avp<Integer>(1, "Monograph");
    list.add(obj1);

    Mockito.when(bibliographicModelDao.getBibliographicModelList(any(Session.class)))
      .thenReturn(list);

    List<Avp<Integer>> response = storageService.getBibliographicRecordTemplates();
    assertEquals("Monograph", response.get(0).getLabel());

  }

 //@Test
	/*public void testGetAuthorityRecordRecordTemplatesById() throws Exception {
   Model model = new AuthorityModel();
   model.setId(1);
   model.setRecordFields("\"{\"id\":1,\"fields\":[{\"code\":\"001\",\"mandatory\":true,\"fieldStatus\":\"unchanged\",\"fixedField\":{\"keyNumber\":0,\"categoryCode\":1,\"headerTypeCode\":39,\"code\":\"001\",\"displayValue\":\"00006591069\",\"sequenceNumber\":0,\"attributes\":{}},\"added\":false},{\"code\":\"005\",\"mandatory\":true,\"fieldStatus\":\"unchanged\",\"fixedField\":{\"categoryCode\":1,\"description\":\"005 Data/ora di transazione\",\"headerTypeCode\":41,\"code\":\"005\",\"displayValue\":\"20190808172710.\",\"sequenceNumber\":0,\"attributes\":{}},\"added\":false},{\"code\":\"008\",\"mandatory\":true,\"fieldStatus\":\"unchanged\",\"fixedField\":{\"keyNumber\":285348,\"categoryCode\":1,\"headerTypeCode\":31,\"code\":\"008\",\"displayValue\":\"910906s1971    it     e      000 0 ita c\",\"sequenceNumber\":0,\"attributes\":{}},\"added\":false},{\"code\":\"040\",\"mandatory\":true,\"fieldStatus\":\"unchanged\",\"variableField\":{\"keyNumber\":0,\"categoryCode\":1,\"headingTypeCode\":\"1\",\"itemTypeCode\":\"-1\",\"functionCode\":\"-1\",\"ind1\":\" \",\"ind2\":\" \",\"code\":\"040\",\"displayValue\":\"\\u001FaItFiC\",\"subfields\":[],\"sequenceNumber\":0,\"skipInFiling\":0},\"added\":false}]}\"");
   ObjectMapper objectMapper = new ObjectMapper();
   RecordTemplate recordTemplate = objectMapper.readValue(model.getRecordFields(), RecordTemplate.class);

   Mockito.when(authorityModelDao.load(anyInt(), any(Session.class)))
     .thenReturn(model);
   //TODO i tipi di ritorno sono diversi, Storage ha template, invece il DAO Model
   RecordTemplate response = storageService.getAuthorityRecordRecordTemplatesById(1);
   assertEquals(1, response.toString());
   }*/

/*	@Test
	public void testSaveAuthorityRecordTemplate() {
   Model model = new AuthorityModel();
   model.setId(1);
   model.setRecordFields("\"{\"id\":1,\"fields\":[{\"code\":\"001\",\"mandatory\":true,\"fieldStatus\":\"unchanged\",\"fixedField\":{\"keyNumber\":0,\"categoryCode\":1,\"headerTypeCode\":39,\"code\":\"001\",\"displayValue\":\"00006591069\",\"sequenceNumber\":0,\"attributes\":{}},\"added\":false},{\"code\":\"005\",\"mandatory\":true,\"fieldStatus\":\"unchanged\",\"fixedField\":{\"categoryCode\":1,\"description\":\"005 Data/ora di transazione\",\"headerTypeCode\":41,\"code\":\"005\",\"displayValue\":\"20190808172710.\",\"sequenceNumber\":0,\"attributes\":{}},\"added\":false},{\"code\":\"008\",\"mandatory\":true,\"fieldStatus\":\"unchanged\",\"fixedField\":{\"keyNumber\":285348,\"categoryCode\":1,\"headerTypeCode\":31,\"code\":\"008\",\"displayValue\":\"910906s1971    it     e      000 0 ita c\",\"sequenceNumber\":0,\"attributes\":{}},\"added\":false},{\"code\":\"040\",\"mandatory\":true,\"fieldStatus\":\"unchanged\",\"variableField\":{\"keyNumber\":0,\"categoryCode\":1,\"headingTypeCode\":\"1\",\"itemTypeCode\":\"-1\",\"functionCode\":\"-1\",\"ind1\":\" \",\"ind2\":\" \",\"code\":\"040\",\"displayValue\":\"\\u001FaItFiC\",\"subfields\":[],\"sequenceNumber\":0,\"skipInFiling\":0},\"added\":false}]}\"");
   ObjectMapper objectMapper = new ObjectMapper();
   RecordTemplate recordTemplate = objectMapper.readValue(model.getRecordFields(), RecordTemplate.class);

   Mockito.when(authorityModelDao.save(any(Model.class), any(Session.class)))
     .thenReturn(model);
    storageService.saveAuthorityRecordTemplate(recordTemplate);
    //TODO per i void cosa bisogna fare?
    //TODO La response manca
    assertEquals(1, response.toString());

	}*/

	@Test
	public void testDeleteBibliographicRecordTemplate() throws Exception {
    Model model = new AuthorityModel();
    model.setId(1);

    Mockito.when(bibliographicModelDao.load(anyInt(), any(Session.class)))
      .thenReturn(model);

    Mockito.doNothing().when(bibliographicModelDao).delete(any(Model.class), any(Session.class));

    storageService.deleteBibliographicRecordTemplate("1");
  }
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
