package librisuite.business.common.group;


public abstract class MarcTagGroup implements TagGroup {
	private boolean canSort = false;
	private boolean singleSort = false;
	
	public MarcTagGroup() {
		super();
	}

	public MarcTagGroup(boolean canSort, boolean singleSort) {
		super();
		this.canSort = canSort;
		this.singleSort = singleSort;
	}

	public boolean isCanSort() {
		return canSort;
	}

	public boolean isSingleSort() {
		return singleSort;
	}

	public void setCanSort(boolean canSort) {
		this.canSort = canSort;
	}

	public void setSingleSort(boolean singleSort) {
		this.singleSort = singleSort;
	}

}
