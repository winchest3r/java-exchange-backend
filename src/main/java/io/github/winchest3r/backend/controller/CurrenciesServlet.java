package io.github.winchest3r.backend.controller;

import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.*;

@WebServlet(name="CurrenciesServlet", urlPatterns = {"/api/currencies/*"})
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();

        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            if (pathInfo == null || pathInfo.equals("/")) {

                /*
                 * TODO: Get all data from model
                 * convert result to json
                 */

                out.print("{\"dummy\": 42}");
            } else {
                String[] splits = pathInfo.split("/");

                // if path is not like /api/currencies/CODE
                if (splits.length != 2) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }

                /* 
                 * TODO: Find currency by CODE
                 * if there is no CODE like this - 404 (SC_NOT_FOUND)
                 * convert result to json
                 */
                
                out.print(String.format("{\"dummy\":%s}", splits[1]));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (/*Servlet*/Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
