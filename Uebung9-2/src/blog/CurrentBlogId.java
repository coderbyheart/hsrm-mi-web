package blog;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "currentBlogId")
@SessionScoped
public class CurrentBlogId {
	private long id;

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		HttpServletRequest req = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String id = req.getParameter("id");
		if (id != null) {
			this.id = Long.parseLong(id);
		}
		return this.id;
	}
}
