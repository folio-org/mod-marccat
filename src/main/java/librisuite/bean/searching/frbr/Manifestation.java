package librisuite.bean.searching.frbr;

public class Manifestation {
	private Integer amicusNumber;
	private String title;
	private boolean authority;

	public boolean isAuthority() {
		return authority;
	}

	public void setAuthority(boolean authority) {
		this.authority = authority;
	}

	public Integer getAmicusNumber() {
		return amicusNumber;
	}

	public void setAmicusNumber(Integer amicusNumber) {
		this.amicusNumber = amicusNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
