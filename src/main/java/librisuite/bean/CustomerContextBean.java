package librisuite.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class CustomerContextBean extends LibrisuiteBean {

	private static final Log logger = LogFactory.getLog(CustomerContextBean.class);

	private boolean enabled = false;
	/**
	 * Public Testing Constructor
	 */
	public CustomerContextBean() {}

	public abstract String getDisplayName();
	
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
