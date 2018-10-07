package org.folio.cataloging.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.bean.cataloguing.copy.CopyNoteListElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.CopyNoteKey;
import org.folio.cataloging.dao.persistence.HLDG_NTE;
import org.folio.cataloging.dao.persistence.T_HLDG_NTE_ISOLANG_VW;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * @author elena
 * @version $Revision: 1.7 $, $Date: 2004/10/28 07:30:42 $
 * @since 1.0
 */
public class DAOCopyNotes extends HibernateUtil {

  public HLDG_NTE load(int copyNumber, int copyNoteNumber)
    throws DataAccessException {
    HLDG_NTE cn = null;
    try {
      Session s = currentSession ( );

      cn =
        (HLDG_NTE) s.get (
          HLDG_NTE.class,
          new CopyNoteKey (copyNumber, copyNoteNumber));
    } catch (HibernateException e) {
      logAndWrap (e);

    } catch (NullPointerException e) {
//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
      e.printStackTrace ( );
    }
    return cn;
  }

  /**
   * @param copyNumber is the bibliographic number and copyId
   * @return a list with all the copies from this amicusNumber
   * access to HLDG_NTE table
   * @since 1.0
   */

  public List getCopyNotesList(int copyNumber, Locale locale)
    throws DataAccessException {
    List listAllNotes = null;
    List result = new Vector ( );
    try {
      Session s = currentSession ( );
      listAllNotes =
        s.find (
          "from HLDG_NTE as hn where hn.key.copyIdNumber = "
            + copyNumber);

      Iterator iter = listAllNotes.iterator ( );
      while (iter.hasNext ( )) {
        HLDG_NTE rawCopyNote = (HLDG_NTE) iter.next ( );
        CopyNoteListElement copyNoteListElement =
          new CopyNoteListElement ( );
        copyNoteListElement.setNoteType (
          getCopyNoteTypeText (locale, rawCopyNote.getNoteType ( )));
        copyNoteListElement.setNoteText (rawCopyNote.getNoteText ( ));
        copyNoteListElement.setCopyNoteNumber (rawCopyNote.getKey ( ).getCopyNoteNumber ( ));
        result.add (copyNoteListElement);
      }
    } catch (HibernateException e) {
      logAndWrap (e);
    }
    return result;
  }

  public List getCopyNotesType(Locale locale) throws DataAccessException {
    List copyNotesTypeList = null;
    List result = new Vector ( );

    try {
      Session s = currentSession ( );
      copyNotesTypeList =
        (List) s.find (
          "from T_HLDG_NTE_ISOLANG_VW as vw where vw.isoLanguage ='"
            + locale.getISO3Language ( )
            + "'");
      if (copyNotesTypeList.size ( ) <= 0) {
        copyNotesTypeList =
          (List) s.find (
            "from T_HLDG_NTE_ISOLANG_VW as vw where vw.isoLanguage ='"
              + new Locale ("en").getISO3Language ( )
              + "'");
      }
      result = copyNotesTypeList;
    } catch (DataAccessException e) {
//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
      e.printStackTrace ( );
    } catch (HibernateException e) {
//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
      e.printStackTrace ( );
    } catch (ClassCastException e) {
//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
      e.printStackTrace ( );
    }
    return result;
  }

  public String getCopyNoteTypeText(Locale locale, short code)
    throws DataAccessException {
    List copyNotesTypeList = null;
    String result = new String ("");
    try {
      Session s = currentSession ( );
      copyNotesTypeList =
        (List) s.find (
          "from T_HLDG_NTE_ISOLANG_VW as vw where vw.isoLanguage ='"
            + locale.getISO3Language ( )
            + "' and vw.code = "
            + code);
      if (copyNotesTypeList.size ( ) <= 0) {
        copyNotesTypeList =
          (List) s.find (
            "from T_HLDG_NTE_ISOLANG_VW as vw where vw.isoLanguage ='"
              + new Locale ("en").getISO3Language ( )
              + "' and vw.code = "
              + code);
      }
      Iterator iter = copyNotesTypeList.iterator ( );
      while (iter.hasNext ( )) {
        T_HLDG_NTE_ISOLANG_VW thniw =
          (T_HLDG_NTE_ISOLANG_VW) iter.next ( );
        result = thniw.getLabelStringText ( );
      }

    } catch (HibernateException e) {
      logAndWrap (e);
    }
    return result;
  }

  public int getLastCopyNotes(int copyNumber) throws DataAccessException {

    List listAllNotes = null;
    int result = 0;
    try {
      Session s = currentSession ( );

      listAllNotes =
        s.find (
          "from HLDG_NTE hn where hn.key.copyIdNumber = "
            + copyNumber);

      Iterator iter = listAllNotes.iterator ( );
      while (iter.hasNext ( )) {
        HLDG_NTE rawCopyNote = (HLDG_NTE) iter.next ( );
        int theLastNumber = rawCopyNote.getKey ( ).getCopyNoteNumber ( );
        if (result < theLastNumber) {
          result = theLastNumber;
        }
      }
    } catch (HibernateException e) {
      logAndWrap (e);
    }
    return result + 1;
  }

  public void save(final HLDG_NTE copyNote) throws DataAccessException {
    new TransactionalHibernateOperation ( ) {
      public void doInHibernateTransaction(Session s) throws HibernateException {
        s.save (copyNote);
      }
    }.execute ( );
  }

  public void delete(final HLDG_NTE copyNote) throws DataAccessException {
    new TransactionalHibernateOperation ( ) {
      public void doInHibernateTransaction(Session s) throws HibernateException {
        s.delete (copyNote);
      }
    }.execute ( );
  }

  public void edit(final HLDG_NTE copyNote) throws DataAccessException {
    new TransactionalHibernateOperation ( ) {
      public void doInHibernateTransaction(Session s) throws HibernateException {
        s.update (copyNote);
      }
    }.execute ( );
  }
}
