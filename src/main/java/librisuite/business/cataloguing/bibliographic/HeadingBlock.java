package librisuite.business.cataloguing.bibliographic;

import java.util.List;

public class HeadingBlock 
{
	private String permalink;
	private String viafID;
	private List<String> headingsText;
	private List<String> titleText;
	private List<String> tag4XX;
	private List<String> tag5XX;
		
			
	public void setViafID(String viafID) {
		this.viafID = viafID;
	}
	
	public String getViafID() {
		return viafID;
	}

	public void setHeadingsText(List<String> headingsText) {
		this.headingsText = headingsText;
	}

	public List<String> getHeadingsText() {
		return headingsText;
	}

	public void setTitleText(List<String> titleText) {
		this.titleText = titleText;
	}

	public List<String> getTitleText() {
		return titleText;
	}

	public List<String> getTag4XX()
	{
		return tag4XX;
	}

	public void setTag4XX(List<String> tag4XX)
	{
		this.tag4XX = tag4XX;
	}

	public List<String> getTag5XX()
	{
		return tag5XX;
	}

	public void setTag5XX(List<String> tag5XX)
	{
		this.tag5XX = tag5XX;
	}
	
	public int getTag4XXSize()
	{
		if (tag4XX == null)
			return 0;
		
		return tag4XX.size();
	}
	
	public int getTag5XXSize()
	{
		if (tag5XX == null)
			return 0;
		
		return tag5XX.size();
	}
	
	public int getTitleSize()
	{
		if (titleText == null)
			return 0;
		
		return titleText.size();
	}
	
	public int getHeadingsTextSize()
	{
		if (headingsText == null)
			return 0;
		
		return headingsText.size();
	}

	public String getPermalink()
	{
		return permalink;
	}

	public void setPermalink(String permalink)
	{
		this.permalink = permalink;
	}
}
