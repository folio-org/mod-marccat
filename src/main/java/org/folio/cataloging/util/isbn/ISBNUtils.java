package org.folio.cataloging.util.isbn;

/**
 * @author Christian Chiama
 * @version $Revision: 4 $ $Date: 2018-10-1
 */
public class ISBNUtils {

    private static ISBNUtils ourInstance = new ISBNUtils ( );

    public static ISBNUtils getInstance() {
        return ourInstance;
    }

    private ISBNUtils() {}

    static String removeHyphen(String ISBN){
        return  ISBN.replaceAll ("-","");
    }

    public static String removeChar(String ISBN, String character){
        return  ISBN.replaceAll (character,"");
    }

    public static String stripChar(String ISBN){
        String regepx = "[^\\s\\w]*";
        return  ISBN.replaceAll (regepx,"");
    }

    public static String rightAlign(String str, int width, char filler)
    {
        while (str.length() < width) {
            str += filler;
        }
        return str;
    }
}
