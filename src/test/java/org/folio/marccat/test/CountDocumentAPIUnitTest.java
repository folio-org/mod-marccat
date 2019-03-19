package org.folio.marccat.test;

import org.folio.marccat.config.AbstractUnitTest;
import org.folio.marccat.resources.domain.CountDocument;
import org.folio.marccat.resources.domain.RecordTemplateCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CountDocumentAPIUnitTest extends AbstractUnitTest {


  @Test
  public void getCountDocumentById() {
/*    final CountDocument countDocument = get(address("/document-count-by-id?view=1&id=6577422"), CountDocument.class);
    System.out.println("Count Documents: " + countDocument.getCountDocuments());*/
    System.out.println("Count Documents: ");

  }
}
