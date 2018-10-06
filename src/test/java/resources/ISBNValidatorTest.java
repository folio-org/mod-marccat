package resources;


import org.folio.cataloging.util.isbn.ISBN;
import org.folio.cataloging.util.isbn.ISBNValidator;
import org.junit.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


//TODO FIXME
public class ISBNValidatorTest {

  @Test
  public void validISBN10() throws Exception {

    new ISBNValidator().initialize(ISBN.Type.ISBN10);

    assertValidISBN ("99921-58-10-7");
    assertValidISBN ("9971-5-0210-0");
    assertValidISBN ("960-425-059-0");
    assertValidISBN ("80-902734-1-6");
    assertValidISBN ("0-9752298-0-X");
    assertValidISBN ("0-85131-041-9");
    assertValidISBN ("0-684-84328-5");
    assertValidISBN ("1-84356-028-3");
  }

  @Test
  public void invalidISBN10() throws Exception {
    new ISBNValidator().initialize(ISBN.Type.ISBN10);

    // invalid check-digit
    assertInvalidISBN ("99921-58-10-8");
    assertInvalidISBN ("9971-5-0210-1");
    assertInvalidISBN ("960-425-059-2");
    assertInvalidISBN ("80-902734-1-8");
    assertInvalidISBN ("0-9752298-0-3");
    assertInvalidISBN ("0-85131-041-X");
    assertInvalidISBN ("0-684-84328-7");
    assertInvalidISBN ("1-84356-028-1");

    // invalid length
    assertInvalidISBN ("");
    assertInvalidISBN ("978-0-5");
    assertInvalidISBN ("978-0-55555555555555");
  }

  @Test
  public void validISBN13() throws Exception {
    new ISBNValidator().initialize(ISBN.Type.ISBN13);

    assertValidISBN ("978-123-456-789-7");
    assertValidISBN ("978-91-983989-1-5");
    assertValidISBN ("978-988-785-411-1");
    assertValidISBN ("978-1-56619-909-4");
    assertValidISBN ("978-1-4028-9462-6");
    assertValidISBN ("978-0-85131-041-1");
    assertValidISBN ("978-0-684-84328-5");
    assertValidISBN ("978-1-84356-028-9");
    assertValidISBN ("978-0-54560-495-6");
  }

  @Test
  public void invalidISBN13() throws Exception {
    new ISBNValidator().initialize(ISBN.Type.ISBN13);

    // invalid check-digit
    assertInvalidISBN ("978-123-456-789-6");
    assertInvalidISBN ("978-91-983989-1-4");
    assertInvalidISBN ("978-988-785-411-2");
    assertInvalidISBN ("978-1-56619-909-1");
    assertInvalidISBN ("978-1-4028-9462-0");
    assertInvalidISBN ("978-0-85131-041-5");
    assertInvalidISBN ("978-0-684-84328-1");
    assertInvalidISBN ("978-1-84356-028-1");
    assertInvalidISBN ("978-0-54560-495-4");

    // invalid length
    assertInvalidISBN ("");
    assertInvalidISBN ("978-0-54560-4");
    assertInvalidISBN ("978-0-55555555555555");
  }

  private void assertValidISBN(String isbn) {
    assertTrue (new ISBNValidator ( ).isValid (isbn), isbn + " should be a valid ISBN");
  }

  private void assertInvalidISBN(String isbn) {
    assertFalse (new ISBNValidator ( ).isValid (isbn), isbn + " should be an invalid ISBN");
  }

}
