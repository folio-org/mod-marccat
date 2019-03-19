package org.folio.marccat.test;

import org.folio.marccat.config.AbstractUnitTest;
import org.folio.marccat.resources.domain.Heading;
import org.folio.marccat.resources.domain.HeadingTypeCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HeadingAPIUnitTest extends AbstractUnitTest {

  /**
   * Setup fixture for this integration test case.
   */
  @Before
  public void setUp() {
    super.setUp();
  }

  @Test
  public void createHeading() {
    System.out.println("createHeading");
  }

  @Test
  public void updateHeading() {
    System.out.println("updateHeading");
  }

  @Test
  public void deleteHeading() {
    System.out.println("deleteHeading");
  }
}
