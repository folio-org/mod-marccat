package org.folio.cataloging.business.in;

import java.util.ArrayList;
import java.util.List;
//import librisuite.hibernate;


public class Fonte {
	
	private String label; 
	private String value;	
	private List filtri = new ArrayList();
		
	public List getFiltri() {
		return filtri;
	}
	
	public void setFiltri(List filtri) {
		this.filtri = filtri;
	}
	
	//Nicola
	/*public void addNewFiltro(String nome, String ragione){
		this.filtri.add(new ValueLabelElement(ragione, nome));
	}*/
	
	//CAMELIA, AGGIUNGO UN NOUVO FILTRO ALLA FONTE
	public void addNewFiltro(Filtro filtro){
		this.filtri.add(filtro);
	}
	//FINE CAMELIA
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
}
