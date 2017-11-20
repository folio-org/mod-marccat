package org.folio.cataloging.bean.searching.frbr;

public class HeadingRel {
	Integer amicusNumberRel;
	Integer headingNumber;
	Integer type;
	String code;
	String label;
	String title;
	boolean authority;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getAmicusNumberRel() {
		return amicusNumberRel;
	}
	public void setAmicusNumberRel(Integer amicusNumberRel) {
		this.amicusNumberRel = amicusNumberRel;
	}
	public Integer getHeadingNumber() {
		return headingNumber;
	}
	public void setHeadingNumber(Integer headingNumber) {
		this.headingNumber = headingNumber;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean isAuthority() {
		return authority;
	}
	public void setAuthority(boolean authority) {
		this.authority = authority;
	}
}
