package org.folio.marccat;


import org.folio.marccat.resources.domain.FieldCollection;
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
public class TemplateManagementTest extends BaseIntegrationTestCase {
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

  @Test
  public void getMandatoryFields() {
    final FieldCollection mandatoryFields = get(address("/bibliographic/fields/mandatory"), FieldCollection.class);
    mandatoryFields.getFields().forEach( t -> {
      System.out.println("MandatoryField found : " + t.getCode());
    });
    System.out.println("MandatoryField count: " + mandatoryFields.getFields().size());
  }

  @Test
  public void getTemplates() {
    final RecordTemplateCollection recordTemplates = get(address("/record-templates"), RecordTemplateCollection.class);
    recordTemplates.getRecordTemplates().forEach( t -> {
      System.out.println("Template found : " + t.getName());
    });
    System.out.println("Total Template: " + recordTemplates.getRecordTemplates().size());
  }

}
