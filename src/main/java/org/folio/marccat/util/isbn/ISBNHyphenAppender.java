package org.folio.marccat.util.isbn;

import java.util.Arrays;

/**
 * Appends hyphens to org.folio.marccat.util.isbn.ISBNAlgorithm-10 and org.folio.marccat.util.isbn.ISBNAlgorithm-13 without hyphens.
 *
 * @author Christian Chiama
 * @version $Revision: 4 $ $Date: 2018-10-1
 */
public class ISBNHyphenAppender {


  /**
   * Appends hyphens to an org.folio.marccat.util.isbn.ISBNAlgorithm-10 without hyphens.
   * <p>
   * In an org.folio.marccat.util.isbn.ISBNAlgorithm-10 with hyphens, these hyphens separate the number of the
   * group (similar but no equal to country), the number of the editor, the
   * number of the title and the "checksum" number.
   *
   * @param ISBN10 org.folio.marccat.util.isbn.ISBNAlgorithm to which hyphens are to be added
   * @return the org.folio.marccat.util.isbn.ISBNAlgorithm-10 with the added hyphens
   * @throws NullPointerException          if the org.folio.marccat.util.isbn.ISBNAlgorithm-10 provided is {@code null}
   * @throws IllegalArgumentException      if the length of the org.folio.marccat.util.isbn.ISBNAlgorithm-10 provided is
   *                                       not 10
   * @throws UnsupportedOperationException if the org.folio.marccat.util.isbn.ISBNAlgorithm-10 provided is from a
   *                                       org.folio.marccat.util.isbn.ISBNAlgorithm group not implemented
   */
  String appendHyphenToISBN10(String ISBN10) {
    //Checks if the ISBN10 is null
    if (ISBN10 == null) {
      throw new NullPointerException("The org.folio.marccat.util.isbn.ISBNAlgorithm provided cannot be null");
    }

    //Checks if the length of ISBN10 is 10
    if (ISBN10.length() != 10) {
      throw new IllegalArgumentException("The org.folio.marccat.util.isbn.ISBNAlgorithm " + ISBN10 +
        " is not an org.folio.marccat.util.isbn.ISBNAlgorithm-10. The length of the org.folio.marccat.util.isbn.ISBNAlgorithm is not 10");
    }

    //Gets the group for the ISBN10
    Group group = Group.getGroup(ISBN10);

    //Checks if the group is implemented
    if (group == null) {
      throw new UnsupportedOperationException(ISBN10 +
        " is from a group not implemented");
    }

    //Gets the group number
    String groupNumber = String.valueOf(group.getNumber());

    //Gets the length of the group number
    int groupNumberLength = groupNumber.length();

    int maximumPublisherNumerLength =
      group.getMaximumPublisherNumberLength();

    //Gets the publisher part
    String publisherPart =
      ISBN10.substring(groupNumberLength).
        substring(0, maximumPublisherNumerLength);

    //Gets the valid publisher numbers of the group
    String[][] validPublisherNumbers = group.getValidPublisherNumbers();

    //Tries to find the number of the publisher in one of the valid number
    //ranges for the group
    int i = 0;
    boolean found = false;
    while (!found && i < validPublisherNumbers.length) {

      //Add "0 padding" to the maximum length of the number of publisher
      String minValue = this.rightPad(validPublisherNumbers[i][0],
        '0', maximumPublisherNumerLength);

      //Adds "9 padding" to the maxiumum publisher number length
      String maxValue = this.rightPad(validPublisherNumbers[i][1],
        '9', maximumPublisherNumerLength);

      found = (publisherPart.compareTo(minValue) >= 0 &&
        publisherPart.compareTo(maxValue) <= 0);

      i++;
    }

    String result;
    if (found) {
      //Gets the mid part
      //The mid part is the org.folio.marccat.util.isbn.ISBNAlgorithm part without the group number and the
      //check digit
      String midPart = ISBN10.substring(groupNumberLength, 9);

      int midHyphenPosition = validPublisherNumbers[i - 1][0].length();

      //Builds the result with hyphens
      result = new StringBuilder(groupNumber)//Appends the group number
        .append('-')//Appends the first hyphen
        .append(midPart.substring(0, midHyphenPosition))//Appends the number of the publisher
        .append('-')//Appends the mid hyphen
        .append(midPart.substring(midHyphenPosition))//Appends the number of the title
        .append('-')//Appends the last hyphen
        .append(ISBN10.substring(9)).toString();//Appends the check number

    } else {
      throw new IllegalArgumentException(ISBN10 +
        " is a invalid org.folio.marccat.util.isbn.ISBNAlgorithm for this group");
    }

    return result;
  }

  /**
   * Appends hyphens to an org.folio.marccat.util.isbn.ISBNAlgorithm-13 without hyphens.
   * <p>
   * In an org.folio.marccat.util.isbn.ISBNAlgorithm-13 with hyphens, these hyphens separate the first three
   * digits, the number of the group (similar but no equal to country), the
   * number of the editor, the number of the title and the "checksum" number.
   *
   * @param ISBN13 org.folio.marccat.util.isbn.ISBNAlgorithm to which hyphens are to be added
   * @return the org.folio.marccat.util.isbn.ISBNAlgorithm-13 with the added hyphens
   * @throws NullPointerException          if the org.folio.marccat.util.isbn.ISBNAlgorithm-13 provided is {@code null}
   * @throws IllegalArgumentException      if the length of the org.folio.marccat.util.isbn.ISBNAlgorithm-13 provided is
   *                                       not 13
   * @throws UnsupportedOperationException if the org.folio.marccat.util.isbn.ISBNAlgorithm-13 provided is from a
   *                                       org.folio.marccat.util.isbn.ISBNAlgorithm group not implemented
   */
  public String appendHyphenToISBN13(String ISBN13) {
    if (ISBN13 == null) {
      throw new NullPointerException("The org.folio.marccat.util.isbn.ISBNAlgorithm provided cannot be null");
    }

    if (ISBN13.length() != 13) {
      throw new IllegalArgumentException("The org.folio.marccat.util.isbn.ISBNAlgorithm " + ISBN13 +
        " is not an org.folio.marccat.util.isbn.ISBNAlgorithm-13. The length of the ISBNAlgorithm is not 13");
    }

    return new StringBuilder(ISBN13.substring(0, 3))
      .append('-')
      .append(this.appendHyphenToISBN10(ISBN13.substring(3))).
        toString();
  }

  /**
   * Right pad a {@code String} with the specified {@code character}. The
   * string is padded to the size of {@code maxLength}.
   *
   * @param string    String to pad
   * @param character the character to pad with
   * @param maxLength the size to pad to
   * @return right padded {@code String} or original {@code String} if no
   * padding is necessary
   * @throws NumberFormatException if the parameter {@code string} is null
   */
  private String rightPad(String string, char character, int maxLength) {
    int padLength = maxLength - string.length();
    if (padLength > 0) {
      char[] charPad = new char[padLength];

      Arrays.fill(charPad, character);

      string = new StringBuilder(string)
        .append(String.valueOf(charPad))
        .toString();
    }

    return string;
  }

  /**
   * @param isbn
   * @return the new isbn with hiphen, 10 digit and 13 digit
   * @throws Exception
   */
  public String formatIsbn(String isbn) throws Exception {
    String newIsbn = "";
    int lenIsbn = 10;
    isbn = isbn.trim();

    if (isbn.length() < 10 || isbn.length() < 13) {
      if (isbn.length() > 2) {
        if (isbn.substring(0, 3).equalsIgnoreCase("978") || isbn.substring(0, 3).equalsIgnoreCase("979")) {
          lenIsbn = 13;
        }
      }
      isbn = ISBNUtils.rightAlign(isbn, lenIsbn, '0');
    }
    System.out.println("isbn riempito : " + isbn);

    ISBNHyphenAppender hyphenAppender = new ISBNHyphenAppender();
    try {
      if (isbn.length() == 10) {
        newIsbn = hyphenAppender.appendHyphenToISBN10(isbn);
      } else if (isbn.length() == 13) {
        newIsbn = hyphenAppender.appendHyphenToISBN13(isbn);
      } else {
        throw new Exception("Length org.folio.marccat.util.isbn.ISBNAlgorithm not provided");
      }
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }

    System.out.println("Isbn restituito: " + newIsbn);
    return newIsbn;
  }

}
