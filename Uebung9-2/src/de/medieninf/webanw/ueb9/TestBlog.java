package de.medieninf.webanw.ueb9;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.*;
import org.junit.*;

public class TestBlog {

	private final static int INITIAL_SIZE=3;
	private BlogBean bb;
	
	
	@Before
	public void setUp() throws Exception {
		bb = new BlogBean();
	}

	@Test
	public void testBlogEntries() {
		List<BlogEntry> lbe = bb.getBlogs();
		Assert.assertEquals(INITIAL_SIZE, lbe.size());
		long sum_ids = 0;
		for (BlogEntry entry : lbe) {
			sum_ids += entry.getBlogId();
		}
		Assert.assertEquals(6, sum_ids);
	}
	
	@Test
	public void testBlogEntry() {
		Assert.assertEquals(INITIAL_SIZE, bb.getBlogs().size());
		BlogEntry entry = new BlogEntry();
		entry.setTitle("AutoBlog");
		entry.setContent("Dies ist ein automatisch generierter Blog.\n Und so weiter...");
		entry = bb.update(entry);
		Assert.assertNotSame(0, entry.getBlogId());
		System.out.println("next id greater on each run: " + entry.getBlogId());
		Assert.assertEquals(INITIAL_SIZE+1, bb.getBlogs().size());
		BlogEntry entryCopy = bb.getBlogEntry(entry.getBlogId());
		Assert.assertNotNull(entryCopy);
		bb.remove(entryCopy);
		Assert.assertEquals(INITIAL_SIZE, bb.getBlogs().size());
	}
	
	@Test
	public void testBlogComments() {
		List<BlogComment> comments = new ArrayList<BlogComment>();
		for (BlogEntry entry : bb.getBlogs()) {
			comments.addAll(entry.getComments());
		}
		Assert.assertEquals(4, comments.size());		
		int[] count = {0, 2, 2, 0}; // two comments in 1 and 2, none in 3
		for (BlogComment comment : comments) {
			count[(int) comment.getEntry().getBlogId()] -= 1;
		}
		Assert.assertEquals(0, count[1]+count[2]+count[3]);		
	}

	@Test
	public void testDate() {
		Assert.assertEquals(INITIAL_SIZE, bb.getBlogs().size());
		BlogEntry entry = new BlogEntry();
		entry.setTitle("AutoBlog");
		entry.setContent("Dies ist ein automatisch generierter Blog.\n Und so weiter...");
		Date dateOrig = entry.getDate();
		entry = bb.update(entry);
		BlogEntry entryCopy = bb.getBlogEntry(entry.getBlogId());
		Date dateCopy = entryCopy.getDate();
		bb.remove(entry);
		Assert.assertEquals(INITIAL_SIZE, bb.getBlogs().size());
		Assert.assertEquals(dateCopy, dateOrig);		
	}
	
	@Test
	public void testImage() {
		int blogId = 1;
		BlogImage image = new BlogImage();
		BlogEntry entry = bb.getBlogEntry(blogId);
		Assert.assertNotNull(entry);
		image.setEntry(entry);
		BufferedImage img = renderButton("Image for " + blogId);
		image.setImage(img);
		image = bb.update(image);
		// entry = bb.update(entry); // not necessary
		entry = bb.getBlogEntry(blogId);
		BlogImage imageCopy = entry.getImage();
		Assert.assertNotNull(imageCopy);
		bb.remove(imageCopy);
		Assert.assertEquals(image, imageCopy);
	}
	
	
	private BufferedImage renderButton(String text) {
		int width = 128;
		int height = 32;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(Color.black);
		g2d.drawRoundRect(0, 0, width-2, height-2, 6, 6);
		Font font = new Font(Font.SERIF, Font.PLAIN, 12);
		g2d.setFont(font);
		FontMetrics metrics = g2d.getFontMetrics(font);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		int adv = metrics.stringWidth(text);
		int hgt = metrics.getHeight();
		g2d.drawString(text, (width-adv)/2, (height/2)-2+(hgt/2));
		g2d.dispose();
		return img;
	}

	
}
