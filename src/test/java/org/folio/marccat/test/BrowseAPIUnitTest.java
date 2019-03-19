package org.folio.marccat.test;

import org.folio.marccat.config.AbstractUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BrowseAPIUnitTest extends AbstractUnitTest {


  @Test
  public void getRecord() {
    System.out.println("getRecord");
  }


}
