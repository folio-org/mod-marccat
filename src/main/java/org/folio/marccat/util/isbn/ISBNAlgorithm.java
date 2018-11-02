package org.folio.marccat.util.isbn;

import org.folio.marccat.F;
import org.folio.marccat.log.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Christian Chiama
 * @version $Revision: 4 $ $Date: 2018-10-1
 */
public class ISBNAlgorithm {

  private final static Log LOGGER = new Log(F.class);

  private static int n = 0;
  private static int v = 0;
  private static boolean ErrorOccurred = false;
  private ISBNValidator isbnValidator = new ISBNValidator();

  /**
   * Il metodo converte il codice org.folio.marccat.util.isbn.ISBNAlgorithm da 10 a 13 caratteri e viceversa e ritorna una lista di valori convertiti
   * Questa lista conterra' un solo valore se si converte da 13 a 10, due valori se si converte da 10 a 13 (978-,979-)
   *
   * @param isbnCode
   * @return
   */
  public List <String> isbnConvertor(String isbnCode) {
    List <String> isbnList = new ArrayList <>();
    if (isbnCode.length() == 10) {
      isbnList.add(ISBN1013(isbnCode, "978"));
      isbnList.add(ISBN1013(isbnCode, "979"));
    } else if (isbnCode.length() == 13) {
      isbnList.add(ISBN1310(isbnCode));
    }
    return isbnList;
  }

  int CharToInt(char a) {
    return Integer.parseInt(String.valueOf(a));
  }

  public String ISBN1310(String ISBN) {
    isbnValidator = new ISBNValidator();
    if (!isbnValidator.checkChecksumISBN13(ISBN)) return "error";
    String isbn13 = ISBN.substring(3, 12);

    IntStream
      .range(0, 9)
      .forEach(i -> {
        if (!ErrorOccurred) {
          v = CharToInt(isbn13.charAt(i));
          if (v == -1) {
            ErrorOccurred = true;
          } else {
            n += (10 - i) * v;
          }
        }
      });

    if (ErrorOccurred) return "ERROR";

    n = 11 - (n % 11);
    return isbn13 + GlobalConst.CheckDigits.substring(n, n + 1);
  }

  public String ISBN1013(String ISBN, String prefix) {
    if (isbnValidator.checkChecksumISBN10(ISBN)) return "error";
    String s12 = prefix + ISBN.substring(0, 9);
    IntStream
      .range(0, 12)
      .forEach(i -> {
        if (!ErrorOccurred) {
          v = CharToInt(s12.charAt(i));
          if (v == -1) {
            ErrorOccurred = true;
          } else {
            if ((i % 2) == 0) {
              n = n + v;
            } else {
              n = n + 3 * v;
            }
          }
        }
      });

    if (ErrorOccurred) return "ERROR";

    n = n % 10;
    if (n != 0) {
      n = 10 - n;
    }
    return s12 + GlobalConst.CheckDigits.substring(n, n + 1);
  }

  public String convert(String isbn) {
    switch (isbn.length()) {
      case 10:
        return ISBN1013(isbn, "978");
      case 13:
        return ISBN1310(isbn);
    }
    return isbn;
  }


  public String changeQueryWithIsbnConvertion(String query) {
    StringBuilder builder = new StringBuilder();
    String[] parts = query.split(GlobalConst.SPACE);
    for (int i = 0; i < parts.length; i++) { // da chiedere ora è inutile ciclare con parts.length = 2, fa un ciclo
      if (GlobalConst.BN_INDEX.equalsIgnoreCase(parts[i])) {
        try {
          List <String> isbnList;
          String isbnCode = parts[i + 1].replaceAll(GlobalConst.HYPHENS, GlobalConst.NO_SPACE);
          System.out.println("BN code ----------> " + isbnCode);
          isbnList = isbnConvertor(isbnCode);

          if (isbnList.size() > 0) {
            builder.append(GlobalConst.OPEN_PARENTHESIS)
              .append(parts[i])
              .append(GlobalConst.SPACE).append(isbnCode)
              .append(GlobalConst.SPACE_OR_BN_INDEX_SPACE).append(isbnList.get(0));

            if (isbnList.size() > 1) {
              builder.append(GlobalConst.SPACE_OR_BN_INDEX_SPACE).append(isbnList.get(1));
            }
            builder.append(GlobalConst.CLOSE_PARENTHESIS).append(GlobalConst.SPACE);
            i++;
          } else {
            LOGGER.debug("Error in the query - incorrect isbn, can not make 10_13 character conversion " + isbnCode);
            return query;
          }

        } catch (Exception e) {
          LOGGER.debug("Error in query syntax - 10_13 characters can not be converted " + e);
          return query;
        }

      } else {
        builder.append(parts[i]).append(" ");
      }
    }
    return builder.toString().trim();
  }
}
