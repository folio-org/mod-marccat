package org.folio.cataloging.bean.searching.frbr;

public class GenericEntity {
	private Integer id;
	private String label;
	private boolean heading;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isHeading() {
		return heading;
	}
	public void setHeading(boolean heading) {
		this.heading = heading;
	}
}
