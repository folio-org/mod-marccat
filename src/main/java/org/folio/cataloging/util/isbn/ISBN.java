package org.folio.cataloging.util.isbn;

import java.util.ArrayList;
import java.util.List;

public class ISBN {


    static int V = 0;
    static int N = 0;
    static boolean ErrorOccurred = false;
    /**
     * Il metodo converte il codice org.folio.cataloging.util.isbn.ISBN da 10 a 13 caratteri e viceversa e ritorna una lista di valori convertiti
     * Questa lista conterra' un solo valore se si converte da 13 a 10, due valori se si converte da 10 a 13 (978-,979-)
     *
     * @param isbnCode
     * @return
     */
    public static List <String> isbnConvertor(String isbnCode) {
        List <String> isbnList = new ArrayList <> ( );
        if (isbnCode.length ( ) == 10) {
            isbnList.add (ISBN1013 (isbnCode, "978"));
            isbnList.add (ISBN1013 (isbnCode, "979"));
        } else if (isbnCode.length ( ) == 13) {
            isbnList.add (ISBN1310 (isbnCode));
        }
        return isbnList;
    }

    /* Change a character to its integer value */
    private static int CharToInt(char a) {
        return Integer.parseInt (String.valueOf (a));
    }

    private static String ISBN1310(String ISBN)
    {
        String s9;
        int i, n, v;
        boolean ErrorOccurred;
        ErrorOccurred = false;
        s9 = ISBN.substring(3, 12);
        n = 0;
        for (i=0; i<9; i++)
        {
            if (!ErrorOccurred) {
                v = CharToInt(s9.charAt(i));
                if (v==-1){
                    ErrorOccurred = true;
                } else {
                    n = n + (10 - i) * v;
                }
            }
        }
        if (ErrorOccurred) return "ERROR";

        n = 11 - (n % 11);
        return s9 + GlobalConst.CheckDigits.substring(n, n+1);
    }

    /* Convert org.folio.cataloging.util.isbn.ISBN-10 to org.folio.cataloging.util.isbn.ISBN-13 */
    private static String ISBN1013(String ISBN, String prefix)
    {
        String s12;
        int i, n, v;
        boolean ErrorOccurred;
        ErrorOccurred = false;
        s12 = prefix + ISBN.substring(0, 9);
        n = 0;
        for (i=0; i<12; i++)
        {
            if (!ErrorOccurred) {
                v = CharToInt(s12.charAt(i));
                if (v==-1){
                    ErrorOccurred = true;
                } else {
                    if ((i % 2)==0){
                        n = n + v;
                    } else {
                        n = n + 3*v;
                    }
                }
            }
        }
        if (ErrorOccurred) return "ERROR";

        n = n % 10;
        if (n!=0) {
            n = 10 - n;
        }
        return s12 + GlobalConst.CheckDigits.substring(n, n+1);
    }

    public static String changeQueryWithIsbnConvertion(String query) {
        StringBuilder builder = new StringBuilder ( );
        String[] parts = query.split (GlobalConst.SPACE);
        for ( int i = 0; i < parts.length; i++ ) {
            if (GlobalConst.BN_INDEX.equalsIgnoreCase (parts[i])) {
                try {
                    List <String> isbnList;
                    String isbnCode;
                    isbnCode = parts[i + 1].replaceAll (GlobalConst.HYPHENS, GlobalConst.NO_SPACE);
                    System.out.println ("BN code ----------> " + isbnCode);
                    isbnList = isbnConvertor (isbnCode);

                    if (isbnList.size ( ) > 0) {
                        builder.append (GlobalConst.OPEN_PARENTHESIS)
                                .append (parts[i])
                                .append (GlobalConst.SPACE).append (isbnCode)
                                .append (GlobalConst.SPACE_OR_BN_INDEX_SPACE).append (isbnList.get (0));

                        if (isbnList.size ( ) > 1) {
                            builder.append (GlobalConst.SPACE_OR_BN_INDEX_SPACE).append (isbnList.get (1));
                        }
                        builder.append (GlobalConst.CLOSE_PARENTHESIS).append (GlobalConst.SPACE);
                        i++;
                    } else {
                        //logger.debug ("Error in the query - incorrect isbn, can not make 10_13 character conversion " + isbnCode);
                        System.out.println ("Error in the query - incorrect isbn, can not make 10_13 character conversion : " + isbnCode);
                        return query;
                    }

                } catch (Exception e) {
                    //logger.debug ("Error in query syntax - 10_13 characters can not be converted " + e);
                    System.out.println ("Error in query syntax - 10_13 characters can not be converted " + e);
                    return query;
                }

            } else {
                builder.append (parts[i]).append (" ");
            }
        }
        return builder.toString ( ).trim ( );
    }

    public static void main(String[] args) throws Exception {
        ISBNHyphenAppender appender = new ISBNHyphenAppender ( );
        System.out.println (appender.appendHyphenToISBN13 ("9788817101646"));
        System.out.println (ISBN1013 ("8817101646", "978"));
        System.out.println (changeQueryWithIsbnConvertion ("BN 8817101646"));
        System.out.println (ISBNUtils.stripChar ("(88-85876-08-0)"));
        System.out.println (ISBNUtils.removeChar ("(88-85876-08-0)","-"));
        System.out.println ("---------------------------------------------------");
        System.out.println (appender.formatIsbn ("9788885876088"));




    }
}
