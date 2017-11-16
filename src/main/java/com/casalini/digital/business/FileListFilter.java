package com.casalini.digital.business;

import java.io.File;
import java.io.FilenameFilter;

public class FileListFilter implements FilenameFilter
{
	protected String pattern;
	
	public FileListFilter(String str){
		pattern = str;
	}
		
	public boolean accept (File dir, String name)
	{
//---->	Deve accettare tutte le directory e tutti i files che iniziano per la stringa passata
		return ((new File(dir, name).isDirectory()) || (name.toLowerCase().startsWith(pattern.toLowerCase())));
	}
}