package org.folio.cataloging.dao.persistence;


import java.io.Serializable;
import java.util.Date;

public class CasDataXfer implements Serializable {


    /** identifier field */
    private long bibItmNbr;
       
    /** nullable persistent field */
    private String transactionId;
    
    /** nullable persistent field */
    private String operator;
    
    /** nullable persistent field */
    private Date xferDate;
    
    private boolean NTI;

    /** default constructor */
    public CasDataXfer() {
    }

    /** minimal constructor */
    public CasDataXfer(long bibItmNbr) {
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

	public Date getXferDate() {
		return xferDate;
	}

	public void setXferDate(Date xferDate) {
		this.xferDate = xferDate;
	}

	public boolean isNTI() {
		return NTI;
	}

	public void setNTI(boolean nti) {
		NTI = nti;
	}
}
