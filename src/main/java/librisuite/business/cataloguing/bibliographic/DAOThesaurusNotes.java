/*
 * (c) LibriCore
 * 
 * Created on 20-jul-2004
 * 
 * DAOCopyNotes.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import librisuite.bean.cataloguing.copy.CopyNoteListElement;
import librisuite.bean.cataloguing.thesaurus.ThesaurusNoteListElement;
import librisuite.business.common.DataAccessException;
import librisuite.hibernate.CopyNoteKey;
import librisuite.hibernate.HLDG_NTE;
import librisuite.hibernate.THS_NTE;
import librisuite.hibernate.T_HLDG_NTE_ISOLANG_VW;
import librisuite.hibernate.T_THS_NTE_TYP;
import librisuite.hibernate.ThesaurusNoteKey;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * @author carmen
 * @version $Revision: 1.7 $, $Date: 2004/10/28 07:30:42 $
 * @since 1.0
 */
public class DAOThesaurusNotes extends HibernateUtil {

	public THS_NTE load(int headingNumber, int noteNumber)
		throws DataAccessException {
		THS_NTE cn = null;
		try {
			Session s = currentSession();

			cn =
				(THS_NTE) s.get(
						THS_NTE.class,
						new ThesaurusNoteKey(headingNumber, noteNumber));
		} catch (HibernateException e) {
			logAndWrap(e);

		} catch (NullPointerException e) {
//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
			e.printStackTrace();
		} 
		return cn;
	}

	/**
	 * @param headingNumber
	 * @return a list with all the notes from this headingNumber
	 * access to THS_NTE table
	 * @since 1.0
	 */

	public List getThesaurusNotesList(int headingNumber, Locale locale)
		throws DataAccessException {
		List listAllNotes = null;
		List result = new Vector();
		try {
			Session s = currentSession();
			listAllNotes =
				s.find(
					"from THS_NTE as hn where hn.key.headingNumber = "
						+ headingNumber);

			Iterator iter = listAllNotes.iterator();
			while (iter.hasNext()) {
				THS_NTE rawThesaurusNote = (THS_NTE) iter.next();
				ThesaurusNoteListElement thesaurusNoteListElement =
					new ThesaurusNoteListElement();
				thesaurusNoteListElement.setNoteType(
				getThesaurusNoteTypeText(locale, rawThesaurusNote.getNoteType()));
				thesaurusNoteListElement.setNoteText(rawThesaurusNote.getNoteText().substring(2));
				thesaurusNoteListElement.setThesaurusNoteNumber(rawThesaurusNote.getKey().getNoteNumber());
				result.add(thesaurusNoteListElement);
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	
	public List getThesaursNotesType(Locale locale) throws DataAccessException {
		List thesaurusNotesTypeList = null;
		List result = new Vector();

		try {
			Session s = currentSession();
			thesaurusNotesTypeList =
				(List) s.find(
					"from T_THS_NTE_TYP as vw where vw.language ='"
						+ locale.getISO3Language()
						+ "'");
			
			result = thesaurusNotesTypeList;
		} catch (DataAccessException e) {
//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
			e.printStackTrace();
		} catch (HibernateException e) {
//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
			e.printStackTrace();
		} catch (ClassCastException e) {
//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
			e.printStackTrace();
		} 
		return result;
	}

	public String getThesaurusNoteTypeText(Locale locale, short code)
		throws DataAccessException {
		List thesaurusNotesTypeList = null;
		String result = new String("");
		try {
			Session s = currentSession();
			thesaurusNotesTypeList =
				(List) s.find(
					"from T_THS_NTE_TYP as vw where vw.language ='"
						+ locale.getISO3Language()
						+ "' and vw.code = "
						+ code);
			
			Iterator iter = thesaurusNotesTypeList.iterator();
			while (iter.hasNext()) {
				T_THS_NTE_TYP thniw =
					(T_THS_NTE_TYP) iter.next();
				result = thniw.getLongText();
			}

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

	public int getLastThesaurusNotes(int headingNumber) throws DataAccessException {

		List listAllNotes = null;
		int result = 0;
		try {
			Session s = currentSession();

			listAllNotes =
				s.find(
					"from THS_NTE hn where hn.key.headingNumber = "
						+ headingNumber);

			Iterator iter = listAllNotes.iterator();
			while (iter.hasNext()) {
				THS_NTE rawThsNote = (THS_NTE) iter.next();
				int theLastNumber = rawThsNote.getKey().getNoteNumber();
				if (result < theLastNumber) {
					result = theLastNumber;
				}
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result + 1;
	}

	public void save(final THS_NTE thesaurusNote) throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException {
				s.save(thesaurusNote);
			}
		}.execute();		
	}

	public void delete(final THS_NTE thesaurusNote) throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException {
				s.delete(thesaurusNote);
			}
		}.execute();
	}

	public void edit(final THS_NTE thesaurusNote) throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s) throws HibernateException {
				s.update(thesaurusNote);
			}
		}.execute();
	}
}
