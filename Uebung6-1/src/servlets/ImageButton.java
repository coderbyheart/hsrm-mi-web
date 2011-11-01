package servlets;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImageListing
 */
@WebServlet("/ImageButton")
public class ImageButton extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String imageText = request.getParameter("text");
		if (imageText == null)
			imageText = "??";

		// Caching
		String tempDir = request.getServletContext().getInitParameter(
				"cacheDir");

		StringBuffer hexString = new StringBuffer();
		try {
			byte[] bytesOfMessage = imageText.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			for (int i = 0; i < thedigest.length; i++) {
				hexString.append(Integer.toHexString(0xFF & thedigest[i]));
			}
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			for (int i = 0; i < imageText.length(); i++) {
				hexString
						.append(Integer.toHexString(0xFF & imageText.charAt(i)));
			}
		}

		File imgFile = new File(tempDir + "/" + hexString);
		String format = "png";
		BufferedImage i;
		if (imgFile.exists()) {
			i = ImageIO.read(imgFile);
		} else {
			// Bild erzeugen
			int width, height;
			width = 200;
			height = 50;
			i = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = i.createGraphics();
			g.drawString(imageText, 10, 10);
			ImageIO.write(i, format, imgFile);
		}

		// Zum Browser senden
		ServletOutputStream outs = response.getOutputStream();
		ImageIO.write(i, format, outs);
	}
}
