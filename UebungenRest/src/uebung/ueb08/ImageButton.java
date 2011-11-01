package uebung.ueb08;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

@SuppressWarnings("serial")
public class ImageButton extends BodyTagSupport {
	private String url;
	private String type;

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type == null ? "link" : type;
	}

	public boolean isLink() {
		return getType().equals("link");
	}

	public int doStartTag() {
		return EVAL_BODY_AGAIN;
	}

	public int doAfterBody() {
		
		if (bodyContent != null) {
			try {
				JspWriter out = getPreviousOut();
				if (isLink()) {
					// Bild erzeugen
					int width, height;
					width = 200;
					height = 50;
					String imageText = bodyContent.getString();
					
					byte[] thedigest = imageText.getBytes("UTF-8");
					try {
						byte[] bytesOfMessage = imageText.getBytes("UTF-8");
						MessageDigest md = MessageDigest.getInstance("MD5");
						thedigest = md.digest(bytesOfMessage);
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					String format = "png";
					String imgFile = thedigest.toString() + "." + format;
					
					BufferedImage i = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
					Graphics2D g = i.createGraphics();
					g.drawString(imageText, 10, 10);

					File outFile = new File(this.pageContext.getServletContext().getRealPath("WEB-INF/buttons/") + "/" + imgFile);
					try {
						ImageIO.write(i, format, outFile);
					} catch (IOException e) {
						e.printStackTrace();
					}

					// Ausgeben
					out.println("<a href=\"" + getUrl() + "\">"
							+ "<img src=\"WEB-INF/buttons/\"" + imgFile + " alt=\"" + imageText + "\" width=\"" + width + "\" height=\"" + height + "\" />" + "</a>");
				} else {
					out.println("<button>" + bodyContent.getString()
							+ "</button>");
				}
				bodyContent.clearBody();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return SKIP_BODY;
	}
}
