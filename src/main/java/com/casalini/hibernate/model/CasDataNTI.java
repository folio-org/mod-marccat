package com.casalini.hibernate.model;


import java.io.Serializable;
import java.util.Date;

public class CasDataNTI implements Serializable {


    /** identifier field */
    private long bibItmNbr;
       
    /** nullable persistent field */
    private String transactionId;
    
    /** nullable persistent field */
    private String operator;
    
    /** nullable persistent field */
    private Date ntiDate;

    /** default constructor */
    public CasDataNTI() {
    }

    /** minimal constructor */
    public CasDataNTI(long bibItmNbr) {
        this.bibItmNbr = bibItmNbr;
    }

	public long getBibItmNbr() {
		return bibItmNbr;
	}

	public void setBibItmNbr(long bibItmNbr) {
		this.bibItmNbr = bibItmNbr;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getNtiDate() {
		return ntiDate;
	}

	public void setNtiDate(Date ntiDate) {
		this.ntiDate = ntiDate;
	}
}

