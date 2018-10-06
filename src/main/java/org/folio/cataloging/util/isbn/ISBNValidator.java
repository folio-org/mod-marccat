package org.folio.cataloging.util.isbn;

import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author Christian Chiama
 * @version $Revision: 4 $ $Date: 2018-10-1
 */
public class ISBNValidator{

  /**
   * Pattern to replace all non ISBN characters. ISBN can have digits or 'X'.
   */
  private static String REGEX = "[^\\dX]";
  private static Pattern NON_ISBN_CHARACTERS = Pattern.compile( REGEX );

  private int length;

  private Function<String, Boolean> checkChecksumFunction;


  public void initialize(ISBN.Type isbnType) {
    switch ( isbnType ) {
      case ISBN10:
        length = 10;
        checkChecksumFunction = this::checkChecksumISBN10;
        break;
      case ISBN13:
        length = 13;
        checkChecksumFunction = this::checkChecksumISBN13;
        break;
    }
  }

  public boolean isValid(CharSequence isbn) {
    if ( isbn == null ) {
      return true;
    }
    // Replace all non-digit (or !=X) chars
    String digits = NON_ISBN_CHARACTERS.matcher( isbn ).replaceAll( "" );

    // Check if the length of resulting string matches the expecting one
    if ( digits.length() != length ) {
      return false;
    }
    return checkChecksumFunction.apply( digits );
  }


  private boolean checkChecksumISBN10(String isbn) {
    int sum = 0;
    for ( int i = 0; i < isbn.length() - 1; i++ ) {
      sum += ( isbn.charAt( i ) - '0' ) * ( i + 1 );
    }
    char checkSum = isbn.charAt( 9 );
    return sum % 11 == ( checkSum == 'X' ? 10 : checkSum - '0' );
  }

  private boolean checkChecksumISBN13(String isbn) {
    int sum = 0;
    for ( int i = 0; i < isbn.length() - 1; i++ ) {
      sum += ( isbn.charAt( i ) - '0' ) * ( i % 2 == 0 ? 1 : 3 );
    }
    char checkSum = isbn.charAt( 12 );
    return 10 - sum % 10 == ( checkSum - '0' );
  }
}
