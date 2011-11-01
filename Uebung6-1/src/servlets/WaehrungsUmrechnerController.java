package servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.WaehrungsUmrechner;

@SuppressWarnings("serial")
public class WaehrungsUmrechnerController extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		WaehrungsUmrechner rechner = getRechner(request);
		String mode = request.getParameter("modify");
		
		RequestDispatcher rd;
		if (mode != null) {
			 rd = request.getRequestDispatcher("/waehrungsrechner-modify.jsp");	
		} else {
			String sourceValue = request.getParameter("sourceValue");
			String targetValue = request.getParameter("targetValue");
			if (sourceValue != null && !sourceValue.equals("")) {
				rechner.setSourceValue(sourceValue);
			} else if (targetValue != null && !targetValue.equals("")) {
				rechner.setTargetValue(targetValue);
			}
			rd = request.getRequestDispatcher("/waehrungsrechner.jsp");
		}
		rd.forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {

		WaehrungsUmrechner rechner = getRechner(request);
		
		String sourceCurrency = getParamUTF8(request, "sourceCurrency");
		String targetCurrency = getParamUTF8(request, "targetCurrency");
		String rate = request.getParameter("rate");
		
		rechner.setSourceCurrency(sourceCurrency);
		rechner.setTargetCurrency(targetCurrency);
		rechner.setRate(new BigDecimal(rate.replace(",", ".")));

		RequestDispatcher rd;
		rd = request.getRequestDispatcher("/waehrungsrechner.jsp");
		rd.forward(request, response);
	}
	
	protected WaehrungsUmrechner getRechner(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		WaehrungsUmrechner rechner = null;
		if (session.getAttribute("rechner") == null) {
			rechner = new WaehrungsUmrechner();
		} else {
			rechner = (WaehrungsUmrechner)session.getAttribute("rechner");
		}
		session.setAttribute("rechner", rechner);
		return rechner;
	}
	
	String getParamUTF8(HttpServletRequest request, String name) {
		String param = request.getParameter(name);
		if (param == null) return null;
		try {
			return new String(request.getParameter(name).getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return param;
		}
	}
}
