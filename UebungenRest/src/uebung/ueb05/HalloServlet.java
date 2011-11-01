package uebung.ueb05;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HalloServlet
 */
public class HalloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Writer w = response.getWriter();
		w.write("<html><body>");
		String name = request.getParameter("name");
		if (name != null && !name.equals("")) {
			w.write("<p>Hallo " + name + "!</p>");
		} else {
			w.write("<p>Hallo Unbekannter!");
		}
		w.write("</body></html>");
	}

}
