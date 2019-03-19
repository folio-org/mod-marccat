package org.folio.marccat.test;

import org.folio.marccat.config.AbstractUnitTest;
import org.folio.marccat.resources.domain.FieldCollection;
import org.folio.marccat.search.SearchResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SearchUnitAPITest extends AbstractUnitTest {


  @Test
  public void getBibliographicSearchResults() {
    final String params = "&from=1&to=10&dpo=1&ml=170&q=%22Manzoni%22";
    final SearchResponse response = get(address("/search?view=1" + params), SearchResponse.class);
    System.out.println("Bibliographic Records found : 0");
  }

  @Test
  public void getAuthoritySearchResults() {
    final String params = "&from=1&to=10&dpo=1&ml=170&q=%22Manzoni%22";
    final SearchResponse response = get(address("/search?view=-1" + params), SearchResponse.class);
    System.out.println("Authority Records found : 0");
  }
}

