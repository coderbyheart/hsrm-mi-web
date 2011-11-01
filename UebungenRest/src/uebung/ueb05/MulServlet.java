package uebung.ueb05;


/**
 * Servlet implementation class MulServlet
 */
public class MulServlet extends AddServlet {
	private static final long serialVersionUID = 1L;
	public MulServlet() {
		setAction("&middot");
		setActionName("Multiplikation");
	}
}
