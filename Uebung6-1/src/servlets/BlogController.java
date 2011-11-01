package servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.EncryptPassword;
import util.SessionIdentifierGenerator;
import blog.BlogBean;
import blog.BlogList;
import blog.User;

@SuppressWarnings("serial")
public class BlogController extends HttpServlet {
	private User user;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// User-Objekt initialisieren
		user = getUser(request);
		
		// Setup
		if (request.getParameter("setup") != null) {
			doSetup();
		}
		
		RequestDispatcher rd = null;
		if (!user.isLoggedIn()) {
			if (request.getParameter("login") != null) {
				doLogin(request, response);
			}
			if (user.isLoggedIn()) {
				initBlogList(request);
				rd = request.getRequestDispatcher("/blog/listblogs.jsp");
			} else {
				rd = request.getRequestDispatcher("/blog/login.jsp");
			}
		} else {
			// Logout
			if (request.getParameter("logout") != null) {
				user = getUser(request, true);
				rd = request.getRequestDispatcher("/blog/login.jsp");
			}
			// Eintrag hinzufügen
			else if (request.getParameter("add") != null) {
				rd = request.getRequestDispatcher("/blog/addblog.jsp");
			}
			else if (request.getParameter("addblog") != null) {
				doAddBlog(request, response);
				rd = request.getRequestDispatcher("/blog/listblogs.jsp");
			}
			// Blogeinträge anzeigen
			else {		
				initBlogList(request);
				rd = request.getRequestDispatcher("/blog/listblogs.jsp");
			}
		}
		rd.forward(request, response);
	}
	
	/**
	 * Alle POST-Request auf GET umleiten, da wir die Request context-sensitiv behandeln
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request, response);
	}

	private void initBlogList(HttpServletRequest request) {
		BlogList bloglist = new BlogList();
		HttpSession session = request.getSession();
		session.setAttribute("bloglist", bloglist);
		try {
			Connection conn = (Connection) getServletContext()
					.getAttribute("connection");
			PreparedStatement blogFind = conn
					.prepareStatement("SELECT * FROM blog ORDER BY date DESC");
			ResultSet rs = blogFind.executeQuery();
			while (rs.next()) {
				BlogBean entry = new BlogBean();
				entry.setText(rs.getString("text"));
				entry.setTitle(rs.getString("title"));
				entry.setDate(rs.getTimestamp("date"));
				try {
					entry.setImage(new URL(rs.getString("image")));
				} catch (MalformedURLException e) {
				}
				bloglist.addBlog(entry);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doAddBlog(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Connection conn = (Connection) getServletContext().getAttribute(
					"connection");
			PreparedStatement blogCreate = conn
					.prepareStatement("INSERT INTO blog (user, title, image, text, date) VALUES(?, ?, ?, ?, ?)");
			blogCreate.setString(1, user.getEmail());
			blogCreate.setString(2, request.getParameter("title"));
			blogCreate.setString(3, request.getParameter("image"));
			blogCreate.setString(4, request.getParameter("text"));
			blogCreate.setTimestamp(5, new Timestamp(new Date().getTime()));
			blogCreate.execute();
			initBlogList(request);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class PasswordHashWithSeed {
		public String hash;
		public String seed;
	}

	private void doLogin(HttpServletRequest request,
			HttpServletResponse response) {

		user = getUser(request, true);

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		Connection conn = (Connection) getServletContext().getAttribute(
				"connection");
		// Check if user exists
		PreparedStatement userFind;
		try {
			userFind = conn
					.prepareStatement("SELECT email, passwordHash, passwordSeed FROM user WHERE email = ?");
			userFind.setString(1, email);
			ResultSet rs = userFind.executeQuery();
			if (rs.next()) {
				try {
					if (rs.getString("passwordHash")
							.equals(hashPassword(password,
									rs.getString("passwordSeed")).hash)) {
						user.setLoggedIn(true);
						user.setEmail(email);
					} else {
						user.setLoginState(User.PASSWORD_MISMATCH);
					}
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				user.setLoginState(User.EMAIL_UNKNOWN);
			}
			rs.close();
			userFind.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private User getUser(HttpServletRequest request, boolean createNew) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null || createNew) {
			user = new User();
		}
		session.setAttribute("user", user);
		return user;
	}

	private User getUser(HttpServletRequest request) {
		return getUser(request, false);
	}

	private void doSetup() {
		try {
			System.out.println("Setup!");
			Connection conn = (Connection) getServletContext().getAttribute(
					"connection");
			// User table
			conn.prepareStatement("DROP TABLE IF EXISTS user").execute();
			conn.prepareStatement(
					"CREATE TABLE user (email TEXT UNIQUE, passwordHash TEXT, passwordSeed TEXT)")
					.execute();
			// Create some Users
			PreparedStatement userCreate = conn
					.prepareStatement("INSERT INTO user (email, passwordHash, passwordSeed) VALUES(?, ?, ?)");
			PasswordHashWithSeed pw = null;
			try {
				pw = hashPassword("password");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String[] users = { "user@domain.tld", "user2@domain.tld",
					"user3@domain.tld" };
			for (String email : users) {
				userCreate.setString(1, email);
				userCreate.setString(2, pw.hash);
				userCreate.setString(3, pw.seed);
				userCreate.execute();
			}

			// Create the blog table
			conn.prepareStatement("DROP TABLE IF EXISTS blog").execute();
			conn.prepareStatement(
					"CREATE TABLE blog (user TEXT, title TEXT, image TEXT, text TEXT, date DATETIME)")
					.execute();

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private PasswordHashWithSeed hashPassword(String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		SessionIdentifierGenerator sidgen = new SessionIdentifierGenerator();
		String seed = sidgen.nextSessionId();
		return hashPassword(password, seed);
	}

	private PasswordHashWithSeed hashPassword(String password, String seed)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String hash = password + seed;
		for (int i = 0; i < 1000; i++) {
			hash = EncryptPassword.SHA512(hash);
		}
		PasswordHashWithSeed r = new PasswordHashWithSeed();
		r.hash = hash;
		r.seed = seed;
		return r;
	}

}