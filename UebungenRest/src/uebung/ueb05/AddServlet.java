package uebung.ueb05;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddServlet
 */
public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String action = "+";
	private String actionName = "Addition";
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int max_num = (Integer) request.getAttribute("max_number");
		
		int a, b;
		a = (int) (Math.random() * max_num);
		b = (int) (Math.random() * max_num);
		Writer w = response.getWriter();
		w.write("<html><head><title>Kopfrechner: " + getActionName() + "</title></head><body>");
		w.write("<form method=\"get\" action=\"\">");
		w.write("<input type=\"hidden\" name=\"a\" value=\"" + a + "\" />");
		w.write("<input type=\"hidden\" name=\"b\" value=\"" + b + "\" />");
		w.write("<p><label for=\"ergebnis\">Was ist " + a + " " + getAction() + " " + b + "?</label><input type=\"text\" name=\"ergebnis\" id=\"ergbenis\" /><button type=\"submit\">weiter</button></p>");
		w.write("</form>");
		w.write("</body></html>");
		w.write(max_num);		
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
}
