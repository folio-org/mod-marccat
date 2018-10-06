package org.folio.cataloging.util.isbn;

import java.util.List;

/**
 * @author Christian Chiama
 * @version $Revision: 4 $ $Date: 2018-10-1
 */
public interface ISBNValidation {

  /**
   *
   * @param isbn
   * @return
   */
  boolean checkChecksumISBN10(String isbn);

  /**
   *
   * @param isbn
   * @return
   */
  boolean checkChecksumISBN13(String isbn);

  /**
   *
   * @param isbnCode
   * @return
   */
  List<String> isbnConvertor(String isbnCode);

  /**
   * @param ISBN10
   * @return
   */
  String appendHyphenToISBN10(String ISBN10);

  /**
   *
   * @param ISBN13
   * @return
   */
  String appendHyphenToISBN13(String ISBN13);
}
