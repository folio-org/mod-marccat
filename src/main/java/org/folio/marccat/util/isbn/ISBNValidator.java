package org.folio.cataloging.util.isbn;

import lombok.Data;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author Christian Chiama
 * @version $Revision: 4 $ $Date: 2018-10-1
 */
@Data
public class ISBNValidator {

  /**
   * Pattern to replace all non ISBN characters. ISBN can have digits or 'X'.
   */
  private static String REGEX = "[^\\dX]";
  private static Pattern NON_ISBN_CHARACTERS = Pattern.compile(REGEX);

  private int length;

  private Function <String, Boolean> checkChecksumFunction;


  /**
   * @param isbnType
   */
  public void initialize(ISBN.Type isbnType) {
    switch (isbnType) {
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

  /**
   * @param isbn
   */
  public void initialize(CharSequence isbn) {
    if (isbn.length() != 10 && isbn.length() != 13) return;
    switch (isbn.length()) {
      case 10:
        length = 10;
        checkChecksumFunction = this::checkChecksumISBN10;
        break;
      case 13:
        length = 13;
        checkChecksumFunction = this::checkChecksumISBN13;
        break;
    }
  }

  /**
   * @param isbn
   * @return
   */
  public boolean isValid(CharSequence isbn) {
    if (isbn == null) {
      return true;
    }
    // Replace all non-digit (or !=X) chars
    String digits = NON_ISBN_CHARACTERS.matcher(isbn).replaceAll("");

    // Check if the length of resulting string matches the expecting one
    if (digits.length() != length) {
      return false;
    }
    return checkChecksumFunction.apply(digits);
  }


  private String deHyphenation(String isbn) {
    return NON_ISBN_CHARACTERS.matcher(isbn).replaceAll("");
  }

  /**
   * @param isbn
   * @return
   */
  public boolean checkChecksumISBN10(String isbn) {
    int sum = 0;
    isbn = deHyphenation(isbn);
    for (int i = 0; i < isbn.length() - 1; i++) {
      sum += (isbn.charAt(i) - '0') * (i + 1);
    }
    char checkSum = isbn.charAt(9);
    return sum % 11 == (checkSum == 'X' ? 10 : checkSum - '0');
  }

  /**
   * @param isbn
   * @return
   */
  public boolean checkChecksumISBN13(String isbn) {
    int sum = 0;
    isbn = deHyphenation(isbn);
    for (int i = 0; i < isbn.length() - 1; i++) {
      sum += (isbn.charAt(i) - '0') * (i % 2 == 0 ? 1 : 3);
    }
    char checkSum = isbn.charAt(12);
    return 10 - sum % 10 == (checkSum - '0');
  }

  /**
   * esempio di utilizzo
   *
   * @return
   * @ISBN(value = "2251004165")
   * public String isbn;
   */
  public Optional <String> findFirstISBN() {
    Optional <String> isbn =
      Optional
        .of(Arrays.stream(ISBNValidator.class.getFields())
          .filter(f -> f.getAnnotation(ISBN.class) != null)
          .map(f -> f.getAnnotation(ISBN.class).value())
          .findFirst().orElse(""));
    return isbn;
  }
}
