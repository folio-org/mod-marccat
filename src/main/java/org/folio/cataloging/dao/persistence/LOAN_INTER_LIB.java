/*
 * (c) LibriCore
 * 
 * Created on 13-jul-2005
 * 
 * LOAN_INTER_LIB.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.Date;

 /**
 * @author Ruben
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class LOAN_INTER_LIB implements Serializable{
	private int loanInterLibNumber;
	private int borrowerNumber;
	private int copyIdNumber;
	private int CopyUnlabeledNumber; 
	private int bibItemNumber;
	private char loanPrd;
	private short requestTyp;
	private String borrowerNote;
	private int orgNumberOrgn;
	private int orgNumberRequest;
	private int branchOrgNumberOrgn;
	private int branchOrgNumberRequest;
	private char statusGoingToSend;
	private char statusGoingToRecive;
	private Date dateLastTransaction;
	private short loanStatisticsTypeCode;
	
	
	
	

	/**
	 * @return Returns the bibItemNumber.
	 * @exception
	 * @since 1.0
	 */
	public int getBibItemNumber() {
		return bibItemNumber;
	}
	/**
	 * @param bibItemNumber The bibItemNumber to set.
	 * @exception
	 * @since 1.0
	 */
	public void setBibItemNumber(int bibItemNumber) {
		this.bibItemNumber = bibItemNumber;
	}
	/**
	 * @return Returns the borrowerNote.
	 * @exception
	 * @since 1.0
	 */
	public String getBorrowerNote() {
		return borrowerNote;
	}
	/**
	 * @param borrowerNote The borrowerNote to set.
	 * @exception
	 * @since 1.0
	 */
	public void setBorrowerNote(String borrowerNote) {
		this.borrowerNote = borrowerNote;
	}
	/**
	 * @return Returns the borrowerNumber.
	 * @exception
	 * @since 1.0
	 */
	public int getBorrowerNumber() {
		return borrowerNumber;
	}
	/**
	 * @param borrowerNumber The borrowerNumber to set.
	 * @exception
	 * @since 1.0
	 */
	public void setBorrowerNumber(int borrowerNumber) {
		this.borrowerNumber = borrowerNumber;
	}
	/**
	 * @return Returns the branchOrgNumberOrgn.
	 * @exception
	 * @since 1.0
	 */
	public int getBranchOrgNumberOrgn() {
		return branchOrgNumberOrgn;
	}
	/**
	 * @param branchOrgNumberOrgn The branchOrgNumberOrgn to set.
	 * @exception
	 * @since 1.0
	 */
	public void setBranchOrgNumberOrgn(int branchOrgNumberOrgn) {
		this.branchOrgNumberOrgn = branchOrgNumberOrgn;
	}
	/**
	 * @return Returns the branchOrgNumberRequest.
	 * @exception
	 * @since 1.0
	 */
	public int getBranchOrgNumberRequest() {
		return branchOrgNumberRequest;
	}
	/**
	 * @param branchOrgNumberRequest The branchOrgNumberRequest to set.
	 * @exception
	 * @since 1.0
	 */
	public void setBranchOrgNumberRequest(int branchOrgNumberRequest) {
		this.branchOrgNumberRequest = branchOrgNumberRequest;
	}
	/**
	 * @return Returns the copyIdNumber.
	 * @exception
	 * @since 1.0
	 */
	public int getCopyIdNumber() {
		return copyIdNumber;
	}
	/**
	 * @param copyIdNumber The copyIdNumber to set.
	 * @exception
	 * @since 1.0
	 */
	public void setCopyIdNumber(int copyIdNumber) {
		this.copyIdNumber = copyIdNumber;
	}
	/**
	 * @return Returns the copyUnlabeledNumber.
	 * @exception
	 * @since 1.0
	 */
	public int getCopyUnlabeledNumber() {
		return CopyUnlabeledNumber;
	}
	/**
	 * @param copyUnlabeledNumber The copyUnlabeledNumber to set.
	 * @exception
	 * @since 1.0
	 */
	public void setCopyUnlabeledNumber(int copyUnlabeledNumber) {
		CopyUnlabeledNumber = copyUnlabeledNumber;
	}
	/**
	 * @return Returns the dateLastTransaction.
	 * @exception
	 * @since 1.0
	 */
	public Date getDateLastTransaction() {
		return dateLastTransaction;
	}
	/**
	 * @param dateLastTransaction The dateLastTransaction to set.
	 * @exception
	 * @since 1.0
	 */
	public void setDateLastTransaction(Date dateLastTransaction) {
		this.dateLastTransaction = dateLastTransaction;
	}
	/**
	 * @return Returns the loanInterLibNumber.
	 * @exception
	 * @since 1.0
	 */
	public int getLoanInterLibNumber() {
		return loanInterLibNumber;
	}
	/**
	 * @param loanInterLibNumber The loanInterLibNumber to set.
	 * @exception
	 * @since 1.0
	 */
	public void setLoanInterLibNumber(int loanInterLibNumber) {
		this.loanInterLibNumber = loanInterLibNumber;
	}
	/**
	 * @return Returns the loanPrd.
	 * @exception
	 * @since 1.0
	 */
	public char getLoanPrd() {
		return loanPrd;
	}
	/**
	 * @param loanPrd The loanPrd to set.
	 * @exception
	 * @since 1.0
	 */
	public void setLoanPrd(char loanPrd) {
		this.loanPrd = loanPrd;
	}
	/**
	 * @return Returns the loanStatisticsTypeCode.
	 * @exception
	 * @since 1.0
	 */
	public short getLoanStatisticsTypeCode() {
		return loanStatisticsTypeCode;
	}
	/**
	 * @param loanStatisticsTypeCode The loanStatisticsTypeCode to set.
	 * @exception
	 * @since 1.0
	 */
	public void setLoanStatisticsTypeCode(
			short loanStatisticsTypeCode) {
		this.loanStatisticsTypeCode = loanStatisticsTypeCode;
	}
	
	/**
	 * @return Returns the orgNumberOrgn.
	 * @exception
	 * @since 1.0
	 */
	public int getOrgNumberOrgn() {
		return orgNumberOrgn;
	}
	/**
	 * @param orgNumberOrgn The orgNumberOrgn to set.
	 * @exception
	 * @since 1.0
	 */
	public void setOrgNumberOrgn(int orgNumberOrgn) {
		this.orgNumberOrgn = orgNumberOrgn;
	}
	/**
	 * @return Returns the orgNumberRequest.
	 * @exception
	 * @since 1.0
	 */
	public int getOrgNumberRequest() {
		return orgNumberRequest;
	}
	/**
	 * @param orgNumberRequest The orgNumberRequest to set.
	 * @exception
	 * @since 1.0
	 */
	public void setOrgNumberRequest(int orgNumberRequest) {
		this.orgNumberRequest = orgNumberRequest;
	}
	/**
	 * @return Returns the requestTyp.
	 * @exception
	 * @since 1.0
	 */
	public short getRequestTyp() {
		return requestTyp;
	}
	/**
	 * @param requestTyp The requestTyp to set.
	 * @exception
	 * @since 1.0
	 */
	public void setRequestTyp(short requestTyp) {
		this.requestTyp = requestTyp;
	}
	/**
	 * @return Returns the statusGoingToRecive.
	 * @exception
	 * @since 1.0
	 */
	public char getStatusGoingToRecive() {
		return statusGoingToRecive;
	}
	/**
	 * @param statusGoingToRecive The statusGoingToRecive to set.
	 * @exception
	 * @since 1.0
	 */
	public void setStatusGoingToRecive(char statusGoingToRecive) {
		this.statusGoingToRecive = statusGoingToRecive;
	}
	/**
	 * @return Returns the statusGoingToSend.
	 * @exception
	 * @since 1.0
	 */
	public char getStatusGoingToSend() {
		return statusGoingToSend;
	}
	/**
	 * @param statusGoingToSend The statusGoingToSend to set.
	 * @exception
	 * @since 1.0
	 */
	public void setStatusGoingToSend(char statusGoingToSend) {
		this.statusGoingToSend = statusGoingToSend;
	}
}
