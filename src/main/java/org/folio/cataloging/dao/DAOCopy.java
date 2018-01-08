package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.bean.cataloguing.copy.CopyListElement;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.util.StringText;

import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.folio.cataloging.F.fixedCharPadding;

@SuppressWarnings("unchecked")
public class DAOCopy extends HibernateUtil {
	private Log logger = LogFactory.getLog(DAOCopy.class);

	public static final Comparator<CPY_ID> CPY_ID_COMPARATOR = new Comparator<CPY_ID>() {

		@Override
		public int compare(CPY_ID o1, CPY_ID o2) {
			// execute this: (o1>o2 ? -1 : (o1==o2 ? 0 : 1));
			int i1 = o1.getCopyIdNumber();
			int i2 = o2.getCopyIdNumber();
			if (i1 == i2)
				return 0;
			else if (i1 < i2)
				return -1;
			return 1;
		}
	};

	public CPY_ID load(int CopyNumber) throws DataAccessException {
		CPY_ID c = null;
		try {

			Session s = currentSession();

			c = (CPY_ID) s.get(CPY_ID.class, new Integer(CopyNumber));
			if (c != null) {
				if (c.getShelfListKeyNumber() != null) {
					c.setShelfList(new DAOShelfList().load(c
							.getShelfListKeyNumber().intValue()));
				}
			}
			if ((new DAOGlobalVariable().getValueByName("barrcode"))
					.equals("1")) {
				c.setBarcodeAssigned(true);

			} else {
				c.setBarcodeAssigned(false);

			}
		} catch (HibernateException e) {
			logAndWrap(e);

		}
		return c;
	}

	public CPY_ID loadBarcode(String barCode) throws DataAccessException {
		CPY_ID result = null;
		// List listCopies = null;
		logger.info("hola " + barCode + "-");
		try {
			Session s = currentSession();
			// logger.info("pasa por aki (load barcode)" + barCode);
			List listCopies = s
					.find("from CPY_ID ci where ci.barCodeNumber = '"
							+ barCode.trim() + "'");
			// logger.info("hola1 (load barcode)" + listCopies.size());
			Iterator iter = listCopies.iterator();
			while (iter.hasNext()) {
				// logger.info("hola2 (load barcode)");
				CPY_ID rawCopy = (CPY_ID) iter.next();
				result = rawCopy;
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}

		return result;

	}

	/**
	 * @param copyBarCode
	 *            is the barCode of the copy
	 * @return the BibItemNumber from CPY_ID table
	 * @since 1.0
	 */

	public int getBibItemNumber(String copyBarCode) throws DataAccessException,
			HibernateException {
		int result = 0;

		List listAllCopies = null;
		try {
			Session s = currentSession();
			listAllCopies = s.find("from CPY_ID ci where ci.barCodeNumber = '"
					+ copyBarCode + "'");

			Iterator iter = listAllCopies.iterator();
			while (iter.hasNext()) {
				CPY_ID rawCopy = (CPY_ID) iter.next();
				result = rawCopy.getBibItemNumber();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}

		return result;
	}

	/**
	 * @param copyIdNumber
	 *            is the copyIdNumber of the copy
	 * @return the BibItemNumber from CPY_ID table
	 * @since 1.0
	 */

	public int getBibItemNumber(int copyIdNumber) throws DataAccessException,
			HibernateException {
		int result = 0;

		List listAllCopies = null;
		try {
			Session s = currentSession();
			listAllCopies = s.find("from CPY_ID ci where ci.copyIdNumber = "
					+ copyIdNumber);

			Iterator iter = listAllCopies.iterator();
			while (iter.hasNext()) {
				CPY_ID rawCopy = (CPY_ID) iter.next();
				result = rawCopy.getBibItemNumber();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}

		return result;
	}

	/**
	 * @param copyBarCode
	 *            is the barCode of the copy
	 * @return the CopyIdNumber from CPY_ID table
	 * @since 1.0
	 */

	public int getCopyIdNumber(String copyBarCode) throws DataAccessException,
			HibernateException {
		int result = 0;
		List listAllCopies = null;
		try {
			Session s = currentSession();
			listAllCopies = s.find("from CPY_ID ci where ci.barCodeNumber = '"
					+ copyBarCode + "'");

			Iterator iter = listAllCopies.iterator();
			while (iter.hasNext()) {
				CPY_ID rawCopy = (CPY_ID) iter.next();
				result = rawCopy.getCopyIdNumber();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

	/**
	 * @param copyBarCode
	 *            is the barCode of the copy
	 * @return the non-labelled copy from CPY_ID table
	 * @since 1.0
	 */

	public CPY_ID getNonLabelledCopy(String copyBarCode)
			throws DataAccessException {
		List list = find("from CPY_ID as c where c.barCodeNumber = rpad(?, 14)"
				+ " and c.bibItemNumber = 0", new Object[] { copyBarCode },
				new Type[] { Hibernate.STRING });
		if (list.size() == 1) {
			return (CPY_ID) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param amicusNumber
	 *            is the bibliographic number
	 * @return a list with all the copies from this amicusNumber access to
	 *         CPY_ID table
	 * @since 1.0
	 */

	public List getAllCopies(int amicusNumber) throws DataAccessException {
		List listAllCopies = null;
		List result = new ArrayList();
		Session s = currentSession();
		Query q = null;
		try {
			// q = s.createQuery("select distinct ci,lib1.librarySymbolCode,
			// lib2.librarySymbolCode, lctn.labelStringText " +
			// " from CPY_ID ci, LIB lib1, LIB lib2, LCTN_VW lctn" +
			// " where ci.bibItemNumber =" +amicusNumber+
			// " AND (ci.organisationNumber=lib1.organisationNumber and
			// ci.branchOrganisationNumber=lib2.organisationNumber)" +
			// " AND (ci.branchOrganisationNumber=lctn.organisationNumber and
			// ci.locationNameCode=lctn.locationNumber)"+
			// " order by
			// lib1.librarySymbolCode,lib2.librarySymbolCode,lctn.labelStringText,ci.barCodeNumber");
			q = s
					.createQuery("select distinct ci,lib1.librarySymbolCode, lib2.librarySymbolCode, lctn.labelStringText "
							+ " from CPY_ID ci, LIB lib1, LIB lib2, LCTN_VW lctn"
							+ " where ci.bibItemNumber ="
							+ amicusNumber
							+ " AND (ci.organisationNumber=lib1.organisationNumber and ci.branchOrganisationNumber=lib2.organisationNumber)"
							+ " AND (ci.branchOrganisationNumber=lctn.organisationNumber(+) and ci.locationNameCode=lctn.locationNumber(+))"
							+ " order by lib1.librarySymbolCode,lib2.librarySymbolCode,lctn.labelStringText,ci.barCodeNumber");
			listAllCopies = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}

		Iterator iter = listAllCopies.iterator();
		while (iter.hasNext()) {
			Object[] obj = (Object[]) iter.next();
			CPY_ID rawCopy = (CPY_ID) (obj[0]);
			if (rawCopy.getShelfListKeyNumber() != null) {
				try {
					rawCopy.setShelfList(new DAOShelfList().loadShelf(rawCopy
							.getShelfListKeyNumber().intValue()));
				} catch (Exception e) {
					System.out.println(rawCopy.getShelfListKeyNumber());
				}
			}
			result.add(rawCopy);
		}

		return result;
	}

	public List getAllCopiesByMainLibrary(int amicusNumber, int mainLibrary)
			throws DataAccessException {
		List listAllCopies = null;
		List result = new ArrayList();
		listAllCopies = find(" from CPY_ID ci where ci.bibItemNumber = ? "
				+ " AND ci.organisationNumber = ? ", new Object[] {
				new Integer(amicusNumber), new Integer(mainLibrary) },
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });

		Iterator iter = listAllCopies.iterator();
		while (iter.hasNext()) {
			CPY_ID rawCopy = (CPY_ID) iter.next();
			result.add(rawCopy);
		}
		return result;
	}

	public List getAllCopiesByBranch(int amicusNumber, int branchNumber)
			throws DataAccessException {
		List listAllCopies = null;
		List result = new ArrayList();

		listAllCopies = find("from CPY_ID ci where ci.bibItemNumber = ? "
				+ " AND ci.branchOrganisationNumber = ? ", new Object[] {
				new Integer(amicusNumber), new Integer(branchNumber) },
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		Iterator iter = listAllCopies.iterator();
		while (iter.hasNext()) {
			CPY_ID rawCopy = (CPY_ID) iter.next();
			result.add(rawCopy);
		}
		return result;
	}

	public List getAllCopiesByShelfListKey(int shelfListKeyNumber,
			int mainLibrary) throws DataAccessException {
		List listAllCopies = null;
		List result = new ArrayList();

		listAllCopies = find("from CPY_ID ci where ci.shelfListKeyNumber = ? "
				+ " AND ci.organisationNumber = ? ", new Object[] {
				new Integer(shelfListKeyNumber), new Integer(mainLibrary) },
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });

		Iterator iter = listAllCopies.iterator();
		while (iter.hasNext()) {
			CPY_ID rawCopy = (CPY_ID) iter.next();
			result.add(rawCopy);
		}
		return result;
	}

	/**
	 * @param amicusNumber
	 *            is the bibliographic number
	 * @return a list with all the copies from this amicusNumber access to
	 *         CPY_ID table
	 * @since 1.7
	 */

	public List getListCopiesElement(int amicusNumber, int mainLibrary,
			Locale locale) throws DataAccessException {
		List listAllCopies = null;
		List result = new ArrayList();
		DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
		DAOLocation dl = new DAOLocation();
		DAOShelfList dsl = new DAOShelfList();
		DAOCopyNotes dcn = new DAOCopyNotes();
		DAOInventory dci = new DAOInventory();
		DAODiscard ddsc = new DAODiscard();
		Format formatter = new SimpleDateFormat("dd/MM/yyyy");

		listAllCopies = getAllCopiesByMainLibrary(amicusNumber, mainLibrary);
		Iterator iter = listAllCopies.iterator();
		while (iter.hasNext()) {
			CPY_ID rawCopy = (CPY_ID) iter.next();

			CopyListElement rawCopyListElement = new CopyListElement(rawCopy);
			List discaredList = ddsc.loadItem(rawCopy.getCopyIdNumber());
			if (discaredList.size() > 0) {
				DiscardCopy discard = (DiscardCopy) discaredList.get(0);
				rawCopyListElement.setDiscardCode(discard.getDiscardCode());
				rawCopyListElement.setDiscardDate(formatter.format(discard
						.getDiscardDate()));
			}
			rawCopyListElement
					.setDueDate(getDueDate(rawCopy.getCopyIdNumber()));
			rawCopyListElement.setBranchSymbol(doh.getLibOrBranchSymbol(rawCopy
					.getBranchOrganisationNumber()));
			rawCopyListElement.setLibrarySymbol(doh
					.getLibOrBranchSymbol(rawCopy.getOrganisationNumber())); // NIC

			LCTN location = dl.load(rawCopy.getBranchOrganisationNumber(),
					rawCopy.getLocationNameCode(), locale);
			if (location == null) {
				logger.warn("Invalid location code in copy "
						+ rawCopy.getCopyIdNumber());
				throw new DataAccessException();
			} else {
				rawCopyListElement.setLocation(location.getLabelStringText());
			}

			if (rawCopy.getShelfListKeyNumber() != null) {
				rawCopy.setShelfList(new DAOShelfList().load(rawCopy
						.getShelfListKeyNumber().intValue()));
				if (rawCopy.getShelfList() != null) {
					rawCopyListElement
							.setShelfList((new StringText(rawCopy
									.getShelfList().getStringText()))
									.toDisplayString());
				}
			}
			/**
			 * MODIFICA BARBARA - inserire nella lista delle copie il tipo
			 * prestito e il posseduto
			 * 
			 * @since 1.0
			 */
			rawCopyListElement.setLoanType(new DAOCodeTable().getLongText(
					rawCopy.getLoanPrd(),
					T_LOAN_PRD.class, locale));
			rawCopyListElement.setCopyStatementText(rawCopy
					.getCopyStatementText());
			rawCopyListElement.setCopyRemarkNote(rawCopy.getCopyRemarkNote());

			rawCopyListElement.setHowManyNotes(dcn.getCopyNotesList(
					rawCopy.getCopyIdNumber(), locale).size());
			rawCopyListElement.setHowManyInventory(dci
					.getInventoryCount(rawCopy.getCopyIdNumber()));
			result.add(rawCopyListElement);
		}
		return result;
	}

	/** liographic number
	 * @return a list with all the copies from this amicusNumber access to
	 *         CPY_ID table
	 * @since 1.7
	 */

	public List getListCopiesElement(int mainLibrary, Locale locale)
			throws DataAccessException {
		List listAllCopies = null;
		List result = new ArrayList();
		DAOOrganisationHierarchy doh = new DAOOrganisationHierarchy();
		DAOLocation dl = new DAOLocation();
		DAOShelfList dsl = new DAOShelfList();
		DAOCopyNotes dcn = new DAOCopyNotes();
		DAOInventory dci = new DAOInventory();
		DAODiscard ddsc = new DAODiscard();
		Format formatter = new SimpleDateFormat("dd/MM/yyyy");

		listAllCopies = getAllCopies(mainLibrary);
		Iterator iter = listAllCopies.iterator();
		try {
			while (iter.hasNext()) {
				CPY_ID rawCopy = (CPY_ID) iter.next();

				CopyListElement rawCopyListElement = new CopyListElement(
						rawCopy);
				List discaredList = ddsc.loadItem(rawCopy.getCopyIdNumber());
				if (discaredList.size() > 0) {
					DiscardCopy discard = (DiscardCopy) discaredList.get(0);
					rawCopyListElement.setDiscardCode(discard.getDiscardCode());
					rawCopyListElement.setDiscardDate(formatter.format(discard
							.getDiscardDate()));
				}
				try {
					rawCopyListElement.setDueDate(getDueDate(rawCopy
							.getCopyIdNumber()));
				} catch (Exception e) {
				}

				rawCopyListElement.setBranchSymbol(doh
						.getLibOrBranchSymbol(rawCopy
								.getBranchOrganisationNumber()));
				rawCopyListElement.setLibrarySymbol(doh
						.getLibOrBranchSymbol(rawCopy.getOrganisationNumber())); // NIC

				LCTN location = dl.load(rawCopy.getBranchOrganisationNumber(),
						rawCopy.getLocationNameCode(), locale);
				if (location == null) {
					// logger.warn("Invalid location code in copy " +
					// rawCopy.getCopyIdNumber() + "(Org number : " +
					// rawCopy.getBranchOrganisationNumber() + " Location : " +
					// rawCopy.getLocationNameCode() + " Lang : " +
					// locale.getISO3Language() + ")");
					// throw new DataAccessException();
				} else {
					rawCopyListElement.setLocation(location
							.getLabelStringText());
				}

				if (rawCopy.getShelfListKeyNumber() != null) {
					rawCopy.setShelfList(new DAOShelfList().loadShelf(rawCopy
							.getShelfListKeyNumber().intValue()));
					if (rawCopy.getShelfList() != null) {
						rawCopyListElement.setShelfList((new StringText(rawCopy
								.getShelfList().getStringText()))
								.toDisplayString());
					}
				}
				/**
				 * MODIFICA BARBARA - inserire nella lista delle copie il tipo
				 * prestito e il posseduto
				 * 
				 * @since 1.0
				 */
				rawCopyListElement.setLoanType(new DAOCodeTable().getLongText(
						rawCopy.getLoanPrd(),
						T_LOAN_PRD.class, locale));
				rawCopyListElement.setCopyStatementText(rawCopy
						.getCopyStatementText());
				rawCopyListElement.setCopyRemarkNote(rawCopy
						.getCopyRemarkNote());

				rawCopyListElement.setHowManyNotes(dcn.getCopyNotesList(
						rawCopy.getCopyIdNumber(), locale).size());
				rawCopyListElement.setHowManyInventory(dci
						.getInventoryCount(rawCopy.getCopyIdNumber()));

				refineCopyListElementForSummarizeHolding(rawCopyListElement,
						result, locale);
				result.add(rawCopyListElement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void refineCopyListElementForSummarizeHolding(
			CopyListElement rawCopyListElement, List result, Locale locale) {

		Connection connection = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			connection = currentSession().connection();
			stmt = connection
					.prepareStatement("select hl.hldg_nbr,hl.hldg_stmt_txt,hl.bnd_vol_cpy_id from hldg hl left outer join hldg_bnd_cpy_acs_pnt bnd on hl.hldg_nbr=bnd.hldg_nbr left outer join hldg_cpy_acs_pnt cpy on hl.hldg_nbr=cpy.hldg_nbr where bnd.cpy_id_nbr=? or cpy.cpy_id_nbr=? or hl.bnd_vol_cpy_id=?");
			stmt.setInt(1, rawCopyListElement.getCopyIdNumber());
			stmt.setInt(2, rawCopyListElement.getCopyIdNumber());
			stmt.setInt(3, rawCopyListElement.getCopyIdNumber());
			rs = stmt.executeQuery();

			while (rs.next()) {
				rawCopyListElement.setHldgNbr(rs.getInt("hldg_nbr"));
				rawCopyListElement.setHldgText(rs.getString("hldg_stmt_txt"));
				rawCopyListElement.setBndCpyIdNbr(rs.getInt("bnd_vol_cpy_id"));
			}

			if (rawCopyListElement.getBndCpyIdNbr() != 0) {

				connection = currentSession().connection();
				stmt1 = connection
						.prepareStatement("select * from bnd_cpy where bib_itm_nbr=?");
				stmt1
						.setInt(1, rawCopyListElement.getCopy()
								.getBibItemNumber());
				rs1 = stmt1.executeQuery();

				while (rs1.next()) {

					CPY_ID cpy = new CPY_ID();
					cpy.setBibItemNumber(rawCopyListElement.getCopy()
							.getBibItemNumber());
					CopyListElement copy = new CopyListElement(cpy);

					int cpy_id = rs1.getInt("CPY_ID_NBR");
					int brnch = rs1.getInt("BRNCH_ORG_NBR");
					copy.setBndCpyIdNbr(rawCopyListElement.getBndCpyIdNbr());
					copy.getCopy().setCopyIdNumber(cpy_id);
					copy.setHldgNbr(rawCopyListElement.getHldgNbr());
					copy.getCopy().setBarCodeNumber(rs1.getString("BRCDE_NBR"));

					copy.getCopy().setBranchOrganisationNumber(brnch);
					copy.getCopy().setOrganisationNumber(rs1.getInt("ORG_NBR"));

					copy.setShelfList(getShelfListFromBndTable((rs1
							.getInt("SHLF_LIST_KEY_NBR"))));
					LCTN location = new DAOLocation().load(brnch, rs1
							.getShort("LCTN_NME_CDE"), locale);
					copy.setLocation(location.getLabelStringText());

					result.add(copy);
				}
			}

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				rs.close();
				if (rs1 != null)
					rs1.close();
			} catch (SQLException e) {
			}
			try {
				stmt.close();
				if (stmt1 != null)
					stmt1.close();
			} catch (SQLException e) {
			}
		}
	}

	private String getShelfListFromBndTable(int shlfKey) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = "";
		try {
			connection = currentSession().connection();
			stmt = connection
					.prepareStatement("select shlf_list_strng_text from bnd_shlf_list where shlf_list_key_nbr=?");
			stmt.setInt(1, shlfKey);

			rs = stmt.executeQuery();

			while (rs.next()) {
				String shlfText = rs.getString("shlf_list_strng_text");
				shlfText = new StringText(shlfText).toDisplayString();
				return shlfText;
			}
		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}

	public String getCopyStatus(int copyNumber, Character loanType)
			throws DataAccessException {
		String result = new String("");
/*
		DAOCirculationItem dci = new DAOCirculationItem();

		int loans = dci.getLoan(copyNumber);
		int reservations = dci.getReservation(copyNumber);
		try {
			Date date = dci.load(copyNumber).getCirculationItemDueDate();
		} catch (NullPointerException e) {
			result = "empty";
		}
		if (reservations >= 1) {
			result = "Reservation";
		} else if (!(result.equals("empty"))) {
			Date date = dci.load(copyNumber).getCirculationItemDueDate();
			Format formatter = new SimpleDateFormat("dd/MM/yyyy");
			String str = formatter.format(date);
			result = "On Loan " + str;
		} else if (loanType.equals("1") || loanType.equals("9")) {
			result = "Excluded";
		} else if (loanType.equals("E")) {
			result = "Retired";
		} else if (loans == 0 && !(loanType.equals("D"))) {
			result = "Available";
		}
*/
		return result;
	}

	public String getBarcode(int copyIdNumber) throws DataAccessException,
			HibernateException {
		String result = new String();
		List listAllCopies = null;
		try {
			Session s = currentSession();
			listAllCopies = s.find("from CPY_ID ci where ci.copyIdNumber = "
					+ copyIdNumber);

			Iterator iter = listAllCopies.iterator();
			while (iter.hasNext()) {
				CPY_ID rawCopy = (CPY_ID) iter.next();
				result = rawCopy.getBarCodeNumber();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

	public int getBranch(int copyIdNumber) throws DataAccessException,
			HibernateException {
		int result = 0;
		List listAllCopies = null;
		try {
			Session s = currentSession();
			listAllCopies = s.find("from CPY_ID ci where ci.copyIdNumber = "
					+ copyIdNumber);
			Iterator iter = listAllCopies.iterator();
			while (iter.hasNext()) {
				CPY_ID rawCopy = (CPY_ID) iter.next();
				result = rawCopy.getBranchOrganisationNumber();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

	public char getLoanPrd(int copyIdNumber) throws DataAccessException,
			HibernateException {

		char result = '0';
		List listAllCopies = null;
		try {
			Session s = currentSession();

			listAllCopies = (List) s.find(
					"from CPY_ID ci where ci.copyIdNumber = ?",
					new Object[] { new Integer(copyIdNumber) },
					new Type[] { Hibernate.INTEGER });

			Iterator iter = listAllCopies.iterator();
			while (iter.hasNext()) {
				CPY_ID rawCopy = (CPY_ID) iter.next();
				result = rawCopy.getLoanPrd();

			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

	public String getDueDate(int copyNumber) throws DataAccessException {
		String result = new String("");
/*		DAOCirculationItem dci = new DAOCirculationItem();

		CIRT_ITM ci = dci.load(copyNumber);
		if (ci != null) {
			Date date = ci.getCirculationItemDueDate();
			Format formatter = new SimpleDateFormat("dd/MM/yyyy");
			result = formatter.format(date);
		}
*/
		return result;
	}

	public List getBranchSymbolsHoldingCopies(int amicusNumber)
			throws DataAccessException {
		List raw = find(" select distinct cpy.branchOrganisationNumber, "
				+ " lib.librarySymbolCode from "
				+ " CPY_ID as cpy, LIB as lib "
				+ " where cpy.bibItemNumber = ? and "
				+ " cpy.branchOrganisationNumber = lib.organisationNumber",
				new Object[] { new Integer(amicusNumber) },
				new Type[] { Hibernate.INTEGER });
		List result = new ArrayList();
		Iterator iter = raw.iterator();
		Object[] aRow;
		while (iter.hasNext()) {
			aRow = (Object[]) iter.next();
			result.add(new Avp(String.valueOf(aRow[0]),
					(String) aRow[1]));
		}
		return result;
	}

	public List getBranchSymbolsHoldingCopies(int amicusNumber, int mainLibrary)
			throws DataAccessException {

		List raw;

		if (mainLibrary != 0)
			raw = find(" select distinct cpy.branchOrganisationNumber, "
					+ " lib.librarySymbolCode from "
					+ " CPY_ID as cpy, LIB as lib "
					+ " where cpy.bibItemNumber = ? and "
					+ " cpy.organisationNumber = ? and "
					+ " cpy.branchOrganisationNumber = lib.organisationNumber",
					new Object[] { new Integer(amicusNumber),
							new Integer(mainLibrary) }, new Type[] {
							Hibernate.INTEGER, Hibernate.INTEGER });
		else
			raw = find(" select distinct cpy.branchOrganisationNumber, "
					+ " lib.librarySymbolCode from "
					+ " CPY_ID as cpy, LIB as lib "
					+ " where cpy.bibItemNumber = ? and "
					+ " cpy.branchOrganisationNumber = lib.organisationNumber",
					new Object[] { new Integer(amicusNumber) },
					new Type[] { Hibernate.INTEGER });

		List result = new ArrayList();
		Iterator iter = raw.iterator();
		Object[] aRow;
		while (iter.hasNext()) {
			aRow = (Object[]) iter.next();
			result.add(new Avp(String.valueOf(aRow[0]),
					(String) aRow[1]));
		}
		return result;
	}

	public List getLibrarySymbolsHoldingCopies(int amicusNumber) // NIC
			throws DataAccessException {
		List raw = find(" select distinct cpy.organisationNumber, "
				+ " lib.librarySymbolCode from "
				+ " CPY_ID as cpy, LIB as lib "
				+ " where cpy.bibItemNumber = ? and "
				+ " cpy.organisationNumber = lib.organisationNumber",
				new Object[] { new Integer(amicusNumber) },
				new Type[] { Hibernate.INTEGER });
		List result = new ArrayList();
		Iterator iter = raw.iterator();
		Object[] aRow;
		while (iter.hasNext()) {
			aRow = (Object[]) iter.next();
			result.add(new Avp(String.valueOf(aRow[0]),
					(String) aRow[1]));
		}
		return result;
	}

	public void delete(final int copyNumber, final String userName) throws DataAccessException 
	{
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException, RecordNotFoundException,
					DataAccessException {
				// TODO make sure no circulation records (AMICUS doesn't)
				CPY_ID copy = (CPY_ID) s.get(CPY_ID.class, new Integer(
						copyNumber));
				if (copy.getShelfListKeyNumber() != null) {
					copy.setShelfList(new DAOShelfList().load(copy
							.getShelfListKeyNumber().intValue()));
				}
				if (copy == null) {
					throw new RecordNotFoundException();
				}

				// detach the shelflist
				detachShelfList(copy, copy.getShelfList());

				// Bug 2292: Scrive la tabella CPY_ID_AGENT
				saveCpyIdAgent(userName, copy.getCopyIdNumber());

				// delete the copy itself
				s.delete(copy);

				/*
				 * Natascia 27/06/2007 : se non ci sono piu' copie per la
				 * biblioteca selezionata, devo cancellare il tag 850 nella
				 * tabella smry_hldg **PRN 223
				 */
				DAOSummaryHolding ds = new DAOSummaryHolding();
				ds.deleteRecord(copy.getBibItemNumber(), copy
						.getOrganisationNumber());
				/* */

			}
		}.execute();
	}

	public void detachShelfList(CPY_ID copy, SHLF_LIST shelf)
			throws DataAccessException {
		Session s = currentSession();

		if (shelf == null) {
			return;
		}
		try {
			/*
			 * If only our copies bib_item is using this shelf list then remove
			 * the entry from SHLF_LIST_ACS_PNT
			 */

			if (countShelfFromCopyUses(copy, shelf) != 0) {
				if (countShelfListAccessPointUses(copy, shelf) == 1) {
					logger.warn("Cancella  SHLF_LIST_ACS_PNT");

					s
							.delete(
									"from SHLF_LIST_ACS_PNT as c where c.shelfListKeyNumber = ?"
											+ " and c.bibItemNumber = ?",
									new Object[] {
											new Integer(shelf
													.getShelfListKeyNumber()),
											new Integer(copy.getBibItemNumber()) },
									new Type[] { Hibernate.INTEGER,
											Hibernate.INTEGER });
					/*
					 * AND if only our copy is using this shelf list number then
					 * delete the shelf list number
					 */
					List l = find(
							"select count(*) from CPY_ID as c where c.shelfListKeyNumber = ?",
							new Object[] { new Integer(shelf
									.getShelfListKeyNumber()) },
							new Type[] { Hibernate.INTEGER });
					if (l.size() > 0 && ((Integer) l.get(0)).intValue() == 1) {
						s.delete(shelf);
					}
				}
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
	}

	/**
	 * Counts the number of copies using this shelf list and bib_itm
	 * 
	 * @since 1.0
	 */
	public int countShelfListAccessPointUses(CPY_ID copy, SHLF_LIST shelf)
			throws DataAccessException {
		List l = find(
				"select count(*) from CPY_ID as c where c.shelfListKeyNumber = ?"
						+ " and c.bibItemNumber = ?", new Object[] {
						new Integer(shelf.getShelfListKeyNumber()),
						new Integer(copy.getBibItemNumber()) }, new Type[] {
						Hibernate.INTEGER, Hibernate.INTEGER });
		if (l.size() > 0) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}

	/**
	 * Counts the number of copies using this shelf list and bib_itm
	 * 
	 * @since 1.0
	 */
	public int countShelfListAccessPointUsesForModifyHeading(CPY_ID copy,
			SHLF_LIST shelf) throws DataAccessException {
		List l = find(
				"select count(*) from SHLF_LIST_ACS_PNT as c where c.shelfListKeyNumber = ?"
						+ " and c.bibItemNumber = ? and c.mainLibraryNumber=?",
				new Object[] { new Integer(shelf.getShelfListKeyNumber()),
						new Integer(copy.getBibItemNumber()),
						new Integer(copy.getOrganisationNumber()) },
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER,
						Hibernate.INTEGER });
		if (l.size() > 0) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}

	public int countCopyByShelf(CPY_ID copy, SHLF_LIST shelf)
			throws DataAccessException {
		DAOShelfList ds = (DAOShelfList) shelf.getDAO();
		SHLF_LIST match = (SHLF_LIST) ds.getMatchingHeading(shelf);
		if (match != null) {
			List l = find(
					"select count(*) from CPY_ID as c where c.shelfListKeyNumber = ? and c.copyIdNumber<> ?",
					new Object[] { new Integer(match.getShelfListKeyNumber()),
							new Integer(copy.getCopyIdNumber()) }, new Type[] {
							Hibernate.INTEGER, Hibernate.INTEGER });
			if (l.size() > 0) {
				return ((Integer) l.get(0)).intValue();
			}
		}
		return 0;
	}

	/**
	 * Counts the number of copies using this shelf list and bib_itm
	 * 
	 * @since 1.0
	 */
	public int countShelfFromCopyUses(CPY_ID copy, SHLF_LIST shelf)
			throws DataAccessException {
		List l = find(
				"select count(*) from CPY_ID as c where c.shelfListKeyNumber = ?"
						+ " and c.bibItemNumber = ?"
						+ " and c.copyIdNumber = ?", new Object[] {
						new Integer(shelf.getShelfListKeyNumber()),
						new Integer(copy.getBibItemNumber()),
						new Integer(copy.getCopyIdNumber()) },
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER,
						Hibernate.INTEGER });
		if (l.size() > 0) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}

	/**
	 * Handles the persistence of a CPY_ID including attached SHLF_LIST,
	 * ShelfListAccessPoint and SummaryHolding The shelflist contained in the
	 * copy is to be persisted. If the copy on the DB contains a different
	 * shelflist it is contained in oldShelfList and will be detached
	 * (consistent with db integrity rules)
	 * 
	 * @param copy
	 *            the copy to be persisted
	 * @param oldShelfList
	 *            an optional SHLF_LIST object to be detached from this copy
	 * @throws DataAccessException
	 */
	public void saveCopy(final CPY_ID copy, final SHLF_LIST oldShelfList, final String userName) throws DataAccessException 
	{
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException, DataAccessException {
				/*
				 * 
				 */
				if (oldShelfList != null) {
					detachShelfList(copy, oldShelfList);
				}
				if (copy.getShelfList() != null) {
					logger.debug("shelf is not null!!");

					if (copy.getShelfList().isChanged()) {
						/*
						 * If the shelf list is changed, the approach is to
						 * detach the current shelf list (which will be deleted
						 * if it is no longer used), and then to generate a new
						 * shelf list with the modified settings
						 */
						detachShelfList(copy, copy.getShelfList());
						/*
						 * TODO originally an evict was here. It may be safer to
						 * deep copy and or newInstance at this point
						 */
						copy.getShelfList().markNew();
					}
					if (copy.getShelfList().getUpdateStatus() == UpdateStatus.UNCHANGED) {
						// if the edited version was scanned from the db then
						// add
						// a new entry to the acs_pnt table
						logger.debug("shelf is UNCHANGED");
						attachShelfList(copy, copy.getShelfList());
					}
					if (copy.getShelfList().isNew()) {
						SHLF_LIST match = getMatchHeading(copy);
						if (match != null) {
							copy.setShelfList(match);
						} else {
							SHLF_LIST shelf = (SHLF_LIST) LibrisuiteUtils
									.deepCopy(copy.getShelfList());
							shelf.generateNewKey();
							copy.setShelfList(shelf);
							/*
							 * When string text is modified in UI, the sortform
							 * is not changed (still old value). The trigger to
							 * set the sortform is only activated when the
							 * inserted sortform is null. Without the below
							 * line, the SHLF_LIST_I3 unique index is violated
							 */
							copy.getShelfList().setSortForm(null); // otherwise
						}
						attachShelfList(copy, copy.getShelfList());
						copy.markChanged();
					}

					persistByStatus(copy.getShelfList());
					copy.setShelfListKeyNumber(new Integer(copy.getShelfList()
							.getShelfListKeyNumber()));
				} else { // shelf list is null
					logger.debug("setting key to null");
					copy.setShelfListKeyNumber(null);
				}
				if (copy.isNew()) {
					copy.generateNewKey();
					//copy.setCreationDate(new Date());
				}
				
				// Bug 2292: Scrive la tabella CPY_ID_AGENT
				saveCpyIdAgent(userName, copy.getCopyIdNumber());

				persistByStatus(copy);
				createSummaryHolding(copy);
			}
		}.execute();
	}

	/**
	 * @param copy
	 * @return
	 * @throws DataAccessException
	 */
	public SHLF_LIST getMatchHeading(CPY_ID copy) throws DataAccessException {
		DAOShelfList ds = (DAOShelfList) copy.getShelfList().getDAO();
		SHLF_LIST match = (SHLF_LIST) ds
				.getMatchingHeading(copy.getShelfList());
		return match;
	}

	private void createSummaryHolding(CPY_ID copy) throws DataAccessException {
		new DAOSummaryHolding().createSummaryHoldingIfRequired(copy);
	}

	public void attachShelfList(CPY_ID copy, SHLF_LIST shelf)
			throws DataAccessException {
		if (countShelfListAccessPointUses(copy, shelf) == 0) {
			// logger.warn("Attacca SHLF_LIST_ACS_PNT");
			try {
				SHLF_LIST_ACS_PNT ap = new SHLF_LIST_ACS_PNT(copy
						.getBibItemNumber(), copy.getOrganisationNumber(),
						shelf.getShelfListKeyNumber());
				currentSession().save(ap);
			} catch (HibernateException e) {
				logAndWrap(e);
			}
		}
	}

	public void attachShelfListForModifyCopy(CPY_ID copy, SHLF_LIST shelf)
			throws DataAccessException {
		if (countShelfFromCopyUses(copy, shelf) != 0) {
			if (countShelfListAccessPointUses(copy, shelf) == 1) {
				// logger.warn("Attacca SHLF_LIST_ACS_PNT");
				try {
					SHLF_LIST_ACS_PNT ap = new SHLF_LIST_ACS_PNT(copy
							.getBibItemNumber(), copy.getOrganisationNumber(),
							shelf.getShelfListKeyNumber());
					currentSession().save(ap);
				} catch (HibernateException e) {
					logAndWrap(e);
				}
			}
		}
	}

	public void saveOrUpdateShelfList(CPY_ID copy, SHLF_LIST shelf)
			throws DataAccessException {
		try {
			SHLF_LIST_ACS_PNT ap = new SHLF_LIST_ACS_PNT(copy
					.getBibItemNumber(), copy.getOrganisationNumber(), shelf
					.getShelfListKeyNumber());
			currentSession().update(ap);

		} catch (HibernateException e) {
			logAndWrap(e);
		}
	}

	public void edit(final CPY_ID copyId) throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException {
				s.update(copyId);
			}
		}.execute();
	}

	public Integer getOrganizationNumbe(int copyIdNumber) throws SQLException {
		Connection connection = null;
		PreparedStatement queryStatement = null;
		ResultSet resultSet = null;
		try {

			connection = currentSession().connection();
			queryStatement = connection
					.prepareStatement("SELECT ORG_NBR FROM CPY_ID WHERE CPY_ID_NBR=?");
			queryStatement.setInt(1, copyIdNumber);
			resultSet = queryStatement.executeQuery();

			resultSet.next();

			return resultSet.getInt("ORG_NBR");

		} catch (SQLException exception) {
			throw exception;
		} catch (Exception exception) {
			throw new SQLException(exception);
		} finally {
			try {
				resultSet.close();
			} catch (Exception ignore) {
			}
			try {
				queryStatement.close();
			} catch (Exception ignore) {
			}

		}
	}

	public List<String> viewWhereRecordCreated(int amicusNumber)
			throws SQLException {
		Connection connection = null;
		PreparedStatement queryStatement = null;
		ResultSet resultSet = null;
		List<String> views = new ArrayList<String>();
		try {

			connection = currentSession().connection();
			queryStatement = connection
					.prepareStatement("SELECT USR_VW_IND FROM BIB_ITM WHERE BIB_ITM_NBR=?");
			queryStatement.setInt(1, amicusNumber);
			resultSet = queryStatement.executeQuery();

			while (resultSet.next()) {
				views.add(resultSet.getString("USR_VW_IND"));
			}

			return views;

		} catch (SQLException exception) {
			throw exception;
		} catch (Exception exception) {
			throw new SQLException(exception);
		} finally {
			try {
				resultSet.close();
			} catch (Exception ignore) {
			}
			try {
				queryStatement.close();
			} catch (Exception ignore) {
			}

		}
	}

	public String calculateSortForm(String text, SortFormParameters parms)
			throws DataAccessException, SortFormException {
		String result = "";
		int bufSize = 600;
		int rc;

		Session s = currentSession();
		CallableStatement proc = null;
		Connection connection = null;

		try {
			connection = s.connection();
			proc = connection
					.prepareCall("{ ? = call AMICUS.PACK_SORTFORM.SF_PREPROCESS(?, ?, ?, ?, ?, ?, ?, ?) }");
			proc.registerOutParameter(1, Types.INTEGER);
			proc.setString(2, text);
			proc.registerOutParameter(3, Types.VARCHAR);
			proc.setInt(4, bufSize);
			proc.setInt(5, parms.getSortFormMainType());
			proc.setInt(6, parms.getSortFormSubType());
			proc.setInt(7, parms.getNameTitleOrSubjectType());
			proc.setInt(8, parms.getNameSubtype());
			proc.setInt(9, parms.getSkipInFiling());
			proc.execute();

			rc = proc.getInt(1);

			if (rc != 0) {
				throw new SortFormException(String.valueOf(rc));
			}
			result = proc.getString(3);

			proc.close();

			proc = connection
					.prepareCall("{ ? = call AMICUS.PACK_SORTFORM.SF_BUILDSRTFRM(?, ?, ?, ?, ?, ?, ?, ?) }");
			proc.registerOutParameter(1, Types.INTEGER);
			proc.setString(2, result);
			proc.registerOutParameter(3, Types.VARCHAR);
			proc.setInt(4, bufSize);
			proc.setInt(5, parms.getSortFormMainType());
			proc.setInt(6, parms.getSortFormSubType());
			proc.setInt(7, parms.getNameTitleOrSubjectType());
			proc.setInt(8, parms.getNameSubtype());
			proc.setInt(9, parms.getSkipInFiling());
			proc.execute();

			rc = proc.getInt(1);

			if (rc != 0) {
				throw new SortFormException(String.valueOf(rc));
			}
			result = proc.getString(3);
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				if (proc != null) {
					proc.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void createHardback(List<CopyListElement> elements, String posseduto) {

		Connection connection = null;
		Statement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;

		try {
			connection = currentSession().connection();
			stmt = connection.createStatement();
			rs = stmt
					.executeQuery("select HLDG_SEQ.NEXTVAL   l_hldg_nbr from DUAL");
			int value = 0;

			while (rs.next())
				value = rs.getInt("l_hldg_nbr");

			insertHardbackTable(elements, posseduto, value);

			stmt1 = connection
					.prepareStatement("INSERT INTO HLDG(HLDG_NBR, HLDG_STMT_TXT) VALUES (?, ?)");
			stmt1.setInt(1, value);
			stmt1.setString(2, posseduto);
			stmt1.executeUpdate();

			// for(CopyListElement element: elements)
			// {
			// stmt2 = connection.prepareStatement("INSERT INTO
			// HLDG_CPY_ACS_PNT(HLDG_NBR, CPY_ID_NBR) VALUES (?, ?)");
			// stmt2.setInt(1, value);
			// stmt2.setInt(2, element.getCopy().getCopyIdNumber());
			// stmt2.executeUpdate();
			// }

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				stmt.close();
			} catch (SQLException e) {
			}
			try {

				stmt1.close();
			} catch (SQLException e) {
			}

		}
	}

	public void insertHardbackTable(List<CopyListElement> elements,
			String posseduto, Integer value) {

		Connection connection = null;
		PreparedStatement stmt2 = null;
		try {
			connection = currentSession().connection();

			for (CopyListElement element : elements) {
				stmt2 = connection
						.prepareStatement("INSERT INTO HLDG_CPY_ACS_PNT(HLDG_NBR, CPY_ID_NBR) VALUES (?, ?)");
				stmt2.setInt(1, value);
				stmt2.setInt(2, element.getCopy().getCopyIdNumber());
				stmt2.executeUpdate();
			}

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				stmt2.close();

			} catch (SQLException e) {
			}
		}
	}

	public void removeHardback(String[] copyIdList, Integer value) {

		Connection connection = null;
		PreparedStatement stmt2 = null;
		try {
			connection = currentSession().connection();

			for (int i = 0; i < copyIdList.length; i++) {
				stmt2 = connection
						.prepareStatement("DELETE HLDG_CPY_ACS_PNT  WHERE HLDG_NBR=? AND CPY_ID_NBR=?");
				stmt2.setInt(1, value);
				stmt2.setInt(2, Integer.parseInt(copyIdList[i]));
				stmt2.executeUpdate();
			}

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				stmt2.close();
			} catch (SQLException e) {
			}
		}
	}

	public void removeHardback(Integer value) {

		Connection connection = null;

		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;

		try {
			connection = currentSession().connection();
			stmt2 = connection
					.prepareStatement("DELETE HLDG  WHERE HLDG_NBR=?");
			stmt2.setInt(1, value);
			stmt2.executeUpdate();

			stmt1 = connection
					.prepareStatement("DELETE HLDG_CPY_ACS_PNT WHERE HLDG_NBR=?");
			stmt1.setInt(1, value);
			stmt1.executeUpdate();

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				stmt2.close();
			} catch (SQLException e) {
			}
			try {
				stmt1.close();
			} catch (SQLException e) {
			}
		}
	}

	public void removeHardbackHLDG_CPY_ACS_PNT(Integer value) {

		Connection connection = null;
		PreparedStatement stmt2 = null;
		try {
			connection = currentSession().connection();

			stmt2 = connection
					.prepareStatement("DELETE HLDG_CPY_ACS_PNT  WHERE HLDG_NBR=?");
			stmt2.setInt(1, value);
			stmt2.executeUpdate();

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				stmt2.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public void insertDefinitiveHardbackBNDTable(Integer cpy_id,
			Integer hldg_nbr) {

		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = currentSession().connection();

			stmt = connection
					.prepareStatement("INSERT INTO HLDG_BND_CPY_ACS_PNT (HLDG_NBR, CPY_ID_NBR) VALUES (?, ?)");
			stmt.setInt(1, hldg_nbr);
			stmt.setInt(2, cpy_id);
			stmt.executeUpdate();

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	public void deleteTemporaryCopiesFromCPY_IDTable(Integer hldg_nbr) {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = currentSession().connection();

			stmt = connection
					.prepareStatement("DELETE CPY_ID WHERE CPY_ID_NBR IN (SELECT CPY_ID_NBR FROM HLDG_CPY_ACS_PNT WHERE HLDG_NBR=? )");
			stmt.setInt(1, hldg_nbr);
			stmt.executeUpdate();

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	public void updatedefiniteCopyHLDG(Integer cpy_id, Integer hldg_nbr) {

		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = currentSession().connection();

			stmt = connection
					.prepareStatement("UPDATE HLDG SET BND_VOL_CPY_ID=? WHERE HLDG_NBR=?");
			stmt.setInt(1, cpy_id);
			stmt.setInt(2, hldg_nbr);
			stmt.executeUpdate();

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}

	}

	public void deleteHardbackShelflistKey(Integer hldg_nbr) {

		Connection connection = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			connection = currentSession().connection();

			stmt = connection
					.prepareStatement("SELECT A.SHLF_LIST_KEY_NBR FROM CPY_ID A WHERE A.CPY_ID_NBR IN (SELECT CPY_ID_NBR FROM HLDG_CPY_ACS_PNT WHERE HLDG_NBR=?)");
			stmt.setInt(1, hldg_nbr);
			rs = stmt.executeQuery();

			while (rs.next()) {
				int key = rs.getInt("SHLF_LIST_KEY_NBR");

				stmt2 = connection
						.prepareStatement("SELECT count(*) FROM CPY_ID A WHERE A.SHLF_LIST_KEY_NBR=?");
				stmt2.setInt(1, key);
				rs2 = stmt2.executeQuery();
				int count = 0;
				if (rs2.next())
					count = rs2.getInt(1);

				if (count == 1) {
					stmt3 = connection
							.prepareStatement("DELETE FROM SHLF_LIST WHERE SHLF_LIST_KEY_NBR=?");
					stmt3.setInt(1, key);
					stmt3.executeUpdate();

					stmt4 = connection
							.prepareStatement("DELETE FROM SHLF_LIST_ACS_PNT WHERE SHLF_LIST_KEY_NBR=?");
					stmt4.setInt(1, key);
					stmt4.executeUpdate();
				} else if (count > 1) {
					stmt4 = connection
							.prepareStatement("DELETE FROM SHLF_LIST_ACS_PNT WHERE SHLF_LIST_KEY_NBR=?");
					stmt4.setInt(1, key);
					stmt4.executeUpdate();
				}

			}

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				if (rs2 != null)
					rs2.close();
			} catch (SQLException e) {
			}
			try {
				stmt.close();
			} catch (SQLException e) {
			}
			try {
				if (stmt2 != null)
					stmt2.close();
			} catch (SQLException e) {
			}
			try {
				if (stmt3 != null)
					stmt3.close();
			} catch (SQLException e) {
			}
			try {
				if (stmt4 != null)
					stmt4.close();
			} catch (SQLException e) {
			}

		}

	}

	private String INSERT_BND_CPY = "INSERT INTO BND_CPY a "
			+ "SELECT BIB_ITM_NBR," + "CPY_ID_NBR," + "SHLF_LIST_KEY_NBR,"
			+ "ORG_NBR," + "BRNCH_ORG_NBR," + "ORGNL_ORG_NBR," + "BRCDE_NBR,"
			+ "DYNIX_SRL_ID_NBR," + "TRSTN_DTE," + "CRTN_DTE," + "ILL_CDE,"
			+ "HLDG_SBCPT_STUS_CDE," + "HLDG_RTNTN_CDE," + "LOAN_PRD_CDE,"
			+ "HLDG_SRS_TRMT_CDE," + "HLDG_STUS_TYP_CDE," + "LCTN_NME_CDE,"
			+ "HLDG_LVL_OF_DTL_CDE," + "HLDG_ACSN_LIST_CDE," + "CPY_NBR_DSC,"
			+ "CPY_RMRK_NTE," + "CPY_STMT_TXT," + "CPY_RMRK_NTE_SRT_FORM,"
			+ "TMP_LCTN_ORG_NBR," + "TMP_LCTN_NME_CDE," + "MTRL_DESC,"
			+ "CST, " + "CURCY_TYP_CDE, " + "CURCY_XCHNG_RTE,"
			+ "TRSFR_CSTDY_NBR, " + "PHSCL_CPY_TPE,"
			+ "MTHD_ACQ FROM cpy_id b WHERE b.cpy_id_nbr=?";

	public void insertBND_CPY(Integer hldg_nbr) {

		Connection connection = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		try {
			connection = currentSession().connection();

			connection = currentSession().connection();
			stmt = connection
					.prepareStatement("SELECT CPY_ID_NBR FROM HLDG_CPY_ACS_PNT WHERE HLDG_NBR=?");
			stmt.setInt(1, hldg_nbr);

			rs = stmt.executeQuery();

			while (rs.next()) {
				int cpy_id_nbr = rs.getInt("CPY_ID_NBR");
				stmt1 = connection.prepareStatement(INSERT_BND_CPY);
				stmt1.setInt(1, cpy_id_nbr);
				stmt1.executeUpdate();
			}

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {

			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				stmt.close();
			} catch (SQLException e) {
			}
			try {
				if (stmt1 != null)
					stmt1.close();
			} catch (SQLException e) {
			}
		}
	}

	private String INSERT_BND_SHLF_LIST = "INSERT INTO BND_SHLF_LIST "
			+ "SELECT ORG_NBR,SHLF_LIST_KEY_NBR,SHLF_LIST_TYP_CDE,SHLF_LIST_STRNG_TEXT,SHLF_LIST_SRT_FORM FROM SHLF_LIST A WHERE A.SHLF_LIST_KEY_NBR=?";

	public void insertBND_SHLF_LIST(Integer hldg_nbr) {

		Connection connection = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		try {
			connection = currentSession().connection();

			connection = currentSession().connection();
			stmt = connection
					.prepareStatement("SELECT A.SHLF_LIST_KEY_NBR FROM CPY_ID A WHERE A.CPY_ID_NBR IN (SELECT CPY_ID_NBR FROM HLDG_CPY_ACS_PNT WHERE HLDG_NBR=?)");
			stmt.setInt(1, hldg_nbr);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int shlfListKey = rs.getInt("SHLF_LIST_KEY_NBR");
				if (!isBNDShelfList(shlfListKey)) {
					stmt1 = connection.prepareStatement(INSERT_BND_SHLF_LIST);
					stmt1.setInt(1, shlfListKey);
					stmt1.executeUpdate();
				}
			}

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {

			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				stmt.close();
			} catch (SQLException e) {
			}
			try {
				if (stmt1 != null)
					stmt1.close();
			} catch (SQLException e) {
			}
		}

	}

	private boolean isBNDShelfList(int shelfListKey) {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = currentSession().connection();

			connection = currentSession().connection();
			stmt = connection
					.prepareStatement("SELECT A.SHLF_LIST_KEY_NBR FROM BND_SHLF_LIST A WHERE A.SHLF_LIST_KEY_NBR=?");
			stmt.setInt(1, shelfListKey);
			rs = stmt.executeQuery();

			return rs.next();

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {

			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return false;
	}

	public void definitivehardback(Integer cpy_id, Integer hldg_nbr,
			List<CopyListElement> elements) {

		updatedefiniteCopyHLDG(cpy_id, hldg_nbr);
		insertDefinitiveHardbackBNDTable(elements, "", hldg_nbr);
		insertBND_CPY(hldg_nbr);
		insertBND_SHLF_LIST(hldg_nbr);
		deleteHardbackShelflistKey(hldg_nbr);
		deleteTemporaryCopiesFromCPY_IDTable(hldg_nbr);
		removeHardbackHLDG_CPY_ACS_PNT(hldg_nbr);

	}

	public boolean isLockCopy(int bibItmNbr) throws DataAccessException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = currentSession().connection();
			stmt = connection
					.prepareStatement("SELECT * FROM S_LCK_TBL WHERE TBL_KEY_NBR=?");
			stmt.setInt(1, bibItmNbr);

			rs = stmt.executeQuery();

			return rs.next();

		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	public boolean isLockCopyFromUser(int bibItmNbr, String userName)
			throws DataAccessException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = currentSession().connection();
			stmt = connection
					.prepareStatement("SELECT * FROM S_LCK_TBL WHERE TBL_KEY_NBR=? AND USR_NME=?");
			stmt.setInt(1, bibItmNbr);
			stmt.setString(2, fixedCharPadding(userName, 12));

			rs = stmt.executeQuery();

			return rs.next();

		} catch (Exception e) {
			throw new DataAccessException();
		}
	}

	/**
	 * Il metodo controllo se ci sono copie (non da trasferire) associate al
	 * record origine per l'org
	 * 
	 * @param amicusNumber
	 * @param orgNumber
	 * @param condition
	 * @return
	 * @throws DataAccessException
	 */
	public int countCopies(int amicusNumber, int orgNumber, String condition)
			throws DataAccessException {
		List l = find("select count(*) from CPY_ID as c"
				+ " where c.organisationNumber = ?"
				+ " and c.bibItemNumber = ?" + " and c.copyIdNumber "
				+ condition, new Object[] { new Integer(orgNumber),
				new Integer(amicusNumber) }, new Type[] { Hibernate.INTEGER,
				Hibernate.INTEGER });

		if (l.size() > 0) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}

	/**
	 * Il metodo controllo se ci sono copie (non da trasferire) associate al
	 * record origine per l'org e con la stessa shelfListKeyNumber
	 * 
	 * @param amicusNumber
	 * @param orgNumber
	 * @param shelfListKeyNumber
	 * @param condition
	 * @return
	 * @throws DataAccessException
	 */
	public int countCopiesByShelfListKeyNbr(int amicusNumber, int orgNumber,
			int shelfListKeyNumber, String condition)
			throws DataAccessException {
		List l = find("select count(*) from CPY_ID as c"
				+ " where c.organisationNumber = ?"
				+ " and c.bibItemNumber = ?" + " and c.shelfListKeyNumber = ?"
				+ " and c.copyIdNumber " + condition, new Object[] {
				new Integer(orgNumber), new Integer(amicusNumber),
				new Integer(shelfListKeyNumber) }, new Type[] {
				Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER });

		if (l.size() > 0) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}

	public List<Avp> getDescriptionSubfield4()
			throws DataAccessException {
		List raw = find("SELECT distinct a.code  FROM T_NME_WRK_RLTR as a ORDER BY a.code ASC");
		List<Avp> result = new ArrayList<Avp>();
		Iterator iter = raw.iterator();
		while (iter.hasNext()) {
			String row = (String) iter.next();
			result.add(new Avp(row, row));
		}
		return result;
	}

	public List<Avp> getDescriptionSubfieldE()
			throws DataAccessException {
		List<Avp> result = new ArrayList<Avp>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = currentSession().connection();
			stmt = connection
					.prepareStatement("SELECT T_NME_WRK_RLTR.TBL_LNG_ENG_TXT FROM T_NME_WRK_RLTR  ORDER BY T_NME_WRK_RLTR.TBL_LNG_ENG_TXT ASC");
			rs = stmt.executeQuery();

			while (rs.next()) {
				String row = rs.getString("TBL_LNG_ENG_TXT");
				result.add(new Avp(row, row));
			}
			return result;

		} catch (Exception e) {
			try {
				logAndWrap(e);
				return result;
			} catch (DataAccessException e1) {
				logAndWrap(e1);
				return result;
			} finally {
				try {
					rs.close();
				} catch (SQLException s) {
				}
				try {
					stmt.close();
				} catch (SQLException q) {
				}
			}
		}
	}

	/* Bug 2292 */
	public void saveCpyIdAgent(String userName, int cpyIdNbr) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt0 = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		Session session = currentSession();
		int count = 0;
		try {
			connection = session.connection();
			// --------> Lock della riga
			stmt0 = connection.prepareStatement("SELECT * FROM CPY_ID_AGENT WHERE CPY_ID_NBR = ? FOR UPDATE");
			stmt0.setInt(1, cpyIdNbr);

			stmt1 = connection.prepareStatement("UPDATE CPY_ID_AGENT SET USERNAME = ?, TRSTN_DTE = SYSDATE , AGENT_ID = 1 WHERE CPY_ID_NBR = ?");
			stmt1.setString(1, userName);
			// stmt1.setDate(2, new java.sql.Date(System.currentTimeMillis()));
			stmt1.setInt(2, cpyIdNbr);
			count = stmt1.executeUpdate();

			if (!(count > 0)) {
				stmt2 = connection.prepareStatement("INSERT INTO CPY_ID_AGENT (CPY_ID_NBR, USERNAME, AGENT_ID, TRSTN_DTE) VALUES (?,?,?,SYSDATE)");
				stmt2.setInt(1, cpyIdNbr);
				stmt2.setString(2, userName);
				stmt2.setInt(3, 1);
				// stmt2.setDate(4, new
				// java.sql.Date(System.currentTimeMillis()));
				stmt2.execute();
			}
			/* Il commit o rollback lo fa hibernate in automatico se le operazioni successive vanno bene: quindi se sulla CPY_ID va tutto ok committa altrimenti no */
			// connection.commit(); 
		} catch (HibernateException e) {
			e.printStackTrace();
			logAndWrap(e);
			// try {
			// connection.rollback();
			// } catch (SQLException e1) {
			// e1.printStackTrace();
			// }
		} catch (SQLException e) {
			e.printStackTrace();
			logAndWrap(e);
			// try {
			// connection.rollback();
			// } catch (SQLException e1) {
			// e1.printStackTrace();
			// }
		}

		finally {
			try {
				if (stmt0 != null) {
					stmt0.close();
				}
				if (stmt1 != null) {
					stmt1.close();
				}
				if (stmt2 != null) {
					stmt2.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logAndWrap(e);
			}
		}
	}

	public void insertDefinitiveHardbackBNDTable(
			List<CopyListElement> elements, String posseduto, Integer value) {

		Connection connection = null;
		PreparedStatement stmt2 = null;
		try {
			connection = currentSession().connection();

			for (CopyListElement element : elements) {
				stmt2 = connection
						.prepareStatement("INSERT INTO HLDG_BND_CPY_ACS_PNT (HLDG_NBR, CPY_ID_NBR) VALUES (?, ?)");
				stmt2.setInt(1, value);
				stmt2.setInt(2, element.getCopy().getCopyIdNumber());
				stmt2.executeUpdate();
			}

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				stmt2.close();

			} catch (SQLException e) {
			}
		}
	}

	/**
	 * remove the final binding in the tables : BND_SHLF_LIST, BND_CPY, HLDG,
	 * HLDG_BND_CPY_ACS_PNT
	 */
	public void removeDefinitiveHardback(Integer hldgNbr) {
		removeDefinitiveBndShelfAndCopy(hldgNbr);
		removeDefinitiveHldgAndBndCopy(hldgNbr);
	}

	public void removeDefinitiveHldgAndBndCopy(Integer hldgNbr) {

		Connection connection = null;

		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;

		try {
			connection = currentSession().connection();
			stmt2 = connection
					.prepareStatement("DELETE HLDG  WHERE HLDG_NBR=?");
			stmt2.setInt(1, hldgNbr);
			stmt2.executeUpdate();

			stmt1 = connection
					.prepareStatement("DELETE HLDG_BND_CPY_ACS_PNT WHERE HLDG_NBR=?");
			stmt1.setInt(1, hldgNbr);
			stmt1.executeUpdate();

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				stmt2.close();
			} catch (SQLException e) {
			}
			try {
				stmt1.close();
			} catch (SQLException e) {
			}
		}
	}

	public void removeDefinitiveBndShelfAndCopy(Integer hldg_nbr) {

		Connection connection = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			connection = currentSession().connection();

			stmt = connection
					.prepareStatement("SELECT A.SHLF_LIST_KEY_NBR, A.CPY_ID_NBR  FROM BND_CPY A WHERE A.CPY_ID_NBR IN (SELECT CPY_ID_NBR FROM HLDG_BND_CPY_ACS_PNT WHERE HLDG_NBR=?)");
			stmt.setInt(1, hldg_nbr);
			rs = stmt.executeQuery();

			while (rs.next()) {
				int key = rs.getInt("SHLF_LIST_KEY_NBR");
				int cpyId = rs.getInt("CPY_ID_NBR");

				stmt2 = connection
						.prepareStatement("SELECT count(*) FROM BND_CPY A WHERE A.SHLF_LIST_KEY_NBR=?");
				stmt2.setInt(1, key);
				rs2 = stmt2.executeQuery();
				int count = 0;
				if (rs2.next())
					count = rs2.getInt(1);

				if (count == 1) {
					stmt3 = connection
							.prepareStatement("DELETE FROM BND_SHLF_LIST WHERE SHLF_LIST_KEY_NBR=?");
					stmt3.setInt(1, key);
					stmt3.executeUpdate();

				}
				stmt4 = connection
						.prepareStatement("DELETE FROM BND_CPY WHERE SHLF_LIST_KEY_NBR=? and CPY_ID_NBR =?");
				stmt4.setInt(1, key);
				stmt4.setInt(2, cpyId);
				stmt4.executeUpdate();

			}

		} catch (Exception e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				e1.printStackTrace();
			}
		}

		finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				if (rs2 != null)
					rs2.close();
			} catch (SQLException e) {
			}
			try {
				stmt.close();
			} catch (SQLException e) {
			}
			try {
				if (stmt2 != null)
					stmt2.close();
			} catch (SQLException e) {
			}
			try {
				if (stmt3 != null)
					stmt3.close();
			} catch (SQLException e) {
			}
			try {
				if (stmt4 != null)
					stmt4.close();
			} catch (SQLException e) {
			}

		}

	}

}