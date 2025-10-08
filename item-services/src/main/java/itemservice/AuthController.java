package itemservice;

import java.io.*;
import java.sql.*;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

@WebServlet("/AuthController")
public class AuthController extends HttpServlet {

	@Resource(name = "jdbc/item")
	private DataSource dataSource;
		
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("LOGIN".equalsIgnoreCase(action)) {
            handleLogin(req, resp);
        } else if ("SIGNUP".equalsIgnoreCase(action)) {
            handleSignup(req, resp);
        }
        else if  ("DELETE_ACCOUNT".equalsIgnoreCase(action)) {
        	handleDeleteAccount(req, resp);
        }
        else if ("FORGOT_PASSWORD".equalsIgnoreCase(action)) {
            handleForgotPassword(req, resp);
        }

    }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    	doPost(req,resp);
    }
    // ===== LOGIN =====
    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean remember = "true".equals(req.getParameter("remember"));

        try (Connection conn = dataSource.getConnection()) {
			
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Login success
                HttpSession session = req.getSession(true);
                session.setAttribute("USER", username);
                session.setAttribute("isLogin", true);

                if (remember) {
                    Cookie cookie = new Cookie("username", username);
                    cookie.setMaxAge(60 * 60 * 24 * 5);
                    cookie.setPath(req.getContextPath());
                    resp.addCookie(cookie);
                }

                resp.sendRedirect(req.getContextPath() + "/ItemController?action=LOAD_ITEMS");
            } else {
            	resp.sendRedirect("login.html?error=Invalid+username+or+password");
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // ===== SIGNUP =====
    private void handleSignup(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try (Connection conn = dataSource.getConnection()) {
            // Check if username already exists
            PreparedStatement check = conn.prepareStatement("SELECT id FROM users WHERE username=?");
            check.setString(1, username);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                resp.sendRedirect("login.html?error=Username+already+exists");
                return;
            }

            // Insert new user
            PreparedStatement insert = conn.prepareStatement("INSERT INTO users(username, password) VALUES (?, ?)");
            insert.setString(1, username);
            insert.setString(2, password);
            insert.executeUpdate();

            resp.sendRedirect("login.html");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
    
    private void handleDeleteAccount(HttpServletRequest req, HttpServletResponse resp)
         throws IOException, ServletException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try (Connection conn = dataSource.getConnection()) {
            // Verify first
            PreparedStatement check = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            check.setString(1, username);
            check.setString(2, password);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
                resp.sendRedirect("login.html?error=Invalid+username+or+password");
                return;
            }

            // Delete if found
            PreparedStatement del = conn.prepareStatement("DELETE FROM users WHERE username=?");
            del.setString(1, username);
            del.executeUpdate();

            // Done
            resp.sendRedirect("login.html?error=Account+deleted+successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("login.html?error=Error+deleting+account");
        }
    }
    
    private void handleForgotPassword(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String username = req.getParameter("username");
        String newPassword = req.getParameter("newPassword");

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement check = conn.prepareStatement("SELECT * FROM users WHERE username=?");
            check.setString(1, username);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
            	resp.sendRedirect("reset-password.html?error=Username+not+found");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("UPDATE users SET password=? WHERE username=?");
            ps.setString(1, newPassword);
            ps.setString(2, username);
            ps.executeUpdate();

            resp.sendRedirect("login.html?message=Password+updated+successfully#signin");


        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("reset-password.html?error=Error+resetting+password");
        }
    }

}
