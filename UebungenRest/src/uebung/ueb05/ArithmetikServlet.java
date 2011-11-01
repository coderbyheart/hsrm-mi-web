package uebung.ueb05;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ArithmetikServlet
 */
public class ArithmetikServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String reset = request.getParameter("reset");
		if (session.isNew() || (reset != null && reset.equals("1"))) {
			session.setAttribute("num_add", 0);
			session.setAttribute("num_mul", 0);
			session.setAttribute("ergebnisse", new HashMap<Integer, Boolean>());
		}
		int num_add = (Integer) session.getAttribute("num_add");
		int num_mul = (Integer) session.getAttribute("num_mul");
		int max_num_add = Integer.parseInt(getServletConfig().getInitParameter("anz_add"));
		int max_num_mul = Integer.parseInt(getServletConfig().getInitParameter("anz_mul"));
		@SuppressWarnings("unchecked")
		HashMap<Integer, Boolean> ergebnisse = (HashMap<Integer, Boolean>)session.getAttribute("ergebnisse");
		
		// Ergebnis speichern
		String as = request.getParameter("a");
		String bs = request.getParameter("b");
		String es = request.getParameter("ergebnis");
		int a = as == null || as.equals("") ? 0 : Integer.parseInt(as);
		int b = bs == null || bs.equals("") ? 0 : Integer.parseInt(bs);
		int ergebnis = es == null || es.equals("") ? 0 : Integer.parseInt(es);
		if (num_mul == 0 && num_add > 0 && num_add <= max_num_add) {
			ergebnisse.put(num_add, a + b == ergebnis);
		} else if (num_mul > 0 && num_mul <= max_num_mul) {
			ergebnisse.put(num_add + num_mul, a * b == ergebnis);
		}
		
		request.setAttribute("max_number", Integer.parseInt(getServletConfig().getInitParameter("max_number")));
		RequestDispatcher rd;
		if (num_add < max_num_add) {
			session.setAttribute("num_add", num_add + 1);
			rd = getServletContext().getNamedDispatcher("AddServlet");
			rd.forward(request, response);
		} else if (num_mul < max_num_mul) {
			session.setAttribute("num_mul", num_mul + 1);
			rd = getServletContext().getNamedDispatcher("MulServlet");
			rd.forward(request, response);
		} else {
			Writer w = response.getWriter();
			w.write("<html><head><title>Kopfrechner</title></head><body>");
			w.write("<p>Fertig. <a href=\"?reset=1\">Nochmal!</a></p>");
			for(int frage: ergebnisse.keySet()) {
				w.write("<p>Frage " + frage + ": " + (ergebnisse.get(frage) ? "korrekt" : "falsch") + "</p>");	
			}
			w.write("</body></html>");
		}
	}
}
