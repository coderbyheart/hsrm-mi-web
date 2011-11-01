package uebung.ueb07;

import java.util.*;

/**
 * Liste der BlogBeans als Repräsentation des gesamten Datenbestands.
 * Speichern und wiederherstellung ist *nicht* thread-safe. Falls
 * man das tun wollte, dann sollte man eine Datenbank verwenden.
 * @author pb
 */
public class BlogList {

	protected List<BlogBean> blogs;

	/**
	 * Erstellt eine leere BlogList
	 */
	public BlogList() {
		blogs = new ArrayList<BlogBean>();
	}

	/**
	 * Fügt Dummie-Einträge der BlogList hinzu.
	 */
	public synchronized void addDummies() {
		BlogBean bb = new BlogBean();
		bb.setText("Hallo, das ist der erste Blog");
		bb.addComment("Das ist der erste Kommentar");
		bb.addComment("Das ist der zweite Kommentar");
		blogs.add(bb);
		bb = new BlogBean();
		bb.setText("Das ist der zweite Blog - ohne Kommentare");
		blogs.add(bb);
		bb = new BlogBean();
		bb.setText("Das ist der dritte Blog.<br />\n Mit <strong>fettem</strong> Markup und äöüß Umlauten.");
		bb.addComment("Nur ein Kommentar. Dafür mit äöüß Umlauten");
	}

	/**
	 * Anzahl der Blogs in der BlogList
	 * @return int Anzahl Blogs
	 */
	public synchronized int size() {
		return blogs.size();
	}
	
	/**
	 * Anzahl als getter
	 * @see BlogList#size()
	 * @return int Anzahl Blogs
	 */
	public synchronized int getSize() {
		return size();
	}

	/**
	 * Liste aller Blogs 
	 * @return List<BlogBean> Liste aller Blogs
	 */
	public synchronized List<BlogBean> getBlogs() {
		return blogs;
	}
	
	/**
	 * Blog, das zu einem bestimmten Zeitpunkt erstellt wude 
	 * @param date Date Zeitpunkt/Erstelldatum
	 * @return BlogBean Blog oder null wenn keiner zu dem Zeitpunkt erstellt wurde
	 */
	public synchronized BlogBean getBlogByDate(Date date) {
		for(BlogBean bb : blogs) {
			if (bb.getDate().equals(date))
				return bb;
		}
		return null;
	}

	/**
	 * Fügt einen Blog der Liste hinzu.
	 * @param bb BlogBean Hinzuzufügender Blog
	 */
	public synchronized void addBlog(BlogBean bb) {
		blogs.add(bb);
	}

	/**
	 * Fügt einen neunen Blog mit nichtleerem Titel und nichtleerem Text hinzu.
	 * @param title String Titel
	 * @param text String Text
	 */
	public synchronized void addBlog(String title, String text) {
		title = title.trim();
		text = text.trim();
		if (title.length() == 0 || text.length() == 0)
			return;
		BlogBean bb = new BlogBean();
		bb.setTitle(title);
		bb.setText(text);
		blogs.add(bb);
	}

	/**
	 * Gibt den i.ten Blog in der Liste zurück.
	 * @param i int i.ter
	 * @return BlogBean oder null wenn i.ter nicht existiert
	 */
	public synchronized BlogBean getBlog(int i) {
		if ((i < 0) || (i >= blogs.size())) {
			return null;
		}
		return blogs.get(i);
	}
	
	/**
	 * Lädt alle BlogBeans aus dem Speicherverzeichnis und fügt sie der Liste
	 * hinzu. Falls Laden problematisch wird eine Ausnahme geworfen.
	 */
	public void load() {
		blogs.clear();
		/*
		for (String pathpart: FileHelper.getFilenamesDirectory(BlogBean.getSaveDir(), "")) {
			BlogBean bb = new BlogBean(pathpart);
			if ((bb.getText().length() > 0) || (bb.getTitle().length() > 0)) {
				blogs.add(bb);
			}
		}
		*/
		// TODO Implement
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof BlogList)) {
			return false;
		}
		BlogList b = (BlogList) o;
		return (blogs.equals(b.blogs));
	}
	
}
