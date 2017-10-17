/*
 * © LibriCore
 * 
 * Created on Apr 14, 2004
 */
package com.libricore.librisuite.common;

import java.util.Locale;

/**
 * @author wimc
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class L10nCode {
    private String l10nCode = new String(".@_default_");

    public L10nCode(String l10nCode) {
        this.l10nCode = l10nCode;
    }

    public L10nCode(Locale aLocale) {
        this.l10nCode =
            new String(
                aLocale.getLanguage()
                    + "_"
                    + aLocale.getCountry()
                    + "@"
                    + aLocale.getVariant());
    }

    public L10nCode strip() {
        L10nCode newL10nCode = null;

        if (this.l10nCode.equals(".@_default_")) {
            newL10nCode = new L10nCode(".@_other_");
        } else {
            if (this.l10nCode.equals(".@_other_")) {
                return null;
            } else {
                int decimalpointIndex = this.l10nCode.indexOf('.');
                if (decimalpointIndex != -1) {
                    int commercialatIndex =
                        this.l10nCode.indexOf('@', decimalpointIndex);
                    if (commercialatIndex != -1) {
                        newL10nCode =
                            new L10nCode(
                                this.l10nCode.substring(
                                    0,
                                    decimalpointIndex).concat(
                                    this.l10nCode.substring(
                                        commercialatIndex)));
                    } else {
                        newL10nCode =
                            new L10nCode(
                                this.l10nCode.substring(
                                    0,
                                    decimalpointIndex));
                    }
                } else {
                    int commercialatIndex =
                        this.l10nCode.indexOf('@');
                    if (commercialatIndex != -1) {
                        newL10nCode =
                            new L10nCode(
                                this.l10nCode.substring(
                                    0,
                                    commercialatIndex));
                    } else {
                        int underscoreIndex =
                            this.l10nCode.indexOf('_');
                        if (underscoreIndex != -1) {
                            newL10nCode =
                                new L10nCode(
                                    this.l10nCode.substring(
                                        0,
                                        underscoreIndex));
                        } else {
                            newL10nCode = new L10nCode(".@_default_");
                        }
                    }
                }
            }
        }

        return newL10nCode;
    }

    /**
     * @return l10ncode
     */
    public String getL10nCode() {
        return this.l10nCode;
    }

    /**
     * @param l10nCode
     */
    public void setL10nCode(String l10nCode) {
        this.l10nCode = l10nCode;
    }

    public int hashCode() {
        return this.l10nCode.hashCode();
    }

    public boolean equals(Object anObject) {
        if (anObject instanceof L10nCode) {
            L10nCode aL10nCode = (L10nCode) anObject;
            return aL10nCode.l10nCode.equals(this.l10nCode);
        }
        return false;
    }
}
