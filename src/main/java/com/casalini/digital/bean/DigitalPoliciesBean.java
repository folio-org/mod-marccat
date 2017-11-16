package com.casalini.digital.bean;

import com.casalini.hibernate.model.CasDgaPolicy;

import librisuite.bean.LibrisuiteBean;

public class DigitalPoliciesBean extends LibrisuiteBean implements Comparable 
{	 
	private String policyCode;
	private String policyName;
	private String policyType;
	private Float policyPrice;
	private String policyCurcy;
	private int policyStamps;
	private boolean dgaPolicy = false;
	private Float policyTotPrice;
	
	public DigitalPoliciesBean(){	
	}
	
	public DigitalPoliciesBean(CasDgaPolicy policy){
		this.policyCode=policy.getIdPolicy();
		this.policyCurcy=policy.getCurcyPolicy();
		this.policyName=policy.getDenPolicy();
		this.policyPrice=policy.getPricePolicy();
		this.policyStamps=policy.getNumStampaPolicy();
		this.policyType=policy.getTypePolicy();
		this.dgaPolicy=true;
		this.policyTotPrice=policy.getPriceTotal();
	}
	
	public Float getPolicyTotPrice() {
		return policyTotPrice;
	}

	public void setPolicyTotPrice(Float policyTotPrice) {
		this.policyTotPrice = policyTotPrice;
	}

	public boolean isDgaPolicy() {
		return dgaPolicy;
	}

	public void setDgaPolicy(boolean dgaPolicy) {
		this.dgaPolicy = dgaPolicy;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getPolicyCurcy() {
		return policyCurcy;
	}

	public void setPolicyCurcy(String policyCurcy) {
		this.policyCurcy = policyCurcy;
	}

	public int getPolicyStamps() {
		return policyStamps;
	}

	public void setPolicyStamps(int policyStamps) {
		this.policyStamps = policyStamps;
	}

	public String getPolicyCode() {
		return policyCode;
	}

	public void setPolicyCode(String policyCode) {
		this.policyCode = policyCode;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public Float getPolicyPrice() {
		return policyPrice;
	}

	public void setPolicyPrice(Float policyPrice) {
		this.policyPrice = policyPrice;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DigitalPoliciesBean other = (DigitalPoliciesBean) obj;
		
		return (policyCode.equals(other.policyCode)&& policyType.equals(other.policyType));
	
	}

	public int hashCode() {
		return 0;
	}

	public int compareTo(Object o) 
	{
		DigitalPoliciesBean poli = (DigitalPoliciesBean)o;		
		int result = this.policyCode.compareTo(poli.getPolicyCode());
//		Se i codici non sono uguali
		if(result != 0)
			return result;
//		Altrimenti controllo il tipo
		return this.policyType.compareTo(poli.getPolicyType());
	}
}
