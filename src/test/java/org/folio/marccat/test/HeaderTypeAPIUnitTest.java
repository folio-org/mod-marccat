package org.folio.marccat.test;

import org.folio.marccat.config.AbstractUnitTest;
import org.folio.marccat.resources.domain.HeadingTypeCollection;
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
public class HeaderTypeAPIUnitTest extends AbstractUnitTest {

  @Test
  public void getHeaderTypes() {
    final HeadingTypeCollection headingTypeCollection = get(address("/header-types?type=B&code=32"), HeadingTypeCollection.class);
    headingTypeCollection.getHeadingTypes().forEach(h -> System.out.println("Heading Type found : " + h.getCode()));
    System.out.println("Total Heading Types: " + headingTypeCollection.getHeadingTypes().size());
  }
}
