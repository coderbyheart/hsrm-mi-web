package blog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletRequest;

import de.medieninf.webanw.ueb9.BlogBean;
import de.medieninf.webanw.ueb9.BlogComment;

@ManagedBean(name = "currentBlogComments")
@RequestScoped
public class CurrentBlogComments {

	public DataModel<CommentsPerDay> getCommentsPerDay() {
		HashMap<String, CommentsPerDay> map = new HashMap<String, CommentsPerDay>();
		for (BlogComment c : getCurrentBlog().getBlogEntry().getComments()) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String day = df.format(c.getDate());
			if (!map.containsKey(day)) {
				map.put(day, new CommentsPerDay(c.getDate()));
			} else {
				CommentsPerDay cpd = map.get(day);
				cpd.inc();
			}
		}
		ListDataModel<CommentsPerDay> result = new ListDataModel<CommentsPerDay>();
		ArrayList<CommentsPerDay> list = new ArrayList<CommentsPerDay>();
		for (CommentsPerDay cpd : map.values()) {
			list.add(cpd);
		}
		result.setWrappedData(list);
		return result;
	}

	public List<BlogComment> getComments() {
		HttpServletRequest req = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String commentsOnDay = req.getParameter("commentsOnDay");

		if (commentsOnDay != null) {
			List<BlogComment> comments = new ArrayList<BlogComment>();
			for (BlogComment c : getCurrentBlog().getBlogEntry().getComments()) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String day = df.format(c.getDate());
				if (!day.equals(commentsOnDay))
					continue;
				comments.add(c);
			}
			return comments;
		}

		FacesContext fc = FacesContext.getCurrentInstance();
		BlogCommentSearch bcs = fc.getApplication().evaluateExpressionGet(fc,
				"#{bcs}", BlogCommentSearch.class);
		if (bcs.getSearchTerm() != null && bcs.getSearchTerm().length() > 2) {
			BlogBean bb = fc.getApplication().evaluateExpressionGet(fc,
					"#{bb}", BlogBean.class);
			return bb.searchComments(getCurrentBlog().getBlogEntry(),
					bcs.getSearchTerm());
		}

		return getCurrentBlog().getBlogEntry().getComments();

	}

	public CurrentBlog getCurrentBlog() {
		FacesContext fc = FacesContext.getCurrentInstance();
		CurrentBlog cb = fc.getApplication().evaluateExpressionGet(fc,
				"#{currentBlog}", CurrentBlog.class);
		return cb;
	}

	public boolean isCommentsFiltered() {
		HttpServletRequest req = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		return req.getParameter("commentsOnDay") != null;
	}

}
