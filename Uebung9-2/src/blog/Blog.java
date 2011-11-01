package blog;

import java.util.List;

import javax.faces.context.FacesContext;

import de.medieninf.webanw.ueb9.BlogBean;
import de.medieninf.webanw.ueb9.BlogComment;
import de.medieninf.webanw.ueb9.BlogEntry;

public class Blog extends BlogBean {
	private String searchTerm;

	@Override
	public List<BlogEntry> getBlogs() {
		if (searchTerm != null && searchTerm.length() > 2) {
			return super.searchBlogEntries(searchTerm);
		}
		return super.getBlogs();
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void addEntry() {
		FacesContext fc = FacesContext.getCurrentInstance();
		NewBlogEntry newBlog = fc.getApplication().evaluateExpressionGet(fc,
				"#{newblogentry}", NewBlogEntry.class);
		BlogBean bb = fc.getApplication().evaluateExpressionGet(fc, "#{bb}",
				BlogBean.class);

		BlogEntry entry = new BlogEntry();
		entry.setTitle(newBlog.getTitle());
		entry.setContent(newBlog.getContent());
		entry.setDate(new java.util.Date());
		bb.update(entry);
	}
}
