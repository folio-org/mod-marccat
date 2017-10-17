/*
 * (c) LibriCore
 * 
 * Created on Dec 8, 2004
 * 
 * DAOMarcLoading.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.hibernate.LDG_STATS;
import librisuite.hibernate.LOADING_MARC_FILE;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.FormFile;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.marc.Record;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/08/18 11:38:25 $
 * @since 1.0
 */
public class DAOMarcLoading extends HibernateUtil {
	private static final Log logger = LogFactory.getLog(DAOMarcLoading.class);

	public LOADING_MARC_FILE loadFile(
		FormFile file,
		int cataloguingView,
		int startingRecord,
		int numberOfRecords,
		int characterSetCode,
		int startingAmicusNumber)
		throws DataAccessException, MarcLoadingException, SQLException {
//		logger.info("loading file " + file.getFileName());
		
		LOADING_MARC_FILE row = copyFileToClob(file);
		
		if (logger.isDebugEnabled()) {
			logger.debug("file saved in row " + row.getFileNumber());
		}
		startLoadingRun(
			cataloguingView,
			startingRecord,
			numberOfRecords,
			characterSetCode,
			startingAmicusNumber,
			row);
//		logger.info("load successful");
		return row;
	}

	public LOADING_MARC_FILE loadFile(byte[] fileBytes, int cataloguingView,
			int startingRecord, int numberOfRecords, int characterSetCode,
			int startingAmicusNumber) throws DataAccessException,
			MarcLoadingException, SQLException {
		LOADING_MARC_FILE row = copyFileToClob(fileBytes);
		if (logger.isDebugEnabled()) {
			logger.debug("file saved in row " + row.getFileNumber());
		}
		startLoadingRun(cataloguingView, startingRecord, numberOfRecords,
				characterSetCode, startingAmicusNumber, row);
		logger.info("load successful");
		return row;
	}

	private LOADING_MARC_FILE copyFileToClob(final FormFile file)
		throws DataAccessException {
		final LOADING_MARC_FILE row = new LOADING_MARC_FILE();
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
				throws HibernateException, SQLException, IOException {
				row.setBlob(Hibernate.createBlob(" ".getBytes()));
				s.save(row);
				s.flush();
				s.refresh(row, LockMode.UPGRADE); //grabs an Oracle CLOB
				
				OutputStream os = row.getBlob().setBinaryStream(0);
				os.write(file.getFileData());
				os.close();
			}
		}
		.execute();

		return row;
	}
	
	private LOADING_MARC_FILE copyFileToClob(final byte[] fileBytes)
			throws DataAccessException 
	{
		final LOADING_MARC_FILE row = new LOADING_MARC_FILE();
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException, SQLException, IOException {
				row.setBlob(Hibernate.createBlob(" ".getBytes()));
				s.save(row);
				s.flush();
				s.refresh(row, LockMode.UPGRADE); 
				java.sql.Blob blob = (java.sql.Blob) row.getBlob();				
				OutputStream os =  blob.setBinaryStream(1L);
				os.write(fileBytes);
				os.close();
			}
		}.execute();

		return row;
	}
	public LOADING_MARC_FILE loadFile(
			FormFile file,
			int cataloguingView,
			int startingRecord,
			int numberOfRecords,
			int characterSetCode,
			int startingAmicusNumber,
			Record record)
			throws DataAccessException, MarcLoadingException, SQLException {
			logger.info("loading file " + file.getFileName());
			
			LOADING_MARC_FILE row = copyFileToClob(file,record);
			
			if (logger.isDebugEnabled()) {
				logger.debug("file saved in row " + row.getFileNumber());
			}
			startLoadingRun(
				cataloguingView,
				startingRecord,
				numberOfRecords,
				characterSetCode,
				startingAmicusNumber,
				row);
			logger.info("load successful");
			return row;
		}
	
	private LOADING_MARC_FILE copyFileToClob(final FormFile file,final Record record) throws DataAccessException {
	final LOADING_MARC_FILE row = new LOADING_MARC_FILE();
	new TransactionalHibernateOperation() {
		public void doInHibernateTransaction(Session s)
			throws HibernateException, SQLException, IOException {
			row.setBlob(Hibernate.createBlob(" ".getBytes()));
			s.save(row);
			s.flush();
			s.refresh(row, LockMode.UPGRADE); //grabs an Oracle CLOB
			
			OutputStream os = row.getBlob().setBinaryStream(0);
			MarcWriter writer = new MarcStreamWriter(os);
			writer.write(record);
			writer.close();
			os.close();
		}
	}
	.execute();

	return row;
}
	
	public void startLoadingRun(
		int cataloguingView,
		int startingRecord,
		int numberOfRecords,
		int characterSetCode,
		int startingAmicusNumber,
		LOADING_MARC_FILE row)
		throws MarcLoadingException, SQLException, DataAccessException {
		CallableStatement proc = null;
		try {
			Session s = currentSession();
			Connection con;
			con = s.connection();
			proc = con.prepareCall(
					"{ ? = call AMICUS.US_BBL_LOADING(?, ?, ?, ?, ?, ?) }");
			proc.registerOutParameter(1, Types.INTEGER);
			proc.setInt(2, cataloguingView);
			proc.setInt(3, startingRecord);
			proc.setInt(4, numberOfRecords);
			proc.setInt(5, characterSetCode);
			proc.setInt(6, startingAmicusNumber);
			proc.setInt(7, row.getFileNumber().intValue());
			proc.execute();

			int rc = proc.getInt(1);

			if (rc != 0) {
				throw new MarcLoadingException(String.valueOf(rc));
			}
			s.refresh(row, LockMode.NONE);
		} catch (HibernateException e) {
			logAndWrap(e);
		} finally {
			try {
				if(proc!=null) {
					proc.close();
				}
			} catch (SQLException ex) {
				// do nothing
				ex.printStackTrace();
			}
		} 

	}

	public LDG_STATS getStats(int loadingStatisticsNumber)
		throws DataAccessException {
		return (LDG_STATS) load(
			LDG_STATS.class,
			new Integer(loadingStatisticsNumber));
	}

	public List getResults(int loadingStatisticsNumber)
		throws DataAccessException {
		List l =
			find(
				"from LOADING_MARC_RECORDS as r "
					+ " where r.loadingStatisticsNumber = ? "
					+ " order by r.sequence ",
				new Object[] { new Integer(loadingStatisticsNumber)},
				new Type[] { Hibernate.INTEGER });
		return l;
	}

	
}
