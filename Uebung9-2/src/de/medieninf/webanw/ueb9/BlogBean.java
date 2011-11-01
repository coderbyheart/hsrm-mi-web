package de.medieninf.webanw.ueb9;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

@ApplicationScoped
@ManagedBean(name="bb")
public class BlogBean implements Blog {

	private EntityManagerFactory emf;
    private ThreadLocal<EntityManager> em;
    private ThreadLocal<EntityTransaction> tx;

    public BlogBean() {
        emf = Persistence.createEntityManagerFactory("blog");
        em = new ThreadLocal<EntityManager>();
        tx = new ThreadLocal<EntityTransaction>();
    }
    
    private void cEm() {
    	if (em.get() != null) 
    		em.get().close();
    	em.set(emf.createEntityManager());
    	tx.set(em.get().getTransaction());
    }
    
    private void dEm() {
    	tx.remove();
    	em.get().close();
    	em.remove();
    }

	@Override
	public BlogComment addComment(BlogEntry entry, String content) {
		BlogComment comment = new BlogComment();
		comment.setContent(content);
		comment.setEntry(entry);
		comment.setDate(new java.util.Date());
		cEm();
		tx.get().begin();
		em.get().persist(comment);
	    tx.get().commit();
	    return comment;
	}

	@Override
	public void addImage(BlogEntry entry, BlogImage image) {
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BlogEntry> getBlogs() {
		cEm();
        String queryString = "SELECT b FROM blog b";
        Query query = em.get().createQuery(queryString);
        List<BlogEntry> blogs = query.getResultList();
        dEm();
        return blogs;
	}
	
	@Override
	public BlogEntry getBlogEntry(long blogId) {
		cEm();
        String queryString = "SELECT b FROM blog b WHERE b.blogId=:blogId";
        Query query = em.get().createQuery(queryString);
		query.setParameter("blogId", blogId);
		try {
			BlogEntry entry = (BlogEntry) query.getSingleResult();
	        dEm();
			return entry;
		} catch (NoResultException e) {
	        dEm();
			return null;
		}
	}
	
	@Override
	public void remove(BlogEntry entry) {
		cEm();
		tx.get().begin();
		entry = em.get().merge(entry);
		em.get().remove(entry);
		tx.get().commit();
        dEm();
	}

	@Override
	public void remove(BlogComment comment) {
		cEm();
		tx.get().begin();
		comment = em.get().merge(comment);
		em.get().remove(comment);
		tx.get().commit();
        dEm();
	}

	@Override
	public void remove(BlogImage image) {
		cEm();
		tx.get().begin();
		image = em.get().merge(image);
		em.get().remove(image);
		tx.get().commit();
        dEm();
	}

	private Query genQuerySearchBlogEntries (String text) {
		StringBuffer queryString = new StringBuffer("SELECT b FROM blog b");
		if (text != null && !text.equals("")) {
			String joinString = " WHERE ";
			queryString.append(joinString + "b.title LIKE :text");
			joinString = " OR ";
			queryString.append(joinString + "b.content LIKE :text");
		}
		Query query = em.get().createQuery(queryString.toString());
		if (text != null && !text.equals("")) {
			query.setParameter("text", "%" + text + "%");
		}
		return query;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<BlogEntry> searchBlogEntries(String text) {
		cEm();
        Query query = genQuerySearchBlogEntries(text);
        List<BlogEntry> entries = query.getResultList();
        dEm();
        return entries;
    }

	private Query genQuerySearchComments (BlogEntry entry, String text) {
		StringBuffer queryString = new StringBuffer("SELECT c FROM comment c WHERE c.entry=:entry ");
		if (text != null && !text.equals("")) {
			String joinString = " AND ";
			queryString.append(joinString + "c.content LIKE :text");
		}
		Query query = em.get().createQuery(queryString.toString());
		query.setParameter("entry", entry);
		if (text != null && !text.equals("")) {
			query.setParameter("text", "%" + text + "%");
		}
		return query;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<BlogComment> searchComments(BlogEntry entry, String text) {
		cEm();
        Query query = genQuerySearchComments(entry, text);
        List<BlogComment> entries = query.getResultList();
        dEm();
        return entries;
	}

	@Override
	public BlogEntry update(BlogEntry entry) {
		cEm();
		tx.get().begin();
		entry = em.get().merge(entry);
	    tx.get().commit();
        dEm();
	    return entry;
	}

	@Override
	public BlogComment update(BlogComment comment) {
		cEm();
		tx.get().begin();
		comment = em.get().merge(comment);
	    tx.get().commit();
        dEm();
	    return comment;
	}

	@Override
	public BlogImage update(BlogImage image) {
		cEm();
		tx.get().begin();
		image = em.get().merge(image);
	    tx.get().commit();
        dEm();
	    return image;
	}

}
