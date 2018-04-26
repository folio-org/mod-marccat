package org.folio.cataloging.it;

import org.folio.cataloging.resources.domain.FieldCollection;
import org.folio.cataloging.resources.domain.RecordTemplateCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.folio.cataloging.ModCataloging.BASE_URI;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class TemplateManagementTestCase extends BaseIntegrationTestCase {
    /**
     * Setup fixture for this integration test case.
     */
    @Before
    public void setUp() {
        get(address("record-templates"), RecordTemplateCollection.class)
                .getRecordTemplates()
                .forEach(template -> {
                    client.delete(address("record-templates/{id}"), template.getId());
                });

        assertEquals(0, get(address("record-templates"), RecordTemplateCollection.class)
                .getRecordTemplates().size());
    }

    @Test
    public void createNewTemplate() {
        final FieldCollection mandatoryFields = get(address("fields/bibliographic/mandatory?lang=eng"), FieldCollection.class);
        
    }
}
