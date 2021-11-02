package controllers;

import models.User;
import models.UserRole;
import services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RegistrationController implements Controller {

    public static final String USER_SESSION_ATTRIBUTE = "userEmail";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showPage(req, resp, "registration.jsp");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();

        UserService service = new UserService();

        String errorText = require(params, "name", "email", "password");
        if (!errorText.isEmpty()) {
            showRegistrationError(request, response, errorText);
            return;
        }

        String name = request.getParameter("name");
        String login = request.getParameter("email");
        String password = request.getParameter("password");

        if (!service.findUserByLogin(login).isPresent()) {
            service.createUser(new User(name, login, UserRole.USER, password));
            request.setAttribute("registered", "You have successfully registered");
            showPage(request, response, "login.jsp");
        } else {
            showRegistrationError(request, response, "Sorry, you are already registered");
        }
    }

    private void showRegistrationError(HttpServletRequest req, HttpServletResponse resp, String errorText) {
        req.setAttribute("error", errorText);
        showPage(req, resp, "registration.jsp");
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
}
