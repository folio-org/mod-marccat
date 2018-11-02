package org.folio.marccat;

import org.folio.marccat.resources.domain.RecordTemplateCollection;
import org.folio.marccat.search.SearchResponse;
import org.folio.marccat.search.domain.Record;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SearchTestCase extends BaseIntegrationTestCase{

  /**
   * Setup fixture for this integration test case.
   */
  @Before
  public void setUp() {
    super.setUp();
    Arrays.asList(get(address("/search?lang=ita&view=1&ml=170&q=Manzoni&from=1&to=1&dpo=1"), SearchResponse.class)
      .getRecord())
      .forEach(record -> {
        System.out.println(record.getRecordView());
      });

    assertTrue(get(address("/search?lang=ita&view=1&ml=170&q=Manzoni&from=1&to=1&dpo=1"), SearchResponse.class)
      .getRecord().length > 0);
  }
}
