package com.casalini.hibernate.model;

import java.io.Serializable;
import java.util.Date;

public class CasDataExp implements Serializable {


    /** identifier field */
    private Long bibItmNbr;
    
    /** nullable persistent field */
    private Long srcId;
    
    /** nullable persistent field */
    private Date impDte;
    
    /** nullable persistent field */
    private String invcNbr;

    /** default constructor */
    public CasDataExp() {
    }

    /** minimal constructor */
    public CasDataExp(Long bibItmNbr) {
        this.bibItmNbr = bibItmNbr;
    }

	public Long getBibItmNbr() {
		return bibItmNbr;
	}

	public void setBibItmNbr(Long bibItmNbr) {
		this.bibItmNbr = bibItmNbr;
	}

	public Date getImpDte() {
		return impDte;
	}

	public void setImpDte(Date impDte) {
		this.impDte = impDte;
	}

	public Long getSrcId() {
		return srcId;
	}

	public void setSrcId(Long srcId) {
		this.srcId = srcId;
	}

	public String getInvcNbr() {
		return invcNbr;
	}

	public void setInvcNbr(String invcNbr) {
		this.invcNbr = invcNbr;
	}

	

}
