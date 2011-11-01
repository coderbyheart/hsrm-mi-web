package de.medieninf.webanw.ueb9;

import java.util.List;

public interface Blog {

	/**
	 * Get all blog entries.
	 * @return List<Blog> all blog entries
	 */
	public List<BlogEntry> getBlogs();

	/**
	 * Get all blog entries containting text.
	 * @param text to search for
	 * @return List<BlogEntry> all blog entries containting text
	 */
	public List<BlogEntry> searchBlogEntries(String text);
	
	/**
	 * Get the blog entry with corresponding blockId if it exists
	 * @param blogId to search for
	 * @return corresponding block entry or null
	 */
	public BlogEntry getBlogEntry(long blogId);
	
	/**
	 * Update or create BlogEntry.
	 * @param entry the retrieved or new entry
	 * @return updated or new entry
	 */
	public BlogEntry update(BlogEntry entry);
	
	/**
	 * Remove BlogEntry
	 * @param entry the entry to be removed
	 */
	public void remove(BlogEntry entry);
	
	/**
	 * Create new BlogComment with current date
	 * @param entry the comment refers to
	 * @param content of the comment
	 * @return the created comment
	 */
	public BlogComment addComment(BlogEntry entry, String content);
	
	/**
	 * Update BlogComment.
	 * @param comment the retrieved comment
	 * @return updated comment
	 */
	public BlogComment update(BlogComment comment);
	
	/**
	 * Remove BlogComment
	 * @param comment the comment to be removed
	 */
	public void remove(BlogComment comment);
	
	/**
	 * Get all blog comments containting text of one specific blog.
	 * @param entry to which the search comments belong
	 * @param text that is search for
	 * @return List of comments of that blog entry that contain the search text
	 */
	public List<BlogComment> searchComments(BlogEntry entry, String text);
	
	public void addImage(BlogEntry entry, BlogImage image);
	
	/**
	 * Update BlogImage.
	 * @param image the retrieved or new image
	 * @return updated or new image
	 */
	public BlogImage update(BlogImage image);
	
	/**
	 * Remove BlogImage
	 * @param image the image to be removed
	 */
	public void remove(BlogImage image);
	
}
