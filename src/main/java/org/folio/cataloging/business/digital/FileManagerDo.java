package org.folio.cataloging.business.digital;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.common.UploadFileDigitalException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.DAODigital;

public class FileManagerDo
{
	private List listChilds = null;
	private String DIGITAL_CONTEXT = "";
	private String DIGITAL_VIRTUAL_PATH = "";
//---------------> pathParent serve per tornare indietro al livello superiore
	private String pathParent = null;
//---------------> pathCorr serve per visualizzare il path corrente
	private String pathCorr = null;
//---------------> pathCorrDisplay serve per visualizzare il path corrente nascondendo la root
	private String pathCorrDisplay = null;
	private String pathCopyFile = null;
	private long sizeFileCopy = 0;
	private String mainDirectoryByOperation;
	private static final String SLASH = "/";
	
	private static final String DIG_PROC_LOADED = Defaults.getString("digital.proc.isLoaded");
	private static final String DIG_PROC_LOAD = Defaults.getString("digital.proc.load");
	private static final String DIG_PROC_DELETE = Defaults.getString("digital.proc.delete");
	private static final String DIG_VIRTUAL_PATH = Defaults.getString("digital.virtual.path");
	private static final String DIG_HOME_REPOSITORY = Defaults.getString("digital.home.rep");
	private static final Log logger = LogFactory.getLog(FileManagerDo.class);
	
		
	public FileManagerDo() throws DataAccessException{
		getHomeRepositoryProc();
		getDigVirtualPathProc();
	}
	
	public boolean isDirectoryExists(String path)
	{
		File f = new File(path);
		return (f.exists() && f.isDirectory());
	}
	
	public void directoryCreate(String path) throws DirectoryCreationException
	{
		File f = new File(path);
		if (!f.mkdirs()){
			logger.error("Errore nella creazione della directory : " + path); 
			throw new DirectoryCreationException();
		}else{
			/* 20130924: Sotto weblogic il file creato non aveva i permessi di lettura */
			f.setReadable(true, false);
		}
	}

	
	public boolean repositoryExsist(String path) 
	{
		boolean exist = true;
		File fileCreated = new File(path);
		File filesList [] = fileCreated.listFiles();
		if (filesList == null || filesList.length == 0){
			exist=false;
		}
		return exist;
	}

	public String takeNameFromPath(String path)throws Exception
	{
//		System.out.println("Path ----------------> " + path);
//		System.out.println("File separator usato : " + File.separator);
		
		String[] arr = path.split(File.separator);
//		for (int i = 0; i < arr.length; i++) {
//			System.out.println(i + " => " +arr[i]);
//		}
		String nameFile = null;
		if (arr!=null) {
			if (arr.length>0) {
				nameFile = arr[arr.length-1];
//				System.out.println("nome file " + arr[arr.length-1]);
			} else
				throw new Exception();
		}else 
			throw new Exception();

		return nameFile;
	}
	
	public void copyFile(String pathOrigine, String pathDestinazione, String fileName) throws IOException, Exception
	{		
		FileInputStream fis = new FileInputStream(pathOrigine);
//		String fileNew = pathDestinazione + File.separator + fileName;
		String fileNew = (new StringBuffer().append(pathDestinazione).append(File.separator).append(fileName).toString());
//		System.out.println("File new in copyFile : " + fileNew);
		FileOutputStream fos = new FileOutputStream(fileNew);
		
		byte [] dati = new byte[fis.available()];
		setSizeFileCopy(dati.length);
//		int appo = dati.length;
//		System.out.println("appo size : " + appo); 
		
		fis.read(dati);
		fos.write(dati);
	
		fis.close();
		fos.close();
	}

	public String getPathParent() {
		return pathParent;
	}

	public void setPathParent(String pathParent) {
		this.pathParent = pathParent;
	}

	public String getpathCorr() {
		return pathCorr;
	}

	public void setPathCorr(String pathCorr) 
	{
		this.pathCorr = pathCorr;

		String appo = getpathCorr();
		
		if (getpathCorr()!=null ) {
			appo = appo.replaceAll(getDIGITAL_CONTEXT(), "/HomeRepository/");
			setPathCorrDisplay(appo);
		} else
			setPathCorrDisplay(null);
	}

	public String getDIGITAL_CONTEXT() {
		return DIGITAL_CONTEXT;
	}
	private void setDIGITAL_CONTEXT(String digital_context) {
		DIGITAL_CONTEXT = digital_context + SLASH;
	}
	
	public void getHomeRepositoryProc() throws DataAccessException
	{
		new TransactionalHibernateOperation() 
		{
			public void doInHibernateTransaction(Session s) throws SQLException, HibernateException, UploadFileDigitalException 
				{
				CallableStatement proc = null;
				try {	
					
					Connection connection = s.connection();
					proc = connection.prepareCall("{ ? = call " + DIG_HOME_REPOSITORY + "() }");
					proc.registerOutParameter(1, Types.CHAR);
					proc.execute();
					String home = proc.getString(1);
					if (home == null) {
						throw new UploadFileDigitalException("error.digital.loadedFile");
					}
					setDIGITAL_CONTEXT(home);
					logger.debug("HOME digital repository --> " + home);

				} finally {
					try {
						if(proc!=null) proc.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		.execute();
	}
	
	public void getDigVirtualPathProc() throws DataAccessException
	{
		new TransactionalHibernateOperation() 
		{
			public void doInHibernateTransaction(Session s) throws SQLException, HibernateException, UploadFileDigitalException 
				{
				CallableStatement proc = null;
				try {	
					
					Connection connection = s.connection();
					proc = connection.prepareCall("{ ? = call " + DIG_VIRTUAL_PATH + "() }");
					proc.registerOutParameter(1, Types.CHAR);
					proc.execute();
					String virtualPath = proc.getString(1);
					if (virtualPath == null) {
						throw new UploadFileDigitalException("error.digital.loadedFile");
					}
					setDIGITAL_VIRTUAL_PATH(virtualPath);

				} finally {
					try {
						if(proc!=null) proc.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		.execute();
	}
	
	public void isFileLoadedDigProc(final String directoryFile, final String fileName) throws DataAccessException, UploadFileDigitalException
	{
		new TransactionalHibernateOperation() 
		{
			public void doInHibernateTransaction(Session s) throws SQLException, HibernateException, UploadFileDigitalException 
				{
				CallableStatement proc = null;
				try {	
					
					String pathRelativo = findPathRelative(directoryFile);
					
					logger.debug("PARAMETRI CHIAMATA PROC " + DIG_PROC_LOADED);
					logger.debug("directoryFile    : " + directoryFile);
					logger.debug("path relativo    : " + pathRelativo);
					logger.debug("fileName         : " + fileName);
					
					Connection connection = s.connection();
					proc = connection.prepareCall("{ ? = call " + DIG_PROC_LOADED + "(?,?) }");
					proc.registerOutParameter(1, Types.INTEGER);
					proc.setString(2, pathRelativo);
					proc.setString(3, fileName);
					proc.execute();
					int rc = proc.getInt(1);
					if (rc > 0) {
						throw new UploadFileDigitalException("error.digital.loadedFile");
					}

				} finally {
					try {
						if(proc!=null) proc.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		.execute();
	}
	
	public void loadDigProc(final String directoryFile, final String fileName, final long size, final int amicusNumber, final int userView, final String operation) throws DataAccessException
	{
		new TransactionalHibernateOperation() 
		{
			public void doInHibernateTransaction(Session s) throws HibernateException, SQLException 
				{
				CallableStatement proc = null;
				try {				
					Date dataSys = null;					
					logger.info("PROCEDURA RICHIAMATA : " + DIG_PROC_LOAD);
					logger.debug(" directoryFile    : " + directoryFile);
					logger.debug(" fileName         : " + fileName);
					logger.debug(" amicusNumber     : " + amicusNumber);
					logger.debug(" data sql systema : " + dataSys);
					logger.debug(" user view        : " + userView);
					logger.debug(" Nuovo parametro: SIZE FILE : " + size);
					
					Connection connection = s.connection();
					proc = connection.prepareCall("{call " + DIG_PROC_LOAD + " (?,?,?,?,?,?,?) }");
					proc.setString(1, directoryFile);
					proc.setString(2, fileName);
					proc.setInt(3, amicusNumber);
					proc.setInt(4, userView);
					proc.setDate(5, dataSys);
					proc.setLong(6, size);
					proc.setString(7, operation);
					proc.execute();

				} finally {
					try {
						if(proc!=null) proc.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		.execute();
	}


	public String findPathRelative(String fullPath)
	{
		String pathRelativo = "";
		String workPath = "";
		
//		System.out.println("path iniziale : " + fullPath);
		
		workPath = cntrPath(fullPath);
		
		int diff = workPath.length() - getDIGITAL_CONTEXT().length();
		if (diff != 0) {
			pathRelativo = workPath.substring(workPath.length()-diff);
		}
		
//		System.out.println("path relativo : " + pathRelativo);
		
		return pathRelativo;
	}
	
	private String cntrPath(String fullPath)
	{
		String workPath = fullPath;
		
		if (fullPath.length() < getDIGITAL_CONTEXT().length()) {
//--->  Se stiamo sulla root, l'ultimo carattere del path deve essere "/" altrimenti ha problemi nel riconoscimento dello stesso
			if (!fullPath.substring(fullPath.length()-1).equalsIgnoreCase(SLASH) ) 
				workPath = fullPath + SLASH; 
		
		} else if (fullPath.length() > getDIGITAL_CONTEXT().length()) {
//--->  Se non stiamo sulla root, l'ultimo carattere del path NON deve essere "/" altrimenti inserisce un path errato in base dati
			if (fullPath.substring(fullPath.length()-1).equalsIgnoreCase(SLASH))
				workPath = fullPath.substring(0, fullPath.length()-1);
		}
		return workPath;
	}
	
	public void deleteDigProc(final String directoryFile, final String fileName, final String operation) throws DataAccessException
	{
		new TransactionalHibernateOperation() 
		{
			public void doInHibernateTransaction(Session s) throws HibernateException, SQLException 
				{
				CallableStatement proc = null;
				try {				
					logger.debug("PROCEDURA RICHIAMATA : " + DIG_PROC_DELETE);
					logger.debug(" directoryFile    : " + directoryFile);
					logger.debug(" fileName         : " + fileName);
					
					Connection connection = s.connection();
					proc = connection.prepareCall("{call " + DIG_PROC_DELETE + " (?,?,?) }");
					proc.setString(1, directoryFile);
					proc.setString(2, fileName);
					proc.setString(3, operation);
					proc.execute();

				} finally {
					try {
						if(proc!=null) proc.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		.execute();
	}
	
	public String getPathCopyFile() {
		return pathCopyFile;
	}

	public void setPathCopyFile(String pathCopyFile) {
		this.pathCopyFile = pathCopyFile;
	}

	public String getPathCorrDisplay() {
		return pathCorrDisplay;
	}

	public void setPathCorrDisplay(String pathCorrDisplay) {
		this.pathCorrDisplay = pathCorrDisplay;
	}

	public List getListChilds() {
		return listChilds;
	}
	
	public long getSizeFileCopy() {   		
        return sizeFileCopy;
	}

	public String getDIGITAL_VIRTUAL_PATH() {
		return DIGITAL_VIRTUAL_PATH;
	}

	private void setDIGITAL_VIRTUAL_PATH(String digital_virtual_path) {
		DIGITAL_VIRTUAL_PATH = digital_virtual_path;
	}

	public void setSizeFileCopy(long sizeFileCopy) 
	{
		long kbSize = 0;
		long byteSize = sizeFileCopy;
		
        if (byteSize!=0) {   	
            // Divide by 1024 to get size in KB
            kbSize = byteSize / 1024;
            
            if (byteSize % 1024!=0)
            	kbSize=kbSize+1;
        }
		this.sizeFileCopy = kbSize;
	}
	
	public int countRLTSP(String amicusNumber) throws DataAccessException, NumberFormatException
	{
//		System.out.println("Nome file da cancellare : " + amicusNumber);
		
//----> Dal nome file tolgo l'estenzione (individiata dal .) tolgo gli eventuali _tipo 
//----> per ottenere l'amicus Number puro per la count nel db		
		String fileName = amicusNumber.substring(0,amicusNumber.indexOf("."));
//		System.out.println("Nome file per il dao : " + fileName);
		int i = fileName.indexOf("_");
		if (i!=-1){
			fileName=fileName.substring(0,i);
		}
		DAODigital daoDigital = new DAODigital();
		return daoDigital.countRLTSP(Integer.parseInt(fileName));
	}

	public String getMainDirectoryByOperation() {
		return mainDirectoryByOperation;
	}

	public void setMainDirectoryByOperation(String mainDirectoryByOperation) {
		this.mainDirectoryByOperation = mainDirectoryByOperation;
	}
}