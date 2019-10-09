package org.folio.api;

import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.BrowseAPI;
import org.folio.marccat.shared.MapHeading;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;



import java.util.ArrayList;
import java.util.List;

public class BrowseAPITest {

  private MockMvc mockMvc;

  @Mock
  private StorageService storageService;

  @InjectMocks
  private BrowseAPI browseApi;

  @Before
  public void init(){
    MockitoAnnotations.initMocks(this);
    /*mockMvc = standaloneSetup(storageService)
      .addFilters(new CORSFilter())
      .build();*/
  }

  @Test
  public void test_get_first_page_success() throws Exception {
    /*List<User> users = Arrays.asList(
      new User(1, "Daenerys Targaryen"),
      new User(2, "John Snow"));*/
    List<MapHeading> list = new ArrayList <>();
    when(storageService.getFirstPage("Ti Storia", 1, 170, 10, "ita")).thenReturn(list);
    mockMvc.perform(get("/browse"));
      /*.andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$[0].id", is(1)))
      .andExpect(jsonPath("$[0].username", is("Daenerys Targaryen")))
      .andExpect(jsonPath("$[1].id", is(2)))
      .andExpect(jsonPath("$[1].username", is("John Snow")));*/
    //verify(userService, times(1)).getAll();
    //verifyNoMoreInteractions(userService);
  }
}
