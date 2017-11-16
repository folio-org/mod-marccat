package librisuite.hibernate;

import java.io.Serializable;

public class RdaMarcTagDisplay extends LabelTagDisplay implements Serializable  
{
	private static final long serialVersionUID = 4091846105600659469L;
	
	private String marcTagNumberText;
	

	public String getMarcTagNumberText() {
		return marcTagNumberText;
	}

	public void setMarcTagNumberText(String marcTagNumberText) {
		this.marcTagNumberText = marcTagNumberText;
	}

	
}