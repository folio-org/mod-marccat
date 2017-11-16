package librisuite.hibernate;

import java.io.Serializable;

public class Diacritici implements Serializable 
{   
	private static final long serialVersionUID = 8229065630727630431L;
	
	private int idCarattere;
	private String setCarattere;
	private String font;
	private String carattere;
	private String nomeCarattere;
	private String codiceUnicode;
	private String codiceUtf8;
    
    /** default constructor */
    public Diacritici() {
    }

	public int getIdCarattere() {
		return idCarattere;
	}

	public void setIdCarattere(int idCarattere) {
		this.idCarattere = idCarattere;
	}

	public String getSetCarattere() {
		return setCarattere;
	}

	public void setSetCarattere(String setCarattere) {
		this.setCarattere = setCarattere;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getCarattere() {
		return carattere;
	}

	public void setCarattere(String carattere) {
		this.carattere = carattere;
	}

	public String getNomeCarattere() {
		return nomeCarattere;
	}

	public void setNomeCarattere(String nomeCarattere) {
		this.nomeCarattere = nomeCarattere;
	}

	public String getCodiceUnicode() {
		return codiceUnicode;
	}

	public void setCodiceUnicode(String codiceUnicode) {
		this.codiceUnicode = codiceUnicode;
	}

	public String getCodiceUtf8() {
		return codiceUtf8;
	}

	public void setCodiceUtf8(String codiceUtf8) {
		this.codiceUtf8 = codiceUtf8;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
