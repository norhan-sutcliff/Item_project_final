package itemservice;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String ctx = req.getContextPath();        // e.g. /item-services
        String servletPath = req.getServletPath(); // e.g. /login.html or /ItemController
        String path = (servletPath != null) ? servletPath.toLowerCase() : "";

        HttpSession session = req.getSession(false);
        Boolean isLogin = (session != null) ? (Boolean) session.getAttribute("isLogin") : null;

        boolean isLoginPage   = path.endsWith("login.html");
        boolean isAuth        = path.contains("authcontroller");
        boolean isLogout      = path.contains("logoutcontroller");
        boolean isResetPass      = path.contains("reset-password.html");
        boolean isStaticAsset = path.contains("css") || path.contains("js") || path.contains("images");

        System.out.println("[AuthFilter] path=" + servletPath + ", isLogin=" + isLogin);

        // ✅ CASE 1: already logged in and tries to visit login page → redirect to items
        if (Boolean.TRUE.equals(isLogin) && isLoginPage) {
            System.out.println("[AuthFilter] Logged-in user tried to access login.html → redirecting to LOAD_ITEMS");
            res.sendRedirect(ctx + "/ItemController?action=LOAD_ITEMS");
            return;
        }

        // ✅ CASE 2: allow login page, static files, and AuthController itself
        if (isLoginPage || isAuth || isLogout || isStaticAsset || isResetPass) {
            chain.doFilter(request, response);
            return;
        }

        // ✅ CASE 3: not logged in — try cookie auto-login first
        if (!Boolean.TRUE.equals(isLogin)) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("username".equals(c.getName())) {
                        HttpSession newSession = req.getSession(true);
                        newSession.setAttribute("USER", c.getValue());
                        newSession.setAttribute("isLogin", true);
                        chain.doFilter(request, response);
                        return;
                    }
                }
            }

            // ❌ no session, no cookie → go to login page
            res.sendRedirect(ctx + "/login.html?error=Please+login+first");
            return;
        }

        // ✅ CASE 4: logged in and accessing other pages
        chain.doFilter(request, response);
    }
}
