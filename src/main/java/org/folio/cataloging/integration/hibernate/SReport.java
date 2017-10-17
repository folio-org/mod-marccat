package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SReport implements Serializable {

    /** identifier field */
    private String appNme;

    /** identifier field */
    private String compNme;

    /** identifier field */
    private String reportCall;

    /** identifier field */
    private String paramCall;

    /** identifier field */
    private String menuEntry;

    /** identifier field */
    private String preview;

    /** identifier field */
    private String printdlg;

    /** identifier field */
    private String sendByMail;

    /** identifier field */
    private String emailAddr;

    public SReport(String appNme) {
		super();
		this.appNme = appNme;
	}

	/** full constructor */
    public SReport(String appNme, String compNme, String reportCall, String paramCall, String menuEntry, String preview, String printdlg, String sendByMail, String emailAddr) {
        this.appNme = appNme;
        this.compNme = compNme;
        this.reportCall = reportCall;
        this.paramCall = paramCall;
        this.menuEntry = menuEntry;
        this.preview = preview;
        this.printdlg = printdlg;
        this.sendByMail = sendByMail;
        this.emailAddr = emailAddr;
    }

    /** default constructor */
    public SReport() {
    }

    public String getAppNme() {
        return this.appNme;
    }

    public void setAppNme(String appNme) {
        this.appNme = appNme;
    }

    public String getCompNme() {
        return this.compNme;
    }

    public void setCompNme(String compNme) {
        this.compNme = compNme;
    }

    public String getReportCall() {
        return this.reportCall;
    }

    public void setReportCall(String reportCall) {
        this.reportCall = reportCall;
    }

    public String getParamCall() {
        return this.paramCall;
    }

    public void setParamCall(String paramCall) {
        this.paramCall = paramCall;
    }

    public String getMenuEntry() {
        return this.menuEntry;
    }

    public void setMenuEntry(String menuEntry) {
        this.menuEntry = menuEntry;
    }

    public String getPreview() {
        return this.preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPrintdlg() {
        return this.printdlg;
    }

    public void setPrintdlg(String printdlg) {
        this.printdlg = printdlg;
    }

    public String getSendByMail() {
        return this.sendByMail;
    }

    public void setSendByMail(String sendByMail) {
        this.sendByMail = sendByMail;
    }

    public String getEmailAddr() {
        return this.emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("appNme", getAppNme())
            .append("compNme", getCompNme())
            .append("reportCall", getReportCall())
            .append("paramCall", getParamCall())
            .append("menuEntry", getMenuEntry())
            .append("preview", getPreview())
            .append("printdlg", getPrintdlg())
            .append("sendByMail", getSendByMail())
            .append("emailAddr", getEmailAddr())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof SReport) ) return false;
        SReport castOther = (SReport) other;
        return new EqualsBuilder()
            .append(this.getAppNme(), castOther.getAppNme())
            .append(this.getCompNme(), castOther.getCompNme())
            .append(this.getReportCall(), castOther.getReportCall())
            .append(this.getParamCall(), castOther.getParamCall())
            .append(this.getMenuEntry(), castOther.getMenuEntry())
            .append(this.getPreview(), castOther.getPreview())
            .append(this.getPrintdlg(), castOther.getPrintdlg())
            .append(this.getSendByMail(), castOther.getSendByMail())
            .append(this.getEmailAddr(), castOther.getEmailAddr())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getAppNme())
            .append(getCompNme())
            .append(getReportCall())
            .append(getParamCall())
            .append(getMenuEntry())
            .append(getPreview())
            .append(getPrintdlg())
            .append(getSendByMail())
            .append(getEmailAddr())
            .toHashCode();
    }

}
