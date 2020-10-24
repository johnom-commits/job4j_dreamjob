package ru.job4j.dream.servlet;

import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CitiesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        String cityId = req.getParameter("name");
        List<String> cities = PsqlStore.instOf().getCities();
        var str = new StringBuilder();
        String nameCity = "";
        if (!cityId.equals("0")) {
            nameCity = PsqlStore.instOf().getNameCity(Integer.parseInt(cityId));
            str.append("<option>Choose the city</option>");
        } else {
            str.append("<option selected>Choose the city</option>");
        }
        for (String st : cities) {
            if (st.equals(nameCity)) {
                str.append("<option selected>");
            } else {
                str.append("<option>");
            }
            str.append(st).append("</option>");
        }
        writer.println(str.toString());
        writer.flush();
    }
}
