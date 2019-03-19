package org.folio.marccat.test;

import org.folio.marccat.config.AbstractUnitTest;
import org.folio.marccat.resources.domain.CountDocument;
import org.folio.marccat.resources.domain.FieldCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FieldCollectionAPIUnitTest extends AbstractUnitTest {

  /**
   * Setup fixture for this integration test case.
   */
  @Before
  public void setUp() {
    super.setUp();
  }


  @Test
  public void getFieldCollection() {
    final FieldCollection fieldCollection = get(address("/bibliographic/fields/mandatory?view=1"), FieldCollection.class);
    System.out.println("Heading fieldCollection found : " + fieldCollection.getFields().size());
    fieldCollection.getFields().forEach(f -> System.out.println("Mandatory value : " + f.isMandatory()));
  }
}


