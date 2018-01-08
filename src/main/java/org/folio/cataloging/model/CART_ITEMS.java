package org.folio.cataloging.model;

import org.folio.cataloging.business.codetable.Avp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CART_ITEMS implements Serializable
{
	private static final long serialVersionUID = 2522128570785338271L;

	private int bibItemNumber;
	private int branchId;
	private String branchDescription;
	private String title;
	private String author;
	private String publisher;
	private String isbnIssn;
	private String isJournals;
	private String descriptionTag300;
	private String leaderPos6;
	private String userName;
	private boolean selected;
	private List<Avp> branchesList;
	private String branches;
	private Date transactionDate;	

	public List<Avp> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<Avp> branchesList) {
		this.branchesList = branchesList;
	}

	public String getBranchDescription() {
		return branchDescription;
	}

	public void setBranchDescription(String branchDescription) {
		this.branchDescription = branchDescription;
	}

	public int getBibItemNumber() {
		return bibItemNumber;
	}

	public void setBibItemNumber(int bibItemNumber) {
		this.bibItemNumber = bibItemNumber;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIsbnIssn() {
		return isbnIssn;
	}

	public void setIsbnIssn(String isbnIssn) {
		this.isbnIssn = isbnIssn;
	}

	public String getIsJournals() {
		return isJournals;
	}

	public void setIsJournals(String isJournals) {
		this.isJournals = isJournals;
	}

	public String getDescriptionTag300() {
		return descriptionTag300;
	}

	public void setDescriptionTag300(String descriptionTag300) {
		this.descriptionTag300 = descriptionTag300;
	}

	public String getLeaderPos6() {
		return leaderPos6;
	}

	public void setLeaderPos6(String leaderPos6) {
		this.leaderPos6 = leaderPos6;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String getBranches() {
		return branches;
	}

	public void setBranches(String branches) {
		this.branches = branches;
	}
	
	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + bibItemNumber;
		result = prime
				* result
				+ ((branchDescription == null) ? 0 : branchDescription
						.hashCode());
		result = prime * result + branchId;
		result = prime
				* result
				+ ((descriptionTag300 == null) ? 0 : descriptionTag300
						.hashCode());
		result = prime * result
				+ ((isJournals == null) ? 0 : isJournals.hashCode());
		result = prime * result
				+ ((isbnIssn == null) ? 0 : isbnIssn.hashCode());
		result = prime * result
				+ ((leaderPos6 == null) ? 0 : leaderPos6.hashCode());
		result = prime * result
				+ ((publisher == null) ? 0 : publisher.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
				+ ((transactionDate == null) ? 0 : transactionDate.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CART_ITEMS other = (CART_ITEMS) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (bibItemNumber != other.bibItemNumber)
			return false;
		if (branchDescription == null) {
			if (other.branchDescription != null)
				return false;
		} else if (!branchDescription.equals(other.branchDescription))
			return false;
		if (branchId != other.branchId)
			return false;
		if (descriptionTag300 == null) {
			if (other.descriptionTag300 != null)
				return false;
		} else if (!descriptionTag300.equals(other.descriptionTag300))
			return false;
		if (isJournals == null) {
			if (other.isJournals != null)
				return false;
		} else if (!isJournals.equals(other.isJournals))
			return false;
		if (isbnIssn == null) {
			if (other.isbnIssn != null)
				return false;
		} else if (!isbnIssn.equals(other.isbnIssn))
			return false;
		if (leaderPos6 == null) {
			if (other.leaderPos6 != null)
				return false;
		} else if (!leaderPos6.equals(other.leaderPos6))
			return false;
		if (publisher == null) {
			if (other.publisher != null)
				return false;
		} else if (!publisher.equals(other.publisher))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (transactionDate == null) {
			if (other.transactionDate != null)
				return false;
		} else if (!transactionDate.equals(other.transactionDate))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	
}