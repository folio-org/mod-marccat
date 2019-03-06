package org.folio.marccat;

import org.folio.marccat.resources.domain.RecordTemplateCollection;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BrowsingTestCase  extends BaseIntegrationTestCase{

  /**
   * Setup fixture for this integration test case.
   */
  @Before
  public void setUp() {
    super.setUp();
    get(address("/record-templates?type=B"), RecordTemplateCollection.class)
      .getRecordTemplates()
      .forEach(template -> {
        delete(address("/record-template/" + template.getId() + "?type=B"));
      });

    assertEquals(0, get(address("/record-templates?type=B"), RecordTemplateCollection.class)
      .getRecordTemplates().size());
  }
}
