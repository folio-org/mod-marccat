package org.folio.cataloging.dao.persistence;


import java.io.Serializable;

public class CasQltyRec implements Serializable 
{
	private static final long serialVersionUID = 2299675142382967471L;
       
    private int transactionId;
    
    private int bibItmNbr;
    
    /** default constructor */
    public CasQltyRec() {
    }

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getBibItmNbr() {
		return bibItmNbr;
	}

	public void setBibItmNbr(int bibItmNbr) {
		this.bibItmNbr = bibItmNbr;
	}
}
