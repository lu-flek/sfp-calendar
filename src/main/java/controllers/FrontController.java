package controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet("/app/*")
public class FrontController extends HttpServlet implements Controller {
    public static final Map<String, Controller> URL_MAPPING;
    public static final Map<String, String> PAGE_MAPPING;
    public static final UserController USER_CONTROLLER = new UserController();
    public static final SiteController SITE_CONTROLLER = new SiteController();

    public static final String NOT_FOUND_PAGE = "404";

    static {
        // URL regex -> controller to process
        URL_MAPPING = Map.of(
                URLs.USERS, USER_CONTROLLER,
                URLs.LOGIN, new LoginController(),
                URLs.LOGOUT, new LogoutController(),
                URLs.CALENDAR, new CalendarController(),
                URLs.HOME, new HomeController(),
                URLs.REGISTRATION, new RegistrationController(),
                NOT_FOUND_PAGE, SITE_CONTROLLER
        );
        // shortcuts for pages
        PAGE_MAPPING = Map.of(
                URLs.LOGIN, "login.jsp"
        );
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        String page = findMapping(action, PAGE_MAPPING.keySet(), "");
        if (!page.isEmpty()) {
            showPage(req, resp, PAGE_MAPPING.get(page));
            return;
        }

        String key = findMapping(action, URL_MAPPING.keySet(), NOT_FOUND_PAGE);

        URL_MAPPING.get(key).doGet(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        String key = findMapping(action, URL_MAPPING.keySet(), NOT_FOUND_PAGE);

        URL_MAPPING.get(key).doPost(req, resp);
    }

    private String getAction(HttpServletRequest req) {
        String action = req.getPathInfo();
        if (action == null) action = "";
        return action;
    }

    private String findMapping(String action, Set<String> urlSet, String _default) {
        return urlSet.stream()
                .filter(action::matches)
                .findFirst()
                .orElse(_default);
    }

}

