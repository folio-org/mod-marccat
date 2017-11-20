package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class CasRecordCachePK implements Serializable {

    /** identifier field */
    private Long bibItmNbr;

    /** identifier field */
    private BigDecimal usrVwCde;

    /** full constructor */
    public CasRecordCachePK(Long bibItmNbr, BigDecimal usrVwCde) {
        this.bibItmNbr = bibItmNbr;
        this.usrVwCde = usrVwCde;
    }

    /** default constructor */
    public CasRecordCachePK() {
    }

    public Long getBibItmNbr() {
        return this.bibItmNbr;
    }

    public void setBibItmNbr(Long bibItmNbr) {
        this.bibItmNbr = bibItmNbr;
    }

    public BigDecimal getUsrVwCde() {
        return this.usrVwCde;
    }

    public void setUsrVwCde(BigDecimal usrVwCde) {
        this.usrVwCde = usrVwCde;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("bibItmNbr", getBibItmNbr())
            .append("usrVwCde", getUsrVwCde())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof CasRecordCachePK) ) return false;
        CasRecordCachePK castOther = (CasRecordCachePK) other;
        return new EqualsBuilder()
            .append(this.getBibItmNbr(), castOther.getBibItmNbr())
            .append(this.getUsrVwCde(), castOther.getUsrVwCde())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getBibItmNbr())
            .append(getUsrVwCde())
            .toHashCode();
    }

}
