package com.casalini.digital.bean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileDigitalBean implements Comparable 
{
	private String name = null;
	private String ultMod = null;
	private String size = null;
	private String pathComponent = null;
	private boolean isdirectory = false;
	private String pathParent = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private File fileDigital = null;
	
	public FileDigitalBean(File file) 
	{		
		setFileDigital(file);
		setName(file.getName());
		setUltMod(sdf.format(new Date(file.lastModified())));
		setPathComponent(file.getPath());
		setIsdirectory(file.isDirectory()); 
		setSize(file.length());
	}
	
	public String getpathComponent() {
		return pathComponent;
	}

	public void setPathComponent(String pathComponent) {
		this.pathComponent = pathComponent;
	}

	public boolean isdirectory() {
		return isdirectory;
	}

	public void setIsdirectory(boolean isdirectory) {
		this.isdirectory = isdirectory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUltMod() {
		return ultMod;
	}

	public void setUltMod(String ultMod) {
		this.ultMod = ultMod;
	}

	public String getSize(){    
		return size; 
	}

	public void setSize(long sizeFile) 
	{
		if (isdirectory()){
			this.size = "";
		}
		
		long kbSize = 0;
		long byteSize = sizeFile;
        if (byteSize!=0) {   	
            // Divide by 1024 to get size in KB
            kbSize = byteSize / 1024;
            
            if (byteSize % 1024!=0)
            	kbSize=kbSize+1;
        }
        this.size = "" + kbSize;
	}

	public String getPathParent() {
		return pathParent;
	}

	public void setPathParent(String pathParent) {
		this.pathParent = pathParent;
	}

	public File getFileDigital() {
		return fileDigital;
	}

	public void setFileDigital(File fileDigital) {
		this.fileDigital = fileDigital;
	}

	public int compareTo(Object obj) 
	{
		FileDigitalBean dig = (FileDigitalBean)obj;
		return this.name.compareTo(dig.getName());
	}
}