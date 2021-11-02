package controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {
    default void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    default void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    default void showPage(HttpServletRequest req, HttpServletResponse resp, String page) {
        try {
            req.getRequestDispatcher("/pages/" + page).forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
