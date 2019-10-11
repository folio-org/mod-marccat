package org.folio.api;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.integration.Configuration;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.BrowseAPI;
import org.folio.marccat.resources.domain.HeadingDecorator;
import org.folio.marccat.resources.domain.HeadingDecoratorCollection;
import org.folio.marccat.shared.MapHeading;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BrowseAPITest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private StorageService storageService;

  @Mock
  private Configuration configuration;

  @InjectMocks
  private BrowseAPI browseApi;

  @Before
  public void init(){
  //  MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(browseApi).build();
  }

  @Test
  public void test_get_first_page_success() throws Exception {
    MapHeading heading = new MapHeading();
    heading.setHeadingNumber(1);
    MapHeading heading2 = new MapHeading();
    heading.setHeadingNumber(2);
    List <MapHeading> list = Arrays.asList(heading, heading2);
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("query", "Ti storia");
    requestParams.add("view", "1");
    requestParams.add("mainLibrary", "170");
    requestParams.add("pageSize", "10");
    requestParams.add("lang", "ita");
    when(storageService.getFirstPage(anyString(), anyInt(), anyInt(), anyInt(), anyString())).thenReturn(list);
    mockMvc.perform(get("/marccat/browse")
      .params(requestParams)
      .header(Global.OKAPI_TENANT_HEADER_NAME,"tnx"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$[0].headingNumber", is(1)))
      .andExpect(jsonPath("$[1].headingNumber", is(2)))
      .andReturn().getResponse().getContentAsString();
  }

  @Test
  public void test_get_next_page_success() throws Exception {
    MapHeading heading = new MapHeading();
    heading.setHeadingNumber(1);
    MapHeading heading2 = new MapHeading();
    heading.setHeadingNumber(2);
    List <MapHeading> list = Arrays.asList(heading, heading2);
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("query", "Ti storia");
    requestParams.add("view", "1");
    requestParams.add("mainLibrary", "170");
    requestParams.add("pageSize", "10");
    requestParams.add("lang", "ita");
    when(storageService.getNextPage(anyString(), anyInt(), anyInt(), anyInt(), anyString())).thenReturn(list);
    mockMvc.perform(get("/marccat/next-page")
      .params(requestParams)
      .header(Global.OKAPI_TENANT_HEADER_NAME,"tnx"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$[0].headingNumber", is(1)))
      .andExpect(jsonPath("$[1].headingNumber", is(2)))
      .andReturn().getResponse().getContentAsString();
  }

  @Test
  public void test_get_previous_page_success() throws Exception {
    MapHeading heading = new MapHeading();
    heading.setHeadingNumber(1);
    MapHeading heading2 = new MapHeading();
    heading.setHeadingNumber(2);
    List <MapHeading> list = Arrays.asList(heading, heading2);
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("query", "Ti storia");
    requestParams.add("view", "1");
    requestParams.add("mainLibrary", "170");
    requestParams.add("pageSize", "10");
    requestParams.add("lang", "ita");
    when(storageService.getPreviousPage(anyString(), anyInt(), anyInt(), anyInt(), anyString())).thenReturn(list);
    mockMvc.perform(get("/marccat/next-page")
      .params(requestParams)
      .header(Global.OKAPI_TENANT_HEADER_NAME,"tnx"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$[0].headingNumber", is(1)))
      .andExpect(jsonPath("$[1].headingNumber", is(2)))
      .andReturn().getResponse().getContentAsString();

  }

}
