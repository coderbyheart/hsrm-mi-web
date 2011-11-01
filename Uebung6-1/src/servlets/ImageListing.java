package servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImageListing
 */
@WebServlet("/ImageListing")
public class ImageListing extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		File picsDir = new File(getServletContext()
				.getRealPath("WEB-INF/pics/"));

		String show = request.getParameter("show");
		if (show != null && show.length() > 0) {
			// WICHTIG! Hier den getOutputStream() nehmen
			ServletOutputStream outs = response.getOutputStream();
			FileInputStream imgFile = new FileInputStream(picsDir + "/" + show);
			int ch;
			while ((ch = imgFile.read()) != -1)
				outs.write((char) ch);
		} else {
			ArrayList<String> images = new ArrayList<String>();
			for (String pic : picsDir.list()) {
				images.add(pic);
			}
			getServletContext().setAttribute("listing", images);

			RequestDispatcher rd = request
					.getRequestDispatcher("8-3-listing.jsp");
			rd.forward(request, response);

		}
	}
}
