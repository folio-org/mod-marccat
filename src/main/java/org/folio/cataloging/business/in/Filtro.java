package org.folio.cataloging.business.in;

import org.folio.cataloging.business.codetable.Avp;

import java.util.ArrayList;

public class Filtro {
	private int idFiltro;
	private String descFiltro;
	private String tipoFiltro;
	private String valFiltro;
	private boolean valFiltroBoolean;
	private ArrayList valFiltroLista;	
	
	private final static String MULTILIST = "MULTILIST";
	private final static String BOOLEAN = "BOOLEAN";
	
	private String label; 
	private int value;	
	
	public int getIdFiltro() {
		return idFiltro;
	}
	public void setIdFiltro(int idFiltro) {
		this.idFiltro = idFiltro;
	}
	public String getDescFiltro() {
		return descFiltro;
	}
	public void setDescFiltro(String descFiltro) {
		this.descFiltro = descFiltro;
	}
	public String getTipoFiltro() {
		return tipoFiltro;
	}
	public void setTipoFiltro(String tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}
	public String getValFiltro() {
		
		return valFiltro;
	}
	public void setValFiltro(String valFiltro) {
		if (this.tipoFiltro.equalsIgnoreCase(MULTILIST)){
			if(valFiltro!=null && (!valFiltro.equals(""))){
			String[] v = valFiltro.split("\\/");	
			ArrayList lista = new ArrayList();
			for (int i = 0; i < v.length; i++) {
				Avp ve = new Avp(v[i],v[i]);
				lista.add(ve);
			}
			this.valFiltroLista = lista;
			}else{
			this.valFiltroLista = new ArrayList();	
			}
		}else if(this.tipoFiltro.equalsIgnoreCase(BOOLEAN)){
			if(valFiltro!=null && (!valFiltro.equals(""))){
				if(valFiltro.equals("0")){
				this.valFiltroBoolean=false;
				}else{
					this.valFiltroBoolean=true;	
				}
			}
		}
		if(!this.tipoFiltro.equalsIgnoreCase(BOOLEAN)){
		this.valFiltro = valFiltro;
		}
	}
	
	public String getLabel() {
		return label;
	}	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	public ArrayList getValFiltroLista() {
		return valFiltroLista;
	}
	public void setValFiltroLista(ArrayList valFiltroLista) {
		this.valFiltroLista = valFiltroLista;
	}
	public boolean isValFiltroBoolean() {
		return valFiltroBoolean;
	}
	public void setValFiltroBoolean(boolean valFiltroBoolean) {
		this.valFiltroBoolean = valFiltroBoolean;
	}
//	public String getValFiltroBoolean() {
//		return valFiltroBoolean;
//	}
//	public void setValFiltroBoolean(String valFiltroBoolean) {
//		this.valFiltroBoolean = valFiltroBoolean;
//	}
	
}
