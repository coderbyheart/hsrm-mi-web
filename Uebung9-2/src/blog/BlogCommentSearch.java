package blog;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="bcs")
@RequestScoped
public class BlogCommentSearch {
	private String searchTerm;
	
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public String getSearchTerm() {
		return searchTerm;
	}
}
