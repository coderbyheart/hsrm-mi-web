package blog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import de.medieninf.webanw.ueb9.BlogBean;
import de.medieninf.webanw.ueb9.BlogComment;
import de.medieninf.webanw.ueb9.BlogEntry;

@ManagedBean(name = "currentBlog")
@RequestScoped
public class CurrentBlog {
	public BlogEntry getBlogEntry() {
		FacesContext fc = FacesContext.getCurrentInstance();
		BlogBean bb = fc.getApplication().evaluateExpressionGet(fc, "#{bb}",
				BlogBean.class);
		CurrentBlogId currentBlogId = fc.getApplication()
				.evaluateExpressionGet(fc, "#{currentBlogId}",
						CurrentBlogId.class);

		return bb.getBlogEntry(currentBlogId.getId());
	}

	public DataModel<CommentsPerDay> getCommentsPerDay() {
		HashMap<String, CommentsPerDay> map = new HashMap<String, CommentsPerDay>();
		for (BlogComment c : getBlogEntry().getComments()) {
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

	public void addComment() {
		FacesContext fc = FacesContext.getCurrentInstance();
		NewBlogComment newComment = fc.getApplication().evaluateExpressionGet(
				fc, "#{newblogcomment}", NewBlogComment.class);
		BlogBean bb = fc.getApplication().evaluateExpressionGet(fc, "#{bb}",
				BlogBean.class);
		bb.addComment(getBlogEntry(), newComment.getComment());
		newComment.setComment(null);
	}
}
