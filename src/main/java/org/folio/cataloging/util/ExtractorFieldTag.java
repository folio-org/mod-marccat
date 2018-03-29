package org.folio.cataloging.util;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExtractorFieldTag implements TagConstant{

	
	/**
	 * Get the complete string text from the first tag found and return the stringValue
	 * @param content
	 * @param sequenceTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getFiedValue(String content,String[] sequenceTag) 
	{
		StringBuilder builder = new StringBuilder();
		
		MarcReader reader = new MarcStreamReader(new ByteArrayInputStream(content.getBytes()),"UTF-8");
		org.marc4j.marc.Record record = reader.next();
		List<VariableField> dfList = record.getVariableFields(sequenceTag);
		for(VariableField field: dfList){
			DataField df = (DataField) field;
			List<Subfield> subfields = df.getSubfields();
			for(Subfield subfield: subfields){
				if(subfield.getData() != null)
					builder.append(subfield.getData()).append(" ");
			}
			if (builder.length()>0){
				builder.deleteCharAt(builder.length() - 1);
				break;
			}
		}
		return builder.toString();
	}
	
	/**
	 * Get the complete string text from the first tag found and return the stringValue
	 * @param content
	 * @param sequenceTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getFieldsValues(String content,String[] sequenceTag) 
	{
		List<String> listValues = new ArrayList<String>();
		MarcReader reader = new MarcStreamReader(new ByteArrayInputStream(content.getBytes()));
		org.marc4j.marc.Record record = reader.next();
		List<VariableField> dfList = record.getVariableFields(sequenceTag);
		for(VariableField field: dfList){
			DataField df = (DataField) field;
			String result = "";
			List<Subfield> subfields = df.getSubfields();
			for(Subfield subfield: subfields){
				if(subfield.getData() != null){
					result = result + subfield.getData() + " ";
				}
			}
			if (result.length()>0){
				result = result.substring(0, result.length() -1);
			}
			listValues.add(result);
		}
		return listValues;
	}
	
	/**
	 * Get the stringValue for a specific tag's subfield
	 * @param content
	 * @param tag
	 * @param subfield
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getFiedValue(String content,String tag,char subfield) {
		String result = null;
		
		MarcReader reader = new MarcStreamReader(new ByteArrayInputStream(content.getBytes()));
		org.marc4j.marc.Record record = reader.next();
		List<VariableField> dfList = record.getVariableFields(tag);
		for(VariableField f1: dfList){
			DataField df = (DataField) f1;
			List<Subfield> subfields = df.getSubfields();
			for(Subfield field: subfields){
				if(field.getCode() == subfield)
					return field.getData();
			}
		}
		return result;
	}
	
	/**
	 * Get the complete string text from the first tag found and return the stringValue
	 * @param content
	 * @param sequenceTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getFiedValue(String content,String[] sequenceTag,char subf) 
	{
		StringBuilder builder = new StringBuilder();
		
		MarcReader reader = new MarcStreamReader(new ByteArrayInputStream(content.getBytes()));
		org.marc4j.marc.Record record = reader.next();
		List<VariableField> dfList = record.getVariableFields(sequenceTag);
		for(VariableField field: dfList){
			DataField df = (DataField) field;
				Subfield subfield = df.getSubfield(subf);
				if(subfield != null)
					return subfield.getData();
		
		}
		return builder.toString();
	}
	
	/**
	 * Get the Leader object
	 * @param content
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Leader getLeader(String content) {
		MarcReader reader = new MarcStreamReader(new ByteArrayInputStream(content.getBytes()));
		org.marc4j.marc.Record record = reader.next();
		return record.getLeader();
	}
	
}
