package blog;

import java.text.DateFormat;
import java.util.*;
import java.io.*;
import java.net.URL;

/**
 * Repräsentation eine vollständigen Blogs mit Titel, Text, opt. Bild und opt. Kommentaren 
 * @author pb
 */
public class BlogBean {

	private Date date;
	private String title;
	private String text; // HTML-fragment representing blog entry
	private URL image;
	private List<CommentBean> comments;

	/**
	 * Neuer Blog ohne Inhalte mit aktuellem Datum
	 */
	public BlogBean() {
		setDate(new Date());
		text = "";
		title = "";
		comments = new ArrayList<CommentBean>();
	}
	
	/**
	 * Setzt Text des Blogs 
	 * @param text String Text des Blogs
	 */
	public void setText(String text) {
		this.text = text; 
	}

	/**
	 * Gibt Text des Blogs zurück
	 * @return String Text des Blogs
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setzt Titel des Blogs
	 * @param title String Titel des Blogs
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gibt Titel des Blogs zurück
	 * @return String Titel des Blogs
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Gibt Erstellungsdatum des Blogs zurück. Dient als
	 * (wenn auch schwache) Identifikation. 
	 * @return Date Erstellungsdatum
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Erstellungsdatum als millis. 
	 * @see BlogBean#getDate()
	 * @return long millis des Erstellungsdatums
	 */
	public long getDateMillis() {
		return getDate().getTime();
	}

	/**
	 * Gibt Liste aller Kommentare zurück.
	 * @return List<CommentBean> Liste der Kommentare
	 */
	public List<CommentBean> getComments() {
		return comments;
	}

	/**
	 * Gibt Liste aller Kommentare sortiert nach Zeit zurück
	 * @return List<CommentBean> Liste der Kommentare sortiert nach Zeit
	 */
	public List<CommentBean> getCommentsSortByDate() {
		CommentBean[] cbs = new CommentBean[comments.size()];
		cbs = comments.toArray(cbs);
		Comparator<CommentBean> comparator = new Comparator<CommentBean>() {
			public int compare(CommentBean cb1, CommentBean cb2) {
				return cb2.getDate().compareTo(cb1.getDate());
			}
		};
		Arrays.sort(cbs, comparator);
		List<CommentBean> ret = new ArrayList<CommentBean>();
		for (CommentBean cb : cbs) {
			ret.add(cb);
		}
		return ret;
	}

	/**
	 * Gibt i.ten Kommentar zurück
	 * @param i int i.ter
	 * @return CommentBean Kommentar
	 */
	public CommentBean getComment(int i) {
		if ((i < 0) || (i >= comments.size())) {
			return null;
		}
		return comments.get(i);
	}
	
	/**
	 * Fügt Kommentar hinzu
	 * @param s String Kommentar
	 */
	public void addComment(String s) {
		CommentBean cb = new CommentBean(s);
		comments.add(cb);
	}

	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BlogBean)) {
			return false;
		}
		BlogBean b = (BlogBean) o;
		if (!title.equals(b.title))
			return false;
		if (!text.equals(b.text))
			return false;
		if (comments.size() != b.comments.size())
			return false;
		for (CommentBean cb : comments) {
			if (!b.comments.contains(cb))
				return false;
		}
		for (CommentBean cb : b.comments) {
			if (!comments.contains(cb))
				return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int ret = 0;
		if (getDate() != null)
			ret ^= getDate().hashCode();
		if (title != null) 
			ret ^= title.hashCode();
		if (text != null) 
			ret ^= text.hashCode();
		if (comments != null)
			ret ^= comments.size();
		return ret;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(DateFormat.getDateInstance().format(getDate()));
		sb.append("]-");
		sb.append(title);
		sb.append("-");
		sb.append(text);
		sb.append("-{");
		for (CommentBean comment : comments) {
			sb.append(" " + comment.toString());
		}
		sb.append(" }");
		return sb.toString();
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setImage(URL image) {
		this.image = image;
	}

	public URL getImage() {
		return image;
	}
}
