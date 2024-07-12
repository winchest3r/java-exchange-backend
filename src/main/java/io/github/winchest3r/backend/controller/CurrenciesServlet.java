package io.github.winchest3r.backend.controller;

import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.*;
import java.util.stream.*;

import io.github.winchest3r.backend.service.*;
import io.github.winchest3r.backend.dao.*;
import io.github.winchest3r.backend.model.*;
import io.github.winchest3r.backend.util.*;

@WebServlet(name="CurrenciesServlet", urlPatterns = {"/api/currencies/*"})
public class CurrenciesServlet extends HttpServlet {
    CurrencyService service;

    @Override
    public void init(ServletConfig config) {
        service = new CurrencyService(new CurrencyDaoSimple());
        /*
         * TODO: Remove test data with sql data access object
         */
        service.createCurrency("Russian Ruble", "RUB", "₽");
        service.createCurrency("US Dollar", "USD", "$");
        service.createCurrency("Euro", "EUR", "€");
        service.createCurrency("Yen", "JPY", "¥");
        service.createCurrency("Yuan", "CNY", "¥");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();

        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            if (pathInfo == null || pathInfo.equals("/")) {
                out.print("[");
                out.print(service
                    .getAllCurrencies()
                    .stream()
                    .map(JsonUtils::getCurrency)
                    .collect(Collectors.joining(","))
                );
                out.print("]");
            } else {
                String[] splits = pathInfo.split("/");

                // if path is not like /api/currencies/CODE
                if (splits.length != 2) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }

                Currency cur = service.getCurrencyByCode(splits[1]);

                if (cur == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                
                out.print(JsonUtils.getCurrency(cur));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
