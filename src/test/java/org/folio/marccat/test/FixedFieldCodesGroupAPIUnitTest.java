package org.folio.marccat.test;

import org.folio.marccat.config.AbstractUnitTest;
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
public class FixedFieldCodesGroupAPIUnitTest extends AbstractUnitTest {

  /**
   * Setup fixture for this integration test case.
   */
  @Before
  public void setUp() {
    super.setUp();
  }

  @Test
  public void getFieldCodeGroups() {
    System.out.println("getFieldCodeGroups");
  }
}
