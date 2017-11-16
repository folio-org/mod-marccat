package librisuite.bean.searching.frbr;

import java.util.List;

public class Work {
	private Integer amicusNumber;
	private List<Expression> expressionList;
	private String title;
	private boolean authority;
	

	public Work(Integer amicusNumber)
	{
		this.amicusNumber = amicusNumber;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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

	public List<Expression> getExpressionList() {
		return expressionList;
	}

	public void setExpressionList(List<Expression> expressionList) {
		this.expressionList = expressionList;
	}
}
