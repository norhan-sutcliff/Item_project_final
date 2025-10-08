package itemservice;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

@WebServlet("/DeleteAccountController")
public class DeleteAccountController extends HttpServlet {

    @Resource(name = "jdbc/item")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("USER") == null) {
            resp.sendRedirect("login.html?error=Please+login+first");
            return;
        }

        String username = (String) session.getAttribute("USER");

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE username = ?");
            ps.setString(1, username);
            int rows = ps.executeUpdate();

            session.invalidate();

            Cookie cookie = new Cookie("username", "");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);

            if (rows > 0) {
                resp.sendRedirect("login.html?message=Account+deleted+successfully");
            } else {
                resp.sendRedirect("login.html?error=Account+not+found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("login.html?error=Error+deleting+account");
        }
    }
}
