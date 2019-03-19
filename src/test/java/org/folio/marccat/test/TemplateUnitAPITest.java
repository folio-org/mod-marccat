package org.folio.marccat.test;


import org.folio.marccat.config.AbstractUnitTest;
import org.folio.marccat.resources.domain.RecordTemplateCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TemplateUnitAPITest extends AbstractUnitTest {


  @Test
  public void getTemplates() {
    final RecordTemplateCollection recordTemplates = get(address("/record-templates?type=B"), RecordTemplateCollection.class);
    recordTemplates.getRecordTemplates().forEach( t -> System.out.println("Template found : " + t.getName()));
    System.out.println("Total Template: " + recordTemplates.getRecordTemplates().size());
  }

}
