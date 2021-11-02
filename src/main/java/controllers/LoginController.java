package controllers;

import services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class LoginController extends HttpServlet implements Controller {

    public static final String USER_SESSION_ATTRIBUTE = "userEmail";

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();

        UserService service = new UserService();

        String errorText = require(params, "email", "password");
        if (!errorText.isEmpty()) {
            showLoginError(request, response, errorText);
            return;
        }

        String login = request.getParameter("email");
        String password = request.getParameter("password");

        if (service.checkPassword(login, password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute(USER_SESSION_ATTRIBUTE, login);
            response.sendRedirect("/calendar");
        } else {
            showLoginError(request, response, "Sorry, your email/password pair is incorrect");
        }
    }

    private void showLoginError(HttpServletRequest req, HttpServletResponse resp, String errorText) {
        req.setAttribute("error", errorText);
        showPage(req, resp, "/login");
    }

    private String require(Map<String, String[]> params, String... requiredParams) {
        StringBuilder errorText = new StringBuilder();
        final String separator = "<br/>";

        for (String param : requiredParams) {
            boolean paramExists = params.containsKey(param);
            paramExists &= !params.get(param)[0].isBlank();
            if (!paramExists) {
                errorText.append(separator).append("The field is required: ").append(param);
            }
        }
        if (errorText.length() == 0) return "";

        return errorText.substring(separator.length());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Controller.super.doGet(request, response);
    }

    @Override
    public void showPage(HttpServletRequest req, HttpServletResponse resp, String page) {
        Controller.super.showPage(req, resp, "/login.jsp");
    }
}