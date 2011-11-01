package filter;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

/**
 * Servlet Filter implementation class GrayscaleLargeImages
 */
@WebFilter("/GrayscaleLargeImages")
public class GrayscaleLargeImages implements Filter {

	private static class ByteArrayServletStream extends ServletOutputStream {

		ByteArrayOutputStream baos;

		ByteArrayServletStream(ByteArrayOutputStream baos) {
			this.baos = baos;
		}

		public void write(int param) throws IOException {
			baos.write(param);
		}
	}

	private static class ByteArrayPrintWriter {

		private ByteArrayOutputStream baos = new ByteArrayOutputStream();

		private PrintWriter pw = new PrintWriter(baos);

		private ServletOutputStream sos = new ByteArrayServletStream(baos);

		public PrintWriter getWriter() {
			return pw;
		}

		public ServletOutputStream getStream() {
			return sos;
		}

		byte[] toByteArray() {
			return baos.toByteArray();
		}
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		String show = request.getParameter("show");
		if (show != null && show.length() > 0) {

			final ByteArrayPrintWriter pw = new ByteArrayPrintWriter();
			ServletResponseWrapper wrapper = new HttpServletResponseWrapper(
					(HttpServletResponse) response) {
				public PrintWriter getWriter() {
					return pw.getWriter();
				}

				public ServletOutputStream getOutputStream() {
					return pw.getStream();
				}

			};

			chain.doFilter(request, wrapper);
			
			InputStream imgin = new ByteArrayInputStream(pw.baos.toByteArray());
			BufferedImage image = ImageIO.read(imgin);

			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			BufferedImageOp op = new ColorConvertOp(cs, null);
			image = op.filter(image, null);

			ServletOutputStream outs = response.getOutputStream();
			ImageIO.write(image, "PNG", outs);
		}

		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
