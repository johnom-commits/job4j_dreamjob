package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CandidateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        String file = req.getParameter("file");
        var candidate = new Candidate(Integer.parseInt(id), req.getParameter("name"));
        if (!file.equals("null")) {
            candidate.setPhotoId(Integer.parseInt(file));
        }
        String city = req.getParameter("city");
        if (!city.equals("Choose the city")) {
           candidate.setCity(PsqlStore.instOf().getIdCity(city));
        }
        PsqlStore.instOf().save(candidate);
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
