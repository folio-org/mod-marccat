/*
 * (c) LibriCore
 *
 *
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

public class RelationReciprocal implements Serializable {
	
	private int relationshipTypeCode;
	private int relationshipReciprocalTypeCode;		            

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getRelationshipReciprocalTypeCode() {
		return relationshipReciprocalTypeCode;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getRelationshipTypeCode() {
		return relationshipTypeCode;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setRelationshipReciprocalTypeCode(int i) {
		relationshipReciprocalTypeCode = i;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setRelationshipTypeCode(int i) {
		relationshipTypeCode = i;
	}

	public boolean equals(Object other) {
		  if ( !(other instanceof RelationReciprocal) ) return false;
		  RelationReciprocal castOther = (RelationReciprocal) other;
		  return ((castOther.getRelationshipTypeCode() == getRelationshipTypeCode())
		     		&& (castOther.getRelationshipReciprocalTypeCode() == getRelationshipReciprocalTypeCode()));
	  }

	  public int hashCode() {
		  return super.hashCode() + relationshipTypeCode + relationshipReciprocalTypeCode;
	  }
}
